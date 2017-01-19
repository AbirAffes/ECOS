package tn.crns.ecos.sessions.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tn.crns.ecos.outils.FileStorageEntity;
import tn.crns.ecos.sessions.interfaces.FileStorageServiceLocal;
import tn.crns.ecos.sessions.interfaces.FileStorageServiceRemote;

/**
 * Session Bean implementation class FileStorageService
 */
@Stateless
public class FileStorageService implements FileStorageServiceRemote,
		FileStorageServiceLocal {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public FileStorageService() {
	}

	@Override
	public boolean suppFichier(Long id) {
		boolean b = false;
		try {
			em.remove(chercherFichierParId(id));
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public boolean modifierFichier(FileStorageEntity fichier) {
		boolean b = false;
		try {
			em.merge(fichier);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public FileStorageEntity chercherFichierParId(Long id) {
		return em.find(FileStorageEntity.class, id);
	}

}
