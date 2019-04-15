import com.google.gson.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "Servlet", urlPatterns = "/Servlet")



public class Servlet extends HttpServlet {
    @Override
    public void init() throws ServletException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Book> list = new ArrayList<>();

        list.add(new Book("Harry Potter", "J. K. Rowling",3000, "978-3-16-148410-0", 5, "./img/hp.jpg"));
        list.add(new Book("King of the Ring", "John Ronald Reuel Tolkien",5000, "‎178-3-16-148410-0", 9, "./img/ring.jpg"));
        list.add(new Book("The Three-Body Problem", "	Liu Cixin",4000, "‎278-3-16-148410-0", 7, "./img/tb.jpg"));
        Book book1=new Book("Harry Potter", "J. K. Rowling",3000, "978-3-16-148410-0", 5, "./img/hp.jpg");
        String json = new Gson().toJson(book1);



        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}
