package tn.crns.ecos.sessions.interfaces;

import java.util.List;

import javax.ejb.Remote;

import tn.crns.ecos.model.Enseignant;
import tn.crns.ecos.model.Item;
import tn.crns.ecos.model.Objectif;
import tn.crns.ecos.model.Station;

@Remote
public interface StationsServiceRemote {

	public Integer ajoutStation(Station station);

	public boolean ajoutItem(Item item);

	public Integer ajoutObjectif(Objectif obj);

	public boolean modifierStation(Station station);

	public boolean modifierItem(Item item);

	public boolean supprimerStation(Integer id);

	public boolean supprimerItem(Integer id);

	public Station chercherStationParId(Integer id);

	public List<Station> listerStations();

	public List<Station> listerStationsValides();

	public List<Station> listerStationsParAuteur(Enseignant enseignant);

	public List<Enseignant> listerAuteursParStation(Station station);

	public List<Item> listerItemsParStation(Station station);

	public List<Objectif> listerObjs();

	public Item chercherItemParId(Integer id);

	public List<Item> listerItemsParStationEtObje(Station station, Objectif obj);

	public Objectif chercherObjParId(Integer id);

	public Objectif chercherObjParDescription(String d);
	
	public boolean detachStation(Station s);
	boolean modifierObjectif(Objectif obj);
	List<Objectif> listerObjectifsParStation(Station station);
	List<Item> listerItemsParObjectif(Objectif o);
}
