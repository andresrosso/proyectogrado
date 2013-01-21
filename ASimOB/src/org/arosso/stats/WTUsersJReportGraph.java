/*
 * Created on 12-oct-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.arosso.stats;

import java.awt.Image;
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
public class WTUsersJReportGraph{
	
	public static double media =0;
	public static double varianza =0;
	public static double desviacionEstandar =0;

	
	WTUsersJReportGraph(){
		super();
	}
	
	public Image getGraphic(){
        try {
			final XYSeries series = new XYSeries("Tiempo De Espera");
			series.add(0,0);
			DatabaseMannager db = DatabaseMannager.getInstance();
			db.regs.clear();
			//db.query("SELECT (tembarque-tarribo),tarribo from PASAJEROS");
			db.query("SELECT (ENTRYTIME-ARRIVALTIME), ARRIVALTIME from PASSENGER");
			int tam = db.regs.size();
			double promedio = 0.0;
			int acumulado = 0;
			int num_pasajeros=0;
			int num_conjuntos=1;
			Vector registros = db.regs;
			double sumatoriaX = 0;
			for(int j=0;j<tam;j++){
				float t_espera = Float.parseFloat(((Vector)registros.get(j)).get(0).toString());
				float t_arribo = Float.parseFloat(((Vector)registros.get(j)).get(1).toString());
				if( t_arribo<=(num_conjuntos*(300)) ){//Cada 5 minutos
						num_pasajeros++;
				}
				else{
					sumatoriaX = sumatoriaX + t_espera;
					series.add(num_pasajeros,sumatoriaX/num_pasajeros);
					num_pasajeros=1;
					num_conjuntos++;
				}
				if(j==(tam-1)){
					series.add(num_conjuntos*5,num_pasajeros);
					System.out.println(num_conjuntos*5+","+num_pasajeros);
				}
				
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
	        final JFreeChart chart = ChartFactory.createXYAreaChart(
	            "Gráfico Tiempo de Espera vrs Hora Llamada",
	            "Tiempo Origen Llamada", 
	            "tiempo de espera (seg)", 
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
	        db.regs.clear();
	        return chart.createBufferedImage(520,400);
	       
		} catch (Exception e) {
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
			DatabaseMannager broker = DatabaseMannager.getInstance();
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WTUsersJReportGraph gt = new WTUsersJReportGraph();
		gt.getGraphic();
	}
}
