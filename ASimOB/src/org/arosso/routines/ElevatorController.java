package org.arosso.routines;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.arosso.exception.CallIllegalState;
import org.arosso.exception.ElevatorIlegalState;
import org.arosso.model.BuildingModel;
import org.arosso.model.Elevator;
import org.arosso.model.Passenger;
import org.arosso.routines.egcs.ai.AnnTrainer;
import org.arosso.sim.SimulationRoutine;
import org.arosso.stats.StatisticsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description of ElevatorController.
 */
public class ElevatorController extends SimulationRoutine implements
		IElevatorController, Observer {

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
	
	//Trainer for ANN
	AnnTrainer trainer = new AnnTrainer();
	
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
		//Add observer to notify a call added
		elevator.addObserver(this);
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
    	if((currentTaskTime.floatValue()>taskTimeMAX.floatValue()) ||currentTaskTime.floatValue()<0 || taskTimeMAX.floatValue()<0 ){
    		logger.error("The current task time is not appropiate, look at that!!! currentTaskTime( "+currentTaskTime.floatValue()+") taskTimeMAX ("+taskTimeMAX.floatValue()+")");
    	}
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
    			new ElevatorIlegalState("The elevator ("+this.getElevator().getId()+") is supposed to be -MOVING, RESTING OR OUT OF SERVICE- but is "+this.getElevator().getState() );
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
		callsToCome.clear();
		// If there are calls
		if (elevator.getCalls().size() > 0) {
			// if all call has the same direction
			if (allCallsHasSameDirection(elevator.getCalls())) {
				// find direction and target floor
				findDirectionAndTargetFloor();
			}else{
				// get better route call
				logger.error("Error on doRestingTask>> Elevator ["+elevator.getId()+"] This is not common but, there are 2 calls in different direction");
				findDirectionAndTargetFloor();
			}
			this.changeState(Elevator.State.MOVING, 0);
		} else {
			if(elevator.getPosition().intValue()==elevator.getRestFloor()){
				return;	
			}else{
				elevator.getCalls().add(new Passenger(buildingModel.getSimulationClock().intValue()+1, elevator.getRestFloor(),elevator.getRestFloor(),Passenger.Type.MOCK_CALL));
				findDirectionAndTargetFloor();
				this.changeState(Elevator.State.MOVING, 0);
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
				this.changeState(Elevator.State.OPEN_DOOR, elevator.getDoorOpenTime());
			}//Check if there are calls in floor
			else if(hasReachFloorForCall(elevator.getCalls())){
				//Check if it is a mock call
				if(elevator.getCalls().get(0).getType() == Passenger.Type.MOCK_CALL){
					elevator.setTargetFloor(elevator.getPosition().intValue());
					elevator.setDirection(Elevator.Direction.NONE);
					this.changeState(Elevator.State.RESTING, 0);
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
						this.changeState(Elevator.State.OPEN_DOOR, elevator.getDoorOpenTime());
					}else{
						if (elevator.getPassengers().isEmpty()) {
							if(elevator.getPosition().intValue()==elevator.getTargetFloor()){
								elevator.setDirection(Elevator.Direction.NONE);
								this.changeState(Elevator.State.OPEN_DOOR, elevator.getDoorOpenTime());
							}
							else{
								return;
							}
						}else{
							logger.info("Elevator ["+elevator.getId()+"] Continue moving ignoring different direction call" + callsToCome);
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
		callsToCome.clear();
		//If there are passengers that get destination floor
		if(hasReachFloorForCall(elevator.getPassengers())){
			this.changeState(Elevator.State.EXIT_PASS, elevator.getPassangerTransferTime()*(passToExit.size()));
			logger.debug("Elevator ["+elevator.getId()+"] has reach floor to exit a passenger ");
		}else if(hasReachFloorForCall(elevator.getCalls())){
			//Check if there are passengers
			if (!elevator.getPassengers().isEmpty()) {
				findDirectionAndTargetFloor();
				callsToCome = removeDifferentDirectionCalls(callsToCome, elevator.getDirection());
			}else{
				if(allCallsHasSameDirection(callsToCome)){
					logger.debug("Elevator ["+elevator.getId()+"] All calls has the same direction.");
				}else{
					logger.error("Error on doOpenDoorTask>> Elevator ["+elevator.getId()+"] There are calls in different direction.");
				}
			}
			logger.debug("Elevator ["+elevator.getId()+"] has reach floor to pickUp a passenger ");
			findDirectionAndTargetFloor();
			this.changeState(Elevator.State.COMING_PASS, elevator.getPassangerTransferTime()*(callsToCome.size()));
		}else{
			this.changeState(Elevator.State.CLOSE_DOOR, elevator.getDoorCloseTime());
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
			this.changeState(Elevator.State.RESTING, 0);
			return;
		}
		this.changeState(Elevator.State.MOVING, 0);
		callsToCome.clear();
		passToExit.clear();
	}

	/**
	 * Coming passenger to the elevator
	 */
	private void doComingPassengerTask() {
		this.removeCalls(callsToCome);
		this.addPassengers(callsToCome);
		logger.error("COMING PASS>>"+callsToCome);
		if(directionValidForAnyPassenger(elevator.getPassengers())){
			this.changeState(Elevator.State.CLOSE_DOOR, elevator.getDoorCloseTime());
			logger.error("COMING PASS DIR VALID>>"+callsToCome);
		}else{
			logger.error("Error on doComingPassengerTask>> Elevator ["+elevator.getId()+"] There are passengers in different direction, pass: "+elevator.getPassengers());
		}
		callsToCome.clear();
	}
	
	/**
	 * Remove calls in other direcion
	 */
	public Vector<Passenger> removeDifferentDirectionCalls(Vector<Passenger> callList, Elevator.Direction validDirection ){
		Vector<Passenger> callLisToRemove = new Vector<Passenger>();
		for(Passenger passenger : callList){
			if(passenger.getDirection() == validDirection || elevator.getDirection() == Elevator.Direction.NONE ){
				logger.debug("Elevator ["+elevator.getId()+"] The call is valid for the direction. call = "+passenger);
			}else{
				callLisToRemove.add(passenger);
			}
		}
		logger.info("removeDifferentDirectionCalls>> Elevator ["+elevator.getId()+"] removecalls = "+callLisToRemove);
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
		//Based on callstocome
		else if (callsToCome.size() > 0) {
			elevator.setDirection(callsToCome.get(0).getDirection());
			// Based on passenger
			highestCall = getHighestCall(dir,callsToCome);
			elevator.setTargetFloor(highestCall.getDestinationFloor());
			logger.debug("Elevator ["+elevator.getId()+"] Find direction based on callstocome: "+elevator.getDirection() +", target floor: "+elevator.getTargetFloor());
		} 
		// Based on calls
		else if (elevator.getCalls().size() > 0) {
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

		logger.info("findDirectionAndTargetFloor>> passengers ["+elevator.getPassengers()+"], callsToCome ["+callsToCome+"], calls ["+elevator.getCalls()+"]  = Dir("+elevator.getDirection()+") Tf("+elevator.getTargetFloor()+")" );
	}
	
	/**
	 * Find the direction of the elevator and its taget floor.
	 */
	public int recalculateTargetFloor(Passenger call) {
		int higestFloor = this.getElevator().getTargetFloor();
		// Based on call
		if(this.elevator.getDirection()==Elevator.Direction.UP && call.getOriginFloor()>higestFloor){
			return  call.getOriginFloor();
		}else if(this.elevator.getDirection()==Elevator.Direction.DOWN && call.getOriginFloor()<higestFloor){
			return  call.getOriginFloor();
		}
		return higestFloor;
	}

	/**
	 * Get the highest call based on the elevator direction
	 * 
	 * @param dir
	 * @return
	 */
	public Passenger getHighestCall(Elevator.Direction dir, Vector<Passenger> calls) {
		
		Passenger hightCall = calls.get(0);
		int highFloor;
		//If is a call or a passenger
		if(hightCall.getType() == Passenger.Type.CALL || hightCall.getType() == Passenger.Type.MOCK_CALL){
			highFloor=hightCall.getOriginFloor();
		}else{
			highFloor=hightCall.getDestinationFloor();
		}
		
		//If the elevator does not have direction, take the on way call or change direction
		if(dir==Elevator.Direction.NONE){
			boolean in_it = false;
			int hightDiff = 0;
			for (Passenger call : calls) {
				if(call.getType() == Passenger.Type.CALL || call.getType() == Passenger.Type.MOCK_CALL){
					//Getting account of the last elevator direction
					if(elevator.getDirection()==Elevator.Direction.DOWN && call.getOriginFloor()<elevator.getPosition().intValue() && (call.getOriginFloor()<hightCall.getOriginFloor())){
							hightCall = call;
							in_it = true;
					}else if(elevator.getDirection()==Elevator.Direction.UP && call.getOriginFloor()<elevator.getPosition().intValue() && (call.getOriginFloor()>hightCall.getOriginFloor())){
							hightCall = call;
							in_it = true;
					}
				}
			}
			//If there are not calls in the same direction
			if(in_it==false){
				for (Passenger call : calls) {
					if(call.getType() == Passenger.Type.CALL || call.getType() == Passenger.Type.MOCK_CALL){
						if(Math.abs(call.getOriginFloor()-elevator.getPosition().intValue())>hightDiff){
								hightCall = call;
								hightDiff = Math.abs(call.getOriginFloor()-elevator.getPosition().intValue());
								in_it=false;
						}
					}
				}
			}
		}
		//If the elevator has direction, take the highest call 
		else{
			for (Passenger call : calls) {
				int floor = 0; 
				//for passenger
				if(call.getType() == Passenger.Type.PASSENGER){
					floor = call.getDestinationFloor();
					if(floor>highFloor && dir==Elevator.Direction.UP){
						highFloor = floor;
						hightCall = call;
					}else if(floor<highFloor && dir==Elevator.Direction.DOWN){
						highFloor = floor;
						hightCall = call;
					}
				}
				//for calls
				else if( (call.getType() == Passenger.Type.CALL || call.getType() == Passenger.Type.MOCK_CALL) && call.getDirection()==dir){
					floor = call.getOriginFloor();
					if(floor>highFloor && dir==Elevator.Direction.UP){
						highFloor = floor;
						hightCall = call;
					}else if(floor<highFloor && dir==Elevator.Direction.DOWN){
						highFloor = floor;
						hightCall = call;
					}
				}
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
				logger.error("Error on addCall>> Elevator ["+elevator.getId()+"] There are more calls, so something is wrong with that mock call");
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
			passenger.setEntryTime(buildingModel.getSimulationClock().intValue());
			passenger.setType(Passenger.Type.PASSENGER);
			elevator.addPassenger(passenger);
			//Sets the time the passeger gets into the elevator
		} else {
			logger.error("Error on addPassenger>> Elevator ["+elevator.getId()+"] The passenger " + passenger + ", could not be added.");
		}
	}
	
	@Override
	public void removePassenger(Passenger passenger) {
		passenger.setExitTime(this.buildingModel.getSimulationClock().intValue());
		//Update statistics
		statisticsManager.updateStatistics(passenger,this.getElevator().getId());
		//Sets the time the passenger exit from elevator
		this.elevator.removePassenger(passenger);
		logger.info("Elevator ["+elevator.getId()+"] The passenger " + passenger + ", was removed. -> "+passenger.toStringComplete());
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
	
	private void changeState(Elevator.State state,float taskTimeMAX){
		elevator.setState(state);
		this.taskTimeMAX = taskTimeMAX;
		if(taskTimeMAX>0){
			currentTaskTime = buildingModel.getDeltaAdvaceTime();
		}else{
			currentTaskTime = 0f;
		}
		logger.info("Changing state ("+state+") timeMax("+taskTimeMAX+")");
	}

	@Override
	public void update(Observable obj, Object arg) {
		if (arg instanceof Passenger) {
			Passenger pass = (Passenger) arg;
			int targetFloor = this.recalculateTargetFloor(pass);
			this.elevator.setTargetFloor(targetFloor);
			logger.info("Call added>> Time ("+buildingModel.getSimulationClock()+") " +"Call "+pass.toStringComplete()+" TF("+targetFloor+")");
		}
	}
}
