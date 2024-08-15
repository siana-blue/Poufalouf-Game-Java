package fr.poufalouf.combat;

import java.io.IOException;
import java.util.ArrayList;
import javax.script.ScriptException;

import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.tools.Characteristic;
import fr.poufalouf.tools.Constantes;

/**
 * Attaque en combat
 * 
 * <p>Une attaque est d�termin�e par une table d'effets : il s'agit d'entiers (positifs ou n�gatifs), li�s aux
 * caract�ristiques que cette attaque modifie.</p>
 * 
 * @author Anaïs Vernet
 */
public class Attack extends Move {
	
	/**
	 * Constructeur Attack.
	 * 
	 * <p>Ce constructeur appelle le constructeur 1 (copie) de la classe m�re.</p>
	 * 
	 * @param move
	 * 		L'action servant de mod�le pour cr�er cette attaque.
	 * @throws ObjectInstanciationException
	 * 		si le lanceur pass� en param�tre est null (attacker).
	 */
	public Attack(Move move)
	throws ObjectInstanciationException {
		
		super(move);
		
	}
	
	/**
	 * <p>Retourne une liste d'Attack cr��es � partir du fichier dont le nom est donn� par le champ listAttacksFileName
	 * de la classe Constantes. Cette m�thode appelle la m�thode createMovesFromFile de la classe Move.</p>
	 * 
	 * @param user
	 * 		Le lanceur des attaques.
	 * @param opponent
	 * 		L'adversaire du lanceur.
	 * @param names
	 * 		La liste des noms des attaques � lire dans le fichier listAttacksFileName.
	 * @return
	 * 		La liste des attaques cr��es.
	 * @throws IOException
	 * 		si la m�thode createMovesFromFile de la classe Move l�ve une IOException.
	 * @throws ScriptException
	 * 		si la m�thode createMovesFromFile de la classe Move l�ve une ScriptException.
	 * @throws IllegalArgumentException
	 * 		si la m�thode createMovesFromFile de la classe Move l�ve une IllegalArgumentException.
	 * @throws ObjectInstanciationException
	 * 		si la m�thode createMovesFromFile de la classe Move l�ve une ObjectInstanciationException.
	 */
	public static ArrayList<Attack> createAttacksFromFile(CombatCharacter user, CombatCharacter opponent,
			ArrayList<String> names)
			throws IOException, ScriptException, IllegalArgumentException, ObjectInstanciationException {
		
		ArrayList<Attack> attacks = new ArrayList<Attack>();
		ArrayList<Move> moves = Move.createMovesFromFile(Constantes.listAttacksFileName, user, opponent, names);
		for (Move m : moves) {
			attacks.add(new Attack(m));
		}
		
		return attacks;
		
	}
	
	/**
	 * Indique si cette attaque peut �tre lanc�e.
	 * 
	 * <p>Si le lanceur ou la cible pass�s en param�tre sont null, alors faux est retourn�. Sinon, un nombre al�atoire
	 * est tir� entre 1 et la somme de la pr�cision du lanceur et de l'esquive de la cible. Si ce nombre est sup�rieur
	 * � la pr�cision du lanceur, alors faux est retourn�. Sinon, vrai est retourn�. Si la pr�cision ou l'esquive ne
	 * sont pas renseign�es, alors 0 leur est attribu�.</p>
	 * 
	 * @param lanceur
	 * 		Le lanceur de cette attaque.
	 * @param cible
	 * 		La cible de cette attaque.
	 * @return
	 * 		Vrai si cette attaque peut �tre lanc�e.
	 */
	public static boolean succeed(CombatCharacter lanceur, CombatCharacter cible) {
		
		if (lanceur == null || cible == null)
			return false;
		int precision = lanceur.characteristic(Characteristic.ACCURACY);
		int esquive = cible.characteristic(Characteristic.EVASION);
		int hsd = (int) (Math.random()*(precision+esquive))+1;
		System.out.println("ATTAQUE de "+lanceur.getName());
		System.out.println(lanceur.getName()+" : pr�cision "+precision);
		System.out.println(cible.getName()+" : esquive "+esquive);
		System.out.println("Nombre tir� : "+hsd);
		if (hsd > precision)
			return false;
		
		return true;
		
	}

}
