package ligueBaseball;

public class TupleJoueur {
	String Nom;
	String Prenom;
	int JoueurId;
	String EquipeNom;
	
	public TupleJoueur(int joueurId, String nom, String prenom)
	{
		Nom = nom;
		Prenom = prenom;
		JoueurId = joueurId;
		EquipeNom = "";
	}

	public TupleJoueur(int joueurId, String nom, String prenom,String equipeNom)
	{
		Nom = nom;
		Prenom = prenom;
		JoueurId = joueurId;
		EquipeNom = equipeNom;
	}
}
