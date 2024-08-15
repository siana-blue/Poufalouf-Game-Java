package fr.poufalouf.tools;

import java.io.IOException;

/**
 * Interface permettant � une classe d'observer un Observable
 * 
 * <p>Cette interface doit �tre impl�ment�e pour pouvoir observer un Observable et �tre notifi� de tout changement de 
 * celui-ci.</p>
 * 
 * @author Anaïs Vernet
 */
public interface Observer {

	/**
	 * Met � jour cet Observer selon l'Observable observ�.
	 * 
	 * @param obs
	 * 		L'Observable observ�.
	 * @throws IOException
	 * 		si la m�thode red�finie l�ve une IOException.
	 */
	public void update(Observable obs) throws IOException;
	
}
