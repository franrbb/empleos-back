package com.springboot.empleos.app.usuarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.empleos.app.usuarios.service.IUsuarioService;

@RestController
public class UsuarioController {
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@GetMapping()
	public ResponseEntity<?> listar(){
		
		return new ResponseEntity<>(usuarioService.findAll(), HttpStatus.OK);
		
	}

}
