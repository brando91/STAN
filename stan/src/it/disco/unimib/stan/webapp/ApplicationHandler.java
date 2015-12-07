package it.disco.unimib.stan.webapp;


public interface ApplicationHandler {

	String route();
	
	String process(Communication requestAndResponse) throws Exception;
}
