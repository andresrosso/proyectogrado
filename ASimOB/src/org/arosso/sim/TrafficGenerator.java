package org.arosso.sim;

import static org.arosso.util.Constants.BUILDING_PROPS_FILE_DEF;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.arosso.model.BasicTrafficModel;
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
    public TrafficGenerator() throws IOException, Exception {
		this(BUILDING_PROPS_FILE_DEF);
	}
    
    /**
     * The constructor.
     * @throws Exception 
     */
    public TrafficGenerator(String buildingPropsFile) throws IOException, Exception {
    	super();
    	InputStream is = this.getClass().getResourceAsStream(buildingPropsFile);
    	Properties prop = new Properties();  
        prop.load(is);  
        String modelClass = prop.getProperty("trafficModel");
        ClassLoader classLoader = this.getClass().getClassLoader();
        classLoader.loadClass(modelClass);
        is.close();
        logger.info("BuildingModel made based on properties ("+buildingPropsFile+")");
    }
    
    /**
     * Description of the method Operation1.
     */
    public void Operation1() {
    	// Start of user code for method Operation1
    	// End of user code
    }
     
    /**
     * Description of the method calculateDestinationFloor.
     */
    public void calculateDestinationFloor() {
    	// Start of user code for method calculateDestinationFloor
    	// End of user code
    }
     
    /**
     * Description of the method calculateOriginFloor.
     */
    public void calculateOriginFloor() {
    	// Start of user code for method calculateOriginFloor
    	// End of user code
    }
     
    /**
     * Description of the method calculateArrivalTime.
     */
    public void calculateArrivalTime() {
    	// Start of user code for method calculateArrivalTime
    	// End of user code
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
