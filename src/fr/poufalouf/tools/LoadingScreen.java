package fr.poufalouf.tools;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Ecran de chargement
 * 
 * <p>Cette classe repr�sente un �cran de chargement comprenant une barre de progression qui peut �tre mise � jour.</p>
 * <p>Il s'agit d'une impl�mentation de l'interface Observable. Dans ce programme, avant que la vue ait �t� int�gr�e dans
 * un pattern MVC au d�but de la proc�dure main() de la classe Test, elle observe un objet LoadingScreen qui la met
 * � jour dans sa m�thode refresh() red�finie (appel � la m�thode draw() de cette classe dans la m�thode red�finie
 * refresh() de la classe View.)</p>
 * <p>Il s'agit �galement d'une impl�mentation de l'interface Drawable, et un �cran de chargement g�n�re une image
 * combin�e compos�e des �l�ments du cadre et des carr�s de la barre de progression.</p>
 * 
 * @author Anaïs Vernet
 */
public class LoadingScreen implements Drawable, Observable {
	
	/**
	 * La largeur d'un bord de cet �cran en pixels.
	 * 
	 * <p>Il s'agit de l'�paisseur entre le bord gauche de l'�cran et la barre de progression.</p>
	 */
	private final int sizeWBorder;
	/**
	 * La hauteur d'un bord de cet �cran en pixels.
	 * 
	 * <p>Il s'agit de l'�paisseur entre le bord haut de l'�cran et la barre de progression.</p>
	 */
	private final int sizeHBorder;
	/**
	 * La taille d'un carr� de chargement dans la barre de progression.
	 */
	private final int sizeTile;
	/**
	 * Le nom du fichier texture de l'�cran.
	 */
	private final String screenTextureID;
	/**
	 * Le nom du fichier texture d'un carr� de chargement.
	 */
	private final String tileTextureID;
	/**
	 * Le nombre de carr�s de chargement contenus au maximum par la barre de progression.
	 */
	private final int length;
	/**
	 * Le nombre de carr�s de chargement � afficher dans la barre de progression.
	 */
	private int nbLoadedTiles;
	/**
	 * L'abscisse par rapport au Display de cet �cran de chargement.
	 */
	private double x;
	/**
	 * L'ordonn�e par rapport au Display de cet �cran de chargement.
	 */
	private double y;
	/**
	 * La largeur de cet �cran de chargement.
	 */
	private double w;
	/**
	 * La hauteur de cet �cran de chargement.
	 */
	private double h;
	/**
	 * La liste des images utilis�es pour g�n�rer l'image combin�e repr�sentant cette barre de chargement.
	 */
	private ArrayList<TexturedImage> images;
	/**
	 * La liste des observateurs de cet �cran de chargement.
	 */
	private ArrayList<Observer> observateurs;

