package sistemas.edu.pe.sistemaescaneo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="documentos")
public class Documento implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message ="no puede estar vacio")
	@Column(nullable=false)
	private String codigoDoc;
	
	@NotNull(message="No puede estar vacío el anaquel")
	private int anaquel;
	
	@NotNull(message="No puede estar vacío la columna")
	private int columna;
	
	@CreationTimestamp
	private Date dateCreated;
	
	@UpdateTimestamp
	private Date lastUpdated;

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
	
	private static final long serialVersionUID = 1L;
	
}
