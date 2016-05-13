package ihm;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FreeTree extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int positionCounter;
	private int startElementPosition;
	private int lastElementPosition;
	private static final int NUMBER_ELEMENT_ALLOWED = 100;
	
	GridBagConstraints constraint;
	
	Collection<JTextField> elements;
	Collection<JTextField> elementNumbers;

	public FreeTree(){

		super (new GridBagLayout());
		
		this.positionCounter = 0;
		
		this.constraint = new GridBagConstraints();
		this.constraint.gridx = 0;
		this.constraint.gridy = positionCounter;
		this.constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
		constraint.fill = GridBagConstraints.BOTH;
		
		elements = new LinkedList<JTextField>();
		elementNumbers = new LinkedList<JTextField>();
	    
	    constraint.insets = new Insets(20, 7, 3, 7); //marges autour de l'element
		//titre
	    JLabel titre = new JLabel("Titre : "); //creation du label titre
	    
	    constraint.gridx = 0;
	    constraint.gridy = ++positionCounter;
		this.add(titre, constraint); //ajout du label titre
		
		JTextField textFieldTitre = new JTextField(15); //creation de la zone de texte textFieldTitre2de taille 15
		titre.setLabelFor(textFieldTitre); //attribution de la zone de texte textFieldTitre au label titre
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		this.add(textFieldTitre, constraint); //ajout de la zone de texte textFieldTitre
	    
		lastElementPosition = ++positionCounter;
		
		addElement();
		
		this.startElementPosition = positionCounter;

		//bouton d'ajout d'element
		
		JButton ajoutElement = new JButton("+ Ajouter un �l�ment");
		constraint.gridx = 1;
		positionCounter += NUMBER_ELEMENT_ALLOWED;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		this.add(ajoutElement, constraint); //ajout du bouton ajoutElement
		
		JPanel thisPanel = this;
		
		ajoutElement.addActionListener(new ActionListener() {
	    	
		    public void actionPerformed(ActionEvent arg0) {	
		    	
		    	if (lastElementPosition >= startElementPosition + NUMBER_ELEMENT_ALLOWED) {
					JOptionPane.showMessageDialog(thisPanel, 
		    				"Impossible d'ajouter un element suppl�mentaire dans la partie " + textFieldTitre.getText(), "Erreur", 
							JOptionPane.WARNING_MESSAGE);
					ajoutElement.setEnabled(false);
					return;
				}
				else {
					ajoutElement.setEnabled(true);
				}
		    		
		    	addElement();
				
		    	thisPanel.revalidate();
		    }
		});
		
		
		//commentaire
	    JLabel commentaire = new JLabel("Commentaire : "); //creation du label emailCl
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(0, 7, 0, 7); //marges autour de l'element
	    this.add(commentaire, constraint); //ajout du label emailCl
	    
	    JTextArea textAreaCommentaire = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneCom = new JScrollPane(textAreaCommentaire);
	    commentaire.setLabelFor(textAreaCommentaire); //attribution de la zone de texte au label emailCl

		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		this.add(scrollPaneCom, constraint); //ajout de la zone de texte emailCl
	    
	    // Bouton supprimer
	    JButton delete = new JButton("- Supprimer");
	    
	    constraint.gridx = 1;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		this.add(delete, constraint); //ajout du bouton supprimer dans conteneurPrincipal
	}
	
	public void addElement() {
		
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
    	
		//element
	    JLabel element = new JLabel("El�ment : "); //creation du label dateDebut
		constraint.gridx = 0;
		constraint.gridy = ++lastElementPosition;
		constraint.gridwidth = 1;
		this.add(element, constraint); //ajout du label nbBPOuverts
		
	    JTextField textFieldElement = new JTextField(15); //initialisation de la zone de texte textFieldNbBPOuverts
	    element.setLabelFor(textFieldElement); //attribution de la zone de texte au label nbBPOuverts
		constraint.gridx = 1;
		constraint.gridwidth = 1;
		this.add(textFieldElement, constraint); //ajout de la zone de texte textFieldNbBPOuverts
		elements.add(textFieldElement);
		
		//nombre
	    JLabel nombre = new JLabel("Nombre : "); //creation du label dateDebut
		constraint.gridx = 2;
		constraint.gridwidth = GridBagConstraints.RELATIVE;
		this.add(nombre, constraint); //ajout du label dateDebut
		
	    JTextField textFieldNombre = new JTextField(2); //initialisation de la zone de texte dateFin formattee par le masque
	    nombre.setLabelFor(textFieldNombre); //attribution de la zone de texte au label dateFin
		constraint.gridx = 3;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		this.add(textFieldNombre, constraint); //ajout de la zone de texte dateFin
		elementNumbers.add(textFieldNombre);
	}
}
