package IHM;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
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
	
	private JFormattedTextField textFieldTelRedac = new JFormattedTextField(); 	  //declaration du textField telephone initialise dans le try
	private JFormattedTextField textFieldCodePostal = new JFormattedTextField();  //declaration du textField code postal initialise dans le try
	private JFormattedTextField textFieldTelCl = new JFormattedTextField(); 	  //declaration du textField telephone client initialise dans le try
	private JFormattedTextField textFieldDateDebut = new JFormattedTextField();   //declaration du textField date debut initialise dans le try
	private JFormattedTextField textFieldDateFin = new JFormattedTextField();     //declaration du textField date fin initialise dans le try
	private JComboBox<String> comboBoxRapport = new JComboBox<String>();  		  //declaration de la comboBox des durée de rapport
	private JComboBox<String> comboBoxMoisBP = new JComboBox<String>(); 		  //declaraton de la comboBox des mois pour les BP
	private JFormattedTextField textFieldPourcent = new JFormattedTextField();   //declaration du textFieldPourcent1 date fin initialise dans le try
	private JComboBox<String> comboBoxMoisDI = new JComboBox<String>(); 		  //declaraton de la comboBox des mois pour les DI
	private JComboBox<String> comboBoxTypeCompteur = new JComboBox<String>(); 	  //declaraton de la comboBox des types de compteur
	private JComboBox<String> comboBoxMoisCompteur = new JComboBox<String>(); 	  //declaraton de la comboBox des mois pour les compteur
	private JComboBox<String> comboBoxUnite = new JComboBox<String>(); 	  		  //declaraton de la comboBox des unites
	private JCheckBox domaine = new JCheckBox();
	private JCheckBox etat = new JCheckBox();
	
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
	    
		JLabel titreRedacteur = new JLabel("Redacteur"); //titre de la partie redacteur du formulaire
		titreRedacteur.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreRedacteur
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreRedacteur, c); //ajout du titreRedacteur dans conteneurPrincipal
		
	    c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
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
			textFieldTelRedac = new JFormattedTextField(maskTel); //initialisation de la zone de texte tel formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    tel.setLabelFor(textFieldTelRedac); //attribution de la zone de texte au label tel
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldTelRedac, c); //ajout de la zone de texte tel
	   
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
		
		
		JLabel titreClient = new JLabel("Client"); //titre de la partie client du formulaire
		titreClient.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreClient
		c.gridx = 0;
		c.gridy = 7;
		c.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
		conteneurPrincipal.add(titreClient, c); //ajout du titreClient dans le panel conteneurPrincpal
		
		c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
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
	    
		JLabel titreRapport = new JLabel("Rapport"); //titre de la partie rapport du formulaire
		titreRapport.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreRapport
		c.gridx = 0;
		c.gridy = 17;
		c.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreRapport, c); //ajout du titreRapport dans conteneurPrincipal
		
	    c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		//rapport d'activite
	    JLabel rapportActivite = new JLabel("Rapport d'activté : "); ////creation du label rapportActivite
		c.gridy = 18;
		c.gridwidth = GridBagConstraints.RELATIVE;
		conteneurPrincipal.add(rapportActivite, c); //ajout du label rapportActivite
		String[] choixRapport = {"Hebdomadaire", "Mensuel", "Bimensuel", "Trimestriel", "Semestriel", "Annuel"}; //liste des differents choix de la duree du rapport d'activite
		comboBoxRapport = new JComboBox(choixRapport); //initialisation du comboBox comboBoxRapport avec la liste choixRapport
		comboBoxRapport.setPreferredSize(new Dimension(100, 20)); //dimension de la comboBoxRapport
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		rapportActivite.setLabelFor(comboBoxRapport); //attribution de la comboBox comboBoxRapport au label rapportActivite
		conteneurPrincipal.add(comboBoxRapport, c); //ajout de la comboBox comboBoxRapport
	    
	    //date debut
	    JLabel dateDebut = new JLabel("Date de début : "); //creation du label dateDebut
		c.gridx = 0;
		c.gridy = 19;
		c.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(dateDebut, c); //ajout du label dateDebut
	    try{
			MaskFormatter maskDate  = new MaskFormatter("##/##/####"); //masque pour le format date
			textFieldDateDebut = new JFormattedTextField(maskDate); //initialisation de la zone de texte textFieldDateDebut formattee par le masque maskDate
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    dateDebut.setLabelFor(textFieldDateDebut); //attribution de la zone de texte textFieldDateDebut au label dateDebut
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldDateDebut, c); //ajout de la zone de texte textFieldDateDebut
		
		//date fin
	    JLabel dateFin = new JLabel("Date de fin : "); //creation du label dateFin
		c.gridx = 0;
		c.gridy = 20;
		c.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(dateFin, c); //ajout du label dateFin
	    try{
			MaskFormatter maskDate  = new MaskFormatter("##/##/####"); //masque pour le format date
			textFieldDateFin = new JFormattedTextField(maskDate); //initialisation de la zone de texte textFieldDateFin formattee par le masque maskDate
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    dateFin.setLabelFor(textFieldDateFin); //attribution de la zone de texte textFieldDateFin au label dateFin
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldDateFin, c); //ajout de la zone de texte textFieldDateFin
		
		
		
		
		/*----------------------------------------------formulaire bons preventifs----------------------------------------------------------*/
	    
		JLabel titreBP = new JLabel("Bons préventifs"); //titre de la partie bons preventifs du formulaire
		titreBP.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreBP
		c.gridx = 0;
		c.gridy = 21;
		c.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreBP, c); //ajout du titreBP dans conteneurPrincipal
		
	    c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		//mois
	    JLabel moisBP = new JLabel("mois : ");
		c.gridy = 22;
		conteneurPrincipal.add(moisBP, c); //ajout du label moisBP
		String[] choixMois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"}; //liste differents choix de la duree du rapport d'activite
		comboBoxMoisBP = new JComboBox(choixMois); //initialisation de la comboBox comboBoxMoisBP avec la liste choixMois
		comboBoxMoisBP.setPreferredSize(new Dimension(100, 20)); //dimension de la comboBoxMoisBP
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		moisBP.setLabelFor(comboBoxMoisBP); //attribution de la comboBox comboBoxMoisBP au label moisBP
		conteneurPrincipal.add(comboBoxMoisBP, c); //ajout de la zone de texte comboBox comboBoxMoisBP
		
	    //nombre BP ouverts
	    JLabel nbBPOuverts = new JLabel("Nombre de bons préventifs ouverts : "); //creation du label nbBPOuverts
	    c.gridx = 0;
		c.gridy = 23;
		c.gridwidth = 1;
	    conteneurPrincipal.add(nbBPOuverts, c); //ajout du label nbBPOuverts
	    JTextField textFieldNbBPOuverts = new JTextField(2); //creation de la zone de texte textFieldNbBPOuverts
	    nbBPOuverts.setLabelFor(textFieldNbBPOuverts); //attribution de la zone de texte au label nbBPOuverts
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNbBPOuverts, c); //ajout de la zone de texte textFieldNbBPOuverts
		
		//nombre BP fermes
	    JLabel nbBPFermes = new JLabel("Nombre de bons préventifs fermés : "); //creation du label nbBPFermes
		c.gridx = 0;
		c.gridy = 24;
		c.gridwidth = 1;
	    conteneurPrincipal.add(nbBPFermes, c); //ajout du label nbBPFermes
	    JTextField textFieldNbBPFermes = new JTextField(2); //creation de la zone de texte textFieldNbBPFermes
	    nbBPFermes.setLabelFor(textFieldNbBPFermes); //attribution de la zone de texte textFieldNbBPFermes au label nbBPFermes
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNbBPFermes, c); //ajout de la zone de texte textFieldNbBPFermes
		
		//commentaire BP
	    JLabel commentaireBP = new JLabel("Commentaire : "); //creation du label commentaireBP
		c.gridx = 0;
		c.gridy = 25;
		 c.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    conteneurPrincipal.add(commentaireBP, c); //ajout du label commentaireBP
	    JTextArea textAreaCommentaireBP = new JTextArea(4, 15); //creation de la zone de texte textAreaCommentaireBP
	    JScrollPane scrollPaneComBP = new JScrollPane(textAreaCommentaireBP); //creation de la scrollPane scrollPaneComBP contenant textAreaCommentaireBP
	    commentaireBP.setLabelFor(textAreaCommentaireBP); //attribution de la zone de texte textAreaCommentaireBP au label commentaireBP
		c.gridy = 26;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneComBP, c); //ajout de la scrollPane scrollPaneComBP
	    
	    
	    /*----------------------------------------formulaire bons preventifs par domaine------------------------------------------------*/
	    
	    JLabel titreBPDomaine = new JLabel("Bons préventifs par domaines"); //titre de la partie Bons preventifs par domaine du formulaire
	    titreBPDomaine.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		c.gridx = 0; //position horizontale
		c.gridy = 27; //position verticale
		c.gridwidth = 1; //nombre de cases occupees à partir de sa postion horizontale
		c.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreBPDomaine, c); //ajout du titreBPDomaine dans conteneurPrincipal
	    
	    String[] listeDomaines = {"Clos et ouvert", "Aménagement intérieur", "Ascenseur, monte-charge", "CVC", "Plomberie sanitaire",
	    						  "Electricité CFO", "Sûreté", "Sécurité détection incendie", "Aménagements extérieurs", "Centrale énergie",
	    						  "Cont, réglementaire"}; //liste des differents domaines
	    int nbDomaines = listeDomaines.length; //taille de la liste des domaines
	    int positionyDomaine = 28; //position verticale de depart de la boucle
	    c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    for(int i = 0; i < nbDomaines; i++){
	    	domaine = new JCheckBox(listeDomaines[i]);
	    	c.gridwidth = 1; //nombre de cases occupees à partir de sa postion horizontale
	    	c.gridx = 0; //position horizontale
			c.gridy = positionyDomaine + i; //position de l'element a la position verticale de depart + i
			conteneurPrincipal.add(domaine, c); //ajout de la checkbox domaine
			try{
				MaskFormatter maskPourcent  = new MaskFormatter("##.##%"); //masque pour le format pourcentage
				textFieldPourcent = new JFormattedTextField(maskPourcent); //initialisation de la zone de texte Pourcent1 formattee par le masque
		    }catch(ParseException e){
				e.printStackTrace(); //exception
			}
		    c.gridx = 1; //position horizontale
			c.gridwidth = GridBagConstraints.REMAINDER; //dernier element de sa ligne
			conteneurPrincipal.add(textFieldPourcent, c); //ajout de la zone de texte Pourcent1
	    }
	  
		//commentaire BP par domaine
	    JLabel commentaireBPDomaine = new JLabel("Commentaire : "); //creation du label emailCl
		c.gridx = 0;
		c.gridy = 39;
		c.gridwidth = 1;
		 c.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    conteneurPrincipal.add(commentaireBPDomaine, c); //ajout du label emailCl
	    JTextArea textAreaCommentaireBPDomaine = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneComBPDomaine = new JScrollPane(textAreaCommentaireBPDomaine);
	    commentaireBPDomaine.setLabelFor(textAreaCommentaireBPDomaine); //attribution de la zone de texte au label emailCl
		c.gridy = 40;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneComBPDomaine, c); //ajout de la zone de texte emailCl
	    
	    /*----------------------------------------------formulaire arborescence libre----------------------------------------------------------*/
	    
		JLabel titreArboLibre = new JLabel("Arborescence libre"); //titre de la parte rapport du formulaire
		titreArboLibre.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		c.gridx = 0;
		c.gridy = 41;
		c.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreArboLibre, c); //ajout du titreRapportr dans conteneurPrincipal
		
	    c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		//titre
	    JLabel titre = new JLabel("Titre : ");
		c.gridy = 42;
		conteneurPrincipal.add(titre, c); //ajout du label 
		JTextField textFieldTitre = new JTextField(15); //creation de la zone de texte nomSite de taille 15
		titre.setLabelFor(textFieldTitre); //attribution de la zone de texte au label nomSite
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldTitre, c); //ajout de la zone de texte nomSite
		
	    //element
	    JLabel element = new JLabel("Elément : "); //creation du label dateDebut
		c.gridx = 0;
		c.gridy = 43;
		c.gridwidth = 1;
	    conteneurPrincipal.add(element, c); //ajout du label nbBPOuverts
	    JTextField textFieldElement = new JTextField(15); //initialisation de la zone de texte textFieldNbBPOuverts
	    element.setLabelFor(textFieldElement); //attribution de la zone de texte au label nbBPOuverts
		c.gridx = 1;
		c.gridwidth = 1;
		conteneurPrincipal.add(textFieldElement, c); //ajout de la zone de texte textFieldNbBPOuverts
		
		//nombre
	    JLabel nombre = new JLabel("Nombre  : "); //creation du label dateDebut
		c.gridx = 2;
		c.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(nombre, c); //ajout du label dateDebut
	    JTextField textFieldNombre = new JTextField(2); //initialisation de la zone de texte dateFin formattee par le masque
	    nombre.setLabelFor(textFieldNombre); //attribution de la zone de texte au label dateFin
		c.gridx = 3;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNombre, c); //ajout de la zone de texte dateFin
		
		//bouton d'ajout d'element
		JButton ajoutElement = new JButton("+ Ajouter un élément");
		c.gridx = 1;
		c.gridy = 44;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(ajoutElement, c); //ajout du bouton ajoutElement
		
		//commentaire
	    JLabel commentaire = new JLabel("Commentaire : "); //creation du label emailCl
		c.gridx = 0;
		c.gridy = 45;
		 c.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    conteneurPrincipal.add(commentaire, c); //ajout du label emailCl
	    JTextArea textAreaCommentaire = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneCom = new JScrollPane(textAreaCommentaire);
	    commentaire.setLabelFor(textAreaCommentaire); //attribution de la zone de texte au label emailCl
		c.gridy = 46;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneCom, c); //ajout de la zone de texte emailCl
	    
	    //bouton d'ajout d'arborescence libre
	  	JButton ajoutArboLibre = new JButton("+ Ajouter une arborescence libre");
	  	c.gridx = 0;
	  	c.gridy = 47;
	  	c.gridwidth = 1;
	  	conteneurPrincipal.add(ajoutArboLibre, c); //ajout du bouton ajoutArboLibre
	    
	    /*----------------------------------------------formulaire demandes d'intervention----------------------------------------------------------*/
	    
		JLabel titreDI = new JLabel("Demandes d'intervention"); //titre de la parte rapport du formulaire
		titreDI.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		c.gridx = 0;
		c.gridy = 48;
		c.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreDI, c); //ajout du titreRapportr dans conteneurPrincipal
		
	    c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		//mois
	    JLabel moisDI = new JLabel("Mois : ");
		c.gridy = 49;
		conteneurPrincipal.add(moisDI, c); //ajout du label
		comboBoxMoisDI = new JComboBox(choixMois);
		comboBoxMoisDI.setPreferredSize(new Dimension(100, 20));
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		moisDI.setLabelFor(comboBoxMoisDI); //attribution de la zone de texte au label adr
		conteneurPrincipal.add(comboBoxMoisDI, c); //ajout de la zone de texte adr
		
	    //nombre d'interventions
	    JLabel nbIntervention = new JLabel("Nombre d'interventions : "); //creation du label dateDebut
	    c.gridx = 0;
		c.gridy = 50;
		c.gridwidth = 1;
	    conteneurPrincipal.add(nbIntervention, c); //ajout du label nbBPOuverts
	    JTextField textFieldNbIntervention = new JTextField(2); //initialisation de la zone de texte textFieldNbBPOuverts
	    nbIntervention.setLabelFor(textFieldNbIntervention); //attribution de la zone de texte au label nbBPOuverts
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNbIntervention, c); //ajout de la zone de texte textFieldNbBPOuverts
		
		//commentaire DI
	    JLabel commentaireDI = new JLabel("Commentaire : "); //creation du label emailCl
		c.gridx = 0;
		c.gridy = 51;
		 c.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    conteneurPrincipal.add(commentaireDI, c); //ajout du label emailCl
	    JTextArea textAreaCommentaireDI = new JTextArea(4, 15); //creation de la zone de texte emailCl de taille 15
	    JScrollPane scrollPaneComDI = new JScrollPane(textAreaCommentaireDI);
	    commentaireDI.setLabelFor(textAreaCommentaireDI); //attribution de la zone de texte au label emailCl
		c.gridy = 52;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneComDI, c); //ajout de la zone de texte emailCl
	    
	    
	    /*----------------------------------------formulaire demandes d'intervention par état------------------------------------------------*/
	    JLabel titreDIEtat = new JLabel("Demandes d'intervention par état"); //titre de la partie Bons preventifs par domaine du formulaire
	    titreDIEtat.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre rapport
		c.gridx = 0;
		c.gridy = 53;
		c.gridwidth = 1;
		c.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreDIEtat, c); //ajout du titreBPDomaine dans conteneurPrincipal
	    
	    String[] listeEtats = {"Attente de lecture avant exécution", "Attente de lecture par validateur",
	    						"Attente de réalisation", "En cours de réalisation", "Réalisation partielle"}; //liste des etats
	    int nbEtats = listeEtats.length; //nombre d'etats
	    int positionyEtat = 54; //poston verticale de depart
	    c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    for(int i = 0; i < nbEtats; i++){
	    	etat = new JCheckBox(listeEtats[i]); //creation d'une checkbox pour chaque etat possible
		    c.gridx = 0;
			c.gridy = positionyEtat + i; 
			c.gridwidth = 1;
		    conteneurPrincipal.add(etat, c); //ajout de la checkbox etat
			JTextField textFieldNbEtat = new JTextField(15); //initialisation de la zone de texte textFieldNbEtat
		    c.gridx = 1;
			c.gridwidth = GridBagConstraints.REMAINDER;
			conteneurPrincipal.add(textFieldNbEtat, c); //ajout de la zone de texte textFieldNbEtat
	    }
	    
		//commentaire DI par etat
	    JLabel commentaireDIEtat = new JLabel("Commentaire : "); //creation du label commentaireDIEtat
		c.gridx = 0;
		c.gridy = 59;
		c.gridwidth = 1;
		 c.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    conteneurPrincipal.add(commentaireDIEtat, c); //ajout du label commentaireDIEtat
	    JTextArea textAreaCommentaireDIEtat = new JTextArea(4, 15); //creation de la zone de texte textAreaCommentaireDIEtat
	    JScrollPane scrollPaneComDIEtat = new JScrollPane(textAreaCommentaireDIEtat); //creation de la scrollPane scrollPaneComDIEtat contenant textAreaCommentaireDIEtat
	    commentaireDIEtat.setLabelFor(textAreaCommentaireDIEtat); //attribution de la zone de texte textAreaCommentaireDIEtat au label commentaireDIEtat
		c.gridy = 60;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneComDIEtat, c); //ajout de scrollPaneComDIEtat
	    
	    /*----------------------------------------------formulaire arborescence libre----------------------------------------------------------*/
	    
		JLabel titreArboLibre2 = new JLabel("Arborescence libre"); //titre de la partie arborescence libre 2 du formulaire
		titreArboLibre2.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreArboLibre2
		c.gridx = 0;
		c.gridy = 61;
		c.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreArboLibre2, c); //ajout du titreArboLibre2 dans conteneurPrincipal
		
	    c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		//titre
	    JLabel titre2 = new JLabel("Titre : "); //creation du label titre2
		c.gridy = 62;
		conteneurPrincipal.add(titre2, c); //ajout du label titre2
		JTextField textFieldTitre2 = new JTextField(15); //creation de la zone de texte textFieldTitre2 de taille 15
		titre2.setLabelFor(textFieldTitre2); //attribution de la zone de texte textFieldTitre2 au label titre2
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldTitre2, c); //ajout de la zone de texte textFieldTitre2
		
	    //element
	    JLabel element2 = new JLabel("Elément : "); //creation du label element2
		c.gridx = 0;
		c.gridy = 63;
		c.gridwidth = 1;
	    conteneurPrincipal.add(element2, c); //ajout du label element2
	    JTextField textFieldElement2 = new JTextField(15); //creation de la zone de texte textFieldElement2
	    element2.setLabelFor(textFieldElement2); //attribution de la zone de texte textFieldElement2 au label element2
		c.gridx = 1;
		c.gridwidth = 1;
		conteneurPrincipal.add(textFieldElement2, c); //ajout de la zone de texte textFieldElement2
		
		//nombre
	    JLabel nombre2 = new JLabel("Nombre  : "); //creation du label nombre2
		c.gridx = 2;
		c.gridwidth = GridBagConstraints.RELATIVE;
	    conteneurPrincipal.add(nombre2, c); //ajout du label nombre2
	    JTextField textFieldNombre2 = new JTextField(2); //creation de la zone de texte textFieldNombre2
	    nombre2.setLabelFor(textFieldNombre2); //attribution de la zone de texte textFieldNombre2 au label nombre2
		c.gridx = 3;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(textFieldNombre2, c); //ajout de la zone de texte textFieldNombre2
		
		//bouton d'ajout d'element
		JButton ajoutElement2 = new JButton("+ Ajouter un élément");
		c.gridx = 1;
		c.gridy = 64;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(ajoutElement2, c); //ajout du bouton ajoutElement
		
		//commentaire
	    JLabel commentaire2 = new JLabel("Commentaire : "); //creation du label commentaire2
		c.gridx = 0;
		c.gridy = 65;
		 c.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    conteneurPrincipal.add(commentaire2, c); //ajout du label commentaire2
	    JTextArea textAreaCommentaire2 = new JTextArea(4, 15); //creation de la zone de texte textAreaCommentaire2
	    JScrollPane scrollPaneCom2 = new JScrollPane(textAreaCommentaire2);
	    commentaire2.setLabelFor(textAreaCommentaire2); //attribution de la zone de texte textAreaCommentaire2 au label commentaire2
		c.gridy = 66;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    conteneurPrincipal.add(scrollPaneCom2, c); //ajout de la scrollPaneCom2
	    
	    //bouton d'ajout d'arborescence libre
	  	JButton ajoutArboLibre2 = new JButton("+ Ajouter une arborescence libre");
	  	c.gridx = 0;
	  	c.gridy = 67;
	  	c.gridwidth = 1;
	  	conteneurPrincipal.add(ajoutArboLibre2, c); //ajout du bouton ajoutArboLibre
	    
	    /*----------------------------------------------formulaire compteurs----------------------------------------------------------*/
	    
		JLabel titreCompteurs = new JLabel("Compteurs"); //titre de la partie compteurs du formulaire
		titreCompteurs.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreCompteurs
		c.gridx = 0;
		c.gridy = 68;
		c.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreCompteurs, c); //ajout du titreCompteurs dans conteneurPrincipal
		
	    c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
	    
	    //type de compteur
	    JLabel typeCompteur = new JLabel("Type du compteur : "); //creation du label typeCompteur
		c.gridy = 69;
		conteneurPrincipal.add(typeCompteur, c); //ajout du label 
		String[] choixTypeCompteur = {"eau", "gaz", "électricité", "énergie"}; //differents choix de type de compteur
		comboBoxTypeCompteur = new JComboBox(choixTypeCompteur);
		comboBoxTypeCompteur.setPreferredSize(new Dimension(100, 20));
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		typeCompteur.setLabelFor(comboBoxTypeCompteur); //attribution de la zone de texte comboBoxTypeCompteur au label typeCompteur
		conteneurPrincipal.add(comboBoxTypeCompteur, c); //ajout de la zone de texte comboBoxTypeCompteur
		
		//mois
	    JLabel moisCompteur = new JLabel("Mois : ");
	    c.gridx = 0;
		c.gridy = 70;
		conteneurPrincipal.add(moisCompteur, c); //ajout du label moisCompteur
		comboBoxMoisCompteur = new JComboBox(choixMois);
		comboBoxMoisCompteur.setPreferredSize(new Dimension(100, 20));
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		moisCompteur.setLabelFor(comboBoxMoisCompteur); //attribution de la moisCompteur au label moisCompteur
		conteneurPrincipal.add(comboBoxMoisCompteur, c); //ajout de la comboBoxMoisCompteur
		
		//consommation
	    JLabel consommation = new JLabel("Consommation : ");
	    c.gridx = 0;
		c.gridy = 71;
		c.gridwidth = 1;
		conteneurPrincipal.add(consommation, c); //ajout du label consommation
		JTextField textFieldConsommation = new JTextField(15); //creation de la zone de texte textFieldConsommation de taille 15
		consommation.setLabelFor(textFieldConsommation); //attribution de la zone de texte textFieldConsommation au label consommation
		c.gridx = 1;
		conteneurPrincipal.add(textFieldConsommation, c); //ajout de la zone de texte textFieldConsommation
		
		//unite
	    String[] choixUnite = {"m³", "kWh", "MWh"}; //differents choix de l'unite
	    comboBoxUnite = new JComboBox(choixUnite);
	    comboBoxUnite.setPreferredSize(new Dimension(20, 20));
		c.gridx = 2;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(comboBoxUnite, c); //ajout de la comboBoxUnite
		
		//bouton d'ajout de mois pour le compteur
		JButton ajoutMoisCompteur = new JButton("+ Ajouter un mois");
		c.gridx = 1;
		c.gridy = 72;
		c.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(ajoutMoisCompteur, c); //ajout du bouton ajoutMoisCompteur
		
		//commentaire
		 c.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
	    JLabel commentaireCompteur = new JLabel("Commentaire : "); //creation du label commentaireCompteur
		c.gridx = 0;
		c.gridy = 73;
	    conteneurPrincipal.add(commentaireCompteur, c); //ajout du label commentaireCompteur
	    JTextArea textAreaComCompteur = new JTextArea(4, 15); //creation de la zone de texte textAreaComCompteur
	    JScrollPane scrollPaneComCompteur = new JScrollPane(textAreaComCompteur);
	    commentaireCompteur.setLabelFor(textAreaComCompteur); //attribution de la zone de texte au label commentaireCompteur
	    c.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		c.gridy = 74;
		c.gridwidth = GridBagConstraints.REMAINDER;
	    conteneurPrincipal.add(scrollPaneComCompteur, c); //ajout de la scrollPaneComCompteur
	    
	    //bouton d'ajout de compteur
	  	JButton ajoutCompteur = new JButton("+ Ajouter un compteur");
	  	c.gridx = 0;
	  	c.gridy = 75;
	  	c.gridwidth = 1;
	  	conteneurPrincipal.add(ajoutCompteur, c); //ajout du bouton ajoutCompteur
		
		/*-----------------------------------------Bouton de validation du formulaire--------------------------------------------------- */
		
		JButton valideForm = new JButton("Génerer le rapport"); //bouton de validation du formulaire 
		//valideForm.setBackground(new Color(224, 35, 60));
	    c.gridy = 76;
	    c.gridwidth = GridBagConstraints.REMAINDER;
	    c.insets = new Insets(40, 0, 0, 0); //marges autour de l'element
	    conteneurPrincipal.add(valideForm, c); //ajout du bouton de validation
	    
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
		    	System.out.println(moisBP.getText() + comboBoxMoisBP.getSelectedItem().toString()); //affichage console des données mois
		    	System.out.println(nbBPOuverts.getText() + textFieldNbBPOuverts.getText()); 		//affichage console des données nbBPOuverts
		    	System.out.println(nbBPFermes.getText() + textFieldNbBPFermes.getText()); 			//affichage console des données nbBPFermes
		    	System.out.println(commentaireBP.getText() + textAreaCommentaireBP.getText()); 		//affichage console des données commentaireBP
		    	//partie bons preventifs par domaine
		    	System.out.println(titreBPDomaine.getText()); //affichage console du titre de la partie du formulaire
		    	for(int i = 0; i < nbDomaines; ++i){
		    		if (domaine.isSelected() == true) {
		    			System.out.println(domaine.getText() + " : " + textFieldPourcent.getText());           
		    		}
		    	}
		    	System.out.println (commentaireBP.getText() + textAreaCommentaireBP.getText()); 	//ecriture des données commentaireBP
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
		    	domaine.addItemListener(new ItemListener() {
		            public void itemStateChanged(ItemEvent e) {         
		            	System.out.println(domaine.getText() + (e.getStateChange()==1? " " : textFieldPourcent.getText()));
		            } 
		         });
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
		    	fw.println (moisBP.getText() + comboBoxMoisBP.getSelectedItem().toString());	//ecriture des données moisBP
		    	fw.println (nbBPOuverts.getText() + textFieldNbBPOuverts.getText()); 			//ecriture des données nbBPOuverts
		    	fw.println (nbBPFermes.getText() + textFieldNbBPFermes.getText()); 				//ecriture des données nbBPFermes
		    	fw.println (commentaireBP.getText() + textAreaCommentaireBP.getText()); 		//ecriture des données commentaireBP
		    	//partie bons preventifs par domaine
			    fw.println (titreBPDomaine.getText()); 										//ecriture du titre de la partie du formulaire
		    	fw.println (commentaireBPDomaine.getText() + textAreaCommentaireBPDomaine.getText()); 	//ecriture des données commentaireBPDomaine
		    	//partie arborescence libre
		    	fw.println (titreArboLibre.getText()); 								//ecriture du titre de la partie du formulaire
		    	fw.println (titre.getText() + textFieldTitre.getText()); 			//ecriture des données titre
		    	fw.println (element.getText() + textFieldElement.getText()); 		//ecriture des données element
		    	fw.println (nombre.getText() + textFieldNombre.getText()); 			//ecriture des données nombre
		    	fw.println (commentaire.getText() + textAreaCommentaire.getText()); //ecriture des données commentaire
		    	//partie domaines d'intervention
		    	fw.println(titreDI.getText()); 												//ecriture du titre de la partie du formulaire
		    	fw.println(moisDI.getText() + comboBoxMoisBP.getSelectedItem().toString()); //ecriture des données moisDI
		    	fw.println(nbIntervention.getText() + textFieldNbIntervention.getText()); 	//ecriture des données nbIntervention
		    	fw.println(commentaireDI.getText() + textAreaCommentaireBP.getText()); 		//ecriture des données commentaireDI
		    	//partie domaines d'intervention par etat
		    	System.out.println(titreDIEtat.getText()); 													//ecriture du titre de la partie du formulaire
		    	System.out.println (commentaireDIEtat.getText() + textAreaCommentaireDIEtat.getText()); 	//ecriture des données commentaireBPDomaine
		    	domaine.addItemListener(new ItemListener() {
		            public void itemStateChanged(ItemEvent e) {         
		            	System.out.println(domaine.getText() + (e.getStateChange()==1? " " : textFieldPourcent.getText()));
		            } 
		         });
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

}