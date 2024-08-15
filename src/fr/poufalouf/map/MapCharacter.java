package fr.poufalouf.map;

import java.util.ArrayList;

import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.game.GameCharacter;
import fr.poufalouf.tools.Characteristic;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Orientation;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.Status;
import fr.poufalouf.tools.ZoneType;

/**
 * Objet repr�sentant un personnage du jeu sur une carte
 * 
 * <p>Cet objet est li� � une instance de GameCharacter.</p>
 * 
 * @author Anaïs Vernet
 */
public abstract class MapCharacter extends MapObject {
	
	/**
	 * Le personnage repr�sent� par cet objet.
	 */
	private GameCharacter character;
	/**
	 * Indique si cet objet est en cours de r�ception de d�g�ts.
	 */
	private boolean hurt;
	
	/**
	 * Constructeur Personnage.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il s'agit d'une classe abstraite, pour instancier un personnage, il est n�cessaire d'utiliser une classe
	 * fille.</p>
	 * <p>L'orientation de ce personnage est initialis�e � SUD.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>character : un nouveau personnage construit avec le fichier dont le nom est pass� en param�tre
	 * (characterFile), et dont le nom est celui de ce personnage.</li>
	 * <li>hurt : false.</li>
	 * </ul></p>
	 * 
	 * @param characterFile
	 * 		Le nom du fichier des caract�ristiques du personnage repr�sent� par cet objet.
	 * @param name
	 * 		Le nom de ce personnage.
	 * @param carte
	 * 		La carte � laquelle appartient ce personnage.
	 * @param x
	 * 		L'abscisse par rapport au jeu de cet objet.
	 * @param y
	 * 		L'ordonn�e par rapport au jeu de cet objet.
	 * @param w
	 * 		La largeur de cet objet.
	 * @param h
	 * 		La hauteur de cet objet.
	 * @throws ObjectInstanciationException
	 * 		si le constructeur GameCharacter l�ve une ObjectInstanciationException.
	 */
	public MapCharacter(String characterFile, String name, Map carte, int x, int y, int w, int h)
	throws ObjectInstanciationException {
		
		super(name, carte, x, y, w, h);
		
		this.character = new GameCharacter(this.getName(), characterFile);
		this.hurt = false;
		
		this.setOrientation(Orientation.SUD);
		
	}
	
	/**
	 * Retourne la caract�ristique de ce personnage sp�cifi�e en param�tre.
	 * 
	 * <p>Il s'agit d'un lien vers le GameCharacter de cette classe.</p>
	 * 
	 * @param c
	 * 		Le type de caract�ristique � retourner.
	 * @return
	 * 		La caract�ristique demand�e, ou null si cette caract�ristique n'est pas renseign�e.
	 */
	public int characteristic(Characteristic c) {
		
		return this.character.characteristic(c);
		
	}
	
	/**
	 * Modifie une caract�ristique de ce personnage, ou l'ajoute si elle n'est pas renseign�e.
	 * 
	 * <p>Il s'agit d'un lien vers le GameCharacter de cette classe.</p>
	 * 
	 * @param c
	 * 		La caract�ristique � modifier.
	 * @param value
	 * 		La nouvelle valeur de la caract�ristique.
	 */
	public void changeCharacteristic(Characteristic c, int value) {
		
		this.character.changeCharacteristic(c, value);
		
	}
	
	/**
	 * Joue le comportement de ce personnage quand il encaisse des d�g�ts.
	 * 
	 * @param damages
	 * 		Le nombre de d�g�ts subis par ce personnage.
	 */
	public void takesDamages(int damages) {
		
		this.setHurt(true);
		this.setStandby(true);
		int health = this.characteristic(Characteristic.HP);
		this.changeCharacteristic(Characteristic.HP, Math.max(health-damages, 0));

		
	}
	
