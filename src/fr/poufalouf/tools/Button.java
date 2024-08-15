package fr.poufalouf.tools;

import java.util.ArrayList;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;

import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.game.GameObject;

/**
 * Bouton activable
 * 
 * @author Anaïs Vernet
 */
public class Button extends GameObject implements GUIElement {
	
	/**
	 * Le texte de ce bouton, dont les coordonn�es sont pr�cis�es en pourcentage de la taille de ce bouton.
	 */
	private Text text;
	/**
	 * L'image dessin�e sur ce bouton, dont les coordonn�es sont pr�cis�es en pourcentage de la taille de ce bouton.
	 */
	private TexturedImage image;
	/**
	 * Indique si ce bouton est s�lectionn�.
	 */
	private boolean highlighted;
	/**
	 * Indique si ce bouton est activ�.
	 */
	private boolean activated;
	/**
	 * Indique si ce bouton est en transition d'un �tat � un autre.
	 */
	private boolean inTransition;
	/**
	 * Indique si ce bouton est mort.
	 */
	private boolean dead;
	
	/**
	 * Constructeur Button 1.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il associe le texte pass� en param�tre (text) � ce bouton, gr�ce � un objet Text.</p>
	 * <p>Il appelle ensuite la m�thode createAnimationFromFile(String) de la classe Animation pour ajouter les animations
	 * n�cessaires � ce bouton, gr�ce au param�tre animID. Si une exception est lev�e, aucune animation n'est ajout�e.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>text : un nouveau Text portant le texte pass� en param�tre (text), de couleur blanche, de police standard
	 * et de taille adapt�e � ce bouton.</li>
	 * <li>image : une nouvelle TexturedImage standard.</li>
	 * <li>highlighted : faux.</li>
	 * <li>activated : faux.</li>
	 * <li>inTransition : faux.</li>
	 * <li>dead : faux.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de ce bouton.
	 * @param x
	 * 		L'abscisse de ce bouton.
	 * @param y
	 * 		L'ordonn�e de ce bouton.
	 * @param w
	 * 		La largeur de ce bouton.
	 * @param h
	 * 		La largeur de ce bouton.
	 * @param animID
	 * 		Le nom du script d'animation de ce bouton.
	 * @param text
	 * 		Le texte � afficher sur ce bouton.
	 * 
	 * @throws ObjectInstanciationException
	 * 		si la lecture des animations de ce bouton l�ve une exception.
	 */
	public Button(String name, double x, double y, double w, double h, String animID, String text)
	throws ObjectInstanciationException{
		
		super(name, x, y, w, h);
		
		this.text = new Text((text==null)?"":text, Color.white, 0.03, 0.3, "res/textures/misc/lettres.png",
						(text==null)?0:(0.5-0.03*text.length()/2), 0.35);
		this.image = new TexturedImage(null, 0, 0, 0, 0, null);
		this.highlighted = false;
		this.activated = false;
		this.inTransition = false;
		this.dead = false;
		
		ArrayList<Animation> anims;
		try {
			anims = Animation.createAnimationFromFile(animID);
			for (Animation a : anims) {
				if (a != null)
					this.addAnimation(a);
			}
		} catch (Exception e) {
			throw new ObjectInstanciationException("Erreur lors de l'initialisation des animations.", this.getName(), e);
		}
		
		this.setDelayUpdate(90);
		
	}
	
	/**
	 * Constructeur Button 2.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Il associe l'ic�ne pass�e en param�tre (image) � ce bouton. Les coordonn�es de cette ic�ne doivent �tre des
	 * fractions de la taille de ce bouton.</p>
	 * <p>Il appelle ensuite la m�thode createAnimationFromFile(String) de la classe Animation pour ajouter les animations
	 * n�cessaires � ce bouton, gr�ce au param�tre animID. Si une exception est lev�e, aucune animation n'est ajout�e.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>text : un nouveau Text standard.</li>
	 * <li>image : l'image pass�e en param�tre (image), ou une nouvelle TexturedImage standard, si le param�tre est
	 * null.</li>
	 * <li>highlighted : faux.</li>
	 * <li>activated : faux.</li>
	 * <li>inTransition : faux.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de ce bouton.
	 * @param x
	 * 		L'abscisse de ce bouton.
	 * @param y
	 * 		L'ordonn�e de ce bouton.
	 * @param w
	 * 		La largeur de ce bouton.
	 * @param h
	 * 		La largeur de ce bouton.
	 * @param animID
	 * 		Le nom du script d'animation de ce bouton.
	 * @param image
	 * 		L'ic�ne � afficher sur ce bouton.
	 * 
	 * @throws ObjectInstanciationException
	 * 		si la lecture des animations de ce bouton l�ve une exception.
	 */
	public Button(String name, double x, double y, double w, double h, String animID, TexturedImage image)
	throws ObjectInstanciationException{
		
		super(name, x, y, w, h);

		this.text = new Text(null, null, 0, 0, null, 0, 0);
		this.image = (image==null)?new TexturedImage(null, 0, 0, 0, 0, null):image;
		this.highlighted = false;
		this.activated = false;
		this.inTransition = false;
		
		ArrayList<Animation> anims;
		try {
			anims = Animation.createAnimationFromFile(animID);
			for (Animation a : anims) {
				if (a != null)
					this.addAnimation(a);
			}
		} catch (Exception e) {
			throw new ObjectInstanciationException("Erreur lors de l'initialisation des animations.", this.getName(), e);
		}
		
		this.setDelayUpdate(90);
		
	}
	
