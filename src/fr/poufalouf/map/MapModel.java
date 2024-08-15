package fr.poufalouf.map;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import fr.poufalouf.InitializationException;
import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.UpdateException;
import fr.poufalouf.tools.Bar;
import fr.poufalouf.tools.Characteristic;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.LoadingScreen;
import fr.poufalouf.tools.Model;
import fr.poufalouf.tools.Orientation;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.Status;
import fr.poufalouf.tools.TexturedImage;
import fr.poufalouf.tools.UserEvent;
import fr.poufalouf.tools.Zone;
import fr.poufalouf.tools.ZoneType;

/**
 * Type de mod�le li� � une Map
 * 
 * <p>Ce type de mod�le g�re les d�placements des objets et leurs comportements sur une carte 2D vue de haut (type Map). Il
 * s'utilise en combinaison avec un MapControl.</p>
 * <p>Dans ce type de mod�les, les objets sont tous rep�r�s par un nom du type nom_de_l'objet(x:valeur,y:valeur) o� x et y
 * sont leurs coordonn�es par rapport au jeu. Le nom pr�c�dent les coordonn�es ne doit contenir que des caract�res
 * compris entre a et z, A et Z, 0 et 9. Les coordonn�es ne sont pas toujours pr�cis�es dans le nom, notamment pour
 * les objets mobiles.</p>
 * 
 * @author Anaïs Vernet
 */
public class MapModel extends Model {
	
	/**
	 * La carte g�r�e par ce mod�le.
	 */
	private Map carte;
	/**
	 * Indique quelles zones doivent �tre affich�es pendant le jeu.
	 */
	private HashMap<ZoneType, Boolean> drawingZone;
	/**
	 * Indique si ce mod�le doit �tre quitt� ou chang�.
	 */
	private boolean terminated;
	/**
	 * Ce tableau indique l'ordre d'appui des touches fl�ch�es.
	 */
	private Orientation arrowKeys[];
	/**
	 * Indique si le jeu est en mode combat.
	 */
	private boolean inCombatMode;
	/**
	 * Indique si les anneaux de d�tections doivent �tre affich�s.
	 */
	private boolean showingDetectionRings;
	/**
	 * Le nom de l'adversaire rencontr� lors de l'entr�e en mode combat.
	 */
	private String opponent;
	
	/**
	 * Constructeur MapModel.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Le param�tre de ce contructeur doit contenir en premier objet un LoadingScreen.</p>
	 * <p>Le constructeur Model appelle les deux m�thodes initialize(LoadingScreen) et updateObjects() red�finies dans
	 * cette classe. L'initialisation de la carte se fait dans la premi�re m�thode. Un bool�en � l'�tat faux est ajout�
	 * dans la table drawingZone de cette classe pour chaque ZoneType de l'�num�ration.</p>
	 * <p>Si la m�thode initialize(LoadingScreen) cr�e une carte null, alors ce constructeur affecte une nouvelle
	 * Map standard � ce mod�le.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>carte : la carte est initialis�e dans la m�thode initialize(LoadingScreen) de cette classe, appel�e par le
	 * constructeur de la classe m�re. Cette m�thode cr�e une nouvelle Map de dimensions d�finies par les champs
	 * cellPerMapW et cellPerMapH de la classe Constantes.
	 * Si pour une quelconque raison la carte est toujours null apr�s l'appel � la m�thode initialize(LoadingScreen), une
	 * nouvelle Map standard est cr��e.</li>
	 * <li>drawingZone : une nouvelle table (HashMap) de Boolean r�f�renc�s par des ZoneType.</li>
	 * <li>terminated : false.</li>
	 * <li>arrowKeys : un nouveau tableau d'Orientation dont tous les champs sont initialis�s � Orientation.AUCUNE.</li>
	 * <li>inCombatMode : false.</li>
	 * <li>showingDetectionRings : false.</li>
	 * <li>opponent : "".</li>
	 * </ul></p>
	 * 
	 * @param param
	 * 		L'�cran de chargement affichant la progression de l'initialisation. Il est pass� � la m�thode
	 * initialize(LoadingScreen) de cette classe.
	 * @throws InitializationException
	 * 		si le constructeur de la classe m�re l�ve une InitializationException.
	 */
	public MapModel(ArrayList<Object> param) throws InitializationException {
		
		/* 
		 * Les variables de cette classe sont fix�es dans la m�thode initialize de cette classe appel�e par le
		 * constructeur de la classe m�re.
		 */
		
		super(param);
		if (this.carte == null)
			this.carte = new Map(0, 0, null);
		this.drawingZone = new HashMap<ZoneType, Boolean>();
		this.drawingZone.put(ZoneType.IMAGE, new Boolean(false));
		this.drawingZone.put(ZoneType.COLLISION, new Boolean(false));
		this.drawingZone.put(ZoneType.EFFECT, new Boolean(false));
		this.drawingZone.put(ZoneType.DETECTION, new Boolean(false));
		this.terminated = false;
		this.arrowKeys = new Orientation[4];
		this.inCombatMode = false;
		this.showingDetectionRings = false;
		this.opponent = "";
		
		for (int i=0;i<4;i++)
			this.arrowKeys[i] = Orientation.AUCUNE;
		
	}
	
	/**
	 * V�rifie que la liste des param�tres contient en premier objet un LoadingScreen.
	 * 
	 * @return
	 * 		Vrai si les param�tres sont corrects.
	 */
	@Override
	public boolean checkParameters(ArrayList<Object> param) {
		
		if (param.size() < 1)
			return false;
		if (!(param.get(0) instanceof LoadingScreen))
			return false;
		return true;
		
	}
	
