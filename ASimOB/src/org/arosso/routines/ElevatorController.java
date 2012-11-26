package org.arosso.routines;
import java.io.IOException;

import javax.lang.model.element.ElementKind;

import org.arosso.exception.ElevatorIlegalState;
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
    private Float currentTaskTime = 0f;
    
    /**
     * The ammount of time that the task takes
     */
    private Float taskTimeMAX = 0f;
    
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
    	//Check if there is a task running
    	if(taskTimeMAX==0){
    		//There is a task running, so do it
    		switch(elevator.getState()){
			case MOVING:
				doMoveElevator();
				break;
    		case RESTING:
    			
    			break;
			case OUT_OF_SERVICE:
				break;
    		default:
    			throw new ElevatorIlegalState("The elevaor ("+this.getElevator().getElevatorId()+
    					") is supposed to be -MOVING, RESTING OR OUT OF SERVICE- but is "+this.getElevator().getState() );
    			break;
    		};
    	}else{
    		//There is a task running, so do it
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
    	}
    	
    	//Update de elevator state at this simulation time
    	updateElevatorState(buildingModel.getSimulationClock());
    	//Elevator state
    	
    	//After all do calculate the elevator direccition
    	elevator.setDirection(calcDirection());
    }
    
    /**
     * Move the elevator to the next position
     */
	private void doMoveElevator(){
		float position = calcNextMovement();
		//Verify if the elevator is in floor
		boolean hasDecimals = (((int)(round(position*100))) % 100) != 0;
		if(hasDecimals){
			logger.debug("Elevator "+this.elevator.getId()+" in floor");
			//if there are passengers
			if(this.elevator.getPassengers().size()>0){
				
			}else{
				//if there are calls
				if(this.elevator.getCalls().size()>0){
					
				}else{
					//Go to next position
					elevator.setPosition(position);
				}
				logger.debug("Mock call for Elevator "+this.elevator.getId()+" in floor");
			}
		}else{
			//Go to next position
			elevator.setPosition(position);
		}
	}
	
    /**
     * Returns elevator.
     * @return elevator 
     */
    public Elevator getElevator() {
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
    
    public boolean checkPassengerGetsGoalFloor(){
    	
    }
    
}
