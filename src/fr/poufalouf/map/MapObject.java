package fr.poufalouf.map;

import java.util.ArrayList;
import java.util.HashMap;

import fr.poufalouf.game.GameObject;
import fr.poufalouf.tools.CombinedImage;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Orientation;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.Status;
import fr.poufalouf.tools.TexturedImage;
import fr.poufalouf.tools.Zone;
import fr.poufalouf.tools.ZoneType;

/**
 * Objet du jeu sur une carte 2D vue de haut
 * 
 * <p>Cette classe est abstraite, il n'est pas possible de d�finir un MapObject, mais il faut utiliser
 * une classe fille qui sp�cifie son comportement.</p>
 * <p>Un MapObject impl�mente l'interface Drawable et pourra g�n�rer une image pour afficher son �tat.</p>
 * 
 * <b>Initialisation d'un MapObject</b>
 * 
 * <p>Pour cr�er un nouveau MapObject, il faut suivre les �tapes suivantes :
 * <ul>
 * <li>Appeler le constructeur de cette classe avec comme param�tres la carte � laquelle appartient l'objet, ses
 * coordonn�es, ses dimensions et son identifiant de texture.</li>
 * <li>D�finir les animations de l'objet : pour cela, il faut cr�er plusieurs instances de classe Animation, rep�r�es
 * par un Status, et compos�es d'AnimationFrame. Il faudra alors mettre � jour ou cr�er le fichier script correspondant
 * du dossier animations.</li>
 * <li>D�finir un comportement de l'objet gr�ce � une red�finition �ventuelle des m�thodes updateAnim() et playBehavior()
 * de cette classe. Pour les classes h�ritant de cette classe, d'autres m�thodes peuvent �tre � red�finir.</li>
 * </ul></p>
 * 
 * @author Anaïs Vernet
 */
public abstract class MapObject extends GameObject {
	
	/**
	 * La carte dans laquelle �volue cet objet.
	 */
	protected final Map carte;
	/**
	 * La liste des cases sur lesquelles se trouve cet objet.
	 */
	private ArrayList<Case> cells;
	/**
	 * Indique si cet objet est invisible.
	 */
	private boolean invisible;
	/**
	 * Indique si cet objet est en standby.
	 */
	private boolean standby;
	/**
	 * La table des zones de cet objet.
	 */
	private HashMap<ZoneType, Zone> zones;
	/**
	 * La vitesse de cet objet.
	 * 
	 * <p>Il s'agit du nombre de pas parcourus en une frame.</p>
	 */
	private int vitesse;
	/**
	 * L'acc�l�ration de cet objet.
	 * 
	 * <p>Il s'agit du gain de vitesse de cet objet par frame.</p>
	 */
	private int acceleration;
	/**
	 * La hauteur de cet objet.
	 */
	private int hauteur;
	/**
	 * La vitesse de d�placement en hauteur de cet objet.
	 */
	private int vitesseZ;
	/**
	 * Indique si cet objet doit �tre mis � jour lors de la boucle du mod�le.
	 */
	private boolean toUpdate;
	/**
	 * Indique si cet objet vole.
	 */
	private boolean flying;
	
