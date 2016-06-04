package ihm;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Classe de gestion de la barre de progression
 * @author Lucie PELLOTIERRO
 *
 */
public class ProgressBarFrame extends JFrame{

	/**
	 * Numero de serialisation genere par defaut.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * La JProgressBar de la fenetre
	 */
	private JProgressBar pbar;
	
	/**
	 * La valeur minimum de la JProgressBar
	 */
	public static final int MY_MINIMUM = 0;
	
	/**
	 * La valeur maximum de la JProgressBar
	 */
	public static final int MY_MAXIMUM = 100;
	
	/**
	 * Creation et affichage de la fenetre
	 */
	public ProgressBarFrame() {
		// Creation de la JFrame
		super ("Création du pdf");
		
		// Creation d'un lien vers cette JFrame lorsque le this ne pointe plus dessus
		JFrame thisFrame = this;
		
		// Initialisation de la JProgressBar
		pbar = new JProgressBar();
		pbar.setMinimum(MY_MINIMUM);
		pbar.setMaximum(MY_MAXIMUM);
		pbar.setStringPainted(true);
		pbar.setValue(MY_MINIMUM);
		
		// Delegation a l'EDT (Event Dispatch Thread) pour la gestion du thread de la JFrame
		EventQueue.invokeLater(new Runnable() {
            
			@Override
            public void run() {
            	JPanel progressBarPanel = new JPanel();
        		
        		// Ajout de la JProgressBar a la JFrame
        		progressBarPanel.add(pbar);
        		
        		thisFrame.setContentPane(progressBarPanel);
        		// Attribution de la taille minimale a la JFrame
        		thisFrame.pack();
        		// Position de la JFrame au centre de l'ecran
        		thisFrame.setLocationRelativeTo(null);
        		// Indique que lorsque l'affichage de cette JFrame change (setVisible(), toFront()...) elle doit etre mise en focus
        		thisFrame.setAutoRequestFocus(true);
        		// Affichage de la JFrame
        		thisFrame.setVisible(true);
        		// Mise au premier plan de la fenetre
        		thisFrame.toFront();
            }
        });
	}
	
	/**
	 * Change la valeur affichee par la JProgressBar
	 * @param newValue La nouvelle valeur a afficher
	 */
	public void updateBar(final int newValue) {
		// Changement de la valeur de la JprogressBar
		pbar.setValue(newValue);
		
		// Actualisation de la JFrame
		this.repaint();
		this.revalidate();
	}
	
	/**
	 *  Obtention de la valeur de la JProgressBar
	 * @return La valeur de la JProgressBar
	 */
	public int getProgress() {
		return  pbar.getValue();
	}
}