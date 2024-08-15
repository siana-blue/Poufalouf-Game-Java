package fr.poufalouf.tools;

/**
 * Enum�ration des �tats que peut prendre un objet du jeu
 * 
 * @author Anaïs Vernet
 */
public enum Status {
	
	/**
	 * Etat caract�risant l'arr�t, ou le comportement par d�faut.
	 */
	STILL,
	/**
	 * Etat d'activation.
	 */
	ACTIVATED,
	/**
	 * Etat caract�risant la marche.
	 */
	WALKING,
	/**
	 * Etat caract�risant le saut.
	 */
	JUMPING,
	/**
	 * Etat caract�risant l'encaissement de dommage.
	 */
	HURT,
	/**
	 * Etat de noyade.
	 */
	DROWNING,
	/**
	 * Etat pr�c�dant la mort d'un objet.
	 */
	DYING,
	/**
	 * Etat de fin de vie d'un objet.
	 */
	DEAD,
	/**
	 * Etat d'attaque.
	 */
	ATTACKING,
	/**
	 * Etat d'attaque (2).
	 */
	ATTACKING2,
	/**
	 * Etat surlign� ou survol�.
	 */
	HIGHLIGHTED,
	/**
	 * Etat de transition.
	 */
	TRANSITION;

}
