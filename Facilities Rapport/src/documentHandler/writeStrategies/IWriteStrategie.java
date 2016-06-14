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
	 * Les strategies utilisables
	 * @author Lucie PELOTTIERRO
	 *
	 */
	public enum Strategie{
		DEFAULT, CUSTOMER
	}

	/**
	 * Edite le Document avec les donnees en se servant du {@link PdfWriter} tout en mettant a jour la {@link ProgressBarFrame}
	 * @param datas Les donnees a mettre dans le Document
	 * @param document Le {@link Document} a editer
	 * @param writer Le {@link PdfWriter} que l'on doit utiliser
	 * @param pBFrame La {@link ProgressBarFrame} que l'on doit mettre a jour
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est pas attrapee
	 */
	public boolean writeDocument (final Collection<IDataHandler> datas, final Document document, final PdfWriter writer, 
			ProgressBarFrame pBFrame) throws Exception;
	
	/**
	 * Obtient un {@link IDataHandler} correspondant a cette strategie
	 * @param partTitle Le titre du {@link IDataHandler}
	 * @return un {@link IDataHandler} correspondant a cette strategie
	 */
	public IDataHandler getDataHandler(final String partTitle);
}