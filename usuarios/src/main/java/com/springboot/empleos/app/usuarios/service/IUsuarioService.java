package com.springboot.empleos.app.usuarios.service;

import com.springboot.empleos.app.usuarios.entity.Usuario;

public interface IUsuarioService {
	
	public Usuario findByUsername(String username);

}
