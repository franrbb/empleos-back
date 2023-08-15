package com.springboot.empleos.app.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
	
	@Bean
	public SecurityWebFilterChain configure(ServerHttpSecurity http) {
		return http.authorizeExchange()
				.pathMatchers("/api/security/oauth/**").permitAll()
				.pathMatchers(HttpMethod.GET, "/api/vacantes/home/{status}/{destacado}", "/api/vacantes/home/verDetalle/{id}").permitAll()
				.pathMatchers(HttpMethod.GET, "/api/vacantes", "/api/vacantes/{id}", 
						"/api/categorias", "/api/categorias{id}", 
						"/api/usuarios/usuarios", "/api/usuarios/usuarios{id}").hasRole("ADMIN")
				.pathMatchers(HttpMethod.POST, "/api/vacantes", "/api/categorias", "/api/usuarios/usuarios").hasRole("ADMIN")
				.pathMatchers(HttpMethod.PUT, "/api/vacantes/{id}", "/api/categorias/{id}", "/api/usuarios/usuarios/{id}").hasRole("ADMIN")
				.pathMatchers(HttpMethod.DELETE, "/api/vacantes/{id}", "/api/categorias{id}", "/api/usuarios/usuarios/{id}").hasRole("ADMIN")
				.anyExchange().authenticated()
				.and().addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.csrf().disable()
				.build();
	}

}
