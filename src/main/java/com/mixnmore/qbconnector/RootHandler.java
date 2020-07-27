/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mixnmore.qbconnector;

import com.mixnmore.qbconnector.service.QBWebConnectorSvcSoapController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Pranshu
 */
public class RootHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String initialResponse = "QBConnector is on.";
        exchange.sendResponseHeaders(200, initialResponse.length());
        try (OutputStream opStream = exchange.getResponseBody()) {
            opStream.write(initialResponse.getBytes());
        }
        new QBWebConnectorSvcSoapController();
    }
    
}
