package sistemas.edu.pe.sistemaescaneo.service;

import java.util.List;

import sistemas.edu.pe.sistemaescaneo.entity.Documento;

public interface IDocumentoService {
	
	public List<Documento> findAll();
	
	public Documento findById(Long id);
	
	public Documento save(Documento documento);
	
	public void delete(Long id);
}
