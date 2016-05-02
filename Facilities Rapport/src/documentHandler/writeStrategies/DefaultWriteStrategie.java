package documentHandler.writeStrategies;

import java.util.ArrayList;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;

/**
 * Cette classe est la strategie d'edition de document par defaut
 * @author Lucie PELLOTIERRO
 *
 */
public class DefaultWriteStrategie implements IWriteStrategie{

	/**
	 * Fonction d'edition de document
	 */
	@Override
	public boolean writeDocument(Map<String, Object> datas, String reportPathName, Document document)
			throws Exception {
		// On ouvre le document a la modification
		document.open();
		
		// Ceci est la liste des paragraphes a ajouter au document
		ArrayList<Paragraph> paragraphs = new ArrayList<Paragraph>();
		
		// On creer un paragraphe
		Paragraph para1 = new Paragraph();
		// Auquel on ajoute des Phrase
		para1.add("Prénom " + datas.get("Forname"));
		para1.setSpacingBefore(50);
		paragraphs.add(para1);
		
		Paragraph para2 = new Paragraph();
		para2.add("Nom " + datas.get("Name"));
		para2.setSpacingBefore(50);
		paragraphs.add(para2);
		
		try {
			// On ajoute chaque Paragraphe au document
			for (Paragraph para : paragraphs) {
				document.add(para);
			}	
		} 
		catch (DocumentException e) {
			e.printStackTrace();
		}
		
		// A la fin, on ferme tous les flux
		document.close();

		return true;
	}

}
