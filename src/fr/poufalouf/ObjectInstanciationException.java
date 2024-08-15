package fr.poufalouf;

/**
 * Exception lev�e lorsqu'un objet du jeu n'a pas pu �tre instanci� correctement
 * 
 * @author Anaïs Vernet
 */
public class ObjectInstanciationException extends Exception {
	
	/**
	 * Constructeur ObjectInstanciationException 1.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il ajoute le message "Erreur d'instanciation de l'objet nom_de_l'objet : " avant le message pass� en
	 * param�tre.</p>
	 * 
	 * @param message
	 * 		Le message de l'erreur.
	 * @param objectName
	 * 		Le nom de l'objet ayant caus� l'erreur.
	 */
	public ObjectInstanciationException(String message, String objectName) {
		
		super("Erreur d'instanciation de l'objet "+objectName+" : "+message);
		
	}
	
	/**
	 * Constructeur ObjectInstanciationException 2.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il ajoute le message "Erreur d'instanciation de l'objet nom_de_l'objet : " avant le message pass� en
	 * param�tre.</p>
	 * 
	 * @param message
	 * 		Le message de l'erreur.
	 * @param objectName
	 * 		Le nom de l'objet ayant caus� l'erreur.
	 * @param e
	 * 		L'exception ayant caus� l'erreur.
	 */
	public ObjectInstanciationException(String message, String objectName, Throwable e) {
		
		super("Erreur d'instanciation de l'objet "+objectName+" : "+message, e);
		
	}

}
