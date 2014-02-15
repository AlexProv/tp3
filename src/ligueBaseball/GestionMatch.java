package ligueBaseball;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

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
			int matchId = match.maxMatch();
			match.ajoutMatch(matchId, equipeLocalId, equipeVisiteurId, matchDate, matchHeure);
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
		}
	}
	
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
	
	
}
