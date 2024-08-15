package fr.poufalouf;

import java.io.IOException;
import java.util.ArrayList;

import fr.poufalouf.combat.CombatCharacter;
import fr.poufalouf.combat.CombatControl;
import fr.poufalouf.combat.CombatModel;
import fr.poufalouf.map.Douni;
import fr.poufalouf.map.MapCamera;
import fr.poufalouf.map.MapControl;
import fr.poufalouf.map.MapModel;
import fr.poufalouf.map.MapObject;
import fr.poufalouf.map.Poufalouf;
import fr.poufalouf.tools.Camera;
import fr.poufalouf.tools.Characteristic;
import fr.poufalouf.tools.LoadingScreen;
import fr.poufalouf.tools.ScriptReader;
import fr.poufalouf.tools.View;

/**
 * Classe principale du projet Poufalouf
 * 
 * <p>Ce projet a pour but de tester les possibilit�s offertes par OpenGL et LWJGL pour cr�er un jeu en 2D.
 * C'est �galement l'occasion de s'entra�ner � programmer un jeu fonctionnel en pr�vision de la r�alisation de Poufalouf 
 * et les Poulpes Cosmiques.</p>
 * 
 * <b>Vocabulaire et conventions</b>
 * 
 * <p>Le Display fait r�f�rence au programme de rendu OpenGL qui affiche les images du jeu. Dans la suite de la
 * documentation, il est possible de l'assimiler � la fen�tre du programme.</p>
 * <p>Quand il est fait mention de pixels, il s'agit d'unit�s d'affichage relatives � la taille
 * initiale du Display. Si celui-ci change de dimensions, les pixels sont �galement redimensionn�s.</p>
 * <p>La largeur et la hauteur d�signent les dimensions �quivalentes du point de vue de l'�cran. Pour une carte 2D 
 * vue de haut par exemple, la hauteur sera similaire � la direction Nord-Sud sur la carte.</p>
 * <p>Les coordonn�es et les dimensions d'objets ou d'images peuvent �tre mentionn�es par le terme de rectangle de
 * coordonn�es. Ces rectangles sont d�finis par la classe Rectangle du package tools, et stockent quatre valeurs de type
 * double : une abscisse x, une ordonn�e y, une largeur w, et une hauteur h. Les rectangles de coordonn�es sont donc
 * un moyen de rassembler en un seul objet les coordonn�es et les dimensions d'une image.</p>
 * <p>Les m�thodes dont le nom commence par 'set' ou 'get' sont des setters ou des getters simples, c'est-�-dire qu'elles
 * retournent l'instance d'une variable de classe sans la modifier et sans traitement pr�alable, mis � part le test
 * de nullit� du param�tre pour les setters.</p>
 * <p>Il est fait mention dans la documentation de textures et de fragments de texture. Une texture est une image
 * repr�sentant un objet affichable � l'�cran (objet du jeu, �cran de chargement etc...). Ces textures sont stock�es
 * dans des fichiers texture. Il s'agit de fichiers image (PNG). Un fragment de texture est une portion de ces fichiers,
 * d�finie par un jeu de coordonn�es comprises entre 0 et 1, relatives aux dimensions de l'image enti�re. Par exemple,
 * un fragment de texture de coordonn�es (0, 0.5, 1, 0.5) a son point sup�rieur gauche tout � gauche � mi-hauteur
 * du fichier texture entier, et il occupe toute la moiti� inf�rieure du fichier.</p>
 * <p>Les constructeurs de chaque classe sont num�rot�s s'ils sont plusieurs. Le premier constructeur est num�rot� 1,
 * puis chaque constructeur poss�dant un nombre sup�rieur de param�tres prend une unit�. Si deux constructeurs ont le
 * m�me nombre de param�tres, le premier est suivi d'un suffixe 'a' et le deuxi�me d'un suffixe 'b', et ainsi de suite.
 * Si le constructeur 1 a un param�tre, et le suivant trois param�tres, ce dernier sera quand m�me appel� constructeur
 * 2.</p>
 * <p>Quand il est pr�cis� qu'une instance de classe est standard, cela signifie qu'elle a �t� construite par son
 * constructeur 1 avec les param�tres par d�faut, � savoir 0 pour les nombres, false pour les bool�ens et null
 * pour tout autre type de variable. S'il s'agit d'une classe abstraite ou d'une interface standard, cela signifie
 * qu'une instance anonyme de cette classe ou interface a �t� cr��e avec toutes ses red�finitions vides.</p>
 * 
 * <b>Les deux threads centraux du programme</b>
 * 
 * <p>Le programme lance deux threads principaux :
 * <ul>
 * <li>Le thread initial ex�cutant la m�thode main().</li>
 * <li>Le thread de jeu (GameThread) mettant � jour l'affichage OpenGL et g�rant les �v�nements du jeu (gestion des
 * contr�les, mise en mouvement des objets etc...)</li>
 * </ul></p>
 * <p>Le thread initial a pour r�le de lancer la g�n�ration des mod�les du jeu, alors que le thread de jeu a pour r�le
 * d'afficher sur le Display les images envoy�es par ce mod�le ou tout autre portion de code, ainsi que de g�rer ces
 * mod�les suivant les actions de l'utilisateur.</p>
 * 
 * <b>Le pattern MVC</b>
 * 
 * <p>La plupart du temps, l'affichage et la mise � jour des �l�ments du jeu est g�r�e par un pattern MVC.</p>
 * <p>La vue (V) du pattern g�re l'affichage des images. Le thread de jeu poss�de l'instance de cette vue et en parcourt
 * les images pour les afficher � l'�cran. La vue met � jour ses images en fonction des informations envoy�es par le mod�le
 * ou d'autres portions de code (ce dernier cas est exceptionnel).</p>
 * <p>Le mod�le (M) du pattern g�re l'�volution des �l�ments du jeu. C'est lui qui d�place les objets sur la carte par
 * exemple. Il envoie les informations concernant les �l�ments du jeu � la vue apr�s chaque mise � jour.</p>
 * <p>Le contr�le (C) du pattern sert d'interface entre l'utilisateur et le mod�le. A chaque fois qu'un �v�nement
 * utilisateur est d�clench�, si le thread de jeu ne se charge pas de le traiter (il est prioritaire), le contr�le
 * r�agit � cette action. Il teste la possibilit� de l'action et donne son accord au mod�le si celle-ci est faisable.
 * Le mod�le mettra alors ses �l�ments � jour en fonction de cette action.</p>
 * <p>La vue est toujours g�r�e par la classe View. Quelque soit le contexte du jeu, c'est cette classe qui stockera les
 * images � afficher � l'�cran.</p>
 * <p>Le mod�le et le contr�le, quant � eux, peuvent se d�cliner selon plusieurs types de jeu.</p>
 * <p>La classe Model est une classe abstraite et ne peut donc pas �tre instanci�e. Il est n�cessaire de sp�cifier le
 * type de jeu g�r� par le mod�le. Ainsi, pour une phase de jeu de type d�placement sur la carte, la classe g�rant le
 * mod�le devra �tre de type MapModel, h�rit� de la classe Model.</p>
 * <p>La classe Control est �galement abstraite, et de m�me, il faudra utiliser une de ses classes filles
 * pour instancier le contr�le du pattern. L'objet MapControl sera donc utilis� pour un d�placement sur carte par
 * exemple.</p>
 * 
 * <b>Gestion de l'h�ritage multiple</b>
 * 
 * <p>Dans ce programme, le probl�me de l'h�ritage multiple est rencontr�. En effet, un objet a des caract�ristiques
 * h�ritant d'un objet g�n�ral li� au mod�le dans lequel il se trouve, mais �galement des caract�ristiques li�es au jeu
 * qui lui sont propres, et qu'il partage avec des objets semblables dans d'autres mod�les. Prenons l'exemple de la
 * classe GameCharacter et de ses d�riv�es. Les personnages sont d�finis de mani�re g�n�rale par leurs caract�ristiques
 * (PV, force, etc...), par une liste d'objets qu'ils portent, leurs attaques, etc... Ces propri�t�s se transmettent
 * d'un mod�le � un autre. Les objets gagn�s par un personnage sur la carte doivent �tre transf�r�s dans les combats.</p>
 * <p>Pour ce faire, un pattern h�ritage multiple 'maison' a �t� impl�ment�. Toujours sur le m�me exemple, le personnage
 * sur la carte sera repr�sent� par une classe h�ritant de MapObject, permettant par exemple de d�terminer les cases sur
 * lesquelles il se trouve. Il devra �galement �tre li� � un objet GameCharacter, g�n�ralisant le concept de personnage.
 * Il poss�dera donc une instance de GameCharacter, priv�e, dont toutes les m�thodes utiles seront rappel�es. Ainsi, on peut
 * appeler les m�thodes de GameCharacter directement depuis un MapCharacter, les m�thodes 'lien' se contentant de
 * transmettre les param�tres re�us � la m�thode homonyme de GameCharacter. Le MapCharacter h�rite de MapObject.</p>
 * <p>Le MapCharacter, dans cet exemple, devra �galement pouvoir retourner l'instance de GameCharacter pour le passage
 * d'un mod�le � l'autre.</p>
 * 
 * <b>Les packages</b>
 * 
 * <p>Le code de ce programme est divis� selon les packages suivants :
 * <ul>
 * <li>Package central fr.poufalouf : il engloble les trois classes Test, GameThread et ExceptionMessageBox
 * ainsi que les packages suivants.</li>
 * <li>Package tools : il rassemble tous les objets utilis�s pour la gestion d'objets simples et g�n�raux comme les
 * rectangles, les images, ou les composants g�n�riques du pattern MVC (View, Model et Control).</li>
 * <li>Les packages de jeu : ils sont sp�cialis�s dans la gestion d'un mode de jeu. Le package map contient
 * par exemple les classes MapModel, MapControl, MapObject, Decor, Case etc... Le package game contient les objets
 * g�n�raux du jeu, qui servent � envelopper les donn�es � passer d'un mod�le � un autre.</li>
 * </ul></p>
 * 
 * <p>Cette classe ne contient qu'une seule m�thode : la m�thode main(), qui g�n�re les mod�les du jeu et cr�� d�s
 * l'initialisation un thread de jeu pour afficher le Display.</p>
 *
 * @author Anaïs Vernet
 */
