package com.springboot.empleos.app.solicitudes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.empleos.app.solicitudes.entity.Solicitud;

public interface ISolicitudRepository extends JpaRepository<Solicitud, Long>{

}
