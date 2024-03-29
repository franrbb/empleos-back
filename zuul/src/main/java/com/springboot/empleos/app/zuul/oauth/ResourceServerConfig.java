package com.springboot.empleos.app.zuul.oauth;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/api/security/oauth/**").permitAll()
		.antMatchers(HttpMethod.GET, "/api/vacantes/home/{status}/{destacado}", "/api/vacantes/home/verDetalle/{id}", "/api/vacantes/img/**", "/api/vacantes/filtrar/{term}").permitAll()
		.antMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
		.antMatchers(HttpMethod.GET, "/api/vacantes", "/api/vacantes/{id}","/api/vacantes/page/**",
				"/api/categorias", "/api/categorias{id}", 
				"/api/usuarios", "/api/usuarios/{id}, /api/usuarios/**", "/api/solicitudes").hasRole("ADMIN")
		.antMatchers(HttpMethod.GET, "/api/solicitudes/**").hasAnyRole("ADMIN", "USER")
		.antMatchers(HttpMethod.POST, "/api/solicitudes/**").hasAnyRole("ADMIN", "USER")
		.antMatchers(HttpMethod.POST, "/api/vacantes", "/api/categorias").hasRole("ADMIN")
		.antMatchers(HttpMethod.POST, "/api/solicitudes").hasAnyRole("ADMIN", "USER")
		.antMatchers(HttpMethod.PUT, "/api/vacantes/{id}", "/api/categorias/{id}", "/api/usuarios/{id}").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE, "/api/vacantes/{id}", "/api/categorias{id}", "/api/usuarios/{id}").hasRole("ADMIN")
		.anyRequest().authenticated();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	/**
	 * Método que se encarga de almanecar datos de autenticación(user, roles...),
	 * traducir datos como el token
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey("algun_codigo_secreto_aeiou");
		return tokenConverter;
	}

}
