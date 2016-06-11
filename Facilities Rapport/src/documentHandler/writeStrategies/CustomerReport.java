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
 * Cette classe est la strategie d'edition de document par defaut
 * @author Lucie PELLOTTIERO
 *
 */
public class CustomerReport implements IWriteStrategie{
	
	private int chartWidth;
	private int chartHeight;
	
	private static final String LOGO_VINCI_PATH = Form.ICONS_PATH + File.separator + Form.ICONS_NAME[0];
	private static Image logoVinci;
	
	public static final int MAX_DOMAIN_SIZE = 9;
	
	public CustomerReport () {
		chartWidth  = 500;
		chartHeight = 350;
		
		try {
			logoVinci = Image.getInstance(LOGO_VINCI_PATH);
		} 
		catch (BadElementException | IOException e) {
			e.printStackTrace();
		}
	}
	
	//en-tête et pied de page
	static class MyFooter extends PdfPageEventHelper {
        private static final Font FOOTER_FONT = new Font(Font.FontFamily.UNDEFINED, 8, Font.NORMAL);
        private static final Font COLOR_FOOTER_FONT = new Font(Font.FontFamily.UNDEFINED, 8, Font.NORMAL, BaseColor.RED);
        
        private static final Image VINCI_LOGO_HEADER = Image.getInstance(logoVinci);
        static {
        	VINCI_LOGO_HEADER.scaleAbsolute(VINCI_LOGO_HEADER.getWidth() / 2,
        									VINCI_LOGO_HEADER.getHeight() / 2);
        }
        
        public void onStartPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            
            //contenu en-tête
            Phrase header1 = new Phrase("Centre d’Expertises VFPA", FOOTER_FONT);
            Phrase header2 = new Phrase("Aix en Provence", FOOTER_FONT);
            Phrase header3 = new Phrase("Support aux Opérations", FOOTER_FONT);
            
            //en-tête position
            //logoVinci.setAbsolutePosition((PageSize.POSTCARD.getWidth() / 2), 22);
            VINCI_LOGO_HEADER.setAbsolutePosition(document.left() + document.leftMargin() - 60, 
            		document.top() - document.topMargin() + 43);
            try {
				writer.getDirectContent().addImage(VINCI_LOGO_HEADER);
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
        
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            
            //contenu pied de page
            Phrase footer1 = new Phrase("PROVENCE MAINTENANCE SERVICES - Immeuble Le Rubis - 165 av Galilée - 13 857 Aix En Provence Cedex 3", FOOTER_FONT);
            Phrase footer2 = new Phrase("Tél. : 04 42 90 55 80 - Fax. : 04 42 90 55 81  -  www.vinci-facilities.com - RCS 433 899 788 Aix en Provence – TVA FR 194 433 899 788", FOOTER_FONT);
            Phrase footer3 = new Phrase("Ce document est la propriété du Centre d’Expertises VFPA. Toute reproduction est interdite sans autorisations", COLOR_FOOTER_FONT);
            
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
    }//fin MyFooter

