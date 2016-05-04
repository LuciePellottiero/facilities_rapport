package tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import documentHandler.CreateReportDocument;
import documentHandler.writeStrategies.IWriteStrategie;

/**
 * Cette class permet de tester certaines parties de l'application
 * @author Lucie PELLOTTEIRO
 *
 */
public class PdfTest {

	/**
	 * Le main a executer pour tester. Pour l'instant les arguments sont innutiles
	 * @param args Innutiles pour l'instant
	 */
	public static void main(String[] args) {
		
		// On prepare les donnees
		Map<Integer, Collection<Object>> datas = new HashMap<Integer, Collection<Object>>();
		
		// La liste des String a ajouter
		Collection<Object> strings = new ArrayList<Object>();
		
		// On ajoute autant de String que necessaire
		// (attention, un nombre de donnee minimum peut etre attendu)
		strings.add("Lucie");
		strings.add("PELLOTIERRO");
		
		// On ajoute les String en utilisant l'attribut pour préciser le type de donnée
		datas.put(IWriteStrategie.DATA_TYPE_STRING, strings);
		
		// On prepare la liste de diagramme
		Collection<Object> graphics = new ArrayList<Object>();
		
		// On ajoute un diagramme (attention, ceci n'est qu'un exemple)
		graphics.add(GraphTest.generateBarChart());
		
		// On ajoute aux donnees la liste des diagrammes
		datas.put(IWriteStrategie.DATA_TYPE_JFREECHART, graphics);
		
		// L'utilisation du DocumentStamper a ete abandonnee
		/*
		try {
			if (DocumentStamper.pdfStampe(datas)) {
				System.out.println("Stamping successful");
			}
			else {
				System.out.println("An error occured");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}*/
		
		
		try {
			// Finallement on creer le document
			CreateReportDocument.createPdf(datas);
			System.out.println("Rapport termine");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}