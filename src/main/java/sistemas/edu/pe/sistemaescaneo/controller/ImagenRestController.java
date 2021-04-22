package sistemas.edu.pe.sistemaescaneo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import sistemas.edu.pe.sistemaescaneo.entity.Documento;
import sistemas.edu.pe.sistemaescaneo.entity.Imagen;
import sistemas.edu.pe.sistemaescaneo.service.IDocumentoService;
import sistemas.edu.pe.sistemaescaneo.service.IImagenService;
import sistemas.edu.pe.sistemaescaneo.service.IUploadFileService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class ImagenRestController {
	
	@Autowired
	private IImagenService imagenService;
	
	@Autowired
	private IDocumentoService documentoService;
	
	@Autowired
	private IUploadFileService uploadService;
	
	// Cargando imagenes de un documento
	@PostMapping("/documentos/upload")
	public  ResponseEntity<?> upload(@RequestParam("imagenes") MultipartFile[] imagenes, @RequestParam("id") Long id){
		
		Map<String, Object> response = new HashMap<>();
		Documento documento = documentoService.findById(id);
		
		List<Imagen> imagenesLista = new ArrayList<Imagen>();
			
		for (MultipartFile imagen : imagenes) {
			String nombreImagen  = null;
			Imagen imagenNueva = new Imagen();
			try {
				nombreImagen = uploadService.copiar(imagen);
				imagenNueva.setNombre(nombreImagen);
				imagenNueva.setDocumento(documento);
				imagenesLista.add(imagenNueva);
			} catch(IOException e) {
				response.put("mensaje", "Error al subir la imagen del documento: "+ nombreImagen);
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
	
		documento.setImagenes(imagenesLista);
		documentoService.save(documento);

		response.put("documento", documento);
		response.put("mensaje", "Has subido correctamente las imagenes");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/documentos/upload/{id}")
	public  ResponseEntity<?> update(@RequestParam("imagen") MultipartFile imagen, @PathVariable("id") Long id){
		
		Map<String, Object> response = new HashMap<>();
		Imagen imagenAnterior = imagenService.findById(id);
		
		String nombreImagen;
		Imagen imagenActualizada = null;
		try {
			nombreImagen = uploadService.copiar(imagen);
			imagenAnterior.setNombre(nombreImagen);
			imagenActualizada = imagenService.save(imagenAnterior);
		} catch(IOException e) {
				response.put("mensaje", "Error al actualizar la imagen: "+ imagenActualizada);
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		response.put("mensaje", "Has subido correctamente las imagenes");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	// Obteniendo una imagen segun su nombre
	@GetMapping("/uploads/img/{nombreImagen:.+}")
	public ResponseEntity<Resource> verImagen(@PathVariable String nombreImagen){
		Resource recurso = null;
		
		try {
			recurso = uploadService.cargar(nombreImagen);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename()+ "\"");
		
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);

	}
	
	@DeleteMapping("/uploads/img/{id}")
	public ResponseEntity<?> eliminarImagen(@PathVariable Long id){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			Imagen image = imagenService.findById(id);
			imagenService.delete(id);
			uploadService.eliminar(image.getNombre());
		} catch (DataAccessException e) {
			e.printStackTrace();
			response.put("mensaje", "Error al realizar la eliminación en la BD");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El documento ha sido eliminado con éxito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	

}
