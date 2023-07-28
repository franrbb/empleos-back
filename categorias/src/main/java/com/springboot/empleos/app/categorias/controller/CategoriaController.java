package com.springboot.empleos.app.categorias.controller;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.empleos.app.categorias.entity.Categoria;
import com.springboot.empleos.app.categorias.service.ICategoriaService;

@RestController
public class CategoriaController {
	
	@Autowired
	private ICategoriaService categoriaService;
	
	@GetMapping()
	public ResponseEntity<?> listarCategorias(){
		
		return new ResponseEntity<>(categoriaService.findAll(), HttpStatus.OK);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id){
		Optional<Categoria> categoriaOpt = categoriaService.findById(id);
		Map<String, Object> response = new HashMap<String, Object>();
		
		Categoria categoria = null;
		
		if(!categoriaOpt.isPresent()) {
			response.put("mensaje", "La categoria con ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}else {
			try {
				categoria = categoriaOpt.get();
			} catch (DataAccessException e) {
				response.put("mensaje", "Error al realizar la consulta en la base de datos.");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(categoria, HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<?> save(@Valid @RequestBody Categoria categoria, BindingResult result){
		
		Categoria newCategoria = null;
		
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
			newCategoria = categoriaService.save(categoria);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La categoria ha sido creada con éxito");
		response.put("categoria", newCategoria);
		
		return new ResponseEntity<>(newCategoria, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Categoria categoria, BindingResult result, @PathVariable Long id) {
		Optional<Categoria> categoriaOpt = categoriaService.findById(id);
		Categoria categoriaUpload = null;
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
										.stream()
										.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
										.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(!categoriaOpt.isPresent()) {
			response.put("mensaje", "La categoria con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}else {
			try {
				Categoria categoriaActual = categoriaOpt.get();
				categoriaActual.setNombre(categoria.getNombre());
				categoriaActual.setDescripcion(categoria.getDescripcion());
				
				categoriaUpload = categoriaService.save(categoriaActual);
			}catch (DataAccessException e) {
				response.put("mensaje", "Error al actualizar la categoria en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		response.put("mensaje", "La categoria ha sido actualizada con éxito");
		response.put("categoria", categoriaUpload);
		
		return new ResponseEntity<>(categoriaUpload, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Optional<Categoria> categoriaOpt = categoriaService.findById(id);
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(!categoriaOpt.isPresent()) {
			response.put("mensaje", "La categoria con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}else {
			try {
				categoriaService.delete(id);
			}catch (DataAccessException e) {
				response.put("mensaje", "Error al eliminar la categoria en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		response.put("mensaje", "La categoria ha sido eliminada con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
