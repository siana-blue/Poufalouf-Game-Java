package fr.poufalouf.tools;

import java.util.ArrayList;

/**
 * Image compos�e de plusieurs autres images
 * 
 * <p>Cette classe h�rite de la classe TexturedImage, il s'agit donc d'une image textur�e pouvant �tre affich�e comme
 * telle. Cette image sera appel�e image principale. Si cette image est trait�e comme une CombinedImage, alors
 * elle est rendue par plusieurs images superpos�es dans l'ordre de la liste qu'elle contient. La premi�re
 * image affich�e est celle d�finie par le constructeur, l'image principale. Elle est positionn�e de mani�re absolue par
 * ses coordonn�es. Les images de la liste sont ensuite affich�es en coordonn�es relatives, fractions de la taille de 
 * l'image principale. Une image d'abscisse 0.5 sera affich�e � partir du milieu de l'image principale. De m�me, la largeur
 * et la hauteur sont exprim�es en pourcentages de l'image principale.</p>
 * <p>Il est possible de n'affecter aucun identifiant de texture � cet objet pour l'image principale. Dans ce
 * cas, l'image principale ne sera pas affich�e par le GameThread mais servira � positionner les sous-images de cette
 * classe qui, elles, seront affich�es.</p>
 * 
 * @author Anaïs Vernet
 */
public class CombinedImage extends TexturedImage {
	
	/**
	 * La liste des images composant cette image combin�e.
	 */
	private ArrayList<TexturedImage> images;
	
	/**
	 * Constructeur CombinedImage.
	 * 
	 * <p>Ce constructeur appelle le constructeur 2 de la classe m�re.</p>
	 * <p>Il cr�e une image textur�e ayant les propri�t�s de l'image pass�e en param�tre (img). Si ce param�tre est null,
	 * le constructeur m�re recevra des param�tres par d�faut.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>images : une nouvelle liste (ArrayList) de TexturedImage.</li>
	 * </ul></p>
	 * 
	 * @param img
	 * 		L'image principale de cette image combin�e.
	 */
	public CombinedImage(TexturedImage img) {
		
		super(img == null ? null : img.getName(), img == null ? 0 : img.getX(), img == null ? 0 : img.getY(),
				img == null ? 0 : img.getW(), img == null ? 0 : img.getH(), img == null ? null : img.getTextureID(),
						img == null ? null : img.getTextRect());
		this.images = new ArrayList<TexturedImage>();

	}
	
	/**
	 * Clone cette image combin�e.
	 * 
	 * @return
	 * 		Une copie de cette image combin�e, ou null si le clonage �choue.
	 */
	@Override
	public CombinedImage clone() {
		
		CombinedImage image = (CombinedImage) super.clone();
		
		if (image == null)
			return null;
		
		image.images = new ArrayList<TexturedImage>();
		for (TexturedImage i : this.images) {
			/*
			 * La boucle infinie est impossible car la m�thode addImage(TexturedImage) de cette classe interdit
			 * l'ajout d'images combin�es.
			 */
			image.images.add(i.clone());
		}
		
		return image;
		
	}
	
	/**
	 * Retourne une nouvelle image textur�e reprenant l'image principale de cette image combin�e.
	 * 
	 * @return
	 * 		L'image principale de cette image combin�e.
	 */
	public TexturedImage mainImage() {
		
		TexturedImage img = new TexturedImage(this.getName(), this.getX(), this.getY(), this.getW(), this.getH(),
				this.getTextureID(), this.getTextRect());
		img.setCouleur(this.getCouleur());
		img.setPlan(this.getPlan());
		img.setAngle(this.getAngle());
		img.setSound(this.getSound());
		img.setSpatialSound(this.isSpatialSound());
		return img;
		
	}
	
	/**
	 * Retourne une copie de la liste des sous-images de cette image combin�e.
	 * 
	 * <p>La liste retourn�e est une copie mais les instances de TexturedImage sont les originales.</p>
	 * 
	 * @return
	 * 		Une copie de la liste des sous-images.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TexturedImage> images() {
		
		return (ArrayList<TexturedImage>) this.images.clone();
		
	}
	
	/**
	 * Ajoute une image textur�e � la liste des images de cette image combin�e.
	 * 
	 * <p>Si l'image pass�e en param�tre est null, alors elle n'est pas ajout�e. Les images
	 * combin�es sont interdites pour �viter une erreur de boucle infinie lors du clonage de cette image. Une boucle
	 * infinie pourrait effectivement survenir si une image combin�e poss�de comme sous-image une instance d'elle-m�me
	 * ou une instance de CombinedImage poss�dant une instance d'elle-m�me (et ainsi de suite). Les CombinedImage
	 * sont donc transform�es en une s�rie de TexturedImage qui sont toutes ajout�es � celle-ci.</p>
	 * <p>Les coordonn�es de l'image � ajouter doivent �tre en coordonn�es relatives � l'image principale de cette image
	 * combin�e.</p>
	 * 
	 * @param img
	 * 		L'image textur�e � ajouter.
	 */
	public void addImage(TexturedImage img) {
		
		if (img != null) {
			if (img instanceof CombinedImage) {
				if (img.getTextureID() != "")
					this.addImage(((CombinedImage) img).mainImage());
				for (TexturedImage i : ((CombinedImage) img).images()) {
					TexturedImage t = i.clone();
					t.setX(t.getX()*img.getW()+img.getX());
					t.setY(t.getY()*img.getH()+img.getY());
					t.setW(t.getW()*img.getW());
					t.setH(t.getH()*img.getH());
					this.addImage(t);
				}
			} else if (img.getTextureID() != "")
				this.images.add(img);
		}
		
	}

}
