package fr.poufalouf.map;

import java.util.ArrayList;
import java.util.HashMap;

import fr.poufalouf.tools.CombinedImage;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Drawable;
import fr.poufalouf.tools.Orientation;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.TexturedImage;
import fr.poufalouf.tools.Zone;
import fr.poufalouf.tools.ZoneType;

/**
 * El�ment de d�cor repr�sentant le terrain d'une carte
 * 
 * <p>Cette classe impl�mente l'interface Drawable et retourne une image combin�e contenant la superposition des textures
 * de cette case.</p>
 * 
 * @author Anaïs Vernet
 */
public class Case implements Drawable {
	
	/**
	 * Le nom du fichier texture � charger pour afficher cette case.
	 */
	private final String textureID;
	/**
	 * La carte � laquelle appartient cette case.
	 */
	private Map carte;
	/**
	 * L'abscisse de cette case par rapport au jeu.
	 */
	private double x;
	/**
	 * L'ordonn�e de cette case par rapport au jeu.
	 */
	private double y;
	/**
	 * La largeur de cette case.
	 */
	private double w;
	/**
	 * La hauteur de cette case.
	 */
	private double h;
	/**
	 * Le type de cette case.
	 */
	private CaseType type;
	/**
	 * La liste des num�ros de textures de la case.
	 * 
	 * <p>Une case peut avoir plusieurs textures superpos�es. Le premier �l�ment de la liste repr�sente l'image
	 * dessin�e en premier sur cette case.</p>
	 * <p>Les num�ros sont attribu�s par ordre croissant d'abord sur une ligne de gauche � droite puis de haut
	 * en bas dans le fichier texture utilis� par cette case. Le num�ro 0 indique la premi�re case de texture en haut
	 * � gauche sur le fichier texture.</p>
	 * <p>L'image g�n�r�e par ce Drawable sera une superposition de textures d�termin�es par ces num�ros.</p>
	 */
	private ArrayList<Integer> textureNumbers;
	/**
	 * La liste des objets pr�sents sur cette case.
	 */
	private ArrayList<MapObject> occupants;
	/**
	 * La table des zones de cette case.
	 */
	private HashMap<ZoneType, Zone> zones;
	
	/**
	 * Constructeur Case.
	 * 
	 * <p>Apr�s l'initialisation, le num�ro de texture pass� en dernier param�tre est ajout� � la liste
	 * des num�ros de texture de cette case.</p>
	 * <p>Une zone d'effet est ajout� � cette case, de la taille de la case.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>textureID : l'identifiant pass� en param�tre (textureID), ou "" si le param�tre est null.</li>
	 * <li>carte : la carte pass�e en param�tre (carte), ou une nouvelle Map standard si le param�tre est null.</li>
	 * <li>x : l'abscisse pass�e en param�tre (x).</li>
	 * <li>y : l'ordonn�e pass�e en param�tre (y).</li>
	 * <li>w : la largeur pass�e en param�tre (w).</li>
	 * <li>h : la hauteur pass�e en param�tre (h).</li>
	 * <li>type : SOLID.</li>
	 * <li>textureNumbers : une nouvelle liste (ArrayList) d'Integer.</li>
	 * <li>occupants : une nouvelle liste (ArrayList) de MapObject.</li>
	 * <li>zones : une nouvelle table (HashMap) de Zone rep�r�es par des ZoneType.</li>
	 * </ul></p>
	 * 
	 * @param carte
	 * 		La carte � laquelle appartient cette case.
	 * @param x
	 * 		L'abscisse de cette case par rapport au jeu.
	 * @param y
	 * 		L'ordonn�e de cette case par rapport au jeu.
	 * @param w
	 * 		La largeur de cette case.
	 * @param h
	 * 		La hauteur de cette case.
	 * @param textureID
	 * 		L'identifiant de texture de cette case.
	 * @param textureNumber
	 * 		Le num�ro de texture de la premi�re image de cette case.
	 */
	public Case(Map carte, int x, int y, int w, int h, String textureID, int textureNumber) {
		
		if (carte != null)
			this.carte = carte;
		else
			this.carte = new Map(0, 0, null);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.type = CaseType.SOLID;
		if (textureID != null)
			this.textureID = textureID;
		else
			this.textureID = "";
		this.type = CaseType.SOLID;
		this.textureNumbers = new ArrayList<Integer>();
		this.occupants = new ArrayList<MapObject>();
		this.zones = new HashMap<ZoneType, Zone>();
		
		this.textureNumbers.add(new Integer(textureNumber));
		this.zones.put(ZoneType.EFFECT, new Zone("Zone d'effet de la case ("+this.x+", "+this.y+")",
				this.x, this.y, new Rectangle(this.w, this.h), ZoneType.EFFECT.getColor()));
		
	}
	
