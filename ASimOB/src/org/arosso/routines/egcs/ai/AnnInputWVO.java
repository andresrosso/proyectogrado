package org.arosso.routines.egcs.ai;

import static org.arosso.util.Constants.ANNCONTROLLER_FILES_PATH;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Set;

import org.arosso.model.Elevator;
import org.arosso.model.Elevator.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnInputWVO {
	
	private static AnnInputWVO instance = null;
	
	/**
	 * INPUT MAP
	 */
	private HashMap<String, InputWVO> inputWMap = new HashMap<String, InputWVO>();
	
    /**
     * Logger
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());
    

	class InputWVO{
		float posElevatorAtAssign;
		float passInElevator;
		float inputTraffic;
		float outputTraffic;
		float interfloorTraffic;
		float directionElevator;
		float timeToWait;
		
		public InputWVO() {
			super();
		}

		public InputWVO(float posElevatorAtAssign, float passInElevator, float inputTraffic, float outputTraffic, float interfloorTraffic, Elevator.Direction directionElevatorT) {
			super();
			this.posElevatorAtAssign = posElevatorAtAssign;
			this.passInElevator = passInElevator;
			this.inputTraffic = inputTraffic;
			this.outputTraffic = outputTraffic;
			this.interfloorTraffic = interfloorTraffic;
			if(directionElevatorT==Direction.UP){
				directionElevator = 1;
			}else if(directionElevatorT==Direction.DOWN){
				directionElevator = 0.5f;
			}else{
				directionElevator = 0;
			}
		}

		public String toString(){
			return posElevatorAtAssign+";"+
					passInElevator+";"+
					inputTraffic+";"+
					outputTraffic+";"+
					interfloorTraffic+";"+
					directionElevator+";"+
					timeToWait;
		}
	}
	
	private AnnInputWVO() {
		super();
	}
	
	public static AnnInputWVO getInstance(){
		if(instance==null){
			instance = new AnnInputWVO();
		}
		return instance;
	}
	
	public void addInputWVO(float posElevatorAtAssign, float passInElevator, float inputTraffic, float outputTraffic, float interfloorTraffic, Elevator.Direction directionElevator, String pass){
		InputWVO annInputWVO = new InputWVO(posElevatorAtAssign,passInElevator,inputTraffic,outputTraffic,interfloorTraffic,directionElevator);
		this.inputWMap.put(pass, annInputWVO);
	}

	public float getPosElevatorAtAssign(String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		return annInputWVO.posElevatorAtAssign;
	}

	public void setPosElevatorAtAssign(float posElevatorAtAssign, String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		annInputWVO.posElevatorAtAssign=posElevatorAtAssign;
	}

	public float getPassInElevator(String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		return annInputWVO.passInElevator;
	}

	public void setPassInElevator(float passInElevator, String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		annInputWVO.passInElevator = passInElevator;
	}

	public float getInputTraffic(String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		return annInputWVO.inputTraffic;
	}

	public void setInputTraffic(float inputTraffic, String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		annInputWVO.inputTraffic = inputTraffic;
	}

	public float getOutputTraffic(String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		return annInputWVO.outputTraffic;
	}

	public void setOutputTraffic(float outputTraffic, String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		annInputWVO.outputTraffic = outputTraffic;
	}

	public float getInterfloorTraffic(String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		return annInputWVO.interfloorTraffic;
	}

	public void setInterfloorTraffic(float interfloorTraffic, String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		annInputWVO.interfloorTraffic = interfloorTraffic;
	}

	public float getDirectionElevator(String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		return annInputWVO.directionElevator;
	}

	public void setDirectionElevator(float directionElevator, String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		annInputWVO.directionElevator = directionElevator;
	}

	public float getTimeToWait(String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		return annInputWVO.timeToWait;
	}

	public void setTimeToWait(float timeToWait, String pass) {
		InputWVO annInputWVO = inputWMap.get(pass);
		annInputWVO.timeToWait = timeToWait;
	}

	public HashMap<String, InputWVO> getInputWMap() {
		return inputWMap;
	}

	public void setInputWMap(HashMap<String, InputWVO> inputWMap) {
		this.inputWMap = inputWMap;
	}
	
	public void writeToFile(){
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("AnnInputWVO.train");
			  BufferedWriter out = new BufferedWriter(fstream);
			  Set<String> keys = inputWMap.keySet();
			  for(String key : keys){
				  out.write( (inputWMap.get(key)).toString() );
				  out.write( "\n" );
			  }
			  out.close();
		}catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		}
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