	/**
	 * Met � jour l'animation et l'�tat de ce personnage.
	 * 
	 * <p>Les animations de ce personnage sont s�lectionn�es de la mani�re suivante :
	 * <ul>
	 * <li>Si le timer de l'animation en cours est � z�ro, l'animation STILL est choisie. S'il s'agissait de
	 * l'animation HURT, alors ce personnage n'est plus en standby. Si l'animation �tait DYING, alors DEAD est choisie.</li>
	 * <li>Si les points de vie de ce personnage sont � z�ro et si ce personnage n'est pas en standby, alors DYING est
	 * choisie.</li>
	 * <li>Si ce personnage re�oit des d�g�ts, l'animation HURT est choisie.</li>
	 * <li>Si la vitesse verticale de ce personnage est non nulle, l'animation JUMPING est choisie. Les cases
	 * VOID sont alors accessibles � ce personnage.</li>
	 * <li>Si la vitesse de ce personnage est strictement positive, l'animation WALKING est choisie.</li>
	 * <li>Dans les autres cas, l'animation STILL est choisie, si l'animation en cours n'est pas HURT.</li>
	 * </ul></p>
	 * 
	 * <p>Le comportement d'un personnage est le suivant :
	 * <ul>
	 * <li>Si ce personnage est bless� (bool�en hurt), alors le bool�en hurt de la classe MapObject est pass� � l'�tat
	 * faux, ce personnage passe en standby, et sa vitesse est fix�e � -4, son acc�l�ration � 2.</li>
	 * <li>Si sa vitesse verticale est non nulle, alors sa hauteur augmente en fonction de sa valeur.</li>
	 * <li>Si la hauteur devient sup�rieure au champ heightJump de la classe Constantes, la vitesse verticale de
	 * ce personnage change de signe, et ainsi il tombe.</li>
	 * <li>Si la vitesse verticale de ce personnage est strictement n�gative et si sa hauteur est inf�rieure ou �gale �
	 * 0, alors sa vitesse verticale et sa hauteur sont annul�es. Les cases VOID deviennent inacessibles.</li>
	 * <li>Si la vitesse de ce personnage est non nulle, il bouge (m�thode move() de la classe MapObject)
	 * d'autant de pas que la valeur absolue de sa vitesse. D�s qu'il y a une collision, le mouvement s'arr�te.</li>
	 * <li>Sinon, il est arr�t�.</li>
	 * </ul></p>
	 * 
	 * <p>Si ce personnage est sur une case interdite, et qu'il n'est pas en train de sauter,
	 * alors ses points de vie sont mis � z�ro.</p>
	 */
	@Override
	public void playBehavior() {
		
		ArrayList<MapObject> objs = new ArrayList<MapObject>();
		MapObject brume = this.carte.objets().get("Brume");
		if (brume != null)
			objs.add(brume);
		int health = this.characteristic(Characteristic.HP);
		if (this.currentAnim().getTimer() == 0) {
			switch (this.currentAnim().getStatus()) {
			case HURT:
				this.setStandby(false);
				this.changeCurrentAnim(Status.STILL);
				break;
			case DYING:
				this.changeCurrentAnim(Status.DEAD);
				break;
			case DEAD:
			case DROWNING:
				this.setFinished(true);
				break;
			default:
				this.changeCurrentAnim(Status.STILL);
			}
		} else if (this.isHurt() && this.currentAnim().getStatus() != Status.HURT) {
			this.changeCurrentAnim(Status.HURT);
			this.setHurt(false);
		} else if (health == 0 && this.currentAnim().getStatus() != Status.DEAD
				&& this.currentAnim().getStatus() != Status.DYING && this.currentAnim().getStatus() != Status.DROWNING) {
			this.setStandby(true);
			this.changeCurrentAnim(Status.DYING);
			this.setOrientation(Orientation.AUCUNE);
			this.changeZone(ZoneType.DETECTION, new Rectangle(-1));
		} else if (this.getVitesseZ() != 0) {
			this.changeCurrentAnim(Status.JUMPING);
		} else if (this.getVitesse() > 0) {
			if (this.colliding(objs, ZoneType.COLLISION, this.getOrientation()) == null)
				this.changeCurrentAnim(Status.WALKING);
			else
				this.changeCurrentAnim(Status.STILL);
		} else if (this.currentAnim().getStatus() == Status.JUMPING || this.currentAnim().getStatus() == Status.WALKING) {
			this.changeCurrentAnim(Status.STILL);
		}
	
		if (this.currentAnim().getStatus() == Status.JUMPING)  {
			this.setHauteur(this.getHauteur()+this.getVitesseZ());
			if (this.getHauteur() >= Constantes.heightJump)
				this.setVitesseZ(-this.getVitesseZ());
			if (this.getVitesseZ() < 0 && this.getHauteur() <= 0) {
				this.setVitesseZ(0);
				this.setHauteur(0);
			}
		}
		
		for (int i=0;i<Math.abs(this.getVitesse());i++) {
			if (this.getVitesse() > 0) {
				if (this.colliding(objs, ZoneType.COLLISION, this.getOrientation()) == null)
					this.move(this.getOrientation(), Constantes.sizeStep);
				else {
					this.setVitesse(0);
				}
			} else if (this.getVitesse() < 0) {
				if (this.colliding(objs, ZoneType.COLLISION, this.getOrientation().opposite()) == null)
					this.move(this.getOrientation().opposite(), Constantes.sizeStep);
				else {
					this.setVitesse(0);
				}
			}
		}
		
	}
	
	/**
	 * Retourne le GameCharacter li� � ce personnage.
	 * 
	 * @return
	 * 		Le GameCharacter.
	 */
	public GameCharacter getCharacter() {
		return this.character;
	}
	
	/**
	 * Met � jour l'�tat du bool�en hurt de cette classe.
	 * 
	 * @param hurt
	 * 		Vrai si cet objet re�oit des d�g�ts.
	 */
	public void setHurt(boolean hurt) {
		this.hurt = hurt;
	}
	
	/**
	 * Indique si cet objet est en train d'encaisser des dommages.
	 * 
	 * @return
	 * 		L'�tat du bool�en hurt de cette classe.
	 */
	public boolean isHurt() {
		return this.hurt;
	}

}
