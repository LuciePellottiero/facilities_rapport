package documentHandler.writeStrategies;

import java.util.Collection;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import dataHandler.IDataHandler;

/**
 * Cette interface decrit les strategies d'edition de document
 * @author Lucie PELLOTTIERO
 *
 */
public interface IWriteStrategie {
	
	/**
	 * Un type de donnee possible
	 */
	public static final Integer DATA_TYPE_STRING     = 0;
	/**
	 * Un type de donnee possible
	 */
	public static final Integer DATA_TYPE_JFREECHART = 1;
	
	/**
	 * Fonction qui permet de lancer l'edition d'un document a partir des donnees fournies
	 * @param datas Les donnees fournies
	 * @param reportPathName Le nom du document rapport
	 * @param document l'objet document a editer
	 * @param writer Le PdfWriter qui permettra de mettre des Objet (tel des JFreeChart)
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est geree
	 */
	public boolean writeDocument (Collection<IDataHandler> datas, Document document, PdfWriter writer) throws Exception;
}