	/**
	 * Constructeur LoadingScreen.
	 * 
	 * <p>Apr�s l'initialisation, ce constructeur construit le cadre entourant
	 * la barre en ajoutant les images n�cessaires � la liste des images de cette classe.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>sizeWBorder : la largeur pass�e en param�tre (sizeWBorder).</li>
	 * <li>sizeHBorder : la hauteur pass�e en param�tre (sizeHBorder).</li>
	 * <li>sizeTile : la taille pass�e en param�tre (sizeTile).</li>
	 * <li>screenTextureID : le nom de fichier pass� en param�tre (loadScreenTextureFile), ou "" si le param�tre est
	 * null.</li>
	 * <li>tileTextureID : le nom de fichier pass� en param�tre (loadTileTextureFile), ou "" si le param�tre est null.</li>
	 * <li>length : la longueur pass�e en param�tre (length), ou 1 si le param�tre est n�gatif ou nul.</li>
	 * <li>nbLoadedTiles : 0.</li>
	 * <li>x : l'abscisse calcul�e afin de centrer cet �cran au centre du Display.</li>
	 * <li>y : l'ordonn�e calcul�e afin de centrer cet �cran au centre du Display.</li>
	 * <li>w : la largeur calcul�e comme suivant : <code>sizeTile*length + 2*sizeWBorder.</code></li>
	 * <li>h : la hauteur calcul�e comme suivant : <code>sizeTile + 2*sizeHBorder.</code></li>
	 * <li>images : une nouvelle liste (ArrayList) de TexturedImage.</li>
	 * <li>observateurs : une nouvelle liste (ArrayList) d'Observer.</li>
	 * </ul></p>
	 * 
	 * @param loadScreenTextureFile
	 * 		Le nom du fichier texture de cet �cran de chargement.
	 * @param loadTileTextureFile
	 * 		Le nom du fichier texture des carr�s de chargement.
	 * @param sizeWBorder
	 * 		La largeur d'un bord de cet �cran.
	 * @param sizeHBorder
	 * 		La hauteur d'un bord de cet �cran.
	 * @param sizeTile
	 * 		La taille d'un carr� de chargement de cet �cran.
	 * @param length
	 * 		Le nombre maximal de carr�s de chargement.
	 */
	public LoadingScreen(String loadScreenTextureFile, String loadTileTextureFile, int sizeWBorder,
			 int sizeHBorder, int sizeTile, int length) {
		
		this.sizeWBorder = sizeWBorder;
		this.sizeHBorder = sizeHBorder;
		this.sizeTile = sizeTile;
		if (loadScreenTextureFile != null)
			this.screenTextureID = loadScreenTextureFile;
		else
			this.screenTextureID = "";
		if (loadTileTextureFile != null)
			this.tileTextureID = loadTileTextureFile;
		else
			this.tileTextureID = "";
		this.length = Math.max(length, 1);
		this.nbLoadedTiles = 0;
		this.w = this.sizeTile*this.length+this.sizeWBorder*2;
		this.h = this.sizeHBorder*2+this.sizeTile;
		this.x = Constantes.initialDisplayWidth/2-this.w/2;
		this.y = Constantes.initialDisplayHeight/2-this.h/2;
		this.images = new ArrayList<TexturedImage>();
		this.observateurs = new ArrayList<Observer>();
		
		// Pr�paration du cadre
		
		TexturedImage barLeftSide = new TexturedImage("Chargement bord gauche", 0, 0, 1./(2+this.length), 1,
				this.screenTextureID, new Rectangle(0, 0, 1./3, 1));
		TexturedImage barRightSide = new TexturedImage("Chargement bord droit", 1-1./(2+this.length), 0,
				1./(2+this.length), 1, this.screenTextureID, new Rectangle(2./3, 0, 1./3, 1));
		this.images.add(barLeftSide);
		this.images.add(barRightSide);
		for (int i=0;i<this.length;i++) {
			TexturedImage upTile = new TexturedImage("Chargement haut "+(i+1), (1.+i)/(2+this.length), 0,
					1./(2+this.length), 1./3, this.screenTextureID, new Rectangle(1./3, 0, 1./3, 1./3));
			TexturedImage downTile = new TexturedImage("Chargement bas "+(i+1), (1.+i)/(2+this.length), 2./3,
					1./(2+this.length), 1./3, this.screenTextureID, new Rectangle(1./3, 2./3, 1./3, 1./3));
			TexturedImage centerTile = new TexturedImage("Chargement centre "+(i+1), (1.+i)/(2+this.length), 1./3,
					1./(2+this.length), 1./3, this.screenTextureID, new Rectangle(1./3, 1./3, 1./3, 1./3));
			this.images.add(upTile);
			this.images.add(downTile);
			this.images.add(centerTile);
		}
		
	}
	
	/**
	 * Affiche l'�tat actuel de cette barre de chargement.
	 * 
	 * <p>Apr�s avoir ajout� les images des carr�s de la barre si n�cessaire, cette m�thode fait appel � la m�thode
	 * notifyObservers() de cette classe, afin de mettre les observateurs � jour.</p>
	 * 
	 * @throws IOException
	 * 		si la m�thode notifyObservers() de cette classe l�ve une IOException.
	 */
	public void draw() throws IOException {
		
		// Ajout des carr�s suppl�mentaires si n�cessaire.
		for (int i=this.images.size()-2-this.length*3;i<this.nbLoadedTiles;i++) {
			this.images.add(new TexturedImage("Chargement carr� "+(i+1), (1.+i)/(2+this.length), 1./3, 1./(2+this.length),
					1./3, this.tileTextureID));
		}
		
		this.notifyObservers();
		
	}
	