	/**
	 * Constructeur MapObject.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Apr�s l'initialisation, ce constructeur ajoute deux zones � la table des zones de cette classe. Une zone
	 * d'image d�finissant la zone d'affichage de l'image de cet objet, et une zone de collision. Ces deux zones sont
	 * r�f�renc�es respectivement par les ZoneType IMAGE et COLLISION.</p>
	 * <p>La zone d'image est initialis�e comme une nouvelle Zone de couleur bleue, nomm�e "Zone Image "+[nom de cet objet]
	 * de coordonn�es celles de cet objet, et de dimensions celles pass�es en param�tres (w et h).</p>
	 * <p>La zone de collision est initialis�e comme une nouvelle Zone de couleur rouge nomm�e
	 * "Zone Collision "+[nom de cet objet], de coordonn�es celles de cet objet, de dimensions (-1, -1).</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>carte : la carte pass�e en param�tre (carte), ou une nouvelle carte standard si le param�tre est null.</li>
	 * <li>cells : une nouvelle liste (ArrayList) de Case.</li>
	 * <li>invisible : faux.</li>
	 * <li>standby : fauux.</li>
	 * <li>zones : une nouvelle table (HashMap) de Zone r�f�renc�es par des ZoneType.</li>
	 * <li>vitesse : 0.</li>
	 * <li>acceleration : 0.</li>
	 * <li>hauteur : 0.</li>
	 * <li>vitesseZ : 0.</li>
	 * <li>toUpdate : vrai.</li>
	 * <li>flying : faux.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de cet objet.
	 * @param carte
	 * 		La carte � laquelle appartient cet objet.
	 * @param x
	 * 		L'abscisse de cet objet par rapport au jeu.
	 * @param y
	 * 		L'ordonn�e de cet objet par rapport au jeu.
	 * @param w
	 * 		La largeur de cet objet.
	 * @param h
	 * 		La hauteur de cet objet.
	 */
	public MapObject(String name, Map carte, double x, double y, double w, double h) {
		
		super(name, x, y, w, h);
		
		// Initialisation des autres param�tres
		
		if (carte != null)
			this.carte = carte;
		else
			this.carte = new Map(0, 0, null);
		this.cells = new ArrayList<Case>();
		this.invisible = false;
		this.standby = false;
		this.zones = new HashMap<ZoneType, Zone>();
		this.vitesse = 0;
		this.acceleration = 0;
		this.hauteur = 0;
		this.vitesseZ = 0;
		this.toUpdate = true;
		this.flying = false;
		
		this.zones.put(ZoneType.IMAGE,
				new Zone("Zone Image "+this.getName(), this.getX(), this.getY(), new Rectangle(w, h),
						ZoneType.IMAGE.getColor()));
		this.zones.put(ZoneType.COLLISION, new Zone("Zone Collision "+this.getName(), this.getX(), this.getY(),
				new Rectangle(-1), ZoneType.COLLISION.getColor()));
		
	}
	
	/**
	 * Met � jour la liste des cases sur lesquelles se trouve cet objet.
	 * 
	 * <p>Cette m�thode met �galement � jour la liste des occupants des cases concern�es, en supprimant cet objet s'il a
	 * quitt� une case, o� en l'ajoutant � la liste des occupants s'il arrive sur une case.</p>
	 */
	public void refreshCells() {
		
		ArrayList<Case> cases = new ArrayList<Case>();
		Case cell;
		if (this.zone(ZoneType.IMAGE) != null) {
			for (int i=(int) (this.getX()/Constantes.sizeCell);
			i<(this.getX()+this.zone(ZoneType.IMAGE).getObjRect().getW())/Constantes.sizeCell;i++) {
				for (int j=(int) (this.getY()/Constantes.sizeCell);
				j<(this.getY()+this.zone(ZoneType.IMAGE).getObjRect().getH())/Constantes.sizeCell;j++) {
					// Ajout de la case sans risque d'erreur
					cell = this.carte.cell(i, j);
					if (cell != null)
						cases.add(cell);
				}
			}
		}
		for (Case c : this.cells) {
			if (!cases.contains(c))
				c.removeOccupant(this);
		}
		for (Case c : cases) {
			if (!c.occupants().contains(this))
				c.addOccupant(this);
		}
		this.cells = cases;
		
	}
	
