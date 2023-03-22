package com.fmt;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023 This class handles anything
 * to do with Equipment Purchased, such as constructing, getting and setting,
 */
public class EquipmentPurchased extends Equipment {

	private final double purchasePrice;
	private String invCode;

	public EquipmentPurchased(String code, String name, String model, double purchasePrice) {
		super(code, name, model);
		this.purchasePrice = purchasePrice;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setInvCode(String invCode) {
		this.invCode = invCode;
	}
	
	public double getTotal() {
		return Math.round(this.purchasePrice * 100.00) / 100.00;

	}

}
