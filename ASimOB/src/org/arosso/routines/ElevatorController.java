package org.arosso.routines;
import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;

import org.arosso.exception.CallIllegalState;
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
public class ElevatorController extends SimulationRoutine implements IElevatorController {
    
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
     * Passengers to exit from the elevator
     */
    ArrayList<Passenger> passToExit = new ArrayList<Passenger>();
    
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
    			throw new ElevatorIlegalState("The elevator ("+this.getElevator().getId()+
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
		boolean isInFloor = (position - ((int)position)) == 0;
		//IS IN FLOOR
		if(isInFloor){
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

	@Override
	public void addCall(Passenger call) {
		switch(call.getType()){
		case CALL:
			removeMockCalls();
			elevator.addCall(call);
			break;
		case MOCK_CALL:
			if(elevator.getCalls().size()>0 || elevator.getPassengers().size()>0){
				logger.error("There are more calls, so something is wrong with that mock call");
			}else{
				logger.info("Going to rest the elevator "+elevator.getId());
			}
			break;
		default:
			new CallIllegalState("Its no supposed to happen, the call is not of type CALL or PASSENGER");
		}
	}

	@Override
	public void removeCall(Passenger call) {
		// TODO Auto-generated method stub
		this.elevator.getCalls().remove(call);
		logger.debug("The call "+call+", was removed.");
	}

	@Override
	public void addPassenger(Passenger passenger) {
		removeMockCalls();
		if(directionValidForPassenger(passenger)){
			elevator.addPassenger(passenger);
		}else{
			logger.error("The passenger "+passenger+", could not be added.");
		}
	}

	@Override
	public void removePassenger(Passenger passenger) {
		// TODO Auto-generated method stub
		this.elevator.getPassengers().remove(passenger);
		logger.debug("The passenger "+passenger+", was removed.");
	}
	
	/**
	 * Check if the direction of the call is valid
	 * @param call
	 * @return
	 */
	private boolean directionValidForPassenger(Passenger pass){
		if( getPassDirection(pass)==elevator.getDirection() || 
				(elevator.getDirection() == Elevator.Direction.NONE) ){
			return true;
		}else{
			new CallIllegalState("The passenger is not going in same direction of elevator. "+pass);
			return false;
		}
	}
	
	/**
	 * Check if the direction of the call is valid
	 * @param call
	 * @return
	 */
	boolean directionValidForCall(Passenger call){
		//TODO: CHEECK ENYTHING
		return true;
	}
	
	/**
	 * Remove the mocks calls
	 */
	public void removeMockCalls(){
		for(Passenger call : elevator.getCalls()){
			if(call.getType()==Passenger.Type.MOCK_CALL){
				elevator.getCalls().remove(call);
			}
		}
	}
	
	public Elevator.Direction getPassDirection(Passenger pass){
		if(pass.getOriginFloor()<pass.getDestinationFloor()){
			return Elevator.Direction.UP;
		}else if(pass.getOriginFloor()>pass.getDestinationFloor()){
			return Elevator.Direction.DOWN;
		}else{
			new CallIllegalState("There is a call with no direction, it is so strange :S "+pass);
			return Elevator.Direction.NONE;
		}
	}
	
	
	public boolean makeListPassengersGetsDestFloor(int destFloor){
		if(passToExit.size()>0){
			new CallIllegalState("The list of exit passengers should be empty before that check , "+passToExit);
			return false;
		}
		for(Passenger pass : elevator.getPassengers()){
			if(pass.getDestinationFloor()==destFloor){
				passToExit.add(pass);
			}
		}
		return passToExit.size()>0;
	}
    
}
