package tn.crns.ecos.sessions.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tn.crns.ecos.model.Administrateur;
import tn.crns.ecos.model.Enseignant;
import tn.crns.ecos.model.Etudiant;
import tn.crns.ecos.model.Utilisateur;
import tn.crns.ecos.sessions.interfaces.UsersServiceLocal;
import tn.crns.ecos.sessions.interfaces.UsersServiceRemote;

/**
 * Session Bean implementation class UsersService
 */
@Stateless
public class UsersService implements UsersServiceLocal, UsersServiceRemote {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public UsersService() {

	}

	@Override
	public boolean ajoutEtudiant(Etudiant etudiant) {
		boolean b = false;
		try {
			em.persist(etudiant);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public boolean ajoutEnseignant(Enseignant enseignant) {
		boolean b = false;
		try {
			em.persist(enseignant);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public boolean ajoutAdmin(Administrateur admin) {
		boolean b = false;
		try {
			em.persist(admin);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public boolean suppEtudiant(Integer id) {
		boolean b = false;
		try {
			em.remove(chercherEtudiantParId(id));
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public boolean suppEnseignant(Integer id) {
		boolean b = false;
		try {
			em.remove(chercherEnseignantParId(id));
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public boolean suppAdmin(Integer id) {
		boolean b = false;
		try {
			em.remove(chercherAdminParId(id));
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public boolean modifierEtudiant(Etudiant etudiant) {
		boolean b = false;
		try {
			em.merge(etudiant);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public boolean modifierEnseignant(Enseignant enseignant) {
		boolean b = false;
		try {
			em.merge(enseignant);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public boolean modifierAdmin(Administrateur admin) {
		boolean b = false;
		try {
			em.merge(admin);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public List<Etudiant> listerEtudiants() {
		return em.createQuery("select e from Etudiant e", Etudiant.class)
				.getResultList();
	}

	@Override
	public List<Administrateur> listerAdmins() {
		return em.createQuery("select a from Administrateur a",
				Administrateur.class).getResultList();
	}

	@Override
	public List<Enseignant> listerEnseignants() {
		return em.createQuery("select e from Enseignant e", Enseignant.class)
				.getResultList();
	}

	@Override
	public Etudiant chercherEtudiantParId(Integer id) {
		return em.find(Etudiant.class, id);
	}

	@Override
	public Enseignant chercherEnseignantParId(Integer id) {
		return em.find(Enseignant.class, id);
	}

	@Override
	public Administrateur chercherAdminParId(Integer id) {
		return em.find(Administrateur.class, id);
	}

	@Override
	public Etudiant chercherEtudiantParCIN(Long cin) {
		Etudiant found = null;
		TypedQuery<Etudiant> query = em.createQuery(
				"select e from Etudiant e where e.cin=:param1", Etudiant.class);
		query.setParameter("param1", cin);

		try {
			found = query.getSingleResult();
		} catch (NoResultException ex) {
			System.out.println("Cin etudiant inexistant!");
		}
		return found;
	}

	@Override
	public Enseignant chercherEnseignantParCIN(Long cin) {
		Enseignant found = null;
		TypedQuery<Enseignant> query = em.createQuery(
				"select e from Enseignant e where e.cin=:param1",
				Enseignant.class);
		query.setParameter("param1", cin);

		try {
			found = query.getSingleResult();
		} catch (NoResultException ex) {
			System.out.println("Cin enseignant inexistant!");
		}
		return found;
	}

	@Override
	public Utilisateur authentifier(Long cin, String password) {
		Utilisateur utilisateur = null;
		Query query = em
				.createQuery("select u from Utilisateur u where u.cin=:cin and u.pwd=:pwd");
		query.setParameter("cin", cin).setParameter("pwd", password);
		try {
			utilisateur = (Utilisateur) query.getSingleResult();
		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(
					Level.WARNING,
					"authentication failed with cin=" + cin + " and password="
							+ password);
		}
		return utilisateur;
	}

	@Override
	public Utilisateur authentifierParCIN(Long cin) {
		Utilisateur utilisateur = null;
		Query query = em
				.createQuery("select u from Utilisateur u where u.cin=:cin");
		query.setParameter("cin", cin);
		try {
			utilisateur = (Utilisateur) query.getSingleResult();
		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING,
					"authentication failed with cin=" + cin);
		}
		return utilisateur;
	}

	@Override
	public boolean existeCin(Long cin) {
		boolean exists = false;
		String jpql = "select case when (count(u) > 0)  then true else false end from Utilisateur u where u.cin=:cin";
		Query query = em.createQuery(jpql);
		query.setParameter("cin", cin);
		exists = (Boolean) query.getSingleResult();
		return exists;

	}

	/* à verifier */
	@Override
	public List<Enseignant> membresComite() {
		TypedQuery<Enseignant> query = em.createQuery(
				"select e from Enseignant e where e.membre_comite=:param1",
				Enseignant.class);
		query.setParameter("param1", true);

		return query.getResultList();
	}

}
