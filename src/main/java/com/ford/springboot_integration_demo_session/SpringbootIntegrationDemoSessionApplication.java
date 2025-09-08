package com.ford.springboot_integration_demo_session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringbootIntegrationDemoSessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootIntegrationDemoSessionApplication.class, args);
	}

}
