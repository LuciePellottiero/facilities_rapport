package utilities.chartGenerator;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;

/**
 * Description des generateurs de graphe.<br>
 * Permet a l'heure actuelle de generer des graphes camemberts et en barres.
 * @author Lucie PELLOTTIERO
 *
 */
public interface IChartGenerator {

	/**
	 * Genere un graphe camembert.
	 * @param title Le titre du graphe
	 * @param pieDataSet Les donnees du graphe (sense etre un pourcentage (0 a 1))
	 * @param legends Afficher la legende ou non
	 * @return Le JFreeChart camembert genere
	 * @throws Exception Aucune Exception n'est attrapee
	 */
	public JFreeChart generatePieChart(String title, PieDataset pieDataSet, Boolean legends) throws Exception;
	
	/**
	 * Genere un graphe en barre.
	 * @param title Le titre du graphe
	 * @param categoryAxisLabel Le titre de l'axe des categorie (axe X)
	 * @param valueAxisLabel Le titre de l'axe des valeurs (axe Y)
	 * @param barDataSet Les donnees du graphe en barre
	 * @param legends Afficher la legende ou non
	 * @param fontToFit Indique si on doit changer la taille ainsi que le ratio d'affichage :<br>
	 * - > 0 : prend la valeur de fontToFit comme taille de Font et modifie le ratio d'affichage<br>
	 * - < 0 : laisse la font et le ratio d'affichage par defaut<br>
	 * ratio d'affichage : ratio place/caractere a afficher
	 * @return Le JFreeChart en barre genere
	 * @throws Exception Aucune Exception n'est attrapee
	 */
	public JFreeChart generateBarChart(String title, String categoryAxisLabel, String valueAxisLabel, CategoryDataset barDataSet,
			Boolean legends, int fontToFit) throws Exception;
}
