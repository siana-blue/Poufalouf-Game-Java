package fr.poufalouf.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.opengl.Display;

import fr.poufalouf.UpdateException;

/**
 * Classe g�rant les images repr�sentant les �l�ments du jeu
 * 
 * <p>Cette classe fait partie du design pattern MVC. Elle poss�de un contr�le li� � un mod�le qu'elle observe.</p>
 * <p>La classe View est g�n�rique et peut �tre li�e � un couple Control-Model de tout type li� au jeu. Elle poss�de une
 * cam�ra qui indique la portion du jeu qu'elle doit afficher.</p>
 * <p>Les classes Control, Model et Camera peuvent quant � elles se sp�cialiser en fonction du mode de jeu rendu (combat,
 * carte etc...)</p>
 * <p>La classe View impl�mente l'interface Observer et observe un mod�le qui la tient au courant de son �volution 
 * au moyen d'une liste d'objets Drawable.</p>
 * <p>Si le mod�le contient des informations concernant la musique de fond � jouer, cette classe g�re la musique gr�ce
 * aux variables musicFile et playingMusic. La musique doit �tre au format OGG.</p>
 * 
 * @author Anaïs Vernet
 */
public class View implements Observer {
	
	/**
	 * L'abscisse de cette vue par rapport au Display.
	 */
	private int x;
	/**
	 * L'ordonn�e de cette vue par rapport au Display.
	 */
	private int y;
	/**
	 * La largeur de cette vue.
	 */
	private int w;
	/**
	 * La hauteur de cette vue.
	 */
	private int h;
	/**
	 * Coefficient de multiplication en largeur des dimensions et des coordonn�es des images � rendre.
	 * 
	 * <p>Quand la vue est redimensionn�e, il est n�cessaire que toutes les images soient redimensionn�es de la m�me fa�on,
	 * ce qui est g�r� par ce coefficient en largeur.</p>
	 */
	private double coeffWidth;
	/**
	 * Coefficient de multiplication en hauteur des dimensions et des coordonn�es des images � rendre.
	 * 
	 * <p>Quand la vue est redimensionn�e, il est n�cessaire que toutes les images soient redimensionn�es de la m�me fa�on,
	 * ce qui est g�r� par ce coefficient en hauteur.</p>
	 */
	private double coeffHeight;
	/**
	 * Le contr�le appliqu� entre cette vue et le mod�le.
	 */
	private Control ctrl;
	/**
	 * La cam�ra indiquant la partie du jeu � afficher.
	 */
	private Camera camera;
	/**
	 * La liste des images de cette vue.
	 */
	private ArrayList<Image> images;
	/**
	 * La liste des images rendues par VBO.
	 */
	private ArrayList<TexturedImage> vboImages;
	/**
	 * Le nom du fichier de la musique de fond � jouer. Il doit s'agir d'un fichier OGG.
	 */
	private String musicFile;
	/**
	 * La liste des sons � jouer.
	 */
	private ArrayList<String> soundsToPlay;
	
	/**
	 * Constructeur View 1.
	 * 
	 * <p>Avec ce constructeur, cette vue est d�tach�e de tout mod�le et a un contr�le standard.</p>
	 * <p>La cam�ra par d�faut est de type Camera et de coordonn�es (0, 0) par rapport au jeu.</p>
	 * <p>Apr�s l'initialisation, cette vue est imm�diatement redimensionn�e et repositionn�e gr�ce aux m�thodes
	 * resizeToDisplay() et setPosition() de cette classe.</p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>x : 0.</li>
	 * <li>y : 0.</li>
	 * <li>w : le champ initialDisplayWidth de la classe Constantes.</li>
	 * <li>h : le champ initialDisplayHeight de la classe Constantes.</li>
	 * <li>coeffWidth : 1.</li>
	 * <li>coeffHeight : 1.</li>
	 * <li>ctrl : un nouveau Control standard.</li>
	 * <li>camera : une nouvelle Camera standard.</li>
	 * <li>images : une nouvelle liste (ArrayList) d'Image.</li>
	 * <li>vboImages : une nouvelle liste (ArrayList) de TexturedImage.</li>
	 * <li>musicFile : "".</li>
	 * <li>soundsToPlay : une nouvelle liste (ArrayList) de String.</li>
	 * </ul></p>
	 */
	public View() {
		
		this.x = 0;
		this.y = 0;
		this.w = Constantes.initialDisplayWidth;
		this.h = Constantes.initialDisplayHeight;
		this.coeffWidth = 1;
		this.coeffHeight = 1;
		this.ctrl = new Control(null) {
			
			@Override
			public void check() {
				// Ne rien faire
			}

			@Override
			public void pollKeyboardDown(int keyID) {
				// Ne rien faire
			}

			@Override
			public void pollKeyboardUp(int keyID) {
				// Ne rien faire
			}
			
		};
		this.camera = new Camera(0, 0);
		this.images = new ArrayList<Image>();
		this.vboImages = new ArrayList<TexturedImage>();
		this.musicFile = "";
		this.soundsToPlay = new ArrayList<String>();
		
		setPosition();
		resizeToDisplay();
		
	}
	
