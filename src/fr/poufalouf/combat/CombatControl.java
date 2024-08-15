package fr.poufalouf.combat;

import org.lwjgl.input.Keyboard;

import fr.poufalouf.tools.Control;
import fr.poufalouf.tools.UserEvent;

/**
 * Type de contr�le li� � un �cran de combat
 * 
 * @author Anaïs Vernet
 */
public class CombatControl extends Control {
	
	/**
	 * Constructeur CombatControl.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * 
	 * @param model
	 * 		Le mod�le � associer � ce contr�le.
	 */
	public CombatControl(CombatModel model) {
		
		super(model);
		
	}

	/**
	 * Ne fait rien.
	 */
	@Override
	public void check() {
		
		// ne rien faire
		
	}

	/**
	 * Traite les informations relatives au contr�le clavier (touche enfonc�e).
	 * 
	 * <p>Les actions g�r�es par cette classe sont :
	 * <ul>
	 * <li>Aucune touche.</li>
	 * </ul></p>
	 * 
	 * <p>Les autres touches sont transmises au mod�le gr�ce � la m�thode setPressed(int, UserEvent) de la classe Model,
	 * � l'�tat KEY_PRESSED.</p>
	 * 
	 * @param keyID
	 * 		L'information clavier � traiter.
	 */
	@Override
	public void pollKeyboardDown(int keyID) {

		CombatModel mod = (CombatModel) this.model;
		
		switch(keyID) {
		case Keyboard.KEY_SPACE:
			mod.setTerminated(true);
			break;
		default:
			mod.setPressed(keyID, UserEvent.KEY_PRESSED);
		}
		
	}

	/**
	 * Traite les informations relatives au contr�le clavier (touche rel�ch�e).
	 * 
	 * <p>Les actions g�r�es par cette classe sont :
	 * <ul>
	 * <li>Aucune touche.</li>
	 * </ul></p>
	 * 
	 * <p>Les autres touches sont transmises au mod�le gr�ce � la m�thode setPressed(int, UserEvent) de la classe Model,
	 * � l'�tat KEY_RELEASED.</p>
	 * 
	 * @param keyID
	 * 		L'information clavier � traiter.
	 */
	@Override
	public void pollKeyboardUp(int keyID) {
		
		CombatModel mod = (CombatModel) this.model;
		
		mod.setPressed(keyID, UserEvent.KEY_RELEASED);

	}

}
