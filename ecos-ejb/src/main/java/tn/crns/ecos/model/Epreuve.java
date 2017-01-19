package tn.crns.ecos.model;

import java.io.Serializable;
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
import javax.persistence.OneToOne;

@Entity
public class Epreuve implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "SCORE")
	private Integer score;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Item> notes;

	@Column(name = "EVALUATION")
	private String evalGlobale;

	@Column(name = "COMMENTAIRES")
	private String commentaires;

	@ManyToOne
	@JoinColumn(name = "CANDIDAT")
	private Etudiant candidat;

	@OneToOne
	@JoinColumn(name = "PS")
	private Etudiant patientS;

	@OneToOne
	@JoinColumn(name = "MO")
	private Enseignant medecinOb;

	@OneToOne
	@JoinColumn(name = "EXAMEN")
	private Examen examen;

	@OneToOne
	@JoinColumn(name = "STATION")
	private Station station;

	public Epreuve() {

	}

	public Epreuve(Integer score, String evalGlobale, String commentaires,
			Etudiant candidat, Etudiant patientS, Enseignant medecinOb,
			Examen examen, Station station) {
		this.score = score;
		this.evalGlobale = evalGlobale;
		this.commentaires = commentaires;
		this.candidat = candidat;
		this.patientS = patientS;
		this.medecinOb = medecinOb;
		this.examen = examen;
		this.station = station;
		// this.candidat.getEpreuves().add(this);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public List<Item> getNotes() {
		return notes;
	}

	public void setNotes(List<Item> notes) {
		this.notes = notes;
	}

	public String getEvalGlobale() {
		return evalGlobale;
	}

	public void setEvalGlobale(String evalGlobale) {
		this.evalGlobale = evalGlobale;
	}

	public String getCommentaires() {
		return commentaires;
	}

	public void setCommentaires(String commentaires) {
		this.commentaires = commentaires;
	}

	public Etudiant getCandidat() {
		return candidat;
	}

	public void setCandidat(Etudiant candidat) {
		this.candidat = candidat;
	}

	public Etudiant getPatientS() {
		return patientS;
	}

	public void setPatientS(Etudiant patientS) {
		this.patientS = patientS;
	}

	public Enseignant getMedecinOb() {
		return medecinOb;
	}

	public void setMedecinOb(Enseignant medecinOb) {
		this.medecinOb = medecinOb;
	}

	public Examen getExamen() {
		return examen;
	}

	public void setExamen(Examen examen) {
		this.examen = examen;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

}
