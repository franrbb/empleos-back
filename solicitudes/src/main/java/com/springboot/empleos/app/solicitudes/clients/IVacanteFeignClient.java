package com.springboot.empleos.app.solicitudes.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.springboot.empleos.app.vacantes.entity.Vacante;

@FeignClient(name = "microservicio-vacantes")
public interface IVacanteFeignClient {
	
	@GetMapping("/{id}")
	Vacante findById(@PathVariable Long id);

}
