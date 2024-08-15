package fr.poufalouf.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import fr.poufalouf.InitializationException;
import fr.poufalouf.MissingObjectException;
import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.UpdateException;
import fr.poufalouf.game.GameCharacter;
import fr.poufalouf.tools.Button;
import fr.poufalouf.tools.ButtonList;
import fr.poufalouf.tools.Characteristic;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.GUIElement;
import fr.poufalouf.tools.Model;
import fr.poufalouf.tools.Orientation;
import fr.poufalouf.tools.PoppingObject;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.Screen;
import fr.poufalouf.tools.ScreenType;
import fr.poufalouf.tools.Text;
import fr.poufalouf.tools.TexturedImage;
import fr.poufalouf.tools.UserEvent;

/**
 * Type de mod�le li� � un �cran de combat
 * 
 * <p>Ce mod�le g�re les int�ractions entre les deux adversaires d'un combat au tour par tour.</p>
 * <p>Un combat est divis� en tours dans lesquels les deux opposants peuvent attaquer.</p>
 * 
 * @author Anaïs Vernet
 */
public class CombatModel extends Model {
	
	/**
	 * Le combat g�r� par ce mod�le.
	 */
	private Combat combat;
	/**
	 * Indique si ce mod�le doit �tre quitt� ou chang�.
	 */
	private boolean terminated;
	/**
	 * La table des �crans d'information de ce combat.
	 */
	private HashMap<ScreenType, Screen> screens;
	/**
	 * Le d�lai � attendre avant la prochaine action, en ms.
	 */
	private int delay;
	/**
	 * Le compteur de temps de ce mod�le.
	 */
	private long timeCount;
	/**
	 * Le d�lai � attendre, li� aux s�lections d'objets. Typiquement utilis� pour le d�lai d'attente avant le d�filement
	 * lors du maintien d'une touche enfonc�e.
	 */
	private int delaySelection;
	/**
	 * Le compteur de temps de ce mod�le li� aux s�lections d'objets. Typiquement utilis� pour le d�lai d'attente avant
	 * le d�filement lors du maintien d'une touche enfonc�e.
	 */
	private long timeCountSelection;
	/**
	 * La phase actuelle du combat.
	 */
	private CombatPhase currentPhase;
	/**
	 * Indique si le tour de jeu est au joueur.
	 */
	private boolean playerPlaying;
	/**
	 * Indique si on est en d�but de tour (un tour �tant compos� de l'action des deux opposants).
	 */
	private boolean startOfTurn;
	/**
	 * Le texte de l'�cran sup�rieur (ligne 1).
	 */
	private String texteLigne1;
	/**
	 * Le texte de l'�cran sup�rieur (ligne 2).
	 */
	private String texteLigne2;
	/**
	 * L'�cran actuellement actif.
	 */
	private ScreenType activeScreen;
	/**
	 * Le nom de l'action en cours.
	 */
	private String actionName;
	
	/**
	 * La liste des noms des boutons disponibles lors du combat.
	 */
	private static final String[] buttonsNames = {"Attaque", "Objet 1", "Objet 2"};
	
	/**
	 * Constructeur CombatModel.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>combat : le combat est initialis� dans la m�thode initialize(LoadingScreen) de cette classe, appel�e par
	 * le constructeur de la classe m�re. Si le combat n'est pas initialis� pour un quelconque raison, alors un nouveau
	 * combat standard est cr��.</li>
	 * <li>terminated : false.</li>
	 * <li>screens : une nouvelle table (HashMap) de Screen (initialis�e dans la m�thode initialize(LoadingScreen) de
	 * cette classe).</li>
	 * <li>delay : 0.</li>
	 * <li>timeCount : 0.</li>
	 * <li>delaySelection : 0.</li>
	 * <li>timeCountSelection : 0.</li>
	 * <li>currentPhase : START.</li>
	 * <li>playerPlaying : vrai.</li>
	 * <li>startOfTurn : vrai.</li>
	 * <li>texteLigne1 : "".</li>
	 * <li>texteLigne2 : "".</li>
	 * <li>activeScreen : ScreenType.PLAYER.</li>
	 * <li>actionName : "".</li>
	 * </ul></p>
	 * 
	 * @param param
	 * 		Les points de vie du joueur et de l'adversaire respectivement.
	 * @throws InitializationException
	 * 		si le constructeur de la classe m�re l�ve une InitializationException.
	 */
	public CombatModel(ArrayList<Object> param) throws InitializationException {
		
		super(param);
		
		if (this.combat == null)
			try {
				this.combat = new Combat(null, null);
			} catch (ObjectInstanciationException e) {
				throw new InitializationException("Erreur lors de l'initialisation de l'�cran de combat.", e);
			}
		this.terminated = false;
		if (this.screens == null)
			this.screens = new HashMap<ScreenType, Screen>();
		this.delay = 0;
		this.timeCount = 0;
		this.delaySelection = 0;
		this.timeCountSelection = 0;
		this.currentPhase = CombatPhase.START;
		this.playerPlaying = true;
		this.startOfTurn = true;
		this.texteLigne1 = "";
		this.texteLigne2 = "";
		this.activeScreen = ScreenType.PLAYER;
		this.actionName = "";
		
	}
	
