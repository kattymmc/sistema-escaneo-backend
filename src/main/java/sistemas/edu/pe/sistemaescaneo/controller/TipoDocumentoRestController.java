package sistemas.edu.pe.sistemaescaneo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import sistemas.edu.pe.sistemaescaneo.entity.TipoDocumento;
import sistemas.edu.pe.sistemaescaneo.service.ITipoDocumentoService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class TipoDocumentoRestController {
	
	@Autowired
	private ITipoDocumentoService tipoDocumentoService;

	@GetMapping("/tipo-documento")
	public List<TipoDocumento> listarTipoDocumento(){
		return tipoDocumentoService.findAll();
	}
	
	@GetMapping("/tipo-documento/{id}")
	public ResponseEntity<?> listarTipoDocumentoPorId(@PathVariable Long id){
		
		TipoDocumento tipoDocumento = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			tipoDocumento = tipoDocumentoService.findById(id);
		}  catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la Base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(tipoDocumento == null) {
			response.put("mensaje", "El tipo de documento con ID: ".concat(id.toString().concat(" no existe en la BD")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<TipoDocumento>(tipoDocumento, HttpStatus.OK);
	}
	
	@PostMapping("/tipo-documento")
	public ResponseEntity<?> crearTipoDocumento(@Valid @RequestBody TipoDocumento documento, BindingResult result) {
		TipoDocumento tipoDocumentoNuevo = null;
		Map<String, Object> response = new HashMap<>();

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
			tipoDocumentoNuevo = tipoDocumentoService.save(documento);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar al insertar en la Base de Datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El documento ha sido creado con éxito");
		response.put("tipoDocumento", tipoDocumentoNuevo);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/tipo-documento/{id}")
	public ResponseEntity<?> actualizarTipoDocumento(@Valid @RequestBody TipoDocumento tipoDocumento, BindingResult result, @PathVariable Long id) {
		TipoDocumento tipoDocumentoAnterior = tipoDocumentoService.findById(id);
		TipoDocumento tipoDocumentoActualizado = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(tipoDocumento == null) {
			response.put("mensaje", "Error: no se puedo editar, El tipo de documento con ID ".concat(id.toString().concat(" no existe en la BD")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			tipoDocumentoAnterior.setDescripcion(tipoDocumento.getDescripcion());
			tipoDocumentoActualizado = tipoDocumentoService.save(tipoDocumentoAnterior);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la actualizacion en la BD");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El tipo de documento ha sido actualizado con éxito");
		response.put("tipoDocumento", tipoDocumentoActualizado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/tipo-documento/{id}")
	public ResponseEntity<?> eliminarTipoDocumento(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			tipoDocumentoService.delete(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la eliminación en la BD");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El tipo de documento ha sido eliminado con éxito");
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
