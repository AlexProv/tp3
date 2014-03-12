package ligueBaseball;

public class TupleJoueur {
	String Nom;
	String Prenom;
	int JoueurId;
	String EquipeNom;
	
	/**
	 * structure de donnee pour les Joueur
	 * @param joueurId
	 * @param nom
	 * @param prenom
	 */
	public TupleJoueur(int joueurId, String nom, String prenom)
	{
		Nom = nom;
		Prenom = prenom;
		JoueurId = joueurId;
		EquipeNom = "";
	}

	/**
	 * structure de donnee pour les Joueurs
	 * @param joueurId
	 * @param nom
	 * @param prenom
	 * @param equipeNom
	 */
	public TupleJoueur(int joueurId, String nom, String prenom,String equipeNom)
	{
		Nom = nom;
		Prenom = prenom;
		JoueurId = joueurId;
		EquipeNom = equipeNom;
	}
}
