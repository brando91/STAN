package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.CSVTable;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.WorkingAreaPaths;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class AnnotationPage implements Page {

	@Override
	public String route() {
		return "annotation";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception {
		String error = requestAndResponse.getParameter("error");
		CookieManager cookieManager = new CookieManager(requestAndResponse);
		WorkingTable workingTable = new WorkingTable(cookieManager.getTable(), cookieManager.getUser());
		
		if(!nullOrEmpty(error) || nullOrEmpty(workingTable.getTableName())){
			requestAndResponse.setResponseStatus(400);
			if(nullOrEmpty(error)) error = "Missing table";
			return new ErrorTemplate("annotation", "", error).page();
		}
		else{
			String tableName = workingTable.getTableName();
			
			String path = new WorkingAreaPaths().tables(cookieManager.getUser()).file(tableName).path();
			CSVTable table = new CSVTable(new FileResource(path))
											.withSeparator(workingTable.getSeparator())
											.withTextDelimiter(workingTable.getDelimiter());
			if(workingTable.hasHeader()) table.withHeader();

			return new Template("annotation").breadcrumb(urlEncode(tableName))
											 .header(table.getHeader().asAnnotatedList(workingTable))
											 .info(requestAndResponse.getParameter("info"))
											 .page();
		}
	}

	private boolean nullOrEmpty(String value) {
		return (value == null || value.isEmpty());
	}

	private String urlEncode(String toEncode) throws UnsupportedEncodingException {
		return URLEncoder.encode(toEncode, "UTF-8");
	}
}