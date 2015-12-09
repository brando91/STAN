package it.disco.unimib.stan.core;


public class Environment {

	public int localPort() {
		return 8081;
	}
	
	public int debugPort() {
		return 8090;
	}

	public int productionPort() {
		return 80;
	}
}
