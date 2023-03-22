package com.fmt;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023 This class handles anything
 * to do with equipment, such as constructing, getting, setting, and converting
 * to string
 */

public class Equipment extends Item {

	private String model;

	public Equipment(String code, String name, String model) {
		super(code, name);
		this.model = model;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String toString() {
		return this.getCode() + ", " + this.getName() + ", " + this.model;
	}

	@Override
	protected String getUnit() {
		return null;
	}

	@Override
	protected double getUnitPrice() {
		return 0;
	}

	@Override
	protected double getHourlyRate() {
		return 0;
	}

	@Override
	protected double getTotal() {
		return 0;
	}

	@Override
	protected double getTax() {
		return 0;
	}

}
