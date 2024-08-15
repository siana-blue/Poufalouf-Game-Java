package fr.poufalouf.map;

import org.lwjgl.input.Keyboard;

import fr.poufalouf.tools.Control;
import fr.poufalouf.tools.UserEvent;
import fr.poufalouf.tools.ZoneType;

/**
 * Type de contr�le li� � une Map
 * 
 * <p>Le but de ce contr�le est de traiter les commandes utilisateur.</p>
 * 
 * @author Anaïs Vernet
 */
public class MapControl extends Control {
	
	/**
	 * Constructeur MapControl.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * 
	 * @param model
	 * 		Le mod�le � associer � ce contr�le.
	 */
	public MapControl(MapModel model) {
		
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
	 * <li>Numpad 1 : afficher les collisions.</li>
	 * <li>Numpad 2 : afficher les rectangles d'images.</li>
	 * <li>Numpad 3 : afficher les zones d'effet.</li>
	 * <li>Numpad 4 : afficher les zones de d�tection.</li>
	 * <li>Espace : afficher les anneaux de d�tection.</li>
	 * </ul></p>
	 * 
	 * <p>Les autres touches mettent � jour la table des touches enfonc�es de la classe Model.</p>
	 * 
	 * @param keyID
	 * 		L'information clavier � traiter.
	 */
	@Override
	public void pollKeyboardDown(int keyID) {
		
		MapModel mod = (MapModel) this.model;
		
		switch (keyID) {
		case Keyboard.KEY_NUMPAD1:
			mod.drawZone(ZoneType.COLLISION, true);
			break;
		case Keyboard.KEY_NUMPAD2:
			mod.drawZone(ZoneType.IMAGE, true);
			break;
		case Keyboard.KEY_NUMPAD3:
			mod.drawZone(ZoneType.EFFECT, true);
			break;
		case Keyboard.KEY_NUMPAD4:
			mod.drawZone(ZoneType.DETECTION, true);
			break;
		case Keyboard.KEY_SPACE:
			mod.setShowingDetectionRings(true);
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
	 * <li>Numpad 1 : ne pas afficher les collisions.</li>
	 * <li>Numpad 2 : ne pas afficher les rectangles d'images.</li>
	 * <li>Numpad 3 : ne pas afficher les zones d'effet.</li>
	 * <li>Numpad 4 : ne pas afficher les zones de d�tection.</li>
	 * <li>Espace : afficher les anneaux de d�tection.</li>
	 * </ul></p>
	 * 
	 * <p>Les autres touches mettent � jour la table des touches enfonc�es de la classe Model.</p>
	 * 
	 * @param keyID
	 * 		L'information clavier � traiter.
	 */
	@Override
	public void pollKeyboardUp(int keyID) {
		
		MapModel mod = (MapModel) this.model;
		
		switch (keyID) {
		case Keyboard.KEY_NUMPAD1:
			mod.drawZone(ZoneType.COLLISION, false);
			break;
		case Keyboard.KEY_NUMPAD2:
			mod.drawZone(ZoneType.IMAGE, false);
			break;
		case Keyboard.KEY_NUMPAD3:
			mod.drawZone(ZoneType.EFFECT, false);
			break;
		case Keyboard.KEY_NUMPAD4:
			mod.drawZone(ZoneType.DETECTION, false);
			break;
		case Keyboard.KEY_SPACE:
			mod.setShowingDetectionRings(false);
			break;
		default:
			mod.setPressed(keyID, UserEvent.KEY_RELEASED);
		}
		
	}

}
