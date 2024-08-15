package fr.poufalouf;

/**
 * Exception lev�e lorsqu'un objet est manquant.
 * 
 * @author Anaïs Vernet
 */
public class MissingObjectException extends Exception {
	
	/**
	 * Constructeur MissingObjectException 1.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * 
	 * @param message
	 * 		Le message de cette exception.
	 */
	public MissingObjectException(String message) {
		
		super(message);
		
	}

}
