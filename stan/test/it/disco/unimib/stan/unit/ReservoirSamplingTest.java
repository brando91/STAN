package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.experiments.ReservoirSampling;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class ReservoirSamplingTest {
	
	@Test
	public void emptySampling() throws Exception {
		HashMap<Integer, ArrayList<String>> results = new ReservoirSampling<String>().samples(0, new ArrayList<String>());
		
		assertThat(results.size(), equalTo(0));
	}
	
	@Test
	public void samplingWithOneElement() throws Exception {
		ArrayList<String> items = new ArrayList<String>();
		items.add("first");
		HashMap<Integer, ArrayList<String>> results = new ReservoirSampling<String>().samples(1, items);
		
		assertThat(results.size(), equalTo(1));
	}
	
	@Test
	public void samplingElements() throws Exception {
		ArrayList<String> items = new ArrayList<String>();
		items.add("first");
		items.add("second");
		HashMap<Integer, ArrayList<String>> results = new ReservoirSampling<String>().samples(0.5, items);
		
		assertThat(results.size(), equalTo(2));
		assertThat(results.get(0), not(equalTo(results.get(1))));
	}
	
	@Test
	public void notPerfectlyDividedGroups() throws Exception {
		ArrayList<String> items = new ArrayList<String>();
		items.add("1");
		items.add("2");
		items.add("3");
		items.add("4");
		items.add("5");
		items.add("6");
		items.add("7");
		items.add("8");
		items.add("9");
		items.add("10");
		
		HashMap<Integer, ArrayList<String>> results = new ReservoirSampling<String>().samples(0.3, items);
		
		assertThat(results.size(), equalTo(4));
		assertThat(results.get(1).size(), equalTo(3));
		assertThat(results.get(2).size(), equalTo(3));
		assertThat(results.get(3).size(), equalTo(3));
		assertThat(results.get(4).size(), equalTo(1));
	}
	
	@Test
	public void limitSamples() throws Exception {
		ArrayList<String> items = new ArrayList<String>();
		items.add("1");
		items.add("2");
		items.add("3");
		items.add("4");
		items.add("5");
		items.add("6");
		items.add("7");
		items.add("8");
		items.add("9");
		items.add("10");
		
		ArrayList<String> results = new ReservoirSampling<String>().getAtMostSamples(8, items);
		
		assertThat(results.size(), equalTo(8));
	}
	
	@Test
	public void tooBiglimit() throws Exception {
		ArrayList<String> items = new ArrayList<String>();
		items.add("1");
		items.add("2");
		items.add("3");
		items.add("4");
		
		ArrayList<String> results = new ReservoirSampling<String>().getAtMostSamples(5, items);
		
		assertThat(results.size(), equalTo(4));
	}
	
	@Test
	public void limitExactlyBigAsListSize() throws Exception {
		ArrayList<Double> items = new ArrayList<Double>();
		items.add(1.0);
		items.add(2.0);
		items.add(3.0);
		items.add(4.0);
		
		ArrayList<Double> results = new ReservoirSampling<Double>().getAtMostSamples(4, items);
		
		assertThat(results.size(), equalTo(4));
	}
	
	@Test
	public void limitDistinct() throws Exception {
		ArrayList<String> items = new ArrayList<String>();
		items.add("cane");
		items.add("cane");
		items.add("gatto");
		items.add("gallo");
		
		ArrayList<String> results = new ReservoirSampling<String>().getAtMostDistinctSamples(4, items);
		
		assertThat(results.size(), equalTo(3));
		assertThat(results.contains("cane"), equalTo(true));
		assertThat(results.contains("gatto"), equalTo(true));
		assertThat(results.contains("gallo"), equalTo(true));
	}
	
	@Test
	public void distinct() throws Exception {
		ArrayList<String> items = new ArrayList<String>();
		items.add("cane");
		items.add("cane");
		items.add("gatto");
		items.add("cane");
		
		ArrayList<String> results = new ReservoirSampling<String>().getAtMostDistinctSamples(2, items);
		
		assertThat(results.size(), equalTo(2));
		assertThat(results.contains("cane"), equalTo(true));
		assertThat(results.contains("gatto"), equalTo(true));
	}

}
