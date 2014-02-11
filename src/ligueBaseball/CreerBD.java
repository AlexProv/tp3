package ligueBaseball;

/****************************************************************/
/* Marc Frappier                                                */
/* Universit� de Sherbrooke                                     */
/****************************************************************/

import java.sql.*;
import java.io.*;

/**
 * <pre>
 * 
 * Permet de cr�er la BD utilis�e par Biblio.java.
 * 
 * Param�tres:0- serveur SQL 
 *           1- bd nom de la BD 
 *           2- user id pour �tablir une connexion avec le serveur SQL
 *           3- mot de passe pour le user id
 * </pre>
 */
class CreerBD {
	public static void main(String args[]) throws Exception, SQLException,
			IOException {

		if (args.length < 3) {
			System.out
					.println("Usage: java CreerBD <serveur> <bd> <user> <password>");
			return;
		}

		Connexion cx = new Connexion(args[0], args[1], args[2], args[3]);
	    cx.getConnection().setAutoCommit(true);
		cx.fermer();
	}
}
