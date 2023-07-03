package com.springboot.empleos.app.vacantes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.empleos.app.vacantes.entity.Vacante;
import com.springboot.empleos.app.vacantes.repository.IVacanteRespository;

@Service
public class VacanteService implements IVacanteService{
	
	@Autowired
	private IVacanteRespository vacanteRepository;

	@Override
	public List<Vacante> buscarPorEstatusAndDestacado(String estatus, int destacado) {
		// TODO Auto-generated method stub
		return vacanteRepository.findByEstatusAndDestacado(estatus, destacado);
	}

	@Override
	public Optional<Vacante> findById(Long id) {
		// TODO Auto-generated method stub
		return vacanteRepository.findById(id);
	}

}
