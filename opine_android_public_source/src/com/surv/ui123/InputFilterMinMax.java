package com.surv.ui123;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMinMax implements InputFilter {

	private int min, max;

	public InputFilterMinMax(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public InputFilterMinMax(String min, String max) {
		this.min = Integer.parseInt(min);
		this.max = Integer.parseInt(max);
	}

	private boolean isInRange(int a, int b, int c) {
		return b > a ? c >= a && c <= b : c >= b && c <= a;
	}

	@Override
	public CharSequence filter(CharSequence arg0, int arg1, int arg2,
			Spanned arg3, int arg4, int arg5) {
		try {
			int input = Integer.parseInt(arg3.toString() + arg0.toString());
			if (isInRange(min, max, input))
				return null;
		} catch (NumberFormatException nfe) {
		}
		return "";
	}
}