	/**
	 * Retourne une copie de la liste des cases de la carte pass�e en param�tre sur lesquelles cet objet se trouve.
	 * 
	 * @param border
	 * 		Vrai si les cases entourant les cases sur lesquelles se trouve cet objet doivent aussi �tre retourn�es.
	 * @return
	 * 		La liste des cases sur lesquelles cet objet se trouve. Ni la liste retourn�e, ni ses �lements ne sont null.
	 */
	public ArrayList<Case> cells(boolean border) {
		
		@SuppressWarnings("unchecked")
		ArrayList<Case> cases = (ArrayList<Case>) this.cells.clone();
		if (border) {
			for (Case cell : this.cells) {
				if (cell == null)
					continue;
				for (int i=Orientation.SUD.ordinal();i<Orientation.SUD_EST.ordinal();i++) {
					Case c = cell.nextCell(Orientation.values()[i]);
					if (!cases.contains(c))
						cases.add(c);
				}
			}
		}
		
		return cases;
		
	}
	
	/**
	 * Indique si cet objet entre en collision avec un objet de la carte ou l'un des bords de celle-ci, dans la direction
	 * sp�cifi�e.
	 * 
	 * <p>Cette m�thode retourne l'objet avec lequel il y a collision, this s'il y a collision avec un bord de la carte,
	 * ou null s'il n'y a pas collision.</p>
	 * <p>Pour cela, cette m�thode parcourt tout d'abord tous les objets de la liste pass�e en param�tre et appelle la
	 * m�thode colliding(Zone, Orientation, int) de la zone de cet objet de type sp�cifi� en param�tre. C'est la zone
	 * de collision des objets de la carte qui est utilis�.</p>
	 * <p>Dans un deuxi�me temps, cette m�thode v�rifie la collision de cet objet avec une case de type interdit.</p>
	 * <p>Dans un troisi�me temps, la m�thode utilise la zone image de cet objet pour d�terminer si elle sort ou non de la
	 * carte. Si oui, la m�thode retourne vrai.</p>
	 * <p>Par souci de performance du programme, seuls les objets retourn�s par la m�thode cells de cette classe sont
	 * test�s pour les collisions.</p>
	 * 
	 * @param excludedObjects
	 * 		La liste des objets � exclure du test.
	 * @param zoneType
	 * 		Le type de zone de cet objet servant au test de collision.
	 * @param or
	 * 		L'orientation dans laquelle effectuer la v�rification.
	 * @return
	 * 		L'objet avec lequel il y a collision, this s'il y a collision avec un bord de la carte,
	 * ou null s'il n'y a pas collision.
	 */
	public MapObject colliding(ArrayList<MapObject> excludedObjects, ZoneType zoneType, Orientation or) {
		
		if (or == null || this.zone(ZoneType.IMAGE) == null || this.zone(zoneType) == null)
			return null;
		ArrayList<Case> cases = this.cells(true);
		ArrayList<MapObject> objets = new ArrayList<MapObject>();
		Zone z = this.zone(zoneType);
		for (Case cell : cases) {
			if (cell == null)
				continue;
			// Ajout des objets des cases � la liste des objets � tester
			for (MapObject obj : cell.occupants()) {
				if (obj != null && obj != this) {
					if (excludedObjects == null)
						objets.add(obj);
					else if (!excludedObjects.contains(obj))
						objets.add(obj);
				}
			}
		}
		for (MapObject obj : objets) {
			// V�rification des collisions avec d'autres objets
			if (z.colliding(obj.zone(ZoneType.COLLISION), or))
				return obj;
		}
		// V�rification des collisions avec un bord de la carte
		switch (or) {
		case NORD:
			if (this.zone(ZoneType.IMAGE).getY() <= 0)
				return this;
			break;
		case SUD:
			if (this.zone(ZoneType.IMAGE).getY()+this.zone(ZoneType.IMAGE).getObjRect().getH() >= this.carte.getNbCasesHeight()*Constantes.sizeCell)
				return this;
			break;
		case OUEST:
			if (this.zone(ZoneType.IMAGE).getX() <= 0)
				return this;
			break;
		case EST:
			if (this.zone(ZoneType.IMAGE).getX()+this.zone(ZoneType.IMAGE).getObjRect().getW() >= this.carte.getNbCasesWidth()*Constantes.sizeCell)
				return this;
			break;
		default:
		}
		
		return null;
		
	}
	
