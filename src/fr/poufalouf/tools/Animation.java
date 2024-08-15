package fr.poufalouf.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

/**
 * Animation d'un objet du jeu
 * 
 * <p>Il s'agit d'une succession d'AnimationFrame. Les images d'une animation doivent toutes �tre stock�es dans un m�me
 * fichier texture. Seules les coordonn�es des fragments de texture � utiliser peuvent varier d'une frame � l'autre.</p>
 * 
 * @author Anaïs Vernet
 */
public class Animation implements Cloneable {

	/**
	 * L'�tat caract�ris� par cette animation.
	 */
	private final Status status;
	/**
	 * Le nom du fichier texture de cette animation.
	 */
	private final String textureID;
	/**
	 * Le nombre de cycles pr�vus pour cette animation.
	 */
	private final int nbCycles;
	/**
	 * La liste des frames composant cette animation.
	 */
	private ArrayList<AnimationFrame> frames;
	/**
	 * Le num�ro de la frame actuelle de cette animation.
	 */
	private int currentFrame;
	/**
	 * Le nombre de cycles restant pour cette animation.
	 */
	private int timer;
	
	/**
	 * Constructeur Animation.
	 * 
	 * <p>La longueur pass�e en param�tre est le nombre de cycles d'AnimationFrame qu'est cens�e durer cette animation.
	 * Le timer de cette animation est r�gl� sur cette valeur.
	 * Si le param�tre de ce constructeur est n�gatif, alors cette animation tourne � l'infini tant qu'un
	 * �v�nement ext�rieur ne vient pas la modifier. Le contr�le de fin d'animation doit se faire dans les m�thodes
	 * des objets. Par d�faut, une animation continue de tourner m�me quand elle est cens�e �tre finie, si on ne lui
	 * dit pas sur quoi encha�ner.</p>
	 * <p>Apr�s initialisation, ce constructeur ajoute une premi�re AnimationFrame � cette animation, gr�ce � son dernier
	 * param�tre, ou s'il est null, en construisant une nouvelle AnimationFrame standard.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>status : le Status pass� en param�tre (status), ou STILL si le param�tre est null.</li>
	 * <li>textureID : l'identifiant pass� en param�tre (textureID), ou "" si le param�tre est null.</li>
	 * <li>nbCycles : la longueur pass�e en param�tre (length).</li>
	 * <li>frames : une nouvelle liste (ArrayList) d'AnimationFrame.</li>
	 * <li>currentFrame : 0.</li>
	 * <li>timer : la dur�e pass�e en param�tre.</li>
	 * </ul></p>
	 * 
	 * @param status
	 * 		L'�tat caract�ris� par cette animation.
	 * @param textureID
	 * 		Le nom du fichier texture de cette animation.
	 * @param animationFrame
	 * 		La premi�re �tape de cette animation.
	 * @param length
	 * 		Le nombre de cycles qu'est cens�e durer cette animation.
	 */
	public Animation(Status status, String textureID, AnimationFrame animationFrame, int length) {
		
		if (status != null)
			this.status = status;
		else
			this.status = Status.STILL;
		if (textureID != null)
			this.textureID = textureID;
		else
			this.textureID = "";
		this.nbCycles = length;
		this.frames = new ArrayList<AnimationFrame>();
		this.currentFrame = 0;
		this.timer = length;
		
		if (animationFrame != null)
			this.frames.add(animationFrame);
		else
			this.frames.add(new AnimationFrame(null, null, 0));
		
	}

	/**
	 * Ajoute une frame � cette animation.
	 * 
	 * @param af
	 * 		La frame � ajouter. Si elle est null, elle n'est pas ajout�e.
	 */
	public void addFrame(AnimationFrame af) {
		
		if (af != null)
			this.frames.add(af);
		
	}
	
	/**
	 * Incr�mente la frame actuelle de cette animation.
	 * 
	 * <p>Cette frame repasse � 0 si elle est la derni�re du tableau de frames. Dans ce cas, si le timer de
	 * cette classe est strictement positive, alors elle est d�cr�ment�e.</p>
	 */
	public void incrementFrame() {
		
		if (this.currentFrame == this.frames.size()-1) {
			this.currentFrame = 0;
			if (this.timer > 0)
				this.timer--;
		} else {
			this.currentFrame++;
		}
		
	}
	
