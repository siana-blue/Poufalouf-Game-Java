package fr.poufalouf.tools;

import java.io.IOException;

/**
 * Interface permettant � une classe d'�tre observ�e par des Observer et de les tenir au courant de son �volution
 * 
 * <p>Un objet impl�mentant cette interface doit tenir ses observateurs inform�s de son �volution au moyen de la m�thode
 * notifyObservers().</p>
 * 
 * @author Anaïs Vernet
 */
public interface Observable {

	/**
	 * Envoie des informations � tous les observateurs de cet Observable et leur demande de se mettre � jour.
	 * 
	 * @throws IOException
	 * 		si la m�thode red�finie l�ve une IOException.
	 */
	public void notifyObservers() throws IOException;
	
	/**
	 * Ajoute un observateur � la liste des observateurs.
	 * 
	 * @param obs
	 * 		L'observateur � ajouter.
	 */
	public void addObserver(Observer obs);
	
	/**
	 * Retire un observateur de la liste des observateurs.
	 * 
	 * @param obs
	 * 		L'observateur � retirer.
	 */
	public void removeObserver(Observer obs);
	
}
