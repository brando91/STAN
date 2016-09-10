package it.disco.unimib.stan.webapp;


public interface Page {

	String route();
	
	String process(Communication requestAndResponse) throws Exception;
}
