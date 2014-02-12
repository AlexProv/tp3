package ligueBaseball;

import java.sql.SQLException;
import java.util.List;

public class GestionArbitre {

	private Arbitre arbitre;
	private Connexion cx;

	public GestionArbitre(Arbitre arbitre){
		this.cx = arbitre.getConnexion();
		this.arbitre = arbitre;
		
	}
	
	public void ajout(String nom, String prenom)throws SQLException, LigueBaseballException, Exception{
        try {
           if (arbitre.existe(nom, prenom))
               throw new LigueBaseballException("Arbitre existe deja: " + arbitre);
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

	public void getArbitre(){
	try {
		List<TupleArbitre> listArbitres = arbitre.getArbitre();
		for (TupleArbitre tupleArbitre : listArbitres) {
			System.out.println(tupleArbitre.arbitreNom + ", " + tupleArbitre.arbitrePrenom);
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
}
	
	
}
