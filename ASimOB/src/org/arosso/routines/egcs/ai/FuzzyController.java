package org.arosso.routines.egcs.ai;

import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.Rule;

import org.arosso.model.BuildingModel;
import org.arosso.model.Elevator;
import org.arosso.model.Passenger;
import org.arosso.routines.egcs.ElevatorGroupController;
import org.arosso.sim.RoutineManager;
import org.arosso.sim.TrafficModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuzzyController implements ElevatorGroupController {

	public static FIS fis;
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
	public FuzzyController() {
		super();
		try {
			// create and load default properties
			InputStream inFuzzyBrake = this.getClass().getClassLoader().getResourceAsStream("org/arosso/routines/egcs/ai/fuzzyEGController.fcl");
			fis = FIS.load(inFuzzyBrake, true);
			if (fis == null) { // Error while loading?
				System.err.println("Can't load file: '" + inFuzzyBrake + "'");
				return;
			}
		} catch (Exception e) {
			logger.error("Error creating fuzzy controller", e);
		}
	}

	@Override
	public int assignCall(Passenger passenger) {
		try {
			Elevator bestMatch = null;
			FuzzyController brake = new FuzzyController();
			// ANN
			AnnS sTime = new AnnS();
			AnnW wTime = new AnnW();
			double highestPriority = -100;
			// TRaffic model
			TrafficModel model = RoutineManager.getInstance().getTrafficGenerator().getTrafficModel();
			// Assign the nearest elevator in the same direction
			for (Elevator e : BuildingModel.getInstance().getElevators()) {
				// Variables
				int time = BuildingModel.getInstance().getSimulationClock().intValue();
				float trIN = model.getEstimatedTrafficIn(time) / 7;
				float trOUT = model.getEstimatedTrafficOut(time) / 7;
				float trINTERFLOOR = model.getEstimatedTrafficInterfloor(time) / 7;
				float passInElev = (float) ((float) e.getPassengers().size() / e.getCapacity());
				float posElev = Math.abs(e.getPosition() - passenger.getOriginFloor()) / BuildingModel.getInstance().getNumFloors();
				// Estimate Service Time
				AnnInputSVO.InputSVO inputSVO = AnnInputSVO.getInstance().new InputSVO(passInElev, trIN, trOUT, trINTERFLOOR, e.getDirection());
				double serviceTime = sTime.Go(inputSVO);
				// Estimate Waiting Time
				AnnInputWVO.InputWVO inputWVO = AnnInputWVO.getInstance().new InputWVO(posElev, passInElev, trIN, trOUT, trINTERFLOOR, e.getDirection());
				double waitingTime = wTime.Go(inputWVO);
				// Calcular paradas en PO y PD
				float cPo = 0;
				float cPd = 0;
				for (Passenger pass : e.getPassengers()) {
					if (pass.getDestinationFloor() == passenger.getOriginFloor()) {
						cPo++;
					} else if (pass.getDestinationFloor() == passenger.getDestinationFloor()) {
						cPd++;
					}
				}
				for (Passenger call : e.getCalls()) {
					if (call.getDestinationFloor() == passenger.getOriginFloor()) {
						cPo++;
					} else if (call.getDestinationFloor() == passenger.getDestinationFloor()) {
						cPd++;
					}
				}
				// Set inputs
				brake.fis.setVariable("tEspera", waitingTime);
				brake.fis.setVariable("tViaje", serviceTime);
				brake.fis.setVariable("porcentajeOcupacion", passInElev);
				brake.fis.setVariable("paradasEnPO", cPo / 10f);
				brake.fis.setVariable("paradasEnPD", cPd / 10f);

				// Evaluate
				brake.fis.evaluate();

				// Show output variable's chart
				double priority = fis.getVariable("prioridad").getLatestDefuzzifiedValue();

				// if (e.getDirection() == passenger.getDirection()) {
				if (priority > highestPriority) {
					highestPriority = priority;
					bestMatch = e;
				}
				// }
			}
			return bestMatch.getId();
		} catch (Exception e) {
			logger.error("Error on fuzzy elevatopr group ",e);
		}
		return -1;
		// If is null, then assign arbitrary elevator
		/*
		 * if (bestMatch == null) { return (int) (Math.random() *
		 * buildingModel.getNumElevators()); }
		 */
	}

	public double getOutPut(double te, double tv, double pc, double ppo, double ppd) {
		// Set inputs
		fis.setVariable("tEspera", te);
		fis.setVariable("tViaje", tv);
		fis.setVariable("porcentajeOcupacion", pc);
		fis.setVariable("paradasEnPO", ppo);
		fis.setVariable("paradasEnPD", ppd);
		// Evaluate
		fis.evaluate();
		// Show output variable's chart
		// fis.getVariable("brake").chartDefuzzifier(true);
		// fis.chart();
		System.out.println("Priority = " + fis.getVariable("prioridad").getLatestDefuzzifiedValue());
		return fis.getVariable("prioridad").getLatestDefuzzifiedValue();
	}

	public static void main(String[] args) {
		FuzzyController brake = new FuzzyController();
		brake.fis.chart();

		// Set inputs
		brake.fis.setVariable("tEspera", 0.9);
		brake.fis.setVariable("tViaje", 0.9);
		brake.fis.setVariable("porcentajeOcupacion", 0.1);
		brake.fis.setVariable("paradasEnPO", 0.2);
		brake.fis.setVariable("paradasEnPD", 0.2);

		// Evaluate
		brake.fis.evaluate();

	    // Show each rule (and degree of support)
	    for( Rule r : fis.getFunctionBlock("brake").getFuzzyRuleBlock("No1").getRules() )
	      System.out.println(r);
	    
		// Show output variable's chart
		brake.fis.getVariable("prioridad").chartDefuzzifier(true);

		// Print ruleSet
		System.out.println(brake.fis.getVariable("prioridad").getLatestDefuzzifiedValue());

	}

}
