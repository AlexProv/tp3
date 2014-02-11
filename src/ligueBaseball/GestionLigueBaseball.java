package ligueBaseball;
import java.sql.*;

/**
 * La classe GestionLigueBaseball
 * @author Mathieu Lavoie, Alex Provencher et Vincent Gagnon
 *
 */
public class GestionLigueBaseball
{
public Connexion cx;
public Equipe equipe;
public Terrain terrain;
public GestionEquipe gestionEquipe;
public GestionJoueur gestionJoueur;
public GestionArbitre gestionArbitre;

/**
  * Ouvre une connexion avec la BD relationnelle et
  * alloue les gestionnaires de transactions et de tables.
  * <pre>
  * 
  * @param serveur SQL
  * @param bd nom de la bade de donnees
  * @param user user id pour etablir une connexion avec le serveur SQL
  * @param password mot de passe pour le user id
  *</pre>
  */
public GestionLigueBaseball(String serveur, String bd, String user, String password)
  throws LigueBaseballException, SQLException
{
// allocation des objets pour le traitement des transactions
cx = new Connexion(serveur, bd, user, password);
equipe = new Equipe(cx);
terrain = new Terrain(cx);
gestionEquipe = new GestionEquipe(equipe, terrain);
}

public void fermer()
  throws SQLException
{
// fermeture de la connexion
cx.fermer();
}
}
