/*
 * Created on 12-oct-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.arosso.stats;

import java.awt.Image;

import org.arosso.model.BuildingModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author arosso
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TEnergyJReportGraph {

	public static double acumulado = 0;
	public static double media = 0;
	public static double varianza = 0;
	public static double desviacionEstandar = 0;

	public Image getGraphic() {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		BuildingModel edificio;
		try {
			edificio = BuildingModel.getInstance();
			double sumatoriaX = 0;
			for (int j = 0; j < edificio.getNumElevators(); j++) {
				dataset.addValue(edificio.getElevators().get(j).getFloorsRunned(), "Distancia(pisos)", "[" + j + "]");
				sumatoriaX = sumatoriaX + edificio.getElevators().get(j).getFloorsRunned();
			}
			acumulado = sumatoriaX;
			double promedio = 0.0;
			int acumulado = 0;
			int num_pasajeros = 0;
			int num_conjuntos = 1;
			// HALLAMOS LA MEDIA
			media = sumatoriaX / edificio.getNumElevators();
			// HALLAMOS LA VARIANZA
			double sumatoriaV = 0;
			for (int j = 0; j < edificio.getNumElevators(); j++) {
				double numerador = (edificio.getElevators().get(j).getFloorsRunned() - media) * (edificio.getElevators().get(j).getFloorsRunned() - media);
				sumatoriaV = sumatoriaV + (numerador / (edificio.getNumElevators() - 1));
			}
			varianza = sumatoriaV;
			// HALLAMOS LA DESVIACION ESTANDAR
			desviacionEstandar = Math.sqrt(varianza);
			JFreeChart chart = ChartFactory.createBarChart("Comparación Energía Consumida", "Ascensor", "Distancia(pisos)", dataset, PlotOrientation.VERTICAL, false, true, false);
			return chart.createBufferedImage(520, 400);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
