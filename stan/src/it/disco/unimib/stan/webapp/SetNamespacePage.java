package it.disco.unimib.stan.webapp;

public class SetNamespacePage implements Page {

	@Override
	public String route() {
		return "set-namespace";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception {
		String info = requestAndResponse.getParameter("info");
		String error = requestAndResponse.getParameter("error");
		Template template = new Template("namespace");

		if(isSet(info)) template.info(info);
		if(isSet(error)) template.error(error);
		
		CookieManager cookieManager = new CookieManager(requestAndResponse);
		WorkingTable currentTable = new WorkingTable(cookieManager.getTable(), cookieManager.getUser());
		
		return template
					.breadcrumb("Namespace")
					.currentTableName(currentTable.getTableName())
					.content(currentTable.getNamespace())
					.page();
	}

	private boolean isSet(String parameter) {
		return (parameter != null && !parameter.isEmpty());
	}
}
