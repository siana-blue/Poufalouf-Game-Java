package fr.poufalouf.map;

import fr.poufalouf.tools.CombinedImage;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.ZoneType;

/**
 * El�ment de d�cor d'une carte
 * 
 * <p>Ce type d'objet n'a pas de comportement propre et reste immobile.</p>
 * 
 * @author Anaïs Vernet
 */
public class Decor extends MapObject {
	
	/**
	 * Le nombre de plans de perspective � ajouter � ce d�cor lors de la g�n�ration de son image.
	 */
	private int plan;
	
	/**
	 * Constructeur Decor.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il modifie ensuite la zone de collision de ce d�cor.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>plan : le nombre pass� en param�tre (plan).</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de ce d�cor, auquel sera ajout� dans le constructeur les coordonn�es de cet objet.
	 * @param carte
	 * 		La carte � laquelle appartient ce d�cor.
	 * @param x
	 * 		L'abscisse par rapport au jeu de ce d�cor.
	 * @param y
	 * 		L'ordonn�e par rapport au jeu de ce d�cor.
	 * @param w
	 * 		La largeur de cet objet.
	 * @param h
	 * 		La hauteur de cet objet.
	 * @param collisionZone
	 * 		Le rectangle des coordonn�es par rapport � cet objet de la zone de collision de ce d�cor.
	 * @param plan
	 * 		Le nombre de plans de perspective suppl�mentaires � ajouter � ce d�cor lors de la g�n�ration de son image.
	 */
	public Decor(String name, Map carte, double x, double y, double w, double h, Rectangle collisionZone, 
			int plan) {
		
		super(name+"(x:"+x+",y:"+y+")", carte, x, y, w, h);
		this.plan = plan;
		this.changeZone(ZoneType.COLLISION, collisionZone);
		
	}
	
	/**
	 * G�n�re une image pour ce d�cor.
	 * 
	 * <p>Cette m�thode appelle la m�thode de la classe m�re, puis ajoute le nombre de plan de ce d�cor � l'image
	 * g�n�r�e (ce nombre peut �tre n�gatif).</p>
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	@Override
	public CombinedImage generateObjectImage() {
		
		CombinedImage img = super.generateObjectImage();
		
		if (img != null)
			img.setPlan(img.getPlan()+this.plan);
		
		return img;
		
	}

}
