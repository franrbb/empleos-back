package com.springboot.empleos.app.vacantes.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
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

import com.springboot.empleos.app.vacantes.entity.Vacante;
import com.springboot.empleos.app.vacantes.service.IVacanteService;

import lombok.extern.java.Log;

@RestController
@Log
public class VacanteController {
	
	@Autowired
	IVacanteService vacanteService;
	
	@GetMapping("/home/{status}/{destacado}")
	public ResponseEntity<?> buscarPorEstatusAndDestacado(@PathVariable String status, @PathVariable Integer destacado){
		
		//return ResponseEntity.ok().body(vacanteService.buscarPorEstatusAndDestacado(status, destacado));
		return new ResponseEntity<>(vacanteService.buscarPorEstatusAndDestacado(status, destacado), HttpStatus.OK);
		
	}
	
	@GetMapping("/home/verDetalle/{id}")
	public ResponseEntity<?> verDetalle(@PathVariable Long id){
		Optional<Vacante> vacanteOpt = vacanteService.findById(id);
		Map<String, Object> response = new HashMap<String, Object>();
		
		Vacante vacante = null;
		
		if(!vacanteOpt.isPresent()) {
			response.put("mensaje", "La vacante con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}else {
			try {
				vacante = vacanteOpt.get();
			} catch (DataAccessException e) {
				response.put("mensaje", "Error al realizar la consulta en la base de datos.");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		if(!"aprobada".equalsIgnoreCase(vacante.getEstatus()) || vacante.getDestacado() != 1) {
			response.put("mensaje", "La vacante con ID: ".concat(id.toString().concat(" no esta aprobada.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(vacante, HttpStatus.OK);
	}
	
	/** 
	 * Método para mostrar imágenes 
	 * */
	@GetMapping("/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
		
		Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
		log.info(rutaArchivo.toString());
		Resource recurso = null;
		
		try {
			recurso =  new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {

		}
		
		if(!recurso.exists() && !recurso.isReadable()) {
			throw new RuntimeException("Error no se pudo cargar la imagen " +nombreFoto);
		}
		
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attchment; filename=\"" +recurso.getFilename() + "\"");
		
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
		
	}
	
	@GetMapping()
	public ResponseEntity<?> findAll(){
		
		return new ResponseEntity<>(vacanteService.findAll(), HttpStatus.OK);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id){
		Optional<Vacante> vacanteOpt = vacanteService.findById(id);
		Map<String, Object> response = new HashMap<String, Object>();
		
		Vacante vacante = null;
		
		if(!vacanteOpt.isPresent()) {
			response.put("mensaje", "La vacante con ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}else {
			try {
				vacante = vacanteOpt.get();
			} catch (DataAccessException e) {
				response.put("mensaje", "Error al realizar la consulta en la base de datos.");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(vacante, HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<?> save(@Valid @RequestBody Vacante vacante, BindingResult result){
		
		Vacante newVacante = null;
		
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
			newVacante = vacanteService.save(vacante);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La vacante ha sido creada con éxito");
		response.put("vacante", newVacante);
		
		return new ResponseEntity<>(newVacante, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Vacante vacante, BindingResult result, @PathVariable Long id) {
		Optional<Vacante> vacanteOpt = vacanteService.findById(id);
		Vacante vacanteUpload = null;
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
										.stream()
										.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
										.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(!vacanteOpt.isPresent()) {
			response.put("mensaje", "La vacante con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}else {
			try {
				Vacante vacanteActual = vacanteOpt.get();
				vacanteActual.setNombre(vacante.getNombre());
				vacanteActual.setDescripcion(vacante.getDescripcion());
				vacanteActual.setSalario(vacante.getSalario());
		        vacanteActual.setEstatus(vacante.getEstatus());
				vacanteActual.setDestacado(vacante.getDestacado());
				vacanteActual.setImagen(vacante.getImagen());
				vacanteActual.setDetalles(vacante.getDetalles());
				vacanteActual.setCategoria(vacante.getCategoria());
				
				vacanteUpload = vacanteService.save(vacanteActual);
			}catch (DataAccessException e) {
				response.put("mensaje", "Error al actualizar la vacante en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		response.put("mensaje", "La vacante ha sido actualizada con éxito");
		response.put("vacante", vacanteUpload);
		
		return new ResponseEntity<>(vacanteUpload, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Optional<Vacante> vacanteOpt = vacanteService.findById(id);
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(!vacanteOpt.isPresent()) {
			response.put("mensaje", "La vacante con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}else {
			try {
				vacanteService.delete(id);
			}catch (DataAccessException e) {
				response.put("mensaje", "Error al eliminar la vacante en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		response.put("mensaje", "La vacante ha sido eliminada con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
		
}