	/**
	 * Retourne la case voisine dans la direction pass�e en param�tre.
	 * 
	 * <p>Cette m�thode peut utiliser les orientations NORD, SUD, EST, OUEST ainsi que les compos�es de ces directions.</p>
	 * 
	 * @param orientation
	 * 		La direction dans laquelle faire la recherche.
	 * @return
	 * 		La case voisine si elle existe, null sinon.
	 */
	public Case nextCell(Orientation orientation) {
		
		if (orientation == null)
			return null;
		
		double line, col;
		
		switch (orientation) {
		case NORD:
			line = this.getY()/Constantes.sizeCell-1;
			col = this.getX()/Constantes.sizeCell;
			break;
		case SUD:
			line = this.getY()/Constantes.sizeCell+1;
			col = this.getX()/Constantes.sizeCell;
			break;
		case EST:
			line = this.getY()/Constantes.sizeCell;
			col = this.getX()/Constantes.sizeCell+1;
			break;
		case OUEST:
			line = this.getY()/Constantes.sizeCell;
			col = this.getX()/Constantes.sizeCell-1;
			break;
		case NORD_EST:
			line = this.getY()/Constantes.sizeCell-1;
			col = this.getX()/Constantes.sizeCell+1;
			break;
		case NORD_OUEST:
			line = this.getY()/Constantes.sizeCell-1;
			col = this.getX()/Constantes.sizeCell-1;
			break;
		case SUD_EST:
			line = this.getY()/Constantes.sizeCell+1;
			col = this.getX()/Constantes.sizeCell+1;
			break;
		case SUD_OUEST:
			line = this.getY()/Constantes.sizeCell+1;
			col = this.getX()/Constantes.sizeCell-1;
			break;
		default:
			return null;
		}
		
		return this.carte.cell((int) col, (int) line);
		
	}
	
	/**
	 * G�n�re une image caract�risant l'�tat de cette case.
	 * 
	 * <p>Ici, l'image est une image combin�e dont la position est d�termin�e par les coordonn�es de cette case.
	 * La taille de l'image en hauteur et en largeur est �gale au champ sizeCell de la classe Constantes.
	 * L'identifiant de la texture de l'image est celui de cette case. Enfin, les m�thodes textX(int) et textY(int) de
	 * cette classe sp�cifient les coordonn�es du fragment de texture de chaque image de la liste de la CombinedImage, 
	 * et les dimensions de ce fragment sont �gales au rapport des champs sizeTextureTile sur 
	 * sizeTerrain de la classe Constantes.</p>
	 * <p>Le plan de perspective de cette image est �gal � -1.</p>
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	@Override
	public CombinedImage generateImage() {
		
		TexturedImage img = new TexturedImage("Case("+this.getX()+","+this.getY()+")", this.getX(),
				this.getY(), Constantes.sizeCell, Constantes.sizeCell, this.getTextureID(), 
				new Rectangle(this.textX(0), this.textY(0),
						(double) Constantes.sizeTextureTile/Constantes.sizeTerrain,
						(double) Constantes.sizeTextureTile/Constantes.sizeTerrain));
		
		CombinedImage image = new CombinedImage(img);
		image.setPlan(-1);
		
		for (int i=1;i<this.textureNumbers.size();i++) {
			image.addImage(new TexturedImage(img.getName()+"nv"+i, 0, 0, 1, 1, this.getTextureID(), 
				new Rectangle(this.textX(i), this.textY(i),
						(double) Constantes.sizeTextureTile/Constantes.sizeTerrain,
						(double) Constantes.sizeTextureTile/Constantes.sizeTerrain)));
		}
		
		return image;
		
	}
	
	/**
	 * Retourne faux.
	 * 
	 * @return
	 * 		Faux.
	 */
	@Override
	public boolean isVBORendered() {
		
		return false;
		
	}
	
