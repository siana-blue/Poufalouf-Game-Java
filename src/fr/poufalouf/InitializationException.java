package fr.poufalouf;

/**
 * Exception lev�e lorsqu'une initialisation ne s'est pas faite correctement
 * 
 * @author Anaïs Vernet
 */
public class InitializationException extends Exception {
	
	/**
	 * Constructeur InitializationException 1.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il ajoute le message "Erreur d'initialisation : " avant le message pass� en
	 * param�tre.</p>
	 * 
	 * @param message
	 * 		Le message de l'erreur.
	 */
	public InitializationException(String message) {
		
		super("Erreur d'initialisation : "+message);
		
	}
	
	/**
	 * Constructeur InitializationException 2.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il ajoute le message "Erreur d'initialisation : " avant le message pass� en
	 * param�tre.</p>
	 * 
	 * @param message
	 * 		Le message de l'erreur.
	 * @param e
	 * 		L'exception ayant caus� l'erreur.
	 */
	public InitializationException(String message, Throwable e) {
		
		super("Erreur d'initialisation : "+message, e);
		
	}

}
