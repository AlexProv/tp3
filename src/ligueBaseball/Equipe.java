package ligueBaseball;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Equipe {

	private PreparedStatement stmtExiste;
	private PreparedStatement stmtInsert;
	private PreparedStatement stmtInsertTerrain;
	private PreparedStatement stmtDelete;
	private PreparedStatement stmtSelectAll;
	private PreparedStatement stmtExisteJoueurEquipe;
	private PreparedStatement stmtMaxId;
	private Connexion cx;

	/**
	 * Creation d'une instance. Des enonces SQL pour chaque requete sont
	 * precompiles.
	 */
	public Equipe(Connexion cx) throws SQLException {

		this.cx = cx;
		stmtExiste = cx.getConnection().prepareStatement(
						"select * from equipe where equipenom = ?");
		stmtInsertTerrain = cx.getConnection().prepareStatement(
				"insert into equipe (equipeid, terrainid, equipenom) "
						+ "values (?,?,?)");
		stmtInsert = cx.getConnection().prepareStatement(
				"insert into equipe (equipeid, equipenom) "
						+ "values (?,?)");
		stmtDelete = cx.getConnection().prepareStatement(
				"delete from equipe where equipenom = ?");
		stmtSelectAll = cx.getConnection().prepareStatement(
				"select equipeid, equipenom from equipe order by equipenom");
		stmtExisteJoueurEquipe = cx.getConnection()
				.prepareStatement("select distinct faitpartie.equipeid from faitpartie, equipe where "
						+ "faitpartie.equipeid = equipe.equipeid and equipe.equipenom = ?");
		stmtMaxId = cx.getConnection().prepareStatement(
				"select max(equipeid) from equipe");
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
	public int existe(String equipeNom) throws SQLException {
		int equipeId = -1;
		stmtExiste.setString(1,equipeNom);
		ResultSet rset = stmtExiste.executeQuery();
		if(rset.next()){
			equipeId = rset.getInt(1);
		}
		rset.close();
		return equipeId;

	}
	
	public void ajoutEquipe(int equipeId, String equipeNom) throws SQLException{
		stmtInsert.setInt(1, equipeId);
		stmtInsert.setString(2, equipeNom);
		stmtInsert.executeUpdate();
	}

	/**
	 * Ajout d'une nouvelle equipe
	 */
	public void ajoutEquipe(int equipeId, int terrainId, String equipeNom)
			throws SQLException {
		stmtInsertTerrain.setInt(1, equipeId);
		stmtInsertTerrain.setInt(2, terrainId);
		stmtInsertTerrain.setString(3, equipeNom);
		stmtInsertTerrain.executeUpdate();
	}

	/**
	 * Suppression d'une equipe.
	 */
	public int suppressionEquipe(String equipeNom) throws SQLException {
		stmtDelete.setString(1, equipeNom);
		return stmtDelete.executeUpdate();
	}

	/**
	 * 
	 * @return Liste de TupleEquipe
	 * @throws SQLException
	 */
	public List<TupleEquipe> getEquipes() throws SQLException {
		List<TupleEquipe> listEquipes = new ArrayList<TupleEquipe>();
		ResultSet rset = stmtSelectAll.executeQuery();
		while (rset.next()) {
			TupleEquipe tupleEquipe = new TupleEquipe(rset.getInt(1), rset.getString(2));
			listEquipes.add(tupleEquipe);
		}
		rset.close();
		return listEquipes;
	}

	public boolean existeJoueurs(String equipeNom) throws SQLException {
		stmtExisteJoueurEquipe.setString(1, equipeNom);
		ResultSet rset = stmtExisteJoueurEquipe.executeQuery();
		boolean existe = rset.next();
		rset.close();
		return existe;
	}

	public int maxJoueur() throws SQLException {
		ResultSet rset = stmtMaxId.executeQuery();
		int equipeId = 0;
		if (rset.next()) {
			equipeId = rset.getInt(1) + 1;
		}
		rset.close();
		return equipeId;
	}
}
