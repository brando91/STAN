package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.experiments.GoldStandardEntry;

import org.junit.Test;

public class GoldStandardEntryTest {
	
	@Test
	public void display() throws Exception {
		assertThat(new GoldStandardEntry("id", "value", 1).display(), equalTo("id Q0 value 1"));
	}
	
	@Test
	public void noWhiteSpaces() throws Exception {
		assertThat(new GoldStandardEntry("an id", "a value", 1).display(), equalTo("an_id Q0 a_value 1"));
	}
	
	@Test
	public void lowerCase() throws Exception {
		assertThat(new GoldStandardEntry("ID", "Value", 1).display(), equalTo("id Q0 value 1"));
	}

}
