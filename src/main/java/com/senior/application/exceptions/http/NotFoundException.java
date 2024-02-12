package com.senior.application.exceptions.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotFoundException() {
        super("A entidade buscada não foi encontrada!");
	}
	
	public NotFoundException(String message) {
        super(message);
	}

}