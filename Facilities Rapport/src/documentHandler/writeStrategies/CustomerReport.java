package documentHandler.writeStrategies;

import java.util.Collection;
import java.util.Iterator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import dataHandler.CustomerDataHandler;
import dataHandler.IDataHandler;
import ihm.ProgressBarFrame;

/**
 * Cette classe est la strategie d'edition de document par defaut
 * @author Lucie PELLOTTIERO
 *
 */
public class CustomerReport implements IWriteStrategie{
	
	@SuppressWarnings("unused")
	private int chartWidth;
	@SuppressWarnings("unused")
	private int chartHeight;
	
	public static final int MAX_DOMAIN_SIZE = 9;
	
	public CustomerReport () {
		chartWidth  = 500;
		chartHeight = 350;
	}
	
	//en-tête et pied de page
	class MyFooter extends PdfPageEventHelper {
        Font footerFont = new Font(Font.FontFamily.UNDEFINED, 8, Font.NORMAL);
        Font colorFooterFont = new Font(Font.FontFamily.UNDEFINED, 8, Font.NORMAL, BaseColor.RED);
		
        public void onStartPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            
            //contenu en-tête
            Phrase header1 = new Phrase("Centre d’Expertises VFPA", footerFont);
            Phrase header2 = new Phrase("Aix en Provence", footerFont);
            Phrase header3 = new Phrase("Support aux Opérations", footerFont);
			
            //en-tête position
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
    }

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
	    Font baseConcreteFont = new Font (basefont, 12, Font.NORMAL);
	    
		// On creer un Iterator pour les donnees
		Iterator<IDataHandler> datasIterator = datas.iterator();
		
		int counter = pBFrame.getProgress();
		
		int progressIncrement = ProgressBarFrame.MY_MAXIMUM - counter / datas.size();
		
		
		
		Paragraph paragraph1 = new Paragraph("First paragraph");
        Paragraph paragraph2 = new Paragraph("Second paragraph");
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(Chunk.NEWLINE);
		
        IDataHandler currentDataPart = datasIterator.next();
		Iterator<Collection<Object>> currentPartIter = currentDataPart.getDataStorage().iterator();
		
		@SuppressWarnings("unused")
		Iterator<Object> datasTypeIter = currentPartIter.next().iterator();
		Iterator<Object> datasIter     = currentPartIter.next().iterator();
		
		
		// On creer un paragraphe
		Paragraph para = new Paragraph();
		
		/*----------page 1, page de garde--------------*/
		para.add(new Phrase ("Rapport d'activité ", baseConcreteFont));
		para.add(new Phrase ((String)datasIter.next(), baseConcreteFont));
		para.add(Chunk.NEWLINE);
		para.add(new Phrase ((String)datasIter.next(), baseConcreteFont));
		para.add(new Phrase (" au ", baseConcreteFont));
		para.add(new Phrase ((String)datasIter.next(), baseConcreteFont));
		
		/*---------page 2, coordonnées client - redacteur-----------*/
		
		//passage à la partie suivante : client
		currentDataPart = datasIterator.next();
		currentPartIter = currentDataPart.getDataStorage().iterator();
		datasTypeIter = currentPartIter.next().iterator();
		datasIter     = currentPartIter.next().iterator();
		
		//on recupere les donnees pour les mettre ensuite dans un tableau
		Phrase nomSiteClient = (new Phrase ((String)datasIter.next(), baseConcreteFont));
		Phrase codeClient = (new Phrase ((String)datasIter.next(), baseConcreteFont));
		Phrase adresseClient = (new Phrase ((String)datasIter.next(), baseConcreteFont));
		Phrase codePostalClient = (new Phrase ((String)datasIter.next(), baseConcreteFont));
		Phrase villeClient = (new Phrase ((String)datasIter.next(), baseConcreteFont));
		Phrase nomClient = (new Phrase ((String)datasIter.next(), baseConcreteFont));
		Phrase telClient = (new Phrase ((String)datasIter.next(), baseConcreteFont));
		Phrase emailClient = (new Phrase ((String)datasIter.next(), baseConcreteFont));
		
		para.add(nomSiteClient);
		para.add(codeClient);
		para.add(adresseClient);
		para.add(codePostalClient);
		para.add(villeClient);
		para.add(nomClient);
		para.add(telClient);
		para.add(emailClient);
		
		Paragraph para1 = new Paragraph();
		
		para1.add("BORDER DE MERDE TU VA MARCHER OUI!");
		document.add(para1);
		
		System.out.println("nom Client : " + nomClient);
	
		//passage a la partie suivante, redacteur
		currentDataPart = datasIterator.next();
		currentPartIter = currentDataPart.getDataStorage().iterator();
		datasTypeIter = currentPartIter.next().iterator();
		datasIter     = currentPartIter.next().iterator();
		
		//nouvelle page
		para.add(Chunk.NEXTPAGE);
		para.add(new Phrase ("Pilotage du marché multitechnique", baseConcreteFont));
		para.add(Chunk.NEWLINE);
		
		//tableau contenant les donnees client et redacteur
		PdfPTable table = new PdfPTable(2);
		table.getDefaultCell().setBorder(0);
		table.addCell("CLIENT");
		table.addCell("PRESTATAIRE");
		table.addCell(nomClient);
		table.addCell(new Phrase ((String)datasIter.next(), baseConcreteFont));
		table.addCell(nomSiteClient);
		table.addCell(new Phrase ((String)datasIter.next(), baseConcreteFont));
		table.addCell(codeClient);
		table.addCell(new Phrase ((String)datasIter.next(), baseConcreteFont));
		table.addCell(adresseClient);
		table.addCell(new Phrase ((String)datasIter.next(), baseConcreteFont));
		table.addCell(codePostalClient);
		table.addCell(new Phrase ((String)datasIter.next(), baseConcreteFont));
		table.addCell(villeClient);
		table.addCell("");
		table.addCell(telClient);
		table.addCell("");
		table.addCell(emailClient);
		table.addCell("");
		para.add(table);
		
		
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
			document.newPage();
			
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
