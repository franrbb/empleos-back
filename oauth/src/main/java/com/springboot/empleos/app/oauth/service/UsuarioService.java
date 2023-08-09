package com.springboot.empleos.app.oauth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.empleos.app.oauth.clients.IUsuarioFeignClient;
import com.springboot.empleos.app.usuarios.entity.Usuario;

@Service
public class UsuarioService implements  IUsuarioService, UserDetailsService{
	
	private final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);
	
	@Autowired
	private IUsuarioFeignClient client;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = client.findByUsername(username);
		
		if(usuario == null) {
			logger.error("Erron en el login, no existe el usuario " +username+ " en el sistema");
			throw new UsernameNotFoundException("Erron en el login, no existe el usuario " +username+ " en el sistema");
		}
		
		List<GrantedAuthority> authorities = usuario.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority -> logger.info("Role: " +authority.getAuthority()))
				.collect(Collectors.toList());
		
		logger.info("Usuario autenticado: " +username);
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

	@Override
	public Usuario findByUsername(String usename) {
		return client.findByUsername(usename);
	}

}
