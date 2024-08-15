package fr.poufalouf.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;

import fr.poufalouf.UpdateException;
import fr.poufalouf.tools.Animation;
import fr.poufalouf.tools.CombinedImage;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Drawable;
import fr.poufalouf.tools.Image;
import fr.poufalouf.tools.Orientation;
import fr.poufalouf.tools.PoppingObject;
import fr.poufalouf.tools.Status;
import fr.poufalouf.tools.TexturedImage;

/**
 * Un objet du jeu
 * 
 * <p>Il s'agit d'une classe abstraite qui encapsule tous les types d'objets des diff�rents modes de jeu.</p>
 * 
 * @author Anaïs Vernet
 */
public abstract class GameObject implements Drawable {

	/**
	 * Le nom de cet objet.
	 */
	private String name;
	/**
	 * L'abscisse par rapport au jeu de cet objet.
	 */
	private double x;
	/**
	 * L'ordonn�e par rapport au jeu de cet objet.
	 */
	private double y;
	/**
	 * La largeur de cet objet.
	 */
	private double w;
	/**
	 * La hauteur de cet objet.
	 */
	private double h;
	/**
	 * La couleur de cet objet.
	 */
	private Color color;
	/**
	 * Le plan de perspective de cet objet.
	 */
	private int plan;
	/**
	 * Le d�lai en millisecondes entre chaque mise � jour de cet objet.
	 */
	private int delayUpdate;
	/**
	 * L'orientation de cet objet.
	 */
	private Orientation orientation;
	/**
	 * La liste des items apparaissants sur cet objet.
	 */
	private ArrayList<PoppingObject> popups;
	/**
	 * La table des animations de cet objet.
	 */
	private HashMap<Status, Animation> anim;
	/**
	 * Le statut de l'animation actuelle de cet objet.
	 */
	private Status currentAnim;
	/**
	 * Le compteur de temps de cet objet.
	 * 
	 * <p>Ce compteur est utilis� pour d�terminer si cet objet doit �tre mis � jour par le mod�le.</p>
	 */
	private long compteurTemps;
	/**
	 * Le son � ajouter au mod�le.
	 */
	private String soundToAdd;
	/**
	 * Indique si le son de cet objet doit �tre spatialis�.
	 */
	private boolean spatialSound;
	/**
	 * Indique si cet objet doit �tre supprim� du jeu.
	 */
	private boolean finished;
	
	/**
	 * Constructeur GameObject.
	 * 
	 * <p></p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>name : le nom pass� en param�tre (name), ou "" si le param�tre est null.</li>
	 * <li>x : l'abscisse pass�e en param�tre (x).</li>
	 * <li>y : l'ordonn�e pass�e en param�tre (y).</li>
	 * <li>w : la largeur pass�e en param�tre (w).</li>
	 * <li>h : la largeur pass�e en param�tre (h).</li>
	 * <li>color : Color.white.</li>
	 * <li>plan : 0.</li>
	 * <li>delayUpdate : la valeur du champ delayUpdate de la classe Constantes.</li>
	 * <li>orientation : le champ AUCUNE de la classe Orientation.</li>
	 * <li>popups : une nouvelle liste (ArrayList) de PoppingObject.</li>
	 * <li>anim : une nouvelle table (HashMap) d'Animation r�f�renc�es par des Status.</li>
	 * <li>currentAnim : STILL.</li>
	 * <li>compteurTemps : la valeur retourn�e par Sys.getTime().</li>
	 * <li>soundToAdd : "".</li>
	 * <li>spatialSound : vrai.</li>
	 * <li>finished : faux.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de cet objet.
	 * @param x
	 * 		L'abscisse de cet objet par rapport au jeu.
	 * @param y
	 * 		L'ordonn�e de cet objet par rapport au jeu.
	 * @param w
	 * 		La largeur de cet objet.
	 * @param h
	 * 		La hauteur de cet objet.
	 */
	public GameObject(String name, double x, double y, double w, double h) {
		
		if (name != null)
			this.name = name;
		else
			this.name = "";
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.color = Color.white;
		this.plan = 0;
		this.delayUpdate = Constantes.delayUpdate;
		this.orientation = Orientation.AUCUNE;
		this.popups = new ArrayList<PoppingObject>();
		this.anim = new HashMap<Status, Animation>();
		this.currentAnim = Status.STILL;
		this.compteurTemps = Sys.getTime();
		this.soundToAdd = "";
		this.spatialSound = true;
		this.finished = false;
		
	}
	
	/**
	 * Met � jour l'�tat de cet objet en fonction de son comportement propre (red�fini par cette m�thode dans les classes
	 * filles).
	 * 
	 * <p>Par d�faut, cette m�thode v�rifie le timer de l'animation en cours; s'il est � z�ro, l'animation STILL
	 * est choisie.</p>
	 * @throws UpdateException
	 * 		jamais, mais possible dans les m�thodes red�finies.
	 */
	public void playBehavior() throws UpdateException {
		
		Animation a = this.currentAnim();
		if (a != null)
			if (a.getTimer() == 0)
				this.changeCurrentAnim(Status.STILL);
		
	}
	
