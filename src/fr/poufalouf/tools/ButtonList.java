package fr.poufalouf.tools;

import java.util.ArrayList;
import java.util.HashMap;

import fr.poufalouf.ObjectInstanciationException;
import fr.poufalouf.game.GameObject;

/**
 * Une liste de boutons.
 * 
 * @author Anaïs Vernet
 */
public class ButtonList extends GameObject implements GUIElement {
	
	/**
	 * La liste des boutons de cette liste.
	 */
	private ArrayList<Button> buttons;
	/**
	 * Le nombre de lignes visibles en m�me temps dans cette liste.
	 */
	private int nbVisibleLines;
	/**
	 * Le nombre de boutons par ligne de cette liste.
	 */
	private int nbButtonsPerLine;
	/**
	 * Le num�ro de la ligne visible en haut de la liste.
	 */
	private int firstVisibleLine;
	/**
	 * Le bouton s�lectionn� sur cette liste.
	 */
	private int selectedButton;
	/**
	 * Indique si cette liste a le focus.
	 */
	private boolean highlighted;
	
	/**
	 * Constructeur ButtonList 1.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Apr�s initialisation, il ajoute un bouton pour toutes les cha�nes de caract�res pass�es en param�tre (buttons),
	 * dont les animations sont sp�cifi�es en param�tre (animID). La liste des noms des boutons doit �tre de m�me taille
	 * que la liste des cha�nes de caract�res.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>buttons : une nouvelle liste (ArrayList) de Button.</li>
	 * <li>nbVisibleLines : le nombre pass� en param�tre (nbVisibleLines), ou 1 si le param�tre est inf�rieur � 1.</li>
	 * <li>nbButtonsPerLine : le nombre pass� en param�tre (nbButtonsPerLines), ou 1 si le param�tre est inf�rieur � 1.</li>
	 * <li>firstVisibleLine : 0.</li>
	 * <li>selectedButton : 0.</li>
	 * <li>highlighted : faux.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de cette liste.
	 * @param x
	 * 		L'abscisse de cette liste.
	 * @param y
	 * 		L'ordonn�e de cette liste.
	 * @param w
	 * 		La largeur de cette liste.
	 * @param h
	 * 		La hauteur de cette liste.
	 * @param buttonsText
	 * 		La liste des textes des boutons � ajouter � cette liste.
	 * @param buttonsNames
	 * 		La liste des noms des boutons � ajouter.
	 * @param animID
	 * 		Le nom du fichier des animations des boutons de cette liste.
	 * @param nbVisibleLines
	 * 		Le nombre de lignes visibles en m�me temps sur cette liste.
	 * @param nbButtonsPerLine
	 * 		Le nombre de boutons par ligne de cette liste.
	 * @throws ObjectInstanciationException
	 * 		si l'instanciation de l'un des boutons de cette liste l�ve une exception.
	 */
	public ButtonList(String name, double x, double y, double w, double h, ArrayList<String> buttonsText,
			ArrayList<String> buttonsNames, String animID, int nbVisibleLines, int nbButtonsPerLine)
	throws ObjectInstanciationException {
		
		super(name, x, y, w, h);
		
		this.buttons = new ArrayList<Button>();
		this.nbVisibleLines = Math.max(1, nbVisibleLines);
		this.nbButtonsPerLine = Math.max(1, nbButtonsPerLine);
		this.firstVisibleLine = 0;
		this.selectedButton = 0;
		this.highlighted = false;
		
		this.changeButtons(buttonsText, buttonsNames, animID);
		
	}
	
