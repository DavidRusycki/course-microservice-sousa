package com.msclientes3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Msclientes3Application {

	@RequestMapping(value = "/")
	public String home() {
		return "Eureka Client microservice";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Msclientes3Application.class, args);
	}

}
