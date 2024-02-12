package com.senior.application.util;

import java.util.Optional;

import com.senior.application.exceptions.http.NotFoundException;

/**
 * Classe utilitária para tratar retornos Optional do repository
*/
public class OptionalUtil {

	public static <T> T tratarOptional(Optional<T> opt, String message) {
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new NotFoundException(message);
	}
}
