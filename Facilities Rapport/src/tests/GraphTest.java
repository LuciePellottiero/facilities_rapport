package tests;

import java.awt.Graphics2D;
import java.io.FileOutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Cette class teste la generation de graphe dans les PDF
 * @author Lucie PELLOTTIERO
 *
 */
public abstract class GraphTest {
	
	/**
	 * Permet de generer un exemple de graphe camembert
	 * @return Un JFreeChart sous forme de camembert
	 */
	public static JFreeChart generatePieChart() {
        DefaultPieDataset dataSet = new DefaultPieDataset();
        dataSet.setValue("China", 19.64);
        dataSet.setValue("India", 17.3);
        dataSet.setValue("United States", 4.54);
        dataSet.setValue("Indonesia", 3.4);
        dataSet.setValue("Brazil", 2.83);
        dataSet.setValue("Pakistan", 2.48);
        dataSet.setValue("Bangladesh", 2.38);
 
        JFreeChart chart = ChartFactory.createPieChart(
                "World Population by countries", dataSet, true, true, false);
 
        return chart;
    }
 
	/**
	 * Permet de generer un exemple de graphe en bares
	 * @return Un JFreeChart sous forme de bares
	 */
    public static JFreeChart generateBarChart() {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        dataSet.setValue(791, "Population", "1750 AD");
        dataSet.setValue(978, "Population", "1800 AD");
        dataSet.setValue(1262, "Population", "1850 AD");
        dataSet.setValue(1650, "Population", "1900 AD");
        dataSet.setValue(2519, "Population", "1950 AD");
        dataSet.setValue(6070, "Population", "2000 AD");
 
        JFreeChart chart = ChartFactory.createBarChart(
                "World Population growth", "Year", "Population in millions",
                dataSet, PlotOrientation.VERTICAL, false, true, false);
 
        return chart;
    }
    
    /**
     * Permet de creer un PDF et d'y inserer un graphe
     * @param chart Le JFreeChart a dessiner
     * @param width La largeur du JFreeChart a dessiner
     * @param height La hauteur du JFreeChart a dessiner
     * @param fileName Le nom (donc le chemin) que doit prendre le fichier a generer
     */
    public static void writeChartToPDF(JFreeChart chart, int width, int height, String fileName) {
        PdfWriter writer = null;
     
        Document document = new Document();
     
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(
                    fileName));
            document.open();
            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template = contentByte.createTemplate(width, height);
            
			Graphics2D graphics2d = new PdfGraphics2D(template, width, height);
            
            java.awt.geom.Rectangle2D rectangle2d = new java.awt.geom.Rectangle2D.Double(0, 0, width,
                    height);
     
            chart.draw(graphics2d, rectangle2d);
             
            graphics2d.dispose();
            contentByte.addTemplate(template, 0, 0);
     
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        document.close();
    }

    /**
     * Permet de creer 2 PDF avec chaqun un graphe different mais avec les memes donnees
     * @param args Inutilises
     */
	public static void main(String[] args) {
		writeChartToPDF(generateBarChart(), 500, 400, "barchart.pdf");
	    writeChartToPDF(generatePieChart(), 500, 400, "piechart.pdf");
	}

}