	/**
	 * Constructeur ButtonList 2.
	 * 
	 * <p>Ce constructeur appelle le constructeur de la classe m�re.</p>
	 * <p>Apr�s initialisation, il ajoute un bouton pour toutes les ic�nes pass�es en param�tre (buttons),
	 * les animations des boutons �tant sp�cifi�es en param�tre (animID). Les ic�nes sont exprim�es en coordonn�es
	 * fractions de la taille des boutons. La liste des noms des boutons doit �tre de m�me taille que la liste des
	 * ic�nes.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>buttons : une nouvelle liste (ArrayList) de Button.</li>
	 * <li>nbVisibleLines : le nombre pass� en param�tre (nbVisibleLines), ou 1 si le param�tre est inf�rieur � 1.</li>
	 * <li>nbButtonsPerLine : le nombre pass� en param�tre (nbButtonsPerLines), ou 1 si le param�tre est inf�rieur � 1.</li>
	 * <li>firstVisibleLine : 0.</li>
	 * <li>selectedButton : 0.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de cette liste.
	 * @param x
	 * 		L'abscisse de cette liste.
	 * @param y
	 * 		L'ordonn�e de cette liste.
	 * @param w
	 * 		La largeur de cette liste.
	 * @param h
	 * 		La hauteur de cette liste.
	 * @param animID
	 * 		Le fichier des animations des boutons de cette liste.
	 * @param nbVisibleLines
	 * 		Le nombre de lignes visibles en m�me temps sur cette liste.
	 * @param nbButtonsPerLine
	 * 		Le nombre de boutons par ligne de cette liste.
	 * @param buttonsImages
	 * 		La liste des ic�nes des boutons de cette liste.
	 * @param buttonsNames
	 * 		La liste des noms des boutons � ajouter.
	 * @throws ObjectInstanciationException
	 * 		si l'instanciation de l'un des boutons de cette liste l�ve une exception.
	 */
	public ButtonList(String name, double x, double y, double w, double h, String animID,
			int nbVisibleLines, int nbButtonsPerLine, ArrayList<TexturedImage> buttonsImages,
			ArrayList<String> buttonsNames) throws ObjectInstanciationException {
		
		super(name, x, y, w, h);
		
		this.buttons = new ArrayList<Button>();
		this.nbVisibleLines = Math.max(1, nbVisibleLines);
		this.nbButtonsPerLine = Math.max(1, nbButtonsPerLine);
		this.firstVisibleLine = 0;
		this.selectedButton = 0;
		
		this.changeButtons(animID, buttonsImages, buttonsNames);
		
	}
	
	/**
	 * Fait d�filer la s�lection dans cette liste, dans le sens sp�cifi� en param�tre.
	 * 
	 * <p>Si le num�ro du bouton s�lectionn� n'est pas valide, rien n'est fait.</p>
	 * 
	 * @param or
	 * 		NORD, SUD, OUEST ou EST, en �quivalence avec la direction de d�filement souhait�e. Une autre orientation
	 * n'aura aucun effet.
	 * @return
	 * 		Vrai si le d�filement a eu lieu.
	 */
	public boolean scroll(Orientation or) {
		
		if (this.selectedButton < 0 || this.selectedButton >= this.buttons.size())
			return false;
		switch (or) {
		case SUD:
			if (this.selectedButton >= this.buttons.size()-this.nbButtonsPerLine) {
				if (this.buttons.size()%this.nbButtonsPerLine == 0)
					return false;
				if (this.selectedButton < this.buttons.size()-this.buttons.size()%this.nbButtonsPerLine) {
					this.selectedButton = this.buttons.size()-1;
					this.firstVisibleLine = this.buttons.size()/this.nbButtonsPerLine-this.nbVisibleLines
					+((this.buttons.size()%this.nbButtonsPerLine==0)?0:1);
				}
				else
					return false;
			} else {
				if (this.selectedButton/this.nbButtonsPerLine == this.firstVisibleLine+this.nbVisibleLines-1)
					this.firstVisibleLine++;
				this.selectedButton+=this.nbButtonsPerLine;
			}
			break;
		case NORD:
			if (this.selectedButton < this.nbButtonsPerLine)
				return false;
			if (this.selectedButton/this.nbButtonsPerLine == this.firstVisibleLine)
				this.firstVisibleLine--;
			this.selectedButton-=this.nbButtonsPerLine;
			break;
		case EST:
			if (this.selectedButton%this.nbButtonsPerLine == this.nbButtonsPerLine-1
					|| this.selectedButton == this.buttons.size()-1)
				return false;
			this.selectedButton++;
			break;
		case OUEST:
			if (this.selectedButton%this.nbButtonsPerLine == 0)
				return false;
			this.selectedButton--;
			break;
		default:
		}
		return true;
		
	}
	
	/**
	 * Retourne une copie de la liste des boutons de cette liste.
	 * 
	 * <p>La liste est une copie, mais les instances de Button sont les originales.</p>
	 * 
	 * @return
	 * 		Une copie de la liste des boutons.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Button> buttons() {
		return (ArrayList<Button>) this.buttons.clone();
	}
	
	/**
	 * Efface tous les boutons de cette liste.
	 */
	public void clearButtons() {
		
		this.buttons.clear();
		
	}
	
