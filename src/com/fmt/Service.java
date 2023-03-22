package com.fmt;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023 This class handles anything
 * to do with service, such as constructing, getting, setting, and converting to
 * string
 */

public class Service extends Item {

	private double hourlyRate;
	private double hoursWorked;

	public Service(String code, String name, double hourlyRate, double hoursWorked) {
		super(code, name);
		this.hourlyRate = hourlyRate;
		this.hoursWorked = hoursWorked;
	}

	public double getHoursWorked() {
		return hoursWorked;
	}

	public void setHoursWorked(double hoursWorked) {
		this.hoursWorked = hoursWorked;
	}

	public Service(String code, String name, double hourlyRate) {
		super(code, name);
		this.hourlyRate = hourlyRate;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}

	public void setHourlyRate(double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

	public String toString() {
		return this.getCode() + ", " + this.getName() + ", " + this.hourlyRate;
	}

	@Override
	protected String getModel() {
		return null;
	}

	@Override
	protected String getUnit() {
		return null;
	}

	@Override
	protected double getUnitPrice() {
		return 0;
	}

	public double getTax() {
		double tax = this.getTotal() * 0.0345;
		return Math.round(tax * 100.00) / 100.00;
	}

	public double getTotal() {
		double total = this.hourlyRate * this.hoursWorked;
		return Math.round(total * 100.00) / 100.00;
	}

}
