package documentHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * Permet l'utilisation de template mais a ete abandonne car on a besoin de creer certains elements dynamiquements.
 * @author Lucie PELLOTTIERO
 * @deprecated
 */
public class DocumentStamper {
	
	/**
	 * Chemin vers le template PDF par defaut.
	 */
	private final static String DEFAULT_PDF_TEMPLATE_PATH = "Facilities Rapport" + File.separator + "Files" + File.separator +
			"Templates" + File.separator;
	
	/**
	 * Nom du template pdf par defaut.
	 */
	private final static String DEFAULT_PDF_TEMLPATE_NAME = "DefaultPdfTemplate.pdf";
	
	/**
	 * Chemin vers l'emplacement par defaut du rapport PDF.
	 */
	private final static String DEFAULT_REPORT_PATH   = "Facilities Rapport" + File.separator + "Files" + File.separator + 
			"Reports";
	
	/**
	 * Nom par defaut du rapport PDF.
	 */
	private final static String DEFAULT_REPORT_NAME = "PdfRepport.pdf";
	
	/**
	 * Creer le rapport PDF par defaut a partir du template PDF par defaut
	 * @param datas Les donnees a mettre dans le rapport
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est attrape
	 */
	public static boolean pdfStampe (Map<String, List<Float>> datas) throws Exception{
		
		
		// Le PdfReader qui permet d'obtenir le template
		PdfReader reader = new PdfReader(DEFAULT_PDF_TEMPLATE_PATH + File.separator + DEFAULT_PDF_TEMLPATE_NAME);
		
		// Le PdfStamper qui permet d'ecrire dans un nouveau fichier par dessus le PdfReader
        PdfStamper stamper = new PdfStamper(reader,
          new FileOutputStream(DEFAULT_REPORT_PATH + File.separator + DEFAULT_REPORT_NAME)); 
        
        return pdfStamper (datas, stamper);
	}
	
	/**
	 * Creer le rapport PDF avec le nom precise a partir du template PDF par defaut
	 * @param datas Les donnees a mettre dans le rapport
	 * @param reportPathName le nom du rapport PDF
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est attrape
	 */
	public static boolean pdfStampe (Map<String, List<Float>> datas, String reportPathName) throws Exception{
		
		// Le PdfReader qui permet d'obtenir le template
		PdfReader reader = new PdfReader(DEFAULT_PDF_TEMPLATE_PATH + File.separator + DEFAULT_PDF_TEMLPATE_NAME);
		
		// Le PdfStamper qui permet d'ecrire dans un nouveau fichier par dessus le PdfReader
        PdfStamper stamper = new PdfStamper(reader,
          new FileOutputStream(reportPathName)); 
        
        return pdfStamper (datas, stamper);
	}
	
	/**
	 * Creer le rapport PDF avec le nom precise a partir du template PDF precise
	 * @param datas Les donnees a mettre dans le rapport
	 * @param reportPathName le nom du rapport PDF
	 * @param templatePathName Le nom du template PDF
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est attrape
	 */
	public static boolean pdfStampe (Map<String, List<Float>> datas, String reportPathName, String templatePathName) throws Exception{
		
		// Le PdfReader qui permet d'obtenir le template
		PdfReader reader = new PdfReader(templatePathName); 
		
		// Le PdfStamper qui permet d'ecrire dans un nouveau fichier par dessus le PdfReader
        PdfStamper stamper = new PdfStamper(reader,
          new FileOutputStream(reportPathName)); 
        
        return pdfStamper (datas, stamper);
	}
	
	/**
	 * Met les donnees fournies dans le document PDF
	 * @param datas Les donnees a mettre dans le rapport
	 * @param stamper le PdfStamper fournis pour ecrire dans le PDF
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est attrape
	 */
	private static boolean pdfStamper (Map<String, List<Float>> datas, PdfStamper stamper) throws Exception {
		// On parametre la BaseFont que l'on veut utiliser
        BaseFont bf = BaseFont.createFont(
                BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED); 
	
        // Pour iterer sur les donnees qui sont dans une Map, on recupere un Iterator du type d'Entree de la Map
        Iterator<Entry<String, List<Float>>> iterator = datas.entrySet().iterator();
        
        // Tant que l'on a une element suivant
		while (iterator.hasNext()) {
			//On prend l'element suivant
			Map.Entry<String, List<Float>> data = iterator.next();
			
			//On souhaite ecrire par dessus le template a la page 1
			PdfContentByte over = stamper.getOverContent(1);
			
			// On commence la modification
			over.beginText();
			// On parametre la Font et la taille
            over.setFontAndSize(bf, 10);   
            // On rentre les coordonnees x et y sachant que 0, 0 est en haut a gauche et que l'unite est en unite PDF
            over.setTextMatrix(data.getValue().get(0), data.getValue().get(1));   
            
            // On met le texte souhaite
            over.showText(data.getKey()); 
            // On arrete les modifications
            over.endText();
            
            //On les appliques (je crois que cela sert a ca)
            over.stroke();
		}
		
		// Une fois que l'on a termine, on ferme les flux
		stamper.close();
		stamper.getReader().close();
		
		return true;
	}
}
