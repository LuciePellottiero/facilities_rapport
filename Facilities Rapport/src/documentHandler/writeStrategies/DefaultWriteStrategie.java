package documentHandler.writeStrategies;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.JFreeChart;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Cette classe est la strategie d'edition de document par defaut
 * @author Lucie PELLOTTIERO
 *
 */
public class DefaultWriteStrategie implements IWriteStrategie{

	/**
	 * Chaque type de donnée requise
	 */
	private final static Integer[] EACH_DATA_TYPE_REQUIRED = {DATA_TYPE_STRING, DATA_TYPE_JFREECHART};
	
	/**
	 * Chaque type de donnée requise mais sous forme de string pour l'affichage d'exception
	 */
	private final static String[] EACH_DATA_TYPE_REQUIRED_STRING = {"String", "JFreeChart"};
	
	/**
	 * Pour chaque type de donnée attendu, le nombre de donnée attendu
	 */
	private final static int[] EACH_DATA_TYPE_NUMBER = {2, 1}; 
	
	
	/**
	 * Fonction d'edition de document
	 */
	@Override
	public boolean writeDocument(Map<Integer, Collection<Object>> datas, Document document, PdfWriter writer)
			throws Exception {
		
		/******Check des données*****/
		if (datas.size() < EACH_DATA_TYPE_REQUIRED.length) {
			throw new Exception ("Not enough data types, " + EACH_DATA_TYPE_REQUIRED.length + " waited");
		}
		
		// Pour iterer sur les donnees qui sont dans une map, on recupere un Iterator du type d'entree de la map
        Iterator<Entry<Integer, Collection<Object>>> iterator = datas.entrySet().iterator();
        
        // Permet d'avoir un indice
        int counter = 0;
        // Tant que l'on a une element suivant
		while (iterator.hasNext()) {
			//On prend l'element suivant
			Entry<Integer, Collection<Object>> data = iterator.next();
			
			// On verifie que l'on a le bon type de donnee (en tous cas, le bon type annonce)
			if (data.getKey() != EACH_DATA_TYPE_REQUIRED[counter]) {
				String exceptionMsg = "Unnexpected data type, number " + counter + ", " + 
						EACH_DATA_TYPE_REQUIRED_STRING[counter] + " required verify your data order : \n";
				
				for (String typeStr : EACH_DATA_TYPE_REQUIRED_STRING) {
					exceptionMsg += typeStr + " ";
				}
				throw new Exception (exceptionMsg);
			}		
			// On vérifie que l'on a bien assez de données pour chaque type de données.
			else if (data.getValue().size() < EACH_DATA_TYPE_NUMBER[counter]) {
				throw new Exception ("Not enough data for the type number " + counter);
			}
			
			++counter;
		}
		/******Fin check des donnees*****/
		
		// On ouvre le document a la modification
		document.open();
		
		// On creer un Iterator pour les string
		Iterator<Object> stringsIterator = datas.get(EACH_DATA_TYPE_REQUIRED[0]).iterator();
		
		// On creer un paragraphe
		Paragraph para1 = new Paragraph();
		// Auquel on ajoute des Phrase
		para1.add("Prénom " + stringsIterator.next());
		para1.setSpacingBefore(50);
		// On ajout le paragraphe au document
		document.add(para1);
		
		// On refait la même chose
		Paragraph para2 = new Paragraph();
		para2.add("Nom " + stringsIterator.next());
		para2.setSpacingBefore(10);
		document.add(para2);
		
		// On creer un Iterator pour les JFreeChart
		Iterator<Object> JFreeChartIterator = datas.get(EACH_DATA_TYPE_REQUIRED[1]).iterator();
		
		// Ceci est le paragraphe precedent le graphique
		Paragraph para3 = new Paragraph();
		para3.add("Exemple de diagramme : ");
		para3.setSpacingBefore(50);
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
		
		// A la fin, on ferme tous les flux
		document.close();

		return true;
	}

}
