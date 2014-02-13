package ligueBaseball;

import java.sql.SQLException;
import java.util.List;

public class GestionJoueur {
	private Joueur joueur;
	private Connexion cx;

	public GestionJoueur(Joueur joueur) {
		this.cx = joueur.getConnexion();
		this.joueur = joueur;
	}

	public void ajout(String joueurNom, String joueurPrenom, String equipe, int numero) 	
			throws SQLException, LigueBaseballException, Exception {
		try{
			if(!joueur.equipeExiste(equipe))
				throw new LigueBaseballException("equipe inexistante");
			if(!joueur.numeroExiste(equipe, numero))
				throw new LigueBaseballException("Numero deja pris!");
			
			joueur.ajoutJoueur(joueurNom, joueurPrenom, numero, equipe);
		}
		catch (Exception e){
			cx.rollback();
			throw e;
		}
	}
	
	public void ajout(String joueurNom, String joueurPrenom)
			throws SQLException, LigueBaseballException, Exception {
		try {
			if (joueur.existe(joueurNom, joueurPrenom))
				throw new LigueBaseballException("Joueur existe deja: "
						+ joueurNom + " " + joueurPrenom);
			joueur.ajoutJoueur(joueurNom, joueurPrenom);
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	public void supprimer(String joueurNom, String joueurPrenom) throws SQLException,
			LigueBaseballException, Exception {
		try {
			boolean exister = joueur.existe(joueurNom, joueurPrenom);
			if (exister == false)
				throw new LigueBaseballException("Joueur inexistant: "
						+ joueurNom + " " + joueurPrenom);
			
			joueur.suppressionJoueur(joueur.getId(joueurNom, joueurPrenom));
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}
	
	public void getJoueur(int equipeId) throws SQLException
	{
		try{
			List<TupleJoueur> tj = joueur.selectjoueurEquipe(equipeId);
			for(TupleJoueur t : tj)
			{
				System.out.println(t.Nom + " " + t.Prenom + " " + t.EquipeNom);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
