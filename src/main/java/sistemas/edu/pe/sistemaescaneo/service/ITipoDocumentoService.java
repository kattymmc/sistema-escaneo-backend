package sistemas.edu.pe.sistemaescaneo.service;

import java.util.List;

import sistemas.edu.pe.sistemaescaneo.entity.TipoDocumento;

public interface ITipoDocumentoService {

	public List<TipoDocumento> findAll();

	public TipoDocumento findById(Long id);
	
	public TipoDocumento save(TipoDocumento tipoDocumento);
	
	public void delete(Long id);
	
}
