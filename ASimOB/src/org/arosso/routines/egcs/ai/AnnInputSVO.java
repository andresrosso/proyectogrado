package org.arosso.routines.egcs.ai;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Set;

import org.arosso.model.Elevator;
import org.arosso.model.Elevator.Direction;
import org.arosso.routines.egcs.ai.AnnInputSVO.InputSVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnInputSVO {
	
	private static AnnInputSVO instance = null;
	
	/**
	 * INPUT MAP
	 */
	private HashMap<String, InputSVO> inputWMap = new HashMap<String, InputSVO>();
	
    /**
     * Logger
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());
    

	public class InputSVO{
		float passInElevator;
		float inputTraffic;
		float outputTraffic;
		float interfloorTraffic;
		float directionElevator;
		float timeToService;
		
		public InputSVO() {
			super();
		}
		
		public InputSVO(float passInElevator, float inputTraffic, float outputTraffic, float interfloorTraffic, Elevator.Direction directionElevatorT) {
			super();
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
			return 	passInElevator+";"+
					inputTraffic+";"+
					outputTraffic+";"+
					interfloorTraffic+";"+
					directionElevator+";"+
					timeToService;
		}
	}
	
	private AnnInputSVO() {
		super();
	}
	
	public static AnnInputSVO getInstance(){
		if(instance==null){
			instance = new AnnInputSVO();
		}
		return instance;
	}
	
	public void addInputSVO(float passInElevator, float inputTraffic, float outputTraffic, float interfloorTraffic, Elevator.Direction directionElevator, String pass){
		InputSVO annInputSVO = new InputSVO(passInElevator,inputTraffic,outputTraffic,inputTraffic,directionElevator);
		this.inputWMap.put(pass, annInputSVO);
	}
	
	public float getPassInElevator(String pass) {
		InputSVO annInputSVO = inputWMap.get(pass);
		return annInputSVO.passInElevator;
	}

	public void setPassInElevator(float passInElevator, String pass) {
		InputSVO annInputSVO = inputWMap.get(pass);
		annInputSVO.passInElevator = passInElevator;
	}

	public float getInputTraffic(String pass) {
		InputSVO annInputSVO = inputWMap.get(pass);
		return annInputSVO.inputTraffic;
	}

	public void setInputTraffic(float inputTraffic, String pass) {
		InputSVO annInputSVO = inputWMap.get(pass);
		annInputSVO.inputTraffic = inputTraffic;
	}

	public float getOutputTraffic(String pass) {
		InputSVO annInputSVO = inputWMap.get(pass);
		return annInputSVO.outputTraffic;
	}

	public void setOutputTraffic(float outputTraffic, String pass) {
		InputSVO annInputSVO = inputWMap.get(pass);
		annInputSVO.outputTraffic = outputTraffic;
	}

	public float getInterfloorTraffic(String pass) {
		InputSVO annInputSVO = inputWMap.get(pass);
		return annInputSVO.interfloorTraffic;
	}

	public void setInterfloorTraffic(float interfloorTraffic, String pass) {
		InputSVO annInputSVO = inputWMap.get(pass);
		annInputSVO.interfloorTraffic = interfloorTraffic;
	}

	public float getDirectionElevator(String pass) {
		InputSVO annInputSVO = inputWMap.get(pass);
		return annInputSVO.directionElevator;
	}

	public void setDirectionElevator(float directionElevator, String pass) {
		InputSVO annInputSVO = inputWMap.get(pass);
		annInputSVO.directionElevator = directionElevator;
	}

	public float getTimeToService(String pass) {
		InputSVO annInputSVO = inputWMap.get(pass);
		return annInputSVO.timeToService;
	}

	public void setTimeToService(float timeToService, String pass) {
		InputSVO annInputSVO = inputWMap.get(pass);
		annInputSVO.timeToService = timeToService;
	}

	public HashMap<String, InputSVO> getInputWMap() {
		return inputWMap;
	}

	public void setInputWMap(HashMap<String, InputSVO> inputWMap) {
		this.inputWMap = inputWMap;
	}
	
	public void writeToFile(){
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("AnnInputSVO.train");
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
