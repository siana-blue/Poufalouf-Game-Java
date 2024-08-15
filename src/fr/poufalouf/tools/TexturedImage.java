package fr.poufalouf.tools;

import org.newdawn.slick.Color;

/**
 * Image textur�e
 * 
 * <p>Cette image poss�de un identifiant de texture et un rectangle d�terminant les dimensions du fragment de texture
 * � utiliser pour afficher cette image.</p>
 * 
 * @author Anaïs Vernet
 */
public class TexturedImage extends Image {
	
	/**
	 * Le nom du fichier texture de cette image.
	 */
	private String textureID;
	/**
	 * Le rectangle d�terminant les coordonn�es et les dimensions du fragment de texture � utiliser pour cette image.
	 */
	private Rectangle textRect;
	
	/**
	 * Constructeur TexturedImage 1.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il associe le nom du fichier texture � cette image gr�ce � l'identifiant textureID pass� en param�tre.
	 * Les dimensions de la texture sont initialis�es � (0, 0, 1, 1), c'est-�-dire � la totalit� de l'image.</p>
	 * <p>La couleur de cette image est blanche.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>textureID : l'identifiant pass� en param�tre (textureID), ou "" s'il est null.</li>
	 * <li>textRect : un nouveau Rectangle de coordonn�es (0, 0, 1, 1).</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de cette image.
	 * @param x
	 * 		L'abscisse de cette image.
	 * @param y
	 * 		L'ordonn�e de cette image.
	 * @param w
	 * 		La largeur de cette image.
	 * @param h
	 * 		La hauteur de cette image.
	 * @param textureID
	 * 		Le nom du fichier texture de cette image.
	 */
	public TexturedImage(String name, double x, double y, double w, double h, String textureID) {
		
		super(name, x, y, w, h, Color.white);
		if (textureID != null)
			this.textureID = textureID;
		else
			this.textureID = "";
		this.textRect = new Rectangle(0, 0, 1, 1);
		
	}
	
	/**
	 * Constructeur TexturedImage 2.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il associe le nom du fichier texture � cette image gr�ce � l'identifiant textureID pass� en param�tre.
	 * Enfin, les dimensions de la texture sont initialis�es gr�ce au dernier param�tre de ce constructeur.</p>
	 * <p>La couleur de cette image est blanche.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>textureID : l'identifiant pass� en param�tre (textureID), ou "" s'il est null.</li>
	 * <li>textRect : le rectangle pass� en param�tre (text), ou un rectangle de coordonn�es (0, 0) et de dimensions
	 * d�finies par le champ sizeTextureFragment de la classe Constantes si le param�tre est null.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de cette image.
	 * @param x
	 * 		L'abscisse de cette image.
	 * @param y
	 * 		L'ordonn�e de cette image.
	 * @param w
	 * 		La largeur de cette image.
	 * @param h
	 * 		La hauteur de cette image.
	 * @param textureID
	 * 		Le nom de fichier texture de l'image.
	 * @param text
	 * 		Le rectangle des coordonn�es du fragment de texture de cette image.
	 */
	public TexturedImage(String name, double x, double y, double w, double h, String textureID, Rectangle text) {
		
		super(name, x, y, w, h, Color.white);
		if (textureID != null)
			this.textureID = textureID;
		else
			this.textureID = "";
		if (text != null)
			this.textRect = text;
		else
			this.textRect = new Rectangle(Constantes.sizeTextureFragment);
		
	}
	
	/**
	 * Clone cette image textur�e.
	 * 
	 * @return
	 * 		Une copie de cette image textur�e, ou null si le clonage �choue.
	 */
	@Override
	public TexturedImage clone() {
		
		return (TexturedImage) super.clone();
		
	}
	
	/**
	 * Retourne l'identifiant de texture de cette image.
	 * 
	 * @return
	 * 		L'identifiant de texture.
	 */
	public String getTextureID() {
		return this.textureID;
	}

	/**
	 * Retourne le rectangle des coordonn�es du fragment de texture de cette image.
	 * 
	 * @return
	 * 		Le rectangle des coordonn�es du fragment de texture de cette image.
	 */
	public Rectangle getTextRect() {
		return this.textRect;
	}

}
