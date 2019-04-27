

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

@WebServlet(name = "GetImage", urlPatterns = {"/getImage"})
public class GetImage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("image/png");

        ServletContext sc = getServletContext();
        InputStream is = sc.getResourceAsStream("/WEB-INF/img/hp.png");

        BufferedImage bi = ImageIO.read(is);
        OutputStream os = response.getOutputStream();
        ImageIO.write(bi, "png", os);
    }
}