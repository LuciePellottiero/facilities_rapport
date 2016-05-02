package Utilitie;

import java.io.File;

public class FileRessource {

	 /**This function get the absolute file name. 
	 * @param fileName The relative file name 
	 * @return The absolute file name 
	 */ 
	public static String getResourcePath(String fileName) { 
		 final File f = new File(""); 
		 final String dossierPath = f.getAbsolutePath() + File.separator + fileName; 
		 return dossierPath; 
	} 
 
	
	/**This function get the File object from the relative file name. 
	 * @param fileName The relative file name 
	 * @return The File object 
	 */ 
	public static File getResource(String fileName) { 
		final String completeFileName = getResourcePath(fileName); 
		File file = new File(completeFileName); 
		return file; 
	} 
}
