package fr.poufalouf.tools;

import java.io.IOException;
import java.util.ArrayList;

import fr.poufalouf.UpdateException;

/**
 * Classe abstraite servant de lien entre la vue (l'utilisateur) et le mod�le de jeu
 * 
 * <p>Un contr�le ne peut pas �tre instanci� de mani�re g�n�rique. Il est n�cessaire de choisir un type de Control pour le
 * pattern MVC (MapControl pour une carte par exemple).</p>
 * <p>Les fonctions d'un contr�le sont la v�rification de l'�tat du jeu et les corrections �ventuelles, ainsi que la
 * gestion des contr�les utilisateur.</p>
 * <p>Les contr�les poss�dent l'instance du mod�le qu'ils surveillent.</p>
 * 
 * @author Anaïs Vernet
 */
public abstract class Control {
	
	/**
	 * Le mod�le surveill� par ce contr�le.
	 */
	protected Model model;
	
	/**
	 * Constructeur Control.
	 * 
	 * <p></p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>model : le mod�le pass� en param�tre (model), ou un Model standard si le param�tre est null.</li>
	 * </ul></p>
	 * 
	 * @param model
	 * 		Le mod�le � associer � ce contr�le.
	 */
	public Control(Model model) {
		
		if (model != null)
			this.model = model;
		else {
			try {
				this.model = new Model(null) {

					@Override
					public boolean checkParameters(ArrayList<Object> param) {
						return true;
					}
					
					@Override
					public void initialize(ArrayList<Object> param) {
						// ne rien faire
					}

					@Override
					public void updateObjects() {
						// ne rien faire
					}

					@Override
					public void update() {
						// ne rien faire
					}
					
				};
			} catch (Exception e) {
				this.model = null;
				// ne rien faire car impossible
			}
		}
		
	}
	
	/**
	 * V�rifie l'�tat du mod�le avant de lui laisser la main pour sa mise � jour.
	 * 
	 * <p>Cela se fait par un appel � la m�thode check() de cette classe suivi d'un appel � la m�thode refresh() de la
	 * classe Model.</p>
	 * 
	 * @throws IOException
	 * 		si la m�thode refresh() de la classe Model l�ve une IOException.
	 * @throws UpdateException
	 * 		si la m�thode refresh() de la classe Model l�ve une UpdateException.
	 */
	public void refresh() throws IOException, UpdateException {
		
		this.check();
		this.model.refresh();
		
	}
	
	/**
	 * V�rifie l'�tat du mod�le et y apporte les modifications n�cessaires.
	 */
	public abstract void check();
	
	/**
	 * Traite les informations relatives au contr�le clavier (touche enfonc�e).
	 * 
	 * @param keyID
	 * 		L'information clavier � traiter.
	 */
	public abstract void pollKeyboardDown(int keyID);
	
	/**
	 * Traite les informations relatives au contr�le clavier (touche rel�ch�e).
	 * 
	 * @param keyID
	 * 		L'information clavier � traiter.
	 */
	public abstract void pollKeyboardUp(int keyID);

}
