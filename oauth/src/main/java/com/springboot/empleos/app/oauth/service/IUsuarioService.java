package com.springboot.empleos.app.oauth.service;

import com.springboot.empleos.app.usuarios.entity.Usuario;

public interface IUsuarioService {
	
	Usuario findByUsername(String usename);
	
	Usuario update(Usuario usuario, Long id);

}
