import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "Booklist", urlPatterns = "/Booklist")



public class Booklist extends HttpServlet {
    @Override
    public void init() throws ServletException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Book> list = new ArrayList<>();
        // JDBC driver name and database URL
        String flag = request.getParameter("flag");
        String bookisbn="";
        if(flag.equals("TRUE"))
        {

        }
        else {
            bookisbn = request.getParameter("isbn");
        }
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
               sql = "SELECT name , author , price , isbn, stock, img FROM booklist ";
               rs = stmt.executeQuery(sql);
           }
           else {
               sql = "SELECT name , author , price , isbn, stock, img FROM booklist WHERE isbn = ?";
               preparedStatement = conn.prepareStatement(sql);
               preparedStatement.setString(1,bookisbn);
               rs = preparedStatement.executeQuery();
           }
           while (rs.next()) {
               //Retrieve by column name
               String name = rs.getString("name");
               String author = rs.getString("author");
               Double price = rs.getDouble("price");
               String isbn = rs.getString("isbn");
               int stock = rs.getInt("stock");
               String img = rs.getString("img");
               list.add(new Book(name, author, price, isbn, stock, img));
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