	/**
	 * Remplace les boutons de cette liste par ceux pass�s en param�tres.
	 * 
	 * @param buttonsText
	 * 		La liste des textes des boutons.
	 * @param buttonsNames
	 * 		La liste des noms des boutons.
	 * @param animID
	 * 		Le nom du fichier des animations des boutons de cette liste.
	 * @throws ObjectInstanciationException
	 * 		si l'instanciation d'un bouton l�ve une ObjectInstanciationException.
	 */
	public void changeButtons(ArrayList<String> buttonsText, ArrayList<String> buttonsNames, String animID)
	throws ObjectInstanciationException {
		
		HashMap<String, Button> bts = new HashMap<String, Button>();
		for (Button bt : this.buttons)
			bts.put(bt.getName(), bt);
		this.clearButtons();
		if (buttonsText != null) {
			int size = buttonsText.size();
			String s;
			for (int i=0;i<size;i++) {
				s = buttonsText.get(i);
				Button b = bts.get(buttonsNames.get(i));
				if (b == null) {
					if (s != null) {
						try {
							this.buttons.add(new Button(buttonsNames.get(i), 0, 0, 0, 0, animID, buttonsText.get(i)));
						} catch (Exception e) {
							throw new ObjectInstanciationException("Erreur lors de la cr�ation du bouton "+i+".",
									this.getName(), e);
						}
					}
				} else
					this.buttons.add(b);
			}
		}
		
	}
	
	/**
	 * Remplace les boutons de cette liste par ceux pass�s en param�tres.
	 * 
	 * @param animID
	 * 		Le nom du fichier des animations des boutons de cette liste.
	 * @param buttonsImages
	 * 		La liste des ic�nes des boutons de cette liste.
	 * @param buttonsNames
	 * 		La liste des noms des boutons.
	 * @throws ObjectInstanciationException
	 * 		si l'instanciation d'un bouton l�ve une ObjectInstanciationException.
	 */
	public void changeButtons(String animID, ArrayList<TexturedImage> buttonsImages, ArrayList<String> buttonsNames)
	throws ObjectInstanciationException {
		
		HashMap<String, Button> bts = new HashMap<String, Button>();
		for (Button bt : this.buttons)
			bts.put(bt.getName(), bt);
		this.clearButtons();
		if (buttonsImages != null) {
			int size = buttonsImages.size();
			TexturedImage t;
			for (int i=0;i<size;i++) {
				t = buttonsImages.get(i);
				Button b = bts.get(buttonsNames.get(i));
				if (b == null) {
					if (t != null) {
						try {
							this.buttons.add(new Button(buttonsNames.get(i), 0, 0, 0, 0, animID, buttonsImages.get(i)));
						} catch (Exception e) {
							throw new ObjectInstanciationException("Erreur lors de la cr�ation du bouton "+i+".",
									this.getName(), e);
						}
					}
				} else
					this.buttons.add(b);
			}
		}
		
	}
	
	/**
	 * Retourne le bouton activ� de cette liste.
	 * 
	 * <p>Si le num�ro du bouton s�lectionn� n'est pas valide, null est retourn�.</p>
	 * 
	 * @return
	 * 		Le bouton activ� de cette liste, ou null si aucun bouton n'est activ�.
	 */
	public Button activatedButton() {
		
		if (this.selectedButton < 0 || this.selectedButton >= this.buttons.size())
			return null;
		Button b = this.buttons.get(this.selectedButton);
		if (b.isActivated())
			return b;
		
		return null;
		
	}
	
	/**
	 * Retourne le bouton s�lectionn� de cette liste.
	 * 
	 * <p>Si le num�ro du bouton s�lectionn� n'est pas valide, null est retourn�.</p>
	 * 
	 * @return
	 * 		Le bouton s�lectionn�e de cette liste.
	 */
	public Button selectedButton() {
		
		if (this.selectedButton < 0 || this.selectedButton >= this.buttons.size())
			return null;
		return this.buttons.get(this.selectedButton);
		
	}
	
	/**
	 * Met � jour cette liste de boutons.
	 * 
	 * <p>La frame de l'animation des boutons est incr�ment�e.
	 * Si le timer de l'animation TRANSITION en cours est � z�ro, alors l'animation de ce bouton
	 * est modifi�e : HIGHLIGHTED s'il est s�lectionn�, STILL sinon.</p>
	 */
	@Override
	public void update() {
		
		if (this.selectedButton < 0)
			this.selectedButton = 0;
		if (this.selectedButton > this.buttons.size()-1)
			this.selectedButton = this.buttons.size()-1;
		Button b;
		for (int i=0;i<this.buttons.size();i++) {
			b = this.buttons.get(i);
			if (i == this.selectedButton) {
				if (!b.isHighlighted())
					b.highlight();
			} else if (b.isHighlighted())
				b.leave();
			b.update();
		}
		
	}
	
