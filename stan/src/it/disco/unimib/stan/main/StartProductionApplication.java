package it.disco.unimib.stan.main;

import it.disco.unimib.stan.core.Environment;
import it.disco.unimib.stan.webapp.WebApplication;

public class StartProductionApplication {

	public static void main(String[] args) throws Exception {

		new WebApplication(new Environment().productionPort()).start();
		
	}
}