	/**
	 * Met � jour ce bouton.
	 * 
	 * <p>
	 * <ul>
	 * <li>Si ce bouton est en transition et si son animation n'est pas TRANSITION, alors elle le devient.</li>
	 * <li>Si ce bouton est mort, son animation est DEAD, sinon et si son animation est DEAD, alors elle est STILL.</li>
	 * <li>Si ce bouton n'est pas en transition, s'il est s�lectionn�, son animation est HIGHLIGHTED, et s'il est activ�,
	 * son animation est ACTIVATED.</li>
	 * </ul></p>
	 * <p>La frame de l'animation en cours est incr�ment�e.
	 * Si le timer de l'animation TRANSITION en cours est � z�ro, alors l'animation de ce bouton
	 * est modifi�e : HIGHLIGHTED s'il est s�lectionn�, ACTIVATED s'il est activ�, STILL sinon. Le bool�en inTransition
	 * de cette classe devient faux.</p>
	 */
	@Override
	public void update() {
		
		long temps = Sys.getTime();
		if (temps-this.getCompteurTemps() > this.getDelayUpdate()) {
			this.setCompteurTemps(temps);
			Animation a = this.currentAnim();
			if (a != null) {
				if (this.inTransition) {
					this.changeCurrentAnim(Status.TRANSITION);
				} else {
					if (this.dead)
						this.changeCurrentAnim(Status.DEAD);
					else {
						if (a.getStatus() == Status.DEAD)
							this.changeCurrentAnim(Status.STILL);
						if (this.activated)
							this.changeCurrentAnim(Status.ACTIVATED);
						else if (this.highlighted)
							this.changeCurrentAnim(Status.HIGHLIGHTED);
						else
							this.changeCurrentAnim(Status.STILL);
					}
				}
				a.incrementFrame();
				if (a.getTimer() == 0 && a.getStatus() == Status.TRANSITION) {
					if (this.activated)
						this.changeCurrentAnim(Status.ACTIVATED);
					else if (this.highlighted)
						this.changeCurrentAnim(Status.HIGHLIGHTED);
					else
						this.changeCurrentAnim(Status.STILL);
					this.inTransition = false;
				}
			}
		}
		
	}
	
	/**
	 * Indique si ce type d'�l�ment peut �tre s�lectionn�.
	 * 
	 * @return
	 * 		Vrai.
	 */
	@Override
	public boolean isSelectable() {
		
		return true;
		
	}
	
	/**
	 * Rend ce bouton inactif, et le d�sactive.
	 */
	@Override
	public void disable() {
		
		this.dead = true;
		this.activated = false;
		this.highlighted = false;
		
	}
	
	/**
	 * Rend ce bouton actif.
	 */
	@Override
	public void enable() {
		
		this.dead = false;
		
	}
	
	/**
	 * Indique si cet �l�ment est actif.
	 * 
	 * @return
	 * 		Vrai si cet �l�ment est actif.
	 */
	@Override
	public boolean isEnabled() {
		
		return !this.dead;
		
	}
	
	/**
	 * S�lectionne ce bouton.
	 */
	@Override
	public void highlight() {
		
		this.inTransition = true;
		this.highlighted = true;
		
	}
	
	/**
	 * D�selectionne ce bouton.
	 */
	@Override
	public void leave() {
		
		this.highlighted = false;
		
	}
	
	/**
	 * Indique si ce bouton est s�lectionn�.
	 * 
	 * @return
	 * 		Vrai si ce bouton est s�lectionn�.
	 */
	@Override
	public boolean isHighlighted() {
		
		if (!this.inTransition)
			return this.highlighted;
		return false;
		
	}
	
	/**
	 * Active ce bouton.
	 */
	@Override
	public void activate() {
		
		this.inTransition = true;
		this.activated = true;
		
	}
	
	/**
	 * D�sactive ce bouton.
	 */
	@Override
	public void desactivate() {
		
		this.inTransition = true;
		this.activated = false;
		
	}
	
	/**
	 * Indique si ce bouton est activ�.
	 * 
	 * @return
	 * 		Vrai si ce bouton est activ�.
	 */
	@Override
	public boolean isActivated() {
		
		if (!this.inTransition)
			return this.activated;
		return false;
		
	}
	
	/**
	 * Indique si ce bouton est en cours de transition d'un �tat � un autre.
	 * 
	 * @return
	 * 		Vrai si ce bouton est en cours de transition.
	 */
	@Override
	public boolean isInTransition() {
		return this.inTransition;
	}

	/**
	 * G�n�re une image repr�sentant ce bouton.
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	@Override
	public CombinedImage generateObjectImage() {
		
		CombinedImage img = new CombinedImage(new TexturedImage("Image "+this.getName(), this.getX(), this.getY(),
				this.getW(), this.getH(), this.currentAnim().getTextureID(),
				this.currentAnim().textRect(Orientation.AUCUNE)));
		img.addImage(this.text.generateImage());
		img.addImage(this.image);
		
		return img;
		
	}
	
	/**
	 * Retourne le texte de ce bouton.
	 * 
	 * @return
	 * 		Le texte (objet) de ce bouton.
	 */
	public Text getText() {
		return this.text;
	}

}
