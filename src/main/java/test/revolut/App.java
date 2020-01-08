package test.revolut;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.revolut.util.BankConstants;

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
    static int port = BankConstants.DEFAULT_PORT;

    public static void main( String[] args ) throws IOException {


        if(args!=null && args.length==1){
            try {
                port =  Integer.parseInt(args[0]);
            } catch (Exception e) {
               logger.error("Invalid command line arguments.");
            }
        }
        if(port >65535 || port < 100){
            logger.error("Invalid command line arguments.Enter between 100 and 65535, defaulting to 8085");
            port = BankConstants.DEFAULT_PORT;
        }
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
        return UriBuilder.fromUri("http://" + getHostName() + "/").port(port).build();
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
