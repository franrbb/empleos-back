package com.springboot.empleos.app.categorias.repositoy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.empleos.app.categorias.entity.Categoria;

public interface ICategoriaRepository extends JpaRepository<Categoria, Long>{

}
