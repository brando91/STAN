package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.WorkingAreaPaths;

import java.io.File;

public class ExportTriplesPage implements ApplicationHandler {

	@Override
	public String route() {
		return "export/triples";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception {
		ParameterValidator parameterValidator = new ParameterValidator("annotations", "subject");
		if(parameterValidator.isCorrectRequest(requestAndResponse)){
			CookieManager cookieManager = new CookieManager(requestAndResponse);
			JsonAnnotationStatus jsonStatus = new JsonAnnotationStatus(requestAndResponse.getParameter("annotations"));
			String currentTableName = cookieManager.getTable();
			String currentUser = cookieManager.getUser();
			String tablePath = new WorkingAreaPaths().tables(currentUser).file(currentTableName).path();
			
			new JsonAnnotations().save(currentTableName, currentUser, jsonStatus, requestAndResponse.getParameter("subject"));
			new JsonMappings().save(currentTableName, currentUser, jsonStatus, new File(tablePath).getAbsolutePath());
			
			String mappings = new WorkingAreaPaths().mappings(currentUser).file(currentTableName + "_mappings.ttl").path();
			String triples = new WorkingAreaPaths().mappings(currentUser).toDownload().file(currentTableName + "_triples.ttl").path();
			new RMLProcessor().generateTriples(mappings, triples);

			return "Saved";
		}
		
		requestAndResponse.setResponseStatus(400);
		if(parameterValidator.missingParameter().equals("subject")) return "You didn't set the subject column";
		return "No annotations passed";
	}

}
