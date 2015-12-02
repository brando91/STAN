package it.disco.unimib.stan.deployment;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class AliveTest extends WebDeploymentTest{

	@Test
	public void alivePage() throws Exception {
		user.browseTo("alive");
		
		assertThat(user.pageContent(), containsString("Alive"));
	}
	
	@Test
	public void alivePageReturn200() throws Exception {
		user.browseTo("alive");
		
		assertThat(user.statusCode(), is(200));
	}
}
