package ihm;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class PersonnalDocumentListener implements DocumentListener {

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
