package org.arosso.routines;

import java.util.Vector;

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
	 * Remove a call from the elevator
	 * @param call Call object
	 */
	public void removeCalls(Vector<Passenger> call);
	
	/**
	 * Add a passenger to the elevator
	 * @param call Call object
	 */
	public void addPassenger(Passenger passenger);	
	
	/**
	 * Add a passenger to the elevator
	 * @param call Call object
	 */
	public void addPassengers(Vector<Passenger> passenger);

	/**
	 * Remove a passenger from the elevator
	 * @param call Call object
	 */
	public void removePassenger(Passenger passenger);
	
	
}
