package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.experiments.GoldStandard;
import it.disco.unimib.stan.experiments.GoldStandardEntry;

import java.util.ArrayList;

import org.junit.Test;

public class GoldStandardTest {

	@Test
	public void goldStandardWithOneEntry(){
		ArrayList<GoldStandardEntry> entries = new GoldStandard("")
										.addEntry("id", "value", 2)
										.entries();
		
		assertThat(entries.get(0).display(), equalTo("id Q0 value 2"));
	}
	
	@Test
	public void goldStandardWithTwoEntry(){
		ArrayList<GoldStandardEntry> entries = new GoldStandard("")
										.addEntry("id1", "candidate1", 2)
										.addEntry("id2", "candidate2", 1)
										.entries();
		
		assertThat(entries.get(0).display(), equalTo("id1 Q0 candidate1 2"));
		assertThat(entries.get(1).display(), equalTo("id2 Q0 candidate2 1"));
	}
}
