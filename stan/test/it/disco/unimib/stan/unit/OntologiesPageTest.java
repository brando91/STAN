package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.webapp.OntologiesPage;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class OntologiesPageTest extends WebUnitTest{
	
	@Before
	public void setUp() throws IOException{
		new AnnotationArea().clear();
	}
	
	@Test
	public void route() throws Exception {
		assertThat(new OntologiesPage().route(), equalTo("ontologies"));
	}
}
