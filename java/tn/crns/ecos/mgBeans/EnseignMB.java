package tn.crns.ecos.mgBeans;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import tn.crns.ecos.model.Enseignant;
import tn.crns.ecos.model.Epreuve;
import tn.crns.ecos.model.Examen;
import tn.crns.ecos.model.Item;
import tn.crns.ecos.model.Objectif;
import tn.crns.ecos.model.Station;
import tn.crns.ecos.model.StationExamen;
import tn.crns.ecos.model.StationExamenPK;
import tn.crns.ecos.outils.FileStorageEntity;
import tn.crns.ecos.sessions.interfaces.EpreuveServiceLocal;
import tn.crns.ecos.sessions.interfaces.ExamensServiceLocal;
import tn.crns.ecos.sessions.interfaces.StationsServiceLocal;
import tn.crns.ecos.sessions.interfaces.UsersServiceLocal;

@ManagedBean(name = "enseignMB")
@ViewScoped
public class EnseignMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* attributs */

	@EJB
	private StationsServiceLocal metier;
	@EJB
	private UsersServiceLocal metier1;
	@EJB
	private ExamensServiceLocal metier2;
	@EJB
	private EpreuveServiceLocal metier3;

	private Station station;

	/*-------------------------------------------------------------------------------------------- Profile */

	@ManagedProperty("#{identity}")
	private IdentiteBean identiteBean;

	private Enseignant ident;
	private String nPwd;

	/*-------------------------------------------------------------------------------------------- Affichage */

	private StationToPDF stationToPDF=new StationToPDF();
	
	private List<ArrayList<Item>> itemsDesObjectifs;
	private List<Objectif> objectifsDeLaStation;

	private String ids;
	private List<Enseignant> auteursStat;
	private List<String> objAfficher;
	private List<Item> itemsStat;
	private StreamedContent dbImage;// /////////////////////////////

	private String objetMail;
	private String msgMail;

	/*-------------------------------------------------------------------------------------------- Ajout Station */
	private Integer idStationToAddObjectifs;
	private Integer nbObjToAddObjectifs;

	private int activeIndex;
	private List<String> competences = new ArrayList<String>();
	private List<String> idAuteurs;
	private String obj;
	private Map<String, String> lesObjectifs = new HashMap<String, String>();
	private Item item;
	private boolean verif = false;
	private boolean verifObj = false;
	private List<Enseignant> enseigns;
	private List<Item> items;
	private List<ArrayList<Item>> itemsOfObjectifs;
	private List<Objectif> objectifs;
	private List<String> objctifs;
	private UploadedFile file;
	private List<Item> itemsSupp;
	private Integer nbObjectifs;
	private boolean skip;
	/*-------------------------------------------------------------------------------------------- Modifier station */

	private Station s;
	private List<Item> itemsM;
	private List<String> competencesM = new ArrayList<String>();
	private List<String> objAfficherM;
	private List<String> idAuteursM = new ArrayList<String>();
	private boolean grilleOnOff = false;
	private List<Item> itemsS;
	private List<Item> itemsSt;
	private List<String> objAfficherSt;
	private FileStorageEntity fichier;
	private List<Objectif> listObjectifsStation;
	// private Station station_pass;

	/*-------------------------------------------------------------------- liste stations et gestion de ses station */

	private List<Station> stations;
	private List<Station> stationsFiltres;
	private List<Station> stationsOfUser;

	/*---------------------------------------------------------------------------- choix de stations pour un examen */
	private List<Examen> examensIdentIsJury;
	private List<Examen> examensProchains;
	private List<Examen> examensFiltres;
	private List<Station> stationsEx;
	private String[] idStations;
	private Integer idExamen;
	private int activeIndex1;
	private boolean verifStat = false;
	private List<StationExamen> listStationsExamen;

	/*-------------------------------------------------------------------------------------------- les resultats */

	private List<Epreuve> epreuves;
	private List<Epreuve> epreuvesFiltres;
	private String idE;
	private String idSt;
	private Examen examen;

	/* init() */

	@PostConstruct
	public void init() {
		station = new Station();
		s = new Station();
		item = new Item();
		items = new ArrayList<Item>();
		epreuves = new ArrayList<Epreuve>();
		itemsM = new ArrayList<Item>();
		itemsSupp = new ArrayList<Item>();
		itemsSt = new ArrayList<Item>();
		objctifs = new ArrayList<String>();
		objAfficher = new ArrayList<String>();
		objAfficherM = new ArrayList<String>();
		objAfficherSt = new ArrayList<String>();
		setStations(metier.listerStations());
		setExamensProchains(metier2.listerProchainsExamens());
		ident = this.getIdentite();
		enseigns = new ArrayList<Enseignant>();
		objectifs = new ArrayList<Objectif>();
		itemsOfObjectifs = new ArrayList<ArrayList<Item>>();
		stationsOfUser = metier.listerStationsParAuteur(ident);
		lesObjectifs = new HashMap<String, String>();
		idStations = new String[10];
	}

	/*--------------------------------------------------------------------------------------------*/

	public StreamedContent fileDownload(FileStorageEntity file) {
		byte[] bytes = file.getContent();
		InputStream ist = new ByteArrayInputStream(bytes);
		StreamedContent download = new DefaultStreamedContent(ist,
				file.getContentType(), file.getFileName());
		return download;
	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public boolean isDuComite() {
		Enseignant e = metier1.chercherEnseignantParId(identiteBean
				.getUtilisateur().getId());
		return e.getMembre_comite();
	}

	public boolean isDuJury() {
		Enseignant e = metier1.chercherEnseignantParId(identiteBean
				.getUtilisateur().getId());
		if (metier2.listerExamensParEnseignant(e).size() > 0)
			return true;
		else
			return false;

	}

	/* Methodes */
	/*-------------------------------------------------------------------------------------------- Profile */

	public Enseignant getIdentite() {
		return metier1.chercherEnseignantParId(identiteBean.getUtilisateur()
				.getId());
	}

	public void modifUser() {
		if (nPwd.length() >= 8) {
			ident.setPwd(nPwd);
			metier1.modifierEnseignant(ident);
			ident = this.getIdentite();
			nPwd = null;
		} else {
			nPwd = null;
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Erreur!", "Mot de passe trop court.");
			FacesContext.getCurrentInstance().addMessage(null, message);

		}
	}

	/*-------------------------------------------------------------------------------------------- Affichage */

	public void toXml(Integer id) {
		Station s = metier.chercherStationParId(id);
		s.setAuteurs(null);
		s.setFichier_joint(null);
		s.setObjectifs(null);
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Station.class);

			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty("jaxb.encoding", "UTF-8");
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller.marshal(s, new File("tableau.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	public String stationToPdf()
	{
		station.setObjectifs(objectifsDeLaStation);
		stationToPDF.downloadPDF(station);
		System.out.println("done");
		addMessage("Succès", "Pdf téléchargé.");
		return "success";
	}
	
	public void afficherStation() {
		objectifsDeLaStation = new ArrayList<Objectif>();
		itemsDesObjectifs = new ArrayList<ArrayList<Item>>();
		ArrayList<Item> items;
		station = metier.chercherStationParId(Integer.parseInt(ids));
		objectifsDeLaStation = metier.listerObjectifsParStation(station);
		System.out.println("kkkk" + objectifsDeLaStation.size());
		for (int i = 0; i < objectifsDeLaStation.size(); i++) {
			items = new ArrayList<Item>();
			Objectif o = objectifsDeLaStation.get(i);
			items.addAll(metier.listerItemsParObjectif(o));
			itemsDesObjectifs.add(items);
		}
		auteursStat = metier.listerAuteursParStation(station);
		if (station.getFichier_joint() != null)
			if (station.getFichier_joint().getContentType()
					.equals("image/jpeg"))
				dbImage = new DefaultStreamedContent(new ByteArrayInputStream(
						station.getFichier_joint().getContent()), station
						.getFichier_joint().getContentType(), station
						.getFichier_joint().getFileName());
		System.out.println(station.getDomaine());

	}

	public void envoyerMail() {
	}

	/*-------------------------------------------------------------------------------------------- Ajout Station */

	public void allerGrille() {
		objectifs = new ArrayList<Objectif>();
		System.out.println("kkkk" + nbObjToAddObjectifs);
		for (int s1 = 0; s1 < nbObjToAddObjectifs; s1++) {
			Objectif o = new Objectif();
			objectifs.add(o);
		}
		System.out.println("kkkk2" + objectifs.size());
	}

	public void allerItems(int index) {
		Integer somme = 0;
		for (int i = 0; i < objectifs.size(); i++)
			somme = somme + objectifs.get(i).getPonderation();
		if (somme == 100) {
			itemsOfObjectifs = new ArrayList<ArrayList<Item>>();
			System.out.println("kkkk3" + nbObjToAddObjectifs);
			for (int s1 = 0; s1 < nbObjToAddObjectifs; s1++) {
				ArrayList<Item> itemsOfObj = new ArrayList<Item>();
				for (int s = 0; s < objectifs.get(s1).getNbItems(); s++) {
					Item i = new Item();
					i.setObjectif(objectifs.get(s1));
					itemsOfObj.add(i);
				}
				itemsOfObjectifs.add(itemsOfObj);
			}
			System.out.println("kkkk4" + objectifs.size());
			this.activeIndex = index;
			verifObj = true;
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erreur!",
					"Somme des pondérations erronée.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public String saveStationFinal() {
		Boolean pondérationsOk = true;
		int i = 0;
		while (i < itemsOfObjectifs.size() && pondérationsOk) {
			Integer somme = 0;
			for (int j = 0; j < itemsOfObjectifs.get(i).size(); j++)
				somme = somme + itemsOfObjectifs.get(i).get(j).getPonderation();
			if (somme != objectifs.get(i).getPonderation())
				pondérationsOk = false;
			i++;
		}
		if (pondérationsOk) {
			for (int k = 0; k < objectifs.size(); k++) {
				for (int h = 0; h < itemsOfObjectifs.get(k).size(); h++)
					objectifs.get(k).getItems()
							.add(itemsOfObjectifs.get(k).get(h));
				objectifs.get(k).setId(metier.ajoutObjectif(objectifs.get(k)));
				objectifs.get(k).setStation(
						metier.chercherStationParId(idStationToAddObjectifs));
				metier.modifierObjectif(objectifs.get(k));
			}
			stationsOfUser.add(station);
			addMessage("Station", "déposée!");
			station = new Station();
			setStations(metier.listerStations());
			competences = new ArrayList<String>();
			idAuteurs = new ArrayList<String>();
			enseigns = new ArrayList<Enseignant>();
			items = new ArrayList<Item>();
			objctifs = new ArrayList<String>();
			objAfficher = new ArrayList<String>();
			lesObjectifs = new HashMap<String, String>();
			objectifs = new ArrayList<Objectif>();
			itemsOfObjectifs = new ArrayList<ArrayList<Item>>();
			FacesContext.getCurrentInstance().getExternalContext().getFlash()
					.setKeepMessages(true);
			return "/pages/enseignant/enshome?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erreur!",
					"Somme des pondérations erronée.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return "erreur";
		}

	}

	public void verifStation(int index) {
		if (nbObjectifs == null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erreur!",
					"Nombre d'objectifs requis.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			Enseignant e = null;
			for (int s1 = 0; s1 < idAuteurs.size(); s1++) {
				Integer id = Integer.parseInt(idAuteurs.get(s1));
				e = metier1.chercherEnseignantParId(id);
				enseigns.add(e);
			}
			this.activeIndex = index;
			verif = true;
		}
	}

	public void suivant(int index) {
		this.activeIndex = index;
	}

	public void handleFileUpload(FileUploadEvent event) {
		this.setFile(event.getFile());
		station.setFichier_joint(new FileStorageEntity(event.getFile()
				.getContentType(), event.getFile().getFileName(), event
				.getFile().getContents()));
		FacesMessage message = new FacesMessage("Succès", event.getFile()
				.getFileName() + " a été téléchargé.");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	/*
	 * public void suppItem(String description) { for (int h = 0; h <
	 * this.items.size(); h++) if
	 * (items.get(h).getDescription().equals(description)) {
	 * itemsSupp.add(items.get(h)); items.remove(h);
	 * System.out.print("suuuuuuuuuuuuuuuuuuuuuuup"); } }
	 */

	public String saveStation() {
		String competence = "";
		station.setDate(new Date());
		for (int j = 0; j < this.competences.size(); j++)
			competence = competences.get(j) + "-" + competence;
		station.setCompetence(competence);
		this.getStations().add(station);
		Integer ide = metier.ajoutStation(station);
		setNbObjToAddObjectifs(nbObjectifs);
		setIdStationToAddObjectifs(ide);
		station.setId(ide);
		System.out.println(station.getId().toString());
		System.out.println(enseigns.size());
		station.setAuteurs(enseigns);
		metier.modifierStation(station);
		return "/pages/enseignant/ajoutGrille?faces-redirect=true&includeViewParams=true";
	}

	public List<Enseignant> getEnseignantsList() {
		return metier1.listerEnseignants();
	}

	/*-------------------------------------------------------------------------------------------- Modifier station */

	public void verifStation1(int index) {
		Enseignant e = null;
		if (idAuteursM.size() == 0) {
			for (int s1 = 0; s1 < idAuteurs.size(); s1++) {
				Integer id = Integer.parseInt(idAuteurs.get(s1));
				e = metier1.chercherEnseignantParId(id);
				enseigns.add(e);
			}
		} else {
			for (int s1 = 0; s1 < idAuteursM.size(); s1++) {
				Integer id = Integer.parseInt(idAuteursM.get(s1));
				e = metier1.chercherEnseignantParId(id);
				enseigns.add(e);
			}
		}
		// itemsM = items;
		// objAfficherM = objAfficher;
		/*
		 * for (int s2 = 0; s2 < itemsSt.size(); s2++)
		 * itemsM.add(itemsSt.get(s2));
		 * 
		 * for (int s3 = 0; s3 < objAfficherSt.size(); s3++)
		 * objAfficherM.add(objAfficherSt.get(s3));
		 */
		if (competencesM.size() == 0)
			competencesM = competences;
		System.out.println("test domaine " + station.getDomaine());
		s = station;
		// / s.setObjectifs(metier.listerObjectifsParStation(station_pass));
		System.out.println("s id " + s.getId().toString());
		if (fichier != null)
			s.setFichier_joint(fichier);
		else
			s.setFichier_joint(station.getFichier_joint());
		this.activeIndex = index;
		verif = true;
	}

	/*public void ajoutItemM() {
		Objectif obje = metier.chercherObjParDescription(obj);
		if (obje == null) {
			obje = new Objectif(obj);
			if (!(this.getObjAfficherSt().contains(obj))) {
				this.getObjAfficherSt().add(obj);
			}
		}

		item.setObjectif(obje);

		if (lesObjectifs.get(obj) == null)
			lesObjectifs.put(obj, obj);
		this.getItemsSt().add(item);
		item = new Item();
		obj = null;
	}*/

	public void preModifierGrille() {
		List<Item> items = new ArrayList<Item>();
		listObjectifsStation = new ArrayList<Objectif>();

		listObjectifsStation = metier.listerObjectifsParStation(metier
				.chercherStationParId(Integer.parseInt(ids)));
		
		for(int i=0;i<listObjectifsStation.size();i++)
		{
			items=metier.listerItemsParObjectif(listObjectifsStation.get(i));
			listObjectifsStation.get(i).setItems(items);
		}

	}

	public void preModifierStation() {
		s = new Station();

		verif = false;
		activeIndex = 0;
		idAuteurs = new ArrayList<String>();
		competences = new ArrayList<String>();
		competencesM = new ArrayList<String>();
		/*
		 * items = new ArrayList<Item>(); lesObjectifs = new HashMap<String,
		 * String>(); objAfficher = new ArrayList<String>(); objctifs = new
		 * ArrayList<String>();
		 */

		Station station_pass = metier.chercherStationParId(Integer
				.parseInt(ids));
		metier.detachStation(station_pass);
		station = station_pass;
		// items = metier.listerItemsParStation(station);
		System.out.println(station.getDomaine());
		System.out.println(station.getId().toString());
		for (int j = 0; j < this.station.getCompetence().length(); j++)
			if (!station.getCompetence().substring(j, j + 1).equals("-"))
				competences.add(station.getCompetence().substring(j, j + 1));

		for (int s1 = 0; s1 < this.station.getAuteurs().size(); s1++) {
			System.out.println(station.getAuteurs().get(s1).getId().toString());
			idAuteurs.add(station.getAuteurs().get(s1).getId().toString());
		}

		// items = itemsS;

		/*
		 * for (int i = 0; i < this.getObjectifs().size(); i++)
		 * lesObjectifs.put(this.getObjectifs().get(i).getObjectif(), this
		 * .getObjectifs().get(i).getObjectif());
		 */

		/*
		 * for (int i = 0; i < items.size(); i++) { if
		 * (lesObjectifs.get(items.get(i).getObjectif().getObjectif()) == null)
		 * { lesObjectifs.put(items.get(i).getObjectif().getObjectif(),
		 * items.get(i).getObjectif().getObjectif()); } }
		 * 
		 * for (int h = 0; h < this.getItems().size(); h++) if
		 * (!(objAfficher.contains(this.getItems().get(h).getObjectif()
		 * .getObjectif()))) {
		 * objAfficher.add(this.getItems().get(h).getObjectif() .getObjectif());
		 * objctifs.add(this.getItems().get(h).getObjectif().getObjectif()); }
		 */
	}

	public String modStation() {

		String competence = "";
		s.setDate(station.getDate());

		for (int j = 0; j < this.competencesM.size(); j++)
			competence = competencesM.get(j) + "-" + competence;
		s.setCompetence(competence);

		/*
		 * for (int p = 0; p < this.objAfficherSt.size(); p++) { if
		 * (metier.chercherObjParDescription(objAfficherSt.get(p)) == null)
		 * metier.ajoutObjectif(new Objectif(objAfficherSt.get(p))); }
		 * 
		 * for (int h = 0; h < this.itemsSt.size(); h++) { Objectif obje =
		 * metier.chercherObjParDescription(itemsSt.get(h)
		 * .getObjectif().getObjectif());
		 * 
		 * itemsSt.get(h).setObjectif(obje); metier.ajoutItem(itemsSt.get(h));
		 * itemsSt.get(h).setStation(s); metier.modifierItem(itemsSt.get(h)); //
		 * ///////s.getItems().add(itemsM.get(h)); }
		 */
		/*
		 * for (int z = 0; z < this.itemsSupp.size(); z++) {
		 * metier.supprimerItem(this.itemsSupp.get(z).getId()); }
		 */
		// s.setItems(itemsStationM);
		System.out.println("taille auteurs   " + enseigns.size());
		// System.out.println("taille items " + itemsM.size());

		s.setAuteurs(enseigns);
		this.getStations().add(s);
		metier.modifierStation(s);

		addMessage("Station", "modifiée!");
		station = new Station();
		setStations(metier.listerStations());
		competences = new ArrayList<String>();
		competencesM = new ArrayList<String>();
		idAuteurs = new ArrayList<String>();
		idAuteursM = new ArrayList<String>();
		enseigns = new ArrayList<Enseignant>();
		/*
		 * items = new ArrayList<Item>(); objctifs = new ArrayList<String>();
		 * objAfficher = new ArrayList<String>(); lesObjectifs = new
		 * HashMap<String, String>();
		 */
		return "success";

	}
	
	public String modGrille() {

		for(int i=0;i<listObjectifsStation.size();i++)
		{
			for(int h = 0; h < listObjectifsStation.get(i).getItems().size(); h++)
				metier.modifierItem(listObjectifsStation.get(i).getItems().get(h));
			
		}

		addMessage("Grille", "modifiée!");
		station = new Station();
		setStations(metier.listerStations());

		return "success";

	}

	public void suppItemM(String description) {
		for (int h = 0; h < this.itemsM.size(); h++)
			if (itemsM.get(h).getDescription().equals(description)) {
				itemsSupp.add(itemsM.get(h));
				itemsM.remove(h);
			}
	}

	public void modifGrille() {
		grilleOnOff = true;
	}

	public void handleFileUpload1(FileUploadEvent event) {
		this.setFile(event.getFile());

		fichier = new FileStorageEntity(event.getFile().getContentType(), event
				.getFile().getFileName(), event.getFile().getContents());
		FacesMessage message = new FacesMessage("Succès", event.getFile()
				.getFileName() + " a été téléchargé.");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	/*---------------------------------------------------------------------------------- liste stations et gestion de ses station */

	public String supprimerStation(Integer id) {
		this.getStations().remove(metier.chercherStationParId(id));
		metier.supprimerStation(id);
		setStations(metier.listerStations());
		setStationsOfUser(metier.listerStationsParAuteur(ident));
		return "success";
	}

	public String validerS(Integer id) {
		station = metier.chercherStationParId(id);
		station.setValide(true);
		metier.modifierStation(station);
		addMessage("Station", "validée!");
		setStations(metier.listerStations());
		station = new Station();
		return "success";
	}

	/*-------------------------------------------------------------------------------------------- choix de stations pour un examen */
	public void listExamJury() {
		examensIdentIsJury = metier2.listerProchainsExamensWhereIsJury(ident);
	}

	public void listStationsEx(Examen e) {
		stationsEx = metier2.listeStationsExamens(e);
	}

	public boolean isJury(Integer id) {
		System.out.println("////////////////////////");
		Examen e = metier2.chercherExamenParId(id);
		List<Enseignant> listJ = metier2.listerJuryExamens(e);
		System.out.println(listJ.contains(ident));
		return listJ.contains(ident);
	}

	public List<Station> getStationsValid() {
		return metier.listerStationsValides();
	}

	public String enregistrerChoixStations() {
		Examen e = metier2.chercherExamenParId(idExamen);
		List<StationExamen> listeStatioExam = new ArrayList<StationExamen>();
		Integer somme = 0;
		Integer ids;
		for (int i = 0; i < listStationsExamen.size(); i++)
			somme = somme + listStationsExamen.get(i).getPonderation();
		for (int i = 0; i < listStationsExamen.size(); i++) {
			listStationsExamen.get(i).setExamen(e);
			ids = listStationsExamen.get(i).getStation().getId();
			listStationsExamen.get(i).setStationExamPK(
					new StationExamenPK(ids, idExamen));
		}

		if (somme == 100) {
			listeStatioExam = metier2.listeStationsExamensSuivantExam(e);
			if (!listeStatioExam.isEmpty()) {
				for (int j = 0; j < listeStatioExam.size(); j++)
					metier2.supprimerStationExamen(listeStatioExam.get(j));
			}
			e.setStations(listStationsExamen);
			metier2.modifierExamen(e);

			addMessage("Examen", "mis à jour!");
			FacesContext.getCurrentInstance().getExternalContext().getFlash()
					.setKeepMessages(true);
			return "/pages/enseignant/enshome?faces-redirect=true";

		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erreur!",
					"Somme des pondérations erronée.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return "erreur";
		}

	}

	public void suivant1(int index) {
		this.activeIndex1 = index;
	}

	public void caracteriserStationsExamen(int index) {

		// if (stationsEx.size() == 4) {
		listStationsExamen = new ArrayList<StationExamen>();
		System.out.println("kkkk3" + stationsEx.size());
		for (int s1 = 0; s1 < stationsEx.size(); s1++) {
			StationExamen s = new StationExamen();
			s.setStation(stationsEx.get(s1));
			listStationsExamen.add(s);

		}
		System.out.println("kkkk4" + listStationsExamen.size());
		this.activeIndex1 = index;
		verifStat = true;
		/*
		 * } else { FacesMessage message = new FacesMessage(
		 * FacesMessage.SEVERITY_ERROR, "Nombre selectionné erroné!",
		 * "Veuillez choisir 4 stations.");
		 * FacesContext.getCurrentInstance().addMessage(null, message); }
		 */
	}

	/*public String modifierExamen(RowEditEvent event) {
		List<Station> stati = new ArrayList<Station>();
		Station stat = new Station();
		Examen ex = new Examen();
		Examen exam = metier2.chercherExamenParId(((Examen) event.getObject())
				.getId());
		System.out.println("idExamen: " + exam.getId());
		ex = exam;
		if (idStations.length != 0) {
			for (int k = 0; k < idStations.length; k++) {
				Integer id = Integer.parseInt(idStations[k]);
				System.out.println("id: " + id);
				stat = metier.chercherStationParId(id);
				System.out.println("idStation Ch: " + stat.getId());
				System.out.print("Ex ajoute:::");
				stati.add(stat);

			}
			ex.setCircuit_stations(stati);
			metier2.modifierExamen(ex);
			this.setExamensProchains(metier2.listerProchainsExamens());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Succès!", "Examen mis à jour");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		idStations = null;
		return "success";
	}*/

	/*-------------------------------------------------------------------------------------------- les resultats */

	public void afficherResultat() {
		station = metier.chercherStationParId(Integer.parseInt(idSt));
		examen = metier2.chercherExamenParId(Integer.parseInt(idE));
		epreuves = metier3.listerEpreuvesParStationExamen(station, examen);
	}

	public List<Examen> getExamenPasses() {
		return metier2.listerExamensPasses();
	}

	public List<Station> getStationOfEx(Examen e) {
		return metier2.listeStationsExamens(e);
	}

	/*--------------------------------------------------------------------------------------------getters and setters*/

	public Integer getIdStationToAddObjectifs() {
		return idStationToAddObjectifs;
	}

	public StationToPDF getStationToPDF() {
		return stationToPDF;
	}

	public void setStationToPDF(StationToPDF stationToPDF) {
		this.stationToPDF = stationToPDF;
	}

	public List<Objectif> getListObjectifsStation() {
		return listObjectifsStation;
	}

	public void setListObjectifsStation(List<Objectif> listObjectifsStation) {
		this.listObjectifsStation = listObjectifsStation;
	}

	public boolean isVerifStat() {
		return verifStat;
	}

	public void setVerifStat(boolean verifStat) {
		this.verifStat = verifStat;
	}

	public List<StationExamen> getListStationsExamen() {
		return listStationsExamen;
	}

	public void setListStationsExamen(List<StationExamen> listeStationsExamen) {
		listStationsExamen = listeStationsExamen;
	}

	public Integer getIdExamen() {
		return idExamen;
	}

	public void setIdExamen(Integer idExamen) {
		this.idExamen = idExamen;
	}

	public List<ArrayList<Item>> getItemsDesObjectifs() {
		return itemsDesObjectifs;
	}

	public void setItemsDesObjectifs(List<ArrayList<Item>> itemsDesObjectifs) {
		this.itemsDesObjectifs = itemsDesObjectifs;
	}

	public List<Objectif> getObjectifsDeLaStation() {
		return objectifsDeLaStation;
	}

	public void setObjectifsDeLaStation(List<Objectif> objectifsDeLaStation) {
		this.objectifsDeLaStation = objectifsDeLaStation;
	}

	public List<ArrayList<Item>> getItemsOfObjectifs() {
		return itemsOfObjectifs;
	}

	public void setItemsOfObjectifs(List<ArrayList<Item>> itemsOfObjectifs) {
		this.itemsOfObjectifs = itemsOfObjectifs;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public void setIdStationToAddObjectifs(Integer idStationToAddObjectifs) {
		this.idStationToAddObjectifs = idStationToAddObjectifs;
	}

	public Integer getNbObjToAddObjectifs() {
		return nbObjToAddObjectifs;
	}

	public void setNbObjToAddObjectifs(Integer nbObjToAddObjectifs) {
		this.nbObjToAddObjectifs = nbObjToAddObjectifs;
	}

	public Station getStation() {
		return station;
	}

	public List<Objectif> getObjectifs() {
		return objectifs;
	}

	public void setObjectifs(List<Objectif> objectifs) {
		this.objectifs = objectifs;
	}

	public Integer getNbObjectifs() {
		return nbObjectifs;
	}

	public void setNbObjectifs(Integer nbObjectifs) {
		this.nbObjectifs = nbObjectifs;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public Station getS() {
		return s;
	}

	public void setS(Station s) {
		this.s = s;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<String> getCompetences() {
		return competences;
	}

	public void setCompetences(List<String> competences) {
		this.competences = competences;
	}

	public List<Station> getStations() {
		return stations;
	}

	public void setStations(List<Station> stations) {
		this.stations = stations;
	}

	public List<String> getIdAuteurs() {
		return idAuteurs;
	}

	public void setIdAuteurs(List<String> idAuteurs) {
		this.idAuteurs = idAuteurs;
	}

	public List<String> getIdAuteursM() {
		return idAuteurs;
	}

	public void setIdAuteursM(List<String> idAuteurs) {
		this.idAuteursM = idAuteurs;
	}

	public List<Examen> getExamensFiltres() {
		return examensFiltres;
	}

	public void setExamensFiltres(List<Examen> examensFiltres) {
		this.examensFiltres = examensFiltres;
	}

	public String[] getIdStations() {
		return idStations;
	}

	public void setIdStations(String[] idStations) {
		this.idStations = idStations;
	}

	public List<Station> getStationsFiltres() {
		return stationsFiltres;
	}

	public void setStationsFiltres(List<Station> stationsFiltres) {
		this.stationsFiltres = stationsFiltres;
	}

	public IdentiteBean getIdentiteBean() {
		return identiteBean;
	}

	public void setIdentiteBean(IdentiteBean identiteBean) {
		this.identiteBean = identiteBean;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Enseignant getIdent() {
		return ident;
	}

	public void setIdent(Enseignant ident) {
		this.ident = ident;
	}

	public String getnPwd() {
		return nPwd;
	}

	public void setnPwd(String nPwd) {
		this.nPwd = nPwd;
	}

	public String getObj() {
		return obj;
	}

	public void setObj(String obj) {
		this.obj = obj;
	}

	public Map<String, String> getLesObjectifs() {
		return lesObjectifs;
	}

	public void setLesObjectifs(Map<String, String> lesObjectifs) {
		this.lesObjectifs = lesObjectifs;
	}

	public List<String> getObjctifs() {
		return objctifs;
	}

	public void setObjctifs(List<String> objctifs) {
		this.objctifs = objctifs;
	}

	public boolean isVerif() {
		return verif;
	}

	public void setVerif(boolean verif) {
		this.verif = verif;
	}

	public void setVerifObj(boolean verif) {
		this.verifObj = verif;
	}

	public boolean isVerifObj() {
		return verifObj;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public int getActiveIndex1() {
		return activeIndex1;
	}

	public void setActiveIndex1(int activeIndex) {
		this.activeIndex1 = activeIndex;
	}

	public List<String> getObjAfficher() {
		return objAfficher;
	}

	public void setObjAfficher(List<String> objAfficher) {
		this.objAfficher = objAfficher;
	}

	public List<Enseignant> getEnseigns() {
		return enseigns;
	}

	public void setEnseigns(List<Enseignant> enseigns) {
		this.enseigns = enseigns;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getObjetMail() {
		return objetMail;
	}

	public void setObjetMail(String objetMail) {
		this.objetMail = objetMail;
	}

	public String getMsgMail() {
		return msgMail;
	}

	public void setMsgMail(String msgMail) {
		this.msgMail = msgMail;
	}

	public boolean isGrilleOnOff() {
		return grilleOnOff;
	}

	public void setGrilleOnOff(boolean grilleOnOff) {
		this.grilleOnOff = grilleOnOff;
	}

	public List<String> getCompetencesM() {
		return competencesM;
	}

	public void setCompetencesM(List<String> competencesM) {
		this.competencesM = competencesM;
	}

	public List<String> getObjAfficherM() {
		return objAfficherM;
	}

	public void setObjAfficherM(List<String> objAfficherM) {
		this.objAfficherM = objAfficherM;
	}

	public List<Item> getItemsM() {
		return itemsM;
	}

	public void setItemsM(List<Item> itemsM) {
		this.itemsM = itemsM;
	}

	public List<Station> getStationsOfUser() {
		return stationsOfUser;
	}

	public void setStationsOfUser(List<Station> stationsOfUser) {
		this.stationsOfUser = stationsOfUser;
	}

	public List<Item> getItemsStat() {
		return itemsStat;
	}

	public void setItemsStat(List<Item> itemsStat) {
		this.itemsStat = itemsStat;
	}

	public List<Enseignant> getAuteursStat() {
		return auteursStat;
	}

	public void setAuteursStat(List<Enseignant> auteursStat) {
		this.auteursStat = auteursStat;
	}

	public List<Station> getStationsEx() {
		return stationsEx;
	}

	public void setStationsEx(List<Station> stationsEx) {
		this.stationsEx = stationsEx;
	}

	public List<Item> getItemsS() {
		return itemsS;
	}

	public void setItemsS(List<Item> itemsS) {
		this.itemsS = itemsS;
	}

	public List<Item> getItemsSt() {
		return itemsSt;
	}

	public void setItemsSt(List<Item> itemsSt) {
		this.itemsSt = itemsSt;
	}

	public List<String> getObjAfficherSt() {
		return objAfficherSt;
	}

	public void setObjAfficherSt(List<String> objAfficherSt) {
		this.objAfficherSt = objAfficherSt;
	}

	public List<Item> getItemsSupp() {
		return itemsSupp;
	}

	public void setItemsSupp(List<Item> itemsSupp) {
		this.itemsSupp = itemsSupp;
	}

	public StreamedContent getDbImage() {
		return dbImage;
	}

	public void setDbImage(StreamedContent dbImage) {
		this.dbImage = dbImage;
	}

	public String getIdSt() {
		return idSt;
	}

	public void setIdSt(String idSt) {
		this.idSt = idSt;
	}

	public String getIdE() {
		return idE;
	}

	public void setIdE(String idE) {
		this.idE = idE;
	}

	public Examen getExamen() {
		return examen;
	}

	public void setExamen(Examen examen) {
		this.examen = examen;
	}

	public List<Epreuve> getEpreuves() {
		return epreuves;
	}

	public void setEpreuves(List<Epreuve> epreuves) {
		this.epreuves = epreuves;
	}

	public List<Epreuve> getEpreuvesFiltres() {
		return epreuvesFiltres;
	}

	public void setEpreuvesFiltres(List<Epreuve> epreuvesFiltres) {
		this.epreuvesFiltres = epreuvesFiltres;
	}

	public List<Examen> getExamensProchains() {
		return examensProchains;
	}

	public void setExamensProchains(List<Examen> examensProchains) {
		this.examensProchains = examensProchains;
	}

	public FileStorageEntity getFichier() {
		return fichier;
	}

	public void setFichier(FileStorageEntity fichier) {
		this.fichier = fichier;
	}

	public List<Examen> getExamensIdentIsJury() {
		return examensIdentIsJury;
	}

	public void setExamensIdentIsJury(List<Examen> examensIdentIsJury) {
		this.examensIdentIsJury = examensIdentIsJury;
	}

}
