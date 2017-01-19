package tn.crns.ecos.model;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Objectif implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "OBJECTIF")
	private String objectif;

	@Column(name = "PONDERATION")
	private Integer ponderation;
	
	@Column(name = "ITEMS_NB")
	private Integer nbItems;

	@XmlTransient
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "STATION")
	private Station station;

	
	@OneToMany(mappedBy = "objectif", cascade = { CascadeType.PERSIST,
			CascadeType.REMOVE} ,orphanRemoval=true, fetch = FetchType.EAGER)
	private List<Item> items = new ArrayList<Item>();

	public Objectif(String objectif, Integer ponderation, Station station) {
		super();
		this.objectif = objectif;
		this.ponderation = ponderation;
		this.station = station;
		this.station.getObjectifs().add(this);
	}

	public Objectif(String objectif, Integer ponderation) {
		super();
		this.objectif = objectif;
		this.ponderation = ponderation;
	}
	
	public Objectif() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getObjectif() {
		return objectif;
	}

	public void setObjectif(String objectif) {
		this.objectif = objectif;
	}

	public Integer getPonderation() {
		return ponderation;
	}

	public void setPonderation(Integer ponderation) {
		this.ponderation = ponderation;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items.clear();
	    this.items.addAll(items);
	}

	public Integer getNbItems() {
		return nbItems;
	}

	public void setNbItems(Integer nbItems) {
		this.nbItems = nbItems;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((objectif == null) ? 0 : objectif.hashCode());
		result = prime * result
				+ ((ponderation == null) ? 0 : ponderation.hashCode());
		result = prime * result + ((station == null) ? 0 : station.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Objectif other = (Objectif) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (objectif == null) {
			if (other.objectif != null)
				return false;
		} else if (!objectif.equals(other.objectif))
			return false;
		if (ponderation == null) {
			if (other.ponderation != null)
				return false;
		} else if (!ponderation.equals(other.ponderation))
			return false;
		if (station == null) {
			if (other.station != null)
				return false;
		} else if (!station.equals(other.station))
			return false;
		return true;
	}

}
