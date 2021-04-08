package sistemas.edu.pe.sistemaescaneo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(basePackages= {"sistemas.edu.pe.sistemaescaneo.controller"})
public class GlobalRestErrorController {

	// Manejando errores
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
		
		Map<String, Object> response = new HashMap<>();

		response.put("message", e.getName().concat(" deberia ser ").concat(e.getRequiredType().getSimpleName()));
		response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
	    
	    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
	    
	}

}
