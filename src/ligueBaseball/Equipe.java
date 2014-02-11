package ligueBaseball;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Equipe {

	private PreparedStatement stmtExiste;
	private PreparedStatement stmtInsert;
	private PreparedStatement stmtDelete;
	private PreparedStatement stmtSelectAll;
	private PreparedStatement stmtExisteJoueur;
	private PreparedStatement stmtMaxId;
	private Connexion cx;

	/**
	 * Creation d'une instance. Des enonces SQL pour chaque requete sont
	 * precompiles.
	 */
	public Equipe(Connexion cx) throws SQLException {

		this.cx = cx;
		stmtExiste = cx.getConnection().prepareStatement(
						"select equipeid, terrainid, equipenom from equipe where equipenom = ?");
		stmtInsert = cx.getConnection().prepareStatement(
				"insert into equipe (equipeid, terrainid, equipenom) "
						+ "values (?,?,?)");
		stmtDelete = cx.getConnection().prepareStatement(
				"delete from equipe where equipenom = ?");
		stmtSelectAll = cx.getConnection().prepareStatement(
				"select equipeid, equipenom from equipe order by equipenom");
		stmtExisteJoueur = cx.getConnection()
				.prepareStatement("select faitpartie.equipeid from faitpartie, equipe where "
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
	public boolean existe(String equipeNom) throws SQLException {

		stmtExiste.setString(1, equipeNom);
		return stmtExiste.execute();
	}

	/**
	 * Ajout d'une nouvelle equipe
	 */
	public void ajoutEquipe(int equipeId, int terrainId, String equipeNom)
			throws SQLException {
		stmtInsert.setInt(1, equipeId);
		stmtInsert.setInt(2, terrainId);
		stmtInsert.setString(3, equipeNom);
		stmtInsert.executeUpdate();
	}

	/**
	 * Suppression d'une equipe.
	 */
	public int suppressionEquipe(String equipeNom) throws SQLException {
		/* Suppression du livre. */
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
		stmtExisteJoueur.setString(1, equipeNom);
		return stmtExisteJoueur.execute();
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
