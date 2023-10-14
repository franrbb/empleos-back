package com.springboot.empleos.app.usuarios.service;

import java.util.List;
import java.util.Optional;

import com.springboot.empleos.app.usuarios.entity.Usuario;

public interface IUsuarioService {

	List<Usuario> findAll();

	Optional<Usuario> findById(Long id);

	Usuario save(Usuario usuario);

	void delete(Long id);
	
	int bloquear(Long id);
	
	int activar(Long id);

}
