package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.WorkingAreaPaths;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class UploadTableFromUrlPage implements ApplicationHandler {

	@Override
	public String route() {
		return "upload-table-url";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception {
		ParameterValidator parameterValidator = new ParameterValidator("url", "separator");
		
		if(parameterValidator.isCorrectRequest(requestAndResponse)){
			String url = requestAndResponse.getParameter("url");
			String separator = requestAndResponse.getParameter("separator");
			String delimiter = requestAndResponse.getParameter("delimiter");
			boolean hasHeader = requestAndResponse.isCheckBoxEnabled("header");
			
			try{
				URL link = new URL(url);
				String tableName = link.openConnection().getHeaderField("Content-Disposition").split("=")[1];
				
				CookieManager cookieManager = new CookieManager(requestAndResponse);
				String user = cookieManager.setUserIfMissing();
				cookieManager.setTable(tableName);
				
				FileUtils.copyURLToFile(link, new File(new WorkingAreaPaths().tables(user).file(tableName).path()));
				
				new WorkingTable(tableName, user)
										  .withSeparator(separator)
										  .withDelimiter(delimiter)
										  .withHeader(hasHeader)
										  .save();
	
				requestAndResponse.sendRedirect("/annotation", "info", tableName + " successfully uploaded");
			}
			catch(Exception e){
				requestAndResponse.sendRedirect("/annotation", "error", "Error importing table");
			}
		}
		else{
			requestAndResponse.sendRedirect("/annotation", "error", "Error importing table");
		}
		return "Redirected";
	}
}
