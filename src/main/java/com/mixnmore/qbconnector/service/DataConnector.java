package com.mixnmore.qbconnector.service;

import com.mixnmore.qbconnector.types.Address;
import com.mixnmore.qbconnector.types.Customer;
import com.mixnmore.qbconnector.types.Invoice;
import com.mixnmore.qbconnector.types.InvoiceItem;
import com.mixnmore.qbconnector.types.Item;
import com.mixnmore.qbconnector.types.SalesTax;
import com.mixnmore.qbconnector.types.Term;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pranshu Agarwal
 */
public class DataConnector {

	private static final String ip = "104.45.154.82:1535";
	private static final String insta = "MSSQLSERVER";
	private static final String username = "ssyh";
	private static final String password = "mX7rK^vz}]wX~cudYK~_)>(DZn/[>[";

	private static final String QUERY_LOGIN = "SELECT EmployeeID FROM tblEmployees WHERE EmployeeUserName=? AND EmployeePassword=?";
	private static final String QUERY_GET_TERMS = "SELECT termName, termDaysOffset FROM tblTerms";
	private static final String QUERY_GET_SALES_TAXES = "SELECT TaxID, TaxName, TaxPercent FROM tblTaxIDs";
	private static final String QUERY_GET_ITEMS = "SELECT InvoiceItemDesc, InvoiceItemNum FROM tblInvoiceItems GROUP BY InvoiceItemDesc, InvoiceItemNum";
	private static final String QUERY_GET_CUSTOMERS = "SELECT c.CustID, c.CustFirstName, c.CustLastName, c.CustContact, c.CustPhone, c.CustEmail, c.CustFax, c.CustAddID, "
			+ "ca.AddressName AS CustAddrLine1, ca.AddressLine1 AS CustAddrLine2, ca.AddressLine2 AS CustAddrLine3, ca.AddressCity AS custAddrCity, "
			+ "ca.AddressState AS custAddrState, ca.AddressZip AS CustAddrZip, ca.AddressEmail AS CustAddrEmail,ca.AddressPhone AS CustAddrPhone, "
			+ "ca.AddressFax AS CustAddrFax, c.CustBillAddID, cb.AddressName AS BillAddrLine1, cb.AddressLine1 AS BillAddrLine2, cb.AddressLine2 AS BillAddrLine3, "
			+ "cb.AddressCity AS BillAddrCity, cb.AddressState AS BillAddrState, cb.AddressZip AS BillAddrZip, cb.AddressEmail AS BillAddrEmail, "
			+ "cb.AddressPhone AS BillAddrPhone, ca.AddressFax AS BillAddrFax, c.CustTermsID, terms.termName, c.CustTaxStatusID, tax.TaxName, c.CustSalesmanID, "
			+ "s.SalesmanName, s.SalesmanPercent FROM tblCustomers AS c LEFT JOIN tblAddressBook AS ca ON ca.AddressID = c.CustAddID LEFT JOIN tblAddressBook AS cb "
			+ "ON cb.AddressID = c.CustBillAddID LEFT JOIN tblTerms AS terms ON terms.termID = c.CustTermsID LEFT JOIN tblTaxIDs AS tax ON tax.TaxID = c.CustTaxStatusID "
			+ "LEFT JOIN tblSalesmans AS s ON s.SalesmanID = c.CustSalesmanID";

	private static final String QUERY_GET_INVOICES = "SELECT inv.InvoiceID, inv.InvoiceNumber, inv.InvoiceDate, inv.InvoiceDue, inv.InvoiceCustID, inv.InvoiceBillToAddID, "
			+ "inv.InvoiceShipToAddID, c.CustFirstName, c.CustLastName, c.CustTermsID, c.CustTaxStatusID, term.termName, term.termDaysOffset, ba.AddressName AS BillAddrLine1, "
			+ "ba.AddressLine1 AS BillAddrLine2, ba.AddressLine2 AS BillAddrLine3, ba.AddressCity AS BillCity, ba.AddressState AS BillState, ba.AddressZip AS BillZip, "
			+ "ba.AddressPhone AS BillPhone, ba.AddressFax AS BillFax, ba.AddressEmail AS BillEmail, sa.AddressName AS ShipAddrLine1, sa.AddressLine1 AS ShipAddrLine2, "
			+ "sa.AddressLine2 AS ShipAddrLine3, sa.AddressCity AS ShipCity, sa.AddressState AS ShipState, sa.AddressZip AS ShipZip, sa.AddressPhone AS ShipPhone, "
			+ "sa.AddressFax AS ShipFax, sa.AddressEmail AS ShipEmail, invitm.InvoiceItemID, invitm.InvoiceItemNum, invitm.InvoiceItemQTY, invitm.InvoiceItemDesc, "
			+ "invitm.InvoiceItemSellPrice, invitm.[InvoiceItem Taxable], invitm.InvoiceItemSellPrice*invitm.InvoiceItemQTY AS InvoiceItemTotal, tax.TaxName "
			+ "FROM tblInvoices AS inv LEFT JOIN tblCustomers AS c ON c.CustID = inv.InvoiceCustID LEFT JOIN tblInvoiceItems AS invitm ON invitm.InvoiceID = inv.InvoiceID "
			+ "LEFT JOIN tblAddressBook AS ba ON ba.AddressID = inv.InvoiceBillToAddID LEFT JOIN tblAddressBook AS sa ON sa.AddressID = inv.InvoiceShipToAddID "
			+ "LEFT JOIN tblTerms AS term ON term.termID = c.CustTermsID LEFT JOIN tblTaxIDs AS tax ON tax.TaxID = c.CustTaxStatusID WHERE inv.InvoiceDate >= ?";

