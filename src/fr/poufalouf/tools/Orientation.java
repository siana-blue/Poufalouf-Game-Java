package fr.poufalouf.tools;

/**
 * Enum�ration des diff�rentes orientations possibles d'un objet
 * 
 * @author Anaïs Vernet
 */
public enum Orientation {

	/**
	 * Orientation non d�finie.
	 */
	AUCUNE,
	/**
	 * Orientation Sud ou vers le bas.
	 */
	SUD,
	/**
	 * Orientation Sud-Ouest ou vers le bas � gauche.
	 */
	SUD_OUEST,
	/**
	 * Orientation Ouest ou vers la gauche.
	 */
	OUEST,
	/**
	 * Orientation Nord-Ouest ou vers le haut � gauche.
	 */
	NORD_OUEST,
	/**
	 * Orientation Nord ou vers le haut.
	 */
	NORD,
	/**
	 * Orientation Nord-Est ou vers le haut � droite.
	 */
	NORD_EST,
	/**
	 * Orientation Est ou vers la droite.
	 */
	EST,
	/**
	 * Orientation Sud-Est ou vers le bas � droite.
	 */
	SUD_EST,
	/**
	 * Orientation horizontale ou Est-Ouest.
	 */
	HORIZONTALE,
	/**
	 * Orientation verticale ou Nord-Sud.
	 */
	VERTICALE,
	/**
	 * Centre.
	 */
	CENTRE;
	
	/**
	 * Retourne l'orientation suivante selon une rotation dans le sens sp�cifi� en param�tre.
	 * 
	 * @param clockwise
	 * 		Vrai si le sens de rotation est celui des aiguilles d'une montre.
	 * @return
	 * 		L'orientation suivante de la rotation.
	 */
	public Orientation next(boolean clockwise) {
		
		if (this.ordinal() > Orientation.SUD_EST.ordinal() || this.ordinal() < Orientation.SUD.ordinal())
			return this;
		if (clockwise) {
			if (this.ordinal() == Orientation.SUD_EST.ordinal())
				return Orientation.SUD;
			return Orientation.values()[this.ordinal()+1];
		}
		if (this.ordinal() == Orientation.SUD.ordinal())
			return Orientation.SUD_EST;
		return Orientation.values()[this.ordinal()-1];
		
	}
	
	/**
	 * Retourne la somme de cette orientation avec l'orientation pass�e en param�tre.
	 * 
	 * <p>
	 * <ul>
	 * <li>Si l'orientation pass�e en param�tre n'est pas un point cardinal, this est retourn�.</li>
	 * <li>Si cette orientation et l'orientation pass�e en param�tre est un point cardinal simple (N,S,E,O), alors la
	 * combinaison est retourn�e.</li>
	 * <li>Si les deux directions sont incompatibles (NORD-SUD par exemple), ou si l'une des deux est un point cardinal
	 * compos�, le param�tre est retourn�.</li>
	 * </ul></p>
	 * 
	 * @param or
	 * 		L'orientation � ajouter � cette orientation.
	 * @return
	 * 		La somme de cette orientation avec l'orientation pass�e en param�tre.
	 */
	public Orientation plus(Orientation or) {
		
		if (or == null || or.ordinal() < SUD.ordinal() || or.ordinal() > SUD_EST.ordinal())
			return this;
		if ((this == SUD && or == OUEST) || (this == OUEST && or == SUD))
			return SUD_OUEST;
		if ((this == SUD && or == EST) || (this == EST && or == SUD))
			return SUD_EST;
		if ((this == NORD && or == OUEST) || (this == OUEST && or == NORD))
			return NORD_OUEST;
		if ((this == NORD && or == EST) || (this == EST && or == NORD))
			return NORD_EST;
		return or;
		
	}
	
