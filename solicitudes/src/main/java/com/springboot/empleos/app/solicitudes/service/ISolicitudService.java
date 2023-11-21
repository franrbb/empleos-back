package com.springboot.empleos.app.solicitudes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.empleos.app.solicitudes.entity.Solicitud;
import com.springboot.empleos.app.usuarios.entity.Usuario;
import com.springboot.empleos.app.vacantes.entity.Vacante;

public interface ISolicitudService {
	
	List<Solicitud> listasolicitudes();
	
	//Optional<Solicitud> findById(Long id);
	
	Solicitud save(Solicitud solicitud);
	
	void deleteById(Long id);
	
	Vacante findById(Long id);
	
	Usuario findByUsername(String nombre);

}
