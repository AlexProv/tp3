package ligueBaseball;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author Mathieu Lavoie, Alex Provencher et Vincent Gagnon
 *
 */
public class Terrain {
	private PreparedStatement stmtExiste;
	private PreparedStatement stmtInsert;
	private PreparedStatement stmtMax;
	private Connexion cx;

	public Terrain(Connexion cx) throws SQLException {
		
		this.cx = cx;
		stmtExiste = cx.getConnection().prepareStatement
			    ("select terrainid from terrain where terrainnom = ?");
		stmtInsert = cx.getConnection().prepareStatement
				("insert into terrain (terrainid, terrainnom, terrainadresse) " +
					      "values (?,?,?)");
		stmtMax = cx.getConnection().prepareStatement
				("select max(terrainid) from terrain");
	}
	
	/**
	  * Retourner la connexion associee.
	  */
	public Connexion getConnexion() {
		return cx;
	}
	
	/**
	  * Verifie si une equipe existe.
	  */
	public boolean existe(String terrainNom) throws SQLException {

		stmtExiste.setString(1,terrainNom);
		ResultSet rset = stmtExiste.executeQuery();
		boolean terrainExiste = rset.next();
		rset.close();
		return terrainExiste;
	}

	/**
	  * Ajout d'une nouvelle equipe
	  */
	public void ajoutTerrain(int terrainId, String terrainNom, String terrainAdresse)
	  throws SQLException
	{
		stmtInsert.setInt(1,terrainId);
		stmtInsert.setString(2,terrainNom);
		stmtInsert.setString(3,terrainAdresse);
		stmtInsert.executeUpdate();
	}
	
	public int getTerrain(String terrainNom) throws SQLException{
		stmtExiste.setString(1,terrainNom);
		ResultSet rset = stmtExiste.executeQuery();
		int terrainId = 0;
		if(rset.next()){
			terrainId = rset.getInt(1);
		}
		rset.close();
		return terrainId;
	}
	
	public int maxTerrain() throws SQLException{
		ResultSet rset = stmtMax.executeQuery();
		int terrainId = 0;
		if(rset.next()){
			terrainId = rset.getInt(1) + 1;
		}
		rset.close();
		return terrainId;
	}
}
