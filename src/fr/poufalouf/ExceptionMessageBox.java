package fr.poufalouf;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * Classe permettant d'afficher une fen�tre d'erreur
 * 
 * <p>Elle ne contient qu'une m�thode statique affichant une fen�tre pour donner des informations sur le Throwable
 * (l'erreur) pass� en param�tre.</p>
 * 
 * @author Anaïs Vernet
 */
public class ExceptionMessageBox {

	/**
	 * Affiche une fen�tre donnant des informations sur le Throwable pass� en param�tre.
	 * 
	 * @param e
	 * 		Le Throwable � afficher.
	 * @param title
	 * 		Le titre de la fen�tre.
	 */
	public static void show(Throwable e, String title) {
		
		if (GraphicsEnvironment.isHeadless()) {
			System.err.println("Interface graphique non reconnue ou inexistante.");
			if (e != null)
				e.printStackTrace();
			return;
		}
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			// Si une exception est lev�e, le LookAndFeel par d�faut est utilis�, l'exception n'est donc pas trait�e.
			e1.printStackTrace();
		}
		
		int choix;
		if (e == null) {
			JOptionPane.showMessageDialog(null, "Erreur non d�finie.", title, JOptionPane.ERROR_MESSAGE);
			choix = JOptionPane.CLOSED_OPTION;
		} else {
			Object options[] = {"OK", "D�tails"};
			choix = JOptionPane.showOptionDialog(null, e.getMessage(), title, JOptionPane.DEFAULT_OPTION, 
					JOptionPane.ERROR_MESSAGE, null, options, options[0]);
		}
		
		if (choix != JOptionPane.CLOSED_OPTION && choix != JOptionPane.OK_OPTION) {
			JFrame frame = new JFrame(title);
			frame.setSize(400, 300);
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			
			JTextArea textBox = new JTextArea();
			Throwable t = e;
			do {
				StackTraceElement[] error = t.getStackTrace();
				textBox.append(t.toString()+"\n\n");
				if (error == null || error.length == 0)
					textBox.append("La description de l'erreur n'est pas disponible.");
				else {
					for (StackTraceElement err : error) {
						if (err != null)
							textBox.append("Ligne "+err.getLineNumber()+" M�thode "+err.getMethodName()+" de la classe "
									+err.getClassName()+".\n");
					}
				}
				textBox.append("\n**************\n");
				t = t.getCause();
			} while (t != null);
			textBox.setEditable(false);
			textBox.setLineWrap(true);
			textBox.setWrapStyleWord(true);
			
			JButton ok = new JButton("OK");
			OkListener okListener = new OkListener();
			ok.addActionListener(okListener);
			
			JScrollPane scrolls = new JScrollPane(textBox);
			frame.add(scrolls, BorderLayout.CENTER);
			frame.add(ok, BorderLayout.SOUTH);
			frame.setVisible(true);
			
			while (!okListener.isQuitting() && frame.isDisplayable()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
			frame.dispose();
		}
		
	}
	
	/**
	 * Classe interne � ExceptionMessageBox servant � g�rer le bouton "OK" de la fen�tre des d�tails de l'erreur.
	 * 
	 * @author Anaïs Vernet
	 */
	private static class OkListener implements ActionListener {
		
		/**
		 * Indique si la m�thode show(Throwable, String) de la classe ExceptionMessageBox doit fermer la fen�tre d'erreur.
		 */
		private boolean quitting;
		
		/**
		 * Constructeur OkListener.
		 * 
		 * <p></p>
		 * 
		 * <b>Initialisation</b>
		 * 
		 * <p>
		 * <ul>
		 * <li>quitting : faux.</li>
		 * </ul></p>
		 */
		public OkListener() {
			
			this.quitting = false;
			
		}
		
		/**
		 * D�finit l'action � effectuer lorsque le bouton "OK" est cliqu�.
		 * 
		 * <p>Passe le bool�en quitting de cette classe � l'�tat vrai.</p>
		 * 
		 * @param a
		 * 		Inutile ici.
		 */
		@Override
		public void actionPerformed(ActionEvent a) {
			
			this.quitting = true;
			
		}
		
		/**
		 * Retourne l'�tat du bool�en quitting de cette classe.
		 * 
		 * @return
		 * 		L'�tat du bool�en quitting, vrai si la fen�tre d'erreur doit �tre quitt�e.
		 */
		public boolean isQuitting() {
			return this.quitting;
		}
		
	}
	
}
