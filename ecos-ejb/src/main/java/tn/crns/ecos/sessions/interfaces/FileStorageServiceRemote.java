package tn.crns.ecos.sessions.interfaces;

import javax.ejb.Remote;

import tn.crns.ecos.outils.FileStorageEntity;

@Remote
public interface FileStorageServiceRemote {

	public FileStorageEntity chercherFichierParId(Long id);

	public boolean suppFichier(Long id);

	public boolean modifierFichier(FileStorageEntity fichier);

}
