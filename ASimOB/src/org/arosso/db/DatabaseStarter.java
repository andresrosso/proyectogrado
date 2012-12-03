package org.arosso.db;

import java.io.IOException;

import org.hsqldb.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseStarter {

	private Server server;
	
    
    /**
     * Logger
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());

	public void destroy() {
		server.shutdown();
		logger.info("Database is now down.");
	}

	public void initialize() throws IOException {
		server = new Server();
		server.setDatabaseName(0, "asimobdb"); // EL nombre con el que queremos
												// publicarla
		//String dbpath = sce.getServletContext().getInitParameter("dbpath");
		String dbpath = (new java.io.File(".").getCanonicalPath());
		server.setDatabasePath(0, dbpath);
		logger.info("Database ("+server.getDatabaseName(0, true)
				+"). Started in ("+server.getDatabaseName(0, true)+")");
		server.start();
	}
}