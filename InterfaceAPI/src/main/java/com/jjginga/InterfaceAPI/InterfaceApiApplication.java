package com.jjginga.InterfaceAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InterfaceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterfaceApiApplication.class, args);
	}

}