	/**
	 * Ajoute un d�lai d'attente avant la prochaine action de la m�thode playTurn() de cette classe.
	 * 
	 * <p>Durant ce d�lai, la m�thode playTurn() se termine d�s son appel.</p>
	 * 
	 * @param time
	 * 		Le d�lai d'attente.
	 */
	private void delay(int time) {
		
		this.delay = time;
		this.timeCount = Sys.getTime();
		
	}
	
	/**
	 * Ajoute un d�lai d'attente avant la prochaine s�lection de la m�thode playTurn() de cette classe.
	 * 
	 * <p>Utilis� pour le d�lai d'attente avant le d�filement lors du maintien d'une touche enfonc�e.</p>
	 * 
	 * @param time
	 * 		Le d�lai d'attente.
	 */
	private void delaySelection(int time) {
		
		this.delaySelection = time;
		this.timeCountSelection = Sys.getTime();
		
	}
	
	/**
	 * Retourne l'�cran actif.
	 * 
	 * @return
	 * 		L'�cran actif, et jamais null.
	 * @throws MissingObjectException
	 * 		si l'�cran actif est manquant.
	 */
	private Screen activeScreen() throws MissingObjectException {
		
		Screen sc = this.screens.get(this.activeScreen);
		if (sc == null)
			throw new MissingObjectException("L'�cran "+this.activeScreen.toString()+" est manquant.");
		return sc;
		
	}
	
