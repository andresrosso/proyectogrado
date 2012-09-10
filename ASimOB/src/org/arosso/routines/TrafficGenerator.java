package org.arosso.routines;

import static org.arosso.util.Constants.ASIMOB_PATH;

import java.io.IOException;

import org.arosso.model.BuildingModel;
import org.arosso.model.Passenger;
import org.arosso.sim.SimulationRoutine;
import org.arosso.sim.TrafficModel;
import org.arosso.util.PropertiesBroker;
import org.arosso.util.PropertiesBroker.PROP_SET;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description of TrafficGenerator.
 */
public class TrafficGenerator extends SimulationRoutine {
    /**
     * Description of the property basicTrafficModel.
     */
    private TrafficModel trafficModel = null;
    
    /**
     * 
     */
    private BuildingModel buildingModel;
    
    /**
     * Logger
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * Default constructor
     * @throws IOException
     * @throws Exception
     */
    public TrafficGenerator(String routineName) throws IOException, Exception {
		this(routineName, ASIMOB_PATH);
	}
    
    /**
     * The constructor.
     * @throws Exception 
     */
    public TrafficGenerator(String routineName, String buildingPropsFile) throws IOException, Exception {
    	super();
    	// Read model properties
    	PropertiesBroker propertiesBroker = PropertiesBroker.getInstance();
        String modelClass = propertiesBroker.getProperty(PROP_SET.SIMULATION,"trafficModelImplementation");
        Float trafficGenActivationTime = Float.valueOf(propertiesBroker.getProperty(PROP_SET.SIMULATION,"trafficGenActivationTime"));
        logger.info("BuildingModel made based on properties ("+buildingPropsFile+")");
        ClassLoader classLoader = this.getClass().getClassLoader();
        Class myObjectClass = classLoader.loadClass(modelClass);
        trafficModel = (TrafficModel) myObjectClass.newInstance();
    	logger.debug("TrafficGenerator created!");
    	//Set variables
    	this.setRoutineName(routineName);
    	this.setActivationTime(trafficGenActivationTime);
    	//Get building model
    	buildingModel = BuildingModel.getInstance();
    }
    
    @Override
    public void execute() {
    	logger.info("Generating passangers");
    	int pass2gen = this.trafficModel.getEstimatedPassengerNumber(buildingModel.getSimulationClock());
    	for(int j=0;j<=pass2gen; j++){
    		Passenger passenger = new Passenger();
    		passenger.setArrivalTime( this.trafficModel.getEstimatedArrivalTime(buildingModel.getSimulationClock()) );
    		passenger.setOriginFloor( this.trafficModel.getEstimatedOriginFloor(buildingModel.getSimulationClock()) );
    		passenger.setDestinationFloor( this.trafficModel.getEstimatedDestinationFloor(buildingModel.getSimulationClock()) );
    		buildingModel.getPassenger().add(passenger);
    		logger.info("Passenger generated "+passenger);
    	}
    }
    
    /**
     * Returns basicTrafficModel.
     * @return basicTrafficModel 
     */
    public TrafficModel getTrafficModel() {
    	return this.trafficModel;
    }
    
    /**
     * Sets a value to attribute basicTrafficModel. 
     * @param newBasicTrafficModel 
     */
    public void setTrafficModel(TrafficModel newTrafficModel) {
        this.trafficModel = newTrafficModel;
    }
    
    @Override
    public String toString() {
    	return this.trafficModel.getClass().toString();
    }
    
    
}
