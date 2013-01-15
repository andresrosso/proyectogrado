package org.arosso.sim;

import static org.arosso.util.Constants.ROUTINE_DEFAULT_ACTIVATION_TIME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.arosso.model.BuildingModel;
import org.arosso.model.BuildingModel.SIM_STATE;
import org.arosso.routines.ArrivalChecker;
import org.arosso.routines.ElevatorController;
import org.arosso.routines.TrafficGenerator;
import org.arosso.routines.egcs.ai.AnnInputSVO;
import org.arosso.routines.egcs.ai.AnnInputWVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description of RoutineManager.
 */
public class RoutineManager extends Thread {
	/**
	 * Description of the property rutineManager.
	 */
	private static RoutineManager instance = null;

	/**
	 * Description of the property registeredRoutines.
	 */
	private ArrayList<SimulationRoutine> registeredRoutines = null;

	/**
	 * Description of the property deltaAdvance.
	 */
	private Object deltaAdvance = null;

	/**
	 * Description of the property model.
	 */
	private BuildingModel buildingModel;

	// ROUTINES TO LOAD

	/** Checks if there are arrivals and enqeue the arrivals */
	private ArrivalChecker arrivalChecker;
	/** Controls de dynamics of the elevator car, each car has a controller*/
	private ElevatorController[] elevatorController;
	/** Generate users to the building */
	private TrafficGenerator trafficGenerator;

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * The constructor.
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	private RoutineManager() throws IOException, Exception {
		super();
		init();
	}
	
	public static RoutineManager getInstance() throws Exception{
		if(instance==null){
			instance = new RoutineManager();
		}
		return instance;
	}

	/**
	 * Init the routine manager and load all default routine
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	private void init() throws IOException, Exception {
		//Create a list of routines
		registeredRoutines = new ArrayList<SimulationRoutine>();
		//Gets the building model
		buildingModel = BuildingModel.getInstance();
		//Add traffic generator routine (dynamic trafficGenerator class load)
		trafficGenerator = new TrafficGenerator("Traffic Generator");
		registeredRoutines.add(trafficGenerator);
		//Add Arrival checker routine
		arrivalChecker = new ArrivalChecker("Arrival Checker", ROUTINE_DEFAULT_ACTIVATION_TIME);
		registeredRoutines.add(arrivalChecker);
		elevatorController = new ElevatorController[buildingModel.getNumElevators()];
		//Add a elevator controller routine for each elevator
		for(int j=0; j<buildingModel.getNumElevators(); j++){
			elevatorController[j] = new ElevatorController("Elevator Controller ["+j+"]", 
					ROUTINE_DEFAULT_ACTIVATION_TIME, 
					buildingModel.getElevators().get(j));
			registeredRoutines.add(elevatorController[j] );
		}

		
	}

	@Override
	public void run() {
		List<SimulationRoutine> listSync = Collections.synchronizedList(registeredRoutines);
		synchronized(listSync){
			while (buildingModel.getSimulationClock() <= buildingModel.getEndSimulationTime() && buildingModel.simState != SIM_STATE.STOPPED) {
				if (buildingModel.simState == SIM_STATE.STARTED) {
					// Execute the routines that take place at that time
					for (SimulationRoutine routine : listSync) {
						//Calc the routine 
						float roudedCarry = (float) (Math.round((buildingModel.getSimulationClock() % routine.activationTime)*10.0f)/10.0f);
						//Simulation running
						//logger.info("SimClock "+buildingModel.getSimulationClock()+", " +	"Routine (" +routine.getRoutineName() +") ActTime("+routine.getActivationTime()+") = " + (roudedCarry) );
						if ( roudedCarry == 0) {
							logger.debug("Executing routine (" + routine.getRoutineName() + ") at time:" + buildingModel.getSimulationClock());
							synchronized(logger){
								routine.execute();
							}
						}
					}
					// Advance time
					buildingModel.setSimulationClock(buildingModel.getSimulationClock() + buildingModel.getDeltaAdvaceTime());
				}
				//Sleep the time for X delay time
				try {
					Thread.sleep(buildingModel.getDelayTime());
				} catch (InterruptedException e) {
					logger.error("Error dalaying executed routines", e);
					e.printStackTrace();
				}
			}
		}
		//Save the ANN
		AnnInputWVO.getInstance().writeToFile();
		AnnInputSVO.getInstance().writeToFile();
		logger.info("Simulation has finished state : " + buildingModel.simState);
	}

	/**
	 * Description of the method addRoutine.
	 * 
	 * @param routine
	 */
	public void addRoutine(SimulationRoutine routine) {
		// Start of user code for method addRoutine
		// End of user code
	}

	/**
	 * Description of the method removeRoutine.
	 * 
	 * @param routine
	 */
	public void removeRoutine(SimulationRoutine routine) {
		// Start of user code for method removeRoutine
		// End of user code
	}

	/**
	 * Description of the method executeRoutine.
	 * 
	 * @param routine
	 */
	public void executeRoutine(SimulationRoutine routine) {
		// Start of user code for method executeRoutine
		// End of user code
	}

	/**
	 * Description of the method advanceSimulationClock.
	 */
	public void advanceSimulationClock() {
		// Start of user code for method advanceSimulationClock
		// End of user code
	}

	/**
	 * Description of the method RoutineManger.
	 * 
	 * @param model
	 */
	public void RoutineManger(SimulationModel model) {
		// Start of user code for method RoutineManger
		// End of user code
	}

	// Start of user code (user defined methods)

	// End of user code

	/**
	 * Returns elevatorController.
	 * 
	 * @return elevatorController
	 */
	public ElevatorController[] getElevatorController() {
		return this.elevatorController;
	}

	/**
	 * Sets a value to attribute elevatorController.
	 * 
	 * @param newElevatorController
	 */
	public void setElevatorController(ElevatorController[] newElevatorController) {
		this.elevatorController = newElevatorController;
	}

	/**
	 * Returns registeredRoutines.
	 * 
	 * @return registeredRoutines
	 */
	public ArrayList<SimulationRoutine> getRegisteredRoutines() {
		return this.registeredRoutines;
	}

	/**
	 * Sets a value to attribute registeredRoutines.
	 * 
	 * @param newRegisteredRoutines
	 */
	public void setRegisteredRoutines(ArrayList<SimulationRoutine> newRegisteredRoutines) {
		this.registeredRoutines = newRegisteredRoutines;
	}

	/**
	 * Returns deltaAdvance.
	 * 
	 * @return deltaAdvance
	 */
	public Object getDeltaAdvance() {
		return this.deltaAdvance;
	}

	/**
	 * Sets a value to attribute deltaAdvance.
	 * 
	 * @param newDeltaAdvance
	 */
	public void setDeltaAdvance(Object newDeltaAdvance) {
		this.deltaAdvance = newDeltaAdvance;
	}

	@Override
	public String toString() {
		String routinesList = "";
		for (SimulationRoutine routine : registeredRoutines) {
			routinesList += routine;
		}
		return routinesList;
	}

	public TrafficGenerator getTrafficGenerator() {
		return trafficGenerator;
	}

}
