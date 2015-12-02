package it.disco.unimib.stan.webapp;

import java.io.File;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;

public class WebApplication {
	
	private Server server;

	public WebApplication(int port) throws Exception {
		this.server = new Server(port);
		HandlerCollection handlers = new HandlerCollection();
		handlers.setHandlers(new Handler[]{
									trackSessions(),
									application()
									});
		this.server.setHandler(handlers);
	}

	private HandlerList application() throws Exception {
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[]{
				staticResources(),
				new DownloadResourcesHandler(),
				new Handlers()
		});
		return handlers;
	}

	private SessionHandler trackSessions() {
		HashSessionManager sessions = new HashSessionManager();
		sessions.setMaxInactiveInterval(60 * 60);
		return new SessionHandler(sessions);
	}
	
	private Handler staticResources() throws Exception {
		ResourceHandler resources = new ResourceHandler();
		resources.setDirectoriesListed(false);
		resources.setResourceBase(new File("./assets/").getCanonicalPath());
		ContextHandler contextHandler = new ContextHandler("/assets");
		contextHandler.setHandler(resources);
		return contextHandler;
	}

	public void start() throws Exception {
		this.server.start();
	}

	public void stop() throws Exception {
		this.server.stop();
	}
	
}