	/**
	 * G�n�re une image repr�sentant cet objet.
	 * 
	 * <p>Cette m�thode appelle la m�thode generateObjectImage de cette classe pour cr�er une image combin�e. Elle ajoute
	 * alors � celle-ci une image pour chaque pop-up de cet objet, exprim� en coordonn�es relatives � cet objet.</p>
	 * <p>Avant cela, la m�thode checkPopups de cette classe est appel�e pour mettre � jour la liste.</p>
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	@Override
	public CombinedImage generateImage() {
		
		CombinedImage cbImg = this.generateObjectImage();
		
		this.checkPopups();
		if (cbImg != null) {
			for (PoppingObject p : this.popups) {
				if (p != null) {
					Image img = p.generateImage();
					if (img instanceof TexturedImage)
						cbImg.addImage((TexturedImage) img);
				}
			}
			String s = this.sound();
			if (s != "" && s != null) {
				cbImg.setSound(s);
				cbImg.setSpatialSound(this.spatialSound);
			}
		}
		
		return cbImg;
		
	}
	
	/**
	 * G�n�re une image repr�sentant cet objet (et utilis�e ensuite dans la m�thode generateImage de cette classe).
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	public abstract CombinedImage generateObjectImage();
	
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
	 * Modifie le nom de cet objet.
	 * 
	 * @param name
	 * 		Le nom de cet objet.
	 */
	public void setName(String name) {
		if (name != null)
			this.name = name;
	}
	
	/**
	 * Retourne le nom de cet objet.
	 * 
	 * @return
	 * 		Le nom de cet objet.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Met � jour l'abscisse par rapport au jeu de cet objet.
	 * 
	 * @param x
	 * 		L'abscisse par rapport au jeu de cet objet.
	 */
	@Override
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Retourne l'abscisse par rapport au jeu de cet objet.
	 * 
	 * @return
	 * 		L'abscisse par rapport au jeu de cet objet.
	 */
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * Met � jour l'ordonn�e par rapport au jeu de cet objet.
	 * 
	 * @param y
	 * 		L'ordonn�e par rapport au jeu de cet objet.
	 */
	@Override
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Retourne l'ordonn�e par rapport au jeu de cet objet.
	 * 
	 * @return
	 * 		L'ordonn�e par rapport au jeu de cet objet.
	 */
	@Override
	public double getY() {
		return this.y;
	}
	
	/**
	 * Met � jour la largeur de cet objet.
	 * 
	 * @param w
	 * 		La largeur de cet objet.
	 */
	@Override
	public void setW(double w) {
		this.w = w;
	}
	
	/**
	 * Retourne la largeur de cet objet.
	 * 
	 * @return
	 * 		La largeur de cet objet.
	 */
	@Override
	public double getW() {
		return this.w;
	}
	
	/**
	 * Met � jour la hauteur de cet objet.
	 * 
	 * @param h
	 * 		La hauteur de cet objet.
	 */
	@Override
	public void setH(double h) {
		this.h = h;
	}
	
	/**
	 * Retourne la hauteur de cet objet.
	 * 
	 * @return
	 * 		La hauteur de cet objet.
	 */
	@Override
	public double getH() {
		return this.h;
	}
	
	/**
	 * Modifie la couleur de cet objet.
	 * 
	 * @param color
	 * 		La couleur de cet objet. Si elle est null, rien n'est fait.
	 */
	public void setColor(Color color) {
		if (color != null)
			this.color = color;
	}
	
	/**
	 * Retourne la couleur de cet objet.
	 * 
	 * @return
	 * 		La couleur de cet objet.
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Modifie le plan de perspective de cet objet.
	 * 
	 * @param plan
	 * 		Le plan de perspective de cet objet.
	 */
	public void setPlan(int plan) {
		this.plan = plan;
	}
	
	/**
	 * Retourne le plan de perspective de cet objet.
	 * 
	 * @return
	 * 		Le plan de perspective de cet objet.
	 */
	public int getPlan() {
		return this.plan;
	}
	
	/**
	 * Met � jour le d�lai entre chaque mise � jour de cet objet.
	 * 
	 * @param delayUpdate
	 * 		Le d�lai en millisecondes.
	 */
	public void setDelayUpdate(int delayUpdate) {
		this.delayUpdate = delayUpdate;
	}
	
	/**
	 * Retourne le d�lai entre chaque mise � jour de cet objet.
	 * 
	 * @return
	 * 		Le d�lai en millisecondes.
	 */
	public int getDelayUpdate() {
		return this.delayUpdate;
	}
	
	/**
	 * Met � jour l'orientation de cet objet.
	 * 
	 * @param orientation
	 * 		L'orientation de cet objet. Si elle est null, elle n'est pas ajout�e.
	 */
	public void setOrientation(Orientation orientation) {
		if (orientation != null)
			this.orientation = orientation;
	}
	
	/**
	 * Retourne l'orientation de cet objet.
	 * 
	 * @return
	 * 		L'orientation de cet objet.
	 */
	public Orientation getOrientation() {
		return this.orientation;
	}
	
