package fr.poufalouf.combat;

import fr.poufalouf.game.GameObject;
import fr.poufalouf.tools.CombinedImage;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.TexturedImage;

/**
 * Objet d'un �cran de combat
 * 
 * @author Anaïs Vernet
 */
public class CombatObject extends GameObject {
	
	/**
	 * Constructeur CombatObject.
	 * 
	 * @param name
	 * 		Le nom de cet objet.
	 * @param rect
	 * 		Le rectangle des coordonn�es de cet objet.
	 */
	public CombatObject(String name, Rectangle rect) {
		
		super(name, (rect==null)?0:rect.getX(), (rect==null)?0:rect.getY(), (rect==null)?0:rect.getW(),
				(rect==null)?0:rect.getH());
		
		this.setDelayUpdate(60);
		this.setSpatialSound(false);
		
	}
	
	/**
	 * G�n�re une image repr�sentant cet objet.
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	@Override
	public CombinedImage generateObjectImage() {
		
		if (this.currentAnim() == null)
			return null;
		CombinedImage t = new CombinedImage(new TexturedImage("Image "+this.getName(), this.getX(), this.getY(),
				this.getW(), this.getH(), this.currentAnim().getTextureID(),
				this.currentAnim().textRect(this.getOrientation())));
		t.setPlan(this.getPlan());
		return t;
		
	}
	
}
