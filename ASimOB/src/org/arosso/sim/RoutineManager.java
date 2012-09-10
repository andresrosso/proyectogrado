package org.arosso.sim;
import static org.arosso.util.Constants.ROUTINE_DEFAULT_ACTIVATION_TIME;

import java.io.IOException;
import java.util.ArrayList;

import org.arosso.model.BuildingModel;
import org.arosso.model.BuildingModel.SIM_STATE;
import org.arosso.routines.ArrivalChecker;
import org.arosso.routines.ElevatorController;
import org.arosso.routines.TrafficGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description of RoutineManager.
 */
public class RoutineManager extends Thread {
    
    /**
     * Description of the property rutineManager.
     */
    private RoutineManager rutineManager = null;
    
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
    
    //ROUTINES TO LOAD
    
    /** Checks if there are arrivals and enqeue the arrivals*/
    private ArrivalChecker arrivalChecker;
    /** Controls de dynamics of the elevator car*/
    private ElevatorController elevatorController;
    /** Generate users to the building*/
    private TrafficGenerator trafficGenerator;
    
    /**
     * Logger
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    
    /**
     * The constructor.
     * @throws Exception 
     * @throws IOException 
     */
    public RoutineManager() throws IOException, Exception {
    	super();
		init();
    }

    
    /**
     * Init the routine manager and load all default routine
     * @throws Exception 
     * @throws IOException 
     */
    private void init() throws IOException, Exception{
    	buildingModel = BuildingModel.getInstance();
    	arrivalChecker=new ArrivalChecker("ArrivalChecker",ROUTINE_DEFAULT_ACTIVATION_TIME);
    	elevatorController=new ElevatorController("ArrivalChecker",ROUTINE_DEFAULT_ACTIVATION_TIME);
    	trafficGenerator=new TrafficGenerator("ArrivalChecker");
    	registeredRoutines = new ArrayList<SimulationRoutine>();
    	registeredRoutines.add(arrivalChecker);
    	registeredRoutines.add(elevatorController);
    	registeredRoutines.add(trafficGenerator);
    }

	@Override
	public void run() {
		while(buildingModel.getSimulationClock()<=buildingModel.getEndSimulationTime() && 
				buildingModel.simState!=SIM_STATE.STOPPED){
			buildingModel.setSimulationClock( buildingModel.getSimulationClock() + buildingModel.getDeltaAdvaceTime() );
			for(SimulationRoutine routine : registeredRoutines){
				  if(buildingModel.getSimulationClock() % routine.activationTime == 0){
					  logger.debug("Executing routine ("+routine.getRoutineName()+") at time:"+buildingModel.getSimulationClock());
					  routine.execute();
				  }
				  try {
					  Thread.sleep(buildingModel.getDelayTime());
				  } catch (InterruptedException e) {
						logger.error("Error dalaying executed routines",e);
						e.printStackTrace();
				  }
			}
		}
		logger.info("Simulation has finished state : "+buildingModel.simState);
	}
    
    /**
     * Description of the method addRoutine.
     * @param routine 
     */
    public void addRoutine(SimulationRoutine routine) {
    	// Start of user code for method addRoutine
    	// End of user code
    }
     
    /**
     * Description of the method removeRoutine.
     * @param routine 
     */
    public void removeRoutine(SimulationRoutine routine) {
    	// Start of user code for method removeRoutine
    	// End of user code
    }
     
    /**
     * Description of the method executeRoutine.
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
     * @return elevatorController 
     */
    public ElevatorController getElevatorController() {
    	return this.elevatorController;
    }
    
    /**
     * Sets a value to attribute elevatorController. 
     * @param newElevatorController 
     */
    public void setElevatorController(ElevatorController newElevatorController) {
        this.elevatorController = newElevatorController;
    }
    
    /**
     * Returns rutineManager.
     * @return rutineManager 
     */
    public RoutineManager getRutineManager() {
    	return this.rutineManager;
    }
    
    /**
     * Sets a value to attribute rutineManager. 
     * @param newRutineManager 
     */
    public void setRutineManager(RoutineManager newRutineManager) {
        this.rutineManager = newRutineManager;
    }
    
    
    /**
     * Returns registeredRoutines.
     * @return registeredRoutines 
     */
    public ArrayList<SimulationRoutine> getRegisteredRoutines() {
    	return this.registeredRoutines;
    }
    
    /**
     * Sets a value to attribute registeredRoutines. 
     * @param newRegisteredRoutines 
     */
    public void setRegisteredRoutines(ArrayList<SimulationRoutine>  newRegisteredRoutines) {
        this.registeredRoutines = newRegisteredRoutines;
    }
    
    /**
     * Returns deltaAdvance.
     * @return deltaAdvance 
     */
    public Object getDeltaAdvance() {
    	return this.deltaAdvance;
    }
    
    /**
     * Sets a value to attribute deltaAdvance. 
     * @param newDeltaAdvance 
     */
    public void setDeltaAdvance(Object newDeltaAdvance) {
        this.deltaAdvance = newDeltaAdvance;
    }
    
    @Override
    public String toString() {
    	String routines="";
    	for(SimulationRoutine routine : registeredRoutines){
    		routines += routine;
    	}
    	return super.toString();
    }

    
    
}
