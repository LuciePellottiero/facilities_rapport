package dataHandler;

import java.util.Collection;

import org.jfree.chart.JFreeChart;

public interface IDataHandler {
	
	public static final int DATA_TYPE_STRING = 0;

	public void addString (String strToAdd, String title);
	
	public void addJFreeChart (JFreeChart chartToAdd, String title, Collection<Object> datas);
	
	public String getPartTitle();
	
	public Collection<Collection<Object>> getDataStorage();
}