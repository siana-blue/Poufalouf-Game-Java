package fr.poufalouf.combat;

import fr.poufalouf.tools.CombinedImage;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Orientation;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.ShiftingImage;

/**
 * D�cor d'un �cran de combat
 * 
 * <p>Cet objet de combat red�finit la m�thode geretateImage() et ne prend donc pas en compte les animations qui pourraient
 * lui �tre attribu�es.</p>
 * 
 * @author Anaïs Vernet
 */
public class CombatDecor extends CombatObject {
	
	/**
	 * L'image de ce d�cor.
	 */
	private ShiftingImage image;
	/**
	 * La vitesse de d�filement de l'image de ce d�cor.
	 */
	private int shiftingSpeed;
	
	/**
	 * Constructeur CombatDecor.
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
	 * @param or
	 * 		L'orientation de ce d�cor.
	 * @param rect
	 * 		Le rectangle des coordonn�es de ce d�cor.
	 * @param textureID
	 * 		Le nom du fichier texture de ce d�cor.
	 * @param shiftingSpeed
	 * 		La vitesse de d�filement de ce d�cor.
	 */
	public CombatDecor(String name, Orientation or, Rectangle rect, String textureID, int shiftingSpeed) {
		
		super(name, rect);
		
		this.image = new ShiftingImage("Image "+this.getName(), this.getX(), this.getY(), this.getW(), this.getH(),
				textureID, Constantes.initialDisplayWidth, Constantes.initialDisplayHeight);
		this.shiftingSpeed = shiftingSpeed;
		
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
	 * <p>Cette m�thode retourne l'image g�n�r�e par la ShiftingImage image de cette classe, puis met � jour le plan
	 * de l'image g�n�r�e avant de la retourner.</p>
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
