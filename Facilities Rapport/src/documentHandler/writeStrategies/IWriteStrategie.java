package documentHandler.writeStrategies;

import java.util.Map;

import com.itextpdf.text.Document;

/**
 * Cette interface decrit les strategies d'edition de document
 * @author Lucie PELLOTIERRO
 *
 */
public interface IWriteStrategie {
	
	/**
	 * Fonction qui permet de lancer l'edition d'un document a partir des donnees fournies
	 * @param datas Les donnees fournies
	 * @param reportPathName Le nom du document rapport
	 * @param document l'objet document a editer
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est geree
	 */
	public boolean writeDocument (Map<String, Object> datas, String reportPathName, Document document) throws Exception;
}