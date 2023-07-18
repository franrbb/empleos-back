package com.springboot.empleos.app.categorias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.empleos.app.categorias.service.ICategoriaService;

@RestController
//@CrossOrigin(origins = {"http://localhost:4200"})
public class CategoriaController {
	
	@Autowired
	private ICategoriaService categoriaService;
	
	@GetMapping()
	public ResponseEntity<?> listarCategorias(){
		
		//return ResponseEntity.ok().body(categoriaService.findAll());
		return new ResponseEntity<>(categoriaService.findAll(), HttpStatus.OK);
		
	}

}
