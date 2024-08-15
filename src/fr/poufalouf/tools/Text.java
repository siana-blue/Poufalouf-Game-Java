package fr.poufalouf.tools;

import org.newdawn.slick.Color;

/**
 * Objet affichable � l'�cran et repr�sentant un texte
 * 
 * <p>L'image g�n�r�e par cet objet est une image combin�e utilisant le fichier texture sp�cifi� en tant que police pour
 * ce texte. Ce fichier doit contenir les lettres n�cessaires dans l'ordre ASCII. Le texte est affich� sur une ligne.</li>
 * 
 * @author Anaïs Vernet
 */
public class Text implements GUIElement {
	
	/**
	 * Le texte � afficher.
	 */
	private String text;
	/**
	 * La couleur de ce texte.
	 */
	private Color color;
	/**
	 * La largeur des caract�res de ce texte.
	 */
	private double sizeW;
	/**
	 * La hauteur des caract�res de ce texte.
	 */
	private double sizeH;
	/**
	 * Le nom du fichier image servant de police pour ce texte.
	 */
	private String font;
	/**
	 * L'abscisse de ce texte.
	 */
	private double x;

	/**
	 * L'ordonn�e de ce texte.
	 */
	private double y;
	
	/**
	 * Constructeur Text.
	 * 
	 * <p></p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>text : le texte pass� en param�tre (texte), ou "" si le param�tre est null.</li>
	 * <li>color : la couleur pass�e en param�tre (color), ou Color.white si le param�tre est null.</li>
	 * <li>sizeW : la largeur pass�e en param�tre (sizeW).</li>
	 * <li>sizeH : la hauteur pass�e en param�tre (sizeH).</li>
	 * <li>font : le nom pass� en param�tre (font), ou "" si le param�tre est null.</li>
	 * <li>x : l'abscisse pass�e en param�tre (x).</li>
	 * <li>y : l'ordonn�e pass�e en param�tre (y).</li>
	 * </ul></p>
	 * 
	 * @param text
	 * 		Le texte � afficher.
	 * @param color
	 * 		La couleur de ce texte.
	 * @param sizeW
	 * 		La largeur des caract�res de ce texte.
	 * @param sizeH
	 * 		La hauteur des caract�res de ce texte.
	 * @param font
	 * 		Le nom du fichier image servant de police.
	 * @param x
	 * 		L'abscisse de ce texte.
	 * @param y
	 * 		L'ordonn�e de ce texte.
	 */
	public Text(String text, Color color, double sizeW, double sizeH, String font, double x, double y) {
		
		if (text != null)
			this.text = text;
		else
			this.text = "";
		if (color != null)
			this.color = new Color(color);
		else
			this.color = Color.white;
		this.sizeW = sizeW;
		this.sizeH = sizeH;
		if (font != null)
			this.font = font;
		else
			this.font = "";
		this.x = x;
		this.y = y;
		
	}

	/**
	 * Retourne une image repr�sentant ce texte.
	 * 
	 * <p>L'image principale est invisible et sert de support aux lettres. Chaque lettre est repr�sent�e par une image
	 * textur�e dont les coordonn�es sont calcul�es gr�ce � sa position dans le texte, et dont les coordonn�es du
	 * fragment de texture d�pend du code ASCII de la lettre.</p>
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	@Override
	public CombinedImage generateImage() {

		CombinedImage image = new CombinedImage(new TexturedImage("(TEXT) "+this.getText()+" [support]",
				this.getX(), this.getY(), this.getText().length()*this.getSizeW(), this.getSizeH(), null));
		int s = this.getText().length();
		TexturedImage img;
		for (int i=0;i<s;i++) {
			char c = this.getText().charAt(i);
			img = new TexturedImage("(TEXT) "+this.getText()+" ["+c+"/"+i+"]",
					(double) i/s, 0, (double) 1/s, 1, this.getFont(),
					new Rectangle((double) (c%16)/16, (double) (c/16)/16, 1./16, 1./16));
			img.setCouleur(this.getColor());
			image.addImage(img);
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
	 * Met � jour ce texte.
	 * 
	 * <p>Ne fait rien.</p>
	 */
	@Override
	public void update() {
		
		// ne rien faire
		
	}
	
	/**
	 * Indique si ce type d'�l�ment peut �tre s�lectionn�.
	 * 
	 * @return
	 * 		Faux.
	 */
	@Override
	public boolean isSelectable() {
		
		return false;
		
	}
	
	/**
	 * Ne fait rien.
	 */
	@Override
	public void enable() {
		
		// ne rien faire
		
	}
	
	/**
	 * Ne fait rien.
	 */
	@Override
	public void disable() {
		
		// ne rien faire
		
	}
	
	/**
	 * Retourne faux.
	 * 
	 * @return
	 * 		Faux.
	 */
	@Override
	public boolean isEnabled() {
		
		return false;
		
	}
	
