package tests;

import java.util.ArrayList;
import java.util.Collection;
import dataHandler.DefaultDataHandler;
import dataHandler.IDataHandler;
import documentHandler.CreateReportDocument;
import ihm.ProgressBarFrame;

/**
 * Cette class permet de tester certaines parties de l'application
 * @author Lucie PELLOTTEIRO
 *
 */
public abstract class PdfTest {

	/**
	 * Le main a executer pour tester. Pour l'instant les arguments sont inutiles
	 * @param args Inutiles pour l'instant
	 */
	public static void main(String[] args) {
		
		Collection<IDataHandler> datas = new ArrayList<IDataHandler>();
		
		IDataHandler redacteurPart = new DefaultDataHandler("Rédacteur");
		
		redacteurPart.addString("Lucie"      , "prénom :");
		redacteurPart.addString("PELLOTTIERO", "nom      :");
		
		datas.add(redacteurPart);
		
		IDataHandler clientPart = new DefaultDataHandler("Client");
		
		clientPart.addString("Thomas", "prénom :");
		clientPart.addString("MEDARD", "nom      :");
		
		datas.add(clientPart);
		
		// On prepare les donnees
		/*Map<Integer, Collection<Object>> datas = new HashMap<Integer, Collection<Object>>();
		
		// La liste des String a ajouter
		Collection<Object> strings = new ArrayList<Object>();
		
		// On ajoute autant de String que necessaire
		// (attention, un nombre de donnees minimum peut etre attendu)
		strings.add("Lucie");
		strings.add("PELLOTTIERO");
		
		// On ajoute les String en utilisant l'attribut pour preciser le type de donnee
		datas.put(IWriteStrategie.DATA_TYPE_STRING, strings);
		
		// On prepare la liste de diagramme
		Collection<Object> graphics = new ArrayList<Object>();
		
		// On ajoute un diagramme (attention, ceci n'est qu'un exemple)
		graphics.add(GraphTest.generateBarChart());
		
		// On ajoute aux donnees la liste des diagrammes
		datas.put(IWriteStrategie.DATA_TYPE_JFREECHART, graphics);
		*/
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
			// Finalement on cree le document
			CreateReportDocument.createPdf(datas, new ProgressBarFrame());
			System.out.println("Rapport termine");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}