package sistemas.edu.pe.sistemaescaneo.service;

import sistemas.edu.pe.sistemaescaneo.entity.Imagen;

public interface IImagenService {

	public Imagen findById(Long id);
	
	public void delete(Long id);
}