	/**
	 * Modifie l'abscisse de cette case.
	 * 
	 * @param x
	 * 		L'abscisse de cette case.
	 */
	@Override
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Retourne l'abscisse de cette case.
	 * 
	 * @return
	 * 		L'abscisse de cette case.
	 */
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * Modifie l'ordonn�e de cette case.
	 * 
	 * @param y
	 * 		L'ordonn�e de cette case.
	 */
	@Override
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Retourne l'ordonn�e de cette case.
	 * 
	 * @return
	 * 		L'ordonn�e de cette case.
	 */
	@Override
	public double getY() {
		return this.y;
	}

	/**
	 * Modifie la largeur de cette case.
	 * 
	 * @param w
	 * 		La largeur de cette case.
	 */
	@Override
	public void setW(double w) {
		this.w = w;
	}

	/**
	 * Retourne la largeur de cette case.
	 * 
	 * @return
	 * 		La largeur de cette case.
	 */
	@Override
	public double getW() {
		return this.w;
	}

	/**
	 * Modifie la hauteur de cette case.
	 * 
	 * @param h
	 * 		La hauteur de cette case.
	 */
	@Override
	public void setH(double h) {
		this.h = h;
	}

	/**
	 * Retourne la hauteur de cette case.
	 * 
	 * @return
	 * 		La hauteur de cette case.
	 */
	@Override
	public double getH() {
		return this.h;
	}
	
	/**
	 * Modifie le type de cette case.
	 * 
	 * @param type
	 * 		Le type de cette case. S'il est null, rien n'est fait.
	 */
	public void setType(CaseType type) {
		if (type != null)
			this.type = type;
	}
	
	/**
	 * Retourne le type de cette case.
	 * 
	 * @return
	 * 		Le type de cette case.
	 */
	public CaseType getType() {
		return this.type;
	}
	
	/**
	 * Retourne l'abscisse du fragment de texture � utiliser pour cette case, relative � l'image enti�re du terrain.
	 * 
	 * <p>Cette abscisse est calcul�e gr�ce au num�ro de texture situ� � l'indice pass� en param�tre dans la liste
	 * des num�ros de texture de cette case.</p>
	 * 
	 * @param index
	 * 		L'indice du num�ro de texture.
	 * @return
	 * 		L'abscisse du fragment de texture.
	 */
	public double textX(int index) {
		
		if (index < 0 || index >= this.textureNumbers.size())
			return 0;
		return ((this.textureNumber(index)%(Constantes.sizeTerrain/Constantes.sizeTextureTile)))*((double) Constantes.sizeTextureTile/Constantes.sizeTerrain);
		
	}
	/**
	 * Retourne l'ordonn�e du fragment de texture � utiliser pour cette case, relative � l'image enti�re du terrain.
	 * 
	 * <p>Cette ordonn�e est calcul�e gr�ce au num�ro de texture situ� � l'indice pass� en param�tre dans la liste
	 * des num�ros de texture de cette case.</p>
	 * 
	 * @param index
	 * 		L'indice du num�ro de texture.
	 * @return
	 * 		L'ordonn�e du fragment de texture.
	 */
	public double textY(int index) {
		
		if (index < 0 || index >= this.textureNumbers.size())
			return 0;
		return (this.textureNumber(index)/(Constantes.sizeTerrain/Constantes.sizeTextureTile))*((double) Constantes.sizeTextureTile/Constantes.sizeTerrain);
		
	}

