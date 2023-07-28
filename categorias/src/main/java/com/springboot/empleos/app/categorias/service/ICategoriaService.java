package com.springboot.empleos.app.categorias.service;

import java.util.List;
import java.util.Optional;

import com.springboot.empleos.app.categorias.entity.Categoria;

public interface ICategoriaService {
	
	List<Categoria> findAll();
	
	Optional<Categoria> findById(Long id);
	
	Categoria save(Categoria categoria);
	
	void delete(Long id);

}
