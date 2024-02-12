package com.senior.application.util;

/**
 * Classe utilitária para validar valores Integer
 */
public class IntegerUtil {

	public static Boolean isNullOrZero(Integer value) {
		return value == null || value.intValue() == 0;
	}
}
