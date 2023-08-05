package com.springboot.empleos.app.usuarios.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.springboot.empleos.app.usuarios.service.IUsuarioService;

@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService usuarioService;
	
	 @Autowired
     private AuthenticationConfiguration authenticationConfiguration;

     @Bean
     public static BCryptPasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
     }

     @Bean
     public AuthenticationManager authenticationManager() throws Exception {
         return authenticationConfiguration.getAuthenticationManager();
     }
     
     @Autowired
     public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(usuarioService)
        .passwordEncoder(passwordEncoder());
     }
   
   
     @Bean
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
              .anyRequest().authenticated()
              .and()
              .csrf().disable()
              .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        	return http.build();
     }
}
