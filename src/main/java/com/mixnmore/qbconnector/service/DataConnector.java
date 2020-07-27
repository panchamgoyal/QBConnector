package com.mixnmore.qbconnector.service;

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
	
}
