package fr.poufalouf.combat;

import java.util.ArrayList;
import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.tools.Animation;
import fr.poufalouf.tools.CombinedImage;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.Status;

/**
 * Effet d�coratif dans un combat
 * 
 * @author Anaïs Vernet
 */
public class CombatEffect extends CombatObject {
	
	/**
	 * L'action repr�sent�e par cet effet.
	 */
	private Move action;
	/**
	 * Le style d'animation de cet effet.
	 */
	private CombatEffectStyle style;
	/**
	 * Indique si cet effet a atteint le moment o� l'action qui lui est associ�e doit agir.
	 */
	private boolean readyToStrike;
	/**
	 * La frame � laquelle l'action associ�e doit agir.
	 */
	private int frameToStrike;
	/**
	 * Les coordonn�es de la trajectoire de cet effet.
	 */
	private Rectangle[] trajectory;
	
	/**
	 * Constructeur CombatEffect.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>L'animation pass�e en param�tre est ajout�e � la table des animations de cet effet. Enfin, le plan
	 * de cet objet est d�fini par le champ maxPlan de la classe Constantes.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>action : l'action pass�e en param�tre (action), ou une nouveau Move standard si le param�tre est
	 * null.</li>
	 * <li>style : le style pass� en param�tre (style), ou RELATIVE_TO_SCREEN si le param�tre est null.</li>
	 * <li>readyToStrike : faux.</li>
	 * <li>frameToStrike : le num�ro pass� en param�tre (frameToStrike).</li>
	 * <li>trajectory : le tableau pass� en param�tre (trajectory), ou un nouveau tableau de Rectangle vide si le
	 * param�tre est null.</li>
	 * </ul></p>
	 * 
	 * @param action
	 * 		L'action repr�sent�e par cet effet.
	 * @param name
	 * 		Le nom de cet effet.
	 * @param rect
	 * 		Le rectangle des coordonn�es de cet effet.
	 * @param anims
	 * 		La liste des animations de cet effet.
	 * @param style
	 * 		Le style d'animation de cet effet.
	 * @param frameToStrike
	 * 		Le num�ro de la frame � laquelle l'action associ�e � cet effet doit agir.
	 * @param trajectory
	 * 		Les coordonn�es de la trajectoire de cet effet.
	 * @throws ObjectInstanciationException
	 * 		si le param�tre action est null.
	 */
	public CombatEffect(Move action, String name, Rectangle rect, ArrayList<Animation> anims, CombatEffectStyle style,
			int frameToStrike, Rectangle[] trajectory) throws ObjectInstanciationException {
		
		super(name, rect);
		
		if (action == null)
			throw new ObjectInstanciationException("L'attaque associ�e � cet effet n'est pas d�finie.", this.getName());
		this.action = action;
		this.style = (style==null)?CombatEffectStyle.RELATIVE_TO_SCREEN:style;
		this.readyToStrike = false;
		this.frameToStrike = frameToStrike;
		this.trajectory = (trajectory==null)?new Rectangle[0]:trajectory;
		
		for (Animation a : anims)
			this.addAnimation(a);
		this.setPlan(Constantes.maxPlan);
		
	}
	
	/**
	 * R�initialise cet effet.
	 * 
	 * <p>Le bool�en readyToStrike de cette classe passe � faux, et l'animation de cet effet est r�initialis�e.</p>
	 */
	public void reset() {
		
		this.readyToStrike = false;
		this.setFinished(false);
		this.changeCurrentAnim(Status.STILL);
		this.currentAnim().reset();
		
	}
	
