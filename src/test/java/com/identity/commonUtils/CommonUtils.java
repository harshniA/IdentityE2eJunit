
package com.identity.commonUtils;

public class CommonUtils {
	private static int shortTime = 8;
	private static int veryShortTime = 4;

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static int getShortTime() {
		return shortTime;
	}

	public static int getVeryShortTime() {
		return veryShortTime;
	}


}