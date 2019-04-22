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


@WebServlet(name = "Booklist", urlPatterns = "/Booklist")



public class Booklist extends HttpServlet {
    @Override
    public void init() throws ServletException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Book> list = new ArrayList<>();

        list.add(new Book("Harry Potter", "J. K. Rowling",3000, "978-3-16-148410-0", 5, "./img/hp.jpg"));
        list.add(new Book("King of the Ring", "John Ronald Reuel Tolkien",5000, "‎178-3-16-148410-0", 9, "./img/ring.jpg"));
        list.add(new Book("The Three-Body Problem", "Liu Cixin",4000, "‎278-3-16-148410-0", 7, "./img/tb.jpg"));

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
