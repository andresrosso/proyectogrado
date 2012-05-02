package org.arosso.sim;
import java.util.Observer;

import org.arosso.model.Elevator;

/**
 * Description of ElevatorController.
 */
public class ElevatorController extends SimulationRoutine {
    
    /**
     * Description of the property elevator.
     */
    public Elevator elevator = null;
    
    
    /**
     * The constructor.
     */
    public ElevatorController() {
    	// Start of user code constructor
    	super();
    	// End of user code
    }
    
    // Start of user code (user defined methods)
    
    // End of user code
    
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
    
    
}
