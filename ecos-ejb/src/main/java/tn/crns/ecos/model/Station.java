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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tn.crns.ecos.outils.FileStorageEntity;

@XmlRootElement
@Entity
public class Station implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "VALIDEE")
	private Boolean valide = false;

	@Column(name = "CREATION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name = "DOMAINE")
	private String domaine;

	@Column(name = "CONTEXTE")
	private String contexte;

	@Column(name = "SYSTEME")
	private String systeme;

	@Column(name = "PLAINTE")
	private String plainte;

	@Column(name = "COMPETENCE")
	private String competence;

	@Column(name = "SIMULATION")
	private Boolean simulation;

	@Column(name = "MOYEN_SIM")
	private String moyenSimulation;

	@JsonIgnore
	@Lob
	@Column(name = "S_CLINIQUE", columnDefinition = "text")
	private String situationClinique;

	@JsonIgnore
	@Lob
	@Column(name = "INSTRU_C", columnDefinition = "mediumtext")
	private String instrucCandidat;

	@JsonIgnore
	@Lob
	@Column(name = "INSTRU_PS", columnDefinition = "mediumtext")
	private String instrucPS;

	@JsonIgnore
	@Lob
	@Column(name = "INSTRU_MO", columnDefinition = "mediumtext")
	private String instrucMO;

	@Column(name = "MATERIEL")
	private String materiel;

	@XmlTransient
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FILE")
	private FileStorageEntity fichier_joint;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private List<Enseignant> auteurs = new ArrayList<Enseignant>();

	@JsonIgnore
	@OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE , orphanRemoval=true)
	private List<Objectif> objectifs = new ArrayList<Objectif>();

	public Station() {
		super();
	}

	public Station(String domaine, String contexte, String systeme,
			String plainte, String competence, Boolean simulation,
			String moyenSimulation, String situationClinique,
			String instrucCandidat, String instrucPS, String instrucMO,
			String materiel, FileStorageEntity fichier_joint,
			String titre_fichier, List<Enseignant> auteurs) {
		super();
		this.domaine = domaine;
		this.contexte = contexte;
		this.systeme = systeme;
		this.plainte = plainte;
		this.simulation = simulation;
		this.moyenSimulation = moyenSimulation;
		this.situationClinique = situationClinique;
		this.instrucCandidat = instrucCandidat;
		this.instrucPS = instrucPS;
		this.instrucMO = instrucMO;
		this.materiel = materiel;
		this.fichier_joint = fichier_joint;
		this.competence = competence;
		this.date = new Date();
		this.auteurs = auteurs;

		for (int i = 0; i < this.auteurs.size(); i++)
			this.auteurs.get(i).getStations().add(this);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setValide(Boolean valide) {
		this.valide = valide;
	}

	public String getDomaine() {
		return domaine;
	}

	public void setDomaine(String domaine) {
		this.domaine = domaine;
	}

	public String getContexte() {
		return contexte;
	}

	public void setContexte(String contexte) {
		this.contexte = contexte;
	}

	public String getSysteme() {
		return systeme;
	}

	public void setSysteme(String systeme) {
		this.systeme = systeme;
	}

	public String getPlainte() {
		return plainte;
	}

	public void setPlainte(String plainte) {
		this.plainte = plainte;
	}

	public String getCompetence() {
		return competence;
	}

	public void setCompetence(String competence) {
		this.competence = competence;
	}

	public Boolean getSimulation() {
		return simulation;
	}

	public void setSimulation(Boolean simulation) {
		this.simulation = simulation;
	}

	public Boolean getValide() {
		return valide;
	}

	public String getMoyenSimulation() {
		return moyenSimulation;
	}

	public void setMoyenSimulation(String moyenSimulation) {
		this.moyenSimulation = moyenSimulation;
	}

	public String getSituationClinique() {
		return situationClinique;
	}

	public void setSituationClinique(String situationClinique) {
		this.situationClinique = situationClinique;
	}

	public String getInstrucCandidat() {
		return instrucCandidat;
	}

	public void setInstrucCandidat(String instrucCandidat) {
		this.instrucCandidat = instrucCandidat;
	}

	public String getInstrucPS() {
		return instrucPS;
	}

	public void setInstrucPS(String instrucPS) {
		this.instrucPS = instrucPS;
	}

	public String getInstrucMO() {
		return instrucMO;
	}

	public void setInstrucMO(String instrucMO) {
		this.instrucMO = instrucMO;
	}

	public String getMateriel() {
		return materiel;
	}

	public void setMateriel(String materiel) {
		this.materiel = materiel;
	}

	public List<Enseignant> getAuteurs() {
		return auteurs;
	}

	public void setAuteurs(List<Enseignant> auteurs) {
		this.auteurs = auteurs;
	}

	/*
	 * public List<Item> getItems() { return items; }
	 * 
	 * public void setItems(List<Item> items) { this.items = items; }
	 */
	public FileStorageEntity getFichier_joint() {
		return fichier_joint;
	}

	public void setFichier_joint(FileStorageEntity fichier_joint) {
		this.fichier_joint = fichier_joint;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Objectif> getObjectifs() {
		return objectifs;
	}

	public void setObjectifs(List<Objectif> objectifs) {
		this.objectifs=objectifs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auteurs == null) ? 0 : auteurs.hashCode());
		result = prime * result
				+ ((competence == null) ? 0 : competence.hashCode());
		result = prime * result
				+ ((contexte == null) ? 0 : contexte.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((domaine == null) ? 0 : domaine.hashCode());
		result = prime * result
				+ ((fichier_joint == null) ? 0 : fichier_joint.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((instrucCandidat == null) ? 0 : instrucCandidat.hashCode());
		result = prime * result
				+ ((instrucMO == null) ? 0 : instrucMO.hashCode());
		result = prime * result
				+ ((instrucPS == null) ? 0 : instrucPS.hashCode());
		result = prime * result
				+ ((materiel == null) ? 0 : materiel.hashCode());
		result = prime * result
				+ ((moyenSimulation == null) ? 0 : moyenSimulation.hashCode());
		result = prime * result
				+ ((objectifs == null) ? 0 : objectifs.hashCode());
		result = prime * result + ((plainte == null) ? 0 : plainte.hashCode());
		result = prime * result
				+ ((simulation == null) ? 0 : simulation.hashCode());
		result = prime
				* result
				+ ((situationClinique == null) ? 0 : situationClinique
						.hashCode());
		result = prime * result + ((systeme == null) ? 0 : systeme.hashCode());
		result = prime * result + ((valide == null) ? 0 : valide.hashCode());
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
		Station other = (Station) obj;
		if (auteurs == null) {
			if (other.auteurs != null)
				return false;
		} else if (!auteurs.equals(other.auteurs))
			return false;
		if (competence == null) {
			if (other.competence != null)
				return false;
		} else if (!competence.equals(other.competence))
			return false;
		if (contexte == null) {
			if (other.contexte != null)
				return false;
		} else if (!contexte.equals(other.contexte))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (domaine == null) {
			if (other.domaine != null)
				return false;
		} else if (!domaine.equals(other.domaine))
			return false;
		if (fichier_joint == null) {
			if (other.fichier_joint != null)
				return false;
		} else if (!fichier_joint.equals(other.fichier_joint))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (instrucCandidat == null) {
			if (other.instrucCandidat != null)
				return false;
		} else if (!instrucCandidat.equals(other.instrucCandidat))
			return false;
		if (instrucMO == null) {
			if (other.instrucMO != null)
				return false;
		} else if (!instrucMO.equals(other.instrucMO))
			return false;
		if (instrucPS == null) {
			if (other.instrucPS != null)
				return false;
		} else if (!instrucPS.equals(other.instrucPS))
			return false;
		if (materiel == null) {
			if (other.materiel != null)
				return false;
		} else if (!materiel.equals(other.materiel))
			return false;
		if (moyenSimulation == null) {
			if (other.moyenSimulation != null)
				return false;
		} else if (!moyenSimulation.equals(other.moyenSimulation))
			return false;
		if (objectifs == null) {
			if (other.objectifs != null)
				return false;
		} else if (!objectifs.equals(other.objectifs))
			return false;
		if (plainte == null) {
			if (other.plainte != null)
				return false;
		} else if (!plainte.equals(other.plainte))
			return false;
		if (simulation == null) {
			if (other.simulation != null)
				return false;
		} else if (!simulation.equals(other.simulation))
			return false;
		if (situationClinique == null) {
			if (other.situationClinique != null)
				return false;
		} else if (!situationClinique.equals(other.situationClinique))
			return false;
		if (systeme == null) {
			if (other.systeme != null)
				return false;
		} else if (!systeme.equals(other.systeme))
			return false;
		if (valide == null) {
			if (other.valide != null)
				return false;
		} else if (!valide.equals(other.valide))
			return false;
		return true;
	}

}
