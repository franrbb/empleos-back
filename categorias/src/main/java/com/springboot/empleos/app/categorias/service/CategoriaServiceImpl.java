package com.springboot.empleos.app.categorias.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.empleos.app.categorias.entity.Categoria;
import com.springboot.empleos.app.categorias.repositoy.ICategoriaRepository;

@Service
public class CategoriaServiceImpl implements ICategoriaService {
	
	@Autowired
	private ICategoriaRepository categoriaRepository;

	@Override
	public List<Categoria> findAll() {
		return categoriaRepository.findAll();
	}

	@Override
	public Optional<Categoria> findById(Long id) {
		return categoriaRepository.findById(id);
	}

	@Override
	public Categoria save(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	@Override
	public void delete(Long id) {
		categoriaRepository.deleteById(id);
	}

}
