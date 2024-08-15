package fr.poufalouf.tools;

import java.util.ArrayList;

/**
 * Objet d�finissant la portion du jeu � afficher par une vue
 * 
 * <p>Une cam�ra simple est immobile et sa position ne peut �tre red�finie que par des appels explicites � ses
 * setters. Il est possible de d�finir des instances de classes h�rit�es de la classe Camera pour des
 * usages sp�cifiques � certaines situations (combat, carte etc...).</p>
 * <p>Les coordonn�es d'une cam�ra sont des coordonn�es par rapport au jeu.</p>
 * 
 * @author Anaïs Vernet
 */
public class Camera {
	
	/**
	 * L'abscisse de cette cam�ra par rapport au jeu.
	 */
	private int x;
	/**
	 * L'ordonn�e de cette cam�ra par rapport au jeu.
	 */
	private int y;
	
	/**
	 * Constructeur Camera.
	 * 
	 * <p></p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>x : l'abscisse pass�e en param�tre (x).</li>
	 * <li>y : l'ordonn�e pass�e en param�tre (y).</li>
	 * </ul></p>
	 * 
	 * @param x
	 * 		L'abscisse de cette cam�ra.
	 * @param y
	 * 		L'ordonn�e de cette cam�ra.
	 */
	public Camera(int x, int y) {
		
		this.x = x;
		this.y = y;
		
	}
	
	/**
	 * Met � jour la position de cette cam�ra par rapport � une liste de Drawable.
	 * 
	 * <p>Pour une cam�ra simple, cette m�thode ne fait rien.</p>
	 * 
	 * @param drws
	 * 		La liste des Drawable d�terminant le repositionnement de cette cam�ra.
	 */
	public void update(ArrayList<Drawable> drws) {
		
		// ne rien faire
		
	}

	/**
	 * Met � jour l'abscisse de cette cam�ra par rapport au jeu.
	 * 
	 * @param x
	 * 		L'abscisse de cette cam�ra.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Retourne l'abscisse de cette cam�ra par rapport au jeu.
	 * 
	 * @return
	 * 		L'abscisse de cette cam�ra.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Met � jour l'ordonn�e de cette cam�ra par rapport au jeu.
	 * 
	 * @param y
	 * 		L'ordonn�e de cette cam�ra.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Retourne l'ordonn�e de cette cam�ra par rapport au jeu.
	 * 
	 * @return
	 * 		L'ordonn�e de cette cam�ra.
	 */
	public int getY() {
		return this.y;
	}

}
