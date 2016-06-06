package ihm;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Classe principale appelee lors de l'execution de l'application
 * @author Lucie PELLOTTIERO
 *
 */
public class FacilitiesRapport {
	
	/**
	 * Fonction principale appelee lors de l'execution de l'application
	 * @param args les arguments passes a l'application (innutilises)
	 */
	public static void main(final String[] args){ 
		// Delegation a l'EDT (Event Dispatch Thread) pour la gestion des threads (le formulaire + la barre de progression)
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                	// Creation du formulaire
					new Form();
				} 
                catch (Exception e) {
					e.printStackTrace();
					// Affichage de l'erreur
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur", 
							JOptionPane.WARNING_MESSAGE);
				}
            }
        });	
	}
	
}
