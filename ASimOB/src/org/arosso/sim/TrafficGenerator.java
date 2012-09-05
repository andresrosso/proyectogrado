package org.arosso.sim;

import static org.arosso.util.Constants.ASIMOB_PATH;

import java.io.IOException;

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
    public TrafficModel trafficModel = null;
    
    
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
    }
    
    @Override
    public void execute() {
    	logger.info("Generating passangers");
    }
    
    /**
     * Description of the method calculateDestinationFloor.
     */
    public void calculateDestinationFloor(long time) {
    	trafficModel.getEstimatedDestinationFloor(time);
    }
     
    /**
     * Description of the method calculateOriginFloor.
     */
    public void calculateOriginFloor(long time) {
    	trafficModel.getEstimatedOriginFloor(time);
    }
     
    /**
     * Description of the method calculateArrivalTime.
     */
    public void calculateArrivalTime(long time) {
    	trafficModel.getEstimatedArrivalTime(time);
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
