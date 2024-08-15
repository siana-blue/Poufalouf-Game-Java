package fr.poufalouf.map;

import java.util.ArrayList;
import java.util.HashMap;

import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.tools.Animation;
import fr.poufalouf.tools.AnimationFrame;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.Status;
import fr.poufalouf.tools.ZoneType;

/**
 * Poufalouf repr�sent� sur une carte
 * 
 * @author Anaïs Vernet
 */
public final class Poufalouf extends MapCharacter implements ShootingObject {
	
	/**
	 * La liste des projectiles lanc�s par Poufalouf.
	 */
	private ArrayList<Projectile> projectiles;
	/**
	 * La liste des animations des projectiles de Poufalouf.
	 */
	private ArrayList<Animation> animsProjectiles;
	/**
	 * La taille du laser en cours d'�mission.
	 */
	private int laserSize;
	/**
	 * La date du dernier tir.
	 */
	private int lastShoot;
	/**
	 * Le nombre total de lasers lanc�s.
	 */
	private int nbProjectiles;
	
	/**
	 * Constructeur Poufalouf.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>La collision de cet objet est �galement d�finie ici.</p>
	 * <p>Les animations des projectiles sont ensuite fix�es.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>projectiles : une nouvelle liste (ArrayList) de Projectile.</li>
	 * <li>animsProjectiles : une nouvelle liste (ArrayList) d'Animation.</li>
	 * <li>laserSize : 0.</li>
	 * <li>lastShoot : 0.</li>
	 * <li>nbProjectiles : 0.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de ce Poufalouf.
	 * @param carte
	 * 		La carte � laquelle appartient Poufalouf.
	 * @param x
	 * 		L'abscisse relative au jeu de Poufalouf.
	 * @param y
	 * 		L'ordonn�e relative au jeu de Poufalouf.
	 * @throws ObjectInstanciationException
	 * 		si le constructeur MapCharacter l�ve une ObjectInstanciationException.
	 */
	public Poufalouf(String name, Map carte, int x, int y) throws ObjectInstanciationException {
		
		super("res/scripts/personnages/poufalouf.txt", name, carte, x, y, Constantes.sizeCell, Constantes.sizeCell);
		
		this.projectiles = new ArrayList<Projectile>();
		this.animsProjectiles = new ArrayList<Animation>();
		this.laserSize = 0;
		this.lastShoot = 0;
		this.nbProjectiles = 0;
		
		this.changeZone(ZoneType.COLLISION, new Rectangle(0.25, 0.5, 0.5, 0.5,
				Constantes.sizeCell));
		this.addZone(ZoneType.EFFECT, new Rectangle(0.25, 0.5, 0.5, 0.5,
				Constantes.sizeCell), "Zone d'effet "+this.getName());
		
		this.animsProjectiles.add(new Animation(Status.STILL, "res/textures/map/attacks/laser.png",
				new AnimationFrame(new Rectangle(-1, 0, 1, 1, Constantes.sizeTextureFragment), null, -1), -1));
		this.animsProjectiles.add(new Animation(Status.ACTIVATED, "res/textures/map/attacks/laser.png",
				new AnimationFrame(new Rectangle(0, 2, 1, 1, Constantes.sizeTextureFragment), null, -1), 1));
		this.animsProjectiles.add(new Animation(Status.DEAD, "res/textures/map/attacks/laser.png",
				new AnimationFrame(new Rectangle(1, 2, 1, 1, Constantes.sizeTextureFragment), null, -1), 1));
		
	}

