package ligueBaseball;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Mathieu Lavoie, Alex Provencher et Vincent Gagnon
 *	classe qui fait toute les requettes concernant les arbitres a la base de donnees
 */
public class Arbitre {


	private PreparedStatement stmtInsertArbitre;
	private PreparedStatement stmtSelectAll;
	private PreparedStatement stmtExisteArbitre;
	private PreparedStatement stmtMaxId;
	private PreparedStatement stmtArbitrer;
	private PreparedStatement stmtCountArbitreMatch;
	private Connexion cx;
	
	
	/**
	 * Creation d'une instance. Des enonces SQL pour chaque requete sont
	 * precompiles.
	 */
	public Arbitre(Connexion cx) throws SQLException{
		this.cx = cx;
		stmtInsertArbitre = cx.getConnection().prepareStatement
		    ("insert into arbitre (arbitreId, arbitreNom, arbitrePrenom) " +
		      "values (?,?,?)");
		stmtSelectAll = cx.getConnection().prepareStatement
			("select arbitreNom, arbitrePrenom from arbitre order by arbitreNom");
		stmtExisteArbitre= cx.getConnection().prepareStatement
			("select arbitreID from arbitre where arbitreNom= ? and arbitrePrenom = ?");
		stmtMaxId = cx.getConnection().prepareStatement
			("select max(arbitreid) from arbitre");
		stmtArbitrer = cx.getConnection().prepareStatement(
				"insert into arbitrer (arbitreID, matchId) values (?,?)");
		stmtCountArbitreMatch = cx.getConnection().prepareStatement(
			"select count(arbitreid) from arbitrer where matchId= ?");
	}
	
	public Connexion getConnexion() {
		return cx;
	}
	
	/**
	 * ajoute un arbitre
	 * @param arbitreNom
	 * @param arbitrePrenom
	 * @throws SQLException
	 */
	public void ajoutArbitre(String arbitreNom,String arbitrePrenom)throws SQLException{
		int id = maxArbitre();
	    	stmtInsertArbitre.setInt(1, id);
	    	stmtInsertArbitre.setString(2,arbitreNom);
		stmtInsertArbitre.setString(3,arbitrePrenom);
		stmtInsertArbitre.executeUpdate();
	}
	
	/**
	 * @return retourne l'id du prochain arbitre a etre cree
	 * @throws SQLException
	 */
	private int maxArbitre() throws SQLException{
		ResultSet rset = stmtMaxId.executeQuery();
		int arbitreId = 0;
		if (rset.next()){
			arbitreId = rset.getInt(1) + 1;
		}
		rset.close();
		return arbitreId;
	}

	/**
	 * @return retorune une list d'arbitre de tout les arbitres existant
	 * @throws SQLException
	 */
	public List<TupleArbitre> getArbitre() throws SQLException {
	    List<TupleArbitre> listArbitres = new ArrayList<TupleArbitre>();
		ResultSet rset = stmtSelectAll.executeQuery();
		while (rset.next()) {
			TupleArbitre tupleArbitre = new TupleArbitre(rset.getString(1), rset.getString(2));
			listArbitres.add(tupleArbitre);
		}
		rset.close();
		return listArbitres;
	}

	/**
	 * 
	 * @param nom
	 * @param prenom
	 * @return retourne l'id de l'arbitre qui correspond au parametre.
	 * @throws SQLException
	 */
	public int existe(String nom, String prenom) throws SQLException {
		int arbitreId = -1;
	    stmtExisteArbitre.setString(1, nom);
	    stmtExisteArbitre.setString(2, prenom);
	    ResultSet rset = stmtExisteArbitre.executeQuery();
		if(rset.next()){
			arbitreId = rset.getInt(1);
		}
		rset.close();
		return arbitreId;
	}
	
	
	/**
	 * assigne un arbitre à un match
	 * @param arbitreId
	 * @param matchId
	 * @throws SQLException 
	 */
	public void assignerArbitreMatch(int arbitreId, int matchId) throws SQLException{
		stmtArbitrer.setInt(1, arbitreId);
		stmtArbitrer.setInt(2, matchId);
		stmtArbitrer.execute();
	}
	
	/**
	 * Count les arbitres par match
	 * @param matchId
	 * @throws SQLException 
	 */
	public int countArbitreMatch(int matchId) throws SQLException{
		stmtCountArbitreMatch.setInt(1, matchId);
	    	ResultSet rset = stmtCountArbitreMatch.executeQuery();
		int i = -1;
		if(rset.next()){
		    i = rset.getInt(1);
		}
		return i;
	}
	
}
