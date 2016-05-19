package documentHandler.writeStrategies;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import org.jfree.chart.JFreeChart;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import dataHandler.IDataHandler;
import ihm.ProgressBarFrame;

/**
 * Cette classe est la strategie d'edition de document par defaut
 * @author Lucie PELLOTTIERO
 *
 */
public class DefaultWriteStrategie implements IWriteStrategie{
	
	private int chartWidth;
	private int chartHeight;
	
	public DefaultWriteStrategie () {
		chartWidth  = 500;
		chartHeight = 400;
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
		}
		
		// A la fin, on ferme tous les flux
		document.close();
		writer.close();

		return true;
	}

	@Override
	public boolean writeDocument(Collection<IDataHandler> datas, Document document, PdfWriter writer,
			ProgressBarFrame pBFrame) throws Exception {
		// On ouvre le document a la modification
				document.open();
				
				// Creation de la font par defaut
		        BaseFont basefont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
				// Creation de la font concrete
		        Font baseConcreteFont = new Font (basefont, 12, Font.NORMAL);
		        
				// On creer un Iterator pour les donnees
				Iterator<IDataHandler> datasIterator = datas.iterator();
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						pBFrame.updateBar(90);
					}
				});
				
				int counter = pBFrame.getProgress();
				
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
					
					final int finalCounter = ++counter;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							pBFrame.updateBar(finalCounter);
						}
					});
				}
				
				// A la fin, on ferme tous les flux
				document.close();
				writer.close();

				return true;
	}

}
