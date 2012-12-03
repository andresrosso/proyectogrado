package org.arosso.db;

import java.io.IOException;

public class TestDatabaseStarter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DatabaseStarter starter = new DatabaseStarter();
		try {
			starter.initialize();
			starter.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
