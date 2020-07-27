package com.mixnmore.qbconnector.service;

import com.mixnmore.qbconnector.types.Address;
import com.mixnmore.qbconnector.types.Customer;
import com.mixnmore.qbconnector.types.SalesTax;
import com.mixnmore.qbconnector.types.Term;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	private static final String QUERY_GET_CUSTOMERS = "SELECT c.CustID, c.CustFirstName, c.CustLastName, c.CustContact, c.CustPhone, c.CustEmail, c.CustFax, c.CustAddID, "
			+ "ca.AddressName AS CustAddrLine1, ca.AddressLine1 AS CustAddrLine2, ca.AddressLine2 AS CustAddrLine3, ca.AddressCity AS custAddrCity, "
			+ "ca.AddressState AS custAddrState, ca.AddressZip AS CustAddrZip, ca.AddressEmail AS CustAddrEmail,ca.AddressPhone AS CustAddrPhone, "
			+ "ca.AddressFax AS CustAddrFax, c.CustBillAddID, cb.AddressName AS BillAddrLine1, cb.AddressLine1 AS BillAddrLine2, cb.AddressLine2 AS BillAddrLine3, "
			+ "cb.AddressCity AS BillAddrCity, cb.AddressState AS BillAddrState, cb.AddressZip AS BillAddrZip, cb.AddressEmail AS BillAddrEmail, "
			+ "cb.AddressPhone AS BillAddrPhone, ca.AddressFax AS BillAddrFax, c.CustTermsID, terms.termName, c.CustTaxStatusID, tax.TaxName, c.CustSalesmanID, "
			+ "s.SalesmanName, s.SalesmanPercent FROM tblCustomers AS c LEFT JOIN tblAddressBook AS ca ON ca.AddressID = c.CustAddID LEFT JOIN tblAddressBook AS cb "
			+ "ON cb.AddressID = c.CustBillAddID LEFT JOIN tblTerms AS terms ON terms.termID = c.CustTermsID LEFT JOIN tblTaxIDs AS tax ON tax.TaxID = c.CustTaxStatusID "
			+ "LEFT JOIN tblSalesmans AS s ON s.SalesmanID = c.CustSalesmanID";
    
    private static final Logger LOG = Logger.getLogger(DataConnector.class.getName());
    
    private static Connection getConnection() {
        try {
            String connectionUrl="jdbc:sqlserver://"+ip+";instance="+insta+";DatabaseName=ADASupplies";
            return DriverManager.getConnection(connectionUrl, username, password);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Unable to get connection to DB", ex);
            return null;
        }
    }
	
	public static boolean checkCredentials(String username, String password) {
		try (Connection connection = getConnection()) {
			if(connection == null)
				return false;
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
			if(connection == null)
				return Collections.EMPTY_LIST;
			PreparedStatement stmt = connection.prepareStatement(QUERY_GET_TERMS);
			ResultSet result = stmt.executeQuery();
			List<Term> foundTerms = new LinkedList<>();
			while(result.next()) {
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
			if(connection == null)
				return Collections.EMPTY_LIST;
			PreparedStatement stmt = connection.prepareStatement(QUERY_GET_SALES_TAXES);
			ResultSet result = stmt.executeQuery();
			List<SalesTax> foundTaxes = new LinkedList<>();
			while(result.next()) {
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
			if(connection == null)
				return Collections.EMPTY_LIST;
			PreparedStatement stmt = connection.prepareStatement(QUERY_GET_CUSTOMERS);
			ResultSet result = stmt.executeQuery();
			List<Customer> foundCustomers = new LinkedList<>();
			while(result.next()) {
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
}