	/**
	 * Joue un tour de combat.
	 * 
	 * <p>Cette m�thode ne fait rien si un d�lai est en train de s'�couler, ou si l'un des personnages est en train
	 * d'attaquer (m�thode currentAttack() de la classe CombatCharacter retournant une Attack non null), ou en train
	 * d'utiliser un objet (m�thode currentItemUse() de la classe CombatCharacter).</p>
	 * 
	 * <p>Un tour est s�par� en phases, rep�r�es par l'�num�ration CombatPhase :
	 * <ul>
	 * <li>START : si on est en d�but de tour (les deux opposants ont jou�, ou en d�but de combat), un nombre al�atoire
	 * est tir� entre 1 et la somme des vitesses des deux opposants. Si le nombre tir� est sup�rieur � la vitesse du
	 * joueur, alors l'initiative passe � l'adversaire. Sinon, c'est le joueur qui attaquera en premier pour ce nouveau
	 * tour.</li>
	 * <li>CHOICE : phase de s�lection d'actions. Le joueur peut utiliser des objets ou d�cider d'entrer en phase
	 * d'attaque.</li>
	 * <li>ATTACK : le personnage dont c'est le tour peut tenter de lancer une attaque.</li>
	 * <li>ACTION : phase interm�diaire durant laquelle un personnage lance une action. L'action est d�termin�e par
	 * son type et par son nom s�par�s par un '|' (ex : Attack|COUP DE POING).</li>
	 * </ul></p>
	 * 
	 * @throws MissingObjectException
	 * 		si un �cran est manquant, ou si une action a un nom invalide.
	 */
	private void playTurn() throws MissingObjectException {
		
		long temps = Sys.getTime();
		if (temps-this.timeCount < this.delay)
			return;
		this.delay = 0;
		CombatCharacter poufalouf = this.player();
		CombatCharacter opponent = this.opponent();
		if (poufalouf == null || opponent == null)
			throw new MissingObjectException("L'un des personnages est manquant.");
		if (poufalouf.isActing() || opponent.isActing())
			return;
		
		ButtonList bl;
		Button b;
		GUIElement gui;
		
		switch (this.currentPhase) {
		case START:
			this.texteLigne1 = "";
			this.texteLigne2 = "";
			if (this.startOfTurn) {
				int vitessePf = poufalouf.characteristic(Characteristic.SPEED);
				int vitesseOpp = opponent.characteristic(Characteristic.SPEED);
				int hsd = (int) (Math.random()*(vitessePf+vitesseOpp))+1;
				this.playerPlaying = (hsd <= vitessePf);
				this.startOfTurn = false;
			} else
				this.startOfTurn = true;
			this.currentPhase = CombatPhase.CHOICE;
			this.activeScreen = ScreenType.PLAYER;
			if (this.playerPlaying) {
				this.activeScreen().enableAll();
				this.activeScreen().select(0, 0);
			}
			else
				this.activeScreen().disableAll();
			break;
		case CHOICE:
			if (this.playerPlaying) {
				this.texteLigne1 = "";
				this.texteLigne2 = "";
				if (this.activeScreen == ScreenType.PLAYER) {
					gui = this.activeScreen().selectedElement();
					if (gui instanceof Button) {
						b = (Button) gui;
						if (b.isActivated()) {
							if (b.getName() == "Attaque") {
								this.currentPhase = CombatPhase.ATTACK;
								this.activeScreen = ScreenType.ATTACK;
							} else if (b.getName() == "Objet 1" || b.getName() == "Objet 2") {
								this.activeScreen = ScreenType.OBJECTS;
							}
						}
					} else
						throw new MissingObjectException("La s�lection de l'�cran "+this.activeScreen.toString()+
								" n'est pas un bouton.");
				} else {
					gui = this.activeScreen().selectedElement();
					if (!(gui instanceof ButtonList))
						throw new MissingObjectException("L'�cran "+this.activeScreen.toString()+
								" ne contient pas la liste des objets.");
					bl = (ButtonList) gui;
					b = bl.activatedButton();
					if (b != null) {
						this.activeScreen = ScreenType.PLAYER;
						b.desactivate();
						this.activeScreen().selectedElement().disable();
						this.texteLigne1 = "Poufalouf utilise";
						this.texteLigne2 = b.getName();
						this.currentPhase = CombatPhase.ACTION;
						this.actionName = "Object|"+b.getName();
						this.delay(500);
					}
				}
			} else {
				this.currentPhase = CombatPhase.ATTACK;
				for (int i=0;i<opponent.items().size();i++)
				{
					int hsd = (int) (Math.random()*3);
					if (hsd == 0) {
						this.texteLigne1 = opponent.getName() + " utilise";
						this.texteLigne2 = opponent.items().get(i).getName();
						this.currentPhase = CombatPhase.ACTION;
						this.actionName = "Object|"+opponent.items().get(i).getName();
						this.delay(500);
						break;
					}
				}
				
			}
			break;
		case ATTACK:
			if (this.playerPlaying) { // tour du joueur
				gui = this.activeScreen().selectedElement();
				if (!(gui instanceof ButtonList))
					throw new MissingObjectException("L'�cran "+this.activeScreen.toString()+
							"ne contient pas la liste des attaques.");
				bl = (ButtonList) gui;
				this.activeScreen = ScreenType.ATTACK;
				b = bl.activatedButton();
				if (b != null) {
					this.activeScreen = ScreenType.PLAYER;
					b.desactivate();
					this.activeScreen().selectedElement().disable();
					this.texteLigne1 = "Poufalouf attaque";
					this.texteLigne2 = b.getName();
					this.currentPhase = CombatPhase.ACTION;
					this.actionName = "Attack|"+b.getName();
					this.delay(500);
				}
			} else { // tour de l'adversaire
				ArrayList<String> attacks = opponent.attacks();
				int hsd = (int) (Math.random()*attacks.size());
				this.texteLigne1 = "Douni attaque";
				this.texteLigne2 = attacks.get(hsd);
				this.currentPhase = CombatPhase.ACTION;
				this.actionName = "Attack|"+this.texteLigne2;
				this.delay(500);
			}
			break;
		case ACTION:
			String strs[] = this.actionName.split("\\|");
			if (strs.length < 2)
				this.currentPhase = CombatPhase.CHOICE;
			else {
				if (strs[0].equals("Attack")) {
					if (this.playerPlaying) {
						if (Attack.succeed(poufalouf, opponent))
							poufalouf.useAttack(strs[1]);
						else {
							this.texteLigne1 = "Echec";
							this.texteLigne2 = "";
							this.delay = 2000;
						}
					} else {
						if (Attack.succeed(opponent, poufalouf))
							opponent.useAttack(strs[1]);
						else {
							this.texteLigne1 = "Echec";
							this.texteLigne2 = "";
							this.delay = 2000;
						}
					}
					this.currentPhase = CombatPhase.START;
					this.playerPlaying = !this.playerPlaying;
				} else if (strs[0].equals("Object")) {
					if (this.playerPlaying)
						poufalouf.useItem(strs[1]);
					else
						opponent.useItem(strs[1]);
					this.currentPhase = CombatPhase.CHOICE;
				} else
					throw new MissingObjectException("Action inconnue : "+this.actionName);
				this.actionName = "";
			}
			break;
		}
		
		if (poufalouf.isActing() || opponent.isActing())
			return;
		int usedKeys[] = {Keyboard.KEY_RETURN, Keyboard.KEY_LCONTROL,
				Keyboard.KEY_UP, Keyboard.KEY_RIGHT, Keyboard.KEY_DOWN, Keyboard.KEY_LEFT};
		gui = this.activeScreen().selectedElement();
		if (gui == null)
			throw new MissingObjectException("Aucun bouton s�lectionn� sur l'�cran "+this.activeScreen.toString()+".");
		if (!gui.isInTransition()) {
			for (int key : usedKeys) {
				switch (key) {
				case Keyboard.KEY_RETURN:
					if (this.keyPressed(key) == UserEvent.KEY_PRESSED) {
						this.activeScreen().activateSelection();
						this.addSound("res/sounds/misc/activation.wav");
					}
					break;
				case Keyboard.KEY_LCONTROL:
					if (this.keyPressed(key) == UserEvent.KEY_PRESSED) {
						if (this.activeScreen == ScreenType.OBJECTS) {
							this.activeScreen = ScreenType.PLAYER;
							gui = this.activeScreen().selectedElement();
							gui.desactivate();
							this.addSound("res/sounds/misc/cancel.wav");
						}
					}
					break;
				case Keyboard.KEY_LEFT:
					if (this.keyPressed(key) == UserEvent.KEY_PRESSED || this.keyPressed(key) == UserEvent.KEY_DOWN) {
						if (this.keyPressed(key) == UserEvent.KEY_DOWN) {
							if (temps-this.timeCountSelection < this.delaySelection)
								break;
							this.delaySelection(200);
						} else
							this.delaySelection(500);
						if (this.activeScreen().changeSelection(Orientation.OUEST))
							this.addSound("res/sounds/misc/ting.wav");
					}
					break;
				case Keyboard.KEY_RIGHT:
					if (this.keyPressed(key) == UserEvent.KEY_PRESSED || this.keyPressed(key) == UserEvent.KEY_DOWN) {
						if (this.keyPressed(key) == UserEvent.KEY_DOWN) {
							if (temps-this.timeCountSelection < this.delaySelection)
								break;
							this.delaySelection(200);
						} else 
							this.delaySelection(500);
						if (this.activeScreen().changeSelection(Orientation.EST))
							this.addSound("res/sounds/misc/ting.wav");
					}
					break;
				case Keyboard.KEY_DOWN:
					if (this.keyPressed(key) == UserEvent.KEY_PRESSED || this.keyPressed(key) == UserEvent.KEY_DOWN) {
						if (this.keyPressed(key) == UserEvent.KEY_DOWN) {
							if (temps-this.timeCountSelection < this.delaySelection)
								break;
							this.delaySelection(200);
						} else 
							this.delaySelection(500);
						if (this.activeScreen().changeSelection(Orientation.SUD))
							this.addSound("res/sounds/misc/ting.wav");
					}
					break;
				case Keyboard.KEY_UP:
					if (this.keyPressed(key) == UserEvent.KEY_PRESSED
							|| this.keyPressed(key) == UserEvent.KEY_DOWN) {
						if (this.keyPressed(key) == UserEvent.KEY_DOWN) {
							if (temps-this.timeCountSelection < this.delaySelection)
								break;
							this.delaySelection(200);
						} else 
							this.delaySelection(500);
						if (this.activeScreen().changeSelection(Orientation.NORD))
							this.addSound("res/sounds/misc/ting.wav");
					}
					break;
				}
				
				if (this.keyPressed(key) == UserEvent.KEY_PRESSED)
					this.setPressed(key, UserEvent.KEY_DOWN);
				else if (this.keyPressed(key) == UserEvent.KEY_RELEASED)
					this.setPressed(key, UserEvent.KEY_UP);
			}
		}
		
	}
	