public class Test {
	
	/**
	 * M�thode principale du projet Poufalouf
	 * 
	 * <p>Cette m�thode est charg�e de g�n�rer les diff�rents mod�les du jeu. Durant ces proc�dures d'initialisation,
	 * le thread de jeu doit tourner en parall�le pour afficher l'avancement de la g�n�ration de l'environnement.</p>
	 * <p>La premi�re action de cette m�thode est donc l'instanciation d'un thread de jeu (GameThread). Ce thread doit
	 * �tre li� � une instance de vue (View) afin de pouvoir afficher une barre de progression. Cette barre est cr��e
	 * gr�ce � la classe LoadingScreen. La vue li�e � ce thread est red�finie sous
	 * forme de classe anonyme. En effet, le pattern MVC n'est pas encore impl�ment�, et la vue doit �tre autonome.
	 * Elle n'attend donc pas d'information d'un mod�le pour se mettre � jour, mais utilise une red�finition de la m�thode
	 * refresh() de la classe View. Cette red�finition, plut�t que d'interroger le mod�le, interroge directement
	 * l'instance de LoadingScreen (au travers de la m�thode draw() de cette derni�re classe). 
	 * L'�cran de chargement sera, quant � lui, mis � jour lors de l'initialisation du mod�le (dans le constructeur Model
	 * et plus pr�cis�ment lors de l'appel � la fonction initialize(LoadingScreen) de la classe Model).</p>
	 * <p>Le thread de jeu est alors lanc�, avec cette instance anonyme de View.</p>
	 * <p>Le thread initial lance ensuite l'initialisation du mod�le, et l'affichage progressif de la barre de progression
	 * se fait dans le thread de jeu. Le pattern MVC est assembl� d�s que cette initialisation est termin�e, rempla�ant
	 * l'instance anonyme de View associ�e au thread de jeu par une instance standard int�gr�e au pattern.</p>
	 * 
	 * <b>Etat actuel de la m�thode principale</b>
	 * 
	 * <p>Cette m�thode passe d'un mod�le � un autre (combat � map) en fonction de la situation. Elle g�re le transfert
	 * des propri�t�s des personnages entre chaque mod�le. Si Poufalouf meurt, elle cr�e un nouveau MapModel pour
	 * recommencer une partie.</p>
	 * 
	 * @param args
	 * 		Ce programme ne prend pas d'argument.
	 */
	public static void main(String[] args) {
		
		/*
		 * *******************************
		 * R�gles � respecter dans le code
		 * *******************************
		 * 
		 * Ces r�gles ont pour but d'�viter un maximum d'erreurs de programmation. Les RuntimeException n'�tant pas
		 * lev�es par le programme, il faut absolument en minimiser les occurences.
		 * 
		 * EXCEPTIONS
		 * 
		 * Toutes les RuntimeException doivent �tre trait�es directement, et aucune ne doit pouvoir filtrer vers la
		 * m�thode m�re.
		 * 
		 * Toutes les autres exceptions doivent �tre transmises � la classe principale de chaque thread (Test et
		 * GameThread) o� elles sont trait�es (sauf ExceptionMessageBox qui s'autog�re).
		 * 
		 * VARIABLES DE CLASSE
		 * 
		 * Toutes les variables d'une classe doivent �tre initialis�es dans le constructeur � des valeurs diff�rentes de
		 * null. Les listes et tableaux d'une classe ne doivent pas contenir d'�l�ment null.
		 * 
		 * Un constructeur ne peut jamais �chouer, dans le sens o� m�me si une RuntimeException survient, il devra
		 * construire un objet non null dont toutes les variables sont non null. S'il �choue vraiment, c'est qu'il l�ve
		 * une Exception transmise au thread parent.
		 * 
		 * Une variable de classe ne doit jamais pouvoir �tre r�initialis�e � null, m�me par un setter.
		 * 
		 * Les variables de classe non priv�es doivent �tre d�clar�es final afin de ne pas pouvoir �tre null.
		 * 
		 * DIVERS
		 * 
		 * Traiter tous les warnings et errors possiblement lev�s par Eclipse sauf Externalized String, Empty Statement,
		 * Serializable ID, et Unchecked Type.
		 * 
		 * Une fois qu'une variable a �t� initialis�e, si elle est accessible par un getter, elle doit toujours �tre lue
		 * par ce getter, et si elle est modifiable par un setter, elle doit toujours l'�tre par celui-ci, m�me au sein de
		 * la classe dans laquelle elle est visible.
		 * 
		 * Les termes set et get sont r�serv�s aux setters et getters classiques, c'est-�-dire aux fonctions qui renvoient
		 * ou mettent � jour directement les variables de classe, avec un test pr�venant les erreurs li�es � null.
		 */
		
		// Initialisation du thread de jeu
		
		GameThread gameThread = new GameThread(new View());
		ArrayList<String> files = new ArrayList<String>();
		files.add("res/textures/misc/loadingScreen.png");
		files.add("res/textures/misc/loadingTile.png");
		gameThread.addTextures(files);
		try {
			final LoadingScreen ldScreen = new LoadingScreen("res/textures/misc/loadingScreen.png",
					"res/textures/misc/loadingTile.png", 32, 32, 32, 10);
			View vueInter = new View() {
				
				@Override
				public void refresh() throws IOException {
					
					resizeToDisplay();
					setPosition();
					ldScreen.draw();
					
				}
				
			};
			ldScreen.addObserver(vueInter);
			gameThread.changeView(vueInter);
			Thread gameLoop = new Thread(gameThread);
			gameLoop.start();
			
			// Attend que le Display soit cr�� avant de continuer
			
			while (!gameThread.isReady() && gameThread.isPlaying())
				Thread.sleep(10);
			
			// si le thread de jeu ne s'est pas arr�t� sous le coup d'une exception
			if (gameThread.isPlaying()) {
				
				// Initialisation de la carte
				
				ArrayList<Object> param = new ArrayList<Object>();
				param.add(ldScreen);
				MapModel mod = new MapModel(param);
				View vueMap = new View(new MapControl(mod), new MapCamera(0, 0));
				mod.addObserver(vueMap);
				
				// Initialisation du mod�le de combat
				
				CombatModel cmod = null;
				View vueCombat = null;
				View emptyView = new View();
				
				// Chargement des ressources
				
				Test.addWorldResources(gameThread, "ALL");
				Test.addWorldResources(gameThread, "PUMPKINS");
				
				// Attribution de la vue relative � la carte au thread de jeu
				
				gameThread.changeView(vueMap);
				
				// Boucle principale
				
				while (gameThread.isPlaying()) {
					Thread.sleep(10);
					if (mod.isTerminated()) {
						ldScreen.reset();
						gameThread.changeView(vueInter);
						param.clear();
						param.add(ldScreen);
						mod = new MapModel(param);
						vueMap = new View(new MapControl(mod), new MapCamera(0, 0));
						mod.addObserver(vueMap);
						gameThread.changeView(vueMap);
					}
					if (mod.isInCombatMode()) {
						mod.setInCombatMode(false);
						gameThread.changeView(emptyView);
						param.clear();
						MapObject obj = mod.objets().get("Poufalouf");
						if (obj instanceof Poufalouf) {
							param.add(((Poufalouf) obj).getCharacter());
						}
						obj = mod.opponent();
						if (obj instanceof Douni) {
							param.add(((Douni) obj).getCharacter());
						}
						cmod = new CombatModel(param);
						vueCombat = new View(new CombatControl(cmod), new Camera(0, 0));
						cmod.addObserver(vueCombat);
						gameThread.changeView(vueCombat);
					}
					if (cmod != null && cmod.isTerminated()) {
						gameThread.changeView(emptyView);
						MapObject obj = mod.objets().get("Poufalouf");
						CombatCharacter cc = cmod.player();
						if (obj instanceof Poufalouf && cc != null) {
							((Poufalouf) obj).changeCharacteristic(Characteristic.HP,
									cc.characteristic(Characteristic.HP));
						}
						obj = mod.opponent();
						cc = cmod.opponent();
						if (obj instanceof Douni && cc != null) {
							((Douni) obj).changeCharacteristic(Characteristic.HP,
									cc.characteristic(Characteristic.HP));
						}
						gameThread.changeView(vueMap);
						cmod = null;
					}
				}
			}
			
		} catch (Exception e) {
			gameThread.setFullscreen(false);
			ExceptionMessageBox.show(e, "Erreur");
			gameThread.setPlaying(false);
		}
	}
	
	/**
	 * Charge les ressources du monde dont le nom est pass� en param�tre dans le thread de jeu.
	 * 
	 * @param g
	 * 		Le thread de jeu.
	 * @param worldName
	 * 		Le nom du monde.
	 * @throws IOException
	 * 		si la m�thode readSectionAtFlag de la classe ScriptReader l�ve une IOException.
	 */
	private static void addWorldResources(GameThread g, String worldName) throws IOException {
		
		if (g == null || worldName == null)
			return;
		g.addTextures(ScriptReader.readSectionAtFlag("res/scripts/worlds.txt", worldName, "IMG"));
		g.addSounds(ScriptReader.readSectionAtFlag("res/scripts/worlds.txt", worldName, "SND"));
		
	}

}
