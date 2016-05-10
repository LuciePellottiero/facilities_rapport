package dataHandler;

import java.util.ArrayList;
import java.util.Collection;

import org.jfree.chart.JFreeChart;

public class DefaultDataHandler implements IDataHandler {
	private String partTitle;
	
	private Collection<Collection<Object>> dataStorage;
	Collection<Object> dataTypes;
	Collection<Object> datas;
	
	public DefaultDataHandler(String partTitle) {
		this.partTitle = partTitle;
		dataStorage = new ArrayList<Collection<Object>>();
		
		dataTypes = new ArrayList<Object>();
		datas     = new ArrayList<Object>();
		
		dataStorage.add(dataTypes);
		dataStorage.add(datas);
	}

	@Override
	public void addString(String strToAdd, String title){
		dataTypes.add(DATA_TYPE_STRING);
		datas.add(title);
		datas.add(strToAdd);
	}

	@Override
	public void addJFreeChart(JFreeChart chartToAdd, String title, Collection<Object> datas){

	}

	@Override
	public String getPartTitle() {
		return partTitle;
	}

	@Override
	public Collection<Collection<Object>> getDataStorage() {
		return dataStorage;
	}
}