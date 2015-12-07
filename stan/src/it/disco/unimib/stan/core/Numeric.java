package it.disco.unimib.stan.core;

import java.util.regex.Pattern;

public class Numeric {

	private String string;
	private static final Pattern numberPattern = Pattern.compile("^-?\\d+(\\.?\\d+)?$");

	public Numeric(String string) {
		this.string = string.replace(",", ".");
	}

	public Double asDouble() {
		return Double.parseDouble(this.string);
	}

	public boolean isNumeric() {
	    return this.string != null && numberPattern.matcher(this.string).matches();
	}
}