	/**
	 * D�place cet objet d'un pas selon l'orientation pass�e en param�tre.
	 * 
	 * <p>Les coordonn�es de cet objet sont modifi�es, puis ses zones sont mises � jour gr�ce � la m�thode updateZones()
	 * de cette classe. A chaque pas de 1 pixel, la collision de l'objet est test�e. S'il y a collision, le d�placement
	 * est arr�t�.</p>
	 * 
	 * @param or
	 * 		La direction du mouvement.
	 * @param step
	 * 		Le pas de d�placement.
	 * @return
	 * 		L'objet avec lequel cet objet est entr� en collision, null s'il n'y a pas collision.
	 */
	public MapObject move(Orientation or, int step) {
		
		MapObject o;
		for (int i=0;i<step;i++) {
			if ((o=this.colliding(null, ZoneType.COLLISION, or)) != null)
				return o;
			switch(or) {
			case NORD:
				this.setY(this.getY()-1);
				break;
			case SUD:
				this.setY(this.getY()+1);
				break;
			case OUEST:
				this.setX(this.getX()-1);
				break;
			case EST:
				this.setX(this.getX()+1);
				break;
			case SUD_OUEST:
				this.setX(this.getX()-1);
				this.setY(this.getY()+1);
				break;
			case SUD_EST:
				this.setX(this.getX()+1);
				this.setY(this.getY()+1);
				break;
			case NORD_OUEST:
				this.setX(this.getX()-1);
				this.setY(this.getY()-1);
				break;
			case NORD_EST:
				this.setX(this.getX()+1);
				this.setY(this.getY()-1);
				break;
			default:
			}
			this.updateZones();
			this.refreshCells();
		}
		
		return null;
		
	}
	
	/**
	 * D�place un objet en un point de coordonn�es pass�es en param�tre.
	 * 
	 * <p>Apr�s que les coordonn�es de cet objet ont �t� modifi�es, la m�thode updateZones est appel�e, puis le nom
	 * de l'objet est modifi� s'il contenait ses coordonn�es (d�s qu'une parenth�se est rep�r�e dans le nom, celui-ci
	 * est tronqu� et les nouvelles coordonn�es sont ajout�es).</p>
	 * 
	 * @param x
	 * 		L'abscisse de cet objet.
	 * @param y
	 * 		L'ordonn�e de cet objet.
	 */
	public void displace(int x, int y) {
		
		String s;
		String name = this.getName();
		this.setX(x);
		this.setY(y);
		this.updateZones();
		// Renomme cet objet s'il poss�de dans son nom ses coordonn�es
		for (int i=0;i<name.length();i++) {
			if (name.charAt(i) == '(') {
				s = name.substring(0, i);
				s = s.concat("(x:"+this.getX()+",y:"+this.getY()+")");
				this.setName(s);
				break;
			}
		}
		
	}
	
	/**
	 * Stoppe cet objet.
	 * 
	 * <p>Cela se traduit par l'appel � l'animation STILL, et � l'annulation des vitesses horizontales et verticales de
	 * cet objet, ainsi que de son acc�l�ration et de sa hauteur.</p>
	 */
	public void stop() {
		
		this.changeCurrentAnim(Status.STILL);
		this.setVitesse(0);
		this.setVitesseZ(0);
		this.setAcceleration(0);
		this.setHauteur(0);
		
	}
	
	/**
	 * Met � jour la position des zones de cet objet selon sa position.
	 */
	public void updateZones() {
		
		for (Zone z : this.zones.values()) {
			z.setX((int) (this.getX()+z.getObjRect().getX()));
			z.setY((int) (this.getY()+z.getObjRect().getY()));
		}
		
	}
	
