package fr.poufalouf.map;

import java.util.ArrayList;
import java.util.HashMap;

import fr.poufalouf.tools.Animation;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.Status;
import fr.poufalouf.tools.ZoneType;

/**
 * Projectile lanc� par un objet sur une carte 2D
 * 
 * @author Anaïs Vernet
 */
public class Projectile extends MapObject implements ActivableObject {
	
	/**
	 * Le lanceur de ce projectile.
	 */
	private ShootingObject lanceur;
	/**
	 * Le nombre de blessures inflig�es par ce projectile.
	 */
	private int strength;
	/**
	 * Indique si ce projectile est activ�.
	 */
	private boolean activated;
	/**
	 * L'activateur de ce projectile.
	 */
	private MapObject activateur;
	
	/**
	 * Constructeur Projectile.
	 * 
	 * <p>Apr�s initialisation, ce constructeur ajoute � la table des animations de cet objet les animations pr�sentes
	 * dans la liste pass�e en param�tre (les animations sont clon�es).</p>
	 * <p>Il donne l'orientation du lanceur � ce projectile.</p>
	 * <p>Il donne la vitesse pass�e en param�tre � ce projectile.</p>
	 * <p>Il ajoute enfin les zones de la liste pass�e en param�tre � ce projectile si cette liste n'est pas nulle. Dans
	 * la table, ce sont les rectangles des coordonn�es par rapport � l'objet des zones qui sont sp�cifi�es.</p>
	 * <p>Si ce projectile entre en collision d�s son lancement, cela est d�tect� dans ce constructeur et le projectile
	 * est activ�.</p>
	 * <p>Le type de case VOID est retir� de la liste des cases inacessibles de cet objet.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>lanceur : le lanceur pass� en param�tre (lanceur), ou un nouveau ShootingObject standard si le param�tre est
	 * null.</li>
	 * <li>strength : la puissance pass�e en param�tre (strength).</li>
	 * <li>activated : false.</li>
	 * <li>activateur : this.</li>
	 * </ul></p>
	 * 
	 * @param lanceur
	 * 		Le lanceur de cet objet.
	 * @param animations
	 * 		La liste des animations de ce projectile.
	 * @param zones
	 * 		La table des rectangle des zones � ajouter � ce projectile (rep�r�es par le types de ces zones).
	 * @param strength
	 * 		La puissance de ce projectile.
	 * @param x
	 * 		L'abscisse de ce projectile.
	 * @param y
	 * 		L'ordonn�e de ce projectile.
	 * @param w
	 * 		La largeur de ce projectile.
	 * @param h
	 * 		La hauteur de ce projectile.
	 * @param vitesse
	 * 		La vitesse de ce projectile.
	 */
	public Projectile(ShootingObject lanceur, ArrayList<Animation> animations, HashMap<ZoneType, Rectangle> zones,
			int strength, double x, double y, double w, double h, int vitesse) {

		super("Projectile "+(!(lanceur instanceof MapObject)?"":
			(lanceur.getNbProjectiles()+" de "+((MapObject) lanceur).getName())),
			(lanceur instanceof MapObject)?((MapObject) lanceur).carte:null, x, y, w, h);
		
		if (lanceur != null)
			this.lanceur = lanceur;
		else
			this.lanceur = new ShootingObject() {

				@Override
				public void shoot() {
					// ne rien faire
				}

				@Override
				public ArrayList<Projectile> projectiles() {
					return null;
				}

				@Override
				public int getNbProjectiles() {
					return 0;
				}
			
		};
		this.strength = strength;
		this.activated = false;
		this.activateur = this;
		
		if (animations != null) {
			for (Animation a : animations) {
				if (a != null)
					this.addAnimation(a.clone());
			}
		}
		
		if (zones != null) {
			for (ZoneType z : ZoneType.values()) {
				Rectangle r = zones.get(z);
				if (r == null)
					continue;
				if (this.zone(z) == null)
					this.addZone(z, r, "Zone "+z.toString()+" "+this.getName());
				else
					this.changeZone(z, r);
			}
		}
		this.setVitesse(vitesse);
		
		if (lanceur instanceof MapObject) {
			this.setOrientation(((MapObject) lanceur).getOrientation());
		}
		
		this.setDelayUpdate(60);
		
	}
	
