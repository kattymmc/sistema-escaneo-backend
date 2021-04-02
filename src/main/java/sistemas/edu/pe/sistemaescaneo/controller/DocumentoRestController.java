package sistemas.edu.pe.sistemaescaneo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import sistemas.edu.pe.sistemaescaneo.entity.Documento;
import sistemas.edu.pe.sistemaescaneo.entity.Imagen;
import sistemas.edu.pe.sistemaescaneo.service.IDocumentoService;
import sistemas.edu.pe.sistemaescaneo.service.IImagenService;
import sistemas.edu.pe.sistemaescaneo.service.IUploadFileService;

@CrossOrigin(origins={"http://localhost:4200"}) 
@RestController
@RequestMapping("/api")
public class DocumentoRestController {

	@Autowired
	private IDocumentoService documentoService;
	
	@Autowired
	private IImagenService imagenService;
	
	@Autowired
	private IUploadFileService uploadService;
	
	@GetMapping("/documentos")
	public List<Documento> listarDocumentos(){
		return documentoService.findAll();
	}
	
	@GetMapping("/documentos/{id}")
	public ResponseEntity<?> listarDocumentoPorId(@PathVariable Long id){
		
		Documento documento = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			documento = documentoService.findById(id);
		}  catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la Base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(documento == null) {
			response.put("mensaje", "El documento con ID: ".concat(id.toString().concat(" no existe en la BD")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Documento>(documento, HttpStatus.OK);
	}
	
	@PostMapping("/documentos")
	public ResponseEntity<?> crearDocumento(@Valid @RequestBody Documento documento, BindingResult result) {
		Documento documentoNuevo = null;
		Map<String, Object> response = new HashMap<>();
		
		System.out.println(documento.getTipoDocumento());

		// Manejando errores añadidas en la entidad
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
				
		try {
			documentoNuevo = documentoService.save(documento);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar al insertar en la Base de Datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El documento ha sido creado con éxito");
		response.put("documento", documentoNuevo);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/documentos/{id}")
	public ResponseEntity<?> actualizarDocumento(@Valid @RequestBody Documento documento, BindingResult result, @PathVariable Long id) {
		Documento documentoAnterior = documentoService.findById(id);
		Documento documentoActualizado = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(documento == null) {
			response.put("mensaje", "Error: no se puedo editar, El documento con ID ".concat(id.toString().concat(" no existe en la BD")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			documentoAnterior.setCodigoDoc(documento.getCodigoDoc());
			documentoAnterior.setAnaquel(documento.getAnaquel());
			documentoAnterior.setColumna(documento.getColumna());
			documentoAnterior.setTipoDocumento(documento.getTipoDocumento());
			documentoActualizado = documentoService.save(documentoAnterior);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la actualizacion en la BD");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El documento ha sido actualizado con éxito");
		response.put("documento", documentoActualizado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/documentos/{id}")
	public ResponseEntity<?> eliminarDocumento(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			Documento documento = documentoService.findById(id);
			
			// Eliminando imagenes del documento
			List<Imagen> imagenes = documento.getImagenes();
			for(Imagen imagen: imagenes) {
				uploadService.eliminar(imagen.getNombre());
			}
			
			// Eliminando documento
			documentoService.delete(id);
			
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la eliminación en la BD");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El documento ha sido eliminado con éxito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@GetMapping("documentos/filtrar/{term}")
	public List<Documento> filtrarPorCodigoDoc(@PathVariable String term){
		return documentoService.findDocumentoByCodigoDoc(term);
	}
	
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
	
	// Manejando errores
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
		
		Map<String, Object> response = new HashMap<>();

		response.put("message", e.getName().concat(" deberia ser ").concat(e.getRequiredType().getSimpleName()));
		response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
	    
	    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

	}
}
