package fr.poufalouf.combat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import org.newdawn.slick.Color;

import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.tools.Animation;
import fr.poufalouf.tools.AnimationFrame;
import fr.poufalouf.tools.Characteristic;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.PoppingObject;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.ScriptReader;
import fr.poufalouf.tools.Status;
import fr.poufalouf.tools.Text;

/**
 * Classe englobant les diff�rents moyens d'interaction entre personnages dans un combat
 * 
 * @author Anaïs Vernet
 */
public class Move {
	
	/**
	 * Le nom de cette action.
	 */
	private String name;
	/**
	 * L'effet de rendu de cette action.
	 */
	private CombatEffect anim;
	/**
	 * La table des effets sur les caract�ristiques de cette action.
	 */
	private HashMap<Characteristic, Integer> effects;
	/**
	 * La table des taux de r�ussite de cette attaque pour les effets sur les caract�ristiques correspondantes.
	 */
	private HashMap<Characteristic, Integer> successRates;
	/**
	 * Le lanceur de cette action.
	 */
	private CombatCharacter user;
	/**
	 * La cible de cette action.
	 */
	private CombatCharacter target;
	/**
	 * Indique si cette attaque inflige des dommages.
	 */
	private boolean offensive;
	/**
	 * Le statut de l'animation que doit prendre le lanceur de cette action.
	 */
	private Status userAnim;
	/**
	 * Indique si cette action a d�j� frapp�.
	 */
	private boolean hasStruck;
	
	/**
	 * Constructeur Move 1.
	 * 
	 * <p>Il s'agit d'un constructeur copie (tous les champs du param�tre sont copi�s dans l'instance construite. Il
	 * s'agit de r�elles copies, en profondeur dans les listes. Les r�f�rences aux personnages et aux actions sont
	 * quant � elles toujours des r�f�rences vers les m�mes objets.</p>
	 * 
	 * @param move
	 * 		L'action � copier.
	 * @throws ObjectInstanciationException
	 * 		si le constructeur CombatEffect l�ve une ObjectInstanciationException (si le param�tre move est null).
	 */
	public Move(Move move) throws ObjectInstanciationException {
		
		this.name = move.name;
		this.anim = new CombatEffect(this, move.getAnim().getName(),
				new Rectangle(move.getAnim().getX(), move.getAnim().getY(), move.getAnim().getW(), move.getAnim().getH()),
				move.getAnim().anims(), move.getAnim().getStyle(), move.getAnim().getFrameToStrike(),
				move.getAnim().getTrajectory());
		this.effects = new HashMap<Characteristic, Integer>();
		Set<Characteristic> set = move.effects.keySet();
		for (Characteristic c : set) {
			this.effects.put(c, move.effects.get(c));
		}
		this.successRates = new HashMap<Characteristic, Integer>();
		set = move.successRates.keySet();
		for (Characteristic c : set) {
			this.successRates.put(c, move.successRates.get(c));
		}
		this.user = move.user;
		this.target = move.target;
		this.offensive = move.offensive;
		this.userAnim = move.userAnim;
		this.hasStruck = move.hasStruck;
		
	}
	
