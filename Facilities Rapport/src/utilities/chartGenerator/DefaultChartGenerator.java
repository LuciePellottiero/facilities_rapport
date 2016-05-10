package utilities.chartGenerator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;

public class DefaultChartGenerator implements IChartGenerator {

	@Override
	public JFreeChart generatePieChart(String title, PieDataset pieDataSet) throws Exception {
		
		JFreeChart chart = ChartFactory.createPieChart(title, pieDataSet, false, true, false);
		
		return chart;
	}

	@Override
	public JFreeChart generateBarChart(String title, String categoryAxisLabel, String valueAxisLabel, CategoryDataset barDataSet) throws Exception {

		JFreeChart chart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel, barDataSet, PlotOrientation.VERTICAL, false, true, false);
		
		return chart;
	}

}
