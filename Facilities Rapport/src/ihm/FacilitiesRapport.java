package ihm;

import java.io.IOException;

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
	public static void main(String[] args){ 
		// Delegation a l'EDT (Event Dispatch Thread) pour la gestion de la barre de progression
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                	// Creation du formulaire
					new Formulaire();
				} 
                catch (IOException e) {
					e.printStackTrace();
				}
            }
        });	
	}
	
}