	/**
	 * Ne fait rien.
	 */
	@Override
	public void highlight() {
		
		// ne rien faire
		
	}
	
	/**
	 * Ne fait rien.
	 */
	@Override
	public void leave() {
		
		// ne rien faire
		
	}
	
	/**
	 * Retourne faux.
	 * 
	 * @return
	 * 		Faux.
	 */
	@Override
	public boolean isHighlighted() {
		
		return false;
		
	}
	
	/**
	 * Ne fait rien.
	 */
	@Override
	public void activate() {
		// ne rien faire
	}

	/**
	 * Ne fait rien.
	 */
	@Override
	public void desactivate() {
		// ne rien faire
	}

	/**
	 * Retourne faux.
	 * 
	 * @return
	 * 		Faux.
	 */
	@Override
	public boolean isActivated() {
		return false;
	}

	/**
	 * Retourne faux.
	 * 
	 * @return
	 * 		Faux.
	 */
	@Override
	public boolean isInTransition() {
		return false;
	}
	
	/**
	 * Met � jour le texte.
	 * 
	 * @param text
	 * 		Le texte � afficher.
	 */
	public void setText(String text) {
		if (text != null)
			this.text = text;
	}
	
	/**
	 * Le texte affich� par cet objet.
	 * 
	 * @return
	 * 		Le texte.
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * Met � jour la couleur de ce texte.
	 * 
	 * @param color
	 * 		La couleur de ce texte.
	 */
	public void setColor(Color color) {
		if (color != null)
			this.color = new Color(color);
	}
	
	/**
	 * Retourne une copie de la couleur de ce texte.
	 * 
	 * @return
	 * 		La couleur de ce texte.
	 */
	public Color getColor() {
		return new Color(this.color);
	}
	
	/**
	 * Met � jour la largeur des caract�res de ce texte.
	 * 
	 * @param sizeW
	 * 		La largeur des caract�res de ce texte.
	 */
	public void setSizeW(double sizeW) {
		this.sizeW = sizeW;
	}
	
	/**
	 * Retourne la largeur des caract�res de ce texte.
	 * 
	 * @return
	 * 		La largeur des caract�res de ce texte.
	 */
	public double getSizeW() {
		return this.sizeW;
	}
	
	/**
	 * Met � jour la hauteur des caract�res de ce texte.
	 * 
	 * @param sizeH
	 * 		La hauteur des caract�res de ce texte.
	 */
	public void setSizeH(double sizeH) {
		this.sizeH = sizeH;
	}
	
	/**
	 * Retourne la hauteur des caract�res de ce texte.
	 * 
	 * @return
	 * 		La hauteur des caract�res de ce texte.
	 */
	public double getSizeH() {
		return this.sizeH;
	}
	
	/**
	 * Met � jour le nom du fichier image servant de police.
	 *
	 * @param font
	 * 		Le nom du fichier image.
	 */
	public void setFont(String font) {
		if (font != null)
			this.font = font;
	}
	
	/**
	 * Retourne le nom du fichier image servant de police.
	 * 
	 * @return
	 * 		Le nom du fichier image.
	 */
	public String getFont() {
		return this.font;
	}
	
	/**
	 * Modifie l'abscisse de ce texte.
	 * 
	 * @param x
	 * 		L'abscisse de ce texte.
	 */
	@Override
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Retourne l'abscisse de ce texte.
	 * 
	 * @return
	 * 		L'abscisse de ce texte.
	 */
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * Modifie l'ordonn�e de ce texte.
	 * 
	 * @param y
	 * 		L'ordonn�e de ce texte.
	 */
	@Override
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Retourne l'ordonn�e de ce texte.
	 * 
	 * @return
	 * 		L'ordonn�e de ce texte.
	 */
	@Override
	public double getY() {
		return this.y;
	}

	/**
	 * Modifie la largeur des lettres de ce texte.
	 * 
	 * @param w
	 * 		La largeur des lettres de ce texte.
	 */
	@Override
	public void setW(double w) {
		this.sizeW = w;
	}

	/**
	 * Retourne la largeur des lettres de ce texte.
	 * 
	 * @return
	 * 		La largeur des lettres de ce texte.
	 */
	@Override
	public double getW() {
		return this.sizeW;
	}

	/**
	 * Modifie la hauteur des lettres de ce texte.
	 * 
	 * @param h
	 * 		La hauteur des lettres de ce texte.
	 */
	@Override
	public void setH(double h) {
		this.sizeH = h;
	}

	/**
	 * Retourne la hauteur des lettres de ce texte.
	 * 
	 * @return
	 * 		La hauteur des lettres de ce texte.
	 */
	@Override
	public double getH() {
		return this.sizeH;
	}

}
