package it.disco.unimib.stan.core;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogEvents {
	
	static{
		PropertyConfigurator.configureAndWatch("log4j.properties");
	}
	
	public void error(Object message, Exception exception){
		Logger.getLogger("errorLogger").error(message, exception);
	}
	
	public void info(Object message){
		Logger.getLogger("reportsLogger").info(message);
	}
	
	public void debug(Object message){
		Logger.getLogger("debugLogger").debug(message);
	}

}
