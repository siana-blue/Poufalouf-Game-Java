package fr.poufalouf.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.tools.Characteristic;
import fr.poufalouf.tools.ScriptReader;

/**
 * Personnage du jeu
 * 
 * <p>Cette classe contient les informations n�cessaires au passage des donn�es d'un personnage d'un mod�le � un autre.</p>
 * 
 * @author Anaïs Vernet
 */
public class GameCharacter {
	
	/**
	 * Le nom de ce personnage.
	 */
	private String name;
	/**
	 * Les caract�ristiques de ce personnage.
	 */
	private HashMap<Characteristic, Integer> characteristics;
	/**
	 * La liste des attaques de ce personnage (par leurs noms).
	 */
	private ArrayList<String> attacksNames;
	/**
	 * Les objets poss�d�s par ce personnage, leurs quantit�s rep�r�es par leurs noms.
	 */
	private HashMap<String, Integer> objects;
	
	/**
	 * Constructeur GameCharacter.
	 * 
	 * <p>Apr�s initialisation, les propri�t�s de ce personnage sont initialis�es gr�ce au fichier
	 * dont le nom est pass� en param�tre (m�thode setPropertiesFromFile(String) de cette classe.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>name : le nom pass� en param�tre (name), ou "" si le param�tre est null.</li>
	 * <li>characteristics : une nouvelle table (HashMap) d'Integer rep�r�s par des Characteristic.</li>
	 * <li>attakcsNames : une nouvelle liste (ArrayList) de String.</li>
	 * <li>objects : une nouvelle table (HashMap) d'Integer rep�r�s par des String.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de ce param�tre.
	 * @param file
	 * 		Le nom du fichier des caract�ristiques de ce personnage.
	 * @throws ObjectInstanciationException
	 * 		si une erreur est lev�e par la m�thode setCharacteristicsFromFile(String).
	 */
	public GameCharacter(String name, String file) throws ObjectInstanciationException {
		
		this.name = (name==null)?"":name;
		this.characteristics = new HashMap<Characteristic, Integer>();
		this.attacksNames = new ArrayList<String>();
		this.objects = new HashMap<String, Integer>();
		
		try{
			this.setPropertiesFromFile(file);
		} catch (Exception e) {
			throw new ObjectInstanciationException("Erreur constructeur.", this.name, e);
		}
		
	}
	
	/**
	 * Met � jour la table des propri�t�s de ce personnage d'apr�s le fichier dont le nom est pass� en param�tre.
	 * 
	 * <p>Le fichier doit �tre de la forme suivante :
	 * <ul>
	 * <li>Une ligne de nombres s�par�s par des virgules, dans l'ordre de l'�num�ration de la classe Characteristics,
	 * d�finit les caract�ristiques de ce personnage.</li>
	 * <li>La ligne suivante contient le nom des attaques de ce personnage, s�par�s par des virgules.</li>
	 * <li>La ligne suivante contient le nom des objets poss�d�s au d�part par ce personnage, s�par�s par des virgules,
	 * en alternance avec leurs quantit�s. Exemple : POTION,2,BOMBE,1. Cette ligne peut �tre vide (auquel cas il faut
	 * bien la sauter (la laisser vide) dans le fichier texte).</li>
	 * </ul></p>
	 * 
	 * @param fileName
	 * 		Le nom du fichier pass� en param�tre.
	 * @throws IOException
	 * 		si une erreur survient lors de la lecture du fichier.
	 * @throws ScriptException
	 * 		si le script n'est pas correctement renseign�.
	 * @throws Exception
	 * 		si une erreur survient lors de la cr�ation d'un pattern.
	 */
	public void setPropertiesFromFile(String fileName) throws IOException, ScriptException, Exception {
		
		ArrayList<String> lines;
		String line = null;
		Pattern pattern = Pattern.compile("^\\d+(,\\d+){"+(Characteristic.values().length-1)+"}$");
		
		lines = ScriptReader.readAll(fileName);
		if (lines.size() == 0)
			throw new ScriptException("Erreur dans le script personnage "+fileName+" : le fichier est vide.");
		// Caract�ristiques
		line = lines.get(0);
		Matcher mtc = pattern.matcher(line);
		if (!mtc.find())
			throw new ScriptException("Erreur dans le script personnage "+fileName+" : caract�ristique manquante.");
		this.characteristics.clear();
		String[] strs = line.split(",");
		for (int i=0;i<strs.length;i++) {
			this.changeCharacteristic(Characteristic.values()[i], Integer.decode(strs[i]).intValue());
		}
		
		// Attaques
		if (lines.size() < 2)
			throw new ScriptException("Erreur dans le script personnage "+fileName+" : les attaques de ce personnage ne " +
			"sont pas d�finies.");
		line = lines.get(1);
		strs = line.split(",");
		this.attacksNames.clear();
		for (int i=0;i<strs.length;i++) {
			this.attacksNames.add(strs[i]);
		}
		
		// Objets
		for (int l=2;l<lines.size();l++) {
			line = lines.get(l);
			strs = line.split(",");
			for (int i=0;i<strs.length;i+=2) {
				if (i+1 >= strs.length)
					throw new ScriptException("Erreur dans le script personnage "+fileName+" : un objet n'a pas sp�cifi� sa " +
							"quantit�.");
				try {
					this.objects.put(strs[i], Integer.decode(strs[i+1]));
				} catch (Exception e) {
					throw new ScriptException("Erreur dans le script personnage "+fileName+" : objet non valide ou quantit� " +
							"sp�cifi�e incorrecte.");
				}
			}
		}
		
	}
	
