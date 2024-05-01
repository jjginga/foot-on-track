package com.jjginga.TrainingPlan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TrainningPlanApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainningPlanApplication.class, args);
	}

}
