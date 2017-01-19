package tn.crns.ecos.mgBeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;

import tn.crns.ecos.model.Administrateur;
import tn.crns.ecos.model.Enseignant;
import tn.crns.ecos.model.Etudiant;
import tn.crns.ecos.model.Examen;
import tn.crns.ecos.outils.QRcodeGenerator;
import tn.crns.ecos.outils.RandomString;
import tn.crns.ecos.sessions.interfaces.ExamensServiceLocal;
import tn.crns.ecos.sessions.interfaces.UsersServiceLocal;

@ManagedBean(name = "adminMB")
@ViewScoped
public class AdminMB implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* attributs */

	@EJB
	private UsersServiceLocal metier;

	@EJB
	private ExamensServiceLocal metier2;

	QRcodeGenerator qrG = new QRcodeGenerator();
	@ManagedProperty("#{identity}")
	private IdentiteBean identiteBean;
	/*-------------------------------------------------------------------------------------------- gestion enseignants */

	private Enseignant ens;
	private List<Enseignant> enseignantsFiltres;
	private List<Enseignant> enseignants;

	/*-------------------------------------------------------------------------------------------- gestion etudiants */

	private Etudiant etu;
	private List<Etudiant> etudiantsFiltres;
	private List<Etudiant> etudiants;

	/*-------------------------------------------------------------------------------------------- gestion admins */

	private Administrateur admin;
	private List<Administrateur> admins;

	/*-------------------------------------------------------------------------------------------- gestion examens */

	private Examen ex;
	private List<Examen> examensFiltres;
	private List<Examen> examens;
	private List<String> idJury;
	private List<Enseignant> jurys;
	private List<Etudiant> candidats;
	private String idPresident;

	/* init() */

	@PostConstruct
	public void init() {

		ens = new Enseignant();
		etu = new Etudiant();
		admin = new Administrateur();
		ex = new Examen();

		idJury = new ArrayList<String>();

		setEnseignants(metier.listerEnseignants());
		setEtudiants(metier.listerEtudiants());
		setAdmins(metier.listerAdmins());
		setExamens(metier2.listerExamens());

	}

	/* methodes */
	public Administrateur getIdentite() {
		return metier.chercherAdminParId(identiteBean.getUtilisateur().getId());
	}

	/*-------------------------------------------------------------------------------------------- Add Message growl */

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	/*-------------------------------------------------------------------------------------------- gestion enseignants */

	public String ajouterEnseignant() {
		boolean existe = metier.existeCin(ens.getCin());
		if (existe == false) {
			byte[] img = qrG.genererQRcode(ens.getCin().toString(),
					"C:/Users/Workstation/Desktop/enseignants/"
							+ ens.getCin().toString() + ".png"); // à changer
			ens.setPwd(new RandomString().nextString());
			ens.setImg(img);
			this.getEnseignants().add(ens);
			metier.ajoutEnseignant(ens);
			ens = new Enseignant();
			setEnseignants(metier.listerEnseignants());
			addMessage("Nouvel enseignant", "ajouté!");
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Erreur", "Cin existant " + ens.getCin().toString() + "!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return "success";
	}

	public String modifierEnseignant(RowEditEvent event) {
		Integer id = null;
		boolean existe = metier.existeCin(((Enseignant) event.getObject())
				.getCin());
		if (existe)
			id = metier.authentifierParCIN(
					(((Enseignant) event.getObject()).getCin())).getId();

		if ((existe == false)
				|| (id == ((Enseignant) event.getObject()).getId())) {

			File MyFile = new File("C:/Users/Workstation/Desktop/enseignants/"
					+ metier.chercherEnseignantParId(
							((Enseignant) event.getObject()).getId()).getCin()
							.toString() + ".png");
			MyFile.delete();
			byte[] img = qrG.genererQRcode(((Enseignant) event.getObject())
					.getCin().toString(),
					"C:/Users/Workstation/Desktop/enseignants/"
							+ ((Enseignant) event.getObject()).getCin()
									.toString() + ".png"); // à changer
			((Enseignant) event.getObject()).setImg(img);
			metier.modifierEnseignant((Enseignant) event.getObject());
			setEnseignants(metier.listerEnseignants());
			addMessage("Enseignant", "modifié!");
		} else {
			setEnseignants(metier.listerEnseignants());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Erreur", "Cin existant!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

		return "success";
	}

	public String supprimerEnseignant(Integer id) {
		File MyFile = new File("C:/Users/Workstation/Desktop/enseignants/"
				+ metier.chercherEnseignantParId(id).getCin().toString()
				+ ".png");
		MyFile.delete();
		this.getEnseignants().remove(metier.chercherEnseignantParId(id));
		metier.suppEnseignant(id);
		setEnseignants(metier.listerEnseignants());
		addMessage("Enseignant", "supprimé!");
		return "success";
	}

	/*-------------------------------------------------------------------------------------------- gestion etudiants */
	
	public String ajouterEtudiant() {
		boolean existe = metier.existeCin(etu.getCin());
		if (existe == false) {
			byte[] img = qrG.genererQRcode(etu.getCin().toString(),
					"C:/Users/Workstation/Desktop/etudiants/"
							+ etu.getCin().toString() + ".png"); // à changer
			etu.setImg(img);
			etu.setPwd(new RandomString().nextString());
			this.getEtudiants().add(etu);
			metier.ajoutEtudiant(etu);
			etu = new Etudiant();
			setEtudiants(metier.listerEtudiants());
			addMessage("Nouvel étudiant", "ajouté!");
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Erreur", "Cin existant " + etu.getCin().toString() + "!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return "success";
	}

	public String modifierEtudiant(RowEditEvent event) {

		Integer id = null;
		boolean existe = metier.existeCin(((Etudiant) event.getObject())
				.getCin());
		if (existe)
			id = metier.authentifierParCIN(
					(((Etudiant) event.getObject()).getCin())).getId();

		if ((existe == false) || (id == ((Etudiant) event.getObject()).getId())) {

			File MyFile = new File("C:/Users/Workstation/Desktop/etudiants/"
					+ metier.chercherEtudiantParId(
							((Etudiant) event.getObject()).getId()).getCin()
							.toString() + ".png");
			MyFile.delete();
			byte[] img = qrG.genererQRcode(((Etudiant) event.getObject())
					.getCin().toString(),
					"C:/Users/Workstation/Desktop/etudiants/"
							+ ((Etudiant) event.getObject()).getCin()
									.toString() + ".png"); // à
															// changer
			((Etudiant) event.getObject()).setImg(img);
			metier.modifierEtudiant((Etudiant) event.getObject());
			setEtudiants(metier.listerEtudiants());
			addMessage("Etudiant", "modifié!");

		} else {
			setEtudiants(metier.listerEtudiants());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Erreur", "Cin existant!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

		return "success";
	}

	public String supprimerEtudiant(Integer id) {
		File MyFile = new File("C:/Users/Workstation/Desktop/etudiants/"
				+ metier.chercherEtudiantParId(id).getCin().toString() + ".png");
		MyFile.delete();
		this.getEtudiants().remove(metier.chercherEtudiantParId(id));
		metier.suppEtudiant(id);
		setEtudiants(metier.listerEtudiants());
		addMessage("Etudiant", "supprimé!");
		return "success";
	}

	/*-------------------------------------------------------------------------------------------- gestion admins */

	public String ajouterAdmin() {
		boolean existe = metier.existeCin(admin.getCin());
		if (existe == false) {
			this.getAdmins().add(admin);
			metier.ajoutAdmin(admin);
			admin = new Administrateur();
			setAdmins(metier.listerAdmins());
			addMessage("Nouvel administrateur", "ajouté!");
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Erreur", "Cin existant!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return "success";
	}

	public String modifierAdmin(RowEditEvent event) {

		Integer id = null;
		boolean existe = metier.existeCin(((Administrateur) event.getObject())
				.getCin());
		if (existe)
			id = metier.authentifierParCIN(
					(((Administrateur) event.getObject()).getCin())).getId();

		if ((existe == false)
				|| (id == ((Administrateur) event.getObject()).getId())) {

			metier.modifierAdmin((Administrateur) event.getObject());
			setAdmins(metier.listerAdmins());
			addMessage("Admin", "modifié!");

		} else {
			setAdmins(metier.listerAdmins());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Erreur", "Cin existant!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

		return "success";
	}

	public String supprimerAdmin(Integer id) {
		this.getAdmins().remove(metier.chercherAdminParId(id));
		metier.suppAdmin(id);
		setAdmins(metier.listerAdmins());
		addMessage("Admin", "supprimé!");
		return "success";
	}

	/*-------------------------------------------------------------------------------------------- gestion examens */	
	public String ajouterExamen() {
		for (int s = 0; s < idJury.size(); s++) {
			Integer id = Integer.parseInt(idJury.get(s));
			ex.getJury().add(metier.chercherEnseignantParId(id));
		}
		Integer idP = Integer.parseInt(idPresident);
		ex.setPresident(metier.chercherEnseignantParId(idP));
		this.getExamens().add(ex);
		metier2.ajoutExamen(ex);
		ex = new Examen();
		setExamens(metier2.listerExamens());
		idJury = new ArrayList<String>();
		idPresident = null;
		addMessage("Nouvel examen", "créé!");
		return "success";
	}

	public String supprimerExamen(Integer id) {
		this.getExamens().remove(metier2.chercherExamenParId(id));
		metier2.supprimerExamen(id);
		setExamens(metier2.listerExamens());
		addMessage("Examen", "supprimé!");
		return "success";
	}

	public String modifierExamen(RowEditEvent event) {
		List<Enseignant> enseign = new ArrayList<Enseignant>();
		Examen exam = metier2.chercherExamenParId(((Examen) event.getObject())
				.getId());
		//exam.setJury(metier2.listerJuryExamens(exam));
		exam.setHoraire(((Examen) event.getObject()).getHoraire());
		exam.setDate(((Examen) event.getObject()).getDate());
		exam.setDebut_inscri(((Examen) event.getObject()).getDebut_inscri());
		exam.setFin_inscri(((Examen) event.getObject()).getFin_inscri());
		exam.setDomaine(((Examen) event.getObject()).getDomaine());
		exam.setNbMax(((Examen) event.getObject()).getNbMax());
		if (idPresident != null) {
			Integer idP = Integer.parseInt(idPresident);
			exam.setPresident(metier.chercherEnseignantParId(idP));
		}
		if (idJury.size() > 0) {
			for (int k = 0; k < idJury.size(); k++) {
				Integer id = Integer.parseInt(idJury.get(k));
				enseign.add(metier.chercherEnseignantParId(id));
			}

			/*
			 * exam.getJury().clear(); for (int k1 = 0; k1 < enseign.size();
			 * k1++) exam.getJury().add(enseign.get(k1));
			 */
			exam.setJury(enseign);
		}

		metier2.modifierExamen(exam);
		setExamens(metier2.listerExamens());
		idJury = new ArrayList<String>();
		idPresident = null;
		addMessage("Examen", "modifié!");

		return "success";
	}

	public void listCandidats(Examen e) {
		candidats = metier2.listerCandidatsExamen(e);
	}

	public void listJuryE(Examen e) {
		System.out.println("////");
		jurys = metier2.listerJuryExamens(e);
	}

	public List<Enseignant> getEnseignantsList() {
		return metier.listerEnseignants();
	}

	/*-------------------------------------------------------------------------------------------- fichier Excel */

	public void insertEnseignantsViaExcel(FileUploadEvent event) {
		XSSFWorkbook workbook = null;
		XSSFSheet sheet;
		// XSSFCell poNum;
		// XSSFRow row1;
		Iterator<Row> rowIterator;
		Long cin = null;
		String nom = null;
		String prenom = null;
		String mail = null;
		String aff = null;
		String grade = null;
		boolean erreur = false;
		try {
			workbook = new XSSFWorkbook(event.getFile().getInputstream());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = workbook.getSheetAt(0); // index de la feuille / tjrs la
										// première feuille

		FormulaEvaluator evaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();
		sheet = workbook.getSheetAt(0);
		rowIterator = sheet.iterator();
		Row row = rowIterator.next();
		while (rowIterator.hasNext() && !erreur) {
			row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext() && !erreur) {
				Cell cell = cellIterator.next();
				switch (cell.getColumnIndex()) {
				case 0:
					if (evaluator.evaluateInCell(cell).getCellType() == Cell.CELL_TYPE_NUMERIC) {
						cin = new Long((long) cell.getNumericCellValue());
						System.out.print(cin + " ");
					} else {
						erreur = true;
					}
					break;
				case 1:
					if (evaluator.evaluateInCell(cell).getCellType() == Cell.CELL_TYPE_STRING) {
						nom = cell.getStringCellValue();
						System.out.print(nom + " ");
					}
					break;
				case 2:
					if (evaluator.evaluateInCell(cell).getCellType() == Cell.CELL_TYPE_STRING) {
						prenom = cell.getStringCellValue();
						System.out.print(prenom + " ");
					}
					break;
				case 3:
					if (evaluator.evaluateInCell(cell).getCellType() == Cell.CELL_TYPE_STRING) {
						mail = cell.getStringCellValue();
						System.out.print(mail + " ");
					}
					break;
				case 4:
					if (evaluator.evaluateInCell(cell).getCellType() == Cell.CELL_TYPE_STRING) {
						aff = cell.getStringCellValue();
						System.out.print(aff + " ");
					}
					break;
				case 5:
					if (evaluator.evaluateInCell(cell).getCellType() == Cell.CELL_TYPE_STRING) {
						grade = cell.getStringCellValue();
						System.out.print(grade + " ");
					}
					break;
				default:
					break;
				}
			}
			System.out.println("");
			ens = new Enseignant();
			ens.setCin(cin);
			ens.setNom(nom);
			ens.setPrenom(prenom);
			ens.setMail(mail);
			ens.setAffiliation(aff);
			ens.setGrade(grade);
			ens.setMembre_comite(false);
			ens.setPresident_comite(false);
			this.ajouterEnseignant();

		}
	}

	public void insertEtudiantsViaExcel(FileUploadEvent event) {
		XSSFWorkbook workbook = null;
		XSSFSheet sheet;
		// XSSFCell poNum;
		// XSSFRow row1;
		Iterator<Row> rowIterator;
		Long cin = null;
		String nom = null;
		String prenom = null;
		String mail = null;
		boolean erreur = false;
		try {
			workbook = new XSSFWorkbook(event.getFile().getInputstream());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = workbook.getSheetAt(0); // index de la feuille / tjrs la
										// première feuille

		FormulaEvaluator evaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();
		sheet = workbook.getSheetAt(0);
		rowIterator = sheet.iterator();
		Row row = rowIterator.next();
		while (rowIterator.hasNext() && !erreur) {
			row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext() && !erreur) {
				Cell cell = cellIterator.next();
				switch (cell.getColumnIndex()) {
				case 0:
					if (evaluator.evaluateInCell(cell).getCellType() == Cell.CELL_TYPE_NUMERIC) {
						cin = new Long((long) cell.getNumericCellValue());
						System.out.print(cin + " ");
					} else {
						erreur = true;
					}
					break;
				case 1:
					if (evaluator.evaluateInCell(cell).getCellType() == Cell.CELL_TYPE_STRING) {
						nom = cell.getStringCellValue();
						System.out.print(nom + " ");
					}
					break;
				case 2:
					if (evaluator.evaluateInCell(cell).getCellType() == Cell.CELL_TYPE_STRING) {
						prenom = cell.getStringCellValue();
						System.out.print(prenom + " ");
					}
					break;
				case 3:
					if (evaluator.evaluateInCell(cell).getCellType() == Cell.CELL_TYPE_STRING) {
						mail = cell.getStringCellValue();
						System.out.print(mail + " ");
					}
					break;
				default:
					break;
				}
			}
			System.out.println("");
			etu = new Etudiant();
			etu.setCin(cin);
			etu.setNom(nom);
			etu.setPrenom(prenom);
			etu.setMail(mail);

			this.ajouterEtudiant();
		}

	}

	/*-------------------------------------------------------------------------------------------- getters and setters */

	public List<Enseignant> getEnseignantsFiltres() {
		return enseignantsFiltres;
	}

	public void setEnseignantsFiltres(List<Enseignant> enseignantsFiltres) {
		this.enseignantsFiltres = enseignantsFiltres;
	}

	public List<Etudiant> getEtudiantsFiltres() {
		return etudiantsFiltres;
	}

	public void setEtudiantsFiltres(List<Etudiant> etudiantsFiltres) {
		this.etudiantsFiltres = etudiantsFiltres;
	}

	public List<Examen> getExamensFiltres() {
		return examensFiltres;
	}

	public void setExamensFiltres(List<Examen> examensFiltres) {
		this.examensFiltres = examensFiltres;
	}

	public Enseignant getEns() {
		return ens;
	}

	public void setEns(Enseignant ens) {
		this.ens = ens;
	}

	public Etudiant getEtu() {
		return etu;
	}

	public void setEtu(Etudiant etu) {
		this.etu = etu;
	}

	public Administrateur getAdmin() {
		return admin;
	}

	public void setAdmin(Administrateur admin) {
		this.admin = admin;
	}

	public Examen getEx() {
		return ex;
	}

	public void setEx(Examen ex) {
		this.ex = ex;
	}

	public List<String> getIdJury() {
		return idJury;
	}

	public void setIdJury(List<String> idJury) {
		this.idJury = idJury;
	}

	public List<Enseignant> getEnseignants() {
		return enseignants;
	}

	public void setEnseignants(List<Enseignant> enseignants) {
		this.enseignants = enseignants;
	}

	public List<Etudiant> getEtudiants() {
		return etudiants;
	}

	public void setEtudiants(List<Etudiant> etudiants) {
		this.etudiants = etudiants;
	}

	public List<Administrateur> getAdmins() {
		return admins;
	}

	public void setAdmins(List<Administrateur> admins) {
		this.admins = admins;
	}

	public List<Examen> getExamens() {
		return examens;
	}

	public void setExamens(List<Examen> examens) {
		this.examens = examens;
	}

	public List<Etudiant> getCandidats() {
		return candidats;
	}

	public void setCandidats(List<Etudiant> candidats) {
		this.candidats = candidats;
	}

	public List<Enseignant> getJurys() {
		return jurys;
	}

	public void setJurys(List<Enseignant> jurys) {
		this.jurys = jurys;
	}

	public String getIdPresident() {
		return idPresident;
	}

	public void setIdPresident(String idPresident) {
		this.idPresident = idPresident;
	}

	public IdentiteBean getIdentiteBean() {
		return identiteBean;
	}

	public void setIdentiteBean(IdentiteBean identiteBean) {
		this.identiteBean = identiteBean;
	}

}
