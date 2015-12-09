package it.disco.unimib.stan.unit;

import it.disco.unimib.stan.core.Environment;
import it.disco.unimib.stan.webapp.WebApplication;

import org.junit.After;
import org.junit.Before;

public class WebUnitTest {
	
	private WebApplication application;
	protected WebUser user;
	
	@Before
	public void startBrowser(){
		noLogging();
		user = webUser();
	}

	@After
	public void stopBrowser(){
		user.close();
	}
	
	@Before
	public void startServer() throws Exception{
		this.application = new WebApplication(applicationPort());
		application.start();
	}

	@After
	public void stopServer() throws Exception{
		this.application.stop();
	}
	
	private String applicationHome() {
		return "http://localhost:"+ applicationPort();
	}
	
	private WebUser webUser() {
		return new WebUser(applicationHome());
	}
	
	protected int applicationPort() {
		return new Environment().debugPort();
	}
	
	private void noLogging() {
		System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "fatal");
	}
}
