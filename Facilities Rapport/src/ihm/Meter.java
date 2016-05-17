package ihm;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Meter {
	
	private int positionCounter;
	private int startElementPosition;
	private int lastElementPosition;
	private int numberElementAllowed;
	/**
	 * Declaration de la comboBox des types de compteur
	 */
	private JComboBox<String>   comboBoxTypeCompteur;
	/**
	 * Declaration de la comboBox des mois pour les compteur
	 */
	private JComboBox<String>   comboBoxMoisCompteur;
	/**
	 * Declaration de la comboBox des unites
	 */
	private JComboBox<String>   comboBoxUnite;
	
	private final String[] MONTH_CHOICE = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", 
			"Août", "Septembre", "Octobre", "Novembre", "Décembre"}; 
	
	GridBagConstraints constraint;
	
	JComponent mainContainer;
	
	Collection<JTextField> elements;
	Collection<JTextField> elementNumbers;

	public Meter(int startPosition, int nbElementsAllowed, JComponent mainContainer) {
		
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
		
		JLabel titreCompteurs = new JLabel("Compteurs"); //titre de la partie compteurs du formulaire
		titreCompteurs.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreCompteurs
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
		mainContainer.add(titreCompteurs, constraint); //ajout du titreCompteurs dans conteneurPrincipal
		
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    
	    //type de compteur
	    JLabel typeCompteur = new JLabel("Type du compteur : "); //creation du label typeCompteur
		constraint.gridy = ++positionCounter;
		mainContainer.add(typeCompteur, constraint); //ajout du label 
		String[] choixTypeCompteur = {"eau", "gaz", "électricité", "énergie"}; //differents choix de type de compteur
		comboBoxTypeCompteur = new JComboBox<String>(choixTypeCompteur);
		comboBoxTypeCompteur.setPreferredSize(new Dimension(100, 20));
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		typeCompteur.setLabelFor(comboBoxTypeCompteur); //attribution de la zone de texte comboBoxTypeCompteur au label typeCompteur
		mainContainer.add(comboBoxTypeCompteur, constraint); //ajout de la zone de texte comboBoxTypeCompteur
		
		lastElementPosition = ++positionCounter;
		
		addMois();
		
		this.startElementPosition = positionCounter;

		//bouton d'ajout de mois
		
		JButton ajoutMois = new JButton("+ Ajouter un mois");
		constraint.gridx = 1;
		positionCounter += numberElementAllowed;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		mainContainer.add(ajoutMois, constraint); //ajout du bouton ajoutCompteur
		
		ajoutMois.addActionListener(new ActionListener() {
	    	
		    public void actionPerformed(ActionEvent arg0) {	
		    	
		    	if (lastElementPosition >= startElementPosition + numberElementAllowed) {
					JOptionPane.showMessageDialog(mainContainer, 
		    				"Impossible d'ajouter un element supplémentaire dans la partie " + titreCompteurs.getText(), "Erreur", 
							JOptionPane.WARNING_MESSAGE);
					ajoutMois.setEnabled(false);
					return;
				}
				else {
					ajoutMois.setEnabled(true);
				}
		    		
		    	addMois();
				
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
	
	public void addMois() {
		
		constraint.insets = new Insets(7, 7, 3, 7); //marges autour de l'element
		
		//mois
	    JLabel moisCompteur = new JLabel("Mois : ");
	    constraint.gridx = 0;
	    constraint.gridy = ++lastElementPosition;
	    mainContainer.add(moisCompteur, constraint); //ajout du label moisCompteur
	    comboBoxMoisCompteur = new JComboBox<String>(MONTH_CHOICE);
	    comboBoxMoisCompteur.setPreferredSize(new Dimension(100, 20));
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		moisCompteur.setLabelFor(comboBoxMoisCompteur); //attribution de la moisCompteur au label moisCompteur
		mainContainer.add(comboBoxMoisCompteur, constraint); //ajout de la comboBoxMoisCompteur
		
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		
		//consommation
	    JLabel consommation = new JLabel("Consommation : ");
	    constraint.gridx = 0;
	    constraint.gridy = ++lastElementPosition;
		constraint.gridwidth = 1;
		mainContainer.add(consommation, constraint); //ajout du label consommation
		JTextField textFieldConsommation = new JTextField(15); //creation de la zone de texte textFieldConsommation de taille 15
		consommation.setLabelFor(textFieldConsommation); //attribution de la zone de texte textFieldConsommation au label consommation
		constraint.gridx = 1;
		mainContainer.add(textFieldConsommation, constraint); //ajout de la zone de texte textFieldConsommation
		
		//unite
	    String[] choixUnite = {"m³", "kWh", "MWh"}; //differents choix de l'unite
	    comboBoxUnite = new JComboBox<String>(choixUnite);
	    comboBoxUnite.setPreferredSize(new Dimension(60, 20));
		constraint.gridx = 2;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		mainContainer.add(comboBoxUnite, constraint); //ajout de la comboBoxUnite
	}
}
