package com.springboot.empleos.app.vacantes.entity;

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

import com.springboot.empleos.app.categorias.entity.Categoria;

import lombok.Data;

@Entity
@Table(name = "vacantes")
@Data
public class Vacante implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	private String descripcion;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fecha;
	
	private Double salario;
	
	private Integer destacado;
	
	private String imagen;
	
	private String estatus;
	
	private String detalles;
	
	@OneToOne
	@JoinColumn(name = "idCategoria")
	private Categoria categoria;
	
	@PrePersist
	public void prePersist() {
		this.fecha = new Date();
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
