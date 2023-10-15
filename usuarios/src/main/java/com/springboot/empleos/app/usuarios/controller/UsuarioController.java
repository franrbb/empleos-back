package com.springboot.empleos.app.usuarios.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.empleos.app.usuarios.entity.Role;
import com.springboot.empleos.app.usuarios.entity.Usuario;
import com.springboot.empleos.app.usuarios.service.IUsuarioService;

@RestController
public class UsuarioController {
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping()
	public ResponseEntity<?> listar(){
		
		return new ResponseEntity<>(usuarioService.findAll(), HttpStatus.OK);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id){
		Optional<Usuario> usuarioOpt = usuarioService.findById(id);
		Map<String, Object> response = new HashMap<String, Object>();
		
		Usuario usuario = null;
		
		if(!usuarioOpt.isPresent()) {
			response.put("mensaje", "El usuario con ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}else {
			try {
				usuario = usuarioOpt.get();
			} catch (DataAccessException e) {
				response.put("mensaje", "Error al realizar la consulta en la base de datos.");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(usuario, HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<?> save(@Valid @RequestBody Usuario usuario, BindingResult result){
		
		Usuario newUsuario = null;
		
		/*List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setId(1L);
		role.setNombre("ROLE_USER");
		roles.add(role);*/
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
										.stream()
										.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
										.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			String passwordBcrypt = passwordEncoder.encode(usuario.getPassword());
			usuario.setPassword(passwordBcrypt);
			usuario.setEnabled(true);
			//usuario.setRoles(roles);
			newUsuario = usuarioService.save(usuario);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El usuario ha sido creado con éxito");
		response.put("usuario", newUsuario);
		
		return new ResponseEntity<>(newUsuario, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Optional<Usuario> usuarioOpt = usuarioService.findById(id);
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(!usuarioOpt.isPresent()) {
			response.put("mensaje", "El usuario con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}else {
			try {
				usuarioService.delete(id);
			}catch (DataAccessException e) {
				response.put("mensaje", "Error al eliminar el usuario en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		response.put("mensaje", "El usuario ha sido eliminado con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/lock/{id}")
	public ResponseEntity<?> bloquear(@PathVariable("id") Long id) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		usuarioService.bloquear(id);
		
		response.put("mensaje", "El usuario ha sido bloqueado con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
    @GetMapping("/unlock/{id}")
    public ResponseEntity<?> desbloquear(@PathVariable Long id) {
    	
    	Map<String, Object> response = new HashMap<String, Object>();
    	usuarioService.activar(id);
    	
    	response.put("mensaje", "El usuario ha sido desbloqueado con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
    
}
