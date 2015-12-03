package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.KarmaNumericAnnotator;
import it.disco.unimib.stan.core.KarmaTextualAnnotator;
import it.disco.unimib.stan.core.LogEvents;
import it.disco.unimib.stan.core.TempPath;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.MultipartConfigElement;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

@MultipartConfig
public class Handlers extends AbstractHandler {
	
	private ApplicationHandler[] pages;
	
	public Handlers() throws Exception {
		this.pages = new ApplicationHandler[]{
				new HomePage(),
				new StartPage(),
				new AlivePage(),
				new UploadTablePage(),
				new UploadTableFromUrlPage(),
				new AnnotationPage(),
				new RowsPage(),
				new SaveStatusPage(),
				new ExportMappingsPage(),
				new ExportTriplesPage(),
				new SetNamespacePage(),
				new SaveSettedNamespacePage(),
				new OntologiesPage(),
				new KarmaAnnotationPage(new KarmaTextualAnnotator(), new KarmaNumericAnnotator()),
				new AnnotationApiPage(),
				new SuggestionsPage()
		};
	}

	@Override
	public void handle(String path, Request base, HttpServletRequest request, HttpServletResponse response) throws IOException {
		base.setQueryEncoding("UTF-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		request.getSession();
		base.setHandled(true);
		base.setAttribute("org.eclipse.multipartConfig", new MultipartConfigElement(new TempPath().path()));
		String pageFor = pageFor(path, request, response);
		PrintWriter writer = response.getWriter();
		writer.write(pageFor);
		
		LogEvents.stan().info(base.getRemoteAddr() + "|" + 
							 base.getHeader("User-Agent") + "|" + 
							 base.getMethod() + "|" + 
							 url(path, base) +  "|" + 
							 response.getStatus());
	}
	
	private String pageFor(String path, HttpServletRequest request, HttpServletResponse response) {
		try{
			String page = errorPage(404, "Page not found.");
			response.setStatus(404);
			for(ApplicationHandler handler : this.pages){
				if(path.matches("/" + handler.route())){
					response.setStatus(200);
					page = handler.process(new ServerCommunication(request, response));
					FileUtils.deleteQuietly(new File(new TempPath().path()));
				}
			}
			return page;
		}
		catch(Exception ex){
			response.setStatus(500);
			LogEvents.stan().error(ex.getMessage(), ex);
			return errorPage(500, "An error occurred while serving the request. <br> " + ex);
		}
	}

	private String errorPage(int code, String message) {
		return new Template("error")
						.status(code)
						.content(message)
						.breadcrumb("")
						.page();
	}
	
	private String url(String path, Request base){
		String queryString = (base.getQueryString() != null) ? base.getQueryString() : "";
		return path + "?" + queryString;
	}
}
