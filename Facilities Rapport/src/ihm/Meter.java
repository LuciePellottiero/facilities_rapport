package ihm;

import java.awt.Component;
import java.awt.Container;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import utilities.OperationUtilities;

/**
 * JPanel de compteur
 * @author Lucie PELLOTTIERO
 *
 */
public class Meter extends JPanel{
	
	/**
	 * Numero de serialization par defaut
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * La position du dernier mois
	 */
	private int lastMonthPosition;
	
	/**
	 * Le GridBagConstraints de ce Meter
	 */
	private final GridBagConstraints constraint;
	
	/**
	 * Le JComboBox pour le type de compteur<br>
	 * Chaque type possède une liste d'energie associe
	 * - Eau -> m³<br>
	 * - Gaz -> m³, KWh, MWh<br>
	 * - Electricite -> KWh, MWh<br>
	 * - Energie -> KWh, MWh
	 */
	private final JComboBox<String> meterType;
	/**
	 * Le JTextArea du commentaire de ce Meter
	 */
	private final JTextArea comment;
	
	/**
	 * Le JButton d'ajout de mois pour ce Meter
	 */
	private final JButton addMonth;
	
	/**
	 * Le nombre maximum de mois que l'on peut ajouter
	 */
	private static final int NUMBER_MONTH_ALLOWED = Integer.MAX_VALUE;
	
