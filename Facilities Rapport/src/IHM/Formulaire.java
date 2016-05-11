package IHM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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

public class Formulaire extends JFrame{
	
	/**
	 * Truc utilise par JFrame
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Declaration du textField telephone initialise dans le try
	 */
	private JFormattedTextField textFieldTelRedac; 	  
	/**
	 * Declaration du textField code postal initialise dans le try
	 */
	private JFormattedTextField textFieldCodePostal;  
	/**
	 * Declaration du textField telephone client initialise dans le try
	 */
	private JFormattedTextField textFieldTelCl;
	/**
	 * Declaration du textField date debut initialise dans le try
	 */
	private JFormattedTextField textFieldDateDebut;
	/**
	 * Declaration du textField date fin initialise dans le try
	 */
	private JFormattedTextField textFieldDateFin;
	/**
	 * Declaration de la comboBox des durée de rapport
	 */
	private JComboBox<String>   comboBoxRapport;

	/**
	 * Declaration de la Collection<JFormattedTextField> des Pourcents date fin remplis dans le try
	 */
	private Collection<JFormattedTextField> textFieldPourcentsBP;
	/**
	 * Declaration de la comboBox des mois pour les DI
	 */
	private JComboBox<String>   comboBoxMoisDI;
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
	/**
	 * Declaration de la Collection<JCheckBox> des domaines de bon de prevention remplis au dessus du try
	 */
	private Collection<JCheckBox> domainesBP;
	
	/**
	 * String utilisee pour chaque JLable de mois de bons preventifs
	 */
	private final String preventiveVoucherMonthLabel    = "Mois : ";
	private final String nbPreventiveVoucherOpenedLabel = "Nombre de bons préventifs ouverts : ";
	private final String nbPreventiveVoucherClosedLabel = "Nombre de bons préventifs fermés : ";
	private final String preventiveVoucherCommentLabel  = "Commentaire : ";
    
