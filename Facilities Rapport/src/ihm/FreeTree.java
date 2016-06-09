package ihm;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import utilities.OperationUtilities;

/**
 * JPanel d'arborescence libre.
 * @author Lucie PELLOTTIERO
 *
 */
public class FreeTree extends JPanel{
	
	/**
	 * Numero de serialization par defaut
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Nombre maximum d'element que l'on peut ajouter.<br>
	 * Grace au JPanel des elements, ce nombre est theoriquement infinie
	 */
	private static final int NUMBER_ELEMENT_ALLOWED = Integer.MAX_VALUE;
	
	/**
	 * Les String qui peuvent etres affichees sur le bouton d'ajout d'element selon le contexte
	 */
	private static final String[] ADD_ELEMENT_TEXT = {"Ajouter un élément", "Remplissez la partie Titre", 
			"Remplissez tous les éléments" , "Limite d'élément atteinte"};
	
	/**
	 * Le GridBagConstraints de ce FreeTree.
	 */
	private final GridBagConstraints constraint;
	
	/**
	 * Les JTextField des elements. On pourra ensuite les recuperer pour la generation de document
	 */
	private final Collection<JTextField> elements;
	/**
	 * Les JTextField du nombre associe a un element. On pourra ensuite les recuperer pour la generation de document
	 */
	private final Collection<JTextField> elementNumbers;
	/**
	 * Le JTextField du titre de ce FreeTree.<br>
	 * Le modifier modifie aussi la TitleBorder de ce FreeTree
	 */
	private final JTextField titleTextField;
	/**
	 * Le JTextArea du commentaire de ce FreeTree
	 */
	private final JTextArea textAreaComment;
	
	/**
	 * Le JButton d'ajout d'element. Il peut etre modifier dans la fonction d'ajout d'element
	 */
	private final JButton addElement;
	/**
	 * L'ImageIcon representant un "+" pour eviter d'avoir a le reconstruir a chaque ajout d'element.<br>
	 * En effet, lorsque le JBouton addElement est modifier, on moodifie aussi son ImageIcon.
	 */
	private final ImageIcon addElementIcon;
	
	/**
	 * La position du dernier element. Elle peut etre modifier dans l'ActionListener du JButton addElement.
	 */
	private int lastElementPosition;