	/**
	 * G�n�re une image caract�risant l'�tat de cet objet.
	 * 
	 * <p>Cette m�thode retourne une image textur�e. Son abscisse est celle de cet objet, et son ordonn�e est
	 * celle de cet objet moins la hauteur de cet objet multipli�e par le champ sizeHStep de la classe Constantes. Les
	 * dimensions de cette image sont celles du rectangle objRect de la zone d'image de cet objet. Enfin, le fragment
	 * de texture � appliquer � l'image est d�termin�e par la m�thode textRect(Orientation) de l'animation en cours
	 * de cet objet (m�thode currentAnim() de cette classe), et le nom du fichier texture est �galement obtenu gr�ce
	 * � l'animation en cours.</p>
	 * <p>Le plan de perspective de l'image prend la valeur de l'ordonn�e par rapport au jeu de cet objet.</p>
	 * 
	 * @return
	 * 		L'image g�n�r�e, ou null si la zone d'image de cet objet est null ou si l'animation courrante n'est pas
	 * valide.
	 */
	@Override
	public CombinedImage generateObjectImage() {
		
		if (this.zone(ZoneType.IMAGE) == null || this.currentAnim() == null)
			return null;
		CombinedImage image = new CombinedImage(new TexturedImage("Image "+this.getName(), this.getX(),
				this.getY()-this.getHauteur()*Constantes.sizeHStep, 
				this.zone(ZoneType.IMAGE).getObjRect().getW(), this.zone(ZoneType.IMAGE).getObjRect().getH(),
				this.currentAnim().getTextureID(), this.currentAnim().textRect(this.getOrientation())));
		if (this.isInvisible()) {
			image.setW(-1);
		}
		if ((this.currentAnim().getStatus() == Status.DEAD || this.currentAnim().getStatus() == Status.ACTIVATED)
				&& this instanceof Mine) {
			image.setPlan(0);
		} else if (this instanceof Projectile) {
			if (((Projectile) this).getLanceur() instanceof MapObject)
				image.setPlan((int) ((MapObject) ((Projectile) this).getLanceur()).getY()
						+((this.getOrientation() == Orientation.NORD)?-1:1));
		} else {
			image.setPlan((int) (this.getY()+this.getHauteur()*Constantes.sizeHStep));
		}
		
		return image;
		
	}
	
	/**
	 * Met � jour l'�tat du bool�en invisible de cet objet.
	 * 
	 * @param invisible
	 * 		Vrai si cet objet doit devenir invisible.
	 */
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	/**
	 * Indique si cet objet est invisible.
	 * 
	 * @return
	 * 		Vrai si cet objet est invisible.
	 */
	public boolean isInvisible() {
		return this.invisible;
	}
	
	/**
	 * Met � jour l'�tat du bool�en standby de cet objet.
	 * 
	 * @param standby
	 * 		Vrai si cet objet doit passer en mode standby.
	 */
	public void setStandby(boolean standby) {
		this.standby = standby;
	}
	
	/**
	 * Indique si cet objet est invisible.
	 * 
	 * @return
	 * 		Vrai si cet objet est invisible.
	 */
	public boolean isStandby() {
		return this.standby;
	}
	
	/**
	 * Retourne la zone de type pass� en param�tre, ou null si ce type n'est pas dans la table de cette classe.
	 * 
	 * @param z
	 * 		Le type de la zone � r�cup�rer.
	 * @return
	 * 		La zone du type pass� en param�tre.
	 */
	public Zone zone(ZoneType z) {
		
		return this.zones.get(z);
		
	}

