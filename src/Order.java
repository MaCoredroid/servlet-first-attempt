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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@WebServlet(name = "Order", urlPatterns = "/Order")

public class Order extends HttpServlet {
    @Override
    public void init() throws ServletException {

    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<info> isbnlist = new ArrayList<>();
        List<Book> list = new ArrayList<>();
        // JDBC driver name and database URL

        String username = request.getParameter("username");
        String flag= request.getParameter("flag");
        if(flag.equals("FALSE"))
        {
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

                sql = "SELECT isbn,number FROM orders WHERE username = ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, username);
                rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    //Retrieve by column name

                    String isbn = rs.getString("isbn");
                    int number = rs.getInt("number");
                    isbnlist.add(new info(isbn, number));
                }
                // Clean-up environment
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            }

        }
        else
        {
            long startdate = Long.parseLong(request.getParameter("start"));
            long enddate= Long.parseLong(request.getParameter("end"));
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

                sql = "SELECT isbn,number,time FROM orders WHERE username = ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, username);
                rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    //Retrieve by column name
                    String tempdate=rs.getString("time");
                    Long temp=Long.parseLong(tempdate);
                    System.out.print("temp"+temp);
                    System.out.print("startdate"+startdate);
                    System.out.print("enddate"+enddate);
                    if((temp>startdate) && (temp <enddate))
                    {
                        String isbn = rs.getString("isbn");
                        int number = rs.getInt("number");
                        isbnlist.add(new info(isbn, number));
                    }
                }
                // Clean-up environment
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            }

        }
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

            for (info temp : isbnlist) {
                sql = "SELECT name , author , price , isbn, stock, img FROM booklist WHERE isbn = ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, temp.isbn);
                rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    //Retrieve by column name

                    String name = rs.getString("name");
                    String author = rs.getString("author");
                    Double price = rs.getDouble("price");
                    String isbn = rs.getString("isbn");
                    String img = rs.getString("img");
                    int stock = temp.number;
                    list.add(new Book(name, author, price, isbn, stock, img));
                }
            }
            // Clean-up environment

            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        String json = new Gson().toJson(list);


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer jb = new StringBuffer();
        String line = null;
        String username="";
        String isbn="";
        int duplicate=0;
        int originnumber=0;
        int number=0;
        int stock =0;
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
            isbn = jsonObject.get("isbn").toString().replaceAll("^\"|\"$", "");
            number = Integer.parseInt( jsonObject.get("number").toString().replaceAll("^\"|\"$", ""));

        } catch (Exception e) {
        }
        try {

            final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
            Class.forName(JDBC_DRIVER);
            final String DB_URL = "jdbc:mysql://localhost/BOOK?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";

            //  Database credentials
            final String USER = "root";
            final String PASS = "1224";

            Connection conn = null;
            Connection conn1 = null;
            Connection conn2 = null;
            Connection conn3 = null;



            ResultSet rs = null;
            ResultSet rs1 = null;


            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn1 = DriverManager.getConnection(DB_URL, USER, PASS);
            conn2 = DriverManager.getConnection(DB_URL, USER, PASS);
            conn3 = DriverManager.getConnection(DB_URL, USER, PASS);

            PreparedStatement preparedStatement = null;
            PreparedStatement preparedStatement1 = null;
            PreparedStatement preparedStatement2 = null;
            PreparedStatement preparedStatement3 = null;


            // Execute SQL query
            String sqlquery = "";
            sqlquery = "SELECT stock FROM booklist WHERE isbn = ?";
            preparedStatement = conn.prepareStatement(sqlquery);
            preparedStatement.setString(1, isbn);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                stock = rs.getInt("stock");
            }
            conn.close();
            preparedStatement.close();
            rs.close();


            if (stock < number)
            {


                conn1.close();
                conn2.close();


                preparedStatement1.close();
                preparedStatement2.close();

                response.setCharacterEncoding("UTF-8");
                response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
                response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                response.setHeader("Access-Control-Max-Age", "86400");
                response.setHeader("Access-Control-Allow-Headers", "Content-Type");
                PrintWriter out = response.getWriter();
                out.print("FALSE");
                out.flush();
                return;
            }
            else
            {
                String sqlx="";
                sqlx="SELECT count(*),number from orders where username=? AND isbn=?";
                preparedStatement3 = conn3.prepareStatement(sqlx);
                preparedStatement3.setString(1, username);
                preparedStatement3.setString(2, isbn);
                rs1 = preparedStatement3.executeQuery();
                while (rs1.next()) {
                    duplicate = rs1.getInt("count(*)");
                    originnumber=rs1.getInt("number");
                }

                rs1.close();
                conn3.close();
                preparedStatement3.close();

                if(duplicate!=0)
                {
                    int temp=number+originnumber;
                    String sql = "";
                    sql = "UPDATE orders SET number=? where username=? AND isbn=?";
                    preparedStatement1 = conn1.prepareStatement(sql);
                    preparedStatement1.setInt(1, temp);
                    preparedStatement1.setString(2, username);
                    preparedStatement1.setString(3, isbn);
                    preparedStatement1.executeUpdate();
                }

                else
                {

                    Date date = Calendar.getInstance().getTime();
                    String strDate=Long.toString(date.getTime());
                    String sql = "";
                    sql = "INSERT INTO orders"
                            + "(username, isbn, number,time) VALUES"
                            + "(?,?,?,?)";
                    preparedStatement1 = conn1.prepareStatement(sql);
                    preparedStatement1.setString(1, username);
                    preparedStatement1.setString(2, isbn);
                    preparedStatement1.setInt(3, number);
                    preparedStatement1.setString(4, strDate);
                    preparedStatement1.executeUpdate();
                }


                int leftstock = stock-number;
                if(leftstock>0) {
                    String sqlma = "";
                    sqlma = "UPDATE booklist SET stock = ? WHERE isbn = ?";
                    preparedStatement2 = conn2.prepareStatement(sqlma);
                    preparedStatement2.setInt(1, leftstock);
                    preparedStatement2.setString(2, isbn);
                    preparedStatement2.executeUpdate();
                }
                else
                {
                    String sqlma = "";
                    sqlma = "DELETE FROM booklist WHERE isbn = ?";
                    preparedStatement2 = conn2.prepareStatement(sqlma);
                    preparedStatement2.setString(1, isbn);
                    preparedStatement2.executeUpdate();
                }

                conn1.close();
                conn2.close();

                preparedStatement1.close();
                preparedStatement2.close();

                response.setCharacterEncoding("UTF-8");
                response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
                response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                response.setHeader("Access-Control-Max-Age", "86400");
                response.setHeader("Access-Control-Allow-Headers", "Content-Type");
                PrintWriter out = response.getWriter();
                out.print("TRUE");
                out.flush();


            }
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }





    }
}

