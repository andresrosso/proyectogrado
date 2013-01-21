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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import sistema.tasks.Cronometro;
import util.Constantes;
import util.HSQLDBbroker;

/**
 * @author arosso
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TiempoEsperaXfranjasJReportGraph{
	
	public static double media =0;
	public static double varianza =0;
	public static double desviacionEstandar =0;

	
	TiempoEsperaXfranjasJReportGraph(){
		super();
	}
	
	public Image getGraphic(){
        try {
        	final XYSeries series = new XYSeries("Trafico");
			HSQLDBbroker db = HSQLDBbroker.getInstantance();
			 db.registros.clear();
			double promedio = 0.0;
			int acumulado = 0;
			int num_pasajeros=0;
			int num_conjuntos=1;
			long tiempo = Cronometro.getInstance().getTiempo();
			int franja = 1;
			double sumatoriaX = 0;
			int t_espera = 0;
			
			for(int k=0;k<tiempo;k=(franja-1)*Constantes.getInstance().t_arribo){
			db.query("SELECT (tembarque-tarribo) from PASAJEROS where tarribo > "+k+" AND tarribo < "+franja*Constantes.getInstance().t_arribo);
			int tam = db.registros.size();
			Vector registros = db.registros;
			for(int j=0;j<registros.size();j++){
			t_espera = t_espera + Integer.parseInt(((Vector)registros.get(j)).get(0).toString());
			}
			series.add((franja)*Constantes.getInstance().t_arribo/60,t_espera/tam);		
			franja++;
			db.registros.clear();
			t_espera = 0;
			}
			
			final XYSeriesCollection data = new XYSeriesCollection(series);
	        final JFreeChart chart = ChartFactory.createXYBarChart(
	            "Gráfico Media Tiempo de Espera X Franjas",
	            "Tiempo Origen Llamada", 
	            false,
	            "media tiempo de espera (seg)", 
	            data,
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            true
	        );
	        //	      change the auto tic
	        //Numeros Fijos
			//7CategoryPlot plot = chart.getCategoryPlot();
			//set the range axis to display integers only...
			//final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			//rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());					
	        db.registros.clear();
	        return chart.createBufferedImage(520,400);
	       
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
        //series.add(1.0, 500.2);
        //7series.add(5.0, 694.1);
        //series.add(4.0, 100.0);
        //series.add(12.5, 734.4);
	}
	public static void main(String[] args) {
		try {
			HSQLDBbroker broker = HSQLDBbroker.getInstantance();
			broker.update("CREATE TABLE PASAJEROS ( id INTEGER IDENTITY, " +
					"po INTEGER , pd INTEGER, tarribo INTEGER, tembarque INTEGER, " +
					"tdesembarque INTEGER, ascensor INTEGER)");
			broker.update(
					"INSERT INTO PASAJEROS ( id, po , pd, tarribo, tembarque, " +
					"tdesembarque, ascensor ) " +
					"VALUES(1,2,3,5,5,6,0)");
			
			broker.update(
					"INSERT INTO PASAJEROS ( id, po , pd, tarribo, tembarque, " +
					"tdesembarque, ascensor ) " +
					"VALUES(2,2,3,5,5,6,0)");
			broker.update(
					"INSERT INTO PASAJEROS ( id, po , pd, tarribo, tembarque, " +
					"tdesembarque, ascensor ) " +
					"VALUES(3,2,3,5,5,6,0)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		graficoTiempoEspera_acumXfranjas gt = new graficoTiempoEspera_acumXfranjas();
		gt.getGraphic();
	}
}