	/**
	 * En fonction du syle de cette animation, met � jour l'animation de la cible et du lanceur de l'attaque repr�sent�e
	 * par cet effet.
	 * 
	 * <p>
	 * <ul>
	 * <li>RELATIVE_TO_TARGET : L'action agit quand le timer de l'animation est � 1 (� la fin) et que sa frame
	 * est au num�ro sp�cifi� par le champ frameToStrike de cette classe.</li>
	 * <li>RELATIVE_TO_TARGET_HEART : La m�me chose que ci-dessus.</li>
	 * <li>FROM_ATTACKER_TO_TARGET : L'action agit quand le timer de l'animation est �gal au nombre de cylces d'animation
	 * (au d�but).</li>
	 * <li>THROWN_TO_TARGET : L'action agit quand l'animation atteint sa derni�re frame, et l'animation passe � DEAD.</li>
	 * </ul></p>
	 */
	@Override
	public void playBehavior() {
		
		switch (this.style) {
		case RELATIVE_TO_TARGET:
		case RELATIVE_TO_TARGET_HEART:
			if (this.currentAnim().getTimer() == 1 && this.currentAnim().frameNumber() == this.frameToStrike) {
				this.readyToStrike = true;
			}
			break;
		case FROM_ATTACKER_TO_TARGET:
			if (this.currentAnim().frameNumber() == 0) {
				if (this.currentAnim().getTimer() == this.currentAnim().getNbCycles())
					this.readyToStrike = true;
			}
			break;
		case THROWN_TO_TARGET:
			if (this.currentAnim().frameNumber() == this.currentAnim().numberOfFrames()-1
					&& this.currentAnim().getStatus() == Status.STILL) {
				this.readyToStrike = true;
				this.changeCurrentAnim(Status.DEAD);
			}
			break;
		default:
		}
		
	}
	
