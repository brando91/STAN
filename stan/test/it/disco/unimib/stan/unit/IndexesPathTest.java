package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import it.disco.unimib.stan.core.IndexesPath;

import org.junit.Test;

public class IndexesPathTest {

	@Test
	public void root() throws Exception {
		assertThat(new IndexesPath().path(), equalTo("./indexes"));
	}
	
	@Test
	public void karmaIndex() throws Exception {
		assertThat(new IndexesPath().karma("dataset").path(), equalTo("./indexes/karma-dataset"));
	}
}
