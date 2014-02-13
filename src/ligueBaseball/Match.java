package ligueBaseball;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class Match {
	
	private Connexion cx;
	private PreparedStatement stmtExiste;
	
	public Match(Connexion cx) throws SQLException{
		this.cx = cx;
		this.stmtExiste = cx.getConnection().prepareStatement("select match.matchid from match, "
				+ "equipe e1, equipe e2 where match.equipelocal = e1.equipeid and e1.equipenom = ? and "
				+ "match.equipevisiteur = e2.equipeid and e2.equipenom = ? and match.matchdate = ? and "
				+ "match.matchheure = ?");
	}
	
	public Connexion getConnexion() {
		return cx;
	}
	
	public int existe(java.sql.Date matchDate, String matchHeure, String equipeNomLocal, 
			String equipeNomVisiteur) throws SQLException{
		int matchId = -1;
	    stmtExiste.setDate(1, matchDate);
	    stmtExiste.setTime(2, Time.valueOf(matchHeure));
	    stmtExiste.setString(3, equipeNomLocal);
	    stmtExiste.setString(4, equipeNomVisiteur);
	    ResultSet rset = stmtExiste.executeQuery();
		if(rset.next()){
			matchId = rset.getInt(1);
		}
		rset.close();
		return matchId;
	}
}