	/**
	 * Constructeur Move 2.
	 * 
	 * <p></p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>name : le nom pass� en param�tre, ou "" si le param�tre est null.</li>
	 * <li>anim : un nouveau CombatEffect associ� � cette action, dont le nom est "Effet "+le nom de l'action, le rectangle
	 * de coordonn�es celui pass� en param�tre (animBox), l'animation celle pass�e en param�tre (anim), et le style
	 * celui pass� en param�tre (style).</li>
	 * <li>effects : la table pass�e en param�tre (effects), ou une nouvelle table (HashMap) d'Integer rep�r�s par des
	 * Characteristic si le param�tre est null.</li>
	 * <li>successRates : la table pass�e en param�tre (successRates), ou une nouvelle table (HashMap) d'Integer rep�r�s
	 * par des Characteristic.</li>
	 * <li>user : le personnage pass� en param�tre (user), ou un nouveau CombatCharacter si le param�tre est
	 * null.</li>
	 * <li>target : le personnage pass� en param�tre (user), ou un nouveau CombatCharacter si le param�tre est
	 * null. Il faudra mettre � jour ce champ si la cible n'est pas le lanceur.</li>
	 * <li>offensive : le bool�en pass� en param�tre (offensive).</li>
	 * <li>userAnim : STILL.</li>
	 * <li>hasStruck : faux.</li>
	 * </ul></p>
	 * 
	 * @param user
	 * 		Le lanceur de cette action.
	 * @param name
	 * 		Le nom de cette action.
	 * @param animBox
	 * 		Le nom du rectangle des coordonn�es de l'animation de cette action.
	 * @param anims
	 * 		La liste des animations de cette action.
	 * @param style
	 * 		Le style d'animation de cette action.
	 * @param effects
	 * 		La table des effets de cette action.
	 * @param successRates
	 * 		La table des taux de r�ussite des effets.
	 * @param offensive
	 * 		Vrai si cette action inflige des dommages � l'adversaire.
	 * @param frameToStrike
	 * 		Le num�ro de la frame de l'animation o� cette action doit agir.
	 * @param trajectory
	 * 		La trajectoire de l'effet de rendu de cette action.
	 * @throws ObjectInstanciationException
	 * 		si le constructeur CombatEffect l�ve une ObjectInstanciationException.
	 */
	public Move(CombatCharacter user, String name, Rectangle animBox, ArrayList<Animation> anims, CombatEffectStyle style,
			HashMap<Characteristic, Integer> effects, HashMap<Characteristic, Integer> successRates, boolean offensive,
			int frameToStrike, Rectangle[] trajectory)
	throws ObjectInstanciationException {
		
		this.name = (name==null)?"":name;
		this.anim = new CombatEffect(this, "Effet "+this.getName(), animBox, anims, style, frameToStrike, trajectory);
		if (effects != null)
			this.effects = effects;
		else
			this.effects = new HashMap<Characteristic, Integer>();
		if (successRates != null)
			this.successRates = successRates;
		else
			this.successRates = new HashMap<Characteristic, Integer>();
		if (user == null)
			throw new ObjectInstanciationException("Le lanceur de cette action n'est pas d�fini.", this.getName());
		this.user = user;
		this.target = user;
		this.offensive = offensive;
		this.userAnim = Status.STILL;
		this.hasStruck = false;
		
	}
	
	/**
	 * Lance cette action.
	 * 
	 * <p>L'animation du lanceur prend la valeur sp�cifi�e par le champ userAnim de cette classe, et l'effet
	 * de cette attaque est r�initialis� (m�thode reset() de la classe CombatEffect).</p>
	 * <p>Le bool�en hasStruck de cette classe passe � l'�tat faux.</p>
	 */
	public void use() {
		
		this.hasStruck = false;
		this.user.changeCurrentAnim(this.userAnim);
		this.getAnim().reset();
		
	}
	
	/**
	 * Met � jour l'�tat de cette action.
	 * 
	 * <p>Cette m�thode v�rifie si l'effet associ� � cette action a atteint son point d'action (m�thode isReadyToStrike()
	 * de la classe CombatEffect). Si c'est le cas, la m�thode act() de cette classe est appel�e.</p>
	 */
	public void update() {
		
		this.getAnim().playBehavior();
		if (this.getAnim().isReadyToStrike() && !this.hasStruck())
			this.act();
	}
	
