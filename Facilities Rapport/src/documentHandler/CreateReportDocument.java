package documentHandler;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import dataHandler.IDataHandler;
import documentHandler.writeStrategies.DefaultWriteStrategie;
import documentHandler.writeStrategies.IWriteStrategie;
import ihm.ProgressBarFrame;
import utilities.FileUtilities;

/**
 * Ceci est la classe permettant de creer les fichiers de rapport
 * @author Lucie PELLOTTIERRO
 *
 */
public class CreateReportDocument {
	
	/**
	 * Cette String est le chemin par defaut vers lequel doit etre creer le rapport PDF
	 */
	private final static String DEFAULT_REPORT_PATH = "Facilities Rapport" + File.separator + 
			"Files" + File.separator + "Reports";
	
	private final static String DEFAULT_FILE_NAME = "PdfRepport.pdf";
	
	/**
	 * Creer le document PDF du rapport a l'emplacement par defaut avec mise a jour de la barre de progression.
	 * @param datas La liste des donnees indexee par leur type (voir les constantes dans IDataHandler)
	 * @param pBFrame La barre de progression a mettre a jour
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune exception n'est attrapee
	 */
	public static boolean createPdf (final Collection<IDataHandler> datas, final ProgressBarFrame pBFrame) throws Exception{
		
		// Tout d'abord, on creer le descripteur de ficher (l'objet File)
		final File pdfReport = FileUtilities.getResource(DEFAULT_REPORT_PATH + File.separator + DEFAULT_FILE_NAME);
		// Puis on creer le fichier a l'emplacement precise par defaut
		pdfReport.createNewFile();
		
		// On met a jour la barre de progression
		pBFrame.updateBar(pBFrame.getProgress() + 1);
		
		// On creer le Document correspondant
		final Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		
		// Le PdfWriter precise que le document doit etre au format PDF.
		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfReport));
		
		// On cree la strategie que l'on va utiliser pour creer le document
		final IWriteStrategie writeStrategie = new DefaultWriteStrategie();
		
		// On met a jour la barre de progression
		pBFrame.updateBar(pBFrame.getProgress() + 1);
		
		// On effectue l'edition du PDF par la strategie
		final boolean result = writeStrategie.writeDocument(datas, document, pdfWriter, pBFrame);
		
		// On n'oublie pas de fermer tous les flux
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
		
		//On renvoie le resultat
		return result;
	}
	
	/**
	 * Creer le document PDF du rapport a l'emplacement precise.
	 * @param datas La liste des donnees indexee par leur type (voir les constantes dans IDataHandler)
	 * @param reportPathName Le nom complet du fichier
	 * @param writeStrategieNumber Le numero de la strategie d'ecriture a appliquer (voir IWriteStrategie)
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est attrapee
	 */
	public static boolean createtPdf (final Collection<IDataHandler> datas, final String reportPathName, final int writeStrategieNumber) 
			throws Exception{
		// Tout d'abord, on creer le descripteur de ficher (l'objet File)
		final File pdfReport = FileUtilities.getResource(reportPathName);
		// Puis on creer le fichier a l'emplacement precise dans les parametres
		pdfReport.createNewFile();
		
		// On creer le Document correspondant
		final Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		
		// Le PdfWriter precise que le document doit etre au format PDF.
		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfReport));
		
		// Initialisation de la strategie
		final IWriteStrategie writeStrategie;
		
		// On cree la strategie que l'on va utiliser pour creer le document
		switch (writeStrategieNumber) {
			// Strategie par defaut
			case IWriteStrategie.DEFAULT_STRATEGIE :
				writeStrategie = new DefaultWriteStrategie();
				break;
			// Si strategie non reconnue
			default :
				throw new Exception ("Unknown strategie type, use final static attributs from IWriteStrategie interface.");
		}
		 
		
		// On effectue l'edition du PDF par la strategie
		final boolean result = writeStrategie.writeDocument(datas, document, pdfWriter);
		
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
		return result;		
	}
}
