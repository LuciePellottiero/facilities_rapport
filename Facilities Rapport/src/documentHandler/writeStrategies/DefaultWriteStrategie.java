package documentHandler.writeStrategies;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Iterator;

import org.jfree.chart.JFreeChart;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import dataHandler.IDataHandler;
import ihm.ProgressBarFrame;

/**
 * Cette classe est la strategie d'edition du rapport PDF par defaut.
 * @author Lucie PELLOTTIERO
 *
 */
public class DefaultWriteStrategie implements IWriteStrategie{
	
	/**
	 * La largeur des graphes.
	 */
	private int chartWidth;
	/**
	 * La hauteur des grahes.
	 */
	private int chartHeight;
	
	/**
	 * Le constructeur par defaut.<br>
	 * chartWidth = 500<br>
	 * charHeight = 350
	 */
	public DefaultWriteStrategie () {
		chartWidth  = 500;
		chartHeight = 350;
	}
	
	@Override
	public boolean writeDocument(Collection<IDataHandler> datas, Document document, PdfWriter writer,
			ProgressBarFrame pBFrame) throws Exception {
		// On ouvre le document a la modification
		document.open();
		
		// Creation de la BaseFfont par defaut
	    BaseFont basefont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
		// Creation de la Font concrete
	    Font baseConcreteFont = new Font (basefont, 12, Font.NORMAL);
	    
		// On creer un Iterator pour les donnees
		Iterator<IDataHandler> datasIterator = datas.iterator();
		
		// On obtient le progres courant de la ProgressBar
		int counter = pBFrame.getProgress();
		
		// On definit le nombre d'increment pour la ProgressBar en fonction du nombre de donnee et de son degre d'avancement prealable
		int progressIncrement = ProgressBarFrame.MY_MAXIMUM - counter / datas.size();
		
		// On itere sur les parties
		while (datasIterator.hasNext()) {
			
			// La partie courante
			IDataHandler currentDataPart = datasIterator.next();
			// L'iterator sur les donnees de la partie courante
			Iterator<Collection<Object>> currentPartIter = currentDataPart.getDataStorage().iterator();
			
			// L'Iterator sur les types de donnees
			Iterator<Object> datasTypeIter = currentPartIter.next().iterator();
			// L'Iterator sur les donnes
			Iterator<Object> datasIter     = currentPartIter.next().iterator();
			
			// On creer un paragraphe
			Paragraph para = new Paragraph();
			
			// On ajoute le titre du paragraphe
			para.add(new Phrase(currentDataPart.getPartTitle(), baseConcreteFont));
			// On ajoute une nouvelle ligne
			para.add(Chunk.NEWLINE);
			
			// Tous ce qui releve des tableaux est une tentative de mise en page a l'aide de tableau mais c'est un echec
			// On cree un tableau a 2 colonnes : titre donnee
			//PdfPTable table = new PdfPTable(2);
			// On enleve les bordures (ne fonctionne pas mais bon...)
			//table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			
			/*
			// On creer des float pour determiner la largeur que doivent prendre les colonnes
			float[] tableCellsWidths = new float[] {0f, 0f};
			
			// On creer un tableau de cellules a ajoute a la fin
			ArrayList<PdfPCell> tableCells = new ArrayList<PdfPCell>();
			*/
			
			// On itere sur le type de donne
			while (datasTypeIter.hasNext()) {
			
				switch ((IDataHandler.DataType)datasTypeIter.next()) {
					// Si c'est une String
					case STRING:
						
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
						
						// On ajoute le titre de la donnee
						para.add(new Phrase ((String)datasIter.next(), baseConcreteFont));
						// Puis on ajoute la donnee
						para.add(new Phrase ((String)datasIter.next(), baseConcreteFont));
						// Enfin on ajoute un saut de ligne
						para.add(Chunk.NEWLINE);
						break;
						
					// Si c'est un graphe
					case JFREECHART :
						
						// On obtient le PdfContentByte du PdfWriter
						PdfContentByte contentByte = writer.getDirectContent();
						// A partir de ca, on cree un PdfTemplate avec les tailles de l'instance
			            PdfTemplate template = contentByte.createTemplate(chartWidth, chartHeight);
			            
			            // On crer l'objet Graphics2D dans le template qui en prend toute la place
						Graphics2D graphics2d = new PdfGraphics2D(template, chartWidth, chartHeight);
			            
						// On crer un Rectangle2D avec la bonne taille
			            java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(0, 0, chartWidth,
			            		chartHeight);
			            
			            // On obtient le graphe
			            JFreeChart chart = (JFreeChart) datasIter.next();
			            // On le dessine dans le rectangle a l'interieur du Graphics2D
			            chart.draw(graphics2d, rectangle2d);
			             
			            // On libere la memoire du Graphics2D
			            graphics2d.dispose();
			            
			            // On obtient un objet Image a partir du template
			            Image chartImage = Image.getInstance(template);
			            
			            // Que l'on peut ajouter normalement dans le Paragraphe
			            para.add(chartImage);
			            // Enfin on ajoute un saut de ligne
			            para.add(Chunk.NEWLINE);
						break;
						
					// Si c'est une Image
					case IMAGE :
						
						// On obtient l'objet awt.Image (different de IText.Image)
						java.awt.Image image = (java.awt.Image) datasIter.next();
						
						// On le convertit en IText.Image que l'on insere dans le paragraphe
						para.add(Image.getInstance(image, null));
						break;	
						
					default:
						// Si le type est inconnue, on lance une Exception
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
			// S'il y a une suite, on ajoute une nouvelle page
			if (datasTypeIter.hasNext()) {
				para.add(Chunk.NEWPAGE);
			}
			// On ajout le paragraphe au document
			document.add(para);
			
			// S'il y a eu un ajout de nouvelle page, on indique au Document de prevoir une nouvelle page
			if (datasTypeIter.hasNext()) {
				document.newPage();
			}
	
			// On met a jour la ProgressBar
			counter += progressIncrement;
			pBFrame.updateBar(counter);
		}
		
		// A la fin, on ferme tous les flux
		document.close();
		writer.close();
	
		// Renvoie de réussite
		return true;
	}
}
