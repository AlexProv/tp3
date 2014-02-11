package ligueBaseball;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class Joueur {

	private PreparedStatement stmtInsert;
	private PreparedStatement stmtDelete;
	private PreparedStatement stmtSelectJoueurEquipeParam;
	private PreparedStatement stmtMaxId;
	private Connexion cx;
	
	public Joueur(Connexion cx) throws  SQLException
	{
		this.cx = cx;
		
		stmtInsert =  cx.getConnection().prepareStatement
			    ("INSERT INTO joueur (joueurid, joueurnom, joueurprenom) VALUES (?,?,?)");
		stmtDelete = cx.getConnection().prepareStatement
			    ("delete from joueur where joueurid = ?");
		stmtMaxId = cx.getConnection().prepareStatement
			    ("select max(joueurid) from joueur");
	}
}
