package IHM;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

public class Formulaire extends JFrame{
	
	private JFormattedTextField textFieldTel = new JFormattedTextField(); 		  //declaration du textField telephone initialise dans le try
	private JFormattedTextField textFieldCodePostal = new JFormattedTextField();  //declaration du textField code postal initialise dans le try
	private JFormattedTextField textFieldTelCl = new JFormattedTextField(); 	  //declaration du textField telephone client initialise dans le try
	private JFormattedTextField textFieldDateDebut = new JFormattedTextField();   //declaration du textField date debut initialise dans le try
	private JFormattedTextField textFieldDateFin = new JFormattedTextField();     //declaration du textField date fin initialise dans le try
	private JComboBox<String> comboBoxRapport = new JComboBox<String>();  		  //declaration de la liste des durée de rapport
	private JComboBox<String> comboBoxMois = new JComboBox<String>(); 			  //declaraton de la liste des mois
	private JFormattedTextField textFieldPourcent1 = new JFormattedTextField();   //declaration du textFieldPourcent1 date fin initialise dans le try
	private JFormattedTextField textFieldPourcent2 = new JFormattedTextField();   //declaration du textFieldPourcent2 date fin initialise dans le try
	private JFormattedTextField textFieldPourcent3 = new JFormattedTextField();   //declaration du textFieldPourcent3 date fin initialise dans le try
	private JFormattedTextField textFieldPourcent4 = new JFormattedTextField();   //declaration du textFieldPourcent4 date fin initialise dans le try
	private JFormattedTextField textFieldPourcent5 = new JFormattedTextField();   //declaration du textFieldPourcent5 date fin initialise dans le try
	private JFormattedTextField textFieldPourcent6 = new JFormattedTextField();   //declaration du textFieldPourcent6 date fin initialise dans le try
	private JFormattedTextField textFieldPourcent7 = new JFormattedTextField();   //declaration du textFieldPourcent7 date fin initialise dans le try
	private JFormattedTextField textFieldPourcent8 = new JFormattedTextField();   //declaration du textFieldPourcent8 date fin initialise dans le try
	private JFormattedTextField textFieldPourcent9 = new JFormattedTextField();   //declaration du textFieldPourcent9 date fin initialise dans le try
	private JFormattedTextField textFieldPourcent10 = new JFormattedTextField();  //declaration du textFieldPourcent10 date fin initialise dans le try
	private JFormattedTextField textFieldPourcent11 = new JFormattedTextField();  //declaration du textFieldPourcent11 date fin initialise dans le try
	
