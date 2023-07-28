package com.springboot.empleos.app.categorias.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Entity
@Table(name = "categorias")
@Data
public class Categoria implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(name = "NOMBRE")
	private String nombre;
	
	@NotEmpty
	@Column(name = "DESCRIPCION",length = 15000)
	private String descripcion;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
