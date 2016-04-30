package IHM;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;

public class Form extends JFrame{
	private JFormattedTextField textFieldTel = new JFormattedTextField(); //declaration du textField telephone initialise dans le try
	private JFormattedTextField textFieldCode = new JFormattedTextField(); //declaration du textField telephone initialise dans le try
	private JFormattedTextField textFieldCodePostal = new JFormattedTextField(); //declaration du textField telephone initialise dans le try
	private JFormattedTextField textFieldTelCl = new JFormattedTextField(); //declaration du textField telephone initialise dans le try
	public Form() throws IOException{
		JPanel fenetre = new JPanel(); //creation de la fenetre principale
		
		this.setTitle("Facilities Rapport"); //titre fenetre
		this.setSize(700, 600); //taille fenetre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //pour fermer la fenetre
	    this.setLocationRelativeTo(null); //position de la fenetre
	    fenetre.setBackground(Color.white); //couleur de fond de la fenetre
	    fenetre.setLayout(new BorderLayout()); 
	    
	    JLabel titre = new JLabel("Facilities Rapport", SwingConstants.CENTER); //titre formulaire
		titre.setFont(new Font("Arial",Font.BOLD,18)); //police + taille du titre formulaire
		
		JPanel conteneurPrincipal = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
	    
	    
		/*-----------------------------------------formulaire redacteur--------------------------------------------*/
		
	    JPanel conteneurRedacteur = new JPanel(); //panel pour la partie redacteur du formulaire
	    
		JLabel titreRedacteur = new JLabel("Redacteur"); //titre redacteur
		titreRedacteur.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre redacteur
		
		JPanel redacteur = new JPanel(new GridBagLayout()); //creation du panel redacteur
		redacteur.setSize (700, 400);
		GridBagConstraints cRed = new GridBagConstraints();
		cRed.fill = GridBagConstraints.BOTH;
		
		//contenu du panel redacteur
		//nom
		JLabel nom = new JLabel("Nom : "); //creation du label nom
		//c.weightx = 0.5;
		cRed.gridx = 0;
		cRed.gridy = 1;
		redacteur.add(nom, cRed); //ajout du label nom au panel redacteur
		JTextField textFieldNom = new JTextField(15); //creation de la zone de texte adr de taille 15
		cRed.weightx = 1.5;
		cRed.gridx = 1;
		cRed.gridy = 1;
		nom.setLabelFor(textFieldNom); //attribution de la zone de texte au label adr
		redacteur.add(textFieldNom, cRed); //ajout de la zone de texte adr au panel redacteur  
		
		//adresse 
		JLabel adr = new JLabel("Adresse : "); //creation du label adr
		cRed.gridx = 0;
		cRed.gridy = 2;
	    redacteur.add(adr, cRed); //ajout du label adr au panel redacteur
	    JTextArea textAreaAdr = new JTextArea(3, 15); //creation de la zone de texte adr de taille 3 en hauteur et 15 en largeur
	    LineBorder border = new LineBorder(Color.GRAY, 1); //création d'une bordure
	    textAreaAdr.setBorder(border); //attribution de la bordure a la zone de texte adr
		cRed.gridx = 1;
		cRed.gridy = 2;
	    adr.setLabelFor(textAreaAdr); //attribution de la zone de texte au label adr
	    redacteur.add(textAreaAdr, cRed); //ajout de la zone de texte adr au panel redacteur 
	    
	    //telephone
	    JLabel tel = new JLabel("Téléphone : "); //creation du label tel
		cRed.gridx = 0;
		cRed.gridy = 3;
	    redacteur.add(tel, cRed); //ajout du label tel au panel redacteur
	    try{
			MaskFormatter maskTel  = new MaskFormatter("## ## ## ## ##"); //masque pour le format du numero de telephone
			textFieldTel = new JFormattedTextField(maskTel); //initialisation de la zone de texte tel formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    tel.setLabelFor(textFieldTel); //attribution de la zone de texte au label tel
		cRed.gridx = 1;
		cRed.gridy = 3;
		redacteur.add(textFieldTel, cRed); //ajout de la zone de texte tel au panel redacteur
	   
	    //email
	    JLabel email = new JLabel("Email : "); //creation du label email
		cRed.gridx = 0;
		cRed.gridy = 4;
	    redacteur.add(email, cRed); //ajout du label nom au panel redacteur
	    JTextField textFieldEmail = new JTextField(15); //creation de la zone de texte email de taille 15
	    email.setLabelFor(textFieldEmail); //attribution de la zone de texte au label email
		cRed.gridx = 1;
		cRed.gridy = 4;
	    redacteur.add(textFieldEmail, cRed); //ajout de la zone de texte email au panel redacteur 
	    
	    //nom charge d'affaire
	    JLabel nomCA = new JLabel("Nom du chargé d'affaire : "); //creation du label nomCA
		cRed.gridx = 0;
		cRed.gridy = 5;
	    redacteur.add(nomCA, cRed); //ajout du label nomCA au panel redacteur
	    JTextField textFieldNomCA = new JTextField(15); //creation de la zone de texte nomCA de taille 15
	    nomCA.setLabelFor(textFieldNomCA); //attribution de la zone de texte au label nomCA
		cRed.gridx = 1;
		cRed.gridy = 5;
	    redacteur.add(textFieldNomCA, cRed); //ajout de la zone de texte nomCA au panel redacteur 
	    
	    JButton valideRedacteur = new JButton("Valider"); //bouton de validation de la partie redacteur
		//action declenchee par le bouton de validation du formulaire
		valideRedacteur.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {	
		    	System.out.println(nom.getText() + textFieldNom.getText()); //affichage console des données nom
		    	System.out.println(adr.getText() + textAreaAdr.getText()); //affichage console des données adr
		    	System.out.println(tel.getText() + textFieldTel.getText()); //affichage console des données tel
		    	System.out.println(email.getText() + textFieldEmail.getText()); //affichage console des données email
		    	System.out.println(nomCA.getText() + textFieldNomCA.getText()); //affichage console des données nomCA
				
		    	File f = new File ("rapport.txt"); //création d'un rapport au format txt
		    	PrintWriter fw;
				     
		    	try {
		    		fw = new PrintWriter (new BufferedWriter (new FileWriter (f)));
		    		fw.println ("Redacteur"); //test d"ecriture 	
		    		fw.println (nom.getText() + textFieldNom.getText()); //ecriture des donnees nom
		    		fw.println (adr.getText() + textAreaAdr.getText()); //ecriture des donnees adr
		    		fw.println (tel.getText() + textFieldTel.getText()); //ecriture des donnees tel
		    		fw.println (email.getText() + textFieldEmail.getText()); //ecriture des donnees email
		    		fw.println (nomCA.getText() + textFieldNomCA.getText()); //ecriture des donnees nomCA
		    		fw.println ("\r\n"); //retour ligne
		    		fw.close();
		    	} catch (IOException e) {
		    		System.out.println("lecture non valide");
		    		e.printStackTrace();
				}
		    }
		});
		
	
        
