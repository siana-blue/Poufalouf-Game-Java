package fr.poufalouf.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.tools.Animation;

/**
 * Carte 2D vue du ciel
 * 
 * <p>Cette classe g�re les objets du jeu en mode carte. Afin d'�tre correctement utilis�e, elle doit �tre utilis�e avec
 * les classes suivantes :
 * <ul>
 * <li>MapModel</li>
 * <li>MapControl</li>
 * <li>MapCamera</li>
 * </ul></p>
 * 
 * @author Anaïs Vernet
 */
public class Map {

	/**
	 * Le nombre de cases en largeur sur cette carte.
	 */
	private final int nbCasesWidth;
	/**
	 * Le nombre de cases en hauteur sur cette carte.
	 */
	private final int nbCasesHeight;
	/**
	 * Le fichier texture utilis� pour les cases de cette carte.
	 */
	private final String terrainID;
	/**
	 * Le tableau de cases de cette carte.
	 */
	private Case[][] carte;
	/**
	 * La liste des objets de cette carte.
	 */
	private HashMap<String, MapObject> objets;
	/**
	 * Table de hachage stockant le nombre d'objets d'une classe dans le jeu.
	 * 
	 * <p>La cl� est le nom de l'objet sans (x:valeur,y:valeur).</p>
	 */
	private HashMap<String, Integer> nbObjets;
	/**
	 * Table de hachage stockant les animations des objets du jeu.
	 * 
	 * <p>La cl� est le nom de l'objet sans (x:valeur,y:valeur).</p>
	 */
	private HashMap<String, ArrayList<Animation>> anims;
	
	/**
	 * Constructeur Map.
	 * 
	 * <p>Cette case est initialis�e avec un tableau de cases vide et une liste d'objets vide, ainsi qu'un
	 * fichier texture pour le paysage.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>nbCasesWidth : la largeur pass�e en param�tre (w), ou 1 si le param�tre est inf�rieur � 1.</li>
	 * <li>nbCasesHeight : la hauteur pass�e en param�tre (h), ou 1 si le param�tre est inf�rieur � 1.</li>
	 * <li>terrainID : l'identifiant pass� en param�tre (terrainID), ou "" si le param�tre est null.</li>
	 * <li>carte : un nouveau tableau de Case de taille [nbCasesWidth][nbCasesHeight].</li>
	 * <li>objets : une nouvelle table (HashMap) de MapObject r�f�renc�s par des String.</li>
	 * <li>brume : une nouvelle Brume d'identifiant "res/terrain/brume.png".</li>
	 * <li>nbObjets : une nouvelle table (HashMap) d'Integer r�f�renc�s par des String.</li>
	 * <li>anims : une nouvelle table (HashMap) d'ArrayList[Animation] r�f�renc�s par des String (construite dans la m�thode
	 * initialize(LoadingScreen) de cette classe).</li>
	 * </ul></p>
	 * 
	 * @param w
	 * 		Le nombre de cases en largeur.
	 * @param h
	 * 		Le nombre de cases en hauteur.
	 * @param terrainID
	 * 		Le nom du fichier texture du terrain de cette carte.
	 */
	public Map(int w, int h, String terrainID) {
		
		this.nbCasesWidth = Math.max(w, 1);
		this.nbCasesHeight = Math.max(h, 1);
		if (terrainID != null)
			this.terrainID = terrainID;
		else
			this.terrainID = "";
		this.carte = new Case[this.nbCasesWidth][this.nbCasesHeight];
		this.objets = new HashMap<String, MapObject>();
		this.nbObjets = new HashMap<String, Integer>();
		this.anims = new HashMap<String, ArrayList<Animation>>();
		
	}
	
	/**
	 * Ajoute un objet � cette carte.
	 * 
	 * <p>Cette m�thode ajoute l'objet � la liste des objets de cette map.</p>
	 * <p>Elle met � jour la table nbObjets de cette classe. Si aucun objet portant le m�me nom (sans les
	 * coordonn�es) n'est d�j� pr�sent dans le jeu (test r�alis� gr�ce � la table nbObjets), alors les animations
	 * correspondant � cet objet sont charg�es dans la table anims, gr�ce au fichier portant le m�me nom (en minuscules)
	 * que cet objet. Si, au contraire, un objet du m�me type est d�j� pr�sent, l'objet se voit attribuer les animations
	 * stock�es dans la table anims � la cl� du nom de l'objet. Ce nom simplifi� de l'objet est �gal � l'ensemble des
	 * caract�res alphanum�riques lus depuis le d�but du nom. D�s qu'un espace, ou un caract�re autre quelconque
	 * (une parenth�se par exemple pour les coordonn�es) est rencontr�, alors le nom est tronqu�.</p>
	 * <p>Si le nom de cet objet est invalide, alors l'objet n'est pas ajout�.</p>
	 * 
	 * @param obj
	 * 		L'objet � ajouter � cette carte.
	 * @param readAnimFromFile
	 * 		Vrai si les animations de l'objet doivent �tre lues dans les scripts d'animations.
	 * @throws ObjectInstanciationException
	 * 		si une erreur survient lors de l'ajout des animations de l'objet � ajouter.
	 */
	public void addObject(MapObject obj, boolean readAnimFromFile) throws ObjectInstanciationException {
		
		if (obj == null)
			return;
		
		// Simplification du nom de l'objet
		
		String nameObj;
		Pattern pattern = Pattern.compile("^[A-Za-z0-9]*");
		Matcher matcher = pattern.matcher(obj.getName());
		if (matcher.find())
			nameObj = matcher.group();
		else
			return;
		
		// Ajout des animations de l'objet si demand�
		
		if (readAnimFromFile) {
			if (this.anims.get(nameObj) == null) {
				String animFile = "res/scripts/animations/map/"+nameObj.toLowerCase()+".txt";
				try {
					ArrayList<Animation> anim = Animation.createAnimationFromFile(animFile);
					this.anims.put(nameObj, anim);
					for (Animation a : anim) {
						obj.addAnimation(a.clone());
					}
				} catch (Exception e) {
					throw new ObjectInstanciationException("erreur lors de l'ajout des animations.", obj.getName(), e);
				}
			} else {
				for (Animation a : this.anims.get(nameObj)) {
					obj.addAnimation(a.clone());
				}
			}
		}
		
		// Mise � jour de la table nbObjets
		
		Integer n = this.nbObjets.get(nameObj);
		if (n == null)
			n = new Integer(0);
		this.nbObjets.put(nameObj, new Integer(n.intValue()+1));
		
		// Ajout de l'objet � cette carte
		
		this.objets.put(obj.getName(), obj);
		
		// Mise � jour de la liste des occupants des cases de la carte
		
		obj.refreshCells();
		
	}
	
