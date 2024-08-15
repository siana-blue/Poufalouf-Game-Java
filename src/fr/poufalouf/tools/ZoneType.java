package fr.poufalouf.tools;

import org.newdawn.slick.Color;

/**
 * Enum�ration des diff�rents types de zones du jeu
 * 
 * @author Anaïs Vernet
 */
public enum ZoneType {

	/**
	 * Zone d'affichage d'une image.
	 */
	IMAGE(Color.blue),
	/**
	 * Zone de collision.
	 */
	COLLISION(Color.red),
	/**
	 * Zone d'activation d'effet.
	 */
	EFFECT(Color.green),
	/**
	 * Zone de d�tection.
	 */
	DETECTION(Color.yellow);
	
	/**
	 * La couleur associ�e � ce type de zone.
	 */
	private final Color color;
	
	/**
	 * Constructeur ZoneType.
	 * 
	 * <p>Il s'agit d'un constructeur priv� qui ne peut �tre utilis� que pour initialiser les champs de cette
	 * �num�ration.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>color : la couleur pass�e en param�tre, ou Color.white si le param�tre est null.</li>
	 * </ul></p>
	 * 
	 * @param color
	 * 		La couleur � associer � ce type de zone.
	 */
	private ZoneType(Color color) {
		
		if (color != null)
			this.color = color;
		else
			this.color = Color.white;
		
	}

	/**
	 * Retourne la couleur associ�e � ce type de zone.
	 * 
	 * @return
	 * 		La couleur associ�e � ce type de zone.	
	 */
	public Color getColor() {
		return this.color;
	}
	
}
