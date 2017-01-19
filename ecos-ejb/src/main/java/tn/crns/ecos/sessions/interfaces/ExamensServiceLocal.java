package tn.crns.ecos.sessions.interfaces;

import java.util.List;

import javax.ejb.Local;

import tn.crns.ecos.model.Enseignant;
import tn.crns.ecos.model.Etudiant;
import tn.crns.ecos.model.Examen;
import tn.crns.ecos.model.Station;
import tn.crns.ecos.model.StationExamen;

@Local
public interface ExamensServiceLocal {

	public boolean ajoutExamen(Examen examen);

	public boolean modifierExamen(Examen examen);

	public boolean supprimerExamen(Integer id);

	public Examen chercherExamenParId(Integer id);

	public List<Examen> listerExamens();

	public List<Examen> listerProchainsExamens();

	public List<Examen> listerProchainsExamensG();

	public List<Examen> listerProchainsExamensP();

	public List<Examen> listerProchainsExamensC();

	public List<Examen> listerProchainsExamensM();

	public List<Examen> listerExamensPasses();

	public List<Examen> listerExamensParDate();

	public List<Enseignant> listerJuryExamens(Examen ex);

	public List<Etudiant> listerCandidatsExamen(Examen ex);

	public List<Station> listeStationsExamens(Examen ex);

	List<Examen> listerExamensParEnseignant(Enseignant enseignant);

	List<Examen> listerProchainsExamensWhereIsJury(Enseignant e);

	boolean supprimerStationExamen(StationExamen es);

	List<StationExamen> listeStationsExamensSuivantExam(Examen ex);

	Station avoirStationDeStationExam(StationExamen se);


}
