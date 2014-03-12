package ligueBaseball;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Mathieu Lavoie, Alex Provencher et Vincent Gagnon
 * classe qui fait toute les requettes concernant les matchs a la base de donnees
 */
public class Match {
	
	private Connexion cx;
	private PreparedStatement stmtAjoutMatch;
	private PreparedStatement stmtExiste;
	private PreparedStatement stmtUpdatePoints;
	private PreparedStatement stmtMaxId;
	private PreparedStatement stmtTousResultats;
	private PreparedStatement stmtTousResultatsDate;
	private PreparedStatement stmtTousResultatsEquipe;
	//private PreparedStatement stmtGetTerrainId;
	
	/**
	 * @param cx object de connection a la base de donnee
	 * @throws SQLException 
	 *
	 * Creation d'une instance. Des enonces SQL pour chaque requete sont
	 * precompiles.
	 */
	public Match(Connexion cx) throws SQLException{
		this.cx = cx;
		stmtExiste = cx.getConnection().prepareStatement("select match.matchid from match, "
				+ "equipe e1, equipe e2 where match.equipelocal = e1.equipeid and e1.equipenom = ? and "
				+ "match.equipevisiteur = e2.equipeid and e2.equipenom = ? and match.matchdate = ? and "
				+ "match.matchheure = ?");
		stmtAjoutMatch = cx.getConnection().prepareStatement(
				"insert into match (matchid, equipelocal, equipevisiteur, matchdate, matchheure, terrainid)"
				+ "values (?,?,?,?,?,(select terrainid from equipe where equipeid = ?))");
		stmtMaxId = cx.getConnection().prepareStatement(
				"select max(matchid) from match");
		stmtUpdatePoints = cx.getConnection().prepareStatement("update match set pointslocal=?, pointsvisiteur=? where matchid = ?");
		stmtTousResultats = cx.getConnection().prepareStatement(
				"select pointslocal, pointsvisiteur, array_to_string(array_agg(arbitre.arbitrenom),',')"
				+ " from match left outer join arbitrer on arbitrer.matchid = match.matchid "
				+ "left outer join arbitre on arbitre.arbitreid = arbitrer.arbitreid "
				+ "where pointslocal is not null "
				+ "group by pointslocal, pointsvisiteur, matchdate order by matchdate");
		stmtTousResultatsDate = cx.getConnection().prepareStatement(
				"select pointslocal, pointsvisiteur, array_to_string(array_agg(arbitre.arbitrenom),',')"
				+ " from match left outer join arbitrer on arbitrer.matchid = match.matchid "
				+ "left outer join arbitre on arbitre.arbitreid = arbitrer.arbitreid "
				+ "where pointslocal is not null and match.matchdate >= ? "
				+ "group by pointslocal, pointsvisiteur, matchdate order by matchdate");
		stmtTousResultatsEquipe = cx.getConnection().prepareStatement(
				"select pointslocal, pointsvisiteur, array_to_string(array_agg(arbitre.arbitrenom),',') "
				+ "from match left outer join arbitrer on arbitrer.matchid = match.matchid "
				+ "left outer join arbitre on arbitre.arbitreid = arbitrer.arbitreid "
				+ "left outer join equipe e1 on e1.equipeid = match.equipelocal "
				+ "left outer join equipe e2 on e2.equipeid = match.equipevisiteur "
				+ "where pointslocal is not null and (e1.equipenom = ? or e2.equipenom = ?) "
				+ "group by pointslocal, pointsvisiteur, matchdate order by matchdate");
		//stmtGetTerrainId = cx.getConnection().prepareStatement("select terrainid from equipe where equipeid = ?");
	}
	
	public Connexion getConnexion() {
		return cx;
	}
	
