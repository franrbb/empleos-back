package com.springboot.empleos.app.vacantes.service;

import java.util.List;

import com.springboot.empleos.app.vacantes.entity.Vacante;

public interface IVacanteService {
	
	List<Vacante> buscarPorEstatusAndDestacado(String estatus, int destacado);

}
