package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.containsString;
import it.disco.unimib.stan.webapp.AlivePage;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class AlivePageTest {

	@Test
	public void route() throws Exception {
		assertThat(new AlivePage().route(), equalTo("alive"));
	}
	
	@Test
	public void aliveContent() throws Exception {
		FileUtils.write(new File("./version"), "today");
		assertThat(new AlivePage().process(new CommunicationTestDouble()), containsString("today"));
	}
}