	/**
	 * @param matchId 
	 * @param equipeLocalId
	 * @param equipeVisiteurId
	 * @param matchDate
	 * @param matchTime
	 * @throws SQLException
	 */
	public void ajoutMatch(int matchId, int equipeLocalId, int equipeVisiteurId, Date matchDate, Time matchTime) throws SQLException{
		stmtAjoutMatch.setInt(1, matchId);
		stmtAjoutMatch.setInt(2, equipeLocalId);
		stmtAjoutMatch.setInt(3, equipeVisiteurId);
		stmtAjoutMatch.setDate(4, matchDate);
		stmtAjoutMatch.setTime(5, matchTime);
		stmtAjoutMatch.setInt(6, equipeLocalId);
		stmtAjoutMatch.executeUpdate();
	}
	
	/**
	 * @param matchDate
	 * @param matchHeure
	 * @param equipeNomLocal
	 * @param equipeNomVisiteur
	 * @return index correspodant a l'enregistrement qui sont identique au parametre
	 * @throws SQLException
	 */
	public int existe(Date matchDate, Time matchHeure, String equipeNomLocal, 
			String equipeNomVisiteur) throws SQLException{
		int matchId = -1;
	    stmtExiste.setDate(3, matchDate);
	    stmtExiste.setTime(4, matchHeure);
	    stmtExiste.setString(1, equipeNomLocal);
	    stmtExiste.setString(2, equipeNomVisiteur);
	    ResultSet rset = stmtExiste.executeQuery();
		if(rset.next()){
			matchId = rset.getInt(1);
		}
		rset.close();
		return matchId;
	}
	/**
	 * @return retorune l'id du prochain match a etre cree
	 * @throws SQLException
	 */
	public int maxMatch() throws SQLException {
		ResultSet rset = stmtMaxId.executeQuery();
		int matchId = 0;
		if (rset.next()) {
			matchId = rset.getInt(1) + 1;
		}
		rset.close();
		return matchId;
	}
	
	
	/**
	 * change le pointage pour un matchid donner
	 * @param matchId
	 * @param pointsLocal
	 * @param pointsVisiteur
	 * @throws SQLException
	 */
	public void updatePointage(int matchId, int pointsLocal, int pointsVisiteur) throws SQLException{
		stmtUpdatePoints.setInt(1, pointsLocal);
		stmtUpdatePoints.setInt(2, pointsVisiteur);
		stmtUpdatePoints.setInt(3, matchId);
		stmtUpdatePoints.executeUpdate();
	}
	
	/**
	 * @return retorune une liste avec tout les resultas de tout les matchs
	 * @throws SQLException
	 */
	public List<TupleMatch> afficherResultat() throws SQLException{
		List<TupleMatch> list = new ArrayList<TupleMatch>();
		ResultSet rset = stmtTousResultats.executeQuery();
		while(rset.next()){
			list.add(new TupleMatch(rset.getInt(1), rset.getInt(2), rset.getString(3)));
		}
		return list;
	}
	
	/**
	 * @param date
	 * @return liste de touts les match qui on ete jouer a la date en parametre
	 * @throws SQLException
	 */
	public List<TupleMatch> afficherResultat(Date date) throws SQLException{
		List<TupleMatch> list = new ArrayList<TupleMatch>();
		stmtTousResultatsDate.setDate(1, date);
		ResultSet rset = stmtTousResultatsDate.executeQuery();
		while(rset.next()){
			list.add(new TupleMatch(rset.getInt(1), rset.getInt(2), rset.getString(3)));
		}
		return list;
	}
	
	/**
	 * @param equipe
	 * @return liste de touts les match qui on ete jouer a l'equipe en parametre
	 * @throws SQLException
	 */
	public List<TupleMatch> afficherResultat(String equipe) throws SQLException{
	    	List<TupleMatch> list = new ArrayList<TupleMatch>();
		stmtTousResultatsEquipe.setString(1, equipe);
		stmtTousResultatsEquipe.setString(2, equipe);
		ResultSet rset = stmtTousResultatsEquipe.executeQuery();
		while(rset.next()){
			list.add(new TupleMatch(rset.getInt(1), rset.getInt(2), rset.getString(3)));
		}
		return list;
	}
}
