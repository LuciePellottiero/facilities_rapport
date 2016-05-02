package utilities;

import java.io.File;

/**
 * Contient les fonctions utiles pour la gestion des fichiers
 * @author tmedard
 *
 */
public class FileUtilities {

	 /**
	 * Cette fonction permet d'obtenir le chemin absolue d'un fichier
	 * @param fileName Le nom relatif du fichier
	 * @return Le nom absolue du fichier
	 */ 
	public static String getResourcePath(String fileName) { 
		// On cree un File de maniere relative
		final File f = new File("");
		// On obtient le chemin absolue auquel on ajoute le nom du fichier voulu
		final String dossierPath = f.getAbsolutePath() + File.separator + fileName; 
		return dossierPath; 
	} 
 
	
	/**
	 * Cette fonction permet d'obtenir le File d'un fichier a partir de son nom relatif
	 * @param fileName Le nom relatif du fichier
	 * @return L'objet File
	 */ 
	public static File getResource(String fileName) { 
		// On obtient le nom absolue du fichier
		final String completeFileName = getResourcePath(fileName); 
		// On le cree a partir de ce nom
		File file = new File(completeFileName); 
		return file; 
	} 
}
