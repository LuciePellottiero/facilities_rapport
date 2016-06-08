package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import dataHandler.DefaultDataHandler;
import dataHandler.IDataHandler;
import documentHandler.CreateReportDocument;
import documentHandler.writeStrategies.IWriteStrategie;
import utilities.OperationUtilities;
import utilities.chartGenerator.DefaultChartGenerator;
import utilities.chartGenerator.IChartGenerator;

/**
 * JFrame du formulaire
 * @author Lucie PELLOTTIERO
 *
 */
public class Form extends JFrame{
	
	/**
	 * Numero de serialisation genere par defaut
	 */
	private static final long serialVersionUID = 5604247470620479438L;
	
	/**
	 * JButton d'ajout de mois de bon preventif
	 */
	private final JButton addPreventiveVoucherMonth;
	/**
	 * JButton d'ajout de mois de demande d'intervention
	 */
	private final JButton addInterventionDemandMonth;
	
	/**
	 * Le chemin relatif vers le repertoir des icons
	 */
	public final static String ICONS_PATH = "Facilities Rapport" + File.separator + 
			"Files" + File.separator + "Icons";
	
	/**
	 * Le nom des fichiers des icons
	 */
	public final static String[] ICONS_NAME = {"vinciFacilitiesIcon.png", "addIcon.png", "addPictureIcon.png", 
			"pdfIcon.png", "removeIcon.png", "folderIcon.png", "validateIcon.png"};
	
	/**
	 * Collection des pourcentage des bons preventifs par domaine
	 */
	private Collection<JFormattedTextField> preventiveVoucherPourcentageFields;
	/**
	 * Collection des JCheckBox des bons preventifs par domaine
	 */
	private Collection<JCheckBox> preventiveVoucherDomains;
	
	/**
	 * L'ImageIcon du logo du client
	 */
	private ImageIcon logoIcon;
	
	/**
	 * String utilisees pour chaque JLable de mois de bons preventifs
	 */
	private final static String[] PREVENTIVE_VOUCHER_MONTH_LABELS = {"Mois : ", "Nombre de bons préventifs ouverts : ", 
			"Nombre de bons préventifs fermés : ", "Commentaire : "};
    
