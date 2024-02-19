package com.fmt;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//import java.util.Comparator;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023 This class handles anything
 * to do with Invoices, such as constructing, getting and setting,
 */
public class Invoice {

	private final String invoiceCode;
	private final Store store;
	private final Person customer;
	private final Person salesPerson;
	private final LocalDate invoiceDate;
	private List<Item> purchased;

	public Invoice(String Invoicecode, Store store, Person customer, Person salesPerson, LocalDate date) {
		this.invoiceCode = Invoicecode;
		this.store = store;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.invoiceDate = date;
		this.purchased = new ArrayList<Item>();
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public Store getStore() {
		return store;
	}

	public Person getCustomer() {
		return customer;
	}

	public Person getSalesPerson() {
		return salesPerson;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public List<Item> getPurchased() {
		return purchased;
	}

	public void getAddItem(Item item) {
		this.purchased.add(item);
	}

	public int getNumItems() {
		int numItems = this.purchased.size();
		return numItems;
	}

	public double getTotalTax() {
		double totalTax = 0.0;
		for (Item item : this.purchased) {
			totalTax += item.getTax();
		}
		return Math.round((totalTax) * 100.00) / 100.00;
	}

	public double getTotal() {
		double total = 0.0;
		for (Item item : this.purchased) {
			total += item.getTotal();
		}
		total += this.getTotalTax();
		return Math.round((total) * 100.00) / 100.00;
	}

	public String toString() {
		return "Invoice [invoiceCode=" + invoiceCode + ", store=" + store + ", customer=" + customer + ", salesPerson="
				+ salesPerson + ", invoiceDate=" + invoiceDate + ", purchased=" + purchased + "]";
	}

	public static final Comparator<Invoice> byTotal = new Comparator<>() {
	    public int compare(Invoice p1, Invoice p2) {
	        return Double.compare(p1.getTotal(), p2.getTotal());
	    }
	};

	public static final Comparator<Invoice> byManager = new Comparator<>() {
	    public int compare(Invoice p1, Invoice p2) {
	        return p1.getStore().getManager().getLastName().compareTo(p2.getStore().getManager().getLastName());
	    }
	};

	public static final Comparator<Invoice> byInvoiceCode = new Comparator<>() {
	    public int compare(Invoice p1, Invoice p2) {
	        return p1.getInvoiceCode().compareTo(p2.getInvoiceCode());
	    }
	};

	public static final Comparator<Invoice> byStore = new Comparator<>() {
	    public int compare(Invoice p1, Invoice p2) {
	        int storeCmp =  p2.getStore().getStoreCode().compareTo(p1.getStore().getStoreCode());
	        if (storeCmp != 0) {
	            return storeCmp;
	        } else {
	        	int lastNameCmp = p2.getSalesPerson().getLastName().compareTo(p1.getSalesPerson().getLastName());
		        if (lastNameCmp != 0) {
		            return lastNameCmp;
		        } else {
		            return p2.getSalesPerson().getFirstName().compareTo(p1.getSalesPerson().getFirstName());
		        }
	        }
	    }
	};
	
	public static final Comparator<Invoice> byCustomer = new Comparator<>() {
	    public int compare(Invoice p1, Invoice p2) {
	        int lastNameCmp = p2.getCustomer().getLastName().compareTo(p1.getCustomer().getLastName());
	        if (lastNameCmp != 0) {
	            return lastNameCmp;
	        } else {
	            return p2.getCustomer().getFirstName().compareTo(p1.getCustomer().getFirstName());
	        }
	    }
	};


	}
	
//	SortedInvoiceList invoicesByLastName = new SortedInvoiceList(new Comparator<Invoice>() {
//	    public int compare(Invoice invoice1, Invoice invoice2) {
//	        String lastName1 = invoice1.getCustomer().getLastName();
//	        String lastName2 = invoice2.getCustomer().getLastName();
//	        int lastNameCompare = lastName1.compareTo(lastName2);
//	        if (lastNameCompare != 0) {
//	            return lastNameCompare;
//	        } else {
//	            String firstName1 = invoice1.getCustomer().getFirstName();
//	            String firstName2 = invoice2.getCustomer().getFirstName();
//	            return firstName1.compareTo(firstName2);
//	        }
//	    }
//	});
//
//	invoicesByLastName.add(invoice1);
//	invoicesByLastName.add(invoice2);
//	invoicesByLastName.add(invoice3);
//
	
//	Invoice firstInvoiceByLastName = invoicesByLastName.get(0);


