package it.disco.unimib.stan.core;

public class IndexesPath {

	private String path;
	
	public IndexesPath(){
		this.path = "./indexes";
	}

	public String path() {
		return this.path;
	}

	public IndexesPath karma(String dataset) {
		this.path += "/karma-" + dataset;
		return this;
	}

	public IndexesPath test() {
		karma("test");
		return this;
	}

	public IndexesPath numericTest() {
		this.karmaNumeric("test");
		return this;
	}

	public IndexesPath karmaNumeric(String dataset) {
		this.path += "/karma-numeric-" + dataset;
		return this;
	}

	public IndexesPath labelling(String dataset) {
		this.path += "/labelling-" + dataset;
		return this;
	}
	
	public IndexesPath labellingTest() {
		return labelling("test");
	}

	public IndexesPath ecommerceRanges() {
		this.path += "/ecommerce-ranges";
		return this;
	}

	public IndexesPath ecommerceDomains() {
		this.path += "/ecommerce-domains";
		return this;
	}
}
