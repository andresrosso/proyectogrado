package org.arosso.sim;

import static org.arosso.util.Constants.TRAFFIC_FILES_PATH;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import org.arosso.model.BuildingModel;
import org.arosso.model.Passenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description of BasicTrafficModel.
 */
public class PoissonTrafficModel implements TrafficModel {
	/**
	 * Description of the property trafficDensity.
	 */
	public Vector<Float> trafficDensity = null;
	/**
	 * Building model
	 */
	private BuildingModel buildingModel = null;
	/**
	 * Map of properties
	 */
	private Properties trafficProps;
	private Properties trafficIN;
	private Properties trafficINTERFLOOR;
	private Properties trafficOUT;
	/** Properties constants */
	private static String TRAFFIC_FILE = "traffic.properties";
	private static String INCOMMING_FILE = "incomming.tra";
	private static String INTERFLOOR_FILE = "interfloor.tra";
	private static String OUTCOMMING_FILE = "outcomming.tra";

	private int timeGap = 0;
	private int population = 0;
	private int numFractions = 2;
	private int countFractions = 0;
	
	/**
	 * Logger
	 */
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * The constructor.
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public PoissonTrafficModel() throws IOException, Exception {
		// Start of user code constructor
		super();
		buildingModel = BuildingModel.getInstance();
		logger.info(this.getClass() + " loaded!");
		// traffic properties
		trafficProps = loadPropsFile(TRAFFIC_FILES_PATH + TRAFFIC_FILE);
		timeGap = Integer.valueOf(trafficProps.getProperty("timegap"));
		population = Integer.valueOf(trafficProps.getProperty("population"));
		trafficIN = loadPropsFile(TRAFFIC_FILES_PATH + INCOMMING_FILE);
		trafficINTERFLOOR = loadPropsFile(TRAFFIC_FILES_PATH + INTERFLOOR_FILE);
		trafficOUT = loadPropsFile(TRAFFIC_FILES_PATH + OUTCOMMING_FILE);
	}
	
	

	/**
	 * Load properties file
	 * 
	 * @param buildingPropsFile
	 * @return
	 * @throws IOException
	 */
	private Properties loadPropsFile(String buildingPropsFile) throws IOException {
		Properties simProp = new Properties();
		InputStream is = this.getClass().getResourceAsStream(buildingPropsFile);
		simProp.load(is);
		is.close();
		return simProp;
	}

	@Override
	public int getEstimatedPassengerNumber(Float time) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector<Passenger> getPassengersForPeriod(int time) {
		Vector<Passenger> passengers = new Vector<Passenger>();
		try {
			time = time - (timeGap*countFractions);
			if(countFractions==numFractions){
				countFractions=0;
			}else{
				countFractions++;
			}
			// GENERATE INCOMMING TRAFFIC
			String trInS = (String) trafficIN.get(Integer.toString(time));
			int trIn = (int)((Float.valueOf(trInS)/100)*population);
			for (int j = 0; j < trIn; j++) {
				int arrivalTime = time + (int) (Math.random() * timeGap);
				int destFloor = (int) (Math.random() * (buildingModel.getNumFloors() - 1) + 1);
				Passenger passenger = new Passenger(arrivalTime, 0, destFloor, Passenger.Type.CALL);
				passengers.add(passenger);
			}
			logger.info("Incoming traffic Time("+time+")  ("+trIn+") = ");
			// GENERATE INTERFLOOR TRAFFIC
			String trInterS = (String) trafficINTERFLOOR.get(Integer.toString(time));
			int trInter = (int)((Float.valueOf(trInterS)/100)*population);
			for (int j = 0; j < trInter; j++) {
				int arrivalTime = time + (int) (Math.random() * timeGap);
				int originFloor = (int) (Math.random() * (buildingModel.getNumFloors() - 1) + 1);
				int destFloor = (int) (Math.random() * (buildingModel.getNumFloors() - 1) + 1);
				if (originFloor == destFloor) {
					originFloor = originFloor + 1;
					if (originFloor > buildingModel.getNumFloors()) {
						originFloor = originFloor - 2;
					}
				}
				Passenger passenger = new Passenger(arrivalTime, originFloor, destFloor, Passenger.Type.CALL);
				passengers.add(passenger);
			}
			logger.info("INTERFLOOR traffic Time("+time+") ("+trInter+") = ");
			// GENERATE OUTGOING TRAFFIC
			String trOutS = (String) trafficOUT.get(Integer.toString(time));
			int trOut = (int)((Float.valueOf(trOutS)/100)*population);
			for (int j = 0; j < trOut; j++) {
				int arrivalTime = time + (int) (Math.random() * timeGap);
				int originFloor = (int) (Math.random() * (buildingModel.getNumFloors() - 1) + 1);
				Passenger passenger = new Passenger(arrivalTime, originFloor, 0, Passenger.Type.CALL);
				passengers.add(passenger);
			}
			logger.info("OUTGOING traffic Time("+time+") ("+trOut+") = ");
		} catch (Exception e) {
			logger.error("There was a problem generating passengers",e);
		}
		return passengers;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see .TrafficModel
	 * 
	 * @param time
	 */
	public int getEstimatedOriginFloor(Float time) {
		int oriFloor = (int) (Math.random() * buildingModel.getNumFloors());
		return oriFloor;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see .TrafficModel
	 * 
	 * @param time
	 */
	public int getEstimatedDestinationFloor(Float time) {
		int destFloor = (int) (Math.random() * buildingModel.getNumFloors());
		return destFloor;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see .TrafficModel
	 * 
	 * @param time
	 */
	public Float getEstimatedArrivalTime(Float time) {
		// The arrival period is 5 minutes
		Float arrivalTime = time + (int) (Math.random() * 300);
		return arrivalTime;
	}

	/**
	 * Returns trafficDensity.
	 * 
	 * @return trafficDensity
	 */
	public Vector<Float> getTrafficDensity() {
		return this.trafficDensity;
	}

	/**
	 * Sets a value to attribute trafficDensity.
	 * 
	 * @param newTrafficDensity
	 */
	public void setTrafficDensity(Vector<Float> newTrafficDensity) {
		this.trafficDensity = newTrafficDensity;
	}

	public static void main(String[] args) {
		PoissonTrafficModel model;
		try {
			model = new PoissonTrafficModel();
			System.out.println(model.getPassengersForPeriod(0));
			System.out.println(model.getPassengersForPeriod(300));
			System.out.println(model.getPassengersForPeriod(600));
			System.out.println(model.getPassengersForPeriod(900));
			System.out.println(model.getPassengersForPeriod(1200));
			System.out.println(model.getPassengersForPeriod(1500));
			System.out.println(model.getPassengersForPeriod(1800));
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Override
	public Float getEstimatedTrafficIn(int time) {
		int fraction = time/300;
		time = fraction*300;
		return Float.valueOf((String)trafficIN.get(Integer.toString(time)));
	}

	@Override
	public Float getEstimatedTrafficOut(int time) {
		int fraction = time/300;
		time = fraction*300;
		return Float.valueOf((String)trafficOUT.get(Integer.toString(time)));
	}

	@Override
	public Float getEstimatedTrafficInterfloor(int time) {
		int fraction = time/300;
		time = fraction*300;
		return Float.valueOf((String)trafficINTERFLOOR.get(Integer.toString(time)));
	}

}
