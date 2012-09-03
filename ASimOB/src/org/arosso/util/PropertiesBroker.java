package org.arosso.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import static org.arosso.util.Constants.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesBroker {

	public enum PROP_SET{SIMULATION, TRAFFIC};
	
	/** Properties path*/
	private static String PROP_PATH = BUILDING_PROPS_PATH;
	
	/**
	 * Singleton instance
	 */
	private static PropertiesBroker properties;
	
	/**
	 * Map of properties
	 */
	private static HashMap<PROP_SET, Properties> propertiesMap;
	
	/**
     * Logger
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
	/**
	 * Defaulf constructor
	 * @throws Exception 
	 */
	private PropertiesBroker() throws Exception {
		super();
		propertiesMap = new HashMap<PropertiesBroker.PROP_SET, Properties>();
		// Simulation properties
		logger.debug("Propery for simulation ready to load["+PROP_PATH + PROP_SET.SIMULATION.toString().toLowerCase() + ".properties"+"]");
		Properties simProp = loadPropsFile(PROP_PATH + PROP_SET.SIMULATION.toString().toLowerCase() + ".properties");
		propertiesMap.put(PROP_SET.SIMULATION, simProp);
		// Building properties
		//Properties simProp = loadPropsFile(PROP_PATH + PROP_SET.SIMULATION.toString().toLowerCase() + ".properties");
		//propertiesMap.put(PROP_SET.SIMULATION, simProp);
		//logger.debug("Propery for simulation ready ["+PROP_PATH + PROP_SET.SIMULATION.toString().toLowerCase() + ".properties"+"]");
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	public static PropertiesBroker getInstance() throws Exception{
		if (properties == null) {
			properties = new PropertiesBroker();
		}
		return properties;
	}
	
	/**
	 * Return a desired property
	 * @param set
	 * @param key
	 * @return
	 */
	public String getProperty(PROP_SET set, String key){
		Properties props = propertiesMap.get(set);
		logger.debug("getting ("+key+") = "+props.getProperty(key));
		return props.getProperty(key);
	}
	
	/**
	 * Load properties file
	 * @param buildingPropsFile
	 * @return
	 * @throws IOException
	 */
	private Properties loadPropsFile(String buildingPropsFile) throws IOException {
		Properties simProp = new Properties();
    	InputStream is = this.getClass().getResourceAsStream(buildingPropsFile);
    	simProp.load(is);
        is.close();
        return simProp;
	}

}
