package com.mixnmore.qbconnector.service;

import com.mixnmore.qbconnector.types.Customer;
import com.mixnmore.qbconnector.types.SalesTax;
import com.mixnmore.qbconnector.types.Term;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

public class XMLBuilderService {

	private static final Logger LOG = Logger.getLogger(XMLBuilderService.class.getName());

	public String buildTermsAddXml() {
		List<Term> terms = DataConnector.getNewTerms();
		String bodyXml = "";
		if (terms == null || terms.isEmpty()) {
			return bodyXml;
		}
		try {
			for (Term term : terms) {
				String xml = IOUtils.resourceToString("templates\\TermsAdd.xml", null, getClass().getClassLoader());
				xml = xml.replace("##TERMNAME##", term.name == null ? "" : term.name);
				xml = xml.replace("##ACTIVE##", term.active ? "true" : "false");
				xml = xml.replace("##DUEDAYS##", Integer.toString(term.dueAfterDays));
				bodyXml += xml;
			}
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Unable to load XML message template file", ex);
			return null;
		}
		return insertIntoBaseXml(bodyXml);
	}
	
	public String buildTaxesAddXml() {
		List<SalesTax> taxes = DataConnector.getNewSalesTaxes();
		String bodyXml = "";
		if (taxes == null || taxes.isEmpty()) {
			return bodyXml;
		}
		try {
			for (SalesTax tax : taxes) {
				String xml = IOUtils.resourceToString("templates\\SalesTaxAdd.xml", null, getClass().getClassLoader());
				xml = xml.replace("##TAXNAME##", tax.name == null ? "" : tax.name);
				xml = xml.replace("##ACTIVE##", tax.active ? "true" : "false");
				xml = xml.replace("##TAXPERCENT##", Double.toString(tax.taxRate));
				//xml = xml.replace("##TAXID##", tax.uidpk == null ? "" : tax.uidpk);
				xml = xml.replace("##TAXAGENCYNAME##", tax.taxAgencyName == null ? "tax vendor" : tax.taxAgencyName);
				bodyXml += xml;
			}
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Unable to load XML message template file", ex);
			return null;
		}
		return insertIntoBaseXml(bodyXml);
	}
	
	public String buildCustomerAddXml() {
		List<Customer> customers = DataConnector.getNewCustomers();
		String bodyXml = "";
		if (customers == null || customers.isEmpty()) {
			return bodyXml;
		}
		try {
			for (Customer cust : customers) {
				String xml = IOUtils.resourceToString("templates\\CustomerAdd.xml", null, getClass().getClassLoader());
				xml = xml.replace("##CUSTNAME##", (cust.firstName == null ? "" : cust.firstName) + (cust.lastName == null ? "" : cust.lastName));
				xml = xml.replace("##ACTIVE##", "true");
				xml = xml.replace("##FIRSTNAME##", cust.firstName == null ? "" : cust.firstName);
				xml = xml.replace("##LASTNAME##", cust.lastName == null ? "" : cust.lastName);
				xml = xml.replace("##BILLADDR1##", cust.billAddress.line1 == null ? "" : cust.billAddress.line1);
				xml = xml.replace("##BILLADDR2##", cust.billAddress.line2 == null ? "" : cust.billAddress.line2);
				xml = xml.replace("##BILLADDR3##", cust.billAddress.line3 == null ? "" : cust.billAddress.line3);
				xml = xml.replace("##BILLCITY##", cust.billAddress.city == null ? "" : cust.billAddress.city);
				xml = xml.replace("##BILLSTATE##", cust.billAddress.state == null ? "" : cust.billAddress.state);
				xml = xml.replace("##BILLZIP##", cust.billAddress.zipCode == null ? "" : cust.billAddress.zipCode);
				xml = xml.replace("##SHIPADDR1##", cust.shipAddress.line1 == null ? "" : cust.shipAddress.line1);
				xml = xml.replace("##SHIPADDR2##", cust.shipAddress.line2 == null ? "" : cust.shipAddress.line2);
				xml = xml.replace("##SHIPADDR3##", cust.shipAddress.line3 == null ? "" : cust.shipAddress.line3);
				xml = xml.replace("##SHIPCITY##", cust.shipAddress.city == null ? "" : cust.shipAddress.city);
				xml = xml.replace("##SHIPSTATE##", cust.shipAddress.state == null ? "" : cust.shipAddress.state);
				xml = xml.replace("##SHIPZIP##", cust.shipAddress.zipCode == null ? "" : cust.shipAddress.zipCode);
				xml = xml.replace("##PHONE##", cust.phoneNo == null ? "" : cust.phoneNo);
				xml = xml.replace("##FAX##", cust.fax == null ? "" : cust.fax);
				xml = xml.replace("##EMAIL##", cust.email == null ? "" : cust.email);
				xml = xml.replace("##CONTACT##", cust.contactName == null ? "" : cust.contactName);
				xml = xml.replace("##TERMNAME##", cust.paymentTermName == null ? "" : cust.paymentTermName);
				xml = xml.replace("##SALESTAXCODE##", "TAX");
				xml = xml.replace("##SALESTAXNAME##", cust.salesTaxName == null ? "" : cust.salesTaxName);
				bodyXml += xml;
			}
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Unable to load XML message template file", ex);
			return null;
		}
		return insertIntoBaseXml(bodyXml);
		
	}
	
	private String insertIntoBaseXml(String body) {
		try {
			String baseXml = IOUtils.resourceToString("templates\\Base.xml", null, getClass().getClassLoader());
			baseXml = baseXml.replace("##REQUESTBODY##", body);
			return baseXml;
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Unable to load base template file", ex);
			return "";
		}
	}
}
