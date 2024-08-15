package fr.poufalouf.tools;

import org.newdawn.slick.Color;

/**
 * Objet utilis� par le jeu pour afficher une image � l'�cran
 * 
 * <p>Une image se d�finit par ses coordonn�es, ses dimensions, sa couleur et son plan de perspective.</p>
 * <p>Une image simple ne poss�de pas de texture. Pour avoir une image textur�e, il est n�cessaire d'invoquer une
 * instance de classe TexturedImage, h�rit�e de cette classe.</p>
 * <p>Cette classe impl�mente l'interface Comparable, et peut �tre class�e parmi d'autres images selon son plan de 
 * perspective.</p>
 * <p>Une image est �galement directement dessinable en tant que Drawable, sa fonction generateImage renvoie une 
 * copie d'elle-m�me.</p>
 * 
 * @author Anaïs Vernet
 */
public class Image implements Drawable, Comparable<Image>, Cloneable {
	
	/**
	 * Le nom de cette image.
	 */
	private final String name;
	/**
	 * L'abscisse de cette image.
	 */
	private double x;
	/**
	 * L'ordonn�e de cette image.
	 */
	private double y;
	/**
	 * La largeur de cette image.
	 */
	private double w;
	/**
	 * La hauteur de cette image.
	 */
	private double h;
	/**
	 * La couleur de cette image.
	 */
	private Color couleur;
	/**
	 * Le plan de perspective de cette image.
	 * 
	 * <p>Lors de l'affichage des images, la proc�dure charg�e du dessin doit afficher en premier les images avec le plan
	 * de perspective minimal, et en dernier les images avec le plan de perspective maximal.</p>
	 */
	private int plan;
	/**
	 * L'angle de rotation de cette image, dans le sens des aiguilles d'une montre, en degr�s.
	 */
	private int angle;
	/**
	 * Le nom du fichier son jou� avec cette image.
	 */
	private String sound;
	/**
	 * Indique si le son de cette image doit �tre spatialis�.
	 */
	private boolean spatialSound;
	
	/**
	 * Constructeur Image.
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
	 * <li>h : la hauteur pass�e en param�tre (h).</li>
	 * <li>couleur : la couleur pass�e en param�tre (couleur), ou Color.white si le param�tre est null.</li>
	 * <li>plan : 0.</li>
	 * <li>angle : 0.</li>
	 * <li>sound : "".</li>
	 * <li>spatialSound : vrai.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de cette image.
	 * @param x
	 * 		L'abscisse de cette image.
	 * @param y
	 * 		L'ordonn�e de cette image.
	 * @param w
	 * 		La largeur de cette image.
	 * @param h
	 * 		La hauteur de cette image.
	 * @param couleur
	 * 		La couleur de cette image.
	 */
	public Image(String name, double x, double y, double w, double h, Color couleur) {
		
		if (name != null)
			this.name = name;
		else
			this.name = "";
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		if (couleur != null)
			this.couleur = couleur;
		else
			this.couleur = Color.white;
		this.plan = 0;
		this.angle = 0;
		this.sound = "";
		this.spatialSound = true;
		
	}
	