	/**
	 * Construit un FreeTree qui est un JPanel.
	 */
	public FreeTree(){

		// Utilise le constructeur de JPanel avec un GridBagLayout
		super (new GridBagLayout());
		
		// Creer un lien vers ce FreeTree utilise lorsque this n'a point plus vers ce FreeTree
		final FreeTree thisFreeTree = this;
		
		// Creer une TitleBorder avec Arborescance libre en titre. Elle sera ammenée a changer
		this.setBorder(BorderFactory.createTitledBorder("Arborescence libre"));
		
		// Creer la GridBagConsraints que l'on utilisera pour ce FreeTree
		constraint = new GridBagConstraints();
		// Initialise toutes les valeurs qui nous sont utiles
		constraint.gridx = 0;
		constraint.gridy = 0;
		constraint.weightx = 0;
		// Ajoute des marge pour chaques Component de ce FreeTree
		constraint.insets = new Insets(5, 0, 0, 0);
		// Indique que les elements peuvent prendre la place disponnible verticalement
		constraint.fill = GridBagConstraints.VERTICAL;
		
		// Initialise les Collection
		elements = new LinkedList<JTextField>();
		elementNumbers = new LinkedList<JTextField>();	    
	    
		// JLabel du titre
	    final JLabel title = new JLabel("Titre : ");
	    
		this.add(title, constraint);
		
		// Creation de la zone de texte textFieldTitre2de taille 15
		// La modification de ce JTextField modifie aussi la TitleBorder de ce FreeTree
		titleTextField = new JTextField(15);
		// Attribution du JTextField textFieldTitre au JLabel titre
		title.setLabelFor(titleTextField);
		
		// Ajout du JTextField textFieldTitre
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.fill = GridBagConstraints.BOTH;
		constraint.weightx = 1;
		this.add(titleTextField, constraint);
		
		// Initialisation du lastElementPosition
		lastElementPosition = 0;
		
		// Initialisation du JPanel qui contiendra tous les elements
		final JPanel elementsPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints elementConstraint = new GridBagConstraints();
		elementConstraint.gridx = 0;
		elementConstraint.gridy = lastElementPosition;
		elementConstraint.weightx = 1;
		elementConstraint.gridwidth = GridBagConstraints.REMAINDER;
		elementConstraint.insets = new Insets(3, 0, 1, 0);
		elementConstraint.fill = GridBagConstraints.BOTH;
		
		constraint.gridx = 0;
		++constraint.gridy;
		this.add(elementsPanel, constraint);

		// JButton d'ajout d'element
		addElement = new JButton(ADD_ELEMENT_TEXT[1]);
		
		// Tentative d'ajout de l'ImageIcon au JButton addElement
		// Tentative d'obtiention de l'image
		addElementIcon = new ImageIcon(Form.ICONS_PATH + File.separator + Form.ICONS_NAME[1]);
		// Si l'image n'a pas d'erreur
	    if (addElementIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
	    	// Definition de la hauteur de l'image en fonction de la taille du JButton
		    final int iconHeight = (int) (addElement.getPreferredSize().getHeight() - addElement.getPreferredSize().getHeight() / 3);
		    // Definition de la largeur de l'image en fonction de la taille du JButton
		    final int iconWidth  = addElementIcon.getIconWidth() / (addElementIcon.getIconHeight() / iconHeight);
		    
		    // Redimensionne l'image selon la largeur et la hauteur precedente
		    Image tmpImg = addElementIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    addElementIcon.setImage(tmpImg);
	    }
		
	    // Desactivation du JButton car il faut d'abord remplir le titre avant d'ajouter un element
	    // (cette contrainte n'est pas forte et est contournable, mais cela ne pose pas de problème)
		addElement.setEnabled(false);
		
		constraint.gridx = 1;
		++constraint.gridy;
		this.add(addElement, constraint); //ajout du bouton ajoutElement
		
		addElement.addActionListener(new ActionListener() {
	    	
		    public void actionPerformed(ActionEvent arg0) {	
		    	
		    	// Si le dernier element est le dernier que l'on peut ajouter
		    	// Alors on desactive le JButton et on empeche l'ajout d'element
		    	if (lastElementPosition >= NUMBER_ELEMENT_ALLOWED) {
					JOptionPane.showMessageDialog(thisFreeTree, 
		    				"Impossible d'ajouter un element supplémentaire dans la partie " + titleTextField.getText(), "Erreur", 
							JOptionPane.WARNING_MESSAGE);
					
					addElement.setEnabled(false);
					addElement.setText(ADD_ELEMENT_TEXT[3]);
					addElement.setIcon(null);
					
					return;
				}

		    	// Lors de l'ajout d'un element, on desactive le JButton
		    	// Pour indiquer qu'il faut remplir l'element precedent
		    	// (cette contrainte n'est pas forte et est contournable, mais cela ne pose pas de problème)
		    	addElement.setText(ADD_ELEMENT_TEXT[2]);
				addElement.setEnabled(false);
				addElement.setIcon(null);
		    	
				// Creation d'un nouvel element
		    	final JPanel elementPanel = addElement();
		    	
		    	// Ajout du nouvel element
		    	elementConstraint.gridy = ++lastElementPosition;
				elementsPanel.add(elementPanel, elementConstraint);
		    	
				elementsPanel.revalidate();
		    	
				// Si le dernier element est le dernier que l'on peut ajouter,
		    	// alors on desactive le JButton
				// Etant donne la taille maximale, cette verification est superflue par rapport a sa frequence de realisation
		    	/*if (lastElementPosition >= NUMBER_ELEMENT_ALLOWED) {
					
					addElement.setEnabled(false);
					addElement.setText(ADD_ELEMENT_TEXT[3]);
					addElement.setIcon(null);
				}*/
		    }
		});
		
		// Lors de la modification du JTextField du titre, cela modifie aussi le titre de la TitleBorder de ce FreeTree
		titleTextField.getDocument().addDocumentListener(new PersonnalDocumentListener() {
			
			@Override
			public void update(DocumentEvent arg0) {
				// Lors de la modification du titre, on modifie la TitleBorder de ce FreeTree
				thisFreeTree.setBorder(BorderFactory.createTitledBorder(titleTextField.getText()));
				
				// Si on a effacer le titre, alors on indique qu'il faut le remplir avant d'ajouter de nouveaux elements
				// (cette contrainte n'est pas forte et est contournable, mais cela ne pose pas de problème)
				if (titleTextField.getText().equals("")) {
					addElement.setText(ADD_ELEMENT_TEXT[1]);
					addElement.setEnabled(false);
					addElement.setIcon(null);
				}
				// Si le titre a ete rempli, alors on retablit la fonction d'ajout d'element si elle a ete desactivee
				else {
					if (!addElement.isEnabled()) {
						addElement.setText(ADD_ELEMENT_TEXT[0]);
						addElement.setEnabled(true);
						addElement.setIcon(addElementIcon);
					}
				}
			}
		});
		
		// JLabel du commentaire de FreeTree
	    final JLabel commentaire = new JLabel("Commentaire : "); //creation du label emailCl
		
	    constraint.gridx = 0;
		++constraint.gridy;
		constraint.insets = new Insets(0, 0, 0, 0);
	    this.add(commentaire, constraint);
	    
	    // Creation de la JTextArea du commentaire
	    textAreaComment = new JTextArea(4, 15);
	    // Creation de la JSCrollPane pout pouvoir ecrir de long commentaire
	    final JScrollPane scrollPaneCom = new JScrollPane(textAreaComment);
	    commentaire.setLabelFor(scrollPaneCom);

		++constraint.gridy;
		constraint.insets = new Insets(0, 0, 3, 0);
		this.add(scrollPaneCom, constraint);
	}
	
