package ligueBaseball;
import java.sql.*;

/**
 * Gestion des transactions de reli�es � la cr�ation et
 * suppresion de membres dans une biblioth�que.
 *
 *<pre>
 * Marc Frappier - 83 427 378
 * Universit� de Sherbrooke
 * version 2.0 - 13 novembre 2004
 * ift287 - exploitation de bases de donn�es
 *
 * Ce programme permet de g�rer les transaction reli�es � la 
 * cr�ation et suppresion de membres.
 *
 * Pr�-condition
 *   la base de donn�es de la biblioth�que doit exister
 *
 * Post-condition
 *   le programme effectue les maj associ�es � chaque
 *   transaction
 * </pre>
 */

public class GestionMembre {

private Connexion cx;
private Membre membre;
private Reservation reservation;

/**
  * Creation d'une instance
  */
public GestionMembre(Membre membre, Reservation reservation) {

this.cx = membre.getConnexion();
this.membre = membre;
this.reservation = reservation;
}

/**
  * Ajout d'un nouveau membre dans la base de donnees.
  * S'il existe deja, une exception est levee.
  */
public void inscrire(int idMembre, String nom, long telephone, int limitePret)
  throws SQLException, LigueBaseballException, Exception
{
try {
    /* V�rifie si le membre existe d�ja */
    if (membre.existe(idMembre))
        throw new LigueBaseballException("Membre existe deja: " + idMembre);

    /* Ajout du membre. */
    membre.inscrire(idMembre, nom, telephone, limitePret);
    cx.commit();
    }
catch (Exception e)
    {
    cx.rollback();
    throw e;
    }
}

/**
  * Suppression d'un membre de la base de donnees.
  */
public void desinscrire(int idMembre)
  throws SQLException, LigueBaseballException, Exception
{
try {
    /* V�rifie si le membre existe et son nombre de pret en cours */
    TupleMembre tupleMembre = membre.getMembre(idMembre);
    if (tupleMembre == null)
        throw new LigueBaseballException("Membre inexistant: " + idMembre);
    if (tupleMembre.nbPret > 0)
        throw new LigueBaseballException
            ("Le membre " + idMembre + " a encore des prets.");
    if (reservation.getReservationMembre(idMembre) !=null)
        throw new LigueBaseballException
        ("Membre " + idMembre + " a des r�servations");

    /* Suppression du membre */
    int nb = membre.desinscrire(idMembre);
    if (nb == 0)
        throw new LigueBaseballException
        ("Membre " + idMembre + " inexistant");
    cx.commit();
    }
catch (Exception e)
    {
    cx.rollback();
    throw e;
    }
}
}//class
