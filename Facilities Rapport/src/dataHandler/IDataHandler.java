package dataHandler;

import java.awt.Image;
import java.util.Collection;

import org.jfree.chart.JFreeChart;

public interface IDataHandler {
	
	public static final int DATA_TYPE_STRING     = 0;
	public static final int DATA_TYPE_JFREECHART = 1;
	public static final int DATA_TYPE_IMAGE      = 2;

	public void addString (String strToAdd, String title);
	
	public void addJFreeChart (JFreeChart chartToAdd);
	
	public void addImage (Image imageToAdd);
	
	public String getPartTitle();
	
	public Collection<Collection<Object>> getDataStorage();
	
	public boolean isEmpty();
}