	/**
	 * Retourne le num�ro de la frame en cours.
	 * 
	 * @return
	 * 		Le num�ro de la frame en cours.
	 */
	public int frameNumber() {
		return this.currentFrame;
	}
	
	/**
	 * Retourne le nombre total de frames de cette animation.
	 * 
	 * @return
	 * 		Le nombre de frames.
	 */
	public int numberOfFrames() {
		return this.frames.size();
	}
	
	/**
	 * Repositionne la frame actuelle de cette animation � z�ro, et r�initialise le timer.
	 */
	public void reset() {
		
		this.currentFrame = 0;
		this.timer = this.nbCycles;
		
	}
	
	/**
	 * Retourne le rectangle des coordonn�es du fragment de texture � utiliser pour la frame actuelle de cette animation.
	 * 
	 * <p>Si l'orientation pass�e en param�tre est non nulle et diff�rente de AUCUNE, alors elle peut �tre utilis�e pour
	 * d�terminer les coordonn�es du fragment de texture.</p>
	 * <p>
	 * <ul>
	 * <li>Si l'abscisse du rectangle d�fini par la frame actuelle est n�gative, alors elle est d�finie par le quotient de
	 * la division par 4 du nombre retourn� par la m�thode getNumText() de la classe Orientation (en num�ro d'image
	 * sur une ligne de fichier texture, commen�ant � 0 � gauche).</li>
	 * <li>Si l'ordonn�e est n�gative, alors elle est d�finie par le reste de la division par 4.</li>
	 * <li>Si l'abscisse et l'ordonn�e sont n�gatives, alors elles sont toutes deux d�finies de cette mani�re.</li>
	 * <li>Si elles sont positives, elles sont d�finies par la frame actuelle, normalement.</li>
	 * <li>Si l'orientation est �gale � AUCUNE, alors si l'abscisse d�finie par la frame est n�gative, le rectangle
	 * retourn� � une abscisse nulle (utile pour les barri�res qui peuvent soit �tre d�termin�es par l'orientation, soit
	 * par la frame).</li>
	 * </ul></p>
	 * 
	 * @param or
	 * 		L'orientation d�finissant les coordonn�es, si cette animation est orientable.
	 * @return
	 * 		Le rectangle des coordonn�es du fragment de texture.
	 */
	public Rectangle textRect(Orientation or) {
		
		/*
		 * currentFrame est forc�ment un indice valide. Il s'agit d'une variable priv�e dont les modifications sont
		 * contr�l�es dans les m�thodes incrementFrame() et reset().
		 */
		
		Rectangle rect;

		if (or != null && or != Orientation.AUCUNE) {
			if (this.frames.get(this.currentFrame).getText().getY() < 0) {
				rect = new Rectangle((or.getNumText()%4)*Constantes.sizeTextureFragment,
						(or.getNumText()/4)*Constantes.sizeTextureFragment,
						this.frames.get(this.currentFrame).getText().getW(),
						this.frames.get(this.currentFrame).getText().getH());
			} else if (this.frames.get(this.currentFrame).getText().getX() < 0) {
				rect = new Rectangle((or.getNumText()%4)*Constantes.sizeTextureFragment,
						this.frames.get(this.currentFrame).getText().getY(),
						this.frames.get(this.currentFrame).getText().getW(),
						this.frames.get(this.currentFrame).getText().getH());
			} else if (this.frames.get(this.currentFrame).getText().getY() < 0) {
				rect = new Rectangle(this.frames.get(this.currentFrame).getText().getX(),
						(or.getNumText()/4)*Constantes.sizeTextureFragment,
						this.frames.get(this.currentFrame).getText().getW(),
						this.frames.get(this.currentFrame).getText().getH());
			} else {
				rect = this.frames.get(this.currentFrame).getText();
			}
		} else {
			rect = this.frames.get(this.currentFrame).getText();
			if (rect.getX() < 0)
				rect = new Rectangle(0, rect.getY(), rect.getW(), rect.getH());
		}
		
		return rect;
		
	}
	
	/**
	 * Retourne le son associ� � la frame actuelle, si le cycle en cours est �gal � celui sp�cifi� par la frame (champ
	 * cycleForSound). Sinon, retourne "".
	 * 
	 * @return
	 * 		Le son de la frame actuelle.
	 */
	public String sound() {
		
		String s = "";
		if (this.frames.get(this.currentFrame).getCycleForSound() == this.nbCycles-this.timer+1
				|| this.frames.get(this.currentFrame).getCycleForSound() == -1)
			s = this.frames.get(this.currentFrame).getSound();
		return s;
		
	}
	