	private static final Logger LOG = Logger.getLogger(DataConnector.class.getName());

	private static Connection getConnection() {
		try {
			String connectionUrl = "jdbc:sqlserver://" + ip + ";instance=" + insta + ";DatabaseName=ADASupplies";
			return DriverManager.getConnection(connectionUrl, username, password);
		} catch (SQLException ex) {
			LOG.log(Level.SEVERE, "Unable to get connection to DB", ex);
			return null;
		}
	}

	public static boolean checkCredentials(String username, String password) {
		try (Connection connection = getConnection()) {
			if (connection == null) {
				return false;
			}
			PreparedStatement stmt = connection.prepareStatement(QUERY_LOGIN);
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet result = stmt.executeQuery();
			return result.next();
		} catch (SQLException ex) {
			LOG.log(Level.SEVERE, "Error during query execution", ex);
			return false;
		}
	}

	public static List<Term> getNewTerms() {
		try (Connection connection = getConnection()) {
			if (connection == null) {
				return Collections.EMPTY_LIST;
			}
			PreparedStatement stmt = connection.prepareStatement(QUERY_GET_TERMS);
			ResultSet result = stmt.executeQuery();
			List<Term> foundTerms = new LinkedList<>();
			while (result.next()) {
				Term term = new Term();
				term.name = result.getString("termName");
				term.active = true;
				term.dueAfterDays = result.getInt("termDaysOffset");
				foundTerms.add(term);
			}
			return foundTerms;
		} catch (SQLException ex) {
			LOG.log(Level.SEVERE, "Error during query execution", ex);
			return Collections.EMPTY_LIST;
		}
	}

	public static List<SalesTax> getNewSalesTaxes() {
		try (Connection connection = getConnection()) {
			if (connection == null) {
				return Collections.EMPTY_LIST;
			}
			PreparedStatement stmt = connection.prepareStatement(QUERY_GET_SALES_TAXES);
			ResultSet result = stmt.executeQuery();
			List<SalesTax> foundTaxes = new LinkedList<>();
			while (result.next()) {
				SalesTax tax = new SalesTax();
				tax.active = true;
				tax.name = result.getString("TaxName");
				tax.taxRate = result.getDouble("TaxPercent");
				tax.uidpk = result.getString("TaxID");
				foundTaxes.add(tax);
			}
			return foundTaxes;
		} catch (SQLException ex) {
			LOG.log(Level.SEVERE, "Error during query execution", ex);
			return Collections.EMPTY_LIST;
		}
	}

	public static List<Customer> getNewCustomers() {
		try (Connection connection = getConnection()) {
			if (connection == null) {
				return Collections.EMPTY_LIST;
			}
			PreparedStatement stmt = connection.prepareStatement(QUERY_GET_CUSTOMERS);
			ResultSet result = stmt.executeQuery();
			List<Customer> foundCustomers = new LinkedList<>();
			while (result.next()) {
				Customer cust = new Customer();
				cust.uidpk = result.getString("CustID");
				cust.firstName = result.getString("CustFirstName");
				cust.lastName = result.getString("CustLastName");
				cust.contactName = result.getString("CustContact");
				cust.phoneNo = result.getString("CustPhone");
				cust.email = result.getString("CustEmail");
				cust.fax = result.getString("CustFax");

				Address custAddr = new Address();
				custAddr.line1 = result.getString("CustAddrLine1");
				custAddr.line2 = result.getString("CustAddrLine2");
				custAddr.line3 = result.getString("CustAddrLine3");
				custAddr.city = result.getString("custAddrCity");
				custAddr.state = result.getString("custAddrState");
				custAddr.zipCode = result.getString("CustAddrZip");
				custAddr.email = result.getString("CustAddrEmail");
				custAddr.phone = result.getString("CustAddrPhone");
				custAddr.fax = result.getString("CustAddrFax");
				cust.shipAddress = custAddr;

				Address billAddr = new Address();
				billAddr.line1 = result.getString("BillAddrLine1");
				billAddr.line2 = result.getString("BillAddrLine2");
				billAddr.line3 = result.getString("BillAddrLine3");
				billAddr.city = result.getString("BillAddrCity");
				billAddr.state = result.getString("BillAddrState");
				billAddr.zipCode = result.getString("BillAddrZip");
				billAddr.email = result.getString("BillAddrEmail");
				billAddr.phone = result.getString("BillAddrPhone");
				billAddr.fax = result.getString("BillAddrFax");
				cust.billAddress = billAddr;

				cust.paymentTermName = result.getString("termName");
				cust.salesTaxName = result.getString("TaxName");
				foundCustomers.add(cust);
			}
			return foundCustomers;
		} catch (SQLException ex) {
			LOG.log(Level.SEVERE, "Error during query execution", ex);
			return Collections.EMPTY_LIST;
		}
	}