	/**
	 * Retourne une image repr�sentant cet effet.
	 * 
	 * <p>Cette m�thode appelle la m�thode generateImage() de la classe m�re.</p>
	 * <p>Elle la repositionne ensuite selon le style d'animation de cet effet :
	 * <ul>
	 * <li>RELATIVE_TO_SCREEN : les coordonn�es sont mulitipli�es par les champs initialDisplayWidth et initialDisplayHeight
	 * de la classe Constantes.</li>
	 * <li>RELATIVE_TO_TARGET : les coordonn�es sont fractions de l'image de la cible.</li>
	 * <li>RELATIVE_TO_TARGET_HEART : les coordonn�es (w et h) sont des fractions de la taille de la cible. x et y ne sont
	 * pas d�termin�es selon les coordonn�es de l'attaque mais calcul�es en fonction du coeur du personnage cible.</li>
	 * <li>FROM_ATTACKER_TO_TARGET : seules w est d�termin�e � partir de l'attaque, renseign�e en taille absolue.
	 * Les autres coordonn�es sont calcul�es de fa�on � partir du centre du lanceur vers le centre de la cible.
	 * h est calcul�e de fa�on � obternir une longueur d'image suffisante, puis l'image subit une rotation pour relier
	 * les deux adversaires.</li>
	 * <li>THROWN_TO_TARGET : seules w et h sont d�termin�es � partir de l'attaque, en coordonn�es relatives au lanceur.
	 * Les autres coordonn�es sont calcul�es par rapport au lanceur, puis le projectile avance entre les deux personnages
	 * avec un nombre de pas �gal au nombre de frames de l'animation. L'animation DEAD est ensuite jou�e au niveau
	 * du point d'impact. Les coordonn�es de la trajectoire de cet effet sont ensuite ajout�es aux coordonn�es
	 * calcul�es (et les dimensions sont multipli�es par celle de la trajectoire). L'indice de la trajectoire est, s'il
	 * est valide, �gal � celui de la frame en cours (m�thode frameNumber de la classe Animation).</li>
	 * </ul></p>
	 */
	@Override
	public CombinedImage generateObjectImage() {
		
		CombinedImage t = super.generateObjectImage();
		t.setCouleur(this.getColor());
		CombatCharacter target = this.action.getTarget();
		CombatCharacter attacker = this.action.getUser();
		
		double x1;
		double x2;
		double y1;
		double y2;
		double d;
		double theta;
		
		switch (this.style) {
		case RELATIVE_TO_SCREEN:
			t.setX(t.getX()*Constantes.initialDisplayWidth);
			t.setY(t.getY()*Constantes.initialDisplayHeight);
			t.setW(t.getW()*Constantes.initialDisplayWidth);
			t.setH(t.getH()*Constantes.initialDisplayHeight);
			t.setPlan(Constantes.maxPlan);
			break;
		case RELATIVE_TO_TARGET:
			t.setW(t.getW()*target.getW());
			t.setH(t.getH()*target.getH());
			t.setX(target.getX()+t.getX()*target.getW());
			t.setY(target.getY()+t.getY()*target.getH());
			t.setPlan(Constantes.maxPlan);
			break;
		case RELATIVE_TO_TARGET_HEART:
			t.setW(t.getW()*target.getW());
			t.setH(t.getH()*target.getH());
			t.setX(target.getX()+target.getHeartCenterX()*target.getW()-t.getW()/2);
			t.setY(target.getY()+target.getHeartCenterY()*target.getH()-t.getH()/2);
			t.setPlan(Constantes.maxPlan);
			break;
		case FROM_ATTACKER_TO_TARGET:
			x1 = attacker.getX()+attacker.getShootCenterX()*attacker.getW();
			x2 = target.getX()+target.getHeartCenterX()*target.getW();
			y1 = attacker.getY()+attacker.getShootCenterY()*attacker.getH();
			y2 = target.getY()+target.getHeartCenterY()*target.getH();
			theta = Math.acos((x1-x2)/Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)))/Math.PI*180;
			d = Math.sqrt(Math.pow(Math.abs(y2-y1), 2)+Math.pow(Math.abs(x2-x1), 2));
			t.setH(d);
			t.setX(Math.abs(x2-x1)/2+Math.min(x1, x2)-t.getW()/2);
			t.setY(Math.abs(y1-y2)/2+Math.min(y1, y2)-t.getH()/2);
			t.setAngle(90+((y2>y1)?-1:1)*(int) theta);
			t.setPlan(Constantes.attackEffectPlan);
			break;
		case THROWN_TO_TARGET:
			double step;
			int f = this.currentAnim().frameNumber();
			if (this.currentAnim().getStatus() == Status.STILL)
				step = (double) f/(this.currentAnim().numberOfFrames()-1);
			else
				step = 1;
			x1 = attacker.getX()+attacker.getShootCenterX()*attacker.getW();
			x2 = target.getX()+target.getHeartCenterX()*target.getW();
			y1 = attacker.getY()+attacker.getShootCenterY()*attacker.getH();
			y2 = target.getY()+target.getHeartCenterY()*target.getH();
			t.setW(t.getW()*attacker.getW());
			t.setH(t.getH()*attacker.getH());
			t.setX(attacker.getX()+attacker.getShootCenterX()*attacker.getW()-t.getW()/2);
			t.setY(attacker.getY()+attacker.getShootCenterY()*attacker.getH()-t.getH()/2);
			theta = Math.acos(Math.abs(x1-x2)/Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)));
			d = Math.sqrt(Math.pow(Math.abs(y2-y1), 2)+Math.pow(Math.abs(x2-x1), 2));
			t.setX(t.getX()+
					((x2>x1)?1:-1)*d*step*Math.cos(theta));
			t.setY(t.getY()+
					((y2>y1)?1:-1)*d*step*Math.sin(theta));
			if (f < this.trajectory.length && this.currentAnim().getStatus() == Status.STILL) {
				Rectangle r = this.trajectory[f];
				if (r != null) {
					t.setX(t.getX()+this.trajectory[f].getX());
					t.setY(t.getY()+this.trajectory[f].getY());
					t.setW(t.getW()*this.trajectory[f].getW());
					t.setH(t.getH()*this.trajectory[f].getH());
				}
			}
			t.setPlan(Constantes.attackEffectPlan);
			break;
		}
		
		return t;
		
	}
	
	/**
	 * Retourne le style d'animation de cet effet.
	 * 
	 * @return
	 * 		Le style d'animation.
	 */
	public CombatEffectStyle getStyle() {
		return this.style;
	}
	
	/**
	 * Indique si cet effet a atteint le moment o� l'attaque qui lui est associ�e doit agir.
	 * 
	 * @return
	 * 		Vrai s'il est temps d'appliquer les effets de l'attaque.
	 */
	public boolean isReadyToStrike() {
		return this.readyToStrike;
	}
	
	/**
	 * Retourne le num�ro de la frame o� l'action li�e � cet effet doit agir.
	 * 
	 * @return
	 * 		Le num�ro de la frame.
	 */
	public int getFrameToStrike() {
		return this.frameToStrike;
	}
	
	/**
	 * Retourne une copie de la trajectoire de cet effet.
	 * 
	 * @return
	 * 		Une copie de la trajectoire de cet effet.
	 */
	public Rectangle[] getTrajectory() {
		return this.trajectory.clone();
	}

}