	private File f = new File ("rapport.txt"); //creation d'un rapport au format txt
	private PrintWriter fw = new PrintWriter (new BufferedWriter (new FileWriter (f)));
	
	
	public Formulaire() throws IOException{
		
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
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
	    
	    
		/*-----------------------------------------formulaire redacteur--------------------------------------------*/
	    
		JLabel titreRedacteur = new JLabel("Redacteur"); //titre redacteur
		titreRedacteur.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre redacteur
		c.gridx = 0;
		c.gridy = 0;
		
	    conteneurPrincipal.add(titreRedacteur, c); //ajout du titreRedacteur dans conteneurPrincipal
		
		//nom
		JLabel nom = new JLabel("Nom : "); //creation du label nom
		c.gridy = 1;
		c.gridwidth = 1;
		conteneurPrincipal.add(nom, c); //ajout du label
		JTextField textFieldNom = new JTextField(15); //creation de la zone de texte adr de taille 15
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		nom.setLabelFor(textFieldNom); //attribution de la zone de texte au label adr
		conteneurPrincipal.add(textFieldNom, c); //ajout de la zone de texte adr
		
		//adresse 
		JLabel adr = new JLabel("Adresse : "); //creation du label adr
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
	    conteneurPrincipal.add(adr, c); //ajout du label adr
	    JTextArea textAreaAdr = new JTextArea(3, 15); //creation de la zone de texte adr de taille 3 en hauteur et 15 en largeur
	    JScrollPane scrollPaneAdr = new JScrollPane(textAreaAdr);
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
	    adr.setLabelFor(textAreaAdr); //attribution de la zone de texte au label adr
	    conteneurPrincipal.add(scrollPaneAdr, c); //ajout de la zone de texte adr
	    
	    //telephone
	    JLabel tel = new JLabel("Téléphone : "); //creation du label tel
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
	    conteneurPrincipal.add(tel, c); //ajout du label tel
	    try{
			MaskFormatter maskTel  = new MaskFormatter("## ## ## ## ##"); //masque pour le format du numero de telephone
			textFieldTel = new JFormattedTextField(maskTel); //initialisation de la zone de texte tel formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    tel.setLabelFor(textFieldTel); //attribution de la zone de texte au label tel
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldTel, c); //ajout de la zone de texte tel
	   
	    //email
	    JLabel email = new JLabel("Email : "); //creation du label email
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
	    conteneurPrincipal.add(email, c); //ajout du label nom
	    JTextField textFieldEmail = new JTextField(15); //creation de la zone de texte email de taille 15
	    email.setLabelFor(textFieldEmail); //attribution de la zone de texte au label email
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(textFieldEmail, c); //ajout de la zone de texte email au panel redacteur 
	    
	    //nom charge d'affaire
	    JLabel nomCA = new JLabel("Nom du chargé d'affaire : "); //creation du label nomCA
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
	    conteneurPrincipal.add(nomCA, c); //ajout du label nomCA au panel redacteur
	    JTextField textFieldNomCA = new JTextField(15); //creation de la zone de texte nomCA de taille 15
	    nomCA.setLabelFor(textFieldNomCA); //attribution de la zone de texte au label nomCA
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(textFieldNomCA, c); //ajout de la zone de texte nomCA
	
        
		/*-------------------------------------------formulaire client-------------------------------------------------*/
		
		
		JLabel titreClient = new JLabel("Client"); //titre client
		titreClient.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre client
		c.gridx = 0;
		c.gridy = 7;
		conteneurPrincipal.add(titreClient, c); //ajout du titreClient dans le panel conteneurPrincpal
		
		//nom
		JLabel nomSite = new JLabel("Nom du site : "); //creation du label nomSite
		c.gridy = 8;
		c.gridwidth = 1;
		conteneurPrincipal.add(nomSite, c); //ajout du label nomSite
		JTextField textFieldNomSite = new JTextField(15); //creation de la zone de texte nomSite de taille 15
		nomSite.setLabelFor(textFieldNomSite); //attribution de la zone de texte au label nomSite
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNomSite, c); //ajout de la zone de texte nomSite
		
