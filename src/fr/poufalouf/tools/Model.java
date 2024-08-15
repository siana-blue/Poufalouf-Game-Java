package fr.poufalouf.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import fr.poufalouf.InitializationException;
import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.UpdateException;

/**
 * Classe abstraite dont h�ritent tous les types de mod�les du jeu
 * 
 * <p>Un mod�le ne peut pas �tre instanci� de mani�re g�n�rique. Il est n�cessaire de choisir un type de Model pour le
 * pattern MVC (MapModel pour une carte par exemple).</p>
 * <p>Les fonctions d'un mod�le sont l'initialisation d'une zone de jeu (combat, carte etc...) ainsi que la mise � jour de
 * ses composants. Le contr�le v�rifie que les mouvements de l'utilisateur sont corrects avant de demander au mod�le de
 * les appliquer. Celui-ci ne v�rifie rien et applique les changements directement (d�placement, disparition d'�l�ment
 * etc...)</p>
 * <p>Un mod�le impl�mente l'interface Observable, et est observ� par un Observer qu'il doit tenir au courant. A chaque 
 * fois qu'un changement a lieu dans le mod�le, la vue qui l'observe (ou tout autre observateur) se met � jour.</p>
 * <p>Le pattern MVC se compose donc des �l�ments suivants :
 * <ul>
 * <li>Une vue (View) qui poss�de un contr�le (Control).</li>
 * <li>Un contr�le qui poss�de un mod�le (Model).</li>
 * <li>Un mod�le qui poss�de une liste d'observateurs, dont la vue (g�n�ralement seule).</li>
 * </ul>
 * </p>
 * <p>Le cycle est ainsi boucl�.</p>
 * 
 * @author Anaïs Vernet
 */
public abstract class Model implements Observable {
	
	/**
	 * La liste des objets Drawable repr�sentant les objets du jeu. 
	 * 
	 * <p>Ils vont permettre aux observateurs de se mettre � jour.</p>
	 */
	private ArrayList<Drawable> drawables;
	/**
	 * La liste des observateurs de ce mod�le.
	 */
	private ArrayList<Observer> observateurs;
	/**
	 * La table des �tats des touches du clavier.
	 */
	private HashMap<Integer, UserEvent> keys;
	/**
	 * Le nom de la musique de fond en cours de lecture.
	 */
	private String musique;
	/**
	 * La liste des sons � jouer.
	 */
	private ArrayList<String> sounds;
	
	/**
	 * Constructeur Model.
	 * 
	 * <p>Initialise ce mod�le. Cette mise � jour consiste � ajouter les objets
	 * du jeu � la liste des Drawable. La m�thode de cette classe utilis�e pour cela, initialize(ArrayList<Object>),
	 * est abstraite et d�pend du type de mod�le utilis�.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>drawables : une nouvelle liste (ArrayList) de Drawable.</li>
	 * <li>observateurs : une nouvelle liste (ArrayList) d'Observer.</li>
	 * <li>keys : une nouvelle table (HashMap) de UserEvent r�f�renc�s par des Integer.</li>
	 * <li>musique : "".</li>
	 * <li>sounds : une nouvelle liste (ArrayList) de String.</li>
	 * </ul></p>
	 * 
	 * @param param
	 * 		Les param�tres de ce mod�le.
	 * @throws InitializationException
	 * 		si la m�thode initialize(ArrayList<Object>) de cette classe l�ve une InitializationException, ou si les
	 * param�tres de ce constructeur ne sont pas valides (test r�alis� gr�ce � la m�thode abstraite
	 * checkParameters(ArrayList<Object>) de cette classe).
	 */
	public Model(ArrayList<Object> param) throws InitializationException {
		
		if (!(this.checkParameters(param)))
			throw new InitializationException("Les param�tres du constructeur du mod�le sont incorrects.");
		this.drawables = new ArrayList<Drawable>();
		this.observateurs = new ArrayList<Observer>();
		this.keys = new HashMap<Integer, UserEvent>();
		this.musique = "";
		this.sounds = new ArrayList<String>();
		initialize(param);
		
	}
	
	/**
	 * Met � jour ce mod�le et demande � tous ses observateurs d'en faire autant.
	 * 
	 * <p>Cette m�thode appelle successivement les m�thodes update(), updateObjects() et notifyObservers() de cette
	 * classe.</p>
	 * 
	 * @throws IOException
	 * 		si la m�thode notifyObservers() de cette classe l�ve une IOException.
	 * @throws UpdateException
	 * 		si la m�thode update() de cette classe l�ve une exception.
	 */
	public void refresh() throws IOException, UpdateException {
		
		try {
			update();
		} catch (ObjectInstanciationException e) {
			throw new UpdateException("Impossible de mettre � jour le mod�le.", e);
		}
		updateObjects();
		notifyObservers();

	}
	
	/**
	 * Ajoute un objet � la liste des Drawable repr�sentant les objets du jeu.
	 * 
	 * @param drw
	 * 		Drawable � ajouter � la liste. S'il est null, il n'est pas ajout�.
	 */
	public void addDrawable(Drawable drw) {
		
		if (drw != null)
			this.drawables.add(drw);
		
	}
	
	/**
	 * Efface la liste des Drawable repr�sentant les objets du jeu.
	 */
	public void clearDrawables() {
		
		this.drawables = new ArrayList<Drawable>();
		
	}
	
