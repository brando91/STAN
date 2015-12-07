package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import it.disco.unimib.stan.webapp.Context;

import org.junit.Test;

public class ContextTest {

	@Test
	public void emptyContext() throws Exception {
		assertThat(new Context("").cleaned(), equalTo(""));
	}
	
	@Test
	public void dottedContext() throws Exception {
		assertThat(new Context("tabella.csv").cleaned(), equalTo("tabella"));
	}
	
	@Test
	public void multipleDots() throws Exception {
		assertThat(new Context("la.mia.tabella.csv").cleaned(), equalTo("la mia tabella"));
	}
	
	@Test
	public void underscoreContext() throws Exception {
		assertThat(new Context("la_tabella.csv").cleaned(), equalTo("la tabella"));
	}
	
	@Test
	public void minusContext() throws Exception {
		assertThat(new Context("la-tabella.csv").cleaned(), equalTo("la tabella"));
	}
	
	@Test
	public void simpleContext() throws Exception {
		assertThat(new Context("contesto").cleaned(), equalTo("contesto"));
	}
}
