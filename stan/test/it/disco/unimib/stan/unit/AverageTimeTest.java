package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import it.disco.unimib.stan.core.AverageTime;

import org.junit.Test;

public class AverageTimeTest {

	@Test
	public void fiveMinutes() throws Exception {
		AverageTime time = new AverageTime();
		time.add(minutes(6));
		time.add(minutes(5));
		time.add(minutes(4));
		
		assertThat(time.average(), equalTo("5 min, 0 sec, 0 ms"));
	}
	
	@Test
	public void fiveMinutesAndThirtySeconds() throws Exception {
		AverageTime time = new AverageTime();
		time.add(minutes(6));
		time.add(minutes(5));
		
		assertThat(time.average(), equalTo("5 min, 30 sec, 0 ms"));
	}
	
	@Test
	public void fifteenSeconds() throws Exception {
		AverageTime time = new AverageTime();
		time.add(seconds(10));
		time.add(seconds(20));
		
		assertThat(time.average(), equalTo("0 min, 15 sec, 0 ms"));
	}
	
	@Test
	public void milliseconds() throws Exception {
		AverageTime time = new AverageTime();
		time.add(seconds(10));
		time.add(seconds(21));
		
		assertThat(time.average(), equalTo("0 min, 15 sec, 500 ms"));
	}
	
	@Test
	public void twoSeconds() throws Exception {
		AverageTime time = new AverageTime();
		time.add(seconds(1));
		time.add(seconds(3));
		
		assertThat(time.average(), equalTo("0 min, 2 sec, 0 ms"));
	}

	private long minutes(int minutes) {
		return seconds(minutes * 60);
	}
	
	private long seconds(int seconds) {
		return seconds * 1000;
	}
}
