package com.ms.app.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@RibbonClient(name = "microservicio-producto")
//@EnableCircuitBreaker //para Hystrix
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class SpringbootServicioItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioItemApplication.class, args);
	}	

}
