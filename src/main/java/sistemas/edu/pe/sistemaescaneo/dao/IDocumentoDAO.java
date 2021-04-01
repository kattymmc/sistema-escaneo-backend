package sistemas.edu.pe.sistemaescaneo.dao;

import org.springframework.data.repository.CrudRepository;

import sistemas.edu.pe.sistemaescaneo.entity.Documento;

public interface IDocumentoDAO extends CrudRepository<Documento, Long>{

}
