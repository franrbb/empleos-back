package com.springboot.empleos.app.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.springboot.empleos.app.usuarios.entity.Usuario;

@RepositoryRestResource(path = "usuarios")
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{
	
	@RestResource(path="buscar-username")
	public Usuario findByUsername(@Param("nombre") String username);

}
