package it.disco.unimib.stan.webapp;

public class HomePage implements Page {

	@Override
	public String route() {
		return "";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception {
		String table = new CookieManager(requestAndResponse).getTable();
		return new Template("home")
				.hasPreviousWork(!nullOrEmpty(table))
				.content(table)
				.page();
	}

	private boolean nullOrEmpty(String table) {
		return table == null || table.isEmpty();
	}
}
