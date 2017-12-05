package hello;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) throws Exception {
        // Create a basic jetty server object that will listen on port 8080.
        // Note that if you set this to port 0 then a randomly available port
        // will be assigned that you can either look in the logs for the port,
        // or programmatically obtain it for use in test cases.
        Server server = new Server(8080);

        // Servlet Context path
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");

        // Add our Async Servlet
        ServletHolder asyncHolder = contextHandler.addServlet(AsyncServlet.class, "/async");
        asyncHolder.setAsyncSupported(true);

        // Add our Hello Servlet
        contextHandler.addServlet(HelloServlet.class, "/");

        // Set Context Handler
        server.setHandler(contextHandler);

        // Start things up!
        logger.debug("Jetty Server Started");
        server.start();

        // The use of server.join() the will make the current thread join and
        // wait until the server is done executing.
        // See
        // http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()
        server.join();
    }

}
