package com.fmt;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023 This class handles anything
 * to do with Items, such as constructing, getting and converting to string
 */

public abstract class Item {

	private String code;
	private String name;

	public Item(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public abstract String toString();

	public String getName() {
		return name;
	}

	protected abstract String getModel();

	protected abstract String getUnit();

	protected abstract double getUnitPrice();

	protected abstract double getHourlyRate();

	protected abstract double getTotal();

	protected abstract double getTax();

}
