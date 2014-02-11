package ligueBaseball;
import java.sql.*;

/**
 * Gestion des transactions d'interrogation dans une biblioth�que.
 * 
 * <pre>
 * 
 *  
 *   Marc Frappier - 83 427 378
 *   Universit� de Sherbrooke
 *   version 2.0 - 13 novembre 2004
 *   ift287 - exploitation de bases de donn�es
 *  
 *   Ce programme permet de faire diverses interrogations
 *   sur l'�tat de la biblioth�que.
 *  
 *   Pr�-condition
 *     la base de donn�es de la biblioth�que doit exister
 *  
 *   Post-condition
 *     le programme effectue les maj associ�es � chaque
 *     transaction
 *   
 *  
 * </pre>
 */

public class GestionInterrogation {

private PreparedStatement stmtLivresRetard;
private PreparedStatement stmtLivresTitreMot;
private PreparedStatement stmtListeTousLivres;
private Connexion cx;

/**
 * Creation d'une instance
 */
public GestionInterrogation(Connexion cx) throws SQLException {

this.cx = cx;
stmtLivresRetard = cx.getConnection().prepareStatement
    ("select t1.idlivre, t1.titre, t1.datePret, " +
        "to_date(?,'YYYY-MM-DD') - t1.datePret - 14 , t1.idMembre, " +
        "t2.nom " +
      "from livre t1, membre t2 " +
      "where  t1.idmembre = t2.idmembre and " +
             "to_date(?,'YYYY-MM-DD') - t1.datePret > 14 " +
      "group by t1.idlivre, t1.titre, t1.datePret, t1.idMembre, t2.nom");

stmtLivresTitreMot = cx.getConnection().prepareStatement
    ("select t1.idLivre, t1.titre, t1.auteur, t1.idmembre, t1.datePret + 14 " +
      "from livre t1 " +
      "where lower(titre) like ?");

stmtListeTousLivres = cx.getConnection().prepareStatement
	("select t1.idLivre, t1.titre, t1.auteur, t1.idmembre, t1.datePret " +
        "from livre t1");
}

/**
 * Affiche les livres pr�t�s depuis plus de 14 jours.
 */
public void listerLivresRetard(String date) throws SQLException {

stmtLivresRetard.setString(1,date);
stmtLivresRetard.setString(2,date);
ResultSet rset = stmtLivresRetard.executeQuery();

System.out.println("idlivre titre datePret retard idmembre nom");
while (rset.next())
    {
    System.out.println(
        rset.getInt(1) + " " +
        rset.getString(2) + " " +
        rset.getDate(3) + " " +
        rset.getInt(4) + " " +
        rset.getInt(5) + " " +
        rset.getString(6));
    }
cx.commit();
}

/**
 * Affiche les livres contenu un mot dans le titre
 */
public void listerLivresTitre(String mot) throws SQLException {

stmtLivresTitreMot.setString(1,"%" + mot + "%");
ResultSet rset = stmtLivresTitreMot.executeQuery();

int idMembre;
System.out.println("idLivre titre auteur idMembre dateRetour");
while (rset.next())
    {
    System.out.print(
        rset.getInt(1) + " " +
        rset.getString(2) + " " +
        rset.getString(3));
    idMembre = rset.getInt(4);
    if (!rset.wasNull())
        {
        System.out.print(" " + idMembre + " " + rset.getDate(5));
        }
    System.out.println();
    }
cx.commit();
}

/**
 * Affiche tous les livres de la BD
 */
public void listerLivres() throws SQLException {

ResultSet rset = stmtListeTousLivres.executeQuery();

System.out.println("idLivre titre auteur idMembre datePret");
int idMembre;
while (rset.next())
    {
    System.out.print(
        rset.getInt("idLivre") + " " +
        rset.getString("titre") + " " +
        rset.getString("auteur"));
    idMembre = rset.getInt("idMembre");
    if (!rset.wasNull())
        {
        System.out.print(" " + idMembre + " " + rset.getDate("datePret"));
        }
    System.out.println();
    }
cx.commit();
}
}
