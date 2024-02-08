package com.senior.prova.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class ProvaJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProvaJavaApplication.class, args);
		System.out.println("Esta é uma API de teste para Sênior Sistemas");
	}
}
