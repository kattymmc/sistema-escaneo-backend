package sistemas.edu.pe.sistemaescaneo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sistemas.edu.pe.sistemaescaneo.dao.ITipoDocumentoDAO;
import sistemas.edu.pe.sistemaescaneo.entity.TipoDocumento;
import sistemas.edu.pe.sistemaescaneo.service.ITipoDocumentoService;

@Service
public class TipoDocumentoServiceImpl implements ITipoDocumentoService {
	@Autowired
	private ITipoDocumentoDAO tipoDocumentoDAO;
	
	@Override
	@Transactional(readOnly = true)
	public List<TipoDocumento> findAll() {
		return (List<TipoDocumento>) tipoDocumentoDAO.findAll();
	}

	@Override
	@Transactional
	public TipoDocumento save(TipoDocumento documento) {
		return tipoDocumentoDAO.save(documento);
	}

	@Override
	@Transactional(readOnly = true)
	public TipoDocumento findById(Long id) {
		return tipoDocumentoDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		tipoDocumentoDAO.deleteById(id);
		
	}

}
