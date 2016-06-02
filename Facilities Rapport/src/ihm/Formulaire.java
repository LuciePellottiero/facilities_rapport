package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MaskFormatter;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import dataHandler.DefaultDataHandler;
import dataHandler.IDataHandler;
import documentHandler.CreateReportDocument;
import utilities.OperationUtilities;
import utilities.chartGenerator.DefaultChartGenerator;
import utilities.chartGenerator.IChartGenerator;

/**
 * JFrame du formulaire
 * @author Lucie PELLOTTIERO
 *
 */
public class Formulaire extends JFrame{
	
	/**
	 * Numero de serialisation genere par defaut
	 */
	private static final long serialVersionUID = 1L;

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
			"pdfIcon.png", "removeIcon.png"};
	
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
	private static final int NUMBER_FREE_TREE_ALLOWED = 30;
	
	/**
	 * Nombre de compteur que l'on peut ajouter
	 */
	private static final int NUMBER_METER_ALLOWED = 30;
	
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
	 * La position verticale de la derniere arborescence libre 2
	 */
	private int freeTrees2LastPosition;
	/**
	 * La position verticale du dernier compteur
	 */
	private int meterLastPosition;
	
	/**
	 * Le nombre de mois que l'on doit ajouter (pour les bons preventifs et les demandes d'intervention)<br>
	 * selon le type du rapport
	 */
	private int monthNumber;
	
	public Formulaire() throws IOException{
		super();
	    
	    final ImageIcon vinciIcion = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[0]);
	    if (vinciIcion.getImageLoadStatus() != MediaTracker.ERRORED) {
	    	this.setIconImage(vinciIcion.getImage());
	    }
	    
		final JPanel fenetre = new JPanel(); //creation de la fenetre principale
		
		// Lien vers ce formulaire pour l'affichage de fenetre d'information
		final Formulaire mainFrame = this;
		
		positionCounter = 0;
		
		this.setTitle("Facilities Rapport"); //titre fenetre
		
		this.setSize(700, 600); //taille fenetre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //pour fermer la fenetre
	    this.setLocationRelativeTo(null); //position de la fenetre
	    fenetre.setBackground(Color.white); //couleur de fond de la fenetre
	    fenetre.setLayout(new BorderLayout()); 
	    
	    final JLabel titreFacilitiesRapport = new JLabel("Facilities Rapport", SwingConstants.CENTER); //titre formulaire
		titreFacilitiesRapport.setFont(new Font("Arial",Font.BOLD,18)); //police + taille du titre formulaire
		
		final JPanel conteneur = new JPanel();
		final JPanel conteneurPrincipal = new JPanel(new GridBagLayout());
		final GridBagConstraints constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.BOTH;   
		
		Insets titleInset = new Insets(20, 0, 5, 0); //marges autour des titres
		
		/*-----------------------------------------formulaire redacteur--------------------------------------------*/
	    
		final JLabel titreRedacteur = new JLabel("Redacteur"); //titre de la partie redacteur du formulaire
		titreRedacteur.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreRedacteur
		
		constraint.gridx = 0;
		constraint.gridy = positionCounter;
		constraint.insets = titleInset; //marges autour de l'element
	    conteneurPrincipal.add(titreRedacteur, constraint); //ajout du titreRedacteur dans conteneurPrincipal
		
		//nom
		final JLabel nom = new JLabel("Nom : "); //creation du label nom
		
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		conteneurPrincipal.add(nom, constraint); //ajout du label
		
		final JTextField textFieldNom = new JTextField(15); //creation de la zone de texte adr de taille 15
		nom.setLabelFor(textFieldNom); //attribution de la zone de texte au label adr
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNom, constraint); //ajout de la zone de texte adr
		
		//adresse 
		final JLabel adr = new JLabel("Adresse : "); //creation du label adr
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(adr, constraint); //ajout du label adr
	    
	    final JTextArea textAreaAdr = new JTextArea(3, 15); //creation de la zone de texte adr de taille 3 en hauteur et 15 en largeur
	    
	    final JScrollPane scrollPaneAdr = new JScrollPane(textAreaAdr);
	    adr.setLabelFor(textAreaAdr); //attribution de la zone de texte au label adr
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(scrollPaneAdr, constraint); //ajout de la zone de texte adr
	    
	    //telephone
	    final JLabel tel = new JLabel("Téléphone : "); //creation du label tel
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(tel, constraint); //ajout du label tel
	    
	    JFormattedTextField textFieldTelRedac = null;
	   
	    try{
			final MaskFormatter maskTel  = new MaskFormatter("## ## ## ## ##"); //masque pour le format du numero de telephone
			textFieldTelRedac = new JFormattedTextField(maskTel); //initialisation de la zone de texte tel formattee par le masque
			textFieldTelRedac.setValue("00 00 00 00 00");
	    }
	    catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    tel.setLabelFor(textFieldTelRedac); //attribution de la zone de texte au label tel
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldTelRedac, constraint); //ajout de la zone de texte tel
		
		final JFormattedTextField finalTextFieldTelRedac = textFieldTelRedac;
	   
	    //email
	    final JLabel email = new JLabel("Email : "); //creation du label email
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(email, constraint); //ajout du label nom
	    
	    final JTextField textFieldEmail = new JTextField(15); //creation de la zone de texte email de taille 15
	    email.setLabelFor(textFieldEmail); //attribution de la zone de texte au label email
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(textFieldEmail, constraint); //ajout de la zone de texte email au panel redacteur 
	    
	    //nom charge d'affaire
	    final JLabel nomCA = new JLabel("Nom du chargé d'affaire : "); //creation du label nomCA
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(nomCA, constraint); //ajout du label nomCA au panel redacteur
	   
	    final JTextField textFieldNomCA = new JTextField(15); //creation de la zone de texte nomCA de taille 15
	    nomCA.setLabelFor(textFieldNomCA); //attribution de la zone de texte au label nomCA
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(textFieldNomCA, constraint); //ajout de la zone de texte nomCA
        
		/*-------------------------------------------formulaire client-------------------------------------------------*/
		
		
		final JLabel titreClient = new JLabel("Client"); //titre de la partie client du formulaire
		titreClient.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreClient
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = titleInset; //marges autour de l'element
		conteneurPrincipal.add(titreClient, constraint); //ajout du titreClient dans le panel conteneurPrincpal
	
		//nom
		final JLabel nomSite = new JLabel("Nom du site : "); //creation du label nomSite
		
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		conteneurPrincipal.add(nomSite, constraint); //ajout du label nomSite
		
		final JTextField textFieldNomSite = new JTextField(15); //creation de la zone de texte nomSite de taille 15
		nomSite.setLabelFor(textFieldNomSite); //attribution de la zone de texte au label nomSite
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNomSite, constraint); //ajout de la zone de texte nomSite
		
		//code
	    final JLabel code = new JLabel("Code : "); //creation du label code
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(code, constraint); //ajout du label code
	    
	    final JTextField textFieldCode = new JTextField(15); //création de la zone de texte code
	    code.setLabelFor(textFieldCode); //attribution de la zone de texte au label code
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldCode, constraint); //ajout de la zone de texte code
		
		//adresse client
		final JLabel adrCl = new JLabel("Adresse : "); //creation du label adrCl
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(adrCl, constraint); //ajout du label adrCl
	    
	    final JTextArea textAreaAdrCl = new JTextArea(3, 15); //creation de la zone de texte adrCl de taille 3 en hauteur et 15 en largeur
	   
	    final JScrollPane scrollPaneAdrCl = new JScrollPane(textAreaAdrCl);
	    adrCl.setLabelFor(textAreaAdrCl); //attribution de la zone de texte au label adrCl
	    
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;  
	    conteneurPrincipal.add(scrollPaneAdrCl, constraint); //ajout de la zone de texte adrCl
	    
	    //code postal
	    final JLabel codePostal = new JLabel("Code postal : "); //creation du label codePostal
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(codePostal, constraint); //ajout du label codePostal
	    
	    JFormattedTextField textFieldCodePostal = null;
	    try{
			MaskFormatter maskCodePostal  = new MaskFormatter("## ###"); //masque pour le format du code postal
			textFieldCodePostal = new JFormattedTextField(maskCodePostal); //initialisation de la zone de texte codePostal formattee par le masque
			textFieldCodePostal.setValue("00 000");
	    }
	    catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    
	    codePostal.setLabelFor(textFieldCodePostal); //attribution de la zone de texte au label codePostal
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldCodePostal, constraint); //ajout de la zone de texte codePostal
		
		final JFormattedTextField finalTtextFieldCodePostal = textFieldCodePostal;
		
		//ville
		final JLabel ville = new JLabel("Ville : "); //creation du label ville
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		conteneurPrincipal.add(ville, constraint); //ajout du label ville
		
		final JTextField textFieldVille = new JTextField(15); //creation de la zone de texte ville de taille 15
		ville.setLabelFor(textFieldVille); //attribution de la zone de texte au label ville
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldVille, constraint); //ajout de la zone de texte ville
		
		//nom du client
	    final JLabel nomClient = new JLabel("Nom du client : "); //creation du label nomCl
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(nomClient, constraint); //ajout du label nomCl
	   
	    final JTextField textFieldNomClient = new JTextField(15); //creation de la zone de texte nomCl de taille 15
	    nomClient.setLabelFor(textFieldNomClient); //attribution de la zone de texte au label nomCl
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(textFieldNomClient, constraint); //ajout de la zone de texte nomCl 
	    
	    //telephone client
	    final JLabel telCl = new JLabel("Téléphone : "); //creation du label telCl
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(telCl, constraint); //ajout du label telCl
	   
	    JFormattedTextField textFieldTelCl = null;
	   
	    try{
			MaskFormatter maskTelCl  = new MaskFormatter("## ## ## ## ##"); //masque pour le format du numero de telephone
			textFieldTelCl = new JFormattedTextField(maskTelCl); //initialisation de la zone de texte telCl formattee par le masque
			textFieldTelCl.setValue("00 00 00 00 00");
	    }
	    catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    
	    telCl.setLabelFor(textFieldTelCl); //attribution de la zone de texte au label telCl
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldTelCl, constraint); //ajout de la zone de texte telCl
		
		final JFormattedTextField finalTextFieldTelCl = textFieldTelCl;
	   
	    //email client
	    final JLabel emailCl = new JLabel("Email : "); //creation du label emailCl
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(emailCl, constraint); //ajout du label emailCl
	   
	    final JTextField textFieldEmailCl = new JTextField(15); //creation de la zone de texte emailCl de taille 15
	    emailCl.setLabelFor(textFieldEmailCl); //attribution de la zone de texte au label emailCl
		
	    constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(textFieldEmailCl, constraint); //ajout de la zone de texte emailCl
	    
	    final JLabel customerLogo = new JLabel ("Logo client : ");
	    customerLogo.setHorizontalTextPosition(JLabel.CENTER);
	    customerLogo.setVerticalTextPosition(JLabel.BOTTOM);
	    
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		conteneurPrincipal.add(customerLogo, constraint);
	    
		final JLabel customerLogoFile = new JLabel();
		customerLogoFile.setPreferredSize(new Dimension(90, (int) customerLogoFile.getPreferredSize().getHeight()));
		customerLogoFile.setHorizontalTextPosition(JLabel.CENTER);
		customerLogo.setVerticalTextPosition(JLabel.BOTTOM);
		
		constraint.gridx = 1;
		constraint.weightx = 1;
		conteneurPrincipal.add(customerLogoFile, constraint);
		
		int iconHeight;
	    int iconWidth;
	    Image tmpImg;
		
		final JButton deleteLogo = new JButton();
		
		final ImageIcon removePictureIcon = new ImageIcon(ICONS_PATH + File.separator + ICONS_NAME[4]);
	    if (removePictureIcon.getImageLoadStatus() != MediaTracker.ERRORED) {
	    	iconWidth = (int) (deleteLogo.getPreferredSize().getWidth() - deleteLogo.getPreferredSize().getWidth() / 2);
		    iconHeight  = removePictureIcon.getIconHeight() / (removePictureIcon.getIconWidth() / iconWidth);
		    
		    tmpImg = removePictureIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
		    removePictureIcon.setImage(tmpImg);
		    deleteLogo.setIcon(removePictureIcon);
	    }
		
		deleteLogo.setVisible(false);
		deleteLogo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				logoIcon = null;
				//customerLogoFile.setText("");
				customerLogoFile.setIcon(null);
				deleteLogo.setVisible(false);
				
				conteneurPrincipal.repaint();
			}
		});
		
		constraint.gridx = 2;
		constraint.weightx = 0;
		constraint.fill = GridBagConstraints.HORIZONTAL;
		conteneurPrincipal.add(deleteLogo, constraint);
		
	    final FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "Fichiers images", ImageIO.getReaderFileSuffixes());

	    final JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileFilter(filter);
	    fileChooser.setAccessory(new ImagePreview(fileChooser));
	    
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
				        
						conteneurPrincipal.repaint();
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
		conteneurPrincipal.add(addLogo, constraint);

		
		/*----------------------------------------------formulaire rapport----------------------------------------------------------*/
	    
		JLabel titreRapport = new JLabel("Rapport"); //titre de la partie rapport du formulaire
		titreRapport.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreRapport
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = titleInset; //marges autour de l'element
		constraint.fill = GridBagConstraints.BOTH;
	    conteneurPrincipal.add(titreRapport, constraint); //ajout du titreRapport dans conteneurPrincipal
		
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		
	    //rapport d'activite
	    JLabel rapportActivite = new JLabel("Rapport d'activité : "); ////creation du label rapportActivite
	    
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.RELATIVE;
		conteneurPrincipal.add(rapportActivite, constraint); //ajout du label rapportActivite
		
		final String[] choixRapport = {"Hebdomadaire", "Mensuel", "Bimensuel", "Trimestriel", "Semestriel", "Annuel"}; //liste des differents choix de la duree du rapport d'activite
		final JComboBox<String> comboBoxRapport = new JComboBox<String>(choixRapport); //initialisation du comboBox comboBoxRapport avec la liste choixRapport
		comboBoxRapport.setPreferredSize(new Dimension(100, 20)); //dimension de la comboBoxRapport
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		rapportActivite.setLabelFor(comboBoxRapport); //attribution de la comboBox comboBoxRapport au label rapportActivite
		conteneurPrincipal.add(comboBoxRapport, constraint); //ajout de la comboBox comboBoxRapport
		
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
		conteneurPrincipal.add(updateVoucherMonth, constraint);
		
	    //date debut
	    final JLabel dateDebut = new JLabel("Date de début : "); //creation du label dateDebut
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(dateDebut, constraint); //ajout du label dateDebut
	    
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
		conteneurPrincipal.add(textFieldDateDebut, constraint); //ajout de la zone de texte textFieldDateDebut
		JFormattedTextField finalTextFieldDateDebut = textFieldDateDebut;
		
		//date fin
	    JLabel dateFin = new JLabel("Date de fin : "); //creation du label dateFin
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(dateFin, constraint); //ajout du label dateFin
	    
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
		conteneurPrincipal.add(textFieldDateFin, constraint); //ajout de la zone de texte textFieldDateFin
		JFormattedTextField finalTextFieldDateFin = textFieldDateFin;
		
		/*----------------------------------------------formulaire bons preventifs----------------------------------------------------------*/
		
		JLabel titreBP = new JLabel("Bons préventifs"); //titre de la partie bons preventifs du formulaire
		titreBP.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreBP
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = titleInset; //marges autour de l'element
		conteneurPrincipal.add(titreBP, constraint); //ajout du titreBP dans conteneurPrincipal
	    
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
		conteneurPrincipal.add(preventiveVoucherMonthsPanel, constraint);
	    
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
		conteneurPrincipal.add(commentaireBP, constraint);
	    
		JTextArea textAreaCommentaireBP = new JTextArea(4, 15); //creation de la zone de texte textAreaCommentaireBP
	    JScrollPane scrollPaneComBP = new JScrollPane(textAreaCommentaireBP); //creation de la scrollPane scrollPaneComBP contenant textAreaCommentaireBP
	    commentaireBP.setLabelFor(textAreaCommentaireBP); //attribution de la zone de texte textAreaCommentaireBP au label commentaireBP
	    
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		conteneurPrincipal.add(scrollPaneComBP, constraint); //ajout de la scrollPane scrollPaneComBP
	    
	    constraint.gridx = 1;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(10, 0, 3, 0); //marges autour de l'element
		conteneurPrincipal.add(addPreventiveVoucherMonth, constraint); //ajout du bouton ajoutElement
	    
	    /*----------------------------------------formulaire bons preventifs par domaine------------------------------------------------*/
	    
	    JLabel titreBPDomaine = new JLabel("Bons préventifs par domaines"); //titre de la partie Bons preventifs par domaine du formulaire
	    titreBPDomaine.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		constraint.gridx = 0; //position horizontale
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1; //nombre de cases occupees à partir de sa postion horizontale
		constraint.insets = titleInset; //marges autour de l'element
	    conteneurPrincipal.add(titreBPDomaine, constraint); //ajout du titreBPDomaine dans conteneurPrincipal
	    
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
					conteneurPrincipal.revalidate();
				}
			});
			
			constraint.gridwidth = 1; //nombre de cases occupees à partir de sa postion horizontale
	    	constraint.gridx = 0; //position horizontale
			constraint.gridy = ++positionCounter + i; //position de l'element a la position verticale de depart + i
			conteneurPrincipal.add(currentDomain, constraint); //ajout de la checkbox domaine
			
		    constraint.gridx = 1; //position horizontale
			constraint.gridwidth = GridBagConstraints.REMAINDER; //dernier element de sa ligne
			conteneurPrincipal.add(currentTextFieldPourcent, constraint); //ajout de la zone de texte Pourcent1
	    }
	  
		//commentaire BP par domaine
	    final JLabel commentaireBPDomaine = new JLabel("Commentaire : "); //creation du label emailCl
		
	    constraint.gridx = 0;
		positionCounter += 11;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    conteneurPrincipal.add(commentaireBPDomaine, constraint); //ajout du label emailCl
	    
	    final JTextArea textAreaCommentaireBPDomaine = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    final JScrollPane scrollPaneComBPDomaine = new JScrollPane(textAreaCommentaireBPDomaine);
	    commentaireBPDomaine.setLabelFor(textAreaCommentaireBPDomaine); //attribution de la zone de texte au label emailCl
	    
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneComBPDomaine, constraint); //ajout de la zone de texte emailCl
	    
	    /*----------------------------------------------formulaire arborescence libre----------------------------------------------------------*/
	    
	    final JLabel titreArboLibre1 = new JLabel("Arborescence libre"); //titre de la parte rapport du formulaire
	    titreArboLibre1.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = titleInset;
	    conteneurPrincipal.add(titreArboLibre1, constraint); //ajout du titreRapport dans conteneurPrincipal
	    
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
	    conteneurPrincipal.add(freeTreesPanel, constraint);
	    
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
					JOptionPane.showMessageDialog(conteneurPrincipal, 
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
	  	conteneurPrincipal.add(ajoutArboLibre, constraint); //ajout du bouton ajoutArboLibre
	
	    
	    /*----------------------------------------------formulaire demandes d'intervention----------------------------------------------------------*/
	    
		JLabel titreDI = new JLabel("Demandes d'intervention"); //titre de la parte rapport du formulaire
		titreDI.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = titleInset; //marges autour de l'element
	    conteneurPrincipal.add(titreDI, constraint); //ajout du titreRapportr dans conteneurPrincipal
	    
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
		conteneurPrincipal.add(InterventionDemandsPanel, constraint); //ajout du bouton ajoutElement
		
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
					JOptionPane.showMessageDialog(conteneurPrincipal, 
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
		conteneurPrincipal.add(addInterventionDemandMonth, constraint); //ajout du bouton ajoutElement
		
		//commentaire DI
	    final JLabel commentaireDI = new JLabel("Commentaire : "); //creation du label emailCl
		
	    constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    conteneurPrincipal.add(commentaireDI, constraint); //ajout du label emailCl
	    
	    final JTextArea textAreaCommentaireDI = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    final JScrollPane scrollPaneComDI = new JScrollPane(textAreaCommentaireDI);
	    commentaireDI.setLabelFor(textAreaCommentaireDI); //attribution de la zone de texte au label emailCl
	    
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneComDI, constraint); //ajout de la zone de texte emailCl
	    
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
	    conteneurPrincipal.add(titreDIEtat, constraint); //ajout du titreBPDomaine dans conteneurPrincipal
	    
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
					conteneurPrincipal.revalidate();
				}
			} );
			
			states.add(etat);
			nbStates.add(textFieldNbEtat);
			
			constraint.gridx = 0;
			constraint.gridy = ++positionCounter + i; 
			constraint.gridwidth = 1;
		    conteneurPrincipal.add(etat, constraint); //ajout de la checkbox etat
		    
		    constraint.gridx = 1;
			constraint.gridwidth = GridBagConstraints.REMAINDER;
			conteneurPrincipal.add(textFieldNbEtat, constraint); //ajout de la zone de texte textFieldNbEtat
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
	    conteneurPrincipal.add(commentaireDIEtat, constraint); //ajout du label commentaireDIEtat    
	    
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneComDIEtat, constraint); //ajout de scrollPaneComDIEtat
	    
	    /*----------------------------------------formulaire demandes d'intervention par domaine------------------------------------------------*/
	    
	    final JLabel titreDIDomaine = new JLabel("Demandes d'intervention par domaines"); //titre de la partie Bons preventifs par domaine du formulaire
	    titreDIDomaine.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		
	    constraint.gridx = 0; //position horizontale
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1; //nombre de cases occupees à partir de sa postion horizontale
		constraint.insets = titleInset; //marges autour de l'element
	    conteneurPrincipal.add(titreDIDomaine, constraint); //ajout du titreBPDomaine dans conteneurPrincipal
	    
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
					conteneurPrincipal.revalidate();
				}
			});
			
			interventionDomains.add(diDomaine);
			interventionPourcents.add(textFieldPourcentDI);
			
			constraint.gridwidth = 1; //nombre de cases occupees à partir de sa postion horizontale
	    	constraint.gridx = 0; //position horizontale
			constraint.gridy = ++positionCounter + i; //position de l'element a la position verticale de depart + i
			conteneurPrincipal.add(diDomaine, constraint); //ajout de la checkbox domaine
			
		    constraint.gridx = 1; //position horizontale
			constraint.gridwidth = GridBagConstraints.REMAINDER; //dernier element de sa ligne
			conteneurPrincipal.add(textFieldPourcentDI, constraint); //ajout de la zone de texte Pourcent1
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
	    conteneurPrincipal.add(commentaireDIDomaine, constraint); //ajout du label emailCl

	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneComDIDomaine, constraint); //ajout de la zone de texte emailCl
	    
	    /*----------------------------------------------formulaire arborescence libre----------------------------------------------------------*/
	    
	    final JLabel titreArboLibre2 = new JLabel("Arborescence libre"); //titre de la parte rapport du formulaire
	    titreArboLibre2.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreBP
	    
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = titleInset; //marges autour de l'element
	    conteneurPrincipal.add(titreArboLibre2, constraint); //ajout du titreRapportr dans conteneurPrincipal
	    
	    freeTrees2LastPosition = ++positionCounter;
	    Integer startFreeTree2Position = freeTrees2LastPosition;
	    
	    positionCounter += NUMBER_FREE_TREE_ALLOWED;
	    
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
	  	
	  	final List<Boolean> listFreeTreeAvailabilitys2 = Arrays.asList(new Boolean[NUMBER_FREE_TREE_ALLOWED]);
		for (int i = 0; i < listFreeTreeAvailabilitys2.size(); ++i) {
			listFreeTreeAvailabilitys2.set(i, true);
		}
	  	
	  	ajoutArboLibre2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (freeTrees2LastPosition >= NUMBER_FREE_TREE_ALLOWED + startFreeTree2Position) {
					JOptionPane.showMessageDialog(conteneurPrincipal, 
		    				"Impossible d'ajouter une arborescence libre dans la partie " + titreArboLibre2.getText(), "Erreur", 
							JOptionPane.WARNING_MESSAGE);
					
			    	ajoutArboLibre2.setText(ADD_FREE_TREE_TEXT[1]);
			    	ajoutArboLibre2.setEnabled(false);
			    	ajoutArboLibre2.setIcon(null);
			    	
			    	return;
			    }
				
				int tmpFreeTreePosition = 0;
				
				for (int i = 0; i < listFreeTreeAvailabilitys2.size(); ++i) {
					if (listFreeTreeAvailabilitys2.get(i) != false) {
						listFreeTreeAvailabilitys2.set(i, false);
						tmpFreeTreePosition = i;
						break;
					}
				}
				
				final int freeTreePosition = tmpFreeTreePosition;
				
				FreeTree freeTree = new FreeTree();
			    
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
						
						listFreeTreeAvailabilitys2.set(freeTreePosition, true);
						--freeTrees2LastPosition;
						
						ajoutArboLibre2.setText(ADD_FREE_TREE_TEXT[0]);
						ajoutArboLibre2.setEnabled(true);
						ajoutArboLibre2.setIcon(addFreeTreeIcon2);
						
						freeTrees2.remove(freeTree);
						
						conteneurPrincipal.remove(freeTree);
						conteneurPrincipal.revalidate();
					}
				});
			    
			    GridBagConstraints freeTreeConstraint = freeTree.getConstraint();
			    freeTreeConstraint.gridx = 0;
			    ++freeTreeConstraint.gridy;
			    freeTreeConstraint.gridwidth = 1;
				freeTreeConstraint.insets = new Insets(0, 0, 3, 0); //marges autour de l'element
				freeTree.add(delete, freeTreeConstraint); //ajout du bouton supprimer dans conteneurPrincipal
				
			    constraint.gridx = 0;
				constraint.gridy = startFreeTree2Position + freeTreePosition;
				constraint.insets = new Insets(0, 0, 0, 0); //marges autour de l'element
			    constraint.gridwidth = 4;
			    conteneurPrincipal.add(freeTree, constraint);
				
				conteneurPrincipal.revalidate();
				
			    if (freeTrees2LastPosition++ >= NUMBER_FREE_TREE_ALLOWED + startFreeTree2Position) {
			    	ajoutArboLibre2.setText(ADD_FREE_TREE_TEXT[1]);
			    	ajoutArboLibre2.setEnabled(false);
			    	ajoutArboLibre2.setIcon(null);
			    }
			}
		});
	  	
	    
	  	constraint.gridx = 1;
	  	constraint.gridy = ++positionCounter;
	  	constraint.gridwidth = GridBagConstraints.REMAINDER;
	  	conteneurPrincipal.add(ajoutArboLibre2, constraint); //ajout du bouton ajoutArboLibre
	    
	    /*----------------------------------------------formulaire compteurs----------------------------------------------------------*/
	    
	  	final JLabel meterTitle = new JLabel("Compteurs"); //titre de la parte rapport du formulaire
	  	meterTitle.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreBP
	  	
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = titleInset; //marges autour de l'element
	    conteneurPrincipal.add(meterTitle, constraint); //ajout du titreRapportr dans conteneurPrincipal
	    
	    final Collection<Meter> meters = new LinkedList<Meter>();
	    
	  	meterLastPosition = ++positionCounter;
	  	final int meterStartPosition = meterLastPosition;
    
	  	positionCounter += NUMBER_METER_ALLOWED;
    
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
	  	
	  	final List<Boolean> listMeterAvailabilitys = Arrays.asList(new Boolean[NUMBER_METER_ALLOWED]);
		for (int i = 0; i < listMeterAvailabilitys.size(); ++i) {
			listMeterAvailabilitys.set(i, true);
		}
  	
	  	ajoutCompteur.addActionListener(new ActionListener() {
	  		@Override
	  		public void actionPerformed(ActionEvent e) {
	  			
	  			if (meterLastPosition >= meterStartPosition + NUMBER_METER_ALLOWED) {
	  				JOptionPane.showMessageDialog(conteneurPrincipal, 
		    				"Impossible d'ajouter un compteur dans la partie " + meterTitle.getText(), "Erreur", 
							JOptionPane.WARNING_MESSAGE);
	  				
	  				ajoutCompteur.setEnabled(false);
  					ajoutCompteur.setText(ADD_METER_TEXT[1]);
  					ajoutCompteur.setIcon(null);
  					
  					return;
				}
	  			
	  			int tmpMeterPosition = 0;
				
				for (int i = 0; i < listMeterAvailabilitys.size(); ++i) {
					if (listMeterAvailabilitys.get(i) != false) {
						listMeterAvailabilitys.set(i, false);
						tmpMeterPosition = i;
						break;
					}
				}
				
				final int currentMeterPosition = tmpMeterPosition;
	  			
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
	  					listMeterAvailabilitys.set(currentMeterPosition, true);
	  					--meterLastPosition;
	  					
	  					ajoutCompteur.setEnabled(true);
	  					ajoutCompteur.setText(ADD_METER_TEXT[0]);
	  					ajoutCompteur.setIcon(addMeterIcon);
	  					
	  					meters.remove(meter);
	  					
	  					conteneurPrincipal.remove(meter);
	  					conteneurPrincipal.revalidate();
	  				}
	  			});
	  		    
	  		    GridBagConstraints meterConstraint = meter.getConstraint();
	  		    meterConstraint.gridx = 0;
	  			++meterConstraint.gridy;
	  			meterConstraint.gridwidth = 1;
	  			meterConstraint.insets = new Insets(0, 0, 3, 0); //marges autour de l'element
	  			meter.add(delete, meterConstraint); //ajout du bouton supprimer dans conteneurPrincipal
	  			
	  			meters.add(meter);
	  			
	  			meterLastPosition++;
	  			
	  			constraint.gridx = 0;
				constraint.gridy = meterStartPosition + currentMeterPosition;
				constraint.insets = new Insets(15, 0, 5, 0); //marges autour de l'element
				constraint.gridwidth = GridBagConstraints.REMAINDER;
				conteneurPrincipal.add(meter, constraint);
				
				if (meterLastPosition >= meterStartPosition + NUMBER_METER_ALLOWED) {
					ajoutCompteur.setEnabled(false);
  					ajoutCompteur.setText(ADD_METER_TEXT[1]);
  					ajoutCompteur.setIcon(null);
				}
			
				conteneurPrincipal.revalidate();
	  		}
	  	});
	  	
	  	constraint.gridx = 1;
	  	constraint.gridy = ++positionCounter;
	  	constraint.gridwidth = GridBagConstraints.REMAINDER;
	  	conteneurPrincipal.add(ajoutCompteur, constraint); //ajout du bouton ajoutCompteur
	  	
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
	    conteneurPrincipal.add(valideForm, constraint); //ajout du bouton de validation
	    
		//action declenchee par le bouton de validation du formulaire
	    valideForm.addActionListener(new ActionListener() {
	    	
		    public void actionPerformed(ActionEvent arg0) {	
		    	/*---affichage console---*/
		    	//partie rapport
		    	System.out.println(titreRedacteur.getText()); 						//affichage console du titre de la partie du formulare 	
		    	System.out.println(nom.getText() + textFieldNom.getText()); 		//affichage console des données nom
		    	System.out.println(adr.getText() + textAreaAdr.getText()); 			//affichage console des données adr
		    	//System.out.println(tel.getText() + textFieldTelRedac.getText()); 	//affichage console des données tel
		    	System.out.println(email.getText() + textFieldEmail.getText()); 	//affichage console des données email
		    	System.out.println(nomCA.getText() + textFieldNomCA.getText());		//affichage console des données nomCA
		    	//partie client
		    	System.out.println(titreClient.getText()); 									//affichage console du titre de la partie du formulaire
		    	System.out.println(nomSite.getText() + textFieldNomSite.getText()); 		//affichage console des données nomSite
		    	System.out.println(code.getText() + textFieldCode.getText()); 				//affichage console des données code
		    	System.out.println(adrCl.getText() + textAreaAdrCl.getText()); 				//affichage console des données adrCl
		    	System.out.println(codePostal.getText() + finalTtextFieldCodePostal.getText()); 	//affichage console des données codePostal
		    	System.out.println(ville.getText() + textFieldVille.getText()); 			//affichage console des données ville
		    	System.out.println(telCl.getText() + finalTextFieldTelCl.getText()); 			//affichage console des données telCl
		    	System.out.println(emailCl.getText() + textFieldEmailCl.getText()); 		//affichage console des données emailCl  
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
		    	fw.println (titreRedacteur.getText()); 					 	//ecriture du titre de la partie du formulare 	
		    	fw.println (nom.getText() + textFieldNom.getText()); 	 	//ecriture des donnees nom
		    	fw.println (adr.getText() + textAreaAdr.getText()); 	 	//ecriture des donnees adr
		    	fw.println (tel.getText() + finalTextFieldTelRedac.getText()); 	//ecriture des donnees tel
		    	fw.println (email.getText() + textFieldEmail.getText()); 	//ecriture des donnees email
		    	fw.println (nomCA.getText() + textFieldNomCA.getText()); 	//ecriture des donnees nomCA
		    	//partie client
		    	fw.println (titreClient.getText()); 								//ecriture du titre de la partie du formulaire
			    fw.println (nomSite.getText() + textFieldNomSite.getText());		//ecriture des données nomSite
			    fw.println (code.getText() + textFieldCode.getText()); 				//ecriture des données code
			    fw.println (adrCl.getText() + textAreaAdrCl.getText()); 		    //ecriture des données adrCl
			    fw.println (codePostal.getText() + finalTtextFieldCodePostal.getText());  //ecriture des données codePostal
			    fw.println (ville.getText() + textFieldVille.getText()); 			//ecriture des données ville
			    fw.println (telCl.getText() + finalTextFieldTelCl.getText()); 			//ecriture des données telCl
			    fw.println (emailCl.getText() + textFieldEmailCl.getText()); 		//ecriture des données emailCl
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
				    	
				    	IDataHandler writerPart = new DefaultDataHandler(titreRedacteur.getText());
						
				    	if (textFieldNom.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + nom.getText() + "\" de la partie " + titreRedacteur.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		writerPart.addString(textFieldNom.getText(), nom.getText());   // nom
				    	}
				    	
				    	if (textAreaAdr.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + adr.getText() + "\" de la partie " + titreRedacteur.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		writerPart.addString(textAreaAdr.getText(), adr.getText());   // adresse 
				    	}
				    	
				    	if (finalTextFieldTelRedac.getValue().equals("              ")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + tel.getText() + "\" de la partie " + titreRedacteur.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		writerPart.addString(finalTextFieldTelRedac.getText(), tel.getText());   // telephone 
				    	}
				    	
				    	if (textFieldEmail.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + email.getText() + "\" de la partie " + titreRedacteur.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		writerPart.addString(textFieldEmail.getText(), email.getText()); // email
				    	}
				    	
				    	if (textFieldNomCA.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + nomCA.getText() + "\" de la partie " + titreRedacteur.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		writerPart.addString(textFieldNomCA.getText(), nomCA.getText()); // nom du chage d'affaire
				    	}
				    	
				    	datas.add(writerPart);

				    	publish(pBarFrame.getProgress() + incrementUnit);
				    	
				    	/*-----------------Partie client-----------------*/
				    	
				    	IDataHandler clientPart = new DefaultDataHandler(titreClient.getText());
				    	
				    	if (textFieldNomSite.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + nomSite.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(textFieldNomSite.getText(), nomSite.getText());    // nom
				    	}
				    	
				    	if (textFieldCode.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + code.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(textFieldCode.getText(), code.getText());       // code
				    	}
				    	
				    	if (textAreaAdrCl.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + adrCl.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(textAreaAdrCl.getText(), adrCl.getText());      // adresse
				    	}
				    	
				    	if (finalTtextFieldCodePostal.getValue().equals("      ")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + codePostal.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(finalTtextFieldCodePostal.getText(), codePostal.getText()); // code postal
				    	}
				    	
				    	if (textFieldVille.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + ville.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(textFieldVille.getText(), ville.getText());      // ville
				    	}
				    	
				    	if (finalTextFieldTelCl.getValue().equals("              ")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + telCl.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(finalTextFieldTelCl.getText(), telCl.getText());      // telephone client
				    	}
				    	
				    	if (textFieldEmailCl.getText().equals("")) {
				    		JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + emailCl.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		stopPdfCreation(pBarFrame);
				    		return null;
				    	}
				    	else {
				    		clientPart.addString(textFieldEmailCl.getText(), emailCl.getText());    // email client
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
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" +  PREVENTIVE_VOUCHER_MONTH_LABELS[1] + "\" de la partie " + titreBP.getText() + 
						    				" du mois numéro " + counter + " doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else if (!OperationUtilities.isNumeric(textFieldNbBPOuverts.getText())) {
						    		JOptionPane.showMessageDialog(fenetre, 
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
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" + PREVENTIVE_VOUCHER_MONTH_LABELS[2] + "\" de la partie " + titreBP.getText() +
						    				" du mois numéro " + counter + " doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else if (!OperationUtilities.isNumeric(textFieldNbBPFermes.getText())) {
						    		JOptionPane.showMessageDialog(fenetre, 
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
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" +  PREVENTIVE_VOUCHER_MONTH_LABELS[1] + "\" de la partie " + titreBP.getText() + 
						    				" du mois numéro " + counter + " doit être remplis avec un nombre "
						    						+ "(Les bons préventifs completement vides seront ignorés)", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else if (!OperationUtilities.isNumeric(textFieldNbBPOuverts.getText())) {
						    		JOptionPane.showMessageDialog(fenetre, 
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
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" + PREVENTIVE_VOUCHER_MONTH_LABELS[2] + "\" de la partie " + titreBP.getText() +
						    				" du mois numéro " + counter + " doit être remplis avec un nombre "
						    						+ "(Les bons préventifs completement vides seront ignorés)", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else if (!OperationUtilities.isNumeric(textFieldNbBPFermes.getText())) {
						    		JOptionPane.showMessageDialog(fenetre, 
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
				    		final int dialogResult = JOptionPane.showConfirmDialog (fenetre, 
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
								JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en bare dans la partie"
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
					    			JOptionPane.showMessageDialog(fenetre, 
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
				    					JOptionPane.showMessageDialog(fenetre, 
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
				    					JOptionPane.showMessageDialog(fenetre, 
							    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreBPDomaine.getText() + 
							    				" n'est pas un nombre valide", "Erreur", 
												JOptionPane.WARNING_MESSAGE);
				    					stopPdfCreation(pBarFrame);
				    					return null;
				    				}
				    				
				    				total += currentPourcentage;
				    				
				    				if (total > 100 && !continueAbove100) {
				    					int dialogResult = JOptionPane.showConfirmDialog (fenetre, 
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
				    			int dialogResult = JOptionPane.showConfirmDialog (fenetre, 
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
								JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en bare dans la partie"
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
								JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en camembert dans la partie " +
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
				    			JOptionPane.showMessageDialog(fenetre, 
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
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"Elément : \" de la partie " + currentTree.getTitleTextField().getText() + 
						    				" (Arboresence libre 1) doit être remplis", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
				    			else if (currentElementNumber.getText().equals("") || 
				    					!OperationUtilities.isNumeric(currentElementNumber.getText())) {
				    				JOptionPane.showMessageDialog(fenetre, 
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
									JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en barre dans la partie " +
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
				    		final int dialogResult = JOptionPane.showConfirmDialog (fenetre, 
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
								JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en bare dans la partie"
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
				    				JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" + currentState.getText() + "\" de la partie " + titreDIEtat.getText() + 
						    				" doit être remplis avec un nombre si la case est cochée", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
				    				stopPdfCreation(pBarFrame);
				    				return null;
				    			}
				    			else if (!OperationUtilities.isNumeric(currentNbState.getText())) {
				    				JOptionPane.showMessageDialog(fenetre, 
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
				    					JOptionPane.showMessageDialog(fenetre, 
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
				    					JOptionPane.showMessageDialog(fenetre, 
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
								JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en camembert dans la partie "
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
					    			JOptionPane.showMessageDialog(fenetre, 
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
				    					JOptionPane.showMessageDialog(fenetre, 
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
				    					JOptionPane.showMessageDialog(fenetre, 
							    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreDIDomaine.getText() + 
							    				" n'est pas un nombre valide", "Erreur", 
												JOptionPane.WARNING_MESSAGE);
				    					stopPdfCreation(pBarFrame);
				    					return null;
				    				}
				    				
				    				total += currentPourcentage;
				    				
				    				if (total > 100 && !continueAbove100) {
				    					int dialogResult = JOptionPane.showConfirmDialog (fenetre, 
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
				    			int dialogResult = JOptionPane.showConfirmDialog (fenetre, 
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
								JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en camembert dans la partie " +
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
				    			JOptionPane.showMessageDialog(fenetre, 
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
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"Elément : \" de la partie " + currentTree.getTitleTextField().getText() + 
						    				" (Arboresence libre 2) doit être remplis", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
				    			else if (currentElementNumber.getText().equals("") || 
				    					!OperationUtilities.isNumeric(currentElementNumber.getText())) {
				    				JOptionPane.showMessageDialog(fenetre, 
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
									JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en barre dans la partie " +
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
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"Consommation : \" de la partie " + 
						    				" Compteur doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
				    			else if (!OperationUtilities.isNumeric(currentConsumption.getText())) {
				    				JOptionPane.showMessageDialog(fenetre, 
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
									JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en"
											+ " barre dans la partie Compteur : \n"
											+ e.getMessage(), "Erreur", 
											JOptionPane.WARNING_MESSAGE);
								}
				    		}
							
							if (!currentMeterData.isEmpty()) {
								datas.add(currentMeterData);
							}
						}
				    	
						try {
							if (!datas.isEmpty()) {
								// Finallement on creer le document
								CreateReportDocument.createPdf(datas, pBarFrame);
								JOptionPane.showMessageDialog(fenetre, "Rapport généré", "Rapport généré", 
										JOptionPane.INFORMATION_MESSAGE);
								publish(ProgressBarFrame.MY_MAXIMUM);
								stopPdfCreation(pBarFrame);

							}
							else {
								JOptionPane.showMessageDialog(fenetre, 
					    				"Aucune donnée à rédiger dans le rapport", "Erreur", 
										JOptionPane.WARNING_MESSAGE);
								stopPdfCreation(pBarFrame);
							}
						} 
						catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(fenetre, e.getMessage(), "Erreur", 
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
	    
	    conteneur.add(conteneurPrincipal, BorderLayout.CENTER); //ajout du conteneur principal contenant tous les champs formulaires au centre du conteuneur
	    
	    JScrollPane scrollPanePrincipale = new JScrollPane(conteneur); //scrollPane principale sur le conteneur
	    scrollPanePrincipale.getVerticalScrollBar().setUnitIncrement(16); //vitesse de scroll
	    
	    fenetre.add(titreFacilitiesRapport, BorderLayout.NORTH); //ajout du titre en haut de la fenetre 
	    fenetre.add(scrollPanePrincipale, BorderLayout.CENTER); //ajout de la scrollPane principale (et du conteneur) au centre de la fenetre
	    
	    this.setContentPane(fenetre);
	    
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
}