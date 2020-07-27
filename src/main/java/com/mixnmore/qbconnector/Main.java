package com.mixnmore.qbconnector;

import com.mixnmore.qbconnector.service.DataConnector;
import com.mixnmore.qbconnector.service.QBWebConnectorSvcSoapController;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Endpoint;

/**
 * @author Pranshu Agarwal
 */
public class Main {
    
    private static final Logger LOG = Logger.getLogger(DataConnector.class.getName());
    
    private static HttpServer server;
    
    public static void main(final String[] args) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            final Endpoint endpoint = Endpoint.create(new QBWebConnectorSvcSoapController());
            server = HttpServer.create(new InetSocketAddress(8062), 0);
            server.createContext("/", new RootHandler());
            final HttpContext context = server.createContext("/SoapReq");
            server.setExecutor(null); // creates a default executor
            server.start();
            endpoint.publish(context);
            System.out.println("Server is now on.");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error while starting server", e);
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Unable to find JDBC driver", ex);
        }
    }
    
}
