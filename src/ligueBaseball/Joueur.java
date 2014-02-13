package ligueBaseball;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class Joueur {

	private PreparedStatement stmtInsert;
	private PreparedStatement stmtInsertWithEquipe;
	private PreparedStatement stmtDelete;
	private PreparedStatement stmtExist;
	private PreparedStatement stmtSelectJoueurEquipeParam;
	private PreparedStatement stmtSelectJoueurEquipe;
	private PreparedStatement stmtMaxId;
	private PreparedStatement stmtGetId;
	private PreparedStatement stmtGetEquipeId;
	private PreparedStatement stmtEquipeExiste;
	private PreparedStatement stmtNumeroExiste;
	
	private Connexion cx;
	
	public Joueur(Connexion cx) throws  SQLException
	{
		this.cx = cx;
		
		stmtInsert =  cx.getConnection().prepareStatement
			    ("INSERT INTO joueur (joueurid, joueurnom, joueurprenom) VALUES (?,?,?)");
		stmtInsertWithEquipe =  cx.getConnection().prepareStatement
			    ("INSERT INTO faitpartie (joueurid, equipeid, numero) VALUES (?,?,?)");
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
		stmtEquipeExiste = cx.getConnection().prepareStatement
			    ("Select * from equipe where equipenom = ?");
		stmtNumeroExiste = cx.getConnection().prepareStatement
			    ("Select * from equipe e, faitpartie f, joueur j where equipenom = ? and e.numero = ? and j.joueurid=f.joueurid and f.equipeid = e.equipeid");
		stmtGetEquipeId =  cx.getConnection().prepareStatement("select equipeid from equipe where equipenom= ?");
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
		return stmtExist.executeQuery().next();
	}
	
	public void ajoutJoueur(String joueurNom, String joueurPrenom)
			throws SQLException
	{
		
		
		int maxJoueurId = getMaxJoueurId();
		
		stmtInsert.setInt(0, maxJoueurId);
		stmtInsert.setString(1, joueurNom);
		stmtInsert.setString(2, joueurPrenom);
		
		stmtInsert.executeUpdate();
	}
	
	public void ajoutJoueur(String joueurNom, String joueurPrenom, int numero, String equipe) throws SQLException, LigueBaseballException
	{
		stmtGetEquipeId.setString(0, equipe);
		ResultSet rset = stmtGetEquipeId.executeQuery();
		int equipeId = 0;
		if(rset.next())
		{
			equipeId = rset.getInt(0);
		}
		int joueurId = getMaxJoueurId();
		
		stmtInsertWithEquipe.setInt(joueurId, 0);
		stmtInsertWithEquipe.setInt(equipeId, 1);
		stmtInsertWithEquipe.setInt(numero, 2);
		
		ajoutJoueur(joueurNom, joueurPrenom);
	}

	public boolean numeroExiste(String equipe, int numero) throws SQLException
	{
		stmtNumeroExiste.setString(0, equipe);
		stmtNumeroExiste.setInt(1, numero);
		if(!stmtNumeroExiste.executeQuery().next()){
			//throw new LigueBaseballException("Numero deja utiliser!");
			return true;
		}	
		return false;
	}
	
	public boolean equipeExiste(String equipe) throws SQLException
	{
		stmtEquipeExiste.setString(0, equipe);
		if(!stmtEquipeExiste.executeQuery().next()){
			//throw new LigueBaseballException("Equipe inexistante!");
			return true;
		}
		return false;
	}
	
	private int getMaxJoueurId() throws SQLException
	{
		ResultSet rset = stmtMaxId.executeQuery();   
		int maxJoueurId = 0;
		if (rset.next()){
			maxJoueurId = rset.getInt(1) + 1;
		}
		rset.close();
		return maxJoueurId;
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
