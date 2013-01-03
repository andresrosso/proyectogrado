package org.arosso.routines;

import java.io.IOException;
import java.util.Vector;

import org.arosso.db.DatabaseMannager;
import org.arosso.exception.CallIllegalState;
import org.arosso.exception.ElevatorIlegalState;
import org.arosso.model.BuildingModel;
import org.arosso.model.Elevator;
import org.arosso.model.Passenger;
import org.arosso.sim.SimulationRoutine;
import org.arosso.stats.StatisticsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description of ElevatorController.
 */
public class ElevatorController extends SimulationRoutine implements
		IElevatorController {

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
	Vector<Passenger> passToExit = new Vector<Passenger>();
	/**
	 * Passengers to exit from the elevator
	 */
	Vector<Passenger> callsToCome = new Vector<Passenger>();
	
	/**
	 * Statistics mannager
	 */
	private static StatisticsManager statisticsManager;
	
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// Elevator speed
	private float speed;
	
	// Floor gap distance
	private float floorGap;
	
	/**
	 * Static code to get the reference of statistics manager
	 */
	static{
		statisticsManager = StatisticsManager.getInstance();
	}
	
	/**
	 * The constructor.
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public ElevatorController(String routineName, Float activationTime,
			Elevator elevator) throws IOException, Exception {
		super(routineName, activationTime);
		this.elevator = elevator;
		speed = elevator.getSpeed();
		buildingModel = BuildingModel.getInstance();
		floorGap = buildingModel.getFloorGapDistance();
	}

	@Override
    public void execute() {
    	logger.info("Time ("+buildingModel.getSimulationClock()+") " +
    			", Pos("+elevator.getPosition()+ ")"+
    			", Calls("+elevator.getCalls()+") "+
    			", Pass("+elevator.getPassengers()+")"+
    			", Dir("+elevator.getDirection()+")"+
    			", STATE("+elevator.getState()+")"+
    			", TFloor("+elevator.getTargetFloor()+")"
    			//"Elevator ["+elevator.getId()+"] " +
    			);
    	//Check if there is a task running
    	if( taskTimeMAX==0 || (currentTaskTime.floatValue()==taskTimeMAX.floatValue()) ){
    		//There is a task running, so do it
    		switch(elevator.getState()){
    		case OUT_OF_SERVICE:
				break;
    		case RESTING:
    			doRestingTask();
    			break;
			case MOVING:
				doMovingTask();
				break;
			case OPEN_DOOR:
				doOpenDoorTask();
				break;
			case EXIT_PASS:
				doExitPassengerTask();
				break;
			case CLOSE_DOOR:
				doCloseDoorTask();
				break;
			case COMING_PASS:
				doComingPassengerTask();
				break;
    		default:
    			new ElevatorIlegalState("The elevator ("+this.getElevator().getId()+
    					") is supposed to be -MOVING, RESTING OR OUT OF SERVICE- but is "+this.getElevator().getState() );
    		}
    	}else{
    		currentTaskTime += buildingModel.getDeltaAdvaceTime();
    	}
    	logger.info("Time ("+buildingModel.getSimulationClock()+") " +
    			", Pos("+elevator.getPosition()+ ")"+
    			", Calls("+elevator.getCalls()+") "+
    			", Pass("+elevator.getPassengers()+")"+
    			", Dir("+elevator.getDirection()+")"+
    			", STATE("+elevator.getState()+")"+
    			", TFloor("+elevator.getTargetFloor()+")"
    			//"Elevator ["+elevator.getId()+"] " +
    			);
    }
    	
    /**
     * Task for resting state
     */
	private void doRestingTask() {
		// If there are calls
		if (elevator.getCalls().size() > 0) {
			// if all call has the same direction
			if (allCallsHasSameDirection(elevator.getCalls())) {
				// find direction and target floor
				findDirectionAndTargetFloor();
			}else{
				// get better route call
				//TODO: Its no really  common so do it easy
				logger.error("Elevator ["+elevator.getId()+"] This is not common but, there are 2 calls in different direction");
				findDirectionAndTargetFloor();
			}
			elevator.setState(Elevator.State.MOVING);
		} else {
			if(elevator.getPosition().intValue()==elevator.getRestFloor()){
				return;	
			}else{
				elevator.getCalls().add(new Passenger(buildingModel.getSimulationClock().intValue()+1, elevator.getRestFloor(),elevator.getRestFloor(),Passenger.Type.MOCK_CALL));
				findDirectionAndTargetFloor();
				elevator.setState(Elevator.State.MOVING);
			}
		}
	}
	
	/**
	 * Move the elevator to the next position
	 */
	private void doMovingTask() {
		float position = calcNextMovement();
		//Move elevator to next position
		elevator.setPosition(position);
		// Verify if the elevator is in floor
		boolean isInFloor = (position - ((int) position)) == 0;
		// IT IS IN FLOOR
		if (isInFloor) {
			logger.debug("Elevator " + this.elevator.getId() + " in floor");
			//If there are passengers that get destination floor
			if(hasReachFloorForCall(elevator.getPassengers())){
				logger.debug("Elevator ["+elevator.getId()+"] Open door for passenger exit. " + passToExit);
				currentTaskTime = buildingModel.getDeltaAdvaceTime();
				taskTimeMAX = elevator.getDoorOpenTime();
				elevator.setState(Elevator.State.OPEN_DOOR);
			}//Check if there are calls in floor
			else if(hasReachFloorForCall(elevator.getCalls())){
				//Check if it is a mock call
				if(elevator.getCalls().get(0).getType() == Passenger.Type.MOCK_CALL){
					elevator.setTargetFloor(elevator.getPosition().intValue());
					elevator.setDirection(Elevator.Direction.NONE);
					elevator.setState(Elevator.State.RESTING);
					elevator.getCalls().remove(0);
					if(elevator.getCalls().size()>0){
						logger.warn("Elevator ["+elevator.getId()+"] There was a mock call and other type for the elevator. Strange-");
						doMovingTask();
					}
					return;
				}//If the call is not a mock call
				else{
					if(directionValidForAnyPassenger(callsToCome)){
						logger.debug("Elevator ["+elevator.getId()+"] Open door for call. " + callsToCome);
						currentTaskTime = buildingModel.getDeltaAdvaceTime();
						taskTimeMAX = elevator.getDoorOpenTime();
						elevator.setState(Elevator.State.OPEN_DOOR);
					}else{
						if (elevator.getPassengers().isEmpty()) {
							elevator.setDirection(Elevator.Direction.NONE);
							currentTaskTime = buildingModel.getDeltaAdvaceTime();
							taskTimeMAX = elevator.getDoorOpenTime();
							elevator.setState(Elevator.State.OPEN_DOOR);
						}else{
							logger.debug("Elevator ["+elevator.getId()+"] Continue moving ignoring different direction call" + callsToCome);
							return;
						}
					}
				}
			}

		} else {
			logger.debug("Elevator ["+elevator.getId()+"] Continue moving" + callsToCome);
			return;
		}
	}

	/**
	 * Open the elevator door
	 */
	private void doOpenDoorTask() {
		//If there are passengers that get destination floor
		if(hasReachFloorForCall(elevator.getPassengers())){
			elevator.setState(Elevator.State.EXIT_PASS);
			currentTaskTime = buildingModel.getDeltaAdvaceTime();
			taskTimeMAX = elevator.getPassangerTransferTime()*(passToExit.size());
			logger.debug("Elevator ["+elevator.getId()+"] has reach floor to exit a passenger ");
		}else if(hasReachFloorForCall(elevator.getCalls())){
			//Check if there are passengers
			if (!elevator.getPassengers().isEmpty()) {
				callsToCome = removeDifferentDirectionCalls(callsToCome, elevator.getDirection());
			}else{
				if(allCallsHasSameDirection(callsToCome)){
					logger.debug("Elevator ["+elevator.getId()+"] All calls has the same direction.");
				}else{
					logger.error("Elevator ["+elevator.getId()+"] There are calls in different direction.");
				}
			}
			logger.debug("Elevator ["+elevator.getId()+"] has reach floor to pickUp a passenger ");
			findDirectionAndTargetFloor();
			elevator.setState(Elevator.State.COMING_PASS);
			taskTimeMAX = elevator.getPassangerTransferTime()*(callsToCome.size());
		}else{
			currentTaskTime = buildingModel.getDeltaAdvaceTime();
			taskTimeMAX = elevator.getDoorCloseTime();
			elevator.setState(Elevator.State.CLOSE_DOOR);
		}
	}

	/**
	 * Exit passenger from the elevator
	 */
	private void doExitPassengerTask() {
		for(Passenger passenger : passToExit){
			removePassenger(passenger);
		}
		passToExit.clear();
		doOpenDoorTask();
	}

	/**
	 * Close the elevator door
	 */
	private void doCloseDoorTask() {
		//If there are passengers
		if(!elevator.getPassengers().isEmpty()){
			if(directionValidForAnyPassenger(elevator.getPassengers())){
				logger.debug("Elevator ["+elevator.getId()+"] Close the door and go.");
				findDirectionAndTargetFloor();
			}else{
				new CallIllegalState("Elevator ["+elevator.getId()+"] There are passengers in different direction, pass:"+elevator.getPassengers()+" - Elevator Dir:"+elevator.getDirection());
			}
		}//If there are calls
		else if(!elevator.getCalls().isEmpty()){
			findDirectionAndTargetFloor();
		}else{
			elevator.setState(Elevator.State.RESTING);
			return;
		}
		elevator.setState(Elevator.State.MOVING);
		currentTaskTime = taskTimeMAX = 0f;
	}

	/**
	 * Coming passenger to the elevator
	 */
	private void doComingPassengerTask() {
		this.removeCalls(callsToCome);
		this.addPassengers(callsToCome);
		callsToCome.clear();
		if(directionValidForAnyPassenger(elevator.getPassengers())){
			elevator.setState(Elevator.State.CLOSE_DOOR);
			currentTaskTime = buildingModel.getDeltaAdvaceTime();
			taskTimeMAX = elevator.getDoorCloseTime();
		}else{
			logger.error("Elevator ["+elevator.getId()+"] There are passengers in different direction, pass: "+elevator.getPassengers());
		}
	}
	
	/**
	 * Remove calls in other direcion
	 */
	public Vector<Passenger> removeDifferentDirectionCalls(Vector<Passenger> callList, Elevator.Direction validDirection ){
		Vector<Passenger> callLisToRemove = new Vector<Passenger>();
		for(Passenger passenger : callList){
			if(passenger.getDirection() == validDirection || elevator.getDirection() == Elevator.Direction.NONE ){
				logger.debug("Elevator ["+elevator.getId()+"] The call is valid for thge direction. call = "+passenger);
			}else{
				callLisToRemove.add(passenger);
			}
		}
		callList.removeAll(callLisToRemove);
		return callList;
	}
	
	/**
	 * Check if a passenger has reach the destination floor or if there is a call in the floor.
	 * @param calls
	 * @return
	 */
	public boolean hasReachFloorForCall(Vector<Passenger> calls){
		passToExit.clear();
		callsToCome.clear();
		for(Passenger passenger : calls){
			if(passenger.getType() == Passenger.Type.PASSENGER){
				if(passenger.getDestinationFloor() == elevator.getPosition().intValue()){
					passToExit.add(passenger);
				}
			}else if(passenger.getType() == Passenger.Type.CALL || passenger.getType() == Passenger.Type.MOCK_CALL){
				//If there is a call in the floor return true
				if(passenger.getOriginFloor() == elevator.getPosition().intValue()){
					callsToCome.add(passenger);
				}
			}
		}
		if(!passToExit.isEmpty()||!callsToCome.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * Find the direction of the elevator and its taget floor.
	 */
	public void findDirectionAndTargetFloor() {
		Elevator.Direction dir = Elevator.Direction.NONE;
		Passenger highestCall = null;
		//Check for passengers
		if (elevator.getPassengers().size() > 0) {
			elevator.setDirection(elevator.getPassengers().get(0).getDirection());
			// Based on passenger
			highestCall = getHighestCall(dir,elevator.getPassengers());
			elevator.setTargetFloor(highestCall.getDestinationFloor());
			logger.debug("Elevator ["+elevator.getId()+"] Find direction based on passenger: "+elevator.getDirection() +", target floor: "+elevator.getTargetFloor());
		} 
		// Based on calls
		else{
			if (elevator.getCalls().size() > 0) {
				// Based on calls
				highestCall = getHighestCall(dir,elevator.getCalls());
				elevator.setTargetFloor(highestCall.getOriginFloor());
				if(elevator.getPosition().intValue()<highestCall.getOriginFloor()){
					elevator.setDirection(Elevator.Direction.UP);
				}else if(elevator.getPosition().intValue()>highestCall.getOriginFloor()){
					elevator.setDirection(Elevator.Direction.DOWN);
				}else{
					elevator.setDirection(Elevator.Direction.NONE);
				}
				logger.debug("Elevator ["+elevator.getId()+"] Find direction based on call: "+elevator.getDirection() +", target floor: "+elevator.getTargetFloor());
			}
		}
	}

	/**
	 * Get the highest call based on the elevator direction
	 * 
	 * @param dir
	 * @return
	 */
	public Passenger getHighestCall(Elevator.Direction dir, Vector<Passenger> calls) {
		int high = 0;
		Passenger hightCall = calls.get(0);
		for (Passenger call : calls) {
			int floor = 0; 
			if(call.getType() == Passenger.Type.CALL || call.getType() == Passenger.Type.MOCK_CALL){
				floor = call.getOriginFloor();
			}else if(call.getType() == Passenger.Type.PASSENGER){
				floor = call.getDestinationFloor();
			}
			int diff = Math.abs(elevator.getPosition().intValue() - floor);
			if (diff > high) {
				high = diff;
				hightCall = call;
			}
		}
		return hightCall;
	}
	
	/**
	 * Returns elevator.
	 * 
	 * @return elevator
	 */
	public Elevator getElevator() {
		return this.elevator;
	}

	/**
	 * Sets a value to attribute elevator.
	 * 
	 * @param newElevator
	 */
	public void setElevator(Elevator newElevator) {
		this.elevator = newElevator;
	}

	private void updateElevatorState(Double time) {

	}

	/**
	 * Check if all calls are going in same direction.
	 * 
	 * @param calls
	 *            the list of call to validate.
	 * @return
	 */
	private boolean allCallsHasSameDirection(Vector<Passenger> calls) {
		Passenger lastCall = null;
		for (Passenger aCall : calls) {
			if (lastCall == null) {
				lastCall = aCall;
			} else {
				if (lastCall.getDirection() != aCall.getDirection()) {
					return false;
				}
			}
		}
		return true;
	}

	private float calcNextMovement() {
		float time = this.activationTime;
		// Distance in metters
		float d = speed / time;
		// Distance in floors
		float position = d / floorGap;
		// return the position
		if (elevator.getState().equals(Elevator.State.MOVING)) {
			if (elevator.getDirection().equals(Elevator.Direction.DOWN)) {
				position = elevator.getPosition() - position;
			} else if (elevator.getDirection().equals(Elevator.Direction.UP)) {
				position = elevator.getPosition() + position;
			} else if (elevator.getDirection().equals(Elevator.Direction.NONE)) {
				position = elevator.getPosition();
			}
		}
		return position;
	}

	@Override
	public void addCall(Passenger call) {
		switch (call.getType()) {
		case CALL:
			removeMockCalls();
			elevator.addCall(call);
			break;
		case MOCK_CALL:
			if (elevator.getCalls().size() > 0
					|| elevator.getPassengers().size() > 0) {
				logger.error("Elevator ["+elevator.getId()+"] There are more calls, so something is wrong with that mock call");
			} else {
				logger.debug("Elevator ["+elevator.getId()+"] Going to rest the elevator " + elevator.getId());
			}
			break;
		default:
			new CallIllegalState("Elevator ["+elevator.getId()+"] Its no supposed to happen, the call is not of type CALL or PASSENGER");
		}
	}

	@Override
	public void removeCall(Passenger call) {
		this.elevator.getCalls().remove(call);
		logger.debug("The call " + call + ", was removed.");
	}

	@Override
	public void removeCalls(Vector<Passenger> calls) {
		for(Passenger call : calls){
			this.removeCall(call);
		}
	}

	@Override
	public void addPassengers(Vector<Passenger> passengers){
		removeMockCalls();
		for(Passenger passenger : passengers){
			this.addPassenger(passenger);
		}
	}
	
	@Override
	public void addPassenger(Passenger passenger) {
		if (directionValidForPassenger(passenger)) {
			passenger.setType(Passenger.Type.PASSENGER);
			elevator.addPassenger(passenger);
			//Sets the time the passeger gets into the elevator
			passenger.setEntryTime(this.buildingModel.getSimulationClock());
		} else {
			logger.error("Elevator ["+elevator.getId()+"] The passenger " + passenger + ", could not be added.");
		}
	}
	
	@Override
	public void removePassenger(Passenger passenger) {
		this.elevator.getPassengers().remove(passenger);
		//Sets the time the passeger exit from elevator
		passenger.setExitTime(this.buildingModel.getSimulationClock());
		//Update statistics
		statisticsManager.updateStatistics(passenger,this.getElevator().getId());
		logger.debug("Elevator ["+elevator.getId()+"] The passenger " + passenger + ", was removed.");
	}
	
	boolean directionValidForAnyPassenger(Vector<Passenger> pass){
		for(Passenger passenger : pass){
			if(directionValidForPassenger(passenger)){
				return true;
			}
		}
		return false;
	}
	/**
	 * Check if the direction of the call is valid
	 * 
	 * @param call
	 * @return
	 */
	private boolean directionValidForPassenger(Passenger pass) {
		if (pass.getDirection() == elevator.getDirection() || (elevator.getDirection() == Elevator.Direction.NONE)) {
			return true;
		} else {
			new CallIllegalState("Elevator ["+elevator.getId()+"] The passenger is not going in same direction of elevator. "+ pass);
			return false;
		}
	}

	/**
	 * Remove the mocks calls
	 */
	public void removeMockCalls() {
		for (Passenger call : elevator.getCalls()) {
			if (call.getType() == Passenger.Type.MOCK_CALL) {
				elevator.getCalls().remove(call);
			}
		}
	}

	public boolean makeListPassengersGetsDestFloor(int destFloor) {
		if (passToExit.size() > 0) {
			new CallIllegalState("Elevator ["+elevator.getId()+"] The list of exit passengers should be empty before that check , "
							+ passToExit);
			return false;
		}
		for (Passenger pass : elevator.getPassengers()) {
			if (pass.getDestinationFloor() == destFloor) {
				passToExit.add(pass);
			}
		}
		return passToExit.size() > 0;
	}

}
