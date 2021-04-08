package sistemas.edu.pe.sistemaescaneo.service;

import java.util.List;

import sistemas.edu.pe.sistemaescaneo.entity.Usuario;

public interface IUsuarioService {

	public List<Usuario> findAll();
	
	public Usuario findByUsername(String username);
	
	public Usuario save(Usuario usuario);
	
	public void delete(Long id);
}
