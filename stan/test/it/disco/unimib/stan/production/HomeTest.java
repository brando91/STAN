package it.disco.unimib.stan.production;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HomeTest extends WebDeploymentTest{

	@Test
	public void startPageIsUp() throws Exception {
		user.browseTo("start");
		
		assertThat(user.pageContent(), containsString("Upload"));
	}
	
	@Test
	public void homePageIsUp() throws Exception {
		user.browseTo("");
		
		assertThat(user.pageContent(), containsString("Start a new annotation"));
	}
}