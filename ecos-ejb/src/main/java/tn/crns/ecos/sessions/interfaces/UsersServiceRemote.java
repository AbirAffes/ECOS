package tn.crns.ecos.sessions.interfaces;

import java.util.List;

import javax.ejb.Remote;

import tn.crns.ecos.model.Administrateur;
import tn.crns.ecos.model.Enseignant;
import tn.crns.ecos.model.Etudiant;
import tn.crns.ecos.model.Utilisateur;

@Remote
public interface UsersServiceRemote {

	public boolean ajoutEtudiant(Etudiant etudiant);

	public boolean ajoutEnseignant(Enseignant enseignant);

	public boolean ajoutAdmin(Administrateur admin);

	public boolean suppEtudiant(Integer id);

	public boolean suppEnseignant(Integer id);

	public boolean suppAdmin(Integer id);

	public boolean modifierEtudiant(Etudiant etudiant);

	public boolean modifierEnseignant(Enseignant enseignant);

	public boolean modifierAdmin(Administrateur admin);

	public List<Etudiant> listerEtudiants();

	public List<Enseignant> listerEnseignants();

	public List<Administrateur> listerAdmins();

	public Etudiant chercherEtudiantParId(Integer id);

	public Enseignant chercherEnseignantParId(Integer id);

	public Administrateur chercherAdminParId(Integer id);

	public Utilisateur authentifier(Long cin, String password);

	public Utilisateur authentifierParCIN(Long cin);

	public boolean existeCin(Long cin);

	public List<Enseignant> membresComite();

	public Enseignant chercherEnseignantParCIN(Long cin);

	public Etudiant chercherEtudiantParCIN(Long cin);
}
