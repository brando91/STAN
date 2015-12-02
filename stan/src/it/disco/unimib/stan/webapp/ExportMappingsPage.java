package it.disco.unimib.stan.webapp;


public class ExportMappingsPage implements ApplicationHandler {

	@Override
	public String route() {
		return "export/mappings";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception {
		ParameterValidator parameterValidator = new ParameterValidator("annotations", "subject");
		if(parameterValidator.isCorrectRequest(requestAndResponse)){
			CookieManager cookieManager = new CookieManager(requestAndResponse);
			String currentTable = cookieManager.getTable();
			JsonAnnotationStatus jsonStatus = new JsonAnnotationStatus(requestAndResponse.getParameter("annotations"));
			String user = cookieManager.getUser();
			
			new JsonAnnotations().save(currentTable, user, jsonStatus, requestAndResponse.getParameter("subject"));
			new JsonMappings().save(currentTable, user, jsonStatus, currentTable);

			return "Saved";
		}
		
		requestAndResponse.setResponseStatus(400);
		if(parameterValidator.missingParameter().equals("subject")) return "You didn't set the subject column";
		return "No annotations passed";
	}
}
