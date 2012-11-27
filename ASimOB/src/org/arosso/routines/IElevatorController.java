package org.arosso.routines;

import org.arosso.model.Passenger;

public interface IElevatorController {
	
	/**
	 * Add a call to the elevator
	 * @param call Call object
	 */
	public void addCall(Passenger call);

	/**
	 * Remove a call from the elevator
	 * @param call Call object
	 */
	public void removeCall(Passenger call);
	
	/**
	 * Add a passenger to the elevator
	 * @param call Call object
	 */
	public void addPassenger(Passenger passenger);

	/**
	 * Remove a passenger from the elevator
	 * @param call Call object
	 */
	public void removePassenger(Passenger passenger);
	
	
}
