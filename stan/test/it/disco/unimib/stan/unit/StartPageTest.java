package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.webapp.StartPage;

import org.junit.Test;

public class StartPageTest{

	@Test
	public void route() throws Exception {
		assertThat(new StartPage().route(), equalTo("start"));
	}
	
	@Test
	public void containsUploadButton() throws Exception {
		assertThat(new StartPage().process(new CommunicationTestDouble()), containsString("Upload"));
	}
}
