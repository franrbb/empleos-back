package com.springboot.empleos.app.usuarios.service;

import java.util.List;

import com.springboot.empleos.app.usuarios.entity.Usuario;

public interface IUsuarioService {
	
	List<Usuario> findAll();

}
