package documentHandler;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import dataHandler.IDataHandler;
import documentHandler.writeStrategies.IWriteStrategie;
import ihm.ProgressBarFrame;

/**
 * Ceci est la classe permettant de creer les fichiers de rapport
 * @author Lucie PELLOTTIERO
 *
 */
public class CreateReportDocument {
	
	/**
	 * Chemin par defaut vers lequel doit etre creer le rapport PDF
	 */
	public final static String DEFAULT_REPORT_PATH = new File("").getAbsolutePath();
	
	/**
	 * Nom par defaut du rapport
	 */
	public final static String DEFAULT_FILE_NAME = "PdfReport";
	
	/**
	 * Creer le document PDF du rapport a l'emplacement precise.
	 * @param datas La liste des donnees indexee par leur type (voir les constantes dans {@link IDataHandler})
	 * @param reportPathName Le nom complet du fichier (avec chemin absolue)
	 * @param writeStrategieNumber Le type de la strategie d'ecriture a appliquer (voir {@link IWriteStrategie})
	 * @param pBFrame La barre de progression a mettre a jour
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est attrapee
	 */
	public static String createPdf (final Collection<IDataHandler> datas, final String reportPathName, final IWriteStrategie strategie,
			final ProgressBarFrame pBFrame) 
			throws Exception{
		// Tout d'abord, on creer le descripteur de ficher (l'objet File)
		final File pdfReport = new File(reportPathName);
		// Puis on creer le fichier a l'emplacement precise dans les parametres
		pdfReport.createNewFile();
		
		// On met a jour la barre de progression
		pBFrame.updateBar(pBFrame.getProgress() + 1);
		
		// On creer le Document correspondant
		final Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		
		// Le PdfWriter precise que le document doit etre au format PDF.
		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfReport));
		
		// On met a jour la barre de progression
		pBFrame.updateBar(pBFrame.getProgress() + 1);
		
		// On effectue l'edition du PDF par la strategie
		strategie.writeDocument(datas, document, pdfWriter, pBFrame);
		
		// On oublie pas de fermer tous les flux
		pdfWriter.close();
		document.close();
		
		// Si le repertoir parent n'est pas null
		if (pdfReport.getParentFile() != null) {
			// On ouvre le dossier du rapport
			Desktop.getDesktop().open(pdfReport.getParentFile());
		}
		else {
			// Si le rapport n'a pas de parent, alors on ouvre le rapport directement
			Desktop.getDesktop().open(pdfReport);
		}
		
		// On renvoie le resultat
		return pdfReport.getAbsolutePath();		
	}
}
