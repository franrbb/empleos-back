package com.springboot.empleos.app.categorias.service;

import java.util.List;

import com.springboot.empleos.app.categorias.entity.Categoria;

public interface ICategoriaService {
	
	List<Categoria> findAll();

}
