package fr.poufalouf.tools;

import java.util.ArrayList;

/**
 * Un �cran int�gr� au jeu pouvant afficher diff�rentes informations
 * 
 * <p>Un �cran poss�de une liste 2D de GUIElement repr�sentant les �l�ments affichables. Les deux dimensions de la liste
 * repr�sentent les lignes et les colonnes de s�lection des GUIElement. Cela n'influence pas leurs positions, mais est
 * utilis� pour d�finir quel bouton est au-dessus d'un autre lors d'une s�lection par exemple.</p>
 * 
 * @author Anaïs Vernet
 */
public class Screen implements Drawable {

	/**
	 * La marge horizontale laiss�e pour les drawables plac�s automatiquement selon leur orientation, en pourcentage.
	 */
	private static final double marginW = 0.08;
	/**
	 * La marge verticale laiss�e pour les drawables plac�s automatiquement selon leur orientation, en pourcentage.
	 */
	private static final double marginH = 0.13;
	/**
	 * Le nom de cet �cran.
	 */
	private String name;
	/**
	 * Le nom du fichier texture de l'image principale de cet �cran.
	 */
	private String textureID;
	/**
	 * L'abscisse de cet �cran.
	 */
	private double x;
	/**
	 * L'ordonn�e de cet �cran.
	 */
	private double y;
	/**
	 * La largeur de cet �cran.
	 */
	private double w;
	/**
	 * La hauteur de cet �cran.
	 */
	private double h;
	/**
	 * La liste 2D (ligne et colonne de s�lection) des objets � afficher sur cet �cran.
	 */
	private ArrayList<ArrayList<GUIElement>> elements;
	/**
	 * La liste 2D (ligne et colonne de s�lection) des orientations des �l�ments de cet �cran.
	 */
	private ArrayList<ArrayList<Orientation>> elementsOrientation;
	/**
	 * L'indice de la ligne de l'objet actuellement s�lectionn�.
	 */
	private int selectedRow;
	/**
	 * L'indice de la colonne de l'objet actuellement s�lectionn�.
	 */
	private int selectedCol;
	
	/**
	 * Constructeur Screen.
	 * 
	 * <p></p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>name : le nom pass� en param�tre (name), ou "" si le param�tre est null.</li>
	 * <li>textureID : le nom pass� en param�tre (textureID), ou "" si le param�tre est null.</li>
	 * <li>x : l'abscisse du rectangle pass� en param�tre (rect).</li>
	 * <li>y : l'ordonn�e du rectangle pass� en param�tre (rect).</li>
	 * <li>w : la largeur du rectangle pass� en param�tre (rect).</li>
	 * <li>h : la hauteur du rectangle pass� en param�tre (rect).</li>
	 * <li>elements : une nouvelle liste 2D (ArrayList d'ArrayList) de GUIElement.</li>
	 * <li>elementsOrientation : une nouvelle liste 2D (ArrayList d'ArrayList) d'Orientation.</li>
	 * <li>selectedRow : 0.</li>
	 * <li>selectedCol : 0.</li>
	 * </ul></p>
	 * 
	 * @param name
	 * 		Le nom de cet �cran.
	 * @param textureID
	 * 		Le nom du fichier texture de l'image principale de cet �cran.
	 * @param rect
	 * 		Le rectangle des coordonn�es de cet �cran.
	 */
	public Screen(String name, String textureID, Rectangle rect) {
		
		if (name != null)
			this.name = name;
		else
			this.name = "";
		if (textureID != null)
			this.textureID = textureID;
		else
			this.textureID = "";
		if (rect != null) {
			this.x = (int) rect.getX();
			this.y = (int) rect.getY();
			this.w = (int) rect.getW();
			this.h = (int) rect.getH();
		} else {
			this.x = this.y = this.w = this.h = 0;
		}
		this.elements = new ArrayList<ArrayList<GUIElement>>();
		this.elementsOrientation = new ArrayList<ArrayList<Orientation>>();
		this.selectedRow = 0;
		this.selectedCol = 0;
		
	}
	
