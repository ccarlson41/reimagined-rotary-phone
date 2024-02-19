package com.fmt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023 This class handles anything
 * to do with Stores, such as constructing, getting and setting,
 */
public class Store {

	private final String storeCode;
	private final Person manager;
	private final Address address;
	public List<Invoice> sales;

	public Store(String storeCode, Person manager, Address address) {
		this.storeCode = storeCode;
		this.manager = manager;
		this.address = address;
		this.sales = new ArrayList<>();
	}
	

	public String toString() {
		return this.storeCode + ", " + this.manager + ", " + this.address + "sales:: " + sales;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public Person getManager() {
		return manager;
	}

	public Address getAddress() {
		return address;
	}

	public List<Invoice> getSales() {
		return sales;
	}

	public void getAddInvoice(Invoice i) {
		this.sales.add(i);
	}

	public int getNumItems() {
		int numItems = 0;
		for (Invoice invoice : sales) {
			numItems += invoice.getNumItems();
		}
		return numItems;
	}

	public double getTotal() {
		double total = 0.0;
		for (Invoice inv : this.sales) {
			total += inv.getTotal();
		}
		return Math.round((total) * 100.00) / 100.00;
	}

	public static final Comparator<Store> byManagerLast = new Comparator<>() {
		public int compare(Store p1, Store p2) {
			return p1.getManager().getLastName().compareTo(p2.getManager().getLastName());
		}
	};

	public static final Comparator<Store> byManagerFirst = new Comparator<>() {
		public int compare(Store p1, Store p2) {
			return p1.getManager().getFirstName().compareTo(p2.getManager().getFirstName());
		}
	};

	public static final Comparator<Store> byTotal = new Comparator<>() {
		public int compare(Store p1, Store p2) {
			return Double.compare(p1.getTotal(), p2.getTotal());
		}
	};

	static Comparator<Store> byLastFirstTotal = byManagerLast.thenComparing(byManagerFirst).thenComparing(byTotal);

}
