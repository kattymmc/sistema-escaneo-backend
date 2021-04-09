package sistemas.edu.pe.sistemaescaneo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sistemas.edu.pe.sistemaescaneo.config.JwtUtil;
import sistemas.edu.pe.sistemaescaneo.dto.NuevoUsuario;
import sistemas.edu.pe.sistemaescaneo.entity.Persona;
import sistemas.edu.pe.sistemaescaneo.entity.Rol;
import sistemas.edu.pe.sistemaescaneo.entity.Usuario;
import sistemas.edu.pe.sistemaescaneo.models.AuthenticationRequest;
import sistemas.edu.pe.sistemaescaneo.models.AuthenticationResponse;
import sistemas.edu.pe.sistemaescaneo.service.IRolService;
import sistemas.edu.pe.sistemaescaneo.service.IUsuarioService;

@CrossOrigin(origins={"http://localhost:4200"}) 
@RestController
@RequestMapping("/api")
public class UsuarioRestController {
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IRolService rolService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@PostMapping("/usuarios/register")
	public ResponseEntity<?> crearUsuario(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult result) {
		Usuario usuarioNuevo = null;
		Map<String, Object> response = new HashMap<>();
		List<Rol> roles = new ArrayList<>();
		Persona persona = new Persona(nuevoUsuario.getNombre(),nuevoUsuario.getApellidomat(),
				nuevoUsuario.getApellidopat(),nuevoUsuario.getEmail());
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			roles.add(rolService.findByNombre("ROLE_USER"));
			if(nuevoUsuario.getRoles().contains("admin")) {
				roles.add(rolService.findByNombre("ROLE_ADMIN"));
			}
			Usuario usuario = new Usuario(nuevoUsuario.getUsername(), nuevoUsuario.getPassword(), 
					nuevoUsuario.getEnabled(),roles,persona);
			usuarioNuevo = usuarioService.save(usuario);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar al insertar en la Base de Datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El usuario ha sido registrado con éxito");
		response.put("usuario", usuarioNuevo);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PostMapping("/autenticacion")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		Map<String, Object> response = new HashMap<>();
		try {
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		} catch (BadCredentialsException e) {
			response.put("mensaje", "Credenciales incorrectas");
			throw new Exception("Incorrect username or password", e);
		}
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());
		
		final String jwt = jwtTokenUtil.generateToken(userDetails);
	
		response.put("usuario",userDetails);
		response.put("token", new AuthenticationResponse(jwt));
				
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
