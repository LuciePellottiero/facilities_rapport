package utilities.chartGenerator;

import java.awt.Font;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.TextAnchor;

public class DefaultChartGenerator implements IChartGenerator {

	@Override
	public JFreeChart generatePieChart(String title, PieDataset pieDataSet, Boolean legends) throws Exception {
		
		JFreeChart chart = ChartFactory.createPieChart(title, pieDataSet, legends, true, false);
		
		return chart;
	}

	@Override
	public JFreeChart generateBarChart(String title, String categoryAxisLabel, String valueAxisLabel, 
			CategoryDataset barDataSet, Boolean legends, Boolean fontToFit) throws Exception {
		
		JFreeChart chart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel, barDataSet,
				PlotOrientation.VERTICAL, legends, true, false);
		
		CategoryPlot plot = chart.getCategoryPlot(); 
		CategoryAxis domainAxis = plot.getDomainAxis();
		
		if (fontToFit) {	 
			Font font = new Font("Dialog", Font.PLAIN, 8);
			domainAxis.setTickLabelFont(font);
			domainAxis.setMaximumCategoryLabelWidthRatio(22);
		}
		
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator(); 
		renderer.setBaseItemLabelGenerator(generator); 
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.INSIDE12, TextAnchor.TOP_CENTER)); 
		renderer.setPositiveItemLabelPositionFallback(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		
		return chart;
	}

}
