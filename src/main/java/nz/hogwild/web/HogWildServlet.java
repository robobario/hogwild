package nz.hogwild.web;

import com.google.common.io.ByteStreams;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class HogWildServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream inputStream = getStream();
        ByteStreams.copy(inputStream, resp.getOutputStream());
    }

    InputStream getStream() {
        return getClass().getClassLoader().getResourceAsStream("hogwild.html");
    }
}
