package documentHandler.writeStrategies;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.ImageIcon;

import org.jfree.chart.JFreeChart;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import dataHandler.CustomerDataHandler;
import dataHandler.IDataHandler;
import ihm.Form;
import ihm.ProgressBarFrame;

/**
 * Cette classe est la strategie d'edition de rapport pour le client
 * @author Lucie PELLOTTIERO
 *
 */
public class CustomerReport implements IWriteStrategie{
	
	/**
	 * Largeur des graphes
	 */
	private int chartWidth;
	/**
	 * Hauteur des graphes
	 */
	private int chartHeight;
	
	/**
	 * Chemin vers le logo de Vinci
	 */
	private static final String LOGO_VINCI_PATH = Form.ICONS_PATH + File.separator + Form.ICONS_NAME[0];
	/**
	 * Logo de Vinci
	 */
	private static Image logoVinci;
	
	/**
	 * Le constructeur par defaut.<br>
	 * {@link DefaultStrategie.chartWidth} = {@value 500}<br>
	 * {@link DefaultStrategie.chartHeight} = {@value 400}
	 */
	public CustomerReport () {
		chartWidth  = 500;
		chartHeight = 400;
		
		try {
			logoVinci = Image.getInstance(LOGO_VINCI_PATH);
		} 
		catch (BadElementException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * En-tete et pied de page
	 * @author Lucie PELLOTTIERO
	 *
	 */
	static class MyHeaderFooter extends PdfPageEventHelper {
		/**
		 * {@link Font} utilisee dans l'en-tete et le pied de page
		 */
        private final Font defaultFont;
        /**
         * {@link Font} coloree utilisee dans le pied de page
         */
        private final Font coloredFooterFont;
        
        /**
         * Contenu d'en-tete
         */
        private final Phrase header1;
        /**
         * Contenu d'en-tete
         */
        private final Phrase header2;
        /**
         * Contenu d'en-tete
         */
        private final Phrase header3;
        
        /**
         * Logo de Vinci utilise dans l'en-tete.
         */
        private final Image VinciLogoHeader;
        
        /**
         * Contenu de pied de page
         */
        private final Phrase footer1;
        /**
         * Contenu de pied de page
         */
        private final Phrase footer2;
        /**
         * Contenu de pied de page
         */
        private final Phrase footer3;
        
        /**
         * Constructeur
         */
        public MyHeaderFooter() {
        	// Initialisation des fields
        	defaultFont = new Font(Font.FontFamily.UNDEFINED, 8, Font.NORMAL);
        	coloredFooterFont = new Font(Font.FontFamily.UNDEFINED, 8, Font.NORMAL, BaseColor.RED);
        	
        	// Contenus d'en-tete
        	header1 = new Phrase("Centre d’Expertises VFPA", defaultFont);
            header2 = new Phrase("Aix en Provence", defaultFont);
            header3 = new Phrase("Support aux Opérations", defaultFont);
            
            VinciLogoHeader = Image.getInstance(logoVinci);
            // Le logo doit etre redimensionne
        	VinciLogoHeader.scaleAbsolute(VinciLogoHeader.getWidth() / 2,
        									VinciLogoHeader.getHeight() / 2);
        	
        	// Contenus de pied de page
        	footer1 = new Phrase("PROVENCE MAINTENANCE SERVICES - Immeuble Le Rubis - 165 av Galilée -"
        			+ " 13 857 Aix En Provence Cedex 3", defaultFont);
        	footer2 = new Phrase("Tél. : 04 42 90 55 80 - Fax. : 04 42 90 55 81  -  www.vinci-facilities.com -"
        			+ " RCS 433 899 788 Aix en Provence – TVA FR 194 433 899 788", defaultFont);
        	footer3 = new Phrase("Ce document est la propriété du Centre d’Expertises VFPA."
        			+ " Toute reproduction est interdite sans autorisations", coloredFooterFont);
        }
        
        @Override
        public void onStartPage(final PdfWriter writer, final Document document) {
        	// Obtention du ContentByte
            PdfContentByte cb = writer.getDirectContent();
            
            // En-tête position
            VinciLogoHeader.setAbsolutePosition(document.left() + document.leftMargin() - 60, 
            		document.top() - document.topMargin() + 43);
            try {
            	// Ajout du logo
				writer.getDirectContent().addImage(VinciLogoHeader);
			} 
            catch (DocumentException e) {
				e.printStackTrace();
			}
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    header1,
                    document.right() - document.rightMargin() - 30,
                    document.top() + 20, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    header2,
                    document.right() - document.rightMargin() - 30,
                    document.top() + 10, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    header3,
                    document.right() - document.rightMargin() -30,
                    document.top() + 0, 0);
        }
        
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
        	
        	// Permet de creer une nouvelle page meme si elle est vide
        	writer.setPageEmpty(false);
            PdfContentByte cb = writer.getDirectContent();
            
            //pied de page position
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer1,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 5, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer2,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 15, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer3,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 30, 0);
        }
    }

	@Override
	public boolean writeDocument(final Collection<IDataHandler> datas, final Document document, final PdfWriter writer, 
		ProgressBarFrame pBFrame) throws Exception {
	
		// Creation de l'en-tête et pied de page
		MyHeaderFooter event = new MyHeaderFooter();
	    writer.setPageEvent(event);
	    
		// On ouvre le document a la modification
		document.open();
		
		// Creation de la font par defaut
	    BaseFont basefont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
		// Creation de la font concrete
	    BaseColor vinciBlue = new BaseColor(10, 39, 117);
	    Font basicFont = new Font (basefont, 12, Font.NORMAL);
	    Font boldBasicFont = new Font (basefont, 12, Font.BOLD);
	    Font boldTitleFont = new Font (basefont, 18, Font.BOLD, vinciBlue);
	    Font basicTitleFont = new Font (basefont, 18, Font.NORMAL);
	    Font mainTitleFont = new Font (basefont, 35, Font.BOLD);
	    Font firstPageFont = new Font (basefont, 26, Font.NORMAL);
	   
	    
		// On cre un Iterator pour les donnees
		Iterator<IDataHandler> datasIterator = datas.iterator();
		
		// Obtention de l'increment de progression
		int counter = pBFrame.getProgress();
		int progressIncrement = ProgressBarFrame.MY_MAXIMUM - counter / datas.size();
		
        IDataHandler currentDataPart = datasIterator.next();
		Iterator<Collection<Object>> currentPartIter = currentDataPart.getDataStorage().iterator();
		
		Iterator<Object> datasTypeIter = currentPartIter.next().iterator();
		Iterator<Object> datasIter     = currentPartIter.next().iterator();
		
		/*-----------------recuperation des données---------------*/
		
		Phrase titreRapport = new Phrase ("Rapport d'activité " + (String)datasIter.next() , firstPageFont);
		String dateDebut = new String((String)datasIter.next());
		String dateFin = new String((String)datasIter.next());
		
		// Passage à la partie suivante : client
		currentDataPart = datasIterator.next();
		currentPartIter = currentDataPart.getDataStorage().iterator();
		datasTypeIter = currentPartIter.next().iterator();
		datasIter     = currentPartIter.next().iterator();
		
		// Donnees client
		String siteClient = new String((String)datasIter.next());
		Phrase nomSiteClient = (new Phrase (siteClient, basicFont));
		Phrase codeClient = (new Phrase ((String)datasIter.next(), basicFont));
		Phrase adresseClient = (new Phrase ((String)datasIter.next(), basicFont));
		Phrase codePostalClient = (new Phrase ((String)datasIter.next(), basicFont));
		Phrase villeClient = (new Phrase ((String)datasIter.next(), basicFont));
		Phrase nomClient = (new Phrase ((String)datasIter.next(), boldBasicFont));
		Phrase telClient = (new Phrase ((String)datasIter.next(), basicFont));
		Phrase emailClient = (new Phrase ((String)datasIter.next(), basicFont));
		
		java.awt.Image logoClient = null;
		if (datasIter.hasNext()) {
			logoClient = (java.awt.Image) datasIter.next();
		}
		
		// Passage a la partie suivante, redacteur
		currentDataPart = datasIterator.next();
		currentPartIter = currentDataPart.getDataStorage().iterator();
		datasTypeIter = currentPartIter.next().iterator();
		datasIter     = currentPartIter.next().iterator();
		
		/*-----------------------------page 1, page de garde------------------------------*/
		
		// Ajout de nouvelles lignes pour se mettre au milieu
		final Paragraph mainTitle = new Paragraph();
		for(int i = 0; i < 12; ++i){
			mainTitle.add(Chunk.NEWLINE);
		}
		mainTitle.setAlignment(Element.ALIGN_CENTER);
		
		
		// Donnees rapport
		Phrase titrePrincipal = new Phrase(siteClient, mainTitleFont);
		mainTitle.add(titrePrincipal);
		mainTitle.add(Chunk.NEWLINE);
		mainTitle.add(Chunk.NEWLINE);
		mainTitle.add(titreRapport);
		mainTitle.add(Chunk.NEWLINE);
		mainTitle.add(Chunk.NEWLINE);
		if (logoClient != null) {
			final ImageIcon resizedCustomerLogo = new ImageIcon(logoClient.
                    getScaledInstance(-1,
                    150,
                    java.awt.Image.SCALE_SMOOTH));
			final Image titleLogo = Image.getInstance(resizedCustomerLogo.getImage(), null);
			titleLogo.setAlignment(Image.ALIGN_CENTER);
			mainTitle.add(titleLogo);
			mainTitle.add(Chunk.NEWLINE);
		}
		for(int i = 0; i < 4; ++i){
			mainTitle.add(Chunk.NEWLINE);
		}
		Phrase date = new Phrase (dateDebut + " au " + dateFin, firstPageFont);
		mainTitle.add(date);
		
		document.add(mainTitle);
		
		/*---------page 2, coordonnees client - redacteur-----------*/
		
		Paragraph para = new Paragraph();
		
		// Nouvelle page
		
		document.newPage();
		para.add(Chunk.NEWLINE);
		para.add(Chunk.NEWLINE);
		para.add(new Phrase ("Pilotage du marché multitechnique", boldTitleFont));
		para.add(Chunk.NEWLINE);
		para.add(Chunk.NEWLINE);
		para.add(Chunk.NEWLINE);
		
		// Paragraphe contenant les donnees client 
		Paragraph paraClient = new Paragraph();
		paraClient.add(nomClient);
		paraClient.add(Chunk.NEWLINE);
		paraClient.add(nomSiteClient);
		paraClient.add(Chunk.NEWLINE);
		paraClient.add(codeClient);
		paraClient.add(Chunk.NEWLINE);
		paraClient.add(adresseClient);
		paraClient.add(Chunk.NEWLINE);
		paraClient.add(codePostalClient);
		paraClient.add(Chunk.NEWLINE);
		paraClient.add(villeClient);
		paraClient.add(Chunk.NEWLINE);
		paraClient.add("Tél : ");
		paraClient.add(telClient);
		paraClient.add(Chunk.NEWLINE);
		paraClient.add(emailClient);
        
		// Paragraphe contenant les donnees redacteur
		Paragraph paraRedac = new Paragraph();
		paraRedac.add(new Phrase ((String)datasIter.next(), boldBasicFont));
		
		paraRedac.add(Chunk.NEWLINE);
		paraRedac.add(new Phrase ((String)datasIter.next(), basicFont));
		
		paraRedac.add(Chunk.NEWLINE);
		paraRedac.add("Tél : ");
		paraRedac.add(new Phrase ((String)datasIter.next(), basicFont));
		
		paraRedac.add(Chunk.NEWLINE);
		paraRedac.add(new Phrase ((String)datasIter.next(), basicFont));
		
		paraRedac.add(Chunk.NEWLINE);
		Chunk chargeAffaire = new Chunk("Chargé d'affaire", basicFont);
		chargeAffaire.setUnderline(0.7f, -2f);
		Chunk espace = new Chunk(" : ", basicFont);
		Phrase chargedAffaire = new Phrase();
		chargedAffaire.add(chargeAffaire);
		chargedAffaire.add(espace);
		paraRedac.add(chargedAffaire);
		
		paraRedac.add(new Phrase((String)datasIter.next(), boldBasicFont));
		
		// Tableau contenant les donnees client et redacteur
		// Tableau de deux colonnes
		PdfPTable table = new PdfPTable(2); 
		// Pas de bordure
		table.getDefaultCell().setBorder(0); 
		
		Paragraph titreClient = new Paragraph ("CLIENT", basicTitleFont);
		titreClient.setAlignment(Element.ALIGN_CENTER);
		Paragraph titrePrestataire = new Paragraph ("PRESTATAIRE", basicTitleFont);
		titrePrestataire.setAlignment(Element.ALIGN_CENTER);
		PdfPCell cell1 = new PdfPCell(titreClient);
		
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setBorder(0);
		
		PdfPCell cell2 = new PdfPCell(titrePrestataire);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setBorder(0);
		table.addCell(cell1);
		table.addCell(cell2);
		if (logoClient != null) {
			table.addCell(Image.getInstance(logoClient, null));
		}
		else {
			PdfPCell cell = new PdfPCell();
			cell.setBorder(0);
			table.addCell(cell);
		}
		
		PdfPCell cell3 = new PdfPCell();
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setBorder(0);
		
		// Redimension du logo de Vinci (ne fonctionne pas comme souhaite)
		/*ImageIcon logoIcon = new ImageIcon(logoVinciPath);
		logoIcon = new ImageIcon(logoIcon.getImage().
                getScaledInstance(90, -1,
                java.awt.Image.SCALE_SMOOTH));
		System.out.println(cell2.getWidth());
		Chunk chunk = new Chunk(Image.getInstance(logoIcon.getImage(), null), 0, 0);*/
		
		cell3.addElement(logoVinci);
		table.addCell(cell3);
		table.addCell(paraClient);
		table.addCell(paraRedac);
		
		para.add(table);
		
		document.add(para);
		
		/*---------------------------------partie preventif-------------------------------------------*/
		// Atteinte de la premiere partie preventive
		counter = 0;
		while (!(currentDataPart.getPartTitle().equals("Bons préventifs") ||
				currentDataPart.getPartTitle().equals("Bons préventifs par domaines") ||
				currentDataPart.getPartTitle().equals("Arborescence Libre")) &&
				datasIterator.hasNext() && counter < 2) {
			
			currentDataPart = datasIterator.next();
			++counter;
		}
		
		// Si on a bien une partie preventive
		if (currentDataPart.getPartTitle().equals("Bons préventifs") ||
				currentDataPart.getPartTitle().equals("Bons préventifs par domaines") ||
				currentDataPart.getPartTitle().equals("Arborescence Libre")) {
			para = new Paragraph();
			
			document.newPage();
			// Nouvelle page, titre preventif
			for(int i = 0; i < 12; ++i){
				para.add(Chunk.NEWLINE);
			}
			Paragraph titrePreventif = new Paragraph("Préventif", mainTitleFont);
			titrePreventif.setAlignment(Element.ALIGN_CENTER);
			para.add(titrePreventif);
			
			String IMG_PREV_PATH = Form.ICONS_PATH + File.separator + "imagePreventif.jpg";
			
			Image imgPrev;
		
			imgPrev = Image.getInstance(IMG_PREV_PATH);
			
			imgPrev.setAbsolutePosition(document.right() - document.left() / 2 - 300,
										document.bottom() + 300);
			
            try {
            	// Ajout du logo
				writer.getDirectContent().addImage(imgPrev);
			} 
            catch (DocumentException e) {
				e.printStackTrace();
			}
			
			document.add(para);
		}
		
		// Partie bon preventif
		if (currentDataPart.getPartTitle().equals("Bons préventifs")) {
		
			currentPartIter = currentDataPart.getDataStorage().iterator();
			datasTypeIter   = currentPartIter.next().iterator();
			datasIter       = currentPartIter.next().iterator();
			
			Paragraph preventivPar = new Paragraph();
			preventivPar.add(Chunk.NEXTPAGE);
			for(int i = 0; i < 4; ++i){
				preventivPar.add(Chunk.NEWLINE);
			}
			
			preventivPar.add(new Phrase("Préventif – Bilan maintenance préventive", boldTitleFont));
			preventivPar.add(Chunk.NEWLINE);
			preventivPar.add(Chunk.NEWLINE);
			preventivPar.add(Chunk.NEWLINE);
			
			// Counter permettant de change de phrase si besoins
			counter = 0;
			
			while (datasTypeIter.hasNext()) {
				
				switch ((IDataHandler.DataType)datasTypeIter.next()) {
					case STRING:
						
						String data = "";
						if (counter == 0) {
							data += "nombre de bons préventifs ouverts : ";
						}
						else if (counter == 1) {
							data += "nombre de bons préventifs clôturés : ";
						}
						
						++counter;
						
						data += (String)datasIter.next();
						preventivPar.add(new Phrase (data, basicFont));
						preventivPar.add(Chunk.NEWLINE);
						break;
						
					case JFREECHART :
						
						PdfContentByte contentByte = writer.getDirectContent();
			            PdfTemplate template = contentByte.createTemplate(chartWidth, chartHeight);
			            
						Graphics2D graphics2d = new PdfGraphics2D(template, chartWidth, chartHeight);
			            
			            java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(0, 0, chartWidth,
			            		chartHeight);
			     
			            JFreeChart chart = (JFreeChart) datasIter.next();
			            chart.draw(graphics2d, rectangle2d);
			             
			            graphics2d.dispose();
			            //contentByte.addTemplate(template, 0, 0);
			            
			            Image chartImage = Image.getInstance(template);
			            
			            
			            preventivPar.add(chartImage);
			            preventivPar.add(Chunk.NEWLINE);
			            
						break;
					
					default:
						throw new Exception ("data type not handled");
				}
			}
			// Ajout de la partie
			document.add(preventivPar);
			// Passage a la partie suivante si on peut
			if (datasIterator.hasNext()) {
				currentDataPart = datasIterator.next();
			}
		}
		
		// Partie bon preventif par domaines
		if (currentDataPart.getPartTitle().equals("Bons préventifs par domaines")) {
			currentPartIter = currentDataPart.getDataStorage().iterator();
			datasTypeIter   = currentPartIter.next().iterator();
			datasIter       = currentPartIter.next().iterator();
			
			para = new Paragraph();
			
			document.newPage();
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			para.add(new Phrase("Préventif – Domaine technique", boldTitleFont));
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			while (datasTypeIter.hasNext()) {
				
				switch ((IDataHandler.DataType)datasTypeIter.next()) {
					case STRING:
						
						String data = "";
						
						data += (String)datasIter.next();
						para.add(new Phrase (data, basicFont));
						para.add(Chunk.NEWLINE);
						break;
						
					case JFREECHART :
						
						PdfContentByte contentByte = writer.getDirectContent();
			            PdfTemplate template = contentByte.createTemplate(chartWidth, chartHeight);
			            
						Graphics2D graphics2d = new PdfGraphics2D(template, chartWidth, chartHeight);
			            
			            java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(0, 0, chartWidth,
			            		chartHeight);
			     
			            JFreeChart chart = (JFreeChart) datasIter.next();
			            chart.draw(graphics2d, rectangle2d);
			             
			            graphics2d.dispose();
			            //contentByte.addTemplate(template, 0, 0);
			            
			            Image chartImage = Image.getInstance(template);
			            
			            
			            para.add(chartImage);
			            para.add(Chunk.NEWLINE);
			            
						break;
					
					default:
						throw new Exception ("data type not handled");
				}
			}
			para.add(Chunk.NEXTPAGE);
			document.add(para);
			if (datasIterator.hasNext()) {
				currentDataPart = datasIterator.next();
			}
		}
		
		// Partie arborescence libre 1
		while (currentDataPart.getPartTitle().equals("Arborescence Libre")){
			currentPartIter = currentDataPart.getDataStorage().iterator();
			datasTypeIter   = currentPartIter.next().iterator();
			datasIter       = currentPartIter.next().iterator();
			
			para = new Paragraph();
			
			document.newPage();
			
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			final String title = (String) datasIter.next();
			datasTypeIter.next();
			
			para.add(new Phrase("Préventif – " + title, boldTitleFont));
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			while (datasTypeIter.hasNext()) {
				
				switch ((IDataHandler.DataType)datasTypeIter.next()) {
					case STRING:
						
						String data = "";
						
						data += (String)datasIter.next();
						para.add(new Phrase (data, basicFont));
						para.add(Chunk.NEWLINE);
						break;
						
					case JFREECHART :
						
						PdfContentByte contentByte = writer.getDirectContent();
			            PdfTemplate template = contentByte.createTemplate(chartWidth, chartHeight);
			            
						Graphics2D graphics2d = new PdfGraphics2D(template, chartWidth, chartHeight);
			            
			            java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(0, 0, chartWidth,
			            		chartHeight);
			     
			            JFreeChart chart = (JFreeChart) datasIter.next();
			            chart.draw(graphics2d, rectangle2d);
			             
			            graphics2d.dispose();
			            //contentByte.addTemplate(template, 0, 0);
			            
			            Image chartImage = Image.getInstance(template);
			            
			            
			            para.add(chartImage);
			            para.add(Chunk.NEWLINE);
			            
						break;
					
					default:
						throw new Exception ("data type not handled");
				}
			}
			document.add(para);
			if (datasIterator.hasNext()) {
				currentDataPart = datasIterator.next();
			}
			else {
				break;
			}
		}
		
		/*---------------------------------partie correctif-------------------------------------------*/
		
		// Atteinte de la premiere partie corrective
		counter = 0;
		while (!(currentDataPart.getPartTitle().equals("Demandes d'intervention") ||
				currentDataPart.getPartTitle().equals("Demandes d'intervention par états") ||
				currentDataPart.getPartTitle().equals("Demandes d'intervention par domaines") ||
				currentDataPart.getPartTitle().equals("Arborescence Libre")) &&
				datasIterator.hasNext() && counter < 3) {
			
			currentDataPart = datasIterator.next();
			++counter;
		}
		
		if (currentDataPart.getPartTitle().equals("Demandes d'intervention") ||
				currentDataPart.getPartTitle().equals("Demandes d'intervention par états") ||
				currentDataPart.getPartTitle().equals("Demandes d'intervention par domaines") ||
				currentDataPart.getPartTitle().equals("Arborescence Libre")) {
			para = new Paragraph();
			document.newPage();
			// Nouvelle page, titre correctif
			for(int i = 0; i < 12; ++i){
				para.add(Chunk.NEWLINE);
			}
			Paragraph correctiveTitle = new Paragraph("Correctif", mainTitleFont);
			correctiveTitle.setAlignment(Element.ALIGN_CENTER);
			para.add(correctiveTitle);
			
			String IMG_CORRECT_PATH = Form.ICONS_PATH + File.separator + "imageCorrectif.jpg";
			
			Image imgCorrect;
		
			imgCorrect = Image.getInstance(IMG_CORRECT_PATH);
			
			imgCorrect.setAbsolutePosition(document.right() - document.left() / 2 - 300,
										document.bottom() + 300);
			
            try {
            	// Ajout du logo
				writer.getDirectContent().addImage(imgCorrect);
			} 
            catch (DocumentException e) {
				e.printStackTrace();
			}
            
			document.add(para);
		}
		
		// Partie demandes d'intervention
		if (currentDataPart.getPartTitle().equals("Demandes d'intervention")) {
		
			currentPartIter = currentDataPart.getDataStorage().iterator();
			datasTypeIter   = currentPartIter.next().iterator();
			datasIter       = currentPartIter.next().iterator();
			
			para = new Paragraph();
			
			document.newPage();
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			para.add(new Phrase("Correctif – Evolution annuelle", boldTitleFont));
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			counter = 0;
			
			while (datasTypeIter.hasNext()) {
				
				switch ((IDataHandler.DataType)datasTypeIter.next()) {
					case STRING:
						
						String data = "";
						if (counter == 0) {
							data += "nombre de demandes d'interventions : ";
						}
						
						++counter;
						
						data += (String)datasIter.next();
						para.add(new Phrase (data, basicFont));
						para.add(Chunk.NEWLINE);
						break;
						
					case JFREECHART :
						
						PdfContentByte contentByte = writer.getDirectContent();
			            PdfTemplate template = contentByte.createTemplate(chartWidth, chartHeight);
			            
						Graphics2D graphics2d = new PdfGraphics2D(template, chartWidth, chartHeight);
			            
			            java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(0, 0, chartWidth,
			            		chartHeight);
			     
			            JFreeChart chart = (JFreeChart) datasIter.next();
			            chart.draw(graphics2d, rectangle2d);
			             
			            graphics2d.dispose();
			            //contentByte.addTemplate(template, 0, 0);
			            
			            Image chartImage = Image.getInstance(template);
			            
			            
			            para.add(chartImage);
			            para.add(Chunk.NEWLINE);
			            
						break;
					
					default:
						throw new Exception ("data type not handled");
				}
			}
			document.add(para);
			if (datasIterator.hasNext()) {
				currentDataPart = datasIterator.next();
			}
		}
		
		// Partie demandes d'intervention par etats
		if (currentDataPart.getPartTitle().equals("Demandes d'intervention par états")) {
			currentPartIter = currentDataPart.getDataStorage().iterator();
			datasTypeIter   = currentPartIter.next().iterator();
			datasIter       = currentPartIter.next().iterator();
			
			para = new Paragraph();
			
			document.newPage();
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			para.add(new Phrase("Correctif – Etat", boldTitleFont));
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			while (datasTypeIter.hasNext()) {
				
				switch ((IDataHandler.DataType)datasTypeIter.next()) {
					case STRING:
						
						String data = "";
						
						data += (String)datasIter.next();
						para.add(new Phrase (data, basicFont));
						para.add(Chunk.NEWLINE);
						break;
						
					case JFREECHART :
						
						PdfContentByte contentByte = writer.getDirectContent();
			            PdfTemplate template = contentByte.createTemplate(chartWidth, chartHeight);
			            
						Graphics2D graphics2d = new PdfGraphics2D(template, chartWidth, chartHeight);
			            
			            java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(0, 0, chartWidth,
			            		chartHeight);
			     
			            JFreeChart chart = (JFreeChart) datasIter.next();
			            chart.draw(graphics2d, rectangle2d);
			             
			            graphics2d.dispose();
			            //contentByte.addTemplate(template, 0, 0);
			            
			            Image chartImage = Image.getInstance(template);
			            
			            
			            para.add(chartImage);
			            para.add(Chunk.NEWLINE);
			            
						break;
					
					default:
						throw new Exception ("data type not handled");
				}
			}
			document.add(para);
			if (datasIterator.hasNext()) {
				currentDataPart = datasIterator.next();
			}
		}
		
		// Partie demades d'intervention par domaines
		if (currentDataPart.getPartTitle().equals("Demandes d'intervention par domaines")) {
			currentPartIter = currentDataPart.getDataStorage().iterator();
			datasTypeIter   = currentPartIter.next().iterator();
			datasIter       = currentPartIter.next().iterator();
			
			para = new Paragraph();
			
			document.newPage();
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			para.add(new Phrase("Correctif – Domaine technique", boldTitleFont));
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			counter = 0;
			
			while (datasTypeIter.hasNext()) {
				
				switch ((IDataHandler.DataType)datasTypeIter.next()) {
					case STRING:
						
						String data = "";
						
						data += (String)datasIter.next();
						para.add(new Phrase (data, basicFont));
						para.add(Chunk.NEWLINE);
						break;
						
					case JFREECHART :
						
						if (counter == 1) {
							para = new Paragraph();
							
							document.newPage();
							para.add(Chunk.NEWLINE);
							para.add(Chunk.NEWLINE);
							
							para.add(new Phrase("Correctif – Objet des demandes", basicTitleFont));
							para.add(Chunk.NEWLINE);
						}
						
						++counter;
						
						PdfContentByte contentByte = writer.getDirectContent();
			            PdfTemplate template = contentByte.createTemplate(chartWidth, chartHeight);
			            
						Graphics2D graphics2d = new PdfGraphics2D(template, chartWidth, chartHeight);
			            
			            java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(0, 0, chartWidth,
			            		chartHeight);
			     
			            JFreeChart chart = (JFreeChart) datasIter.next();
			            chart.draw(graphics2d, rectangle2d);
			             
			            graphics2d.dispose();
			            //contentByte.addTemplate(template, 0, 0);
			            
			            Image chartImage = Image.getInstance(template);
			            
			            
			            para.add(chartImage);
			            para.add(Chunk.NEWLINE);
			            
						break;
					
					default:
						throw new Exception ("data type not handled");
				}
			}
			document.add(para);
			if (datasIterator.hasNext()) {
				currentDataPart = datasIterator.next();
			}
		}
		
		// Partie arborescence libre 2
		while (currentDataPart.getPartTitle().equals("Arborescence Libre")){
			currentPartIter = currentDataPart.getDataStorage().iterator();
			datasTypeIter   = currentPartIter.next().iterator();
			datasIter       = currentPartIter.next().iterator();
			
			para = new Paragraph();
			
			document.newPage();
			
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			final String title = (String) datasIter.next();
			datasTypeIter.next();
			
			para.add(new Phrase("Correctif – " + title, boldTitleFont));
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			while (datasTypeIter.hasNext()) {
				
				switch ((IDataHandler.DataType)datasTypeIter.next()) {
					case STRING:
						
						String data = "";
						
						data += (String)datasIter.next();
						para.add(new Phrase (data, basicFont));
						para.add(Chunk.NEWLINE);
						break;
						
					case JFREECHART :
						
						PdfContentByte contentByte = writer.getDirectContent();
			            PdfTemplate template = contentByte.createTemplate(chartWidth, chartHeight);
			            
						Graphics2D graphics2d = new PdfGraphics2D(template, chartWidth, chartHeight);
			            
			            java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(0, 0, chartWidth,
			            		chartHeight);
			     
			            JFreeChart chart = (JFreeChart) datasIter.next();
			            chart.draw(graphics2d, rectangle2d);
			             
			            graphics2d.dispose();
			            //contentByte.addTemplate(template, 0, 0);
			            
			            Image chartImage = Image.getInstance(template);
			            
			            
			            para.add(chartImage);
			            para.add(Chunk.NEWLINE);
			            
						break;
					
					default:
						throw new Exception ("data type not handled");
				}
			}
			document.add(para);
			if (datasIterator.hasNext()) {
				currentDataPart = datasIterator.next();
			}
			else {
				break;
			}
		}
		
		/*------------------------------------------------partie compteurs-----------------------------------------------------------*/
		
		// Atteinte de la partie compteur
		counter = 0;
		while (!(currentDataPart.getPartTitle().equals("Compteurs")) &&
				datasIterator.hasNext() && counter < 1) {
			
			currentDataPart = datasIterator.next();
			++counter;
		}
		
		// Si on a une partie compteur
		if (currentDataPart.getPartTitle().equals("Compteurs")) {
			para = new Paragraph();
			document.newPage();
			//nouvelle page, titre compteur
			for(int i = 0; i < 12; ++i){
				para.add(Chunk.NEWLINE);
			}
			Paragraph meterTitle = new Paragraph("Fluides et énergies", mainTitleFont);
			meterTitle.setAlignment(Element.ALIGN_CENTER);
			para.add(meterTitle);
			
			String IMG_COMPTEUR_PATH = Form.ICONS_PATH + File.separator + "imageCompteur.jpg";
			
			Image imgCompteur;
		
			imgCompteur = Image.getInstance(IMG_COMPTEUR_PATH);
			
			imgCompteur.setAbsolutePosition(document.right() - document.left() / 2 - 300,
										document.bottom() + 300);
			
            try {
            	// Ajout du logo
				writer.getDirectContent().addImage(imgCompteur);
			} 
            catch (DocumentException e) {
				e.printStackTrace();
			}
            
			document.add(para);
		}
		
		// Partie compteurs
		while (currentDataPart.getPartTitle().equals("Compteurs")){
			currentPartIter = currentDataPart.getDataStorage().iterator();
			datasTypeIter   = currentPartIter.next().iterator();
			datasIter       = currentPartIter.next().iterator();
			
			para = new Paragraph();
			
			document.newPage();
			
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			final String title = (String) datasIter.next();
			datasTypeIter.next();
			
			para.add(new Phrase("Fluides et énergies - " + title, boldTitleFont));
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			para.add(Chunk.NEWLINE);
			
			while (datasTypeIter.hasNext()) {
				
				switch ((IDataHandler.DataType)datasTypeIter.next()) {
					case STRING:
						
						String data = "";
						
						data += (String)datasIter.next();
						para.add(new Phrase (data, basicFont));
						para.add(Chunk.NEWLINE);
						break;
						
					case JFREECHART :
						
						PdfContentByte contentByte = writer.getDirectContent();
			            PdfTemplate template = contentByte.createTemplate(chartWidth, chartHeight);
			            
						Graphics2D graphics2d = new PdfGraphics2D(template, chartWidth, chartHeight);
			            
			            java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(0, 0, chartWidth,
			            		chartHeight);
			     
			            JFreeChart chart = (JFreeChart) datasIter.next();
			            chart.draw(graphics2d, rectangle2d);
			             
			            graphics2d.dispose();
			            //contentByte.addTemplate(template, 0, 0);
			            
			            Image chartImage = Image.getInstance(template);
			            
			            
			            para.add(chartImage);
			            para.add(Chunk.NEWLINE);
			            
						break;
					
					default:
						throw new Exception ("data type not handled");
				}
			}
			// On ajout le paragraphe au document
			document.add(para);
			//document.newPage();
			
			final int finalCounter = counter + progressIncrement;
	
			pBFrame.updateBar(finalCounter);
			
			if (datasIterator.hasNext()) {
				currentDataPart = datasIterator.next();
			}
			else {
				break;
			}
		}
		
		// A la fin, on ferme tous les flux
		document.close();
		writer.close();
	
		return true;
	}

	@Override
	public IDataHandler getDataHandler(String partTitle) {
		return new CustomerDataHandler(partTitle);
	}
	
}
