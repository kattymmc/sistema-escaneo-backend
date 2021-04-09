package sistemas.edu.pe.sistemaescaneo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sistemas.edu.pe.sistemaescaneo.dao.IRolDAO;
import sistemas.edu.pe.sistemaescaneo.entity.Rol;
import sistemas.edu.pe.sistemaescaneo.service.IRolService;

@Service
public class RolServiceImpl implements IRolService{

	@Autowired
	private IRolDAO rolDAO;
	
	@Override
	public Rol findByNombre(String nombre) {
		return rolDAO.findByNombre(nombre);
	}

}
