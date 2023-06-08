package com.springboot.empleos.app.vacantes.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

}
