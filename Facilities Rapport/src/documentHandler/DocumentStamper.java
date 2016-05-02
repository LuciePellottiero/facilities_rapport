package documentHandler;

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
 * Cette classe devait permettre l'utilisation de template mais a ete abandonne car on a besoin de creer certains elements dynamiquements
 * @author Lucie PELLOTIERRO
 *
 */
public class DocumentStamper {
	
	/**
	 * Chemin vers le template PDF par defaut
	 */
	private static String defaultPdfTemplatePathName = "Facilities Rapport/Files/Templates/DefaultPdfTemplate.pdf";
	
	/**
	 * Chemin vers l'emplacement par defaut du rapport PDF
	 */
	private static String defaultPdfReportPathName   = "Facilities Rapport/Files/Reports/PdfRepport.pdf";
	
	/**
	 * Cette fonction permet de creer le rapport PDF par defaut a partir du template PDF par defaut
	 * @param datas Les donnees a mettre dans le rapport
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est geree
	 */
	public static boolean pdfStampe (Map<String, List<Float>> datas) throws Exception{
		
		
		// Ceci est le PdfReader qui permet d'obtenir le template
		PdfReader reader = new PdfReader(defaultPdfTemplatePathName);
		
		// Ceci est le PdfStamper qui permet d'ecrire dans un nouveau fichier par dessus le PdfReader
        PdfStamper stamper = new PdfStamper(reader,
          new FileOutputStream(defaultPdfReportPathName)); 
        
        return pdfStamper (datas, stamper);
	}
	
	/**
	 * Permet de creer le rapport PDF avec le nom precise a partir du template PDF par defaut
	 * @param datas Les donnees a mettre dans le rapport
	 * @param reportPathName le nom du rapport PDF
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est geree
	 */
	public static boolean pdfStampe (Map<String, List<Float>> datas, String reportPathName) throws Exception{
		
		// Ceci est le PdfReader qui permet d'obtenir le template
		PdfReader reader = new PdfReader(defaultPdfTemplatePathName);
		
		// Ceci est le PdfStamper qui permet d'ecrire dans un nouveau fichier par dessus le PdfReader
        PdfStamper stamper = new PdfStamper(reader,
          new FileOutputStream(reportPathName)); 
        
        return pdfStamper (datas, stamper);
	}
	
	/**
	 * Permet de creer le rapport PDF avec le nom precise a partir du template PDF precise
	 * @param datas Les donnees a mettre dans le rapport
	 * @param reportPathName le nom du rapport PDF
	 * @param templatePathName Le nom du template PDF
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est geree
	 */
	public static boolean pdfStampe (Map<String, List<Float>> datas, String reportPathName, String templatePathName) throws Exception{
		
		// Ceci est le PdfReader qui permet d'obtenir le template
		PdfReader reader = new PdfReader(templatePathName); 
		
		// Ceci est le PdfStamper qui permet d'ecrire dans un nouveau fichier par dessus le PdfReader
        PdfStamper stamper = new PdfStamper(reader,
          new FileOutputStream(reportPathName)); 
        
        return pdfStamper (datas, stamper);
	}
	
	/**
	 * Fonction permettant de mettre les donnees fournies dans le document PDF
	 * @param datas Les donnees a mettre dans le rapport
	 * @param stamper le PdfStamper fournis pour ecrire dans le PDF
	 * @return true si reussi, false sinon
	 * @throws Exception Aucune Exception n'est geree
	 */
	private static boolean pdfStamper (Map<String, List<Float>> datas, PdfStamper stamper) throws Exception {
		// On parametre la font que l'on veut utiliser
        BaseFont bf = BaseFont.createFont(
                BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED); 
	
        // Pour iterer sur les donnees qui sont dans une map, on recupere un Iterator du type d'entrée de la map
        Iterator<Entry<String, List<Float>>> iterator = datas.entrySet().iterator();
        
        // Tant que l'on a une element suivant
		while (iterator.hasNext()) {
			//On prend l'element suivant
			Map.Entry<String, List<Float>> data = iterator.next();
			
			//On souhaite ecrire par dessus le template a la page 1
			PdfContentByte over = stamper.getOverContent(1);
			
			// On commence la modification
			over.beginText();
			// On parametre la font et la taille
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
