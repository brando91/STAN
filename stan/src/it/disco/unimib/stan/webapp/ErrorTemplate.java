package it.disco.unimib.stan.webapp;

public class ErrorTemplate {

	private Template template;

	public ErrorTemplate(String page, String breadcrumbs, String error) {
		this. template = new Template(page)
								.error(error)
								.breadcrumb(breadcrumbs);
	}

	public String page() {
		return this.template.page();
	}

}
