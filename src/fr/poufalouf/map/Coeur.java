package fr.poufalouf.map;

import fr.poufalouf.tools.Characteristic;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.Status;
import fr.poufalouf.tools.ZoneType;

/**
 * Coeur redonnant un point de vie � Poufalouf
 * 
 * @author Anaïs Vernet
 */
public class Coeur extends MapObject implements ActivableObject {

	/**
	 * Indique si cette mine est activ�e.
	 */
	private boolean activated;
	/**
	 * L'activateur de cette mine.
	 */
	private MapObject activateur;
	
	/**
	 * Constructeur coeur.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il ajoute �galement une zone d'effet � ce coeur.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>activated : false.</li>
	 * <li>activateur : this.</li>
	 * </ul></p>
	 * 
	 * @param carte
	 * 		La carte � laquelle appartient cette mine.
	 * @param x
	 * 		L'abscisse par rapport au jeu de cette mine.
	 * @param y
	 * 		L'ordonn�e par rapport au jeu de cette mine.
	 */
	public Coeur(Map carte, int x, int y) {
		
		super("Coeur(x:"+x+",y:"+y+")", carte, x, y, Constantes.sizeCell, Constantes.sizeCell);
		this.activated = false;
		this.activateur = this;
		this.addZone(ZoneType.EFFECT, new Rectangle(0.25, 0.25, 0.5, 0.5, Constantes.sizeCell),
				"Zone d'effet "+this.getName());
		
	}
	
	/**
	 * Met � jour l'animation de ce coeur.
	 * 
	 * <p>Les animations de ce coeur sont s�lectionn�es de la mani�re suivante :
	 * <ul>
	 * <li>Si l'animation en cours est ACTIVATED et si son timer est � z�ro, alors ce coeur est d�truit.</li>
	 * <li>Si ce coeur est activ�, alors l'animation choisie est ACTIVATED.</li>
	 * </ul></p>
	 */
	@Override
	public void playBehavior() {
		
		if (this.currentAnim().getStatus() == Status.ACTIVATED && this.currentAnim().getTimer() == 0) {
			this.setFinished(true);
		} else if (this.isActivated())
			this.changeCurrentAnim(Status.ACTIVATED);
		
	}
	
	/**
	 * Active ce coeur, si l'activateur (un personnage) n'est pas en train d'�tre bless� et n'a pas sa sant� au maximum.
	 * 
	 * <p>Cela se traduit par la disparition de la zone d'effet de ce coeur, et par le gain d'un point de vie de
	 * l'activateur, si ses points de vie ne sont pas d�j� au maximum.</p>
	 * 
	 * @param obj
	 * 		L'activateur de ce coeur. S'il est null, ou n'est pas un personnage, il n'est pas associ� � ce coeur,
	 * et l'activation n'a pas lieu.
	 */
	@Override
	public void activate(MapObject obj) {
		
		if (!(obj instanceof MapCharacter))
			return;
		int health, maxHealth;
		MapCharacter perso = (MapCharacter) obj;
		health = perso.characteristic(Characteristic.HP);
		maxHealth = perso.characteristic(Characteristic.HPMAX);
		if (perso.currentAnim().getStatus() != Status.HURT && health < maxHealth) {
			this.activated = true;
			this.activateur = obj;
			this.changeZone(ZoneType.EFFECT, new Rectangle(-1));
			perso.changeCharacteristic(Characteristic.HP, Math.min(health+2, maxHealth));
		}
		
	}

	/**
	 * Ne fait rien.
	 */
	@Override
	public void playEffect() {

		// ne rien faire
		
	}

	/**
	 * Ne fait rien.
	 */
	@Override
	public void desactivate() {

		// ne rien faire
		
	}

	/**
	 * Indique si ce coeur est activ�.
	 * 
	 * @return
	 * 		L'�tat du bool�en activated de cette classe.
	 */
	@Override
	public boolean isActivated() {
		return this.activated;
	}

	/**
	 * Retourne l'activateur de ce coeur.
	 * 
	 * @return
	 * 		L'activateur de ce coeur.
	 */
	@Override
	public MapObject getActivateur() {
		return this.activateur;
	}

}
