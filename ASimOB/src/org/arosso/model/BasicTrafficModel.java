package org.arosso.model;
import java.io.IOException;
import java.util.Vector;

import org.arosso.sim.TrafficModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description of BasicTrafficModel.
 */
public class BasicTrafficModel implements TrafficModel {
    /**
     * Description of the property trafficDensity.
     */
    public Vector <Float> trafficDensity = null;
    /**
     * Building model
     */
    private BuildingModel buildingModel = null;
    
    /**
     * Logger
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * The constructor.
     * @throws Exception 
     * @throws IOException 
     */
    public BasicTrafficModel() throws IOException, Exception {
    	// Start of user code constructor
    	super();
    	buildingModel = BuildingModel.getInstance();
    	logger.info(this.getClass()+" loaded!");
    }
    
    /**
     * {@inheritDoc}
     * @see .TrafficModel
     *
     */
    public String getTrafficMode() {
    	return "";
    }
    
    /**
     * {@inheritDoc}
     * @see .TrafficModel
     *
     * @param time
     */
    public int getEstimatedPassengerNumber(Double time) {
    	return (int)(Math.random()*10);
    }
    
    /**
     * {@inheritDoc}
     * @see .TrafficModel
     *
     * @param time
     */
    public int getEstimatedDestinationFloor(Double time) {
    	int destFloor = (int)(Math.random()*buildingModel.getNumFloors());
    	return destFloor;
    }
    
    /**
     * {@inheritDoc}
     * @see .TrafficModel
     *
     * @param time
     */
    public int getEstimatedOriginFloor(Double time) {
    	int oriFloor = (int)(Math.random()*buildingModel.getNumFloors());
    	return oriFloor;
    }
    
    /**
     * {@inheritDoc}
     * @see .TrafficModel
     *
     * @param time
     */
    public Double getEstimatedArrivalTime(Double time) {
    	//The arrival period is 5 minutes
    	Double arrivalTime = time + (int)(Math.random()*300);
    	return arrivalTime;
    }
    
    
    /**
     * Returns trafficDensity.
     * @return trafficDensity 
     */
    public Vector <Float> getTrafficDensity() {
    	return this.trafficDensity;
    }
    
    /**
     * Sets a value to attribute trafficDensity. 
     * @param newTrafficDensity 
     */
    public void setTrafficDensity(Vector <Float> newTrafficDensity) {
        this.trafficDensity = newTrafficDensity;
    }
    
    
}
