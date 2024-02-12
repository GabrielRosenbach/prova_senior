package com.senior.application.exceptions.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidStateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidStateException() {
        super("Ocorreu um erro inesperado no servidor!");
	}
	
	public InvalidStateException(String message) {
        super(message);
	}

}