	/**
	 * liste des differents mois
	 */
	private final static String[] MONTH_CHOICE = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", 
			"Août", "Septembre", "Octobre", "Novembre", "Décembre"}; 
	
	/**
	 * Texts que peuvent prendre les JButton d'ajout de mois
	 */
	private final static String[] ADD_MONTH_BUTTON_TEXT = {"Ajouter un mois", "Impossible d'ajouter un mois supplémentaire",
			"Remplissez les mois précedents"};
	
	/**
	 * Texts que peuvent prendre les JButton d'ajour d'arborescence libre
	 */
	private final static String[] ADD_FREE_TREE_TEXT = {"Ajouter arborescence libre", 
			"Impossible d'ajouter une arborescence libre supplémentaire"};
	
	/**
	 * Texts que peut prendre le JButton d'ajout de compteur
	 */
	private final static String[] ADD_METER_TEXT = {"Ajouter un compteur", "Impossible d'ajouter un compteur supplémentaire"};
	
	/**
	 * Nombre de mois de bon preventive que l'on peut ajouter
	 */
	private static final int NUMBER_PREVENTIVE_MONTH_ALLOWED = Integer.MAX_VALUE;

	/**
	 * Nombre d'arborescence libre que l'on peut ajouter
	 */
	private static final int NUMBER_FREE_TREE_ALLOWED = Integer.MAX_VALUE;
	
	/**
	 * Nombre de compteur que l'on peut ajouter
	 */
	private static final int NUMBER_METER_ALLOWED = Integer.MAX_VALUE;
	
	/**
	 * Nombre de mois de demande d'intervention que l'on peut ajouter
	 */
	private static final int NUMBER_INTERVENTION_DEMAND_MONTH_ALLOWED = Integer.MAX_VALUE;
	
	/**
	 * La position vertical a laquelle on doit ajouter un Component au ContentPane
	 */
	private int positionCounter;
	/**
	 * La position verticale du dernier mois de bon preventif
	 */
	private int preventiveVoucherLastMonthPosition;
	/**
	 * La position verticale du dernier mois de demande d'intervention
	 */
	private int interventionDemandLastMonthPosition;
	/**
	 * La position verticale de la derniere arborescence libre 1
	 */
	private int freeTrees1LastPosition;
	
	/**
	 * Le nombre de mois que l'on doit ajouter (pour les bons preventifs et les demandes d'intervention)<br>
	 * selon le type du rapport
	 */
	private int monthNumber;
	
	/**
	 * Les donnees que l'on peut sauvegarder
	 */
	private final Collection<JTextComponent> datasToSerialize;
	
	/**
	 * Construteur principal du Form
	 */
	public Form() {
		// Appel du constructeur de JFrame
		super();
	    
		// Tentative d'ajout de l'ImageIcon au Form
	    final ImageIcon vinciIcion = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[0]);
	    // Si l'image n'a pas d'erreur
	    if (vinciIcion.getImageLoadStatus() != MediaTracker.ERRORED) {
	    	this.setIconImage(vinciIcion.getImage());
	    }
	    
	    //creation du JPanel principal
		final JPanel contentPane = new JPanel(); 
		
		// Lien vers ce Form
		final Form mainFrame = this;
		
		// Compteur de position vertical du GridBagConstraints
		positionCounter = 0;
		
		// Titre fenetre
		this.setTitle("Facilities Rapport");
		
		// Taille fenetre
		this.setSize(700, 600);
		// Pour detruir la fenetre lorsqu'on la ferme
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Position de la fenetre au centre de l'ecran
	    this.setLocationRelativeTo(null);
	    // Couleur de fond de la fenetre
	    contentPane.setBackground(Color.white);
	    contentPane.setLayout(new BorderLayout()); 
	    
	    // Titre formulaire
	    final JLabel titreFacilitiesRapport = new JLabel("Facilities Rapport", SwingConstants.CENTER);
	    // Police + taille du titre formulaire
		titreFacilitiesRapport.setFont(new Font("Arial",Font.BOLD,18)); 
		
		// JPanel qui sera au centre avec un BorderLayout
		final JPanel container = new JPanel();
		// JPanel qui contiendra tous les inputs
		final JPanel mainContainer = new JPanel(new GridBagLayout());
		// GridBagConstraints qui sera applique pour tous ce que l'on ajoute dans le mainContainer
		final GridBagConstraints constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.BOTH;   
		
		// Marges autour des titres
		final Insets titleInset = new Insets(20, 0, 5, 0);
		
		// La barre de menu a mettre sur le Form
		final JMenuBar menuBar = new JMenuBar();
		// Le menu qui concerne le rapoort (pas pdf)
		final JMenu menu = new JMenu("Rapport");
		menu.getAccessibleContext().setAccessibleDescription("Operations sur ce rapport");
		
		// Creation d'une nouvelle fenetre Form
		final JMenuItem newReport = new JMenuItem("Nouveau rapport");
		newReport.getAccessibleContext().setAccessibleDescription("Créer un nouveau rapport vierge prêt à être éditer");
		newReport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {	
				// Delegation a l'EDT (Event Dispatch Thread) pour la gestion des threads (le formulaire + la barre de progression)
				SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		                try {
		                	// Creation du formulaire
							new Form();
						} 
		                catch (Exception e) {
							e.printStackTrace();
							// Affichage de l'erreur
							JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur", 
									JOptionPane.WARNING_MESSAGE);
						}
		            }
		        });
			}
		});
		menu.add(newReport);
		
		menu.addSeparator();
		
		// Permet d'enregistrer les donnees dans la partie redacteur
		final JMenuItem save = new JMenuItem("Enregistrer partie rédacteur");
		save.getAccessibleContext().setAccessibleDescription("Sauvegarder la partie rédacteur de ce rapport pour pouvoir être réeditée plus tard");
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Creation d'un JFileChooser pour choisir la destination (sera un .xml)
				final JFileChooser saveDestination = new JFileChooser();
				saveDestination.setCurrentDirectory(new File ("./"));
				final FileNameExtensionFilter filter = new FileNameExtensionFilter(
			            "xml file", "xml");
				saveDestination.setFileFilter(filter);
				// Ouverture du JFileChooser et recuperation de la reponse de l'utilisateur
				final int returnVal = saveDestination.showSaveDialog(mainFrame);
			    
				// Si la reponse est une validation
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						
						File selectedDestination = saveDestination.getSelectedFile();
						// Si le fichier existe deja
						if (selectedDestination.exists()) {
							// Demande a l'utilisateur si on doit l'ecraser
							final int dialogResult = JOptionPane.showConfirmDialog (contentPane, 
	    							"Le fichier " + selectedDestination.getName() + " existe déjà, voulez-vous l'écraser?",
	    							"Erreur", JOptionPane.YES_NO_OPTION);
							// Si l'utilisateur refuse, alors on empeche la creation du fichier
	    					if(dialogResult == JOptionPane.NO_OPTION){
	    						return;
	    					}
						}
						else {
							// Si le fichier n'existe pas, alors on ajoute l'extension au nom fournis par l'utilisateur
							selectedDestination = new File (saveDestination.getSelectedFile() + ".xml");
						}
						// Sauvegarde des donnees
						xmlSerialization(selectedDestination);
					}
					catch (Exception ex) {
						JOptionPane.showMessageDialog(mainFrame, 
			    				"Le répertoire choisi est invalide", "Erreur", 
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		menu.add(save);
		
		// Chargement des donnees pour la parties redacteur
		final JMenuItem load = new JMenuItem("Charger partie rédacteur");
		load.getAccessibleContext().setAccessibleDescription("Charger la partie rédacteur d'un rapport enregistrée ulterieurement");
		load.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Creation d'un JFileChooser pour choisir le fichier des donnees
				final JFileChooser loadPath = new JFileChooser();
				loadPath.setCurrentDirectory(new File ("./"));
				final FileNameExtensionFilter filter = new FileNameExtensionFilter(
			            "xml file", "xml");
				loadPath.setFileFilter(filter);
				// Ouverture du JFileChooser et recuperation de la reponse de l'utilisateur
				final int returnVal = loadPath.showOpenDialog(mainFrame);
			    
				// Si la reponse est une validation
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						xmlUnserialization(loadPath.getSelectedFile());
					}
					catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(mainFrame, 
			    				"Le répertoire ou le fichier choisi est invalide", "Erreur", 
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		menu.add(load);
		
		menu.addSeparator();
		
		// Permet de fermer la fenetre
		final JMenuItem exit = new JMenuItem("Quitter");
		exit.getAccessibleContext().setAccessibleDescription("Quitter l'application");
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});
		menu.add(exit);
		
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		
		// Initialisation de la Collection des donnees a sauvegarder
		datasToSerialize = new LinkedList<JTextComponent>();
		/*-----------------------------------------formulaire redacteur--------------------------------------------*/
	    
		// Titre de la partie redacteur du formulaire
		final JLabel writerTitle = new JLabel("Redacteur");
		// Police + taille titreRedacteur
		writerTitle.setFont(new Font("Arial",Font.BOLD,14));
		
		constraint.gridx = 0;
		constraint.gridy = positionCounter;
		// Marges autour de l'element
		constraint.insets = titleInset;
		// Ajout du titreRedacteur dans conteneurPrincipal
	    mainContainer.add(writerTitle, constraint); 
		
		// Nom
	    // Creation du label nom
		final JLabel writerName = new JLabel("Nom : ");
		
		// Marges autour de l'element
		constraint.insets = new Insets(0, 7, 3, 7);
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du label
		mainContainer.add(writerName, constraint);
		
		// Creation de la zone de texte du nom de taille 15
		final JTextField writerNameField = new JTextField(15);
		// Attribution du JLabel du nom a la zone de texte
		writerName.setLabelFor(writerNameField);
		datasToSerialize.add(writerNameField);
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		// Ajout de la zone de texte du nom
		mainContainer.add(writerNameField, constraint);
		
		// Adresse 
		// Creation du JLabel adr
		final JLabel writerAdr = new JLabel("Adresse : ");
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du JLabel adr
	    mainContainer.add(writerAdr, constraint);
	    
	    // Creation de la zone de texte adr de taille 3 en hauteur et 15 en largeur
	    final JTextArea writerAdrArea = new JTextArea(3, 15);
	    datasToSerialize.add(writerAdrArea);
	    
	    final JScrollPane scrollPaneAdr = new JScrollPane(writerAdrArea);
	    // Attribution de la zone de texte au label adr
	    writerAdr.setLabelFor(writerAdrArea);
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		// Ajout de la zone de texte adr
	    mainContainer.add(scrollPaneAdr, constraint);
	    
	    // Telephone
	    // Creation du JLabel tel
	    final JLabel writerPhone = new JLabel("Téléphone : ");
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du JLabel tel
	    mainContainer.add(writerPhone, constraint);
	    
	    // Initialisation du JFormattedTextField
	    JFormattedTextField writerPhoneField = null;
	   
	    try{
	    	// Masque pour le format du numero de telephone
			final MaskFormatter phoneMask  = new MaskFormatter("## ## ## ## ##");
			// Iinitialisation de la zone de texte tel formattee par le masque
			writerPhoneField = new JFormattedTextField(phoneMask);
			// Attribution d'une valeur par defaut
			writerPhoneField.setValue("00 00 00 00 00");
			datasToSerialize.add(writerPhoneField);
	    }
	    catch(ParseException e){
	    	// Exception
			e.printStackTrace();
		}
	    // Attribution du JLabel au JTextField
	    writerPhone.setLabelFor(writerPhoneField);
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		// Ajout de la zone de texte tel
		mainContainer.add(writerPhoneField, constraint);
		
		// Obtention de la version final pour la generation du rapport
		final JFormattedTextField finalWriterPhoneField = writerPhoneField;
	   
	    // Email
		// Creation du JLabel email
	    final JLabel writerEmail = new JLabel("Email : ");
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du JLabel email
	    mainContainer.add(writerEmail, constraint);
	    
	    // Creation de la zone de texte email de taille 15
	    final JTextField writerEmailField = new JTextField(15);
	    // Attribution de la zone de texte au label email
	    writerEmail.setLabelFor(writerEmailField);
	    datasToSerialize.add(writerEmailField);
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		// Ajout de la zone de texte email au panel redacteur 
		mainContainer.add(writerEmailField, constraint);
	    
	    // Nom charge d'affaire
		// Creation du JLabel nom charge d'affaire
	    final JLabel writerResponsibleName = new JLabel("Nom du chargé d'affaire : ");
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du JLabel nom du charge d'affaire
	    mainContainer.add(writerResponsibleName, constraint);
	   
	    // Creation de la zone de texte nom du charge d'affaire de taille 15
	    final JTextField writerResponsibleNameField = new JTextField(15);
	    // Attribution du JLabel au JTextField
	    writerResponsibleName.setLabelFor(writerResponsibleNameField); 
	    datasToSerialize.add(writerResponsibleNameField);
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		// Ajout de la zone de texte nom du charge d'affaire
	    mainContainer.add(writerResponsibleNameField, constraint);
        
		/*-------------------------------------------formulaire client-------------------------------------------------*/
		
	    // Titre de la partie client du formulaire
		final JLabel customerTitle = new JLabel("Client");
		// Police + taille titre Client
		customerTitle.setFont(new Font("Arial",Font.BOLD,14)); 
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		// Marges autour de l'element
		constraint.insets = titleInset;
		// Ajout du titreClient dans le panel conteneurPrincpal
		mainContainer.add(customerTitle, constraint);
	
		// Nom
		// Creation du JLabel du nom du site
		final JLabel siteName = new JLabel("Nom du site : ");
		
		// Marges autour de l'element
		constraint.insets = new Insets(0, 7, 3, 7);
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du JLabel du nom du site
		mainContainer.add(siteName, constraint);
		
		// Creation de la zone de texte du nom du site de taille 15
		final JTextField siteNameField = new JTextField(15);
		// Attribution du JLabel au JTextField
		siteName.setLabelFor(siteNameField); 
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		// Ajout de la zone de texte du nom du site
		mainContainer.add(siteNameField, constraint);
		
		// Code
		// Creation du JLabel code
	    final JLabel code = new JLabel("Code : ");
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du JLabel code
	    mainContainer.add(code, constraint);
	    
	    // Creation de la zone de texte code
	    final JTextField codeField = new JTextField(15);
	    // Attribution du JLabel au JTextField
	    code.setLabelFor(codeField);
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		// Ajout de la zone de texte code
		mainContainer.add(codeField, constraint);
		
		// Adresse client
		// Creation du JLlabel adrCl
		final JLabel customerAdr = new JLabel("Adresse : ");
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du label adrCl
	    mainContainer.add(customerAdr, constraint);
	    
	    // Creation de la zone de texte adrCl de taille 3 en hauteur et 15 en largeur
	    final JTextArea customerAdrArea = new JTextArea(3, 15);
	   
	    final JScrollPane customerAdrAreaScollPane = new JScrollPane(customerAdrArea);
	    // Attribution du JLabel au JTextField
	    customerAdr.setLabelFor(customerAdrArea);
	    
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;  
		// Ajout de la zone de texte adrCl
		mainContainer.add(customerAdrAreaScollPane, constraint);
	    
	    // Code postal
		// Creation du label codePostal
	    final JLabel postalCode = new JLabel("Code postal : ");
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du JLabel codePostal
	    mainContainer.add(postalCode, constraint);
	    
	    // Initialisation du JFromattedTextField
	    JFormattedTextField postalCodeField = null;
	    try{
	    	// Masque pour le format du code postal
			MaskFormatter postalCodeMask  = new MaskFormatter("## ###");
			// Initialisation de la zone de texte codePostal formattee par le masque
			postalCodeField = new JFormattedTextField(postalCodeMask);
			// Ajout d'une valeur par defaut
			postalCodeField.setValue("00 000");
	    }
	    catch(ParseException e){
	    	// Exception
	    	e.printStackTrace();
		}
	    
	    // Attribution du JLabel au JFormattedTextField
	    postalCode.setLabelFor(postalCodeField);
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		// Ajout de la zone de texte codePostal
		mainContainer.add(postalCodeField, constraint);
		
		// Creation du JFormattedTextField final pour la generation du rapport
		final JFormattedTextField finalPostalCodeField = postalCodeField;
		
		// Ville
		// Creation du JLabel ville
		final JLabel city = new JLabel("Ville : ");
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du JLabel ville
		mainContainer.add(city, constraint);
		
		// Creation de la zone de texte ville de taille 15
		final JTextField cityField = new JTextField(15);
		// Attribution du JLabel au JTextField
		city.setLabelFor(cityField);
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		// Ajout de la zone de texte ville
		mainContainer.add(cityField, constraint);
		
		// Nom du client
		// Creation du JLabel du nom du client
	    final JLabel customerName = new JLabel("Nom du client : ");
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du JLabel du nom du client
	    mainContainer.add(customerName, constraint);
	   
	    // Creation de la zone de texte du nom du client de taille 15
	    final JTextField customerNameField = new JTextField(15);
	    // Attribution du JLabel au JTextField
	    customerName.setLabelFor(customerNameField);
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		// Ajout de la zone de texte du nom du client
		mainContainer.add(customerNameField, constraint);
	    
	    // Telephone client
		// Creation du JLabel du telephone du client
	    final JLabel customerPhone = new JLabel("Téléphone : ");
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du JLabel du telephone du client
	    mainContainer.add(customerPhone, constraint);
	   
	    JFormattedTextField textFieldTelCl = null;
	   
	    try{
	    	// Masque pour le format du numero de telephone
			MaskFormatter maskTelCl  = new MaskFormatter("## ## ## ## ##");
			// Initialisation de la zone de texte du telephone du client formattee par le masque
			textFieldTelCl = new JFormattedTextField(maskTelCl);
			// Attribution d'une valeur par defaut
			textFieldTelCl.setValue("00 00 00 00 00");
	    }
	    catch(ParseException e){
	    	// Exception
			e.printStackTrace();
		}
	    
	    // Attribution du JLabel au JFormattedTextField
	    customerPhone.setLabelFor(textFieldTelCl);
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		// Ajout de la zone de texte du telephone du client
		mainContainer.add(textFieldTelCl, constraint);
		
		// Version final pour la generation du rapport
		final JFormattedTextField finalTextFieldTelCl = textFieldTelCl;
	   
	    // Email client
		// Creation du JLabel de l'email du client
	    final JLabel customerMail = new JLabel("Email : ");
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		// Ajout du JLabel du mail du client
	    mainContainer.add(customerMail, constraint);
	   
	    // Creation de la zone de texte du mail du client de taille 15
	    final JTextField customerMailField = new JTextField(15);
	    // Attribution du JLabel au JTextField
	    customerMail.setLabelFor(customerMailField);
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
	    // Ajout de la zone de texte du mail du client
		mainContainer.add(customerMailField, constraint);
	    
		// Logo du client
	    final JLabel customerLogo = new JLabel ("Logo client : ");
	    
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		mainContainer.add(customerLogo, constraint);
	    
		// L'image du logo
		final JLabel customerLogoFile = new JLabel();
		// Ajustement de la taille
		customerLogoFile.setPreferredSize(new Dimension(90, (int) customerLogoFile.getPreferredSize().getHeight()));
		// Ajustement du text pour quand il y aura l'image
		// Innutilise car le text est invisible (il est comme cache derriere un autre element)
		customerLogoFile.setHorizontalTextPosition(JLabel.CENTER);
		customerLogoFile.setVerticalTextPosition(JLabel.BOTTOM);
		
		constraint.gridx = 1;
		constraint.weightx = 1;
		mainContainer.add(customerLogoFile, constraint);
		
		// Initialisation des variables des image
		int iconHeight;
	    int iconWidth;
	    Image tmpImg;
		
	    // Le JButton pour supprimer l'image selectionnee
		final JButton deleteLogo = new JButton();
		
		// Tentative d'ajout de l'ImageIcon au JButton deleteLogo
		// Tentative d'obtiention de l'image
		final ImageIcon removePictureIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[4]);
		// Si l'image n'a pas d'erreur
	    if (removePictureIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
	    	// Definition de la largeur de l'image en fonction de la taille du JButton
	    	iconWidth = (int) (deleteLogo.getPreferredSize().getWidth() - deleteLogo.getPreferredSize().getWidth() / 2);
	    	// Definition de la hauteur de l'image en fonction de la taille du JButton
		    iconHeight  = removePictureIcon.getIconHeight() / (removePictureIcon.getIconWidth() / iconWidth);
		    
		    // Redimensionne l'image selon la largeur et la hauteur precedente
		    tmpImg = removePictureIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    removePictureIcon.setImage(tmpImg);
		    deleteLogo.setIcon(removePictureIcon);
	    }
		
		deleteLogo.setVisible(false);
		// Suppression du logo
		deleteLogo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				logoIcon = null;
				//customerLogoFile.setText("");
				customerLogoFile.setIcon(null);
				deleteLogo.setVisible(false);
				
				mainContainer.repaint();
			}
		});
		
		constraint.gridx = 2;
		constraint.weightx = 0;
		constraint.fill = GridBagConstraints.HORIZONTAL;
		mainContainer.add(deleteLogo, constraint);
		
		// Creation d'un filtre par extension de nom de fichier pour image
	    final FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "Fichiers images", ImageIO.getReaderFileSuffixes());

	    // Creation d'un JFileChooser pour choisir le logo du client
	    final JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileFilter(filter);
	    // Ajout d'un ImagePreview pour avoir un apercu d'une image lorsque selectionnee
	    fileChooser.setAccessory(new ImagePreview(fileChooser));
	    
	    // Creation du JButton d'ajout de logo
	    final JButton addLogo = new JButton("Choisir logo...");
	    
	    final ImageIcon addPictureIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[2]);
	    if (addPictureIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
		    iconHeight = (int) (addLogo.getPreferredSize().getHeight() - addLogo.getPreferredSize().getHeight() / 3);
		    iconWidth  = addPictureIcon.getIconWidth() / (addPictureIcon.getIconHeight() / iconHeight);
		    
		    tmpImg = addPictureIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    addPictureIcon.setImage(tmpImg);
		    addLogo.setIcon(addPictureIcon);
	    }
	    addLogo.addActionListener(new  ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				int returnVal = fileChooser.showOpenDialog(mainFrame);
			    
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						File file = fileChooser.getSelectedFile();
						Image tmpImage = ImageIO.read(file);  
				        logoIcon = new ImageIcon(tmpImage);
				        
				        if (logoIcon != null) {
				            if (logoIcon.getIconWidth() > 90) {
				            	logoIcon = new ImageIcon(logoIcon.getImage().
				                                          getScaledInstance(90, -1,
				                                          Image.SCALE_SMOOTH));
				            	customerLogoFile.setPreferredSize(new Dimension(logoIcon.getIconWidth(), logoIcon.getIconHeight()));
				            } 
				        }
				        
				        customerLogoFile.setIcon(logoIcon);
				        //customerLogoFile.setText(fileChooser.getSelectedFile().getName());
						
				        deleteLogo.setVisible(true);
				        
						mainContainer.repaint();
					}
					catch (Exception e) {
						JOptionPane.showMessageDialog(mainFrame, 
			    				"L'image choisi est invalide", "Erreur", 
								JOptionPane.WARNING_MESSAGE);
					}		
			    }
			}
		});
	    
	    constraint.gridx = 3;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		mainContainer.add(addLogo, constraint);

		
		/*----------------------------------------------formulaire rapport----------------------------------------------------------*/
	    
		JLabel titreRapport = new JLabel("Rapport"); //titre de la partie rapport du formulaire
		titreRapport.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreRapport
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = titleInset; //marges autour de l'element
		constraint.fill = GridBagConstraints.BOTH;
	    mainContainer.add(titreRapport, constraint); //ajout du titreRapport dans conteneurPrincipal
		
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		
	    //rapport d'activite
	    JLabel rapportActivite = new JLabel("Rapport d'activité : "); ////creation du label rapportActivite
	    
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.RELATIVE;
		mainContainer.add(rapportActivite, constraint); //ajout du label rapportActivite
		
		final String[] choixRapport = {"Hebdomadaire", "Mensuel", "Bimensuel", "Trimestriel", "Semestriel", "Annuel"}; //liste des differents choix de la duree du rapport d'activite
		final JComboBox<String> comboBoxRapport = new JComboBox<String>(choixRapport); //initialisation du comboBox comboBoxRapport avec la liste choixRapport
		comboBoxRapport.setPreferredSize(new Dimension(100, 20)); //dimension de la comboBoxRapport
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		rapportActivite.setLabelFor(comboBoxRapport); //attribution de la comboBox comboBoxRapport au label rapportActivite
		mainContainer.add(comboBoxRapport, constraint); //ajout de la comboBox comboBoxRapport
		
		final JCheckBox updateVoucherMonth = new JCheckBox("Mettre à jour le nombre de mois");
		
		updateVoucherMonth.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (updateVoucherMonth.isSelected()) {
					comboBoxRapport.setSelectedItem(comboBoxRapport.getSelectedItem());
				}
			}
		});
		
		constraint.gridx = 1;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		mainContainer.add(updateVoucherMonth, constraint);
		
	    //date debut
	    final JLabel dateDebut = new JLabel("Date de début : "); //creation du label dateDebut
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.RELATIVE;
	    mainContainer.add(dateDebut, constraint); //ajout du label dateDebut
	    
	    JFormattedTextField textFieldDateDebut = null;
	    
	    try{
			MaskFormatter maskDate  = new MaskFormatter("##/##/####"); //masque pour le format date
			textFieldDateDebut = new JFormattedTextField(maskDate); //initialisation de la zone de texte textFieldDateDebut formattee par le masque maskDate
			textFieldDateDebut.setValue("00/00/0000");
	    }
	    catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    
	    dateDebut.setLabelFor(textFieldDateDebut); //attribution de la zone de texte textFieldDateDebut au label dateDebut
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		mainContainer.add(textFieldDateDebut, constraint); //ajout de la zone de texte textFieldDateDebut
		JFormattedTextField finalTextFieldDateDebut = textFieldDateDebut;
		
		//date fin
	    JLabel dateFin = new JLabel("Date de fin : "); //creation du label dateFin
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.RELATIVE;
	    mainContainer.add(dateFin, constraint); //ajout du label dateFin
	    
	    JFormattedTextField textFieldDateFin = null;
	    try{
			MaskFormatter maskDate  = new MaskFormatter("##/##/####"); //masque pour le format date
			textFieldDateFin = new JFormattedTextField(maskDate); //initialisation de la zone de texte textFieldDateFin formattee par le masque maskDate
			textFieldDateFin.setValue("00/00/0000");
	    }
	    catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    
	    dateFin.setLabelFor(textFieldDateFin); //attribution de la zone de texte textFieldDateFin au label dateFin
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		mainContainer.add(textFieldDateFin, constraint); //ajout de la zone de texte textFieldDateFin
		JFormattedTextField finalTextFieldDateFin = textFieldDateFin;
		
		/*----------------------------------------------formulaire bons preventifs----------------------------------------------------------*/
		
		JLabel titreBP = new JLabel("Bons préventifs"); //titre de la partie bons preventifs du formulaire
		titreBP.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreBP
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = titleInset; //marges autour de l'element
		mainContainer.add(titreBP, constraint); //ajout du titreBP dans conteneurPrincipal
	    
	    Collection<JComboBox<String>> preventivesVouchersMonths        = new LinkedList<JComboBox<String>>();
	    Collection<JTextField>        nbPreventivesVouchersOpened      = new LinkedList<JTextField>();
	    Collection<JTextField>        nbPreventivesVouchersClosed      = new LinkedList<JTextField>();
	    
	    preventiveVoucherLastMonthPosition = 0;
	    
	    final JPanel preventiveVoucherMonthsPanel = new JPanel(new GridBagLayout());
	    
	    GridBagConstraints preventiveVoucherMonthsConstraint = new GridBagConstraints();
	    preventiveVoucherMonthsConstraint.gridx = 0;
	    preventiveVoucherMonthsConstraint.gridy = preventiveVoucherLastMonthPosition;
		preventiveVoucherMonthsConstraint.weightx = 1;
		preventiveVoucherMonthsConstraint.gridwidth = GridBagConstraints.REMAINDER;
		preventiveVoucherMonthsConstraint.insets = new Insets(3, 0, 1, 0);
		preventiveVoucherMonthsConstraint.fill = GridBagConstraints.BOTH;
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.weightx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(3, 0, 3, 0);
		mainContainer.add(preventiveVoucherMonthsPanel, constraint);
	    
	    final ArrayList<JButton> deleteButtonsPreventivesVouchers = new ArrayList<JButton>();
	    
	    addPreventiveVoucherMonth = new JButton(ADD_MONTH_BUTTON_TEXT[0]);
	    final ImageIcon addIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[1]);
	    
	    if (addIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
		    iconHeight = (int) (addPreventiveVoucherMonth.getPreferredSize().getHeight() - addPreventiveVoucherMonth.getPreferredSize().getHeight() / 3);
		    iconWidth  = addIcon.getIconWidth() / (addIcon.getIconHeight() / iconHeight);
		    
		    tmpImg = addIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    addIcon.setImage(tmpImg);
		    addPreventiveVoucherMonth.setIcon(addIcon);
	    }
		
		addPreventiveVoucherMonth.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				if (preventiveVoucherLastMonthPosition >= NUMBER_PREVENTIVE_MONTH_ALLOWED) {
					JOptionPane.showMessageDialog(preventiveVoucherMonthsPanel, 
		    				"Impossible d'ajouter un mois supplémentaire dans la partie " + PREVENTIVE_VOUCHER_MONTH_LABELS[0], "Erreur", 
							JOptionPane.WARNING_MESSAGE);
					addPreventiveVoucherMonth.setEnabled(false);
					addPreventiveVoucherMonth.setText(ADD_MONTH_BUTTON_TEXT[1]);
					addPreventiveVoucherMonth.setIcon(null);
					return;
				}
				
				
				JPanel preventiveVoucherMonth = createPreventiveVoucherMonth(
						preventivesVouchersMonths, 
						nbPreventivesVouchersOpened, 
						nbPreventivesVouchersClosed, 
						deleteButtonsPreventivesVouchers);
				
				preventiveVoucherMonth.setBorder(BorderFactory.createTitledBorder("Bon préventif"));
				
				preventiveVoucherMonthsConstraint.gridy = ++preventiveVoucherLastMonthPosition;
				preventiveVoucherMonthsPanel.add(preventiveVoucherMonth, preventiveVoucherMonthsConstraint);
				
				preventiveVoucherMonthsPanel.revalidate();
				
				addPreventiveVoucherMonth.setText(ADD_MONTH_BUTTON_TEXT[2]);
				addPreventiveVoucherMonth.setEnabled(false);
				addPreventiveVoucherMonth.setIcon(null);
			}
		});
		
		//commentaire BP
	    final JLabel commentaireBP = new JLabel(PREVENTIVE_VOUCHER_MONTH_LABELS[3]); //creation du label commentaireBP
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.weightx = 0;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
		mainContainer.add(commentaireBP, constraint);
	    
		JTextArea textAreaCommentaireBP = new JTextArea(4, 15); //creation de la zone de texte textAreaCommentaireBP
	    JScrollPane scrollPaneComBP = new JScrollPane(textAreaCommentaireBP); //creation de la scrollPane scrollPaneComBP contenant textAreaCommentaireBP
	    commentaireBP.setLabelFor(textAreaCommentaireBP); //attribution de la zone de texte textAreaCommentaireBP au label commentaireBP
	    
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		mainContainer.add(scrollPaneComBP, constraint); //ajout de la scrollPane scrollPaneComBP
	    
	    constraint.gridx = 1;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(10, 0, 3, 0); //marges autour de l'element
		mainContainer.add(addPreventiveVoucherMonth, constraint); //ajout du bouton ajoutElement
	    
	    /*----------------------------------------formulaire bons preventifs par domaine------------------------------------------------*/
	    
	    JLabel titreBPDomaine = new JLabel("Bons préventifs par domaines"); //titre de la partie Bons preventifs par domaine du formulaire
	    titreBPDomaine.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		constraint.gridx = 0; //position horizontale
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1; //nombre de cases occupees à partir de sa postion horizontale
		constraint.insets = titleInset; //marges autour de l'element
	    mainContainer.add(titreBPDomaine, constraint); //ajout du titreBPDomaine dans conteneurPrincipal
	    
	    String[] listeDomaines = {"Clos et ouvert", "Aménagement intérieur", "Ascenseur, monte-charge", "CVC", "Plomberie sanitaire",
	    						  "Electricité CFO", "Sûreté", "Sécurité détection incendie", "Aménagements extérieurs", "Centrale énergie",
	    						  "Cont, réglementaire"}; //liste des differents domaines
	    int nbDomaines = listeDomaines.length; //taille de la liste des domaines
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    
	    preventiveVoucherPourcentageFields = new ArrayList<JFormattedTextField>();
	    preventiveVoucherDomains           = new ArrayList<JCheckBox>();
	    
	    for(int i = 0; i < nbDomaines; i++){
	    	
	    	JCheckBox currentDomain = new JCheckBox(listeDomaines[i]);
	    	preventiveVoucherDomains.add(currentDomain);
			
	    	MaskFormatter maskPourcent = null;
			try{
				maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format pourcentage
		    }
			catch(ParseException e){
				e.printStackTrace(); //exception
			}
			
			final JFormattedTextField currentTextFieldPourcent = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte Pourcent1 formattee par le masque
			currentTextFieldPourcent.setValue("00.00%");
			preventiveVoucherPourcentageFields.add(currentTextFieldPourcent);
			
			currentTextFieldPourcent.setEnabled(false);
			
			currentDomain.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					currentTextFieldPourcent.setEnabled(currentDomain.isSelected());
					mainContainer.revalidate();
				}
			});
			
			constraint.gridwidth = 1; //nombre de cases occupees à partir de sa postion horizontale
	    	constraint.gridx = 0; //position horizontale
			constraint.gridy = ++positionCounter + i; //position de l'element a la position verticale de depart + i
			mainContainer.add(currentDomain, constraint); //ajout de la checkbox domaine
			
		    constraint.gridx = 1; //position horizontale
			constraint.gridwidth = GridBagConstraints.REMAINDER; //dernier element de sa ligne
			mainContainer.add(currentTextFieldPourcent, constraint); //ajout de la zone de texte Pourcent1
	    }
	  
		//commentaire BP par domaine
	    final JLabel commentaireBPDomaine = new JLabel("Commentaire : "); //creation du label emailCl
		
	    constraint.gridx = 0;
		positionCounter += 11;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    mainContainer.add(commentaireBPDomaine, constraint); //ajout du label emailCl
	    
	    final JTextArea textAreaCommentaireBPDomaine = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    final JScrollPane scrollPaneComBPDomaine = new JScrollPane(textAreaCommentaireBPDomaine);
	    commentaireBPDomaine.setLabelFor(textAreaCommentaireBPDomaine); //attribution de la zone de texte au label emailCl
	    
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    mainContainer.add(scrollPaneComBPDomaine, constraint); //ajout de la zone de texte emailCl
	    
	    /*----------------------------------------------formulaire arborescence libre----------------------------------------------------------*/
	    
	    final JLabel titreArboLibre1 = new JLabel("Arborescence libre"); //titre de la parte rapport du formulaire
	    titreArboLibre1.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = titleInset;
	    mainContainer.add(titreArboLibre1, constraint); //ajout du titreRapport dans conteneurPrincipal
	    
	    freeTrees1LastPosition = 0;
	    
	    // Initialisation du JPanel qui contiendra tous les elements
 		final JPanel freeTreesPanel = new JPanel(new GridBagLayout());
 		final GridBagConstraints freeTreeConstraints = new GridBagConstraints();
 		freeTreeConstraints.gridx = 0;
 		freeTreeConstraints.gridy = freeTrees1LastPosition;
 		freeTreeConstraints.weightx = 1;
 		freeTreeConstraints.gridwidth = GridBagConstraints.REMAINDER;
 		freeTreeConstraints.insets = new Insets(3, 0, 1, 0);
 		freeTreeConstraints.fill = GridBagConstraints.BOTH;
 		
 		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets =  new Insets(0, 0, 0, 0);
	    mainContainer.add(freeTreesPanel, constraint);
	    
	    final Collection<FreeTree> freeTrees1 = new LinkedList<FreeTree>();
	    
	    //bouton d'ajout d'arborescence libre
	  	JButton ajoutArboLibre = new JButton("Ajouter une arborescence libre");
	  	
	  	final ImageIcon addFreeTreeIcon1 = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[1]);
	  	
	  	if (addFreeTreeIcon1.getImageLoadStatus() != MediaTracker.ERRORED) {
		  	iconHeight = (int) (ajoutArboLibre.getPreferredSize().getHeight() - ajoutArboLibre.getPreferredSize().getHeight() / 3);
		    iconWidth  = addFreeTreeIcon1.getIconWidth() / (addFreeTreeIcon1.getIconHeight() / iconHeight);
		    
		    tmpImg = addFreeTreeIcon1.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    addFreeTreeIcon1.setImage(tmpImg);
		    ajoutArboLibre.setIcon(addFreeTreeIcon1);
	  	}
	  	
	  	ajoutArboLibre.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (freeTrees1LastPosition >= NUMBER_FREE_TREE_ALLOWED) {
					JOptionPane.showMessageDialog(mainContainer, 
		    				"Impossible d'ajouter une arborescence libre dans la partie " + titreArboLibre1.getText(), "Erreur", 
							JOptionPane.WARNING_MESSAGE);
					
			    	ajoutArboLibre.setText(ADD_FREE_TREE_TEXT[1]);
			    	ajoutArboLibre.setEnabled(false);
			    	ajoutArboLibre.setIcon(null);
			    	
			    	return;
			    }
			    
			    FreeTree arboLibre = new FreeTree();
			    
			    freeTreeConstraints.gridy = ++freeTrees1LastPosition;
			    freeTreesPanel.add(arboLibre, freeTreeConstraints);
			    
			    freeTrees1.add(arboLibre);
			    
			    // Bouton supprimer
			    final JButton delete = new JButton("Supprimer");
			    
			    final ImageIcon deleteFreeTreeIcon1 = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[4]);
			  	
			  	if (deleteFreeTreeIcon1.getImageLoadStatus() != MediaTracker.ERRORED) {
				  	final int iconHeight = (int) (delete.getPreferredSize().getHeight() - delete.getPreferredSize().getHeight() / 3);
				    final int iconWidth  = deleteFreeTreeIcon1.getIconWidth() / (deleteFreeTreeIcon1.getIconHeight() / iconHeight);
				    
				    final Image tmpImg = deleteFreeTreeIcon1.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
				    deleteFreeTreeIcon1.setImage(tmpImg);
				    delete.setIcon(deleteFreeTreeIcon1);
			  	}
			    
			    delete.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						// Parent de ce JPanel
						final Container parent = arboLibre.getParent();
						
						// Si ce JPanel a bien un parent
						if (parent != null) {
							
							// Obtient le GridBagLayout de son parent
							final GridBagLayout parentLayout = (GridBagLayout)parent.getLayout();
							
							// Change le positionnement de tous les Component qui sont apres celui que l'on efface
							// Cela fonctionne car dans la hierarchie du parent du JPanel, les Component sont tous ajoutes en derniers
							// Parcour les Component qui sont apres celui que l'on efface
							for (int i = OperationUtilities.getComponentIndex(arboLibre); i < parent.getComponentCount(); ++i) {
								
								// Obtention du Component suivant
								final Component currentComponent = parent.getComponent(i);
								// Obtiention de son GridBagConstraints (celui avec lequel il a ete ajoute)
								final GridBagConstraints thisComponentConstraint = parentLayout.getConstraints(currentComponent);
								// Decremente sa coordonnee verticale
								--thisComponentConstraint.gridy;
								// Applique les modifications
								parentLayout.setConstraints(currentComponent, thisComponentConstraint);
							}
							
							// Suppression de cet element de son parent
							parent.remove(arboLibre);
							// On revalide le parent
							parent.revalidate();
						}
						
						--freeTrees1LastPosition;
						
						ajoutArboLibre.setText(ADD_FREE_TREE_TEXT[0]);
				    	ajoutArboLibre.setEnabled(true);
				    	ajoutArboLibre.setIcon(addFreeTreeIcon1);
						
						freeTrees1.remove(arboLibre);
					}
				});
			    
			    GridBagConstraints freeTreeConstraint = arboLibre.getConstraint();
			    freeTreeConstraint.gridx = 0;
			    ++freeTreeConstraint.gridy;
			    freeTreeConstraint.gridwidth = 1;
				freeTreeConstraint.insets = new Insets(0, 0, 3, 0); //marges autour de l'element
				arboLibre.add(delete, freeTreeConstraint); //ajout du bouton supprimer dans conteneurPrincipal

				freeTreesPanel.revalidate();
				
			    /*if (freeTrees1LastPosition >= NUMBER_FREE_TREE_ALLOWED) {
			    	ajoutArboLibre.setText(ADD_FREE_TREE_TEXT[1]);
			    	ajoutArboLibre.setEnabled(false);
			    	ajoutArboLibre.setIcon(null);
			    }*/
			}
		});
	  	
	  	constraint.gridx = 1;
	  	constraint.gridy = ++positionCounter;
	  	constraint.gridwidth = GridBagConstraints.REMAINDER;
	  	mainContainer.add(ajoutArboLibre, constraint); //ajout du bouton ajoutArboLibre
	
	    
	    /*----------------------------------------------formulaire demandes d'intervention----------------------------------------------------------*/
	    
		JLabel titreDI = new JLabel("Demandes d'intervention"); //titre de la parte rapport du formulaire
		titreDI.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = titleInset; //marges autour de l'element
	    mainContainer.add(titreDI, constraint); //ajout du titreRapportr dans conteneurPrincipal
	    
	    final ArrayList<JButton> deleteButtonsInterventionMonths = new ArrayList<JButton>();
	    
		//bouton d'ajout de mois pour les BP
		interventionDemandLastMonthPosition = 0;		
		
		// Initialisation du JPanel qui contiendra tous les mois
 		final JPanel InterventionDemandsPanel = new JPanel(new GridBagLayout());
 		final GridBagConstraints interventionDemandeConstraint = new GridBagConstraints();
 		interventionDemandeConstraint.gridx = 0;
 		interventionDemandeConstraint.gridy = interventionDemandLastMonthPosition;
 		interventionDemandeConstraint.weightx = 1;
 		interventionDemandeConstraint.gridwidth = GridBagConstraints.REMAINDER;
 		interventionDemandeConstraint.insets = new Insets(3, 0, 1, 0);
 		interventionDemandeConstraint.fill = GridBagConstraints.BOTH;
 		
 		constraint.gridx = 0;	
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		mainContainer.add(InterventionDemandsPanel, constraint); //ajout du bouton ajoutElement
		
		Collection<JComboBox<String>> interventionMonths = new LinkedList<JComboBox<String>>();
		Collection<JTextField> interventionNumbers = new LinkedList<JTextField>();
				
		addInterventionDemandMonth = new JButton(ADD_MONTH_BUTTON_TEXT[0]);
		
		final ImageIcon addInterventionMonthIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[1]);
		
		if (addInterventionMonthIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
	  	
		  	iconHeight = (int) (addInterventionDemandMonth.getPreferredSize().getHeight() - addInterventionDemandMonth.getPreferredSize().getHeight() / 3);
		    iconWidth  = addInterventionMonthIcon.getIconWidth() / (addInterventionMonthIcon.getIconHeight() / iconHeight);
		    
		    tmpImg = addInterventionMonthIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    addInterventionMonthIcon.setImage(tmpImg);
		    addInterventionDemandMonth.setIcon(addInterventionMonthIcon);
		}
				
		addInterventionDemandMonth.addActionListener(new ActionListener() {
					    	
			public void actionPerformed(ActionEvent arg0) {
				
				if (interventionDemandLastMonthPosition++ >= NUMBER_INTERVENTION_DEMAND_MONTH_ALLOWED) {
					JOptionPane.showMessageDialog(mainContainer, 
		    				"Impossible d'ajouter un mois supplémentaire dans la partie " + titreDI.getText(), "Erreur", 
							JOptionPane.WARNING_MESSAGE);
					
					addInterventionDemandMonth.setText(ADD_MONTH_BUTTON_TEXT[1]);
					addInterventionDemandMonth.setIcon(null);
					addInterventionDemandMonth.setEnabled(false);
					
					return;
				}
				
				JPanel interventionDemand = createInterventionDemand(interventionMonths, interventionNumbers, 
						deleteButtonsInterventionMonths);
			    
				interventionDemandeConstraint.gridy = ++interventionDemandLastMonthPosition;
				InterventionDemandsPanel.add(interventionDemand, interventionDemandeConstraint);
				
				addInterventionDemandMonth.setText(ADD_MONTH_BUTTON_TEXT[2]);
				addInterventionDemandMonth.setIcon(null);
				addInterventionDemandMonth.setEnabled(false);
				
				/*if (interventionDemandLastMonthPosition++ >= NUMBER_INTERVENTION_DEMAND_MONTH_ALLOWED) {
					addInterventionDemandMonth.setText(ADD_MONTH_BUTTON_TEXT[1]);
					addInterventionDemandMonth.setIcon(null);
					addInterventionDemandMonth.setEnabled(false);
				}*/
				
				InterventionDemandsPanel.revalidate();
			}
		});
		
		constraint.gridx = 1;	
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		mainContainer.add(addInterventionDemandMonth, constraint); //ajout du bouton ajoutElement
		
		//commentaire DI
	    final JLabel commentaireDI = new JLabel("Commentaire : "); //creation du label emailCl
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    mainContainer.add(commentaireDI, constraint); //ajout du label emailCl
	    
	    final JTextArea textAreaCommentaireDI = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    final JScrollPane scrollPaneComDI = new JScrollPane(textAreaCommentaireDI);
	    commentaireDI.setLabelFor(textAreaCommentaireDI); //attribution de la zone de texte au label emailCl
	    
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    mainContainer.add(scrollPaneComDI, constraint); //ajout de la zone de texte emailCl
	    
	    comboBoxRapport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (updateVoucherMonth.isSelected()) {
					
					switch (comboBoxRapport.getSelectedItem().toString()) {
					case "Hebdomadaire":
					case "Mensuel" :	
						monthNumber = 1;
						break;
					
					case "Bimensuel":
						monthNumber = 2;
						break;
					
					case "Trimestriel":
						monthNumber = 4;
						break;
					
					case "Semestriel" :
						monthNumber = 6;
						break;
					
					case "Annuel" :
						monthNumber = 12;
						break;
					
					default:
						monthNumber = 0;
						break;
					}
					
					// Partie bon preventif
					final int previousMonthNumber = deleteButtonsPreventivesVouchers.size();
					
					while (deleteButtonsPreventivesVouchers.size() != monthNumber) {
						
						if (deleteButtonsPreventivesVouchers.size() < monthNumber) {
							addPreventiveVoucherMonth.doClick();
							addPreventiveVoucherMonth.setEnabled(true);
						}
						else {
							deleteButtonsPreventivesVouchers.get(deleteButtonsPreventivesVouchers.size() - 1).doClick();
						}
					}
					
					Iterator<JComboBox<String>> preventiveMonthsIter = preventivesVouchersMonths.iterator();
					
					for (int i = 0; i < deleteButtonsPreventivesVouchers.size(); ++i) {
						JComboBox<String> currentMonth = preventiveMonthsIter.next();
						
						if (i >= previousMonthNumber) {
							currentMonth.setSelectedIndex(i);
						}
					}	
					
					addPreventiveVoucherMonth.setText(ADD_MONTH_BUTTON_TEXT[2]);
					addPreventiveVoucherMonth.setEnabled(false);
					addPreventiveVoucherMonth.setIcon(null);
					
					// Partie demande d'intervention
					final int previousInterventionMonthNumber = deleteButtonsInterventionMonths.size();
					
					while (deleteButtonsInterventionMonths.size() != monthNumber) {
						
						if (deleteButtonsInterventionMonths.size() < monthNumber) {
							addInterventionDemandMonth.doClick();
							addInterventionDemandMonth.setEnabled(true);
						}
						else {
							deleteButtonsInterventionMonths.get(deleteButtonsInterventionMonths.size() - 1).doClick();
						}
					}
					
					Iterator<JComboBox<String>> interventionMonthsIter = interventionMonths.iterator();
					
					for (int i = 0; i < deleteButtonsInterventionMonths.size(); ++i) {
						JComboBox<String> currentMonth = interventionMonthsIter.next();
						
						if (i >= previousInterventionMonthNumber) {
							currentMonth.setSelectedIndex(i);
						}
					}	
					
					addInterventionDemandMonth.setText(ADD_MONTH_BUTTON_TEXT[2]);
					addInterventionDemandMonth.setEnabled(false);
					addInterventionDemandMonth.setIcon(null);
				}
			}
		});	   
	    
	    updateVoucherMonth.setSelected(true);
	    
	    comboBoxRapport.setSelectedIndex(comboBoxRapport.getSelectedIndex());
	    
	    /*----------------------------------------formulaire demandes d'intervention par états------------------------------------------------*/
	   
	    final JLabel titreDIEtat = new JLabel("Demandes d'intervention par états"); //titre de la partie Bons preventifs par domaine du formulaire
	    titreDIEtat.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		constraint.insets = titleInset; //marges autour de l'element
	    mainContainer.add(titreDIEtat, constraint); //ajout du titreBPDomaine dans conteneurPrincipal
	    
	    String[] listeEtats = {"Attente de lecture avant exécution", "Attente de lecture par validateur",
	    						"Attente de réalisation", "En cours de réalisation", "Réalisation partielle"}; //liste des etats
	    int nbEtats = listeEtats.length; //nombre d'etats
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    
	    // Toutes les JCheckBox d'etat
	    Collection<JCheckBox> states    = new ArrayList<JCheckBox>();
	    // Touts les JTextField correspondat
	    Collection<JTextField> nbStates = new ArrayList<JTextField>();
	    
	    for(int i = 0; i < nbEtats; i++){
		    
			JTextField textFieldNbEtat = new JTextField(15); //initialisation de la zone de texte textFieldNbEtat
			textFieldNbEtat.setText("0");
			textFieldNbEtat.setEnabled(false);
			
			JCheckBox etat = new JCheckBox(listeEtats[i]); //creation d'une checkbox pour chaque etat possible
			
			etat.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					textFieldNbEtat.setEnabled(etat.isSelected());
					mainContainer.revalidate();
				}
			} );
			
			states.add(etat);
			nbStates.add(textFieldNbEtat);
			
			constraint.gridx = 0;
			constraint.gridy = ++positionCounter + i; 
			constraint.gridwidth = 1;
		    mainContainer.add(etat, constraint); //ajout de la checkbox etat
		    
		    constraint.gridx = 1;
			constraint.gridwidth = GridBagConstraints.REMAINDER;
			mainContainer.add(textFieldNbEtat, constraint); //ajout de la zone de texte textFieldNbEtat
	    }
	    
		//commentaire DI par etat
	    final JLabel commentaireDIEtat = new JLabel("Commentaire : "); //creation du label commentaireDIEtat
	    
	    final JTextArea textAreaCommentaireDIEtat = new JTextArea(4, 15); //creation de la zone de texte textAreaCommentaireDIEtat
	    final JScrollPane scrollPaneComDIEtat = new JScrollPane(textAreaCommentaireDIEtat); //creation de la scrollPane scrollPaneComDIEtat contenant textAreaCommentaireDIEtat
	    commentaireDIEtat.setLabelFor(textAreaCommentaireDIEtat); //attribution de la zone de texte textAreaCommentaireDIEtat au label commentaireDIEtat
	    	   
		constraint.gridx = 0;
		positionCounter += 5;
		constraint.gridy = positionCounter;
		constraint.gridwidth = 1;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    mainContainer.add(commentaireDIEtat, constraint); //ajout du label commentaireDIEtat    
	    
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    mainContainer.add(scrollPaneComDIEtat, constraint); //ajout de scrollPaneComDIEtat
	    
	    /*----------------------------------------formulaire demandes d'intervention par domaine------------------------------------------------*/
	    
	    final JLabel titreDIDomaine = new JLabel("Demandes d'intervention par domaines"); //titre de la partie Bons preventifs par domaine du formulaire
	    titreDIDomaine.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		
	    constraint.gridx = 0; //position horizontale
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1; //nombre de cases occupees à partir de sa postion horizontale
		constraint.insets = titleInset; //marges autour de l'element
	    mainContainer.add(titreDIDomaine, constraint); //ajout du titreBPDomaine dans conteneurPrincipal
	    
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    
	    final Collection<JCheckBox>  interventionDomains = new ArrayList<JCheckBox>();
	    final Collection<JFormattedTextField> interventionPourcents = new ArrayList<JFormattedTextField>();
	    
	    for(int i = 0; i < nbDomaines; i++){
	    	final JCheckBox diDomaine = new JCheckBox(listeDomaines[i]);
	    	
	    	MaskFormatter maskPourcent = null;
			
	    	try{
				maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format pourcentage
	    	}
			catch(ParseException e){
				e.printStackTrace(); //exception
			}
			
			final JFormattedTextField textFieldPourcentDI = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte Pourcent1 formattee par le masque
			
			textFieldPourcentDI.setEnabled(false);
			textFieldPourcentDI.setValue("00.00%");
			diDomaine.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					textFieldPourcentDI.setEnabled(diDomaine.isSelected());
					mainContainer.revalidate();
				}
			});
			
			interventionDomains.add(diDomaine);
			interventionPourcents.add(textFieldPourcentDI);
			
			constraint.gridwidth = 1; //nombre de cases occupees à partir de sa postion horizontale
	    	constraint.gridx = 0; //position horizontale
			constraint.gridy = ++positionCounter + i; //position de l'element a la position verticale de depart + i
			mainContainer.add(diDomaine, constraint); //ajout de la checkbox domaine
			
		    constraint.gridx = 1; //position horizontale
			constraint.gridwidth = GridBagConstraints.REMAINDER; //dernier element de sa ligne
			mainContainer.add(textFieldPourcentDI, constraint); //ajout de la zone de texte Pourcent1
	    }
	  
		//commentaire BP par domaine
	    final JLabel commentaireDIDomaine = new JLabel("Commentaire : "); //creation du label emailCl
	    
	    final JTextArea textAreaComDIDomaine = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    final JScrollPane scrollPaneComDIDomaine = new JScrollPane(textAreaComDIDomaine);
	    commentaireBPDomaine.setLabelFor(textAreaComDIDomaine); //attribution de la zone de texte au label emailCl
	    
		constraint.gridx = 0;
		positionCounter += 11;
		constraint.gridy = positionCounter;
		constraint.gridwidth = 1;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    mainContainer.add(commentaireDIDomaine, constraint); //ajout du label emailCl

	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    mainContainer.add(scrollPaneComDIDomaine, constraint); //ajout de la zone de texte emailCl
	    
	    /*----------------------------------------------formulaire arborescence libre----------------------------------------------------------*/
	    
	    final JLabel titreArboLibre2 = new JLabel("Arborescence libre"); //titre de la parte rapport du formulaire
	    titreArboLibre2.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreBP
	    
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = titleInset; //marges autour de l'element
	    mainContainer.add(titreArboLibre2, constraint); //ajout du titreRapportr dans conteneurPrincipal

	    // Initialisation du JPanel qui contiendra tous les elements
  		final JPanel freeTrees2Panel = new JPanel(new GridBagLayout());
  		final GridBagConstraints freeTree2Constraints = new GridBagConstraints();
  		freeTree2Constraints.gridx = 0;
  		freeTree2Constraints.gridy = 0;
  		freeTree2Constraints.weightx = 1;
  		freeTreeConstraints.gridwidth = GridBagConstraints.REMAINDER;
  		freeTree2Constraints.insets = new Insets(3, 0, 1, 0);
  		freeTree2Constraints.fill = GridBagConstraints.BOTH;
  		
  		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(0, 0, 0, 0);
		mainContainer.add(freeTrees2Panel, constraint);
	    
	    final Collection<FreeTree> freeTrees2 = new LinkedList<FreeTree>();
	    
	    //bouton d'ajout d'arborescence libre
	  	final JButton ajoutArboLibre2 = new JButton("Ajouter une arborescence libre");
	  	
	  	final ImageIcon addFreeTreeIcon2 = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[1]);
	  	
	  	if (addFreeTreeIcon2.getImageLoadStatus() != MediaTracker.ERRORED) {
		  	iconHeight = (int) (ajoutArboLibre2.getPreferredSize().getHeight() - ajoutArboLibre2.getPreferredSize().getHeight() / 3);
		    iconWidth  = addFreeTreeIcon2.getIconWidth() / (addFreeTreeIcon2.getIconHeight() / iconHeight);
		    
		    tmpImg = addFreeTreeIcon2.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    addFreeTreeIcon2.setImage(tmpImg);
		    ajoutArboLibre2.setIcon(addFreeTreeIcon2);
	  	}
	  	
	  	ajoutArboLibre2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (freeTree2Constraints.gridy >= NUMBER_FREE_TREE_ALLOWED) {
					JOptionPane.showMessageDialog(mainContainer, 
		    				"Impossible d'ajouter une arborescence libre dans la partie " + titreArboLibre2.getText(), "Erreur", 
							JOptionPane.WARNING_MESSAGE);
					
			    	ajoutArboLibre2.setText(ADD_FREE_TREE_TEXT[1]);
			    	ajoutArboLibre2.setEnabled(false);
			    	ajoutArboLibre2.setIcon(null);
			    	
			    	return;
			    }
				
				final FreeTree freeTree = new FreeTree();
			    
				++freeTree2Constraints.gridy;
				freeTrees2Panel.add(freeTree, freeTree2Constraints);
				
				freeTrees2.add(freeTree);
			    
			    // Bouton supprimer
			    final JButton delete = new JButton("Supprimer");
			    
			    final ImageIcon deleteFreeTreeIcon2 = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[4]);
			  	
			  	if (deleteFreeTreeIcon2.getImageLoadStatus() != MediaTracker.ERRORED) {
				  	final int iconHeight = (int) (delete.getPreferredSize().getHeight() - delete.getPreferredSize().getHeight() / 3);
				    final int iconWidth  = deleteFreeTreeIcon2.getIconWidth() / (deleteFreeTreeIcon2.getIconHeight() / iconHeight);
				    
				    final Image tmpImg = deleteFreeTreeIcon2.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
				    deleteFreeTreeIcon2.setImage(tmpImg);
				    delete.setIcon(deleteFreeTreeIcon2);
			  	}
			    
			    delete.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						// Parent de ce JPanel
						final Container parent = freeTree.getParent();
						
						// Si le JPanel a bien un parent
						if (parent != null) {
							
							// Obtient le GridBagLayout de son parent
							final GridBagLayout freeTreeLayout = (GridBagLayout)parent.getLayout();
							
							// Change le positionnement de tous les Component qui sont apres celui que l'on efface
							// Cela fonctionne car dans la hierarchie du parent du JPanel, les Component sont tous ajoutes en derniers
							// Parcour les Component qui sont apres celui que l'on efface
							for (int i = OperationUtilities.getComponentIndex(freeTree); i < parent.getComponentCount(); ++i) {
								
								// Obtention du Component suivant
								final Component currentComponent = parent.getComponent(i);
								// Obtiention de son GridBagConstraints (celui avec lequel il a ete ajoute)
								final GridBagConstraints thisComponentConstraint = freeTreeLayout.getConstraints(currentComponent);
								// Decremente sa coordonnee verticale
								--thisComponentConstraint.gridy;
								// Applique les modifications
								freeTreeLayout.setConstraints(currentComponent, thisComponentConstraint);
							}
							
							// Suppression de ce JPanel de son parent
							parent.remove(freeTree);
							// On revalide le parent
							parent.revalidate();
						}
							
						--freeTree2Constraints.gridy;
						
						ajoutArboLibre2.setText(ADD_FREE_TREE_TEXT[0]);
						ajoutArboLibre2.setEnabled(true);
						ajoutArboLibre2.setIcon(addFreeTreeIcon2);
						
						freeTrees2.remove(freeTree);
					}
				});
			    
			    final GridBagConstraints freeTreeConstraint = freeTree.getConstraint();
			    freeTreeConstraint.gridx = 0;
			    ++freeTreeConstraint.gridy;
			    freeTreeConstraint.gridwidth = 1;
				freeTreeConstraint.insets = new Insets(0, 0, 3, 0);
				//ajout du bouton supprimer dans ce FreeTree
				freeTree.add(delete, freeTreeConstraint); 
				
				// Si le dernier FreeTree est le dernier que l'on peut ajouter,
		    	// alors on desactive le JButton
				// Etant donne la taille maximale, cette verification est superflue par rapport a sa frequence de realisation
			    /*if (freeTree2Constraints.gridy >= NUMBER_FREE_TREE_ALLOWED) {
			    	ajoutArboLibre2.setText(ADD_FREE_TREE_TEXT[1]);
			    	ajoutArboLibre2.setEnabled(false);
			    	ajoutArboLibre2.setIcon(null);
			    }*/
				
				freeTrees2Panel.revalidate();
			}
		});
	  	
	    
	  	constraint.gridx = 1;
	  	constraint.gridy = ++positionCounter;
	  	constraint.gridwidth = GridBagConstraints.REMAINDER;
	  	mainContainer.add(ajoutArboLibre2, constraint); //ajout du bouton ajoutArboLibre
	    
	    /*----------------------------------------------formulaire compteurs----------------------------------------------------------*/
	    
	  	final JLabel meterTitle = new JLabel("Compteurs"); //titre de la parte rapport du formulaire
	  	meterTitle.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreBP
	  	
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = titleInset; //marges autour de l'element
	    mainContainer.add(meterTitle, constraint); //ajout du titreRapportr dans conteneurPrincipal
	    
	    final Collection<Meter> meters = new LinkedList<Meter>();
	    
	    final JPanel metersPanel = new JPanel(new GridBagLayout());
	    final GridBagConstraints metersConstraints = new GridBagConstraints();
		metersConstraints.gridx = 0;
		metersConstraints.gridy = 0;
		metersConstraints.weightx = 1;
		metersConstraints.gridwidth = GridBagConstraints.REMAINDER;
		metersConstraints.insets = new Insets(3, 0, 1, 0);
		metersConstraints.fill = GridBagConstraints.BOTH;
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		mainContainer.add(metersPanel, constraint);
    
	  	//bouton d'ajout de compteur
	  	final JButton ajoutCompteur = new JButton(ADD_METER_TEXT[0]);
	  	
	  	final ImageIcon addMeterIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[1]);
	  	
	  	if (addMeterIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
		  	iconHeight = (int) (ajoutCompteur.getPreferredSize().getHeight() - ajoutCompteur.getPreferredSize().getHeight() / 3);
		    iconWidth  = addMeterIcon.getIconWidth() / (addMeterIcon.getIconHeight() / iconHeight);
		    
		    tmpImg = addMeterIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    addMeterIcon.setImage(tmpImg);
		    ajoutCompteur.setIcon(addMeterIcon);
	  	}
  	
	  	ajoutCompteur.addActionListener(new ActionListener() {
	  		@Override
	  		public void actionPerformed(ActionEvent e) {
	  			
	  			if (metersConstraints.gridy >= NUMBER_METER_ALLOWED) {
	  				JOptionPane.showMessageDialog(mainContainer, 
		    				"Impossible d'ajouter un compteur dans la partie " + meterTitle.getText(), "Erreur", 
							JOptionPane.WARNING_MESSAGE);
	  				
	  				ajoutCompteur.setEnabled(false);
  					ajoutCompteur.setText(ADD_METER_TEXT[1]);
  					ajoutCompteur.setIcon(null);
  					
  					return;
				}
	  			
	  			final Meter meter = new Meter();
	  			meter.setBorder(BorderFactory.createTitledBorder("Compteur"));
	  			
	  		    // Bouton supprimer
	  		    JButton delete = new JButton("Supprimer");
	  		    
	  		    final ImageIcon deleteMeterIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[4]);
	  	  	    if (deleteMeterIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
			  	  	final int iconHeight = (int) (delete.getPreferredSize().getHeight() - delete.getPreferredSize().getHeight() / 3);
			  	    final int iconWidth  = deleteMeterIcon.getIconWidth() / (deleteMeterIcon.getIconHeight() / iconHeight);
			  	    
			  	    final Image tmpImg = deleteMeterIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
			  	    deleteMeterIcon.setImage(tmpImg);
	  	  	    }
		  	    delete.setIcon(deleteMeterIcon);
	  		    
	  		    delete.addActionListener(new ActionListener() {

	  				@Override
	  				public void actionPerformed(ActionEvent e) {
	  					
	  				    // Parent de ce JPanel
	  					final Container parent = meter.getParent();
	  					
	  					// Si le JPanel a bien un parent
	  					if (parent != null) {
	  						
	  						// Obtient le GridBagLayout de son parent
	  						final GridBagLayout freeTreeLayout = (GridBagLayout)parent.getLayout();
	  						
	  						// Change le positionnement de tous les Component qui sont apres celui que l'on efface
	  						// Cela fonctionne car dans la hierarchie du parent du JPanel, les elements sont tous ajoutes en derniers
	  						// Parcour les Component qui sont apres celui que l'on efface
	  						for (int i = OperationUtilities.getComponentIndex(meter); i < parent.getComponentCount(); ++i) {
	  							
	  							// Obtention du Component suivant
	  							final Component currentComponent = parent.getComponent(i);
	  							// Obtiention de son GridBagConstraints (celui avec lequel il a ete ajoute)
	  							final GridBagConstraints thisComponentConstraint = freeTreeLayout.getConstraints(currentComponent);
	  							// Decremente sa coordonnee verticale
	  							--thisComponentConstraint.gridy;
	  							// Applique les modifications
	  							freeTreeLayout.setConstraints(currentComponent, thisComponentConstraint);
	  						}
	  						
	  						// Suppression de ce Meter de son parent
	  						parent.remove(meter);
	  						// On revalide le parent
	  						parent.revalidate();
	  					}
	  					// Decrement l'indicateur de derniere position
	  					--metersConstraints.gridy;
	  					
	  					ajoutCompteur.setEnabled(true);
	  					ajoutCompteur.setText(ADD_METER_TEXT[0]);
	  					ajoutCompteur.setIcon(addMeterIcon);
	  					
	  					meters.remove(meter);
	  				}
	  			});
	  		    
	  		    GridBagConstraints thisMeterConstraint = meter.getConstraint();
	  		    thisMeterConstraint.gridx = 0;
	  			++thisMeterConstraint.gridy;
	  			thisMeterConstraint.gridwidth = 1;
	  			thisMeterConstraint.insets = new Insets(0, 0, 3, 0); //marges autour de l'element
	  			meter.add(delete, thisMeterConstraint); //ajout du bouton supprimer dans conteneurPrincipal
	  			
	  			++metersConstraints.gridy;
	  			metersPanel.add(meter, metersConstraints);
	  			
	  			meters.add(meter);
				
	  		    // Si le dernier Meter est le dernier que l'on peut ajouter,
		    	// alors on desactive le JButton
				// Etant donne le nombre de Meter maximale, cette verification est superflue par rapport a sa frequence de realisation
				/*if (metersConstraints.gridy >= NUMBER_METER_ALLOWED) {
					ajoutCompteur.setEnabled(false);
  					ajoutCompteur.setText(ADD_METER_TEXT[1]);
  					ajoutCompteur.setIcon(null);
				}*/
			
				metersPanel.revalidate();
	  		}
	  	});
	  	
	  	constraint.gridx = 1;
	  	constraint.gridy = ++positionCounter;
	  	constraint.gridwidth = GridBagConstraints.REMAINDER;
	  	mainContainer.add(ajoutCompteur, constraint); //ajout du bouton ajoutCompteur
	  	
	  	ajoutCompteur.doClick();
	  	
	  	addPreventiveVoucherMonth.doClick();
	  	addInterventionDemandMonth.doClick();
		
		/*-----------------------------------------Bouton de validation du formulaire--------------------------------------------------- */
		
		final JButton valideForm = new JButton("Génerer le rapport"); //bouton de validation du formulaire 
		//valideForm.setBackground(new Color(224, 35, 60));
		
		final ImageIcon generateReportIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[3]);
	  	
		if (generateReportIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
		  	iconHeight = (int) (valideForm.getPreferredSize().getHeight() - valideForm.getPreferredSize().getHeight() / 3);
		    iconWidth  = generateReportIcon.getIconWidth() / (generateReportIcon.getIconHeight() / iconHeight);
		    
		    tmpImg = generateReportIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    generateReportIcon.setImage(tmpImg);
		    valideForm.setIcon(generateReportIcon);
		}
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
	    constraint.gridwidth = GridBagConstraints.REMAINDER;
	    constraint.insets = new Insets(40, 0, 0, 0); //marges autour de l'element
	    mainContainer.add(valideForm, constraint); //ajout du bouton de validation
	    
		//action declenchee par le bouton de validation du formulaire
	    valideForm.addActionListener(new ActionListener() {
	    	
		    public void actionPerformed(ActionEvent arg0) {	
		    	/*---affichage console---*/
		    	//partie rapport
		    	System.out.println(writerTitle.getText()); 						//affichage console du titre de la partie du formulare 	
		    	System.out.println(writerName.getText() + writerNameField.getText()); 		//affichage console des données nom
		    	System.out.println(writerAdr.getText() + writerAdrArea.getText()); 			//affichage console des données adr
		    	//System.out.println(tel.getText() + textFieldTelRedac.getText()); 	//affichage console des données tel
		    	System.out.println(writerEmail.getText() + writerEmailField.getText()); 	//affichage console des données email
		    	System.out.println(writerResponsibleName.getText() + writerResponsibleNameField.getText());		//affichage console des données nomCA
		    	//partie client
		    	System.out.println(customerTitle.getText()); 									//affichage console du titre de la partie du formulaire
		    	System.out.println(siteName.getText() + siteNameField.getText()); 		//affichage console des données nomSite
		    	System.out.println(code.getText() + codeField.getText()); 				//affichage console des données code
		    	System.out.println(customerAdr.getText() + customerAdrArea.getText()); 				//affichage console des données adrCl
		    	System.out.println(postalCode.getText() + finalPostalCodeField.getText()); 	//affichage console des données codePostal
		    	System.out.println(city.getText() + cityField.getText()); 			//affichage console des données ville
		    	System.out.println(customerPhone.getText() + finalTextFieldTelCl.getText()); 			//affichage console des données telCl
		    	System.out.println(customerMail.getText() + customerMailField.getText()); 		//affichage console des données emailCl  
		    	//partie rapport
		    	System.out.println(titreRapport.getText()); 													//affichage console du titre de la partie du formulaire
		    	System.out.println(rapportActivite.getText() + comboBoxRapport.getSelectedItem().toString()); 	//affichage console des données duree rapport d'activite
		    	System.out.println(dateDebut.getText() + finalTextFieldDateDebut.getText()); 						//affichage console des données code
		    	System.out.println(dateFin.getText() + finalTextFieldDateFin.getText()); 							//affichage console des données adr
		    	//partie bons preventifs
		    	System.out.println(titreBP.getText()); 												//affichage console du titre de la partie du formulaire
		    	/*System.out.println(moisBP.getText() + comboBoxMoisBP.getSelectedItem().toString()); //affichage console des données mois
		    	System.out.println(nbBPOuverts.getText() + textFieldNbBPOuverts.getText()); 		//affichage console des données nbBPOuverts
		    	System.out.println(nbBPFermes.getText() + textFieldNbBPFermes.getText()); 			//affichage console des données nbBPFermes
		    	System.out.println(commentaireBP.getText() + textAreaCommentaireBP.getText()); 		//affichage console des données commentaireBP
		    	*///partie bons preventifs par domaine
		    	System.out.println(titreBPDomaine.getText()); //affichage console du titre de la partie du formulaire

		    	//System.out.println (commentaireBP.getText() + textAreaCommentaireBP.getText()); 	//ecriture des données commentaireBP
		    	//partie arborescence libre
		    	/*System.out.println(titreArboLibre.getText()); 								//affichage console du titre de la partie du formulaire
		    	/*System.out.println(titre.getText() + textFieldTitre.getText()); 			//affichage console des données titre
		    	System.out.println(element.getText() + textFieldElement.getText()); 		//affichage console des données element
		    	System.out.println(nombre.getText() + textFieldNombre.getText()); 			//affichage console des données nombre
		    	//System.out.println(commentaire.getText() + textAreaCommentaire.getText()); 	//affichage console des données commentaire
		    	//partie domaines d'intervention
		    	System.out.println(titreDI.getText()); 												//affichage console du titre de la partie du formulaire
		    	System.out.println(moisDI.getText() + comboBoxMoisDI.getSelectedItem().toString()); //affichage console des données moisDI
		    	System.out.println(nbIntervention.getText() + textFieldNbIntervention.getText()); 	//affichage console des données nbIntervention
		    	System.out.println(commentaireDI.getText() + textAreaCommentaireDI.getText()); 		//affichage console des données commentaireDI
		    	//partie domaines d'intervention par etat
		    	System.out.println(titreBPDomaine.getText()); 													//affichage console du titre de la partie du formulaire
		    	System.out.println (commentaireBPDomaine.getText() + textAreaCommentaireBPDomaine.getText()); 	//affichage console des données commentaireBPDomaine

		    	//partie arborescence libre 2
		    	System.out.println(titreArboLibre2.getText()); 									//affichage console du titre de la partie du formulaire
		    	/*System.out.println(titre2.getText() + textFieldTitre.getText()); 				//affichage console des données titre2
		    	System.out.println(element2.getText() + textFieldElement.getText()); 			//affichage console des données element2
		    	System.out.println(nombre2.getText() + textFieldNombre.getText()); 				//affichage console des données nombre2
		    	//System.out.println(commentaire2.getText() + textAreaCommentaire.getText()); 	//affichage console des données commentaire2
		    	//partie compteurs
		    	System.out.println(titreCompteurs.getText()); 																	//affichage console du titre de la partie du formulaire
		    	System.out.println(typeCompteur.getText() + comboBoxTypeCompteur.getSelectedItem().toString()); 				//affichage console des données typeCompteur
		    	System.out.println(moisCompteur.getText() + comboBoxMoisCompteur.getSelectedItem().toString()); 				//affichage console des données moisCompteur
		    	System.out.println(consommation.getText() + textFieldConsommation.getText() + comboBoxUnite.getSelectedItem()); //affichage console des données consommation
		         
		    	/*---ecriture dans le fichier texte rapport---*/
		    	
		    	File f = new File ("rapport.txt"); //creation d'un rapport au format txt
		    	PrintWriter fw = null;
				try {
					fw = new PrintWriter (new BufferedWriter (new FileWriter (f)));
				} 
				catch (IOException e1) {
					e1.printStackTrace();
				}
		    	
		    	//partie redacteur
		    	fw.println (writerTitle.getText()); 					 	//ecriture du titre de la partie du formulare 	
		    	fw.println (writerName.getText() + writerNameField.getText()); 	 	//ecriture des donnees nom
		    	fw.println (writerAdr.getText() + writerAdrArea.getText()); 	 	//ecriture des donnees adr
		    	fw.println (writerPhone.getText() + finalWriterPhoneField.getText()); 	//ecriture des donnees tel
		    	fw.println (writerEmail.getText() + writerEmailField.getText()); 	//ecriture des donnees email
		    	fw.println (writerResponsibleName.getText() + writerResponsibleNameField.getText()); 	//ecriture des donnees nomCA
		    	//partie client
		    	fw.println (customerTitle.getText()); 								//ecriture du titre de la partie du formulaire
			    fw.println (siteName.getText() + siteNameField.getText());		//ecriture des données nomSite
			    fw.println (code.getText() + codeField.getText()); 				//ecriture des données code
			    fw.println (customerAdr.getText() + customerAdrArea.getText()); 		    //ecriture des données adrCl
			    fw.println (postalCode.getText() + finalPostalCodeField.getText());  //ecriture des données codePostal
			    fw.println (city.getText() + cityField.getText()); 			//ecriture des données ville
			    fw.println (customerPhone.getText() + finalTextFieldTelCl.getText()); 			//ecriture des données telCl
			    fw.println (customerMail.getText() + customerMailField.getText()); 		//ecriture des données emailCl
		    	//partie rapport
			    fw.println (titreRapport.getText()); 													//ecriture du titre de la partie du formulaire
		    	fw.println (rapportActivite.getText() + comboBoxRapport.getSelectedItem().toString()); 	//ecriture console des données rpportActivite
		    	fw.println (dateDebut.getText() + finalTextFieldDateDebut.getText()); 						//ecriture console des données codeDebut
		    	fw.println (dateFin.getText() + finalTextFieldDateFin.getText()); 							//ecriture console des données dateFin
		    	//partie bons preventifs
			    fw.println (titreBP.getText()); 												//ecriture du titre de la partie du formulaire
		    	/*fw.println (moisBP.getText() + comboBoxMoisBP.getSelectedItem().toString());	//ecriture des données moisBP
		    	fw.println (nbBPOuverts.getText() + textFieldNbBPOuverts.getText()); 			//ecriture des données nbBPOuverts
		    	fw.println (nbBPFermes.getText() + textFieldNbBPFermes.getText()); 				//ecriture des données nbBPFermes
		    	fw.println (commentaireBP.getText() + textAreaCommentaireBP.getText()); 		//ecriture des données commentaireBP
		    	//partie bons preventifs par domaine
			    */fw.println (titreBPDomaine.getText()); 										//ecriture du titre de la partie du formulaire
		    	fw.println (commentaireBPDomaine.getText() + textAreaCommentaireBPDomaine.getText()); 	//ecriture des données commentaireBPDomaine
		    	//partie arborescence libre
		    	/*fw.println (titreArboLibre.getText()); 								//ecriture du titre de la partie du formulaire
		    	/*fw.println (titre.getText() + textFieldTitre.getText()); 			//ecriture des données titre
		    	fw.println (element.getText() + textFieldElement.getText()); 		//ecriture des données element
		    	fw.println (nombre.getText() + textFieldNombre.getText()); 			//ecriture des données nombre
		    	fw.println (commentaire.getText() + textAreaCommentaire.getText()); //ecriture des données commentaire
		    	//partie domaines d'intervention
		    	/*fw.println(titreDI.getText()); 												//ecriture du titre de la partie du formulaire
		    	//fw.println(moisDI.getText() + comboBoxMoisBP.getSelectedItem().toString()); //ecriture des données moisDI
		    	fw.println(nbIntervention.getText() + textFieldNbIntervention.getText()); 	//ecriture des données nbIntervention
		    	//fw.println(commentaireDI.getText() + textAreaCommentaireBP.getText()); 		//ecriture des données commentaireDI
		    	//partie domaines d'intervention par etat
		    	System.out.println(titreDIEtat.getText()); 													//ecriture du titre de la partie du formulaire
		    	System.out.println (commentaireDIEtat.getText() + textAreaCommentaireDIEtat.getText()); 	//ecriture des données commentaireBPDomaine

		    	//partie arborescence libre 2
		    	fw.println(titreArboLibre2.getText()); 									//ecriture du titre de la partie du formulaire
		    	/*fw.println(titre2.getText() + textFieldTitre.getText()); 				//ecriture des données titre2
		    	fw.println(element2.getText() + textFieldElement.getText()); 			//ecriture des données element2
		    	fw.println(nombre2.getText() + textFieldNombre.getText()); 				//ecriture des données nombre2
		    	fw.println(commentaire2.getText() + textAreaCommentaire.getText()); 	//ecriture des données commentaire2
		    	*///partie compteurs
		    	/*fw.println(titreCompteurs.getText()); 																	//ecriture du titre de la partie du formulaire
		    	fw.println(typeCompteur.getText() + comboBoxTypeCompteur.getSelectedItem().toString()); 				//ecriture console des données typeCompteur
		    	fw.println(moisCompteur.getText() + comboBoxMoisCompteur.getSelectedItem().toString()); 				//ecriture console des données moisCompteur
		    	fw.println(consommation.getText() + textFieldConsommation.getText() + comboBoxUnite.getSelectedItem()); //ecriture console des données consommation
		    	*/
		    	fw.println ("\r\n"); //retour ligne
		    	fw.close();
		    	
		    	/*-----------------Ecriture du PDF-----------------*/
		    	
		    	final ProgressBarFrame pBarFrame = new ProgressBarFrame();
		    	
		    	SwingWorker<Void, Integer> pdfCreation = new SwingWorker<Void, Integer>() {
										
					@Override
					protected Void doInBackground() throws Exception {
						pBarFrame.updateBar(0);
						
						mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				    	mainFrame.setEnabled(false);
				    	
				    	final int incrementUnit = 5;
				    	
				    	int fontToFit = 0;
				    	
				    	// On prepare les donnees
				    	Collection<IDataHandler> datas = new ArrayList<IDataHandler>();
				    	
						/*-----------------Partie Redacteur-----------------*/
				    	
				    	IDataHandler writerPart = new DefaultDataHandler(writerTitle.getText());
						
				    	if (writerNameField.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + writerName.getText() + "\" de la partie " + writerTitle.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		writerPart.addString(writerNameField.getText(), writerName.getText());   // nom
				    	}
				    	
				    	if (writerAdrArea.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + writerAdr.getText() + "\" de la partie " + writerTitle.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		writerPart.addString(writerAdrArea.getText(), writerAdr.getText());   // adresse 
				    	}
				    	
				    	if (finalWriterPhoneField.getValue().equals("              ")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + writerPhone.getText() + "\" de la partie " + writerTitle.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		writerPart.addString(finalWriterPhoneField.getText(), writerPhone.getText());   // telephone 
				    	}
				    	
				    	if (writerEmailField.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + writerEmail.getText() + "\" de la partie " + writerTitle.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		writerPart.addString(writerEmailField.getText(), writerEmail.getText()); // email
				    	}
				    	
				    	if (writerResponsibleNameField.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + writerResponsibleName.getText() + "\" de la partie " + writerTitle.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		writerPart.addString(writerResponsibleNameField.getText(), writerResponsibleName.getText()); // nom du chage d'affaire
				    	}
				    	
				    	datas.add(writerPart);

				    	publish(pBarFrame.getProgress() + incrementUnit);
				    	
				    	/*-----------------Partie client-----------------*/
				    	
				    	IDataHandler clientPart = new DefaultDataHandler(customerTitle.getText());
				    	
				    	if (siteNameField.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + siteName.getText() + "\" de la partie " + customerTitle.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(siteNameField.getText(), siteName.getText());    // nom
				    	}
				    	
				    	if (codeField.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + code.getText() + "\" de la partie " + customerTitle.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(codeField.getText(), code.getText());       // code
				    	}
				    	
				    	if (customerAdrArea.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + customerAdr.getText() + "\" de la partie " + customerTitle.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(customerAdrArea.getText(), customerAdr.getText());      // adresse
				    	}
				    	
				    	if (finalPostalCodeField.getValue().equals("      ")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + postalCode.getText() + "\" de la partie " + customerTitle.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(finalPostalCodeField.getText(), postalCode.getText()); // code postal
				    	}
				    	
				    	if (cityField.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + city.getText() + "\" de la partie " + customerTitle.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(cityField.getText(), city.getText());      // ville
				    	}
				    	
				    	if (finalTextFieldTelCl.getValue().equals("              ")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + customerPhone.getText() + "\" de la partie " + customerTitle.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(finalTextFieldTelCl.getText(), customerPhone.getText());      // telephone client
				    	}
				    	
				    	if (customerMailField.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + customerMail.getText() + "\" de la partie " + customerTitle.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(customerMailField.getText(), customerMail.getText());    // email client
				    	}
				    	
				    	if (logoIcon != null) {
				    		clientPart.addImage(logoIcon.getImage());
				    	}
				    	
				    	datas.add(clientPart);
				    	
						publish(pBarFrame.getProgress() + incrementUnit);
				    	
				    	/*-----------------Partie Rapport-----------------*/
				    	
				    	IDataHandler reportPart = new DefaultDataHandler(titreRapport.getText());
						
						reportPart.addString(comboBoxRapport.getSelectedItem().toString(), rapportActivite.getText()); // Type de rapport
						
				    	if (finalTextFieldDateDebut.getText().equals("  /  /    ")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + dateDebut.getText() + "\" de la partie " + titreRapport.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		reportPart.addString(finalTextFieldDateDebut.getText(), dateDebut.getText());
				    	}
				    	
				    	if (finalTextFieldDateFin.getText().equals("  /  /    ")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + dateFin.getText() + "\" de la partie " + titreRapport.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		reportPart.addString(finalTextFieldDateFin.getText(), dateFin.getText());
				    	}
				    	
				    	datas.add(reportPart);
				    	
						publish(pBarFrame.getProgress() + incrementUnit);

				    	/*-----------------Partie Bons preventifs-----------------*/
				    	
				    	IDataHandler preventivesVouchers = new DefaultDataHandler(titreBP.getText());
				    	
				    	Iterator<JComboBox<String>> preventivesVouchersMonthsIter   = preventivesVouchersMonths.iterator();
				    	Iterator<JTextField>        nbPreventivesVouchersOpenedIter = nbPreventivesVouchersOpened.iterator();
				    	Iterator<JTextField>        nbPreventivesVouchersClosedIter = nbPreventivesVouchersClosed.iterator();
				    	
				    	int counter = 1;
				    	
				    	DefaultCategoryDataset barChartDatas = new DefaultCategoryDataset();
				    	IChartGenerator chartGenerator = new DefaultChartGenerator();
				    	
				    	while (preventivesVouchersMonthsIter.hasNext()) {
				    		
				    		JComboBox<String> comboBoxMoisBP        = preventivesVouchersMonthsIter.next();
				    		JTextField        textFieldNbBPOuverts  = nbPreventivesVouchersOpenedIter.next();
				    		JTextField        textFieldNbBPFermes   = nbPreventivesVouchersClosedIter.next();
		 
							if (counter <= 1) {
				    		
					    		//preventivesVouchers.addString(comboBoxMoisBP.getSelectedItem().toString(), 
					    			//	PREVENTIVE_VOUCHER_MONTH_LABELS[0]);
								
						    	if (textFieldNbBPOuverts.getText().equals("")) {
						    		JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"" +  PREVENTIVE_VOUCHER_MONTH_LABELS[1] + "\" de la partie " + titreBP.getText() + 
						    				" du mois numéro " + counter + " doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else if (!OperationUtilities.isNumeric(textFieldNbBPOuverts.getText())) {
						    		JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"" + PREVENTIVE_VOUCHER_MONTH_LABELS[1] + "\" de la partie " + titreBP.getText() + 
						    				" du mois numéro " + counter + " doit être un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else {
									//preventivesVouchers.addString(textFieldNbBPOuverts.getText(), PREVENTIVE_VOUCHER_MONTH_LABELS[1]);
									
									barChartDatas.addValue(Double.parseDouble(textFieldNbBPOuverts.getText()), "Nombre de bons préventifs ouverts", 
											comboBoxMoisBP.getSelectedItem().toString());
						    	}
						    	
						    	if (textFieldNbBPFermes.getText().equals("")) {
						    		JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"" + PREVENTIVE_VOUCHER_MONTH_LABELS[2] + "\" de la partie " + titreBP.getText() +
						    				" du mois numéro " + counter + " doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else if (!OperationUtilities.isNumeric(textFieldNbBPFermes.getText())) {
						    		JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"" + PREVENTIVE_VOUCHER_MONTH_LABELS[2] + "\" de la partie " + titreBP.getText() +
						    				" du mois numéro " + counter + " doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else {
						    		//preventivesVouchers.addString(textFieldNbBPFermes.getText(), PREVENTIVE_VOUCHER_MONTH_LABELS[2]);
						    		barChartDatas.addValue(Double.parseDouble(textFieldNbBPFermes.getText()), "Nombre de bons préventifs clôturés", comboBoxMoisBP.getSelectedItem().toString());
						    	}						    	
						    	++counter;
							}
							else if (!textFieldNbBPOuverts.getText().equals("") ||
									!textFieldNbBPFermes.getText().equals("")) {
								
								//preventivesVouchers.addString(comboBoxMoisBP.getSelectedItem().toString(), PREVENTIVE_VOUCHER_MONTH_LABELS[0]);
								
						    	if (textFieldNbBPOuverts.getText().equals("")) {
						    		JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"" +  PREVENTIVE_VOUCHER_MONTH_LABELS[1] + "\" de la partie " + titreBP.getText() + 
						    				" du mois numéro " + counter + " doit être remplis avec un nombre "
						    						+ "(Les bons préventifs completement vides seront ignorés)", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else if (!OperationUtilities.isNumeric(textFieldNbBPOuverts.getText())) {
						    		JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"" + PREVENTIVE_VOUCHER_MONTH_LABELS[1] + "\" de la partie " + titreBP.getText() + 
						    				" du mois numéro " + counter + " doit être un nombre"
						    						+ " (Les bons préventifs completement vides seront ignorés)", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else {
									//preventivesVouchers.addString(textFieldNbBPOuverts.getText(), PREVENTIVE_VOUCHER_MONTH_LABELS[1]);
									barChartDatas.addValue(Double.parseDouble(textFieldNbBPOuverts.getText()), "Nombre de bons préventifs ouverts", 
											comboBoxMoisBP.getSelectedItem().toString());
						    	}
						    	
						    	if (textFieldNbBPFermes.getText().equals("")) {
						    		JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"" + PREVENTIVE_VOUCHER_MONTH_LABELS[2] + "\" de la partie " + titreBP.getText() +
						    				" du mois numéro " + counter + " doit être remplis avec un nombre "
						    						+ "(Les bons préventifs completement vides seront ignorés)", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else if (!OperationUtilities.isNumeric(textFieldNbBPFermes.getText())) {
						    		JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"" + PREVENTIVE_VOUCHER_MONTH_LABELS[2] + "\" de la partie " + titreBP.getText() +
						    				" du mois numéro " + counter + " doit être remplis avec un nombre "
						    						+ "(Les bons préventifs completement vides seront ignorés)", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else {
						    		//preventivesVouchers.addString(textFieldNbBPFermes.getText(), PREVENTIVE_VOUCHER_MONTH_LABELS[2]);
						    		barChartDatas.addValue(Double.parseDouble(textFieldNbBPFermes.getText()), 
						    				"Nombre de bons préventifs clôturés", 
						    				comboBoxMoisBP.getSelectedItem().toString());
						    	}
						    	
						    	++counter;
							}
				    	}
				    	
				    	if (!textAreaCommentaireBP.getText().equals("")) {
				    		preventivesVouchers.addString(textAreaCommentaireBP.getText(), commentaireBP.getText());
				    	}
				    	
				    	if (preventivesVouchersMonths.size() < monthNumber || preventivesVouchersMonths.size() > monthNumber) {
				    		final int dialogResult = JOptionPane.showConfirmDialog (contentPane, 
	    							"Le nombre de mois de " + titreBP.getText() + " (" + preventivesVouchersMonths.size() + 
	    							"), n'est pas conforme au type de rapport (" +
				    		        comboBoxRapport.getSelectedItem().toString() + ")." + 
	    						    System.lineSeparator() + "Voulez-vous continuer?", 
	    							"Erreur", JOptionPane.YES_NO_OPTION);
	    					
				    		if(dialogResult == JOptionPane.NO_OPTION){
	    						stopPdfCreation(pBarFrame);
	    						return null;
	    					}
	    					else {
	    						pBarFrame.toFront();
	    					}
				    	}
				    	
				    	if (counter > 1) {
					    	try {
								JFreeChart barChart = chartGenerator.generateBarChart(titreBP.getText(), 
										"Mois", "Nombre de bons préventifs", barChartDatas, true, fontToFit);
								
								preventivesVouchers.addJFreeChart(barChart);
				    		} 
				    		catch (Exception e) {
				    			e.printStackTrace();
								JOptionPane.showMessageDialog(contentPane, "Erreur lors de la création du graphe en bare dans la partie"
										+ titreBP.getText() + " : \n"
										+ e.getMessage(), "Erreur", 
										JOptionPane.WARNING_MESSAGE);
							}
					    	
					    	
				    	}
				    	
				    	if (!preventivesVouchers.isEmpty()) {
				    		datas.add(preventivesVouchers);
				    	}
				    	
						publish(pBarFrame.getProgress() + incrementUnit);
				    
				    	/*-----------------Partie Bons preventifs par domaines-----------------*/
						
						fontToFit = 0;
				    	
				    	IDataHandler domainPreventivesVouchers = new DefaultDataHandler(titreBPDomaine.getText());
				    	
				    	// On obtient l'iterator des domaines
				    	Iterator<JCheckBox> domainsIter = preventiveVoucherDomains.iterator();
				    	
				    	// On obtient l'iterator des pourcentages correspondant
				    	Iterator<JFormattedTextField> pourcentsIter = preventiveVoucherPourcentageFields.iterator();
				    	
				    	barChartDatas = new DefaultCategoryDataset();
				    	DefaultPieDataset pieChartDatas = new DefaultPieDataset();
				    	
				    	Map<String, Double> pourcentages = new HashMap<String, Double>();
				    	Double total = new Double (0f);
				    	
				    	boolean continueAbove100 = false;
				    	
				    	// On itere sur les domaines
				    	while (domainsIter.hasNext()) {
				    		// On obtient le JCheckBox courrant
				    		JCheckBox currentDomain = domainsIter.next();
				    		
				    		// On obtient le JFormattedTextField courant correspondant
				    		JFormattedTextField currentPourcent = pourcentsIter.next();
				    		// Si la case est coché 
				    		if (currentDomain.isSelected()) {
				    			
				    			// Mais le pourcentage n'est pas renseigné
				    			if (currentPourcent.getText().equals("  .  %")) {
					    			JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreBPDomaine.getText() + 
						    				" doit être remplis si la case a été cochée", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
					    			stopPdfCreation(pBarFrame);
					    			return null;
				    			}
				    			else {		    				
				    				//domainPreventivesVouchers.addString(currentPourcent.getText(), currentDomain.getText() + " : ");
				    				
				    				final int maxSize = 10;
				    				
				    				if (currentDomain.getText().length() > maxSize) {
				    					fontToFit = 6;
				    					currentDomain.setText(currentDomain.getText().substring(0, maxSize) + System.lineSeparator() + 
				    							currentDomain.getText().substring(maxSize, currentDomain.getText().length()));
				    				}
				    				
				    				double currentPourcentage = 0;
				    				
				    				String pourcentageNumber = currentPourcent.getText().substring(0, 5);
				    				
				    				if (!OperationUtilities.isNumeric(pourcentageNumber)) {
				    					JOptionPane.showMessageDialog(contentPane, 
							    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreBPDomaine.getText() + 
							    				" n'est pas un nombre valide", "Erreur", 
												JOptionPane.WARNING_MESSAGE);
				    					stopPdfCreation(pBarFrame);
				    					return null;
				    				}
				    				
				    				try {
				    					currentPourcentage = Double.parseDouble(pourcentageNumber);
				    				}
				    				catch (NumberFormatException e) {
				    					JOptionPane.showMessageDialog(contentPane, 
							    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreBPDomaine.getText() + 
							    				" n'est pas un nombre valide", "Erreur", 
												JOptionPane.WARNING_MESSAGE);
				    					stopPdfCreation(pBarFrame);
				    					return null;
				    				}
				    				
				    				total += currentPourcentage;
				    				
				    				if (total > 100 && !continueAbove100) {
				    					int dialogResult = JOptionPane.showConfirmDialog (contentPane, 
				    							"Les pourcentages obtenus dans la parties " + titreBPDomaine.getText() + " sont supérieurs"
				    							+ " à 100% (" + total + "%) à partir du champ \"" + currentDomain.getText() + "\".\n "
				    							+ "Voulez-vous continuer tout de même (les pourcentages seront recalculés de manière relative)?",
				    							"Erreur", JOptionPane.YES_NO_OPTION);
				    					if(dialogResult == JOptionPane.NO_OPTION){
				    						stopPdfCreation(pBarFrame);
				    						return null;
				    					}
				    					else {
				    						continueAbove100 = true;
				    						pBarFrame.toFront();
				    					}
				    				}
				    				pourcentages.put(currentDomain.getText(), new Double(currentPourcentage));
				    			}
				    		}
				    	}		
				    	
				    	if (!pourcentages.isEmpty()) {
				    		
				    		if (total < 100) {
				    			int dialogResult = JOptionPane.showConfirmDialog (contentPane, 
		    							"Les pourcentages obtenus dans la parties " + titreBPDomaine.getText() + " sont inférieurs"
		    							+ " à 100% (" + total + "%).\n "
		    							+ "Voulez-vous continuer tout de même (les pourcentages seront recalculés de manière relative)?", 
		    							"Erreur", JOptionPane.YES_NO_OPTION);
		    					if(dialogResult == JOptionPane.NO_OPTION){
		    						stopPdfCreation(pBarFrame);
		    						return null;
		    					}
		    					else {
		    						pBarFrame.toFront();
		    					}
				    		}
				    		
				    		Iterator<Entry<String, Double>> mapIter = pourcentages.entrySet().iterator();
				    		
				    		while (mapIter.hasNext()) {
				    			Map.Entry<String, Double> currentEntry = mapIter.next();
				    			
				    			double currentRelativePourcentage = currentEntry.getValue() / total;
				    			
				    			
				    			final String finalPourcentage = String.format("%.2f", (currentRelativePourcentage * 100)) +
				    					"%";
				    			
				    			pieChartDatas.setValue(currentEntry.getKey() + " : " + finalPourcentage,
				    					currentRelativePourcentage);
				    			
				    			barChartDatas.addValue(currentRelativePourcentage * 100, "Pourcentage d'avancement", currentEntry.getKey());
				    		}
				    		
				    		try {
								JFreeChart barChart = chartGenerator.generateBarChart(titreBPDomaine.getText(), 
										"Domaines techniques", "Pourcentages d'avancement", barChartDatas, false, fontToFit);
								
								domainPreventivesVouchers.addJFreeChart(barChart);
				    		} 
				    		catch (Exception e) {
				    			e.printStackTrace();
								JOptionPane.showMessageDialog(contentPane, "Erreur lors de la création du graphe en bare dans la partie"
										+ titreBPDomaine.getText() + " : \n"
										+ e.getMessage(), "Erreur", 
										JOptionPane.WARNING_MESSAGE);
							}
				    		
				    		try {
								JFreeChart pieChart = chartGenerator.generatePieChart(titreBPDomaine.getText(), pieChartDatas, false);
								
								domainPreventivesVouchers.addJFreeChart(pieChart);
				    		} 
				    		catch (Exception e) {
				    			e.printStackTrace();
								JOptionPane.showMessageDialog(contentPane, "Erreur lors de la création du graphe en camembert dans la partie " +
										titreBPDomaine.getText() + ": \n"
										+ e.getMessage(), "Erreur", 
										JOptionPane.WARNING_MESSAGE);
							}
				    		
				    		datas.add(domainPreventivesVouchers);
				    		
							publish(pBarFrame.getProgress() + incrementUnit);
				    	}
				    	
				    	/*-----------------Partie arborescence libre 1-----------------*/
				    	
				    	fontToFit = 0;
				    	
				    	Iterator<FreeTree> freeTrees1Iter = freeTrees1.iterator();
				    	
				    	while (freeTrees1Iter.hasNext()) {
				    		Boolean isElement = false;
				    		
				    		FreeTree currentTree = freeTrees1Iter.next();
				    		
				    		if (currentTree.getTitleTextField().getText().equals("")) {
				    			JOptionPane.showMessageDialog(contentPane, 
					    				"le champs \"Titre : \" de la partie Arborescence Libre 1" +
					    				" doit être remplis", "Erreur", 
										JOptionPane.WARNING_MESSAGE);
					    		stopPdfCreation(pBarFrame);
					    		return null;
				    		}
				    		
				    		IDataHandler freeTree1 = new DefaultDataHandler(currentTree.getTitleTextField().getText());
				    		
				    		barChartDatas = new DefaultCategoryDataset();
				    		
				    		Iterator<JTextField> currentTreeElementIter = currentTree.getElements().iterator();
				    		Iterator<JTextField> currentTreeElementNumber = currentTree.getElementNumbers().iterator();
				    		
				    		while (currentTreeElementIter.hasNext()) {
				    			JTextField currentElement = currentTreeElementIter.next();
				    			JTextField currentElementNumber = currentTreeElementNumber.next();
				    			
				    			if (currentElement.getText().equals("")) {
						    		JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"Elément : \" de la partie " + currentTree.getTitleTextField().getText() + 
						    				" (Arboresence libre 1) doit être remplis", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
				    			else if (currentElementNumber.getText().equals("") || 
				    					!OperationUtilities.isNumeric(currentElementNumber.getText())) {
				    				JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"Nombre : \" de l'élément " + currentElement.getText() + 
						    				" de la partie " + currentTree.getTitleTextField().getText() + 
						    				" (Arboresence libre 1) doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
				    			}
				    			else {
				    				//freeTree1.addString(currentElementNumber.getText(), currentElement.getText() + " : ");
				    				barChartDatas.addValue(Double.parseDouble(currentElementNumber.getText()), "Nombre", currentElement.getText());
				    				
				    				isElement = true;
				    			}
				    			
				    			if (currentElement.getText().length() > 6) {
				    				fontToFit = 8;
				    			}
				    		}
				    		
				    		if (!currentTree.getTextAreaComment().getText().equals("")) {
				    			freeTree1.addString(currentTree.getTextAreaComment().getText(), "Commentaire : ");
				    		}
				    		
				    		if (isElement) {
					    		try {
									JFreeChart barchart = chartGenerator.generateBarChart(currentTree.getTitleTextField().getText(),
											"Element", "Nombre", barChartDatas, true, fontToFit);
									
									freeTree1.addJFreeChart(barchart);
								} 
					    		catch (Exception e) {
					    			e.printStackTrace();
									JOptionPane.showMessageDialog(contentPane, "Erreur lors de la création du graphe en barre dans la partie " +
											currentTree.getTitleTextField().getText() + " (Arboresence libre 1) : \n"
											+ e.getMessage(), "Erreur", 
											JOptionPane.WARNING_MESSAGE);
								}
				    		}
				    		
				    		datas.add(freeTree1);
				    	}
				    	
						publish(pBarFrame.getProgress() + incrementUnit);
				
				    	/*-----------------Partie demande d'intervention-----------------*/
						
						fontToFit = 0;

				    	IDataHandler interventionDemand = new DefaultDataHandler(titreDI.getText());
				    	
				    	barChartDatas = new DefaultCategoryDataset();
				    	
				    	Iterator<JComboBox<String>> interventionMonthsIter = interventionMonths.iterator();
				    	Iterator<JTextField> interventionNumbersIter = interventionNumbers.iterator();
				    	
				    	while (interventionMonthsIter.hasNext()) {
				    		JComboBox<String> currentInterventionMonths = interventionMonthsIter.next();
				    		JTextField currentInterventionNumber = interventionNumbersIter.next();
				    		
				    		//interventionDemand.addString(currentInterventionMonths.getSelectedItem().toString(), "Mois : ");
					    	
					    	if (currentInterventionNumber.getText().equals("") ||
					    			!OperationUtilities.isNumeric(currentInterventionNumber.getText())) {
					    		JOptionPane.showMessageDialog(mainFrame, 
					    				"le champs \"Nombre d'interventions : \" de la partie " + titreDI.getText() + 
					    				" du mois " + currentInterventionMonths.getSelectedItem().toString() +
					    				" doit être remplis avec un nombre", "Erreur", 
										JOptionPane.WARNING_MESSAGE);
					    		stopPdfCreation(pBarFrame);
					    		return null;
					    	}
					    	else {
					    		//interventionDemand.addString(currentInterventionNumber.getText(), "Nombre d'interventions : ");
					    		barChartDatas.addValue(Double.parseDouble(currentInterventionNumber.getText()), "Nombre", 
					    				currentInterventionMonths.getSelectedItem().toString());
					    	}	
				    	}
				    	
				    	if (!textAreaCommentaireDI.getText().equals("")) {
				    		interventionDemand.addString(textAreaCommentaireDI.getText(), commentaireDI.getText());
				    	}
				    	
				    	if (interventionMonths.size() < monthNumber || interventionMonths.size() > monthNumber) {
				    		final int dialogResult = JOptionPane.showConfirmDialog (contentPane, 
	    							"Le nombre de mois de " + titreDI.getText() + " (" + interventionMonths.size() + 
	    							"), n'est pas conforme au type de rapport (" +
				    		        comboBoxRapport.getSelectedItem().toString() + ")." + 
	    						    System.lineSeparator() + "Voulez-vous continuer?", 
	    							"Erreur", JOptionPane.YES_NO_OPTION);
	    					
				    		if(dialogResult == JOptionPane.NO_OPTION){
	    						stopPdfCreation(pBarFrame);
	    						return null;
	    					}
	    					else {
	    						pBarFrame.toFront();
	    					}
				    	}
				    	
				    	if (!interventionDemand.isEmpty()) {
					    	try {
								JFreeChart barChart = chartGenerator.generateBarChart(titreDI.getText(), 
										"Mois", "Nombre d'intervention", barChartDatas, true, fontToFit);
								
								interventionDemand.addJFreeChart(barChart);
				    		} 
				    		catch (Exception e) {
				    			e.printStackTrace();
								JOptionPane.showMessageDialog(contentPane, "Erreur lors de la création du graphe en bare dans la partie"
										+ titreDI.getText() + " : \n"
										+ e.getMessage(), "Erreur", 
										JOptionPane.WARNING_MESSAGE);
							}
					    	
					    	datas.add(interventionDemand);
				    	}
				    	 	
						publish(pBarFrame.getProgress() + incrementUnit);
				    	
				    	/*-----------------Partie demande d'intervention par etat-----------------*/
						
						fontToFit = 0;
				    	
				    	IDataHandler stateInterventionDemand = new DefaultDataHandler(titreDIEtat.getText());
				    	
				    	
				    	pieChartDatas = new DefaultPieDataset();
				    	
				    	pourcentages = new HashMap<String, Double>();
				    	total = new Double (0f);
				    	
				    	Iterator<JCheckBox> stateJComboBoxIter = states.iterator();
				    	Iterator<JTextField> nbStatesJTextFieldIter = nbStates.iterator();
				    	
				    	while (stateJComboBoxIter.hasNext()) {
				    		JCheckBox currentState = stateJComboBoxIter.next();
				    		JTextField currentNbState = nbStatesJTextFieldIter.next();
				    		
				    		if (currentState.isSelected()) {
				    			if (currentNbState.getText().equals("")) {
				    				JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"" + currentState.getText() + "\" de la partie " + titreDIEtat.getText() + 
						    				" doit être remplis avec un nombre si la case est cochée", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
				    				stopPdfCreation(pBarFrame);
				    				return null;
				    			}
				    			else if (!OperationUtilities.isNumeric(currentNbState.getText())) {
				    				JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"" + currentState.getText() + "\" de la partie " + titreDIEtat.getText() + 
						    				" doit être un nombre si la case est cochée", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
				    				stopPdfCreation(pBarFrame);
				    				return null;
				    			}
				    			else {
				    				//stateInterventionDemand.addString(currentNbState.getText(), currentState.getText() + " : ");
				    				
				    				if (currentState.getText().length() > 6) {
				    					fontToFit = 8;
				    				}
				    				
				    				Integer currentNumber = 0;
				    				
				    				String parsedNumber = currentNbState.getText();
				    				
				    				if (!OperationUtilities.isNumeric(parsedNumber)) {
				    					JOptionPane.showMessageDialog(contentPane, 
							    				"le champs \"" + currentState.getText() + "\" de la partie " + titreDIEtat.getText() + 
							    				" n'est pas un nombre valide", "Erreur", 
												JOptionPane.WARNING_MESSAGE);
				    					stopPdfCreation(pBarFrame);
				    					return null;
				    				}
				    				
				    				try {
				    					currentNumber = Integer.parseInt(parsedNumber);
				    				}
				    				catch (NumberFormatException e) {
				    					JOptionPane.showMessageDialog(contentPane, 
							    				"le champs \"" + currentState.getText() + "\" de la partie " + titreDIEtat.getText() + 
							    				" n'est pas un nombre valide", "Erreur", 
												JOptionPane.WARNING_MESSAGE);
				    					stopPdfCreation(pBarFrame);
				    					return null;
				    				}
				    				
				    				total += currentNumber;
				    				pourcentages.put(currentState.getText(), new Double(currentNumber));
				    			}
				    		}
				    	}
				    	
				    	if (!textAreaCommentaireDI.getText().equals("")) {
				    		stateInterventionDemand.addString(textAreaCommentaireDI.getText(), commentaireDI.getText());
				    	}
				    	
				    	if (pieChartDatas.getItemCount() > 0) {
				    		
				    		Iterator<Entry<String, Double>> mapIter = pourcentages.entrySet().iterator();
				    		
				    		while (mapIter.hasNext()) {
				    			Map.Entry<String, Double> currentEntry = mapIter.next();
				    			
				    			double currentRelativePourcentage = currentEntry.getValue() / total;
				    			pieChartDatas.setValue(currentEntry.getKey() + " : " + 
				    					String.format("%.2f", (currentRelativePourcentage * 100)) + "%",
				    					currentRelativePourcentage);
				    		}
				    		
				    		try {
								JFreeChart pieChart = chartGenerator.generatePieChart(titreBPDomaine.getText(), pieChartDatas, false);
								
								stateInterventionDemand.addJFreeChart(pieChart);
				    		} 
				    		catch (Exception e) {
				    			e.printStackTrace();
								JOptionPane.showMessageDialog(contentPane, "Erreur lors de la création du graphe en camembert dans la partie "
										+ titreDIEtat.getText() + " : \n"
										+ e.getMessage(), "Erreur", 
										JOptionPane.WARNING_MESSAGE);
								stopPdfCreation(pBarFrame);
							}
				    		
				    		datas.add(stateInterventionDemand);
				    		
							publish(pBarFrame.getProgress() + incrementUnit);
				    	}
				    	
				    	/*-----------------Partie demande d'intervention par domaine-----------------*/
				    	
				    	fontToFit = 0;
				    	
				    	IDataHandler domainInterventionDemand = new DefaultDataHandler(titreDIDomaine.getText());
				    	
				    	// On obtient l'iterator des domaines
				    	Iterator<JCheckBox> interventionDomainsIter = interventionDomains.iterator();
				    	
				    	// On obtient l'iterator des pourcentages correspondant
				    	Iterator<JFormattedTextField> interventionPourcentsIter = interventionPourcents.iterator();
				    	
				    	DefaultPieDataset interventionPieChartDatas = new DefaultPieDataset();
				    	
				    	pourcentages = new HashMap<String, Double>();
				    	total = new Double (0f);
				    	
				    	continueAbove100 = false;
				    	
				    	// On itere sur les domaines
				    	while (interventionDomainsIter.hasNext()) {
				    		// On obtient le JCheckBox courrant
				    		JCheckBox currentDomain = interventionDomainsIter.next();
				    		
				    		// On obtient le JFormattedTextField courant correspondant
				    		JFormattedTextField currentPourcent = interventionPourcentsIter.next();
				    		
				    		// Si la case est coché 
				    		if (currentDomain.isSelected()) {
				    			
				    			// Mais le pourcentage n'est pas renseigné
				    			if (currentPourcent.getText().equals("  .  %")) {
					    			JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreDIDomaine.getText() + 
						    				" doit être remplis si la case a été cochée", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
					    			stopPdfCreation(pBarFrame);
					    			return null;
				    			}
				    			else {		    				
				    				//domainInterventionDemand.addString(currentPourcent.getText(), currentDomain.getText() + " : ");
				    				
				    				if (currentDomain.getText().length() > 6) {
				    					fontToFit = 8;
				    				}
				    				
				    				double currentPourcentage = 0;
				    				
				    				String pourcentageNumber = currentPourcent.getText().substring(0, 5);
				    				
				    				if (!OperationUtilities.isNumeric(pourcentageNumber)) {
				    					JOptionPane.showMessageDialog(contentPane, 
							    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreDIDomaine.getText() + 
							    				" n'est pas un nombre valide", "Erreur", 
												JOptionPane.WARNING_MESSAGE);
				    					stopPdfCreation(pBarFrame);
				    					return null;
				    				}
				    				
				    				try {
				    					currentPourcentage = Double.parseDouble(pourcentageNumber);
				    				}
				    				catch (NumberFormatException e) {
				    					JOptionPane.showMessageDialog(contentPane, 
							    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreDIDomaine.getText() + 
							    				" n'est pas un nombre valide", "Erreur", 
												JOptionPane.WARNING_MESSAGE);
				    					stopPdfCreation(pBarFrame);
				    					return null;
				    				}
				    				
				    				total += currentPourcentage;
				    				
				    				if (total > 100 && !continueAbove100) {
				    					int dialogResult = JOptionPane.showConfirmDialog (contentPane, 
				    							"Les pourcentages obtenus dans la parties " + titreDIDomaine.getText() + " sont supérieurs"
				    							+ " à 100% (" + total + "%) à partir du champ \"" + currentDomain.getText() + "\".\n "
				    							+ "Voulez-vous continuer tout de même (les pourcentages seront recalculés de manière relative)?",
				    							"Erreur", JOptionPane.YES_NO_OPTION);
				    					if(dialogResult == JOptionPane.NO_OPTION){
				    						stopPdfCreation(pBarFrame);
				    						return null;
				    					}
				    					else {
				    						continueAbove100 = true;
				    						pBarFrame.toFront();
				    					}
				    				}
				    				pourcentages.put(currentDomain.getText(), new Double(currentPourcentage));
				    			}
				    		}
				    	}		
				    	
				    	if (!textAreaComDIDomaine.getText().equals("")) {
				    		domainInterventionDemand.addString(textAreaComDIDomaine.getText(), commentaireDIDomaine.getText());
				    	}
				    	
				    	if (!pourcentages.isEmpty()) {
				    		
				    		if (total < 100) {
				    			int dialogResult = JOptionPane.showConfirmDialog (contentPane, 
		    							"Les pourcentages obtenus dans la parties " + titreDIDomaine.getText() + " sont inférieurs"
		    							+ " à 100% (" + total + "%).\n "
		    							+ "Voulez-vous continuer tout de même (les pourcentages seront recalculés de manière relative)?", 
		    							"Erreur", JOptionPane.YES_NO_OPTION);
		    					if(dialogResult == JOptionPane.NO_OPTION){
		    						stopPdfCreation(pBarFrame);
		    						return null;
		    					}
		    					else {
		    						pBarFrame.toFront();
		    					}
				    		}
				    		
				    		Iterator<Entry<String, Double>> mapIter = pourcentages.entrySet().iterator();
				    		
				    		while (mapIter.hasNext()) {
				    			Map.Entry<String, Double> currentEntry = mapIter.next();
				    			
				    			double currentRelativePourcentage = currentEntry.getValue() / total;
				    			interventionPieChartDatas.setValue(currentEntry.getKey() + " : " +
				    					String.format("%.2f", (currentRelativePourcentage * 100)) + "%",
				    					currentRelativePourcentage);
				    		}
				    		
				    		try {
								JFreeChart pieChart = chartGenerator.generatePieChart(titreDIDomaine.getText(), interventionPieChartDatas, false);
								
								domainInterventionDemand.addJFreeChart(pieChart);
				    		} 
				    		catch (Exception e) {
				    			e.printStackTrace();
								JOptionPane.showMessageDialog(contentPane, "Erreur lors de la création du graphe en camembert dans la partie " +
										titreDIDomaine.getText() + ": \n"
										+ e.getMessage(), "Erreur", 
										JOptionPane.WARNING_MESSAGE);
								stopPdfCreation(pBarFrame);
							}
				    		
				    		datas.add(domainInterventionDemand);
				    		
							publish(pBarFrame.getProgress() + incrementUnit);
				    	}
				    	
				    	/*-----------------Partie arborescence libre 2-----------------*/
				    	
				    	fontToFit = 0;
				    	
				    	Iterator<FreeTree> freeTrees2Iter = freeTrees2.iterator();
				    	
				    	while (freeTrees2Iter.hasNext()) {
				    		
				    		FreeTree currentTree = freeTrees2Iter.next();
				    		
				    		if (currentTree.getTitleTextField().getText().equals("")) {
				    			JOptionPane.showMessageDialog(contentPane, 
					    				"le champs \"Titre : \" de la partie Arborescence Libre 2" +
					    				" doit être remplis", "Erreur", 
										JOptionPane.WARNING_MESSAGE);
					    		stopPdfCreation(pBarFrame);
					    		return null;
				    		}
				    		
				    		IDataHandler freeTree = new DefaultDataHandler(currentTree.getTitleTextField().getText());
				    		
				    		barChartDatas = new DefaultCategoryDataset();
				    		
				    		Iterator<JTextField> currentTreeElementIter = currentTree.getElements().iterator();
				    		Iterator<JTextField> currentTreeElementNumber = currentTree.getElementNumbers().iterator();
				    		
				    		while (currentTreeElementIter.hasNext()) {
				    			JTextField currentElement = currentTreeElementIter.next();
				    			JTextField currentElementNumber = currentTreeElementNumber.next();
				    			
				    			if (currentElement.getText().equals("")) {
						    		JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"Elément : \" de la partie " + currentTree.getTitleTextField().getText() + 
						    				" (Arboresence libre 2) doit être remplis", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
				    			else if (currentElementNumber.getText().equals("") || 
				    					!OperationUtilities.isNumeric(currentElementNumber.getText())) {
				    				JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"Nombre : \" de l'élément " + currentElement.getText() + 
						    				" de la partie " + currentTree.getTitleTextField().getText() + 
						    				" (Arboresence libre 2) doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
				    			}
				    			else {
				    				//freeTree.addString(currentElementNumber.getText(), currentElement.getText() + " : ");
				    				
				    				if (currentElement.getText().length() > 6) {
				    					fontToFit = 8;
				    				}
				    				
				    				barChartDatas.addValue(Double.parseDouble(currentElementNumber.getText()), "Nombre", 
				    						currentElement.getText());
				    			}
				    		}
				    		
				    		if (!currentTree.getTextAreaComment().getText().equals("")) {
				    			freeTree.addString(currentTree.getTextAreaComment().getText(), "Commentaire : ");
				    		}
				    		
				    		if (barChartDatas.getColumnCount() > 0) {
					    		try {
									JFreeChart barchart = chartGenerator.generateBarChart(currentTree.getTitleTextField().getText(),
											"Element", "Nombre", barChartDatas, true, fontToFit);
									
									freeTree.addJFreeChart(barchart);
								} 
					    		catch (Exception e) {
					    			e.printStackTrace();
									JOptionPane.showMessageDialog(contentPane, "Erreur lors de la création du graphe en barre dans la partie " +
											currentTree.getTitleTextField().getText() + " (Arboresence libre 2) : \n"
											+ e.getMessage(), "Erreur", 
											JOptionPane.WARNING_MESSAGE);
								}
				    		}
				    		
				    		datas.add(freeTree);
				    	}
				    	
						publish(pBarFrame.getProgress() + incrementUnit);
						
						/*-----------------Partie compteurs-----------------*/
						
						fontToFit = 7;
						
						counter = 0;
						
						Iterator<Meter> meterIter = meters.iterator();
						
						while (meterIter.hasNext()) {
							
							IDataHandler currentMeterData = new DefaultDataHandler(meterTitle.getText());
							
							barChartDatas = new DefaultCategoryDataset();
							
							Meter currentMeter = meterIter.next();
							
							currentMeterData.addString(currentMeter.getComboBoxTypeCompteur().getSelectedItem().toString(),
									"Type du compteur : ");
							
							Iterator<JComboBox<String>> currentMonthIter = currentMeter.getMonthComboBoxes().iterator();
							Iterator<JTextField> currentConsumptionIter = currentMeter.getMonthConsumptions().iterator();
							Iterator<String> currentUnitIter = currentMeter.getMonthUnits().iterator();
							
							String currentUnit = null;
							
							while (currentMonthIter.hasNext()) {
								
								JComboBox<String> currentMonth = currentMonthIter.next();
								JTextField currentConsumption = currentConsumptionIter.next();
								currentUnit = currentUnitIter.next();
								
								if (currentConsumption.getText().equals("")) {
						    		JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"Consommation : \" de la partie " + 
						    				" Compteur doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
				    			else if (!OperationUtilities.isNumeric(currentConsumption.getText())) {
				    				JOptionPane.showMessageDialog(contentPane, 
						    				"le champs \"Consommation : \" (" + currentConsumption.getText() + 
						    				") de la partie Compteur " + 
						    				" doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
				    			}
				    			else {
				    				//currentMeterData.addString(currentConsumption.getText() + " " + currentUnit,
				    					//	currentMonth.getSelectedItem().toString() + " : ");
				    				
				    				while (counter < MONTH_CHOICE.length) {
				    					
				    					if (MONTH_CHOICE[counter].equals(currentMonth.getSelectedItem().toString())) {
				    						break;
				    					}

				    					barChartDatas.addValue(0, currentUnit, MONTH_CHOICE[counter]);
				    					
				    					++counter;
				    				}

				    				barChartDatas.addValue(Double.parseDouble(currentConsumption.getText()), currentUnit, 
					    					currentMonth.getSelectedItem().toString());			    				
				    			}
								
								if (counter < MONTH_CHOICE.length) {
									++counter;
								}
								else {
									counter = 0;
								}
								
							}
							
							while (counter < MONTH_CHOICE.length) {

								final String unit;
								
								if (currentUnit != null && !currentUnit.equals("")) {
									unit = currentUnit;
								}
								else {
									unit = Meter.UNIT_CHOICE[currentMeter.getComboBoxTypeCompteur().getSelectedIndex()];
								}
		    					barChartDatas.addValue(0, unit, MONTH_CHOICE[counter]);
		    					
		    					++counter;
		    				}
							
							if (!currentMeter.getTextAreaCommentaire().getText().equals("")) {
								currentMeterData.addString(currentMeter.getTextAreaCommentaire().getText(), "Commentaire : ");
							}
								
							if (barChartDatas.getColumnCount() > 0) {
					    		try {
									JFreeChart barchart = chartGenerator.generateBarChart("Compteur",
											"Mois", "Consommation", barChartDatas, true, fontToFit);
									
									currentMeterData.addJFreeChart(barchart);
								} 
					    		catch (Exception e) {
					    			e.printStackTrace();
									JOptionPane.showMessageDialog(contentPane, "Erreur lors de la création du graphe en"
											+ " barre dans la partie Compteur : \n"
											+ e.getMessage(), "Erreur", 
											JOptionPane.WARNING_MESSAGE);
								}
				    		}
							
							if (!currentMeterData.isEmpty()) {
								datas.add(currentMeterData);
							}
						}
				    	
						// Creation
						try {
							// S'il y a bien des donnees
							if (!datas.isEmpty()) {
								
								/*-----------------Fenetre de dialogue-----------------*/
								// JDialog pour choisir l'emplacement et le nom du rapport
								final JDialog reportConfig = new JDialog(mainFrame, "Configuration du fichier de rapport", 
										Dialog.ModalityType.DOCUMENT_MODAL);
								
								// Contenue de la fenetre
								JPanel contentPane = new JPanel(new GridBagLayout());
								reportConfig.setContentPane(contentPane);
								
								// GridBagConstraints du JDialog
								final GridBagConstraints constraints = new GridBagConstraints();
								// Initialise toutes les valeurs qui nous sont utiles
								constraints.gridx = 0;
								constraints.gridy = 0;
								constraints.weightx = 0;
								constraints.gridwidth = 1;
								// Ajoute des marge pour chaques Component de ce JDialog
								constraints.insets = new Insets(5, 0, 0, 0);
								// Indique que les elements peuvent prendre la place disponnible horizontalement et verticalement
								constraints.fill = GridBagConstraints.BOTH;
								
								// Emplacelemnt du repertoire ou sera genere le rapport
								final JLabel directoryName = new JLabel(CreateReportDocument.DEFAULT_REPORT_PATH + File.separator);
								
								contentPane.add(directoryName, constraints);
								
								// Le nom du fichier du rapport
								final JTextField reportName = new JTextField(CreateReportDocument.DEFAULT_FILE_NAME);
								
								constraints.gridx = 1;
								constraints.gridwidth = 1;
								constraints.weightx = 1;
								contentPane.add(reportName, constraints);
								
								// L'extension du fichier
								final JLabel reportFileType = new JLabel(".pdf");
								
								constraints.gridx = 2;
								constraints.gridwidth = GridBagConstraints.REMAINDER;
								constraints.weightx = 0;
								contentPane.add(reportFileType, constraints);
								
								// Le JFileChooser pour choisir le repertoire
								final JFileChooser destinationChooser = new JFileChooser();
								destinationChooser.setCurrentDirectory(new File("." + File.separator));
								// Obligation de ne choisir qu'un repertoire
								destinationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
								
								// Bouton qui ouvre le JFileChooser pour le repertoire
								final JButton chooseFolder = new JButton("Choisir répertoire...");
								directoryName.setLabelFor(chooseFolder);
								chooseFolder.addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent e) {
										// Ouverture du JFileChooser et recuperation de la reponse de l'utilisateur
										final int returnVal = destinationChooser.showSaveDialog(mainFrame);
									    
										// Si la reponse est une validation
										if(returnVal == JFileChooser.APPROVE_OPTION) {
											try {
												// Affichage du nom du repertoir choisi
												directoryName.setText(destinationChooser.getSelectedFile().getAbsolutePath() + File.separator);
												
												// Au cas ou le nom serait assez long, on reajuste la taille du JDialog
												reportConfig.pack();
												reportConfig.repaint();
											}
											catch (Exception ex) {
												JOptionPane.showMessageDialog(mainFrame, 
									    				"Le répertoire choisi est invalide", "Erreur", 
														JOptionPane.WARNING_MESSAGE);
											}		
									    }
									}
								});
								
								// Tentative d'ajout de l'ImageIcon au JButton chooseFolder
								// Tentative d'obtiention de l'image
								final ImageIcon folerIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[5]);
								// Si l'image n'a pas d'erreur
							    if (folerIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
							    	// Definition de la hauteur de l'image en fonction de la taille du JButton
								    final int iconHeight = (int) (chooseFolder.getPreferredSize().getHeight() - chooseFolder.getPreferredSize().getHeight() / 3);
								    // Definition de la largeur de l'image en fonction de la taille du JButton
								    final int iconWidth  = folerIcon.getIconWidth() / (folerIcon.getIconHeight() / iconHeight);
								    
								    // Redimensionne l'image selon la largeur et la hauteur precedente
								    final Image tmpImg = folerIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
								    folerIcon.setImage(tmpImg);
								    chooseFolder.setIcon(folerIcon);
							    }
								
							    ++constraints.gridy;
								constraints.gridx = 0;
								constraints.gridwidth = GridBagConstraints.REMAINDER;
								contentPane.add(chooseFolder, constraints);
								
								// Bouton de validation
								final JButton approve = new JButton("Valider");
								approve.addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent arg0) {
										// Finallement on creer le document
										
										// Chemin ou a ete cree le rapport
										String filePath = "";
										// Indique si on peut genere le rapport (s'il n'y a pas eu d'imprevu)
										Boolean canWeCreate = true;
										try {
											// Tentative de creation d'un File a l'emplacement indique
											File report = new File(directoryName.getText() + File.separator + 
													reportName.getText() + reportFileType.getText());
											
											// Si le fichier existe deja
											if (report.exists()) {
												// Demande a l'utilisateur si on doit l'ecraser
												final int dialogResult = JOptionPane.showConfirmDialog (contentPane, 
						    							"Le fichier " + reportName.getText() + " existe déjà, voulez-vous l'écraser?",
						    							"Erreur", JOptionPane.YES_NO_OPTION);
												// Si l'utilisateur refuse, alors on empeche la creation du rapport
						    					if(dialogResult == JOptionPane.NO_OPTION){
						    						canWeCreate = false;
						    					}
											}
											
											// S'il n'y a aucun probleme, on cree le rapport
											if (canWeCreate) {
												// Creation du rapport a l'emplacement souhaite + recuperation du nom di fichier
												// (retour de la fonction CreateReportDocument)
												filePath = CreateReportDocument.createPdf(datas, 
														directoryName.getText() + 
														reportName.getText() + reportFileType.getText(), 
														IWriteStrategie.Strategie.DEFAULT,
														pBarFrame);
												
												// Indique que le rapport a bien ete genere et ou
												JOptionPane.showMessageDialog(contentPane, "Rapport généré à l'emplacement :" + 
														System.lineSeparator() + filePath, "Rapport généré", JOptionPane.INFORMATION_MESSAGE);
												
												// Met la barre de progression au maximum
												publish(ProgressBarFrame.MY_MAXIMUM);
												// Indique que l'on a plus besoin du JDialog
												reportConfig.dispose();
												
												stopPdfCreation(pBarFrame);
											}
										} 
										catch (Exception e) {
											e.printStackTrace();
											JOptionPane.showMessageDialog(contentPane, e.getMessage(), "Erreur", 
													JOptionPane.WARNING_MESSAGE);
											
											// En cas d'erreur, il faut stoper la creation du pdf
											stopPdfCreation(pBarFrame);
										}
									}
								});
								
								// Tentative d'ajout de l'ImageIcon au JButton approve
								// Tentative d'obtiention de l'image
								final ImageIcon validateIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[6]);
								// Si l'image n'a pas d'erreur
							    if (validateIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
							    	// Definition de la hauteur de l'image en fonction de la taille du JButton
								    final int iconHeight = (int) (approve.getPreferredSize().getHeight() - approve.getPreferredSize().getHeight() / 3);
								    // Definition de la largeur de l'image en fonction de la taille du JButton
								    final int iconWidth  = validateIcon.getIconWidth() / (validateIcon.getIconHeight() / iconHeight);
								    
								    // Redimensionne l'image selon la largeur et la hauteur precedente
								    final Image tmpImg = validateIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
								    validateIcon.setImage(tmpImg);
								    approve.setIcon(validateIcon);
							    }
								
								++constraints.gridy;
								constraints.gridx = 1;
								contentPane.add(approve, constraints);
								
								// Si l'utilisateur ferme le JDialog
								reportConfig.addWindowListener(new WindowAdapter() {
						            public void windowClosing(java.awt.event.WindowEvent e) {
						            	// Stop la creation du pdf
						                stopPdfCreation(pBarFrame);
						                // Indique que l'on a plus besoin du JDialog
						                reportConfig.dispose();
						            }
						        });
								
								// Met la taille du JDialog au minimum
								reportConfig.pack();
								// Place ke JDialog au centre du formulaire
								reportConfig.setLocationRelativeTo(mainFrame);
								// Fait apparaitre le JDialog
								reportConfig.setVisible(true);

							}
							else {
								JOptionPane.showMessageDialog(contentPane, 
					    				"Aucune donnée à rédiger dans le rapport", "Erreur", 
										JOptionPane.WARNING_MESSAGE);
								stopPdfCreation(pBarFrame);
							}
						} 
						catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(contentPane, e.getMessage(), "Erreur", 
									JOptionPane.WARNING_MESSAGE);
							stopPdfCreation(pBarFrame);
						}

				    	return null;
					}
					
					@Override
					protected void process(List<Integer> chunks) {
				        for (Integer i : chunks){
				        	pBarFrame.updateBar(i);
				        }
				    }
					
					@Override
			        public void done() {
						try {
					        get();
					    } 
						catch (Exception e) {
					        e.printStackTrace();
					    }
						stopPdfCreation(pBarFrame);
			        }

				};
				
				pBarFrame.addWindowListener(new WindowAdapter() {
		            public void windowClosing(java.awt.event.WindowEvent e) {
		                stopPdfCreation(pBarFrame);
		                pdfCreation.cancel(true);
		            }
		        });
				
				/*pdfCreation.addPropertyChangeListener(new PropertyChangeListener() {
					
					@SuppressWarnings("null")
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if ("progress" == evt.getPropertyName()) {
				            int progress = (Integer) evt.getNewValue();
				            pBarFrame.updateBar(progress);
				        } 						
					}
				});
				*/
				
				pdfCreation.execute();
		    }
		});
        
        /*--------------------------------------------------ajout des éléments---------------------------------------------*/    
	    
	    container.add(mainContainer, BorderLayout.CENTER); //ajout du conteneur principal contenant tous les champs formulaires au centre du conteuneur
	    
	    JScrollPane scrollPanePrincipale = new JScrollPane(container); //scrollPane principale sur le conteneur
	    scrollPanePrincipale.getVerticalScrollBar().setUnitIncrement(16); //vitesse de scroll
	    
	    contentPane.add(titreFacilitiesRapport, BorderLayout.NORTH); //ajout du titre en haut de la fenetre 
	    contentPane.add(scrollPanePrincipale, BorderLayout.CENTER); //ajout de la scrollPane principale (et du conteneur) au centre de la fenetre
	    
	    this.setContentPane(contentPane);
	    
	    this.setVisible(true);  //visibilite  
	}	

	private JPanel createPreventiveVoucherMonth (
			final Collection<JComboBox<String>> preventivesVouchersMonths, 
			final Collection<JTextField> nbPreventivesVouchersOpened, 
			final Collection<JTextField> nbPreventivesVouchersClosed,
			final Collection<JButton> deleteButtonsPreventivesVouchers) {
		
		JPanel thisPreventiveVoucherMonthPanel = new JPanel (new GridBagLayout());
		
		int preventiveVoucherMonthPosition = 0;
		
		GridBagConstraints constraint = new GridBagConstraints();
		
		constraint.insets = new Insets(0, 0, 3, 0); //marges autour de l'element
		
		constraint.gridx = 0;
		constraint.gridy = preventiveVoucherMonthPosition;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.weightx = 1;
		constraint.fill = GridBagConstraints.BOTH;
		
		JLabel preventivVoucherMonthJLabel = new JLabel (PREVENTIVE_VOUCHER_MONTH_LABELS[0]);
		
		thisPreventiveVoucherMonthPanel.add(preventivVoucherMonthJLabel, constraint); //ajout du label moisBP
			
		JComboBox<String> comboBoxMoisBP = new JComboBox<String>(MONTH_CHOICE); //initialisation de la comboBox comboBoxMoisBP avec la liste choixMois
		comboBoxMoisBP.setPreferredSize(new Dimension(100, 20)); //dimension de la comboBoxMoisBP
		preventivesVouchersMonths.add(comboBoxMoisBP);
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		preventivVoucherMonthJLabel.setLabelFor(comboBoxMoisBP); //attribution de la comboBox comboBoxMoisBP au label moisBP
		thisPreventiveVoucherMonthPanel.add(comboBoxMoisBP, constraint); //ajout de la zone de texte comboBox comboBoxMoisBP
		
	    //nombre BP ouverts
	    JLabel nbBPOuverts = new JLabel(PREVENTIVE_VOUCHER_MONTH_LABELS[1]); //creation du label nbBPOuverts
	    constraint.gridx = 0;
	    constraint.gridy = ++preventiveVoucherMonthPosition;
	    constraint.gridwidth = 1;
	    thisPreventiveVoucherMonthPanel.add(nbBPOuverts, constraint); //ajout du label nbBPOuverts
	    
	    JTextField textFieldNbBPOuverts = new JTextField("0", 2); //creation de la zone de texte textFieldNbBPOuverts
	    nbBPOuverts.setLabelFor(textFieldNbBPOuverts); //attribution de la zone de texte au label nbBPOuverts
	    nbPreventivesVouchersOpened.add(textFieldNbBPOuverts);
		
	    constraint.gridx = 1;
	    constraint.gridwidth = GridBagConstraints.REMAINDER;
	    thisPreventiveVoucherMonthPanel.add(textFieldNbBPOuverts, constraint); //ajout de la zone de texte textFieldNbBPOuverts
		
		//nombre BP fermes
	    JLabel nbBPFermes = new JLabel(PREVENTIVE_VOUCHER_MONTH_LABELS[2]); //creation du label nbBPFermes
		
	    constraint.gridx = 0;
	    constraint.gridy = ++preventiveVoucherMonthPosition;
	    constraint.gridwidth = 1;
	    thisPreventiveVoucherMonthPanel.add(nbBPFermes, constraint); //ajout du label nbBPFermes
		
	    JTextField textFieldNbBPFermes = new JTextField("0", 2); //creation de la zone de texte textFieldNbBPFermes
	    nbBPFermes.setLabelFor(textFieldNbBPFermes); //attribution de la zone de texte textFieldNbBPFermes au label nbBPFermes
	    nbPreventivesVouchersClosed.add(textFieldNbBPFermes);
		
	    constraint.gridx = 1;
	    constraint.gridwidth = GridBagConstraints.REMAINDER;
	    thisPreventiveVoucherMonthPanel.add(textFieldNbBPFermes, constraint); //ajout de la zone de texte textFieldNbBPFermes
	    
	    int iconHeight;
	    int iconWidth;
	    Image tmpImg;
	    
	    final ImageIcon addIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[1]);
	    
	    if (addIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
		    iconHeight = (int) (addPreventiveVoucherMonth.getPreferredSize().getHeight() - 
		    		addPreventiveVoucherMonth.getPreferredSize().getHeight() / 3);
		    iconWidth  = addIcon.getIconWidth() / (addIcon.getIconHeight() / iconHeight);
		    
		    tmpImg = addIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    addIcon.setImage(tmpImg);
	    }
		
		textFieldNbBPOuverts.getDocument().addDocumentListener(new PersonnalDocumentListener() {
			
			@Override
			public void update(DocumentEvent arg0) {
				if (textFieldNbBPOuverts.getText().equals("") || textFieldNbBPFermes.getText().equals("")) {
					addPreventiveVoucherMonth.setEnabled(false);
					addPreventiveVoucherMonth.setText(ADD_MONTH_BUTTON_TEXT[2]);
					addPreventiveVoucherMonth.setIcon(null);
				}
				else {
					addPreventiveVoucherMonth.setEnabled(true);
					addPreventiveVoucherMonth.setText(ADD_MONTH_BUTTON_TEXT[0]);
					addPreventiveVoucherMonth.setIcon(addIcon);
				}
			}
		});
		
		textFieldNbBPFermes.getDocument().addDocumentListener(new PersonnalDocumentListener() {
			
			@Override
			public void update(DocumentEvent arg0) {
				if (textFieldNbBPOuverts.getText().equals("") || textFieldNbBPFermes.getText().equals("")) {
					addPreventiveVoucherMonth.setEnabled(false);
					addPreventiveVoucherMonth.setText(ADD_MONTH_BUTTON_TEXT[2]);
					addPreventiveVoucherMonth.setIcon(null);
				}
				else {
					addPreventiveVoucherMonth.setEnabled(true);
					addPreventiveVoucherMonth.setText(ADD_MONTH_BUTTON_TEXT[0]);
					addPreventiveVoucherMonth.setIcon(addIcon);
				}
			}
		});
		JButton deleteMonthButton = new JButton("Supprimer mois");
		
		final ImageIcon deleteIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[4]);
		
		if (deleteIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
			iconHeight = (int) ((int) deleteMonthButton.getPreferredSize().getHeight() - 
					deleteMonthButton.getPreferredSize().getHeight() / 3);
		    iconWidth  = deleteIcon.getIconWidth() / (deleteIcon.getIconHeight() / iconHeight);
		    
		    tmpImg = deleteIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
			deleteIcon.setImage(tmpImg);
			
			deleteMonthButton.setIcon(deleteIcon);
		}
		deleteMonthButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {	
				
				preventivesVouchersMonths.remove(comboBoxMoisBP);
				nbPreventivesVouchersOpened.remove(textFieldNbBPOuverts);
				nbPreventivesVouchersClosed.remove(textFieldNbBPFermes);
				
				addPreventiveVoucherMonth.setEnabled(true);
				addPreventiveVoucherMonth.setText(ADD_MONTH_BUTTON_TEXT[0]);
				addPreventiveVoucherMonth.setIcon(addIcon);
				
				// Parent de ce JPanel
				final Container parent = thisPreventiveVoucherMonthPanel.getParent();
				
				// Si le JPanel a bien un parent
				if (parent != null) {
					
					// Obtient le GridBagLayout de son parent
					final GridBagLayout parentLayout = (GridBagLayout)parent.getLayout();
					
					// Change le positionnement de tous les JPanel qui sont apres celui que l'on efface
					// Cela fonctionne car dans la hierarchie du parent de l'element, les elements sont tous ajoutes en derniers
					// Parcour les elements qui sont apres celui que l'on efface
					for (int i = OperationUtilities.getComponentIndex(thisPreventiveVoucherMonthPanel);
							i < parent.getComponentCount();
							++i) {
						
						// Obtention du Component suivant
						Component currentComponent = parent.getComponent(i);
						// Obtiention de son GridBagConstraints (celui avec lequel il a ete ajoute)
						GridBagConstraints thisComponentConstraint = parentLayout.getConstraints(currentComponent);
						// Decremente sa coordonnee verticale
						--thisComponentConstraint.gridy;
						// Applique les modifications
						parentLayout.setConstraints(currentComponent, thisComponentConstraint);
					}
					
					// Suppression de cet element de son parent
					parent.remove(thisPreventiveVoucherMonthPanel);
					parent.revalidate();
				}
				
				deleteButtonsPreventivesVouchers.remove(deleteMonthButton);
				
				--preventiveVoucherLastMonthPosition;
			}
		});
		
		deleteButtonsPreventivesVouchers.add(deleteMonthButton);
		
		constraint.gridx = 0;
		constraint.gridy = ++preventiveVoucherMonthPosition;
		constraint.gridwidth = 1;
		thisPreventiveVoucherMonthPanel.add(deleteMonthButton, constraint);
		
		return thisPreventiveVoucherMonthPanel;
	}
	
	private JPanel createInterventionDemand (final Collection<JComboBox<String>> interventionMonths, 
			final Collection<JTextField> interventionNumbers,
			final ArrayList<JButton> deleteButtonsInterventionMonths) {
		
		JPanel interventionDemand = new JPanel(new GridBagLayout());
		interventionDemand.setBorder(BorderFactory.createTitledBorder("Demande d'intervention"));
		
		int currentPositionCounter = 0;
		
		GridBagConstraints interventionDemandConstraint = new GridBagConstraints();
		interventionDemandConstraint.gridx = 0;
		interventionDemandConstraint.gridy = currentPositionCounter;
		interventionDemandConstraint.weightx = 1;
		interventionDemandConstraint.fill = GridBagConstraints.BOTH;
		
		//mois
		JLabel moisDI = new JLabel("Mois : ");
		
		interventionDemandConstraint.insets = new Insets(3, 7, 0, 7); //marges autour de l'element
		interventionDemandConstraint.gridy = ++currentPositionCounter;
		interventionDemand.add(moisDI, interventionDemandConstraint); //ajout du label moisBP
		
		JComboBox<String> comboBoxMoisDI = new JComboBox<String>(MONTH_CHOICE); //initialisation de la comboBox comboBoxMoisBP avec la liste choixMois
		comboBoxMoisDI.setPreferredSize(new Dimension(100, 20)); //dimension de la comboBoxMoisBP
		moisDI.setLabelFor(comboBoxMoisDI); //attribution du label moisBP à la comboBox comboBoxMoisBP
		interventionMonths.add(comboBoxMoisDI);
		
		interventionDemandConstraint.gridx = 1;
		interventionDemandConstraint.gridwidth = GridBagConstraints.REMAINDER;
		interventionDemand.add(comboBoxMoisDI, interventionDemandConstraint); //ajout de la zone de texte comboBox comboBoxMoisBP
				
		//nombre d'interventions
		JLabel nbIntervention = new JLabel("Nombre d'interventions : "); //creation du label nbBPOuverts
		
		interventionDemandConstraint.insets = new Insets(0, 7, 0, 7); //marges autour de l'element
		interventionDemandConstraint.gridx = 0;
		interventionDemandConstraint.gridy = ++currentPositionCounter;
		interventionDemandConstraint.gridwidth = 1;
		interventionDemand.add(nbIntervention, interventionDemandConstraint); //ajout du label nbBPOuverts
		
		final JTextField textFieldNbIntervention = new JTextField("0", 2); //creation de la zone de texte textFieldNbBPOuverts
		nbIntervention.setLabelFor(textFieldNbIntervention); //attribution de la zone de texte au label nbBPOuverts
		interventionNumbers.add(textFieldNbIntervention);
		
		final ImageIcon addInterventionMonthIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[1]);
		
		if (addInterventionMonthIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
	  	
		  	final int iconHeight = (int) (addInterventionDemandMonth.getPreferredSize().getHeight() - addInterventionDemandMonth.getPreferredSize().getHeight() / 3);
		    final int iconWidth  = addInterventionMonthIcon.getIconWidth() / (addInterventionMonthIcon.getIconHeight() / iconHeight);
		    
		    final Image tmpImg = addInterventionMonthIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    addInterventionMonthIcon.setImage(tmpImg);
		}
		
		textFieldNbIntervention.getDocument().addDocumentListener(new PersonnalDocumentListener() {
			
			@Override
			public void update(DocumentEvent arg0) {
				if (!textFieldNbIntervention.getText().equals("")) {
					addInterventionDemandMonth.setText(ADD_MONTH_BUTTON_TEXT[0]);
					addInterventionDemandMonth.setIcon(addInterventionMonthIcon);
					addInterventionDemandMonth.setEnabled(true);
				}
				else {
					addInterventionDemandMonth.setText(ADD_MONTH_BUTTON_TEXT[2]);
					addInterventionDemandMonth.setIcon(null);
					addInterventionDemandMonth.setEnabled(false);
				}
			}
		});
		
		interventionDemandConstraint.gridx = 1;
		interventionDemandConstraint.gridwidth = GridBagConstraints.REMAINDER;
		interventionDemand.add(textFieldNbIntervention, interventionDemandConstraint); //ajout de la zone de texte textFieldNbBPOuverts
		
		JButton deleteInterventionMonth = new JButton("Supprimer mois");
		
		final ImageIcon deleteInterventionMonthIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[4]);
	  	
		if (deleteInterventionMonthIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
		  	final int iconHeight = (int) (deleteInterventionMonth.getPreferredSize().getHeight() - deleteInterventionMonth.getPreferredSize().getHeight() / 3);
		    final int iconWidth  = deleteInterventionMonthIcon.getIconWidth() / (deleteInterventionMonthIcon.getIconHeight() / iconHeight);
		    
		    final Image tmpImg = deleteInterventionMonthIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    deleteInterventionMonthIcon.setImage(tmpImg);
		    deleteInterventionMonth.setIcon(deleteInterventionMonthIcon);
		}
		
		deleteInterventionMonth.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				interventionMonths.remove(comboBoxMoisDI);
				interventionNumbers.remove(textFieldNbIntervention);
				
				addInterventionDemandMonth.setText(ADD_MONTH_BUTTON_TEXT[0]);
				addInterventionDemandMonth.setIcon(addInterventionMonthIcon);
				addInterventionDemandMonth.setEnabled(true);
				
				// Parent de ce JPanel
				final Container parent = interventionDemand.getParent();
				
				// Si ce JPanel a bien un parent
				if (parent != null) {
					
					// Obtient le GridBagLayout de son parent
					final GridBagLayout parentLayout = (GridBagLayout)parent.getLayout();
					
					// Change le positionnement de tous les Component qui sont apres celui que l'on efface
					// Cela fonctionne car dans la hierarchie du parent du JPanel, les Component sont tous ajoutes en derniers
					// Parcour les Component qui sont apres celui que l'on efface
					for (int i = OperationUtilities.getComponentIndex(interventionDemand); i < parent.getComponentCount(); ++i) {
						
						// Obtention du Component suivant
						final Component currentComponent = parent.getComponent(i);
						// Obtiention de son GridBagConstraints (celui avec lequel il a ete ajoute)
						final GridBagConstraints thisComponentConstraint = parentLayout.getConstraints(currentComponent);
						// Decremente sa coordonnee verticale
						--thisComponentConstraint.gridy;
						// Applique les modifications
						parentLayout.setConstraints(currentComponent, thisComponentConstraint);
					}
					
					// Suppression de cet element de son parent
					parent.remove(interventionDemand);
					// On revalide le parent
					parent.revalidate();
				}
				
				--interventionDemandLastMonthPosition;
				
				deleteButtonsInterventionMonths.remove(deleteInterventionMonth);
			}
		});
		
		deleteButtonsInterventionMonths.add(deleteInterventionMonth);
		
		interventionDemandConstraint.gridx = 0;
		interventionDemandConstraint.gridy = ++currentPositionCounter;
		interventionDemandConstraint.gridwidth = 1;
		interventionDemand.add(deleteInterventionMonth, interventionDemandConstraint);
		
		return interventionDemand;
	}
	
	private void stopPdfCreation(final ProgressBarFrame pBFrame) {
		
		this.setCursor(null);
		this.setEnabled(true);
		
		pBFrame.dispose();
	}
	
	private String xmlSerialization(final File fileToSaveInto) {
		try {
			final XMLEncoder xmlEncoder = new XMLEncoder(new BufferedOutputStream(
			        new FileOutputStream(fileToSaveInto)));
			
			xmlEncoder.writeObject(datasToSerialize);
			
			xmlEncoder.flush();
			xmlEncoder.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", 
					JOptionPane.WARNING_MESSAGE);
		}
		
		return "";
	}
	
	@SuppressWarnings("unchecked")
	private void xmlUnserialization(final File fileToLoadFrom) {
		try {
			final XMLDecoder xmlDecoder = new XMLDecoder(new FileInputStream(fileToLoadFrom));
			
        	this.setUnserializedDatas((Collection<JTextComponent>) xmlDecoder.readObject());
        	
        	this.revalidate();
        	this.repaint();
			
			xmlDecoder.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", 
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void setUnserializedDatas (final Collection<JTextComponent> datasToSerialize) {
		if (this.datasToSerialize.size() != datasToSerialize.size()) {
			JOptionPane.showMessageDialog(this, "Impossible de charger ces données, versions incompatibles", "Erreur", 
					JOptionPane.WARNING_MESSAGE);
		}
		
		final Iterator<JTextComponent> dataToSet = datasToSerialize.iterator();
		
		for (JTextComponent textComponent : this.datasToSerialize) {
			textComponent.setText(dataToSet.next().getText());
		}
	}
}