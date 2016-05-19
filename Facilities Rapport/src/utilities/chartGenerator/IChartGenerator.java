package utilities.chartGenerator;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;

public interface IChartGenerator {

	public JFreeChart generatePieChart(String title, PieDataset pieDataSet, Boolean legends) throws Exception;
	
	public JFreeChart generateBarChart(String title, String categoryAxisLabel, String valueAxisLabel, CategoryDataset barDataSet, Boolean legends) throws Exception;
}