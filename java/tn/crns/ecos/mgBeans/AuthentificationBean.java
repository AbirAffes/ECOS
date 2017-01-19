package tn.crns.ecos.mgBeans;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import tn.crns.ecos.model.Administrateur;
import tn.crns.ecos.model.Enseignant;
import tn.crns.ecos.model.Etudiant;
import tn.crns.ecos.outils.EmailSessionBean;
import tn.crns.ecos.sessions.interfaces.UsersServiceLocal;

@ManagedBean(name = "auth")
@RequestScoped
public class AuthentificationBean {

	/* attributs */

	@EJB
	private UsersServiceLocal usersServiceLocal;
	
	@EJB 
	private EmailSessionBean emailSessionBean; 

	@ManagedProperty("#{identity}")
	private IdentiteBean identiteBean;

	/*-------------------------------------------------------------------------------------------- authentification */
	private Long cin;
	private String password;

	/*----------------------------------------------------------------------------------- première authentification */
	private Long cinE;
	private String mail;
	private String pwd;

	/*------------------------------------------------------------------------------------------------ contacter */
	private String nom;
	private String prenom;
	private String tel;
	private String email;
	private String sujet;
	private String msg;

	/* constructeur */

	public AuthentificationBean() {
	}

	/* methodes */
	/*-------------------------------------------------------------------------------------------- authentification */

	public String doLogin() {
		String navigateTo = null;
		Object found = usersServiceLocal.authentifier(cin, password);
		if (found != null) {
			if (found instanceof Administrateur) {
				Administrateur foundAdmin = (Administrateur) usersServiceLocal
						.authentifier(cin, password);
				identiteBean.setUtilisateur(foundAdmin);
				
				
				if(foundAdmin.getAuto_admin())
					identiteBean.setRole("AdminA");
				if(foundAdmin.getAuto_et())
					identiteBean.setRole("AdminEt");
				if(foundAdmin.getAuto_en())
					identiteBean.setRole("AdminEn");
				if(foundAdmin.getAuto_ex())
					identiteBean.setRole("AdminEx");
				
				
				//identiteBean.setRole("Admin");
				identiteBean.setSurnom("administrateur");
				identiteBean.setHome("/pages/admin/adminhome");
				identiteBean.setProfil(null);
				identiteBean.setInscri(null);
				navigateTo = "/pages/admin/adminhome?faces-redirect=true";
			} else {
				if (found instanceof Etudiant) {
					Etudiant foundStudent = (Etudiant) usersServiceLocal
							.authentifier(cin, password);
					identiteBean.setUtilisateur(foundStudent);
					identiteBean.setRole("Etudiant");
					identiteBean.setSurnom(foundStudent.getNom() + " "
							+ foundStudent.getPrenom());
					identiteBean.setHome("/pages/etudiant/etudhome");
					identiteBean.setProfil("/pages/etudiant/etuProfile");
					identiteBean.setInscri("/pages/etudiant/inscrip");
					navigateTo = "/pages/etudiant/etudhome?faces-redirect=true";
				} else if (found instanceof Enseignant) {
					Enseignant foundprof = (Enseignant) usersServiceLocal
							.authentifier(cin, password);
					identiteBean.setUtilisateur(foundprof);
					identiteBean.setRole("Enseignant");
					identiteBean.setSurnom(foundprof.getNom() + " "
							+ foundprof.getPrenom());
					identiteBean.setHome("/pages/enseignant/enshome");
					identiteBean.setProfil("/pages/enseignant/ensProfile");
					identiteBean.setInscri(null);
					navigateTo = "/pages/enseignant/enshome?faces-redirect=true";
				}
			}
		} else {
			navigateTo = "/pages/auth/accueil?faces-redirect=true";
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Erreur", "Faux identifiants");
			FacesContext.getCurrentInstance().addMessage(null, message);
			FacesContext.getCurrentInstance().getExternalContext().getFlash()
					.setKeepMessages(true);
		}
		return navigateTo;
	}

	public String doLogout() {
		String navigateTo = null;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.clear();
		navigateTo = "/pages/auth/accueil?faces-redirect=true";
		return navigateTo;
	}

	/*-------------------------------------------------------------------------------------------- première authentification */

	public String doLoginFirst() {
		String retour=null;
		Object found = usersServiceLocal.authentifierParCIN(cinE);
		FacesMessage message = null;
		if (found != null) {
			if (found instanceof Etudiant) {
				Etudiant foundStudent = (Etudiant) usersServiceLocal
						.authentifierParCIN(cinE);
				mail = foundStudent.getMail();
				pwd=foundStudent.getPwd();
				message = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Mot de passe envoyé à ", mail);

			} else if (found instanceof Enseignant) {
				Enseignant foundprof = (Enseignant) usersServiceLocal
						.authentifierParCIN(cinE);
				mail = foundprof.getMail();
				pwd= foundprof.getPwd();
				message = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Mot de passe envoyé à ", mail);
			}
			String Newligne=System.getProperty("line.separator"); 
			emailSessionBean.sendEmail(mail, "Mot de passe plateforme ECOS", "Votre mot de passe est : "+pwd+Newligne+" \n Ce mot de passe a été généré automatiquement.  \n Après votre première connexion, il est fortement recommandé de le changer dans la page PROFILE.");
			System.out.println(mail);
			FacesContext.getCurrentInstance().addMessage(null, message);
			FacesContext.getCurrentInstance().getExternalContext().getFlash()
					.setKeepMessages(true);
			retour="/pages/auth/accueil?faces-redirect=true";
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erreur!",
					"CIN invalide");
			FacesContext.getCurrentInstance().addMessage(null, message);
			retour="success";
		}

		return retour;
	}

	/*-------------------------------------------------------------------------------------------- contacter */

	public void contacter() {
		//String sujet=null;
		emailSessionBean.sendEmail("affesabir@gmail.com", "aaaaa", "aaa");}

	/*-------------------------------------------------------------------------------------------- getters and setters */

	public String getPassword() {
		return password;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getCin() {
		return cin;
	}

	public void setCin(Long cin) {
		this.cin = cin;
	}

	public Long getCinE() {
		return cinE;
	}

	public void setCinE(Long cin) {
		this.cinE = cin;
	}

	public IdentiteBean getIdentiteBean() {
		return identiteBean;
	}

	public void setIdentiteBean(IdentiteBean identiteBean) {
		this.identiteBean = identiteBean;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSujet() {
		return sujet;
	}

	public void setSujet(String sujet) {
		this.sujet = sujet;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
