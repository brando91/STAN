package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.experiments.DataPaths;

import org.junit.Test;

public class DataPathsTest {
	
	@Test
	public void dataDirectory() {
		String evaluationPath = new DataPaths().path();
		
		assertThat(evaluationPath, equalTo("../data"));
	}
	
	@Test
	public void ecommerceDirectory() {
		String goldStandardPath = new DataPaths()
								.ecommerce()
								.path();
		
		assertThat(goldStandardPath, equalTo("../data/ecommerce"));
	}
	
	@Test
	public void chicagoDirectory() {
		String goldStandardPath = new DataPaths()
								.chicago()
								.path();
		
		assertThat(goldStandardPath, equalTo("../data/chicago"));
	}
}
