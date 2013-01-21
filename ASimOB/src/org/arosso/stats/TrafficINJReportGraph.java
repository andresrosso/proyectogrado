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
public class TrafficINJReportGraph{
	
	public static double media =0;
	public static double varianza =0;
	public static double desviacionEstandar =0;
	
	TrafficINJReportGraph(){
		super();
	}
	
	public Image getGraphic(){
        try {
			final XYSeries series = new XYSeries("Trafico");
			series.add(0,0);
			DatabaseMannager db = DatabaseMannager.getInstance();
			db.query("SELECT ARRIVALTIME, ORIGINFLOOR from PASSENGER");
			int tam = db.regs.size();
			double promedio = 0.0;
			int acumulado = 0;
			int num_pasajeros=0;
			int num_conjuntos=1;
			Vector registros = db.regs;
			double sumatoriaX = 0;
			for(int j=0;j<tam;j++){
				float t_arribo = Float.parseFloat(((Vector)registros.get(j)).get(0).toString());
				int originFloor = Integer.parseInt(((Vector)registros.get(j)).get(1).toString());
				if( t_arribo<=(num_conjuntos*(300)) ){//Cada 5 minutos
					if(originFloor==0){
						num_pasajeros++;
					}
				}
				else{
					series.add(num_conjuntos*5,num_pasajeros);
					sumatoriaX = sumatoriaX + num_pasajeros;	
					num_pasajeros=1;
					num_conjuntos++;
				}
				if(j==(tam-1)){
					series.add(num_conjuntos*5,num_pasajeros);
					System.out.println(num_conjuntos*5+","+num_pasajeros);
				}
				
			}
			//HALLAMOS LA MEDIA
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
	            "Tiempo (min)",
	            "Tasa De Arribo De Entrada", 
	            "LLamadas Generadas", 
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
        //series.add(1.0, 500.2);
        //7series.add(5.0, 694.1);
        //series.add(4.0, 100.0);
        //series.add(12.5, 734.4);
	}
	
	/*public static void main(String[] args) {
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
		TasaArriboJReportGraph gt = new TasaArriboJReportGraph();
		gt.getGraphic();
	}
	
	public Image getGraphic2(){
        try {
			final XYSeries series = new XYSeries("Trafico");
			//series.add(0,0);
			HSQLDBbroker db = HSQLDBbroker.getInstantance();
			 db.registros.clear();
			double promedio = 0.0;
			int acumulado = 0;
			int num_pasajeros=0;
			int num_conjuntos=1;
			long tiempo = Cronometro.getInstance().getTiempo();
			int franja = 1;
			
			for(int k=0;k<tiempo;k=(franja-1)*Constantes.getInstance().t_arribo){
			db.query("SELECT tarribo from LLAMADAS where tarribo > "+k+" AND tarribo < "+franja*Constantes.getInstance().t_arribo);
			int tam = db.registros.size();
			Vector registros = db.registros;
			double sumatoriaX = 0;
			series.add((franja-1)*Constantes.getInstance().t_arribo/60,tam);		
			franja++;
			db.registros.clear();
			}
			/*
			//HALLAMOS LA MEDIA
			media = sumatoriaX/tam;
			System.out.print("Media: "+media);
			//HALLAMOS LA VARIANZA
			double sumatoriaV =0;
			for(int j=0;j<tam;j++){	
				int t_arribo = Integer.parseInt(((Vector)registros.get(j)).get(0).toString());
				double numerador = (t_arribo-media)*(t_arribo-media);
				sumatoriaV = sumatoriaV + (numerador/(tam-1));
			}
			varianza = sumatoriaV;
			//HALLAMOS LA DESVIACION ESTANDAR
			desviacionEstandar = Math.sqrt(varianza);
			
			final XYSeriesCollection data = new XYSeriesCollection(series);
	        final JFreeChart chart = ChartFactory.createXYStepAreaChart(
	            "Tasa De Arribo",
	            "Tiempo (min)", 
	            "Numero De Llamadas", 
	            data,
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            true
	        );
	       XYPlot plot = (XYPlot)chart.getPlot();
	        ValueAxis axis = plot.getRangeAxis();
	        TickUnits units = (TickUnits)NumberAxis.createIntegerTickUnits();
	        axis.setStandardTickUnits(units);
	        axis.setTickMarksVisible(true);
	        //chart.getRenderingHints().
	        //System.out.print("............"+axis.getLabel());
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
	}*/
}
