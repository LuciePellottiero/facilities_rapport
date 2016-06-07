package documentHandler.writeStrategies;

import java.awt.Graphics2D;
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
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Header;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
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
            
            Image img;
			try {
				img = Image.getInstance("/Facilities Rapport/Files/Icons/vinciFacilitiesIcon.png");
			} catch (BadElementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
	public boolean writeDocument(Collection<IDataHandler> datas, Document document, PdfWriter writer,
			ProgressBarFrame pBFrame) throws Exception {
		
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
		
		// On itere sur les parties
		while (datasIterator.hasNext()) {
			
			IDataHandler currentDataPart = datasIterator.next();
			Iterator<Collection<Object>> currentPartIter = currentDataPart.getDataStorage().iterator();
			
			Iterator<Object> datasTypeIter = currentPartIter.next().iterator();
			Iterator<Object> datasIter     = currentPartIter.next().iterator();
			
			// On creer un paragraphe
			Paragraph para = new Paragraph();
			
			para.add(new Chunk (("Facilities rapport"), baseConcreteFont));
			para.add(Chunk.NEWLINE);
			
			para.add(new Phrase(currentDataPart.getPartTitle(), baseConcreteFont));
			para.add(Chunk.NEWLINE);
			
			// On cree un tableau a 2 colonnes : titre donnee
			//PdfPTable table = new PdfPTable(2);
			// On enleve les bordures (ne fonctionne pas mais bon...)
			//table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			
			/*
			// On creer des float pour determiner la largeur que doivent prendre les colonnes
			float[] tableCellsWidths = new float[] {0f, 0f};
			
			// On creer un tableau de cellules a ajouter a la fin
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
			para.add(Chunk.NEWLINE);
			
			// On ajout le paragraphe au document
			document.add(para);
			document.newPage();
			
			final int finalCounter = counter + progressIncrement;
	
			pBFrame.updateBar(finalCounter);
		}
		
		// A la fin, on ferme tous les flux
		document.close();
		writer.close();
	
		return true;
	}
	
}
