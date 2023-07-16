package com.springboot.empleos.app.vacantes.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.empleos.app.vacantes.entity.Vacante;
import com.springboot.empleos.app.vacantes.service.IVacanteService;

import lombok.extern.java.Log;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
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
	public ResponseEntity<?> findById(@PathVariable Long id){
		
		Optional<Vacante> vacanteOpt = null;
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			vacanteOpt = vacanteService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(!vacanteOpt.isPresent()) {
			response.put("mensaje", "La vacante con ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Vacante vacante = vacanteOpt.get();
		
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
		
		//return ResponseEntity.ok().body(vacanteService.buscarPorEstatusAndDestacado(status, destacado));
		return new ResponseEntity<>(vacanteService.findAll(), HttpStatus.OK);
		
	}
}
