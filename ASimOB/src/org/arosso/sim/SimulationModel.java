package org.arosso.sim;

import java.util.Observable;
/**
 * File generated from the model::SimulationModel uml Interface
 * Generated by the Acceleo 3 <i>UML to Java</i> generator.
 */


// Start of user code (user defined imports)	

// End of user code

/**
 * Description of SimulationModel.
 */
public class SimulationModel{
	
	private Float simulationClock=0f;
	
	private Integer delayTime=0;
	
	private Float deltaAdvaceTime=0F;
	
	private Long endSimulationTime=0L;

	public Float getSimulationClock() {
		simulationClock = (float) (Math.round((simulationClock)*10.0f)/10.0f);
		return simulationClock;
	}

	public void setSimulationClock(Float simulationClock) {
		this.simulationClock = (float) (Math.round((simulationClock)*10.0f)/10.0f);
	}

	public Integer getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}

	public Float getDeltaAdvaceTime() {
		return deltaAdvaceTime;
	}

	public void setDeltaAdvaceTime(Float deltaAdvaceTime) {
		this.deltaAdvaceTime = deltaAdvaceTime;
	}

	public Long getEndSimulationTime() {
		return endSimulationTime;
	}

	public void setEndSimulationTime(Long endSimulationTime) {
		this.endSimulationTime = endSimulationTime;
	}
	
}
