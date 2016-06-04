package utilities;

import java.io.File;

/**
 * Fonctions utiles pour la gestion des fichiers
 * @author Lucie PELLOTTIERO
 *
 */
public class FileUtilities {

	 /**
	 * Obtient le chemin absolu d'un fichier
	 * @param fileName Le nom relatif du fichier
	 * @return Le nom absolu du fichier
	 */ 
	public static String getResourcePath(final String fileName) { 
		// On cree un File de maniere relative
		final File f = new File("");
		// On obtient le chemin absolu auquel on ajoute le nom du fichier voulu
		final String dossierPath = f.getAbsolutePath() + File.separator + fileName; 
		return dossierPath; 
	} 
 
	
	/**
	 * Obtient l'objet File d'un fichier a partir de son nom relatif
	 * @param fileName Le nom (relatif ou absolue) du fichier
	 * @return L'objet File
	 */ 
	public static File getResource(final String fileName) { 
		// On obtient le nom absolu du fichier
		final String completeFileName = getResourcePath(fileName); 
		// On le cree a partir de ce nom
		File file = new File(completeFileName); 
		return file; 
	} 
}
