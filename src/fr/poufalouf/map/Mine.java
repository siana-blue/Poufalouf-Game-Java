package fr.poufalouf.map;

import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.Status;
import fr.poufalouf.tools.ZoneType;

/**
 * Objet tueur enfoui dans le sol
 * 
 * @author Anaïs Vernet
 */
public class Mine extends MapObject implements ActivableObject {

	/**
	 * Indique si cette mine est activ�e.
	 */
	private boolean activated;
	/**
	 * L'activateur de cette mine.
	 */
	private MapObject activateur;
	
	/**
	 * Constructeur Mine.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il ajoute �galement une zone d'effet � cette mine.</p>
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
	public Mine(Map carte, int x, int y) {
		
		super("Mine(x:"+x+",y:"+y+")", carte, x, y, Constantes.sizeCell, Constantes.sizeCell);
		this.activated = false;
		this.activateur = this;
		this.addZone(ZoneType.EFFECT, new Rectangle(0.25, 0.5, 0.5, 0.25, Constantes.sizeCell),
				"Zone d'effet "+this.getName());
		this.setToUpdate(false);
		
	}
	
	/**
	 * Met � jour l'animation de cette mine.
	 * 
	 * <p>Les animations de cette mine sont s�lectionn�es de la mani�re suivante :
	 * <ul>
	 * <li>Si l'animation en cours est ACTIVATED et si son timer est � z�ro, alors l'animation choisie est DEAD.</li>
	 * <li>Si l'animation en cours n'est pas DEAD et si cette mine est activ�e, alors l'animation choisie est
	 * ACTIVATED.</li>
	 * </ul></p>
	 */
	@Override
	public void playBehavior() {
		
		if (this.currentAnim().getStatus() == Status.ACTIVATED && this.currentAnim().getTimer() == 0) {
			this.changeCurrentAnim(Status.DEAD);
		}
		else if (this.isActivated() && this.currentAnim().getStatus() != Status.DEAD)
			this.changeCurrentAnim(Status.ACTIVATED);
		else if (this.currentAnim().getStatus() == Status.DEAD)
			this.setToUpdate(false);
		
	}

	/**
	 * Active cette mine, si l'activateur pass� en param�tre a une hauteur nulle.
	 * 
	 * <p>Cela se traduit par la disparition de la zone d'effet de cette mine, et par le recul
	 * de son activateur. Si celui-ci est un personnage, alors il est bless� (appel � la m�thode takesDamages(int)
	 * de la classe Personnage). S'il est un ActivableObject, alors il est activ� par cette mine.</p>
	 * 
	 * @param obj
	 * 		L'activateur de cette mine. S'il est null, il n'est pas associ� � cette mine, et l'activation n'a pas lieu.
	 */
	@Override
	public void activate(MapObject obj) {
		
		if (obj == null)
			return;
		
		if (obj.getHauteur() == 0 && !obj.isStandby()) {
			this.setToUpdate(true);
			this.activated = true;
			this.activateur = obj;
			if (this.activateur instanceof ActivableObject)
				((ActivableObject) this.activateur).activate(this);
			else {
				this.activateur.setVitesse(-4);
				this.activateur.setAcceleration(2);
			}
			this.changeZone(ZoneType.EFFECT, new Rectangle(-1));
			if (this.activateur instanceof MapCharacter) {
				((MapCharacter) this.activateur).takesDamages(8);
			}
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
	 * Ne fait rien, une mine ne se d�sactive pas.
	 */
	@Override
	public void desactivate() {
		
		// ne rien faire
		
	}

	/**
	 * Indique si cette mine est activ�e.
	 * 
	 * @return
	 * 		L'�tat du bool�en activated de cette classe.
	 */
	@Override
	public boolean isActivated() {
		return this.activated;
	}

	/**
	 * Retourne l'activateur de cette mine.
	 * 
	 * @return
	 * 		L'activateur de cette mine.
	 */
	@Override
	public MapObject getActivateur() {
		return this.activateur;
	}

}
