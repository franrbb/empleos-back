package com.springboot.empleos.app.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.empleos.app.usuarios.entity.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{

}