	/**
	 * Applique les effets de cette action.
	 * 
	 * <p>Si cette action est offensive, alors un nombre al�atoire est tir� entre -0.2 et 0.2. Les dommages inflig�s
	 * sont �gaux � la force du lanceur multipli�e par ce nombre, moins la d�fense de la cible � laquelle on retire un
	 * autre nombre tir� de la m�me fa�on. Apr�s ces calculs, si les dommages sont inf�rieurs � 1 point, alors ils sont
	 * de 1 point.</p>
	 * <p>Si cette action poss�de des points de caract�ristiques � ajouter ou retirer � la cible, alors un nombre
	 * al�atoire est tir� entre 0 et le taux de r�ussite de cet effet (pr�cis� dans la table successRates parall�le
	 * � la table effects, qui contient les points de caract�ristiques). Si ce nombre, ramen� � un entier, est �gal � 0,
	 * alors l'effet est appliqu�.</p>
	 * <p>Cette m�thode attribue � la cible un texte popup repr�sentant les dommages ou les bonus re�us
	 * s'ils sont diff�rents de 0.</p>
	 * <p>Cette m�thode ne fait rien si le bool�en hasStruck de cette classe est vrai. Une fois cette m�thode
	 * accomplie, ce bool�en passe � l'�tat vrai.</p>
	 */
	public void act() {
		
		if (this.offensive) {
			int damages = 0;
			double strength = this.getUser().characteristic(Characteristic.STRENGTH);
			double defense = this.getTarget().characteristic(Characteristic.DEFENSE);
			double coeffHasard = (int) (Math.random()*41)/100.-0.2;
			damages = (int) Math.round((strength*(1-coeffHasard)));
			coeffHasard = (int) (Math.random()*41)/100.-0.2;
			int df = (int) Math.round((defense*(1-coeffHasard)));
			damages -= df;
			if (damages < 1)
				damages = 1;
			if (damages > this.getTarget().characteristic(Characteristic.HP))
				damages = this.getTarget().characteristic(Characteristic.HP);
			int hp = this.getTarget().characteristic(Characteristic.HP);
			this.getTarget().changeCharacteristic(Characteristic.HP, hp-damages);
			Rectangle rect[] = new Rectangle[20];
			for (int i=0;i<20;i++)
				rect[i] = new Rectangle(0, -i*0.01, 1, 1);
			PoppingObject pop = new PoppingObject(new Text(""+damages, Color.white, 0.1, 0.1,
					"res/textures/misc/lettres.png", this.getTarget().getHeartCenterX(), this.getTarget().getHeartCenterY()),
					rect, 1);
			pop.setPlan(Constantes.maxPlan);
			this.getTarget().addPopup(pop);
			this.getTarget().changeCurrentAnim(Status.HURT);
		}
		
		int j = 0;
		for (Characteristic c : Characteristic.values()) {
			int effect = this.effect(c);
			if (effect < 0 && -effect > this.getTarget().characteristic(c))
				effect = -this.getTarget().characteristic(c);
			if (effect == 0)
				continue;
			Integer rate = this.successRates.get(c);
			if (rate == null)
				rate = new Integer(1);
			int hsd = (int) (Math.random()*rate.intValue());
			if (hsd == 0) {
				switch (c) {
				case ACCURACY:
					effect = Math.max(effect, -this.getTarget().characteristic(c)+1);
					break;
				case HP:
					effect = Math.min(effect,
							this.getTarget().characteristic(Characteristic.HPMAX)-this.getTarget().characteristic(c));
					break;
				default:
				}
				
				if (effect != 0) {
					this.getTarget().changeCharacteristic(c, this.getTarget().characteristic(c)+effect);
					Rectangle rect[] = new Rectangle[30];
					for (int i=0;i<20;i++)
						rect[i] = new Rectangle(0, -i*0.01, 1, 1);
					for (int i=20;i<30;i+=2)
						rect[i] = new Rectangle(0, (-i+1)*0.01, 1, 1);
					for (int i=21;i<30;i+=2)
						rect[i] = new Rectangle(0, -i*0.01, 1, 1);
					PoppingObject pop = new PoppingObject(new Text(c.getShortName()+((effect>0)?"+":"")+effect,
							(effect<0)?Color.red:new Color(114,145,255), 0.07, 0.07, "res/textures/misc/lettres.png",
									this.getTarget().getHeartCenterX(), this.getTarget().getHeartCenterY()+0.1*(j+1)),
									rect, 1);
					pop.setPlan(Constantes.maxPlan);
					this.getTarget().addPopup(pop);
					j++;
				}
			}
		}
		this.setStruck(true);
		
	}
	
