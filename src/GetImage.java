

import com.google.gson.Gson;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetImage", urlPatterns = {"/getImage"})
public class GetImage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String flag = request.getParameter("flag");
        if(flag.equals("FALSE")) {
            String isbn = request.getParameter("isbn");
            response.setContentType("image/png");

            ServletContext sc = getServletContext();
            InputStream is = sc.getResourceAsStream("/WEB-INF/img/" + isbn + ".png");

            BufferedImage bi = ImageIO.read(is);
            OutputStream os = response.getOutputStream();
            ImageIO.write(bi, "png", os);
        }
        else
        {
            List<String> list = new ArrayList<>();
            try
            {

                final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
                final String DB_URL="jdbc:mysql://localhost/BOOK?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";

                //  Database credentials
                final String USER = "root";
                final String PASS = "1224";
                Connection conn=null;
                Statement stmt=null;
                Class.forName(JDBC_DRIVER);
                ResultSet rs=null;

                // Open a connection
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement preparedStatement = null;

                // Execute SQL query
                stmt = conn.createStatement();
                String sql="";
                if(flag.equals("TRUE")) {
                    sql = "SELECT isbn FROM booklist ";
                    rs = stmt.executeQuery(sql);
                }
                while (rs.next()) {
                    //Retrieve by column name

                    String tempisbn = rs.getString("isbn");

                    list.add("http://localhost:8080/Javaweb_war_exploded/getImage?flag=FALSE&isbn="+tempisbn);
                }

                // Clean-up environment
                rs.close();
                stmt.close();
                conn.close();
            }catch(SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch(Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            }





            String json = new Gson().toJson(list);



            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        }
    }
}