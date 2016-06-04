package dataHandler;

import java.awt.Image;
import java.util.Collection;

import org.jfree.chart.JFreeChart;

/**
 * Decrit les classes de gestion des donnees pour le traitement du rapport PDF.
 * @author Lucie PELLOTIERRO
 *
 */
public interface IDataHandler {
	
	/**
	 * Type de donne String. Utilise pour la lecture des donnees.
	 */
	public static final int DATA_TYPE_STRING     = 0;
	/**
	 * Type de donne JFreeChart. Utilise pour la lecture des donnees.
	 */
	public static final int DATA_TYPE_JFREECHART = 1;
	/**
	 * Type de donne Image. Utilise pour la lecture des donnees.
	 */
	public static final int DATA_TYPE_IMAGE      = 2;

	/**
	 * Ajouter une String au stockage des donnees.
	 * @param strToAdd la String a ajouter aux donnees
	 * @param title le titre de la donnee
	 */
	public void addString (final String strToAdd, final String title);
	
	
	/**
	 * Ajouter un JFreeChart aux donnees
	 * @param chartToAdd le JFreeChart a ajouter
	 */
	public void addJFreeChart (final JFreeChart chartToAdd);
	
	/**
	 * Ajouter une Image aux donnees
	 * @param imageToAdd L'Image a ajouter aux donnees
	 */
	public void addImage (final Image imageToAdd);
	
	/**
	 * Obtenir le titre de la partie
	 * @return Le titre de la partie
	 */
	public String getPartTitle();
	
	/**
	 * Obtenir le stockage de donnee.\n
	 * Ne devrait etre utilise que pour la lecture de donnee.
	 * @return Le stockage de donnee
	 */
	public Collection<Collection<Object>> getDataStorage();
	
	/**
	 * Est-ce que le stockage de donnee est vide.
	 * @return Est-ce que le stockage est vide.
	 */
	public boolean isEmpty();
}