	/**
	 * liste differents choix de la duree du rapport d'activite
	 */
	private String[] choixMois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", 
			"Août", "Septembre", "Octobre", "Novembre", "Décembre"}; 
	
	private int numberPreventiveMonthAllowed = 100;
	
	private int positionCounter;
	private int preventiveVoucherLastMonthPosition;
	private int positionElement;
	private int positionMoisDI;
	private int positionElement2;
	private int positionMoisCompteur;
	
	
	public Formulaire() throws IOException{
	    // Lien vers ce formulaire pour l'affichage de fenetre d'information
	    Formulaire mainFrame = this;
		
		JPanel fenetre = new JPanel(); //creation de la fenetre principale
		
		this.setTitle("Facilities Rapport"); //titre fenetre
		this.setSize(700, 600); //taille fenetre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //pour fermer la fenetre
	    this.setLocationRelativeTo(null); //position de la fenetre
	    fenetre.setBackground(Color.white); //couleur de fond de la fenetre
	    fenetre.setLayout(new BorderLayout()); 
	    
	    JLabel titreFacilitiesRapport = new JLabel("Facilities Rapport", SwingConstants.CENTER); //titre formulaire
		titreFacilitiesRapport.setFont(new Font("Arial",Font.BOLD,18)); //police + taille du titre formulaire
		
		JPanel conteneur = new JPanel();
		JPanel conteneurPrincipal = new JPanel(new GridBagLayout());
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.BOTH;   
		
		positionCounter = 0;
		
		/*-----------------------------------------formulaire redacteur--------------------------------------------*/
	    
		JLabel titreRedacteur = new JLabel("Redacteur"); //titre de la partie redacteur du formulaire
		titreRedacteur.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreRedacteur
		constraint.gridx = 0;
		constraint.gridy = positionCounter;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreRedacteur, constraint); //ajout du titreRedacteur dans conteneurPrincipal
		
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		//nom
		JLabel nom = new JLabel("Nom : "); //creation du label nom
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		conteneurPrincipal.add(nom, constraint); //ajout du label
		JTextField textFieldNom = new JTextField(15); //creation de la zone de texte adr de taille 15
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		nom.setLabelFor(textFieldNom); //attribution de la zone de texte au label adr
		conteneurPrincipal.add(textFieldNom, constraint); //ajout de la zone de texte adr
		
		//adresse 
		JLabel adr = new JLabel("Adresse : "); //creation du label adr
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(adr, constraint); //ajout du label adr
	    JTextArea textAreaAdr = new JTextArea(3, 15); //creation de la zone de texte adr de taille 3 en hauteur et 15 en largeur
	    JScrollPane scrollPaneAdr = new JScrollPane(textAreaAdr);
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
	    adr.setLabelFor(textAreaAdr); //attribution de la zone de texte au label adr
	    conteneurPrincipal.add(scrollPaneAdr, constraint); //ajout de la zone de texte adr
	    
	    //telephone
	    JLabel tel = new JLabel("Téléphone : "); //creation du label tel
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(tel, constraint); //ajout du label tel
	    try{
			MaskFormatter maskTel  = new MaskFormatter("## ## ## ## ##"); //masque pour le format du numero de telephone
			textFieldTelRedac = new JFormattedTextField(maskTel); //initialisation de la zone de texte tel formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    tel.setLabelFor(textFieldTelRedac); //attribution de la zone de texte au label tel
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldTelRedac, constraint); //ajout de la zone de texte tel
	   
	    //email
	    JLabel email = new JLabel("Email : "); //creation du label email
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(email, constraint); //ajout du label nom
	    JTextField textFieldEmail = new JTextField(15); //creation de la zone de texte email de taille 15
	    email.setLabelFor(textFieldEmail); //attribution de la zone de texte au label email
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(textFieldEmail, constraint); //ajout de la zone de texte email au panel redacteur 
	    
	    //nom charge d'affaire
	    JLabel nomCA = new JLabel("Nom du chargé d'affaire : "); //creation du label nomCA
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(nomCA, constraint); //ajout du label nomCA au panel redacteur
	    JTextField textFieldNomCA = new JTextField(15); //creation de la zone de texte nomCA de taille 15
	    nomCA.setLabelFor(textFieldNomCA); //attribution de la zone de texte au label nomCA
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(textFieldNomCA, constraint); //ajout de la zone de texte nomCA
	
        
		/*-------------------------------------------formulaire client-------------------------------------------------*/
		
		
		JLabel titreClient = new JLabel("Client"); //titre de la partie client du formulaire
		titreClient.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreClient
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
		conteneurPrincipal.add(titreClient, constraint); //ajout du titreClient dans le panel conteneurPrincpal
		
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		//nom
		JLabel nomSite = new JLabel("Nom du site : "); //creation du label nomSite
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		conteneurPrincipal.add(nomSite, constraint); //ajout du label nomSite
		JTextField textFieldNomSite = new JTextField(15); //creation de la zone de texte nomSite de taille 15
		nomSite.setLabelFor(textFieldNomSite); //attribution de la zone de texte au label nomSite
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNomSite, constraint); //ajout de la zone de texte nomSite
		
		//code
	    JLabel code = new JLabel("Code : "); //creation du label code
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(code, constraint); //ajout du label code
	    JTextField textFieldCode = new JTextField(15); //création de la zone de texte code
	    code.setLabelFor(textFieldCode); //attribution de la zone de texte au label code
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldCode, constraint); //ajout de la zone de texte code
		
		//adresse client
		JLabel adrCl = new JLabel("Adresse : "); //creation du label adrCl
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(adrCl, constraint); //ajout du label adrCl
	    JTextArea textAreaAdrCl = new JTextArea(3, 15); //creation de la zone de texte adrCl de taille 3 en hauteur et 15 en largeur
	    JScrollPane scrollPaneAdrCl = new JScrollPane(textAreaAdrCl);
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
	    adrCl.setLabelFor(textAreaAdrCl); //attribution de la zone de texte au label adrCl
	    conteneurPrincipal.add(scrollPaneAdrCl, constraint); //ajout de la zone de texte adrCl
	    
	    //code postal
	    JLabel codePostal = new JLabel("Code postal : "); //creation du label codePostal
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(codePostal, constraint); //ajout du label codePostal
	    try{
			MaskFormatter maskCodePostal  = new MaskFormatter("## ###"); //masque pour le format du code postal
			textFieldCodePostal = new JFormattedTextField(maskCodePostal); //initialisation de la zone de texte codePostal formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    codePostal.setLabelFor(textFieldCodePostal); //attribution de la zone de texte au label codePostal
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldCodePostal, constraint); //ajout de la zone de texte codePostal
		
		//ville
		JLabel ville = new JLabel("Ville : "); //creation du label ville
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		conteneurPrincipal.add(ville, constraint); //ajout du label ville
		JTextField textFieldVille = new JTextField(15); //creation de la zone de texte ville de taille 15
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		ville.setLabelFor(textFieldVille); //attribution de la zone de texte au label ville
		conteneurPrincipal.add(textFieldVille, constraint); //ajout de la zone de texte ville
		
		//nom du client
	    JLabel nomClient = new JLabel("Nom du client : "); //creation du label nomCl
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(nomClient, constraint); //ajout du label nomCl
	    JTextField textFieldNomClient = new JTextField(15); //creation de la zone de texte nomCl de taille 15
	    nomClient.setLabelFor(textFieldNomClient); //attribution de la zone de texte au label nomCl
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(textFieldNomClient, constraint); //ajout de la zone de texte nomCl 
	    
	    //telephone client
	    JLabel telCl = new JLabel("Téléphone : "); //creation du label telCl
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(telCl, constraint); //ajout du label telCl
	    try{
			MaskFormatter maskTelCl  = new MaskFormatter("## ## ## ## ##"); //masque pour le format du numero de telephone
			textFieldTelCl = new JFormattedTextField(maskTelCl); //initialisation de la zone de texte telCl formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    telCl.setLabelFor(textFieldTelCl); //attribution de la zone de texte au label telCl
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldTelCl, constraint); //ajout de la zone de texte telCl
	   
	    //email client
	    JLabel emailCl = new JLabel("Email : "); //creation du label emailCl
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(emailCl, constraint); //ajout du label emailCl
	    JTextField textFieldEmailCl = new JTextField(15); //creation de la zone de texte emailCl de taille 15
	    emailCl.setLabelFor(textFieldEmailCl); //attribution de la zone de texte au label emailCl
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(textFieldEmailCl, constraint); //ajout de la zone de texte emailCl
	    
		
		/*----------------------------------------------formulaire rapport----------------------------------------------------------*/
	    
		JLabel titreRapport = new JLabel("Rapport"); //titre de la partie rapport du formulaire
		titreRapport.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreRapport
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreRapport, constraint); //ajout du titreRapport dans conteneurPrincipal
		
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		//rapport d'activite
	    JLabel rapportActivite = new JLabel("Rapport d'activité : "); ////creation du label rapportActivite
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.RELATIVE;
		conteneurPrincipal.add(rapportActivite, constraint); //ajout du label rapportActivite
		String[] choixRapport = {"Hebdomadaire", "Mensuel", "Bimensuel", "Trimestriel", "Semestriel", "Annuel"}; //liste des differents choix de la duree du rapport d'activite
		comboBoxRapport = new JComboBox<String>(choixRapport); //initialisation du comboBox comboBoxRapport avec la liste choixRapport
		comboBoxRapport.setPreferredSize(new Dimension(100, 20)); //dimension de la comboBoxRapport
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		rapportActivite.setLabelFor(comboBoxRapport); //attribution de la comboBox comboBoxRapport au label rapportActivite
		conteneurPrincipal.add(comboBoxRapport, constraint); //ajout de la comboBox comboBoxRapport
	    
	    //date debut
	    JLabel dateDebut = new JLabel("Date de début : "); //creation du label dateDebut
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(dateDebut, constraint); //ajout du label dateDebut
	    try{
			MaskFormatter maskDate  = new MaskFormatter("##/##/####"); //masque pour le format date
			textFieldDateDebut = new JFormattedTextField(maskDate); //initialisation de la zone de texte textFieldDateDebut formattee par le masque maskDate
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    dateDebut.setLabelFor(textFieldDateDebut); //attribution de la zone de texte textFieldDateDebut au label dateDebut
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldDateDebut, constraint); //ajout de la zone de texte textFieldDateDebut
		
		//date fin
	    JLabel dateFin = new JLabel("Date de fin : "); //creation du label dateFin
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(dateFin, constraint); //ajout du label dateFin
	    try{
			MaskFormatter maskDate  = new MaskFormatter("##/##/####"); //masque pour le format date
			textFieldDateFin = new JFormattedTextField(maskDate); //initialisation de la zone de texte textFieldDateFin formattee par le masque maskDate
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    dateFin.setLabelFor(textFieldDateFin); //attribution de la zone de texte textFieldDateFin au label dateFin
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldDateFin, constraint); //ajout de la zone de texte textFieldDateFin
		
		
		
		
		/*----------------------------------------------formulaire bons preventifs----------------------------------------------------------*/
	    
		JLabel titreBP = new JLabel("Bons préventifs"); //titre de la partie bons preventifs du formulaire
		titreBP.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreBP
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreBP, constraint); //ajout du titreBP dans conteneurPrincipal
		
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    
	    Collection<JComboBox<String>> preventivesVouchersMonths        = new ArrayList<JComboBox<String>>();
	    Collection<JTextField>        nbPreventivesVouchersOpened      = new ArrayList<JTextField>();
	    Collection<JTextField>        nbPreventivesVouchersClosed      = new ArrayList<JTextField>();
	    Collection<JTextArea>         commentsPreventivesVouchers      = new ArrayList<JTextArea>();

	    preventiveVoucherLastMonthPosition = positionCounter;
	    positionCounter += numberPreventiveMonthAllowed;
	    
	    // Creation du mois
	    createPreventiveVoucherMonth(constraint, conteneurPrincipal, choixMois,
	    		preventivesVouchersMonths, nbPreventivesVouchersOpened, 
	    		nbPreventivesVouchersClosed, commentsPreventivesVouchers);
	    
	    JButton ajoutMoisBP = new JButton("+ Ajouter un mois");
		
		constraint.gridx = 1;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(ajoutMoisBP, constraint); //ajout du bouton ajoutElement
		
		ajoutMoisBP.addActionListener(new ActionListener() {
			    	
			public void actionPerformed(ActionEvent arg0) {
				createPreventiveVoucherMonth(constraint, conteneurPrincipal, choixMois, 
						preventivesVouchersMonths, nbPreventivesVouchersOpened, 
						nbPreventivesVouchersClosed, commentsPreventivesVouchers);
				conteneurPrincipal.revalidate();
			}
		});	    
	    
	    /*----------------------------------------formulaire bons preventifs par domaine------------------------------------------------*/
	    
	    JLabel titreBPDomaine = new JLabel("Bons préventifs par domaines"); //titre de la partie Bons preventifs par domaine du formulaire
	    titreBPDomaine.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		constraint.gridx = 0; //position horizontale
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1; //nombre de cases occupees à partir de sa postion horizontale
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreBPDomaine, constraint); //ajout du titreBPDomaine dans conteneurPrincipal
	    
	    String[] listeDomaines = {"Clos et ouvert", "Aménagement intérieur", "Ascenseur, monte-charge", "CVC", "Plomberie sanitaire",
	    						  "Electricité CFO", "Sûreté", "Sécurité détection incendie", "Aménagements extérieurs", "Centrale énergie",
	    						  "Cont, réglementaire"}; //liste des differents domaines
	    int nbDomaines = listeDomaines.length; //taille de la liste des domaines
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    
	    textFieldPourcentsBP = new ArrayList<JFormattedTextField>();
	    domainesBP           = new ArrayList<JCheckBox>();
	    
	    for(int i = 0; i < nbDomaines; i++){
	    	
	    	JCheckBox currentDomain = new JCheckBox(listeDomaines[i]);
	    	domainesBP.add(currentDomain);
			
	    	MaskFormatter maskPourcent = null;
			try{
				maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format pourcentage
		    }
			catch(ParseException e){
				e.printStackTrace(); //exception
			}
			
			final JFormattedTextField currentTextFieldPourcent = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte Pourcent1 formattee par le masque
			textFieldPourcentsBP.add(currentTextFieldPourcent);
			
			currentTextFieldPourcent.setEnabled(false);
			
			currentDomain.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					currentTextFieldPourcent.setEnabled(currentDomain.isSelected());
					fenetre.revalidate();
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
	    JLabel commentaireBPDomaine = new JLabel("Commentaire : "); //creation du label emailCl
		constraint.gridx = 0;
		positionCounter = positionCounter + 11;
		constraint.gridy = positionCounter;
		constraint.gridwidth = 1;
		 constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    conteneurPrincipal.add(commentaireBPDomaine, constraint); //ajout du label emailCl
	    JTextArea textAreaCommentaireBPDomaine = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneComBPDomaine = new JScrollPane(textAreaCommentaireBPDomaine);
	    commentaireBPDomaine.setLabelFor(textAreaCommentaireBPDomaine); //attribution de la zone de texte au label emailCl
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneComBPDomaine, constraint); //ajout de la zone de texte emailCl
	    
	    /*----------------------------------------------formulaire arborescence libre----------------------------------------------------------*/
	    
		JLabel titreArboLibre = new JLabel("Arborescence libre"); //titre de la parte rapport du formulaire
		titreArboLibre.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreArboLibre, constraint); //ajout du titreRapportr dans conteneurPrincipal
		
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		//titre
	    JLabel titre = new JLabel("Titre : ");
	    constraint.gridy = ++positionCounter;
		conteneurPrincipal.add(titre, constraint); //ajout du label 
		JTextField textFieldTitre = new JTextField(15); //creation de la zone de texte nomSite de taille 15
		titre.setLabelFor(textFieldTitre); //attribution de la zone de texte au label nomSite
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldTitre, constraint); //ajout de la zone de texte nomSite
		
	    //element
	    JLabel element = new JLabel("Elément : "); //creation du label dateDebut
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(element, constraint); //ajout du label nbBPOuverts
	    JTextField textFieldElement = new JTextField(15); //initialisation de la zone de texte textFieldNbBPOuverts
	    element.setLabelFor(textFieldElement); //attribution de la zone de texte au label nbBPOuverts
		constraint.gridx = 1;
		constraint.gridwidth = 1;
		conteneurPrincipal.add(textFieldElement, constraint); //ajout de la zone de texte textFieldNbBPOuverts
		
		//nombre
	    JLabel nombre = new JLabel("Nombre : "); //creation du label dateDebut
		constraint.gridx = 2;
		constraint.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(nombre, constraint); //ajout du label dateDebut
	    JTextField textFieldNombre = new JTextField(2); //initialisation de la zone de texte dateFin formattee par le masque
	    nombre.setLabelFor(textFieldNombre); //attribution de la zone de texte au label dateFin
		constraint.gridx = 3;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNombre, constraint); //ajout de la zone de texte dateFin

		//bouton d'ajout d'element
		positionElement = ++positionCounter;
		
		JButton ajoutElement = new JButton("+ Ajouter un élément");
		constraint.gridx = 1;
		positionCounter = positionCounter + 100;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(ajoutElement, constraint); //ajout du bouton ajoutElement
		
		ajoutElement.addActionListener(new ActionListener() {
	    	
		    public void actionPerformed(ActionEvent arg0) {	
		    	constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		    	//element
			    JLabel element = new JLabel("Elément : "); //creation du label dateDebut
				constraint.gridx = 0;
				constraint.gridy = ++positionElement;
				constraint.gridwidth = 1;
			    conteneurPrincipal.add(element, constraint); //ajout du label nbBPOuverts
			    JTextField textFieldElement = new JTextField(15); //initialisation de la zone de texte textFieldNbBPOuverts
			    element.setLabelFor(textFieldElement); //attribution de la zone de texte au label nbBPOuverts
				constraint.gridx = 1;
				constraint.gridwidth = 1;
				conteneurPrincipal.add(textFieldElement, constraint); //ajout de la zone de texte textFieldNbBPOuverts
				
				//nombre
			    JLabel nombre = new JLabel("Nombre : "); //creation du label dateDebut
				constraint.gridx = 2;
				constraint.gridwidth = GridBagConstraints.RELATIVE;
			    conteneurPrincipal.add(nombre, constraint); //ajout du label dateDebut
			    JTextField textFieldNombre = new JTextField(2); //initialisation de la zone de texte dateFin formattee par le masque
			    nombre.setLabelFor(textFieldNombre); //attribution de la zone de texte au label dateFin
				constraint.gridx = 3;
				constraint.gridwidth = GridBagConstraints.REMAINDER;
				conteneurPrincipal.add(textFieldNombre, constraint); //ajout de la zone de texte dateFin	
				
				fenetre.revalidate();
		    }
		});
		
		
		//commentaire
	    JLabel commentaire = new JLabel("Commentaire : "); //creation du label emailCl
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    conteneurPrincipal.add(commentaire, constraint); //ajout du label emailCl
	    JTextArea textAreaCommentaire = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneCom = new JScrollPane(textAreaCommentaire);
	    commentaire.setLabelFor(textAreaCommentaire); //attribution de la zone de texte au label emailCl
	    ++positionCounter;
		constraint.gridy = positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneCom, constraint); //ajout de la zone de texte emailCl
	    
	    //bouton d'ajout d'arborescence libre
	  	JButton ajoutArboLibre = new JButton("+ Ajouter une arborescence libre");
	  	constraint.gridx = 0;
	  	constraint.gridy = ++positionCounter;
	  	constraint.gridwidth = 1;
	  	conteneurPrincipal.add(ajoutArboLibre, constraint); //ajout du bouton ajoutArboLibre
	
	    
	    /*----------------------------------------------formulaire demandes d'intervention----------------------------------------------------------*/
	    
		JLabel titreDI = new JLabel("Demandes d'intervention"); //titre de la parte rapport du formulaire
		titreDI.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreDI, constraint); //ajout du titreRapportr dans conteneurPrincipal
		
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		//mois
	    JLabel moisDI = new JLabel("Mois : ");
	    constraint.gridy = ++positionCounter;
		conteneurPrincipal.add(moisDI, constraint); //ajout du label
		comboBoxMoisDI = new JComboBox<String>(choixMois);
		comboBoxMoisDI.setPreferredSize(new Dimension(100, 20));
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		moisDI.setLabelFor(comboBoxMoisDI); //attribution de la zone de texte au label adr
		conteneurPrincipal.add(comboBoxMoisDI, constraint); //ajout de la zone de texte adr
		
	    //nombre d'interventions
	    JLabel nbIntervention = new JLabel("Nombre d'interventions : "); //creation du label dateDebut
	    constraint.gridx = 0;
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(nbIntervention, constraint); //ajout du label nbBPOuverts
	    JTextField textFieldNbIntervention = new JTextField(2); //initialisation de la zone de texte textFieldNbBPOuverts
	    nbIntervention.setLabelFor(textFieldNbIntervention); //attribution de la zone de texte au label nbBPOuverts
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNbIntervention, constraint); //ajout de la zone de texte textFieldNbBPOuverts
		
		//bouton d'ajout de mois pour les BP
		positionMoisDI = ++positionCounter;
				
		JButton ajoutMoisDI = new JButton("+ Ajouter un mois");
		constraint.gridx = 1;
		positionCounter = ++positionCounter + 100;
		constraint.gridy = positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(ajoutMoisDI, constraint); //ajout du bouton ajoutElement
				
		ajoutMoisDI.addActionListener(new ActionListener() {
					    	
			public void actionPerformed(ActionEvent arg0) {	
				constraint.insets = new Insets(7, 7, 3, 7); //marges autour de l'element
						
				//mois
				JLabel moisDI = new JLabel("Mois : ");
				constraint.gridx = 0;
				constraint.gridy = ++positionMoisDI;
				conteneurPrincipal.add(moisDI, constraint); //ajout du label moisBP
				String[] choixMois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"}; //liste differents choix de la duree du rapport d'activite
				comboBoxMoisDI = new JComboBox<String>(choixMois); //initialisation de la comboBox comboBoxMoisBP avec la liste choixMois
				comboBoxMoisDI.setPreferredSize(new Dimension(100, 20)); //dimension de la comboBoxMoisBP
				constraint.gridx = 1;
				constraint.gridwidth = GridBagConstraints.REMAINDER;
				moisDI.setLabelFor(comboBoxMoisDI); //attribution de la comboBox comboBoxMoisBP au label moisBP
				conteneurPrincipal.add(comboBoxMoisDI, constraint); //ajout de la zone de texte comboBox comboBoxMoisBP
						
				constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
						
				//nombre d'interventions
				JLabel nbIntervention = new JLabel("Nombre d'interventions : "); //creation du label nbBPOuverts
				constraint.gridx = 0;
				constraint.gridy = ++positionMoisDI;
				constraint.gridwidth = 1;
				conteneurPrincipal.add(nbIntervention, constraint); //ajout du label nbBPOuverts
				JTextField textFieldNbIntervention = new JTextField(2); //creation de la zone de texte textFieldNbBPOuverts
				nbIntervention.setLabelFor(textFieldNbIntervention); //attribution de la zone de texte au label nbBPOuverts
				constraint.gridx = 1;
				constraint.gridwidth = GridBagConstraints.REMAINDER;
				conteneurPrincipal.add(textFieldNbIntervention, constraint); //ajout de la zone de texte textFieldNbBPOuverts
						
				fenetre.revalidate();
			}
		});
		
		//commentaire DI
	    JLabel commentaireDI = new JLabel("Commentaire : "); //creation du label emailCl
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		 constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    conteneurPrincipal.add(commentaireDI, constraint); //ajout du label emailCl
	    JTextArea textAreaCommentaireDI = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneComDI = new JScrollPane(textAreaCommentaireDI);
	    commentaireDI.setLabelFor(textAreaCommentaireDI); //attribution de la zone de texte au label emailCl
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneComDI, constraint); //ajout de la zone de texte emailCl
	    
	    
	    /*----------------------------------------formulaire demandes d'intervention par états------------------------------------------------*/
	    JLabel titreDIEtat = new JLabel("Demandes d'intervention par états"); //titre de la partie Bons preventifs par domaine du formulaire
	    titreDIEtat.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
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
			textFieldNbEtat.setEnabled(false);
			
			JCheckBox etat = new JCheckBox(listeEtats[i]); //creation d'une checkbox pour chaque etat possible
			
			etat.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					textFieldNbEtat.setEnabled(etat.isSelected());
					mainFrame.revalidate();
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
	    JLabel commentaireDIEtat = new JLabel("Commentaire : "); //creation du label commentaireDIEtat
	    
	    JTextArea textAreaCommentaireDIEtat = new JTextArea(4, 15); //creation de la zone de texte textAreaCommentaireDIEtat
	    JScrollPane scrollPaneComDIEtat = new JScrollPane(textAreaCommentaireDIEtat); //creation de la scrollPane scrollPaneComDIEtat contenant textAreaCommentaireDIEtat
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
	    
	    JLabel titreDIDomaine = new JLabel("Demandes d'intervention par domaines"); //titre de la partie Bons preventifs par domaine du formulaire
	    titreDIDomaine.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		
	    constraint.gridx = 0; //position horizontale
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1; //nombre de cases occupees à partir de sa postion horizontale
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreDIDomaine, constraint); //ajout du titreBPDomaine dans conteneurPrincipal
	    
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    
	    Collection<JCheckBox>  interventionDomains = new ArrayList<JCheckBox>();
	    Collection<JFormattedTextField> interventionPourcents = new ArrayList<JFormattedTextField>();
	    
	    for(int i = 0; i < nbDomaines; i++){
	    	JCheckBox diDomaine = new JCheckBox(listeDomaines[i]);
	    	
	    	MaskFormatter maskPourcent = null;
			
	    	try{
				maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format pourcentage
		    }
			catch(ParseException e){
				e.printStackTrace(); //exception
			}
			
			JFormattedTextField textFieldPourcentDI = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte Pourcent1 formattee par le masque
			textFieldPourcentDI.setEnabled(false);
			
			diDomaine.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					textFieldPourcentDI.setEnabled(diDomaine.isSelected());
					mainFrame.revalidate();
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
	    JLabel commentaireDIDomaine = new JLabel("Commentaire : "); //creation du label emailCl
	    
	    JTextArea textAreaComDIDomaine = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneComDIDomaine = new JScrollPane(textAreaComDIDomaine);
	    commentaireBPDomaine.setLabelFor(textAreaComDIDomaine); //attribution de la zone de texte au label emailCl
	    
		constraint.gridx = 0;
		positionCounter = positionCounter + 11;
		constraint.gridy = positionCounter;
		constraint.gridwidth = 1;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    conteneurPrincipal.add(commentaireDIDomaine, constraint); //ajout du label emailCl

	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneComDIDomaine, constraint); //ajout de la zone de texte emailCl
	    
	    /*----------------------------------------------formulaire arborescence libre----------------------------------------------------------*/
	    
		JLabel titreArboLibre2 = new JLabel("Arborescence libre"); //titre de la partie arborescence libre 2 du formulaire
		titreArboLibre2.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreArboLibre2
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreArboLibre2, constraint); //ajout du titreArboLibre2 dans conteneurPrincipal
		
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		//titre
	    JLabel titre2 = new JLabel("Titre : "); //creation du label titre2
	    constraint.gridy = ++positionCounter;
		conteneurPrincipal.add(titre2, constraint); //ajout du label titre2
		JTextField textFieldTitre2 = new JTextField(15); //creation de la zone de texte textFieldTitre2 de taille 15
		titre2.setLabelFor(textFieldTitre2); //attribution de la zone de texte textFieldTitre2 au label titre2
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldTitre2, constraint); //ajout de la zone de texte textFieldTitre2
		
	    //element
	    JLabel element2 = new JLabel("Elément : "); //creation du label element2
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
	    conteneurPrincipal.add(element2, constraint); //ajout du label element2
	    JTextField textFieldElement2 = new JTextField(15); //creation de la zone de texte textFieldElement2
	    element2.setLabelFor(textFieldElement2); //attribution de la zone de texte textFieldElement2 au label element2
		constraint.gridx = 1;
		constraint.gridwidth = 1;
		conteneurPrincipal.add(textFieldElement2, constraint); //ajout de la zone de texte textFieldElement2
		
		//nombre
	    JLabel nombre2 = new JLabel("Nombre  : "); //creation du label nombre2
		constraint.gridx = 2;
		constraint.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(nombre2, constraint); //ajout du label nombre2
	    JTextField textFieldNombre2 = new JTextField(2); //creation de la zone de texte textFieldNombre2
	    nombre2.setLabelFor(textFieldNombre2); //attribution de la zone de texte textFieldNombre2 au label nombre2
		constraint.gridx = 3;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNombre2, constraint); //ajout de la zone de texte textFieldNombre2
		
		//bouton d'ajout d'element
		positionElement2 = ++positionCounter;
		
		JButton ajoutElement2 = new JButton("+ Ajouter un élément");
		constraint.gridx = 1;
		positionCounter = positionCounter + 100;
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(ajoutElement2, constraint); //ajout du bouton ajoutElement
		
		ajoutElement2.addActionListener(new ActionListener() {
	    	
		    public void actionPerformed(ActionEvent arg0) {	
		    	
		    	constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		    	
		    	//element
			    JLabel element2 = new JLabel("Elément : "); //creation du label dateDebut
				constraint.gridx = 0;
				constraint.gridy = ++positionElement2;
				constraint.gridwidth = 1;
			    conteneurPrincipal.add(element2, constraint); //ajout du label nbBPOuverts
			    JTextField textFieldElement2 = new JTextField(15); //initialisation de la zone de texte textFieldNbBPOuverts
			    element.setLabelFor(textFieldElement2); //attribution de la zone de texte au label nbBPOuverts
				constraint.gridx = 1;
				constraint.gridwidth = 1;
				conteneurPrincipal.add(textFieldElement2, constraint); //ajout de la zone de texte textFieldNbBPOuverts
				
				//nombre
			    JLabel nombre2 = new JLabel("Nombre : "); //creation du label dateDebut
				constraint.gridx = 2;
				constraint.gridwidth = GridBagConstraints.RELATIVE;
			    conteneurPrincipal.add(nombre2, constraint); //ajout du label dateDebut
			    JTextField textFieldNombre2 = new JTextField(2); //initialisation de la zone de texte dateFin formattee par le masque
			    nombre2.setLabelFor(textFieldNombre2); //attribution de la zone de texte au label dateFin
				constraint.gridx = 3;
				constraint.gridwidth = GridBagConstraints.REMAINDER;
				conteneurPrincipal.add(textFieldNombre2, constraint); //ajout de la zone de texte dateFin	
				
				fenetre.revalidate();
		    }
		});
		
		//commentaire
	    JLabel commentaire2 = new JLabel("Commentaire : "); //creation du label commentaire2
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		 constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    conteneurPrincipal.add(commentaire2, constraint); //ajout du label commentaire2
	    JTextArea textAreaCommentaire2 = new JTextArea(4, 15); //creation de la zone de texte textAreaCommentaire2
	    JScrollPane scrollPaneCom2 = new JScrollPane(textAreaCommentaire2);
	    commentaire2.setLabelFor(textAreaCommentaire2); //attribution de la zone de texte textAreaCommentaire2 au label commentaire2
	    ++positionCounter;
		constraint.gridy = positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneCom2, constraint); //ajout de la scrollPaneCom2
	    
	    //bouton d'ajout d'arborescence libre
	  	JButton ajoutArboLibre2 = new JButton("+ Ajouter une arborescence libre");
	  	constraint.gridx = 0;
	  	constraint.gridy = ++positionCounter;
	  	constraint.gridwidth = 1;
	  	conteneurPrincipal.add(ajoutArboLibre2, constraint); //ajout du bouton ajoutArboLibre
	    
	    /*----------------------------------------------formulaire compteurs----------------------------------------------------------*/
	    
		JLabel titreCompteurs = new JLabel("Compteurs"); //titre de la partie compteurs du formulaire
		titreCompteurs.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreCompteurs
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreCompteurs, constraint); //ajout du titreCompteurs dans conteneurPrincipal
		
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    
	    //type de compteur
	    JLabel typeCompteur = new JLabel("Type du compteur : "); //creation du label typeCompteur
		constraint.gridy = ++positionCounter;
		conteneurPrincipal.add(typeCompteur, constraint); //ajout du label 
		String[] choixTypeCompteur = {"eau", "gaz", "électricité", "énergie"}; //differents choix de type de compteur
		comboBoxTypeCompteur = new JComboBox<String>(choixTypeCompteur);
		comboBoxTypeCompteur.setPreferredSize(new Dimension(100, 20));
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		typeCompteur.setLabelFor(comboBoxTypeCompteur); //attribution de la zone de texte comboBoxTypeCompteur au label typeCompteur
		conteneurPrincipal.add(comboBoxTypeCompteur, constraint); //ajout de la zone de texte comboBoxTypeCompteur
		
		//mois
	    JLabel moisCompteur = new JLabel("Mois : ");
	    constraint.gridx = 0;
	    constraint.gridy = ++positionCounter;
		conteneurPrincipal.add(moisCompteur, constraint); //ajout du label moisCompteur
		comboBoxMoisCompteur = new JComboBox<String>(choixMois);
		comboBoxMoisCompteur.setPreferredSize(new Dimension(100, 20));
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		moisCompteur.setLabelFor(comboBoxMoisCompteur); //attribution de la moisCompteur au label moisCompteur
		conteneurPrincipal.add(comboBoxMoisCompteur, constraint); //ajout de la comboBoxMoisCompteur
		
		//consommation
	    JLabel consommation = new JLabel("Consommation : ");
	    constraint.gridx = 0;
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = 1;
		conteneurPrincipal.add(consommation, constraint); //ajout du label consommation
		JTextField textFieldConsommation = new JTextField(15); //creation de la zone de texte textFieldConsommation de taille 15
		consommation.setLabelFor(textFieldConsommation); //attribution de la zone de texte textFieldConsommation au label consommation
		constraint.gridx = 1;
		conteneurPrincipal.add(textFieldConsommation, constraint); //ajout de la zone de texte textFieldConsommation
		
		//unite
	    String[] choixUnite = {"m³", "kWh", "MWh"}; //differents choix de l'unite
	    comboBoxUnite = new JComboBox<String>(choixUnite);
	    comboBoxUnite.setPreferredSize(new Dimension(20, 20));
		constraint.gridx = 2;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(comboBoxUnite, constraint); //ajout de la comboBoxUnite
		
		//bouton d'ajout de mois pour le compteur
		positionMoisCompteur = ++positionCounter;
		
		JButton ajoutMoisCompteur = new JButton("+ Ajouter un mois");
		constraint.gridx = 1;
		positionCounter = ++positionCounter + 100;
		constraint.gridy = positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(ajoutMoisCompteur, constraint); //ajout du bouton ajoutMoisCompteur
	
		ajoutMoisCompteur.addActionListener(new ActionListener() {
			    	
			public void actionPerformed(ActionEvent arg0) {	
				constraint.insets = new Insets(7, 7, 3, 7); //marges autour de l'element
				
				//mois
			    JLabel moisCompteur = new JLabel("Mois : ");
			    constraint.gridx = 0;
			    constraint.gridy = ++positionMoisCompteur;
				conteneurPrincipal.add(moisCompteur, constraint); //ajout du label moisCompteur
				comboBoxMoisCompteur = new JComboBox<String>(choixMois);
				comboBoxMoisCompteur.setPreferredSize(new Dimension(100, 20));
				constraint.gridx = 1;
				constraint.gridwidth = GridBagConstraints.REMAINDER;
				moisCompteur.setLabelFor(comboBoxMoisCompteur); //attribution de la moisCompteur au label moisCompteur
				conteneurPrincipal.add(comboBoxMoisCompteur, constraint); //ajout de la comboBoxMoisCompteur
				
				constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
				
				//consommation
			    JLabel consommation = new JLabel("Consommation : ");
			    constraint.gridx = 0;
			    constraint.gridy = ++positionMoisCompteur;
				constraint.gridwidth = 1;
				conteneurPrincipal.add(consommation, constraint); //ajout du label consommation
				JTextField textFieldConsommation = new JTextField(15); //creation de la zone de texte textFieldConsommation de taille 15
				consommation.setLabelFor(textFieldConsommation); //attribution de la zone de texte textFieldConsommation au label consommation
				constraint.gridx = 1;
				conteneurPrincipal.add(textFieldConsommation, constraint); //ajout de la zone de texte textFieldConsommation
				
				//unite
			    String[] choixUnite = {"m³", "kWh", "MWh"}; //differents choix de l'unite
			    comboBoxUnite = new JComboBox<String>(choixUnite);
			    comboBoxUnite.setPreferredSize(new Dimension(20, 20));
				constraint.gridx = 2;
				constraint.gridwidth = GridBagConstraints.REMAINDER;
				conteneurPrincipal.add(comboBoxUnite, constraint); //ajout de la comboBoxUnite
				
				fenetre.revalidate();
			}
		});
		
		//commentaire
		 constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    JLabel commentaireCompteur = new JLabel("Commentaire : "); //creation du label commentaireCompteur
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
	    conteneurPrincipal.add(commentaireCompteur, constraint); //ajout du label commentaireCompteur
	    JTextArea textAreaComCompteur = new JTextArea(4, 15); //creation de la zone de texte textAreaComCompteur
	    JScrollPane scrollPaneComCompteur = new JScrollPane(textAreaComCompteur);
	    commentaireCompteur.setLabelFor(textAreaComCompteur); //attribution de la zone de texte au label commentaireCompteur
	    constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(scrollPaneComCompteur, constraint); //ajout de la scrollPaneComCompteur
	    
	    //bouton d'ajout de compteur
	  	JButton ajoutCompteur = new JButton("+ Ajouter un compteur");
	  	constraint.gridx = 0;
	  	constraint.gridy = ++positionCounter;
	  	constraint.gridwidth = 1;
	  	conteneurPrincipal.add(ajoutCompteur, constraint); //ajout du bouton ajoutCompteur
	  	
	  	
		
		/*-----------------------------------------Bouton de validation du formulaire--------------------------------------------------- */
		
		JButton valideForm = new JButton("Génerer le rapport"); //bouton de validation du formulaire 
		//valideForm.setBackground(new Color(224, 35, 60));
		positionCounter = positionCounter + 100;
		constraint.gridy = positionCounter;
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
		    	System.out.println(tel.getText() + textFieldTelRedac.getText()); 	//affichage console des données tel
		    	System.out.println(email.getText() + textFieldEmail.getText()); 	//affichage console des données email
		    	System.out.println(nomCA.getText() + textFieldNomCA.getText());		//affichage console des données nomCA
		    	//partie client
		    	System.out.println(titreClient.getText()); 									//affichage console du titre de la partie du formulaire
		    	System.out.println(nomSite.getText() + textFieldNomSite.getText()); 		//affichage console des données nomSite
		    	System.out.println(code.getText() + textFieldCode.getText()); 				//affichage console des données code
		    	System.out.println(adrCl.getText() + textAreaAdrCl.getText()); 				//affichage console des données adrCl
		    	System.out.println(codePostal.getText() + textFieldCodePostal.getText()); 	//affichage console des données codePostal
		    	System.out.println(ville.getText() + textFieldVille.getText()); 			//affichage console des données ville
		    	System.out.println(telCl.getText() + textFieldTelCl.getText()); 			//affichage console des données telCl
		    	System.out.println(emailCl.getText() + textFieldEmailCl.getText()); 		//affichage console des données emailCl  
		    	//partie rapport
		    	System.out.println(titreRapport.getText()); 													//affichage console du titre de la partie du formulaire
		    	System.out.println(rapportActivite.getText() + comboBoxRapport.getSelectedItem().toString()); 	//affichage console des données duree rapport d'activite
		    	System.out.println(dateDebut.getText() + textFieldDateDebut.getText()); 						//affichage console des données code
		    	System.out.println(dateFin.getText() + textFieldDateFin.getText()); 							//affichage console des données adr
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
		    	System.out.println(titreArboLibre.getText()); 								//affichage console du titre de la partie du formulaire
		    	System.out.println(titre.getText() + textFieldTitre.getText()); 			//affichage console des données titre
		    	System.out.println(element.getText() + textFieldElement.getText()); 		//affichage console des données element
		    	System.out.println(nombre.getText() + textFieldNombre.getText()); 			//affichage console des données nombre
		    	System.out.println(commentaire.getText() + textAreaCommentaire.getText()); 	//affichage console des données commentaire
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
		    	System.out.println(titre2.getText() + textFieldTitre.getText()); 				//affichage console des données titre2
		    	System.out.println(element2.getText() + textFieldElement.getText()); 			//affichage console des données element2
		    	System.out.println(nombre2.getText() + textFieldNombre.getText()); 				//affichage console des données nombre2
		    	System.out.println(commentaire2.getText() + textAreaCommentaire.getText()); 	//affichage console des données commentaire2
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
		    	fw.println (tel.getText() + textFieldTelRedac.getText()); 	//ecriture des donnees tel
		    	fw.println (email.getText() + textFieldEmail.getText()); 	//ecriture des donnees email
		    	fw.println (nomCA.getText() + textFieldNomCA.getText()); 	//ecriture des donnees nomCA
		    	//partie client
		    	fw.println (titreClient.getText()); 								//ecriture du titre de la partie du formulaire
			    fw.println (nomSite.getText() + textFieldNomSite.getText());		//ecriture des données nomSite
			    fw.println (code.getText() + textFieldCode.getText()); 				//ecriture des données code
			    fw.println (adrCl.getText() + textAreaAdrCl.getText()); 		    //ecriture des données adrCl
			    fw.println (codePostal.getText() + textFieldCodePostal.getText());  //ecriture des données codePostal
			    fw.println (ville.getText() + textFieldVille.getText()); 			//ecriture des données ville
			    fw.println (telCl.getText() + textFieldTelCl.getText()); 			//ecriture des données telCl
			    fw.println (emailCl.getText() + textFieldEmailCl.getText()); 		//ecriture des données emailCl
		    	//partie rapport
			    fw.println (titreRapport.getText()); 													//ecriture du titre de la partie du formulaire
		    	fw.println (rapportActivite.getText() + comboBoxRapport.getSelectedItem().toString()); 	//ecriture console des données rpportActivite
		    	fw.println (dateDebut.getText() + textFieldDateDebut.getText()); 						//ecriture console des données codeDebut
		    	fw.println (dateFin.getText() + textFieldDateFin.getText()); 							//ecriture console des données dateFin
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
		    	fw.println (titreArboLibre.getText()); 								//ecriture du titre de la partie du formulaire
		    	fw.println (titre.getText() + textFieldTitre.getText()); 			//ecriture des données titre
		    	fw.println (element.getText() + textFieldElement.getText()); 		//ecriture des données element
		    	fw.println (nombre.getText() + textFieldNombre.getText()); 			//ecriture des données nombre
		    	fw.println (commentaire.getText() + textAreaCommentaire.getText()); //ecriture des données commentaire
		    	//partie domaines d'intervention
		    	fw.println(titreDI.getText()); 												//ecriture du titre de la partie du formulaire
		    	//fw.println(moisDI.getText() + comboBoxMoisBP.getSelectedItem().toString()); //ecriture des données moisDI
		    	fw.println(nbIntervention.getText() + textFieldNbIntervention.getText()); 	//ecriture des données nbIntervention
		    	//fw.println(commentaireDI.getText() + textAreaCommentaireBP.getText()); 		//ecriture des données commentaireDI
		    	//partie domaines d'intervention par etat
		    	System.out.println(titreDIEtat.getText()); 													//ecriture du titre de la partie du formulaire
		    	System.out.println (commentaireDIEtat.getText() + textAreaCommentaireDIEtat.getText()); 	//ecriture des données commentaireBPDomaine

		    	//partie arborescence libre 2
		    	fw.println(titreArboLibre2.getText()); 									//ecriture du titre de la partie du formulaire
		    	fw.println(titre2.getText() + textFieldTitre.getText()); 				//ecriture des données titre2
		    	fw.println(element2.getText() + textFieldElement.getText()); 			//ecriture des données element2
		    	fw.println(nombre2.getText() + textFieldNombre.getText()); 				//ecriture des données nombre2
		    	fw.println(commentaire2.getText() + textAreaCommentaire.getText()); 	//ecriture des données commentaire2
		    	//partie compteurs
		    	fw.println(titreCompteurs.getText()); 																	//ecriture du titre de la partie du formulaire
		    	fw.println(typeCompteur.getText() + comboBoxTypeCompteur.getSelectedItem().toString()); 				//ecriture console des données typeCompteur
		    	fw.println(moisCompteur.getText() + comboBoxMoisCompteur.getSelectedItem().toString()); 				//ecriture console des données moisCompteur
		    	fw.println(consommation.getText() + textFieldConsommation.getText() + comboBoxUnite.getSelectedItem()); //ecriture console des données consommation
		    	
		    	fw.println ("\r\n"); //retour ligne
		    	fw.close();
		    	
		    	/*-----------------Ecriture du PDF-----------------*/
		    	
		    	// On prepare les donnees
		    	Collection<IDataHandler> datas = new ArrayList<IDataHandler>();
		    	
				/*-----------------Partie Redacteur-----------------*/
		    	
		    	/*IDataHandler writerPart = new DefaultDataHandler(titreRedacteur.getText());
				
		    	if (textFieldNom.getText().equals("")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + nom.getText() + "\" de la partie " + titreRedacteur.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		writerPart.addString(textFieldNom.getText(), nom.getText());   // nom
		    	}
		    	
		    	if (textAreaAdr.getText().equals("")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + adr.getText() + "\" de la partie " + titreRedacteur.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		writerPart.addString(textAreaAdr.getText(), adr.getText());   // adresse 
		    	}
		    	
		    	if (textFieldTelRedac.getText().equals("")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + tel.getText() + "\" de la partie " + titreRedacteur.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		writerPart.addString(textFieldTelRedac.getText(), tel.getText());   // telephone 
		    	}
		    	
		    	if (textFieldEmail.getText().equals("")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + email.getText() + "\" de la partie " + titreRedacteur.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		writerPart.addString(textFieldEmail.getText(), email.getText()); // email
		    	}
		    	
		    	if (textFieldNomCA.getText().equals("")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + nomCA.getText() + "\" de la partie " + titreRedacteur.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		writerPart.addString(textFieldNomCA.getText(),    nomCA.getText()); // nom du chage d'affaire
		    	}
		    	
		    	datas.add(writerPart);
		    	
		    	/*-----------------Partie client-----------------*/
		    	
		    	/*IDataHandler clientPart = new DefaultDataHandler(titreClient.getText());
		    	
		    	if (textFieldNomSite.getText().equals("")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + nomSite.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		clientPart.addString(textFieldNomSite.getText(), nomSite.getText());    // nom
		    	}
		    	
		    	if (textFieldCode.getText().equals("")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + code.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		clientPart.addString(textFieldCode.getText(), code.getText());       // code
		    	}
		    	
		    	if (textAreaAdrCl.getText().equals("")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + adrCl.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		clientPart.addString(textAreaAdrCl.getText(), adrCl.getText());      // adresse
		    	}
		    	
		    	if (textFieldCodePostal.getText().equals("")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + codePostal.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		clientPart.addString(textFieldCodePostal.getText(), codePostal.getText()); // code postal
		    	}
		    	
		    	if (textFieldVille.getText().equals("")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + ville.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		clientPart.addString(textFieldVille.getText(),      ville.getText());      // ville
		    	}
		    	
		    	if (textFieldTelCl.getText().equals("")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + telCl.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		clientPart.addString(textFieldTelCl.getText(),      telCl.getText());      // telephone client
		    	}
		    	
		    	if (textFieldEmailCl.getText().equals("")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + emailCl.getText() + "\" de la partie " + titreClient.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		clientPart.addString(textFieldEmailCl.getText(),    emailCl.getText());    // email client
		    	}
		    	
		    	datas.add(clientPart);
		    	
		    	/*-----------------Partie Rapport-----------------*/
		    	
		    	/*IDataHandler reportPart = new DefaultDataHandler(titreRapport.getText());
				
				reportPart.addString(comboBoxRapport.getSelectedItem().toString(), rapportActivite.getText()); // Type de rapport
				
		    	if (textFieldDateDebut.getText().equals("  /  /    ")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + dateDebut.getText() + "\" de la partie " + titreRapport.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		reportPart.addString(textFieldDateDebut.getText(), dateDebut.getText());
		    	}
		    	
		    	if (textFieldDateFin.getText().equals("  /  /    ")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + dateFin.getText() + "\" de la partie " + titreRapport.getText() + " doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		reportPart.addString(textFieldDateFin.getText(), dateFin.getText());
		    	}
		    	
		    	datas.add(reportPart);

		    	/*-----------------Partie Bons preventifs-----------------*/
		    	
		    	IDataHandler preventivesVouchers = new DefaultDataHandler(titreBP.getText());
		    	
		    	Iterator<JComboBox<String>> preventivesVouchersMonthsIter   = preventivesVouchersMonths.iterator();
		    	Iterator<JTextField>        nbPreventivesVouchersOpenedIter = nbPreventivesVouchersOpened.iterator();
		    	Iterator<JTextField>        nbPreventivesVouchersClosedIter = nbPreventivesVouchersClosed.iterator();
		    	Iterator<JTextArea>         commentsPreventivesVouchersIter = commentsPreventivesVouchers.iterator();
		    	
		    	int counter = 1;
		    	while (preventivesVouchersMonthsIter.hasNext()) {
		    		
		    		JComboBox<String> comboBoxMoisBP        = preventivesVouchersMonthsIter.next();
		    		JTextField        textFieldNbBPOuverts  = nbPreventivesVouchersOpenedIter.next();
		    		JTextField        textFieldNbBPFermes   = nbPreventivesVouchersClosedIter.next();
		    		JTextArea         textAreaCommentaireBP = commentsPreventivesVouchersIter.next();

					preventivesVouchers.addString(comboBoxMoisBP.getSelectedItem().toString(), preventiveVoucherMonthLabel);
					
			    	if (textFieldNbBPOuverts.getText().equals("")) {
			    		JOptionPane.showMessageDialog(mainFrame, 
			    				"le champs \"" +  nbPreventiveVoucherOpenedLabel + "\" de la partie " + titreBP.getText() + 
			    				" du mois numéro " + counter + " doit être remplis avec un nombre", "Erreur", 
								JOptionPane.WARNING_MESSAGE);
			    		return;
			    	}
			    	else if (!OperationUtilities.isNumeric(textFieldNbBPOuverts.getText())) {
			    		JOptionPane.showMessageDialog(mainFrame, 
			    				"le champs \"" + nbPreventiveVoucherOpenedLabel + "\" de la partie " + titreBP.getText() + 
			    				" du mois numéro " + counter + " doit être un nombre", "Erreur", 
								JOptionPane.WARNING_MESSAGE);
			    		return;
			    	}
			    	else {
						preventivesVouchers.addString(textFieldNbBPOuverts.getText(), nbPreventiveVoucherOpenedLabel);
			    	}
			    	
			    	if (textFieldNbBPFermes.getText().equals("")) {
			    		JOptionPane.showMessageDialog(mainFrame, 
			    				"le champs \"" + nbPreventiveVoucherClosedLabel + "\" de la partie " + titreBP.getText() +
			    				" du mois numéro " + counter + " doit être remplis avec un nombre", "Erreur", 
								JOptionPane.WARNING_MESSAGE);
			    		return;
			    	}
			    	else if (!OperationUtilities.isNumeric(textFieldNbBPFermes.getText())) {
			    		JOptionPane.showMessageDialog(mainFrame, 
			    				"le champs \"" + nbPreventiveVoucherClosedLabel + "\" de la partie " + titreBP.getText() +
			    				" du mois numéro " + counter + " doit être remplis avec un nombre", "Erreur", 
								JOptionPane.WARNING_MESSAGE);
			    		return;
			    	}
			    	else {
			    		preventivesVouchers.addString(textFieldNbBPFermes.getText(), nbPreventiveVoucherClosedLabel);
			    	}
			    	
			    	if (!textAreaCommentaireBP.getText().equals("")) {
						preventivesVouchers.addString(textAreaCommentaireBP.getText(), preventiveVoucherCommentLabel);
					}
			    	
			    	++counter;
		    	}
		    	
		    	datas.add(preventivesVouchers);
		    
		    	/*-----------------Partie Bons preventifs par domaines-----------------*/
		    	
		    	IDataHandler domainPreventivesVouchers = new DefaultDataHandler(titreBPDomaine.getText());
		    	
		    	// On obtient l'iterator des domaines
		    	Iterator<JCheckBox> domainsIter = domainesBP.iterator();
		    	
		    	// On obtient l'iterator des pourcentages correspondant
		    	Iterator<JFormattedTextField> pourcentsIter = textFieldPourcentsBP.iterator();
		    	
		    	DefaultCategoryDataset barChartDatas = new DefaultCategoryDataset();
		    	DefaultPieDataset      pieChartDatas = new DefaultPieDataset();
		    	
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
			    			JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreBPDomaine.getText() + 
				    				" doit être remplis si la case a été cochée", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		return;
		    			}
		    			else {		    				
		    				domainPreventivesVouchers.addString(currentPourcent.getText(), currentDomain.getText() + " : ");
		    				
		    				double currentPourcentage = 0;
		    				
		    				String pourcentageNumber = currentPourcent.getText().substring(0, 5);
		    				
		    				if (!OperationUtilities.isNumeric(pourcentageNumber)) {
		    					JOptionPane.showMessageDialog(mainFrame, 
					    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreBPDomaine.getText() + 
					    				" n'est pas un nombre valide", "Erreur", 
										JOptionPane.WARNING_MESSAGE);
					    		return;
		    				}
		    				
		    				try {
		    					currentPourcentage = Double.parseDouble(pourcentageNumber);
		    				}
		    				catch (NumberFormatException e) {
		    					JOptionPane.showMessageDialog(mainFrame, 
					    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreBPDomaine.getText() + 
					    				" n'est pas un nombre valide", "Erreur", 
										JOptionPane.WARNING_MESSAGE);
					    		return;
		    				}
		    				
		    				barChartDatas.addValue(currentPourcentage, "Pourcentage d'avancement", currentDomain.getText());
		    				
		    				total += currentPourcentage;
		    				
		    				if (total > 100 && !continueAbove100) {
		    					int dialogResult = JOptionPane.showConfirmDialog (mainFrame, 
		    							"Les pourcentages obtenus dans la parties " + titreBPDomaine.getText() + " sont supérieurs"
		    							+ " à 100% (" + total + "%) à partir du champ \"" + currentDomain.getText() + "\".\n "
		    							+ "Voulez-vous continuer tout de même (les pourcentages seront recalculés de manière relative)?",
		    							"Erreur", JOptionPane.YES_NO_OPTION);
		    					if(dialogResult == JOptionPane.NO_OPTION){
		    						return;
		    					}
		    					else {
		    						continueAbove100 = true;
		    					}
		    				}
		    				pourcentages.put(currentDomain.getText(), new Double(currentPourcentage));
		    			}
		    		}
		    	}		
		    	
		    	if (!domainPreventivesVouchers.isEmpty()) {
		    		IChartGenerator chartGenerator = new DefaultChartGenerator();
		    		
		    		if (total < 100) {
		    			int dialogResult = JOptionPane.showConfirmDialog (mainFrame, 
    							"Les pourcentages obtenus dans la parties " + titreBPDomaine.getText() + " sont inférieurs"
    							+ " à 100% (" + total + "%).\n "
    							+ "Voulez-vous continuer tout de même (les pourcentages seront recalculés de manière relative)?", 
    							"Erreur", JOptionPane.YES_NO_OPTION);
    					if(dialogResult == JOptionPane.NO_OPTION){
    						return;
    					}
		    		}
		    		
		    		try {
						JFreeChart barChart = chartGenerator.generateBarChart(titreBPDomaine.getText(), 
								"Domaines techniques", "Pourcentages d'avancement", barChartDatas);
						
						domainPreventivesVouchers.addJFreeChart(barChart);
		    		} 
		    		catch (Exception e) {
		    			e.printStackTrace();
						JOptionPane.showMessageDialog(mainFrame, "Erreur lors de la création du graphe en bare dans la partie"
								+ titreBPDomaine.getText() + " : \n"
								+ e.getMessage(), "Erreur", 
								JOptionPane.WARNING_MESSAGE);
					}
		    		
		    		Iterator<Entry<String, Double>> mapIter = pourcentages.entrySet().iterator();
		    		
		    		while (mapIter.hasNext()) {
		    			Map.Entry<String, Double> currentEntry = mapIter.next();
		    			
		    			double currentRelativePourcentage = currentEntry.getValue() / total;
		    			pieChartDatas.setValue(currentEntry.getKey() + " : " + OperationUtilities.truncateDecimal(currentRelativePourcentage * 100, 2) + "%",
		    					currentRelativePourcentage);
		    		}
		    		
		    		try {
						JFreeChart pieChart = chartGenerator.generatePieChart(titreBPDomaine.getText(), pieChartDatas);
						
						domainPreventivesVouchers.addJFreeChart(pieChart);
		    		} 
		    		catch (Exception e) {
		    			e.printStackTrace();
						JOptionPane.showMessageDialog(mainFrame, "Erreur lors de la création du graphe en camembert dans la partie " +
								titreBPDomaine.getText() + ": \n"
								+ e.getMessage(), "Erreur", 
								JOptionPane.WARNING_MESSAGE);
					}
		    		
		    		datas.add(domainPreventivesVouchers);
		    	}
		
		    	/*-----------------Partie demande d'intervention-----------------*/
		    	
		    	/*IDataHandler interventionDemand = new DefaultDataHandler(titreDI.getText());
		    	
		    	interventionDemand.addString(comboBoxMoisDI.getSelectedItem().toString(), moisDI.getText());
		    	
		    	if (textFieldNbIntervention.getText().equals("")) {
		    		JOptionPane.showMessageDialog(mainFrame, 
		    				"le champs \"" + nbIntervention.getText() + "\" de la partie " + titreDI.getText() + 
		    				" doit être remplis", "Erreur", 
							JOptionPane.WARNING_MESSAGE);
		    		return;
		    	}
		    	else {
		    		interventionDemand.addString(textFieldNbIntervention.getText(), nbIntervention.getText());
		    	}
		    	
		    	if (!textAreaCommentaireDI.getText().equals("")) {
		    		interventionDemand.addString(textAreaCommentaireDI.getText(), commentaireDI.getText());
		    	}
		    	
		    	datas.add(interventionDemand);
		    	
		    	/*-----------------Partie demande d'intervention par etat-----------------*/
		    	
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
		    				JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + currentState.getText() + "\" de la partie " + titreDIEtat.getText() + 
				    				" doit être remplis avec un nombre si la case est cochée", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		return;
		    			}
		    			else if (!OperationUtilities.isNumeric(currentNbState.getText())) {
		    				JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + currentState.getText() + "\" de la partie " + titreDIEtat.getText() + 
				    				" doit être un nombre si la case est cochée", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		return;
		    			}
		    			else {
		    				stateInterventionDemand.addString(currentNbState.getText(), currentState.getText() + " : ");
		    				
		    				Integer currentNumber = 0;
		    				
		    				String parsedNumber = currentNbState.getText();
		    				
		    				if (!OperationUtilities.isNumeric(parsedNumber)) {
		    					JOptionPane.showMessageDialog(mainFrame, 
					    				"le champs \"" + currentState.getText() + "\" de la partie " + titreDIEtat.getText() + 
					    				" n'est pas un nombre valide", "Erreur", 
										JOptionPane.WARNING_MESSAGE);
					    		return;
		    				}
		    				
		    				try {
		    					currentNumber = Integer.parseInt(parsedNumber);
		    				}
		    				catch (NumberFormatException e) {
		    					JOptionPane.showMessageDialog(mainFrame, 
					    				"le champs \"" + currentState.getText() + "\" de la partie " + titreDIEtat.getText() + 
					    				" n'est pas un nombre valide", "Erreur", 
										JOptionPane.WARNING_MESSAGE);
					    		return;
		    				}
		    				
		    				total += currentNumber;
		    				pourcentages.put(currentState.getText(), new Double(currentNumber));
		    			}
		    		}
		    	}
		    	
		    	if (!stateInterventionDemand.isEmpty()) {
		    		
		    		IChartGenerator chartGenerator = new DefaultChartGenerator();
		    		
		    		Iterator<Entry<String, Double>> mapIter = pourcentages.entrySet().iterator();
		    		
		    		while (mapIter.hasNext()) {
		    			Map.Entry<String, Double> currentEntry = mapIter.next();
		    			
		    			double currentRelativePourcentage = currentEntry.getValue() / total;
		    			pieChartDatas.setValue(currentEntry.getKey() + " : " + 
		    					OperationUtilities.truncateDecimal(currentRelativePourcentage * 100, 2) + "%",
		    					currentRelativePourcentage);
		    		}
		    		
		    		try {
						JFreeChart pieChart = chartGenerator.generatePieChart(titreBPDomaine.getText(), pieChartDatas);
						
						stateInterventionDemand.addJFreeChart(pieChart);
		    		} 
		    		catch (Exception e) {
		    			e.printStackTrace();
						JOptionPane.showMessageDialog(mainFrame, "Erreur lors de la création du graphe en camembert dans la partie "
								+ titreDIEtat.getText() + " : \n"
								+ e.getMessage(), "Erreur", 
								JOptionPane.WARNING_MESSAGE);
					}
		    		
		    		datas.add(stateInterventionDemand);
		    	}
		    	
		    	/*-----------------Partie demande d'intervention par domaine-----------------*/
		    	
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
			    			JOptionPane.showMessageDialog(mainFrame, 
				    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreDIDomaine.getText() + 
				    				" doit être remplis si la case a été cochée", "Erreur", 
									JOptionPane.WARNING_MESSAGE);
				    		return;
		    			}
		    			else {		    				
		    				domainInterventionDemand.addString(currentPourcent.getText(), currentDomain.getText() + " : ");
		    				
		    				double currentPourcentage = 0;
		    				
		    				String pourcentageNumber = currentPourcent.getText().substring(0, 5);
		    				
		    				if (!OperationUtilities.isNumeric(pourcentageNumber)) {
		    					JOptionPane.showMessageDialog(mainFrame, 
					    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreDIDomaine.getText() + 
					    				" n'est pas un nombre valide", "Erreur", 
										JOptionPane.WARNING_MESSAGE);
					    		return;
		    				}
		    				
		    				try {
		    					currentPourcentage = Double.parseDouble(pourcentageNumber);
		    				}
		    				catch (NumberFormatException e) {
		    					JOptionPane.showMessageDialog(mainFrame, 
					    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreDIDomaine.getText() + 
					    				" n'est pas un nombre valide", "Erreur", 
										JOptionPane.WARNING_MESSAGE);
					    		return;
		    				}
		    				
		    				total += currentPourcentage;
		    				
		    				if (total > 100 && !continueAbove100) {
		    					int dialogResult = JOptionPane.showConfirmDialog (mainFrame, 
		    							"Les pourcentages obtenus dans la parties " + titreDIDomaine.getText() + " sont supérieurs"
		    							+ " à 100% (" + total + "%) à partir du champ \"" + currentDomain.getText() + "\".\n "
		    							+ "Voulez-vous continuer tout de même (les pourcentages seront recalculés de manière relative)?",
		    							"Erreur", JOptionPane.YES_NO_OPTION);
		    					if(dialogResult == JOptionPane.NO_OPTION){
		    						return;
		    					}
		    					else {
		    						continueAbove100 = true;
		    					}
		    				}
		    				pourcentages.put(currentDomain.getText(), new Double(currentPourcentage));
		    			}
		    		}
		    	}		
		    	
		    	if (!domainInterventionDemand.isEmpty()) {
		    		IChartGenerator chartGenerator = new DefaultChartGenerator();
		    		
		    		if (total < 100) {
		    			int dialogResult = JOptionPane.showConfirmDialog (mainFrame, 
    							"Les pourcentages obtenus dans la parties " + titreDIDomaine.getText() + " sont inférieurs"
    							+ " à 100% (" + total + "%).\n "
    							+ "Voulez-vous continuer tout de même (les pourcentages seront recalculés de manière relative)?", 
    							"Erreur", JOptionPane.YES_NO_OPTION);
    					if(dialogResult == JOptionPane.NO_OPTION){
    						return;
    					}
		    		}
		    		
		    		Iterator<Entry<String, Double>> mapIter = pourcentages.entrySet().iterator();
		    		
		    		while (mapIter.hasNext()) {
		    			Map.Entry<String, Double> currentEntry = mapIter.next();
		    			
		    			double currentRelativePourcentage = currentEntry.getValue() / total;
		    			interventionPieChartDatas.setValue(currentEntry.getKey() + " : " + OperationUtilities.truncateDecimal(currentRelativePourcentage * 100, 2) + "%",
		    					currentRelativePourcentage);
		    		}
		    		
		    		try {
						JFreeChart pieChart = chartGenerator.generatePieChart(titreDIDomaine.getText(), interventionPieChartDatas);
						
						domainInterventionDemand.addJFreeChart(pieChart);
		    		} 
		    		catch (Exception e) {
		    			e.printStackTrace();
						JOptionPane.showMessageDialog(mainFrame, "Erreur lors de la création du graphe en camembert dans la partie " +
								titreDIDomaine.getText() + ": \n"
								+ e.getMessage(), "Erreur", 
								JOptionPane.WARNING_MESSAGE);
					}
		    		
		    		datas.add(domainInterventionDemand);
		    	}
		    	
				try {
					if (!datas.isEmpty()) {
						// Finallement on creer le document
						CreateReportDocument.createPdf(datas);
						JOptionPane.showMessageDialog(mainFrame, "Rapport généré", "Rapport généré", 
								JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(mainFrame, 
			    				"Aucune donnée à rédiger dans le rapport", "Erreur", 
								JOptionPane.WARNING_MESSAGE);
					}
				} 
				catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Erreur", 
							JOptionPane.WARNING_MESSAGE);
				}
		    	
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

	private void createPreventiveVoucherMonth (GridBagConstraints constraint, JComponent mainContainer, final String[] monthsList,
			Collection<JComboBox<String>> preventivesVouchersMonths, Collection<JTextField> nbPreventivesVouchersOpened,
			Collection<JTextField> nbPreventivesVouchersClosed, Collection<JTextArea> commentsPreventivesVouchers) {
	    
		if (preventiveVoucherLastMonthPosition >= numberPreventiveMonthAllowed) {
			JOptionPane.showMessageDialog(mainContainer, 
    				"Impossible d'ajouter un mois supplémentaire dans la partie " + preventiveVoucherMonthLabel, "Erreur", 
					JOptionPane.WARNING_MESSAGE);
		}
		
		JLabel preventivVoucherMonthJLabel = new JLabel (preventiveVoucherMonthLabel);
		
		GridBagConstraints localConstraint = (GridBagConstraints) constraint.clone();
		
		localConstraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		
		localConstraint.gridx = 0;
		localConstraint.gridy = ++preventiveVoucherLastMonthPosition;
		mainContainer.add(preventivVoucherMonthJLabel, localConstraint); //ajout du label moisBP
			
		JComboBox<String> comboBoxMoisBP = new JComboBox<String>(monthsList); //initialisation de la comboBox comboBoxMoisBP avec la liste choixMois
		comboBoxMoisBP.setPreferredSize(new Dimension(100, 20)); //dimension de la comboBoxMoisBP
		preventivesVouchersMonths.add(comboBoxMoisBP);
		
		localConstraint.gridx = 1;
		localConstraint.gridwidth = GridBagConstraints.REMAINDER;
		preventivVoucherMonthJLabel.setLabelFor(comboBoxMoisBP); //attribution de la comboBox comboBoxMoisBP au label moisBP
		mainContainer.add(comboBoxMoisBP, localConstraint); //ajout de la zone de texte comboBox comboBoxMoisBP
		
	    //nombre BP ouverts
	    JLabel nbBPOuverts = new JLabel(nbPreventiveVoucherOpenedLabel); //creation du label nbBPOuverts
	    localConstraint.gridx = 0;
	    localConstraint.gridy = ++preventiveVoucherLastMonthPosition;
	    localConstraint.gridwidth = 1;
	    mainContainer.add(nbBPOuverts, localConstraint); //ajout du label nbBPOuverts
	    
	    JTextField textFieldNbBPOuverts = new JTextField(2); //creation de la zone de texte textFieldNbBPOuverts
	    nbBPOuverts.setLabelFor(textFieldNbBPOuverts); //attribution de la zone de texte au label nbBPOuverts
	    nbPreventivesVouchersOpened.add(textFieldNbBPOuverts);
		
	    localConstraint.gridx = 1;
	    localConstraint.gridwidth = GridBagConstraints.REMAINDER;
		mainContainer.add(textFieldNbBPOuverts, localConstraint); //ajout de la zone de texte textFieldNbBPOuverts
		
		//nombre BP fermes
	    JLabel nbBPFermes = new JLabel(nbPreventiveVoucherClosedLabel); //creation du label nbBPFermes
		
	    localConstraint.gridx = 0;
	    localConstraint.gridy = ++preventiveVoucherLastMonthPosition;
	    localConstraint.gridwidth = 1;
	    
		mainContainer.add(nbBPFermes, localConstraint); //ajout du label nbBPFermes
	    JTextField textFieldNbBPFermes = new JTextField(2); //creation de la zone de texte textFieldNbBPFermes
	    nbBPFermes.setLabelFor(textFieldNbBPFermes); //attribution de la zone de texte textFieldNbBPFermes au label nbBPFermes
	    nbPreventivesVouchersClosed.add(textFieldNbBPFermes);
		
	    localConstraint.gridx = 1;
	    localConstraint.gridwidth = GridBagConstraints.REMAINDER;
		mainContainer.add(textFieldNbBPFermes, localConstraint); //ajout de la zone de texte textFieldNbBPFermes
		
		//commentaire BP
	    JLabel commentaireBP = new JLabel(preventiveVoucherCommentLabel); //creation du label commentaireBP
		
	    constraint.gridx = 0;
		constraint.gridy = ++preventiveVoucherLastMonthPosition;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    
		JTextArea textAreaCommentaireBP = new JTextArea(4, 15); //creation de la zone de texte textAreaCommentaireBP
	    JScrollPane scrollPaneComBP = new JScrollPane(textAreaCommentaireBP); //creation de la scrollPane scrollPaneComBP contenant textAreaCommentaireBP
	    commentaireBP.setLabelFor(textAreaCommentaireBP); //attribution de la zone de texte textAreaCommentaireBP au label commentaireBP
	    commentsPreventivesVouchers.add(textAreaCommentaireBP);
	    
		constraint.gridy = ++preventiveVoucherLastMonthPosition;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		mainContainer.add(scrollPaneComBP, constraint); //ajout de la scrollPane scrollPaneComBP
	}

}