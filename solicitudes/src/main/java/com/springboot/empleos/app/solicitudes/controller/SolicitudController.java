package com.springboot.empleos.app.solicitudes.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.springboot.empleos.app.solicitudes.entity.Solicitud;
import com.springboot.empleos.app.solicitudes.service.ISolicitudService;
import com.springboot.empleos.app.usuarios.entity.Usuario;
import com.springboot.empleos.app.vacantes.entity.Vacante;

import lombok.extern.java.Log;

@RestController
@Log
public class SolicitudController {
	
	@Autowired
	private ISolicitudService solicitudService;
	
	@GetMapping
	public ResponseEntity<?> listaSolicitudes(){
		return new ResponseEntity<>(solicitudService.listasolicitudes(), HttpStatus.OK); 
	}
	
	/*@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id){
		Optional<Solicitud> solicitudOpt = solicitudService.findById(id);
		Map<String, Object> response = new HashMap<String, Object>();
		
		Solicitud solicitud = null;
		
		if(!solicitudOpt.isPresent()) {
			response.put("mensaje", "La solicitud con ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}else {
			try {
				solicitud = solicitudOpt.get();
			} catch (DataAccessException e) {
				response.put("mensaje", "Error al realizar la consulta en la base de datos.");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(solicitud, HttpStatus.OK);
	}*/
	
	@GetMapping("/vacante/{id}")
	public ResponseEntity<?> findVacanteById(@PathVariable Long id){
		Vacante vacante = solicitudService.findById(id);
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(!"aprobada".equalsIgnoreCase(vacante.getEstatus()) || vacante.getDestacado() != 1) {
			response.put("mensaje", "La vacante con ID: ".concat(id.toString().concat(" no esta aprobada.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(vacante, HttpStatus.OK);
	}
	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<?> findUsuarioById(@PathVariable Long id){
		Usuario usuario = solicitudService.findByIdUsuario(id);
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(usuario == null) {
			response.put("mensaje", "El usuario con ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(usuario, HttpStatus.OK);
		
	}
	
	@PostMapping("/{idVacante}")
	public ResponseEntity<?> save(@Valid @RequestBody Solicitud solicitud, BindingResult result, @PathVariable Long idVacante){
		
		Solicitud newSolicitud = null;
		
		Vacante vacanteSave = new Vacante();
		
		Long vacanteId = null;
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
										.stream()
										.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
										.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		/*ResponseEntity<Vacante> vacante = (ResponseEntity<Vacante>) findVacanteById(idVacante);
		vacanteId = vacante.getBody().getId();
		vacanteSave.setId(vacanteId);*/
		
		try {
			solicitud.setVacante(vacanteSave);
			newSolicitud = solicitudService.save(solicitud);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La solicitud ha sido creada con éxito");
		response.put("solicitud", newSolicitud);
		
		return new ResponseEntity<>(newSolicitud, HttpStatus.CREATED);
	}
	
	@PostMapping("/archivo/{idVacante}")
	public ResponseEntity<?> saveArchivo(@Valid Solicitud solicitud, BindingResult result, @PathVariable Long idVacante, @RequestParam("archivo") MultipartFile archivo){
		
		Solicitud newSolicitud = null;
		
		Vacante vacanteSave = new Vacante();
		
		Long vacanteId = null;
		
		String nombreArchivo = null;
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		/*if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
										.stream()
										.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
										.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}*/
		
		if(!archivo.isEmpty()) {
			nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
			Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
			
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir el documento " +nombreArchivo);
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String archivoAnterior = solicitud.getArchivo();
			
			if(archivoAnterior != null && archivoAnterior.length() > 0) {
				Path rutaArchivoAnterior = Paths.get("uploads").resolve(archivoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaArchivoAnterior.toFile();
				if(archivoFotoAnterior.exists() &&archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
		}
		
		ResponseEntity<Vacante> vacante = (ResponseEntity<Vacante>) findVacanteById(idVacante);
		vacanteId = vacante.getBody().getId();
		vacanteSave.setId(vacanteId);
		
		try {
			solicitud.setArchivo(nombreArchivo);
			solicitud.setVacante(vacanteSave);
			newSolicitud = solicitudService.save(solicitud);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La solicitud ha sido creada con éxito");
		response.put("solicitud", newSolicitud);
		
		return new ResponseEntity<>(newSolicitud, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Optional<Solicitud> solicitudOpt = solicitudService.findByIdSolicitud(id);
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(!solicitudOpt.isPresent()) {
			response.put("mensaje", "La solicitud con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}else {
			try {
				solicitudService.deleteById(id);
			}catch (DataAccessException e) {
				response.put("mensaje", "Error al eliminar la solicitud en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		response.put("mensaje", "La solicitud ha sido eliminada con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/*@GetMapping("/prueba")
	public ResponseEntity algunHandler(HttpServletRequest request, @RequestHeader (name="Authorization", required = false) String token) {
		String authTokenHeader = request.getHeader("Authorization");
		log.info("-----------------------------------------------------" +authTokenHeader);
		log.info(token);
		
		return null;
	}*/
	
}
