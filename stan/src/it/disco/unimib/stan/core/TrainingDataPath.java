package it.disco.unimib.stan.core;

public class TrainingDataPath {
	
	private String root = "";
	private String domain = "";
	private String column = "";
	private String file = "";

	public TrainingDataPath() {
		this.root = "../training-data";
	}
	
	public TrainingDataPath ecommerce() {
		this.domain = "/ecommerce";
		return this;
	}

	public TrainingDataPath test() {
		this.domain = "/test";
		return this;
	}

	public TrainingDataPath column(String column) {
		this.column = "/" + column;
		return this;
	}

	public TrainingDataPath file(String fileName) {
		this.file = "/" + fileName;
		return this;
	}

	public String path() {
		return root + domain + column + file;
	}
}
