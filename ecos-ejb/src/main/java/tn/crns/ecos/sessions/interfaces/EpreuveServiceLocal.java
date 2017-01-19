package tn.crns.ecos.sessions.interfaces;

import java.util.List;

import javax.ejb.Local;

import tn.crns.ecos.model.Epreuve;
import tn.crns.ecos.model.Etudiant;
import tn.crns.ecos.model.Examen;
import tn.crns.ecos.model.Item;
import tn.crns.ecos.model.Station;

@Local
public interface EpreuveServiceLocal {

	public Integer ajoutEpreuve(Epreuve epreuve);

	public boolean modifierEpreuve(Epreuve epreuve);

	public Epreuve chercherEpreuveParId(Integer id);

	public List<Epreuve> listerEpreuvesParStationExamen(Station station,
			Examen examen);

	public List<Epreuve> listerEpreuvesParStation(Station station);

	public List<Epreuve> listerEpreuvesParCandidat(Etudiant etudiant);

	public List<Item> listeItemsEpreuve(Epreuve epreuve);

	public Epreuve getEpreuveById (int id); 
}