	/**
	 * Tous les mois possibles
	 */
	private static final String[] MONTH_CHOICE = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", 
			"Août", "Septembre", "Octobre", "Novembre", "Décembre"}; 
	/**
	 * Tous les textes possibles que peut prendre le JButton addMonth
	 */
	private static final String[] ADD_BUTTON_TEXT = {"Ajouter un mois", "Impossible d'ajouter un mois supplémentaire",
			"Remplissez les mois précedents"};
	
	/**
	 * Lorsque l'on ne peut determiner l'unite ou qu'il n'y en qu'une possible, alors on prend celle de cet array
	 * correspondant a l'index de la selection du JComboBox meterType
	 */
	public static final String[] UNIT_CHOICE = {"m³", "m³", "kWh", "MWh"}; //differents choix de l'unite
	
	/**
	 * On stock un lien vers les JComboBox du choix des mois
	 */
	private final Collection<JComboBox<String>> monthComboBoxes;
	/**
	 * On stock un lien vers les JTextField de la consommation des mois
	 */
	private final Collection<JTextField>        monthConsumptions;
	/**
	 * Etant donne que le l'objet de l'unite peut changer, on stock qu'un lien vers la String la representant
	 */
	private final ArrayList<String>             monthUnits;
	
	/**
	 * L'ImageIcon representant l'icon d'ajout (un +)
	 */
	private final ImageIcon addElementIcon;

	/**
	 * Le constructeur du JPanel du compteur
	 */
	public Meter() {
		
		// On construit le JPanel avec un GridBagLayout
		super (new GridBagLayout());
		
		// Initialisation des Collection
		monthComboBoxes   = new LinkedList<JComboBox<String>>();
		monthConsumptions = new LinkedList<JTextField>();
		monthUnits        = new ArrayList<String>();
		
		// Initialisation du compteur de position des elements dans le JPanel du compteur
		int positionCounter = 0;
		
		// Initialisation du GridBagConstraints
		constraint = new GridBagConstraints();
		constraint.gridx = 0;
		constraint.weightx = 1;
		constraint.gridy = positionCounter;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		constraint.fill = GridBagConstraints.BOTH;

	    // Type de compteur
	    JLabel typeCompteur = new JLabel("Type du compteur : "); //creation du label typeCompteur
		
	    constraint.gridy = ++positionCounter;
	    // Ajout du label 
		this.add(typeCompteur, constraint);
		
		// Differents choix de type de compteur
		final String[] choixTypeCompteur = {"eau", "gaz", "électricité", "énergie"}; 
		
		meterType = new JComboBox<String>(choixTypeCompteur);
		meterType.setPreferredSize(new Dimension(100, 20));
		// Attribution de la zone de texte comboBoxTypeCompteur au label typeCompteur
		typeCompteur.setLabelFor(meterType);
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		// Ajout de la zone de texte comboBoxTypeCompteur
		this.add(meterType, constraint);
		
		lastMonthPosition = 0;

		// Creation du JPanel qui contiendra tous les mois du compteur
		final JPanel monthsPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints monthConstraint = new GridBagConstraints();
		monthConstraint.gridx = 0;
		monthConstraint.gridy = lastMonthPosition;
		monthConstraint.weightx = 1;
		monthConstraint.gridwidth = GridBagConstraints.REMAINDER;
		monthConstraint.insets = new Insets(3, 0, 1, 0);
		monthConstraint.fill = GridBagConstraints.BOTH;
		
		constraint.gridx = 0;	
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		this.add(monthsPanel, constraint);
		
		// Bouton d'ajout de mois
			
		addMonth = new JButton(ADD_BUTTON_TEXT[0]);
		// Tentative d'ajout de l'ImageIcon au JButton addMonth
        // Tentative d'obtiention de l'image
		addElementIcon = new ImageIcon(Form.ICONS_PATH + File.separator + Form.ICONS_NAME[1]);
		// Si l'image n'a pas d'erreur
	    if (addElementIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
	    	// Definition de la hauteur de l'image en fonction de la taille du JButton
		    final int iconHeight = (int) (addMonth.getPreferredSize().getHeight() - addMonth.getPreferredSize().getHeight() / 3);
		    // Definition de la largeur de l'image en fonction de la taille du JButton
		    final int iconWidth  = addElementIcon.getIconWidth() / (addElementIcon.getIconHeight() / iconHeight);
		    
		    // Redimensionne l'image selon la largeur et la hauteur precedente
		    Image tmpImg = addElementIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    addElementIcon.setImage(tmpImg);
	    }
	    addMonth.setIcon(addElementIcon);
		
		constraint.gridx = 1;	
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		this.add(addMonth, constraint);
		
		addMonth.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				// Si on a atteint le nombre de mois maximum, on empeche la creation d'un nouveau mois
				if (lastMonthPosition >= NUMBER_MONTH_ALLOWED) {
					JOptionPane.showMessageDialog(monthsPanel, 
		    				"Impossible d'ajouter un mois supplémentaire dans la partie compteur.", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
				
					addMonth.setEnabled(false);
					addMonth.setText(ADD_BUTTON_TEXT[1]);
					addMonth.setIcon(null);
					
					return;
				}
		    	
				// Creation d'un nouveau mois
		    	JPanel elementPanel = addMonth(lastMonthPosition);
		    	
		    	monthConstraint.gridy = ++lastMonthPosition;
				monthsPanel.add(elementPanel, monthConstraint);
				
				// Desactivation du JButton car il faut d'abord remplir les mois precedents avant d'ajouter un element
			    // (cette contrainte n'est pas forte et est contournable, mais cela ne pose pas de problème)
				addMonth.setEnabled(false);
				addMonth.setText(ADD_BUTTON_TEXT[2]);
				addMonth.setIcon(null);
				
				// Si le dernier mois est le dernier que l'on peut ajouter,
		    	// alors on desactive le JButton
				// Etant donne la taille maximale, cette verification est superflue par rapport a sa frequence de realisation
				/*if (lastMonthPosition >= NUMBER_MONTH_ALLOWED) {
					
					ajoutMois.setEnabled(false);
					ajoutMois.setText(ADD_BUTTON_TEXT[1]);
					ajoutMois.setIcon(null);
				}*/
				
				monthsPanel.revalidate();
		    }
		});
		
		
		// Commentaire
	    JLabel commentaire = new JLabel("Commentaire : ");
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(10, 7, 0, 7);
	    this.add(commentaire, constraint);
	    
	    comment = new JTextArea(4, 15);
	    JScrollPane scrollPaneCom = new JScrollPane(comment);
	    commentaire.setLabelFor(comment);

		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7);
	    this.add(scrollPaneCom, constraint);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Creer un nouveau JPanel qui contient un nouveau mois
	 * @param index l'index auquel on ajoute le mois.<br>
	 * Important pour la gestion des unitees de consommation
	 * @return Le JPanel du mois
	 */
	public JPanel addMonth(int index) {
		
		// Creation du nouveau JPanel
		JPanel monthPanel = new JPanel(new GridBagLayout());
		
		int positionCounter = 0;
		
		// Creation de son JBagConstraints
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridx = 0;
		constraint.weightx = 1;
		constraint.gridy = positionCounter;
		constraint.gridwidth = 1;
		constraint.insets = new Insets(7, 7, 3, 7);
		constraint.fill = GridBagConstraints.BOTH;
		
		// Mois
	    JLabel meterMonth = new JLabel("Mois : ");
	    
	   // Ajout du label moisCompteur
	    monthPanel.add(meterMonth, constraint);
	    
	    final JComboBox<String> meterMonthCombo = new JComboBox<String>(MONTH_CHOICE);
	    meterMonthCombo.setPreferredSize(new Dimension(100, 20));
	    // Attribution de la moisCompteur au label meterMonth
	    meterMonth.setLabelFor(meterMonthCombo); 
	    monthComboBoxes.add(meterMonthCombo);
		
	    constraint.gridx = 1;
	   // Ajout de la meterMonthCombo
		monthPanel.add(meterMonthCombo, constraint); 
		
		final JButton deleteElementButton = new JButton();
		
		// Tentative d'ajout de l'ImageIcon au JButton addMonth
        // Tentative d'obtiention de l'image
		final ImageIcon deleteElementIcon = new ImageIcon(Form.ICONS_PATH + File.separator + Form.ICONS_NAME[4]);
		// Si l'image n'a pas d'erreur
	    if (deleteElementIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
	    	// Definition de la hauteur de l'image en fonction de la taille du JButton
		    final int iconWidth = (int) (deleteElementButton.getPreferredSize().getWidth() - deleteElementButton.getPreferredSize().getWidth() / 2);
		    // Definition de la largeur de l'image en fonction de la taille du JButton
		    final int iconHeight  = deleteElementIcon.getIconHeight() / (deleteElementIcon.getIconWidth() / iconWidth);
		    
		    // Redimensionne l'image selon la largeur et la hauteur precedente
		    Image tmpImg = deleteElementIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    deleteElementIcon.setImage(tmpImg);
	    }
	    deleteElementButton.setIcon(deleteElementIcon);
		
		constraint.gridx = 3;
		constraint.weightx = 0;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.gridheight = GridBagConstraints.REMAINDER;
		monthPanel.add(deleteElementButton, constraint);
		
		// Consommation
	    JLabel consuption = new JLabel("Consommation : ");
	    
	    constraint.insets = new Insets(0, 7, 3, 7);
	    constraint.gridx = 0;
	    constraint.weightx = 1;
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du label consommation
		monthPanel.add(consuption, constraint);
		
		// Creation de la zone de texte consuptionField de taille 15
		JTextField consuptionField = new JTextField(15);
		// Attribution de la zone de texte consuptionField au label consommation
		consuption.setLabelFor(consuptionField);
		monthConsumptions.add(consuptionField);
		
		// Si on efface la consommation, alors on indique qu'il faut la remplir remplir
	    // avant d'ajouter de nouveaux mois
	    // (cette contrainte n'est pas forte et est contournable, mais cela ne pose pas de problème)
		consuptionField.getDocument().addDocumentListener(new PersonnalDocumentListener() {
			
			@Override
			public void update(DocumentEvent arg0) {
				// Si la consommation a ete efface, on desactive l'ajout de mois
				if (!consuptionField.getText().equals("")) {
					addMonth.setEnabled(true);
					addMonth.setText(ADD_BUTTON_TEXT[0]);
					addMonth.setIcon(addElementIcon);
				}
				// Sinon, on reactive l'ajout de mois
				else {
					addMonth.setEnabled(false);
					addMonth.setText(ADD_BUTTON_TEXT[2]);
					addMonth.setIcon(null);
				}
			}
		});
		
		constraint.gridx = 1;
		// Ajout de la zone de texte consuptionField
		monthPanel.add(consuptionField, constraint); 
		
		constraint.gridx = 2;
		constraint.weightx = 0;
		constraint.gridwidth = 1;
		// Ajout d'une unite temporaire
		monthPanel.add(new JLabel(), constraint);
		
	    // Unites de gaz
	    final String[] gazChoice = {"m³", "kWh", "MWh"};
	    // Unites d'energie
	    final String[] energieChoice = {"kWh", "MWh"};
		
	    // Lorsque l'on change le type du compteur, il faut mettre a jour le choix d'unite
	    meterType.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// S'il le mois ne fait pas encore partie de la Collection (s'il vient d'etre cree)
				if (index < monthUnits.size()) {
					monthPanel.remove(monthPanel.getComponentCount() - 1);
					monthUnits.remove(index);
				}
				
				// On ne sait pas a l'avance le type de l'unite
				JComponent unit;
				
				// Si l'unite est un choix
				if (meterType.getSelectedIndex() >= 1) {
					
					// Si l'unite est en energie
					if (meterType.getSelectedIndex() >= 2) {
						unit = new JComboBox<String>(energieChoice);
					}
					// Sinon c'est en gaz
					else {
						unit = new JComboBox<String>(gazChoice);
					}
					
					// Obtention de l'unite selectionne (car on sait que c'est un choix)
					String lastUnit = ((JComboBox<String>) unit).getSelectedItem().toString();
					// Ajout a la Collection des unites
					monthUnits.add(lastUnit);
					
					// Si on change l'unite selectionnee
					((JComboBox<String>) unit).addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							
							// Obtention de la nouvelle unite selectionnee
							String lastUnit = ((JComboBox<String>) unit).getSelectedItem().toString();
							// Remplacement de l'ancienne unite par la nouvelle dans la Collection
							monthUnits.set(index, lastUnit);
						}
					});
				}
				// Si l'unit n'est pas un choix
				else {
					// Impose l'unite par un JLabel
					unit = new JLabel(UNIT_CHOICE[meterType.getSelectedIndex()]);
					monthUnits.add(((JLabel) unit).getText());
				}
				
				constraint.gridx = 2;
				constraint.weightx = 0;
				constraint.gridwidth = 1;
				// Ajout de l'unite
				monthPanel.add(unit, constraint);
				
				monthPanel.revalidate();
			}
		});

	    // Force l'ActionListener a effectuer son action pour initialiser l'unite
		meterType.setSelectedIndex(meterType.getSelectedIndex());
		
		// Action a effectuer lors de la suppression de ce mois
		deleteElementButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Obtention du parent du JPanel de ce mois
				final Container parent = monthPanel.getParent();
				
				// S'il y a un parent (normalement il devrait y en avoir un mais bon, on ne sait jamais...)
				if (parent != null) {
					// Reorganise tous les mois suivant celui que l'on veut supprimer en decrementant leur position vertical dans
					// le GridBagConstraints
					// Obtention du GridBagLayout du parent
					final GridBagLayout meterLayout = (GridBagLayout)parent.getLayout();
					// Iteration sur les Component du parent après celui que l'on veut effacer
					for (int i = OperationUtilities.getComponentIndex(monthPanel); i < parent.getComponentCount(); ++i) {
						
						final Component currentComponent = parent.getComponent(i);
						// Obtention du GridBagConstraints du Component courrant
						final GridBagConstraints thisComponentConstraint = meterLayout.getConstraints(currentComponent);
						// Decremente sa position verticale
						--thisComponentConstraint.gridy;
						// Application des modifications
						meterLayout.setConstraints(currentComponent, thisComponentConstraint);
					}
					
					// Decremente la position du dernier mois
					--lastMonthPosition;
					
					// Enfin, on enleve du parent le JPanel du mois
					parent.remove(monthPanel);
					parent.revalidate();
				}
				
				// Il ne faut pas oublier de supprimer les informations des Collection
				monthComboBoxes.remove(meterMonthCombo);
				monthConsumptions.remove(consuptionField);
				
				// Reactivation du bouton d'ajout de mois
				addMonth.setEnabled(true);
				addMonth.setText(ADD_BUTTON_TEXT[0]);
				addMonth.setIcon(addElementIcon);
			}
		});
				
		return monthPanel;
	}
	
	/**
	 * Obtient le JComboBox du type du compteur
	 * @return Le JComboBox du type du compteur
	 */
	public JComboBox<String> getComboBoxTypeCompteur() {
		return meterType;
	}
	
	/**
	 * Obtient le JTextArea du commmentaire du compteur
	 * @return Le JTextArea du commmentaire du compteur
	 */
	public JTextArea getTextAreaCommentaire() {
		return comment;
	}

	/**
	 * Obtient les JComboBox des mois du compteur
	 * @return Le JComboBox des mois du compteur
	 */
	public Collection<JComboBox<String>> getMonthComboBoxes() {
		return monthComboBoxes;
	}

	/**
	 * Obtient les JTextField des consommations des mois du compteur
	 * @return Les JTextField des consommations des mois du compteur
	 */
	public Collection<JTextField> getMonthConsumptions() {
		return monthConsumptions;
	}

	/**
	 * Obtient les unties de consommation des mois du compteur
	 * @return Les unties de consommation des mois du compteur
	 */
	public ArrayList<String> getMonthUnits() {
		return monthUnits;
	}
	
	/**
	 * Obtient le GridBagConstraint de ce compteur.<br>
	 * Utilise dans le Formulaire pour ajouter un bouton de suppression a la fin du JPanel du compteur
	 * @return Le GridBagConstraint de ce compteur
	 */
	public GridBagConstraints getConstraint() {
		return this.constraint;
	}
}
