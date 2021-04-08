package sistemas.edu.pe.sistemaescaneo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sistemas.edu.pe.sistemaescaneo.dao.IUsuarioDAO;
import sistemas.edu.pe.sistemaescaneo.entity.Usuario;
import sistemas.edu.pe.sistemaescaneo.service.IUsuarioService;

@Service
public class UsuarioService implements UserDetailsService, IUsuarioService {

	private final Logger log = LoggerFactory.getLogger(UsuarioService.class);
	
	@Autowired
	private IUsuarioDAO usuarioDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioDAO.findByUsername(username);
		
		if(usuario == null) {
			log.error("Error en el login: no existe el usuario '"+username+"' en el sistema!");
			throw new UsernameNotFoundException("Error en el login: no existe el usuario '"+username+"' en el sistema!");
		}
		
		List<GrantedAuthority> authorities = usuario.getRoles()
											.stream()
											.map(role -> new SimpleGrantedAuthority(role.getNombre())) // convierte los roles a grantedAuthority
											.peek(authority -> log.info("Role: "+ authority.getAuthority())) // agarra cada rol convertido y lo imprime
											.collect(Collectors.toList()); // recolecta los datos convertidos y lo guarda en un List
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

	@Override
	public List<Usuario> findAll() {
		return (List<Usuario>) usuarioDAO.findAll();
	}

	@Override
	public Usuario findByUsername(String username) {
		return usuarioDAO.findByUsername(username);
	}

	@Override
	public Usuario save(Usuario usuario) {
		String pass = passwordEncoder.encode(usuario.getPassword());
		usuario.setPassword(pass);
		return usuarioDAO.save(usuario);
	}

	@Override
	public void delete(Long id) {
		usuarioDAO.deleteById(id);
	}

	
}