	/**
	 * Retourne le point cardinal r�sultant de la soustraction de l'orientation pass�e en param�tre � cette orientation.
	 * 
	 * <p>
	 * <ul>
	 * <li>Si l'orientation pass�e en param�tre n'est pas un point cardinal, this est retourn�.</li>
	 * <li>La soustraction est intuitive, et n'a lieu que si cette orientation est un point cardinal compos� et le
	 * param�tre un point cardinal simple (N,S,E,O). Par exemple, SUD_OUEST - SUD = OUEST.</li>
	 * <li>Si les deux directions sont incompatibles (NORD_OUEST-SUD par exemple), ou si l'un des deux membres de la
	 * soustraction n'est pas valide (EST-SUD_OUEST par exemple), le param�tre est retourn�.</li>
	 * </ul></p>
	 * 
	 * @param or
	 * 		L'orientation � soustraire de cette orientation.
	 * @return
	 * 		Le point cardinal simple r�sultant de la soustraction du param�tre � cette orientation.
	 */
	public Orientation moins(Orientation or) {
		 
		if (or == null || or.ordinal() < SUD.ordinal() || or.ordinal() > SUD_EST.ordinal())
			return this;
		if ((this == SUD_OUEST && or == OUEST) || (this == SUD_EST && or == EST))
			return SUD;
		if ((this == SUD_OUEST && or == SUD) || (this == NORD_OUEST && or == NORD))
			return OUEST;
		if ((this == NORD_OUEST && or == OUEST) || (this == NORD_EST && or == EST))
			return NORD;
		if ((this == NORD_EST && or == NORD) || (this == SUD_EST && or == SUD))
			return EST;
		return or;
		
	}
	
	/**
	 * Retourne la direction oppos�e � cette orientation.
	 * 
	 * <p>La direction oppos�e est d�finie comme suit :
	 * <ul>
	 * <li>Point cardinal oppos� s'il s'agit d'un point cardinal.</li>
	 * <li>Horizontale pour verticale et inversement.</li>
	 * <li>Cette orientation dans les autres cas (pas de changement).</li>
	 * </ul></p>
	 * 
	 * @return
	 * 		L'orientation oppos�e � cette orientation.
	 */
	public Orientation opposite() {
		
		if (this.ordinal() >= SUD.ordinal() && this.ordinal() <= NORD_OUEST.ordinal()) {
			return Orientation.values()[this.ordinal()+4];
		} else if (this.ordinal() >= NORD.ordinal() && this.ordinal() <= SUD_EST.ordinal()) {
			return Orientation.values()[this.ordinal()-4];
		} else if (this == HORIZONTALE) {
			return VERTICALE;
		} else if (this == VERTICALE) {
			return HORIZONTALE;
		} else
			return this;
		
	}
	
	/**
	 * Retourne une orientation cardinale simple (N,S,E,O) � partir de cette orientation.
	 * 
	 * <p>Si l'orientation pass�e en param�tre n'est pas une combinaison de points cardinaux, cette orientation est 
	 * retourn�e.</p>
	 * <p>Sinon, l'orientation voisine dans le sens inverse des aiguilles d'une montre est retourn�e.</p>
	 * 
	 * @return
	 * 		L'orientation N,S,E,O, ou cette orientation si elle n'est pas un point cardinal compos�.
	 */
	public Orientation toSimpleCardinal() {
		
		switch (this) {
		case SUD_OUEST:
			return SUD;
		case NORD_OUEST:
			return OUEST;
		case NORD_EST:
			return NORD;
		case SUD_EST:
			return EST;
		default:
			return this;
		}
		
	}
	
	/**
	 * Retourne la position correspondant � cette orientation d'un fragment de texture dans un fichier texture.
	 * 
	 * <p>0 est la premi�re position, puis chaque position est num�rot�e par un entier. Dans le fichier texture, la
	 * position 0 est le coin sup�rieur gauche, puis 1, 2 et 3 sont les images suivantes sur la ligne. La num�rotation
	 * continue ensuite sur les lignes suivantes.</p>
	 * <p>SUD correspond � 0, puis les orientations se succ�dent dans l'ordre de cette classe jusqu'� 7. HORIZONTALE
	 * est �galement � la position 1, et VERTICALE � la position 2. AUCUNE est � la position 0, avec SUD.</p>
	 * 
	 * @return
	 * 		La position de l'image.
	 */
	public int getNumText() {
		
		switch(this) {
		case SUD:
			return 0;
		case OUEST:
		case HORIZONTALE:
			return 1;
		case NORD:
		case VERTICALE:
			return 2;
		case EST:
			return 3;
		case SUD_OUEST:
			return 4;
		case NORD_OUEST:
			return 5;
		case NORD_EST:
			return 6;
		case SUD_EST:
			return 7;
		default:
			return 0;
		}
		
	}
	
}
