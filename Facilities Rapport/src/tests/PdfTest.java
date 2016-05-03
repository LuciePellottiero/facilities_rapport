package tests;

import java.util.HashMap;
import java.util.Map;

import documentHandler.CreateReportDocument;

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
		
		Map<String, Object> datas = new HashMap<String, Object>();
		
		datas.put("Forname", "Lucie");
		datas.put("Name", "PELLOTIERRO");
		
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
			CreateReportDocument.createPdf(datas);
			System.out.println("Rapport termine");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}