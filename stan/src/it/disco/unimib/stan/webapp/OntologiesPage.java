package it.disco.unimib.stan.webapp;


public class OntologiesPage implements ApplicationHandler {

	@Override
	public String route() {
		return "ontologies";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception {
		return new Template("ontologies")
						.breadcrumb("Ontologies")
						.page();
	}
}
