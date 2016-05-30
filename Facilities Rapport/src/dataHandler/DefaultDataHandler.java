package dataHandler;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;

import org.jfree.chart.JFreeChart;

/**
 * Cette classe permet de gerer les String, les Image et les JFreeChart lors de la creation du rapport PDF./n
 * Par défaut, un DefaultDataHandler représente une partie du rapport.
 * @author Lucie PELLOTIERRO
 *
 */
public class DefaultDataHandler implements IDataHandler {
	/**
	 * Le titre de la partie
	 */
	private final String partTitle;
	
	/**
	 * Les données stockees (dataTypes et datas)
	 */
	private final Collection<Collection<Object>> dataStorage;
	/**
	 * Les types de chaque donnee. Il y en a un part type. Cela permet de savoir le traitement a effectuer pour cette donnee.
	 */
	private final Collection<Object> dataTypes;
	/**
	 * Les donnees.
	 */
	private final Collection<Object> datas;
	
	/**
	 * Le constructeur
	 * @param partTitle Le titre de la partie
	 */
	public DefaultDataHandler(final String partTitle) {
		// Initialisations
		this.partTitle = partTitle;
		dataStorage = new ArrayList<Collection<Object>>();
		
		dataTypes = new ArrayList<Object>();
		datas     = new ArrayList<Object>();
		
		// Creation de la matrice
		dataStorage.add(dataTypes);
		dataStorage.add(datas);
	}

	@Override
	public void addString(final String strToAdd, final String title){
		// Par defaut, lors de l'ajout de String, on ajoute d'abore le titre suivi de la String avec une seule entree dans dataTypes.
		dataTypes.add(DATA_TYPE_STRING);
		datas.add(title);
		datas.add(strToAdd);
	}

	@Override
	public void addJFreeChart(final JFreeChart chartToAdd){

		dataTypes.add(DATA_TYPE_JFREECHART);
		datas.add(chartToAdd);
	}

	@Override
	public void addImage(final Image imageToAdd) {
		dataTypes.add(DATA_TYPE_IMAGE);
		datas.add(imageToAdd);
	}

	@Override
	public String getPartTitle() {
		return partTitle;
	}

	@Override
	public Collection<Collection<Object>> getDataStorage() {
		return dataStorage;
	}

	@Override
	public boolean isEmpty() {
		return (dataTypes.isEmpty() && datas.isEmpty());
	}
}
