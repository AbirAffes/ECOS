package tn.crns.ecos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.xml.bind.annotation.XmlTransient;

import tn.crns.ecos.outils.RandomString;

@Entity
@PrimaryKeyJoinColumn(name = "ID")
public class Enseignant extends Utilisateur implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@Lob
	@Column(name = "QRCode", columnDefinition = "MEDIUMBLOB")
	@Basic(fetch = FetchType.LAZY)
	private byte[] img;

	@Column(name = "NOM")
	private String nom;

	@Column(name = "PRENOM")
	private String prenom;

	@Column(name = "EMAIL")
	private String mail;

	@XmlTransient
	@Column(name = "M_COMITE")
	private Boolean membre_comite;

	@XmlTransient
	@Column(name = "P_COMITE")
	private Boolean president_comite;

	@XmlTransient
	@Column(name = "GRADE")
	private String grade;

	@Column(name = "AFFILIATION")
	private String affiliation;
	
/*	@XmlTransient
	@ManyToMany(mappedBy = "jury")
	private List<Examen> exams = new ArrayList<Examen>();*/

	@XmlTransient
	@ManyToMany(mappedBy = "auteurs")
	private List<Station> stations = new ArrayList<Station>();

	public Enseignant() {
		super();
	}

	public Enseignant(Long cin, String nom, String prenom, String mail,
			Boolean membre_comite, Boolean president_comite, String grade,
			String aff) {
		super(cin, new RandomString().nextString());
		this.nom = nom;
		this.prenom = prenom;
		this.mail = mail;
		this.membre_comite = membre_comite;
		this.president_comite = president_comite;
		this.grade = grade;
		this.affiliation = aff;

	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@XmlTransient
	public List<Station> getStations() {
		return stations;
	}

	public void setStations(List<Station> stations) {
		this.stations = stations;
	}

	public Boolean getMembre_comite() {
		return membre_comite;
	}

	public void setMembre_comite(Boolean membre_comite) {
		this.membre_comite = membre_comite;
	}

	public Boolean getPresident_comite() {
		return president_comite;
	}

	public void setPresident_comite(Boolean president_comite) {
		this.president_comite = president_comite;
	}

	@XmlTransient
	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
/*

	public List<Examen> getExams() {
		return exams;
	}

	public void setExams(List<Examen> exams) {
		this.exams = exams;
	}
*/
}
