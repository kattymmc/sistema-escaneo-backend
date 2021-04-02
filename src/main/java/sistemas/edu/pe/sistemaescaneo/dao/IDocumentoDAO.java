package sistemas.edu.pe.sistemaescaneo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import sistemas.edu.pe.sistemaescaneo.entity.Documento;

public interface IDocumentoDAO extends CrudRepository<Documento, Long>{
	
	//@Query("select d from Documento d where p.codigoDoc like %?1%")
	//public List<Documento> findByCodigoDoc(String term);

	public List<Documento> findByCodigoDocContainingIgnoreCase(String term);
}
