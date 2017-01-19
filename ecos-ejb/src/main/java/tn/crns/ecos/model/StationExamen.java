package tn.crns.ecos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="examen_stations")
public class StationExamen implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "PONDERATION")
	private Integer ponderation;

	@Column(name = "STATION_NAME")
	private String nom_station;

	@EmbeddedId
	private StationExamenPK stationExamPK;

	@MapsId("idStation")
	@ManyToOne
	@JoinColumn(name = "STATION_ID", referencedColumnName = "id", insertable = false, updatable = false)
	private Station station;

	@MapsId("idExamen")
	@ManyToOne
	@JoinColumn(name = "EXAMEN_ID", referencedColumnName = "id", insertable = false, updatable = false)
	private Examen examen;

	
	public StationExamen() {
		super();
	}

	public Integer getPonderation() {
		return ponderation;
	}

	public void setPonderation(Integer ponderation) {
		this.ponderation = ponderation;
	}

	public String getNom_station() {
		return nom_station;
	}

	public void setNom_station(String nom_station) {
		this.nom_station = nom_station;
	}

	public StationExamenPK getStationExamPK() {
		return stationExamPK;
	}

	public void setStationExamPK(StationExamenPK stationExamPK) {
		this.stationExamPK = stationExamPK;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public Examen getExamen() {
		return examen;
	}

	public void setExamen(Examen examen) {
		this.examen = examen;
	}

}
