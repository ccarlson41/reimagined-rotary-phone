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

	public static final Comparator<Invoice> compareByTotal = new Comparator<>() {
		public int compare(Invoice p1, Invoice p2) {
			return Double.compare(p1.getTotal(), p2.getTotal());
		}
	};

	public static final Comparator<Invoice> byManager = new Comparator<>() {
		public int compare(Invoice p1, Invoice p2) {
			return p1.getStore().getManager().getLastName().compareTo(p2.getStore().getManager().getLastName());
		}
	};

	public static final Comparator<Invoice> compareByInvoiceCode = new Comparator<>() {
		public int compare(Invoice p1, Invoice p2) {
			return p1.getInvoiceCode().compareTo(p2.getInvoiceCode());
		}
	};

}
