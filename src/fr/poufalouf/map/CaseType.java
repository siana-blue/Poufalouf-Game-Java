package fr.poufalouf.map;
/**
 * Enum�ration des types de cases pouvant �tre rencontr�s sur une carte
 * 
 * @author Anaïs Vernet
 */
public enum CaseType {
	
	/**
	 * Case solide sur laquelle les objets peuvent reposer.
	 */
	SOLID,
	/**
	 * Case solide sur laquelle les objets peuvent reposer (d�cor sp�cial).
	 */
	SOLID2,
	/**
	 * Case liquide.
	 */
	WATER,
	/**
	 * Case vide sur laquelle les objets ne peuvent pas aller.
	 */
	VOID;

}