	/**
	 * Retourne le nom de ce personnage.
	 * 
	 * @return
	 * 		Le nom de ce personnage.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Retourne la caract�ristique de ce personnage sp�cifi�e en param�tre.
	 * 
	 * @param c
	 * 		Le type de caract�ristique � retourner.
	 * @return
	 * 		La caract�ristique demand�e, ou 0 si cette caract�ristique n'est pas renseign�e.
	 */
	public int characteristic(Characteristic c) {
		
		Integer i = this.characteristics.get(c);
		return (i==null)?0:this.characteristics.get(c).intValue();
		
	}
	
	/**
	 * Modifie une caract�ristique de ce personnage, ou l'ajoute si elle n'est pas renseign�e.
	 * 
	 * @param c
	 * 		La caract�ristique � modifier.
	 * @param value
	 * 		La nouvelle valeur de la caract�ristique.
	 */
	public void changeCharacteristic(Characteristic c, int value) {
		
		this.characteristics.put(c, new Integer(value));
		
	}
	
	/**
	 * Retourne une copie de la liste des noms des attaques de ce personnage.
	 * 
	 * <p>La liste est une copie, mais les instances d'Attack sont les originales.</p>
	 * 
	 * @return
	 * 		La liste des noms des attaques de ce personnage.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getAttacksNames() {
		return (ArrayList<String>) this.attacksNames.clone();
	}
	
	/**
	 * Retourne la table des quantit�s d'objets poss�d�s par ce personnage.
	 * 
	 * <p>La table est une copie, mais les instances d'Integer sont les originales.</p>
	 * 
	 * @return
	 * 		La table des quantit�s d'objets poss�d�s.
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Integer> getObjects() {
		return (HashMap<String, Integer>) this.objects.clone();
	}
	
	/**
	 * Modifie la quantit� d'objets dont le nom est pass� en param�tre.
	 * 
	 * <p>Si la quantit� est z�ro, alors l'objet est retir� de la liste des objets.</p>
	 * 
	 * @param itemName
	 * 		Le nom des objets dont la quantit� doit �tre modifi�e. S'il est null, rien n'est fait.
	 * @param qty
	 * 		La nouvelle quantit�.
	 */
	public void changeItemQuantity(String itemName, int qty) {
		
		if (itemName != null) {
			if (qty == 0) {
				this.objects.remove(itemName);
			} else
				this.objects.put(itemName, new Integer(qty));
		}
		
	}

}
