package com.desafio.revendaservice;

import org.springframework.boot.SpringApplication;

public class TestRevendaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(RevendaServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
