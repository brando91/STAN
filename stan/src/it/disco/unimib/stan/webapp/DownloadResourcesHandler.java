package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.WorkingAreaPaths;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class DownloadResourcesHandler extends AbstractHandler {

	@Override
	public void handle(String path, Request base, HttpServletRequest request, HttpServletResponse response) throws IOException{
		CookieManager cookieManager = new CookieManager(new ServerCommunication(request, response));
		String user = cookieManager.getUser();

		if(path.matches("/downloads/mappings")){
			base.setQueryEncoding("UTF-8");
			request.getSession();
			base.setHandled(true);
			
			String mappings = cookieManager.getTable() + "_mappings.ttl";
			File mappingsFile = new File(new WorkingAreaPaths().mappings(user).toDownload().file(mappings).path());
			downloadFile(response, mappingsFile, user);
		}
		else if(path.matches("/downloads/triples")){
			base.setQueryEncoding("UTF-8");
			request.getSession();
			base.setHandled(true);
			
			String triples = cookieManager.getTable() + "_triples.ttl";
			File triplesFile = new File(new WorkingAreaPaths().mappings(user).toDownload().file(triples).path());
			downloadFile(response, triplesFile, user);
		}
		else{
			base.setHandled(false);
		}
	}

	private void downloadFile(HttpServletResponse response, File file, String user) throws IOException {
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		response.getWriter().write(FileUtils.readFileToString(file, "UTF-8"));
		FileUtils.deleteQuietly(new File(new WorkingAreaPaths().mappings(user).toDownload().path()));
	}
}
