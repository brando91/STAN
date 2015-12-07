package it.disco.unimib.stan.core;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogEvents {
	
	private String type;
	
	static{
		PropertyConfigurator.configureAndWatch("log4j.properties");
	}
	
	public static LogEvents stan(){
		return new LogEvents("stan");
	}
	
	public static LogEvents labelling(){
		return new LogEvents("labelling");
	}

	private LogEvents(String type) {
		this.type = type;
	}
	
	public void error(Object message, Exception exception){
		logger("error").error(message, exception);
	}
	
	public void info(Object message){
		logger("info").info(message);
	}
	
	public void debug(Object message){
		logger("debug").debug(message);
	}

	private Logger logger(String name) {
		return Logger.getLogger(this.type + "-" + name);
	}

}
