package sistemas.edu.pe.sistemaescaneo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sistemas.edu.pe.sistemaescaneo.dao.IImagenDAO;
import sistemas.edu.pe.sistemaescaneo.entity.Imagen;
import sistemas.edu.pe.sistemaescaneo.service.IImagenService;

@Service
public class ImagenServiceImpl implements IImagenService{
	
	@Autowired
	private IImagenDAO imagenDAO;

	@Override
	public void delete(Long id) {
		imagenDAO.deleteById(id);
	}

	@Override
	public Imagen findById(Long id) {
		return imagenDAO.findById(id).orElse(null);
	}

}
