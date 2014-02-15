package ligueBaseball;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class Match {
	
	private Connexion cx;
	private PreparedStatement stmtAjoutMatch;
	private PreparedStatement stmtExiste;
	private PreparedStatement stmtUpdatePoints;
	private PreparedStatement stmtMaxId;
	
	public Match(Connexion cx) throws SQLException{
		this.cx = cx;
		stmtExiste = cx.getConnection().prepareStatement("select match.matchid from match, "
				+ "equipe e1, equipe e2 where match.equipelocal = e1.equipeid and e1.equipenom = ? and "
				+ "match.equipevisiteur = e2.equipeid and e2.equipenom = ? and match.matchdate = ? and "
				+ "match.matchheure = ?");
		stmtAjoutMatch = cx.getConnection().prepareStatement(
				"insert into match (matchid, equipelocal, equipevisiteur, matchdate, matchheure)"
				+ "values (?,?,?,?,?)");
		stmtMaxId = cx.getConnection().prepareStatement(
				"select max(matchid) from match");
		stmtUpdatePoints = cx.getConnection().prepareStatement("update match set pointslocal=?, pointsvisiteur=? where matchid = ?");
	}
	
	public Connexion getConnexion() {
		return cx;
	}
	
	public void ajoutMatch(int matchId, int equipeLocalId, int equipeVisiteurId, Date matchDate, Time matchTime) throws SQLException{
		stmtAjoutMatch.setInt(1, matchId);
		stmtAjoutMatch.setInt(2, equipeLocalId);
		stmtAjoutMatch.setInt(3, equipeVisiteurId);
		stmtAjoutMatch.setDate(4, matchDate);
		stmtAjoutMatch.setTime(5, matchTime);
		stmtAjoutMatch.executeUpdate();
	}
	
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
	public int maxMatch() throws SQLException {
		ResultSet rset = stmtMaxId.executeQuery();
		int matchId = 0;
		if (rset.next()) {
			matchId = rset.getInt(1) + 1;
		}
		rset.close();
		return matchId;
	}
	
	public void updatePointage(int matchId, int pointsLocal, int pointsVisiteur) throws SQLException{
		stmtUpdatePoints.setInt(1, pointsLocal);
		stmtUpdatePoints.setInt(2, pointsVisiteur);
		stmtUpdatePoints.setInt(3, matchId);
		stmtUpdatePoints.executeUpdate();
	}
}
