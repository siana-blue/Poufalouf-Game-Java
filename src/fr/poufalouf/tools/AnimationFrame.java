package fr.poufalouf.tools;

/**
 * Etape d'une animation
 * 
 * <p>Cette classe est immuable.</p>
 * 
 * @author Anaïs Vernet
 */
public class AnimationFrame {

	/**
	 * Le rectangle des coordonn�es du fragment de texture � utiliser pour cette frame.
	 */
	private final Rectangle text;
	/**
	 * Le nom du fichier son � jouer lors de cette frame.
	 */
	private final String sound;
	/**
	 * Le num�ro du cycle d'animation auquel il faut jouer le son de cette frame (1 pour le premier cycle, -1 pour tous
	 * les cycles).
	 */
	private final int cycleForSound;
	
	/**
	 * Constructeur AnimationFrame.
	 * 
	 * <p></p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>text : le rectangle pass� en param�tre (text), ou un rectangle de coordonn�es (0, 0) et de dimensions d�finies
	 * par le champ sizeTextureFragment de la classe Constantes si le param�tre est null.</li>
	 * <li>sound : le son pass� en param�tre (sound), ou "" si le param�tre est null.</li>
	 * <li>cycleForSound : le num�ro pass� en param�tre (cycleForSound).</li>
	 * </ul></p>
	 * 
	 * @param text
	 * 		Le rectangle des coordonn�es du fragment de texture � utiliser pour cette frame.
	 * @param sound
	 * 		Le nom du fichier son de cette frame (null ou "" pour une frame muette).
	 * @param cycleForSound
	 * 		Le num�ro du cycle durant lequel jou� le son de cette frame (-1 pour tous les cycles, 1 pour le premier).
	 */
	public AnimationFrame(Rectangle text, String sound, int cycleForSound) {
		
		if (text != null)
			this.text = text;
		else
			this.text = new Rectangle(Constantes.sizeTextureFragment);
		this.sound = (sound==null?"":sound);
		this.cycleForSound = cycleForSound;
		
	}

	/**
	 * Retourne le rectangle des coordonn�es du fragment de texture � utiliser pour cette frame.
	 * 
	 * @return
	 * 		Le rectangle des coordonn�es du fragment de texture.
	 */
	public Rectangle getText() {
		return this.text;
	}
	
	/**
	 * Retourne le nom du fichier son de cette frame.
	 * 
	 * @return
	 * 		Le fichier son de cette frame.
	 */
	public String getSound() {
		return this.sound;
	}
	
	/**
	 * Retourne le num�ro du cycle d'animation auquel jouer le son de cette frame.
	 * 
	 * @return
	 * 		Le num�ro du cycle.
	 */
	public int getCycleForSound() {
		return this.cycleForSound;
	}
	
}
