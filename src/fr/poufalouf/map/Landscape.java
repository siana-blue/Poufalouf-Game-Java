package fr.poufalouf.map;

import fr.poufalouf.tools.CombinedImage;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Orientation;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.ShiftingImage;

/**
 * D�cor pouvant d�filer et repr�sentant un �l�ment de paysage.
 * 
 * @author Anaïs Vernet
 */
public class Landscape extends MapObject {
	
	/**
	 * L'image de ce d�cor.
	 */
	private ShiftingImage image;
	/**
	 * La vitesse de d�filement de l'image de ce d�cor.
	 */
	private int shiftingSpeed;
	
	/**
	 * Constructeur Landscape.
	 * 
	 * <p>Ce contructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Apr�s initialisation, l'orientation de cet objet prend la valeur pass�e en param�tre (or).</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p><ul>
	 * <li>image : une nouvelle ShiftingImage dont le fichier texture est sp�cifi� en param�tre (textureID),
	 * dont les dimensions de l'image principale sont d�finies par le param�tre rect, et dont les coordonn�es globales
	 * sont celles de l'�cran entier (champs initialDisplayWidth et initialDisplayHeight de la classe Constantes).</li>
	 * <li>shiftingSpeed : le param�tre de ce constructeur (shiftingSpeed).</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de ce d�cor.
	 * @param carte
	 * 		La carte � laquelle appartient ce d�cor.
	 * @param or
	 * 		L'orientation de ce d�cor.
	 * @param rect
	 * 		Le rectangle des coordonn�es de ce d�cor.
	 * @param textureID
	 * 		Le nom du fichier texture de ce d�cor.
	 * @param shiftingSpeed
	 * 		La vitesse de d�filement de ce d�cor.
	 * @param plan
	 * 		Le plan de perspective de ce d�cor.
	 */
	public Landscape(String name, Map carte, Orientation or, Rectangle rect, String textureID, int shiftingSpeed,
			int plan) {
		
		super(name, carte, (int) ((rect==null)?0:rect.getX()), (int) ((rect==null)?0:rect.getY()),
				(int) ((rect==null)?-1:rect.getW()), (int) ((rect==null)?-1:rect.getH()));
		
		this.image = new ShiftingImage("Image "+this.getName(), this.getX(), this.getY(),
				(int) ((rect==null)?-1:rect.getW()), (int) ((rect==null)?-1:rect.getH()),
				textureID, Constantes.cellPerMapW*Constantes.sizeCell, Constantes.cellPerMapH*Constantes.sizeCell);
		this.shiftingSpeed = shiftingSpeed;
		this.setPlan(plan);
		
		this.setOrientation(or);
		
	}
	
	/**
	 * Fait d�filer l'image de ce d�cor.
	 * 
	 * <p>Ceci se fait par appel � la m�thode shift(Orientation)
	 * de la classe ShiftingImage, le nombre de fois sp�cifi� par le champ shiftingSpeed de cette classe.</p>
	 */
	public void shift() {
		
		for (int i=0;i<this.shiftingSpeed;i++)
			this.image.shift(this.getOrientation());
		
	}
	
	/**
	 * G�n�re une image d�filante pour ce d�cor.
	 * 
	 * <p>Cette m�thode retourne l'image g�n�r�e par la ShiftingImage image de cette classe.</p>
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	@Override
	public CombinedImage generateObjectImage() {
		
		CombinedImage t = this.image.generateImage();
		t.setPlan(this.getPlan());
		
		return t;
		
	}

}
