package com.tipcalculator.app;

import java.text.NumberFormat;

public class CalculateTip {

	private String tipAmount;
	private double tipAmountDouble;

	public double getBillAmount() {
		return billAmount;
	}

	private double billAmount;
	NumberFormat format = NumberFormat.getCurrencyInstance();

	public CalculateTip(double amount) {
		// TODO Auto-generated constructor stub
	}

	public CalculateTip(float amount) {
		setTipAmount(format.format(amount));
	}

	public CalculateTip(int percentage, double amount, int numPeople) {
		billAmount = amount;
		double d = ((percentage * .01) * amount);
		d = (d / numPeople);
		tipAmountDouble = d;
		setTipAmount(format.format(d));
	}

	public CalculateTip(int percentage, double amount) {
		double d = ((percentage * .01) * amount);
		setTipAmount(format.format(d));
	}

	public String formatCurrencyAmount(double amount){
		return format.format(amount);
	}

	public CalculateTip(double a, double b) {
		setTipAmount(format.format(a + b));
	}

	/**
	 * @return the tipAmount
	 */
	public String getTipAmount() {
		return tipAmount;
	}

	/**
	 * @param tipAmount
	 *            the tipAmount to set
	 */
	public void setTipAmount(String tipAmount) {
		this.tipAmount = tipAmount;
	}

	public double getGrandTotalDouble(){
		return tipAmountDouble + billAmount;
	}

	public String getGrandTotal(){
		return (format.format(tipAmountDouble + billAmount) + "");
	}

}