	/**
	 * Retourne une copie de cette image.
	 * 
	 * @return
	 * 		Une copie de cette image.
	 */
	@Override
	public Image generateImage() {
		
		return this.clone();
		
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
	 * Compare cette image � l'image pass�e en param�tre selon leurs plans de perspective.
	 * 
	 * <p>Si le param�tre est null, le plan de cette image est retourn�, sinon, la diff�rence entre le plan de cette image
	 * et celui de l'image pass�e en param�tre est retourn�e.</p>
	 * 
	 * @param img
	 * 		L'image � comparer � cette image.
	 * @return
	 * 		La diff�rence entre le plan de perspective de cette image et celui de l'image � comparer.
	 */
	@Override
	public int compareTo(Image img) {
		
		if (img == null)
			return this.getPlan();
		return this.getPlan()-img.getPlan();
		
	}
	
	/**
	 * Clone cette image.
	 * 
	 * @return
	 * 		Une copie de cette image, ou null si le clonage �choue.
	 */
	@Override
	public Image clone() {
		
		Image image = null;
		
		try {
			image = (Image) super.clone();
		} catch (CloneNotSupportedException e) {
			// Ne peut pas se produire en th�orie.
		}
		
		if (image == null)
			return null;
		
		image.couleur = new Color(this.getCouleur());
		
		return image;
		
	}
	
	/**
	 * Retourne le nom de cette image.
	 * 
	 * @return
	 * 		Le nom de cette image.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Modifie l'abscisse de cette image.
	 * 
	 * @param x
	 * 		L'abscisse de cette image.
	 */
	@Override
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Retourne l'abscisse de cette image.
	 * 
	 * @return
	 * 		L'abscisse de cette image.
	 */
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * Modifie l'ordonn�e de cette image.
	 * 
	 * @param y
	 * 		L'ordonn�e de cette image.
	 */
	@Override
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Retourne l'ordonn�e de cette image.
	 * 
	 * @return
	 * 		L'ordonn�e de cette image.
	 */
	@Override
	public double getY() {
		return this.y;
	}

	/**
	 * Modifie la largeur de cette image.
	 * 
	 * @param w
	 * 		La largeur de cette image.
	 */
	@Override
	public void setW(double w) {
		this.w = w;
	}

	/**
	 * Retourne la largeur de cette image.
	 * 
	 * @return
	 * 		La largeur de cette image.
	 */
	@Override
	public double getW() {
		return this.w;
	}

	/**
	 * Modifie la hauteur de cette image.
	 * 
	 * @param h
	 * 		La hauteur de cette image.
	 */
	@Override
	public void setH(double h) {
		this.h = h;
	}

	/**
	 * Retourne la hauteur de cette image.
	 * 
	 * @return
	 * 		La hauteur de cette image.
	 */
	@Override
	public double getH() {
		return this.h;
	}
	
	/**
	 * Met � jour la couleur de cette image.
	 * 
	 * @param color
	 * 		La couleur de cette image.
	 */
	public void setCouleur(Color color) {
		this.couleur = color;
	}
	
	/**
	 * Retourne la couleur de cette image.
	 * 
	 * @return
	 * 		La couleur de cette image.
	 */
	public Color getCouleur() {
		return this.couleur;
	}
	
	/**
	 * Met � jour le plan de perspective de cette image.
	 * 
	 * @param plan
	 * 		Le plan de perspective de cette image.
	 */
	public void setPlan(int plan) {
		this.plan = plan;
	}
	
	/**
	 * Retourne le plan de perspective de cette image.
	 * 
	 * @return
	 * 		Le plan de perspective de cette image.
	 */
	public int getPlan() {
		return this.plan;
	}
	
	/**
	 * Modifie l'angle de cette image.
	 * 
	 * @param angle
	 * 		L'angle de cette image.
	 */
	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	/**
	 * Retourne l'angle de cette image.
	 * 
	 * @return
	 * 		L'angle de cette image.
	 */
	public int getAngle() {
		return this.angle;
	}
	
	/**
	 * Modifie le son jou� avec cette image.
	 * 
	 * @param sound
	 * 		Le nom du fichier son.
	 */
	public void setSound(String sound) {
		this.sound = sound;
	}
	
	/**
	 * Retourne le son jou� avec cette image.
	 * 
	 * @return
	 * 		Le nom du fichier son.
	 */
	public String getSound() {
		return this.sound;
	}
	
	/**
	 * Modifie la valeur du bool�en spatialSound de cette classe.
	 * 
	 * @param spatialSound
	 * 		Vrai si le son doit �tre spatialis�.
	 */
	public void setSpatialSound(boolean spatialSound) {
		this.spatialSound = spatialSound;
	}
	
	/**
	 * Indique si le son de cette image doit �tre spatialis�.
	 * 
	 * @return
	 * 		Vrai si le son doit �tre spatialis�.
	 */
	public boolean isSpatialSound() {
		return this.spatialSound;
	}
	
}
