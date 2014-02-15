package ligueBaseball;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Arbitre {


	private PreparedStatement stmtInsertArbitre;
	private PreparedStatement stmtSelectAll;
	private PreparedStatement stmtExisteArbitre;
	private PreparedStatement stmtMaxId;
	private Connexion cx;
	
	
	
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

	}
	
	public Connexion getConnexion() {
		return cx;
	}
	
	public void ajoutArbitre(String arbitreNom,String arbitrePrenom)throws SQLException{
		int id = maxArbitre();
	    	stmtInsertArbitre.setInt(1, id);
	    	stmtInsertArbitre.setString(2,arbitreNom);
		stmtInsertArbitre.setString(3,arbitrePrenom);
		stmtInsertArbitre.executeUpdate();
	}
	
	private int maxArbitre() throws SQLException{
		ResultSet rset = stmtMaxId.executeQuery();
		int arbitreId = 0;
		if (rset.next()){
			arbitreId = rset.getInt(1) + 1;
		}
		rset.close();
		return arbitreId;
	}

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
	
	public void assignerArbitreMatch(int arbitreId, int matchId){
		
	}
	
}
