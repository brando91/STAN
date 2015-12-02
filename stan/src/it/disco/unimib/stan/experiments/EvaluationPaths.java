package it.disco.unimib.stan.experiments;

public class EvaluationPaths {
	
	private String path;

	public EvaluationPaths() {
		this.path = "../evaluation";
	}

	public String path() {
		return this.path;
	}

	public EvaluationPaths goldStandards() {
		this.path += "/gold-standards";
		return this;
	}

	public EvaluationPaths results() {
		this.path += "/results";
		return this;
	}
	
	public EvaluationPaths folder(String algorithm) {
		this.path += "/" + algorithm;
		return this;
	}

	public EvaluationPaths file(String fileName) {
		this.path += "/" + fileName;
		return this;
	}

}
