package tn.crns.ecos.sessions.impl;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tn.crns.ecos.model.Epreuve;
import tn.crns.ecos.model.Etudiant;
import tn.crns.ecos.model.Examen;
import tn.crns.ecos.model.Item;
import tn.crns.ecos.model.Station;
import tn.crns.ecos.sessions.interfaces.EpreuveServiceLocal;

/**
 * Session Bean implementation class EpreuveService
 */
@Stateless
public class EpreuveService implements EpreuveServiceLocal{

	@PersistenceContext
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public EpreuveService() {

    }

	@Override
	public Integer ajoutEpreuve(Epreuve epreuve) {
		try {
			em.merge(epreuve);
	
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return epreuve.getId();
	}

	@Override
	public boolean modifierEpreuve(Epreuve epreuve) {
		boolean b = false;
		try {
			em.merge(epreuve);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public List<Epreuve> listerEpreuvesParStationExamen(Station station,
			Examen examen) {
		TypedQuery<Epreuve> query = em
				.createQuery(
						"select e from Epreuve e where e.station=:param1 and e.examen=:param2 ",
						Epreuve.class);
		query.setParameter("param1", station);
		query.setParameter("param2", examen);

		return query.getResultList();
	}


	@Override
	public List<Item> listeItemsEpreuve(Epreuve epreuve) {		
		List<Item> list = new ArrayList<Item>();
		Query query = em
				.createQuery("select e from Epreuve ep inner join ep.notes e where ep.id=:param1");
		query.setParameter("param1", epreuve.getId());
		for (int i = 0; i < query.getResultList().size(); i++) {
			list.add((Item) query.getResultList().get(i));
		}
		return list;
	}

	@Override
	public Epreuve chercherEpreuveParId(Integer id) {
		return em.find(Epreuve.class, id);
	}
	
	/*à verifier*/
	@Override
	public List<Epreuve> listerEpreuvesParStation(Station station) {

		TypedQuery<Epreuve> query = em
				.createQuery(
						"select e from Epreuve e where e.station=:param1",
						Epreuve.class);
		query.setParameter("param1", station);

		return query.getResultList();
		
	}
	/*à verifier*/
	@Override
	public List<Epreuve> listerEpreuvesParCandidat(Etudiant etudiant) {

		TypedQuery<Epreuve> query = em
				.createQuery(
						"select e from Epreuve e where e.candidat=:param1",
						Epreuve.class);
		query.setParameter("param1", etudiant);

		return query.getResultList();
		
	}

	@Override
	public Epreuve getEpreuveById(int id) {
	Epreuve e = em.find(Epreuve.class, id); 
		return e;
	}
}
