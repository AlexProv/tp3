package ligueBaseball;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

/**
 *  * @author Mathieu Lavoie, Alex Provencher et Vincent Gagnon
 * classe intermedaire entre l'usager et les object qui parle a la base de donnee.
 */
public class GestionMatch {
	

	private Match match;
	private Equipe equipe;
	private Connexion cx;
	
	/**
	 * Creation d'une instance
	 */
	public GestionMatch(Match match, Equipe equipe) throws LigueBaseballException {
		this.cx = match.getConnexion();
		this.match = match;
		this.equipe = equipe;
	}
	
	/**
	 * fait la verification et envoie la commande pour ajouter un match
	 * @param matchDate
	 * @param matchHeure
	 * @param equipeLocal
	 * @param equipeVisiteur
	 * @throws SQLException
	 */
	public void ajout(Date matchDate, Time matchHeure, String equipeLocal, String equipeVisiteur) throws SQLException{
		try {
			int equipeLocalId = equipe.existe(equipeLocal);
			if (equipeLocalId == -1)
				throw new LigueBaseballException("Equipe local n'existe pas: "
						+ equipeLocal);
			int equipeVisiteurId = equipe.existe(equipeVisiteur);
			if (equipeVisiteurId == -1)
				throw new LigueBaseballException("Equipe visiteur n'existe pas: "
						+ equipeVisiteur);
			
			int matchID = match.existe(matchDate, matchHeure, equipeLocal, equipeVisiteur); 
			if (matchID > -1)
			    throw new LigueBaseballException("Match existe deja: ");

			
			int matchId = match.maxMatch();
			match.ajoutMatch(matchId, equipeLocalId, equipeVisiteurId, matchDate, matchHeure);
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
		}
	}
	
	/**
	 * fait la verification et modifie le pointage d'un match
	 * @param matchDate
	 * @param matchHeure
	 * @param equipeLocal
	 * @param equipeVisiteur
	 * @param pointsLocal
	 * @param pointsVisiteur
	 * @throws SQLException
	 */
	public void entrerPointage(Date matchDate, Time matchHeure, String equipeLocal, String equipeVisiteur, int pointsLocal, int pointsVisiteur) throws SQLException{
		try {
			int matchId = match.existe(matchDate, matchHeure, equipeLocal, equipeVisiteur);
			if(matchId == -1)
				throw new LigueBaseballException("Match n'existe pas!");
			if(pointsLocal >= 0 && pointsVisiteur > 0)
				match.updatePointage(matchId, pointsLocal, pointsVisiteur);
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
		}
	}

	/**
	 * affiche le resultat du match a la console
	 * @throws SQLException
	 * @throws LigueBaseballException 
	 */
	public void afficherResultatDate() throws SQLException, LigueBaseballException {
		List<TupleMatch> list = match.afficherResultat();
		//if(list.size()==0)
		    
		System.out.println("Local-Visiteur : Liste des arbitres");
		for (TupleMatch tupleMatch : list) {
			System.out.println(tupleMatch.PointageLocal + "-" + tupleMatch.PointageVisiteur + " : " + tupleMatch.ListeArbitres);
		}
	}
	
	/**
	 * affiche le resultat du match a la date en parametre a la console
	 * @param date
	 * @throws SQLException
	 * @throws LigueBaseballException 
	 */
	public void afficherResultatDate(Date date) throws SQLException, LigueBaseballException {
		List<TupleMatch> list = match.afficherResultat(date);
		if(list.size()==0){
		    throw new LigueBaseballException("Il n'y a aucun match a afficher");
		}
		
		System.out.println("Local-Visiteur : Liste des arbitres");
		for (TupleMatch tupleMatch : list) {
			System.out.println(tupleMatch.PointageLocal + "-" + tupleMatch.PointageVisiteur + " : " + tupleMatch.ListeArbitres);
		}
	}
	
	/**
	 * affiche le resultat du match pour l'equipe concernee a la console
	 * @param equipe
	 * @throws SQLException
	 * @throws LigueBaseballException 
	 */
	public void afficherResultatDate(String nomEquipe) throws SQLException, LigueBaseballException {
	    if(equipe.existe(nomEquipe) == -1)
		throw new LigueBaseballException("equipe inexistante");
	    
	    List<TupleMatch> list = match.afficherResultat(nomEquipe);
	    if(list.size()==0){
		throw new LigueBaseballException("Il n'y a aucun match a afficher");
	    }
	    System.out.println("Local-Visiteur : Liste des arbitres");
		for (TupleMatch tupleMatch : list) {
			System.out.println(tupleMatch.PointageLocal + "-" + tupleMatch.PointageVisiteur + " : " + tupleMatch.ListeArbitres);
		}
	}
	
}
