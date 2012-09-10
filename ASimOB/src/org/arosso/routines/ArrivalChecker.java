package org.arosso.routines;

import org.arosso.model.BuildingModel;
import org.arosso.model.Passenger;
import org.arosso.routines.egcs.ElevatorGroupController;
import org.arosso.sim.SimulationRoutine;
import org.arosso.util.PropertiesBroker;
import org.arosso.util.PropertiesBroker.PROP_SET;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File generated from the model::ArrivalChecker uml Class
 * Generated by the Acceleo 3 <i>UML to Java</i> generator.
 */

// Start of user code (user defined imports)	

// End of user code

/**
 * Description of ArrivalChecker.
 */
public class ArrivalChecker extends SimulationRoutine {
    /**
     * Controller to assign calls to elevators
     */
    private ElevatorGroupController controller;
    
    /**
     * 
     */
    private BuildingModel buildingModel;

    
    /**
     * Logger
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * The constructor.
     * @throws Exception 
     */
    public ArrivalChecker(String name, Float activationTime ) throws Exception {
    	super( name,  activationTime);
    	logger.info("ArrivalChecker loaded!");
    	String egcsClass = PropertiesBroker.getInstance().getProperty(PROP_SET.SIMULATION,"egcsImplementation");
        ClassLoader classLoader = this.getClass().getClassLoader();
        Class myObjectClass = classLoader.loadClass(egcsClass);
        logger.debug("ArrivalChecker using EGCS "+myObjectClass);
        controller = (ElevatorGroupController) myObjectClass.newInstance();
        buildingModel = BuildingModel.getInstance();
    }
    
    @Override
    public void execute() {
    	logger.info("Checking for passangers arrivals");
    	for(Passenger passenger: buildingModel.getPassenger()){
    		if(buildingModel.getSimulationClock() == passenger.getArrivalTime()){
    			logger.info("Assign passanger "+passenger);
    			controller.assignCall(passenger);
    		}
    	}
    }
    
    
}