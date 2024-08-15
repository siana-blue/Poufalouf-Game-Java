package fr.poufalouf.tools;

import org.newdawn.slick.Color;

/**
 * Zone d'action d'un objet
 * 
 * <p>Il s'agit d'un rectangle poss�dant une fonction de test de collisions avec d'autres zones, et impl�mentant
 * l'interface Drawable pour g�n�rer une image simple de la couleur et des dimensions de cette zone.</p>
 * 
 * @author Anaïs Vernet
 */
public class Zone implements Drawable, Cloneable {
	
	/**
	 * Le nom de cette zone.
	 */
	private final String name;
	/**
	 * L'abscisse par rapport au jeu de cette zone.
	 */
	private double x;
	/**
	 * L'ordonn�e par rapport au jeu de cette zone.
	 */
	private double y;
	/**
	 * Le rectangle de position et de taille par rapport � un objet de cette zone. Les coordonn�es sont absolues et �
	 * ajouter � celles d'un objet. Les dimensions sont absolues.
	 */
	private Rectangle objRect;
	/**
	 * La couleur de cette zone.
	 */
	private Color couleur;
	
	/**
	 * Constructeur Zone.
	 * 
	 * <p></p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>name : le nom pass� en param�tre (name), ou "" si le param�tre est null.</li>
	 * <li>x : l'abscisse pass�e en param�tre (x).</li>
	 * <li>y : l'ordonn�e pass�e en param�tre (y).</li>
	 * <li>objRect : le rectangle pass� en param�tre (rect), ou un nouveau Rectangle de coordonn�es (0, 0) et de
	 * dimensions (1, 1)*Constantes.sizeCell si le param�tre est null.</li>
	 * <li>couleur : la couleur pass�e en param�tre (couleur), ou Color.white si le param�tre est null.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de cette zone.
	 * @param x
	 * 		L'abscisse par rapport au jeu de cette zone.
	 * @param y
	 * 		L'ordonn�e par rapport au jeu de cette zone.
	 * @param rect
	 * 		Le rectangle d�finissant les coordonn�es par rapport � un objet de cette zone.
	 * @param couleur
	 * 		La couleur de cette zone.
	 */
	public Zone(String name, double x, double y, Rectangle rect, Color couleur) {
		
		if (name != null)
			this.name = name;
		else
			this.name = "";
		this.x = x;
		this.y = y;
		if (rect != null)
			this.objRect = rect;
		else
			this.objRect = new Rectangle(0, 0, 1, 1, Constantes.sizeCell);
		if (couleur != null)
			this.couleur = couleur;
		else
			this.couleur = Color.white;
		
	}
	
	/**
	 * Retourne vrai si cette zone est � l'int�rieur de la zone pass�e en param�tre.
	 * 
	 * <p>Cette m�thode fait appel � la m�thode isInside de la classe Rectangle.</p>
	 * 
	 * @param zone
	 * 		La zone � comparer � celle-ci.
	 * @param border
	 * 		Vrai si les bords de cette zone doivent �tre inclus dans l'int�rieur de la zone pass�e en param�tre.
	 * @param completely
	 * 		Vrai si cette zone doit �tre enti�rement � l'int�rieur de la zone pass�e en param�tre.
	 * @return
	 * 		Vrai si cette zone est � l'int�rieur de la zone pass�e en param�tre. Faux sinon ou si la zone pass�e en
	 * param�tre est null ou si cette zone ou la zone pass�e en param�tre a des dimensions n�gatives.
	 */
	public boolean isInside(Zone zone, boolean border, boolean completely) {
		
		if (zone == null)
			return false;
		if (this.getObjRect().getW() < 0 || this.getObjRect().getH() < 0 || zone.getObjRect().getW() < 0
				|| zone.getObjRect().getH() < 0)
			return false;
		Rectangle r1 = new Rectangle(this.getX(), this.getY(), this.getW(), this.getH());
		Rectangle r2 = new Rectangle(zone.getX(), zone.getY(), zone.getW(), zone.getH());
		
		if (completely)
			return r1.isCompletelyInside(r2, border);
		return r1.isInside(r2, border);
		
	}

