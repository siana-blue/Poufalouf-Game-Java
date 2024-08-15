package fr.poufalouf.map;

import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Orientation;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.Status;
import fr.poufalouf.tools.ZoneType;

/**
 * Chaise tournante sur laquelle Poufalouf peut jouer
 * 
 * <p>Si Poufalouf p�n�tre la zone d'effet de cet objet, son image dispara�t pour laisser place � une animation
 * de chaise tournante. Si le joueur appuie sur la touche de saut, Poufalouf est �ject� de cette chaise.
 * Cette chaise reste orient�e dans la direction qu'elle avait lors de l'arr�t de l'animation.</p>
 * 
 * @author Anaïs Vernet
 */
public class Chaise extends MapObject implements ActivableObject {
	
	/**
	 * Indique si l'activateur de cette chaise tourne dessus.
	 */
	private boolean activTourne;
	/**
	 * Indique si cette chaise est activ�e.
	 */
	private boolean activated;
	/**
	 * L'activateur de cette chaise.
	 */
	private MapObject activateur;
	
	/**
	 * Constructeur Chaise.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Cette chaise est ensuite orient�e d'apr�s le dernier param�tre de ce constructeur.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>activTourne : false.</li>
	 * <li>activated : false.</li>
	 * <li>activateur : this.</li>
	 * </ul></p>
	 * 
	 * @param carte
	 * 		La carte � laquelle appartient cette chaise.
	 * @param x
	 * 		L'abscisse relative au jeu de cette chaise.
	 * @param y
	 * 		L'ordonn�e relative au jeu de cette chaise.
	 * @param orientation
	 * 		L'orientation initiale de cette chaise.
	 */
	public Chaise(Map carte, int x, int y, Orientation orientation) {
		
		super("Chaise(x:"+x+",y:"+y+")", carte, x, y, Constantes.sizeCell, Constantes.sizeCell);
		this.activTourne = false;
		this.activated = false;
		this.activateur = this;
		
		this.setOrientation(orientation);
		this.addZone(ZoneType.EFFECT, new Rectangle(0.25, 0.5, 0.5, 0.5, Constantes.sizeCell),
				"Zone d'effet "+this.getName());
		this.setToUpdate(false);
		
	}
	
	/**
	 * Met � jour l'animation de cette chaise.
	 * 
	 * <p>Deux animations sont possibles : chaise immobile et chaise tournante. Si la chaise est activ�e,
	 * la deuxi�me animation sera choisie. Dans le cas contraire, ce sera la premi�re.</p>
	 */
	@Override
	public void playBehavior() {
		
		if (this.activTourne)
			this.changeCurrentAnim(Status.ACTIVATED);
		else
			this.changeCurrentAnim(Status.STILL);
		
	}
	
	/**
	 * Active cette chaise.
	 * 
	 * <p>Cette chaise ne s'active que si son activateur est Poufalouf et si la hauteur de Poufalouf est nulle.</p>
	 * <p>Apr�s avoir pass� le bool�en activated de cette classe � l'�tat vrai, et chang� l'objet activateur de cette
	 * classe en l'objet pass� en param�tre s'il est non null, l'activation se traduit alors par les actions
	 * suivantes :
	 * <ul>
	 * <li>L'activateur de cette chaise passe en standby.</li>
	 * <li>Le bool�en activTourne de cette classe est pass� � l'�tat vrai.</li>
	 * <li>Poufalouf devient invisible.</li>
	 * <li>La vitesse de Poufalouf devient nulle.</li>
	 * <li>La position de Poufalouf devient celle de cette chaise.</li>
	 * </ul></p>
	 * 
	 * @param act
	 * 		L'objet ayant activ� cette chaise.
	 */
	@Override
	public void activate(MapObject act) {
		
		if (act instanceof Poufalouf && act.currentAnim().getStatus() != Status.JUMPING && !act.isStandby()) {
			this.setToUpdate(true);
			this.activated = true;
			this.activateur = act;
			this.getActivateur().setStandby(true);
			this.activTourne = true;
			this.getActivateur().setVitesse(0);
			act.setX(this.getX());
			this.getActivateur().setY(this.getY());
		}
		
	}
	
	/**
	 * G�re l'effet de cette chaise quand elle est activ�e.
	 * 
	 * <p>Si la vitesse verticale de l'activateur de cette chaise est strictement sup�rieure � z�ro, alors l'activateur
	 * est �ject� de cette chaise. Sinon, cette chaise tourne dans le sens des aiguilles d'une montre (m�thode
	 * next(boolean) de la classe Orientation). L'�jection se traduit par les actions suivantes :
	 * <ul>
	 * <li>Le bool�en activTourne de cette classe est pass� � l'�tat faux.</li>
	 * <li>L'activateur prend l'orientation de cette chaise.</li>
	 * <li>L'activateur redevient visible.</li>
	 * <li>La vitesse de l'activateur est fix�e � 3.</li>
	 * <li>Quand l'activateur retombe sur terre apr�s l'�jection (vitesse verticale nulle), sa vitesse est annul�e, et
	 * son orientation passe � un point cardinal simple (m�thode toSimpleCardinal() de la classe Orientation).</li>
	 * <li>L'activateur n'est plus en standby.</li>
	 * <li>Cette chaise est d�sactiv�e.</li>
	 * </ul></p>
	 */
	@Override
	public void playEffect() {
	
		// Ejecter Poufalouf s'il saute
		if (this.getActivateur().currentAnim().getStatus() == Status.JUMPING && this.activTourne) {
			this.activTourne = false;
			this.getActivateur().setOrientation(this.getOrientation());
			this.getActivateur().setInvisible(false);
			this.getActivateur().setVitesse(3);
		} else if (!this.activTourne && this.getActivateur().currentAnim().getStatus() != Status.JUMPING) {
			this.getActivateur().setVitesse(0);
			this.getActivateur().setOrientation(this.getActivateur().getOrientation().toSimpleCardinal());
			this.getActivateur().setStandby(false);
			this.desactivate();
		} else if (this.activTourne) {
			// Faire tourner la chaise
			this.getActivateur().setInvisible(true);
			this.setOrientation(this.getOrientation().next(true));
			if (this.getOrientation() == Orientation.SUD)
				this.setSound("res/sounds/map/roule.wav");
		}
	}

	/**
	 * D�sactive cette chaise.
	 * 
	 * <p>Le bool�en activated de cette classe passe � l'�tat faux, et l'objet activateur devient this.</p>
	 */
	@Override
	public void desactivate() {
		
		this.setToUpdate(false);
		this.activated = false;
		this.activateur = this;
		
	}

	/**
	 * Indique si cet objet est activ� gr�ce au bool�en activated de cette classe.
	 * 
	 * @return
	 * 		Vrai si cet objet est activ�.
	 */
	@Override
	public boolean isActivated() {
		return this.activated;
	}

	/**
	 * Retourne l'objet activateur de cette chaise.
	 */
	@Override
	public MapObject getActivateur() {
		return this.activateur;
	}

}
