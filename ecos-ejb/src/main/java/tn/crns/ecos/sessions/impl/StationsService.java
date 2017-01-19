package tn.crns.ecos.sessions.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import tn.crns.ecos.model.Enseignant;
import tn.crns.ecos.model.Item;
import tn.crns.ecos.model.Objectif;
import tn.crns.ecos.model.Station;
import tn.crns.ecos.sessions.interfaces.StationsServiceLocal;
import tn.crns.ecos.sessions.interfaces.StationsServiceRemote;

/**
 * Session Bean implementation class StationsService
 */
@Stateless
public class StationsService implements StationsServiceLocal,
		StationsServiceRemote {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public StationsService() {

	}

	@Override
	public Integer ajoutStation(Station station) {
		try {
			em.persist(station);
			em.flush();
		} catch (Exception e) {
			System.out.println("Erreur ! " + e.getMessage());
		}
		return station.getId();
	}

	@Override
	public Integer ajoutObjectif(Objectif obj) {
		// boolean b = false;
		try {
			em.persist(obj);
			em.flush();
			// b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! " + e.getMessage());
		}
		return obj.getId();
	}

	@Override
	public boolean ajoutItem(Item item) {
		boolean b = false;
		try {
			em.persist(item);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! " + e.getMessage());
		}
		return b;
	}

	@Override
	public boolean detachStation(Station s) {
		boolean b = false;
		try {
			em.detach(s);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! " + e.getMessage());
		}
		return b;
	}

	@Override
	public boolean modifierStation(Station station) {
		boolean b = false;
		try {
			em.merge(station);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! " + e.getMessage());
		}
		return b;
	}

	@Override
	public boolean modifierObjectif(Objectif obj) {
		boolean b = false;
		try {
			em.merge(obj);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! " + e.getMessage());
		}
		return b;
	}

	@Override
	public boolean modifierItem(Item item) {
		boolean b = false;
		try {
			em.merge(item);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! " + e.getMessage());
		}
		return b;
	}

	@Override
	public boolean supprimerStation(Integer id) {
		boolean b = false;
		try {

			for (Enseignant ens : chercherStationParId(id).getAuteurs()) {
				ens.getStations().remove(chercherStationParId(id));
				em.merge(ens);
			}
			em.remove(chercherStationParId(id));
			em.flush();
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! " + e.getMessage());
		}
		return b;
	}

	@Override
	public boolean supprimerItem(Integer id) {
		boolean b = false;
		try {
			em.remove(chercherItemParId(id));
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! " + e.getMessage());
		}
		return b;
	}

	@Override
	public List<Station> listerStations() {
		return em.createQuery("select s from Station s", Station.class)
				.getResultList();
	}

	@Override
	public List<Station> listerStationsParAuteur(Enseignant enseignant) {
		TypedQuery<Station> query = em.createQuery(
				"select s from Station s where :param2 member of s.auteurs",
				Station.class);
		query.setParameter("param2", enseignant);
		return query.getResultList();
	}

	@Override
	public List<Station> listerStationsValides() {
		TypedQuery<Station> query = em
				.createQuery("select s from Station s where s.valide=:param1",
						Station.class);
		query.setParameter("param1", true);
		return query.getResultList();
	}

	@Override
	public List<Item> listerItemsParStation(Station station) {
		TypedQuery<Item> query = em.createQuery(
				"select i from Item i where i.station=:param1", Item.class);
		query.setParameter("param1", station);
		return query.getResultList();
	}

	@Override
	public List<Item> listerItemsParObjectif(Objectif o) {
		TypedQuery<Item> query = em.createQuery(
				"select i from Item i where i.objectif=:param1", Item.class);
		query.setParameter("param1", o);
		if (query.getResultList() != null)
			return query.getResultList();
		else
			return null;
	}

	@Override
	public List<Objectif> listerObjectifsParStation(Station station) {
		TypedQuery<Objectif> query = em.createQuery(
				"select o from Objectif o where o.station=:param1",
				Objectif.class);
		query.setParameter("param1", station);
		return query.getResultList();
	}

	@Override
	public List<Enseignant> listerAuteursParStation(Station station) {
		TypedQuery<Enseignant> query = em
				.createQuery(
						"select e from Enseignant e where :param1 member of e.stations",
						Enseignant.class);
		query.setParameter("param1", station);
		return query.getResultList();
	}

	@Override
	public Station chercherStationParId(Integer id) {
		return em.find(Station.class, id);
	}

	@Override
	public Objectif chercherObjParDescription(String d) {
		Objectif obj = null;
		TypedQuery<Objectif> query = em.createQuery(
				"select o from Objectif o where o.objectif=:param1",
				Objectif.class);
		query.setParameter("param1", d);
		List<Objectif> objs = query.getResultList();
		if (objs.size() > 0)
			obj = objs.get(0);
		return obj;
	}

	@Override
	public List<Objectif> listerObjs() {
		return em.createQuery("select o from Objectif o", Objectif.class)
				.getResultList();
	}

	/* à verifier */
	@Override
	public Item chercherItemParId(Integer id) {
		return em.find(Item.class, id);
	}

	/* à verifier */
	@Override
	public Objectif chercherObjParId(Integer id) {
		return em.find(Objectif.class, id);
	}

	/* à verifier */
	@Override
	public List<Item> listerItemsParStationEtObje(Station station, Objectif obj) {
		TypedQuery<Item> query = em
				.createQuery(
						"select i from Item i where i.station=:param1 and i.objectif:=param2",
						Item.class);
		query.setParameter("param1", station);
		query.setParameter("param2", obj);

		return query.getResultList();
	}
}
