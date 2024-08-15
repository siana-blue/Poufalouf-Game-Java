package fr.poufalouf.tools;

/**
 * D�finition des constantes utilis�es par le jeu
 * 
 * <p>Cette classe regroupe un ensemble de champs statiques qui peuvent concerner plusieurs modes de jeu. Ces
 * valeurs peuvent �tre utilis�es par toutes les classes de ce programme.</p>
 * 
 * @author Anaïs Vernet
 */
public final class Constantes {
	
	/*
	 * G�n�ralit�s
	 */
	
	/**
	 * Le d�lai standard entre deux mises � jour d'un objet du jeu (en millisecondes).
	 */
	public static final int delayUpdate = 120;
	
	// Taille de la vue
	
	/**
	 * La largeur initiale du Display.
	 */
	public static final int initialDisplayWidth = 600;
	/**
	 * La hauteur initiale du Display.
	 */
	public static final int initialDisplayHeight = 600;
	
	// Taille d'une image et organisation des fichiers textures
	
	/**
	 * La taille de l'image d'un objet en pixels.
	 */
	public static final int sizeImage = 64;
	/**
	 * Le nombre d'images par ligne (et par colonne) de fichier texture.
	 */
	public static final int nbImagePerLine = 4;
	/**
	 * La taille d'un fragment de texture en pourcentage d'un fichier image entier.
	 * 
	 * <p>Il s'agit de l'inverse du champ nbImagePerLine de cette classe.</p>
	 */
	public static final double sizeTextureFragment = 1./nbImagePerLine;
	/**
	 * La taille d'une image de terrain en pixels.
	 */
	public static final int sizeTerrain = 1024;
	/**
	 * La taille du fragment de texture d'un terrain correspondant � une case en pixels.
	 */
	public static final int sizeTextureTile = 64;
	
	/*
	 * Map
	 */
	
	/**
	 * Le nombre de pas par case du jeu.
	 */
	public static final int stepPerCell = 4;
	/**
	 * Le nombre de cases de la carte en largeur.
	 */
	public static final int cellPerMapW = 100;
	/**
	 * Le nombre de cases de la carte en hauteur.
	 */
	public static final int cellPerMapH = 100;
	/**
	 * La taille d'une case en pixels.
	 */
	public static final int sizeCell = 32;
	/**
	 * La taille d'un pas sur la carte.
	 */
	public static final int sizeStep = sizeCell/stepPerCell;
	/**
	 * La hauteur maximale d'un saut.
	 */
	public static final int heightJump = 3;
	/**
	 * La taille d'un pas en hauteur.
	 */
	public static final int sizeHStep = 12;
	/**
	 * Le plan de perspective le plus en avant.
	 */
	public static final int maxPlan = 100000;
	
	/*
	 * Combat
	 */
	
	/**
	 * La largeur d'un �cran d'information pour les personnages en combat.
	 */
	public static final int sizeScreenW = 200;
	/**
	 * La hauteur d'un �cran d'information pour les personnages en combat.
	 */
	public static final int sizeScreenH = 100;
	/**
	 * Le plan de perspective du personnage en arri�re-plan � l'�cran de combat.
	 */
	public static final int upCharacterPlan = Constantes.maxPlan-10;
	/**
	 * Le plan de perspective du personnage en premier plan � l'�cran de combat.
	 */
	public static final int downCharacterPlan = Constantes.maxPlan-5;
	/**
	 * Le plan de perspective des effets de certaines attaques en combat.
	 */
	public static final int attackEffectPlan = Constantes.maxPlan-7;
	/**
	 * Le nom du fichier contenant la liste des attaques du jeu.
	 */
	public static final String listAttacksFileName = "res/scripts/listeAttaques.txt";
	/**
	 * Le nom du fichier contenant la liste des objets du jeu.
	 */
	public static final String listItemsFileName = "res/scripts/listeObjets.txt";

}
