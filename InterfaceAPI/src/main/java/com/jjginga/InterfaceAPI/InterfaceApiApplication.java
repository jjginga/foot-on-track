package com.jjginga.InterfaceAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jjginga.InterfaceAPI.config", "com.jjginga.InterfaceAPI.authentication"})
@EnableWebFlux
public class InterfaceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterfaceApiApplication.class, args);
	}

}
