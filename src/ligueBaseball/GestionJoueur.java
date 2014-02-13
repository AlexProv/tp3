package ligueBaseball;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class GestionJoueur {
	private Joueur joueur;
	private Equipe equipe;
	private Connexion cx;

	public GestionJoueur(Joueur joueur, Equipe equipe) {
		this.cx = joueur.getConnexion();
		this.joueur = joueur;
		this.equipe = equipe;
	}

	public void ajout(String joueurNom, String joueurPrenom, String nomEquipe, int numero,Date dateDebut)
			throws SQLException, LigueBaseballException, Exception {
		try{
			if(equipe.existe(nomEquipe) == -1)
				throw new LigueBaseballException("equipe inexistante");
			if(!joueur.numeroExiste(nomEquipe, numero))
				throw new LigueBaseballException("Numero deja pris!");
			
			joueur.ajoutJoueur(joueurNom, joueurPrenom, numero, nomEquipe,dateDebut);
		}
		catch (Exception e){
			cx.rollback();
			throw e;
		}
	}
	
	public void ajout(String joueurNom, String joueurPrenom, String nomEquipe, int numero) 	
			throws SQLException, LigueBaseballException, Exception {
		try{
			if(equipe.existe(nomEquipe) == -1)
				throw new LigueBaseballException("equipe inexistante");
			if(!joueur.numeroExiste(nomEquipe, numero))
				throw new LigueBaseballException("Numero deja pris!");
			
			joueur.ajoutJoueur(joueurNom, joueurPrenom, numero, nomEquipe);
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
	
	public void getJoueur(String nomEquipe) throws SQLException
	{
		int equipeId = equipe.existe(nomEquipe);
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

	public void getJoueur() throws SQLException
	{
		getJoueur("");
	}
}
