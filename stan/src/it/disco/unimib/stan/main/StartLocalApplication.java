package it.disco.unimib.stan.main;

import it.disco.unimib.stan.core.Environment;
import it.disco.unimib.stan.webapp.WebApplication;

public class StartLocalApplication {

	public static void main(String[] args) throws Exception {

		new WebApplication(new Environment().localPort()).start();
		
	}
}
