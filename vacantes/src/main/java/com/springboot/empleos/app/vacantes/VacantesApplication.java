package com.springboot.empleos.app.vacantes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@EntityScan({"com.springboot.empleos.app.vacantes.entity",
"com.springboot.empleos.app.categorias.entity"})
public class VacantesApplication {

	public static void main(String[] args) {
		SpringApplication.run(VacantesApplication.class, args);
	}

}
