package com.senior.prova.application.util;

import java.util.Optional;

import com.senior.prova.application.exceptions.http.NotFoundException;

public class OptionalUtil {

	public static <T> T tratarOptional(Optional<T> opt, String message) {
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new NotFoundException(message);
	}
}