		//code
	    JLabel code = new JLabel("Code : "); //creation du label code
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 1;
	    conteneurPrincipal.add(code, c); //ajout du label code
	    JTextField textFieldCode = new JTextField(15); //création de la zone de texte code
	    code.setLabelFor(textFieldCode); //attribution de la zone de texte au label code
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldCode, c); //ajout de la zone de texte code
		
		//adresse client
		JLabel adrCl = new JLabel("Adresse : "); //creation du label adrCl
		c.gridx = 0;
		c.gridy = 10;
		c.gridwidth = 1;
	    conteneurPrincipal.add(adrCl, c); //ajout du label adrCl
	    JTextArea textAreaAdrCl = new JTextArea(3, 15); //creation de la zone de texte adrCl de taille 3 en hauteur et 15 en largeur
	    JScrollPane scrollPaneAdrCl = new JScrollPane(textAreaAdrCl);
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
	    adrCl.setLabelFor(textAreaAdrCl); //attribution de la zone de texte au label adrCl
	    conteneurPrincipal.add(scrollPaneAdrCl, c); //ajout de la zone de texte adrCl
	    
	    //code postal
	    JLabel codePostal = new JLabel("Code postal : "); //creation du label codePostal
		c.gridx = 0;
		c.gridy = 11;
		c.gridwidth = 1;
	    conteneurPrincipal.add(codePostal, c); //ajout du label codePostal
	    try{
			MaskFormatter maskCodePostal  = new MaskFormatter("## ###"); //masque pour le format du code postal
			textFieldCodePostal = new JFormattedTextField(maskCodePostal); //initialisation de la zone de texte codePostal formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    codePostal.setLabelFor(textFieldCodePostal); //attribution de la zone de texte au label codePostal
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldCodePostal, c); //ajout de la zone de texte codePostal
		
		//ville
		JLabel ville = new JLabel("Ville : "); //creation du label ville
		c.gridx = 0;
		c.gridy = 12;
		c.gridwidth = 1;
		conteneurPrincipal.add(ville, c); //ajout du label ville
		JTextField textFieldVille = new JTextField(15); //creation de la zone de texte ville de taille 15
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		ville.setLabelFor(textFieldVille); //attribution de la zone de texte au label ville
		conteneurPrincipal.add(textFieldVille, c); //ajout de la zone de texte ville
		
		//nom du client
	    JLabel nomClient = new JLabel("Nom du client : "); //creation du label nomCl
		c.gridx = 0;
		c.gridy = 13;
		c.gridwidth = 1;
	    conteneurPrincipal.add(nomClient, c); //ajout du label nomCl
	    JTextField textFieldNomClient = new JTextField(15); //creation de la zone de texte nomCl de taille 15
	    nomClient.setLabelFor(textFieldNomClient); //attribution de la zone de texte au label nomCl
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(textFieldNomClient, c); //ajout de la zone de texte nomCl 
	    
	    //telephone client
	    JLabel telCl = new JLabel("Téléphone : "); //creation du label telCl
		c.gridx = 0;
		c.gridy = 14;
		c.gridwidth = 1;
	    conteneurPrincipal.add(telCl, c); //ajout du label telCl
	    try{
			MaskFormatter maskTelCl  = new MaskFormatter("## ## ## ## ##"); //masque pour le format du numero de telephone
			textFieldTelCl = new JFormattedTextField(maskTelCl); //initialisation de la zone de texte telCl formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    telCl.setLabelFor(textFieldTelCl); //attribution de la zone de texte au label telCl
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldTelCl, c); //ajout de la zone de texte telCl
	   
	    //email client
	    JLabel emailCl = new JLabel("Email : "); //creation du label emailCl
		c.gridx = 0;
		c.gridy = 15;
		c.gridwidth = 1;
	    conteneurPrincipal.add(emailCl, c); //ajout du label emailCl
	    JTextField textFieldEmailCl = new JTextField(15); //creation de la zone de texte emailCl de taille 15
	    emailCl.setLabelFor(textFieldEmailCl); //attribution de la zone de texte au label emailCl
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(textFieldEmailCl, c); //ajout de la zone de texte emailCl
	    
		
		/*----------------------------------------------formulaire rapport----------------------------------------------------------*/
	    
		JLabel titreRapport = new JLabel("Rapport"); //titre de la parte rapport du formulaire
		titreRapport.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		c.gridx = 0;
		c.gridy = 17;
	    conteneurPrincipal.add(titreRapport, c); //ajout du titreRapportr dans conteneurPrincipal
		
		//rapport d'activite
	    JLabel rapportActivite = new JLabel("Rapport d'activté : ");
		c.gridy = 18;
		c.gridwidth = GridBagConstraints.RELATIVE;
		conteneurPrincipal.add(rapportActivite, c); //ajout du label 
		String[] choixRapport = {"Hebdomadaire", "Mensuel", "Bimensuel", "Trimestriel", "Semestriel", "Annuel"}; //dfferents choix de la duree du rapport d'activite
		comboBoxRapport = new JComboBox(choixRapport);
		comboBoxRapport.setPreferredSize(new Dimension(100, 20));
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		rapportActivite.setLabelFor(comboBoxRapport); //attribution de la zone de texte au label adr
		conteneurPrincipal.add(comboBoxRapport, c); //ajout de la zone de texte adr
	    
	    //date debut
	    JLabel dateDebut = new JLabel("Date de début : "); //creation du label dateDebut
		c.gridx = 0;
		c.gridy = 19;
		c.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(dateDebut, c); //ajout du label dateDebut
	    try{
			MaskFormatter maskDate  = new MaskFormatter("##/##/####"); //masque pour le format date
			textFieldDateDebut = new JFormattedTextField(maskDate); //initialisation de la zone de texte dateDebut formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    dateDebut.setLabelFor(textFieldDateDebut); //attribution de la zone de texte au label dateDebut
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldDateDebut, c); //ajout de la zone de texte dateDebut
		
		//date fin
	    JLabel dateFin = new JLabel("Date de fin : "); //creation du label dateDebut
		c.gridx = 0;
		c.gridy = 20;
		c.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(dateFin, c); //ajout du label dateDebut
	    try{
			MaskFormatter maskDate  = new MaskFormatter("##/##/####"); //masque pour le format date
			textFieldDateFin = new JFormattedTextField(maskDate); //initialisation de la zone de texte dateFin formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    dateFin.setLabelFor(textFieldDateFin); //attribution de la zone de texte au label dateFin
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldDateFin, c); //ajout de la zone de texte dateFin
		
		
		/*----------------------------------------------formulaire bons preventifs----------------------------------------------------------*/
	    
		JLabel titreBP = new JLabel("Bons préventifs"); //titre de la parte rapport du formulaire
		titreBP.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		c.gridx = 0;
		c.gridy = 21;
	    conteneurPrincipal.add(titreBP, c); //ajout du titreRapportr dans conteneurPrincipal
		
		//mois
	    JLabel mois = new JLabel("Mois : ");
		c.gridy = 22;
		conteneurPrincipal.add(mois, c); //ajout du label 
		String[] choixMois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"}; //dfferents choix de la duree du rapport d'activite
		comboBoxMois = new JComboBox(choixMois);
		comboBoxMois.setPreferredSize(new Dimension(100, 20));
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		mois.setLabelFor(comboBoxMois); //attribution de la zone de texte au label adr
		conteneurPrincipal.add(comboBoxMois, c); //ajout de la zone de texte adr
		
	    //nombre BP ouverts
	    JLabel nbBPOuverts = new JLabel("Nombre de bons préventifs ouverts : "); //creation du label dateDebut
		c.gridx = 1;
		c.gridy = 23;
		c.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(nbBPOuverts, c); //ajout du label nbBPOuverts
	    JTextField textFieldNbBPOuverts = new JTextField(2); //initialisation de la zone de texte textFieldNbBPOuverts
	    nbBPOuverts.setLabelFor(textFieldNbBPOuverts); //attribution de la zone de texte au label nbBPOuverts
		c.gridx = 2;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNbBPOuverts, c); //ajout de la zone de texte textFieldNbBPOuverts
		
		//nombre BP fermes
	    JLabel nbBPFermes = new JLabel("Nombre de bons préventifs fermés : "); //creation du label dateDebut
		c.gridx = 1;
		c.gridy = 24;
		c.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(nbBPFermes, c); //ajout du label dateDebut
	    JTextField textFieldNbBPFermes = new JTextField(2); //initialisation de la zone de texte dateFin formattee par le masque
	    nbBPFermes.setLabelFor(textFieldNbBPFermes); //attribution de la zone de texte au label dateFin
		c.gridx = 2;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNbBPFermes, c); //ajout de la zone de texte dateFin
		
		//commentaire BP
	    JLabel commentaireBP = new JLabel("Commentaire : "); //creation du label emailCl
		c.gridx = 0;
		c.gridy = 25;
	    conteneurPrincipal.add(commentaireBP, c); //ajout du label emailCl
	    JTextArea textAreaCommentaireBP = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneComBP = new JScrollPane(textAreaCommentaireBP);
	    commentaireBP.setLabelFor(textAreaCommentaireBP); //attribution de la zone de texte au label emailCl
		c.gridy = 26;
		c.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(scrollPaneComBP, c); //ajout de la zone de texte emailCl
	    
	    
	    /*----------------------------------------formulaire bons preventifs par domaine------------------------------------------------*/
	    
	    JLabel titreBPDomaine = new JLabel("Bons préventifs par domaines"); //titre de la partie Bons preventifs par domaine du formulaire
	    titreBPDomaine.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		c.gridx = 0;
		c.gridy = 27;
		c.gridwidth = 1;
	    conteneurPrincipal.add(titreBPDomaine, c); //ajout du titreBPDomaine dans conteneurPrincipal
	    
	    JCheckBox domaine1 = new JCheckBox("Clos et ouvert");
	    c.gridx = 0;
		c.gridy = 28;
		c.gridwidth = 1;
	    conteneurPrincipal.add(domaine1, c); //ajout de la checkbox domaine1
	    try{
			MaskFormatter maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format pourcentage
			textFieldPourcent1 = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte Pourcent1 formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldPourcent1, c); //ajout de la zone de texte Pourcent1
	    
		JCheckBox domaine2 = new JCheckBox("Aménagement intérieur");
		c.gridx = 0;
		c.gridy = 29;
		c.gridwidth = 1;
	    conteneurPrincipal.add(domaine2, c); //ajout de la zone de texte emailCl
	    try{
			MaskFormatter maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format date
			textFieldPourcent2 = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte dateFin formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldPourcent2, c); //ajout de la zone de texte emailCl
		
		JCheckBox domaine3 = new JCheckBox("Ascenseur, monte-charge");
		c.gridx = 0;
		c.gridy = 30;
		c.gridwidth = 1;
	    conteneurPrincipal.add(domaine3, c); //ajout de la zone de texte emailCl
	    try{
			MaskFormatter maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format date
			textFieldPourcent3 = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte dateFin formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldPourcent3, c); //ajout de la zone de texte emailCl
		
		JCheckBox domaine4 = new JCheckBox("CVC");
		c.gridx = 0;
		c.gridy = 31;
		c.gridwidth = 1;
	    conteneurPrincipal.add(domaine4, c); //ajout de la zone de texte emailCl
	    try{
			MaskFormatter maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format date
			textFieldPourcent4 = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte dateFin formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldPourcent4, c); //ajout de la zone de texte emailCl
		
		JCheckBox domaine5 = new JCheckBox("Plomberie sanitaire");
		c.gridx = 0;
		c.gridy = 32;
		c.gridwidth = 1;
	    conteneurPrincipal.add(domaine5, c); //ajout de la zone de texte emailCl
	    try{
			MaskFormatter maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format date
			textFieldPourcent5 = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte dateFin formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldPourcent5, c); //ajout de la zone de texte emailCl
		
		JCheckBox domaine6 = new JCheckBox("Eléctricité CFO");
		c.gridx = 0;
		c.gridy = 33;
		c.gridwidth = 1;
	    conteneurPrincipal.add(domaine6, c); //ajout de la zone de texte emailCl
	    try{
			MaskFormatter maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format date
			textFieldPourcent6 = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte dateFin formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldPourcent6, c); //ajout de la zone de texte emailCl
		
		JCheckBox domaine7 = new JCheckBox("Sûreté");
		c.gridx = 0;
		c.gridy = 34;
		c.gridwidth = 1;
	    conteneurPrincipal.add(domaine7, c); //ajout de la zone de texte emailCl
	    try{
			MaskFormatter maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format date
			textFieldPourcent7 = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte dateFin formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldPourcent7, c); //ajout de la zone de texte emailCl
		
		JCheckBox domaine8 = new JCheckBox("Sécurité détection incendie");
		c.gridx = 0;
		c.gridy = 35;
		c.gridwidth = 1;
	    conteneurPrincipal.add(domaine8, c); //ajout de la zone de texte emailCl
	    try{
			MaskFormatter maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format date
			textFieldPourcent8 = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte dateFin formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldPourcent8, c); //ajout de la zone de texte emailCl
		
		JCheckBox domaine9 = new JCheckBox("Aménagements extéreurs");
		c.gridx = 0;
		c.gridy = 36;
		c.gridwidth = 1;
	    conteneurPrincipal.add(domaine9, c); //ajout de la zone de texte emailCl
	    try{
			MaskFormatter maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format date
			textFieldPourcent9 = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte dateFin formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldPourcent9, c); //ajout de la zone de texte emailCl
		
		JCheckBox domaine10 = new JCheckBox("Centrale énergie");
		c.gridx = 0;
		c.gridy = 37;
		c.gridwidth = 1;
	    conteneurPrincipal.add(domaine10, c); //ajout de la zone de texte emailCl
	    try{
			MaskFormatter maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format date
			textFieldPourcent10 = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte dateFin formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldPourcent10, c); //ajout de la zone de texte emailCl
		
		JCheckBox domaine11 = new JCheckBox("Cont, réglementaire");
		c.gridx = 0;
		c.gridy = 38;
		c.gridwidth = 1;
	    conteneurPrincipal.add(domaine11, c); //ajout de la zone de texte emailCl
	    try{
			MaskFormatter maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format date
			textFieldPourcent11 = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte dateFin formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldPourcent11, c); //ajout de la zone de texte emailCl
		
		//commentaire BP par domaine
	    JLabel commentaireBPDomaine = new JLabel("Commentaire : "); //creation du label emailCl
		c.gridx = 0;
		c.gridy = 39;
		c.gridwidth = 1;
	    conteneurPrincipal.add(commentaireBPDomaine, c); //ajout du label emailCl
	    JTextArea textAreaCommentaireBPDomaine = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneComBPDomaine = new JScrollPane(textAreaCommentaireBPDomaine);
	    //commentaireBPDomaine.setLabelFor(textAreaCommentaireBPDomaine); //attribution de la zone de texte au label emailCl
		c.gridy = 40;
		c.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(scrollPaneComBPDomaine, c); //ajout de la zone de texte emailCl
	    
	    /*----------------------------------------------formulaire arborescence libre----------------------------------------------------------*/
	    /*
		JLabel titreArboLibre = new JLabel("Arborescence libre"); //titre de la parte rapport du formulaire
		titreArboLibre.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		c.gridx = 0;
		c.gridy = 42;
	    conteneurPrincipal.add(titreArboLibre, c); //ajout du titreRapportr dans conteneurPrincipal
		
		//titre
	    JLabel titre = new JLabel("Titre : ");
		c.gridy = 43;
		conteneurPrincipal.add(titre, c); //ajout du label 
		JTextField textFieldTitre = new JTextField(15); //creation de la zone de texte nomSite de taille 15
		titre.setLabelFor(textFieldTitre); //attribution de la zone de texte au label nomSite
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldTitre, c); //ajout de la zone de texte nomSite
		
	    //element
	    JLabel element = new JLabel("Elément : "); //creation du label dateDebut
		c.gridx = 1;
		c.gridy = 44;
		c.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(element, c); //ajout du label nbBPOuverts
	    JTextField textFieldElement = new JTextField(15); //initialisation de la zone de texte textFieldNbBPOuverts
	    element.setLabelFor(textFieldElement); //attribution de la zone de texte au label nbBPOuverts
		c.gridx = 2;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldElement, c); //ajout de la zone de texte textFieldNbBPOuverts
		
		//nombre
	    JLabel nombre = new JLabel("Nombre de bons préventifs fermés : "); //creation du label dateDebut
		c.gridx = 3;
		c.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(nombre, c); //ajout du label dateDebut
	    JTextField textFieldNombre = new JTextField(2); //initialisation de la zone de texte dateFin formattee par le masque
	    nombre.setLabelFor(textFieldNombre); //attribution de la zone de texte au label dateFin
		c.gridx = 4;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNombre, c); //ajout de la zone de texte dateFin
		
		//commentaire
	    JLabel commentaire = new JLabel("Commentaire : "); //creation du label emailCl
		c.gridx = 0;
		c.gridy = 25;
	    conteneurPrincipal.add(commentaire, c); //ajout du label emailCl
	    JTextArea textAreaCommentaire = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneCom = new JScrollPane(textAreaCommentaire);
	    commentaire.setLabelFor(textAreaCommentaire); //attribution de la zone de texte au label emailCl
		c.gridy = 26;
		c.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(scrollPaneCom, c); //ajout de la zone de texte emailCl
		*/
		/*-----------------------------------------Bouton de validation du formulaire--------------------------------------------------- */
		
		JButton valideForm = new JButton("Valider"); //bouton de validation du formulaire 
	    c.gridy = 41;
	    c.gridwidth = 1;
	    conteneurPrincipal.add(valideForm, c); //ajout du bouton de validation de la partie client
	    
		//action declenchee par le bouton de validation du formulaire
	    valideForm.addActionListener(new ActionListener() {
	    	
		    public void actionPerformed(ActionEvent arg0) {	
		    	/*---affichage console---*/
		    	//partie rapport
		    	System.out.println(titreRedacteur.getText()); 					//affichage console du titre de la partie du formulare 	
		    	System.out.println(nom.getText() + textFieldNom.getText()); 	//affichage console des données nom
		    	System.out.println(adr.getText() + textAreaAdr.getText()); 		//affichage console des données adr
		    	System.out.println(tel.getText() + textFieldTel.getText()); 	//affichage console des données tel
		    	System.out.println(email.getText() + textFieldEmail.getText()); //affichage console des données email
		    	System.out.println(nomCA.getText() + textFieldNomCA.getText()); //affichage console des données nomCA
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
		    	System.out.println(titreBP.getText()); 											//affichage console du titre de la partie du formulaire
		    	System.out.println(mois.getText() + comboBoxMois.getSelectedItem().toString()); //affichage console des données mois
		    	System.out.println(nbBPOuverts.getText() + textFieldNbBPOuverts.getText()); 	//affichage console des données nbBPOuverts
		    	System.out.println(nbBPFermes.getText() + textFieldNbBPFermes.getText()); 		//affichage console des données nbBPFermes
		    	System.out.println(commentaireBP.getText() + textAreaCommentaireBP.getText()); 	//affichage console des données commentaireBP
		    	
		    	/*---ecriture dans le fichier texte rapport---*/
		    	//partie redacteur
		    	fw.println (titreRedacteur.getText()); 					 	//ecriture du titre de la partie du formulare 	
		    	fw.println (nom.getText() + textFieldNom.getText()); 	 	//ecriture des donnees nom
		    	fw.println (adr.getText() + textAreaAdr.getText()); 	 	//ecriture des donnees adr
		    	fw.println (tel.getText() + textFieldTel.getText()); 	 	//ecriture des donnees tel
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
		    	fw.println (rapportActivite.getText() + comboBoxRapport.getSelectedItem().toString()); 	//ecriture console des données nomSite
		    	fw.println (dateDebut.getText() + textFieldDateDebut.getText()); 						//ecriture console des données code
		    	fw.println (dateFin.getText() + textFieldDateFin.getText()); 							//ecriture console des données adr
		    	//partie bons preventifs
			    fw.println (titreBP.getText()); 											//ecriture du titre de la partie du formulaire
		    	fw.println (mois.getText() + comboBoxMois.getSelectedItem().toString()); 	//ecriture des données mois
		    	fw.println (nbBPOuverts.getText() + textFieldNbBPOuverts.getText()); 		//ecriture des données nbBPOuverts
		    	fw.println (nbBPFermes.getText() + textFieldNbBPFermes.getText()); 			//ecriture des données nbBPFermes
		    	fw.println (commentaireBP.getText() + textAreaCommentaireBP.getText()); 	//ecriture des données commentaireBP
		    	
		    	fw.println ("\r\n"); //retour ligne
		    	fw.close();
		    	
		    }
		});
	    
        
        /*--------------------------------------------------ajout des éléments---------------------------------------------*/
	    
       
	    
	    
	    conteneur.add(conteneurPrincipal, BorderLayout.CENTER); //ajout du conteneur principal contenant tous les champs formulaires au centre du conteuneur
	    
	    JScrollPane scrollPanePrincipale = new JScrollPane(conteneur); //scrollPane principale sur le conteneur
	    
	    fenetre.add(titreFacilitiesRapport, BorderLayout.NORTH); //ajout du titre en haut de la fenetre 
	    fenetre.add(scrollPanePrincipale, BorderLayout.CENTER); //ajout de la scrollPane principale (et du conteneur) au centre de la fenetre
	    
	    this.setContentPane(fenetre); 
	    this.setVisible(true);  //visibilite
	    
	 }	

}

