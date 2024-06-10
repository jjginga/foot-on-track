package com.jjginga.TrainingPlan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableDiscoveryClient
@ComponentScan({"com.common","com.jjginga.TrainingPlan"})
public class TrainningPlanApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainningPlanApplication.class, args);
	}

}
