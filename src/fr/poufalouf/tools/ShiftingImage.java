package fr.poufalouf.tools;

import fr.poufalouf.tools.CombinedImage;
import fr.poufalouf.tools.Orientation;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.TexturedImage;

/**
 * Image pouvant d�filer
 * 
 * <p>Cette classe h�ritant de la classe TexturedImage ajoute une m�thode de d�calage de l'image (shift) prenant en
 * param�tre une Orientation. Elle red�finit la m�thode generateImage() de la classe m�re pour g�n�rer une mosa�que
 * (CombinedImage) de l'image principale.</p>
 * 
 * @author Anaïs Vernet
 */
public class ShiftingImage extends TexturedImage {

	/**
	 * La largeur maximale de la mosa�que de cette image.
	 */
	private double width;
	/**
	 * La hauteur maximale de la mosa�que de cette image.
	 */
	private double height;
	/**
	 * Le d�calage horizontal de cette image en pixels.
	 * 
	 * <p>Cette valeur est inf�rieure � la taille de l'image principale, d�finie par getTextRect(). Elle repr�sente la distance
	 * entre la premi�re extr�mit� gauche du "fragment de mosa�que" entier rencontr� et le bord gauche de l'image enti�re
	 * de cette image. Il s'agit donc d'un d�calage vers la droite.</p>
	 */
	private double horizontalShift;
	/**
	 * Le d�calage vertical de cette image en pixels.
	 * 
	 * <p>Cette valeur est inf�rieure � la taille de l'image principale, d�finie par getTextRect(). Elle repr�sente la distance
	 * entre la premi�re extr�mit� haute du "fragment de mosa�que" entier rencontr� et le bord haut de l'image enti�re
	 * de cette image. Il s'agit donc d'un d�calage vers le bas.</p>
	 */
	private double verticalShift;
	
	/**
	 * Constructeur ShiftingImage 1.
	 * 
	 * <p>Ce constructeur appelle le constructeur 1 de la classe m�re.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>width : la largeur pass�e en param�tre.</li>
	 * <li>height : la hauteur pass�e en param�tre.</li>
	 * <li>horizontalShift : 0.</li>
	 * <li>verticalShift : 0.</li>
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
	 * @param width
	 * 		La largeur maximale de la mosa�que de cette image.
	 * @param height
	 * 		La hauteur maximale de la mosa�que de cette image.
	 */
	public ShiftingImage(String name, double x, double y, double w, double h, String textureID, double width,
			double height) {
		
		super(name, x, y, w, h, textureID);
		this.width = width;
		this.height = height;
		this.horizontalShift = this.verticalShift = 0;
		
	}
	
	/**
	 * Constructeur ShiftingImage 2.
	 * 
	 * <p>Ce constructeur appelle le constructeur 1.</p>
	 * <p>Il associe ensuite les valeurs pass�es en param�tre aux champs horizontalShift et verticalShift de cette
	 * classe.</p>
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
	 * @param width
	 * 		La largeur maximale de la mosa�que de cette image.
	 * @param height
	 * 		La hauteur maximale de la mosa�que de cette image.
	 * @param horizontal
	 * 		La valeur de d�calement horizontal.
	 * @param vertical
	 * 		La valeur de d�calement vertical.
	 */
	public ShiftingImage(String name, int x, int y, int w, int h, String textureID, int width, int height,
			int horizontal, int vertical) {
		
		this(name, x, y, w, h, textureID, width, height);
		this.horizontalShift = horizontal;
		this.verticalShift = vertical;
		
	}
	
	/**
	 * Constructeur ShiftingImage 3.
	 * 
	 * <p>Ce constructeur appelle le constructeur 2 de la classe m�re.</p>
	 * <p>Il associe ensuite les valeurs pass�es en param�tre aux champs horizontalShift et verticalShift de cette
	 * classe.</p>
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
	 * @param text
	 * 		Le rectangle des coordonn�es de texture de cette image.
	 * @param width
	 * 		La largeur maximale de la mosa�que de cette image.
	 * @param height
	 * 		La hauteur maximale de la mosa�que de cette image.
	 * @param horizontal
	 * 		La valeur de d�calement horizontal.
	 * @param vertical
	 * 		La valeur de d�calement vertical.
	 */
	public ShiftingImage(String name, int x, int y, int w, int h, String textureID, Rectangle text, int width, int height,
			int horizontal, int vertical) {
		
		super(name, x, y, w, h, textureID, text);
		this.width = width;
		this.height = height;
		this.horizontalShift = horizontal;
		this.verticalShift = vertical;
		
	}
	
	/**
	 * D�cale cette image dans la direction sp�cifi�e en param�tre.
	 * 
	 * <p>L'orientation pass�e en param�tre, indiquant la direction du d�calage, doit �tre un point cardinal simple.</p>
	 * 
	 * @param orientation
	 * 		L'orientation dans laquelle doit se d�placer cette image.
	 */
	public void shift(Orientation orientation) {
		
		if (orientation == null)
			return;
		
		switch(orientation) {
		case NORD:
			if (this.verticalShift > 0)
				this.verticalShift--;
			else
				this.verticalShift = this.getH()-1;
			break;
		case SUD:
			if (this.verticalShift < this.getH())
				this.verticalShift++;
			else
				this.verticalShift = 0;
			break;
		case OUEST:
			if (this.horizontalShift > 0)
				this.horizontalShift--;
			else
				this.horizontalShift = this.getW()-1;
			break;
		case EST:
			if (this.horizontalShift < this.getW())
				this.horizontalShift++;
			else
				this.horizontalShift = 0;
			break;
		default:
		}
		
	}
	
	/**
	 * G�n�re l'image de cette image.
	 * 
	 * <p>L'image g�n�r�e est une image combin�e dessinant une mosa�que r�p�tant l'image principale de cette image.</p>
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	@Override
	public CombinedImage generateImage() {
		
		CombinedImage img = new CombinedImage(new TexturedImage(this.getName(), 0, 0, this.width, this.height, null));
		
		// x et y d�signent le bord sup�rieur gauche de la sous-image de la mosa�que en cours d'ajout
		double x = 0, y = 0;
		double w, h;
		w = Math.min(this.horizontalShift, this.width)/this.getW();
		h = Math.min(this.verticalShift, this.height)/this.getH();
		double textX = 1-w, textY = 1-h;
		if (w == 0)
			w = Math.min(this.getW(), this.width)/this.getW();
		if (h == 0)
			h = Math.min(this.getH(), this.height)/this.getH();
		
		int nbFrag = 1;
		do {
			// Ajout des sous-images en coordonn�es relatives � l'image principale
			
			img.addImage(new TexturedImage(this.getName()+" fragment "+nbFrag, x/img.getW(), y/img.getH(),
					w*this.getW()/img.getW(), h*this.getH()/img.getH(),
					this.getTextureID(),
					new Rectangle(textX, textY, w, h)));
			
			// Mise � jour de x, y, w et h pour la prochaine sous-image
			
			x += w*this.getW();
			if (x >= this.width && y < this.height) {
				x = 0;
				y += h*this.getH();
				w = Math.min(this.horizontalShift, this.width)/this.getW();
				if (w == 0)
					w = Math.min(this.getW(), this.width)/this.getW();
				h = Math.min(this.getH(), this.height-y)/this.getH();
				textX = 1-w;
				textY = 0;
			} else {
				w = Math.min(this.getW(), this.width-x)/this.getW();
				textX = 0;
			}
			nbFrag++;
		} while (x < this.width || y < this.height);
		
		img.setPlan(this.getPlan());
		
		return img;
		
	}

}
