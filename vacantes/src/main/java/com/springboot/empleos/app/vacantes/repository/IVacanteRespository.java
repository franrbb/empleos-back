package com.springboot.empleos.app.vacantes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.empleos.app.vacantes.entity.Vacante;

@Repository
public interface IVacanteRespository extends JpaRepository<Vacante, Long>{
	
	public List<Vacante> findByEstatusAndDestacado(String estatus, int destacado);
	
	@Query("select v from Vacante v where v.nombre like %?1% or v.categoria.nombre like %?1%")
	//@Query("select v from Vacante v where v.nombre like %?1%")
	public List<Vacante> findByNombre(String term);

}