	/**
	 * <p>Cette m�thode utilise le fichier dont le nom est pass� en param�tre pour cr�er une liste d'actions.</p>
	 * <p>La m�thode readSectionAtFlag(String, String, String) de la classe ScriptReader est utilis�e pour d�terminer les
	 * diff�rentes portions du script � lire.</p>
	 * <p>La structure du fichier est la suivante :
	 * <ul>
	 * <li>Entre crochet, le nom de l'action (ex : [COUP DE POING]).</li>
	 * <li>Suivent plusieurs groupes d'instructions, pr�c�d�s chacun par le signe $. Ces groupes, appel�s blocs,
	 * doivent �tre s�par�s les uns des autres par au moins une ligne :</li>
	 * <li>$ANIM : concerne l'animation de l'effet de cette action. Ce groupe a la m�me structure qu'un script
	 * d'animation, sans le nom de l'�tat associ� (un seul �tat pour un effet d'action). La premi�re ligne indique
	 * le nom du fichier texture de l'effet. La deuxi�me ligne indique le nombre de fois que l'effet doit �tre jou�,
	 * et vaut 0 si le nombre est n�gatif. Enfin, les coordonn�es des fragments de texture de chaque frame sont indiqu�s
	 * au format x,y,w,h, o� ces coordonn�es doivent �tre multipli�es par le champ sizeTextureFragment de la classe
	 * Constantes pour donner la fraction de l'image enti�re du fichier texture (ex : 0,2,4,2 repr�sente la moiti�
	 * inf�rieure de l'image enti�re si un fragment de texture a pour taille 0.25). Au milieu de ces lignes peut se
	 * trouver une ligne comprenant le mot strike, la frame suivante est alors marqu�e comme la frame o� l'action
	 * agira. La derni�re ligne indique l'animation qui doit �tre jou�e par le lanceur lors de l'action.
	 * Entre les lignes des frames, des lignes du type (xN) peuvent �tre ins�r�es, o� N est le nombre
	 * de fois que la ligne doit �tre r�p�t�e. Si N=1, on ajoute une ligne de m�me type � la suite de la frame
	 * pr�c�dente. Si N=3 par exemple, on aura donc 4 frames identiques � la suite.</li>
	 * <li>$BOX : il s'agit d'une ligne contenant les coordonn�es x,y,w,h du rectangle des coordonn�es de l'image de
	 * l'effet de cette action. Ces coordonn�es sont des fractions de grandeurs d�finies par le style d'animation
	 * de l'effet de cette action. Voir la m�thode generateImage() de la classe CombatEffect pour plus d'informations.
	 * Le style d'animation est sp�cifi� � la ligne suivante, suivant le nom correspondant de l'�num�ration
	 * CombatEffectStyle.</li>
	 * <li>$TRAJ : un ensemble de lignes de coordonn�es (x, y, w, h) absolues � ajouter aux coordonn�es calcul�es de la
	 * BOX dans certains types d'animations. Il s'agit d'une trajectoire devant �tre d�finies pour certains styles
	 * d'animations.</li>
	 * <li>$CHAR : cette section comporte autant de lignes que de caract�ristiques modifi�es par cette action. Ces
	 * lignes sont de la forme : CARACTERISTIQUE,points de gain ou de dommages,taux de r�ussite. Le taux de r�ussite
	 * indique la chance qu'a le lanceur de voir chacun des effets sp�ciaux de l'action se r�aliser, en plus des dommages
	 * s'il y en a. Si ce nombre est n, il y a une chance sur n que l'effet soit appliqu�.</li>
	 * <li>$PROP : cette section comporte autant de lignes que de propri�t�s � fixer pour cette action. Les propri�t�s
	 * peuvent etre les suivantes :
	 * <ul>
	 * <li>OFFENSIVE=[true/false] : indique si cette action est offensive (false par d�faut).</li>
	 * <li>TARGET=[user/opponent] : indique quelle est la cible de cette action (opponent par d�faut).</li>
	 * <li>USER_ANIM=[statut d'animation] : indique l'animation que doit prendre le lanceur de cette action (STILL
	 * par d�faut).</li>
	 * </ul>
	 * </ul></p>
	 * <p>Cette m�thode lit le fichier enti�rement, et pour chaque action, v�rifie si son nom correspond � l'un des
	 * champs pass�s en param�tres. Si c'est ce le cas, l'action est charg�e dans la liste retourn�e.</p>
	 * 
	 * @param fileName
	 * 		Le nom du script � lire.
	 * @param user
	 * 		Le lanceur des actions.
	 * @param opponent
	 * 		L'adversaire du lanceur.
	 * @param names
	 * 		La liste des noms des actions � lire dans le fichier listAttacksFileName.
	 * @return
	 * 		La liste des actions cr��es.
	 * @throws IOException
	 * 		si la lecture de la liste d'actions �choue.
	 * @throws ScriptException
	 * 		si le script n'est pas valide.
	 * @throws IllegalArgumentException
	 * 		si les param�tres attacker ou opponent sont null.
	 * @throws ObjectInstanciationException
	 * 		si le constructeur Attack l�ve une ObjectInstanciationException, ce qui se produit si le param�tre attacker
	 * est null.
	 */
	public static ArrayList<Move> createMovesFromFile(String fileName, CombatCharacter user, CombatCharacter opponent,
			ArrayList<String> names)
			throws IOException, ScriptException, IllegalArgumentException, ObjectInstanciationException {
		
		if (user == null || opponent == null)
			throw new IllegalArgumentException("Le lanceur de ces attaques est null.");
		
		ArrayList<Move> moves = new ArrayList<Move>();
		
		int actionsLoaded = 0;
		
		ArrayList<String> lines;
		for (String s : names) {
			
			String actionTextureFile;
			int nbCycles;
			ArrayList<Animation> anims = new ArrayList<Animation>();
			Status userAnim = Status.STILL;
			Rectangle box = null;
			CombatEffectStyle style = CombatEffectStyle.RELATIVE_TO_SCREEN;
			int frameToStrike = 0;
			HashMap<Characteristic, Integer> effects = new HashMap<Characteristic, Integer>();
			HashMap<Characteristic, Integer> successRates = new HashMap<Characteristic, Integer>();
			boolean offensive = true;
			boolean targetUser = false;
			Rectangle[] trajectory = null;
			Move m;
			
			// ANIM
			
			lines = ScriptReader.readSectionAtFlag(fileName, s, "ANIM");
			if (lines.size() < 4)
				throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $ANIM : le bloc n'est pas complet.");
			// nom du fichier texture
			String line = lines.get(0);
			Pattern pttn = Pattern.compile("^[a-zA-Z][a-zA-Z0-9/_]*\\.?[a-zA-Z0-9]*$");
			Matcher mtch;
			mtch = pttn.matcher(line);
			if (!mtch.find())
				throw new ScriptException("Fichier "+fileName+" : "+s+
						". Bloc $ANIM : le fichier texture n'a pas un nom valide.");
			actionTextureFile = line;
			// nombre de cycles d'animation
			line = lines.get(1);
			Integer in = new Integer(0);
			try {
				in = Integer.decode(line);
			} catch (Exception e) {
				throw new ScriptException("Fichier "+fileName+" : "+s+
						". Bloc $ANIM : nombre de cycles d'animation non valide.");
			}
			nbCycles = in.intValue();
			// Frames de l'animation
			int l = 2;
			ArrayList<AnimationFrame> af = new ArrayList<AnimationFrame>();
			Status st;
			Animation anim;
			boolean stillFound = false;
			Pattern pttn2;
			Matcher mtch2;
			Pattern pttn3;
			Matcher mtch3;
			while (l < lines.size()) {
				af.clear();
				line = lines.get(l);
				try {
					st = Status.valueOf(line);
				} catch (Exception e) {
					throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $ANIM : l'animation "+line+" de " +
							"l'action n'est pas d�finie.");
				}
				int j = 0;
				int i;
				int x = 0, y = 0, w = 1, h = 1;
				for (i=l+1;i<lines.size();i++) {
					pttn = Pattern.compile("^-?\\d+,-?\\d+,\\d+,\\d+$");
					pttn2 = Pattern.compile("^x\\d+$");
					pttn3 = Pattern.compile("^[a-zA-Z][a-zA-Z0-9/_]*\\.[a-zA-Z0-9]*(\\[\\d+\\])?$");
					mtch = pttn.matcher(lines.get(i));
					mtch2 = pttn2.matcher(lines.get(i));
					mtch3 = pttn3.matcher(lines.get(i));
					if (mtch.find()) {
						String[] strs;
						try {
							strs = mtch.group().split(",");
							x = Integer.parseInt(strs[0]);
							y = Integer.parseInt(strs[1]);
							w = Integer.parseInt(strs[2]);
							h = Integer.parseInt(strs[3]);
						} catch (Exception e) {
							throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $ANIM : frame mal d�finie.");
						}
						af.add(new AnimationFrame(new Rectangle(x, y, w, h, Constantes.sizeTextureFragment), null, -1));
						j++;
					} else if (lines.get(i).equals("strike")) {
						frameToStrike = j;
					} else if (mtch2.find()) {
						try {
							int n = Integer.parseInt(mtch2.group().substring(1));
							if (n > 100)
								n = 100;
							for (int nb=0;nb<n;nb++) {
								af.add(new AnimationFrame(new Rectangle(x, y, w, h, Constantes.sizeTextureFragment),
										null, -1));
							}
						} catch (Exception e) {
							throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $ANIM : multiplication d'une " +
									"ligne de coordonn�es par un nombre non valide.");
						}
					} else if (mtch3.find()) {
						String[] strs = mtch3.group().split("[\\[\\]]");
						int k = -1;
						if (strs.length > 1)
							k = Integer.parseInt(strs[1]);
						if (af.size() > 0) {
							AnimationFrame fr = af.get(af.size()-1);
							fr = new AnimationFrame(fr.getText(), strs[0], k);
							af.remove(af.size()-1);
							af.add(fr);
						}
					} else {
						break;
					}
				}
				if (af.size() == 0)
					throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $ANIM : l'animation "+st.toString()+
					" ne contient pas de frame.");
				anim = new Animation(st, actionTextureFile, af.get(0), nbCycles);
				if (st == Status.STILL)
					stillFound = true;
				af.remove(0);
				for (AnimationFrame afr : af) {
					if (afr != null)
						anim.addFrame(afr);
				}
				anims.add(anim);
				l = i;
			}
			if (!stillFound) {
				throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $ANIM : l'animation STILL n'a pas �t� " +
						"renseign�e.");
			}
			
			// BOX
			
			lines = ScriptReader.readSectionAtFlag(fileName, s, "BOX");
			if (lines.size() < 2)
				throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $BOX : le bloc n'est pas complet.");
			// Coordonn�es
			line = lines.get(0);
			pttn = Pattern.compile("^-?\\d+\\.?\\d*,-?\\d+\\.?\\d*,\\d+\\.?\\d*,\\d+\\.?\\d*$");
			mtch = pttn.matcher(line);
			if (!mtch.find())
				throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $BOX : la ligne des " +
						"coordonn�es est introuvable ou non valide.");
			String[] strs;
			double x, y, w, h;
			try {
				strs = mtch.group().split(",");
				x = Double.parseDouble(strs[0]);
				y = Double.parseDouble(strs[1]);
				w = Double.parseDouble(strs[2]);
				h = Double.parseDouble(strs[3]);
			} catch (Exception e) {
			throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $BOX : coordonn�es mal d�finies.");
			}
			box = new Rectangle(x, y, w, h);
			// Style d'animation
			line = lines.get(1);
			try {
				style = CombatEffectStyle.valueOf(line);
			} catch (Exception e) {
				throw new ScriptException("Fichier "+fileName+" : "+s+
					". Bloc $BOX : le style d'animation n'est pas d�fini ou n'est pas valide.");
			}
			
			// TRAJ
			
			lines = ScriptReader.readSectionAtFlag(fileName, s, "TRAJ");
			trajectory = new Rectangle[lines.size()];
			pttn = Pattern.compile("^-?\\d+,-?\\d+,\\d+,\\d+$");
			for (l=0;l<lines.size();l++) {
				mtch = pttn.matcher(lines.get(l));
				if (!mtch.find())
					throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $TRAJ : une ligne de la trajectoire " +
							"n'est pas valide.");
				strs = lines.get(l).split(",");
				try {
					x = Double.parseDouble(strs[0]);
					y = Double.parseDouble(strs[1]);
					w = Double.parseDouble(strs[2]);
					h = Double.parseDouble(strs[3]);
				} catch (Exception e) {
					throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $TRAJ : coordonn�es mal d�finies.");
				}
				trajectory[l] = new Rectangle(x, y, w, h);
			}
			
			// CHAR
			
			lines = ScriptReader.readSectionAtFlag(fileName, s, "CHAR");
			if (lines.size() > 0) {
				pttn = Pattern.compile("^.+,-?\\d+,\\d+$");
				for (String ln : lines) {
					mtch = pttn.matcher(ln);
					if (mtch.find()) {
						strs = ln.split(",");
						try {
							Characteristic ch = Characteristic.valueOf(strs[0]);
							effects.put(ch, Integer.decode(strs[1]));
							successRates.put(ch, Integer.decode(strs[2]));
						} catch (Exception e) {
							throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $CHAR : "+
									"les effets de l'attaque ne sont pas correctement renseign�s.");
						}
					} else
						break;
				}
			}
			
			// PROP
			
			lines = ScriptReader.readSectionAtFlag(fileName, s, "PROP");
			pttn = Pattern.compile("^[^\\=]+\\=[^\\=]+$");
			for (int i=0;i<lines.size();i++) {
				line = lines.get(i);
				mtch = pttn.matcher(line);
				if (!mtch.find())
					throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $PROP : ligne non valide.");
				strs = line.split("=");
				if (strs[0].equals("OFFENSIVE")) {
					if (strs[1].equals("false"))
						offensive = false;
					else if (!strs[1].equals("true"))
						throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $PROP : argument " +
								"de la propri�t� OFFENSIVE non valide (accept�s : 'true' et 'false').");
				} else if (strs[0].equals("TARGET")) {
					if (strs[1].equals("user"))
						targetUser = true;
					else if (!strs[1].equals("opponent"))
						throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $PROP : argument " +
						"de la propri�t� TARGET non valide (accept�s : 'user' et 'opponent').");
				} else if (strs[0].equals("USER_ANIM")) {
					try {
						userAnim = Status.valueOf(strs[1]);
					} catch (Exception e) {
						throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $PROP : argument " +
								"de la propri�t� USER_ANIM non valide, l'animation "+strs[1]+" n'est pas d�finie.");
					}
				} else {
					throw new ScriptException("Fichier "+fileName+" : "+s+". Bloc $PROP : propri�t� "+strs[0]+
							" non valide.");
				}
			}
			
			m = new Move(user, s, box, anims, style, effects, successRates, offensive, frameToStrike, trajectory);
			m.setUserAnim(userAnim);
			if (!targetUser)
				m.setTarget(opponent);
			moves.add(m);
			actionsLoaded++;
			
		}
		
		if (actionsLoaded < names.size())
			throw new ObjectInstanciationException("une ou plusieurs attaques n'ont pas pu �tre lues.", user.getName());
		
		return moves;
		
	}
	
	/**
	 * Retourne le nom de cette action.
	 * 
	 * @return
	 * 		Le nom de cette action.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Retourne l'effet de rendu de cette action.
	 * 
	 * @return
	 * 		L'effet de rendu.
	 */
	public CombatEffect getAnim() {
		return this.anim;
	}
	
	/**
	 * Retourne l'effet de cette action correspondant � la caract�ristique pass�e en param�tre.
	 * 
	 * <p>Si cette caract�ristique n'est pas renseign�e, 0 est retourn�.
	 * 
	 * @param c
	 * 		La caract�ristique de l'effet � retourner.
	 * @return
	 * 		La valeur de l'effet, ou 0 si la caract�ristique demand�e n'est pas renseign�e.
	 */
	public int effect(Characteristic c) {
		
		Integer i = this.effects.get(c);
		if (i == null)
			return 0;
		return i.intValue();
		
	}
	
	/**
	 * Retourne le lanceur de cette action.
	 * 
	 * @return
	 * 		Le lanceur de cette action.
	 */
	public CombatCharacter getUser() {
		return this.user;
	}
	
	/**
	 * Change la cible de cette action, si le param�tre n'est pas null.
	 * 
	 * @param target
	 * 		La cible de cette action.
	 */
	public void setTarget(CombatCharacter target) {
		if (target != null)
			this.target = target;
	}
	
	/**
	 * Retourne la cible de cette action.
	 * 
	 * @return
	 * 		La cible de cette action.
	 */
	public CombatCharacter getTarget() {
		return this.target;
	}
	
	/**
	 * Modifie le statut de l'animation prise par le lanceur de cette action.
	 * 
	 * @param st
	 * 		L'animation � prendre.
	 */
	public void setUserAnim(Status st) {
		this.userAnim = (st==null)?Status.ATTACKING:st;
	}
	
	/**
	 * Retourne le statut de l'animation prise par le lanceur de cette action.
	 * 
	 * @return
	 * 		L'animation � prendre.
	 */
	public Status getUserAnim() {
		return this.userAnim;
	}
	
	/**
	 * Indique si cette action a atteint le moment d'appliquer ses effets.
	 * 
	 * @return
	 * 		Vrai si l'action a appliqu� ses effets.
	 */
	public boolean hasStruck() {
		return this.hasStruck;
	}
	
	/**
	 * Modifie la valeur du bool�en hasStruck de cette classe.
	 * 
	 * @param struck
	 * 		Vrai si l'action doit consid�rer avoir d�j� frapp�.
	 */
	public void setStruck(boolean struck) {
		this.hasStruck = struck;
	}

}
