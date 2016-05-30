package documentHandler.writeStrategies;

import java.util.Collection;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import dataHandler.IDataHandler;
import ihm.ProgressBarFrame;

/**
 * Decrit les strategies d'edition de document
 * @author Lucie PELLOTTIERO
 *
 */
public interface IWriteStrategie {
	
	/**
	 * Identifiant de la strategie par defaut
	 */
	public static final int DEFAULT_STRATEGIE = 0;
	
	/**
	 * Edite le Document fournis avec les donnees fournis en se servant du PdfWriter fournis
	 * @param datas Les donnees que l'on doit ecrire
	 * @param document Le Document que l'on edite
	 * @param writer Le PdfWriter que l'on utilise
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est lancee
	 */
	public boolean writeDocument (Collection<IDataHandler> datas, Document document, PdfWriter writer) throws Exception;
	
	/**
	 * Edite le Document avec les donnees en se servant du PdfWriter tout en mettant a jour la ProgressBar
	 * @param datas Les donnees a mettre dans le Document
	 * @param document Le Document a editer
	 * @param writer Le PdfWriter que l'on doit utiliser
	 * @param pBFrame La ProgressBar que l'on doit mettre a jour
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est pas attrapee
	 */
	public boolean writeDocument (Collection<IDataHandler> datas, Document document, PdfWriter writer, ProgressBarFrame pBFrame) throws Exception;
}