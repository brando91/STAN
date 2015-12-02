package it.disco.unimib.stan.webapp;

public class StartPage implements ApplicationHandler {

	@Override
	public String route() {
		return "start";
	}

	@Override
	public String process(Communication requestAndResponse) {
		return new Template("start")
						.breadcrumb("Upload")
						.page();
	}
}
