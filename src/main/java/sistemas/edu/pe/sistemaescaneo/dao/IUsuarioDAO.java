package sistemas.edu.pe.sistemaescaneo.dao;

import org.springframework.data.repository.CrudRepository;

import sistemas.edu.pe.sistemaescaneo.entity.Usuario;

public interface IUsuarioDAO extends CrudRepository<Usuario, Long>{

}
