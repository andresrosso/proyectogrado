package org.arosso.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainWindow extends Thread implements ActionListener {
	
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

	/**
	 * Logger
	 */
	static Logger logger = LoggerFactory.getLogger(MainWindow.class);

	/**
	 * Create the application.
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public MainWindow() throws IOException, Exception {
		try {
			guiModel = new GuiModel();
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
		simInformation.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel summaryPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) summaryPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		simInformation.add(summaryPanel);
		lblTiempo = new JLabel("Tiempo");
		summaryPanel.add(lblTiempo);

		JLabel label = new JLabel("xxx");
		simInformation.add(label);

		JPanel simActionButtonsPanel = new JPanel();
		simInformation.add(simActionButtonsPanel);
		simActionButtonsPanel
				.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

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

		JPanel simGraphics = new JPanel();
		simGraphics.setLayout(new BorderLayout(0, 0));
		simGraphics.setAutoscrolls(true);
		simGraphics.setBorder(BorderFactory.createLineBorder(Color.black));

		// Elevator MATRIX MODEL
		ElevatorTableModel elevatorModel = new ElevatorTableModel(guiModel);
		elevatorModel.setRowCount(guiModel.getNumFloors());
		elevatorModel.setColumnCount(guiModel.getNumElevators() + 1);
		canvas = new JTable(elevatorModel);
		//FLOOR MODEL
		canvas.getColumnModel().getColumn(0).setCellRenderer(new FloorCellRenderer());
		//ELEVATOR COLUMN MODEL
		for (int j = 1; j <= guiModel.getNumElevators(); j++) {
			canvas.getColumnModel().getColumn(j)
					.setCellRenderer(new ElevatorCellRenderer());
		}

		simGraphics.add(canvas);
		simPanel.setLayout(new BoxLayout(simPanel, BoxLayout.X_AXIS));
		simPanel.add(simInformation);
		JScrollPane simGraphicsSP = new JScrollPane(simGraphics);
		simPanel.add(simGraphicsSP);

		JPanel statsPanel = new JPanel();
		tabbedPane.addTab("Statistics", null, statsPanel, null);

		JLabel lblNewLabel = new JLabel("New label 2");
		statsPanel.add(lblNewLabel);
		frmBuildingSimulator.getContentPane().add(tabbedPane);
	}

	public void updateGuiModel() {
		DecimalFormat df = new DecimalFormat("#.###");
		lblTiempo.setText("Tiempo: " + df.format(guiModel.getSimClock()));
		canvas.updateUI();
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
			}
		} catch (Exception e) {
			logger.error("Error action performed "+e.getMessage());
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
			MainWindow window;
			window = new MainWindow();
			window.frmBuildingSimulator.setVisible(true);
			window.start();
		} catch (IOException e) {
			logger.error("Error en el simulador : "+e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Error en el simulador : "+e);
			e.printStackTrace();
		}
	}
}
