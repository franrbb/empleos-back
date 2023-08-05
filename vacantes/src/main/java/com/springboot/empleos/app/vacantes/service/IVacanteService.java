package com.springboot.empleos.app.vacantes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.empleos.app.vacantes.entity.Vacante;

public interface IVacanteService {
	
	List<Vacante> findAll();
	
	public Page<Vacante> findAll(Pageable pageable);
	
	List<Vacante> buscarPorEstatusAndDestacado(String estatus, int destacado);
	
	Optional<Vacante> findById(Long id);
	
	Vacante save(Vacante vacante);
	
	void delete(Long id);
}
