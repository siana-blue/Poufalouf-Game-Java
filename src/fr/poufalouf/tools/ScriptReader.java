package fr.poufalouf.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ensemble de m�thodes permettant de lire les �l�ments d'un script
 * 
 * <p>Dans cette classe, les m�thodes retournent des listes de cha�nes de caract�res correspondant aux lignes d'un fichier
 * script. Toutes les lignes vides (0 caract�res) sont omises.</p>
 * 
 * @author Anaïs Vernet
 */
public class ScriptReader {
	
	/**
	 * Lis un script et retourne une liste de cha�nes de caract�res correspondant aux diff�rentes lignes
	 * (s�par�es par des \n) du fichier.
	 * 
	 * @param scriptName
	 * 		Le nom du fichier script.
	 * @return
	 * 		La liste des lignes comprises dans le script.
	 * @throws IOException
	 * 		si la lecture du fichier script �choue.
	 */
	public static ArrayList<String> readAll(String scriptName) throws IOException {
		
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader bufR;
		String line;
		
		try {
			bufR = new BufferedReader(new InputStreamReader(new FileInputStream(scriptName)));
		} catch (Exception e) {
			throw new IOException("Fichier "+scriptName+" non trouvé ou non lisible.", e);
		}
		
		line = bufR.readLine();
		while (line != null) {
			
			if (line.length() == 0)
				continue;
			lines.add(line);
			line = bufR.readLine();
			
		}
		
		return lines;
		
	}
	
	/**
	 * Lis un script et en extrait une liste de cha�nes de caract�res correspondant aux diff�rentes lignes
	 * (s�par�es par des \n) de la section dont l'ordre d'apparition dans le fichier est sp�cifi� en param�tre.
	 * 
	 * <p>Une section est d�finie par un cha�ne de caract�res entre crochets ([nom de la section]).</p>
	 * <p>Si le num�ro pass� en param�tre est sup�rieur au nombre de sections dans le script, une liste vide sera
	 * retourn�e.</p>
	 * <p>Cette m�thode retourne comme premi�re cha�ne de caract�re le nom de la section, sans les crochets.</p>
	 * 
	 * @param scriptName
	 * 		Le nom du fichier script.
	 * @param index
	 * 		Le num�ro de la section (ordre d'apparition, en commen�ant � 1).
	 * @return
	 * 		La liste des lignes comprises dans la section demand�e.
	 * @throws IOException
	 * 		si la lecture du fichier script �choue.
	 */
	public static ArrayList<String> readSection(String scriptName, int index) throws IOException {
		
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader bufR;
		String line;
		int nb = 1;
		
		try {
			bufR = new BufferedReader(new InputStreamReader(new FileInputStream(scriptName)));
		} catch (Exception e) {
			throw new IOException("Fichier "+scriptName+" non trouv� ou non lisible.", e);
		}
		
		line = bufR.readLine();
		Pattern pattern = Pattern.compile("^\\[.+\\]$");
		boolean cnt = true;
		while (line != null && cnt) {
			Matcher matcher = pattern.matcher(line);
			
			if (matcher.find()) {
				if (nb == index) {
					lines.add(matcher.group().substring(1, matcher.group().length()-1));
					while ((line=bufR.readLine()) != null && cnt) {
						if (line.length() == 0)
							continue;
						if (line.charAt(0) == '[') {
							cnt = false;
							break;
						}
						lines.add(line);
					}
				} else
					nb++;
			}
			line = bufR.readLine();
		}
		
		bufR.close();
		
		return lines;
		
	}
	
	/**
	 * Lis un script et en extrait une liste de cha�nes de caract�res correspondant aux diff�rentes lignes
	 * (s�par�es par des \n) d'une section (entre crochets [nom de la section]).
	 * 
	 * @param scriptName
	 * 		Le nom du fichier script.
	 * @param section
	 * 		Le nom de la section (sans les crochets).
	 * @return
	 * 		La liste des lignes comprises dans la section demand�e.
	 * @throws IOException
	 * 		si la lecture du fichier script �choue.
	 */
	public static ArrayList<String> readSection(String scriptName, String section) throws IOException {
		
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader bufR;
		String line;
		
		try {
			bufR = new BufferedReader(new InputStreamReader(new FileInputStream(scriptName)));
		} catch (Exception e) {
			throw new IOException("Fichier "+scriptName+" non trouv� ou non lisible.", e);
		}
		
		line = bufR.readLine();
		Pattern pattern = Pattern.compile("^\\[.+\\]$");
		boolean cnt = true;
		while (line != null && cnt) {
			Matcher matcher = pattern.matcher(line);
			
			if (matcher.find()) {
				String sectionName = line.substring(1, line.length()-1);
				if (sectionName.equals(section)) {
					while ((line=bufR.readLine()) != null && cnt) {
						if (line.length() == 0)
							continue;
						if (line.charAt(0) == '[') {
							cnt = false;
							break;
						}
						lines.add(line);
					}
				}
			}
			line = bufR.readLine();
		}
		
		bufR.close();
		
		return lines;
		
	}
	
	/**
	 * Lis un script et en extrait une liste de cha�nes de caract�res correspondant aux diff�rentes lignes
	 * (s�par�es par des \n) d'une section (entre crochets [nom de la section]) � un flag pr�cis ($nom du flag).
	 * 
	 * @param scriptName
	 * 		Le nom du fichier script.
	 * @param section
	 * 		Le nom de la section (sans les crochets).
	 * @param flag
	 * 		Le nom du flag (sans le $).
	 * @return
	 * 		La liste des lignes comprises au flag de la section demand�e.
	 * @throws IOException
	 * 		si la lecture du fichier script �choue.
	 */
	public static ArrayList<String> readSectionAtFlag(String scriptName, String section, String flag)
	throws IOException {
		
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader bufR;
		String line;
		
		try {
			bufR = new BufferedReader(new InputStreamReader(new FileInputStream(scriptName)));
		} catch (Exception e) {
			throw new IOException("Fichier "+scriptName+" non trouv� ou non lisible.", e);
		}
		
		line = bufR.readLine();
		Pattern pattern = Pattern.compile("^\\[.+\\]$");
		boolean cnt = true;
		while (line != null && cnt) {
			Matcher matcher = pattern.matcher(line);
			
			if (matcher.find()) {
				String sectionName = line.substring(1, line.length()-1);
				if (sectionName.equals(section)) {
					while ((line=bufR.readLine()) != null && cnt) {
						if (line.equals("$"+flag)) {
							while ((line=bufR.readLine()) != null) {
								if (line.length() == 0)
									continue;
								if (line.charAt(0) == '$' || line.charAt(0) == '[') {
									cnt = false;
									break;
								}
								lines.add(line);
							}
						} else if (line.length() > 0) {
							if (line.charAt(0) == '[') {
								cnt = false;
								break;
							}
						}
					}
				}
			}
			line = bufR.readLine();
		}
		
		bufR.close();
		
		return lines;
		
	}

}
