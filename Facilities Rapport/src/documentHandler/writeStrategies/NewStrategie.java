package documentHandler.writeStrategies;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Iterator;

import org.jfree.chart.JFreeChart;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Header;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import dataHandler.IDataHandler;
import ihm.ProgressBarFrame;

/**
 * Cette classe est la strategie d'edition de document par defaut
 * @author Lucie PELLOTTIERO
 *
 */
public class NewStrategie implements IWriteStrategie{
	
	private int chartWidth;
	private int chartHeight;
	
	public static final int MAX_DOMAIN_SIZE = 9;
	
	public NewStrategie () {
		chartWidth  = 500;
		chartHeight = 350;
	}
	
	
	/**
	 * Fonction d'edition de document
	 */
	@Override
	public boolean writeDocument(Collection<IDataHandler> datas, Document document, PdfWriter writer)
			throws Exception {
	    
		// On ouvre le document a la modification
		document.open();
		
		// Creation de la font par defaut
        BaseFont basefont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
		// Creation de la font concrete
        Font baseConcreteFont = new Font (basefont, 12, Font.NORMAL);
        
		// On creer un Iterator pour les donnees
		Iterator<IDataHandler> datasIterator = datas.iterator();
		
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
			
			String titre = new String("Facilities rapport");
			para.add(new Phrase(titre, baseConcreteFont));
			
			// On cree un tableau a 2 colonnes : titre donnee
			PdfPTable table = new PdfPTable(2);
			// On enleve les bordures (ne fonctionne pas mais bon...)
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			
			/*
			// On creer des float pour determiner la largeur que doivent prendre les colonnes
			float[] tableCellsWidths = new float[] {0f, 0f};
			
			// On creer un tableau de cellules a ajoute a la fin
			ArrayList<PdfPCell> tableCells = new ArrayList<PdfPCell>();
			*/
			
			while (datasTypeIter.hasNext()) {
			
				switch ((int)datasTypeIter.next()) {
					case IDataHandler.DATA_TYPE_STRING:
						
						// On obtient le titre
						/*String stringTitle = (String)datasIter.next();
						
						// On calcul sa largeur pour la mise en page
						float titleWidth = baseConcreteFont.getCalculatedBaseFont(true).getWidthPoint(stringTitle, baseConcreteFont.getCalculatedSize()); 
						if (titleWidth > tableCellsWidths[0]) {
							tableCellsWidths[0] = titleWidth;
						}
						// On ajoute le titre dans la premiere colonne
						PdfPCell title = new PdfPCell(new Phrase (stringTitle, baseConcreteFont));
						// On enleve les bordures
						title.setBorder(Rectangle.NO_BORDER);
						
						tableCells.add(title);

						// On obtient la donnee correspondante
						String stringData = (String)datasIter.next();
						
						// On calcul sa largeur pour la mise en page
						float dataWidth = baseConcreteFont.getCalculatedBaseFont(true).getWidthPoint(stringData, baseConcreteFont.getCalculatedSize()); 
						if (dataWidth > tableCellsWidths[1]) {
							tableCellsWidths[1] = dataWidth;
						}
						
						// Puis on met la valeur dans la seconde colonne
						PdfPCell stringDataCell = new PdfPCell(new Phrase (stringData, baseConcreteFont));
						// On enleve les bordures
						stringDataCell.setBorder(Rectangle.NO_BORDER);
						
						tableCells.add(stringDataCell);
						*/
						
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
			// On definit les largeurs du tableau
			/*table.setWidths(tableCellsWidths);
			
			// On ajoute toutes les cellules
			for (PdfPCell cell : tableCells) {
				table.addCell(cell);
			}
			
			// Enfin, on ajoute le tableau au paragraphe
			para.add(table);
			*/
			
			// On ajout le paragraphe au document
			if (datasTypeIter.hasNext()) {
				para.add(Chunk.NEWPAGE);
			}
			document.add(para);
			document.newPage();
		}
		
		// A la fin, on ferme tous les flux
		document.close();
		writer.close();

		return true;
	}
	
	/*------------------------------------------------------------------------------------------------------------------------------*/
	
	//en-tête et pied de page
	class MyFooter extends PdfPageEventHelper {
        Font footerFont = new Font(Font.FontFamily.UNDEFINED, 8, Font.NORMAL);
        Font colorFooterFont = new Font(Font.FontFamily.UNDEFINED, 8, Font.NORMAL, BaseColor.RED);
		
        String logoVinciPath = "Facilities Rapport" + File.separator + "Files" + File.separator + 
        		"Icons" + File.separator + "vinciFacilitiesIcon.png";
		java.awt.Image logoVinci = Toolkit.getDefaultToolkit().getImage(logoVinciPath);
		
        public void onStartPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            
            //contenu en-tête
            Phrase header1 = new Phrase("Centre d’Expertises VFPA", footerFont);
            Phrase header2 = new Phrase("Aix en Provence", footerFont);
            Phrase header3 = new Phrase("Support aux Opérations", footerFont);
            Phrase provisoire = new Phrase("logo vinci", footerFont);
            
            //en-tête position
            //logoVinci.setAbsolutePosition((PageSize.POSTCARD.getWidth() / 2), 22);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
            		provisoire,
                    document.left() + document.leftMargin() - 60,
                    document.top() + 20, 0);
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
            Phrase footer1 = new Phrase("PROVENCE MAINTENANCE SERVICES - Immeuble Le Rubis - 165 av Galilée - 13 857 Aix En Provence Cedex 3", footerFont);
            Phrase footer2 = new Phrase("Tél. : 04 42 90 55 80 - Fax. : 04 42 90 55 81  -  www.vinci-facilities.com - RCS 433 899 788 Aix en Provence – TVA FR 194 433 899 788", footerFont);
            Phrase footer3 = new Phrase("Ce document est la propriété du Centre d’Expertises VFPA. Toute reproduction est interdite sans autorisations", colorFooterFont);
            
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
	public boolean writeDocument(Collection<IDataHandler> datas, Document document, PdfWriter writer,
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
	    Font underlineBasicFont = new Font (basefont, 12, Font.UNDERLINE);
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
		//donnees rapport
		datasIter.next();
		Phrase titreRapport = new Phrase ("Rapport d'activité " + (String)datasIter.next() , firstPageFont);
		datasIter.next();
		String dateDebut = new String((String)datasIter.next());
		datasIter.next();
		String dateFin = new String((String)datasIter.next());
		Phrase date = new Phrase (dateDebut + " au " + dateFin, firstPageFont);
		
		//passage à la partie suivante : client
		currentDataPart = datasIterator.next();
		currentPartIter = currentDataPart.getDataStorage().iterator();
		datasTypeIter = currentPartIter.next().iterator();
		datasIter     = currentPartIter.next().iterator();
		
		//donnees client
		datasIter.next();
		String siteClient = new String((String)datasIter.next());
		Phrase nomSiteClient = (new Phrase (siteClient, basicFont));
		datasIter.next();
		Phrase codeClient = (new Phrase ((String)datasIter.next(), basicFont));
		datasIter.next();
		Phrase adresseClient = (new Phrase ((String)datasIter.next(), basicFont));
		datasIter.next();
		Phrase codePostalClient = (new Phrase ((String)datasIter.next(), basicFont));
		datasIter.next();
		Phrase villeClient = (new Phrase ((String)datasIter.next(), basicFont));
		datasIter.next();
		Phrase nomClient = (new Phrase ((String)datasIter.next(), boldBasicFont));
		datasIter.next();
		Phrase telClient = (new Phrase ((String)datasIter.next(), basicFont));
		datasIter.next();
		Phrase emailClient = (new Phrase ((String)datasIter.next(), basicFont));
		java.awt.Image logoClient = (java.awt.Image) datasIter.next();
		
		//passage a la partie suivante, redacteur
		currentDataPart = datasIterator.next();
		currentPartIter = currentDataPart.getDataStorage().iterator();
		datasTypeIter = currentPartIter.next().iterator();
		datasIter     = currentPartIter.next().iterator();
		
		/*-----------------------------page 1, page de garde------------------------------*/
		
		PdfContentByte cb = writer.getDirectContent();
		
		Phrase titrePrincipal = new Phrase(siteClient, mainTitleFont);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
				titrePrincipal,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.top() - 200, 0);
		
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
				 titreRapport,
                 (document.right() - document.left()) / 2 + document.leftMargin(),
                 document.top() - 240, 0);
		
