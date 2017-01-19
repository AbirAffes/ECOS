package tn.crns.ecos.sessions.interfaces;

import javax.ejb.Local;

import tn.crns.ecos.outils.FileStorageEntity;

@Local
public interface FileStorageServiceLocal {

	public FileStorageEntity chercherFichierParId(Long id);

	public boolean suppFichier(Long id);

	public boolean modifierFichier(FileStorageEntity fichier);

}
