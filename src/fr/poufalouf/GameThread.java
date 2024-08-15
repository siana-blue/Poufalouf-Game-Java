package fr.poufalouf;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.PNGImageData;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import fr.poufalouf.tools.CombinedImage;
import fr.poufalouf.tools.Constantes;
import fr.poufalouf.tools.Image;
import fr.poufalouf.tools.TexturedImage;
import fr.poufalouf.tools.View;

/**
 * Impl�mentation du thread de jeu
 * 
 * <p>Cette classe g�re les contr�les utilisateur ainsi que l'affichage � l'�cran des objets du jeu. 
 * Il s'agit d'une impl�mentation de Runnable qui est utilis�e pour g�n�rer le thread de jeu.</p>
 * <p>Trois bool�ens sont initialis�s lors de la d�claration des variables :
 * <ul>
 * <li>Le bool�en playing � l'�tat vrai : d�s qu'il passe � l'�tat faux, le programme se ferme.</li>
 * <li>Le bool�en ready � l'�tat faux : quand le thread de jeu est cr�� dans la m�thode principale du programme, le thread
 * initial s'endort le temps que le Display soit cr�� et pr�t � �tre utilis�. Quand le bool�en ready passe � l'�tat vrai,
 * le thread initial se r�veille car le Display est cr��.</li>
 * <li>Le bool�en fullscreen � l'�tat faux : ce bool�en indique simplement si le mode plein �cran est activ�. Par d�faut,
 * il ne l'est pas.</li>
 * </ul></p>
 * <p>La m�thode principale de cette classe est la m�thode run(), qui sert de boucle infinie pour le jeu.</p>
 * <p>Cette classe poss�de une variable qui peut �tre null : musique. Cette variable doit �tre instanci�e une premi�re fois
 * gr�ce � AudioLoader. A partir de ce moment, si aucune exception ne survient, cette variable ne pourra plus �tre null.</p>
 * 
 * @author Anaïs Vernet
 */
public final class GameThread implements Runnable {
	
	/**
	 * � l'�tat faux, le programme quitte la boucle de jeu dans le thread principal et dans le thread de jeu.
	 */
	private boolean playing;
	/**
	 * Indique si le Display a �t� cr��, et permet ainsi au thread principal de continuer.
	 */
	private boolean ready;
	/**
	 * Indique si le mode plein �cran est activ�.
	 */
	private boolean fullscreen;
	/**
	 * La vue contenant les images � afficher.
	 */
	private View vue;
	/**
	 * La musique de fond du jeu. Cette variable peut �tre null.
	 */
	private Audio musique;
	/**
	 * Le nom de la musique de fond.
	 */
	private String musicName;
	/**
	 * Les effets sonores du jeu.
	 */
	private HashMap<String, Audio> sounds;
	/**
	 * La liste des textures disponibles.
	 */
	private HashMap<String, Texture> textures;
	/**
	 * La liste des textures � charger.
	 */
	private ArrayList<String> texturesToAdd;
	/**
	 * La liste des sons � charger.
	 */
	private ArrayList<String> soundsToAdd;
	/**
	 * L'adresse du tampon VBO.
	 */
	private int vboID;
	/**
	 * La taille du tampon VBO.
	 */
	private int vboBufferSize;
	
	/**
	 * Constructeur GameThread.
	 * 
	 * <p></p>
	 * 
	 * <b>Initialisation</b>
	 * 
	 * <p>
	 * <ul>
	 * <li>playing : vrai.</li>
	 * <li>ready : faux.</li>
	 * <li>fullscreen : faux.</li>
	 * <li>vue : la vue pass�e en param�tre (vue), ou une nouvelle View standard si le param�tre est null.</li>
	 * <li>musique : null.</li>
	 * <li>musicName : "".</li>
	 * <li>sounds : une nouvelle table (HashMap) d'Audio rep�r�s par des String.</li>
	 * <li>textures : une nouvelle table (HashMap) de Texture r�f�renc�es par des String.</li>
	 * <li>texturesToAdd : une nouvelle liste (ArrayList) de String.</li>
	 * <li>soundsToAdd : une nouvelle liste (ArrayList) de String.</li>
	 * <li>vboID : 0.</li>
	 * <li>vboBufferSize : 0.</li>
	 * </ul></p>
	 * 
	 * @param vue
	 * 		La vue � associer � ce thread de jeu.
	 */
	public GameThread(View vue) {
		
		this.playing = true;
		this.ready = false;
		this.fullscreen = false;
		if (vue != null)
			this.vue = vue;
		else
			this.vue = new View();
		this.musique = null;
		this.musicName = "";
		this.sounds = new HashMap<String, Audio>();
		this.textures = new HashMap<String, Texture>();
		this.texturesToAdd = new ArrayList<String>();
		this.soundsToAdd = new ArrayList<String>();
		this.vboID = 0;
		this.vboBufferSize = 0;
		
	}
	
