package ihm;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * DocumentListener qui permet d'effectuer la meme action pour chaque type de mise a jour.<br>
 * <strong>Attention!</strong> Il est possible que la fonction update s'applique plusieurs fois selon le type de mise a jour.
 * @author Lucie PELLOTIERRO
 *
 */
public abstract class PersonnalDocumentListener implements DocumentListener {

	/**
	 * Mise a jour a appliquer lors d'un changement du Document ecoute.
	 * @param arg0 le DocumentEvent qui a declencher la mise a jour
	 */
	public abstract void update (DocumentEvent arg0);
	
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		update(arg0);
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		update(arg0);
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		update(arg0);
	}

}
