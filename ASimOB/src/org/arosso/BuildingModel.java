/**
 * File generated from the model::BuildingModel uml Class
 * Generated by the Acceleo 3 <i>UML to Java</i> generator.
 */


import java.util.HashSet;
import ;
import ;
import ;
import ;
import ;
import ;
// Start of user code (user defined imports)	

// End of user code

/**
 * Alejandra Bordamalo
 */
public class BuildingModel extends SimulationModel {
    /**
     * Description of the property elevator.
     */
    public HashSet<Elevator> elevator = new HashSet<Elevator>();
    
    /**
     * Description of the property floor.
     */
    public HashSet<Floor> floor = new HashSet<Floor>();
    
    /**
     * Description of the property passanger.
     */
    public HashSet<Passenger> passanger = new HashSet<Passenger>();
    
    /**
     * Description of the property elevatorGroupController.
     */
    public ElevatorGroupController elevatorGroupController = null;
    
    /**
     * Description of the property time.
     */
    public final Long time = null;
    
    /**
     * Description of the property numFloors.
     */
    public Object numFloors = null;
    
    /**
     * Description of the property floorGapDistance.
     */
    public Object floorGapDistance = null;
    
    /**
     * Description of the property numElevator.
     */
    public Object numElevator = null;
    
    /**
     * http://intermediatetwo.webs.com/listening.htm  en 
     */
    public HashSet<Passenger> users = new HashSet<Passenger>();
    
    /**
     * Description of the property futureArrivals.
     */
    public HashSet<Passenger> futureArrivals = new HashSet<Passenger>();
    
    // Start of user code (user defined attributes)
    
    // End of user code
    
    /**
     * The constructor.
     */
    public BuildingModel() {
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
    public HashSet<Elevator> getElevator() {
    	return this.elevator;
    }
    
    /**
     * Sets a value to attribute elevator. 
     * @param newElevator 
     */
    public void setElevator(HashSet<Elevator> newElevator) {
        this.elevator = newElevator;
    }
    
    /**
     * Returns floor.
     * @return floor 
     */
    public HashSet<Floor> getFloor() {
    	return this.floor;
    }
    
    /**
     * Sets a value to attribute floor. 
     * @param newFloor 
     */
    public void setFloor(HashSet<Floor> newFloor) {
        this.floor = newFloor;
    }
    
    /**
     * Returns passanger.
     * @return passanger 
     */
    public HashSet<Passenger> getPassanger() {
    	return this.passanger;
    }
    
    /**
     * Sets a value to attribute passanger. 
     * @param newPassanger 
     */
    public void setPassanger(HashSet<Passenger> newPassanger) {
        this.passanger = newPassanger;
    }
    
    /**
     * Returns elevatorGroupController.
     * @return elevatorGroupController 
     */
    public ElevatorGroupController getElevatorGroupController() {
    	return this.elevatorGroupController;
    }
    
    /**
     * Sets a value to attribute elevatorGroupController. 
     * @param newElevatorGroupController 
     */
    public void setElevatorGroupController(ElevatorGroupController newElevatorGroupController) {
        this.elevatorGroupController = newElevatorGroupController;
    }
    
    /**
     * Returns time.
     * @return time 
     */
    public Long getTime() {
    	return this.time;
    }
    
    /**
     * Returns numFloors.
     * @return numFloors 
     */
    public Object getNumFloors() {
    	return this.numFloors;
    }
    
    /**
     * Sets a value to attribute numFloors. 
     * @param newNumFloors 
     */
    public void setNumFloors(Object newNumFloors) {
        this.numFloors = newNumFloors;
    }
    
    /**
     * Returns floorGapDistance.
     * @return floorGapDistance 
     */
    public Object getFloorGapDistance() {
    	return this.floorGapDistance;
    }
    
    /**
     * Sets a value to attribute floorGapDistance. 
     * @param newFloorGapDistance 
     */
    public void setFloorGapDistance(Object newFloorGapDistance) {
        this.floorGapDistance = newFloorGapDistance;
    }
    
    /**
     * Returns numElevator.
     * @return numElevator 
     */
    public Object getNumElevator() {
    	return this.numElevator;
    }
    
    /**
     * Sets a value to attribute numElevator. 
     * @param newNumElevator 
     */
    public void setNumElevator(Object newNumElevator) {
        this.numElevator = newNumElevator;
    }
    
    /**
     * Returns users.
     * @return users http://intermediatetwo.webs.com/listening.htm  en 
     */
    public HashSet<Passenger> getUsers() {
    	return this.users;
    }
    
    /**
     * Sets a value to attribute users. 
     * @param newUsers http://intermediatetwo.webs.com/listening.htm  en 
     */
    public void setUsers(HashSet<Passenger> newUsers) {
        this.users = newUsers;
    }
    
    /**
     * Returns futureArrivals.
     * @return futureArrivals 
     */
    public HashSet<Passenger> getFutureArrivals() {
    	return this.futureArrivals;
    }
    
    /**
     * Sets a value to attribute futureArrivals. 
     * @param newFutureArrivals 
     */
    public void setFutureArrivals(HashSet<Passenger> newFutureArrivals) {
        this.futureArrivals = newFutureArrivals;
    }
    
    
}
