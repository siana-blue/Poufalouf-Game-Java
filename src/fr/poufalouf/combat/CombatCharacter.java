package fr.poufalouf.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.UpdateException;
import fr.poufalouf.game.GameCharacter;
import fr.poufalouf.tools.Characteristic;
import fr.poufalouf.tools.Orientation;
import fr.poufalouf.tools.Rectangle;

/**
 * Personnage combattant dans un �cran de combat
 * 
 * <p>Cet objet est li� � une instance de GameCharacter.</p>
 * 
 * @author Anaïs Vernet
 */
public class CombatCharacter extends CombatObject {
	
	/**
	 * Le personnage associ� � cet objet.
	 */
	private GameCharacter character;
	/**
	 * La table des attaques de ce personnage, rep�r�es par leurs noms.
	 */
	private HashMap<String, Attack> attacks;
	/**
	 * La table des objets poss�d�s par ce personnage (les quantit�s correspondantes sont sp�cifi�es dans la classe
	 * GameCharacter).
	 */
	private HashMap<String, CombatItem> items;
	/**
	 * Le nom de l'attaque en cours de lancement.
	 */
	private String currentAttack;
	/**
	 * Le nom de l'objet en cours d'utilisation.
	 */
	private String currentItemUse;
	/**
	 * L'adversaire de ce personnage.
	 */
	private CombatCharacter opponent;
	/**
	 * L'abscisse relative � ce personnage de son coeur.
	 */
	private double heartCenterX;
	/**
	 * L'ordonn�e relative � ce personnage de son coeur.
	 */
	private double heartCenterY;
	/**
	 * L'abscisse relative � ce personnage de son organe de tir.
	 */
	private double shootCenterX;
	/**
	 * L'ordonn�e relative � ce personnage de son organe de tir.
	 */
	private double shootCenterY;
	
	/**
	 * Constructeur CombatCharacter.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Apr�s initialisation, l'orientation de cet objet prend la valeur pass�e en param�tre (or).</p>
	 * <p>Le plan de perspective de ce personnage est son ordonn�e.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>character : le personnage pass� en param�tre (character).</li>
	 * <li>attacks : une nouvelle table (HashMap) d'Attack rep�r�es par des String.</li>
	 * <li>items : une nouvelle table (HashMap) d'CombatItem rep�r�s par des String.</li>
	 * <li>currentAttack : "".</li>
	 * <li>currentItemUse : "".</li>
	 * <li>opponent : this.</li>
	 * <li>heartCenterX : 0.5.</li>
	 * <li>heartCenterY : 0.5.</li>
	 * <li>shootCenterX : 0.5.</li>
	 * <li>shootCenterY : 0.5.</li>
	 * </ul></p>
	 * 
	 * @param character
	 * 		Le personnage associ� � cet objet.
	 * @param name
	 * 		Le nom de ce personnage.
	 * @param or
	 * 		L'orientation de ce personnage.
	 * @param rect
	 * 		Le rectangle des coordonn�es de ce personnage par rapport � l'�cran de combat.
	 * @throws ObjectInstanciationException
	 * 		si le personnage pass� en param�tre est null.
	 */
	public CombatCharacter(GameCharacter character, String name, Orientation or, Rectangle rect)
	throws ObjectInstanciationException {
		
		super(name, rect);
		
		if (character == null)
			throw new ObjectInstanciationException("Les donn�es associ�es � ce personnage (GameCharacter) ne sont pas " +
					"d�finies.", this.getName());
		this.character = character;
		this.attacks = new HashMap<String, Attack>();
		this.items = new HashMap<String, CombatItem>();
		this.currentAttack = "";
		this.currentItemUse = "";
		this.opponent = this;
		this.heartCenterX = 0.5;
		this.heartCenterY = 0.5;
		this.shootCenterX = 0.5;
		this.shootCenterY = 0.5;
		
		if (or != null)
			this.setOrientation(or);
		else
			this.setOrientation(Orientation.AUCUNE);
		this.setPlan((int) this.getY()); // Attention, le plan est red�fini lors de l'initialisation de Combat.
		
	}
	
	/**
	 * Lance l'attaque dont le nom est pass� en param�tre.
	 * 
	 * <p>Le champ currentAttack de cette classe prend le nom pass� en param�tre, et la m�thode use() de la classe
	 * Move est appel�e.</p>
	 * 
	 * @param name
	 * 		Le nom de l'attaque � lancer. S'il est null ou ne correspond � aucune attaque de ce personnage, rien n'est fait.
	 */
	public void useAttack(String name) {
		
		if (!this.attacks.containsKey(name))
			return;
		this.currentAttack = name;
		this.currentAttack().use();
		
	}
	
