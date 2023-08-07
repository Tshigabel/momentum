package com.momentum.investments.momentformgeneratorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@ComponentScan(basePackages = "com.momentum.investments.momentformgeneratorservice")
@EnableJpaRepositories(basePackages = {"com.momentum.investments.momentformgeneratorservice.repository"})
@EntityScan(basePackages = {"com.momentum.investments.momentformgeneratorservice.repository.entity"})
@SpringBootApplication
public class MomentFormGeneratorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MomentFormGeneratorServiceApplication.class, args);
	}

}
