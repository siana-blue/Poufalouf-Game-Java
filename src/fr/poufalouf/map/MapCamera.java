package fr.poufalouf.map;

import java.util.ArrayList;

import fr.poufalouf.tools.Camera;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Drawable;
import fr.poufalouf.tools.ZoneType;

/**
 * Type de cam�ra utilis� avec une Map
 * 
 * <p>Ce type de cam�ra se met � jour selon la position de Poufalouf pour le maintenir au centre de la vue.</p>
 * 
 * @author Anaïs Vernet
 */
public class MapCamera extends Camera {

	/**
	 * Constructeur MapCamera.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * 
	 * @param x
	 * 		L'abscisse de cette cam�ra par rapport au jeu.
	 * @param y
	 * 		L'ordonn�e de cette cam�ra par rapport au jeu.
	 */
	public MapCamera(int x, int y) {
		
		super(x, y);
		
	}
	
	/**
	 * Met � jour les coordonn�es de cette cam�ra selon la position de Poufalouf.
	 * 
	 * <p>Cette m�thode parcourt la liste de Drawable pass�e en param�tre jusqu'� trouver une instance de Poufalouf. Les
	 * coordonn�es de cette cam�ra sont alors modifi�es de fa�on � ce que Poufalouf soit au centre de la vue d'apr�s les
	 * champs initialDisplayWidth et initialDisplayHeight de la classe Constantes.</p>
	 * <p>Au premier Poufalouf rencontr�, la mise � jour a lieu puis cette m�thode est quitt�e.</p>
	 */
	@Override
	public void update(ArrayList<Drawable> images) {
		
		if (images == null)
			return;
		
		for (Drawable obj : images) {
			if (obj instanceof Poufalouf) {
				if (((Poufalouf) obj).zone(ZoneType.IMAGE) == null)
					continue;
				this.setX((int) (((Poufalouf) obj).getX()+((Poufalouf) obj).zone(ZoneType.IMAGE).getObjRect().getW()/2-Constantes.initialDisplayWidth/2));
				this.setY((int) (((Poufalouf) obj).getY()+((Poufalouf) obj).zone(ZoneType.IMAGE).getObjRect().getH()/2-Constantes.initialDisplayHeight/2));
				break;
			}
		}
		
	}
	
}
