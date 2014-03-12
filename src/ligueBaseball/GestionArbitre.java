package ligueBaseball;

import java.sql.SQLException;
import java.util.List;

/**
 *  * @author Mathieu Lavoie, Alex Provencher et Vincent Gagnon
 * classe intermedaire entre l'usager et les object qui parle a la base de donnee.
 */
public class GestionArbitre {

	private Arbitre arbitre;
	private Match match;
	private Connexion cx;

	/**
	 * @param arbitre
	 * @param match
	 */
	public GestionArbitre(Arbitre arbitre, Match match) {
		this.cx = arbitre.getConnexion();
		this.arbitre = arbitre;
		this.match = match;
	}

	/**
	 * fait les verification et envoie le mesage de rajouter une nouveau arbitre avec les parametres
	 * @param nom
	 * @param prenom
	 * @throws SQLException
	 * @throws LigueBaseballException
	 * @throws Exception
	 */
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
		}

	}

	/**
	 * fait les verification et envoie le mesage de retourner un arbitre qui correspond aux parametres
	 */
	public void getArbitre() {
		try {
			List<TupleArbitre> listArbitres = arbitre.getArbitre();
			for (TupleArbitre tupleArbitre : listArbitres) {
				System.out.println(tupleArbitre.arbitreNom + ", "
						+ tupleArbitre.arbitrePrenom);
			}
		} catch (SQLException e) {
		    System.out.println("L'arbitre n'existe pas");
		}

	}

	/**
	 * fait les verification et envoie le mesage de faire arbiter un match a un arbitre
	 * @param matchDate
	 * @param matchHeure
	 * @param equipeNomLocal
	 * @param equipeNomVisiteur
	 * @param arbitreNom
	 * @param arbitrePrenom
	 * @throws LigueBaseballException
	 */
	public void arbitrerMatch(java.sql.Date matchDate, java.sql.Time matchHeure, String equipeNomLocal, 
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
