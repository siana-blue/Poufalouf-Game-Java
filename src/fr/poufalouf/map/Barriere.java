package fr.poufalouf.map;

import java.util.ArrayList;

import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Orientation;
import fr.poufalouf.tools.Rectangle;
import fr.poufalouf.tools.ZoneType;

/**
 * D�cor bloquant repr�sentant une barri�re
 * 
 * <p>L'�tat initial d'une barri�re se limite � un piquet.</p>
 * <p>Deux barri�res c�te-�-c�te peuvent remplir automatiquement l'espace qui les s�pare par une autre instance de barri�re
 * repr�sent�e par des planches liant les deux piquets.</p>
 * 
 * @author Anaïs Vernet
 */
public class Barriere extends Decor {
	
	/**
	 * Constructeur Barriere.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>L'orientation de cette barri�re est ensuite sp�cifi�e. Trois valeurs peuvent �tre prises par cette orientation :
	 * <ul>
	 * <li>AUCUNE : cette barri�re est un simple piquet.</li>
	 * <li>VERTICALE : cette barri�re repr�sente des planches reliant deux piquets dans la direction Nord-Sud.</li>
	 * <li>HORIZONTALE : cette barri�re repr�sente des planches reliant deux piquets dans la direction Est-Ouest.</li>
	 * </ul>
	 * </p>
	 * <p>La zone de collision de cette barri�re est ensuite d�finie selon son orientation.</p>
	 * 
	 * @param carte
	 * 		La carte � laquelle appartient cette barri�re.
	 * @param x
	 * 		L'abscisse par rapport au jeu de cette barri�re.
	 * @param y
	 * 		L'ordonn�e par rapport au jeu de cette barri�re.
	 * @param w
	 * 		La largeur de cette barri�re.
	 * @param h
	 * 		La hauteur de cette barri�re.
	 * @param orientation
	 * 		L'orientation de cette barri�re.
	 */
	public Barriere(Map carte, double x, double y, double w, double h, Orientation orientation) {
		
		super("Barriere"+
				((orientation==Orientation.VERTICALE)?"(LiaisonVt)":
					(orientation==Orientation.HORIZONTALE)?"(LiaisonHz)":""), carte, x, y, w, h, null, 0);
		this.setOrientation(orientation);
		switch (this.getOrientation()) {
		case VERTICALE:
			this.changeZone(ZoneType.COLLISION, new Rectangle(0.5, 0.5, 0, 1,
					Constantes.sizeCell));
			break;
		case HORIZONTALE:
			this.changeZone(ZoneType.COLLISION, new Rectangle(0, 0.75, 0.75, 0,
					Constantes.sizeCell));
			break;
		default:
			this.changeZone(ZoneType.COLLISION, new Rectangle(0.5, 0.75, 0, 0,
					Constantes.sizeCell));
		}
		
	}
	
	/**
	 * Cr�e une barri�re entre deux piquets adjacents.
	 * 
	 * <p>Cette m�thode r�cup�re la Case voisine de cette barri�re dans la direction pass�e en param�tre, gr�ce � la
	 * m�thode getNextCell(Orientation) de la classe Case appliqu�e � la premi�re case occup�e par cette barri�re
	 * (retourn�e par la m�thode cells() de la classe MapObject. Si un occupant de cette case
	 * est une barri�re, alors une barri�re de liaison sous forme de planches est g�n�r�e pour combler l'espace.</p>
	 * <p>Seules les orientations EST et SUD peuvent �tre pass�es en param�tre.
	 * <ul>
	 * <li>Si l'orientation est EST, la barri�re cr��e est d'orientation HORIZONTALE et est plac�e � 18 pixels
	 * du bord de la zone image de cette barri�re. La largeur de la zone image de la nouvelle barri�re est �gal � la
	 * constante sizeCell de la classe Constantes moins 5 pixels.</li>
	 * <li>Si l'orientation est SUD, la barri�re cr��e est d'orientation VERTICALE et est plac�e � 9 pixels du bord de la
	 * zone image de cette barri�re. La hauteur de la zone image de la nouvelle barri�re est �gale � la constante sizeCell
	 * de la classe Constantes.</li>
	 * </ul></p>
	 * 
	 * @param orientation
	 * 		L'orientation dans laquelle faire les tests de remplissage.
	 * @throws ObjectInstanciationException
	 * 		si une erreur survient lors de l'instanciation des barri�res de liaison.
	 */
	public void fillSpaces(Orientation orientation) throws ObjectInstanciationException {
		
		if (this.cells(false) == null || this.cells(false).size() == 0 || this.cells(false).get(0) == null)
			return;
		
		Case nextCell = this.cells(false).get(0).nextCell(orientation);
		ArrayList<MapObject> list = new ArrayList<MapObject>();
		if (nextCell != null) {
			for (MapObject obj : nextCell.occupants()) {
				if (obj != null)
					list.add(obj);
			}
			// Deux boucles s�par�es pour �viter les erreurs de modifications concurrentes.
			for (MapObject obj : list) {
				if (obj instanceof Barriere) {
					if (orientation == Orientation.EST)
						this.carte.addObject(new Barriere(this.carte, this.getX()+18, this.getY(),
								Constantes.sizeCell-4, Constantes.sizeCell, Orientation.HORIZONTALE), true);
					else if (orientation == Orientation.SUD)
						this.carte.addObject(new Barriere(this.carte, this.getX(), this.getY()+9,
								Constantes.sizeCell, Constantes.sizeCell, Orientation.VERTICALE), true);
				}
			}
		}
		
	}
	
}
