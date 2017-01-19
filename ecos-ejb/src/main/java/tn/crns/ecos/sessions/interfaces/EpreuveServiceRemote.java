package tn.crns.ecos.sessions.interfaces;

import java.util.List;

import javax.ejb.Remote;

import tn.crns.ecos.model.Epreuve;
import tn.crns.ecos.model.Etudiant;
import tn.crns.ecos.model.Examen;
import tn.crns.ecos.model.Item;
import tn.crns.ecos.model.Station;

@Remote
public interface EpreuveServiceRemote {

	public Integer ajoutEpreuve(Epreuve epreuve);

	public boolean modifierEpreuve(Epreuve epreuve);

	public Epreuve chercherEpreuveParId(Integer id);

	public List<Epreuve> listerEpreuvesParStationExamen(Station station,
			Examen examen);

	public List<Epreuve> listerEpreuvesParStation(Station station);

	public List<Epreuve> listerEpreuvesParCandidat(Etudiant etudiant);

	public List<Item> listeItemsEpreuve(Epreuve epreuve);

}