	/**
	 * Cr�� une liste (ArrayList) d'animations � partir d'un script.
	 * 
	 * <p>Un fichier d'animations est structur� selon le mod�le suivant :</p>
	 * <p>Chaque bloc d'animations se suit, et est compos� de la m�me mani�re.
	 * <ul>
	 * <li>Entre crochets, le statut correspondant � l'animation. Exemple : [STILL].</li>
	 * <li>Le nom du fichier image utilis� par l'animation. Par exemple : res/poufalouf/still.png.</li>
	 * <li>Le nombre de fois o� doit �tre r�p�t�e l'animation avant de laisser la place � une autre animation. Pour une
	 * animation tournant en boucle, inscrire -1.</li>
	 * <li>Autant de lignes que n�cessaire pour les diff�rentes frames de l'animation. Les coordonn�es exprim�es en num�ros
	 * d'images sont s�par�es par des virgules, dans l'ordre x,y,w,h. Par exemple, pour prendre la seconde
	 * image sur la premi�re ligne, on inscrira 0,1,1,1. La largeur et la hauteur sont exprim�es en pourcentage de la taille
	 * d'une image standard. Entre ces lignes, des lignes du type (xN) peuvent �tre ins�r�es, o� N est le nombre
	 * de fois que la ligne doit �tre r�p�t�e. Si N=1, on ajoute une ligne de m�me type � la suite de la frame
	 * pr�c�dente. Si N=3 par exemple, on aura donc 4 frames identiques � la suite. Une frame peut �galement �tre
	 * suivie d'une ligne d�signant un fichier son (.wav), un son lui sera alors associ�.</li>
	 * </ul></p>
	 * <p>Le fichier d'animation doit �tre plac� dans le dossier res/scripts/animations et porter le nom de l'objet
	 * correspondant en minuscules.</p>
	 * 
	 * @param fileName
	 * 		Le nom du fichier texte � lire.
	 * @return
	 * 		La liste d'animations cr��e.
	 * @throws IOException
	 * 		si le fichier n'a pas pu �tre lu correctement.
	 * @throws ScriptException
	 * 		si le script d'animation contient des erreurs.
	 * @throws Exception
	 * 		si une erreur survient lors de la cr�ation d'un pattern.
	 */
	public static ArrayList<Animation> createAnimationFromFile(String fileName) throws IOException, ScriptException,
		Exception {
		
		ArrayList<Animation> a = new ArrayList<Animation>();
		Animation anim = null;
		ArrayList<String> lines;
		String line;
		boolean stillFound = false;
		int n = 1;
		
		lines = ScriptReader.readSection(fileName, n);
		while (lines.size() > 0) {
			if (lines.size() < 4)
				throw new ScriptException("Erreur dans le script d'animation "+fileName+" : section "+lines.get(0)+
						" incompl�te.");
			line = lines.get(0);
			Status st = Status.STILL;
			try {
				st = Status.valueOf(line);
			} catch (Exception e) {
				throw new ScriptException("Erreur dans le script d'animation "+fileName+" : statut inconnu " +
						line+".");
			}
			line = lines.get(1);
			Pattern ptn = Pattern.compile("^[a-zA-Z][a-zA-Z0-9/_]+\\.?[a-zA-Z0-9]*$");
			Matcher mth;
			if (line == null)
				throw new ScriptException("Erreur dans le script d'animation "+fileName+" : nom de fichier manquant" +
						" pour l'animation "+st.toString()+".");
			mth = ptn.matcher(line);
			if (!mth.find())
				throw new ScriptException("Erreur dans le script d'animation "+fileName+" : nom de fichier" +
						" invalide pour l'animation "+st.toString()+".");
			String animFile = line;
			line = lines.get(2);
			Integer nbCycles = new Integer(0);
			try {
				nbCycles = Integer.decode(line);
			} catch (Exception e) {
					throw new ScriptException("Erreur dans le script d'animation "+fileName+" : nombre de cycles" +
							" manquant pour l'animation "+st.toString()+".");
			}
			boolean frameFound = false;
			ArrayList<AnimationFrame> af = new ArrayList<AnimationFrame>();
			int x = 0, y = 0, w = 1, h = 1;
			for (int i=3;i<lines.size();i++) {
				line = lines.get(i);
				Pattern pttn = Pattern.compile("^-?\\d+,-?\\d+,\\d+,\\d+$");
				Pattern pttn2 = Pattern.compile("^x\\d+$");
				ptn = Pattern.compile("^[a-zA-Z][a-zA-Z0-9/_]+\\.?[a-zA-Z0-9]*(\\[\\d+\\])?$");
				Matcher mtch = pttn.matcher(line);
				Matcher mtch2 = pttn2.matcher(line);
				mth = ptn.matcher(line);
				if (mtch.find()) {
					String[] strs;
					try {
						strs = mtch.group().split(",");
						x = Integer.parseInt(strs[0]);
						y = Integer.parseInt(strs[1]);
						w = Integer.parseInt(strs[2]);
						h = Integer.parseInt(strs[3]);
					} catch (Exception e) {
						throw new ScriptException("Erreur dans le script d'animation "+fileName+" : coordonn�es" +
								" d'animation invalides pour l'animation "+st.toString()+".");
					}
					af.add(new AnimationFrame(new Rectangle(x, y, w, h, Constantes.sizeTextureFragment), null, -1));
					frameFound = true;
				} else if (mtch2.find()) {
					try {
						int k = Integer.parseInt(mtch2.group().substring(1));
						if (k > 100)
							k = 100;
						for (int nb=0;nb<k;nb++) {
							af.add(new AnimationFrame(new Rectangle(x, y, w, h, Constantes.sizeTextureFragment), null, -1));
						}
					} catch (Exception e) {
						throw new ScriptException("Erreur dans le script d'animation "+fileName+" : multiplication " +
								" des coordonn�es par un nombre non valide pour l'animation "+st.toString()+".");
					}
				} else if (mth.find()) {
					try {
						String[] strs = mth.group().split("[\\[\\]]");
						int k = -1;
						if (strs.length > 1)
							k = Integer.parseInt(strs[1]);
						if (af.size() > 0) {
							AnimationFrame fr = af.get(af.size()-1);
							fr = new AnimationFrame(fr.getText(), strs[0], k);
							af.remove(af.size()-1);
							af.add(fr);
						}
					} catch (Exception e) {
						throw new ScriptException("Erreur dans le script d'animation "+fileName+" : ligne son invalide" +
								" pour l'animation "+st.toString()+".");
					}
				} else
					break;
			}
			if (!frameFound)
				throw new ScriptException("Erreur dans le script d'animation "+fileName+" : frame manquante" +
						" pour l'animation "+st.toString()+".");
			anim = new Animation(st, animFile, af.get(0), nbCycles.intValue());
			af.remove(0);
			for (AnimationFrame afr : af) {
				if (afr != null)
					anim.addFrame(afr);
			}
			a.add(anim);
			if (anim.getStatus() == Status.STILL)
				stillFound = true;
			n++;
			lines = ScriptReader.readSection(fileName, n);
		}
		
		if (!stillFound)
			throw new ScriptException("Erreur dans le script d'animation "+fileName+" : animation STILL non trouv�e.");
		
		return a;
		
	}
	
	/**
	 * Clone cette animation.
	 * 
	 * @return
	 * 		Une copie de cette animation.
	 */
	@Override
	public Animation clone() {
		
		Animation anim = null;
		
		try {
			anim = (Animation) super.clone();
		} catch (CloneNotSupportedException e) {
			// Ne peut pas se produire en th�orie.
		}
		
		return anim;
		
	}

	/**
	 * Retourne l'�tat caract�ris� par cette animation.
	 * 
	 * @return
	 * 		L'�tat de cette animation.
	 */
	public Status getStatus() {
		return this.status;
	}
	
	/**
	 * Retourne le nom du fichier texture de cette animation.
	 * 
	 * @return
	 * 		Le nom du fichier texture.
	 */
	public String getTextureID() {
		return this.textureID;
	}
	
	/**
	 * Retourne le nombre de cycles de cette animation.
	 * 
	 * @return
	 * 		Le nombre de cycles.
	 */
	public int getNbCycles() {
		return this.nbCycles;
	}
	
	/**
	 * Retourne le temps restant pour cette animation.
	 * 
	 * @return
	 * 		L'�tat du timer de cette animation.
	 */
	public int getTimer() {
		return this.timer;
	}
	
}
