package test.revolut;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Application Class.
 *
 */
public class App {
  static  Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws IOException {

        logger.info("Starting Bank's Embedded  HTTPServer...\n");
        HttpServer basicHttpServer = createHttpServer();
        basicHttpServer.start();
        logger.info(String.format("\nJersey Application Server started with WADL available at " + "%sapplication.wadl\n", getBaseURI()));
        logger.info("Started Bank's Embedded Jersey HTTPServer Successfully !!!");


    }
    private static HttpServer createHttpServer() throws IOException {
        ResourceConfig applicationResourceConfig = new PackagesResourceConfig("test.revolut");
        final Map<String, Object> config = new HashMap<>();
        config.put("com.sun.jersey.api.json.POJOMappingFeature", true);
        applicationResourceConfig.setPropertiesAndFeatures(config);
        return HttpServerFactory.create(getBaseURI(), applicationResourceConfig);
    }
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://" + getHostName() + "/").port(8085).build();
    }

    private static String getHostName() {
        String hostName = "localhost";
        try {
            hostName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
           logger.error(e.getMessage(),e);

        }
        return hostName;
    }
}