	/**
	 * Met � jour tous les objets n�cessaires sur cet �cran.
	 * 
	 * <p>Cette m�thode appelle la m�thode update() des objets de type Button et ButtonList.</p>
	 */
	public void update() {
		
		GUIElement e = this.selectedElement();
		boolean needToSelectSomething = false;
		for (ArrayList<GUIElement> l : this.elements) {
			if (l == null)
				continue;
			for (GUIElement d : l) {
				if (d == null)
					continue;
				if (d instanceof Button) {
					if (d == e) {
						if (!d.isHighlighted()) {
							if (d.isEnabled())
								d.highlight();
							else
								needToSelectSomething = true;
								
						}
					} else if (d.isHighlighted())
						d.leave();
				}
				d.update();
			}
		}
		if (needToSelectSomething) {
			ArrayList<GUIElement> l;
			GUIElement d;
			boolean cnt = true;
			for (int i=0;i<this.elements.size();i++) {
				l = this.elements.get(i);
				if (l == null)
					continue;
				for (int j=0;j<l.size();j++) {
					d = l.get(j);
					if (d == null)
						continue;
					if (d.isSelectable() && d.isEnabled()) {
						d.highlight();
						this.selectedRow = i;
						this.selectedCol = j;
						cnt = false;
						break;
					}
				}
				if (!cnt)
					break;
			}
		}
		
	}
	
	/**
	 * Retourne l'�l�ment actuellement s�lectionn�.
	 * 
	 * @return
	 * 		L'�l�ment s�lectionn�.
	 */
	public GUIElement selectedElement() {
		
		if (this.selectedRow < 0 || this.selectedRow >= this.elements.size())
			return null;
		if (this.selectedCol < 0 || this.selectedCol >= this.elements.get(this.selectedRow).size())
			return null;
		return this.elements.get(this.selectedRow).get(this.selectedCol);
		
	}
	
	/**
	 * Modifie la s�lection actuelle en fonction de l'orientation pass�e en param�tre.
	 * 
	 * <p>Si l'objet s�lectionn� est une liste, alors la s�lection n'est pas modifi�e, c'est la liste qui d�file.</p>
	 * 
	 * @param or
	 * 		L'orientation utilis�e pour la nouvelle s�lection.
	 * @return
	 * 		Vrai si la s�lection a effectivement chang�.
	 */
	public boolean changeSelection(Orientation or) {
		
		// D�filement de la liste si c'est une liste qui est s�lectionn�e
		GUIElement gui = this.selectedElement();
		if (gui instanceof ButtonList) {
			return ((ButtonList) gui).scroll(or);
		}
		
		// Autres s�lections
		if (or == null)
			return false;
		ArrayList<GUIElement> l;
		GUIElement e;
		switch (or) {
		case NORD:
			if (this.selectedRow > 0 && this.selectedRow < this.elements.size()) {
				for (int j=this.selectedRow-1;j>=0;j--) {
					l = this.elements.get(j);
					if (l == null)
						continue;
					if (l.size() > this.selectedCol) {
						e = l.get(this.selectedCol);
						if (e != null) {
							if (e.isSelectable()) {
								this.selectedRow = j;
								return true;
							}
						}
					}
					for (int i=l.size()-1;i>=0;i--) {
						e = l.get(i);
						if (e == null)
							continue;
						if (e.isSelectable() && e.isEnabled()) {
							this.selectedCol = i;
							this.selectedRow = j;
							return true;
						}
					}
				}
			}
			break;
		case SUD:
			if (this.selectedRow < this.elements.size()-1 && this.selectedRow >= 0) {
				for (int j=this.selectedRow+1;j<this.elements.size();j++) {
					l = this.elements.get(j);
					if (l == null)
						continue;
					if (l.size() > this.selectedCol) {
						e = l.get(this.selectedCol);
						if (e != null) {
							if (e.isSelectable()) {
								this.selectedRow = j;
								return true;
							}
						}
					}
					for (int i=l.size()-1;i>=0;i--) {
						e = l.get(i);
						if (e == null)
							continue;
						if (e.isSelectable() && e.isEnabled()) {
							this.selectedCol = i;
							this.selectedRow = j;
							return true;
						}
					}
				}
			}
			break;
		case OUEST:
			if (this.selectedRow < 0 || this.selectedRow >= this.elements.size())
				return false;
			l = this.elements.get(this.selectedRow);
			if (l == null)
				return false;
			if (this.selectedCol > 0 && this.selectedCol < l.size()) {
				for (int i=this.selectedCol-1;i>=0;i--) {
					e = l.get(i);
					if (e == null)
						continue;
					if (e.isSelectable() && e.isEnabled()) {
						this.selectedCol = i;
						return true;
					}
				}
			}
			break;
		case EST:
			if (this.selectedRow < 0 || this.selectedRow >= this.elements.size())
				return false;
			l = this.elements.get(this.selectedRow);
			if (l == null)
				return false;
			if (this.selectedCol < l.size()-1 && this.selectedCol >= 0) {
				for (int i=this.selectedCol+1;i<l.size();i++) {
					e = l.get(i);
					if (e == null)
						continue;
					if (e.isSelectable() && e.isEnabled()) {
						this.selectedCol = i;
						return true;
					}
				}
			}
			break;
		default:
		}
		
		return false;
		
	}
	
