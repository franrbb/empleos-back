package com.springboot.empleos.app.usuarios.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, length = 20)
	private String username;
	
	private String nombre;
	
	@Column(unique = true)
	private String email;

	@Column(length = 60)
	private String password;

	private Boolean enabled;
	
	@Column(name = "fecha_registro")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaRegistro;
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "usuarios_roles", 
	joinColumns = @JoinColumn(name = "usuario_id"), 
	inverseJoinColumns = @JoinColumn(name = "role_id"),
	uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario_id", "role_id"})})
	private List<Role> roles;
	
	private Integer intentos;
	
	
	@PrePersist
	public void prePersist() {
		fechaRegistro = new Date();
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