	/**
	 * Indique si les param�tres du constructeur sont corrects (instances des bonnes classes).
	 * 
	 * @param param
	 * 		Les param�tres du constructeur.
	 * @return
	 * 		Vrai si les param�tres sont corrects.
	 */
	public abstract boolean checkParameters(ArrayList<Object> param);
	
	/**
	 * Initialise ce mod�le.
	 * 
	 * @param param
	 * 		La liste des param�tres n�cessaires � l'initialisation.
	 * @throws InitializationException
	 * 		si une InitializationException est lev�e dans la m�thode red�finie.
	 */
	public abstract void initialize(ArrayList<Object> param) throws InitializationException;
	
	/**
	 * Met � jour la liste des objets de type Drawable repr�sentant les objets du jeu.
	 * 
	 * <p>Cette m�thode doit ajouter � la liste tous les objets que le mod�le a cr�� durant l'appel � la m�thode update()
	 * de cette classe, objets cr��s dans des variables propres aux classes h�rit�es de Model.</p>
	 * 
	 * @throws UpdateException
	 * 		si une UpdateException est lev�e par la m�thode red�finie.
	 */
	public abstract void updateObjects() throws UpdateException;
	
	/**
	 * Met � jour le contenu de ce mod�le.
	 * 
	 * @throws ObjectInstanciationException
	 * 		si une ObjectInstanciationException est lev�e par la m�thode red�finie.
	 * @throws UpdateException
	 * 		si une UpdateException est lev�e par la m�thode red�finie.
	 */
	public abstract void update() throws ObjectInstanciationException, UpdateException;
	
	/**
	 * Retourne une copie de la liste des Drawable de ce mod�le.
	 * 
	 * <p>La liste retourn�e est une copie mais les instances de Drawable sont les originales.</p>
	 * 
	 * @return
	 * 		Une copie de la liste des Drawable de ce mod�le.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Drawable> drawables() {
		
		return (ArrayList<Drawable>) this.drawables.clone();
		
	}
	
	/**
	 * Retourne l'�tat de la touche du clavier pass�e en param�tre, sous forme de champ de la classe Keyboard de slick.
	 * 
	 * <p>Cette m�thode utilise la table keys de cette classe. Si cette table ne contient aucune valeur pour la cl�
	 * pass�e en param�tre, elle renvoie KEY_UP.</p>
	 * 
	 * @param key
	 * 		La touche du clavier � tester.
	 * @return
	 * 		Une valeur caract�risant l'�tat de la touche.
	 */
	public UserEvent keyPressed(int key) {
		
		Integer n = new Integer(key);
		if (this.keys.get(n) != null)
			return this.keys.get(n);
		
		return UserEvent.KEY_UP;
		
	}
	
	/**
	 * Met � jour l'�tat de la touche pass�e en param�tre.
	 * 
	 * <p>Cette m�thode met � jour le champ correspondant dans la table keys de cette classe. Le premier param�tre
	 * est un champ de la classe Keyboard de slick.</p>
	 * 
	 * @param key
	 * 		La touche du clavier.
	 * @param event
	 * 		L'�tat de la touche du clavier.
	 */
	public void setPressed(int key, UserEvent event) {
		
		this.keys.put(new Integer(key), event);
		
	}
	
	/**
	 * Met � jour le nom du fichier de la musique de fond.
	 * 
	 * @param mus
	 * 		Le nom du fichier de la musique de fond. S'il est null, la mise � jour n'a pas lieu.
	 */
	public void setMusique(String mus) {
		if (mus != null)
			this.musique = mus;
	}
	
	/**
	 * Retourne le nom du fichier de la musique de fond � lire.
	 * 
	 * @return
	 * 		Le nom du fichier de la musique de fond.
	 */
	public String getMusique() {
		return this.musique;
	}
	
	/**
	 * Ajoute le nom d'un son � jouer � ce mod�le.
	 * 
	 * @param fileName
	 * 		Le nom du fichier audio. S'il est null ou "", il n'est pas ajout�.
	 */
	public void addSound(String fileName) {
		
		if (fileName != null && fileName != "")
			this.sounds.add(fileName);
		
	}
	
	/**
	 * Retourne une copie de la liste des sons � jouer pour ce mod�le.
	 * 
	 * <p>La liste sounds de cette classe est alors effac�e.</p>
	 * 
	 * @return
	 * 		Une copie de la liste des sons � jouer.
	 */
	public ArrayList<String> sounds() {
		
		@SuppressWarnings("unchecked")
		ArrayList<String> s = (ArrayList<String>) this.sounds.clone();
		this.sounds.clear();
		
		return s;
		
	}

	/**
	 * Met � jour les observateurs de ce mod�le.
	 * 
	 * <p>Cette m�thode appelle la m�thode update(Observable) de tous les observateurs observant ce mod�le.</p>
	 * 
	 * @throws IOException
	 * 		si la m�thode update(Observable) de l'interface Observer l�ve une IOException.
	 */
	@Override
	public void notifyObservers() throws IOException {
		
		for (Observer observ : this.observateurs) {
			observ.update(this);
		}
		
	}

	/**
	 * Ajoute un observateur � la liste des observateurs.
	 * 
	 * @param obs
	 * 		L'observateur � ajouter. S'il est null, il n'est pas ajout�.
	 */
	@Override
	public void addObserver(Observer obs) {
		
		if (obs != null)
			this.observateurs.add(obs);
		
	}

	/**
	 * Retire un observateur de la liste des observateurs.
	 * 
	 * @param obs
	 * 		L'observateur � retirer.
	 */
	@Override
	public void removeObserver(Observer obs) {
		
		this.observateurs.remove(obs);
		
	}

}
