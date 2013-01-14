package org.arosso.routines.egcs.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnInputSVO {
	
	float posElevatorAtAssign;
	float passInElevator;
	float inputTraffic;
	float outputTraffic;
	float interfloorTraffic;
	float directionElevator;
	float timeToWait;
	
    /**
     * Logger
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void recordAI(){
		logger.info(posElevatorAtAssign+";"+
					passInElevator+";"+
					inputTraffic+";"+
					outputTraffic+";"+
					interfloorTraffic+";"+
					directionElevator+";"+
					timeToWait);
	}
}