	/**
	 * Indique si cette zone entre en collision avec la zone pass�e en param�tre dans la direction sp�cifi�e.
	 * 
	 * <p>Les orientations sp�cifi�es peuvent �tre NORD, SUD, EST ou OUEST.</p>
	 * <p>Si la zone pass�e en param�tre a une largeur ou une hauteur strictement n�gative, faux est retourn�.</p>
	 * <p>La d�tection de collision se fait selon le processus suivant, dans la direction sp�cifi�e. "Avant" d�signe le
	 * bord le plus loin dans la direction sp�cifi�e, "arri�re" le bord le plus proche. Vers le NORD par exemple, les y
	 * augmentant vers le sud, avant d�signe les y les plus faibles et arri�re les y les plus �lev�s. Gauche
	 * et droite d�signent ensuite les c�t�s relatifs � ce positionnement. Si la r�ponse � une seule de ces questions
	 * est non, alors cette m�thode renvoie faux.
	 * <ul>
	 * <li>Est-ce que le bord avant de cette zone d�passe le bord arri�re de la zone test�e ?</li>
	 * <li>Si oui, est-ce qu'il ne d�passe pas le bord avant de la zone test�e ?</li>
	 * <li>Si oui, est-ce que soit :</li>
	 * <li>Le bord gauche de cette zone est plus � gauche que le bord gauche de la zone test�e ET le bord droit de cette
	 * zone est plus � droite que le bord gauche de la zone test�e ?</li>
	 * <li>Le bord droit de cette zone est plus � droite que le bord droit de la zone test�e ET le bord gauche de cette
	 * zone est plus � gauche que le bord droit de la zone test�e ?</li>
	 * <li>Le bord gauche de cette zone est plus � droite que le bord gauche de la zone test�e ET le bord droit de cette
	 * zone est plus � gauche que le bord droit de la zone test�e ?</li>
	 * </ul></p>
	 * 
	 * <p>Si l'orientation pass�e en param�tre n'est pas une orientation cardinale, alors la m�thode isInside de cette
	 * classe est appel�e, avec les bords des zones pris en compte.</p>
	 * 
	 * @param zone
	 * 		La zone test�e.
	 * @param orientation
	 * 		La direction de test de collision.
	 * @return
	 * 		Vrai s'il y a collision.
	 */
	public boolean colliding(Zone zone, Orientation orientation) {

		if (zone == null || zone.getObjRect().getW() < 0 || zone.getObjRect().getH() < 0 || orientation == null
				|| this.getObjRect().getW() < 0 || this.getObjRect().getH() < 0)
			return false;
		double X, Y, w, h, zx, zy, zw, zh;
		X = this.getX();
		Y = this.getY();
		w = this.getObjRect().getW();
		h = this.getObjRect().getH();
		zx = zone.getX();
		zy = zone.getY();
		zw = zone.getObjRect().getW();
		zh = zone.getObjRect().getH();
		
		switch (orientation) {
		case NORD:
			if (Y <= zy+zh && Y >= zy && ((X < zx && X+w > zx) || (X+w > zx+zw && X < zx+zw) || (X >= zx && X+w <= zx+zw)))
				return true;
			break;
		case SUD:
			if (Y+h >= zy && Y+h <= zy+zh && ((X < zx && X+w > zx) || (X+w > zx+zw && X < zx+zw)
					|| (X >= zx && X+w <= zx+zw)))
				return true;
			break;
		case OUEST:
			if (X <= zx+zw && X >= zx && ((Y < zy && Y+h > zy) || (Y+h > zy+zh && Y < zy+zh) || (Y >= zy && Y+h <= zy+zh)))
				return true;
			break;
		case EST:
			if (X+w >= zx && X+w <= zx+zw && ((Y < zy && Y+h > zy) || (Y+h > zy+zh && Y < zy+zh)
					|| (Y >= zy && Y+h <= zy+zh)))
				return true;
			break;
		case NORD_EST:
			if (Y <= zy+zh && Y >= zy
					&& ((X < zx && X+w > zx)
							|| (X+w > zx+zw && X < zx+zw)
							|| (X >= zx && X+w <= zx+zw))
							||
							X+w >= zx && X+w <= zx+zw
							&& ((Y < zy && Y+h > zy)
									|| (Y+h > zy+zh && Y < zy+zh)
									|| (Y >= zy && Y+h <= zy+zh)))
				return true;
			break;
		case NORD_OUEST:
			if (Y <= zy+zh && Y >= zy
					&& ((X < zx && X+w > zx)
							|| (X+w > zx+zw && X < zx+zw)
							|| (X >= zx && X+w <= zx+zw))
							||
							X <= zx+zw && X >= zx
							&& ((Y < zy && Y+h > zy)
									|| (Y+h > zy+zh && Y < zy+zh)
									|| (Y >= zy && Y+h <= zy+zh)))
				return true;
			break;
		case SUD_OUEST:
			if (Y+h >= zy && Y+h <= zy+zh
					&& ((X < zx && X+w > zx)
							|| (X+w > zx+zw && X < zx+zw)
							|| (X >= zx && X+w <= zx+zw))
							||
							X <= zx+zw && X >= zx
							&& ((Y < zy && Y+h > zy)
									|| (Y+h > zy+zh && Y < zy+zh)
									|| (Y >= zy && Y+h <= zy+zh)))
				return true;
			break;
		case SUD_EST:
			if (Y+h >= zy && Y+h <= zy+zh
					&& ((X < zx && X+w > zx)
							|| (X+w > zx+zw && X < zx+zw)
							|| (X >= zx && X+w <= zx+zw))
							||
							X+w >= zx && X+w <= zx+zw
							&& ((Y < zy && Y+h > zy)
									|| (Y+h > zy+zh && Y < zy+zh)
									|| (Y >= zy && Y+h <= zy+zh)))
				return true;
			break;
		default:
			return this.isInside(zone, true, false);
		}
		
		return false;
		
	}
	
