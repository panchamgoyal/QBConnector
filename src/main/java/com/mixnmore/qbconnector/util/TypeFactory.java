package com.mixnmore.qbconnector.util;

import com.mixnmore.qbconnector.types.ArrayOfString;
import com.mixnmore.qbconnector.types.Authenticate;
import com.mixnmore.qbconnector.types.AuthenticateResponse;
import com.mixnmore.qbconnector.types.CloseConnection;
import com.mixnmore.qbconnector.types.CloseConnectionResponse;
import com.mixnmore.qbconnector.types.ConnectionError;
import com.mixnmore.qbconnector.types.ConnectionErrorResponse;
import com.mixnmore.qbconnector.types.GetLastError;
import com.mixnmore.qbconnector.types.GetLastErrorResponse;
import com.mixnmore.qbconnector.types.ReceiveResponseXML;
import com.mixnmore.qbconnector.types.ReceiveResponseXMLResponse;
import com.mixnmore.qbconnector.types.SendRequestXML;
import com.mixnmore.qbconnector.types.SendRequestXMLResponse;
import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class TypeFactory {
    /**
     * Create a new ObjectFactory that can be used to create new instances of schema
     * derived classes for package: com.mixnmore.qbconnector.types
     */
    public TypeFactory() {
    }

    /**
     * Create an instance of {@link SendRequestXMLResponse }
     */
    public SendRequestXMLResponse createSendRequestXMLResponse() {
        return new SendRequestXMLResponse();
    }

    /**
     * Create an instance of {@link ArrayOfString }
     */
    public ArrayOfString createArrayOfString() {
        return new ArrayOfString();
    }

    /**
     * Create an instance of {@link ConnectionErrorResponse }
     */
    public ConnectionErrorResponse createConnectionErrorResponse() {
        return new ConnectionErrorResponse();
    }

    /**
     * Create an instance of {@link ReceiveResponseXML }
     */
    public ReceiveResponseXML createReceiveResponseXML() {
        return new ReceiveResponseXML();
    }

    /**
     * Create an instance of {@link CloseConnectionResponse }
     */
    public CloseConnectionResponse createCloseConnectionResponse() {
        return new CloseConnectionResponse();
    }

    /**
     * Create an instance of {@link AuthenticateResponse }
     */
    public AuthenticateResponse createAuthenticateResponse() {
        return new AuthenticateResponse();
    }

    /**
     * Create an instance of {@link GetLastError }
     */
    public GetLastError createGetLastError() {
        return new GetLastError();
    }

    /**
     * Create an instance of {@link Authenticate }
     */
    public Authenticate createAuthenticate() {
        return new Authenticate();
    }

    /**
     * Create an instance of {@link ReceiveResponseXMLResponse }
     */
    public ReceiveResponseXMLResponse createReceiveResponseXMLResponse() {
        return new ReceiveResponseXMLResponse();
    }

    /**
     * Create an instance of {@link ConnectionError }
     */
    public ConnectionError createConnectionError() {
        return new ConnectionError();
    }

    /**
     * Create an instance of {@link CloseConnection }
     */
    public CloseConnection createCloseConnection() {
        return new CloseConnection();
    }

    /**
     * Create an instance of {@link SendRequestXML }
     */
    public SendRequestXML createSendRequestXML() {
        return new SendRequestXML();
    }

    /**
     * Create an instance of {@link GetLastErrorResponse }
     */
    public GetLastErrorResponse createGetLastErrorResponse() {
        return new GetLastErrorResponse();
    }
}