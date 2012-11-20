package org.arosso.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import org.arosso.model.BuildingModel;
import org.arosso.sim.BuildingSimulator;

public class GuiModel {

	private BuildingSimulator simulator;
	private BuildingModel buildingModel;
	private Double simClock;
	private String lastName;
	private BuildingModel.SIM_STATE simState;
	private boolean simulationFinished = false;
	private	int numElevators;
	private	int numFloors;
	
	
	public GuiModel() throws IOException, Exception {
		buildingModel = BuildingModel.getInstance();
		simulator = new BuildingSimulator();
	}
	
	public void startSimulation() throws IOException, Exception{
		simulator.init();
		buildingModel.setSimulationClock(0d);
		simulator.startSimulation();
	}

	public void pauseSimulation(){
		simulator.pauseSimulation();
	}
	
	public void resumeSimulation(){
		simulator.resumeSimulation();
	}
	
	public void stopSimulation() throws IOException, Exception{
		simulator.stopSimulation();
	}
	
	public Double getSimClock() {
		simClock = buildingModel.getSimulationClock();
		return simClock;
	}

	public boolean isSimulationFinished() {
		return simulationFinished;
	}

	public void setSimulationFinished(boolean simulationFinished) {
		this.simulationFinished = simulationFinished;
	}

	public BuildingModel.SIM_STATE getSimState() {
		simState = buildingModel.getSimState();
		return simState;
	}

	public void setSimState(BuildingModel.SIM_STATE simState) {
		this.simState = simState;
		buildingModel.setSimState(simState);
	}

	public int getNumElevators() {
		numElevators = buildingModel.getNumElevators(); 
		return numElevators;
	}

	public int getNumFloors() {
		numFloors = buildingModel.getNumFloors();
		return numFloors;
	}

	public BuildingModel getBuildingModel() {
		return buildingModel;
	}

	public void setBuildingModel(BuildingModel buildingModel) {
		this.buildingModel = buildingModel;
	}
	
}
