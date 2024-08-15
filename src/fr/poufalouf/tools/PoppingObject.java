package fr.poufalouf.tools;

import java.util.ArrayList;

/**
 * Objet Drawable apparaissant momentan�ment � l'�cran en suivant une trajectoire d�finie
 * 
 * <p>La trajectoire de cet objet est d�finie par un tableau de Rectangle. Les abscisses et ordonn�es de ces rectangles
 * sont ajout�es aux coordonn�es de l'objet object de cette classe, lors de la g�n�ration de l'image. Les tailles w et h
 * des rectangles sont des coefficients � multiplier aux tailles respectives de l'objet.</p>
 * 
 * @author Anaïs Vernet
 */
public class PoppingObject implements Drawable {

	/**
	 * L'objet apparaissant.
	 */
	private Drawable object;
	/**
	 * Les coordonn�es de la trajectoire.
	 */
	private Rectangle[] trajectory;
	/**
	 * Le nombre de fois que la trajectoire doit �tre jou�e. (-1 pour l'infini)
	 */
	private int loop;
	/**
	 * L'indice actuel de cet objet sur sa trajectoire.
	 */
	private int currentIndex;
	/**
	 * Le plan de perspective de cet objet.
	 */
	private int plan;
	
	/**
	 * Constructeur PoppingObject.
	 * 
	 * <p>Le Drawable pass� en param�tre doit sp�cifier ses coordonn�es absolues, sauf s'il est pr�vu qu'il soit
	 * associ� � un GameObject, auquel cas celui-ci le redimensionnera lors de sa propre g�n�ration d'image.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>object : le drawable pass� en param�tre (drw), ou un nouveau Drawable standard si le param�tre est null.</li>
	 * <li>trajectory : les coordonn�es pass�es en param�tre (trajectory), ou un nouveau tableau de Rectangle vide si le
	 * param�tre est null.</li>
	 * <li>loop : le nombre pass� en param�tre (loop).</li>
	 * <li>currentIndex : 0.</li>
	 * <li>plan : Constantes.maxPlan.</li>
	 * </ul></p>
	 * 
	 * @param drw
	 * 		L'objet repr�sent�.
	 * @param trajectory
	 * 		Les coordonn�es de la trajectoire.
	 * @param loop
	 * 		Le nombre de fois que la trajectoire doit �tre jou�e.
	 */
	public PoppingObject(Drawable drw, Rectangle[] trajectory, int loop) {
		
		if (drw != null)
			this.object = drw;
		else
			this.object = new Drawable() {

				@Override
				public Image generateImage() {
					return null;
				}

				@Override
				public boolean isVBORendered() {
					return false;
				}

				@Override
				public double getX() {
					return 0;
				}

				@Override
				public double getY() {
					return 0;
				}

				@Override
				public double getW() {
					return 0;
				}

				@Override
				public double getH() {
					return 0;
				}

				@Override
				public void setX(double x) {
					// ne rien faire
				}

				@Override
				public void setY(double y) {
					// ne rien faire
				}

				@Override
				public void setW(double w) {
					// ne rien faire
				}

				@Override
				public void setH(double h) {
					// ne rien faire
				}
			
		};
		if (trajectory != null)
			this.trajectory = trajectory;
		else
			this.trajectory = new Rectangle[]{};
		this.loop = loop;
		this.currentIndex = 0;
		this.plan = Constantes.maxPlan;
		
	}
	
	/**
	 * Indique si cet objet a fini sa trajectoire et doit donc dispara�tre.
	 * 
	 * <p>Cette m�thode renvoie toujours faux si le champ loop de cette classe est n�gatif.</p>
	 * 
	 * @return
	 * 		Vrai si cet objet a disparu.
	 */
	public boolean isDead() {
		
		if (this.loop != 1)
			return false;
		return this.currentIndex >= this.trajectory.length;
		
	}

	
	/**
	 * Retourne une image repr�sentant cet objet.
	 * 
	 * <p>Cette m�thode appelle la m�thode generateImage() du Drawable de cette classe, qui sp�cifie ses coordonn�es
	 * absolues (sauf dans le cas d'un objet associ� � un GameObject). Elle d�place ensuite cette image
	 * selon les coordonn�es de la trajectoire � l'indice sp�cifi� par le champ currentIndex de cette classe.
	 * Les coordonn�es de la trajectoire sont ajout�es � celles de l'objet, et ses dimensions (w et h) sont multipli�es
	 * par celles de l'objet.</p>
	 * <p>Le champ currentIndex est alors incr�ment�. Quand il atteint la taille des tableaux de la trajectoire,
	 * le champ loop de cette classe est d�cr�ment�, s'il est positif. S'il est �gal � 0, alors null est retourn�.</p>
	 * <p>Si la taille des tableaux trajectoires ne sont pas �gales, alors le d�placement n'est pas fait.</p>
	 */
	@Override
	public Image generateImage() {
		
		if (this.currentIndex >= this.trajectory.length) {
			if (this.loop == 0)
				return null;
			this.loop--;
			this.currentIndex = 0;
		}
		
		Image image = this.object.generateImage();
		
		if (image == null)
			return null;
		
		Rectangle r = this.trajectory[this.currentIndex];
		if (r == null)
			return image;
		
		image.setX(image.getX()+r.getX());
		image.setY(image.getY()+r.getY());
		image.setW(image.getW()*r.getW());
		image.setH(image.getH()*r.getH());
		image.setPlan(this.plan);
		if (image instanceof CombinedImage) {
			ArrayList<TexturedImage> txts = ((CombinedImage) image).images();
			for (TexturedImage t : txts)
				t.setPlan(this.plan);
		}
		
		this.currentIndex++;
		
		return image;
		
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
	 * Modifie l'abscisse du Drawable de cet objet.
	 * 
	 * @param x
	 * 		L'abscisse de cet objet.
	 */
	@Override
	public void setX(double x) {
		this.object.setX(x);
	}
	
	/**
	 * Retourne l'abscisse du Drawable de cet objet.
	 * 
	 * @return
	 * 		L'abscisse de cet objet.
	 */
	@Override
	public double getX() {
		return this.object.getX();
	}
	
	/**
	 * Modifie l'ordonn�e du Drawable de cet objet.
	 * 
	 * @param y
	 * 		L'ordonn�e de cet objet.
	 */
	@Override
	public void setY(double y) {
		this.object.setY(y);
	}
	
	/**
	 * Retourne l'ordonn�e du Drawable de cet objet.
	 * 
	 * @return
	 * 		L'ordonn�e de cet objet.
	 */
	@Override
	public double getY() {
		return this.object.getY();
	}
	
	/**
	 * Modifie la largeur du Drawable de cet objet.
	 * 
	 * @param w
	 * 		La largeur de cet objet.
	 */
	@Override
	public void setW(double w) {
		this.object.setW(w);
	}
	
	/**
	 * Retourne la largeur du Drawable de cet objet.
	 * 
	 * @return
	 * 		La largeur de cet objet.
	 */
	@Override
	public double getW() {
		return this.object.getW();
	}
	
	/**
	 * Modifie la hauteur du Drawable de cet objet.
	 * 
	 * @param h
	 * 		La hauteur de cet objet.
	 */
	@Override
	public void setH(double h) {
		this.object.setH(h);
	}
	
	/**
	 * Retourne la hauteur du Drawable de cet objet.
	 * 
	 * @return
	 * 		La hauteur de cet objet.
	 */
	@Override
	public double getH() {
		return this.object.getH();
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
	
}
