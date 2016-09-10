package it.disco.unimib.stan.webapp;

public class ContactsPage implements Page {

	@Override
	public String route() {
		return "contacts";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception {
		return "";
	}

}
