package com.senior.prova.application.exceptions.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InternalErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InternalErrorException() {
        super("Algo na requisição não está de acordo com o esperado pelo servidor!");
	}
	
	public InternalErrorException(String message) {
        super(message);
	}

}