package com.senior.prova.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
//@EnableJpaRepositories(basePackages = {"com.senior.prova.application.repository.*"})
//@ComponentScan(basePackages = { "com.senior.prova.application.model.*" })
public class ProvaJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProvaJavaApplication.class, args);
		System.out.println("Esta é uma API de teste para Sênior Sistemas");
	}
}