		/*-------------------------------------------formulaire client-------------------------------------------------*/
		
		JPanel conteneurClient = new JPanel(); //panel pour la partie redacteur du formulaire
		JLabel titreClient = new JLabel("Client"); //titre redacteur
		titreClient.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre redacteur
		
		JPanel client = new JPanel(new GridBagLayout()); //creation du panel redacteur
		GridBagConstraints cCl = new GridBagConstraints();
		cCl.fill = GridBagConstraints.BOTH;
		
		//contenu du panel redacteur
		//nom
		JLabel nomSite = new JLabel("Nom du site : "); //creation du label nom
		//c.weightx = 0.5;
		cCl.gridx = 0;
		cCl.gridy = 1;
		client.add(nomSite, cCl); //ajout du label nom au panel redacteur
		JTextField textFieldNomSite = new JTextField(15); //creation de la zone de texte adr de taille 15
		cCl.gridx = 1;
		cCl.gridy = 1;
		nomSite.setLabelFor(textFieldNomSite); //attribution de la zone de texte au label adr
		client.add(textFieldNomSite, cCl); //ajout de la zone de texte adr au panel redacteur 
		
		//code
	    JLabel code = new JLabel("Code : "); //creation du label tel
		cCl.gridx = 0;
		cCl.gridy = 2;
	    client.add(code, cCl); //ajout du label tel au panel redacteur
	    try{
			MaskFormatter maskCode  = new MaskFormatter("##############"); //masque pour le format du numero de telephone
			textFieldCode = new JFormattedTextField(maskCode); //initialisation de la zone de texte tel formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    code.setLabelFor(textFieldCode); //attribution de la zone de texte au label tel
		cCl.gridx = 1;
		cCl.gridy = 2;
		client.add(textFieldCode, cCl); //ajout de la zone de texte tel au panel redacteur
		
		//adresse 
		JLabel adrCl = new JLabel("Adresse : "); //creation du label adr
		cCl.gridx = 0;
		cCl.gridy = 3;
	    client.add(adrCl, cCl); //ajout du label adr au panel redacteur
	    JTextArea textAreaAdrCl = new JTextArea(3, 15); //creation de la zone de texte adr de taille 3 en hauteur et 15 en largeur
	    textAreaAdrCl.setBorder(border); //attribution de la bordure a la zone de texte adr
		cCl.gridx = 1;
		cCl.gridy = 3;
	    adrCl.setLabelFor(textAreaAdrCl); //attribution de la zone de texte au label adr
	    client.add(textAreaAdrCl, cCl); //ajout de la zone de texte adr au panel redacteur 
	    
	    //code postal
	    JLabel codePostal = new JLabel("Code postal : "); //creation du label tel
		cCl.gridx = 0;
		cCl.gridy = 4;
	    client.add(codePostal, cCl); //ajout du label tel au panel redacteur
	    try{
			MaskFormatter maskCodePostal  = new MaskFormatter("## ###"); //masque pour le format du numero de telephone
			textFieldCodePostal = new JFormattedTextField(maskCodePostal); //initialisation de la zone de texte tel formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    codePostal.setLabelFor(textFieldCodePostal); //attribution de la zone de texte au label tel
		cCl.gridx = 1;
		cCl.gridy = 4;
		client.add(textFieldCodePostal, cCl); //ajout de la zone de texte tel au panel redacteur
		
		//ville
		JLabel ville = new JLabel("Ville : "); //creation du label adr
		cCl.gridx = 0;
		cCl.gridy = 5;
		client.add(ville, cCl); //ajout du label adr au panel redacteur
		JTextField textFieldVille = new JTextField(15); //creation de la zone de texte adr de taille 15
		cCl.gridx = 1;
		cCl.gridy = 5;
		ville.setLabelFor(textFieldVille); //attribution de la zone de texte au label adr
		client.add(textFieldVille, cCl); //ajout de la zone de texte adr au panel redacteur 
		
		//nom du client
	    JLabel nomClient = new JLabel("Nom du client : "); //creation du label nomCA
		cCl.gridx = 0;
		cCl.gridy = 6;
	    client.add(nomClient, cCl); //ajout du label nomCA au panel redacteur
	    JTextField textFieldNomClient = new JTextField(15); //creation de la zone de texte nomCA de taille 15
	    nomClient.setLabelFor(textFieldNomClient); //attribution de la zone de texte au label nomCA
		cCl.gridx = 1;
		cCl.gridy = 6;
	    client.add(textFieldNomClient, cCl); //ajout de la zone de texte nomCA au panel redacteur 
	    
	    //telephone
	    JLabel telCl = new JLabel("Téléphone : "); //creation du label tel
		cCl.gridx = 0;
		cCl.gridy = 7;
	    client.add(telCl, cCl); //ajout du label tel au panel redacteur
	    try{
			MaskFormatter maskTelCl  = new MaskFormatter("## ## ## ## ##"); //masque pour le format du numero de telephone
			textFieldTelCl = new JFormattedTextField(maskTelCl); //initialisation de la zone de texte tel formattee par le masque
	    }catch(ParseException e){
			e.printStackTrace(); //exception
		}
	    telCl.setLabelFor(textFieldTelCl); //attribution de la zone de texte au label tel
		cCl.gridx = 1;
		cCl.gridy = 7;
		client.add(textFieldTelCl, cCl); //ajout de la zone de texte tel au panel redacteur
	   
	    //email
	    JLabel emailCl = new JLabel("Email : "); //creation du label email
		cCl.gridx = 0;
		cCl.gridy = 8;
	    client.add(emailCl, cCl); //ajout du label nom au panel redacteur
	    JTextField textFieldEmailCl = new JTextField(15); //creation de la zone de texte email de taille 15
	    emailCl.setLabelFor(textFieldEmailCl); //attribution de la zone de texte au label email
		cCl.gridx = 1;
		cCl.gridy = 8;
	    client.add(textFieldEmailCl, cCl); //ajout de la zone de texte email au panel redacteur 
	    
	    JButton valideClient = new JButton("Valider"); //bouton de validation de la partie redacteur
		//action declenchee par le bouton de validation du formulaire
		valideClient.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {	
		    	System.out.println(nomSite.getText() + textFieldNomSite.getText()); //affichage console des données nom
		    	System.out.println(code.getText() + textFieldCode.getText()); //affichage console des données adr
		    	System.out.println(adrCl.getText() + textAreaAdrCl.getText()); //affichage console des données tel
		    	System.out.println(codePostal.getText() + textFieldCodePostal.getText()); //affichage console des données email
		    	System.out.println(ville.getText() + textFieldVille.getText()); //affichage console des données nomCA
		    	System.out.println(telCl.getText() + textFieldTelCl.getText()); //affichage console des données tel
		    	System.out.println(emailCl.getText() + textFieldEmailCl.getText()); //affichage console des données email
		    	
				
		    	File f = new File ("rapportCl.txt"); //création d'un rapport au format txt
		    	PrintWriter fw;
				     
		    	try {
		    		fw = new PrintWriter (new BufferedWriter (new FileWriter (f)));
		    		fw.println ("Client"); //test d"ecriture 	
		    		fw.println (nomSite.getText() + textFieldNomSite.getText()); //affichage console des données nom
		    		fw.println (code.getText() + textFieldCode.getText()); //affichage console des données adr
		    		fw.println (adrCl.getText() + textAreaAdrCl.getText()); //affichage console des données tel
		    		fw.println (codePostal.getText() + textFieldCodePostal.getText()); //affichage console des données email
		    		fw.println (ville.getText() + textFieldVille.getText()); //affichage console des données nomCA
		    		fw.println (telCl.getText() + textFieldTelCl.getText()); //affichage console des données tel
		    		fw.println (emailCl.getText() + textFieldEmailCl.getText()); //affichage console des données email
		    		fw.println ("\r\n"); //retour ligne
		    		fw.close();
		    	} catch (IOException e) {
		    		System.out.println("lecture non valide");
		    		e.printStackTrace();
				}
		    }
		});
	    
        
	    /*----------------------------------------------combobox et checkbox-------------------------------------------------*/
	    