	/**
	 * S�lectionne l'�l�ment aux indices pass�s en param�tre, si possible.
	 * @param row
	 * 		La ligne de l'�l�ment.
	 * @param col
	 * 		La colonne de l'�l�ment.
	 * @return
	 * 		Vrai si la s�lection est possible et a eu lieu.
	 */
	public boolean select(int row, int col) {
		
		if (row < 0 || row > this.elements.size())
			return false;
		if (col < 0 || col > this.elements.get(row).size())
			return false;
		this.selectedRow = row;
		this.selectedCol = col;
		return true;
		
	}
	
	/**
	 * Active l'�l�ment s�lectionn� de cet �cran.
	 */
	public void activateSelection() {
		
		GUIElement gui = this.selectedElement();
		if (gui != null)
			gui.activate();
		
	}
	
	/**
	 * Rend actifs tous les �l�ments de cet �cran.
	 */
	public void enableAll() {
		
		for (ArrayList<GUIElement> l : this.elements) {
			if (l == null)
				continue;
			for (GUIElement e : l) {
				if (e == null)
					continue;
				e.enable();
			}
		}
		
	}
	
	/**
	 * Rend inactifs tous les �l�ments de cet �cran.
	 */
	public void disableAll() {
		
		for (ArrayList<GUIElement> l : this.elements) {
			if (l == null)
				continue;
			for (GUIElement e : l) {
				if (e == null)
					continue;
				e.disable();
			}
		}
		
	}
	
