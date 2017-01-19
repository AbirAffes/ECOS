package tn.crns.ecos.sessions.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tn.crns.ecos.model.Enseignant;
import tn.crns.ecos.model.Etudiant;
import tn.crns.ecos.model.Examen;
import tn.crns.ecos.model.Objectif;
import tn.crns.ecos.model.Station;
import tn.crns.ecos.model.StationExamen;
import tn.crns.ecos.model.StationExamenPK;
import tn.crns.ecos.sessions.interfaces.ExamensServiceLocal;
import tn.crns.ecos.sessions.interfaces.ExamensServiceRemote;

/**
 * Session Bean implementation class EvaluationService
 */
@Stateless
public class ExamensService implements ExamensServiceLocal,
		ExamensServiceRemote {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public ExamensService() {

	}

	@Override
	public boolean ajoutExamen(Examen examen) {
		boolean b = false;
		try {
			em.persist(examen);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public boolean modifierExamen(Examen examen) {
		boolean b = false;
		try {		
			em.merge(examen);
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	@Override
	public boolean supprimerExamen(Integer id) {
		boolean b = false;
		try {
			em.remove(chercherExamenParId(id));
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}
	
	@Override
	public boolean supprimerStationExamen(StationExamen es) {
		boolean b = false;
		try {
			StationExamenPK pk=new StationExamenPK();
			pk.setIdExamen(es.getExamen().getId());
			pk.setIdStation(es.getStation().getId());
			em.remove(em.find(StationExamen.class, pk));
			b = true;
		} catch (Exception e) {
			System.out.println("Erreur ! "+e.getMessage());
		}
		return b;
	}

	public List<Examen> listerExamens() {
		return em.createQuery("select e from Examen e", Examen.class)
				.getResultList();
	}

	@Override
	public List<Examen> listerExamensPasses() {
		TypedQuery<Examen> query = em.createQuery(
				"select e from Examen e where e.date<:param1", Examen.class);
		query.setParameter("param1", new Date());
		return query.getResultList();
	}

	@Override
	public List<Examen> listerExamensParDate() {
		TypedQuery<Examen> query = em.createQuery(
				"select e from Examen e where e.date=:param1", Examen.class);
		query.setParameter("param1", new Date());
		return query.getResultList();
	}

	@Override
	public List<Enseignant> listerJuryExamens(Examen ex) {
		List<Enseignant> list = new ArrayList<Enseignant>();
		Query query = em
				.createQuery("select e from Examen ex inner join ex.jury e where ex.id=:param1");
		query.setParameter("param1", ex.getId());
		for (int i = 0; i < query.getResultList().size(); i++) {
			list.add((Enseignant) query.getResultList().get(i));
		}
		return list;
	}
	
	@Override
	public List<Examen> listerExamensParEnseignant(Enseignant enseignant) {
		/* pour connaitre juste si un enseignant appartient au jury**************
		 TypedQuery<Examen> query = em.createQuery(
				"select e from Examen e where :param2 member of e.jury",
				Examen.class);
		query.setParameter("param2", enseignant);
		return query.getResultList();*/
		/* pour connaitre si un enseignant est president du jury*******************/
		TypedQuery<Examen> query = em.createQuery(
				"select e from Examen e where e.president=:param2",
				Examen.class);
		query.setParameter("param2", enseignant);
		return query.getResultList();
	}

	@Override
	public List<Station> listeStationsExamens(Examen ex) {
		List<Station> list = new ArrayList<Station>();
		Query query = em
				.createQuery("select s from StationExamen ste inner join ste.station s where ste.examen=:param1");
		query.setParameter("param1", ex);
		for (int i = 0; i < query.getResultList().size(); i++) {
			list.add((Station) query.getResultList().get(i));
		}
		return list;
	}
	
	@Override
	public List<StationExamen> listeStationsExamensSuivantExam(Examen ex) {
		List<StationExamen> list = new ArrayList<StationExamen>();
		Query query = em
				.createQuery("select s from StationExamen s where s.examen=:param1");
		query.setParameter("param1", ex);
		for (int i = 0; i < query.getResultList().size(); i++) {
			list.add((StationExamen) query.getResultList().get(i));
		}
		return list;
	}
	
	@Override
	public Station avoirStationDeStationExam(StationExamen se) {
		Station station = null;
		TypedQuery<Station> query = em
				.createQuery("select s from StationExamen ste inner join ste.station s where ste.idStation=:param1",Station.class);
		query.setParameter("param1", se.getStationExamPK().getIdStation());
	
		try {
			station = query.getSingleResult();
		} catch (NoResultException ex) {
			System.out.println("erreur S of SE!");
		}

		return station;
	}

	@Override
	public List<Etudiant> listerCandidatsExamen(Examen ex) {
		List<Etudiant> list = new ArrayList<Etudiant>();
		Query query = em
				.createQuery("select c from Examen ex inner join ex.candidats c where ex.id=:param1");
		query.setParameter("param1", ex.getId());
		for (int i = 0; i < query.getResultList().size(); i++)
			list.add((Etudiant) query.getResultList().get(i));
		return list;
	}

	@Override
	public List<Examen> listerProchainsExamens() {
		TypedQuery<Examen> query = em.createQuery(
				"select e from Examen e where e.date>=:param1", Examen.class);
		query.setParameter("param1", new Date());
		return query.getResultList();
	}
	
	@Override
	public List<Examen> listerProchainsExamensWhereIsJury(Enseignant e) {
		/* chercher les exams ou eest du jury***************
		 TypedQuery<Examen> query = em.createQuery(
				"select e from Examen e where e.date>=:param1 and :param2 member of e.jury", Examen.class);
		query.setParameter("param1", new Date());
		query.setParameter("param2", e);
		return query.getResultList();*/
	/*chercher les exams ou e president*/
		TypedQuery<Examen> query = em.createQuery(
				"select e from Examen e where e.date>=:param1 and e.president=:param2", Examen.class);
		query.setParameter("param1", new Date());
		query.setParameter("param2", e);
		return query.getResultList();
	}

	@Override
	public List<Examen> listerProchainsExamensM() {
		TypedQuery<Examen> query = em
				.createQuery(
						"select e from Examen e where e.date>=:param1 and e.domaine='M'",
						Examen.class);
		query.setParameter("param1", new Date());
		return query.getResultList();
	}

	@Override
	public List<Examen> listerProchainsExamensC() {
		TypedQuery<Examen> query = em
				.createQuery(
						"select e from Examen e where e.date>=:param1 and e.domaine='C'",
						Examen.class);
		query.setParameter("param1", new Date());
		return query.getResultList();
	}

	@Override
	public List<Examen> listerProchainsExamensP() {
		TypedQuery<Examen> query = em
				.createQuery(
						"select e from Examen e where e.date>=:param1 and e.domaine='P'",
						Examen.class);
		query.setParameter("param1", new Date());
		return query.getResultList();
	}

	@Override
	public List<Examen> listerProchainsExamensG() {
		TypedQuery<Examen> query = em
				.createQuery(
						"select e from Examen e where e.date>=:param1 and e.domaine='G'",
						Examen.class);
		query.setParameter("param1", new Date());
		return query.getResultList();
	}
	

	@Override
	public Examen chercherExamenParId(Integer id) {
		return em.find(Examen.class, id);
	}

}