	/**
	 * V�rifie que les deux premiers param�tres de la liste sont des GameCharacter.
	 * 
	 * @return
	 * 		Vrai si les param�tres sont corrects.
	 */
	@Override
	public boolean checkParameters(ArrayList<Object> param) {
		
		if (param.size() < 2)
			return false;
		if (!(param.get(0) instanceof GameCharacter) || !(param.get(1) instanceof GameCharacter))
			return false;
		return true;
		
	}

	/**
	 * Initialise ce mod�le.
	 * 
	 * <p>Apr�s avoir g�n�r� un �cran de combat, les d�cors de l'image de fond sont ajout�s.</p>
	 * 
	 * @param param
	 * 		Les points de vie du joueur et de l'adversaire respectivement.
	 * @throws InitializationException
	 * 		si une erreur est lev�e par le constructeur Combat, ou pendant l'initialisation des param�tres n�cessaires
	 * � celui-ci.
	 */
	@Override
	public void initialize(ArrayList<Object> param) throws InitializationException {
		
		this.setMusique("res/music/combat.ogg");
		
		// Combat et personnages
		
		ArrayList<Attack> attacksP = new ArrayList<Attack>();
		ArrayList<Attack> attacksO = new ArrayList<Attack>();
		ArrayList<CombatItem> itemsP = new ArrayList<CombatItem>();
		ArrayList<CombatItem> itemsO = new ArrayList<CombatItem>();
		
		try {
			this.combat = new Combat(new CombatCharacter((GameCharacter) param.get(0), "Poufalouf", Orientation.SUD_EST,
							new Rectangle(0.7*Constantes.initialDisplayWidth, 0.6*Constantes.initialDisplayHeight,
									0.33*Constantes.initialDisplayWidth, 0.33*Constantes.initialDisplayHeight)),
					new CombatCharacter((GameCharacter) param.get(1), "Douni", Orientation.NORD_OUEST,
							new Rectangle(0.25*Constantes.initialDisplayWidth, 0.3*Constantes.initialDisplayHeight,
									0.25*Constantes.initialDisplayWidth, 0.25*Constantes.initialDisplayHeight)));
			attacksP = Attack.createAttacksFromFile(this.player(), this.opponent(),
					((GameCharacter) param.get(0)).getAttacksNames());
			attacksO = Attack.createAttacksFromFile(this.opponent(), this.player(),
					((GameCharacter) param.get(1)).getAttacksNames());
			Set<String> set = ((GameCharacter) param.get(0)).getObjects().keySet();
			ArrayList<String> array = new ArrayList<String>();
			for (String s : set) {
				array.add(s);
			}
			itemsP = CombatItem.createCombatItemsFromFile(this.player(), this.opponent(), array);
			array.clear();
			set = ((GameCharacter) param.get(1)).getObjects().keySet();
			for (String s : set) {
				array.add(s);
			}
			itemsO = CombatItem.createCombatItemsFromFile(this.opponent(), this.player(), array);
			for (Attack a : attacksP)
				this.player().addAttack(a);
			for (Attack a : attacksO)
				this.opponent().addAttack(a);
			for (CombatItem it : itemsP)
				this.player().addCombatItem(it);
			for (CombatItem it : itemsO)
				this.opponent().addCombatItem(it);
			this.player().setOpponent(this.opponent());
			this.opponent().setOpponent(this.player());
			this.player().setHeartCenterX(0.5);
			this.player().setHeartCentreY(0.5);
			this.player().setShootCenterX(0.4);
			this.player().setShootCenterY(0.4);
			this.opponent().setHeartCenterX(0.6);
			this.opponent().setHeartCentreY(0.65);
			this.opponent().setShootCenterX(0.8);
			this.opponent().setShootCenterY(0.8);
		} catch (Exception e) {
			throw new InitializationException("Erreur lors de l'initialisation de l'�cran de combat.", e);
		}
		
		// Ecrans
		
		this.screens = new HashMap<ScreenType, Screen>();
		Rectangle r;
		CombatCharacter player = this.combat.player();
		CombatCharacter opponent = this.combat.opponent();
		if (player == null || opponent == null)
			throw new InitializationException("Erreur lors de l'initialisation de l'�cran de combat, personnages non" +
					" d�finis.");
		if (player.getX() > Constantes.initialDisplayWidth/2)
			r = new Rectangle(player.getX()-Constantes.sizeScreenW, player.getY()+player.getH()/2-Constantes.sizeScreenH/2,
					Constantes.sizeScreenW, Constantes.sizeScreenH);
		else
			r = new Rectangle(player.getX()+player.getW(), player.getY()+player.getH()/2-Constantes.sizeScreenH/2,
					Constantes.sizeScreenW, Constantes.sizeScreenH);
		Screen s = new Screen("Ecran "+player.getName(), "res/textures/misc/ecran.png", r);
		try {
			s.addDrawable(new Button(CombatModel.buttonsNames[0], 0, 0, 0.2, 0.4, "res/scripts/animations/divers/" +
			"bouton_attaque.txt", ""), Orientation.AUCUNE, 0);
			s.addDrawable(new Button(CombatModel.buttonsNames[1], 0.6, -0.2, 0.15, 0.3,
			"res/scripts/animations/divers/bouton_objet.txt", ""), Orientation.AUCUNE, 0);
			s.addDrawable(new Button(CombatModel.buttonsNames[2], 0.8, -0.2, 0.15, 0.3,
			"res/scripts/animations/divers/bouton_objet.txt", ""), Orientation.AUCUNE, 0);
		} catch (Exception e) {
			throw new InitializationException("Erreur lors de l'ajout d'un bouton.", e);
		}
		this.screens.put(ScreenType.PLAYER, s);
		if (opponent.getX() > Constantes.initialDisplayWidth/2)
			r = new Rectangle(opponent.getX()-Constantes.sizeScreenW,
					opponent.getY()+opponent.getH()/2-Constantes.sizeScreenH/2,
					Constantes.sizeScreenW, Constantes.sizeScreenH);
		else
			r = new Rectangle(opponent.getX()+opponent.getW(), opponent.getY()+opponent.getH()/2-Constantes.sizeScreenH/2,
					Constantes.sizeScreenW, Constantes.sizeScreenH);
		this.screens.put(ScreenType.OPPONENT, new Screen("Ecran "+opponent.getName(), "res/textures/misc/ecran.png", r));
		r = new Rectangle(10, 10, Constantes.initialDisplayWidth-20, 80);
		this.screens.put(ScreenType.GENERAL, new Screen("Ecran g�n�ralit�s", "res/textures/misc/ecranLong.png", r));
		double rmin = Math.min(Constantes.initialDisplayWidth/2, Constantes.initialDisplayHeight/2);
		r = new Rectangle(Constantes.initialDisplayWidth/2-rmin/2, Constantes.initialDisplayHeight/2-rmin/2,
				rmin, rmin);
		s = new Screen("Ecran d'attaque", "res/textures/misc/ecran.png", r);
		ArrayList<String> attacksNamesP = new ArrayList<String>();
		for (int i=0;i<attacksP.size();i++) {
			attacksNamesP.add(attacksP.get(i).getName());
		}
		try {
			s.addDrawable(new ButtonList("Attaques", 0, 0, 1, 0.6, attacksNamesP, attacksNamesP,
					"res/scripts/animations/divers/bouton_liste.txt", 4, 1), Orientation.CENTRE, 0);
		} catch (Exception e) {
			throw new InitializationException("Erreur lors de l'ajout de l'�cran de la liste des attaques.", e);
		}
		this.screens.put(ScreenType.ATTACK, s);
		s = new Screen("Ecran d'objets", "res/textures/misc/ecran.png", r);
		try {
			s.addDrawable(new ButtonList("Objets", 0, 0, 0.8, 0.8, "res/scripts/animations/divers/bouton_liste_petit.txt",
					4, 4, null, null), Orientation.CENTRE, 0);
		} catch (Exception e) {
			throw new InitializationException("Erreur lors de l'ajout de l'�cran de la liste des objets.", e);
		}
		this.screens.put(ScreenType.OBJECTS, s);
		
		// D�cors
		
		this.combat.addObject(new CombatDecor("D�cor Lune", Orientation.AUCUNE,
				new Rectangle(Constantes.initialDisplayWidth, Constantes.initialDisplayHeight),
				"res/textures/combat/terrain/pumpkins/lune.png", 0));
		CombatDecor d = new CombatDecor("D�cor Ciel", Orientation.EST,
				new Rectangle(Constantes.initialDisplayWidth, Constantes.initialDisplayHeight),
				"res/textures/combat/terrain/pumpkins/nuages.png", 1);
		d.setDelayUpdate(120);
		this.combat.addObject(d);
		this.combat.addObject(new CombatDecor("D�cor Terrain", Orientation.AUCUNE,
				new Rectangle(Constantes.initialDisplayWidth, Constantes.initialDisplayHeight),
				"res/textures/combat/terrain/pumpkins/field.png", 0));

	}

