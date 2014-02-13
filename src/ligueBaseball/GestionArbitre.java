package ligueBaseball;

import java.sql.SQLException;
import java.util.List;

public class GestionArbitre {

	private Arbitre arbitre;
	private Match match;
	private Connexion cx;

	public GestionArbitre(Arbitre arbitre, Match match) {
		this.cx = arbitre.getConnexion();
		this.arbitre = arbitre;
		this.match = match;
	}

	public void ajout(String nom, String prenom) throws SQLException,
			LigueBaseballException, Exception {
		try {
			if (arbitre.existe(nom, prenom) != -1)
				throw new LigueBaseballException("Arbitre existe deja: "
						+ prenom + " " + nom);
			arbitre.ajoutArbitre(nom, prenom);
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}

	}

	public void getArbitre() {
		try {
			List<TupleArbitre> listArbitres = arbitre.getArbitre();
			for (TupleArbitre tupleArbitre : listArbitres) {
				System.out.println(tupleArbitre.arbitreNom + ", "
						+ tupleArbitre.arbitrePrenom);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void arbitrerMatch(java.sql.Date matchDate, String matchHeure, String equipeNomLocal, 
			String equipeNomVisiteur, String arbitreNom, String arbitrePrenom) throws LigueBaseballException {
		try {
			int arbitreId = arbitre.existe(arbitreNom, arbitrePrenom);
			if(arbitreId == -1)
				throw new LigueBaseballException("Arbitre n'existe pas : "
						+ arbitrePrenom + " " + arbitreNom);
			else{
				int matchId = match.existe(matchDate, matchHeure, equipeNomLocal, equipeNomVisiteur);
				if(matchId != -1)
					throw new LigueBaseballException("Match n'existe pas.");
				else{
					
				}
			}
				
		} catch (SQLException e) {
			throw new LigueBaseballException(e.getMessage());
		}
	}

}
