package ihm;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Meter extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int lastMonthPosition;
	@SuppressWarnings("unused")
	private int thisYPosition;
	
	private JComboBox<String> comboBoxTypeCompteur;
	
	private static final int NUMBER_MONTH_ALLOWED = 100;
	
	private static final String[] MONTH_CHOICE = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", 
			"Août", "Septembre", "Octobre", "Novembre", "Décembre"}; 

	public Meter(JComponent mainContainer, int yPosition) {
		
		super (new GridBagLayout());
		
		this.thisYPosition = yPosition;
		
		int positionCounter = 0;
		JPanel thisPanel = this;
		
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridx = 0;
		constraint.weightx = 1;
		constraint.gridy = positionCounter;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		constraint.fill = GridBagConstraints.BOTH;

	    //type de compteur
	    JLabel typeCompteur = new JLabel("Type du compteur : "); //creation du label typeCompteur
		
	    constraint.gridy = ++positionCounter;
		this.add(typeCompteur, constraint); //ajout du label 
		
		final String[] choixTypeCompteur = {"eau", "gaz", "électricité", "énergie"}; //differents choix de type de compteur
		comboBoxTypeCompteur = new JComboBox<String>(choixTypeCompteur);
		comboBoxTypeCompteur.setPreferredSize(new Dimension(100, 20));
		typeCompteur.setLabelFor(comboBoxTypeCompteur); //attribution de la zone de texte comboBoxTypeCompteur au label typeCompteur
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;	
		this.add(comboBoxTypeCompteur, constraint); //ajout de la zone de texte comboBoxTypeCompteur
		
		lastMonthPosition = ++positionCounter;
		
		final int startMonthPosition = lastMonthPosition;

		//bouton d'ajout de mois
		
		JButton ajoutMois = new JButton("+ Ajouter un mois");
		
		positionCounter += NUMBER_MONTH_ALLOWED;
		
		constraint.gridx = 1;	
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		this.add(ajoutMois, constraint); //ajout du bouton ajoutCompteur
		
		ajoutMois.addActionListener(new ActionListener() {
	    	
		    public void actionPerformed(ActionEvent arg0) {	
		    	
		    	if (lastMonthPosition >= startMonthPosition + NUMBER_MONTH_ALLOWED) {
					JOptionPane.showMessageDialog(mainContainer, 
		    				"Impossible d'ajouter un mois supplémentaire dans la partie \"Compteurs\"", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
					ajoutMois.setEnabled(false);
					return;
				}
				else {
					ajoutMois.setEnabled(true);
				}
		    		
		    	JPanel elementPanel = addMois();
		    	
		    	constraint.insets = new Insets(3, 0, 0, 0); //marges autour de l'element
		    	constraint.gridx = 0;
				constraint.gridy = ++lastMonthPosition;
				constraint.gridwidth = GridBagConstraints.REMAINDER;
				thisPanel.add(elementPanel, constraint);
				
				thisPanel.revalidate();
		    }
		});
		
		
		//commentaire
	    JLabel commentaire = new JLabel("Commentaire : "); //creation du label emailCl
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    this.add(commentaire, constraint); //ajout du label emailCl
	    
	    JTextArea textAreaCommentaire = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneCom = new JScrollPane(textAreaCommentaire);
	    commentaire.setLabelFor(textAreaCommentaire); //attribution de la zone de texte au label emailCl

		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    this.add(scrollPaneCom, constraint); //ajout de la zone de texte
	    
	    // Bouton supprimer
	    JButton delete = new JButton("- Supprimer");
	    
	    delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				--thisYPosition;
				
				mainContainer.remove(thisPanel);
				mainContainer.revalidate();
			}
		});
	    
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		constraint.insets = new Insets(0, 0, 3, 0); //marges autour de l'element
		this.add(delete, constraint); //ajout du bouton supprimer dans conteneurPrincipal
	}
	
	public JPanel addMois() {
		
		JPanel monthPanel = new JPanel(new GridBagLayout());
		
		int positionCounter = 0;
		
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridx = 0;
		constraint.weightx = 1;
		constraint.gridy = positionCounter;
		constraint.gridwidth = 1;
		constraint.insets = new Insets(7, 7, 3, 7); //marges autour de l'element
		constraint.fill = GridBagConstraints.BOTH;
		
		//mois
	    JLabel moisCompteur = new JLabel("Mois : ");
	    
	    monthPanel.add(moisCompteur, constraint); //ajout du label moisCompteur
	    
	    JComboBox<String> comboBoxMoisCompteur = new JComboBox<String>(MONTH_CHOICE);
	    comboBoxMoisCompteur.setPreferredSize(new Dimension(100, 20));
	    moisCompteur.setLabelFor(comboBoxMoisCompteur); //attribution de la moisCompteur au label moisCompteur
		
	    constraint.gridx = 1;	
		monthPanel.add(comboBoxMoisCompteur, constraint); //ajout de la comboBoxMoisCompteur
		
		JButton deleteElementButton = new JButton("X");
		
		constraint.gridx = 3;
		constraint.weightx = 0;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.gridheight = GridBagConstraints.REMAINDER;
		monthPanel.add(deleteElementButton, constraint); //ajout du bouton supprimer dans conteneurPrincipal
		
		//consommation
	    JLabel consommation = new JLabel("Consommation : ");
	    
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    constraint.gridx = 0;
	    constraint.weightx = 1;
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		monthPanel.add(consommation, constraint); //ajout du label consommation
		
		JTextField textFieldConsommation = new JTextField(15); //creation de la zone de texte textFieldConsommation de taille 15
		consommation.setLabelFor(textFieldConsommation); //attribution de la zone de texte textFieldConsommation au label consommation
		
		constraint.gridx = 1;
		monthPanel.add(textFieldConsommation, constraint); //ajout de la zone de texte textFieldConsommation
		
		//unite
	    final String[] choixUnite = {"m³", "m³", "kWh", "MWh"}; //differents choix de l'unite
	   //unite d'energie
	    final String[] energieChoice = {"kWh", "MWh"}; //differents choix de l'unite
		
	    JComponent unite;
		
		if (comboBoxTypeCompteur.getSelectedIndex() >= 3) {
			unite = new JComboBox<String>(energieChoice);
		}
		else {
			unite = new JLabel(choixUnite[comboBoxTypeCompteur.getSelectedIndex()]);
		}
		
	    constraint.gridx = 2;
	    constraint.weightx = 0;
		constraint.gridwidth = 1;
		monthPanel.add(unite, constraint); //ajout de la comboBoxUnite
		
		comboBoxTypeCompteur.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				
				monthPanel.remove(monthPanel.getComponentCount() - 1);
				
				JComponent unite;
				if (comboBoxTypeCompteur.getSelectedIndex() >= 3) {
					unite = new JComboBox<String>(energieChoice);
				}
				else {
					unite = new JLabel(choixUnite[comboBoxTypeCompteur.getSelectedIndex()]);
				}
				
				constraint.gridx = 2;
				constraint.weightx = 0;
				constraint.gridwidth = 1;
				monthPanel.add(unite, constraint); //ajout de la comboBoxUnite
				
				monthPanel.revalidate();
			}
		});
		
		JPanel thisPanel = this;
		
		deleteElementButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				thisPanel.remove(monthPanel);
				
				thisPanel.revalidate();
			}
		});
				
		return monthPanel;
	}
}
