package com.springboot.empleos.app.solicitudes.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.springboot.empleos.app.usuarios.entity.Usuario;
import com.springboot.empleos.app.vacantes.entity.Vacante;

import lombok.Data;

@Entity
@Table(name = "solicitudes")
@Data
public class Solicitud implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment MySQL
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fecha;
	
	private String comentarios;
	
	private String archivo;
	
	@OneToOne
	@JoinColumn(name = "idVacante")
	private Vacante vacante;

	@OneToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;
	
	@PrePersist
	public void prePersist() {
		this.fecha = new Date();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
