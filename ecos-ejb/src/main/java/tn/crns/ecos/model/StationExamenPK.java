package tn.crns.ecos.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class StationExamenPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer idStation;
	private Integer idExamen;

	public StationExamenPK() {
	}

	public StationExamenPK(Integer idStation, Integer idExamen) {
		super();
		this.idStation = idStation;
		this.idExamen = idExamen;
	}

	public Integer getIdStation() {
		return idStation;
	}

	public void setIdStation(Integer idStation) {
		this.idStation = idStation;
	}

	public Integer getIdExamen() {
		return idExamen;
	}

	public void setIdExamen(Integer idExamen) {
		this.idExamen = idExamen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idExamen == null) ? 0 : idExamen.hashCode());
		result = prime * result
				+ ((idStation == null) ? 0 : idStation.hashCode());
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
		StationExamenPK other = (StationExamenPK) obj;
		if (idExamen == null) {
			if (other.idExamen != null)
				return false;
		} else if (!idExamen.equals(other.idExamen))
			return false;
		if (idStation == null) {
			if (other.idStation != null)
				return false;
		} else if (!idStation.equals(other.idStation))
			return false;
		return true;
	}

}
