package sistemas.edu.pe.sistemaescaneo.dao;

import org.springframework.data.repository.CrudRepository;

import sistemas.edu.pe.sistemaescaneo.entity.Rol;

public interface IRolDAO extends CrudRepository<Rol, Long>{
	public Rol findByNombre(String nombre);
}