	/**
	 * Calcule le nombre de carr�s de chargement � afficher dans la barre de progression en fonction du pourcentage pass� 
	 * en param�tre.
	 * 
	 * @param pourcentage
	 * 		Le pourcentage d�finissant le nombre de carr�s de chargement.
	 */
	public void update(float pourcentage) {
		
		this.nbLoadedTiles = Math.min((int) Math.ceil(pourcentage*this.length), this.length);
		
	}
	
	/**
	 * Remet cet �cran de chargement � z�ro.
	 * 
	 * <p>Cette m�thode remet le pourcentage � z�ro, et supprime les images des carr�s de chargement de la liste des images
	 * de cet �cran.</p>
	 */
	public void reset() {
		
		this.nbLoadedTiles = 0;
		for (int i=this.images.size()-this.nbLoadedTiles;i<this.images.size();i++)
			this.images.remove(i);
		
	}
	
	/**
	 * Retourne une image repr�sentant cet �cran de chargement.
	 * 
	 * <p>L'image est combin�e et comprend les parties du cadre ainsi que les carr�s de chargement.</p>
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	@Override
	public CombinedImage generateImage() {
		
		CombinedImage img = new CombinedImage(new TexturedImage("Ecran de chargement", 
				this.x, this.y, this.w, this.h, null));
		
		for (int i=0;i<this.images.size();i++) {
			img.addImage(this.images.get(i).clone());
		}
		
		return img;
		
	}
	
	/**
	 * Retourne faux.
	 * 
	 * @return
	 * 		Faux.
	 */
	@Override
	public boolean isVBORendered() {
		
		return false;
		
	}

	/**
	 * Met � jour les observateurs de cet �cran de chargement.
	 * 
	 * <p>Cette m�thode appelle la m�thode update(Observable) de tous les observateurs observant cet �cran.</p>
	 * 
	 * @throws IOException
	 * 		si la m�thode update(Observable) de l'interface Observer l�ve une IOException.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void notifyObservers() throws IOException {
		
		for (Observer observ : (ArrayList<Observer>) this.observateurs.clone()) {
			observ.update(this);
		}
		
	}

	/**
	 * Ajoute un observateur � la liste des observateurs de cet �cran de chargement.
	 * 
	 * @param obs
	 * 		L'observateur � ajouter. S'il est null, il n'est pas ajout�.
	 */
	@Override
	public void addObserver(Observer obs) {
		
		if (obs != null)
			this.observateurs.add(obs);
		
	}

	/**
	 * Retire un observateur de la liste des observateurs de cet �cran de chargement.
	 * 
	 * @param obs
	 * 		L'observateur � retirer.
	 */
	@Override
	public void removeObserver(Observer obs) {
		
		this.observateurs.remove(obs);
		
	}
	
	/**
	 * Modifie l'abscisse de cet �cran.
	 * 
	 * @param x
	 * 		L'abscisse de cet �cran.
	 */
	@Override
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Retourne l'abscisse de cet �cran.
	 * 
	 * @return
	 * 		L'abscisse de cet �cran.
	 */
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * Modifie l'ordonn�e de cet �cran.
	 * 
	 * @param y
	 * 		L'ordonn�e de cet �cran.
	 */
	@Override
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Retourne l'ordonn�e de cet �cran.
	 * 
	 * @return
	 * 		L'ordonn�e de cet �cran.
	 */
	@Override
	public double getY() {
		return this.y;
	}

	/**
	 * Modifie la largeur de cet �cran.
	 * 
	 * @param w
	 * 		La largeur de cet �cran.
	 */
	@Override
	public void setW(double w) {
		this.w = w;
	}

	/**
	 * Retourne la largeur de cet �cran.
	 * 
	 * @return
	 * 		La largeur de cet �cran.
	 */
	@Override
	public double getW() {
		return this.w;
	}

	/**
	 * Modifie la hauteur de cet �cran.
	 * 
	 * @param h
	 * 		La hauteur de cet �cran.
	 */
	@Override
	public void setH(double h) {
		this.h = h;
	}

	/**
	 * Retourne la hauteur de cet �cran.
	 * 
	 * @return
	 * 		La hauteur de cet �cran.
	 */
	@Override
	public double getH() {
		return this.h;
	}

}
