package fr.poufalouf.map;

/**
 * Interface impl�ment�e par un objet d'une carte pouvant �tre activ�
 * 
 * <p>Ce type d'objet doit poss�der une zone d'effet. Lorsque un objet activateur p�n�tre cette zone, une action est
 * d�clench�e, et cet objet entre en mode activ�. Il est ensuite possible de d�sactiver cet objet.</p>
 * 
 * @author Anaïs Vernet
 */
public interface ActivableObject {
	
	/**
	 * Active cet objet.
	 * 
	 * <p>L'objet pass� en param�tre est l'objet ayant d�clench� l'activation de cet objet.</p>
	 * 
	 * @param obj
	 * 		L'objet ayant activ� cet objet.
	 */
	public void activate(MapObject obj);
	
	/**
	 * Effectue les actions de l'effet de cet objet.
	 * 
	 * <p>Ces actions sont r�alis�es � chaque mise � jour de cet objet, entre le moment o� la m�thode activate(MapObject)
	 * de cette interface est appel�e et le moment o� la m�thode desactivate() est appel�e.</p>
	 */
	public void playEffect();
	
	/**
	 * D�sactive cet objet.
	 */
	public void desactivate();
	
	/**
	 * Indique si cet objet est activ�.
	 * 
	 * @return
	 * 		Vrai si cet objet est activ�.
	 */
	public boolean isActivated();
	
	/**
	 * Retourne l'activateur de cet objet.
	 * 
	 * @return
	 * 		L'activateur de cet objet.
	 */
	public MapObject getActivateur();

}
