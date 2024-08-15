package fr.poufalouf.tools;

/**
 * Enum�ration des caract�ristiques d'un personnage ou d'une cr�ature
 * 
 * @author Anaïs Vernet
 */
public enum Characteristic {
	
	/**
	 * Les points de vie.
	 */
	HP("PV"),
	/**
	 * La valeur maximale des points de vie.
	 */
	HPMAX("PV MAX"),
	/**
	 * La pr�cision des attaques.
	 */
	ACCURACY("PRE"),
	/**
	 * La capacit� d'esquive d'une attaque.
	 */
	EVASION("ESQ"),
	/**
	 * La vitesse de lancement des attaques.
	 */
	SPEED("VIT"),
	/**
	 * La force des attaques physiques.
	 */
	STRENGTH("FOR"),
	/**
	 * La r�sistance face aux attaques physiques.
	 */
	DEFENSE("DEF");
	
	/**
	 * L'abr�viation affich�e en combat pour cette caract�ristique.
	 */
	private String shortName;
	
	/**
	 * Constructeur Characteristic.
	 * 
	 * <p>Il s'agit d'un constructeur priv� qui ne peut �tre utilis� que pour initialiser les champs de cette
	 * �num�ration.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>shortName : le nom pass� en param�tre (name), ou "" si le param�tre est null.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		L'abr�viation de cette caract�ristique.
	 */
	private Characteristic(String name) {
		
		if (name == null)
			this.shortName = "";
		else
			this.shortName = name;
		
	}
	
	/**
	 * Retourne l'abr�viation utilis�e en combat pour cette caract�ristique.
	 * 
	 * @return
	 * 		L'abr�viation de cette caract�ristique.
	 */
	public String getShortName() {
		return this.shortName;
	}

}