	/**
	 * Lance un laser rose.
	 * 
	 * <p>Ajoute une salve de quatre projectiles � la liste des projectiles de Poufalouf, orient�s selon la position de
	 * Poufalouf. Une zone de d�g�ts et une zone d'effet sont ajout�es aux projectiles.</p>
	 * <p>La variable laserSize de cette classe est alors incr�ment�e. Quand elle atteint la taille maximale du laser,
	 * cette m�thode ne tire plus de laser jusqu'� ce que le compteur temps de Poufalouf indique une date suffisamment
	 * grande par rapport � la date lastShoot de cette classe. Si le tir a lieu, la variable lastShoot prend la
	 * valeur du compteur temps de Poufalouf.</p>
	 */
	@Override
	public void shoot() {
		
		if (this.currentAnim().getStatus() != Status.STILL)
			return;
		
		HashMap<ZoneType, Rectangle> zones = new HashMap<ZoneType, Rectangle>();
		
		ArrayList<MapObject> objets = new ArrayList<MapObject>();
		objets.add(this);
		for (Projectile o : this.projectiles()) {
			objets.add(o);
		}
		
		if (this.laserSize > 4) {
			if (this.getCompteurTemps()-this.lastShoot < 2000) {
				return;
			}
				this.laserSize = 0;
		}
		
		MapObject o = this.colliding(objets, ZoneType.COLLISION, this.getOrientation());
		Projectile p = null;
		switch (this.getOrientation()) {
		case NORD:
			zones.put(ZoneType.EFFECT, new Rectangle(0.05, 0, 0.1, 0.25, Constantes.sizeCell));
			for (int i=0;i<4;i++) {
				p = new Projectile(this, this.animsProjectiles, zones, 4,
						this.getX()+2*Constantes.sizeCell/5, this.getY()-(i-1)*Constantes.sizeCell/4,
						Constantes.sizeCell/4, Constantes.sizeCell/4, 4);
				p.refreshCells();
				this.projectiles.add(p);
				this.nbProjectiles++;
				// Si Poufalouf est en collision avec un objet, alors le projectile est directement activ�.
				if (o != null && o != this) {
					p.activate(o);
					p.setVitesse(0);
					p.changeCurrentAnim(Status.ACTIVATED);
					break;
				} else if ((o=p.colliding(objets, ZoneType.EFFECT, this.getOrientation())) != null) {
					p.activate(o);
					p.setVitesse(0);
					p.changeCurrentAnim(Status.ACTIVATED);
					break;
				}
			}
			break;
		case SUD:
			zones.put(ZoneType.EFFECT, new Rectangle(0.05, 0, 0.1, 0.25, Constantes.sizeCell));
			for (int i=0;i<4;i++) {
				p = new Projectile(this, this.animsProjectiles, zones, 4,
						this.getX()+2*Constantes.sizeCell/5, this.getY()+Constantes.sizeCell/2+i*Constantes.sizeCell/4,
						Constantes.sizeCell/4, Constantes.sizeCell/4, 4);
				p.refreshCells();
				this.projectiles.add(p);
				this.nbProjectiles++;
				if (o != null && o != this) {
					p.activate(o);
					p.setVitesse(0);
					p.changeCurrentAnim(Status.ACTIVATED);
					break;
				} else if ((o=p.colliding(objets, ZoneType.IMAGE, this.getOrientation())) != null) {
					p.activate(o);
					p.setVitesse(0);
					p.changeCurrentAnim(Status.ACTIVATED);
					break;
				}
			}
			break;
		case EST:
			zones.put(ZoneType.EFFECT, new Rectangle(0, 0.08, 0.25, 0.1, Constantes.sizeCell));
			for (int i=0;i<4;i++) {
				p = new Projectile(this, this.animsProjectiles, zones, 4,
						this.getX()+3*Constantes.sizeCell/4+i*Constantes.sizeCell/4, this.getY()+Constantes.sizeCell/4,
						Constantes.sizeCell/4, Constantes.sizeCell/4, 4);
				p.refreshCells();
				this.projectiles.add(p);
				this.nbProjectiles++;
				if (o != null && o != this) {
					p.activate(o);
					p.setVitesse(0);
					p.changeCurrentAnim(Status.ACTIVATED);
					break;
				} else if ((o=p.colliding(objets, ZoneType.IMAGE, this.getOrientation())) != null) {
					p.activate(o);
					p.setVitesse(0);
					p.changeCurrentAnim(Status.ACTIVATED);
					break;
				}
			}
			break;
		case OUEST:
			zones.put(ZoneType.EFFECT, new Rectangle(0, 0.08, 0.25, 0.1, Constantes.sizeCell));
			for (int i=0;i<4;i++) {
				p = new Projectile(this, this.animsProjectiles, zones, 4,
						this.getX()-i*Constantes.sizeCell/4, this.getY()+Constantes.sizeCell/4,
						Constantes.sizeCell/4, Constantes.sizeCell/4, 4);
				p.refreshCells();
				this.projectiles.add(p);
				this.nbProjectiles++;
				if (o != null && o != this) {
					p.activate(o);
					p.setVitesse(0);
					p.changeCurrentAnim(Status.ACTIVATED);
					break;
				} else if ((o=p.colliding(objets, ZoneType.IMAGE, this.getOrientation())) != null) {
					p.activate(o);
					p.setVitesse(0);
					p.changeCurrentAnim(Status.ACTIVATED);
					break;
				}
			}
			break;
		default:
		}
		
		this.laserSize++;
		this.lastShoot = (int) this.getCompteurTemps();
		this.setSound("res/sounds/map/attacks/laser_court.wav");
		
	}

	/**
	 * Retourne la liste des projectiles de Poufalouf.
	 * 
	 * @return
	 * 		La liste des projectiles de Poufalouf.
	 */
	@Override
	public ArrayList<Projectile> projectiles() {
		return this.projectiles;
	}

	/**
	 * Retourne le nombre total de projectiles lanc�s par Poufalouf.
	 * 
	 * @return
	 * 		Le nombre total de lasers lanc�s.
	 */
	@Override
	public int getNbProjectiles() {
		return this.nbProjectiles;
	}
	
}
