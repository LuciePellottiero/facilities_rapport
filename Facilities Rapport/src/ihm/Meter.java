package ihm;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

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
	
	private static final String[] MONTH_CHOICE = {"Janvier", "F�vrier", "Mars", "Avril", "Mai", "Juin", "Juillet", 
			"Ao�t", "Septembre", "Octobre", "Novembre", "D�cembre"}; 
	
	private Collection<JComboBox<String>> typeComboBoxes;
	private Collection<JComboBox<String>> monthComboBoxes;
	private Collection<JTextField>        monthConsommations;
	private ArrayList<String>             monthUnits;

	public Meter(JComponent mainContainer, int yPosition) {
		
		super (new GridBagLayout());
		
		this.thisYPosition = yPosition;
		typeComboBoxes      = new LinkedList<JComboBox<String>>();
		monthComboBoxes     = new LinkedList<JComboBox<String>>();
		monthConsommations  = new LinkedList<JTextField>();
		monthUnits          = new ArrayList<String>();
		
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
		
		final String[] choixTypeCompteur = {"eau", "gaz", "�lectricit�", "�nergie"}; //differents choix de type de compteur
		comboBoxTypeCompteur = new JComboBox<String>(choixTypeCompteur);
		comboBoxTypeCompteur.setPreferredSize(new Dimension(100, 20));
		typeCompteur.setLabelFor(comboBoxTypeCompteur); //attribution de la zone de texte comboBoxTypeCompteur au label typeCompteur
		typeComboBoxes.add(comboBoxTypeCompteur);
		
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
	    	
		    private int counter = 0;

			public void actionPerformed(ActionEvent arg0) {	
		    	
		    	if (lastMonthPosition >= startMonthPosition + NUMBER_MONTH_ALLOWED) {
					JOptionPane.showMessageDialog(mainContainer, 
		    				"Impossible d'ajouter un mois suppl�mentaire dans la partie \"Compteurs\"", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
					ajoutMois.setEnabled(false);
					return;
				}
				else {
					ajoutMois.setEnabled(true);
				}
		    		
		    	JPanel elementPanel = addMois(counter++);
		    	
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
	
	@SuppressWarnings("unchecked")
	public JPanel addMois(int index) {
		
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
	    monthComboBoxes.add(comboBoxMoisCompteur);
		
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
		monthConsommations.add(textFieldConsommation);
		
		constraint.gridx = 1;
		monthPanel.add(textFieldConsommation, constraint); //ajout de la zone de texte textFieldConsommation
		
		//unite
	    final String[] choixUnite = {"m�", "m�", "kWh", "MWh"}; //differents choix de l'unite
	   //unite d'energie
	    final String[] energieChoice = {"kWh", "MWh"}; //differents choix de l'unite
		
	    JComponent unite;
		
		if (comboBoxTypeCompteur.getSelectedIndex() >= 3) {
			
			unite = new JComboBox<String>(energieChoice);
			
			String lastUnit = ((JComboBox<String>) unite).getSelectedItem().toString();
			monthUnits.add(lastUnit);
			
			((JComboBox<String>) unite).addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent arg0) {
					
					monthUnits.remove(index);
					String lastUnit = ((JComboBox<String>) unite).getSelectedItem().toString();
					monthUnits.add(lastUnit);
				}
			});
		}
		else {
			unite = new JLabel(choixUnite[comboBoxTypeCompteur.getSelectedIndex()]);
			monthUnits.add(((JLabel) unite).getText());
		}
		
	    constraint.gridx = 2;
	    constraint.weightx = 0;
		constraint.gridwidth = 1;
		monthPanel.add(unite, constraint); //ajout de la comboBoxUnite
		
		comboBoxTypeCompteur.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				
				monthPanel.remove(monthPanel.getComponentCount() - 1);
				monthUnits.remove(index);
				
				JComponent unite;
				if (comboBoxTypeCompteur.getSelectedIndex() >= 3) {
					unite = new JComboBox<String>(energieChoice);
					
					String lastUnit = ((JComboBox<String>) unite).getSelectedItem().toString();
					monthUnits.add(lastUnit);
					
					((JComboBox<String>) unite).addItemListener(new ItemListener() {
						
						@Override
						public void itemStateChanged(ItemEvent arg0) {
							
							monthUnits.remove(index);
							String lastUnit = ((JComboBox<String>) unite).getSelectedItem().toString();
							monthUnits.add(lastUnit);
						}
					});
				}
				else {
					unite = new JLabel(choixUnite[comboBoxTypeCompteur.getSelectedIndex()]);
					monthUnits.add(((JLabel) unite).getText());
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

	public Collection<JComboBox<String>> monthComboBoxes() {
		return monthComboBoxes;
	}

	public Collection<JTextField> monthConsommations() {
		return monthConsommations;
	}

	public ArrayList<String> monthUnits() {
		return monthUnits;
	}
}