	@Override
	public boolean writeDocument(final Collection<IDataHandler> datas, final Document document, final PdfWriter writer, 
		ProgressBarFrame pBFrame) throws Exception {
	
		// Creation de l'en-tête et pied de page
		MyFooter event = new MyFooter();
	    writer.setPageEvent(event);
	    
		// On ouvre le document a la modification
		document.open();
		
		// Creation de la font par defaut
	    BaseFont basefont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
		// Creation de la font concrete
	    Font basicFont = new Font (basefont, 12, Font.NORMAL);
	    Font boldBasicFont = new Font (basefont, 12, Font.BOLD);
	    Font boldTitleFont = new Font (basefont, 18, Font.BOLD);
	    Font basicTitleFont = new Font (basefont, 18, Font.NORMAL);
	    Font mainTitleFont = new Font (basefont, 35, Font.BOLD);
	    Font firstPageFont = new Font (basefont, 26, Font.NORMAL);
	   
	    
		// On cre un Iterator pour les donnees
		Iterator<IDataHandler> datasIterator = datas.iterator();
		
		int counter = pBFrame.getProgress();
		
		int progressIncrement = ProgressBarFrame.MY_MAXIMUM - counter / datas.size();
		
        IDataHandler currentDataPart = datasIterator.next();
		Iterator<Collection<Object>> currentPartIter = currentDataPart.getDataStorage().iterator();
		
		Iterator<Object> datasTypeIter = currentPartIter.next().iterator();
		Iterator<Object> datasIter     = currentPartIter.next().iterator();
		
		
		// On cre un paragraphe
		Paragraph para = new Paragraph();
		
		/*-----------------recuperation des données---------------*/
		
		Phrase titreRapport = new Phrase ("Rapport d'activité " + (String)datasIter.next() , firstPageFont);
		String dateDebut = new String((String)datasIter.next());
		String dateFin = new String((String)datasIter.next());
		
		//passage à la partie suivante : client
		currentDataPart = datasIterator.next();
		currentPartIter = currentDataPart.getDataStorage().iterator();
		datasTypeIter = currentPartIter.next().iterator();
		datasIter     = currentPartIter.next().iterator();
		
		//donnees client
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
		
		//passage a la partie suivante, redacteur
		currentDataPart = datasIterator.next();
		currentPartIter = currentDataPart.getDataStorage().iterator();
		datasTypeIter = currentPartIter.next().iterator();
		datasIter     = currentPartIter.next().iterator();
		
		/*-----------------------------page 1, page de garde------------------------------*/
		
		final Paragraph mainTitle = new Paragraph();
		for(int i = 0; i < 15; ++i){
			mainTitle.add(Chunk.NEWLINE);
		}
		mainTitle.setAlignment(Element.ALIGN_CENTER);
		
		//donnees rapport
		mainTitle.add(titreRapport);
		mainTitle.add(Chunk.NEWLINE);
		Phrase titrePrincipal = new Phrase(siteClient, mainTitleFont);
		mainTitle.add(titrePrincipal);
		if (logoClient != null) {
			final ImageIcon resizedCustomerLogo = new ImageIcon(logoClient.
                    getScaledInstance(-1,
                    150,
                    java.awt.Image.SCALE_SMOOTH));
			final Image titleLogo = Image.getInstance(resizedCustomerLogo.getImage(), null);
			titleLogo.setAlignment(Image.ALIGN_CENTER);
			mainTitle.add(titleLogo);
		}
		Phrase date = new Phrase (dateDebut + " au " + dateFin, firstPageFont);
		mainTitle.add(date);
		
		mainTitle.add(Chunk.NEXTPAGE);
		document.add(mainTitle);
		
		/*PdfContentByte cb = writer.getDirectContent();
		
		Phrase titrePrincipal = new Phrase(siteClient, mainTitleFont);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
				titrePrincipal,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.top() - 200, 0);
		
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
				 titreRapport,
                 (document.right() - document.left()) / 2 + document.leftMargin(),
                 document.top() - 240, 0);
		
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
				date,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.top() - 400, 0);
		*/
		
		/*---------page 2, coordonnees client - redacteur-----------*/
		
		
		
		//nouvelle page
		para.add(Chunk.NEXTPAGE);
		para.add(Chunk.NEWLINE);
		para.add(new Phrase ("Pilotage du marché multitechnique", boldTitleFont));
		para.add(Chunk.NEWLINE);
		para.add(Chunk.NEWLINE);
		
		//paragraphe contenant les donnees client 
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
        
		//paragraphe contenant les donnees redacteur
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
		
		//tableau contenant les donnees client et redacteur
		PdfPTable table = new PdfPTable(2); //tableau de deux colonnes
		table.getDefaultCell().setBorder(0); //pas de bordure
		
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
		
		/*---------------------------------partie preventif-------------------------------------------*/
		
		//nouvelle page, titre preventif
		para.add(Chunk.NEXTPAGE);
		for(int i = 0; i < 15; ++i){
			para.add(Chunk.NEWLINE);
		}
		Paragraph titrePreventif = new Paragraph("Préventif", mainTitleFont);
		titrePreventif.setAlignment(Element.ALIGN_CENTER);
		para.add(titrePreventif);
		
		para.add(Chunk.NEXTPAGE);
		
		
		// On itere sur les parties
		while (datasIterator.hasNext()) {
			currentDataPart = datasIterator.next();
			currentPartIter = currentDataPart.getDataStorage().iterator();
			datasTypeIter = currentPartIter.next().iterator();
			datasIter     = currentPartIter.next().iterator();
			
			para.add(Chunk.NEWLINE);
			
				
			while (datasTypeIter.hasNext()) {
			
				switch ((IDataHandler.DataType)datasTypeIter.next()) {
					case STRING:
						
						para.add(new Phrase ((String)datasIter.next(), basicFont));
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
		}
		/*
		// On itere sur les parties
		while (datasIterator.hasNext()) {
			
			IDataHandler currentDataPart = datasIterator.next();
			Iterator<Collection<Object>> currentPartIter = currentDataPart.getDataStorage().iterator();
			
			Iterator<Object> datasTypeIter = currentPartIter.next().iterator();
			Iterator<Object> datasIter     = currentPartIter.next().iterator();
			
			// On creer un paragraphe
			Paragraph para = new Paragraph();
			
			para.add(new Phrase(currentDataPart.getPartTitle(), baseConcreteFont));
			para.add(Chunk.NEWLINE);
			
				
			while (datasTypeIter.hasNext()) {
			
				switch ((int)datasTypeIter.next()) {
					case IDataHandler.DATA_TYPE_STRING:
						
						para.add(new Phrase ((String)datasIter.next(), baseConcreteFont));
						para.add(new Phrase ((String)datasIter.next(), baseConcreteFont));
						para.add(Chunk.NEWLINE);
						break;
						
					case IDataHandler.DATA_TYPE_JFREECHART :
						
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
						
					case IDataHandler.DATA_TYPE_IMAGE :
						
						java.awt.Image image = (java.awt.Image) datasIter.next();
						
						para.add(Image.getInstance(image, null));
						break;	
						
					default:
						throw new Exception ("data type not handled");
				}
				
				//para.add(Chunk.NEWLINE);
			}
			*/
			
			
			para.add(Chunk.NEWLINE);
			
			// On ajout le paragraphe au document
			document.add(para);
			//document.newPage();
			
			final int finalCounter = counter + progressIncrement;
	
			pBFrame.updateBar(finalCounter);
		
		
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
