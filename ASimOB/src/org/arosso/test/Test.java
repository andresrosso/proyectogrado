/**
 * 
 */
package org.arosso.test;

import java.io.IOException;

import org.arosso.sim.BuildingSimulator;

/**
 * @author arosso
 *
 */
public class Test {

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("test ");
		BuildingSimulator bs = new BuildingSimulator();
		try {
			bs.init();
			bs.startSimulation();
			bs.stopSimulation();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
