package tn.crns.ecos.model;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Examen implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "DATE")
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "HORAIRE")
	@Temporal(TemporalType.TIME)
	private Date horaire;

	@Column(name = "DOMAINE")
	private String domaine;

	@Column(name = "CAPACITE")
	private Integer nbMax;

	@Temporal(TemporalType.DATE)
	@Column(name = "DEBUT_INSCRI")
	private Date debut_inscri;

	@Temporal(TemporalType.DATE)
	@Column(name = "DELAI_INSCRI")
	private Date fin_inscri;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private List<Etudiant> candidats = new ArrayList<Etudiant>();

	/*
	 * @JsonIgnore
	 * 
	 * @ManyToMany(cascade = CascadeType.MERGE)
	 * 
	 * @JoinTable(name = "exam_stations", joinColumns = @JoinColumn(name =
	 * "exam_ID", referencedColumnName = "ID"), inverseJoinColumns =
	 * 
	 * @JoinColumn(name = "stat_ID", referencedColumnName = "ID")) private
	 * List<Station> circuit_stations = new ArrayList<Station>();
	 */

	@JsonIgnore
	@OneToMany(mappedBy = "examen", cascade ={ CascadeType.MERGE,CascadeType.REMOVE}, fetch = FetchType.EAGER)
	private List<StationExamen> stations = new ArrayList<StationExamen>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private List<Enseignant> jury = new ArrayList<Enseignant>();

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "PRESIDENT_JURY")
	private Enseignant president;

	public Examen() {
		super();
	}

	public Examen(Date date, Date horaire, String domaine, Integer nbMax,
			Date debut_inscri, Date fin_inscri, List<Enseignant> jury,
			Enseignant president) {
		super();
		this.date = date;
		this.horaire = horaire;
		this.domaine = domaine;
		this.nbMax = nbMax;
		this.debut_inscri = debut_inscri;
		this.fin_inscri = fin_inscri;
		this.jury = jury;
		this.president = president;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDomaine() {
		return domaine;
	}

	public void setDomaine(String domaine) {
		this.domaine = domaine;
	}

	public Integer getNbMax() {
		return nbMax;
	}

	public void setNbMax(Integer nbMax) {
		this.nbMax = nbMax;
	}

	public List<Etudiant> getCandidats() {
		return candidats;
	}

	public void setCandidats(List<Etudiant> candidats) {
		this.candidats = candidats;
	}

	/*
	 * public List<Station> getCircuit_stations() { return circuit_stations; }
	 * 
	 * public void setCircuit_stations(List<Station> circuit_stations) {
	 * this.circuit_stations = circuit_stations; }
	 */

	public List<Enseignant> getJury() {
		return jury;
	}

	public void setJury(List<Enseignant> jury) {
		this.jury = jury;
	}

	public Enseignant getPresident() {
		return president;
	}

	public void setPresident(Enseignant president) {
		this.president = president;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDebut_inscri() {
		return debut_inscri;
	}

	public void setDebut_inscri(Date debut_inscri) {
		this.debut_inscri = debut_inscri;
	}

	public Date getFin_inscri() {
		return fin_inscri;
	}

	public void setFin_inscri(Date fin_inscri) {
		this.fin_inscri = fin_inscri;
	}

	public Date getHoraire() {
		return horaire;
	}

	public void setHoraire(Date horaire) {
		this.horaire = horaire;
	}

	public List<StationExamen> getStations() {
		return stations;
	}

	public void setStations(List<StationExamen> stations) {
		this.stations = stations;
	}

	public void assignStationsACetEnseignant(List<StationExamen> stationExamen) {
		this.setStations(stationExamen);
		for (StationExamen r : stationExamen) {
			r.setExamen(this);
		}

	}

}