	/**
	 * Supprime un objet de cette carte.
	 * 
	 * <p>Cette m�thode met � jour la table nbObjets de cette classe.</p>
	 * 
	 * @param name
	 * 		Le nom de l'objet � supprimer.
	 */
	public void removeObject(String name) {
		
		if (name == null)
			return;
		MapObject obj = this.objets.get(name);
		
		// Suppression de l'objet de la carte
		
		ArrayList<Case> cells = obj.cells(false);
		
		if (cells != null) {
			for (Case cell : cells) {
				if (cell != null)
					cell.removeOccupant(obj);
			}
		}
		this.objets.remove(name);
		
		// Simplification du nom de l'objet
		
		String nameObj;
		Pattern pattern = Pattern.compile("^[A-Za-z0-9]*");
		Matcher matcher = pattern.matcher(name);
		if (matcher.find())
			nameObj = matcher.group();
		else
			return;
		
		// Mise � jour du champ correspondant dans la table nbObjets
		
		Integer n = this.nbObjets.get(nameObj);
		if (n == null)
			n = new Integer(0);
		this.nbObjets.put(nameObj, new Integer(n.intValue()-1));
		
	}
	
	/**
	 * Retourne le nombre de cases de cette carte en largeur.
	 * 
	 * @return
	 * 		Le nombre de cases de cette carte en largeur.
	 */
	public int getNbCasesWidth() {
		return this.nbCasesWidth;
	}
	
	/**
	 * Retourne le nombre de cases de cette carte en hauteur.
	 * 
	 * @return
	 * 		Le nombre de cases de cette carte en hauteur.
	 */
	public int getNbCasesHeight() {
		return this.nbCasesHeight;
	}
	
	/**
	 * Remplace la case aux indices sp�cifi�s par la case pass�e en param�tre.
	 * 
	 * <p>Si les indices ne sont pas valides (hors tableau), ou si la case pass�e en param�tre est null, la case n'est pas
	 * modifi�e.</p>
	 * 
	 * @param x
	 * 		L'indice de la colonne de la case � remplacer.
	 * @param y
	 * 		L'indice de la ligne de la case � remplacer.
	 * @param cell
	 * 		La case de remplacement.
	 */
	public void replaceCell(int x, int y, Case cell) {
		
		if (x < 0 || x >= this.carte.length || y < 0 || y >= this.carte[x].length)
			return;
		if (cell == null)
			return;
		this.carte[x][y] = cell;
		
	}
	
	/**
	 * Retourne la case de cette carte situ�e aux indices sp�cifi�s.
	 * 
	 * @param x
	 * 		L'indice de la colonne de la case.
	 * @param y
	 * 		L'indice de la ligne de la case.
	 * @return
	 * 		Le case situ�e aux coordonn�es indiqu�es. Si l'indice n'est pas valide (hors tableau), null est retourn�.
	 */
	public Case cell(int x, int y) {
		
		if (x < 0 || x >= this.carte.length || y < 0 || y >= this.carte[x].length)
			return null;
		return this.carte[x][y];
		
	}
	
	/**
	 * Renvoie une liste (ArrayList) des cases de cette carte.
	 * 
	 * <p>Les instances de Case dans cette liste sont les originales.</p>
	 * 
	 * @return
	 * 		La liste des cases de cette carte.
	 */
	public ArrayList<Case> cells() {
		
		ArrayList<Case> cells = new ArrayList<Case>();
		for (Case[] cs : this.carte) {
			for (Case c : cs)
				cells.add(c);
		}
		return cells;
		
	}

	/**
	 * Retourne une copie de la table des objets de cette carte.
	 * 
	 * <p>La table retourn�e est une copie mais les instances de MapObject sont les originales.</p>
	 * 
	 * @return
	 * 		Une copie de la table des objets de cette carte.
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, MapObject> objets() {
		return (HashMap<String, MapObject>) this.objets.clone();
	}

	/**
	 * Retourne le fichier texture du terrain de cette carte.
	 * 
	 * @return
	 * 		Le fichier texture du terrain de cette carte.
	 */
	public String getTerrainID() {
		return this.terrainID;
	}
	
}
