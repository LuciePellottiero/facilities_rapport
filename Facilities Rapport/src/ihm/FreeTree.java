package ihm;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FreeTree {
	
	private int positionCounter;
	private int startElementPosition;
	private int lastElementPosition;
	private int numberElementAllowed;
	
	GridBagConstraints constraint;
	
	JComponent mainContainer;
	
	Collection<JTextField> elements;
	Collection<JTextField> elementNumbers;

	public FreeTree(int startPosition, int nbElementsAllowed, JComponent mainContainer) {
		
		this.positionCounter = startPosition;
		numberElementAllowed = nbElementsAllowed;
		
		this.constraint = new GridBagConstraints();
		this.constraint.gridx = 0;
		this.constraint.gridy = positionCounter;
		this.constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
		constraint.fill = GridBagConstraints.BOTH;
		
		this.mainContainer = mainContainer;
		
		elements = new LinkedList<JTextField>();
		elementNumbers = new LinkedList<JTextField>();
		
	    JLabel titreArboLibre = new JLabel("Arborescence libre"); //titre de la parte rapport du formulaire
		titreArboLibre.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    mainContainer.add(titreArboLibre, constraint); //ajout du titreRapportr dans conteneurPrincipal
	    
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		//titre
	    JLabel titre = new JLabel("Titre : "); //creation du label titre
	    constraint.gridy = ++positionCounter;
		mainContainer.add(titre, constraint); //ajout du label titre
		JTextField textFieldTitre = new JTextField(15); //creation de la zone de texte textFieldTitre2de taille 15
		titre.setLabelFor(textFieldTitre); //attribution de la zone de texte textFieldTitre au label titre
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		mainContainer.add(textFieldTitre, constraint); //ajout de la zone de texte textFieldTitre
	    
		lastElementPosition = ++positionCounter;
		
		addElement();
		
		this.startElementPosition = positionCounter;

		//bouton d'ajout d'element
		
		JButton ajoutElement = new JButton("+ Ajouter un élément");
		constraint.gridx = 1;
		positionCounter += numberElementAllowed;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		mainContainer.add(ajoutElement, constraint); //ajout du bouton ajoutElement
		
		ajoutElement.addActionListener(new ActionListener() {
	    	
		    public void actionPerformed(ActionEvent arg0) {	
		    	
		    	if (lastElementPosition >= startElementPosition + numberElementAllowed) {
					JOptionPane.showMessageDialog(mainContainer, 
		    				"Impossible d'ajouter un element supplémentaire dans la partie " + titreArboLibre.getText(), "Erreur", 
							JOptionPane.WARNING_MESSAGE);
					ajoutElement.setEnabled(false);
					return;
				}
				else {
					ajoutElement.setEnabled(true);
				}
		    		
		    	addElement();
				
				mainContainer.revalidate();
		    }
		});
		
		
		//commentaire
	    JLabel commentaire = new JLabel("Commentaire : "); //creation du label emailCl
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    mainContainer.add(commentaire, constraint); //ajout du label emailCl
	    
	    JTextArea textAreaCommentaire = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneCom = new JScrollPane(textAreaCommentaire);
	    commentaire.setLabelFor(textAreaCommentaire); //attribution de la zone de texte au label emailCl

		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    mainContainer.add(scrollPaneCom, constraint); //ajout de la zone de texte emailCl
	}
	
	public void addElement() {
		
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
    	
		//element
	    JLabel element = new JLabel("Elément : "); //creation du label dateDebut
		constraint.gridx = 0;
		constraint.gridy = ++lastElementPosition;
		constraint.gridwidth = 1;
		mainContainer.add(element, constraint); //ajout du label nbBPOuverts
		
	    JTextField textFieldElement = new JTextField(15); //initialisation de la zone de texte textFieldNbBPOuverts
	    element.setLabelFor(textFieldElement); //attribution de la zone de texte au label nbBPOuverts
		constraint.gridx = 1;
		constraint.gridwidth = 1;
		mainContainer.add(textFieldElement, constraint); //ajout de la zone de texte textFieldNbBPOuverts
		elements.add(textFieldElement);
		
		//nombre
	    JLabel nombre = new JLabel("Nombre : "); //creation du label dateDebut
		constraint.gridx = 2;
		constraint.gridwidth = GridBagConstraints.RELATIVE;
		mainContainer.add(nombre, constraint); //ajout du label dateDebut
		
	    JTextField textFieldNombre = new JTextField(2); //initialisation de la zone de texte dateFin formattee par le masque
	    nombre.setLabelFor(textFieldNombre); //attribution de la zone de texte au label dateFin
		constraint.gridx = 3;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		mainContainer.add(textFieldNombre, constraint); //ajout de la zone de texte dateFin
		elementNumbers.add(textFieldNombre);
	}
}
