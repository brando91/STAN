package it.disco.unimib.stan.core;

import java.util.concurrent.TimeUnit;

public class AverageTime {

	private long measures;
	private long sum;

	public AverageTime() {
		this.measures = 0;
		this.sum = 0;
	}
	
	public void add(long milliSeconds) {
		this.sum += milliSeconds;
		this.measures++;
	}
	
	private long totalAverageMillis() {
		return (this.measures > 0) ? this.sum / this.measures : 0;
	}

	public String average() {
		long averageMillis = totalAverageMillis();
		long minutes = TimeUnit.MILLISECONDS.toMinutes(averageMillis);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(averageMillis) - TimeUnit.MINUTES.toSeconds(minutes);
		long millis = averageMillis - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds);
		return String.format("%d min, %d sec, %d ms", minutes, seconds, millis);
	}
}
