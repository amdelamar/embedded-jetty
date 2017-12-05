package hello;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Http2Server.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        // start async thread
        final AsyncContext context = request.startAsync();
        context.start(new Runnable() {
            @Override
            public void run() {
                // start thread
                response.setContentType("text/html");
                response.setStatus(HttpServletResponse.SC_OK);

                String hello = "Hello from Jetty!";
                String content = request.getProtocol() + " | " + request.getScheme()
                        .toUpperCase() + " | ASYNC";

                logger.info(hello + " " + content);

                try {
                    response.getWriter()
                            .println("<div style=\"padding:50px;text-align:center;\"><h1>" + hello + "</h1><p><code>" + content
                                    + "</code></p></div>");
                } catch (IOException e) {
                    logger.error("ERROR:", e);
                }

                // work is finished
                context.complete();
            }
        });

    }

}