	/**
	 * Modifie une zone de cet objet.
	 * 
	 * @param type
	 * 		Le type de zone � modifier. Si elle n'est pas disponible pour cet objet, rien n'est fait.
	 * @param objRect
	 * 		Le rectangle poss�dant les coordonn�es par rapport � cet objet de la zone. S'il est null, aucune modification
	 * n'est faite, m�me pour les pr�c�dents param�tres.
	 */
	public void changeZone(ZoneType type, Rectangle objRect) {
		
		Zone z = this.zone(type);
		if (objRect == null || z == null)
			return;
		z.setObjRect(objRect);
		z.setX(this.getX()+objRect.getX());
		z.setY(this.getY()+objRect.getY());
		
	}
	
	/**
	 * Ajoute une zone � la table des zones de cet objet.
	 * 
	 * @param type
	 * 		Le type de zone � ajouter.
	 * @param rect
	 * 		Le rectangle des coordonn�es � ajouter � celles de cet objet de la zone � ajouter.
	 * @param nameZone
	 * 		Le nom de la zone � ajouter.
	 */
	public void addZone(ZoneType type, Rectangle rect, String nameZone) {
		
		this.zones.put(type, new Zone(nameZone, this.getX(), this.getY(), rect, type.getColor()));
		this.zone(type).setX(this.getX()+rect.getX());
		this.zone(type).setY(this.getY()+rect.getY());
		
	}

	/**
	 * Met � jour la vitesse de cet objet.
	 * 
	 * @param vitesse
	 * 		La vitesse de cet objet.
	 */
	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
	}

	/**
	 * Retourne la vitesse de cet objet.
	 * 
	 * @return
	 * 		La vitesse de cet objet.
	 */
	public int getVitesse() {
		return this.vitesse;
	}
	
	/**
	 * Met � jour l'acc�l�ration de cet objet.
	 * 
	 * @param acceleration
	 * 		L'acc�l�ration de cet objet.
	 */
	public void setAcceleration(int acceleration) {
		this.acceleration = acceleration;
	}
	
	/**
	 * Retourne l'acc�l�ration de cet objet.
	 * 
	 * @return
	 * 		L'acc�l�ration de cet objet.
	 */
	public int getAcceleration() {
		return this.acceleration;
	}

	/**
	 * Met � jour la hauteur de cet objet.
	 * 
	 * @param hauteur
	 * 		La hauteur de cet objet.
	 */
	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}

	/**
	 * Retourne la hauteur de cet objet.
	 * 
	 * @return
	 * 		La hauteur de cet objet.
	 */
	public int getHauteur() {
		return this.hauteur;
	}

	/**
	 * Met � jour la vitesse de d�placement en hauteur de cet objet.
	 * 
	 * @param vitesseZ
	 * 		La vitesse de d�placement en hauteur de cet objet.
	 */
	public void setVitesseZ(int vitesseZ) {
		this.vitesseZ = vitesseZ;
	}

	/**
	 * Retourne la vitesse de d�placement en hauteur de cet objet.
	 * 
	 * @return
	 *   	La vitesse de d�placement en hauteur de cet objet.
	 */
	public int getVitesseZ() {
		return this.vitesseZ;
	}
	
	/**
	 * Met � jour la valeur du bool�en toUpdate de cette classe.
	 * 
	 * @param toUpdate
	 * 		Vrai si cet objet doit �tre mis � jour par le mod�le.
	 */
	public void setToUpdate(boolean toUpdate) {
		this.toUpdate = toUpdate;
	}
	
	/**
	 * Indique si cet objet doit �tre mis � jour par le mod�le.
	 * 
	 * @return
	 * 		Vrai si cet objet doit �tre mis � jour.
	 */
	public boolean isToUpdate() {
		return this.toUpdate;
	}
	
	/**
	 * Indique si cet objet doit voler.
	 * 
	 * @param flying
	 * 		Vrai si cet objet doit voler.
	 */
	public void setFlying(boolean flying) {
		this.flying = flying;
	}
	
	/**
	 * Indique si cet objet vole.
	 * 
	 * @return
	 * 		Vrai si cet objet vole.
	 */
	public boolean isFlying() {
		return this.flying;
	}

}