	/**
	 * Constructeur View 2.
	 * 
	 * <p>Ce constructeur appelle le constructeur 1 et attribue un contr�le � cette vue.</p>
	 * 
	 * @param ctrl
	 * 		Le contr�le � utiliser par cette vue. S'il est null, le Control standard est conserv�.
	 */
	public View(Control ctrl) {
		
		this();
		if (ctrl != null)
			this.ctrl = ctrl;
		
	}
	
	/**
	 * Constructeur View 3.
	 * 
	 * <p>Ce constructeur appelle le constructeur 2 et attribue une cam�ra � cette vue.</p>
	 * 
	 * @param ctrl
	 * 		Le contr�le � utiliser par cette vue.
	 * @param camera
	 * 		La cam�ra � utiliser par cette vue. Si elle est null, la Camera standard est conserv�e.
	 */
	public View(Control ctrl, Camera camera) {
		
		this(ctrl);
		if (camera != null)
			this.camera = camera;
		
	}
	
	/**
	 * Rafra�chit cette vue.
	 * 
	 * <p>Se renseigne sur l'�tat du jeu gr�ce � la m�thode refresh() de la classe Control.
	 * Cette vue se repositionne et se redimensionne auparavant selon le Display gr�ce aux m�thodes resizeToDisplay() et
	 * setPosition() de cette classe.</p>
	 * 
	 * @throws IOException
	 * 		si la m�thode refresh() de la classe Control l�ve une IOException.
	 * @throws UpdateException
	 * 		si la m�thode refresh() de la classe Control l�ve une UpdateException.
	 */
	public void refresh() throws IOException, UpdateException {
		
		resizeToDisplay();
		setPosition();
		this.ctrl.refresh();
		
	}
	
	/**
	 * Positionne cette vue au centre du Display.
	 */
	public void setPosition() {
		
		this.x = Display.getDisplayMode().getWidth()/2-this.getW()/2;
		this.y = Display.getDisplayMode().getHeight()/2-this.getH()/2;
		
	}
	
