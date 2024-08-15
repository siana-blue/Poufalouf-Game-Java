package fr.poufalouf.tools;

/**
 * Forme caract�ris�e par les coordonn�es de son sommet sup�rieur gauche, sa largeur et sa hauteur
 * 
 * <p>Cette classe est utilis�e pour d�finir des zones d'affichage, de collision etc... C'est un moyen pratique de 
 * regrouper des donn�es concernant les dimensions d'un �l�ment.</p>
 * <p>Cette classe est immuable.</p>
 * 
 * @author Anaïs Vernet
 */
public final class Rectangle {

	/**
	 * L'abscisse de ce rectangle.
	 */
	private final double x;
	/**
	 * L'ordonn�e de ce rectangle.
	 */
	private final double y;
	/**
	 * La largeur de ce rectangle.
	 */
	private final double w;
	/**
	 * La hauteur de ce rectangle.
	 */
	private final double h;
	
	/**
	 * Constructeur Rectangle 1.
	 * 
	 * <p>Cr�e un carr� de coordonn�es (0, 0) de c�t� pass� en param�tre.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>x : 0.</li>
	 * <li>y : 0.</li>
	 * <li>w : le c�t� pass� en param�tre (size).</li>
	 * <li>h : le c�t� pass� en param�tre (size).</li>
	 * </ul></p>
	 * 
	 * @param size
	 * 		Le c�t� de ce carr� en pixels.
	 */
	public Rectangle(double size) {
		
		this.x = 0;
		this.y = 0;
		this.w = size;
		this.h = size;
	
	}
	
	/**
	 * Constructeur Rectangle 2.
	 * 
	 * <p>Cr�e un rectangle de coordonn�es (0, 0), de dimensions pass�es en param�tres.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>x : 0.</li>
	 * <li>y : 0.</li>
	 * <li>w : la largeur pass�e en param�tre (w).</li>
	 * <li>h : la hauteur pass�e en param�tre (h).</li>
	 * </ul></p>
	 * 
	 * @param w
	 * 		La largeur de ce rectangle.
	 * @param h
	 * 		La hauteur de ce rectangle.
	 */
	public Rectangle(double w, double h) {
		
		this.x = 0;
		this.y = 0;
		this.w = w;
		this.h = h;
		
	}
	
	/**
	 * Constructeur Rectangle 3.
	 * 
	 * <p>Cr�e un rectangle de coordonn�es pass�es en param�tres, de dimensions pass�es en param�tres.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>x : l'abscisse pass�e en param�tre (x).</li>
	 * <li>y : l'ordonn�e pass�e en param�tre (y).</li>
	 * <li>w : la largeur pass�e en param�tre (w).</li>
	 * <li>h : la hauteur pass�e en param�tre (h).</li>
	 * </ul></p>
	 * 
	 * @param x
	 * 		L'abscisse de ce rectangle.
	 * @param y
	 * 		L'ordonn�e de ce rectangle.
	 * @param w
	 * 		La largeur de ce rectangle.
	 * @param h
	 * 		La hauteur de ce rectangle.
	 */
	public Rectangle(double x, double y, double w, double h) {
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
	}
	
	/**
	 * Constructeur Rectangle 4.
	 * 
	 * <p>Ce constructeur appelle le constructeur 3 en multipliant ses param�tres par l'�chelle pass�e en cinqui�me
	 * param�tre.</p>
	 * <p>Ce constructeur est appel� pour sp�cifier des zones relatives � un objet. Un exemple d'utilisation
	 * pourrait �tre :</p>
	 * <code>Rectangle rect = new Rectangle(0, 0.5, 1, 0.5, tailleCase);</code>
	 * <p>ce qui donnerait un rectangle occupant la moiti� inf�rieure d'une hypoth�tique "case".</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>x : l'abscisse pass�e en param�tre (x) multipli�e par le coefficient pass� en param�tre (echelle).</li>
	 * <li>y : l'ordonn�e pass�e en param�tre (y) multipli�e par le coefficient pass� en param�tre (echelle).</li>
	 * <li>w : la largeur pass�e en param�tre (w) multipli�e par le coefficient pass� en param�tre (echelle).</li>
	 * <li>h : la hauteur pass�e en param�tre (h) multipli�e par le coefficient pass� en param�tre (echelle).</li>
	 * </ul></p>
	 * 
	 * @param x
	 * 		L'abscisse de ce rectangle.
	 * @param y
	 * 		L'ordonn�e de ce rectangle.
	 * @param w
	 * 		La largeur de ce rectangle.
	 * @param h
	 * 		La hauteur de ce rectangle.
	 * @param echelle
	 * 		Le coefficient multiplicateur � appliquer aux pr�c�dents param�tres.
	 */
	public Rectangle(double x, double y, double w, double h, double echelle) {
		
		this(x*echelle, y*echelle, w*echelle, h*echelle);
		
	}
	
