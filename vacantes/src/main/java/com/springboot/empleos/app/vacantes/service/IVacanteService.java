package com.springboot.empleos.app.vacantes.service;

import java.util.List;
import java.util.Optional;

import com.springboot.empleos.app.vacantes.entity.Vacante;

public interface IVacanteService {
	
	List<Vacante> findAll();
	
	List<Vacante> buscarPorEstatusAndDestacado(String estatus, int destacado);
	
	Optional<Vacante> findById(Long id);
	
	Vacante save(Vacante vacante);
	
	void delete(Long id);
}
