package ligueBaseball;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Joueur {

	private PreparedStatement stmtInsert;
	private PreparedStatement stmtInsertWithEquipe;
	private PreparedStatement stmtDelete;
	private PreparedStatement stmtDeletePartie;
	private PreparedStatement stmtExist;
	private PreparedStatement stmtSelectJoueurEquipeParam;
	private PreparedStatement stmtSelectJoueur;
	private PreparedStatement stmtMaxId;
	private PreparedStatement stmtGetId;
	private PreparedStatement stmtGetEquipeId;
	private PreparedStatement stmtNumeroExiste;

	private Connexion cx;

	public Joueur(Connexion cx) throws SQLException {
		this.cx = cx;

		stmtInsert = cx
				.getConnection()
				.prepareStatement(
						"INSERT INTO joueur (joueurid, joueurnom, joueurprenom) VALUES (?,?,?)");
		stmtInsertWithEquipe = cx
				.getConnection()
				.prepareStatement(
						"INSERT INTO faitpartie (joueurid, equipeid, numero, datedebut) VALUES (?,?,?,?)");
		stmtDelete = cx.getConnection().prepareStatement(
				"delete from joueur where joueurid = ?");
		stmtDeletePartie = cx.getConnection().prepareStatement(
				"delete from faitpartie where joueurid = ?");
		stmtMaxId = cx.getConnection().prepareStatement(
				"select max(joueurid) from joueur");
		stmtSelectJoueurEquipeParam = cx.getConnection().prepareStatement(
				"select j.joueurid, j.joueurnom, j.joueurprenom, e.equipenom from faitpartie f, "
				+ "equipe e, joueur j where f.equipeid=e.equipeid and f.joueurid = j.joueurid and "
				+ "e.equipeid = ?");
		stmtExist = cx.getConnection().prepareStatement(
				"Select * from joueur where joueurnom = ? and joueurprenom = ?");
		stmtGetId = cx.getConnection().prepareStatement(
				"Select joueurid from joueur where joueurnom = ? and joueurprenom = ?");
		stmtNumeroExiste = cx.getConnection().prepareStatement(
						"Select * from equipe e, faitpartie f, joueur j where equipenom = ? "
						+ "and f.numero = ? and j.joueurid=f.joueurid and f.equipeid = e.equipeid");
		stmtGetEquipeId = cx.getConnection().prepareStatement(
				"select equipe.equipeid from equipe where equipe.equipenom= ?");
		stmtSelectJoueur = cx.getConnection().prepareStatement(
						"select joueurnom, joueur.joueurprenom,equipe.equipenom from joueur "
						+ "left join faitpartie on faitpartie.joueurid = joueur.joueurid "
						+ "left join equipe on equipe.equipeid = faitpartie.equipeid");

	}

	public int getId(String nom, String prenom) throws SQLException {
		stmtGetId.setString(1, nom);
		stmtGetId.setString(2, prenom);
		ResultSet rset = stmtGetId.executeQuery();
		int id = 0;
		if (rset.next()) {
			id = rset.getInt(1);
		}
		return id;
	}

	public boolean existe(String nom, String prenom) throws SQLException {
		stmtExist.setString(1, nom);
		stmtExist.setString(2, prenom);
		return stmtExist.executeQuery().next();
	}

	public void ajoutJoueur(String joueurNom, String joueurPrenom)
			throws SQLException {
		int maxJoueurId = getMaxJoueurId();

		stmtInsert.setInt(1, maxJoueurId);
		stmtInsert.setString(2, joueurNom);
		stmtInsert.setString(3, joueurPrenom);

		stmtInsert.executeUpdate();
	}

	public void ajoutJoueur(String joueurNom, String joueurPrenom, int numero,
			String nomEquipe) throws SQLException, LigueBaseballException {
		stmtGetEquipeId.setString(1, nomEquipe);
		ResultSet rset = stmtGetEquipeId.executeQuery();
		int equipeId = 0;
		if (rset.next()) {
			equipeId = rset.getInt(1);
		}
		rset.close();
		int joueurId = getMaxJoueurId();

		ajoutJoueur(joueurNom, joueurPrenom);

		stmtInsertWithEquipe.setInt(1, joueurId);
		stmtInsertWithEquipe.setInt(2, equipeId);
		stmtInsertWithEquipe.setInt(3, numero);

		java.util.Calendar cal = java.util.Calendar.getInstance();
		java.util.Date utilDate = cal.getTime();
		java.sql.Date sqlDate = new Date(utilDate.getTime());
		stmtInsertWithEquipe.setDate(4, sqlDate);
		stmtInsertWithEquipe.executeUpdate();
	}

	public void ajoutJoueur(String joueurNom, String joueurPrenom, int numero,
			String nomEquipe, Date dateDebut) throws SQLException,
			LigueBaseballException {
		stmtGetEquipeId.setString(1, nomEquipe);
		ResultSet rset = stmtGetEquipeId.executeQuery();
		int equipeId;
		if (rset.next()) {
			equipeId = rset.getInt(0);
		} else {
			throw new LigueBaseballException("Equipe inexistante");
		}
		int joueurId = getMaxJoueurId();

		stmtInsertWithEquipe.setInt(1, joueurId);
		stmtInsertWithEquipe.setInt(2, equipeId);
		stmtInsertWithEquipe.setInt(3, numero);

		stmtInsertWithEquipe.setDate(4, dateDebut);

		ajoutJoueur(joueurNom, joueurPrenom);
	}

	public boolean numeroExiste(String equipe, int numero) throws SQLException {
		stmtNumeroExiste.setString(1, equipe);
		stmtNumeroExiste.setInt(2, numero);
		if (!stmtNumeroExiste.executeQuery().next()) {
			// throw new LigueBaseballException("Numero deja utiliser!");
			return true;
		}
		return false;
	}

	private int getMaxJoueurId() throws SQLException {
		ResultSet rset = stmtMaxId.executeQuery();
		int maxJoueurId = 0;
		if (rset.next()) {
			maxJoueurId = rset.getInt(1) + 1;
		}
		rset.close();
		return maxJoueurId;
	}

	public List<TupleJoueur> selectjoueurEquipe(int equipeId)
			throws SQLException {
		List<TupleJoueur> lt = new ArrayList<TupleJoueur>();
		ResultSet rset;
		if (equipeId == 0) {
			rset = stmtSelectJoueur.executeQuery();
		} else {
			stmtSelectJoueurEquipeParam.setInt(1, equipeId);
			rset = stmtSelectJoueurEquipeParam.executeQuery();
		}
		while (rset.next()) {
			TupleJoueur tj = new TupleJoueur(rset.getInt(1), rset.getString(2),
					rset.getString(3), rset.getString(4));
			lt.add(tj);
		}
		return lt;
	}

	public List<TupleJoueur> selectjoueurEquipe() throws SQLException {
		return selectjoueurEquipe(0);
	}

	public int suppressionJoueur(int joueurId) throws SQLException {
		stmtDelete.setInt(1, joueurId);
		return stmtDelete.executeUpdate();
	}
	
	public int suppressionFaitPartie(int joueurId) throws SQLException{
		stmtDeletePartie.setInt(1, joueurId);
		return stmtDeletePartie.executeUpdate();
	}

	public Connexion getConnexion() {
		return cx;
	}

}
