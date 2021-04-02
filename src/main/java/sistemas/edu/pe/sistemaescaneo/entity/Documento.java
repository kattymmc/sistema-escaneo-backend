package sistemas.edu.pe.sistemaescaneo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="documentos")
public class Documento implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message ="no puede estar vacio")
	@Column(nullable=false, unique=true)
	private String codigoDoc;
	
	@NotNull(message="No puede estar vacío el anaquel")
	private int anaquel;
	
	@NotNull(message="No puede estar vacío la columna")
	private int columna;
	
	@NotNull(message = "El tipo no puede ser vacío")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tipo_documento")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"}) // Se añade debido a LAZY
	private TipoDocumento tipoDocumento;
	
	@JsonIgnoreProperties("documento")
	@OneToMany(mappedBy = "documento",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Imagen> imagenes;
	
	@CreationTimestamp
	private Date dateCreated;
	
	@UpdateTimestamp
	private Date lastUpdated;
	
	public Documento() {
		this.imagenes = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoDoc() {
		return codigoDoc;
	}

	public void setCodigoDoc(String codigoDoc) {
		this.codigoDoc = codigoDoc;
	}
	

	public int getAnaquel() {
		return anaquel;
	}

	public void setAnaquel(int anaquel) {
		this.anaquel = anaquel;
	}

	public int getColumna() {
		return columna;
	}

	public void setColumna(int columna) {
		this.columna = columna;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public List<Imagen> getImagenes() {
		return imagenes;
	}

	public void setImagenes(List<Imagen> imagenes) {
		this.imagenes = imagenes;
	}

	private static final long serialVersionUID = 1L;
	
}
