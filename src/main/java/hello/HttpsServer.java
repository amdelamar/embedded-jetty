package hello;

import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.SecuredRedirectHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpsServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpsServer.class);

    public static void main(String[] args) throws Exception {

        // HTTP Configuration
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSecureScheme(HttpScheme.HTTPS.asString());
        httpConfig.setSecurePort(8443);
        //httpConfig.setSendServerVersion(false);
        //httpConfig.setSendDateHeader(false);

        // HTTPS Configuration
        HttpConfiguration httpsConfig = new HttpConfiguration();
        httpsConfig.addCustomizer(new SecureRequestCustomizer());
        // config.addCustomizer(new ForwardedRequestCustomizer());

        // Protocols
        HttpConnectionFactory http = new HttpConnectionFactory(httpConfig);
        HttpConnectionFactory https = new HttpConnectionFactory(httpsConfig);

        // Setup Threadpool
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(500);
        threadPool.setMinThreads(8);
        threadPool.setIdleTimeout(60000);

        // New Server
        Server server = new Server(threadPool);

        // SSL Configuration
        // https://www.eclipse.org/jetty/documentation/current/configuring-ssl.html
        String workingDir = System.getProperty("user.dir");
        SslContextFactory ssl = new SslContextFactory();
        ssl.setKeyStorePath(workingDir + "/deploy/keystore.jks");
        ssl.setKeyStorePassword("changeit");
        ssl.setKeyManagerPassword("changeit");
        ssl.setTrustStorePath(workingDir + "/deploy/keystore.jks");
        ssl.setTrustStorePassword("changeit");
        ssl.setUseCipherSuitesOrder(true);
        ssl.setIncludeProtocols("TLSv1.2");
        ssl.setExcludeProtocols("TLSv1", "TLSv1.1"); // force tls1.2
        ssl.setExcludeCipherSuites("(MD5|DES|SHA|SHA1)$"); // dont allow weak ciphers

        // Add Connectors
        ServerConnector connector = new ServerConnector(server, http);
        connector.setName("unsecured");
        connector.setPort(8080);
        server.addConnector(connector);

        ServerConnector connector2 = new ServerConnector(server, ssl, https);
        connector2.setName("secured");
        connector2.setPort(8443);
        server.addConnector(connector2);

        // Add our Servlet
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(HelloServlet.class, "/*");

        // Handler Collection
        // SecuredRedirectHandler redirects HTTP to HTTPS
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] { new SecuredRedirectHandler(), handler });
        server.setHandler(handlers);

        // Start Server
        server.start();
        server.join();
        logger.debug("Jetty Server Started");
    }

}
