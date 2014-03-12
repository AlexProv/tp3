package ligueBaseball;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestionnaire d'une connexion avec une BD relationnelle via JDBC.
 *
 *<pre>
 * Marc Frappier - 83 427 378
 * Universitï¿½ de Sherbrooke
 * version 2.0 - 13 novembre 2004
 * ift287 - exploitation de bases de donnï¿½es
 *
 * Ce programme ouvrir une connexion avec une BD via JDBC.
 * La mï¿½thode serveursSupportes() indique les serveurs supportï¿½s.
 *
 * Prï¿½-condition
 *   le driver JDBC appropriï¿½ doit ï¿½tre accessible.
 *
 * Post-condition
 *   la connexion est ouverte en mode autocommit false et sï¿½rialisable, 
 *   (s'il est supportï¿½ par le serveur).
 * </pre>
 */
public class Connexion {

private Connection conn;

/**
 * Ouverture d'une connexion en mode autocommit false et sï¿½rialisable (si supportï¿½)
 * @param serveur serveur SQL de la BD
 * @bd nom de la base de donnï¿½es
 * @user userid sur le serveur SQL
 * @pass mot de passe sur le serveur SQL
 */
public Connexion(String serveur, String bd, String pass, String user)
  throws SQLException
{
Driver d;
try {
	if (serveur.equals("local")) {
		d = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver")
				.newInstance();
		DriverManager.registerDriver(d);
		conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@127.0.0.1:1521:" + bd, user, pass);
	}
	if (serveur.equals("sti")) {
		d = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver")
				.newInstance();
		DriverManager.registerDriver(d);
		conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@io.usherbrooke.ca:1521:" + bd, user,
				pass);
	} else if (serveur.equals("postgres")) {
		d = (Driver) Class.forName("org.postgresql.Driver")
				.newInstance();
		DriverManager.registerDriver(d);
		conn = DriverManager.getConnection("jdbc:postgresql:" + bd,
				user, pass);
	}

    // mettre en mode de commit manuel
    conn.setAutoCommit(false);
    
    // mettre en mode seialisable si possible
    // (plus haut niveau d'integrite l'acces concurrent aux donnees)
    DatabaseMetaData dbmd = conn.getMetaData();
    if (dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE))
        {
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        System.out.println(
          "Ouverture de la connexion en mode serialisable :\n" +
          "Estampille " + System.currentTimeMillis() + " " + conn);
        }
    else
        System.out.println(
          "Ouverture de la connexion en mode read committed (default) :\n" +
          "Heure " + System.currentTimeMillis() + " " + conn);
    }// try
    
catch (SQLException e) {throw e;}
catch (Exception e)
    {
    e.printStackTrace(System.out);
    throw new SQLException("JDBC Driver non instancie");
    }
}

/**
 *fermeture d'une connexion
 */
public void fermer()
  throws SQLException
{
conn.rollback();
conn.close();
System.out.println("Connexion fermee" + " " + conn);
}

/**
 *commit
 */
public void commit()
  throws SQLException
{
conn.commit();
}

/**
 *rollback
 */
public void rollback()
  throws SQLException
{
    System.out.println("Erreur usager: vous avez commis une erreure, aucune action n'a été commise");
    conn.rollback();
}

/**
 *retourne la Connection jdbc
 */
public Connection getConnection()
{
return conn;
}

/**
  * Retourne la liste des serveurs supportï¿½s par ce gestionnaire de connexions
  */
public static String serveursSupportes()
{
return "local : Oracle installe localement 127.0.0.1\n" +
       "sti   : Oracle installe au Service des technologies de l'information\n" +
       "postgres : Postgres installe localement\n";
}
}// Classe Connexion
