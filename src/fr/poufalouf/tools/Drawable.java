package fr.poufalouf.tools;

/**
 * Interface impl�ment�e par tout objet du jeu pouvant g�n�rer une image et ainsi �tre affich� � l'�cran
 * 
 * @author Anaïs Vernet
 */
public interface Drawable {

	/**
	 * Retourne une image caract�risant l'�tat de l'objet.
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	public Image generateImage();
	/**
	 * Indique si l'image g�n�r�e par ce Drawable doit �tre rendue par VBO.
	 * 
	 * <p>Si c'est le cas, inutile de pr�ciser de plan pour l'image g�n�r�e, elle sera en arri�re-plan.
	 * Seules les images textur�es sont trait�es par VBO.</p>
	 * 
	 * @return
	 * 		Vrai si l'image g�n�r�e est rendue par VBO.
	 */
	public boolean isVBORendered();
	/**
	 * Modifie l'abscisse de cet objet.
	 * 
	 * @param x
	 * 		L'abscisse de cet objet.
	 */
	public void setX(double x);
	/**
	 * Retourne l'abscisse de cet objet.
	 * 
	 * @return
	 * 		L'abscisse de cet objet.
	 */
	public double getX();
	/**
	 * Modifie l'ordonn�e de cet objet.
	 * 
	 * @param y
	 * 		L'ordonn�e de cet objet.
	 */
	public void setY(double y);
	/**
	 * Retourne l'ordonn�e de cet objet.
	 * 
	 * @return
	 * 		L'ordonn�e de cet objet.
	 */
	public double getY();
	/**
	 * Modifie la largeur de cet objet.
	 * 
	 * @param w
	 * 		La largeur de cet objet.
	 */
	public void setW(double w);
	/**
	 * Retourne la largeur de cet objet.
	 * 
	 * @return
	 * 		La largeur de cet objet.
	 */
	public double getW();
	/**
	 * Modifie la hauteur de cet objet.
	 * 
	 * @param h
	 * 		La hauteur de cet objet.
	 */
	public void setH(double h);
	/**
	 * Retourne la hauteur de cet objet.
	 * 
	 * @return
	 * 		La hauteur de cet objet.
	 */
	public double getH();
	
}
