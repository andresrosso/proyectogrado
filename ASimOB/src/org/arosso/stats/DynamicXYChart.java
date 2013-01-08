package org.arosso.stats;


import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class DynamicXYChart {

	// Create a chart:
	private Chart2D chart;
	// Create an ITrace:
	// Note that dynamic charts need limited amount of values!!!
	ITrace2D trace;

	public DynamicXYChart(Color color, String title) {
		chart = new Chart2D();
		trace = new Trace2DLtd(title);
		trace.setColor(color);
		// Add the trace to the chart. This has to be done before adding points
		// (deadlock prevention):
		chart.addTrace(trace);
		chart.setSize(400, 400);
	}

	public Chart2D getChart() {
		return chart;
	}

	public void setData(double x, double y) {
		trace.addPoint(x,y);
	}

	public static void main(String[] args) {
		// Create a frame.
		JFrame frame = new JFrame("MinimalDynamicChart");
		final DynamicXYChart xyChart = new DynamicXYChart(Color.RED,"test");
		// add the chart to the frame:
		frame.getContentPane().add(xyChart.getChart());
		frame.setSize(400, 300);
		// Enable the termination button [cross on the upper right edge]:
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		// Get data from other thread
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {

			private double m_y = 0;
			private long m_starttime = System.currentTimeMillis();

			/**
			 * @see java.util.TimerTask#run()
			 */
			@Override
			public void run() {
				// This is just computation of some nice looking value.
				double rand = Math.random();
				boolean add = (rand >= 0.5) ? true : false;
				this.m_y = (add) ? this.m_y + Math.random() : this.m_y- Math.random();
				// This is the important thing: Point is added from separate
				// Thread.
				xyChart.setData(((double) System.currentTimeMillis() - this.m_starttime), this.m_y);
			}

		};
		// Every 20 milliseconds a new value is collected.
		timer.schedule(task, 1000, 20);
	}
}
