package fr.poufalouf.map;

import java.util.ArrayList;
import java.util.HashMap;

import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.tools.Animation;
import fr.poufalouf.tools.AnimationFrame;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Orientation;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.Status;
import fr.poufalouf.tools.ZoneType;

/**
 * Douni repr�sent� sur une carte
 * 
 * @author Anaïs Vernet
 */
public class Douni extends MapCharacter implements ShootingObject {
	
	/**
	 * La liste des projectiles lanc�s par Douni.
	 */
	private ArrayList<Projectile> projectiles;
	/**
	 * La liste des animations des projectiles de Douni.
	 */
	private ArrayList<Animation> animsProjectiles;
	/**
	 * La date du dernier tir.
	 */
	private int lastShoot;
	/**
	 * Le nombre total de lasers lanc�s.
	 */
	private int nbProjectiles;
	
	/**
	 * Constructeur Douni.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>La collision de cet objet est �galement d�finie ici.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>projectiles : une nouvelle liste (ArrayList) de Projectile.</li>
	 * <li>animsProjectiles : une nouvelle liste (ArrayList) d'Animation.</li>
	 * <li>lastShoot : 0.</li>
	 * <li>nbProjectiles : 0.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de ce Douni.
	 * @param carte
	 * 		La carte � laquelle appartient Douni.
	 * @param x
	 * 		L'abscisse relative au jeu de Douni.
	 * @param y
	 * 		L'ordonn�e relative au jeu de Douni.
	 * @throws ObjectInstanciationException
	 * 		si le constructeur MapCharacter l�ve une ObjectInstanciationException.
	 */
	public Douni(String name, Map carte, int x, int y) throws ObjectInstanciationException {
		
		super("res/scripts/personnages/douni.txt", name, carte, x, y, Constantes.sizeCell, Constantes.sizeCell);
		
		this.projectiles = new ArrayList<Projectile>();
		this.animsProjectiles = new ArrayList<Animation>();
		this.lastShoot = 0;
		this.nbProjectiles = 0;
		
		this.changeZone(ZoneType.COLLISION, new Rectangle(0.25, 0.5, 0.5, 0.5,
				Constantes.sizeCell));
		this.addZone(ZoneType.EFFECT, new Rectangle(0.25, 0.5, 0.5, 0.5, Constantes.sizeCell),
				"Zone d'effet "+this.getName());
		this.addZone(ZoneType.DETECTION, new Rectangle(-1, -1, 3, 3, Constantes.sizeCell),
				"Zone de detection"+this.getName());
		
		this.animsProjectiles.add(new Animation(Status.STILL, "res/textures/map/attacks/orbe_glace.png",
				new AnimationFrame(new Rectangle(0, 0, 1, 4, Constantes.sizeTextureFragment), null, -1), -1));
		this.animsProjectiles.get(0).addFrame(new AnimationFrame(
				new Rectangle(1, 0, 1, 4, Constantes.sizeTextureFragment), null, -1));
		this.animsProjectiles.add(new Animation(Status.ACTIVATED, "res/textures/map/attacks/orbe_glace.png",
				new AnimationFrame(new Rectangle(2, 0, 1, 4, Constantes.sizeTextureFragment), null, -1), 1));
		this.animsProjectiles.add(new Animation(Status.DEAD, "res/textures/map/attacks/orbe_glace.png",
				new AnimationFrame(new Rectangle(3, 0, 1, 4, Constantes.sizeTextureFragment), null, -1), 1));
		
	}
	
	/**
	 * Met � jour le comportement de Douni.
	 * 
	 * <p>Cette m�thode appelle la m�thode playBehavior() de la classe m�re. Dans un deuxi�me temps, le
	 * comportement al�atoire de Douni est g�r�.</p>
	 */
	@Override
	public void playBehavior() {
		
		super.playBehavior();
		
		if (!this.isStandby() && (int) (Math.random()*20) == 0) {
			this.setOrientation(Orientation.values()[(int) (Math.random()*8)+1].toSimpleCardinal());
		}
		if (!this.isStandby() && this.currentAnim().getStatus() != Status.WALKING) {
			if ((int) (Math.random()*10) == 0) {
				this.setVitesse(1);
			}
		} else if (!this.isStandby() && (int) (Math.random()*10) == 0) {
			this.setVitesse(0);
		}
		
		if (this.getCompteurTemps()-this.lastShoot > 2000 && (int) (Math.random()*10) == 0) {
			ArrayList<MapObject> objs = new ArrayList<MapObject>();
			objs.add(this);
			if (this.currentAnim().getStatus() == Status.STILL && this.colliding(objs,
					ZoneType.COLLISION, this.getOrientation()) == null)
				this.shoot();
		}
		
	}

	/**
	 * Lance une boule de glace.
	 * 
	 * <p>Ajoute un projectile � la liste des projectiles de Douni. La date lastShoot de cette classe est alors
	 * mise � jour.</p>
	 */
	@Override
	public void shoot() {
		
		HashMap<ZoneType, Rectangle> zones = new HashMap<ZoneType, Rectangle>();
		zones.put(ZoneType.EFFECT, new Rectangle(0.25, 0.25, 0.5, 0.5, Constantes.sizeCell));
		
		switch (this.getOrientation()) {
		case NORD:
			this.projectiles.add(new Projectile(this, this.animsProjectiles, zones, 4, this.getX(),
					this.getY()-Constantes.sizeCell/2, Constantes.sizeCell, Constantes.sizeCell, 4));
			break;
		case SUD:
			this.projectiles.add(new Projectile(this, this.animsProjectiles, zones, 4, this.getX(),
					this.getY()+Constantes.sizeCell/2, Constantes.sizeCell, Constantes.sizeCell, 4));
			break;
		case EST:
			this.projectiles.add(new Projectile(this, this.animsProjectiles, zones, 4,
					this.getX()+3*Constantes.sizeCell/4, this.getY(), Constantes.sizeCell, Constantes.sizeCell, 4));
			break;
		case OUEST:
			this.projectiles.add(new Projectile(this, this.animsProjectiles, zones, 4,
					this.getX()-3*Constantes.sizeCell/4, this.getY(), Constantes.sizeCell, Constantes.sizeCell, 4));
			break;
		default:
		}
		
		this.lastShoot = (int) this.getCompteurTemps();
		this.nbProjectiles++;
		this.setSound("res/sounds/map/attacks/orbe_glace.wav");
		
	}

	/**
	 * Retourne la liste des projectiles de Douni.
	 * 
	 * @return
	 * 		La liste des projectiles de Douni.
	 */
	@Override
	public ArrayList<Projectile> projectiles() {
		return this.projectiles;
	}

	/**
	 * Retourne le nombre total de projectiles lanc�s par Douni.
	 * 
	 * @return
	 * 		Le nombre total de projectiles lanc�s.
	 */
	@Override
	public int getNbProjectiles() {
		return this.nbProjectiles;
	}

}