	/**
	 * Initialise ce mod�le en cr�ant une nouvelle carte.
	 * 
	 * <p>La carte est g�n�r�e avec les dimensions sp�cifi�es par les champs cellPerMapW et cellPerMapH de la classe
	 * Constantes. La m�thode generateNewMap(LoadingScreen) de cette classe est ensuite appel�e. Tous les objets de la
	 * carte se voient assigner un compteur de temps initialis� � la m�me date afin qu'ils soient synchrones dans leurs
	 * animations. Enfin, la musique du mod�le est initialis�e.</p>
	 * 
	 * @param param
	 * 		L'�cran de chargement affichant la progression de l'initialisation. Il est pass� � la m�thode
	 * generateNewMap(LoadingScreen) de cette classe.
	 */
	@Override
	public void initialize(ArrayList<Object> param) throws InitializationException {
		
		LoadingScreen ldScreen = (LoadingScreen) param.get(0);
		this.carte = new Map(Constantes.cellPerMapW, Constantes.cellPerMapH,
				"res/textures/map/terrain/pumpkins/terrain.png");
		try {
			generateNewMap(ldScreen);
		} catch (Exception e) {
			throw new InitializationException("erreur lors de la g�n�ration de la carte.", e);
		}
		long temps = Sys.getTime();
		for (MapObject obj : this.carte.objets().values()) {
			obj.setCompteurTemps(temps);
		}
		this.setMusique("res/music/champ_citrouilles.ogg");
		
	}
	
