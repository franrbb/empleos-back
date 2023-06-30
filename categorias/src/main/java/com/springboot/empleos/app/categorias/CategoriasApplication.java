package com.springboot.empleos.app.categorias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class CategoriasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CategoriasApplication.class, args);
	}

}
