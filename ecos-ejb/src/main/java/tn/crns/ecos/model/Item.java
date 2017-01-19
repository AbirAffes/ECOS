package tn.crns.ecos.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Item implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "DESCRIPTION")
	private String description;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "OBJECTIF")
	private Objectif objectif;

	@Column(name = "PONDERATION")
	private Integer ponderation;

	public Item(String description, Objectif objectif, Integer ponderation) {
		super();
		this.description = description;
		this.objectif = objectif;
		this.ponderation = ponderation;
		this.objectif.getItems().add(this);
	}

	public Item() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Objectif getObjectif() {
		return objectif;
	}

	public void setObjectif(Objectif objectif) {
		this.objectif = objectif;
	}

	public Integer getPonderation() {
		return ponderation;
	}

	public void setPonderation(Integer ponderation) {
		this.ponderation = ponderation;
	}

}
