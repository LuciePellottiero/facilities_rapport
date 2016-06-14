package dataHandler;

import java.awt.Image;
import java.util.Collection;

import org.jfree.chart.JFreeChart;

/**
 * Decrit les classes de gestion des donnees pour le traitement du rapport PDF.
 * @author Lucie PELLOTTIERO
 *
 */
public interface IDataHandler {
	
	/**
	 * Type des donnees
	 * @author Lucie PELOTTIERRO
	 *
	 */
	public enum DataType {
		STRING, IMAGE, JFREECHART
	}

	/**
	 * Ajouter une {@link String} au stockage des donnees.
	 * @param strToAdd la {@link String} a ajouter aux donnees
	 * @param title le titre de la donnee
	 */
	public void addString (final String strToAdd, final String title);
	
	
	/**
	 * Ajouter un {@link JFreeChart} aux donnees
	 * @param chartToAdd le {@link JFreeChart} a ajouter
	 */
	public void addJFreeChart (final JFreeChart chartToAdd);
	
	/**
	 * Ajouter une {@link Image} aux donnees
	 * @param imageToAdd L'{@link Image} a ajouter aux donnees
	 */
	public void addImage (final Image imageToAdd);
	
	/**
	 * Obtenir le titre de la partie
	 * @return Le titre de la partie
	 */
	public String getPartTitle();
	
	/**
	 * Obtenir le stockage de donnee.<br>
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