        /*
        //ComboBox
		JComboBox<String> combo = new JComboBox<String>(); 
		JLabel label = new JLabel("choix");
		JComboBox<String> combo2 = new JComboBox<String>();
		JLabel label2 = new JLabel("choix2");
	    
	    //CheckBox
		JCheckBox check1 = new JCheckBox("check1");
		JCheckBox check2 = new JCheckBox("check2");
		JCheckBox check3 = new JCheckBox("check3");
		JCheckBox check4 = new JCheckBox("check4");
		JCheckBox check5 = new JCheckBox("check5");
		
		//Texte
		 TextField text = new TextField();
		JLabel labeltext1 = new JLabel("text");
		TextField text2 = new TextField();
		JLabel labeltext2 = new JLabel("text2");
		TextField text3 = new TextField();
		JLabel labeltext3 = new JLabel("text3");
		TextField text4 = new TextField();
		JLabel labeltext4 = new JLabel("text4");
		
	    
	    //Combo 1
	    String[] tab = {"Option 1", "Option 2", "Option3", "Option4"}; //contenu
	    combo = new JComboBox(tab);
	    combo.setPreferredSize(new Dimension(100, 20));
	    
	   
	    //Combo 2
	    String[] tab2 = {"a", "b", "c", "d"}; //contenu
	    combo2 = new JComboBox(tab2);
	    combo2.setPreferredSize(new Dimension(100, 20)); 
	    
	    */
	    
        
        /*--------------------------------------------------ajout des éléments---------------------------------------------*/
        