	/**
	 * Ajoute un objet � la liste des objets pop-ups de cet objet.
	 * 
	 * @param obj
	 * 		L'objet � ajouter. S'il est null, rien n'est fait.
	 */
	public void addPopup(PoppingObject obj) {
		
		if (obj == null)
			return;
		this.popups.add(obj);
		
	}
	
	/**
	 * Supprime de la liste des pop-ups tous les objets dont la m�thode isDead() renvoie vrai.
	 */
	@SuppressWarnings("unchecked")
	public void checkPopups() {
		
		for (PoppingObject p : (ArrayList<PoppingObject>) this.popups.clone()) {
			if (p == null)
				continue;
			if (p.isDead())
				this.popups.remove(p);
		}
		
	}
	
	/**
	 * Retourne une copie de la liste des PoppingObject de cet objet.
	 * 
	 * <p>La liste des objets est une copie, mais les objets sont les originaux.</p>
	 * 
	 * @return
	 * 		Une copie de la liste des objets.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PoppingObject> popups() {
		
		return (ArrayList<PoppingObject>) this.popups.clone();
		
	}
	
	/**
	 * Met � jour l'animation actuelle de cet objet gr�ce au Status pass� en param�tre.
	 * 
	 * <p>Si ce statut ne correspond pas � une case de la table d'animations, rien n'est fait.</p>
	 * <p>La nouvelle animation reprend � 0 (fonction reset() de la classe Animation).</p>
	 * <p>Cette m�thode ne fait rien si le statut de l'animation actuelle est d�j� celui pass� en param�tre.</p>
	 * 
	 * @param status
	 * 		L'�tat de l'animation.
	 */
	public void changeCurrentAnim(Status status) {
		
		if (status == null || !this.anim.containsKey(status))
			return;
		if (this.currentAnim().getStatus() != status) {
			this.currentAnim = status;
			this.currentAnim().reset();
		}
		
	}
	
	/**
	 * Retourne l'animation actuelle de cet objet.
	 * 
	 * @return
	 * 		L'animation actuelle de cet objet.
	 */
	public Animation currentAnim() {
		
		return this.anim.get(this.currentAnim);
		
	}
	
	/**
	 * Ajoute une animation � la table des animations de cet objet.
	 * 
	 * <p>La cl� qui lui est associ�e est le statut de l'animation.</p>
	 * 
	 * @param animation
	 * 		L'animation � ajouter. Si elle est null, elle n'est pas ajout�e.
	 */
	public void addAnimation(Animation animation) {
		
		if (animation == null)
			return;
		this.anim.put(animation.getStatus(), animation);
		
	}
	
	/**
	 * Retourne la liste des animations de cet objet.
	 * 
	 * @return
	 * 		La liste des animations de cet objet.
	 */
	public ArrayList<Animation> anims() {
		
		ArrayList<Animation> anims = new ArrayList<Animation>();
		for (Animation a : this.anim.values()) {
			if (a != null)
				anims.add(a);
		}
		return anims;
		
	}
	
	/**
	 * Met � jour le compteur de temps (en ticks) de cet objet.
	 * 
	 * @param compteurTemps
	 * 		Le compteur de temps de cet objet.
	 */
	public void setCompteurTemps(long compteurTemps) {
		this.compteurTemps = compteurTemps;
	}

	/**
	 * Retourne le compteur de temps (en ticks) de cet objet.
	 * 
	 * @return
	 * 		Le compteur de temps de cet objet.
	 */
	public long getCompteurTemps() {
		return this.compteurTemps;
	}
	
	/**
	 * Modifie le son de cet objet.
	 * 
	 * @param sound
	 * 		Le nom du fichier son.
	 */
	public void setSound(String sound) {
		this.soundToAdd = sound;
	}
	
	/**
	 * Retourne le nom du fichier son � ajouter au mod�le.
	 * 
	 * <p>Celui-ci est alors r�initialis� � "".</p>
	 * 
	 * @return
	 * 		Le nom du fichier son.
	 */
	public String sound() {
		
		String s = this.soundToAdd;
		this.soundToAdd = "";
		
		return s;
		
	}
	
	/**
	 * Modifie la valeur du bool�en spatialSound de cette classe.
	 * 
	 * @param spatialSound
	 * 		Vrai si le son de cet objet doit �tre spatialis�.
	 */
	public void setSpatialSound(boolean spatialSound) {
		this.spatialSound = spatialSound;
	}
	
	/**
	 * Indique si le son de cet objet doit �tre spatialis�.
	 * 
	 * @return
	 * 		Vrai si le son doit �tre spatialis�.
	 */
	public boolean isSpatialSound() {
		return this.spatialSound;
	}
	
	/**
	 * Met � jour l'�tat du bool�en finished de cette classe.
	 * 
	 * @param finished
	 * 		Vrai si cet objet doit �tre supprim� du jeu.
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	/**
	 * Indique si cet objet doit �tre supprim� du jeu.
	 * 
	 * @return
	 * 		Vrai si cet objet doit �tre supprim� du jeu.
	 */
	public boolean isFinished() {
		return this.finished;
	}

}
