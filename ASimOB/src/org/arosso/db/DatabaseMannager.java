package org.arosso.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.arosso.model.Passenger;
import org.hsqldb.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseMannager {
	
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Server server;
	private Connection conn;
	private String dbURL;
	
	private static DatabaseMannager instance = null; 
	
	private DatabaseMannager() {
		logger.info("Database init");
	}
	
	public static DatabaseMannager getInstance() throws Exception {
		if(instance==null){
			instance = new DatabaseMannager();
			instance.initialize();
		}
		return instance;
	}


	/**
	 * 
	 * @throws SQLException
	 */
	public void destroy() throws SQLException {
		this.dropTables();
		this.shutdown();
		server.shutdown();
		logger.info("Database is now down.");
	}

	public void shutdown() throws SQLException {
		Statement st = conn.createStatement();
		// db writes out to files and performs clean shuts down
		// otherwise there will be an unclean shutdown
		// when program ends
		st.execute("SHUTDOWN");
		conn.close(); // if there are no other open connection
	}

	/**
	 * 
	 * @throws Exception 
	 */
	public void initialize() throws Exception {
		server = new Server();
		server.setDatabaseName(0, "asimobdb"); // EL nombre con el que queremos
												// publicarla
		// String dbpath = sce.getServletContext().getInitParameter("dbpath");
		dbURL = (new java.io.File(".").getCanonicalPath());
		server.setDatabasePath(0, dbURL);
		logger.info("Database (" + server.getDatabaseName(0, true)
				+ "). Started in (" + server.getDatabaseName(0, true) + ")");
		// server.setLogWriter(logger.get); // can use custom writer
		// server.setErrWriter(null); // can use custom writer
		server.start();
		createConn();
	}

	/**
	 * we dont want this garbage collected until we are done
	 * 
	 * @throws Exception
	 */
	public void createConn() throws Exception { // note more general exception
		Class.forName("org.hsqldb.jdbcDriver");
		conn = DriverManager.getConnection("jdbc:hsqldb:file:" + dbURL, // filenames
				"sa", // username
				""); // password
	}

	/**
	 * 
	 * @param rs
	 * @throws SQLException
	 */
	public static void dump(ResultSet rs) throws SQLException {
		// the order of the rows in a cursor
		// are implementation dependent unless you use the SQL ORDER statement
		ResultSetMetaData meta = rs.getMetaData();
		int colmax = meta.getColumnCount();
		int i;
		Object o = null;

		// the result set is a cursor into the data. You can only
		// point to one row at a time
		// assume we are pointing to BEFORE the first row
		// rs.next() points to next row and returns true
		// or false if there is no next row, which breaks the loop
		for (; rs.next();) {
			for (i = 0; i < colmax; ++i) {
				o = rs.getObject(i + 1); // Is SQL the first column is indexed

				// with 1 not 0
				System.out.print(o.toString() + " ");
			}

			System.out.println(" ");
		}
	}

	/**
	 * use for SQL command SELECT
	 * 
	 * @param expression
	 * @throws SQLException
	 */
	public synchronized void query(String expression) throws SQLException {
		Statement st = null;
		ResultSet rs = null;
		st = conn.createStatement(); // statement objects can be reused with
		// repeated calls to execute but we
		// choose to make a new one each time
		rs = st.executeQuery(expression); // run the query
		// do something with the result set.
		dump(rs);
		st.close(); // NOTE!! if you close a statement the associated ResultSet
					// is
	}

	/**
	 * use for SQL commands CREATE, DROP, INSERT and UPDATE
	 * 
	 * @param expression
	 * @throws SQLException
	 */
	public synchronized void update(String expression) throws SQLException {
		Statement st = null;
		st = conn.createStatement(); // statements
		int i = st.executeUpdate(expression); // run the query
		if (i == -1) {
			System.out.println("db error : " + expression);
		}
		st.close();
	}
	
	public void createTables(){
		try {
			if(tableExists("PASSENGER")){
				logger.info("table already exists");
			}
			else{
				// arrivalTime; originFloor; destinationFloor; type;
				this.update("CREATE TABLE PASSENGER ( ID INTEGER IDENTITY, ARRIVALTIME DOUBLE, ENTRYTIME DOUBLE, EXITTIME DOUBLE, ORIGINFLOOR INTEGER, DESTINATIONFLOOR INTEGER, PASSTYPE VARCHAR(20), ELEVATOR INTEGER)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertPassenger(Passenger passenger, int elevator){
		try {
			this.update("INSERT INTO PASSENGER(ARRIVALTIME,ENTRYTIME,EXITTIME,ORIGINFLOOR,DESTINATIONFLOOR,PASSTYPE,ELEVATOR) VALUES("
					+passenger.getArrivalTime()+","
					+passenger.getEntryTime()+","
					+passenger.getExitTime()+","
					+passenger.getOriginFloor()+","
					+passenger.getDestinationFloor()+","
					+"'"+passenger.getType()+"'," 
					+elevator
					+")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void dropTables(){
		try {
			this.update("DROP TABLE PASSENGER");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean tableExists(String table) throws SQLException{
		  DatabaseMetaData metadata = this.conn.getMetaData();
		  ResultSet result = metadata.getTables(null,null,table,new String[] { "TABLE" });
		  boolean exists = result.next();
		  result.close();
		  return exists;
	}

	public static void main(String[] args) {
		
		try {
			DatabaseMannager db = DatabaseMannager.getInstance();
			db.createTables();
			Passenger passenger = new Passenger(2, 4, 10, Passenger.Type.CALL);
			db.insertPassenger(passenger,0);
			// do a query
			db.query("SELECT * FROM PASSENGER");
			db.dropTables();
			// databaseStarter.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}