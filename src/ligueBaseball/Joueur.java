package ligueBaseball;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class Joueur {

	private PreparedStatement stmtInsert;
	private PreparedStatement stmtDelete;
	private PreparedStatement stmtExist;
	private PreparedStatement stmtSelectJoueurEquipeParam;
	private PreparedStatement stmtSelectJoueurEquipe;
	private PreparedStatement stmtMaxId;
	private PreparedStatement stmtGetId;
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
		stmtSelectJoueurEquipeParam = cx.getConnection().prepareStatement
			    ("select j.joueurid, j.joueurnom, j.joueurprenom, e.equipenom from faitpartie f, equipe e, joueur j where f.equipeid=e.equipeid and f.joueurid = j.joueurid and e.equipeid = ?");
		stmtSelectJoueurEquipe = cx.getConnection().prepareStatement
			    ("select j.joueurid, j.joueurnom, j.joueurprenom, e.equipenom from faitpartie f, equipe e, joueur j where f.equipeid=e.equipeid and f.joueurid = j.joueurid");
		stmtExist = cx.getConnection().prepareStatement
			    ("Select * from joueur where joueurnom = ? and joueurprenom = ?");
		stmtGetId = cx.getConnection().prepareStatement
			    ("Select joueurid from joueur where joueurnom = ? and joueurprenom = ?");
	}
	
	public int getId(String nom,String prenom) throws SQLException
	{
		stmtGetId.setString(0,nom);
		stmtGetId.setString(1,prenom);
		ResultSet rset = stmtGetId.executeQuery();
		int id = 0;
		if(rset.next())
		{
			id = rset.getInt(0);
		}
		return id;
	}
	public boolean existe(String nom, String prenom) throws SQLException
	{
		stmtExist.setString(0, nom);
		stmtExist.setString(1, prenom);
		return stmtExist.execute();
	}
	
	public void ajoutJoueur(String joueurNom, String joueurPrenom)
			throws SQLException
			{
				ResultSet rset = stmtMaxId.executeQuery();   
				int maxJoueurId = 0;
				if (rset.next()){
					maxJoueurId = rset.getInt(1) + 1;
				}
				rset.close();
				
				stmtInsert.setInt(0, maxJoueurId);
				stmtInsert.setString(1, joueurNom);
				stmtInsert.setString(2, joueurPrenom);
				
				stmtInsert.executeUpdate();
			}
	
	public List<TupleJoueur> selectjoueurEquipe(int equipeId) throws SQLException
	{
		List<TupleJoueur> lt = new ArrayList<TupleJoueur>();
		ResultSet rset;
		if(equipeId == 0)
		{
			 rset = stmtSelectJoueurEquipe.executeQuery();
		}
		else
		{
			stmtSelectJoueurEquipeParam.setInt(0, equipeId);
			rset = stmtSelectJoueurEquipeParam.executeQuery();
		}
		while(rset.next())
		{
			TupleJoueur tj = new TupleJoueur(rset.getInt(0),rset.getString(1),rset.getString(2),rset.getString(3));
			lt.add(tj);
		}
		return lt;
	}
	
	public List<TupleJoueur> selectjoueurEquipe() throws SQLException
	{
		return selectjoueurEquipe(0);
	}
	
	public int suppressionJoueur(int joueurId)
			 throws SQLException
			{
			stmtDelete.setInt(0,joueurId);
			return stmtDelete.executeUpdate();
			}
	public Connexion getConnexion(){
		return cx;
	}

}
