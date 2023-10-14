package com.springboot.empleos.app.usuarios.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.empleos.app.usuarios.entity.Usuario;
import com.springboot.empleos.app.usuarios.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImpl implements IUsuarioService{
	
	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Override
	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	@Override
	public Optional<Usuario> findById(Long id) {
		return usuarioRepository.findById(id);
	}

	@Override
	public Usuario save(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	@Override
	public void delete(Long id) {
		usuarioRepository.deleteById(id);
	}

	@Transactional
	@Override
	public int bloquear(Long id) {
		return usuarioRepository.bloquear(id);
	}

	@Transactional
	@Override
	public int activar(Long id) {
		return usuarioRepository.desbloquear(id);
	}
}