	/**
	 * Indique si ce type d'�l�ment peut �tre s�lectionn�.
	 * 
	 * @return
	 * 		Vrai.
	 */
	@Override
	public boolean isSelectable() {
		
		return true;
		
	}
	
	/**
	 * Ne fait rien.
	 */
	@Override
	public void enable() {
		
		// ne rien faire
		
	}
	
	/**
	 * Ne fait rien.
	 */
	@Override
	public void disable() {
		
		// ne rien faire
		
	}
	
	/**
	 * Retourne vrai.
	 * 
	 * @return
	 * 		Vrai.
	 */
	@Override
	public boolean isEnabled() {
		
		return true;
		
	}
	
	/**
	 * Donne le focus � cette liste.
	 */
	@Override
	public void highlight() {
		
		this.highlighted = true;
		
	}
	
	/**
	 * Enl�ve le focus � cette liste.
	 */
	@Override
	public void leave() {
		
		this.highlighted = false;
		
	}
	
	/**
	 * Indique si cette liste a le focus.
	 */
	@Override
	public boolean isHighlighted() {
		
		return this.highlighted;
		
	}
	
	/**
	 * Active le bouton s�lectionn� de cette liste.
	 * 
	 * <p>Si le num�ro du bouton s�lectionn� n'est pas valide, rien n'est fait.</p>
	 */
	@Override
	public void activate() {
		
		if (this.selectedButton < 0 || this.selectedButton >= this.buttons.size())
			return;
		this.buttons.get(this.selectedButton).activate();
		
	}
	
	/**
	 * Ne fait rien.
	 */
	@Override
	public void desactivate() {
		
		// ne rien faire
		
	}
	
	/**
	 * Indique si l'un des boutons de cette liste est activ�.
	 * 
	 * @return
	 * 		Vrai si l'un des boutons de cette liste est activ�.
	 */
	@Override
	public boolean isActivated() {
		
		if (this.activatedButton() != null)
			return true;
		return false;
		
	}
	
	/**
	 * Indique si le bouton s�lectionn� de cette liste est en cours de transition d'un �tat � un autre.
	 * 
	 * @return
	 * 		Vrai si le bouton s�lectionn� est en cours de transition.
	 */
	@Override
	public boolean isInTransition() {
		
		Button b = this.selectedButton();
		if (b != null)
			return b.isInTransition();
		return false;
		
	}

	/**
	 * Retourne une image repr�sentant cette liste.
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	@Override
	public CombinedImage generateObjectImage() {
		
		CombinedImage image = new CombinedImage(new TexturedImage("Image principale "+this.getName(), this.getX(),
				this.getY(), this.getW(), this.getH(), null));
		
		Button b;
		int size = this.buttons.size();
		int line, col;
		for (int i=0;i<size;i++) {
			line = i/this.nbButtonsPerLine;
			col = i%this.nbButtonsPerLine;
			if (line < this.firstVisibleLine || line >= this.firstVisibleLine+this.nbVisibleLines)
				continue;
			b = this.buttons.get(i);
			if (b == null)
				continue;
			CombinedImage img = b.generateImage();
			img.setX(0.1+(double) col/this.nbButtonsPerLine*0.8);
			img.setY(0.1+(double) (line-this.firstVisibleLine)/this.nbVisibleLines*0.8);
			img.setW(1./this.nbButtonsPerLine*0.8);
			img.setH(1./this.nbVisibleLines*0.8);
			image.addImage(img);
		}
		
		if (this.firstVisibleLine < this.buttons.size()/this.nbButtonsPerLine-this.nbVisibleLines
				+((this.buttons.size()%this.nbButtonsPerLine==0)?0:1))
			image.addImage(new TexturedImage("Fl�che bas", 0.45, 0.9, 0.1, 0.1, "res/textures/misc/fleche.png",
					new Rectangle(0, 0, 0.5, 0.5)));
		if (this.firstVisibleLine > 0)
			image.addImage(new TexturedImage("Fl�che haut", 0.45, 0, 0.1, 0.1, "res/textures/misc/fleche.png",
					new Rectangle(0.5, 0, 0.5, 0.5)));
		
		return image;
		
	}

}
