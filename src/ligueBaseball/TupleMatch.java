package ligueBaseball;

public class TupleMatch {
	
	int PointageLocal;
	int PointageVisiteur;
	String ListeArbitres;
	
	/**
	 * structure de donnee pour les Match
	 * @param pointageLocal
	 * @param pointageVisiteur
	 * @param listeArbitres
	 */
	public TupleMatch(int pointageLocal, int pointageVisiteur, String listeArbitres)
	{
		PointageLocal = pointageLocal;
		PointageVisiteur = pointageVisiteur;
		ListeArbitres = listeArbitres;
	}
}