	/**
	 * Met � jour la liste des objets Drawable de ce mod�le de combat.
	 * 
	 * <p>Cette m�thode supprime tous les drawables pr�sents, puis r�alise les actions suivantes :
	 * <ul>
	 * <li>Chaque CombatObject du combat est ajout� � la liste.</li>
	 * <li>Si cet objet est un CombatCharacter, alors l'animation de son attaque actuelle (m�thode currentAttack() de la
	 * classe CombatCharacter) est ajout�e � la liste.</li>
	 * <li>Les �crans PLAYER et OPPONENT sont mis � jour et ajout�s � la liste des Drawable.</li>
	 * <li>L'�cran GENERAL est ajout� seulement si au moins l'une des deux lignes texteLigne1 et texteLigne2 n'est pas
	 * vide.</li>
	 * <li>L'�cran ATTACK est ajout� si la liste des attaques est visible.</li>
	 * <li>L'�cran OBJECTS est ajout� si la liste des objets est visible. Un objet pop-up est ajout� � chaque bouton de
	 * la liste de cet �cran pour indiquer la quantit� poss�d�e de chaque objet. La liste des boutons de la liste est
	 * mise � jour en fonction des objets poss�d�s.</li>
	 * </ul></p>
	 * @throws UpdateException
	 * 		si une exception est lev�e lors de la mise � jour.
	 */
	@Override
	public void updateObjects() throws UpdateException {
		
		this.clearDrawables();
		
		Collection<CombatObject> objs = this.combat.objets().values();
		for (CombatObject o : objs) {
			if (o == null)
				continue;
			this.addDrawable(o);
		}
		for (ScreenType type : ScreenType.values()) {
			Screen s;
			if ((s=this.screens.get(type)) != null) {
				s.clearText();
				switch(type) {
				case PLAYER:
					s.addDrawable(new Text("PV: "+this.player().characteristic(Characteristic.HP)+"/"
							+this.player().characteristic(Characteristic.HPMAX),
							((double) this.player().characteristic(Characteristic.HP)
									/this.player().characteristic(Characteristic.HPMAX)
									<0.1)?Color.red:Color.green,
							0.08, 0.1, "res/textures/misc/lettres.png", 0, 0),
							Orientation.OUEST, 0);
					s.addDrawable(new Text("POUFALOUF", Color.cyan, 0.08, 0.1, "res/textures/misc/lettres.png",
							0, 0), Orientation.NORD_EST, 0);
					this.addDrawable(s);
					break;
				case OPPONENT:
					s.addDrawable(new Text("PV: "+this.opponent().characteristic(Characteristic.HP)+"/"
							+this.player().characteristic(Characteristic.HPMAX),
							((double) this.opponent().characteristic(Characteristic.HP)
									/this.opponent().characteristic(Characteristic.HPMAX)
									<0.1)?Color.red:Color.green,
							0.08, 0.1, "res/textures/misc/lettres.png", 0, 0),
							Orientation.OUEST, 0);
					s.addDrawable(new Text("DOUNI", Color.cyan, 0.08, 0.1, "res/textures/misc/lettres.png",
							0, 0), Orientation.NORD_EST, 0);
					this.addDrawable(s);
					break;
				case GENERAL:
					if (!this.texteLigne1.equals("") || !this.texteLigne2.equals("")) {
						s.addDrawable(new Text(this.texteLigne1, Color.white,
								0.05, 0.15, "res/textures/misc/lettres.png",
								0.5-0.025*this.texteLigne1.length(), 0.25), 
								(this.texteLigne2.equals(""))?Orientation.CENTRE:Orientation.AUCUNE, 0);
						s.addDrawable(new Text(this.texteLigne2, Color.magenta,
								0.05, 0.2, "res/textures/misc/lettres.png",
								0.5-0.025*this.texteLigne2.length(), 0.55), Orientation.AUCUNE, 0);
						this.addDrawable(s);
					}
					break;
				case ATTACK:
					if (this.activeScreen == ScreenType.ATTACK)
						this.addDrawable(s);
					break;
				case OBJECTS:
					GUIElement gui= s.selectedElement();
					if (gui instanceof ButtonList) {
						ButtonList bl = (ButtonList) gui;
						ArrayList<TexturedImage> itemsImagesP = new ArrayList<TexturedImage>();
						ArrayList<String> itemsNamesP = new ArrayList<String>();
						for (int i=0;i<this.player().items().size();i++) {
							itemsImagesP.add(new TexturedImage("Image objet "+this.player().items().get(i).getName(),
									0.1, 0.1, 0.8, 0.8, this.player().items().get(i).getTextureFile()));
							itemsNamesP.add(this.player().items().get(i).getName());
						}
						try {
							bl.changeButtons("res/scripts/animations/divers/bouton_liste_petit.txt",
									itemsImagesP, itemsNamesP);
						} catch (Exception e) {
							throw new UpdateException("Erreur lors de la mise � jour de la liste des objets.", e);
						}
						Button b;
						for (int i=0;i<bl.buttons().size();i++) {
							b = bl.buttons().get(i);
							if (b != null) {
								String text = "x"+this.player().itemQuantity(this.player().items().get(i).getName());
								Text t = new Text(text, new Color(217, 30, 255), 0.2, 0.2, "res/textures/misc/lettres.png",
										1-text.length()*0.25, 0.7);
								Rectangle[] r = new Rectangle[]{new Rectangle(0, 0, 1, 1)};
								b.addPopup(new PoppingObject(t, r, 1));
							}
						}
					}
					if (this.activeScreen == ScreenType.OBJECTS)
						this.addDrawable(s);
					break;
				}
			}
		}

	}

