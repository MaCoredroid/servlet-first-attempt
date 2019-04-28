import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "Checkuser", urlPatterns = {"/Checkuser"})
public class Checkuser extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("user");
        response.setContentType("text/plain;charset=UTF-8");
        int numberRow = 0;
        try {
            final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
            final String DB_URL = "jdbc:mysql://localhost/BOOK?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";

            //  Database credentials
            final String USER = "root";
            final String PASS = "1224";
            Connection conn = null;
            Statement stmt = null;
            Class.forName(JDBC_DRIVER);
            ResultSet rs = null;

            // Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatement = null;

            // Execute SQL query
            stmt = conn.createStatement();
            String sql = "";
            sql = "SELECT COUNT(*) FROM users WHERE username = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,user);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                  numberRow = rs.getInt("COUNT(*)");
            }
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
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        PrintWriter out = response.getWriter();
        if(numberRow!=0)
        {
            out.print("FALSE");
            out.flush();

        }
        else
        {
            out.print("TRUE");
            out.flush();
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer jb = new StringBuffer();
        String line = null;
        String username="";
        String password="";
        String email="";
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
        } catch (Exception e) {
        }

        try {
            String jsonString = jb.toString();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

            username = jsonObject.get("username").toString().replaceAll("^\"|\"$", "");;
            password = jsonObject.get("password").toString().replaceAll("^\"|\"$", "");
            email = jsonObject.get("useremail").toString().replaceAll("^\"|\"$", "");

        } catch (Exception e) {
        }
        try{
            final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
            final String DB_URL = "jdbc:mysql://localhost/BOOK?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";

            //  Database credentials
            final String USER = "root";
            final String PASS = "1224";
            Connection conn = null;
            Statement stmt = null;
            Class.forName(JDBC_DRIVER);
            ResultSet rs = null;

            // Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatement = null;

            // Execute SQL query
            stmt = conn.createStatement();
            String sql = "";
            sql = "INSERT INTO users"
                    + "(username, password, email) VALUES"
                    + "(?,?,?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,email);
            preparedStatement.executeUpdate();

            stmt.close();
            conn.close();
        }catch(SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        PrintWriter out = response.getWriter();
        out.print("TRUE");
        out.flush();



    }
}
