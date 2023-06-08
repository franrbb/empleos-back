package com.springboot.empleos.app.vacantes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.empleos.app.vacantes.entity.Vacante;

@Repository
public interface IVacanteRespository extends JpaRepository<Vacante, Long>{
	
	public List<Vacante> findByEstatusAndDestacado(String estatus, int destacado);

}
