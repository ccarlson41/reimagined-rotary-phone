package com.fmt;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023 This class handles anything
 * to do with products, such as constructing, getting and and converting to
 * string
 */

public class Product extends Item {

	private String unit;
	private double unitPrice;
	private double amountPurchased;

	public Product(String code, String name, String unit, double unitPrice, double amountPurchased) {
		super(code, name);
		this.unit = unit;
		this.unitPrice = unitPrice;
		this.amountPurchased = amountPurchased;
	}

	public Product(String code, String name, String unit, double unitPrice) {
		super(code, name);
		this.unit = unit;
		this.unitPrice = unitPrice;
	}

	public double getAmountPurchased() {
		return amountPurchased;
	}

	public void setAmountPurchased(double amountPurchased) {
		this.amountPurchased = amountPurchased;
	}

	public String getUnit() {
		return unit;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public String toString() {
		return this.getCode() + ", " + this.getName() + ", " + this.unit + ", " + this.unitPrice;
	}

	protected String getModel() {
		return null;
	}

	protected double getHourlyRate() {
		return 0;
	}

	public double getTax() {
		double tax = this.getTotal() * 0.0715;
		return Math.round(tax * 100.00) / 100.00;
	}

	public double getTotal() {
		double total = this.unitPrice * this.amountPurchased;
		return Math.round(total * 100.00) / 100.00;
	}

}
