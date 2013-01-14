package org.arosso.sim;
import java.io.IOException;
import java.util.Vector;

import org.arosso.model.BuildingModel;
import org.arosso.model.Passenger;
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
    public int getEstimatedPassengerNumber(Float time) {
    	return (int)(Math.random()*50);
    }
    
    /**
     * {@inheritDoc}
     * @see .TrafficModel
     *
     * @param time
     */
    public int getEstimatedDestinationFloor(Float time) {
    	int destFloor = (int)(Math.random()*buildingModel.getNumFloors());
    	return destFloor;
    }
    
    /**
     * {@inheritDoc}
     * @see .TrafficModel
     *
     * @param time
     */
    public int getEstimatedOriginFloor(Float time) {
    	int oriFloor = (int)(Math.random()*buildingModel.getNumFloors());
    	return oriFloor;
    }
    
    /**
     * {@inheritDoc}
     * @see .TrafficModel
     *
     * @param time
     */
    public Float getEstimatedArrivalTime(Float time) {
    	//The arrival period is 5 minutes
    	Float arrivalTime = time + (int)(Math.random()*300);
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

	@Override
	public Vector<Passenger> getPassengersForPeriod(int time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Float getEstimatedTrafficIn(int time) {
		return null;
	}

	@Override
	public Float getEstimatedTrafficOut(int time) {
		return null;
	}

	@Override
	public Float getEstimatedTrafficInterfloor(int time) {
		return null;
	}
    
    
}
