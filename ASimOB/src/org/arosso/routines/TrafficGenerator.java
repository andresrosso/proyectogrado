package org.arosso.routines;

import static org.arosso.util.Constants.ASIMOB_PATH;

import java.io.IOException;
import java.util.Vector;

import org.arosso.model.BuildingModel;
import org.arosso.model.Passenger;
import org.arosso.sim.SimulationRoutine;
import org.arosso.sim.TrafficModel;
import org.arosso.util.PropertiesBroker;
import org.arosso.util.PropertiesBroker.PROP_SET;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description of TrafficGenerator.
 */
public class TrafficGenerator extends SimulationRoutine {
	/**
	 * Description of the property basicTrafficModel.
	 */
	private TrafficModel trafficModel = null;

	private Vector<Passenger> generatedCalls = new Vector<Passenger>();
	/**
     * 
     */
	private BuildingModel buildingModel;

	/**
	 * Logger
	 */
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Default constructor
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public TrafficGenerator(String routineName) throws IOException, Exception {
		this(routineName, ASIMOB_PATH);
	}

	/**
	 * The constructor.
	 * 
	 * @throws Exception
	 */
	public TrafficGenerator(String routineName, String buildingPropsFile) throws IOException, Exception {
		super();
		// Read model properties
		PropertiesBroker propertiesBroker = PropertiesBroker.getInstance();
		String modelClass = propertiesBroker.getProperty(PROP_SET.SIMULATION, "trafficModelImplementation");
		Float trafficGenActivationTime = Float.valueOf(propertiesBroker.getProperty(PROP_SET.SIMULATION, "trafficGenActivationTime"));
		logger.info("BuildingModel made based on properties (" + buildingPropsFile + ")");
		ClassLoader classLoader = this.getClass().getClassLoader();
		Class myObjectClass = classLoader.loadClass(modelClass);
		trafficModel = (TrafficModel) myObjectClass.newInstance();
		logger.debug("TrafficGenerator created!");
		// Set variables
		this.setRoutineName(routineName);
		this.setActivationTime(trafficGenActivationTime);
		// Get building model
		buildingModel = BuildingModel.getInstance();
	}

	@Override
	synchronized public void execute() {
		int pass2gen = this.trafficModel.getEstimatedPassengerNumber(buildingModel.getSimulationClock());
		Float time = buildingModel.getSimulationClock();
		
		for (int j = 0; j <= pass2gen; j++) {
			int originFloor = this.trafficModel.getEstimatedOriginFloor(time);
			int destinationFloor = this.trafficModel.getEstimatedDestinationFloor(time);
			if(originFloor==destinationFloor){
				originFloor++;
			}
			Passenger passenger = new Passenger(
					this.trafficModel.getEstimatedArrivalTime(time).intValue(), 
					originFloor, 
					destinationFloor, 
					Passenger.Type.CALL);
			buildingModel.getCalls().add(passenger);
			logger.info("Call generated " + passenger);
		}
		//generateMockCalls();
		logger.info("Generating calls = " + generatedCalls);
		buildingModel.getCalls().addAll(generatedCalls);
		generatedCalls.clear();
	}

	public void generateMockCalls() {
		Passenger passenger = new Passenger((int) (buildingModel.getSimulationClock() + 0), 20, 39, Passenger.Type.CALL);
		generatedCalls.add(passenger);
		passenger = new Passenger((int) (buildingModel.getSimulationClock() + 5), 20, 33, Passenger.Type.CALL);
		generatedCalls.add(passenger);
		passenger = new Passenger((int) (buildingModel.getSimulationClock() + 24), 5, 37, Passenger.Type.CALL);
		generatedCalls.add(passenger);
		passenger = new Passenger((int) (buildingModel.getSimulationClock() + 27), 3, 32, Passenger.Type.CALL);
		generatedCalls.add(passenger);
		passenger = new Passenger((int) (buildingModel.getSimulationClock() + 24), 0, 37, Passenger.Type.CALL);
		generatedCalls.add(passenger);
		/*
		passenger = new Passenger((int) (buildingModel.getSimulationClock() + 10), 9, 1, Passenger.Type.CALL);
		generatedCalls.add(passenger);
		passenger = new Passenger((int) (buildingModel.getSimulationClock() + 53), 10, 13, Passenger.Type.CALL);
		generatedCalls.add(passenger);
		*/
	}

	/**
	 * Returns basicTrafficModel.
	 * 
	 * @return basicTrafficModel
	 */
	public TrafficModel getTrafficModel() {
		return this.trafficModel;
	}

	/**
	 * Sets a value to attribute basicTrafficModel.
	 * 
	 * @param newBasicTrafficModel
	 */
	public void setTrafficModel(TrafficModel newTrafficModel) {
		this.trafficModel = newTrafficModel;
	}

	@Override
	public String toString() {
		return this.trafficModel.getClass().toString();
	}

}
