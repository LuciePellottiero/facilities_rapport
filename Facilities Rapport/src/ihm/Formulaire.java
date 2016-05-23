package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
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
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
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

	private JButton ajoutMoisBP;
	
	/**
	 * Declaration de la Collection<JFormattedTextField> des Pourcents date fin remplis dans le try
	 */
	private Collection<JFormattedTextField> textFieldPourcentsBP;
	/**
	 * Declaration de la Collection<JCheckBox> des domaines de bon de prevention remplis au dessus du try
	 */
	private Collection<JCheckBox> domainesBP;
	
	/**
	 * String utilisees pour chaque JLable de mois de bons preventifs
	 */
	private final String[] preventiveVoucherMonthLabels    = {"Mois : ", "Nombre de bons préventifs ouverts : ", 
			"Nombre de bons préventifs fermés : ", "Commentaire : "};
    
	/**
	 * liste differents choix de la duree du rapport d'activite
	 */
	private final String[] MONTH_CHOICE = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", 
			"Août", "Septembre", "Octobre", "Novembre", "Décembre"}; 
	
	private final String[] ADD_MONTH_BUTTON_TEXT = {"+ Ajouter un mois", "Impossible d'ajouter un mois supplémentaire",
			"Remplissez les mois précedents"};
	
	private static final int NUMBER_PREVENTIVE_MONTH_ALLOWED = 1000;

	private static final int NUMBER_FREE_TREE_ALLOWED = 30;
	
	private static final int NUMBER_METER_ALLOWED = 30;
	
	private static final int NUMBER_INTERVENTION_DEMAND_ALLOWED = 100;
	
	private int positionCounter;
	private int preventiveVoucherLastMonthPosition;
	private int positionMoisDI;
	private int freeTrees1Position;
	private int freeTrees2Position;
	private int freeTreesCompteurPosition;
	
	public Formulaire() throws IOException{
	    // Lien vers ce formulaire pour l'affichage de fenetre d'information
		
		JPanel fenetre = new JPanel(); //creation de la fenetre principale
		
		Formulaire mainFrame = this;
		
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
	    
	    JFormattedTextField textFieldTelRedac = null;
	    try{
			MaskFormatter maskTel  = new MaskFormatter("## ## ## ## ##"); //masque pour le format du numero de telephone
			textFieldTelRedac = new JFormattedTextField(maskTel); //initialisation de la zone de texte tel formattee par le masque
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
	    
	    JFormattedTextField textFieldCodePostal = null;
	    try{
			MaskFormatter maskCodePostal  = new MaskFormatter("## ###"); //masque pour le format du code postal
			textFieldCodePostal = new JFormattedTextField(maskCodePostal); //initialisation de la zone de texte codePostal formattee par le masque
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
	   
	    JFormattedTextField textFieldTelCl = null;
	    try{
			MaskFormatter maskTelCl  = new MaskFormatter("## ## ## ## ##"); //masque pour le format du numero de telephone
			textFieldTelCl = new JFormattedTextField(maskTelCl); //initialisation de la zone de texte telCl formattee par le masque
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
		JComboBox<String> comboBoxRapport = new JComboBox<String>(choixRapport); //initialisation du comboBox comboBoxRapport avec la liste choixRapport
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
	    
	    JFormattedTextField textFieldDateDebut = null;
	    try{
			MaskFormatter maskDate  = new MaskFormatter("##/##/####"); //masque pour le format date
			textFieldDateDebut = new JFormattedTextField(maskDate); //initialisation de la zone de texte textFieldDateDebut formattee par le masque maskDate
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
		constraint.insets = new Insets(3, 0, 3, 0); //marges autour de l'element
		conteneurPrincipal.add(titreBP, constraint); //ajout du titreBP dans conteneurPrincipal
	    
	    Collection<JComboBox<String>> preventivesVouchersMonths        = new ArrayList<JComboBox<String>>();
	    Collection<JTextField>        nbPreventivesVouchersOpened      = new ArrayList<JTextField>();
	    Collection<JTextField>        nbPreventivesVouchersClosed      = new ArrayList<JTextField>();
	    Collection<JTextArea>         commentsPreventivesVouchers      = new ArrayList<JTextArea>();

	    preventiveVoucherLastMonthPosition = positionCounter;
	    final int preventiveVoucherFirstMonthPosition = positionCounter;
	    positionCounter += NUMBER_PREVENTIVE_MONTH_ALLOWED;
	    
	    ajoutMoisBP = new JButton(ADD_MONTH_BUTTON_TEXT[0]);
		
		ajoutMoisBP.addActionListener(new ActionListener() {
			    	
			public void actionPerformed(ActionEvent arg0) {
				
				if (preventiveVoucherLastMonthPosition >= preventiveVoucherFirstMonthPosition + NUMBER_PREVENTIVE_MONTH_ALLOWED) {
					JOptionPane.showMessageDialog(conteneurPrincipal, 
		    				"Impossible d'ajouter un mois supplémentaire dans la partie " + preventiveVoucherMonthLabels[0], "Erreur", 
							JOptionPane.WARNING_MESSAGE);
					ajoutMoisBP.setEnabled(false);
					ajoutMoisBP.setText(ADD_MONTH_BUTTON_TEXT[1]);
					return;
				}
				else {
					ajoutMoisBP.setEnabled(true);
					ajoutMoisBP.setText(ADD_MONTH_BUTTON_TEXT[0]);
				}
				
				JPanel preventiveVoucherMonth = createPreventiveVoucherMonth(conteneurPrincipal, 
						preventivesVouchersMonths, nbPreventivesVouchersOpened, 
						nbPreventivesVouchersClosed, commentsPreventivesVouchers);
				
				preventiveVoucherMonth.setBorder(BorderFactory.createTitledBorder("Bon préventif"));
				constraint.gridy = ++preventiveVoucherLastMonthPosition;
				constraint.gridwidth = GridBagConstraints.REMAINDER;
				constraint.insets = new Insets(10, 0, 3, 0); //marges autour de l'element
				conteneurPrincipal.add(preventiveVoucherMonth, constraint);
				
				conteneurPrincipal.revalidate();
				
				ajoutMoisBP.setText(ADD_MONTH_BUTTON_TEXT[2]);
				ajoutMoisBP.setEnabled(false);
			}
		});	 
	    
	    constraint.gridx = 1;
		constraint.gridy = positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(10, 0, 3, 0); //marges autour de l'element
		conteneurPrincipal.add(ajoutMoisBP, constraint); //ajout du bouton ajoutElement
	    
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
	    
	    freeTrees1Position = ++positionCounter;
	    
	    positionCounter += NUMBER_FREE_TREE_ALLOWED;
	    
	    Collection<FreeTree> freeTrees1 = new LinkedList<FreeTree>();
	    
	    //bouton d'ajout d'arborescence libre
	  	JButton ajoutArboLibre = new JButton("+ Ajouter une arborescence libre");
	  	ajoutArboLibre.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				constraint.gridx = 0;
				constraint.gridy = ++freeTrees1Position;
				constraint.insets = new Insets(0, 0, 0, 0); //marges autour de l'element
			    constraint.gridwidth = 4;
			    FreeTree arboLibre = new FreeTree(conteneurPrincipal, freeTrees1Position);
			    freeTrees1.add(arboLibre);
				conteneurPrincipal.add(arboLibre, constraint);
				
				conteneurPrincipal.revalidate();
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
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreDI, constraint); //ajout du titreRapportr dans conteneurPrincipal
		
		//bouton d'ajout de mois pour les BP
		positionMoisDI = ++positionCounter;
		positionCounter += NUMBER_INTERVENTION_DEMAND_ALLOWED;
		
		Collection<JComboBox<String>> interventionMonths = new LinkedList<JComboBox<String>>();
		Collection<JTextField> interventionNumbers = new LinkedList<JTextField>();
				
		JButton ajoutMoisDI = new JButton("+ Ajouter un mois");
				
		ajoutMoisDI.addActionListener(new ActionListener() {
					    	
			public void actionPerformed(ActionEvent arg0) {	
				
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
				
				JTextField textFieldNbIntervention = new JTextField(2); //creation de la zone de texte textFieldNbBPOuverts
				nbIntervention.setLabelFor(textFieldNbIntervention); //attribution de la zone de texte au label nbBPOuverts
				interventionNumbers.add(textFieldNbIntervention);
				
				interventionDemandConstraint.gridx = 1;
				interventionDemandConstraint.gridwidth = GridBagConstraints.REMAINDER;
				interventionDemand.add(textFieldNbIntervention, interventionDemandConstraint); //ajout de la zone de texte textFieldNbBPOuverts
				
				JButton deleteInterventionDemand = new JButton("- Supprimer mois");
				
				deleteInterventionDemand.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						interventionMonths.remove(comboBoxMoisDI);
						interventionNumbers.remove(textFieldNbIntervention);
						
						--positionMoisDI;
						
						conteneurPrincipal.remove(interventionDemand);
						conteneurPrincipal.revalidate();
					}
				});
				
				interventionDemandConstraint.gridx = 0;
				interventionDemandConstraint.gridy = ++currentPositionCounter;
				interventionDemandConstraint.gridwidth = 1;
				interventionDemand.add(deleteInterventionDemand, interventionDemandConstraint);
				
				constraint.gridx = 0;
				constraint.gridy = ++positionMoisDI;
				constraint.gridwidth = GridBagConstraints.REMAINDER;
				constraint.insets = new Insets(7, 7, 0, 7); //marges autour de l'element
				conteneurPrincipal.add(interventionDemand, constraint);
				
				conteneurPrincipal.revalidate();
			}
		});
		
		constraint.gridx = 1;	
		constraint.gridy = ++positionCounter;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		conteneurPrincipal.add(ajoutMoisDI, constraint); //ajout du bouton ajoutElement
		
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
	    
	    titreArboLibre = new JLabel("Arborescence libre"); //titre de la parte rapport du formulaire
		
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(titreArboLibre, constraint); //ajout du titreRapportr dans conteneurPrincipal
	    
	    freeTrees2Position += positionCounter;
	    
	    positionCounter += NUMBER_FREE_TREE_ALLOWED;
	    
	    Collection<FreeTree> freeTrees2 = new LinkedList<FreeTree>();
	    
	    //bouton d'ajout d'arborescence libre
	  	JButton ajoutArboLibre2 = new JButton("+ Ajouter une arborescence libre");
	  	
	  	ajoutArboLibre2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				FreeTree freeTree2 = new FreeTree(conteneurPrincipal, freeTrees2Position);
				
				freeTrees2.add(freeTree2);
				
				constraint.gridx = 0;
				constraint.gridy = ++freeTrees2Position;
				constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
				constraint.gridwidth = GridBagConstraints.REMAINDER;
				conteneurPrincipal.add(freeTree2, constraint);
				
				conteneurPrincipal.revalidate();
			}
		});
	  	
	    
	  	constraint.gridx = 1;
	  	constraint.gridy = ++positionCounter;
	  	constraint.gridwidth = GridBagConstraints.REMAINDER;
	  	conteneurPrincipal.add(ajoutArboLibre2, constraint); //ajout du bouton ajoutArboLibre
	    
	    /*----------------------------------------------formulaire compteurs----------------------------------------------------------*/
	    
	  	JLabel meterTitle = new JLabel("Compteurs"); //titre de la parte rapport du formulaire
	  	meterTitle.setFont(new Font("Arial",Font.BOLD,14)); //police + taille titreCompteurs
	  	
		constraint.gridx = 0;
		constraint.gridy = ++positionCounter;
		constraint.insets = new Insets(20, 0, 5, 0); //marges autour de l'element
	    conteneurPrincipal.add(meterTitle, constraint); //ajout du titreRapportr dans conteneurPrincipal
	    
	    Collection<Meter> meters = new LinkedList<Meter>();
	    
	  	freeTreesCompteurPosition = ++positionCounter;
    
	  	positionCounter += NUMBER_METER_ALLOWED;
    
	  	//bouton d'ajout d'arborescence libre
	  	JButton ajoutCompteur = new JButton("+ Ajouter un compteur");
  	
	  	ajoutCompteur.addActionListener(new ActionListener() {
	  		@Override
	  		public void actionPerformed(ActionEvent e) {
	  			
	  			Meter meter = new Meter(conteneurPrincipal, freeTreesCompteurPosition, meters);
	  			meter.setBorder(BorderFactory.createTitledBorder("Compteur"));
	  			
	  			meters.add(meter);
	  			
	  			constraint.gridx = 0;
				constraint.gridy = ++freeTreesCompteurPosition;
				constraint.insets = new Insets(15, 0, 5, 0); //marges autour de l'element
				constraint.gridwidth = GridBagConstraints.REMAINDER;
				conteneurPrincipal.add(meter, constraint);
			
				conteneurPrincipal.revalidate();
	  		}
	  	});
	  	
    
	  	constraint.gridx = 1;
	  	constraint.gridy = ++positionCounter;
	  	constraint.gridwidth = 1;
	  	conteneurPrincipal.add(ajoutCompteur, constraint); //ajout du bouton ajoutCompteur
		
		/*-----------------------------------------Bouton de validation du formulaire--------------------------------------------------- */
		
		JButton valideForm = new JButton("Génerer le rapport"); //bouton de validation du formulaire 
		//valideForm.setBackground(new Color(224, 35, 60));
		
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
		    	
		    	ProgressBarFrame pBarFrame = new ProgressBarFrame();
		    	
		    	SwingWorker<Void, Integer> pdfCreation = new SwingWorker<Void, Integer>() {
										
					@Override
					protected Void doInBackground() throws Exception {
						
						mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				    	mainFrame.setEnabled(false);
				    	
				    	final int incrementUnit = 5;
				    	
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
				    	
				    	if (finalTextFieldTelRedac.getText().equals("")) {
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
				    		writerPart.addString(textFieldNomCA.getText(),    nomCA.getText()); // nom du chage d'affaire
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
				    	
				    	if (finalTtextFieldCodePostal.getText().equals("")) {
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
				    		clientPart.addString(textFieldVille.getText(),      ville.getText());      // ville
				    	}
				    	
				    	if (finalTextFieldTelCl.getText().equals("")) {
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
				    		clientPart.addString(textFieldEmailCl.getText(),    emailCl.getText());    // email client
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
				    	Iterator<JTextArea>         commentsPreventivesVouchersIter = commentsPreventivesVouchers.iterator();
				    	
				    	int counter = 1;
				    	
				    	DefaultCategoryDataset barChartDatas = new DefaultCategoryDataset();
				    	IChartGenerator chartGenerator = new DefaultChartGenerator();
				    	
				    	while (preventivesVouchersMonthsIter.hasNext()) {
				    		
				    		JComboBox<String> comboBoxMoisBP        = preventivesVouchersMonthsIter.next();
				    		JTextField        textFieldNbBPOuverts  = nbPreventivesVouchersOpenedIter.next();
				    		JTextField        textFieldNbBPFermes   = nbPreventivesVouchersClosedIter.next();
				    		JTextArea         textAreaCommentaireBP = commentsPreventivesVouchersIter.next();
		 
							if (counter <= 1) {
				    		
					    		preventivesVouchers.addString(comboBoxMoisBP.getSelectedItem().toString(), preventiveVoucherMonthLabels[0]);
								
						    	if (textFieldNbBPOuverts.getText().equals("")) {
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" +  preventiveVoucherMonthLabels[1] + "\" de la partie " + titreBP.getText() + 
						    				" du mois numéro " + counter + " doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else if (!OperationUtilities.isNumeric(textFieldNbBPOuverts.getText())) {
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" + preventiveVoucherMonthLabels[1] + "\" de la partie " + titreBP.getText() + 
						    				" du mois numéro " + counter + " doit être un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else {
									preventivesVouchers.addString(textFieldNbBPOuverts.getText(), preventiveVoucherMonthLabels[1]);
									
									barChartDatas.addValue(Double.parseDouble(textFieldNbBPOuverts.getText()), "Nombre de bons préventifs ouverts", comboBoxMoisBP.getSelectedItem().toString());
						    	}
						    	
						    	if (textFieldNbBPFermes.getText().equals("")) {
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" + preventiveVoucherMonthLabels[2] + "\" de la partie " + titreBP.getText() +
						    				" du mois numéro " + counter + " doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else if (!OperationUtilities.isNumeric(textFieldNbBPFermes.getText())) {
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" + preventiveVoucherMonthLabels[2] + "\" de la partie " + titreBP.getText() +
						    				" du mois numéro " + counter + " doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else {
						    		preventivesVouchers.addString(textFieldNbBPFermes.getText(), preventiveVoucherMonthLabels[2]);
						    		barChartDatas.addValue(Double.parseDouble(textFieldNbBPFermes.getText()), "Nombre de bons préventifs clôturés", comboBoxMoisBP.getSelectedItem().toString());
						    	}
						    	
						    	if (!textAreaCommentaireBP.getText().equals("")) {
									preventivesVouchers.addString(textAreaCommentaireBP.getText(), preventiveVoucherMonthLabels[3]);
									stopPdfCreation(pBarFrame);
								}
						    	
						    	++counter;
							}
							else if (!textFieldNbBPOuverts.getText().equals("") ||
									!textAreaCommentaireBP.getText().equals("") ||
									!textFieldNbBPFermes.getText().equals("")) {
								
								preventivesVouchers.addString(comboBoxMoisBP.getSelectedItem().toString(), preventiveVoucherMonthLabels[0]);
								
						    	if (textFieldNbBPOuverts.getText().equals("")) {
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" +  preventiveVoucherMonthLabels[1] + "\" de la partie " + titreBP.getText() + 
						    				" du mois numéro " + counter + " doit être remplis avec un nombre "
						    						+ "(Les bons préventifs completement vides seront ignorés)", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else if (!OperationUtilities.isNumeric(textFieldNbBPOuverts.getText())) {
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" + preventiveVoucherMonthLabels[1] + "\" de la partie " + titreBP.getText() + 
						    				" du mois numéro " + counter + " doit être un nombre"
						    						+ " (Les bons préventifs completement vides seront ignorés)", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else {
									preventivesVouchers.addString(textFieldNbBPOuverts.getText(), preventiveVoucherMonthLabels[1]);
									barChartDatas.addValue(Double.parseDouble(textFieldNbBPOuverts.getText()), "Nombre de bons préventifs ouverts", 
											comboBoxMoisBP.getSelectedItem().toString());
						    	}
						    	
						    	if (textFieldNbBPFermes.getText().equals("")) {
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" + preventiveVoucherMonthLabels[2] + "\" de la partie " + titreBP.getText() +
						    				" du mois numéro " + counter + " doit être remplis avec un nombre "
						    						+ "(Les bons préventifs completement vides seront ignorés)", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else if (!OperationUtilities.isNumeric(textFieldNbBPFermes.getText())) {
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" + preventiveVoucherMonthLabels[2] + "\" de la partie " + titreBP.getText() +
						    				" du mois numéro " + counter + " doit être remplis avec un nombre "
						    						+ "(Les bons préventifs completement vides seront ignorés)", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
						    	else {
						    		preventivesVouchers.addString(textFieldNbBPFermes.getText(), preventiveVoucherMonthLabels[2]);
						    		barChartDatas.addValue(Double.parseDouble(textFieldNbBPFermes.getText()), "Nombre de bons préventifs clôturés", 
						    				comboBoxMoisBP.getSelectedItem().toString());
						    	}
						    	
						    	if (!textAreaCommentaireBP.getText().equals("")) {
									preventivesVouchers.addString(textAreaCommentaireBP.getText(), preventiveVoucherMonthLabels[3]);
									stopPdfCreation(pBarFrame);
								}
						    	
						    	++counter;
							}
				    	}
				    	
				    	if (!preventivesVouchers.isEmpty()) {
					    	try {
								JFreeChart barChart = chartGenerator.generateBarChart(titreBP.getText(), 
										"Mois", "Nombre de bons préventifs", barChartDatas, true);
								
								preventivesVouchers.addJFreeChart(barChart);
				    		} 
				    		catch (Exception e) {
				    			e.printStackTrace();
								JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en bare dans la partie"
										+ titreBP.getText() + " : \n"
										+ e.getMessage(), "Erreur", 
										JOptionPane.WARNING_MESSAGE);
							}
					    	
					    	datas.add(preventivesVouchers);
				    	}			    	
				    	
						publish(pBarFrame.getProgress() + incrementUnit);
				    
				    	/*-----------------Partie Bons preventifs par domaines-----------------*/
				    	
				    	IDataHandler domainPreventivesVouchers = new DefaultDataHandler(titreBPDomaine.getText());
				    	
				    	// On obtient l'iterator des domaines
				    	Iterator<JCheckBox> domainsIter = domainesBP.iterator();
				    	
				    	// On obtient l'iterator des pourcentages correspondant
				    	Iterator<JFormattedTextField> pourcentsIter = textFieldPourcentsBP.iterator();
				    	
				    	barChartDatas = new DefaultCategoryDataset();
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
					    			JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"" + currentDomain.getText() + "\" de la partie " + titreBPDomaine.getText() + 
						    				" doit être remplis si la case a été cochée", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
					    			stopPdfCreation(pBarFrame);
					    			return null;
				    			}
				    			else {		    				
				    				domainPreventivesVouchers.addString(currentPourcent.getText(), currentDomain.getText() + " : ");
				    				
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
				    				
				    				barChartDatas.addValue(currentPourcentage, "Pourcentage d'avancement", currentDomain.getText());
				    				
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
				    	
				    	if (!domainPreventivesVouchers.isEmpty()) {
				    		
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
				    		
				    		try {
								JFreeChart barChart = chartGenerator.generateBarChart(titreBPDomaine.getText(), 
										"Domaines techniques", "Pourcentages d'avancement", barChartDatas, false);
								
								domainPreventivesVouchers.addJFreeChart(barChart);
				    		} 
				    		catch (Exception e) {
				    			e.printStackTrace();
								JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en bare dans la partie"
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
				    	
				    	Iterator<FreeTree> freeTrees1Iter = freeTrees1.iterator();
				    	
				    	while (freeTrees1Iter.hasNext()) {
				    		Boolean isElement = false;
				    		
				    		FreeTree currentTree = freeTrees1Iter.next();
				    		
				    		if (currentTree.titleTextField().getText().equals("")) {
				    			JOptionPane.showMessageDialog(fenetre, 
					    				"le champs \"Titre : \" de la partie Arborescence Libre" +
					    				" doit être remplis", "Erreur", 
										JOptionPane.WARNING_MESSAGE);
					    		stopPdfCreation(pBarFrame);
					    		return null;
				    		}
				    		
				    		IDataHandler freeTree1 = new DefaultDataHandler(currentTree.titleTextField().getText());
				    		
				    		barChartDatas = new DefaultCategoryDataset();
				    		
				    		Iterator<JTextField> currentTreeElementIter = currentTree.elements().iterator();
				    		Iterator<JTextField> currentTreeElementNumber = currentTree.elementNumbers().iterator();
				    		
				    		while (currentTreeElementIter.hasNext()) {
				    			JTextField currentElement = currentTreeElementIter.next();
				    			JTextField currentElementNumber = currentTreeElementNumber.next();
				    			
				    			if (currentElement.getText().equals("")) {
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"Elément : \" de la partie " + currentTree.titleTextField().getText() + 
						    				" doit être remplis", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
				    			else if (currentElementNumber.getText().equals("") || !OperationUtilities.isNumeric(currentElementNumber.getText())) {
				    				JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"Nombre : \" de l'élément " + currentElement.getText() + 
						    				" de la partie " + currentTree.titleTextField().getText() + 
						    				" doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
				    			}
				    			else {
				    				freeTree1.addString(currentElementNumber.getText(), currentElement.getText() + " : ");
				    				barChartDatas.addValue(Double.parseDouble(currentElementNumber.getText()), "Nombre", currentElement.getText());
				    				
				    				isElement = true;
				    			}
				    		}
				    		
				    		if (!currentTree.textAreaComment().getText().equals("")) {
				    			freeTree1.addString(currentTree.textAreaComment().getText(), "Commentaire : ");
				    		}
				    		
				    		if (isElement) {
					    		try {
									JFreeChart barchart = chartGenerator.generateBarChart(currentTree.titleTextField().getText(),
											"Element", "Nombre", barChartDatas, true);
									
									freeTree1.addJFreeChart(barchart);
								} 
					    		catch (Exception e) {
					    			e.printStackTrace();
									JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en barre dans la partie " +
											currentTree.titleTextField().getText() + ": \n"
											+ e.getMessage(), "Erreur", 
											JOptionPane.WARNING_MESSAGE);
								}
				    		}
				    		
				    		datas.add(freeTree1);
				    	}
				    	
						publish(pBarFrame.getProgress() + incrementUnit);
				
				    	/*-----------------Partie demande d'intervention-----------------*/
				    	
				    	IDataHandler interventionDemand = new DefaultDataHandler(titreDI.getText());
				    	
				    	barChartDatas = new DefaultCategoryDataset();
				    	
				    	Iterator<JComboBox<String>> interventionMonthsIter = interventionMonths.iterator();
				    	Iterator<JTextField> interventionNumbersIter = interventionNumbers.iterator();
				    	
				    	while (interventionMonthsIter.hasNext()) {
				    		JComboBox<String> currentInterventionMonths = interventionMonthsIter.next();
				    		JTextField currentInterventionNumber = interventionNumbersIter.next();
				    		
				    		interventionDemand.addString(currentInterventionMonths.getSelectedItem().toString(), "Mois : ");
					    	
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
					    		interventionDemand.addString(currentInterventionNumber.getText(), "Nombre d'interventions : ");
					    		barChartDatas.addValue(Double.parseDouble(currentInterventionNumber.getText()), "Nombre", "Mois");
					    	}
					    	
					    	if (!textAreaCommentaireDI.getText().equals("")) {
					    		interventionDemand.addString(textAreaCommentaireDI.getText(), commentaireDI.getText());
					    	}
					    	
					    	if (!interventionDemand.isEmpty()) {
						    	try {
									JFreeChart barChart = chartGenerator.generateBarChart(titreDI.getText(), 
											"Mois", "Nombre d'intervention", barChartDatas, true);
									
									interventionDemand.addJFreeChart(barChart);
					    		} 
					    		catch (Exception e) {
					    			e.printStackTrace();
									JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en bare dans la partie"
											+ titreDI.getText() + " : \n"
											+ e.getMessage(), "Erreur", 
											JOptionPane.WARNING_MESSAGE);
								}
					    	}
					    	
					    	datas.add(interventionDemand);
				    	}
				    	
						publish(pBarFrame.getProgress() + incrementUnit);
				    	
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
				    				stateInterventionDemand.addString(currentNbState.getText(), currentState.getText() + " : ");
				    				
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
				    	
				    	if (!stateInterventionDemand.isEmpty()) {
				    		
				    		Iterator<Entry<String, Double>> mapIter = pourcentages.entrySet().iterator();
				    		
				    		while (mapIter.hasNext()) {
				    			Map.Entry<String, Double> currentEntry = mapIter.next();
				    			
				    			double currentRelativePourcentage = currentEntry.getValue() / total;
				    			pieChartDatas.setValue(currentEntry.getKey() + " : " + 
				    					OperationUtilities.truncateDecimal(currentRelativePourcentage * 100, 2) + "%",
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
				    				domainInterventionDemand.addString(currentPourcent.getText(), currentDomain.getText() + " : ");
				    				
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
				    	
				    	if (!domainInterventionDemand.isEmpty()) {
				    		
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
				    			interventionPieChartDatas.setValue(currentEntry.getKey() + " : " + OperationUtilities.truncateDecimal(currentRelativePourcentage * 100, 2) + "%",
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
				    	
				    	Iterator<FreeTree> freeTrees2Iter = freeTrees2.iterator();
				    	
				    	while (freeTrees2Iter.hasNext()) {
				    		Boolean isThereElements = false;
				    		
				    		FreeTree currentTree = freeTrees2Iter.next();
				    		
				    		if (currentTree.titleTextField().getText().equals("")) {
				    			JOptionPane.showMessageDialog(fenetre, 
					    				"le champs \"Titre : \" de la partie Arborescence Libre" +
					    				" doit être remplis", "Erreur", 
										JOptionPane.WARNING_MESSAGE);
					    		stopPdfCreation(pBarFrame);
					    		return null;
				    		}
				    		
				    		IDataHandler freeTree = new DefaultDataHandler(currentTree.titleTextField().getText());
				    		
				    		barChartDatas = new DefaultCategoryDataset();
				    		
				    		Iterator<JTextField> currentTreeElementIter = currentTree.elements().iterator();
				    		Iterator<JTextField> currentTreeElementNumber = currentTree.elementNumbers().iterator();
				    		
				    		while (currentTreeElementIter.hasNext()) {
				    			JTextField currentElement = currentTreeElementIter.next();
				    			JTextField currentElementNumber = currentTreeElementNumber.next();
				    			
				    			if (currentElement.getText().equals("")) {
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"Elément : \" de la partie " + currentTree.titleTextField().getText() + 
						    				" doit être remplis", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
				    			else if (currentElementNumber.getText().equals("") || 
				    					!OperationUtilities.isNumeric(currentElementNumber.getText())) {
				    				JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"Nombre : \" de l'élément " + currentElement.getText() + 
						    				" de la partie " + currentTree.titleTextField().getText() + 
						    				" doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
				    			}
				    			else {
				    				freeTree.addString(currentElementNumber.getText(), currentElement.getText() + " : ");
				    				barChartDatas.addValue(Double.parseDouble(currentElementNumber.getText()), "Nombre", 
				    						currentElement.getText());
				    				
				    				isThereElements = true;
				    			}
				    		}
				    		
				    		if (!currentTree.textAreaComment().getText().equals("")) {
				    			freeTree.addString(currentTree.textAreaComment().getText(), "Commentaire : ");
				    		}
				    		
				    		if (isThereElements) {
					    		try {
									JFreeChart barchart = chartGenerator.generateBarChart(currentTree.titleTextField().getText(),
											"Element", "Nombre", barChartDatas, true);
									
									freeTree.addJFreeChart(barchart);
								} 
					    		catch (Exception e) {
					    			e.printStackTrace();
									JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en barre dans la partie " +
											currentTree.titleTextField().getText() + ": \n"
											+ e.getMessage(), "Erreur", 
											JOptionPane.WARNING_MESSAGE);
								}
				    		}
				    		
				    		datas.add(freeTree);
				    	}
				    	
						publish(pBarFrame.getProgress() + incrementUnit);
						
						/*-----------------Partie compteurs-----------------*/
						
						Iterator<Meter> meterIter = meters.iterator();
						
						while (meterIter.hasNext()) {
							
							IDataHandler currentMeterData = new DefaultDataHandler(meterTitle.getText());
							
							barChartDatas = new DefaultCategoryDataset();
							
							Boolean isThereMonths = false;
							
							Meter currentMeter = meterIter.next();
							
							currentMeterData.addString(currentMeter.comboBoxTypeCompteur().getSelectedItem().toString(),
									"Type du compteur : ");
							
							Iterator<JComboBox<String>> currentMonthIter = currentMeter.monthComboBoxes().iterator();
							Iterator<JTextField> currentConsumptionIter = currentMeter.monthConsumptions().iterator();
							Iterator<String> currentUnitIter = currentMeter.monthUnits().iterator();
							
							while (currentMonthIter.hasNext()) {
								
								isThereMonths = true;
								
								JComboBox<String> currentMonth = currentMonthIter.next();
								JTextField currentConsumption = currentConsumptionIter.next();
								String currentUnit = currentUnitIter.next();
								
								if (currentConsumption.getText().equals("")) {
						    		JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"Consommation : \" de la partie " + 
						    				" Compteur doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
						    	}
				    			else if (currentConsumption.getText().equals("") || 
				    					!OperationUtilities.isNumeric(currentConsumption.getText())) {
				    				JOptionPane.showMessageDialog(fenetre, 
						    				"le champs \"Consommation : \" (" + currentConsumption.getText() + 
						    				") de la partie Compteur " + 
						    				" doit être remplis avec un nombre", "Erreur", 
											JOptionPane.WARNING_MESSAGE);
						    		stopPdfCreation(pBarFrame);
						    		return null;
				    			}
				    			else {
				    				currentMeterData.addString(currentConsumption.getText() + " " + currentUnit,
				    						currentMonth.getSelectedItem().toString() + " : ");
				    				
				    				barChartDatas.addValue(Double.parseDouble(currentConsumption.getText()), currentUnit, 
				    						currentMonth.getSelectedItem().toString());
				    			}
								
							}
							
							if (!currentMeter.textAreaCommentaire().getText().equals("")) {
								currentMeterData.addString(currentMeter.textAreaCommentaire().getText(), "Commentaire : ");
							}
								
							if (isThereMonths) {
					    		try {
									JFreeChart barchart = chartGenerator.generateBarChart("Compteur",
											"Mois", "Consommation", barChartDatas, true);
									
									currentMeterData.addJFreeChart(barchart);
								} 
					    		catch (Exception e) {
					    			e.printStackTrace();
									JOptionPane.showMessageDialog(fenetre, "Erreur lors de la création du graphe en"
											+ " barre dans la partie Compteur : \n"
											+ e.getMessage(), "Erreur", 
											JOptionPane.WARNING_MESSAGE);
								}
					    		
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
			            mainFrame.setEnabled(true);
			            mainFrame.setCursor(null); //turn off the wait cursor
			        }

				};
				
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

	private JPanel createPreventiveVoucherMonth (JComponent mainContainer, Collection<JComboBox<String>> preventivesVouchersMonths, 
			Collection<JTextField> nbPreventivesVouchersOpened, Collection<JTextField> nbPreventivesVouchersClosed,
			Collection<JTextArea> commentsPreventivesVouchers) {
		
		JPanel thisPreventiveVoucherMonthPanel = new JPanel (new GridBagLayout());
		
		int preventiveVoucherMonthPosition = 0;
		
		GridBagConstraints constraint = new GridBagConstraints();
		
		constraint.insets = new Insets(0, 0, 3, 0); //marges autour de l'element
		
		constraint.gridx = 0;
		constraint.gridy = preventiveVoucherMonthPosition;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.weightx = 1;
		constraint.fill = GridBagConstraints.BOTH;
		
		JLabel preventivVoucherMonthJLabel = new JLabel (preventiveVoucherMonthLabels[0]);
		
		thisPreventiveVoucherMonthPanel.add(preventivVoucherMonthJLabel, constraint); //ajout du label moisBP
			
		JComboBox<String> comboBoxMoisBP = new JComboBox<String>(MONTH_CHOICE); //initialisation de la comboBox comboBoxMoisBP avec la liste choixMois
		comboBoxMoisBP.setPreferredSize(new Dimension(100, 20)); //dimension de la comboBoxMoisBP
		preventivesVouchersMonths.add(comboBoxMoisBP);
		
		constraint.gridx = 1;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		preventivVoucherMonthJLabel.setLabelFor(comboBoxMoisBP); //attribution de la comboBox comboBoxMoisBP au label moisBP
		thisPreventiveVoucherMonthPanel.add(comboBoxMoisBP, constraint); //ajout de la zone de texte comboBox comboBoxMoisBP
		
	    //nombre BP ouverts
	    JLabel nbBPOuverts = new JLabel(preventiveVoucherMonthLabels[1]); //creation du label nbBPOuverts
	    constraint.gridx = 0;
	    constraint.gridy = ++preventiveVoucherMonthPosition;
	    constraint.gridwidth = 1;
	    thisPreventiveVoucherMonthPanel.add(nbBPOuverts, constraint); //ajout du label nbBPOuverts
	    
	    JTextField textFieldNbBPOuverts = new JTextField(2); //creation de la zone de texte textFieldNbBPOuverts
	    nbBPOuverts.setLabelFor(textFieldNbBPOuverts); //attribution de la zone de texte au label nbBPOuverts
	    nbPreventivesVouchersOpened.add(textFieldNbBPOuverts);
		
	    constraint.gridx = 1;
	    constraint.gridwidth = GridBagConstraints.REMAINDER;
	    thisPreventiveVoucherMonthPanel.add(textFieldNbBPOuverts, constraint); //ajout de la zone de texte textFieldNbBPOuverts
		
		//nombre BP fermes
	    JLabel nbBPFermes = new JLabel(preventiveVoucherMonthLabels[2]); //creation du label nbBPFermes
		
	    constraint.gridx = 0;
	    constraint.gridy = ++preventiveVoucherMonthPosition;
	    constraint.gridwidth = 1;
	    thisPreventiveVoucherMonthPanel.add(nbBPFermes, constraint); //ajout du label nbBPFermes
		
	    JTextField textFieldNbBPFermes = new JTextField(2); //creation de la zone de texte textFieldNbBPFermes
	    nbBPFermes.setLabelFor(textFieldNbBPFermes); //attribution de la zone de texte textFieldNbBPFermes au label nbBPFermes
	    nbPreventivesVouchersClosed.add(textFieldNbBPFermes);
		
	    constraint.gridx = 1;
	    constraint.gridwidth = GridBagConstraints.REMAINDER;
	    thisPreventiveVoucherMonthPanel.add(textFieldNbBPFermes, constraint); //ajout de la zone de texte textFieldNbBPFermes
		
		textFieldNbBPOuverts.getDocument().addDocumentListener(new PersonnalDocumentListener() {
			
			@Override
			public void update(DocumentEvent arg0) {
				if (textFieldNbBPOuverts.getText().equals("") || textFieldNbBPFermes.getText().equals("")) {
					ajoutMoisBP.setEnabled(false);
					ajoutMoisBP.setText(ADD_MONTH_BUTTON_TEXT[2]);
				}
				else {
					ajoutMoisBP.setEnabled(true);
					ajoutMoisBP.setText(ADD_MONTH_BUTTON_TEXT[0]);
				}
			}
		});
		
		textFieldNbBPFermes.getDocument().addDocumentListener(new PersonnalDocumentListener() {
			
			@Override
			public void update(DocumentEvent arg0) {
				if (textFieldNbBPOuverts.getText().equals("") || textFieldNbBPFermes.getText().equals("")) {
					ajoutMoisBP.setEnabled(false);
					ajoutMoisBP.setText(ADD_MONTH_BUTTON_TEXT[2]);
				}
				else {
					ajoutMoisBP.setEnabled(true);
					ajoutMoisBP.setText(ADD_MONTH_BUTTON_TEXT[0]);
				}
			}
		});
		
		//commentaire BP
	    JLabel commentaireBP = new JLabel(preventiveVoucherMonthLabels[3]); //creation du label commentaireBP
		
	    constraint.gridx = 0;
		constraint.gridy = ++preventiveVoucherMonthPosition;
		constraint.insets = new Insets(10, 7, 0, 7); //marges autour de l'element
		thisPreventiveVoucherMonthPanel.add(commentaireBP, constraint);
	    
		JTextArea textAreaCommentaireBP = new JTextArea(4, 15); //creation de la zone de texte textAreaCommentaireBP
	    JScrollPane scrollPaneComBP = new JScrollPane(textAreaCommentaireBP); //creation de la scrollPane scrollPaneComBP contenant textAreaCommentaireBP
	    commentaireBP.setLabelFor(textAreaCommentaireBP); //attribution de la zone de texte textAreaCommentaireBP au label commentaireBP
	    commentsPreventivesVouchers.add(textAreaCommentaireBP);
	    
		constraint.gridy = ++preventiveVoucherMonthPosition;
		constraint.gridwidth = GridBagConstraints.REMAINDER;
		constraint.insets = new Insets(0, 7, 3, 7); //marges autour de l'element
		thisPreventiveVoucherMonthPanel.add(scrollPaneComBP, constraint); //ajout de la scrollPane scrollPaneComBP
		
		JButton deleteMonthButton = new JButton("Supprimer mois");
		deleteMonthButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {	
				
				preventivesVouchersMonths.remove(comboBoxMoisBP);
				nbPreventivesVouchersOpened.remove(textFieldNbBPOuverts);
				nbPreventivesVouchersClosed.remove(textFieldNbBPFermes);
				commentsPreventivesVouchers.remove(textAreaCommentaireBP);
				
				ajoutMoisBP.setEnabled(true);
				ajoutMoisBP.setText(ADD_MONTH_BUTTON_TEXT[0]);
				
				--preventiveVoucherLastMonthPosition;
				
				mainContainer.remove(thisPreventiveVoucherMonthPanel);
				
				mainContainer.revalidate();
			}
		});
		
		constraint.gridx = 0;
		constraint.gridy = ++preventiveVoucherMonthPosition;
		constraint.gridwidth = 1;
		thisPreventiveVoucherMonthPanel.add(deleteMonthButton, constraint);
		
		return thisPreventiveVoucherMonthPanel;
	}
	
	private void stopPdfCreation(ProgressBarFrame pBFrame) {
		
		this.setCursor(null);
		this.setEnabled(true);
		
		pBFrame.dispose();
	}

}