	/**
	 * G�n�re une nouvelle carte.
	 * 
	 * <p>Cette m�thode g�n�re des cases textur�es pour le sol de la carte, ainsi que des objets
	 * r�partis de mani�re al�atoire sur la carte. Dans l'�tat actuel du g�n�rateur al�atoire, les actions suivantes sont
	 * r�alis�es :
	 * <ul>
	 * <li>Les cases de la carte se voient assigner une texture d�pendant du terrain de la carte de ce mod�le.</li>
	 * <li>Poufalouf est plac� al�atoirement sur la carte � 23 cases du bord minimum (afin que la zone de vision
	 * n'atteigne pas les bords de la carte).</li>
	 * <li>Douni est plac� de m�me, pr�s de Poufalouf, sans se superposer � lui.</li>
	 * <li>Des barri�res sont g�n�r�es pour d�limiter la zone accessible au joueur.</li>
	 * <li>Les citrouilles sont r�parties sur la carte. Toutes les citrouilles
	 * doivent �tre plac�es, et ne doivent pas se superposer entre elles ni se superposer � Poufalouf. Si une citrouille
	 * ne trouve plus de place, la boucle s'arr�tera au bout d'un temps d�termin� (arr�t de la boucle infinie).</li>
	 * <li>Des barri�res sont r�parties sur la carte. Cette r�partition suit la m�me logique que pour les citrouilles : une
	 * barri�re ne doit pas se superposer � une citrouille, � une autre barri�re, ou � Poufalouf.</li>
	 * <li>Pour chaque barri�re sur la carte, les espaces entre cette barri�re et une barri�re � l'est ou
	 * au sud assez proche sont ferm�s.</li>
	 * <li>Les hautes herbes sont r�parties sur la carte. Elles ne peuvent pas se superposer
	 * � d'autres objets.</li>
	 * <li>Les chaises sont r�parties dans l'enclos. Elles ne peuvent se superposer � aucun objet.</li>
	 * <li>Les mines sont r�parties dans l'enclos. Elles ne peuvent se superposer � aucun objet.</li>
	 * <li>Les coeurs sont r�partis dans l'enclos. Ils ne peuvent se superposer � aucun objet.</li>
	 * <li>Des cases de terre sont g�n�r�es, il s'agit d'une texture du terrain utilis� qui ne peut pas �tre d�pos�e
	 * naturellement par le premier point de cet algorithme. Des bords de terre sont ensuite ajout�s autour des cases
	 * de terre.</li>
	 * </ul></p>
	 * 
	 * <p>Pendant la g�n�ration de cette carte al�atoire, l'�cran de chargement pass� en param�tre est mis � jour en
	 * fonction de l'�volution de la g�n�ration. Il est affich� dans le thread de jeu pendant que cette m�thode
	 * tourne.</p>
	 * 
	 * @param ldScreen
	 * 		L'�cran de chargement affichant la progression de la g�n�ration. S'il est null, rien n'est fait.
	 * @throws ObjectInstanciationException
	 * 		si une erreur survient lors de l'instanciation d'un objet.
	 */
	private void generateNewMap(LoadingScreen ldScreen) throws ObjectInstanciationException {
		
		if (ldScreen == null)
			return;
		
		// G�n�ration des cases
		
		for (int i=0;i<this.carte.getNbCasesWidth();i++) {
			for (int j=0;j<this.carte.getNbCasesHeight();j++) {
				this.carte.replaceCell(i, j, new Case(this.carte,
						i*Constantes.sizeCell,j*Constantes.sizeCell,
						Constantes.sizeCell, Constantes.sizeCell, this.carte.getTerrainID(),
						(int) (Math.random()*1)));
			}
		}
		
		// D�termination du nombre d'objets � placer
		
		int nbCitrouilles = 2000;
		int nbBarrieres = 800;
		int nbHerbes = 800;
		int nbChaises = 100;
		int nbMines = 100;
		int nbCoeurs = 100;
		int totalAttente = nbCitrouilles+nbBarrieres+nbHerbes+nbChaises+nbMines+nbCoeurs+
			2*Constantes.cellPerMapH*Constantes.cellPerMapW;
		boolean cnt = false;
		int line = 0, col = 0;
		ArrayList<MapObject> objs = new ArrayList<MapObject>();
		ArrayList<CaseType> caseTypes = new ArrayList<CaseType>();
		
		// G�n�ration de la terre et de l'eau
		
		this.generateTerrain(2, 240, 300, 3, CaseType.SOLID2, true);
		ldScreen.update((float) (Constantes.cellPerMapW*Constantes.cellPerMapH)/totalAttente);
		this.generateTerrain(1, 248, 400, 3, CaseType.WATER, false);
		ldScreen.update((float) (2*Constantes.cellPerMapW*Constantes.cellPerMapH)/totalAttente);
		try {
			Thread.sleep(50); // Pour s'assurer que la barre de chargement se compl�te.
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Positionnement de Poufalouf
		
		objs.add(new Poufalouf("Poufalouf", this.carte, 0, 0));
		caseTypes.add(CaseType.WATER);
		this.putMapObjects(objs, caseTypes, false, 22);
		
		// Positionnement de Douni
		do {
			int cmpt = 0;
			cnt = false;
			col = (int) this.carte.objets().get("Poufalouf").getX()/Constantes.sizeCell 
				+ (int) (Math.random()*10) - 5;
			line = (int) this.carte.objets().get("Poufalouf").getY()/Constantes.sizeCell 
			+ (int) (Math.random()*10) - 5;
			if (line >= this.carte.getNbCasesHeight()-22)
				line = this.carte.getNbCasesHeight()-23;
			if (col >= this.carte.getNbCasesWidth()-22)
				col = this.carte.getNbCasesWidth()-23;
			if (line <= 22)
				line = 23;
			if (col <= 22)
				col = 23;
			if (this.carte.cell(col, line).occupants().size() > 0)
				cnt = true;
			if (this.carte.cell(col, line).getType() == CaseType.WATER)
				cnt = true;
			// Interdiction de faire une boucle infinie : Douni a le droit � 100 essais
			cmpt++;
			if (cmpt > 100) {
				throw new ObjectInstanciationException("Douni n'a pas pu �tre plac�.", "Douni");
			}
		} while (cnt);
		this.carte.addObject(new Douni("Douni", this.carte, col*Constantes.sizeCell, line*Constantes.sizeCell), true);
		
		// Fermeture de l'enclos
		
		for (int i=19;i<=Constantes.cellPerMapW-21;i++) {
			this.carte.addObject(new Barriere(this.carte, i*Constantes.sizeCell,
					19*Constantes.sizeCell, Constantes.sizeCell, Constantes.sizeCell, Orientation.AUCUNE), true);
			this.carte.addObject(new Barriere(this.carte, i*Constantes.sizeCell,
					(Constantes.cellPerMapH-21)*Constantes.sizeCell, Constantes.sizeCell, Constantes.sizeCell,
					Orientation.AUCUNE), true);
		}
		for (int i=20;i<=Constantes.cellPerMapH-22;i++) {
			this.carte.addObject(new Barriere(this.carte, 19*Constantes.sizeCell,
					i*Constantes.sizeCell, Constantes.sizeCell, Constantes.sizeCell, Orientation.AUCUNE), true);
			this.carte.addObject(new Barriere(this.carte,
					(Constantes.cellPerMapW-21)*Constantes.sizeCell, i*Constantes.sizeCell,
					Constantes.sizeCell, Constantes.sizeCell, Orientation.AUCUNE), true);
		}
		
		// G�n�ration des citrouilles et des barri�res
		
		objs.clear();
		for (int i=0;i<nbCitrouilles;i++) {
			objs.add(new Decor("Citrouille", this.carte, 0, 0, Constantes.sizeCell, Constantes.sizeCell,
						new Rectangle(0, 0.75, 1, 0.25, Constantes.sizeCell), 0));
		}
		for (int i=0;i<nbBarrieres;i++) {
			objs.add(new Barriere(this.carte, 0, 0, Constantes.sizeCell, Constantes.sizeCell, Orientation.AUCUNE));
		}
		this.putMapObjects(objs, caseTypes, false, 0);
		ldScreen.update((float) (2*Constantes.cellPerMapW*Constantes.cellPerMapH+nbCitrouilles+nbBarrieres)/totalAttente);
		
		// Fermeture des espaces entre barri�res
		
		for (Case c : this.carte.cells()) {
			for (MapObject b : c.occupants()) {
				if (b instanceof Barriere) {
					((Barriere) b).fillSpaces(Orientation.EST);
					((Barriere) b).fillSpaces(Orientation.SUD);
					break;
				}
			}
		}
		for (MapObject o : this.carte.objets().values())
			if (o instanceof Barriere)
				o.setToUpdate(false);
		
		// G�n�ration des hautes herbes
		
		Decor dec;
		objs.clear();
		for (int i=0;i<nbHerbes;i++) {
			dec = new Decor("Herbes", this.carte, 0, 0, Constantes.sizeCell, Constantes.sizeCell, null, 2);
			dec.setToUpdate(false);
			objs.add(dec);
		}
		this.putMapObjects(objs, caseTypes, false, 0);
		ldScreen.update((float) (2*Constantes.cellPerMapW*Constantes.cellPerMapH+nbCitrouilles+nbBarrieres+
				nbHerbes)/totalAttente);
		
		// G�n�ration des chaises et des mines
		
		objs.clear();
		caseTypes.clear();
		caseTypes.add(CaseType.WATER);
		for (int i=0;i<nbChaises;i++) {
			objs.add(new Chaise(this.carte, 0, 0, Orientation.SUD));
		}
		for (int i=0;i<nbMines;i++) {
			objs.add(new Mine(this.carte, 0, 0));
		}
		this.putMapObjects(objs, caseTypes, false, 25);
		ldScreen.update((float) (2*Constantes.cellPerMapW*Constantes.cellPerMapH+nbCitrouilles+nbBarrieres+
				nbHerbes+nbChaises+nbMines)/totalAttente);
		
		// G�n�ration des coeurs
		
		objs.clear();
		for (int i=0;i<nbCoeurs;i++) {
			objs.add(new Coeur(this.carte, col*Constantes.sizeCell, line*Constantes.sizeCell));
		}
		this.putMapObjects(objs, null, false, 25);
		ldScreen.update((float) (2*Constantes.cellPerMapW*Constantes.cellPerMapH+nbCitrouilles+nbBarrieres+
				nbHerbes+nbChaises+nbMines+nbCoeurs)/totalAttente);
		
		// Ajout de la brume
		
		this.carte.addObject(new Landscape("Boue", this.carte, null, new Rectangle(0, 0,
				Constantes.cellPerMapW*Constantes.sizeCell/4, Constantes.cellPerMapH*Constantes.sizeCell/4),
				"res/textures/map/terrain/pumpkins/boue.png", 0, -Constantes.maxPlan+3), false);
		this.carte.addObject(new Landscape("Brume", this.carte, Orientation.OUEST, new Rectangle(0, 0,
				Constantes.cellPerMapW*Constantes.sizeCell/2, Constantes.cellPerMapH*Constantes.sizeCell/2),
				"res/textures/map/terrain/pumpkins/brume.png", 2, Constantes.maxPlan-3), false);

	}
	
	/**
	 * G�n�re une portion de terrain.
	 * 
	 * <p>Une case de texture dont le num�ro est sp�cifi� en param�tre est ajout�e � la carte en remplacement d'une case
	 * existante, al�atoirement � une fr�quence sp�cifi�e en param�tre. Puis, les cases l'entourant sont �galement
	 * modifi�es avec une probabilit� sp�cifi�e en param�tre, afin de cr�er une nappe. Cette nappe est ensuite bord�e
	 * gr�ce � une suite de huit textures cons�cutives dans le fichier texture du terrain (dans l'ordre bordures EST,
	 * OUEST, NORD, SUD, SUD_EST, NORD_OUEST, NORD_EST, SUD_OUEST). Les cases modifi�es prennent le type pass� en
	 * param�tre.</p>
	 * <p>En fonction du type de bordures choisie (� l'int�rieur ou � l'ext�rieur), les bordures seront plac�es sur les
	 * cases modifi�es o� sur leurs voisines.</p>
	 * <p>Si un MapObject n'autorisant pas le type de case pass� en param�tre est rencontr�, alors la case n'est pas
	 * modifi�e.</p>
	 * 
	 * @param textureNb
	 * 		Le num�ro de la texture.
	 * @param borderTextureNb
	 * 		Le num�ro de la premi�re des huit textures de la bordure.
	 * @param freq
	 * 		La fr�quence d'apparition de la texture. Si ce param�tre est 500, alors il y a une chance sur 500 pour
	 * que la case soit remplac�e.
	 * @param neighbourFreq
	 * 		La fr�quence d'apparition de la texture � c�t� d'une case d�j� modifi�e. Si ce param�tre est 2, alors il
	 * y a une chance sur 2 pour que la case soit remplac�e.
	 * @param type
	 * 		Le type de la case.
	 * @param borderOutside
	 * 		Vrai si les bordures doivent �tre � l'ext�rieur des cases modifi�es.
	 */
	private void generateTerrain(int textureNb, int borderTextureNb, int freq, int neighbourFreq, CaseType type,
			boolean borderOutside) {
		
		for (Case cell : this.carte.cells()) {
			if (cell == null)
				continue;
			if ((int) (Math.random()*freq) == 0) {
				cell.clearTextures();
				cell.replaceTextureNumber(0, textureNb);
				if (type != null)
					cell.setType(type);
			} else {
				for (Orientation or : Orientation.values()) {
					if (cell.nextCell(or) != null) {
						if (cell.nextCell(or).textureNumber(0) == textureNb)
							if ((int) (Math.random()*neighbourFreq) == 0) {
								cell.clearTextures();
								cell.replaceTextureNumber(0, textureNb);
								if (type != null)
									cell.setType(type);
							}
					}
				}
			}
		}
		if (borderOutside) {
			for (Case cell : this.carte.cells()) {
				for (Orientation or : Orientation.values()) {
					if (cell.nextCell(or) != null && cell.textureNumber(0) != textureNb) {
						if (cell.nextCell(or).textureNumber(0) == textureNb) {
							switch (or) {
							case NORD:
								cell.addTextureNumber(borderTextureNb+3);
								break;
							case NORD_EST:
								cell.addTextureNumber(borderTextureNb+7);
								break;
							case NORD_OUEST:
								cell.addTextureNumber(borderTextureNb+4);
								break;
							case SUD:
								cell.addTextureNumber(borderTextureNb+2);
								break;
							case SUD_EST:
								cell.addTextureNumber(borderTextureNb+5);
								break;
							case SUD_OUEST:
								cell.addTextureNumber(borderTextureNb+6);
								break;
							case EST:
								cell.addTextureNumber(borderTextureNb+1);
								break;
							case OUEST:
								cell.addTextureNumber(borderTextureNb);
								break;
							default:
							}
						}
					}
				}
			}
		} else {
			for (Case cell : this.carte.cells()) {
				for (Orientation or : Orientation.values()) {
					if (cell.textureNumber(0) == textureNb && cell.nextCell(or) != null) {
						if (cell.nextCell(or).textureNumber(0) != textureNb) {
							switch (or) {
							case NORD:
								cell.addTextureNumber(borderTextureNb+3);
								break;
							case SUD:
								cell.addTextureNumber(borderTextureNb+2);
								break;
							case EST:
								cell.addTextureNumber(borderTextureNb+1);
								break;
							case OUEST:
								cell.addTextureNumber(borderTextureNb);
								break;
							default:
							}
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * Ajoute un objet � la carte de ce mod�le en respectant certains crit�res.
	 * 
	 * <p>Cette m�thode place les objets pass�s en param�tre sur des cases dont le type n'est dans la liste des types
	 * interdits (forbiddenTypes), et qui n'est pas d�j� occup�e par un autre objet si le bool�en overOtherObjects est
	 * vrai.</p>
	 * <p>Chaque objet a le droit � 100 essais de
	 * placement avant abandon, afin d'�viter les boucles infinies si les crit�res ne peuvent pas �tre respect�s.</p>
	 * <p>Une marge sous forme de couronne carr�e partant de l'ext�rieur peut �tre d�finie (en nombre de cases, param�tre
	 * margin), pour forcer les objets � se placer au centre de la carte. Sur une carte de 50x50 par exemple, une marge
	 * de 5 force l'objet � se placer sur le carr� central 40x40.</p>
	 * 
	 * @param objets
	 * 		La liste des objets � ajouter.
	 * @param forbiddenTypes
	 * 		Les types de cases interdits pour l'objet � placer.
	 * @param overOtherObjects
	 * 		Vrai si l'objet peut �tre plac� sur une case d�j� occup�e par un MapObject.
	 * @param margin
	 * 		Le nombre de cases de distance entre le bord de la carte et la premi�re case possible pour cet objet.
	 * @throws ObjectInstanciationException
	 * 		si la m�thode addObject de la classe Map l�ve une ObjectInstanciationException.
	 */
	private void putMapObjects(ArrayList<MapObject> objets, ArrayList<CaseType> forbiddenTypes, boolean overOtherObjects,
			int margin) throws ObjectInstanciationException {
		
		int col, line, cmpt;
		boolean cnt;
		if (objets == null)
			return;
		for (MapObject o : objets) {
			if (o == null)
				continue;
			cmpt = 0;
			do {
				cnt = false;
				col = margin + (int) (Math.random()*(this.carte.getNbCasesWidth()-2*margin));
				line = margin + (int) (Math.random()*(this.carte.getNbCasesHeight()-2*margin));
				if (!overOtherObjects && this.carte.cell(col, line).occupants().size() > 0)
					cnt = true;
				if (forbiddenTypes != null)
					if (forbiddenTypes.contains(this.carte.cell(col, line).getType()))
						cnt = true;
				cmpt++;
				if (cmpt >= 100)
					break;
			} while (cnt);
			if (cmpt >= 100)
				continue;
			o.displace(col*Constantes.sizeCell, line*Constantes.sizeCell);
			
			this.carte.addObject(o, true);
		}
		
	}
	
	/**
	 * Met � jour l'orientation de Poufalouf en fonction des touches entr�es par l'utilisateur.
	 * 
	 * <p>Si Poufalouf est en standby, le tableau arrowKeys de cette classe est r�initialis� � la valeur AUCUNE pour
	 * toutes ses orientations, puis cette m�thode est quitt�e.</p>
	 * <p>Si la touche fl�ch�e d'orientation pass�e en param�tre est enfonc�e, et si cette orientation n'�tait pas
	 * d�j� dans le tableau arrowKeys de cette classe, Poufalouf en prend l'orientation, et cette
	 * orientation est ajout�e au tableau arrowKeys de cette classe. La vitesse de Poufalouf passe � 1.</p>
	 * <p>Si la touche est rel�ch�e, l'orientation est retir�e
	 * du tableau de touches qui est tri� pour avoir toutes ses orientations diff�rentes de AUCUNE en d�but de tableau.
	 * Poufalouf prend alors la derni�re orientation diff�rente de AUCUNE du tableau. S'il n'y en a pas, il conserve
	 * son orientation. Enfin, si aucune touche directionnelle n'est enfonc�e, la vitesse de Poufalouf est annul�e.</li>
	 * 
	 * @param poufalouf
	 * 		L'instance de Poufalouf � modifier.
	 * @param or
	 * 		L'orientation de la touche fl�ch�e enfonc�e.
	 * @param down
	 * 		Vrai si la touche est enfonc�e, faux si elle est rel�ch�e.
	 */
	private void updatePoufaloufPosition(Poufalouf poufalouf, Orientation or, boolean down) {
		
		if (poufalouf.isStandby()) {
			for (int i=0;i<4;i++)
				this.arrowKeys[i] = Orientation.AUCUNE;
		} else {
			if (down) {
				// on retire l'orientation bloqu�e de la liste pour que la vitesse soit � nouveau mise � 1
				if (poufalouf.getVitesse() == 0 && or == poufalouf.getOrientation()) {
					for (int i=0;i<4;i++) {
						if (this.arrowKeys[i] == or)
							this.arrowKeys[i] = Orientation.AUCUNE;
					}
				}
				for (int i=0;i<4;i++) {
					if (this.arrowKeys[i] == or)
						return;
				}
				poufalouf.setOrientation(or);
				poufalouf.setVitesse(1);
				for (int i=0;i<4;i++) {
					if (this.arrowKeys[i] == Orientation.AUCUNE) {
						this.arrowKeys[i] = or;
						break;
					}
				}
			} else {
				for (int i=0;i<4;i++) {
					if (this.arrowKeys[i] == or) {
						this.arrowKeys[i] = Orientation.AUCUNE;
					}
				}
				// Cette partie sert � placer toutes les orientations du tableau arrowKeys en d�but de tableau
				Orientation o[] = {Orientation.AUCUNE, Orientation.AUCUNE, Orientation.AUCUNE, Orientation.AUCUNE};
				int j = 0;
				for (int i=0;i<4;i++) {
					if (this.arrowKeys[i] != Orientation.AUCUNE) {
						o[j] = this.arrowKeys[i];
						j++;
					}
				}
				for (int i=0;i<4;i++) {
					this.arrowKeys[i] = o[i];
				}
				// Enfin, Poufalouf prend la derni�re orientation du tableau
				if (this.arrowKeys[0] == Orientation.AUCUNE) {
					poufalouf.setVitesse(0);
				} else {
					for (int i=1;i<4;i++) {
						if (this.arrowKeys[i] == Orientation.AUCUNE) {
							poufalouf.setOrientation(this.arrowKeys[i-1]);
							poufalouf.setVitesse(1);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Met � jour la liste des objets Drawable servant � mettre � jour les observateurs de ce mod�le.
	 * 
	 * <p>Efface tous les objets de la liste des Drawable de la classe Model (m�thode clearDrawable() de cette classe),
	 * puis y ajoute toutes les Case suivies des MapObject de la carte, puis de la brume.</p>
	 * <p>Si les bool�ens drawZone de cette classe sont � l'�tat vrai, alors cette m�thode envoie �galement les rectangles
	 * des zones visibles de chaque objet � la liste des Drawable.</p>
	 * <p>Enfin, pour les objets poss�dant une zone de d�tection, si le bool�en showingDetectionRings est � l'�tat vrai,
	 * alors un anneau de d�tection est ajout� sous forme d'image textur�e.</p>
	 */
	@Override
	public void updateObjects() {
		
		this.clearDrawables();

		for (Case cell : this.carte.cells()) {
			this.addDrawable(cell);
		}
		for (MapObject objet : this.carte.objets().values()) {
			this.addDrawable(objet);
			for (ZoneType z : ZoneType.values()) {
				if (this.isDrawing(z))
					this.addDrawable(objet.zone(z));
			}
			if (objet instanceof MapCharacter) {
				int health, maxHealth;
				MapCharacter perso = (MapCharacter) objet;
				health = perso.characteristic(Characteristic.HP);
				maxHealth = perso.characteristic(Characteristic.HPMAX);
				this.addDrawable(new Bar("Barre de vie "+objet.getName(), "res/textures/misc/barreDeVie.png",
						(double) health/maxHealth, objet.getX(),
						objet.getY()-(int) (Constantes.sizeCell*Constantes.sizeTextureFragment)-4-
						objet.getHauteur()*Constantes.sizeHStep,
						Constantes.sizeCell, (int) (Constantes.sizeCell*Constantes.sizeTextureFragment)));
			}
		}
		if (this.showingDetectionRings) {
			for (MapObject o : this.carte.objets().values()) {
				if (o == null)
					continue;
				if (o.zone(ZoneType.DETECTION) != null && o.zone(ZoneType.DETECTION).getObjRect().getW() >= 0) {
					Zone z = o.zone(ZoneType.DETECTION);
					
					TexturedImage t = new TexturedImage("Anneau de d�tection "+o.getName(),
							z.getX()-4*Constantes.sizeCell/3, z.getY()-4*Constantes.sizeCell/3,
							z.getObjRect().getW()+8*Constantes.sizeCell/3,
							z.getObjRect().getH()+8*Constantes.sizeCell/3, "res/textures/misc/anneau.png");
					t.setPlan(Constantes.maxPlan-2);
					this.addDrawable(t);
				}
			}
		}
		
	}
	
	/**
	 * Met � jour tous les objets de la carte, uniquement si leur derni�re mise � jour remonte � plus longtemps
	 * que le temps actuel moins le d�lai de mise � jour de l'objet.
	 * 
	 * <p>Si Poufalouf n'est plus dans le mod�le, alors celui-ci est termin�.</p>
	 * <p>Cette m�thode applique les actions li�es aux contr�les utilisateurs d�termin�s par la m�thode
	 * keyPressed(int) de la classe Model.</p>
	 * <p>Dans un deuxi�me temps, le mod�le s'int�resse aux collisions entre zones des objets (activation d'effets,
	 * collisions, blessures etc...).</p>
	 * <p>Enfin, chaque objet est trait� s�par�ment, et est mis � jour si son compteur temps d�passe la valeur de son
	 * cycle de mise � jour.</p>
	 * <p>Les actions r�alis�es sur chaque objet � mettre � jour sont les suivantes:
	 * <ul>
	 * <li>Reinitialisation du compteur de temps pour la mise � jour suivante.</li>
	 * <li>Mises � jour des cases occup�es par cet objet.</li>
	 * <li>Activation des effets �ventuels de cet objet s'il est activ� (m�thode playEffect() de la classe
	 * MapObject).</li>
	 * <li>Ajout des projectiles de cet objet � la carte s'il s'agit d'un ShootingObject.</li>
	 * <li>Incr�mentation de la frame de l'animation en cours de l'objet.</li>
	 * <li>Mise � jour de l'animation et de l'action de cet objet gr�ce � la m�thode playBehavior() de la classe
	 * MapObject.</li>
	 * <li>Placement des zones de cet objet en fonction de ses coordonn�es (m�thode updateZones() de la classe
	 * MapObject).</li>
	 * <li>Mise � jour de la vitesse de cet objet en fonction de son acc�l�ration. Si la vitesse de cet objet
	 * est nulle, alors son acc�l�ration est remise � z�ro.</li>
	 * <li>V�rification des collisions entre zones.</li>
	 * <li>Si un objet est sur une case WATER, et qu'il ne flotte pas ou ne vole pas, alors une fin d'objet est ajout�e
	 * au mod�le (classe GameObjectEnd), associ�e � cet objet.</li>
	 * </ul></p>
	 * 
	 * <p>Enfin, cette m�thode supprime de la carte tous les objets marqu�s finished.</p>
	 * 
	 * @throws UpdateException
	 * 		si une erreur survient lors de la mise � jour d'un objet.
	 * @throws ObjectInstanciationException
	 * 		si une erreur survient lors de l'ajout d'un objet � la carte de ce mod�le.
	 */
	@Override
	public void update() throws UpdateException, ObjectInstanciationException {
		
		long temps = Sys.getTime();
		
		Poufalouf poufalouf = (Poufalouf) this.carte.objets().get("Poufalouf");
		
		// V�rification des contr�les utilisateurs
		
		if (poufalouf == null) {
			this.terminated = true;
			return;
		}
		
		int usedKeys[] = {Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT, Keyboard.KEY_UP, Keyboard.KEY_DOWN,
				Keyboard.KEY_LMENU, Keyboard.KEY_LCONTROL};
		
		for (int key : usedKeys) {				
			switch (key) {
			case Keyboard.KEY_LEFT:
				if (this.keyPressed(key) == UserEvent.KEY_DOWN || this.keyPressed(key) == UserEvent.KEY_PRESSED)
					this.updatePoufaloufPosition(poufalouf, Orientation.OUEST, true);
				else
					this.updatePoufaloufPosition(poufalouf, Orientation.OUEST, false);
				break;
			case Keyboard.KEY_RIGHT:
				if (this.keyPressed(key) == UserEvent.KEY_DOWN || this.keyPressed(key) == UserEvent.KEY_PRESSED)
					this.updatePoufaloufPosition(poufalouf, Orientation.EST, true);
				else
					this.updatePoufaloufPosition(poufalouf, Orientation.EST, false);
				break;
			case Keyboard.KEY_UP:
				if (this.keyPressed(key) == UserEvent.KEY_DOWN || this.keyPressed(key) == UserEvent.KEY_PRESSED)
					this.updatePoufaloufPosition(poufalouf, Orientation.NORD, true);
				else
					this.updatePoufaloufPosition(poufalouf, Orientation.NORD, false);
				break;
			case Keyboard.KEY_DOWN:
				if (this.keyPressed(key) == UserEvent.KEY_DOWN || this.keyPressed(key) == UserEvent.KEY_PRESSED)
					this.updatePoufaloufPosition(poufalouf, Orientation.SUD, true);
				else
					this.updatePoufaloufPosition(poufalouf, Orientation.SUD, false);
				break;
			case Keyboard.KEY_LMENU:
				if (this.keyPressed(key) == UserEvent.KEY_PRESSED)
					if (poufalouf.getVitesseZ() == 0 && !poufalouf.isHurt()
							&& (!poufalouf.isStandby() || poufalouf.isInvisible()))
						poufalouf.setVitesseZ(1);
				break;
			case Keyboard.KEY_LCONTROL:
				if (this.keyPressed(key) == UserEvent.KEY_PRESSED || this.keyPressed(key) == UserEvent.KEY_DOWN) {
					if (!poufalouf.isStandby())
						((ShootingObject) poufalouf).shoot();
				}
				break;
			}
			
			if (this.keyPressed(key) == UserEvent.KEY_PRESSED)
				this.setPressed(key, UserEvent.KEY_DOWN);
			else if (this.keyPressed(key) == UserEvent.KEY_RELEASED)
				this.setPressed(key, UserEvent.KEY_UP);
			
		}
		
		// Mise � jour des objets
		
		ArrayList<MapObject> moving = new ArrayList<MapObject>(); // liste pour plus tard
		ArrayList<ActivableObject> activs = new ArrayList<ActivableObject>();
		ArrayList<ShootingObject> shooters = new ArrayList<ShootingObject>();
		ArrayList<MapObject> ennemies = new ArrayList<MapObject>();
		
		ArrayList<MapObject> objets = new ArrayList<MapObject>();
		for (MapObject o : this.carte.objets().values()) {
			if (o instanceof MapCharacter || o instanceof Projectile)
				moving.add(o);
			if (o instanceof ActivableObject)
				activs.add((ActivableObject) o);
			if (o instanceof ShootingObject)
				shooters.add((ShootingObject) o);
			if (o instanceof Douni)
				ennemies.add(o);
			if (o.isToUpdate())
				objets.add(o);
		}
		for (MapObject obj : objets) {
			if (obj == null)
				continue;
			if (temps-obj.getCompteurTemps() > obj.getDelayUpdate()) {
				// R�initialisation du compteur de temps
				obj.setCompteurTemps(temps);
				// Mise � jour des cases occup�es
				obj.refreshCells();
				// Mise � jour de l'objet
				if (obj instanceof Landscape)
					((Landscape) obj).shift();
				if (obj instanceof ActivableObject)
					if (((ActivableObject) obj).isActivated())
						((ActivableObject) obj).playEffect();
				if (obj.currentAnim() != null) {
					obj.currentAnim().incrementFrame();
				}
				obj.playBehavior();
				obj.updateZones();
				
				if (obj.getVitesse() > 0)
					obj.setVitesse(Math.max(obj.getVitesse()+obj.getAcceleration(), 0));
				else if (obj.getVitesse() < 0)
					obj.setVitesse(Math.min(obj.getVitesse()+obj.getAcceleration(), 0));
				if (obj.getVitesse() == 0)
					obj.setAcceleration(0);
				
				if (!(obj instanceof Landscape) && obj.currentAnim().getStatus() != Status.DROWNING && !obj.isFlying()
						&& obj.getHauteur() == 0) {
					ArrayList<Case> cases = obj.cells(false);
					boolean onSoil = false;
					boolean inWater = false;
					for (Case c : cases) {
						if (c.getType() == CaseType.WATER) {
							if (obj.zone(ZoneType.COLLISION).isInside(c.zone(ZoneType.EFFECT), true, false))
								inWater = true;
						} else {
							if (obj.zone(ZoneType.COLLISION).isInside(c.zone(ZoneType.EFFECT), false, false))
								onSoil = true;
						}
					}
					if (inWater && !onSoil) {
						obj.stop();
						obj.setStandby(true);
						if (obj instanceof MapCharacter) {
							((MapCharacter) obj).changeCharacteristic(Characteristic.HP, 0);
						}
						obj.changeZone(ZoneType.DETECTION, new Rectangle(-1));
						obj.changeCurrentAnim(Status.DROWNING);
					}
				}
				
				if (obj.currentAnim() != null) {
					String snd = obj.currentAnim().sound();
					if (snd != null && snd != "")
						obj.setSound(snd);
				}
				
				if (obj.isFinished()) {
					this.carte.removeObject(obj.getName());
				}
			}
		}
		
		// V�rification des collisions entre zones
		
		for (ShootingObject o : shooters) {
			for (Projectile p : o.projectiles()) {
				if (p != null && !this.carte.objets().containsKey(p.getName())) {
					p.setCompteurTemps(((MapObject) o).getCompteurTemps());
					this.carte.addObject(p, false);
					moving.add(p);
				}
			}
		}
		for (MapObject o : moving) {
			if (o == null) {
				continue;
			} else if (o.zone(ZoneType.IMAGE) == null) {
				continue;
			} else if (o.zone(ZoneType.EFFECT) == null) {
				continue;
			}
			for (ActivableObject obj : activs) {
				if (o.zone(ZoneType.EFFECT).isInside(((MapObject) obj).zone(ZoneType.EFFECT),
						(obj instanceof Projectile)?true:false, false)) {
					if (!obj.isActivated())
						obj.activate(o);
				}
			}
		}
		
		for (MapObject o : ennemies) {
			if (o == null)
				continue;
			if (poufalouf.zone(ZoneType.IMAGE).isInside(o.zone(ZoneType.DETECTION), false, false) && !poufalouf.isStandby()
					&& !o.isStandby()) {
				poufalouf.stop();
				o.stop();
				o.changeZone(ZoneType.DETECTION, new Rectangle(-1));
				this.setInCombatMode(true);
				this.opponent = o.getName();
				for (int i : usedKeys) {
					this.setPressed(i, UserEvent.KEY_UP);
				}
			}
		}
		
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
	public synchronized HashMap<String, MapObject> objets() {
		
		return (HashMap<String, MapObject>) this.carte.objets().clone();
		
	}
	
	/**
	 * Retourne le personnage rencontr� lors d'une entr�e en mode combat.
	 * 
	 * <p>Si le champ opponent de cette classe ne correspond � aucun Personnage du jeu, alors null est retourn�.</p>
	 * 
	 * @return
	 * 		L'adversaire rencontr�.
	 */
	public MapCharacter opponent() {
		
		MapObject o = this.carte.objets().get(this.opponent);
		if (o instanceof MapCharacter)
			return (MapCharacter) o;
		return null;
		
	}

	/**
	 * Passe le bool�en du tableau drawingZone de cette classe � l'�tat du bool�en pass� en param�tre pour un type de
	 * zone sp�cifi�.
	 * 
	 * @param type
	 * 		Le type de zone � afficher ou non. S'il n'est pas valide, rien n'est fait.
	 * @param drawing
	 * 		Vrai si la zone doit �tre affich�e.
	 */
	public void drawZone(ZoneType type, boolean drawing) {
		
		if (this.drawingZone.get(type) != null)
			this.drawingZone.put(type, new Boolean(drawing));
		
	}
	
	/**
	 * Indique si la zone de type sp�cifi� du tableau drawingZone de cette classe doit �tre affich�e � l'�cran.
	 * 
	 * @param type
	 * 		Le type de la zone.
	 * @return
	 * 		Vrai si la zone sp�cifi�e doit �tre affich�e. Faux sinon ou si le type n'est pas valide.
	 */
	public boolean isDrawing(ZoneType type) {
		
		Boolean b = this.drawingZone.get(type);
		if (b != null)
			return b.booleanValue();
		return false;
		
	}
	
	/**
	 * Met � jour l'�tat du bool�en terminated de cette classe.
	 * 
	 * @param terminated
	 * 		Vrai si ce mod�le doit �tre quitt�.
	 */
	public synchronized void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}
	
	/**
	 * Indique si ce mod�le doit �tre quitt� ou chang�.
	 * 
	 * @return
	 * 		Vrai si ce mod�le doit �tre quitt�.
	 */
	public boolean isTerminated() {
		return this.terminated;
	}
	
	/**
	 * Met � jour l'�tat du bool�en inCombatMode de cette classe.
	 * 
	 * @param inCombatMode
	 * 		Vrai si le jeu est en mode combat.
	 */
	public synchronized void setInCombatMode(boolean inCombatMode) {
		this.inCombatMode = inCombatMode;
	}
	
	/**
	 * Indique si le jeu est en mode combat.
	 * 
	 * @return
	 * 		Vrai si le jeu est en mode combat.
	 */
	public boolean isInCombatMode() {
		return this.inCombatMode;
	}
	
	/**
	 * Met � jour l'�tat du bool�en showingDetectionRings de cette classe.
	 * 
	 * @param b
	 * 		Vrai si les anneaux de d�tection doivent �tre affich�s.
	 */
	public void setShowingDetectionRings(boolean b) {
		this.showingDetectionRings = b;
	}

}
