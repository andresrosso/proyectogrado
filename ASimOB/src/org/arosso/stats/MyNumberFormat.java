package org.arosso.stats;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * 
 * @author prachi
 */
class MyNumberFormat extends NumberFormat {

	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
		if (number >= 0)
			return toAppendTo.append(number);
		else
			return toAppendTo.append("");
	}

	@Override
	public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
		if (number >= 0)
			return toAppendTo.append(number);
		else
			return toAppendTo.append("");
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		throw new UnsupportedOperationException();
	}
}
