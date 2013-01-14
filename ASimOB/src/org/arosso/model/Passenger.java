/**
 * File generated from the model::Passenger uml Class
 * Generated by the Acceleo 3 <i>UML to Java</i> generator.
 */

package org.arosso.model;

import org.arosso.exception.CallIllegalState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Start of user code (user defined imports)	

// End of user code

/**
 * Description of Passenger.
 */
public class Passenger {
	
	public enum Type {
    	CALL, PASSENGER, MOCK_CALL;
    }
	
	private static int numPassengers = 0;
	private String id;
	
    /**
     * Description of the property arrivalTime.
     */
    private int arrivalTime = 0;
    
    /**
     * Description of the property entryTime.
     */
    private int entryTime = 0;
    
    /**
     * Description of the property exitTime.
     */
    private int exitTime = 0;
    
    /**
     * Description of the property originFloor.
     */
    private Integer originFloor = 0;
    
    /**
     * Description of the property destinationFloor.
     */
    private Integer destinationFloor = 0;
    
    /**
     * Description of the property assignedElevator.
     */
    private Integer assignedElevator = 0;
    
    /**
     * Type of call, for default is CALL the will be PASSENGER
     */
    private Type type = Type.CALL;
    

    /**
     * Logger
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * The constructor.
     */
    private Passenger() {
    	// Start of user code constructor
    	super();
    	id = String.valueOf((numPassengers++));
    }
    
    
    
    public Passenger(int arrivalTime, Integer originFloor,
			Integer destinationFloor, Type type) {
		super();
		this.arrivalTime = arrivalTime;
		this.originFloor = originFloor;
		this.destinationFloor = destinationFloor;
		this.type = type;
		this.id = String.valueOf((numPassengers++));
	}



	/**
     * Description of the method getTravelTime.
     * @return 
     */
    public Double getTravelTime() {
    	// Start of user code for method getTravelTime
    	Double getTravelTime = 0.0D;
    	return getTravelTime;
    	// End of user code
    }
     
    /**
     * Returns arrivalTime.
     * @return arrivalTime 
     */
    public int getArrivalTime() {
    	return this.arrivalTime;
    }
    
    /**
     * Sets a value to attribute arrivalTime. 
     * @param newArrivalTime 
     */
    public void setArrivalTime(int newArrivalTime) {
        this.arrivalTime = newArrivalTime;
    }
    
    /**
     * Returns entryTime.
     * @return entryTime 
     */
    public int getEntryTime() {
    	//logger.info(">>get.entrytime="+this.entryTime+" pass"+this.toStringComplete());
    	return this.entryTime;
    }
    
    /**
     * Sets a value to attribute entryTime. 
     * @param newPushedTime 
     */
    public void setEntryTime(int newPushedTime) {
        this.entryTime = newPushedTime;
        logger.info(">>set.entrytime="+newPushedTime+" pass"+this.toStringComplete());
    }
    
    /**
     * Returns exitTime.
     * @return exitTime 
     */
    public int getExitTime() {
    	return this.exitTime;
    }
    
    /**
     * Sets a value to attribute exitTime. 
     * @param newPulledTime 
     */
    public void setExitTime(int newPulledTime) {
        this.exitTime = newPulledTime;
    }
    
    /**
     * Returns originFloor.
     * @return originFloor 
     */
    public Integer getOriginFloor() {
    	return this.originFloor;
    }
    
    /**
     * Sets a value to attribute originFloor. 
     * @param newOriginFloor 
     */
    public void setOriginFloor(Integer newOriginFloor) {
        this.originFloor = newOriginFloor;
    }
    
    /**
     * Returns destinationFloor.
     * @return destinationFloor 
     */
    public Integer getDestinationFloor() {
    	return this.destinationFloor;
    }
    
    /**
     * Sets a value to attribute destinationFloor. 
     * @param newDestinationFloor 
     */
    public void setDestinationFloor(Integer newDestinationFloor) {
        this.destinationFloor = newDestinationFloor;
    }
    
    /**
     * Returns assignedElevator.
     * @return assignedElevator 
     */
    public Integer getAssignedElevator() {
    	return this.assignedElevator;
    }
    
    /**
     * Sets a value to attribute assignedElevator. 
     * @param newAssignedElevator 
     */
    public void setAssignedElevator(Integer newAssignedElevator) {
        this.assignedElevator = newAssignedElevator;
    }
    
    public Elevator.Direction getDirection(){
    	if(this.getOriginFloor()<this.getDestinationFloor()){
			return Elevator.Direction.UP;
		}else if(this.getOriginFloor()>this.getDestinationFloor()){
			return Elevator.Direction.DOWN;
		}else{
			new CallIllegalState("There is a call with no direction, it is so strange :S "+this);
			return Elevator.Direction.NONE;
		}
    }
    
    @Override
    public String toString() {
    	return "T["+this.arrivalTime+","+this.entryTime+","+this.exitTime+"] D("+this.originFloor+","+this.destinationFloor+")";
    	//return "("+this.originFloor+","+this.destinationFloor+")";
    }
    
    public String toStringComplete() {
    	return "T["+this.arrivalTime+","+this.entryTime+","+this.exitTime+"] D("+this.originFloor+","+this.destinationFloor+")";
    }

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}



	public String getId() {
		return id;
	}
    
}
