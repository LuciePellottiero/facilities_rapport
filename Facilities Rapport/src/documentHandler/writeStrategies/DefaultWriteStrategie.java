package documentHandler.writeStrategies;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import dataHandler.IDataHandler;

/**
 * Cette classe est la strategie d'edition de document par defaut
 * @author Lucie PELLOTTIERO
 *
 */
public class DefaultWriteStrategie implements IWriteStrategie{
	/**
	 * Chaque type de donnee requise
	 */
	private final static Integer[] EACH_DATA_TYPE_REQUIRED = {DATA_TYPE_STRING, DATA_TYPE_JFREECHART};
	
	/**
	 * Chaque type de donnee requise mais sous forme de string pour l'affichage d'exception
	 */
	private final static String[] EACH_DATA_TYPE_REQUIRED_STRING = {"String", "JFreeChart"};
	
	/**
	 * Pour chaque type de donnee attendue, le nombre de donnees attendues
	 */
	private final static int[] EACH_DATA_TYPE_NUMBER = {2, 1}; 
	
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
						break;
					default:
						throw new Exception ("data type not handled");
				}
				
				para.add(Chunk.NEWLINE);
			}
			
			// On ajout le paragraphe au document
			document.add(para);
		}
		
		// Auquel on ajoute des Phrase
		/*paraRedacteur.add(new Phrase((String)stringsIterator.next(), baseConcreteFont));
		paraRedacteur.add(Chunk.NEWLINE);
		paraRedacteur.add(new Phrase("Nom       : " + stringsIterator.next(), baseConcreteFont));
		paraRedacteur.add(Chunk.NEWLINE);
		paraRedacteur.add(new Phrase("adresse   : " + stringsIterator.next(), baseConcreteFont));
		paraRedacteur.add(Chunk.NEWLINE);
		paraRedacteur.add(new Phrase("téléphone : " + stringsIterator.next(), baseConcreteFont));
		paraRedacteur.add(Chunk.NEWLINE);
		paraRedacteur.add(new Phrase("Email     : " + stringsIterator.next(), baseConcreteFont));
		paraRedacteur.add(Chunk.NEWLINE);
		paraRedacteur.add(new Phrase("Nom du chargé d'affaire    : " + stringsIterator.next(), baseConcreteFont));
		*/
		// On creer un paragraphe
		//Paragraph para1 = new Paragraph();
		// Auquel on ajoute des Phrases
		//para1.add("Pr�nom " + stringsIterator.next());
		//para1.setSpacingBefore(50);
		// On ajout le paragraphe au document
		//document.add(para1);
		
		/*Paragraph paraClient = new Paragraph();
		paraClient.add(new Phrase((String)stringsIterator.next(), baseConcreteFont));
		paraRedacteur.add(Chunk.NEWLINE);
		paraRedacteur.add(new Phrase("Nom su site : " + stringsIterator.next(), baseConcreteFont));
		paraRedacteur.add(Chunk.NEWLINE);
		paraRedacteur.add(new Phrase("Code : " + stringsIterator.next(), baseConcreteFont));
		paraRedacteur.add(Chunk.NEWLINE);
		paraRedacteur.add(new Phrase("Nom su site : " + stringsIterator.next(), baseConcreteFont));
		paraRedacteur.add(Chunk.NEWLINE);
		paraRedacteur.add(new Phrase("Nom su site : " + stringsIterator.next(), baseConcreteFont));
		paraRedacteur.add(Chunk.NEWLINE);
		paraRedacteur.add(new Phrase("Nom su site : " + stringsIterator.next(), baseConcreteFont));
		paraRedacteur.add(Chunk.NEWLINE);
		paraRedacteur.add(new Phrase("Nom su site : " + stringsIterator.next(), baseConcreteFont));
		paraRedacteur.add(Chunk.NEWLINE);
		paraRedacteur.add(new Phrase("Nom su site : " + stringsIterator.next(), baseConcreteFont));
		
		// On creer un Iterator pour les JFreeChart
		Iterator<Object> JFreeChartIterator = datas.get(EACH_DATA_TYPE_REQUIRED[2]).iterator();
		
		// Ceci est le paragraphe precedent le graphique
		Paragraph para3 = new Paragraph();
		para3.add("Exemple de diagramme : ");
		para3.setSpacingBefore(50);
		para3.setSpacingAfter(25);
		// On l'ajoute
		document.add(para3);
		
		// Sert a ecrire objets (je crois)
		PdfContentByte contentByte = writer.getDirectContent();
		
		// Taille du graphique
		int width  = 500;
		int height = 400;
		// Creer le cadre virtuel qui contiendra le diagramme
        PdfTemplate template = contentByte.createTemplate(width, height);
        
        // On creer un objet virtuel graphique dans le cadre précedamment creer
		Graphics2D graphics2d = new PdfGraphics2D(template, width, height);
        
		// Dans cet objet virtuel graphique, on creer un rectangle qui contiendra le digramme
        java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(0, 0, width,
                height);
 
        // Finalement, on dessine le diagrame dans l'objet virtuel graphique a l'emplacement du rectangle
        ((JFreeChart) JFreeChartIterator.next()).draw(graphics2d, rectangle2d);
         
        // On libere la memoire parce qu'on est des gens biens
        graphics2d.dispose();
        
        // On ajoute le cadre virtuel au Document via le PdfContentByte
        // a l'emplacement X = 0 (correspond au milieu du document) et 
        // a l'emplacement Y correspondant a la coordonnee verticale du dernier document.add() - la hauteur du diagramme
        // En effet, Y = 0 correspond au pied du document ( (0,0) = en bas au milieu du document)
        contentByte.addTemplate(template, 0, writer.getVerticalPosition(false) - height);    
		*/
		
		// A la fin, on ferme tous les flux
		document.close();
		writer.close();

		return true;
	}

}