	public static List<Item> getNewItems() {
		try (Connection connection = getConnection()) {
			if (connection == null) {
				return Collections.EMPTY_LIST;
			}
			PreparedStatement stmt = connection.prepareStatement(QUERY_GET_ITEMS);
			ResultSet result = stmt.executeQuery();
			List<Item> foundItems = new LinkedList<>();
			while (result.next()) {
				Item item = new Item();
				item.active = true;
				item.description = result.getString("InvoiceItemDesc");
				item.name = result.getString("InvoiceItemNum");
				item.salesTaxCode = "TAX";
				foundItems.add(item);
			}
			return foundItems;
		} catch (SQLException ex) {
			LOG.log(Level.SEVERE, "Error during query execution", ex);
			return Collections.EMPTY_LIST;
		}
	}

	public static List<Invoice> getNewInvoices() {
		try (Connection connection = getConnection()) {
			if (connection == null) {
				return Collections.EMPTY_LIST;
			}
			PreparedStatement stmt = connection.prepareStatement(QUERY_GET_INVOICES);
			LocalDate date = LocalDate.now().minus(Period.ofDays(1));
			stmt.setDate(1, Date.valueOf(date));
			ResultSet result = stmt.executeQuery();
			List<Invoice> foundInvoices = new LinkedList<>();
			int sno = 1;
			while (result.next()) {
				String invNo = result.getString("InvoiceNumber");
				Invoice inv = foundInvoices.stream().filter(i -> i.uidpk.equalsIgnoreCase(invNo)).findFirst().orElse(null);
				if (inv == null) {
					inv = new Invoice();
					inv.uidpk = invNo;
					String firstName = result.getString("CustFirstName");
					String lastName = result.getString("CustLastName");
					inv.customerName = (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName);
					inv.invoiceDate = result.getDate("InvoiceDate");
					inv.salesTaxName = result.getString("TaxName");
					inv.termName = result.getString("termName");

					Address shipAddr = new Address();
					shipAddr.line1 = result.getString("ShipAddrLine1");
					shipAddr.line2 = result.getString("ShipAddrLine2");
					shipAddr.line3 = result.getString("ShipAddrLine3");
					shipAddr.city = result.getString("ShipCity");
					shipAddr.state = result.getString("ShipState");
					shipAddr.zipCode = result.getString("ShipZip");
					shipAddr.email = result.getString("ShipEmail");
					shipAddr.phone = result.getString("ShipPhone");
					shipAddr.fax = result.getString("ShipFax");
					inv.shipAddress = shipAddr;

					Address billAddr = new Address();
					billAddr.line1 = result.getString("BillAddrLine1");
					billAddr.line2 = result.getString("BillAddrLine2");
					billAddr.line3 = result.getString("BillAddrLine3");
					billAddr.city = result.getString("BillCity");
					billAddr.state = result.getString("BillState");
					billAddr.zipCode = result.getString("BillZip");
					billAddr.email = result.getString("BillEmail");
					billAddr.phone = result.getString("BillPhone");
					billAddr.fax = result.getString("BillFax");
					inv.billAddress = billAddr;

					inv.items = new LinkedList<>();
				}
				
				InvoiceItem item = new InvoiceItem();
				item.serialNo = result.getString("InvoiceItemID");
				item.name = result.getString("InvoiceItemNum");
				item.description = result.getString("InvoiceItemDesc");
				item.quantity = result.getInt("InvoiceItemQTY");
				item.amount = result.getDouble("InvoiceItemSellPrice");

			}
			return foundInvoices;
		} catch (SQLException ex) {
			LOG.log(Level.SEVERE, "Error during query execution", ex);
			return Collections.EMPTY_LIST;
		}
	}
}
