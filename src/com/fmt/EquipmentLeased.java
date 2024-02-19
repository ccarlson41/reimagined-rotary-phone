package com.fmt;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Authors: Cam Carlson and Peyton Nelson 03/03/2023 This class handles anything
 * to do with Equipment Leased, such as constructing, getting and setting,
 */
public class EquipmentLeased extends Equipment {

	private final double thirtyDayFee;
	private final LocalDate startDate;
	private final LocalDate endDate;

	public EquipmentLeased(String code, String name, String model, double thirtyDayFee, LocalDate startDate,
			LocalDate endDate) {
		super(code, name, model);
		this.thirtyDayFee = thirtyDayFee;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public double getThirtyDayFee() {
		return thirtyDayFee;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public long getDaysLeased()
	{
		long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		return days;

	}

	public double getTotal() {
		double total = 0.0;
		LocalDate endDate = this.getEndDate();
		LocalDate startDate = this.getStartDate();
		long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		total = (this.getThirtyDayFee()) * (days / 30.00);
		return Math.round(total * 100.00) / 100.00;

	}

	public double getTax() {
        double total = this.getTotal();
        if(total >= 10000 && total < 100000) {
            return 500.0;
        }
        else if(total >= 100000) {
            return 1500.0;
        }
        else {
            return 0.0;
        }
    }
}
