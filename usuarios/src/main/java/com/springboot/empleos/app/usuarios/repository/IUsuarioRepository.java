package com.springboot.empleos.app.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.empleos.app.usuarios.entity.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{
	
	public Usuario findByUsername(String username);
	
	@Query("Select u from Usuario u where u.username=?1")
	public Usuario findByUsername2(String username);

}
