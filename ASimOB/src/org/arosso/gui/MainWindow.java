package org.arosso.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import org.arosso.model.BuildingModel;
import org.arosso.model.Passenger;
import org.arosso.stats.DynamicXYChart;
import org.arosso.stats.StatisticsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainWindow extends Thread implements ActionListener, Observer {

	Thread hilo;
	private JFrame frmBuildingSimulator;
	private GuiController controller;
	private GuiModel guiModel;
	public boolean simulation;

	// Sunchronized components
	JLabel lblTiempo;
	JTable canvas;

	// Actions
	JButton initButton;
	JButton pauseButton;
	JButton stopButton;
	JButton genWTReportpButton;
	JButton genSTReportpButton;
	JButton genTEnergyReportpButton;

	// Dynamic charts
	DynamicXYChart wtChart;
	DynamicXYChart stChart;
	DynamicXYChart trafficChart;

	// Simulation statistics
	StatisticsManager statisticsManager;
	Float lastExecTime = 0f;
	int MAX_EXEC_COUNTER = 5;
	int execCounter = 0;

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Create the application.
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public MainWindow() throws IOException, Exception {
		try {
			guiModel = new GuiModel();
			statisticsManager = StatisticsManager.getInstance();
			statisticsManager.addObserver(this);
			initialize();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBuildingSimulator = new JFrame();
		frmBuildingSimulator.getContentPane().setBackground(Color.WHITE);
		frmBuildingSimulator.setTitle("Building Simulator");
		frmBuildingSimulator.setBackground(Color.GRAY);
		frmBuildingSimulator.setBounds(0, 0, 640, 480);
		frmBuildingSimulator.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frmBuildingSimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBuildingSimulator.getContentPane().setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		JPanel simPanel = new JPanel();
		tabbedPane.addTab("Simulation", null, simPanel, null);

		JPanel simInformation = new JPanel();
		simInformation.setLayout(new BoxLayout(simInformation, BoxLayout.Y_AXIS));

		JPanel summaryPanel = new JPanel();
		summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.X_AXIS));
		simInformation.add(summaryPanel);
		lblTiempo = new JLabel("Tiempo");
		summaryPanel.add(lblTiempo);

		JPanel simActionButtonsPanel = new JPanel();
		simInformation.add(simActionButtonsPanel);
		simActionButtonsPanel.setLayout(new BoxLayout(simActionButtonsPanel, BoxLayout.X_AXIS));

		initButton = new JButton("Init");
		initButton.addActionListener(this);
		simActionButtonsPanel.add(initButton);

		pauseButton = new JButton("Pause");
		pauseButton.setEnabled(false);
		pauseButton.addActionListener(this);
		simActionButtonsPanel.add(pauseButton);

		stopButton = new JButton("Stop");
		stopButton.setEnabled(false);
		stopButton.addActionListener(this);
		simActionButtonsPanel.add(stopButton);

		// Add Dynamic chartS
		// Waiting time chart
		JPanel dynamicChartsPanel = new JPanel();
		dynamicChartsPanel.setLayout(new BoxLayout(dynamicChartsPanel, BoxLayout.Y_AXIS));
		simInformation.add(dynamicChartsPanel);
		// Traffic chart
		trafficChart = new DynamicXYChart(Color.BLUE, "Traffic");
		dynamicChartsPanel.add(trafficChart.getChart());
		// Waiting time chart
		wtChart = new DynamicXYChart(Color.RED, "Wating Time");
		dynamicChartsPanel.add(wtChart.getChart());
		// Service time chart
		//stChart = new DynamicXYChart(Color.BLUE, "Service Time");
		//dynamicChartsPanel.add(stChart.getChart());

		JPanel simGraphics = new JPanel();
		simGraphics.setLayout(new BorderLayout(0, 0));
		simGraphics.setAutoscrolls(true);
		simGraphics.setBorder(BorderFactory.createLineBorder(Color.black));

		// Elevator MATRIX MODEL
		ElevatorTableModel elevatorModel = new ElevatorTableModel(guiModel);
		elevatorModel.setRowCount(guiModel.getNumFloors());
		elevatorModel.setColumnCount(guiModel.getNumElevators() + 1);
		canvas = new JTable(elevatorModel);
		// FLOOR MODEL
		canvas.getColumnModel().getColumn(0).setCellRenderer(new FloorCellRenderer());
		// ELEVATOR COLUMN MODEL
		for (int j = 1; j <= guiModel.getNumElevators(); j++) {
			canvas.getColumnModel().getColumn(j).setCellRenderer(new ElevatorCellRenderer());
		}

		simGraphics.add(canvas);
		simPanel.setLayout(new BoxLayout(simPanel, BoxLayout.X_AXIS));
		simPanel.add(simInformation);
		JScrollPane simGraphicsSP = new JScrollPane(simGraphics);
		simPanel.add(simGraphicsSP);

		// Stats TAB
		JPanel statsPanel = new JPanel();
		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.X_AXIS));
		tabbedPane.addTab("Statistics", null, statsPanel, null);
		// Stats container
		JPanel statsConPanel = new JPanel();
		statsConPanel.setLayout(new BoxLayout(statsConPanel, BoxLayout.Y_AXIS));

		//Stats panel for buttons
		JPanel statsActionButtonsPanel = new JPanel();
		statsActionButtonsPanel.setLayout(new BoxLayout(statsActionButtonsPanel, BoxLayout.X_AXIS));	
		
		genWTReportpButton = new JButton("Generate Waiting Time Report");
		genWTReportpButton.addActionListener(this);

		genSTReportpButton = new JButton("Generate Service Time Report");
		genSTReportpButton.addActionListener(this);

		genTEnergyReportpButton = new JButton("Generate Service Used Energy Report");
		genTEnergyReportpButton.addActionListener(this);

		statsActionButtonsPanel.add(genWTReportpButton);
		statsActionButtonsPanel.add(genSTReportpButton);
		statsActionButtonsPanel.add(genTEnergyReportpButton);

		// Stats Dynamic chartS
		// Waiting time chart
		JPanel statsDynamicChartsPanel = new JPanel();
		statsDynamicChartsPanel.setLayout(new BoxLayout(statsDynamicChartsPanel, BoxLayout.X_AXIS));
		// Traffic chart
		//trafficChart = new DynamicXYChart(Color.GREEN, "Traffic");
		//statsDynamicChartsPanel.add(trafficChart.getChart());
		// Waiting time chart
		//wtChart = new DynamicXYChart(Color.RED, "Wating Time");
		//statsDynamicChartsPanel.add(wtChart.getChart());
		// Service time chart
		stChart = new DynamicXYChart(Color.BLUE, "Service Time");
		statsDynamicChartsPanel.add(stChart.getChart());

		statsConPanel.add(statsActionButtonsPanel);
		statsConPanel.add(statsDynamicChartsPanel);
		statsPanel.add(statsConPanel);
		
		frmBuildingSimulator.getContentPane().add(tabbedPane);
	}

	public void updateGuiModel() {
		DecimalFormat df = new DecimalFormat("#.###");
		lblTiempo.setText("Tiempo: " + df.format(guiModel.getSimClock()));
		canvas.updateUI();
	}

	@Override
	public void update(Observable obj, Object arg) {
		if (arg instanceof Passenger) {
			Passenger pass = (Passenger) arg;
			wtChart.setData(guiModel.getSimClock(), pass.getEntryTime() - pass.getArrivalTime());
			stChart.setData(guiModel.getSimClock(), pass.getExitTime() - pass.getArrivalTime());
			// ;
			// trafficChart;
		}
	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		logger.info("Action: " + source.toString());
		try {
			if (source instanceof JButton) {
				// INIT SIMULATION
				if (((JButton) source).equals(initButton)) {
					((JButton) source).setEnabled(false);
					stopButton.setEnabled(true);
					pauseButton.setEnabled(true);
					pauseButton.setText("Pause");
					guiModel.startSimulation();
				}
				// PAUSE / CONTINUE SIMULATION
				if (((JButton) source).equals(pauseButton)) {
					if (guiModel.getSimState() == BuildingModel.SIM_STATE.STARTED) {
						((JButton) source).setText("Resume");
						guiModel.pauseSimulation();
					} else {
						((JButton) source).setText("Pause");
						guiModel.resumeSimulation();
					}
				}
				// STOP SIMULATION
				if (((JButton) source).equals(stopButton)) {
					((JButton) source).setEnabled(false);
					guiModel.stopSimulation();
					pauseButton.setEnabled(false);
					initButton.setEnabled(true);
				}
				// GENERATE WAITING TIME REPORT
				if (((JButton) source).equals(genWTReportpButton)) {
					statisticsManager.generateWTReport();
				}
				// GENERATE SERVICE TIME REPORT
				if (((JButton) source).equals(genSTReportpButton)) {
					statisticsManager.generateSTReport();
				}
				// GENERATE SERVICE TIME REPORT
				if (((JButton) source).equals(genTEnergyReportpButton)) {
					statisticsManager.generateTEnergyReport();
				}
			}
		} catch (Exception e) {
			logger.error("Error action performed " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (!guiModel.isSimulationFinished()) {
			if (guiModel.getSimState().equals(BuildingModel.SIM_STATE.STARTED)) {
				updateGuiModel();
			}
			// Sleep the time for X delay time
			try {
				Thread.sleep(guiModel.getBuildingModel().getDelayTime());
			} catch (InterruptedException e) {
				logger.error("Error dalaying executed routines", e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			System.out.println("java.endorsed.dirs:\n" + System.getProperty("java.endorsed.dirs"));
			MainWindow window;
			window = new MainWindow();
			window.frmBuildingSimulator.setVisible(true);
			window.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
