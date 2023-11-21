package com.springboot.empleos.app.solicitudes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.empleos.app.solicitudes.clients.IUsuarioFeignClient;
import com.springboot.empleos.app.solicitudes.clients.IVacanteFeignClient;
import com.springboot.empleos.app.solicitudes.entity.Solicitud;
import com.springboot.empleos.app.solicitudes.repository.ISolicitudRepository;
import com.springboot.empleos.app.usuarios.entity.Usuario;
import com.springboot.empleos.app.vacantes.entity.Vacante;

@Service
public class SolicitudServiceImpl implements ISolicitudService{
	
	@Autowired
	private ISolicitudRepository solicitudRepository;
	
	@Autowired
	private IVacanteFeignClient clientVacante;
	
	@Autowired
	private IUsuarioFeignClient clientUsuario;

	@Override
	public List<Solicitud> listasolicitudes() {
		return solicitudRepository.findAll();
	}

	/*@Override
	public Optional<Solicitud> findById(Long id) {
		return solicitudRepository.findById(id);
	}*/

	@Override
	public Solicitud save(Solicitud solicitud) {
		return solicitudRepository.save(solicitud);
	}

	@Override
	public void deleteById(Long id) {
		solicitudRepository.deleteById(id);
	}

	@Override
	public Vacante findById(Long id) {
		return clientVacante.findById(id);
	}

	@Override
	public Usuario findByUsername(String nombre) {
		return clientUsuario.findByUsername(nombre);
	}

}
