package ligueBaseball;

import java.sql.SQLException;

public class GestionArbitre {

	private Arbitre arbitre;
	private Connexion cx;

	public GestionArbitre(Arbitre arbitre){
		this.cx = arbitre.getConnexion();
		this.arbitre = arbitre;
		
	}
	
	public void ajout(String nom, String prenom)throws SQLException, LigueBaseballException, Exception{
        try {
           //if (arbitre.existe())
           //    throw new LigueBaseballException("Equipe existe deja: " + equipeNom);
           arbitre.ajoutArbitre(nom, prenom);
           cx.commit();
           }
        catch (Exception e)
           {
        //       System.out.println(e);
           cx.rollback();
           throw e;
   	}
        
        }

	
	
	
}
