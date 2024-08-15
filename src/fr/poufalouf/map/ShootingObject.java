package fr.poufalouf.map;

import java.util.ArrayList;

/**
 * Interface impl�ment�e par un objet d'une carte pouvant lancer des projectiles
 * 
 * @author Anaïs Vernet
 */
public interface ShootingObject {

	/**
	 * Lance un projectile.
	 */
	public void shoot();
	
	/**
	 * Retourne la liste des projectiles lanc�s par cet objet.
	 * 
	 * <p>Cette m�thode retourne null si la liste des projectiles n'est pas correctement d�finie.</p>
	 * 
	 * @return
	 * 		La liste des projectiles.
	 */
	public ArrayList<Projectile> projectiles();
	
	/**
	 * Retourne le nombre de projectiles lanc�s depuis la cr�ation de cet objet.
	 * 
	 * @return
	 * 		Le nombre total de projectiles lanc�s par cet objet.
	 */
	public int getNbProjectiles();
	
}
