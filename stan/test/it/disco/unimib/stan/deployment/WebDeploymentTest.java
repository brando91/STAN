package it.disco.unimib.stan.deployment;

import it.disco.unimib.stan.core.Environment;
import it.disco.unimib.stan.unit.WebUser;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;

public class WebDeploymentTest {

	protected WebUser user;
	
	@Before
	public void startBrowser() throws IOException{
		noLogging();
		user = webUser();
	}

	@After
	public void stopBrowser(){
		user.close();
	}
	
	private String applicationHome() throws IOException {
		return "http://localhost:"+ applicationPort();
	}
	
	private WebUser webUser() throws IOException {
		return new WebUser(applicationHome());
	}
	
	protected int applicationPort() throws IOException {
		return new Environment().getPort();
	}
	
	private void noLogging() {
		System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "fatal");
	}
}
