package com.mixnmore.qbconnector.types;

import java.util.Date;
import java.util.List;

public class Invoice {
	
	public String uidpk;
	public String customerName;
	public Date invoiceDate;
	
	public Address billAddress;
	public Address shipAddress;
	
	public String termName;
	public String salesTaxName;
	
	public List<InvoiceItem> items;
	
}
