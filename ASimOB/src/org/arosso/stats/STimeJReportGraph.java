package org.arosso.stats;

import java.awt.Image;
import java.sql.SQLException;
import java.util.Vector;

import org.arosso.db.DatabaseMannager;
import org.arosso.gui.MainWindow;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class STimeJReportGraph {

	public static float media = 0;
	public static float varianza = 0;
	public static float desviacionEstandar = 0;
	
	/**
	 * Logger
	 */
	static Logger logger = LoggerFactory.getLogger(STimeJReportGraph.class);

	public Image getGraphic() {
		try {
			final XYSeries series = new XYSeries("Tiempo De Servicio");
			series.add(0, 0);
			DatabaseMannager db = DatabaseMannager.getInstance();
			db.query("SELECT (EXITTIME-ENTRYTIME) from PASSENGER");
			int tam = db.regs.size();
			logger.info("Tam regs ("+tam+"");
			logger.info(db.regs.toString());
			float promedio = 0f;
			int acumulado = 0;
			int num_pasajeros = 0;
			int num_conjuntos = 1;
			float sumatoriaX = 0;

			for (int j = 0; j < tam; j++) {
				float t_espera = Float.parseFloat(((Vector)db.regs.get(j)).get(0).toString());
				series.add(j, t_espera);
				sumatoriaX = sumatoriaX + t_espera;
				logger.info("X("+j+") Y("+t_espera+")");
			}
			// HALLAMOS LA MEDIA
			media = sumatoriaX / tam;
			System.out.print("Media: " + media);
			// HALLAMOS LA VARIANZA
			float sumatoriaV = 0;
			for (int j = 0; j < tam; j++) {
				float t_arribo = Float.parseFloat(((Vector)db.regs.get(j)).get(0).toString());
				float numerador = (t_arribo - media) * (t_arribo - media);
				sumatoriaV = sumatoriaV + (numerador / (tam - 1));
			}
			varianza = sumatoriaV;
			// HALLAMOS LA DESVIACION ESTANDAR
			desviacionEstandar = (float)Math.sqrt((double)varianza);

			final XYSeriesCollection data = new XYSeriesCollection(series);
			final JFreeChart chart = ChartFactory.createXYLineChart("Gráfico Tiempo de Servicio", "llamadas", "tiempo de servicio (seg)", data, PlotOrientation.VERTICAL, true, true, true);
			return chart.createBufferedImage(520, 400);

		} catch (Exception e) {
			logger.error("Error making ST GRAPH",e);
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			DatabaseMannager broker = DatabaseMannager.getInstance();
			broker.update("CREATE TABLE PASAJEROS ( id INTEGER IDENTITY, " + "po INTEGER , pd INTEGER, tarribo INTEGER, tembarque INTEGER, " + "tdesembarque INTEGER, ascensor INTEGER)");
			broker.update("INSERT INTO PASAJEROS ( id, po , pd, tarribo, tembarque, " + "tdesembarque, ascensor ) " + "VALUES(1,2,3,5,5,6,0)");

			broker.update("INSERT INTO PASAJEROS ( id, po , pd, tarribo, tembarque, " + "tdesembarque, ascensor ) " + "VALUES(2,2,3,5,5,6,0)");
			broker.update("INSERT INTO PASAJEROS ( id, po , pd, tarribo, tembarque, " + "tdesembarque, ascensor ) " + "VALUES(3,2,3,5,5,6,0)");
		} catch (Exception e) {
			e.printStackTrace();
		}
		STimeJReportGraph gt = new STimeJReportGraph();
		gt.getGraphic();
	}
}
