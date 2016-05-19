package documentHandler;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;

import javax.swing.SwingUtilities;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import dataHandler.IDataHandler;
import documentHandler.writeStrategieFactory.WriteStrategieFactory;
import documentHandler.writeStrategies.IWriteStrategie;
import ihm.ProgressBarFrame;
import utilities.FileUtilities;

/**
 * Ceci est la classe permettant de creer les rapports
 * @author Lucie PELLOTTIERRO
 *
 */
public class CreateReportDocument {
	
	/**
	 * Cette String est le chemin par defaut vers lequel doit etre creer le rapport PDF
	 */
	private static String defaultPdfReportsPathName = "Facilities Rapport/Files/Reports/PdfRepport.pdf";
	
	/**
	 * Cette fonction permet de creer le document PDF du rapport a l'emplacement par defaut
	 * @param datas La liste des donnees indexee par leur type (voir les constantes dans IWriteStrategie)
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune exception n'est geree
	 */
	public static boolean createPdf (Collection<IDataHandler> datas, ProgressBarFrame pBFrame) throws Exception{
		
		// Tout d'abord, on creer le descripteur de ficher (l'objet File)
		File pdfReport = FileUtilities.getResource(defaultPdfReportsPathName);
		// Puis on creer le fichier a l'emplacement precise precedamment
		pdfReport.createNewFile();
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pBFrame.updateBar(83);
			}
		});
		
		// On creer le Document correspondant
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		
		// Le PdfWriter precise que le document doit etre au format PDF.
		PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfReport));
		
		// On cree la strategie que l'on va utiliser pour creer le document
		IWriteStrategie writeStrategie = WriteStrategieFactory.getStrategie(WriteStrategieFactory.DEFAULT_STRATEGIE);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pBFrame.updateBar(85);
			}
		});
		
		// On effectue l'edition du PDF par la strategie
		boolean result = writeStrategie.writeDocument(datas, document, pdfWriter, pBFrame);
		
		// On n'oublie pas de fermer tous les flux
		pdfWriter.close();
		
		Desktop.getDesktop().open(pdfReport);
		
		//On renvoie le resultat
		return result;
	}
	
	/**
	 * Cette fonction permet de creer le document PDF du rapport a l'emplacement specifie
	 * @param filePathName L'emplacement dans lequel doit etre creer le template
	 * @param writeStrategieNumber La strategie d'edition choisie (voir WriteStrategieFactory pour la liste).
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune exception n'est geree
	 */
	public static boolean createtPdf (Collection<IDataHandler> datas, String reportPathName, int writeStrategieNumber) throws Exception{
		// Tout d'abord, on creer le descripteur de ficher (l'objet File)
		File pdfReport = FileUtilities.getResource(reportPathName);
		// Puis on creer le fichier a l'emplacement precise precedamment
		pdfReport.createNewFile();
		
		// On creer le Document correspondant
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		
		// Le PdfWriter precise que le document doit etre au format PDF.
		PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfReport));
		
		// On cree la strategie que l'on va utiliser pour creer le document
		IWriteStrategie writeStrategie = WriteStrategieFactory.getStrategie(writeStrategieNumber);
		
		// On effectue l'edition du PDF par défaut
		boolean result = writeStrategie.writeDocument(datas, document, pdfWriter);
		
		// On oublie pas de fermer tous les flux
		pdfWriter.close();
		
		Desktop.getDesktop().open(pdfReport);
		
		// On renvoie le resultat
		return result;		
	}
}
