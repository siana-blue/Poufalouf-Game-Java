package fr.poufalouf;

/**
 * Exception lev�e lorsqu'une mise � jour n'est pas faite correctement
 * 
 * @author Anaïs Vernet
 */
public class UpdateException extends Exception {
	
	/**
	 * Constructeur UpdateException 1.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il ajoute le message "Erreur de mise � jour : " avant le message pass� en
	 * param�tre.</p>
	 * 
	 * @param message
	 * 		Le message de l'erreur.
	 */
	public UpdateException(String message) {
		
		super("Erreur d'initialisation : "+message);
		
	}
	
	/**
	 * Constructeur UpdateException 2.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il ajoute le message "Erreur de mise � jour : " avant le message pass� en
	 * param�tre.</p>
	 * 
	 * @param message
	 * 		Le message de l'erreur.
	 * @param e
	 * 		L'exception ayant caus� l'erreur.
	 */
	public UpdateException(String message, Throwable e) {
		
		super("Erreur d'initialisation : "+message, e);
		
	}

}