	/**
	 * Utilise l'objet dont le nom est pass� en param�tre.
	 * 
	 * <p>Le champ currentItemUse de cette classe prend le nom pass� en param�tre, et la m�thode use() de la classe
	 * Move est appel�e. Ce personnage perd un exemplaire de l'objet utilis�.</p>
	 * 
	 * @param name
	 * 		Le nom de l'objet � utiliser. S'il est null ou ne correspond � aucun objet poss�d� par ce personnage, rien
	 * n'est fait.
	 */
	public void useItem(String name) {
		
		if (!this.items.containsKey(name))
			return;
		this.currentItemUse = name;
		this.currentItemUse().use();
		
	}
	
	/**
	 * Joue le comportement de ce personnage.
	 * 
	 * <p>Cette m�thode appelle la m�thode de la classe m�re, puis incr�mente la frame de l'animation de l'attaque
	 * en cours, si elle n'est pas null. Si cette attaque a un timer nul, alors l'attaque en cours devient "".</p>
	 * <p>L'attaque ou l'utilisation d'objet en cours pour ce personnage, s'il y en a une, est mise � jour.</p>
	 * <p>La liste des objets de ce personnage est mise � jour en fonction de celle du GameCharacter qui lui est li�.</p>
	 * 
	 * @throws UpdateException
	 * 		si l'ajout d'un objet lors de la mise � jour de la liste l�ve une exception.
	 */
	@Override
	public void playBehavior() throws UpdateException {
		
		super.playBehavior();
		
		Attack a = this.currentAttack();
		CombatItem it = this.currentItemUse();
		
		if (a != null) {
			a.update();
			if (a.getAnim().currentAnim().getTimer() == 0) {
				this.currentAttack = "";
			}
		}
		
		if (it != null) {
			it.update();
			if (it.getAnim().currentAnim().getTimer() == 0) {
				this.changeItemQuantity(this.currentItemUse, this.itemQuantity(this.currentItemUse)-1);
				this.currentItemUse = "";
			}
		}
		
		HashMap<String, CombatItem> its = this.items;
		ArrayList<CombatItem> i = new ArrayList<CombatItem>();
		ArrayList<String> n = new ArrayList<String>();
		this.items = new HashMap<String, CombatItem>();
		Set<String> set = this.character.getObjects().keySet();
		for (String s : set) {
			CombatItem c = its.get(s);
			if (c == null) {
				n.add(s);
				try {
					i = CombatItem.createCombatItemsFromFile(this, this.opponent, n);
				} catch (Exception e) {
					throw new UpdateException("Erreur lors de l'ajout de l'objet "+n.get(0), e);
				}
				if (i.size() > 0)
					this.items.put(s, i.get(0));
			} else {
				this.items.put(s, c);
			}
		}
		
	}
	
	/**
	 * Retourne l'attaque dont le nom est pass� en param�tre.
	 * 
	 * @param name
	 * 		Le nom de l'attaque.
	 * @return
	 * 		L'attaque demand�e, ou null si le nom ne correspond � aucune attaque.
	 */
	public Attack attack(String name) {
		
		return this.attacks.get(name);
		
	}
	
	/**
	 * Retourne la liste des noms des attaques de ce personnage.
	 * 
	 * @return
	 * 		La liste des noms des attaques.
	 */
	public ArrayList<String> attacks() {
		
		ArrayList<String> strs = new ArrayList<String>();
		for (Attack a : this.attacks.values())
			if (a != null)
				strs.add(a.getName());
		
		return strs;
			
	}
	
	/**
	 * Retourne la liste des objets poss�d�s par ce personnage.
	 * 
	 * @return
	 * 		La liste des objets.
	 */
	public ArrayList<CombatItem> items() {
		
		ArrayList<CombatItem> its = new ArrayList<CombatItem>();
		for (CombatItem it : this.items.values())
			if (it != null)
				its.add(it);
		
		return its;
		
	}
	
	/**
	 * Modifie la quantit� d'objets dont le nom est pass� en param�tre.
	 * 
	 * <p>Si la quantit� est z�ro, alors l'objet est retir� de la liste des objets.</p>
	 * 
	 * <p>Il s'agit d'un lien vers le GameCharacter de cette classe.</p>
	 * 
	 * @param itemName
	 * 		Le nom des objets dont la quantit� doit �tre modifi�e. S'il est null, rien n'est fait.
	 * @param qty
	 * 		La nouvelle quantit�.
	 */
	public void changeItemQuantity(String itemName, int qty) {
		
		this.character.changeItemQuantity(itemName, qty);
		
	}
	
	/**
	 * Retourne le nombre d'objets du nom pass� en param�tre poss�d�s par ce personnage.
	 * 
	 * @param itemName
	 * 		Le nom de l'objet.
	 * @return
	 * 		La quantit� poss�d�e.
	 */
	public int itemQuantity(String itemName) {
		
		Integer i = this.character.getObjects().get(itemName);
		return (i==null)?0:i.intValue();
		
	}
	
	/**
	 * Retourne l'attaque en cours de lancement, ou null si aucune attaque n'est en cours.
	 * 
	 * @return
	 * 		L'attaque lanc�e.
	 */
	public Attack currentAttack() {
		
		return this.attacks.get(this.currentAttack);
		
	}
	
