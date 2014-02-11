package ligueBaseball;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Arbitre {


	private PreparedStatement stmtExiste;
	private PreparedStatement stmtInsert;
	private PreparedStatement stmtDelete;
	private PreparedStatement stmtSelectAll;
	private PreparedStatement stmtExisteJoueur;
	private PreparedStatement stmtMaxId;
	private Connexion cx;
	
	
	
	public Arbitre(Connexion cx) throws SQLException{
		this.cx = cx;
//		stmtExiste = cx.getConnection().prepareStatement
//			    ("select  from equipe where equipenom = ?");
		stmtInsert = cx.getConnection().prepareStatement
		    ("insert into arbitre (arbitreId, arbitreNom, arbitrePrenom) " +
		      "values (?,?,?)");
//		stmtDelete = cx.getConnection().prepareStatement
//		    ("delete from equipe where equipenom = ?");
//		stmtSelectAll = cx.getConnection().prepareStatement
//			("select equipeid, equipenom from equipe order by equipenom");
//		stmtExisteJoueur = cx.getConnection().prepareStatement
//			("select faitpartie.equipeid from faitpartie, equipe where "
//					+ "faitpartie.equipeid = equipe.equipeid and equipe.equipenom = ?");
		stmtMaxId = cx.getConnection().prepareStatement
			("select max(arbitreid) from arbitre");

	}
	
	public Connexion getConnexion() {
		return cx;
	}
	
	public void ajoutArbitre(String arbitreNom,String arbitrePrenom)throws SQLException{
		int id = maxArbitre();
	    	stmtInsert.setInt(1, id);
	    	stmtInsert.setString(2,arbitreNom);
		stmtInsert.setString(3,arbitrePrenom);
		stmtInsert.executeUpdate();
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
	
}