		Image.getInstance(logoClient, null);
		
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
				date,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.top() - 400, 0);
		
		
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
		datasIter.next();
		paraRedac.add(new Phrase ((String)datasIter.next(), boldBasicFont));
		
		paraRedac.add(Chunk.NEWLINE);
		datasIter.next();
		paraRedac.add(new Phrase ((String)datasIter.next(), basicFont));
		
		paraRedac.add(Chunk.NEWLINE);
		datasIter.next();
		paraRedac.add("Tél : ");
		paraRedac.add(new Phrase ((String)datasIter.next(), basicFont));
		
		paraRedac.add(Chunk.NEWLINE);
		datasIter.next();
		paraRedac.add(new Phrase ((String)datasIter.next(), basicFont));
		
		paraRedac.add(Chunk.NEWLINE);
		Chunk chargeAffaire = new Chunk("Chargé d'affaire", basicFont);
		chargeAffaire.setUnderline(0.7f, -2f);
		Chunk espace = new Chunk(" : ", basicFont);
		Phrase chargedAffaire = new Phrase();
		chargedAffaire.add(chargeAffaire);
		chargedAffaire.add(espace);
		paraRedac.add(chargedAffaire);
		
		datasIter.next();
		paraRedac.add(new Phrase((String)datasIter.next(), boldBasicFont));
		
		String logoVinciPath = "Facilities Rapport" + File.separator + "Files" + File.separator + "Icons" + File.separator + "vinciFacilitiesIcon.png";
		Image logoVinci = Image.getInstance(logoVinciPath);
		
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
		table.addCell(Image.getInstance(logoClient, null));
		table.addCell(logoVinci);
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
			
				switch ((int)datasTypeIter.next()) {
					case IDataHandler.DATA_TYPE_STRING:
						
						para.add(new Phrase ((String)datasIter.next(), basicFont));
						para.add(new Phrase ((String)datasIter.next(), basicFont));
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
	
}
