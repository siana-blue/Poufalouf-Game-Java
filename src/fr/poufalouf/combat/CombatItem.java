package fr.poufalouf.combat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.ScriptReader;

/**
 * Objet utilis� en combat
 * 
 * @author Anaïs Vernet
 */
public class CombatItem extends Move {
	
	/**
	 * Le nom du fichier texture de cet objet.
	 */
	private String textureFile;
	
	/**
	 * Constructeur CombatItem.
	 * 
	 * <p>Ce constructeur appelle le constructeur 1 (copie) de la classe m�re.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>textureFile : le nom pass� en param�tre (textureFile), ou une "" si le param�tre est null.</li>
	 * </ul></p>
	 * 
	 * @param move
	 * 		L'action servant de mod�le pour cr�er cet objet.
	 * @param textureFile
	 * 		Le nom du fichier texture de cet objet.
	 * @throws ObjectInstanciationException
	 * 		si le constructeur de la classe m�re l�ve une ObjectInstanciationException.
	 */
	public CombatItem(Move move, String textureFile)
	throws ObjectInstanciationException {
		
		super(move);
		
		if (textureFile != null)
			this.textureFile = textureFile;
		else
			this.textureFile = "";
		
	}
	
	/**
	 * <p>Retourne une liste de CombatItem cr��s � partir du fichier dont le nom est donn� par le champ listItemsFileName
	 * de la classe Constantes. Cette m�thode appelle la m�thode createMovesFromFile de la classe Move.</p>
	 * <p>Apr�s avoir cr�� la liste de Move, cette classe lit le flag IMG du script, pour associer � cet objet une ic�ne.
	 * Le champ textureFile de cette classe prend la valeur de la ligne lue � ce flag.</p>
	 * 
	 * @param user
	 * 		Le propri�taire de cet objet.
	 * @param opponent
	 * 		L'adversaire du propri�taire de cet objet.
	 * @param names
	 * 		La liste des noms des objets � lire dans le fichier listCombatItemsFileName.
	 * @return
	 * 		La liste des objets cr��s.
	 * @throws IOException
	 * 		si la m�thode createMovesFromFile de la classe Move l�ve une IOException.
	 * @throws ScriptException
	 * 		si le script n'est pas valide, ou si la m�thode createMovesFromFile de la classe Move l�ve une ScriptException.
	 * @throws IllegalArgumentException
	 * 		si la m�thode createMovesFromFile de la classe Move l�ve une IllegalArgumentException.
	 * @throws ObjectInstanciationException
	 * 		si la m�thode createMovesFromFile de la classe Move l�ve une ObjectInstanciationException.
	 */
	public static ArrayList<CombatItem> createCombatItemsFromFile(CombatCharacter user, CombatCharacter opponent,
			ArrayList<String> names)
	throws IOException, ScriptException, IllegalArgumentException, ObjectInstanciationException {
		
		ArrayList<CombatItem> items = new ArrayList<CombatItem>();
		ArrayList<Move> moves = Move.createMovesFromFile(Constantes.listItemsFileName, user, opponent, names);
		
		String s;
		ArrayList<String> lines;
		String line;
		Pattern pttn;
		Matcher mtch;
		String texture;

		for (int i=0;i<names.size();i++) {
			
			s = names.get(i);
			
			// IMG
			
			lines = ScriptReader.readSectionAtFlag(Constantes.listItemsFileName, s, "IMG");
			if (lines.size() == 0)
				throw new ScriptException("Objet "+s+". Bloc $IMG : le bloc n'est pas complet.");
			line = lines.get(0);
			pttn = Pattern.compile("^[a-zA-Z][a-zA-Z0-9/_]*\\.?[a-zA-Z0-9]*$");
			mtch = pttn.matcher(line);
			if (!mtch.find())
				throw new ScriptException("Objet "+s+". Bloc $IMG : le nom du fichier texture n'est pas valide.");
			texture = line;
			
			items.add(new CombatItem(moves.get(i), texture));
			
		}
		
		return items;
		
	}
	
	/**
	 * Retourne le nom du fichier texture de cet objet.
	 * 
	 * @return
	 * 		Le nom du fichier texture.
	 */
	public String getTextureFile() {
		return this.textureFile;
	}

}