	/**
	 * Etire cette vue de fa�on � ce qu'elle remplisse au maximum la dimension la plus petite du Display.
	 * 
	 * <p>Les coefficients de redimensionnement sont multipli�s par les rapports des nouvelles dimensions sur les
	 * anciennes.</p>
	 */
	public void resizeToDisplay() {
		
		// Stocke les dimensions initiales de la vue
		
		double w2 = Math.max(this.getW(), 1), h2 = Math.max(this.getH(), 1);
		
		// Etire la vue de fa�on � remplir la dimension la plus petite du Display
		
		this.w = Math.min(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		this.h = this.getW();
		
		// Calcule les nouveaux coefficients � appliquer aux images de la vue
		
		this.coeffWidth *= (this.getW()/w2);
		this.coeffHeight *= (this.getH()/h2);
		
	}
	
	/**
	 * Re�oit les informations relatives au contr�le clavier (touche enfonc�e) et les transmet au contr�le de cette vue.
	 * 
	 * @param keyID
	 * 		L'information clavier � transmettre.
	 */
	public void pollKeyboardDown(int keyID) {
		
		this.ctrl.pollKeyboardDown(keyID);
		
	}
	
	/**
	 * Re�oit les informations relatives au contr�le clavier (touche rel�ch�e) et les transmet au contr�le de cette classe.
	 * 
	 * @param keyID
	 * 		L'information clavier � transmettre.
	 */
	public void pollKeyboardUp(int keyID) {
		
		this.ctrl.pollKeyboardUp(keyID);
		
	}
	
	/**
	 * Met � jour cette vue suivant les objets de l'Observable pass� en param�tre.
	 * 
	 * <p>Cette vue se met � jour gr�ce � une liste d'objets Drawable. Ce tableau est obtenu � partir de
	 * l'Observable. S'il s'agit d'un mod�le (la plupart du temps), la m�thode objets() de la classe Model est
	 * appel�e apr�s avoir cast� l'Observable en Model. S'il s'agit d'un LoadingScreen, cet Observable est directement
	 * ajout� � la liste en tant que Drawable. Si l'Observable n'est d'aucune de ces classes, la mise � jour se
	 * traduit par un �cran noir.</p>
	 * <p>Apr�s avoir effac� toutes les images de cette vue par l'appel � la m�thode clearImages() de cette classe,
	 * cette m�thode repositionne la cam�ra de cette vue suite au nouvel �tat du jeu gr�ce � la m�thode
	 * update(ArrayList[Drawable]) de la classe Camera.</p>
	 * <p>Les images des Drawable de l'Observable pass� en param�tre sont g�n�r�es gr�ce � la m�thode generateImage()
	 * de l'interface Drawable, avant d'�tre repositionn�es par rapport au Display. Les images g�n�r�es par generateImage()
	 * sont en effet positionn�es par rapport au jeu. Les coefficients coeffWidth et coeffHeight de cette classe sont
	 * �galement utilis�s. La formule de d�placement et de redimensionnement des images est la suivante :</p>
	 * <p><code>nouveauXimage = xVue + (xImage-xCamera)*coeffLargeur</code></p>
	 * <p><code>nouveauYimage = yVue + (yImage-yCamera)*coeffHauteur</code></p>
	 * <p><code>nouveauWimage = wImage*coeffLargeur</code></p>
	 * <p><code>nouveauHimage = hImage*coeffHauteur</code></p>
	 * <p>Enfin, les images obtenues sont ajout�es � la liste des images de cette vue.</p>
	 * <p>La liste des images de cette vue est tri�e selon le plan de perspective des images, de fa�on � ce que les images
	 * ayant le plan le plus profond (le plus petit num�riquement) soient dessin�es en premier et �ventuellement
	 * recouvertes par les suivantes.</p>
	 * <p>Enfin, la musique est mise � jour si l'Observable est un Model. Si la cha�ne de caract�re retourn�e par la
	 * m�thode getMusique de la classe Model est diff�rente de la cha�ne musicFile de cette classe, alors celle-ci est
	 * mise � jour. Les sons retourn�s par la m�thode sounds de la classe Model sont ajout�s � la liste soundsToPlay
	 * de cette classe.</p>
	 * 
	 * @param obs
	 * 		L'Observable permettant de mettre la vue � jour.
	 * @throws IOException
	 * 		si la m�thode addTexture(String) de cette classe l�ve une IOException.
	 */
	@Override
	public void update(Observable obs) throws IOException {
		
		ArrayList<Drawable> drws = new ArrayList<Drawable>();
		if (obs instanceof Model)
			drws = ((Model) obs).drawables();
		else if (obs instanceof LoadingScreen)
			drws.add((LoadingScreen) obs);
		
		// Efface les images de cette vue
		
		clearImages();
		
		// Repositionne la cam�ra
		
		this.camera.update(drws);
		
		// G�n�re les images
		
		for (Drawable drw : drws) {
			if (drw == null)
				continue;
			
			Image image = drw.generateImage();
			
			if (image != null) {
				/* 
				 * Les images g�n�r�es par generateImage sont positionn�es par rapport au jeu.
				 * Ces lignes repositionnent les images par rapport au Display et les multiplient par les coefficients 
				 * de la vue.
				 */
				image.setX(this.getX()+(image.getX()-this.camera.getX())*this.coeffWidth);
				image.setY(this.getY()+(image.getY()-this.camera.getY())*this.coeffHeight);
				image.setW(image.getW()*this.coeffWidth);
				image.setH(image.getH()*this.coeffHeight);
				
				this.addImage(image, drw.isVBORendered());
			}
		}
		
		// Les images les plus au fond seront dessin�es les premi�res, pour cr�er une perspective.
		sortImages();
		
		// Gestion des sons
		
		if (obs instanceof Model) {
			this.musicFile = ((Model) obs).getMusique();
			ArrayList<String> snds = ((Model) obs).sounds();
			for (String s : snds) {
				this.soundsToPlay.add(s);
			}
		}
		
	}
	
	/**
	 * Ajoute une image � la liste des images de cette vue, seulement si elle est visible dans la vue.
	 * 
	 * <p>Si cette image est ajout�e � la liste des images VBO, alors elle sera en arri�re-plan, et aura la texture de
	 * la derni�re image ajout�e � la liste VBO.</li>
	 * 
	 * @param image
	 * 		L'image � ajouter.
	 * @param vbo
	 * 		Vrai si cette image doit �tre ajotu�e � la liste des images VBO (ce doit �tre une image textur�e).
	 */
	private void addImage(Image image, boolean vbo) {
		
		if (image != null) {
			if (image.getX()+image.getW() <= this.getX() || image.getY()+image.getH() <= this.getY()
					|| image.getX() >= this.getX()+this.getW() || image.getY() >= this.getY()+this.getH())
				return;
			if (vbo && image instanceof TexturedImage) {
				if (image instanceof CombinedImage) {
					this.vboImages.add(((CombinedImage) image).mainImage());
					for (TexturedImage img : ((CombinedImage) image).images()) {
						TexturedImage t = img.clone();
						t.setX(image.getX()+t.getX()*image.getW());
						t.setY(image.getY()+t.getY()*image.getH());
						t.setW(t.getW()*image.getW());
						t.setH(t.getH()*image.getH());
						this.vboImages.add(t);
					}
				} else
					this.vboImages.add((TexturedImage) image);
			}
			else {
				this.images.add(image);
			}
		}
		
	}
	
	/**
	 * Efface la liste des images de cette vue (standard et VBO).
	 */
	private void clearImages() {
		
		this.images = new ArrayList<Image>();
		this.vboImages = new ArrayList<TexturedImage>();
		
	}
	
	/**
	 * Trie les images de la liste des images � afficher par ordre croissant selon leurs plans de perspective.
	 * 
	 * <p>Les images combin�es sont retir�es de la liste, et remplac�es par une s�rie d'images textur�es ins�r�es dans la
	 * liste dans l'ordre d�fini par l'image principale. Si une sous-image de l'image combin�e a un plan de perspective
	 * �gal au champ maxPlan de la classe Constantes, alors elle est plac�e � la fin de la liste des images de cette
	 * vue.</p>
	 */
	private void sortImages() {
		
		Collections.sort(this.images);
		
		int index;
		TexturedImage tx, img;
		ArrayList<TexturedImage> txts;
		@SuppressWarnings("unchecked")
		ArrayList<Image> imgs = (ArrayList<Image>) this.images.clone();
		for (Image image : imgs) {
			if (image instanceof CombinedImage) {
				index = this.images.indexOf(image);
				this.images.remove(index);
				txts = ((CombinedImage) image).images();
				for (int i=0;i<txts.size();i++) {
					img = txts.get(i);
					TexturedImage t = img.clone();
					t.setX(image.getX()+t.getX()*image.getW());
					t.setY(image.getY()+t.getY()*image.getH());
					t.setW(t.getW()*image.getW());
					t.setH(t.getH()*image.getH());
					if (t.getPlan() < Constantes.maxPlan)
						this.images.add(index+i, t);
					else
						this.images.add(t);
				}
				tx = ((CombinedImage) image).mainImage();
				if (tx.getTextureID() != "")
					this.images.add(index, tx);
			}
		}
		
	}
	
	/**
	 * Retourne une copie de la liste des images de cette vue.
	 * 
	 * <p>La liste retourn�e est une copie mais les instances d'Image sont les originales.</p>
	 * 
	 * @return
	 * 		Une copie de la liste des images de cette vue.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Image> images() {
		
		return (ArrayList<Image>) this.images.clone();
		
	}
	
	/**
	 * Retourne une copie de la liste des images VBO de cette vue.
	 * 
	 * <p>La liste retourn�e est une copie mais les instances d'Image sont les originales.</p>
	 * 
	 * @return
	 * 		Une copie de la liste des images VBO de cette vue.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TexturedImage> vboImages() {
		
		return (ArrayList<TexturedImage>) this.vboImages.clone();
		
	}

	/**
	 * Retourne l'abscisse de cette vue par rapport au Display.
	 * 
	 * @return
	 * 		L'abscisse de cette vue.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Retourne l'ordonn�e de cette vue par rapport au Display.
	 * 
	 * @return
	 * 		L'ordonn�e de cette vue.
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Retourne la largeur de cette vue.
	 * 
	 * @return
	 * 		La largeur de cette vue.
	 */
	public int getW() {
		return this.w;
	}

	/**
	 * Retourne la hauteur de cette vue.
	 * 
	 * @return
	 * 		La hauteur de cette vue.
	 */
	public int getH() {
		return this.h;
	}
	
	/**
	 * Retourne le coefficient de largeur de cette vue.
	 * 
	 * @return
	 * 		Le coefficient de largeur.
	 */
	public double getCoeffWidth() {
		return this.coeffWidth;
	}
	
	/**
	 * Retourne le coefficient de hauteur de cette vue.
	 * 
	 * @return
	 * 		Le coefficient de hauteur.
	 */
	public double getCoeffHeight() {
		return this.coeffHeight;
	}
	
	/**
	 * Retourne le nom du fichier de la musique de fond � jouer.</p>
	 * 
	 * @return
	 * 		Le nom du fichier de la musique de fond.
	 */
	public String getMusicFile() {
		
		return this.musicFile;
		
	}
	
	/**
	 * Retourne une copie de la liste des sons � jouer.
	 * 
	 * <p>La liste soundsToPlay de cette classe est alors effac�e.</p>
	 * 
	 * @return
	 * 		Une copie de la liste des sons � jouer.
	 */
	public ArrayList<String> soundsToPlay() {
		
		@SuppressWarnings("unchecked")
		ArrayList<String> snds = (ArrayList<String>) this.soundsToPlay.clone();
		this.soundsToPlay.clear();
		
		return snds;
		
	}

}
