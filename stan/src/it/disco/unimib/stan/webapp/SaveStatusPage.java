package it.disco.unimib.stan.webapp;


public class SaveStatusPage implements Page {

	@Override
	public String route() {
		return "save-status";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception  {
		if(new ParameterValidator("annotations").isCorrectRequest(requestAndResponse)){
			JsonAnnotationStatus jsonStatus = new JsonAnnotationStatus(requestAndResponse.getParameter("annotations"));
			CookieManager cookieManager = new CookieManager(requestAndResponse);
			new JsonAnnotations().save(cookieManager.getTable(), cookieManager.getUser(), jsonStatus, requestAndResponse.getParameter("subject"));

			return "Saved";
		}
		
		requestAndResponse.setResponseStatus(400);
		return "No annotations passed";
	}

}
