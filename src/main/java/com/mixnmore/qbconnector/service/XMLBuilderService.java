package com.mixnmore.qbconnector.service;

import com.mixnmore.qbconnector.types.Customer;
import com.mixnmore.qbconnector.types.Invoice;
import com.mixnmore.qbconnector.types.InvoiceItem;
import com.mixnmore.qbconnector.types.Item;
import com.mixnmore.qbconnector.types.SalesTax;
import com.mixnmore.qbconnector.types.Term;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

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
				String xml = IOUtils.resourceToString("TermsAdd.xml", null, getClass().getClassLoader());
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
				String xml = IOUtils.resourceToString("SalesTaxAdd.xml", null, getClass().getClassLoader());
				xml = xml.replace("##TAXNAME##", tax.name == null ? "" : tax.name);
				xml = xml.replace("##ACTIVE##", tax.active ? "true" : "false");
				xml = xml.replace("##TAXPERCENT##", Double.toString(tax.taxRate));
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
				String xml = IOUtils.resourceToString("CustomerAdd.xml", null, getClass().getClassLoader());
				xml = xml.replace("##CUSTNAME##", (cust.firstName == null ? "" : cust.firstName) + " " + (cust.lastName == null ? "" : cust.lastName));
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
	
	public String buildItemsAddXml() {
		List<Item> items = DataConnector.getNewItems();
		String bodyXml = "";
		if (items == null || items.isEmpty()) {
			return bodyXml;
		}
		try {
			bodyXml = IOUtils.resourceToString("InventoryItemAdd.xml", null, getClass().getClassLoader());
			for (Item item : items) {
				String xml = IOUtils.resourceToString("InventoryItemAdd.xml", null, getClass().getClassLoader());
				xml = xml.replace("##ITEMNAME##", StringUtils.isEmpty(item.name) ? (item.description == null? "": item.description) : item.name);
				xml = xml.replace("##ACTIVE##", "true");
				xml = xml.replace("##SALESTAXCODE##", "TAX");
				xml = xml.replace("##DESCRIPTION##", item.description == null ? "" : item.description);
				xml = xml.replace("##INCOMEACCOUNT##", "income account");
				xml = xml.replace("##ASSETACCOUNT##", "inventory account");
				xml = xml.replace("##COGSACCOUNT##", "inventory cogs");
				bodyXml += xml;
			}
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Unable to load XML message template file", ex);
			return null;
		}
		return insertIntoBaseXml(bodyXml);
	}
	
	public String buildInvoicesAddXml() {
		List<Invoice> invoices = DataConnector.getNewInvoices();
		String bodyXml = "";
		if (invoices == null || invoices.isEmpty()) {
			return bodyXml;
		}
		try {
			String xmlTemplateInvoice = IOUtils.resourceToString("InvoiceAdd.xml", null, getClass().getClassLoader());
			String xmlTemplateInvoiceItem = IOUtils.resourceToString("InvoiceLineAdd.xml", null, getClass().getClassLoader());
			for (Invoice inv : invoices) {
				String xml = new String(xmlTemplateInvoice);
				xml = xml.replace("##CUSTNAME##", inv.customerName == null ? "" : inv.customerName);
				xml = xml.replace("##INVDATE##", inv.invoiceDate == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(inv.invoiceDate));
				xml = xml.replace("##INVID##", inv.uidpk);
				xml = xml.replace("##BILLADDR1##", inv.billAddress.line1 == null ? "" : inv.billAddress.line1);
				xml = xml.replace("##BILLADDR2##", inv.billAddress.line2 == null ? "" : inv.billAddress.line2);
				xml = xml.replace("##BILLADDR3##", inv.billAddress.line3 == null ? "" : inv.billAddress.line3);
				xml = xml.replace("##BILLCITY##", inv.billAddress.city == null ? "" : inv.billAddress.city);
				xml = xml.replace("##BILLSTATE##", inv.billAddress.state == null ? "" : inv.billAddress.state);
				xml = xml.replace("##BILLZIP##", inv.billAddress.zipCode == null ? "" : inv.billAddress.zipCode);
				
				xml = xml.replace("##SHIPADDR1##", inv.shipAddress.line1 == null ? "" : inv.shipAddress.line1);
				xml = xml.replace("##SHIPADDR2##", inv.shipAddress.line2 == null ? "" : inv.shipAddress.line2);
				xml = xml.replace("##SHIPADDR3##", inv.shipAddress.line3 == null ? "" : inv.shipAddress.line3);
				xml = xml.replace("##SHIPCITY##", inv.shipAddress.city == null ? "" : inv.shipAddress.city);
				xml = xml.replace("##SHIPSTATE##", inv.shipAddress.state == null ? "" : inv.shipAddress.state);
				xml = xml.replace("##SHIPZIP##", inv.shipAddress.zipCode == null ? "" : inv.shipAddress.zipCode);
				
				xml = xml.replace("##TERMNAME##", inv.termName == null ? "" : inv.termName);
				xml = xml.replace("##SALESTAXNAME##", inv.salesTaxName == null ? "" : inv.salesTaxName);
				String itemXml = "";
				for(InvoiceItem item: inv.items) {
					itemXml = itemXml + xmlTemplateInvoiceItem;
					itemXml = itemXml.replace("##ITEMNAME##", item.name);
					itemXml = itemXml.replace("##ITEMDESC##", item.description);
					itemXml = itemXml.replace("##ITEMQTY##", Integer.toString(item.quantity));
					itemXml = itemXml.replace("##AMOUNT##", Double.toString(item.amount));
					itemXml = itemXml.replace("##SERIAL##", item.serialNo);
				}
				
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
			String baseXml = IOUtils.resourceToString("Base.xml", null, getClass().getClassLoader());
			baseXml = baseXml.replace("##REQUESTBODY##", body);
			return baseXml;
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Unable to load base template file", ex);
			return "";
		}
	}
}
