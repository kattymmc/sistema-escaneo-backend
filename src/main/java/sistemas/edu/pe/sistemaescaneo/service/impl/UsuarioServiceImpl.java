package sistemas.edu.pe.sistemaescaneo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sistemas.edu.pe.sistemaescaneo.dao.IUsuarioDAO;
import sistemas.edu.pe.sistemaescaneo.entity.Usuario;
import sistemas.edu.pe.sistemaescaneo.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService{
	
	@Autowired
	private IUsuarioDAO usuarioDAO;

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll() {
		return (List<Usuario>) usuarioDAO.findAll();
	}

}
