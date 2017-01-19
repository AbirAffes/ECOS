package tn.crns.ecos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.xml.bind.annotation.XmlTransient;

import tn.crns.ecos.outils.RandomString;

@Entity
@PrimaryKeyJoinColumn(name = "ID")
public class Etudiant extends Utilisateur implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	@Column(name = "CREDITS")
	private boolean credits = false;

	@Column(name = "ABSENCE")
	private boolean absence = false;

	@Column(name = "STAGE_CH")
	private boolean stage_ch = false;
	@Column(name = "STAGE_PED")
	private boolean stage_ped = false;
	@Column(name = "STAGE_GY")
	private boolean stage_gy = false;
	@Column(name = "STAGE_MED")
	private boolean stage_med = false;

	@Column(name = "VALID_CH")
	private boolean valid_ch = false;
	@Column(name = "VALID_PED")
	private boolean valid_ped = false;
	@Column(name = "VALID_GY")
	private boolean valid_gy = false;
	@Column(name = "VALID_MED")
	private boolean valid_med = false;

	@OneToMany(mappedBy = "candidat", cascade = CascadeType.REMOVE)
	private List<Epreuve> epreuves = new ArrayList<Epreuve>();

	public Etudiant() {
		super();
	}

	public Etudiant(Long cin, String nom, String prenom, String mail) {
		super(cin, new RandomString().nextString());
		this.nom = nom;
		this.prenom = prenom;
		this.mail = mail;

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

	public boolean isCredits() {
		return credits;
	}

	public void setCredits(boolean credits) {
		this.credits = credits;
	}

	public boolean isAbsence() {
		return absence;
	}

	public void setAbsence(boolean absence) {
		this.absence = absence;
	}

	@XmlTransient
	public List<Epreuve> getEpreuves() {
		return epreuves;
	}

	public void setEpreuves(List<Epreuve> epreuves) {
		this.epreuves = epreuves;
	}

	public boolean isStage_ch() {
		return stage_ch;
	}

	public void setStage_ch(boolean stage_ch) {
		this.stage_ch = stage_ch;
	}

	public boolean isStage_ped() {
		return stage_ped;
	}

	public void setStage_ped(boolean stage_ped) {
		this.stage_ped = stage_ped;
	}

	public boolean isStage_gy() {
		return stage_gy;
	}

	public void setStage_gy(boolean stage_gy) {
		this.stage_gy = stage_gy;
	}

	public boolean isStage_med() {
		return stage_med;
	}

	public void setStage_med(boolean stage_med) {
		this.stage_med = stage_med;
	}

	public boolean isValid_ch() {
		return valid_ch;
	}

	public void setValid_ch(boolean valid_ch) {
		this.valid_ch = valid_ch;
	}

	public boolean isValid_ped() {
		return valid_ped;
	}

	public void setValid_ped(boolean valid_ped) {
		this.valid_ped = valid_ped;
	}

	public boolean isValid_gy() {
		return valid_gy;
	}

	public void setValid_gy(boolean valid_gy) {
		this.valid_gy = valid_gy;
	}

	public boolean isValid_med() {
		return valid_med;
	}

	public void setValid_med(boolean valid_med) {
		this.valid_med = valid_med;
	}

	@XmlTransient
	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

}
