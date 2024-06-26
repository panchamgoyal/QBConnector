package com.mixnmore.qbconnector.service;

import com.mixnmore.qbconnector.types.ArrayOfString;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import org.apache.commons.lang3.RandomStringUtils;

@WebService(endpointInterface = "com.mixnmore.qbconnector.service.QBWebConnectorSvcSoap")
public class QBWebConnectorSvcSoapController implements QBWebConnectorSvcSoap {

	private static final Logger LOG = Logger.getLogger(QBWebConnectorSvcSoapController.class.getName());
	private final Map<String, String> userTickets = new HashMap<>();
	private final Map<String, Integer> ticketStages = new HashMap<>();

	@Override
	public ArrayOfString authenticate(String strUserName, String strPassword) {
		ArrayOfString authResponse = new ArrayOfString();
		List<String> responseParams = new LinkedList<>();
		authResponse.string = responseParams;

		boolean authPassed = DataConnector.checkCredentials(strUserName, strPassword);
		if (!authPassed) {
			responseParams.add("");
			responseParams.add("nvu");
			return authResponse;
		}

		String ticketID = RandomStringUtils.randomAlphabetic(10);
		userTickets.put(ticketID, strUserName);
		ticketStages.put(ticketID, 1);
		responseParams.add(ticketID);

		// check if operations are to be performed
		int operationCount = 1;
		if (operationCount == 0) {
			responseParams.add("none");
			return authResponse;
		}

		responseParams.add(""); // provide a company filename
		return authResponse;
	}

	@Override
	public String sendRequestXML(String ticket, String strHCPResponse, String strCompanyFileName, String qbXMLCountry, int qbXMLMajorVers, int qbXMLMinorVers) {

		XMLBuilderService xmlBuilder = new XMLBuilderService();
		String requestXml = "";
		switch (ticketStages.get(ticket)) {
			case 1:
				requestXml = xmlBuilder.buildTermsAddXml();
				break;
			case 2:
				requestXml = xmlBuilder.buildTaxesAddXml();
				break;
			case 3:
				requestXml = xmlBuilder.buildCustomerAddXml();
				break;
			case 4:
				requestXml = xmlBuilder.buildItemsAddXml();
				break;
			case 5:
				requestXml = xmlBuilder.buildInvoicesAddXml();
				break;
		}
		if (!requestXml.isEmpty()) {
			return requestXml;
		}
		return "";
	}

	@Override
	public int receiveResponseXML(String ticket, String response, String hresult, String message) {
		int stage = ticketStages.get(ticket);
		ticketStages.put(ticket, stage + 1);
		if (hresult.isEmpty()) {
			// do error handling here
		}
		return (stage / 5 * 100);
	}

	@Override
	public String connectionError(String ticket, String hresult, String message) {
		LOG.log(Level.WARNING, "Connection error for user: {0}", userTickets.get(ticket));
		return userTickets.get(ticket);
	}

	@Override
	public String getLastError(String ticket) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String closeConnection(String ticket) {
		String removedValue = userTickets.remove(ticket);
		return removedValue;
	}

}