	/**
	 * Retourne une image montrant l'�tat de cet �cran.
	 * 
	 * <p>L'image g�n�r�e est une image combin�e compos�e de l'image principale de cet �cran ainsi que les images
	 * g�n�r�es par les GUIElement de la liste de cette classe, dans l'ordre de la liste. L'image g�n�r�e est plac�e
	 * au premier plan.</p>
	 * <p>Pour chaque �l�ment, l'orientation du m�me indice dans la liste des orientations est regard�e. Si elle est
	 * valide, elle est utilis�e pour positionner l'�l�ment sur l'�cran, sans tenir compte de ses coordonn�es. Les
	 * orientations valides sont les huit orientations cardinales, ainsi que CENTRE.</p>
	 * 
	 * @return
	 * 		L'image g�n�r�e.
	 */
	@Override
	public CombinedImage generateImage() {
		
		CombinedImage cbImg = new CombinedImage(new TexturedImage("Image "+this.getName(),
				this.getX(), this.getY(), this.getW(), this.getH(), this.textureID));
		Drawable d;
		ArrayList<GUIElement> l;
		ArrayList<Orientation> ors;
		for (int j=0;j<this.elements.size();j++) {
			l = this.elements.get(j);
			ors = this.elementsOrientation.get(j);
			if (l == null || ors == null)
				continue;
			for (int i=0;i<l.size();i++) {
				d = l.get(i);
				if (d == null)
					continue;
				Image image = d.generateImage();
				if (!(image instanceof TexturedImage))
					continue;
				switch(ors.get(i)) {
				case NORD:
					image.setX(0.5-image.getW()/2);
					image.setY(Screen.marginH);
					break;
				case NORD_EST:
					image.setX(1-image.getW()-Screen.marginW);
					image.setY(Screen.marginH);
					break;
				case EST:
					image.setX(1-image.getW()-Screen.marginW);
					image.setY(0.5-image.getH()/2);
					break;
				case SUD_EST:
					image.setX(1-image.getW()-Screen.marginW);
					image.setY(1-image.getH()-Screen.marginH);
					break;
				case SUD:
					image.setX(0.5-image.getW()/2);
					image.setY(1-image.getH()-Screen.marginH);
					break;
				case SUD_OUEST:
					image.setX(Screen.marginW);
					image.setY(1-image.getH()-Screen.marginH);
					break;
				case OUEST:
					image.setX(Screen.marginW);
					image.setY(0.5-image.getH()/2);
					break;
				case NORD_OUEST:
					image.setX(Screen.marginW);
					image.setY(Screen.marginH);
					break;
				case CENTRE:
					image.setX(0.5-image.getW()/2);
					image.setY(0.5-image.getH()/2);
					break;
				default:
				}
				cbImg.addImage((TexturedImage) image);
			}
		}
		cbImg.setPlan(Constantes.maxPlan-1);
		
		return cbImg;
		
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
	 * Retourne le nom de cet �cran.
	 * 
	 * @return
	 * 		Le nom de cet �cran.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Modifie l'abscisse de cet �cran.
	 * 
	 * @param x
	 * 		L'abscisse de cet �cran.
	 */
	@Override
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Retourne l'abscisse de cet �cran.
	 * 
	 * @return
	 * 		L'abscisse de cet �cran.
	 */
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * Modifie l'ordonn�e de cet �cran.
	 * 
	 * @param y
	 * 		L'ordonn�e de cet �cran.
	 */
	@Override
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Retourne l'ordonn�e de cet �cran.
	 * 
	 * @return
	 * 		L'ordonn�e de cet �cran.
	 */
	@Override
	public double getY() {
		return this.y;
	}

	/**
	 * Modifie la largeur de cet �cran.
	 * 
	 * @param w
	 * 		La largeur de cet �cran.
	 */
	@Override
	public void setW(double w) {
		this.w = w;
	}

	/**
	 * Retourne la largeur de cet �cran.
	 * 
	 * @return
	 * 		La largeur de cet �cran.
	 */
	@Override
	public double getW() {
		return this.w;
	}

	/**
	 * Modifie la hauteur de cet �cran.
	 * 
	 * @param h
	 * 		La hauteur de cet �cran.
	 */
	@Override
	public void setH(double h) {
		this.h = h;
	}

	/**
	 * Retourne la hauteur de cet �cran.
	 * 
	 * @return
	 * 		La hauteur de cet �cran.
	 */
	@Override
	public double getH() {
		return this.h;
	}
	
	/**
	 * Ajoute un objet GUIElement � la liste des objets de cet �cran.
	 * 
	 * <p>Les coordonn�es de ce GUIElement doivent �tre sp�cifi�es en pourcentages des coordonn�es de cet �cran.</p>
	 * <p>Si une orientation non null est pass�e en param�tre, elle aura un effet lors de la g�n�ration de l'image, qui
	 * rend inutilis�es les coordonn�es du GUIElement.</p>
	 * <p>La ligne de s�lection doit �tre sp�cifi�e, et le GUIElement est alors ajout� � la suite des autres GUIElement sur
	 * cette ligne. Si la ligne pass�e en param�tre est sup�rieure au nombre de lignes d�j� pr�sentes, alors une nouvelle
	 * ligne est ajout�e.</p>
	 * 
	 * @param d
	 * 		Le GUIElement � ajouter. S'il est null, il n'est pas ajout�.
	 * @param or
	 * 		L'orientation de l'�l�ment.
	 * @param line
	 * 		La ligne de s�lection � laquelle doit �tre ajout� ce GUIElement. Si la ligne est inf�rieure ou �gale � z�ro,
	 * alors rien n'est fait.
	 */
	public void addDrawable(GUIElement d, Orientation or, int line) {
		
		if (d != null && line >= 0) {
			int i;
			if (line >= this.elements.size()) {
				this.elements.add(new ArrayList<GUIElement>());
				this.elementsOrientation.add(new ArrayList<Orientation>());
				i = this.elements.size()-1;
			} else
				i = line;
			this.elements.get(i).add(d);
			this.elementsOrientation.get(i).add((or==null)?Orientation.AUCUNE:or);
		}
		
	}
	
	/**
	 * Efface tous les objets Text de cet �cran.
	 */
	@SuppressWarnings("unchecked")
	public void clearText() {
		
		ArrayList<GUIElement> l;
		GUIElement e;
		for (int i=0;i<this.elements.size();i++) {
			l = (ArrayList<GUIElement>) this.elements.get(i).clone();
			if (l == null)
				continue;
			for (int j=0;j<l.size();j++) {
				e = l.get(j);
				if (e instanceof Text) {
					this.elements.get(i).remove(j);
					this.elementsOrientation.get(i).remove(j);
					l = this.elements.get(i);
				}
			}
		}
		
	}

}
