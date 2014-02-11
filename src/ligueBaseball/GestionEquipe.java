package ligueBaseball;
import java.sql.*;
import java.util.List;

/**
 * 
 * @author Mathieu Lavoie, Alex Provencher et Vincent Gagnon
 *
 */
public class GestionEquipe {

private Equipe equipe;
private Terrain terrain;
private Connexion cx;

/**
  * Creation d'une instance
  */
public GestionEquipe(Equipe equipe, Terrain terrain) throws LigueBaseballException
{
this.cx = equipe.getConnexion();
this.equipe = equipe;
this.terrain = terrain;
}

/**
 * Ajout d'une nouvelle equipe dans la base de donnees.
 * S'il existe deja, une exception est levee.
 */
public void ajout(String equipeNom, String nomTerrain)
 throws SQLException, LigueBaseballException, Exception
{
try {
   if (equipe.existe(equipeNom))
       throw new LigueBaseballException("Equipe existe deja: " + equipeNom);
   int equipeId = equipe.maxJoueur();
   int terrainId = terrain.getTerrain(nomTerrain);
   equipe.ajoutEquipe(equipeId, terrainId, equipeNom);
   cx.commit();
   }
catch (Exception e)
   {
//       System.out.println(e);
   cx.rollback();
   throw e;
   }
}

/**
  * Ajout d'une nouvelle equipe dans la base de donnees.
  * S'il existe deja, une exception est levee.
  */
public void ajout(String equipeNom, String nomTerrain, String adresseTerrain)
  throws SQLException, LigueBaseballException, Exception
{
try {
    if (equipe.existe(equipeNom))
        throw new LigueBaseballException("Equipe existe deja: " + equipeNom);
    if (!terrain.existe(nomTerrain)){
    	int terrainId = terrain.maxTerrain();
    	terrain.ajoutTerrain(terrainId, nomTerrain, adresseTerrain);
    }
    int equipeId = equipe.maxJoueur();
    int terrainId = terrain.getTerrain(nomTerrain);
    equipe.ajoutEquipe(equipeId, terrainId, equipeNom);
    cx.commit();
    }
catch (Exception e)
    {
//        System.out.println(e);
    cx.rollback();
    throw e;
    }
}

/**
  * Vente d'un livre.
  */
public void supprimer(String equipeNom)
  throws SQLException, LigueBaseballException, Exception
{
try {
    boolean exister = equipe.existe(equipeNom);
    if (exister == false)
        throw new LigueBaseballException("Equipe inexistante: " + equipeNom);
    boolean jexister = equipe.existeJoueurs(equipeNom);
    if (jexister == true){
    	throw new LigueBaseballException("Impossible de supprimer cette equipe "
    			+ "( " + equipeNom + " ) car il y a des joueurs associes a cette equipe.");
    }
    /* Suppression de l'equipe. */
    equipe.suppressionEquipe(equipeNom);
    cx.commit();
    }
catch (Exception e)
    {
    cx.rollback();
    throw e;
    }
}

public void getEquipes(){
	try {
		List<TupleEquipe> listEquipes = equipe.getEquipes();
		for (TupleEquipe tupleEquipe : listEquipes) {
			System.out.println(tupleEquipe.equipeid + "\t" + tupleEquipe.equipenom);
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
}
}
