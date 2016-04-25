package IHM;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Font;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class Formulaire extends JFrame{
	
	public Formulaire() throws IOException{
		JPanel fenetre = new JPanel(); //creation de la fenetre principale
		
		this.setTitle("Facilities Rapport"); //titre fenetre
		this.setSize(400, 300); //taille fenetre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //pour fermer la fenetre
	    this.setLocationRelativeTo(null); //position de la fenetre
	    fenetre.setBackground(Color.white); //couleur de fond de la fenetre
	    fenetre.setLayout(new BorderLayout()); 
	    
	    JLabel titre = new JLabel("Facilities Rapport", SwingConstants.CENTER); //titre formulaire
		titre.setFont(new Font("Arial",Font.BOLD,18)); //police + taille du titre formulaire
		
		//scrollBar
	    JScrollPane scrollBar = new JScrollPane(fenetre);
	    this.add(scrollBar);
	    
	    
		/*-----------------------------------------formulaire redacteur--------------------------------------------*/
		
	    JPanel conteneurRedacteur = new JPanel(); //panel pour la partie redacteur du formulaire
		JLabel titreRedacteur = new JLabel("Redacteur"); //titre redacteur
		titreRedacteur.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titre redacteur
		
		
		String[] labelsRedacteur = {"Nom : ", "Adresse : ", "Téléphone : ", "Email : ", "Nom du chargé d'affaire : "}; //titre des zones de texte
		int nbLabelsRedac = labelsRedacteur.length; //variable qui correspond au nombre de zones de texte
		JButton valideRedacteur = new JButton("Valider"); //bouton de validation de la partie redacteur
	   
		JPanel redacteur = new JPanel(new SpringLayout()); //creation du panel redacteur
		//initialisation du contenu du panel redacteur
		for (int i = 0; i < nbLabelsRedac; i++) { 
		    JLabel redac = new JLabel(labelsRedacteur[i]); //creation des labels pour chaque titre
		    redacteur.add(redac); //ajout des labels au panel redacteur
		    JTextField textFieldRedac = new JTextField(15); //creation des zones de texte de taille 15
		    redac.setLabelFor(textFieldRedac); //attribution des zones de textes aux labels
		    redacteur.add(textFieldRedac); //ajout des zones de texte au panel redacteur
		    
	    
		    //action declenchee par le bouton de validation du formulaire
		    valideRedacteur.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent arg0) {	
		    		System.out.println(redac.getText() + textFieldRedac.getText()); //affichage console des informations saisies dans le formulaire
				
				
		    		File f = new File ("rapport.txt"); //création d'un rapport au format txt
		    		PrintWriter fw;
				    
		    		try {
		    			fw = new PrintWriter (new BufferedWriter (new FileWriter (f)));
		    			fw.println ("test"); //test d"ecriture 
		    			
		    			fw.println (redac.getText() + textFieldRedac.getText()); //ecriture des donnees formulaire
		    			fw.println (labelsRedacteur[1] + textFieldRedac.getText()); 
		    			fw.println (labelsRedacteur[2] + textFieldRedac.getText());
		    			fw.println (labelsRedacteur[3] + textFieldRedac.getText());
		    			fw.println (labelsRedacteur[4] + textFieldRedac.getText());
		    			fw.println ("\r\n"); //retour ligne
		    			fw.close();

		    		} catch (IOException e) {
		    			System.out.println("lecture non valide");
		    			e.printStackTrace();
					}
		    	}
		    });
		}
		
	    
	    
		//mise en page du panel redacteur
        SpringUtilities.makeCompactGrid(redacteur,
                                        nbLabelsRedac, 2, //rows, cols
                                        6, 6,        	  //initX, initY
                                        6, 6);            //xPad, yPad
		
    
        
        
		/*-------------------------------------------formulaire client-------------------------------------------------*/
		
        /*String[] labelsClient = {"Nom du site : ", "Code du site : ", "Adresse : ", "Code postal : ", "Ville : ",
				"Numéro du client : ", "Téléphone : ", "Mail : ", "Logo"};
		int nbLabelsCl = labelsClient.length; //variable qui correspond au nombre de zones de texte

		JPanel conteneurClient = new JPanel(); //panel pour la partie client du formulaire
		JLabel titreClient = new JLabel("Client"); //titre client
		titreClient.setFont(new Font("Arial", Font.BOLD, 14)); //police + taille titre client
		
		JPanel client = new JPanel (new SpringLayout()); //creation du panel client
		//initialisation du contenu du pannel client
		for (int i = 0; i < nbLabelsCl; i++) {
			JLabel cl = new JLabel(labelsClient[i]); //creation des labels pour chaque titre
			client.add(cl); //ajout des labels pour chaque titre
			JTextField textFieldCl = new JTextField(15); //creation des zones de texte de taille 15
			cl.setLabelFor(textFieldCl); //attribution des zones de texte aux labels
			client.add(textFieldCl); //ajout des zones de texte au panel client
		
	
        //mise en page du panel Client
        //SpringUtilities.makeCompactGrid(client, nbLabelsCl, 2, 6, 6, 6, 6);*/
	    
        
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
        
	    conteneurRedacteur.add(titreRedacteur, BorderLayout.NORTH); //ajout du titreRedacteur en haut du panel conteneurRedacteur 
	    conteneurRedacteur.add(redacteur, BorderLayout.CENTER); //ajout du panel redacteur au centre du conteneurRedacteur
	    //conteneurClient.add(titreClient, BorderLayout.NORTH);
	    //conteneurClient.add(client, BorderLayout.CENTER);
	    
	    fenetre.add(titre, BorderLayout.NORTH); //ajout du titre en haut de la fenetre 
	    fenetre.add(conteneurRedacteur, BorderLayout.CENTER); //ajout du conteneurRedacteur au centre de la fenetre
	    fenetre.add(valideRedacteur, BorderLayout.SOUTH); //ajout du bouton de validation en bas de la fenetre
	    //fenetre.add(conteneurClient, BorderLayout.CENTER);
	    
	   
	    this.setContentPane(fenetre); 
	    this.setVisible(true);  //visibilite
	    
	 }	

}

