package it.disco.unimib.stan.core;


public class ElapsedTime {

	private long start;
	private long stop;

	public void start() {
		this.start = System.currentTimeMillis();
	}

	public void stop() {
		this.stop = System.currentTimeMillis();
	}

	public long delta(){
		return this.stop - this.start;
	}
}
