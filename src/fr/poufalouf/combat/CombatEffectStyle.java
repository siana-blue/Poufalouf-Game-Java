package fr.poufalouf.combat;

/**
 * Enum�ration des types d'animation des effets de combat
 * 
 * @author Anaïs Vernet
 */
public enum CombatEffectStyle {
	
	/**
	 * Effet dont les coordonn�es sont relatives � l'�cran.
	 */
	RELATIVE_TO_SCREEN,
	/**
	 * Effet dont les coordonn�es sont relatives � la cible.
	 */
	RELATIVE_TO_TARGET,
	/**
	 * Effet dont les coordonn�es sont relatives au coeur de la cible.
	 */
	RELATIVE_TO_TARGET_HEART,
	/**
	 * Effet orient� de l'attaquant � la cible.
	 */
	FROM_ATTACKER_TO_TARGET,
	/**
	 * Effet de projectile lanc� vers la cible.
	 */
	THROWN_TO_TARGET;

}