	/**
	 * Retourne l'objet en cours d'utilisation, ou null si aucun objet n'est utilis�.
	 * 
	 * @return
	 * 		L'objet utilis�.
	 */
	public CombatItem currentItemUse() {
		
		return this.items.get(this.currentItemUse);
		
	}
	
	/**
	 * Indique si ce personnage est en train de lancer une action.
	 * 
	 * @return
	 * 		Vrai si ce personnage lance une action.
	 */
	public boolean isActing() {
		
		return (this.currentAttack() != null || this.currentItemUse() != null);
		
	}
	
	/**
	 * Retourne la caract�ristique de ce personnage sp�cifi�e en param�tre.
	 * 
	 * <p>Il s'agit d'un lien vers le GameCharacter de cette classe.</p>
	 * 
	 * @param c
	 * 		Le type de caract�ristique � retourner.
	 * @return
	 * 		La caract�ristique demand�e, ou null si cette caract�ristique n'est pas renseign�e.
	 */
	public int characteristic(Characteristic c) {
		
		return this.character.characteristic(c);
		
	}
	
	/**
	 * Modifie une caract�ristique de ce personnage, ou l'ajoute si elle n'est pas renseign�e.
	 * 
	 * <p>Il s'agit d'un lien vers le GameCharacter de cette classe.</p>
	 * 
	 * @param c
	 * 		La caract�ristique � modifier.
	 * @param value
	 * 		La nouvelle valeur de la caract�ristique.
	 */
	public void changeCharacteristic(Characteristic c, int value) {
		
		this.character.changeCharacteristic(c, value);
		
	}
	
	/**
	 * Ajoute une attaque � la liste des attaques de ce personnage.
	 * 
	 * @param a
	 * 		L'attaque � ajouter. Si elle est null, rien n'est fait.
	 */
	public void addAttack(Attack a) {
		
		if (a != null)
			this.attacks.put(a.getName(), a);
		
	}
	
	/**
	 * Ajoute un objet � la liste des objets de ce personnage.
	 * 
	 * @param it
	 * 		L'objet � ajouter. S'il est null, rien n'est fait.
	 */
	public void addCombatItem(CombatItem it) {
		
		if (it != null)
			this.items.put(it.getName(), it);
		
	}
	
	/**
	 * Met � jour l'adversaire de ce personnage.
	 * 
	 * @param opponent
	 * 		L'adversaire de ce personnage.
	 */
	public void setOpponent(CombatCharacter opponent) {
		if (opponent != null)
			this.opponent = opponent;
	}
	
	/**
	 * Retourne l'adversaire de ce personnage.
	 * 
	 * @return
	 *		L'adversaire de ce personnage.
	 */
	public CombatCharacter getOpponent() {
		return this.opponent;
	}

	/**
	 * Modifie l'abscisse du coeur de ce personnage.
	 * 
	 * @param heartCenterX
	 * 		L'abscisse du coeur de ce personnage.
	 */
	public void setHeartCenterX(double heartCenterX) {
		this.heartCenterX = heartCenterX;
	}

	/**
	 * Retourne l'abscisse du coeur de ce personnage.
	 * 
	 * @return
	 * 		L'abscisse du coeur de ce personnage.
	 */
	public double getHeartCenterX() {
		return this.heartCenterX;
	}

	/**
	 * Modifie l'ordonn�e du coeur de ce personnage.
	 * 
	 * @param heartCenterY
	 * 		L'ordonn�e du coeur de ce personnage.
	 */
	public void setHeartCentreY(double heartCenterY) {
		this.heartCenterY = heartCenterY;
	}

	/**
	 * Retourne l'ordonn�e du coeur de ce personnage.
	 * 
	 * @return
	 * 		L'ordonn�e du coeur de ce personnage.
	 */
	public double getHeartCenterY() {
		return this.heartCenterY;
	}
	
	/**
	 * Modifie l'abscisse de l'organe de tir de ce personnage.
	 * 
	 * @param shootCenterX
	 * 		L'abscisse de l'organe de tir de ce personnage.
	 */
	public void setShootCenterX(double shootCenterX) {
		this.shootCenterX = shootCenterX;
	}

	/**
	 * Retourne l'abscisse de l'organe de tir de ce personnage.
	 * 
	 * @return
	 * 		L'abscisse de l'organe de tir de ce personnage.
	 */
	public double getShootCenterX() {
		return this.shootCenterX;
	}

	/**
	 * Modifie l'ordonn�e de l'organe de tir de ce personnage.
	 * 
	 * @param shootCenterY
	 * 		L'ordonn�e de l'organe de tir de ce personnage.
	 */
	public void setShootCenterY(double shootCenterY) {
		this.shootCenterY = shootCenterY;
	}

	/**
	 * Retourne l'ordonn�e de l'organe de tir de ce personnage.
	 * 
	 * @return
	 * 		L'ordonn�e de l'organe de tir de ce personnage.
	 */
	public double getShootCenterY() {
		return this.shootCenterY;
	}

}
