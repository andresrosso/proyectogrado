package org.arosso.routines;
import org.arosso.model.Elevator;
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
     * Logger
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    
    /**
     * The constructor.
     */
    public ElevatorController(String routineName, Float activationTime) {
    	super(routineName,activationTime);
    }
    
    @Override
    public void execute() {
    	logger.info("Controlling the elevator car");
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
    
    
}
