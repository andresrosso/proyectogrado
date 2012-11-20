package org.arosso.routines;
import java.io.IOException;

import javax.lang.model.element.ElementKind;

import org.arosso.model.BuildingModel;
import org.arosso.model.Elevator;
import org.arosso.model.Passenger;
import org.arosso.sim.SimulationRoutine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description of ElevatorController.
 */
public class ElevatorController extends SimulationRoutine {
    
    /**
     * Description of the property elevator.
     */
    public Elevator elevator = null;
    
    /**
     * Building model referfence
     */
    BuildingModel buildingModel;
    /**
     * 
     */
    private Float targetStopTime = 0f;
    
    /**
     * Logger
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    //Elevatror speed
    private float speed;
    //Floor gap distance
    private float floorGap;
    
    /**
     * The constructor.
     * @throws Exception 
     * @throws IOException 
     */
    public ElevatorController(String routineName, Float activationTime, Elevator elevator) throws IOException, Exception {
    	super(routineName,activationTime);
    	this.elevator = elevator;
    	speed = elevator.getSpeed();
    	buildingModel = BuildingModel.getInstance();
    	floorGap = buildingModel.getFloorGapDistance();
    }
    
    @Override
    public void execute() {
    	logger.info("Controlling the elevator car");
    	//Update de elevator state at this simulation time
    	updateElevatorState(buildingModel.getSimulationClock());
    	//Elevator state
    	switch(elevator.getState()){
			case IN_FLOOR:
				break;
    		case MOVING:
    			elevator.setPosition(calcNextMovement());
    			break;
			case STOPPED:
				break;
    		case OUT_OF_SERVICE:
    			break;
    		default:
    			break;
    	};
    	//After all do calculate the elevator direccition
    	elevator.setDirection(calcDirection());
    }
    
    /**
     * Returns elevator.
     * @return elevator 
     */
    public Object getElevator() {
    	return this.elevator;
    }
    
    
    /**
     * Sets a value to attribute elevator. 
     * @param newElevator 
     */
    public void setElevator(Elevator newElevator) {
        this.elevator = newElevator;
    }
    
    private void updateElevatorState(Double time){
    	
    }
    
    private pickUpPassengers(){
    	//The simulation will only pickup passanger in the same elevator direction
    	
    }
    private Elevator.Direction calcDirection(){
    	Elevator.Direction dir = Elevator.Direction.NONE;
    	//Based on calls
    	for(Passenger call : elevator.getCalls()){
    		
    	}
    	//Based on passengers
    	for(Passenger passenger : elevator.getCalls()){
    		
    	}
    	return dir;
    }
    
    private float calcNextMovement(){
    	float time = this.activationTime;
    	//Distance in metters
    	float d = speed/time;
    	//Distance in floors
    	float position = d/floorGap;
    	//return the position
    	if(elevator.getState().equals(Elevator.State.MOVING)){
	    	if(elevator.getDirection().equals(Elevator.Direction.DOWN)){
	    		position = elevator.getPosition() - position;
	    	}else if(elevator.getDirection().equals(Elevator.Direction.UP)){
	    		position = elevator.getPosition() + position;
	    	}else if(elevator.getDirection().equals(Elevator.Direction.NONE)){
	    		position = elevator.getPosition();
	    	}
    	}
    	return position;
    }
    
    public float calcStopDelay(){
    	//doorCloseTime
    	//doorOpenTime
    	//passangerTransferTime
    	return 0;
    }
    
}
