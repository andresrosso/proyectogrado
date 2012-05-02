package org.arosso.model;
import java.util.Vector;

import org.arosso.sim.TrafficModel;

/**
 * Description of BasicTrafficModel.
 */
public class BasicTrafficModel implements TrafficModel {
    /**
     * Description of the property trafficDensity.
     */
    public Vector <Float> trafficDensity = null;
    
    // Start of user code (user defined attributes)
    
    // End of user code
    
    /**
     * The constructor.
     */
    public BasicTrafficModel() {
    	// Start of user code constructor
    	super();
    	// End of user code
    }
    
    /**
     * {@inheritDoc}
     * @see .TrafficModel
     *
     */
    public void getTrafficMode() {
    	// Start of user code for method getTrafficMode
    	// End of user code
    }
    
    /**
     * {@inheritDoc}
     * @see .TrafficModel
     *
     * @param time
     */
    public void getEstimatedPassengerNumber(Long time) {
    	// Start of user code for method getEstimatedPassengerNumber
    	// End of user code
    }
    
    /**
     * {@inheritDoc}
     * @see .TrafficModel
     *
     * @param time
     */
    public void getEstimatedDestinationFloor(Long time) {
    	// Start of user code for method getEstimatedDestinationFloor
    	// End of user code
    }
    
    /**
     * {@inheritDoc}
     * @see .TrafficModel
     *
     * @param time
     */
    public void getEstimatedOriginFloor(Long time) {
    	// Start of user code for method getEstimatedOriginFloor
    	// End of user code
    }
    
    /**
     * {@inheritDoc}
     * @see .TrafficModel
     *
     * @param time
     */
    public void getEstimatedArrivalTime(Long time) {
    	// Start of user code for method getEstimatedArrivalTime
    	// End of user code
    }
    
    // Start of user code (user defined methods)
    
    // End of user code
    
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