	/**
	 * Ajout d'un element sous la forme d'un JPanel
	 * @return le JPanel contenant l'element
	 */
	public JPanel addElement() {
		
		// Creation du JPanel de cet element avec un GridBagLayout
		final JPanel elementPanel = new JPanel(new GridBagLayout());
		
		// Creation du GridBagCOnstraints de cet element
		final GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridx = 0;
		constraint.weightx = 1;
		constraint.gridy = 0;
		constraint.gridwidth = 1;
		constraint.insets = new Insets(5, 2, 0, 2);
		constraint.fill = GridBagConstraints.BOTH;
    	
		// JLabel du nom de l'element
	    final JLabel element = new JLabel("Elément : ");
		
		elementPanel.add(element, constraint);
		
		// Creation du JTextField pour le nom de l'element
	    final JTextField textFieldElement = new JTextField(15);
	    // Ajout du JTextField a la Collection
	    elements.add(textFieldElement);
	    element.setLabelFor(textFieldElement);
		
	    constraint.gridx = 1;
		elementPanel.add(textFieldElement, constraint);
		
		// JLabel du nombre
	    final JLabel nombre = new JLabel("Nombre : ");
		
	    constraint.gridx = 2;
		elementPanel.add(nombre, constraint);
		
		// Creation du JTextField du nombre de l'element
	    final JTextField textFieldNombre = new JTextField(2);
	    nombre.setLabelFor(textFieldNombre);
	    // Ajout du JTextField a la Collection
	    elementNumbers.add(textFieldNombre);
	    
	    // Si on efface le nom de l'element ou son nombre, alors on indique qu'il faut les remplir
	    // avant d'ajouter de nouveaux elements
	    // (cette contrainte n'est pas forte et est contournable, mais cela ne pose pas de problème)
	    textFieldElement.getDocument().addDocumentListener(new PersonnalDocumentListener() {
			
			@Override
			public void update(DocumentEvent arg0) {
				// Si le titre ou le nombre n'est pas rempli alors on desactive la fonction d'ajout d'element
				// (cette contrainte n'est pas forte et est contournable, mais cela ne pose pas de problème)
				if (textFieldElement.getText().equals("") || textFieldNombre.getText().equals("")) {
					addElement.setText(ADD_ELEMENT_TEXT[2]);
					addElement.setEnabled(false);
					addElement.setIcon(null);
				}
				// Sinon, si ils ont ete rempli, on retablit la fonction d'ajout d'element si elle a ete desactiver
				else {
					if (!addElement.isEnabled()) {
						addElement.setText(ADD_ELEMENT_TEXT[0]);
						addElement.setEnabled(true);
						addElement.setIcon(addElementIcon);
					}
				}
			}
		});
	    
	    // Si on efface le nom de l'element ou son nombre, alors on indique qu'il faut les remplir
	    // avant d'ajouter de nouveaux elements
	    // (cette contrainte n'est pas forte et est contournable, mais cela ne pose pas de problème)
	    textFieldNombre.getDocument().addDocumentListener(new PersonnalDocumentListener() {
			
			@Override
			public void update(DocumentEvent arg0) {
				// Si le titre ou le nombre n'est pas rempli alors on desactive la fonction d'ajout d'element
				// (cette contrainte n'est pas forte et est contournable, mais cela ne pose pas de problème)
				if (textFieldElement.getText().equals("") || textFieldNombre.getText().equals("")) {
					addElement.setText(ADD_ELEMENT_TEXT[2]);
					addElement.setEnabled(false);
					addElement.setIcon(null);
				}
				// Sinon, si ils ont ete rempli, on retablit la fonction d'ajout d'element si elle a ete desactiver
				else {
					if (!addElement.isEnabled()) {
						addElement.setText(ADD_ELEMENT_TEXT[0]);
						addElement.setEnabled(true);
						addElement.setIcon(addElementIcon);
					}
				}
			}
		});
	    
	    constraint.gridx = 3;
		constraint.gridwidth = 1;
		constraint.weightx = 1;
		elementPanel.add(textFieldNombre, constraint);
		
		// Creation du JButton de suppression de cet element
		final JButton deleteElementButton = new JButton();
		
		// Tentative d'ajout de l'ImageIcon au JButton deleteElementButton
		// Tentative d'obtiention de l'image
		final ImageIcon deleteElementIcon = new ImageIcon(Form.ICONS_PATH + File.separator + Form.ICONS_NAME[4]);
		// Si l'image n'a pas d'erreur
	    if (deleteElementIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
	    	// Definition de la hauteur de l'image en fonction de la taille du JButton
		    final int iconHeight = (int) (titleTextField.getPreferredSize().getHeight() - titleTextField.getPreferredSize().getHeight() / 3);
		    // Definition de la largeur de l'image en fonction de la taille du JButton
		    final int iconWidth  = deleteElementIcon.getIconWidth() / (deleteElementIcon.getIconHeight() / iconHeight);
		    
		    // Redimensionne l'image selon la largeur et la hauteur precedente
		    Image tmpImg = deleteElementIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    deleteElementIcon.setImage(tmpImg);
		    deleteElementButton.setIcon(deleteElementIcon);
	    }
	    
	    // Suppression de cet element
		deleteElementButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {		
				
				// On enleve les JTextField que l'on stocké dans les Collections
				elements.remove(textFieldElement);
				elementNumbers.remove(textFieldNombre);
				
				// Si le JButton est desactive, on le reactive
				if (!addElement.isEnabled()) {
					
					addElement.setEnabled(true);
					addElement.setText(ADD_ELEMENT_TEXT[0]);
					addElement.setIcon(addElementIcon);	
				}
				
				// Parent de cet element
				final Container parent = elementPanel.getParent();
				
				// Si l'element a bien un parent
				if (parent != null) {
					
					// Obtient le GridBagLayout de son parent
					final GridBagLayout freeTreeLayout = (GridBagLayout)parent.getLayout();
					
					// Change le positionnement de tous les elements qui sont apres celui que l'on efface
					// Cela fonctionne car dans la hierarchie du parent de l'element, les elements sont tous ajoutes en derniers
					// Parcour les elements qui sont apres celui que l'on efface
					for (int i = OperationUtilities.getComponentIndex(elementPanel); i < parent.getComponentCount(); ++i) {
						
						// Obtention de l'element suivant
						final Component currentComponent = parent.getComponent(i);
						// Obtiention de son GridBagConstraints (celui avec lequel il a ete ajoute)
						final GridBagConstraints thisComponentConstraint = freeTreeLayout.getConstraints(currentComponent);
						// Decremente sa coordonnee verticale
						--thisComponentConstraint.gridy;
						// Applique les modifications
						freeTreeLayout.setConstraints(currentComponent, thisComponentConstraint);
					}
					
					// Suppression de cet element de son parent
					parent.remove(elementPanel);
					// On revalide le parent
					parent.revalidate();
				}
				// Decrement l'indicateur de derniere position
				--lastElementPosition;
			}
		});
		
		constraint.gridx = 4;
		constraint.weightx = 0;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.fill = GridBagConstraints.VERTICAL;
		elementPanel.add(deleteElementButton, constraint); //ajout du bouton supprimer dans conteneurPrincipal
		
		return elementPanel;
	}
	
	
	/**
	 * Obtient la Collection des noms des elements
	 * @return La Collection des noms des elements
	 */
	public Collection<JTextField> getElements() {
		return elements;
	}
	
	/**
	 * Obtient la Collection des nombres lies aux elements
	 * @return La Collection des nombres lies aux elements
	 */
	public Collection<JTextField> getElementNumbers() {
		return elementNumbers;
	}
	
	/**
	 * Obtient le JTextField du titre de ce FreeTree
	 * @return Le JTextField du titre de ce FreeTree
	 */
	public JTextField getTitleTextField() {
		return titleTextField;
	}
	
	/**
	 * Obtient la JTextArea du commentaire de ce FreeTree
	 * @return La JTextArea du commentaire de ce FreeTree
	 */
	public JTextArea getTextAreaComment() {
		return textAreaComment;
	}
	
	/**
	 * Obtient le GridBagConstraints de ce FreeTree.<br>
	 * Utilise pour ajouter de nouveaux Component a la fin de ce FreeTree.
	 * @return Le GridBagConstraints de ce FreeTree
	 */
	public GridBagConstraints getConstraint() {
		return constraint;
	}
}
