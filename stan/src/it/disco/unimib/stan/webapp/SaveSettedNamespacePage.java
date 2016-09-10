package it.disco.unimib.stan.webapp;


public class SaveSettedNamespacePage implements Page {

	@Override
	public String route() {
		return "set-namespace/save";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception {
		if(new ParameterValidator("namespace").isCorrectRequest(requestAndResponse)){
			CookieManager cookieManager = new CookieManager(requestAndResponse);
			new WorkingTable(cookieManager.getTable(), cookieManager.getUser()).saveNamespace(requestAndResponse.getParameter("namespace"));
			requestAndResponse.sendRedirect("/set-namespace", "info", "Namespace successfully saved");
		}
		
		requestAndResponse.setResponseStatus(400);
		requestAndResponse.sendRedirect("/set-namespace", "error", "Error saving the namespace");
		return "Redirected";
	}
}
