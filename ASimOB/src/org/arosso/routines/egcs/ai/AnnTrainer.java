package org.arosso.routines.egcs.ai;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.joone.engine.BiasedLinearLayer;
import org.joone.engine.FullSynapse;
import org.joone.engine.LinearLayer;
import org.joone.engine.Monitor;
import org.joone.engine.NeuralNetEvent;
import org.joone.engine.NeuralNetListener;
import org.joone.engine.SigmoidLayer;
import org.joone.engine.learning.TeachingSynapse;
import org.joone.io.FileInputSynapse;
import org.joone.io.FileOutputSynapse;
import org.joone.net.NeuralNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnTrainer implements NeuralNetListener {
	
    /**
     * Logger
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());

	private static String inputData = "AnnInputWVO.train";
	private static String outputFile = "AnnInputWVO.out";
	private static String outputFileANN = "AnnInputWVO.ann";

	NeuralNet nnet = new NeuralNet();

	/** Creates new AnnTrainer */
	public AnnTrainer() {
	}

	/**
	 * @param args
	 *            the command line arguments
	 */

	public static void main(String args[]) {
		AnnTrainer AnnTrainer = new AnnTrainer();
		AnnTrainer.Go(inputData, outputFile);
	}

	public void Go(String inputFile, String outputFile) {
		/*
		 * Firts, creates the three Layers
		 */
		LinearLayer input = new LinearLayer();
		SigmoidLayer hidden = new SigmoidLayer();
		BiasedLinearLayer output = new BiasedLinearLayer();
		input.setLayerName("input");
		hidden.setLayerName("hidden");
		output.setLayerName("output");
		/* sets their dimensions */
		input.setRows(6);
		hidden.setRows(30);
		output.setRows(1);

		/*
		 * Now create the two Synapses
		 */
		FullSynapse synapse_IH = new FullSynapse(); /* input -> hidden conn. */
		FullSynapse synapse_HO = new FullSynapse(); /* hidden -> output conn. */

		synapse_IH.setName("IH");
		synapse_HO.setName("HO");
		/*
		 * Connect the input layer whit the hidden layer
		 */
		input.addOutputSynapse(synapse_IH);
		hidden.addInputSynapse(synapse_IH);
		/*
		 * Connect the hidden layer whit the output layer
		 */
		hidden.addOutputSynapse(synapse_HO);
		output.addInputSynapse(synapse_HO);

		FileInputSynapse inputStream = new FileInputSynapse();
		/*
		 * The first two columns contain the input values
		 * Vel,Dictancia,Resistencia
		 */
		inputStream.setAdvancedColumnSelector("1,2,3,4,5,6");

		/* This is the file that contains the input data */
		inputStream.setInputFile(new File(inputFile));
		input.addInputSynapse(inputStream);

		TeachingSynapse trainer = new TeachingSynapse();

		/*
		 * Setting of the file containing the desired responses, provided by a
		 * FileInputSynapse
		 */
		FileInputSynapse samples = new FileInputSynapse();
		samples.setInputFile(new File(inputFile));
		/* The output values are on the third column of the file */
		samples.setAdvancedColumnSelector("7");

		trainer.setDesired(samples);

		/* Creates the error output file */
		FileOutputSynapse error = new FileOutputSynapse();
		error.setFileName(outputFile); 
		// error.setBuffered(false);
		trainer.addResultSynapse(error);

		/* Connects the Teacher to the last layer of the net */
		output.addOutputSynapse(trainer);
		nnet.addLayer(input, NeuralNet.INPUT_LAYER);
		nnet.addLayer(hidden, NeuralNet.HIDDEN_LAYER);
		nnet.addLayer(output, NeuralNet.OUTPUT_LAYER);
		nnet.setTeacher(trainer);
		// Gets the Monitor object and set the learning parameters
		Monitor monitor = nnet.getMonitor();
		monitor.setLearningRate(0.5);
		monitor.setMomentum(0.3);

		/*
		 * The application registers itself as monitor's listener so it can
		 * receive the notifications of termination from the net.
		 */
		monitor.addNeuralNetListener(this);

		monitor.setTrainingPatterns(749); /*
										 * # of rows (patterns) contained in the
										 * input file
										 */
		monitor.setTotCicles(8000); /*
									 * How many times the net must be trained on
									 * the input patterns
									 */
		monitor.setLearning(true); /* The net must be trained */
		nnet.go(); /* The net starts the training job */
	}

	public void netStopped(NeuralNetEvent e) {
		System.out.println("Training finished");
		saveNeuralNet(outputFileANN, nnet);
		System.out.println("Ann saved");
	}

	public void cicleTerminated(NeuralNetEvent e) {
	}

	public void netStarted(NeuralNetEvent e) {
		System.out.println("Training...");
	}

	public void errorChanged(NeuralNetEvent e) {
		Monitor mon = (Monitor) e.getSource();
		/* We want print the results every 200 cycles */
		if (mon.getCurrentCicle() % 10 == 0)
			System.out.println(mon.getCurrentCicle() + " epochs remaining - RMSE = " + mon.getGlobalError());
	}

	public void netStoppedError(NeuralNetEvent e, String error) {
	}

	public void saveNeuralNet(String fileName, NeuralNet nnet) {
		try {
			FileOutputStream stream = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(stream);
			out.writeObject(nnet);
			out.close();
		} catch (Exception excp) {
			excp.printStackTrace();
		}
	}

	public NeuralNet restoreNeuralNet(String file) {
		try {
			FileInputStream stream = new FileInputStream(file);
			ObjectInputStream inp = new ObjectInputStream(stream);
			return (NeuralNet) inp.readObject();
		} catch (Exception excp) {
			excp.printStackTrace();
			return null;
		}
	}

}
