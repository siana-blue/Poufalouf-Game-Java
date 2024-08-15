package fr.poufalouf.tools;

/**
 * Barre de couleur pouvant se remplir selon un param�tre qui lui est li�
 * 
 * @author Anaïs Vernet
 */
public class Bar implements Drawable {
	
	/**
	 * Le nom de cette barre.
	 */
	private final String name;
	/**
	 * Le nom du fichier texture de cette barre.
	 */
	private final String textureID;
	/**
	 * Le taux de remplissage de cette barre, compris entre 0 et 1.
	 */
	private double rate;
	/**
	 * L'abscisse de cette barre.
	 */
	private double x;
	/**
	 * L'ordonn�e de cette barre.
	 */
	private double y;
	/**
	 * La largeur de cette barre.
	 */
	private double w;
	/**
	 * La hauteur de cette barre.
	 */
	private double h;
	
	/**
	 * Constructeur Bar.
	 * 
	 * <p></p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>name : le nom pass� en param�tre (name), ou "" si le param�tre est null.</li>
	 * <li>textureID : le nom de fichier pass� en param�tre (textureID), ou "" si le param�tre est null.</li>
	 * <li>rate : le taux pass� en param�tre (rate).</li>
	 * <li>x : l'abscisse pass�e en param�tre (x).</li>
	 * <li>y : l'ordonn�e pass�e en param�tre (y).</li>
	 * <li>w : la largeur pass�e en param�tre (w).</li>
	 * <li>h : la hauteur pass�e en param�tre (h).</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de cette barre.
	 * @param textureID
	 * 		Le nom du fichier texture de cette barre.
	 * @param rate
	 * 		Le taux de remplissage initial de cette barre.
	 * @param x
	 * 		L'abscisse de cette barre.
	 * @param y
	 * 		L'ordonn�e de cette barre.
	 * @param w
	 * 		La largeur de cette barre.
	 * @param h
	 * 		La hauteur de cette barre.
	 * 
	 */
	public Bar(String name, String textureID, double rate, double x, double y, double w, double h) {
		
		if (name != null)
			this.name = name;
		else
			this.name = "";
		if (textureID != null)
			this.textureID = textureID;
		else
			this.textureID = "";
		this.rate = rate;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
	}

	/**
	 * G�n�re une image traduisant l'�tat de cette barre.
	 * 
	 * <p>Il s'agit d'une image combin�e superposant la barre plus ou moins remplie au support de cette barre.
	 * Cette image est plac�e au premier plan.</p>
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	@Override
	public CombinedImage generateImage() {
		
		CombinedImage img = new CombinedImage(new TexturedImage(this.name+" [Support]",
				this.x, this.y, this.w, this.h, this.textureID, new Rectangle(0, 0, 1, Constantes.sizeTextureFragment)));
		
		double r;
		if (this.rate < 0)
			r = 0;
		else if (this.rate > 1)
			r = 1;
		else
			r = this.rate;
		img.addImage(new TexturedImage(this.name+" [Barre]",
				Constantes.sizeTextureFragment, 0, (2*Constantes.sizeTextureFragment)*r, 1,
				this.textureID, new Rectangle(Constantes.sizeTextureFragment, Constantes.sizeTextureFragment,
						(1-2*Constantes.sizeTextureFragment)*r, Constantes.sizeTextureFragment)));
		img.setPlan(Constantes.maxPlan);
		
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
	 * Met � jour le taux de remplissage de cette barre.
	 * 
	 * @param rate
	 * 		Le taux de remplissage.
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * Modifie l'abscisse de cette barre.
	 * 
	 * @param x
	 * 		L'abscisse de cette barre.
	 */
	@Override
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Retourne l'abscisse de cette barre.
	 * 
	 * @return
	 * 		L'abscisse de cette barre.
	 */
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * Modifie l'ordonn�e de cette barre.
	 * 
	 * @param y
	 * 		L'ordonn�e de cette barre.
	 */
	@Override
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Retourne l'ordonn�e de cette barre.
	 * 
	 * @return
	 * 		L'ordonn�e de cette barre.
	 */
	@Override
	public double getY() {
		return this.y;
	}

	/**
	 * Modifie la largeur de cette barre.
	 * 
	 * @param w
	 * 		La largeur de cette barre.
	 */
	@Override
	public void setW(double w) {
		this.w = w;
	}

	/**
	 * Retourne la largeur de cette barre.
	 * 
	 * @return
	 * 		La largeur de cette barre.
	 */
	@Override
	public double getW() {
		return this.w;
	}

	/**
	 * Modifie la hauteur de cette barre.
	 * 
	 * @param h
	 * 		La hauteur de cette barre.
	 */
	@Override
	public void setH(double h) {
		this.h = h;
	}

	/**
	 * Retourne la hauteur de cette barre.
	 * 
	 * @return
	 * 		La hauteur de cette barre.
	 */
	@Override
	public double getH() {
		return this.h;
	}

}