	/**
	 * Affiche les images de la vue de ce thread sur le Display.
	 * 
	 * <p>Si le buffering VBO n'est pas support�, alors des boucles glBegin et glEnd sont utilis�es pour le rendu. Sinon,
	 * les buffers VBO sont utilis�s, qui se voient r�allouer de la m�moire si la taille de la liste images de la vue
	 * associ�e � ce thread est modifi�e. Seules les images issues de Drawable sp�cifiant qu'ils sont rendus par VBO
	 * utiliseront ce syst�me. Pour les autres, les boucles glBegin et glEnd sont utilis�es dans tous les cas.</p>
	 * 
	 * <p>Les images sont affich�es diff�remment selon leurs types :
	 * <ul>
	 * <li>Une image combin�e (CombinedImage) affiche plusieurs images dans l'ordre de la liste des images qui la
	 * composent. Les image combin�es ne sont en th�orie jamais envoy�es � cette m�thode, mais divis�es en
	 * images textur�es par la vue.</li>
	 * <li>Une image textur�e (TexturedImage) est affich�e dans la portion d'�cran d�finie par ses dimensions, et la zone
	 * ainsi d�finie est remplie par la texture de l'image.</li>
	 * <li>Une image simple est dessin�e comme un contour de rectangle de la taille et de la couleur
	 * d�finies par l'image.</li>
	 * </ul></p>
	 * <p>L'image n'est pas affich�e si sa largeur ou sa hauteur est n�gative.</p>
	 * <p>Dans le cas d'une image textur�e, la texture utilis�e est celle correspondant au nom de fichier servant
	 * d'identifiant � l'image. Cette fonction rogne la texture selon les dimensions sp�cifi�es par l'image si 
	 * n�cessaire.</p>
	 * <p>Si un son est associ� � l'image, alors celui-ci est jou�, son nom de fichier �tant pass� au pr�alable � la
	 * m�thode addSound de cette classe pour charger le son si ce n'est pas d�j� fait.</p>
	 * 
	 * @throws Exception
	 * 		si la lecture des fichiers textures est impossible.
	 */
	private void drawImages() throws Exception {
		
		ArrayList<Image> images = this.vue.images();
		ArrayList<TexturedImage> vboImages = this.vue.vboImages();
		DoubleBuffer vboBuffer = BufferUtils.createDoubleBuffer(vboImages.size()*4*7); // 4pts, 2coord+3col+2text
		
		if (this.vboID == 0) {
			images.addAll(0, vboImages);
			vboImages.clear();
		}
		
		double x, y, xw, yh, r, g, b, textX, textY, textXW, textYH;
		double d[] = new double[4*7];
		
		for (TexturedImage image : vboImages) {
			x = image.getX();
			y = image.getY();
			xw = x+image.getW();
			yh = y+image.getH();
			r = image.getCouleur().r;
			g = image.getCouleur().g;
			b = image.getCouleur().b;
			textX = image.getTextRect().getX();
			textY = image.getTextRect().getY();
			textXW = textX+image.getTextRect().getW();
			textYH = textY+image.getTextRect().getH();
			d = new double[]{x,y,r,g,b,textX,textY,
					xw,y,r,g,b,textXW,textY,
					xw,yh,r,g,b,textXW,textYH,
					x,yh,r,g,b,textX,textYH};
			vboBuffer.put(d);
		}
		
		// Rendu des images par VBO
		
		vboBuffer.rewind();
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, this.vboID);
		if (vboBuffer.capacity()*8 > this.vboBufferSize) {
			ARBBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB,
					vboBuffer, ARBBufferObject.GL_STREAM_DRAW_ARB);
			this.vboBufferSize = vboBuffer.capacity()*8; // 8 : taille d'un double
		} else {
			ARBBufferObject.glBufferSubDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0, vboBuffer);
		}
		if (vboImages.size() > 0) {
			Texture text = this.textures.get(vboImages.get(0).getTextureID());
			if (text != null)
				text.bind();
			else
				throw new Exception("Erreur lors de la lecture de la texture des images VBO" +
						" dans la m�thode drawImage(Image) de la classe GameThread. Fichier "
						+vboImages.get(0).getTextureID()+" non trouv�.");
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			GL11.glVertexPointer(2, GL11.GL_DOUBLE, 7*8, 0);
			GL11.glColorPointer(3, GL11.GL_DOUBLE, 7*8, 2*8);
			GL11.glTexCoordPointer(2, GL11.GL_DOUBLE, 7*8, 5*8);
			GL11.glDrawArrays(GL11.GL_QUADS, 0, this.vboBufferSize/7/8);
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		}
		
		for (Image img : images) {
			if (img == null)
				continue;
			
			if (img.getW() < 0 || img.getH() < 0)
				continue;
	
			if (img instanceof CombinedImage) {
				CombinedImage cbImg = (CombinedImage) img;
				// L'image principale de la CombinedImage n'est affich�e que si sa texture est d�finie.
				if (cbImg.getTextureID() != "") {
					cbImg.getCouleur().bind();
					
					Texture text = this.textures.get(cbImg.getTextureID());
					if (text != null)
						text.bind();
					else {
						throw new Exception("Erreur lors de la lecture de la texture de l'image compos�e " +
								img.getName()+" dans la m�thode drawImage(Image) de la classe GameThread. Fichier "
								+cbImg.getTextureID()+" non trouv�.");
					}
					x = cbImg.getX();
					y = cbImg.getY();
					xw = x+cbImg.getW();
					yh = y+cbImg.getH();
					textX = cbImg.getTextRect().getX();
					textY = cbImg.getTextRect().getY();
					textXW = textX+cbImg.getTextRect().getW();
					textYH = textY+cbImg.getTextRect().getH();
					cbImg.getCouleur().bind();
					GL11.glPushMatrix();
						GL11.glTranslated(x+(xw-x)/2, y+(yh-y)/2, 0);
						GL11.glRotatef(cbImg.getAngle(), 0, 0, 1);
						GL11.glTranslated(-(x+(xw-x)/2), -(y+(yh-y)/2), 0);
						GL11.glBegin(GL11.GL_QUADS);
							GL11.glTexCoord2d(textX, textY);
							GL11.glVertex2d(x, y);
							GL11.glTexCoord2d(textXW, textY);
							GL11.glVertex2d(xw, y);
							GL11.glTexCoord2d(textXW, textYH);
							GL11.glVertex2d(xw, yh);
							GL11.glTexCoord2d(textX, textYH);
							GL11.glVertex2d(x, yh);
						GL11.glEnd();
					GL11.glPopMatrix();
				}
				
				for (TexturedImage image : cbImg.images()) {
					image.getCouleur().bind();
					Texture text = this.textures.get(image.getTextureID());
					if (text != null)
						text.bind();
					else {
						throw new Exception("Erreur lors de la lecture de la texture de l'image " + image.getName()+
								" dans l'image compos�e "+img.getName()+" dans la m�thode drawImage(Image) de la " +
								"classe GameThread. Fichier "+image.getTextureID()+" non trouv�.");
					}
					x = cbImg.getX()+image.getX()*cbImg.getW();
					y = cbImg.getY()+image.getY()*cbImg.getH();
					xw = cbImg.getX()+(image.getX()+image.getW())*cbImg.getW();
					yh = cbImg.getY()+(image.getY()+image.getH())*cbImg.getH();
					textX = image.getTextRect().getX();
					textY = image.getTextRect().getY();
					textXW = textX+image.getTextRect().getW();
					textYH = textY+image.getTextRect().getH();
					image.getCouleur().bind();
					GL11.glPushMatrix();
						GL11.glTranslated(x+(xw-x)/2, y+(yh-y)/2, 0);
						GL11.glRotatef(image.getAngle(), 0, 0, 1);
						GL11.glTranslated(-(x+(xw-x)/2), -(y+(yh-y)/2), 0);
						GL11.glBegin(GL11.GL_QUADS);
							GL11.glTexCoord2d(textX, textY);
							GL11.glVertex2d(x, y);
							GL11.glTexCoord2d(textXW, textY);
							GL11.glVertex2d(xw, y);
							GL11.glTexCoord2d(textXW, textYH);
							GL11.glVertex2d(xw, yh);
							GL11.glTexCoord2d(textX, textYH);
							GL11.glVertex2d(x, yh);
						GL11.glEnd();
					GL11.glPopMatrix();
				}
			} else if (img instanceof TexturedImage) {
				TexturedImage image = (TexturedImage) img;
				
				image.getCouleur().bind();
				Texture text = this.textures.get(image.getTextureID());
				if (text != null)
					text.bind();
				else {
					throw new Exception("Erreur lors de la lecture de la texture de l'image " +
							img.getName()+" dans la m�thode drawImage(Image) de la classe GameThread. Fichier "
							+image.getTextureID()+" non trouv�.");
				}
				x = image.getX();
				y = image.getY();
				xw = x+image.getW();
				yh = y+image.getH();
				textX = image.getTextRect().getX();
				textY = image.getTextRect().getY();
				textXW = textX+image.getTextRect().getW();
				textYH = textY+image.getTextRect().getH();
				GL11.glPushMatrix();
					GL11.glTranslated(x+(xw-x)/2, y+(yh-y)/2, 0);
					GL11.glRotatef(image.getAngle(), 0, 0, 1);
					GL11.glTranslated(-(x+(xw-x)/2), -(y+(yh-y)/2), 0);
					GL11.glBegin(GL11.GL_QUADS);
						GL11.glTexCoord2d(textX, textY);
						GL11.glVertex2d(x, y);
						GL11.glTexCoord2d(textXW, textY);
						GL11.glVertex2d(xw, y);
						GL11.glTexCoord2d(textXW, textYH);
						GL11.glVertex2d(xw, yh);
						GL11.glTexCoord2d(textX, textYH);
						GL11.glVertex2d(x, yh);
					GL11.glEnd();
				GL11.glPopMatrix();
			} else {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glColor3d(img.getCouleur().r, img.getCouleur().g, img.getCouleur().b);
				
				if (img.getW() == 0 && img.getH() == 0) {
					// En mode GL_LINE_LOOP, un point unique ne serait pas visible.
					GL11.glBegin(GL11.GL_POINTS);
						GL11.glVertex2d(img.getX(), img.getY());
					GL11.glEnd();
				} else {
					x = img.getX();
					y = img.getY();
					xw = img.getX()+img.getW();
					yh = img.getY()+img.getH();
					GL11.glPushMatrix();
						GL11.glTranslated(x+(xw-x)/2, y+(yh-y)/2, 0);
						GL11.glRotatef(img.getAngle(), 0, 0, 1);
						GL11.glTranslated(-(x+(xw-x)/2), -(y+(yh-y)/2), 0);
						GL11.glBegin(GL11.GL_LINE_LOOP);
							GL11.glVertex2d(x, y);
							GL11.glVertex2d(xw, y);
							GL11.glVertex2d(xw, yh);
							GL11.glVertex2d(x, yh);
						GL11.glEnd();
					GL11.glPopMatrix();
				}
				
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			
			// Lecture du son
			
			String s = img.getSound();
			if (s != "" && s != null) {
				Audio a = this.sounds.get(s);
				if (a == null)
					throw new IOException("La lecture du fichier audio "+s+" a �chou�.");
				if (img.isSpatialSound())
					a.playAsSoundEffect(1, 1, false,
							1.0f/(Constantes.initialDisplayWidth*(float) this.vue.getCoeffWidth())*
							4*((float) img.getX()-this.vue.getX()-
									Constantes.initialDisplayWidth*(float) this.vue.getCoeffWidth()/2),
							1.0f/(Constantes.initialDisplayHeight*(float) this.vue.getCoeffHeight())*
							4*((float) img.getY()-this.vue.getY()-
									Constantes.initialDisplayHeight*(float) this.vue.getCoeffHeight()/2),
							0);
				else
					a.playAsSoundEffect(1, 1, false);
			}
		}
		
	}
	
	/**
	 * R�initialise le COLOR_BUFFER du contexte OpenGL.
	 */
	private void clear() {
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
	}
	
	/**
	 * Active ou ferme le mode plein �cran de l'application en fonction de l'�tat du bool�en fullscreen de cette classe.
	 * 
	 * <p>En mode plein �cran, la souris est captur�e par l'application.</p>
	 * 
	 * @throws LWJGLException
	 * 		si les m�thodes setDisplayMode(DisplayMode) ou setFullscreen(boolean) de la classe Display l�vent une
	 * LWJGLException.
	 */
	private void updateDisplayMode() throws LWJGLException {
		
		// Aucun changement n�cessaire
		if (this.fullscreen == Display.isFullscreen())
			return;
		
		if (this.fullscreen) {
			// Passage en mode plein �cran
			Display.setDisplayMode(Display.getDesktopDisplayMode());
			Display.setFullscreen(true);
			Mouse.setGrabbed(true);
		} else {
			// Passage en mode fen�tr�
			Display.setDisplayMode(new DisplayMode(Constantes.initialDisplayWidth, Constantes.initialDisplayHeight));
			Display.setFullscreen(false);
			Mouse.setGrabbed(false);
		}
		
		// R�initialisation de l'affichage
		reshape();
		
	}
	
	/**
	 * R�ajuste le Viewport et la matrice de projection (glOrtho) selon le Display.
	 * 
	 * <p>Cette m�thode doit �tre appel�e d�s que le Display change de mode.</p>
	 */
	private void reshape() {
		
		GL11.glViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), 0, 1, -1);
		
	}
	
	/**
	 * Cr�e des bordures bleues qui encadrent la vue si celle-ci est plus petite que le Display.
	 */
	private void packView() {
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3d(0, 1, 1);
		GL11.glRecti(0, 0, this.vue.getX(), Display.getDisplayMode().getHeight());
		GL11.glRecti(this.vue.getX(), 0, this.vue.getX()+this.vue.getW(), this.vue.getY());
		GL11.glRecti(this.vue.getX()+this.vue.getW(), 0, Display.getDisplayMode().getWidth(),
				Display.getDisplayMode().getHeight());
		GL11.glRecti(this.vue.getX(), this.vue.getY()+this.vue.getH(), this.vue.getX()+this.vue.getW(),
				Display.getDisplayMode().getHeight());
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
	}
	
	/**
	 * Parcourt tous les contr�les en attente.
	 * 
	 * <p>Les actions g�r�es par cette classe sont :
	 * <ul>
	 * <li>ESCAPE : quitter le jeu (passage du bool�en playing de cette classe � l'�tat faux).</li>
	 * <li>F11 : passer ou quitter le mode plein �cran (m�thode setFullScreen(boolean) de cette classe).</li>
	 * </ul></p>
	 * <p>Les autres contr�les sont pass�s � la vue de cette classe par les m�thodes pollKeyboardDown(int) et
	 * pollKeyboardUp(int) de la classe View.</p>
	 * 
	 */
	private void pollInput() {
		
		while (Keyboard.next()) {
			int key = Keyboard.getEventKey();
			if (Keyboard.isKeyDown(key)) {
				switch (key) {
				case Keyboard.KEY_ESCAPE:
					this.setPlaying(false);
					break;
				case Keyboard.KEY_F11:
					this.setFullscreen(!this.fullscreen);
					break;
				default:
					this.vue.pollKeyboardDown(key);
				}
			} else {
				switch (key) {
				default:
					this.vue.pollKeyboardUp(key);
				}
			}
		}
		
	}
	
	/**
	 * Joue, arr�te ou change la musique de fond en fonction de l'�tat de la vue.
	 * 
	 * <p>Si la m�thode isChangingMusic() de la classe View retourne vrai, alors la musique est chang�e gr�ce � un
	 * AudioLoader par la musique dont le nom de fichier est retourn� par la m�thode getMusicFile() de la classe View.
	 * Si la m�thode isPlayingMusic() de la classe View retourne vrai, et si la variable musique de cette classe n'est
	 * pas null, alors la musique est jou�e en boucle.</p>
	 * <p>La musique doit �tre au format OGG.</p>
	 * <p>Les sons stock�s par la vue sont �galement jou�s, la m�thode addSound de cette classe �tant pr�alablement
	 * appel�e pour charger le son si ce n'est pas d�j� fait.</p>
	 * 
	 * @throws IOException
	 * 		si l'appel � AudioLoader.getAudio(String, InputStream) l�ve une IOException.
	 */
	private void updateSound() throws IOException {
		
		if (this.musicName != this.vue.getMusicFile()) {
			this.musique = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(this.vue.getMusicFile()));
			if (this.musique != null)
				this.musique.playAsMusic(1, 1, true);
			this.musicName = this.vue.getMusicFile();
		}
		ArrayList<String> snds = this.vue.soundsToPlay();
		for (String s : snds) {
			if (!this.sounds.containsKey(s)) {
				this.addSound(s);
			}
			Audio a = this.sounds.get(s);
			if (a == null)
				throw new IOException("La lecture du fichier audio "+s+" a �chou�.");
			a.playAsSoundEffect(1, 1, false);
		}
		
	}
	
	/**
	 * Ajoute une effet sonore � la liste de ce thread.
	 * 
	 * @param fileName
	 * 		Le nom du fichier son.
	 * @throws IOException
	 * 		si l'appel � AudioLoader.getAudio(String, InputStream) l�ve une IOException.
	 */
	private void addSound(String fileName) throws IOException {
		
		if (fileName == null)
			return;
		try {
			this.sounds.put(fileName, AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream(fileName)));
		} catch (IOException e) {
			throw new IOException("Impossible de lire le fichier "+fileName, e);
		}
		
	}
	
	/**
	 * Ajoute une texture � la liste des textures disponibles.
	 * 
	 * @param file
	 * 		Le nom du fichier texture � ajouter.
	 * @throws IOException
	 * 		si le chargement du fichier �choue.
	 */
	private void addTexture(String file) throws IOException {
		
		if (file != "" && file != null && !this.textures.containsKey(file))
			this.textures.put(file, TextureLoader.getTexture("PNG", new FileInputStream(file)));
		
	}
	
	/**
	 * Boucle principale du thread de jeu.
	 * 
	 * <p>Cette m�thode commence par initialiser le contexte OpenGL en utilisant les champs initialDisplayWidth et
	 * initialDisplayHeight de la classe Constantes. Une variable musique de type Audio est cr��e. Elle servira de
	 * param�tre � la m�thode updateSound(Audio) de cette classe. Une fois le Display cr��, il passe le bool�en ready �
	 * l'�tat vrai.</p>
	 * <p>La boucle de jeu est ensuite lanc�e, et ne s'arr�te que lorsque l'utilisateur appuie sur la croix de fermeture
	 * de la fen�tre ou d�clenche un �v�nement de fermeture de l'application (indiqu� au moyen du bool�en playing).</p>
	 * <p>A l'int�rieur de cette boucle, cette m�thode r�alise les actions suivantes :
	 * <ul>
	 * <li>Chargement des textures dont les noms sont sp�cifi�s dans la liste texturesToAdd de cette classe. Cette liste
	 * est ensuite effac�e.</li>
	 * <li>Redimensionnement �ventuel de la vue gr�ce � la m�thode updateDisplayMode() de cette classe.</li>
	 * <li>Gestion des contr�les gr�ce � la m�thode pollInput() de cette classe.</li>
	 * <li>R�initialisation de l'affichage gr�ce � la m�thode clear() de cette classe, rafra�chissement de la vue de
	 * cette classe gr�ce � la m�thode refresh() de la classe View, mise � jour des sons gr�ce � la m�thode updateSound()
	 * de cette classe, et enfin parcours de la liste des images
	 * de la vue en utilisant la m�thode drawImage(Image) de cette classe afin de les dessiner.</li>
	 * <li>Encadrement de la vue par des bandes bleues si n�cessaire gr�ce � la m�thode packView() de cette classe.</li>
	 * <li>Mise � jour du Display.</li>
	 * </ul></p>
	 * <p>La boucle peut �tre quitt�e si la croix de fermeture de la fen�tre est cliqu�e ou si le bool�en playing de cette
	 * classe passe � l'�tat faux. Le bool�en playing de cette classe est alors pass� � l'�tat faux et le Display est 
	 * d�truit.</p>
	 */
	@Override
	public void run() {
		
		try {
			// Initialisation du contexte OpenGL
			
			Display.setDisplayMode(new DisplayMode(Constantes.initialDisplayWidth, Constantes.initialDisplayHeight));
			Display.setTitle("Poufalouf marche et est content !");
			Display.setIcon(new ByteBuffer[] {
				new PNGImageData().loadImage(new FileInputStream("res/icon16.png")),
				new PNGImageData().loadImage(new FileInputStream("res/icon32.png"))
			});
			Display.create();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glClearColor(0, 0, 0, 0);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), 0, 1, -1);
			
			if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
				IntBuffer buf = BufferUtils.createIntBuffer(1);
				ARBBufferObject.glGenBuffersARB(buf);
				this.vboID = buf.get(0);
			}
			
			this.ready = true;
			
			// Boucle principale
			
			while(this.isPlaying() && !Display.isCloseRequested()) {
				synchronized (this) {
					if (this.texturesToAdd.size() > 0) {
						@SuppressWarnings("unchecked")
						ArrayList<String> strs = (ArrayList<String>) this.texturesToAdd.clone();
						for (String s : strs) {
							this.addTexture(s);
						}
						this.texturesToAdd.clear();
					}
					if (this.soundsToAdd.size() > 0) {
						@SuppressWarnings("unchecked")
						ArrayList<String> strs = (ArrayList<String>) this.soundsToAdd.clone();
						for (String s : strs) {
							this.addSound(s);
						}
						this.soundsToAdd.clear();
					}
					updateDisplayMode();
					clear();
					pollInput();
					this.vue.refresh();
					updateSound();
					drawImages();
				}
				packView();
				Display.update();
				Display.sync(60);
			}
			this.setPlaying(false);
			Display.destroy();
		} catch (Exception e) {
			this.setFullscreen(false);
			try {
				this.updateDisplayMode();
			} catch (LWJGLException e1) {
				e1.printStackTrace();
			}
			ExceptionMessageBox.show(e, "Erreur game thread");
			this.setPlaying(false);
			if (Display.isCreated())
				Display.destroy();
			if (AL.isCreated())
				AL.destroy();
		}
		
	}
	
	/**
	 * Met � jour la vue associ�e � ce GameThread.
	 * 
	 * <p>Cette m�thode est synchronis�e pour s'assurer que la vue ne sera pas modifi�e pendant son traitement par
	 * la m�thode run() de cette classe.</p>
	 * <p>Si le param�tre de cette m�thode est null, aucun changement n'est effectu�.</p>
	 * <p>Les noms de textures retourn�s par la m�thode textureList du mod�le associ� � la vue sont ajout�s � la liste
	 * texturesToAdd de cette classe.</p>
	 * 
	 * @param view
	 * 		La vue � associer � ce thread de jeu.
	 * @throws IOException
	 * 		si une erreur survient lors de l'ajout des images n�cessaires � la vue de ce thread.
	 */
	public synchronized void changeView(View view) throws IOException {
		
		if (view != null) {
			this.musique = null;
			this.vue = view;
			this.vue.resizeToDisplay();
			this.vue.setPosition();
		}
		
	}
	
	/**
	 * Ajoute les textures dont les noms sont pass�s en param�tres � la liste des textures � ajouter � ce thread.
	 * 
	 * @param textureNames
	 * 		La liste des textures � ajouter.
	 */
	public synchronized void addTextures(ArrayList<String> textureNames) {
		
		if (textureNames == null)
			return;
		for (String s : textureNames) {
			if (s != null)
				this.texturesToAdd.add(s);
		}
		
	}
	
	/**
	 * Ajoute les sons dont les noms sont pass�s en param�tres � la liste des sons � ajouter � ce thread.
	 * 
	 * @param soundNames
	 * 		La liste des sons � ajouter.
	 */
	public synchronized void addSounds(ArrayList<String> soundNames) {
		
		if (soundNames == null)
			return;
		for (String s : soundNames) {
			if (s != null)
				this.soundsToAdd.add(s);
		}
		
	}

	/**
	 * Met � jour le bool�en playing de ce GameThread.
	 * 
	 * @param playing
	 * 		Vrai si le jeu ne doit pas �tre arr�t�.
	 */
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
	
	/**
	 * Retourne l'�tat du bool�en playing de ce GameThread.
	 * 
	 * @return
	 * 		L'�tat du bool�en playing, vrai tant que le jeu est en cours (quand la boucle infinie de la m�thode run()
	 * de cette classe tourne).
	 */
	public boolean isPlaying() {
		return this.playing;
	}

	/**
	 * Retourne l'�tat du bool�en ready de ce GameThread.
	 * 
	 * @return
	 * 		L'�tat du bool�en ready, vrai si le Display a �t� cr��.
	 */
	public boolean isReady() {
		return this.ready;
	}
	
	/**
	 * Met � jour l'�tat du bool�en fullscreen de ce GameThread.
	 * 
	 * @param fullscreen
	 * 		Vrai si l'affichage doit �tre en mode plein �cran.
	 */
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
	
}
