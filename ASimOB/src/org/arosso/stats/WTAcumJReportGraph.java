/*
 * Created on 12-oct-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.arosso.stats;

import java.awt.Image;
import java.sql.SQLException;
import java.util.Vector;

import org.arosso.db.DatabaseMannager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author arosso
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WTAcumJReportGraph{
	
	public static double media =0;
	public static double varianza =0;
	public static double desviacionEstandar =0;

	
	WTAcumJReportGraph(){
		super();
	}
	
	public Image getGraphic(){
        try {
			final XYSeries series = new XYSeries("Tiempo De Espera");
			DatabaseMannager db = DatabaseMannager.getInstance();
			db.regs.clear();
			//db.query("SELECT (tembarque-tarribo),tarribo from PASAJEROS");
			db.query("SELECT (ENTRYTIME-ARRIVALTIME), ARRIVALTIME from PASSENGER");
			int tam = db.regs.size();
			double promedio = 0.0;
			float acumulado = 0;
			int num_pasajeros=0;
			int num_conjuntos=1;
			Vector registros = db.regs;
			double sumatoriaX = 0;
			series.add(0,0);
			for(int j=0;j<tam;j++){
				float t_espera = Float.parseFloat(((Vector)registros.get(j)).get(0).toString());					
				float t_arribo = Float.parseFloat(((Vector)registros.get(j)).get(1).toString());
				acumulado = acumulado + t_espera; 
				series.add(t_arribo/60,acumulado);
				sumatoriaX = sumatoriaX + t_espera;	
			}
			//	HALLAMOS LA MEDIA
			media = sumatoriaX/tam;
			System.out.print("Media: "+media);
			//HALLAMOS LA VARIANZA
			double sumatoriaV =0;
			for(int j=0;j<tam;j++){	
				float t_arribo = Float.parseFloat(((Vector)registros.get(j)).get(0).toString());	
				double numerador = (t_arribo-media)*(t_arribo-media);
				sumatoriaV = sumatoriaV + (numerador/(tam-1));
			}
			varianza = sumatoriaV;
			//HALLAMOS LA DESVIACION ESTANDAR
			desviacionEstandar = Math.sqrt(varianza);
			
			final XYSeriesCollection data = new XYSeriesCollection(series);
	        final JFreeChart chart = ChartFactory.createXYLineChart(
	            "Gráfico Tiempo de Espera Acumulado",
	            "Tiempo Origen Llamada", 
	            "tiempo de espera (seg)", 
	            data,
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            true
	        );			
	        db.regs.clear();
	        return chart.createBufferedImage(520,400);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
