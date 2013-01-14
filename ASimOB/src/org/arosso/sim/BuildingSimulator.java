package org.arosso.sim;
import java.io.IOException;

import org.arosso.gui.GuiController;
import org.arosso.model.BuildingModel;
import org.arosso.model.BuildingModel.SIM_STATE;
import org.arosso.stats.JasperReportManager;
import org.arosso.stats.StatisticsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File generated from the model::BuildingSimulator uml Class
 * Generated by the Acceleo 3 <i>UML to Java</i> generator.
 */


/**
 * Description of BuildingSimulator.
 */
public class BuildingSimulator {
    /**
     * Description of the property building.
     */
    public BuildingModel building = null;
    
    /**
     * Description of the property rutineManager.
     */
    public RoutineManager routineManager = null;
    
    /**
     * Description of the property statisticsManager.
     */
    public StatisticsManager statisticsManager = null;
    
    /**
     * Description of the property guiController.
     */
    public GuiController guiController = null;
    
    /**
     * Description of the property jasperReportManager.
     */
    public JasperReportManager jasperReportManager = null;
    
    /**
     * Description of the property simTime.
     */
    public Double simTime = 0D;
    
    /**
     * Logger
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * The constructor.
     */
    public BuildingSimulator() {
    	// Start of user code constructor
    	super();
    	logger.debug("BuildingSimulator created!");
    }
   
    public void init() throws IOException, Exception{
    	building = BuildingModel.getInstance();
    	routineManager = RoutineManager.getInstance();
    	logger.info("BuildingSimulator initiated!");
    }
     
    /**
     * Description of the method startSimulation.
     */
    public void startSimulation() {
    	logger.info("BuildingSimulator started!");
    	building.simState = SIM_STATE.STARTED;
    	routineManager.start();
    }

    /**
     * Description of the method pauseSimulation.
     */
    public void pauseSimulation() {
    	logger.info("BuildingSimulator paused!");
    	building.simState = SIM_STATE.PAUSED;
    }

    /**
     * Description of the method pauseSimulation.
     */
    public void resumeSimulation() {
    	logger.info("BuildingSimulator paused!");
    	building.simState = SIM_STATE.STARTED;
    }
     
    
    /**
     * Description of the method stopSimulation.
     */
    public void stopSimulation() {
    	logger.info("BuildingSimulator stopped!");
    	building.simState = SIM_STATE.STOPPED;
    }
     
    /**
     * Description of the method openSimulationConfig.
     */
    public void openSimulationConfig() {
    	logger.info("BuildingSimulator openSimulationConfig!");
    }
     
    /**
     * Description of the method addElevatorCall.
     */
    public void addElevatorCall() {
    	logger.debug("BuildingSimulator addElevatorCall!");
    }
    
    /**
     * Returns building.
     * @return building 
     */
    public BuildingModel getBuilding() {
    	return this.building;
    }
    
    /**
     * Sets a value to attribute building. 
     * @param newBuilding 
     */
    public void setBuilding(BuildingModel newBuilding) {
        this.building = newBuilding;
    }
    
    /**
     * Returns rutineManager.
     * @return rutineManager 
     */
    public RoutineManager getRoutineManager() {
    	return this.routineManager;
    }
    
    /**
     * Sets a value to attribute rutineManager. 
     * @param newRutineManager 
     */
    public void setRutineManager(RoutineManager newRoutineManager) {
        this.routineManager = newRoutineManager;
    }
    
    
    /**
     * Returns statisticsManager.
     * @return statisticsManager 
     */
    public StatisticsManager getStatisticsManager() {
    	return this.statisticsManager;
    }
    
    /**
     * Sets a value to attribute statisticsManager. 
     * @param newStatisticsManager 
     */
    public void setStatisticsManager(StatisticsManager newStatisticsManager) {
        this.statisticsManager = newStatisticsManager;
    }
    
    /**
     * Returns guiController.
     * @return guiController 
     */
    public GuiController getGuiController() {
    	return this.guiController;
    }
    
    /**
     * Sets a value to attribute guiController. 
     * @param newGuiController 
     */
    public void setGuiController(GuiController newGuiController) {
        this.guiController = newGuiController;
    }
    
    /**
     * Returns jasperReportManager.
     * @return jasperReportManager 
     */
    public JasperReportManager getJasperReportManager() {
    	return this.jasperReportManager;
    }
    
    /**
     * Sets a value to attribute jasperReportManager. 
     * @param newJasperReportManager 
     */
    public void setJasperReportManager(JasperReportManager newJasperReportManager) {
        this.jasperReportManager = newJasperReportManager;
    }
    
    /**
     * Returns simTime.
     * @return simTime 
     */
    public Double getSimTime() {
    	return this.simTime;
    }
    
    /**
     * Sets a value to attribute simTime. 
     * @param newSimTime 
     */
    public void setSimTime(Double newSimTime) {
        this.simTime = newSimTime;
    }
    
    @Override
    public String toString() {
    	String sim = "Time: "+this.simTime+"\n";
    	sim += "Building Model {"+this.building+"}\n";
    	
    	return sim;
    }
    
    /**
     * Just for testing purposes
     * @param args
     */
    public static void main(String[] args){
    	BuildingSimulator simulator = new BuildingSimulator();
    	
    	try {
			//simulator.init();
			//System.out.println(simulator);
			//simulator.startSimulation();
			
		} catch (Exception e) {
			simulator.logger.error("Error",e);
		}
    }
    
}
