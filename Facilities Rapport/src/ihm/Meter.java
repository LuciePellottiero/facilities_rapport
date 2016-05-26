package ihm;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import utilities.OperationUtilities;

public class Meter extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int lastMonthPosition;
	
	private final GridBagConstraints constraint;
	
	private final JComboBox<String> comboBoxTypeCompteur;
	private final JTextArea textAreaCommentaire;
	
	private final JButton ajoutMois;
	
	private static final int NUMBER_MONTH_ALLOWED = 30;
	
	private static final String[] MONTH_CHOICE = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", 
			"Août", "Septembre", "Octobre", "Novembre", "Décembre"}; 
	private static final String[] ADD_BUTTON_TEXT = {"Ajouter un mois", "Impossible d'ajouter un mois supplémentaire",
			"Remplissez les mois précedents"};
	
	private final Collection<JComboBox<String>> monthComboBoxes;
	private final Collection<JTextField>        monthConsumptions;
	private final ArrayList<String>             monthUnits;

	private final ImageIcon addElementIcon;

	public Meter() {
		
		super (new GridBagLayout());
		
		monthComboBoxes   = new LinkedList<JComboBox<String>>();
		monthConsumptions = new LinkedList<JTextField>();
		monthUnits        = new ArrayList<String>();
		
		int positionCounter = 0;
		final Meter thisMeter = this;
		
		constraint = new GridBagConstraints();
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
		
		ajoutMois = new JButton(ADD_BUTTON_TEXT[0]);
		addElementIcon = new ImageIcon(Formulaire.ICONS_PATH + File.separator + Formulaire.ICONS_NAME[1]);
	    if (addElementIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
		    final int iconHeight = (int) (ajoutMois.getPreferredSize().getHeight() - ajoutMois.getPreferredSize().getHeight() / 3);
		    final int iconWidth  = addElementIcon.getIconWidth() / (addElementIcon.getIconHeight() / iconHeight);
		    
		    Image tmpImg = addElementIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    addElementIcon.setImage(tmpImg);
	    }
	    ajoutMois.setIcon(addElementIcon);
	    
		positionCounter += NUMBER_MONTH_ALLOWED;
		
		constraint.gridx = 1;	
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		this.add(ajoutMois, constraint); //ajout du bouton ajoutCompteur
		
		ajoutMois.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {	
		    		
		    	JPanel elementPanel = addMois(lastMonthPosition - startMonthPosition);
		    	
		    	constraint.insets = new Insets(3, 0, 0, 0); //marges autour de l'element
		    	constraint.gridx = 0;
				constraint.gridy = ++lastMonthPosition;
				constraint.gridwidth = GridBagConstraints.REMAINDER;
				thisMeter.add(elementPanel, constraint);
				
				ajoutMois.setEnabled(false);
				ajoutMois.setText(ADD_BUTTON_TEXT[2]);
				ajoutMois.setIcon(null);
				
				if (lastMonthPosition >= startMonthPosition + NUMBER_MONTH_ALLOWED) {
					
					ajoutMois.setEnabled(false);
					ajoutMois.setText(ADD_BUTTON_TEXT[1]);
					ajoutMois.setIcon(null);
				}
				
				thisMeter.revalidate();
		    }
		});
		
		
		//commentaire
	    JLabel commentaire = new JLabel("Commentaire : "); //creation du label emailCl
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    this.add(commentaire, constraint); //ajout du label emailCl
	    
	    textAreaCommentaire = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneCom = new JScrollPane(textAreaCommentaire);
	    commentaire.setLabelFor(textAreaCommentaire); //attribution de la zone de texte au label emailCl

		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    this.add(scrollPaneCom, constraint); //ajout de la zone de texte
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
	    
	    final JComboBox<String> comboBoxMoisCompteur = new JComboBox<String>(MONTH_CHOICE);
	    comboBoxMoisCompteur.setPreferredSize(new Dimension(100, 20));
	    moisCompteur.setLabelFor(comboBoxMoisCompteur); //attribution de la moisCompteur au label moisCompteur
	    monthComboBoxes.add(comboBoxMoisCompteur);
		
	    constraint.gridx = 1;	
		monthPanel.add(comboBoxMoisCompteur, constraint); //ajout de la comboBoxMoisCompteur
		
		final JButton deleteElementButton = new JButton();
		
		final ImageIcon deleteElementIcon = new ImageIcon(Formulaire.ICONS_PATH + File.separator + Formulaire.ICONS_NAME[4]);
	    if (deleteElementIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
		    final int iconWidth = (int) (deleteElementButton.getPreferredSize().getWidth() - deleteElementButton.getPreferredSize().getWidth() / 2);
		    final int iconHeight  = deleteElementIcon.getIconHeight() / (deleteElementIcon.getIconWidth() / iconWidth);
		    
		    Image tmpImg = deleteElementIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    deleteElementIcon.setImage(tmpImg);
	    }
	    deleteElementButton.setIcon(deleteElementIcon);
		
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
		monthConsumptions.add(textFieldConsommation);
		
		textFieldConsommation.getDocument().addDocumentListener(new PersonnalDocumentListener() {
			
			@Override
			public void update(DocumentEvent arg0) {
				if (!textFieldConsommation.getText().equals("")) {
					ajoutMois.setEnabled(true);
					ajoutMois.setText(ADD_BUTTON_TEXT[0]);
					ajoutMois.setIcon(addElementIcon);
				}
				else {
					ajoutMois.setEnabled(false);
					ajoutMois.setText(ADD_BUTTON_TEXT[2]);
					ajoutMois.setIcon(null);
				}
			}
		});
		
		constraint.gridx = 1;
		monthPanel.add(textFieldConsommation, constraint); //ajout de la zone de texte textFieldConsommation
		
		//unite
	    final String[] choixUnite = {"m³", "kWh", "MWh"}; //differents choix de l'unite
	    //unite de gaz
	    final String[] gazChoice = {"m³", "kWh", "MWh"}; //differents choix de l'unite
	    //unite d'energie
	    final String[] energieChoice = {"kWh", "MWh"}; //differents choix de l'unite
		
	    JComponent unit;
		
		if (comboBoxTypeCompteur.getSelectedIndex() >= 1) {
			
			if (comboBoxTypeCompteur.getSelectedIndex() >= 2) {
				unit = new JComboBox<String>(energieChoice);
			}
			else {
				unit = new JComboBox<String>(gazChoice);
			}
			
			JComboBox<String> lastUnit = ((JComboBox<String>) unit);
			monthUnits.add(lastUnit.getSelectedItem().toString());
			
			((JComboBox<String>) unit).addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					monthUnits.remove(lastUnit);
					String lastUnit = ((JComboBox<String>) unit).getSelectedItem().toString();
					monthUnits.add(lastUnit);
					
				}
			});
		}
		else {
			unit = new JLabel(choixUnite[comboBoxTypeCompteur.getSelectedIndex()]);
			monthUnits.add(((JLabel) unit).getText());
		}
		
	    constraint.gridx = 2;
	    constraint.weightx = 0;
		constraint.gridwidth = 1;
		monthPanel.add(unit, constraint); //ajout de la comboBoxUnite
		
		comboBoxTypeCompteur.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				monthPanel.remove(monthPanel.getComponentCount() - 1);
				monthUnits.remove(index);
				
				JComponent unit;
				if (comboBoxTypeCompteur.getSelectedIndex() >= 1) {
					
					if (comboBoxTypeCompteur.getSelectedIndex() >= 2) {
						unit = new JComboBox<String>(energieChoice);
					}
					else {
						unit = new JComboBox<String>(gazChoice);
					}
					
					String lastUnit = ((JComboBox<String>) unit).getSelectedItem().toString();
					monthUnits.add(lastUnit);
					
					((JComboBox<String>) unit).addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							monthUnits.remove(lastUnit);
							String lastUnit = ((JComboBox<String>) unit).getSelectedItem().toString();
							monthUnits.add(lastUnit);
							
						}
					});
				}
				else {
					unit = new JLabel(choixUnite[comboBoxTypeCompteur.getSelectedIndex()]);
					monthUnits.add(((JLabel) unit).getText());
				}
				
				constraint.gridx = 2;
				constraint.weightx = 0;
				constraint.gridwidth = 1;
				monthPanel.add(unit, constraint); //ajout de la comboBoxUnite
				
				monthPanel.revalidate();
			}
		});
		
		JPanel thisMeter = this;
		
		deleteElementButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				final GridBagLayout meterLayout = (GridBagLayout)thisMeter.getLayout();
				for (int i = OperationUtilities.getComponentIndex(monthPanel); i < thisMeter.getComponentCount(); ++i) {
					
					Component currentComponent = thisMeter.getComponent(i);
					GridBagConstraints thisComponentConstraint = meterLayout.getConstraints(currentComponent);
					--thisComponentConstraint.gridy;
					meterLayout.setConstraints(currentComponent, thisComponentConstraint);
				}
				
				--lastMonthPosition;
				
				ajoutMois.setEnabled(true);
				ajoutMois.setText(ADD_BUTTON_TEXT[0]);
				ajoutMois.setIcon(addElementIcon);
				
				thisMeter.remove(monthPanel);
				thisMeter.revalidate();
			}
		});
				
		return monthPanel;
	}
	
	public JComboBox<String> comboBoxTypeCompteur() {
		return comboBoxTypeCompteur;
	}
	
	public JTextArea textAreaCommentaire() {
		return textAreaCommentaire;
	}

	public Collection<JComboBox<String>> monthComboBoxes() {
		return monthComboBoxes;
	}

	public Collection<JTextField> monthConsumptions() {
		return monthConsumptions;
	}

	public ArrayList<String> monthUnits() {
		return monthUnits;
	}
	
	public GridBagConstraints constraint() {
		return this.constraint;
	}
}
