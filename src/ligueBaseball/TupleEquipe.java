package ligueBaseball;

/**
 * 
 * @author Mathieu Lavoie, Alex Provencher et Vincent Gagnon
 *
 */

public class TupleEquipe {

	
	int equipeid;
	String equipenom;
  
	/**
	 * structure de donnee pour les Equipes
	 * @param equipeId
	 * @param equipenom
	 */
	public TupleEquipe(int equipeId, String equipenom)
	{
		this.equipeid = equipeId;
		this.equipenom = equipenom;
	}
}
