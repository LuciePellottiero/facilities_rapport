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

/**
 * Generateur par defaut de graphe
 * @author Lucie PELLOTTIERO
 *
 */
public class DefaultChartGenerator implements IChartGenerator {

	@Override
	public JFreeChart generatePieChart(String title, PieDataset pieDataSet, Boolean legends) throws Exception {
		
		// Genere un graphe camembert par defaut avec affichage de tooltips si disponnibles mais pas d'urls
		JFreeChart chart = ChartFactory.createPieChart(title, pieDataSet, legends, true, false);
		
		return chart;
	}

	@Override
	public JFreeChart generateBarChart(String title, String categoryAxisLabel, String valueAxisLabel, 
			CategoryDataset barDataSet, Boolean legends, int fontToFit) throws Exception {
		
		// Genere un graphe en barre par defaut avec affichage des tooltips mais pas des urls
		JFreeChart chart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel, barDataSet,
				PlotOrientation.VERTICAL, legends, true, false);
		
		// Obtient les categories
		CategoryPlot plot = chart.getCategoryPlot();
		// Obtient l'axe des domaines
		CategoryAxis domainAxis = plot.getDomainAxis();
		
		// Si on demande de mettre une Font particuliere
		if (fontToFit > 0) {	 
			
			// Creer a la taille demandee
			final Font font = new Font("Dialog", Font.PLAIN, fontToFit);
			// Modifie la Font de l'axe des domaines
			domainAxis.setTickLabelFont(font);
			// Generate JFreeChart with anti-aliasing
			chart.setAntiAlias(true);
			domainAxis.setUpperMargin(0.00);
			domainAxis.setLowerMargin(0.00);
			// Fixe arbitrairement (evedntuellement a revoir), le ratio place disponnible/charactere affiche
			domainAxis.setMaximumCategoryLabelWidthRatio(1.0f);
		}
		
		// Modifie le rendu des barres
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		// Modifie le Label affiche dans les barres (standard)
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator(); 
		// Ajoute le generateur de label dans les barres
		renderer.setBaseItemLabelGenerator(generator); 
		// Affiche les Label dans les barres
		renderer.setBaseItemLabelsVisible(true);
		// Place les Label en haut au centre des barres
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.INSIDE12, TextAnchor.TOP_CENTER)); 
		// S'il n'y a pas la place, on les places juste au dessu des barres
		renderer.setPositiveItemLabelPositionFallback(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		
		return chart;
	}

}
