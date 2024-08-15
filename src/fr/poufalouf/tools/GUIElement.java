package fr.poufalouf.tools;

/**
 * El�ment de l'interface du jeu (boutons, listes etc...)
 * 
 * @author Anaïs Vernet
 */
public interface GUIElement extends Drawable {
	
	/**
	 * Met � jour cet �l�ment.
	 */
	public void update();
	/**
	 * Indique si ce type d'�l�ment peut �tre s�lectionn�.
	 * 
	 * @return
	 * 		Vrai si ce type d'�l�ment peut �tre s�lectionn�.
	 */
	public boolean isSelectable();
	/**
	 * Rend cet �l�ment actif.
	 */
	public void enable();
	/**
	 * Rend cet �l�ment inactif.
	 */
	public void disable();
	/**
	 * Indique si cet �l�ment est actif.
	 * 
	 * @return
	 * 		Vrai s'il est actif.
	 */
	public boolean isEnabled();
	/**
	 * Donne le focus � cet �l�ment.
	 */
	public void highlight();
	/**
	 * Enl�ve le focus � cet �l�ment.
	 */
	public void leave();
	/**
	 * Indique si cet �l�ment a le focus.
	 * 
	 * @return
	 * 		Vrai si cet �l�ment a le focus.
	 */
	public boolean isHighlighted();
	/**
	 * Active cet �l�ment.
	 */
	public void activate();
	/**
	 * D�sactive cet �l�ment.
	 */
	public void desactivate();
	/**
	 * Indique si cet �l�ment est activ�.
	 * 
	 * @return
	 * 		Vrai si cet �l�ment est activ�.
	 */
	public boolean isActivated();
	/**
	 * Indique si cet �l�ment est en cours de transition d'un �tat � un autre.
	 * 
	 * @return
	 * 		Vrai si cet �l�ment est en cours de transition d'un �tat � un autre.
	 */
	public boolean isInTransition();

}
