package com.mastercard.mcbs.pbrc.offline.batch.util;

import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.COMMA;

public class StringUtil {

	private StringUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static String convertArrayToString(Object[] arr) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : arr)
			sb.append(obj.toString()).append(COMMA);
		return sb.substring(0, sb.length() - 1);
	}
}
