package ihm;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

/**
 * {@link JComponent} a ajouter a un {@link JFileChooser} en tant qu'{@link JFileChooser.accessory} pour obtenir
 *  un apercu d'une {@link Image} lorsqu'elle est selectionnee dans le {@link JFileChooser}.
 * @author Lucie PELLOTTIERO
 *
 */
public class ImagePreview extends JComponent implements PropertyChangeListener{

	/**
	 * Numero de serialization genere
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Le fichier selectionne dans le {@link JFileChooser}
	 */
	private File file;
	/**
	 * L'apercu de l'image si le fichier selectionner en est une
	 */
	private ImageIcon thumbnail;
	
	/**
	 * Constructeur de l'{@link ImagePreview}. Il faut ensute l'ajouter aux {@link JFileChooser.accessory} du {@link JFileChooser}.
	 * @param fileChooser Le {@link JFileChooser} auquel on ajoute cet {@link ImagePreview}
	 */
	public ImagePreview(final JFileChooser fileChooser) {
		// Definition de la taille de cet ImagePreview
        setPreferredSize(new Dimension(100, 50));
        // Ajout du PropertyChangeListener de cet ImagePreview
        fileChooser.addPropertyChangeListener(this);
    }

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		
		// Determine si on doit mettre a jour l'affichage
		boolean update = false;
		// Obtient le nom de l'evenement
		final String eventName = evt.getPropertyName();
		
		// Si le repertoir a change, on enleve l'image et on indique qu'il faut mettre a jour l'affichage
		if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(eventName)) {
		    file = null;
		    update = true;
		} 
		
		// Si un fichier a ete selectionne alors on l'obtient et on indique qu'il faut mettre a jour l'affichage
		else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(eventName)) {
		    file = (File) evt.getNewValue();
		    update = true;
		}
		
		// Si on doit faire la mise a jour de l'affichage
		if (update) {
			// Reinitialise l'apercu
		    thumbnail = null;
		    // Si cet ImagePreview est affiche
		    if (isShowing()) {
		    	// Charge l'image et son apercu
		        loadImage();
		        // Affiche l'apercu
		        repaint();
		    }
		}
	}
	
	/**
	 * Charge l'image si cela est possible
	 */
	public void loadImage() {
		
		// Si le fichier est null, cela signifie que l'on a changer de repertoir donc on reinitialise l'apercu
        if (file == null) {
            thumbnail = null;
            return;
        }

        // Image temporaire qui sera l'image reelle
        Image tmpImage = null;
        try {
        	// Tentative de lecture de l'image
        	tmpImage = ImageIO.read(file);
		    
        	// Si c'est une image
        	if (tmpImage != null) {
        		// On creer une ImageIcon temporaire a partir de l'image
		        ImageIcon tmpIcon = new ImageIcon(tmpImage);
		        
		        // Si la cration a reussi
		        if (tmpIcon != null) {
		        	// Si l'image a besoin d'etre redimensionnee
		            if (tmpIcon.getIconWidth() > 90) {
		            	// On creer une nouvelle ImageIcon qui sera une version redimensionnee de la veritable Image
		            	// Ce sera donc l'apercu
		                thumbnail = new ImageIcon(tmpIcon.getImage().
		                                          getScaledInstance(90, -1,
		                                                      Image.SCALE_DEFAULT));
		            } 
		            else {
		            	// Si il n'y a pas besoin de redimensionner alors on la prend directement comme apercu
		                thumbnail = tmpIcon;
		            }
		        }
        	}
        } 
        catch (IOException e) {
			e.printStackTrace();
			return;
		}
    }
	
	@Override
	protected void paintComponent(Graphics graphic) {
		// Si l'apercu n'a pas ete initialise, on tente de le charger
		// Si cela ne fonctionne pas alors le fichier n'est pas une image
        if (thumbnail == null) {
            loadImage();
        }
        // Si l'apercu n'est pas null
        if (thumbnail != null) {
        	// On obtient les coordonnees ou doit etre placee le coin en haut a gauche de l'image
            int x = getWidth() / 2 - thumbnail.getIconWidth() / 2;
            int y = getHeight() / 2 - thumbnail.getIconHeight() / 2;

            // On ne place pas l'image trop en dessou (lie a la dimension dans le constructeur de cet ImagePreview)
            if (y < 0) {
                y = 0;
            }

            // On ne place pas l'image trop a gauche (lie a la dimension dans le constructeur de cet ImagePreview)
            if (x < 5) {
                x = 5;
            }
            
            // On affiche l'image aux coordonnees calculees dans cet ImagePreview
            thumbnail.paintIcon(this, graphic, x, y);
        }
    }
}