        //scrollBar
	    JScrollPane scrollBar = new JScrollPane(conteneurRedacteur);
	    fenetre.add(scrollBar);
	    
	    cRed.gridx = 0;
		cRed.gridy = 0;
	    redacteur.add(titreRedacteur, cRed); //ajout du titreRedacteur en haut du panel conteneurRedacteur 
	    cRed.gridx = 0;
	    cRed.gridy = 9;
	    redacteur.add(valideRedacteur, cRed); //ajout du bouton de validation en bas de la fenetre
	    conteneurRedacteur.add(redacteur, BorderLayout.CENTER); //ajout du panel redacteur au centre du conteneurRedacteur
	    c.gridx = 0;
	    c.gridy = 0;
	    conteneurPrincipal.add(conteneurRedacteur, c);
	    //conteneurClient.add(titreClient, BorderLayout.NORTH);
	    //conteneurClient.add(client, BorderLayout.CENTER);
	    cCl.gridx = 0;
		cCl.gridy = 0;
	    client.add(titreClient, cCl); //ajout du titreRedacteur en haut du panel conteneurRedacteur 
	    cCl.gridx = 0;
	    cCl.gridy = 9;
	    client.add(valideClient, cCl); //ajout du bouton de validation en bas de la fenetre
	    conteneurClient.add(client, BorderLayout.CENTER); //ajout du panel redacteur au centre du conteneurRedacteur
	    c.gridx = 0;
	    c.gridy = 1;
	    conteneurPrincipal.add(conteneurClient, c);
	    
	    fenetre.add(titre, BorderLayout.NORTH); //ajout du titre en haut de la fenetre 
	    fenetre.add(conteneurPrincipal, BorderLayout.CENTER); //ajout du conteneurRedacteur au centre de la fenetre
	    //fenetre.add(conteneurClient, BorderLayout.CENTER);
	    
	   
	    this.setContentPane(fenetre); 
	    this.setVisible(true);  //visibilite
	    
	 }	

}

