package com.springboot.empleos.app.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.empleos.app.usuarios.entity.Usuario;

@FeignClient("microservicio-usuarios")
public interface IUsuarioFeignClient {
	
	@GetMapping("/usuarios/search/buscar-username")
	Usuario findByUsername(@RequestParam String nombre);
	
	@PutMapping("/usuarios/{id}")
	Usuario update(@RequestBody Usuario usuario, @PathVariable Long id);

}