	/**
	 * Retourne vrai si ce rectangle est � l'int�rieur du rectangle pass� en param�tre.
	 * 
	 * @param rect
	 * 		Le rectangle � comparer � celui-ci.
	 * @param border
	 * 		Vrai si les bords des rectangles comptent comme leurs int�rieurs.
	 * @return
	 * 		Vrai si ce rectangle est � l'int�rieur du rectangle pass� en param�tre.
	 */
	public boolean isInside(Rectangle rect, boolean border) {
		
		if (rect == null)
			return false;
		if (this.getW() < 0 || this.getH() < 0 || rect.getW() < 0 || rect.getH() < 0)
			return false;
		
		if (border)
			return this.getX() <= rect.getX()+rect.getW()
				&& this.getW() >= rect.getX()-this.getX()
				&& this.getY() <= rect.getY()+rect.getH()
				&& this.getH() >= rect.getY()-this.getY();
		return this.getX() < rect.getX()+rect.getW()
			&& this.getW() > rect.getX()-this.getX()
			&& this.getY() < rect.getY()+rect.getH()
			&& this.getH() > rect.getY()-this.getY();
		
	}
	
	/**
	 * Retourne vrai si ce rectangle est enti�rement � l'int�rieur du rectangle pass� en param�tre.
	 * 
	 * @param rect
	 * 		Le rectangle � comparer � celui-ci.
	 * @param border
	 * 		Vrai si les bords des rectangles comptent comme leurs int�rieurs.
	 * @return
	 * 		Vrai si ce rectangle est enti�rement � l'int�rieur du rectangle pass� en param�tre.
	 */
	public boolean isCompletelyInside(Rectangle rect, boolean border) {
		
		if (rect == null)
			return false;
		if (this.getW() < 0 || this.getH() < 0 || rect.getW() < 0 || rect.getH() < 0)
			return false;
		
		if (border)
			return this.getX() <= rect.getX()+rect.getW() && this.getX() >= rect.getX()
				&& this.getW() <= rect.getX()+rect.getW()-this.getX()
				&& this.getY() <= rect.getY()+rect.getH() && this.getY() >= rect.getY()
				&& this.getH() <= rect.getY()+rect.getH()-this.getY();
		return this.getX() < rect.getX()+rect.getW() && this.getX() > rect.getX()
		&& this.getW() < rect.getX()+rect.getW()-this.getX()
		&& this.getY() < rect.getY()+rect.getH() && this.getY() > rect.getY()
		&& this.getH() < rect.getY()+rect.getH()-this.getY();
		
	}

	/**
	 * Retourne l'abscisse de ce rectangle.
	 * 
	 * @return
	 * 		L'abscisse de ce rectangle.
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Retourne l'ordonn�e de ce rectangle.
	 * 
	 * @return
	 * 		L'ordonn�e de ce rectangle.
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * Retourne la largeur de ce rectangle.
	 * 
	 * @return
	 * 		La largeur de ce rectangle.
	 */
	public double getW() {
		return this.w;
	}

	/**
	 * Retourne la hauteur de ce rectangle.
	 * 
	 * @return
	 * 		La hauteur de ce rectangle.
	 */
	public double getH() {
		return this.h;
	}
	
}
