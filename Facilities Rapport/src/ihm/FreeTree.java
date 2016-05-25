package ihm;

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

public class FreeTree extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int NUMBER_ELEMENT_ALLOWED = 100;
	
	private static final String[] ADD_ELEMENT_TEXT = {"Ajouter un élément", "Remplissez la partie Titre", 
			"Remplissez tous les éléments" , "Limite d'élément atteinte"};
	
	private final GridBagConstraints constraint;
	
	private final Collection<JTextField> elements;
	private final Collection<JTextField> elementNumbers;
	private final JTextField titleTextField;
	private final JTextArea textAreaComment;
	
	final JButton addElement;

	private int lastElementPosition;
	
	private final ImageIcon addElementIcon;

	public FreeTree(){

		super (new GridBagLayout());
		
		final JPanel thisFreeTree = this;
		
		this.setBorder(BorderFactory.createTitledBorder("Arborescence libre"));
		
		int positionCounter = 0;
		
		constraint = new GridBagConstraints();
		constraint.gridx = 0;
		constraint.gridy = positionCounter;
		constraint.weightx = 1;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
		constraint.fill = GridBagConstraints.BOTH;
		
		elements = new LinkedList<JTextField>();
		elementNumbers = new LinkedList<JTextField>();
	    
	    constraint.insets = new Insets(20, 0, 3, 0); //marges autour de l'element
		//titre
	    final JLabel title = new JLabel("Titre : "); //creation du label titre
	    
	    constraint.gridx = 0;
	    constraint.gridy = ++positionCounter;
		this.add(title, constraint); //ajout du label titre
		
		titleTextField = new JTextField(15); //creation de la zone de texte textFieldTitre2de taille 15
		title.setLabelFor(titleTextField); //attribution de la zone de texte textFieldTitre au label titre
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		this.add(titleTextField, constraint); //ajout de la zone de texte textFieldTitre
	    
		lastElementPosition = ++positionCounter;
		
		final int startElementPosition = lastElementPosition;

		//bouton d'ajout d'element
		
		addElement = new JButton(ADD_ELEMENT_TEXT[1]);
		
		addElementIcon = new ImageIcon(Formulaire.ICONS_PATH + File.separator + Formulaire.ICONS_NAME[1]);
	    if (addElementIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
		    final int iconHeight = (int) (addElement.getPreferredSize().getHeight() - addElement.getPreferredSize().getHeight() / 3);
		    final int iconWidth  = addElementIcon.getIconWidth() / (addElementIcon.getIconHeight() / iconHeight);
		    
		    Image tmpImg = addElementIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    addElementIcon.setImage(tmpImg);
	    }
		
		addElement.setEnabled(false);
		
		constraint.gridx = 1;
		positionCounter += NUMBER_ELEMENT_ALLOWED;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		this.add(addElement, constraint); //ajout du bouton ajoutElement
		
		addElement.addActionListener(new ActionListener() {
	    	
		    public void actionPerformed(ActionEvent arg0) {	
		    	
		    	if (lastElementPosition >= startElementPosition + NUMBER_ELEMENT_ALLOWED) {
					JOptionPane.showMessageDialog(thisFreeTree, 
		    				"Impossible d'ajouter un element supplémentaire dans la partie " + titleTextField.getText(), "Erreur", 
							JOptionPane.WARNING_MESSAGE);
					
					addElement.setEnabled(false);
					addElement.setText(ADD_ELEMENT_TEXT[3]);
					addElement.setIcon(null);
					
					return;
				}

		    	addElement.setText(ADD_ELEMENT_TEXT[2]);
				addElement.setEnabled(false);
				addElement.setIcon(null);
		    	
		    	final JPanel elementPanel = addElement();
		    	
		    	constraint.insets = new Insets(3, 0, 0, 0); //marges autour de l'element
		    	constraint.gridx = 0;
				constraint.gridy = ++lastElementPosition;
				constraint.gridwidth = GridBagConstraints.REMAINDER;
		    	thisFreeTree.add(elementPanel, constraint);
		    	
		    	thisFreeTree.revalidate();
		    }
		});
		
		titleTextField.getDocument().addDocumentListener(new PersonnalDocumentListener() {
			
			@Override
			public void update(DocumentEvent arg0) {
				thisFreeTree.setBorder(BorderFactory.createTitledBorder(titleTextField.getText()));
				
				if (titleTextField.getText().equals("")) {
					addElement.setText(ADD_ELEMENT_TEXT[1]);
					addElement.setEnabled(false);
					addElement.setIcon(null);
				}
				else {
					addElement.setText(ADD_ELEMENT_TEXT[0]);
					addElement.setEnabled(true);
					addElement.setIcon(addElementIcon);
				}
			}
		});
		
		//commentaire
	    final JLabel commentaire = new JLabel("Commentaire : "); //creation du label emailCl
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(0, 0, 0, 0); //marges autour de l'element
	    this.add(commentaire, constraint); //ajout du label emailCl
	    
	    textAreaComment = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    final JScrollPane scrollPaneCom = new JScrollPane(textAreaComment);
	    commentaire.setLabelFor(textAreaComment); //attribution de la zone de texte au label emailCl

		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 0, 3, 0); //marges autour de l'element
		this.add(scrollPaneCom, constraint); //ajout de la zone de texte emailCl
	}
	
	public JPanel addElement() {
		
		final JPanel elementPanel = new JPanel();
		
		int positionCounter = 0;
		
		final GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridx = 0;
		constraint.weightx = 1;
		constraint.gridy = positionCounter;
		constraint.gridwidth = 1;
		constraint.insets = new Insets(0, 0, 0, 0); //marges autour de l'element
		constraint.fill = GridBagConstraints.BOTH;
    	
		//element
	    final JLabel element = new JLabel("Elément : "); //creation du label dateDebut
		
		elementPanel.add(element, constraint); //ajout du label nbBPOuverts
		
	    final JTextField textFieldElement = new JTextField(15); //initialisation de la zone de texte textFieldNbBPOuverts
	    elements.add(textFieldElement);
	    
	    element.setLabelFor(textFieldElement); //attribution de la zone de texte au label nbBPOuverts
		constraint.gridx = 1;
		elementPanel.add(textFieldElement, constraint); //ajout de la zone de texte textFieldNbBPOuverts	
		
		//nombre
	    final JLabel nombre = new JLabel("Nombre : "); //creation du label dateDebut
		
	    constraint.gridx = 2;
		elementPanel.add(nombre, constraint); //ajout du label dateDebut
		
	    final JTextField textFieldNombre = new JTextField(2); //initialisation de la zone de texte dateFin formattee par le masque
	    nombre.setLabelFor(textFieldNombre); //attribution de la zone de texte au label dateFin
	    elementNumbers.add(textFieldNombre);
	    
	    textFieldElement.getDocument().addDocumentListener(new PersonnalDocumentListener() {
			
			@Override
			public void update(DocumentEvent arg0) {
				if (textFieldElement.getText().equals("") || textFieldNombre.getText().equals("")) {
					addElement.setText(ADD_ELEMENT_TEXT[2]);
					addElement.setEnabled(false);
					addElement.setIcon(null);
				}
				else {
					addElement.setText(ADD_ELEMENT_TEXT[0]);
					addElement.setEnabled(true);
					addElement.setIcon(addElementIcon);
				}
			}
		});
	    
	    textFieldNombre.getDocument().addDocumentListener(new PersonnalDocumentListener() {
			
			@Override
			public void update(DocumentEvent arg0) {
				if (textFieldElement.getText().equals("") || textFieldNombre.getText().equals("")) {
					addElement.setText(ADD_ELEMENT_TEXT[2]);
					addElement.setEnabled(false);
					addElement.setIcon(null);
				}
				else {
					addElement.setText(ADD_ELEMENT_TEXT[0]);
					addElement.setEnabled(true);
					addElement.setIcon(addElementIcon);
				}
			}
		});
	    
	    constraint.gridx = 3;
		constraint.gridwidth = 2;
		elementPanel.add(textFieldNombre, constraint); //ajout de la zone de texte dateFin
			
		final JPanel thisPanel = this;
		
		final JButton deleteElementButton = new JButton();
		
		ImageIcon deleteElementIcon = new ImageIcon(Formulaire.ICONS_PATH + File.separator + Formulaire.ICONS_NAME[4]);
	    if (deleteElementIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
		    final int iconHeight = (int) (deleteElementButton.getPreferredSize().getHeight() - deleteElementButton.getPreferredSize().getHeight() / 3);
		    final int iconWidth  = deleteElementIcon.getIconWidth() / (deleteElementIcon.getIconHeight() / iconHeight);
		    
		    Image tmpImg = deleteElementIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    deleteElementIcon.setImage(tmpImg);
	    }
	    deleteElementButton.setIcon(deleteElementIcon);
		deleteElementButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				thisPanel.remove(elementPanel);
				
				elements.remove(textFieldElement);
				elementNumbers.remove(textFieldNombre);
				
				--lastElementPosition;
				
				if (addElement.getText().equals(ADD_ELEMENT_TEXT[2]) || 
						addElement.getText().equals(ADD_ELEMENT_TEXT[3])) {
					
					addElement.setEnabled(true);
					addElement.setText(ADD_ELEMENT_TEXT[0]);
					addElement.setIcon(addElementIcon);
					
				}
				
				thisPanel.revalidate();
			}
		});
		
		constraint.gridx = 4;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		elementPanel.add(deleteElementButton, constraint); //ajout du bouton supprimer dans conteneurPrincipal
		
		return elementPanel;
	}
	
	public boolean isFilled () {
		return addElement.isEnabled();
	}
	
	public Collection<JTextField> elements() {
		return elements;
	}
	
	public Collection<JTextField> elementNumbers() {
		return elementNumbers;
	}
	
	public JTextField titleTextField() {
		return titleTextField;
	}
	
	public JTextArea textAreaComment() {
		return textAreaComment;
	}
	
	public GridBagConstraints constraint() {
		return constraint;
	}
}
