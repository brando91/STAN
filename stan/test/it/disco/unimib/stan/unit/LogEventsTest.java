package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.LogEvents;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class LogEventsTest {

	@Test
	public void logStanInfo() throws Exception {
		LogEvents.stan().info("hello");
		assertThat(contains("logs/stan/info.log", "hello"), equalTo(true));
	}
	
	@Test
	public void logStanDebug() throws Exception {
		LogEvents.stan().debug("hello");
		assertThat(contains("logs/stan/debug.log", "hello"), equalTo(true));
	}
	
	@Test
	public void logStanError() throws Exception {
		LogEvents.stan().error("hello", new Exception());
		assertThat(contains("logs/stan/error.log", "hello"), equalTo(true));
	}
	
	@Test
	public void logLabellingInfo() throws Exception {
		LogEvents.labelling().info("hello");
		assertThat(contains("logs/labelling/info.log", "hello"), equalTo(true));
	}
	
	@Test
	public void logLabellingDebug() throws Exception {
		LogEvents.labelling().debug("hello");
		assertThat(contains("logs/labelling/debug.log", "hello"), equalTo(true));
	}
	
	@Test
	public void logLabellingError() throws Exception {
		LogEvents.labelling().error("hello", new Exception());
		assertThat(contains("logs/labelling/error.log", "hello"), equalTo(true));
	}
	
	private boolean contains(String file, String log) throws IOException {
		for(String line : FileUtils.readLines(new File(file))){
			if(line.contains(log)) return true;
		}
		return false;
	}
}
