package it.disco.unimib.stan.unit;

import it.disco.unimib.stan.core.Json;

import java.util.HashMap;

public class POSTBuilder {

	private String route;
	private Json data;
	private HashMap<String, String> parameters;

	public POSTBuilder(String route) {
		this.route = route;
		this.data = new Json();
		this.parameters = new HashMap<String, String>();
	}

	public String route() {
		return this.route;
	}

	public POSTBuilder withData(Json json) {
		this.data = json;
		return this;
	}

	public String data() {
		return this.data.serialize();
	}

	public POSTBuilder withParameter(String name, String value) {
		parameters.put(name, value);
		return this;
	}

	public HashMap<String, String> parameters() {
		return this.parameters;
	}

}
