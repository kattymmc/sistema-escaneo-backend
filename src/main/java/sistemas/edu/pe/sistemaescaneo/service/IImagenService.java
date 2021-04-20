package sistemas.edu.pe.sistemaescaneo.service;
import sistemas.edu.pe.sistemaescaneo.entity.Imagen;

public interface IImagenService {
	
	public Imagen save(Imagen imagen);

	public Imagen findById(Long id);
	
	public void delete(Long id);
}
