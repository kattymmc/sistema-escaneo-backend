package sistemas.edu.pe.sistemaescaneo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sistemas.edu.pe.sistemaescaneo.dao.IDocumentoDAO;
import sistemas.edu.pe.sistemaescaneo.entity.Documento;
import sistemas.edu.pe.sistemaescaneo.service.IDocumentoService;

@Service
public class DocumentoServiceImpl implements IDocumentoService{

	@Autowired
	private IDocumentoDAO documentoDAO;
	
	@Override
	@Transactional(readOnly = true)
	public List<Documento> findAll() {
		return (List<Documento>) documentoDAO.findAll();
	}

	@Override
	@Transactional
	public Documento save(Documento documento) {
		return documentoDAO.save(documento);
	}

	@Override
	@Transactional(readOnly = true)
	public Documento findById(Long id) {
		return documentoDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		documentoDAO.deleteById(id);
		
	}

}