	/**
	 * Remplace le num�ro de texture � l'indice sp�cifi� par le num�ro pass� en param�tre.
	 * 
	 * @param index
	 * 		L'indice du num�ro de texture � modifier.
	 * @param textureNumber
	 * 		Le nouveau num�ro de texture.
	 */
	public void replaceTextureNumber(int index, int textureNumber) {
		
		if (index < 0 || index >= this.textureNumbers.size())
			return;
		this.textureNumbers.set(index, new Integer(textureNumber));
		
	}
	
	/**
	 * Ajoute un num�ro de texture � cette case.
	 * 
	 * <p>Cela signifie qu'une nouvelle image est ajout�e � la pile de textures de cette case.</p>
	 * 
	 * @param textureNumber
	 * 		Le num�ro de texture � ajouter.
	 */
	public void addTextureNumber(int textureNumber) {
		
		this.textureNumbers.add(new Integer(textureNumber));
		
	}

	/**
	 * Retourne le num�ro de texture � l'indice sp�cifi�.
	 * 
	 * @param index
	 * 		L'indice du num�ro de texture.
	 * @return
	 * 		Le num�ro de texture, ou -1 si le tableau textureNumbers retourne null � l'indice sp�cifi�.
	 */
	public int textureNumber(int index) {
		
		if (index < 0 || index >= this.textureNumbers.size())
			return -1;
		Integer n = this.textureNumbers.get(index);
		if (n != null)
			return n.intValue();
		return -1;
		
	}
	
	/**
	 * Efface la liste des textures de cette case et ajoute 0 comme premier �l�ment.
	 */
	public void clearTextures() {
		
		this.textureNumbers.clear();
		this.textureNumbers.add(new Integer(0));
		
	}

	/**
	 * Retourne l'identifiant du fichier texture de la case.
	 * 
	 * @return
	 * 		L'identifiant de texture.
	 */
	public String getTextureID() {
		return this.textureID;
	}

	/**
	 * R�initialise la liste des objets occupants cette case.
	 */
	public void clearOccupants() {
		
		this.occupants = new ArrayList<MapObject>();
		
	}

	/**
	 * Retourne une copie de la liste des objets occupant cette case (jamais null).
	 * 
	 * <p>La liste retourn�e est une copie mais les instances de MapObject sont les originales.</p>
	 * 
	 * @return
	 * 		Une copie de la liste des objets.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<MapObject> occupants() {
		
		return (ArrayList<MapObject>) this.occupants.clone();
		
	}
	
	/**
	 * Ajoute un objet � la liste des occupants de cette case.
	 * 
	 * @param obj
	 * 		L'objet � ajouter � la liste. S'il est null, il n'est pas ajout�.
	 */
	public void addOccupant(MapObject obj) {
		
		if (obj != null)
			this.occupants.add(obj);
		
	}
	
	/**
	 * Retire de la liste des occupants de cette case l'objet pass� en param�tre.
	 * 
	 * @param obj
	 * 		L'objet � retirer.
	 */
	public void removeOccupant(MapObject obj) {
		
		this.occupants.remove(obj);
		
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
	 * Ajoute une zone � la table des zones de cette case.
	 * 
	 * @param zoneType
	 * 		Le type de zone � ajouter.
	 * @param rect
	 * 		Le rectangle des coordonn�es � ajouter � celles de cette case de la zone � ajouter.
	 * @param nameZone
	 * 		Le nom de la zone � ajouter.
	 */
	public void addZone(ZoneType zoneType, Rectangle rect, String nameZone) {
		
		this.zones.put(zoneType, new Zone(nameZone, this.getX(), this.getY(), rect, zoneType.getColor()));
		this.zone(zoneType).setX(this.getX()+rect.getX());
		this.zone(zoneType).setY(this.getY()+rect.getY());
		
	}
	
}
