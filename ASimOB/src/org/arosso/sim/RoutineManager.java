package org.arosso.sim;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Description of RoutineManager.
 */
public class RoutineManager {
    
    /**
     * Description of the property rutineManager.
     */
    public RoutineManager rutineManager = null;
    
    /**
     * Description of the property registeredRoutines.
     */
    public ArrayList<SimulationRoutine> registeredRoutines = null;
    
    /**
     * Description of the property simTime.
     */
    public Object simTime = null;
    
    /**
     * Description of the property deltaAdvance.
     */
    public Object deltaAdvance = null;
    
    /**
     * Description of the property model.
     */
    public Object model = null;
    
    //ROUTINES TO LOAD
    
    /** Checks if there are arrivals and enqeue the arrivals*/
    public ArrivalChecker arrivalChecker;
    /** Controls de dynamics of the elevator car*/
    public ElevatorController elevatorController;
    /** Generate users to the building*/
    public TrafficGenerator trafficGenerator;
    
    
    /**
     * The constructor.
     */
    public RoutineManager() {
    	super();
    }
    
    /**
     * Init the routine manager and load all default routine
     * @throws Exception 
     * @throws IOException 
     */
    private void init() throws IOException, Exception{
    	arrivalChecker=new ArrivalChecker();
    	elevatorController=new ElevatorController();
    	trafficGenerator=new TrafficGenerator();
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
     * Description of the method start.
     */
    public void start() {
    	// Start of user code for method start
    	// End of user code
    }
     
    /**
     * Description of the method pause.
     */
    public void pause() {
    	// Start of user code for method pause
    	// End of user code
    }
     
    /**
     * Description of the method stop.
     */
    public void stop() {
    	// Start of user code for method stop
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
     * Returns simTime.
     * @return simTime 
     */
    public Object getSimTime() {
    	return this.simTime;
    }
    
    /**
     * Sets a value to attribute simTime. 
     * @param newSimTime 
     */
    public void setSimTime(Object newSimTime) {
        this.simTime = newSimTime;
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
    
    /**
     * Returns model.
     * @return model 
     */
    public Object getModel() {
    	return this.model;
    }
    
    /**
     * Sets a value to attribute model. 
     * @param newModel 
     */
    public void setModel(Object newModel) {
        this.model = newModel;
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
