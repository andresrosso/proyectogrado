package org.arosso.routines.egcs.ai;

import java.io.FileInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import org.arosso.routines.egcs.ai.AnnInputWVO.InputWVO;
import org.joone.engine.DirectSynapse;
import org.joone.engine.Layer;
import org.joone.engine.Pattern;
import org.joone.net.NeuralNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnW  {

	
    /**
     * Logger
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    //iNPUT NETWORK
	private static String storedANN = "AnnInputWVO.ann";
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AnnW annBrake = new AnnW();
		InputWVO inputSVO = AnnInputWVO.getInstance().new InputWVO(0.275f, 0.0f, 0.59f, 0.11f, 0.11f, 1.0f);
		annBrake.Go(inputSVO);
	}

	public double Go(AnnInputWVO.InputWVO inputData) {
		// We load the serialized XOR neural net
		NeuralNet xor = restoreNeuralNet(storedANN);
		if (xor != null) {
			/*
			 * We get the first layer of the net (the input layer), then remove
			 * all the input synapses attached to it.
			 */
			Layer input = xor.getInputLayer();
			input.removeAllInputs();

			/*
			 * We get the last layer of the net (the output layer), then remove
			 * all the output synapses attached to it and attach a DirectSynapse
			 */
			Layer output = xor.getOutputLayer();
			output.removeAllOutputs();

			DirectSynapse memOut = new DirectSynapse();
			output.addOutputSynapse(memOut);

			// Now we interrogate the net
			/*
			 * Note: because we use nnet.singleStepForward() to interrogate the
			 * network, we don't need any input synapse, nor to invoke nnet.go()
			 */

			xor.getMonitor().setLearning(false);
			
			double[] inputArray = { inputData.posElevatorAtAssign, inputData.passInElevator, inputData.inputTraffic, inputData.outputTraffic, inputData.interfloorTraffic, inputData.directionElevator};
			// Prepare the next input pattern
			Pattern iPattern = new Pattern(inputArray);
			// Interrogate the net
			xor.singleStepForward(iPattern);
			// Read the output pattern and print out it
			Pattern pattern = memOut.fwdGet();
			System.out.println( pattern.getArray()[0]);
			/*for(int j=0 ; j<pattern.getArray().length; j++){
				System.out.println("ANN OUTPUT = "+pattern.getArray()[j]);
				return pattern.getArray()[j];
			}*/
			
		}
		return -1;
	}
	
	public static int max(double[] t) {
	    double maximum = t[0];   // start with the first value
	    int maxIndex = 0;
	    for (int i=1; i<t.length; i++) {
	        if (t[i] > maximum) {
	            maximum = t[i];   // new maximum
	            maxIndex = i;
	        }
	    }
	    return maxIndex;
	}//end method max


	//
	// TO DO
	// add some comments
	private NeuralNet restoreNeuralNet(String fileName) {
		NeuralNet nnet = null;
		try {
			FileInputStream stream = new FileInputStream(fileName);
			ObjectInput input = new ObjectInputStream(stream);
			nnet = (NeuralNet) input.readObject();
		} catch (Exception e) {
			logger.warn(
					"Exception thrown while restoring the Neural Net. Message is : "
							+ e.getMessage(), e);
		}
		return nnet;
	}
}