	/**
	 * G�n�re un rectangle affichable � l'�cran d�finissant cette zone.
	 * 
	 * <p>L'image g�n�r�e est une image simple de dimensions et de position �gales � celle du rectangle de cette zone.
	 * Cette image est plac�e au premier plan.</p>
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	@Override
	public Image generateImage() {
		
		Image img = new Image(this.getName(), this.getX(), this.getY(), this.getObjRect().getW(),
				this.getObjRect().getH(), new Color(this.couleur));
		img.setPlan(Constantes.maxPlan);
		
		return img;
		
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
	 * Clone cette zone.
	 * 
	 * @return
	 * 		Une copie de cette zone.
	 */
	@Override
	public Zone clone() {
		
		Zone zone = null;
		
		try {
			zone = (Zone) super.clone();
		} catch (CloneNotSupportedException e) {
			// Ne peut pas se produire en th�orie.
		}
		
		if (zone == null || this.couleur == null)
			return null;
		
		zone.couleur = new Color(this.couleur);
		
		return zone;
		
	}
	
	/**
	 * Retourne le nom de cette zone.
	 * 
	 * @return
	 * 		Le nom de cette zone.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Modifie l'abscisse de cette zone.
	 * 
	 * @param x
	 * 		L'abscisse de cette zone.
	 */
	@Override
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Retourne l'abscisse de cette zone.
	 * 
	 * @return
	 * 		L'abscisse de cette zone.
	 */
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * Modifie l'ordonn�e de cette zone.
	 * 
	 * @param y
	 * 		L'ordonn�e de cette zone.
	 */
	@Override
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Retourne l'ordonn�e de cette zone.
	 * 
	 * @return
	 * 		L'ordonn�e de cette zone.
	 */
	@Override
	public double getY() {
		return this.y;
	}

	/**
	 * Modifie la largeur de cette zone.
	 * 
	 * @param w
	 * 		La largeur de cette zone.
	 */
	@Override
	public void setW(double w) {
		this.objRect = new Rectangle(this.getObjRect().getX(), this.getObjRect().getY(), w, this.getObjRect().getH());
	}

	/**
	 * Retourne la largeur de cette zone.
	 * 
	 * @return
	 * 		La largeur de cette zone.
	 */
	@Override
	public double getW() {
		return this.getObjRect().getW();
	}

	/**
	 * Modifie la hauteur de cette zone.
	 * 
	 * @param h
	 * 		La hauteur de cette zone.
	 */
	@Override
	public void setH(double h) {
		this.objRect = new Rectangle(this.getObjRect().getX(), this.getObjRect().getY(), this.getObjRect().getW(), h);
	}

	/**
	 * Retourne la hauteur de cette zone.
	 * 
	 * @return
	 * 		La hauteur de cette zone.
	 */
	@Override
	public double getH() {
		return this.getObjRect().getH();
	}

	/**
	 * Met � jour le rectangle des coordonn�es par rapport � l'objet de cette zone.
	 * 
	 * @param objRect
	 * 		Le rectangle des coordonn�es par rapport � l'objet. S'il est null, la mise � jour ne se fait pas.
	 */
	public void setObjRect(Rectangle objRect) {
		if (objRect != null)
			this.objRect = objRect;
	}

	/**
	 * Retourne le rectangle des coordonn�es par rapport � l'objet de cette zone.
	 * 
	 * @return
	 * 		Le rectangle des coordonn�es par rapport � l'objet.
	 */
	public Rectangle getObjRect() {
		return this.objRect;
	}
	
}
