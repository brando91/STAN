package it.disco.unimib.stan.integration;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.IndexesPath;

import java.io.File;

import org.junit.Test;

public class IndexesIntegrationTest {
	
	@Test
	public void ecommerceIndex() throws Exception {
		assertThat(new File(new IndexesPath().labelling("ecommerce").path()).listFiles().length, greaterThan(0));
	}
	
	@Test
	public void ecommerceDomains() throws Exception {
		assertThat(new File(new IndexesPath().ecommerceDomains().path()).listFiles().length, greaterThan(0));
	}
	
	@Test
	public void ecommerceRanges() throws Exception {
		assertThat(new File(new IndexesPath().ecommerceRanges().path()).listFiles().length, greaterThan(0));
	}
}
