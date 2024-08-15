package fr.poufalouf.combat;

import java.util.ArrayList;
import java.util.HashMap;

import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.tools.Animation;
import fr.poufalouf.tools.Constantes;

/**
 * Classe g�rant le combat
 * 
 * @author Anaïs Vernet
 */
public class Combat {
	
	/**
	 * La table des objets de ce combat. Les cl�s sont leurs noms.
	 */
	private HashMap<String, CombatObject> objets;
	/**
	 * Le nom du personnage du joueur.
	 */
	private String playerName;
	/**
	 * Le nom du personnage de l'adversaire.
	 */
	private String opponentName;
	
	/**
	 * Constructeur Combat.
	 * 
	 * <p>Apr�s initialisation, ce constructeur associe les animations n�cessaires aux deux personnages gr�ce �
	 * la m�thode createAnimationFromFile(String) de la classe Animation. Il ajoute les personnages � la liste
	 * des objets de ce combat.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>objets : une nouvelle table (HashMap) de CombatObject rep�r�s par des String.</li>
	 * <li>playerName : le nom du personnage player.</li>
	 * <li>opponentName : le nom du personnage opponent.</li>
	 * </ul></p>
	 * 
	 * @param player
	 * 		Le personnage contr�l� par le joueur dans ce combat.
	 * @param opponent
	 * 		Le personnage contr�l� par l'adversaire dans ce combat.
	 * @throws ObjectInstanciationException
	 * 		si une erreur survient lors de l'ajout des animations des personnages, ou si les personnages pass�s en
	 * param�tres sont null.
	 */
	public Combat(CombatCharacter player, CombatCharacter opponent)
		throws ObjectInstanciationException {
		
		this.objets = new HashMap<String, CombatObject>();
		if (player == null || opponent == null) {
			this.playerName = "";
			this.opponentName = "";
			throw new ObjectInstanciationException("les personnages de ce combat ne sont pas d�finis.", "");
		}
		this.playerName = player.getName();
		this.opponentName = opponent.getName();
		
		try {
			ArrayList<Animation> anim = Animation.createAnimationFromFile("res/scripts/animations/combat/"
					+player.getName().toLowerCase()+".txt");
			for (Animation a : anim) {
				if (a != null)
					player.addAnimation(a);
			}
		} catch (Exception e) {
			throw new ObjectInstanciationException("erreur lors de l'ajout des animations.", player.getName(), e);
		}
		try {
			ArrayList<Animation> anim = Animation.createAnimationFromFile("res/scripts/animations/combat/"
					+opponent.getName().toLowerCase()+".txt");
			for (Animation a : anim) {
				if (a != null)
					opponent.addAnimation(a);
			}
		} catch (Exception e) {
			throw new ObjectInstanciationException("erreur lors de l'ajout des animations.", opponent.getName(), e);
		}
		
		this.addObject(player);
		this.addObject(opponent);
		if (this.player().getY() > this.opponent().getY()) {
			this.player().setPlan(Constantes.downCharacterPlan);
			this.opponent().setPlan(Constantes.upCharacterPlan);
		} else {
			this.player().setPlan(Constantes.upCharacterPlan);
			this.opponent().setPlan(Constantes.downCharacterPlan);
		}
		
	}
	
	/**
	 * Ajoute un objet � la liste des objets de cette classe.
	 * 
	 * <p>Si l'objet est un d�cor, il se voit attribu� comme plan le num�ro suivant celui de l'objet pr�c�demment
	 * ajout�.</p>
	 * 
	 * @param objet
	 * 		L'objet � ajouter.
	 */
	public void addObject(CombatObject objet) {
		
		if (objet != null) {
			if (objet instanceof CombatDecor)
				objet.setPlan(this.objets.size());
			this.objets.put(objet.getName(), objet);
		}
		
	}
	
	/**
	 * Supprime un objet de la liste des objets de cette classe.
	 * 
	 * @param objet
	 * 		L'objet � supprimer du combat.
	 */
	public void removeObject(CombatObject objet) {
		
		this.objets.remove(objet.getName());
		
	}
	
	/**
	 * Retourne une copie de la table des objets de ce combat.
	 * 
	 * @return
	 * 		Une copie de la table des objets.
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, CombatObject> objets() {
		
		return (HashMap<String, CombatObject>) this.objets.clone();
		
	}
	
	/**
	 * Retourne le personnage jou� par le joueur.
	 * 
	 * <p>null peut �tre retourn� si un objet du m�me nom que celui de ce personnage a remplac� celui-ci dans la table
	 * des objets.</p>
	 * 
	 * @return
	 * 		Le personnage du joueur.
	 */
	public CombatCharacter player() {
		
		CombatObject o = this.objets.get(this.playerName);
		
		if (o instanceof CombatCharacter)
			return (CombatCharacter) o;
		
		return null;
		
	}
	
	/**
	 * Retourne le personnage jou� par l'adversaire.
	 * 
	 * <p>null peut �tre retourn� si un objet du m�me nom que celui de ce personnage a remplac� celui-ci dans la table
	 * des objets.</p>
	 * 
	 * @return
	 * 		Le personnage de l'adversaire.
	 */
	public CombatCharacter opponent() {
		
		CombatObject o = this.objets.get(this.opponentName);
		
		if (o instanceof CombatCharacter)
			return (CombatCharacter) o;
		
		return null;
		
	}
	
}