	/**
	 * Met � jour l'animation et l'�tat de ce projectile.
	 * 
	 * <p>Les animations de ce projectile sont choisies de la mani�re suivante :</p>
	 * <p>
	 * <ul>
	 * <li>Si ce projectile est activ�, alors l'animation ACTIVATED est choisie.</li>
	 * <li>Si le timer de l'animation ACTIVATED est � z�ro, alors l'animation DEAD est choisie.</li>
	 * <li>Si le timer de l'animation DEAD est � z�ro, alors ce projectile est supprim�.</li>
	 * </ul></p>
	 * 
	 * <p>Le projectile avance en fonction de son orientation (m�thode move(Orientation) de la classe MapObject), si
	 * son animation est STILL, et si sa zone d'effet n'entre pas en collision avec un objet de la carte (m�thode
	 * isColliding(ZoneType, Orientation) de cette classe).</p>
	 */
	@Override
	public void playBehavior() {
		
		MapObject o = null;
		ArrayList<MapObject> objs = new ArrayList<MapObject>();
		objs.add((this.getLanceur() instanceof MapObject)?(MapObject) this.getLanceur():null);
		
		if (this.currentAnim().getStatus() == Status.STILL) {
			for (int i=0;i<this.getVitesse();i++) {
				if ((o=this.colliding(objs, ZoneType.EFFECT, this.getOrientation()))
						!= null) {
					this.activate(o);
					break;
				}
				o = this.move(this.getOrientation(), Constantes.sizeStep);
			}
		}
		
		if (this.currentAnim().getTimer() == 0) {
			switch (this.currentAnim().getStatus()) {
			case ACTIVATED:
				this.changeCurrentAnim(Status.DEAD);
				break;
			case DEAD:
				this.setFinished(true);
				if (this.lanceur.projectiles() != null)
					this.lanceur.projectiles().remove(this);
				break;
			default:
			}
		} else if (this.isActivated()) {
			this.changeCurrentAnim(Status.ACTIVATED);
			int d = 0;
			if (o != null) {
				switch (this.getOrientation()) {
				case NORD:
					d = (int) (this.getY()-o.zone(ZoneType.COLLISION).getY()
							-o.zone(ZoneType.COLLISION).getObjRect().getH());
					break;
				case SUD:
					d = (int) (o.zone(ZoneType.COLLISION).getY()
							-this.getY()-this.zone(ZoneType.IMAGE).getObjRect().getH());
					break;
				case OUEST:
					d = (int) (this.getX()-o.zone(ZoneType.COLLISION).getX()
							-o.zone(ZoneType.COLLISION).getObjRect().getW());
					break;
				case EST:
					d = (int) (o.zone(ZoneType.COLLISION).getX()
							-this.getX()-this.zone(ZoneType.IMAGE).getObjRect().getW());
					break;
				default:
				}
			}
			this.move(this.getOrientation(), Constantes.sizeStep/2+d);
		}
		
	}

	/**
	 * Active ce projectile.
	 * 
	 * <p>Cela se traduit par le recul de son activateur, et s'il s'agit d'un personnage, celui-ci
	 * est bless�. Si l'activateur est en standby, ce projectile n'est pas activ�.</p>
	 * <p>Si l'objet pass� en param�tre est null, alors ce projectile est activ� et sa zone d'effet est annul�e. S'il est
	 * this, il n'est pas associ� � ce projectile, et l'activation n'a pas lieu. S'il s'agit du lanceur de ce projectile,
	 * rien n'est fait non plus.</p>
	 * <p>Enfin, tous les projectiles ayant le m�me num�ro de salve et le m�me lanceur que celui-ci s'activent avec le m�me
	 * activateur. Un projectile ayant un num�ro de salve �gal � 0 est consid�r� ind�pendant des autres projectiles
	 * portant le num�ro 0.</p>
	 * 
	 * @param obj
	 * 		L'activateur de ce projectile.
	 */
	@Override
	public void activate(MapObject obj) {
		
		if (obj == null  || obj.isStandby()) {
			this.activated = true;
		} else {
			if (obj == this.lanceur)
				return;
			if (obj instanceof Projectile) {
				if (((Projectile) obj).getLanceur() == this.lanceur)
					return;
				((Projectile) obj).activate(null);
			}
			
			this.activated = true;
			this.activateur = obj;
			
			if (this.activateur instanceof MapCharacter) {
				this.activateur.setOrientation(this.getOrientation().opposite());
				this.activateur.setVitesse(-4);
				this.activateur.setAcceleration(2);
				((MapCharacter) this.activateur).takesDamages(this.strength);
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
	 * Met � l'�tat faux le bool�en activated de cette classe.
	 */
	@Override
	public void desactivate() {
		
		this.activated = false;
		
	}

	/**
	 * Retourne le lanceur de ce projectile.
	 * 
	 * @return
	 * 		Le lanceur de ce projectile.
	 */
	public ShootingObject getLanceur() {
		return this.lanceur;
	}
	
	/**
	 * Indique si ce projectile est activ�e.
	 * 
	 * @return
	 * 		L'�tat du bool�en activated de cette classe.
	 */
	@Override
	public boolean isActivated() {
		return this.activated;
	}

	/**
	 * Retourne l'activateur de ce projectile.
	 * 
	 * @return
	 * 		L'activateur de ce projectile.
	 */
	@Override
	public MapObject getActivateur() {
		return this.activateur;
	}
	
}