	/**
	 * Met � jour ce mod�le.
	 * 
	 * <p>Les actions suivantes sont r�alis�es :
	 * <ul>
	 * <li>La m�thode playTurn() de cette classe est appel�e.</li>
	 * <li>Pour chaque CombatObject du combat, leur m�thode playBehavior() est appel�e. S'il s'agit d'un CombatCharacter,
	 * alors l'animation de celui-ci est pr�alablement incr�ment�e d'une frame. S'il s'agit d'un CombatDecor, alors sa
	 * m�thode shift() est pr�alablement appel�e.</li>
	 * <li>Tous les �crans de la liste de cette classe sont mis � jour (m�thode update() de la classe Screen).</li>
	 * <li>Si les PV de l'un des combattants sont � z�ro, alors ce mod�le est termin�.</li>
	 * </ul></p>
	 * @throws UpdateException
	 * 		si la m�thode playBehavior d'un objet l�ve une UpdateException.
	 */
	@Override
	public void update() throws UpdateException {
		
		long time = Sys.getTime();
		
		try {
			this.playTurn();
		} catch (Exception e) {
			throw new UpdateException("Impossible de jouer le tour de combat.", e);
		}
		
		Collection<CombatObject> objs = this.combat.objets().values();
		for (CombatObject o : objs) {
			if (o == null)
				continue;
			if (time-o.getCompteurTemps() >= o.getDelayUpdate()) {
				o.setCompteurTemps(time);
				
				if (o.currentAnim() != null) {
					String snd = o.currentAnim().sound();
					if (snd != null && snd != "")
						o.setSound(snd);
					o.currentAnim().incrementFrame();
				}
				o.playBehavior();
				if (o instanceof CombatEffect) {
					if (o.currentAnim() != null) {
						if (o.currentAnim().getTimer() == 0)
							o.setFinished(true);
					}
				}
				if (o instanceof CombatCharacter) {
					Move m = ((CombatCharacter) o).currentAttack();
					if (m != null) {
						if (!this.combat.objets().containsKey(m.getAnim().getName()))
							if (!m.getAnim().isFinished())
								this.combat.addObject(m.getAnim());
					}
					m = ((CombatCharacter) o).currentItemUse();
					if (m != null) {
						if (!this.combat.objets().containsKey(m.getAnim().getName()))
							if (!m.getAnim().isFinished())
								this.combat.addObject(m.getAnim());
					}
				}
				if (o instanceof CombatDecor) {
					((CombatDecor) o).shift();
				}
				
				if (o.isFinished()) {
					this.combat.removeObject(o);
				}
			}
		}
		
		for (Screen s : this.screens.values()) {
			s.update();
		}
		
		int healthOpp = this.opponent().characteristic(Characteristic.HP);
		int healthPf = this.player().characteristic(Characteristic.HP);
		if (healthOpp == 0 || healthPf == 0) {
			this.setTerminated(true);
		}

	}
	
	/**
	 * Retourne le personnage jou� par le joueur.
	 * 
	 * @return
	 * 		Le personnage du joueur.
	 */
	public CombatCharacter player() {
		
		return this.combat.player();
		
	}
	
	/**
	 * Retourne le personnage jou� par l'adversaire.
	 * 
	 * @return
	 * 		Le personnage de l'adversaire.
	 */
	public CombatCharacter opponent() {
		
		return this.combat.opponent();
		
	}
	
	/**
	 * Met � jour l'�tat du bool�en terminated de cette classe.
	 * 
	 * @param terminated
	 * 		Vrai si ce mod�le doit �tre quitt�.
	 */
	public void setTerminated(boolean terminated) {
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

}
