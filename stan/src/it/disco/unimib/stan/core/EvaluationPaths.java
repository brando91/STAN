package it.disco.unimib.stan.core;

public class EvaluationPaths {

	private String path;
	
	public EvaluationPaths() {
		this.path = "../evaluation";
	}

	public String path() {
		return this.path;
	}

	public EvaluationPaths indexes() {
		this.path += "/labeller-indexes";
		return this;
	}

	public EvaluationPaths yago() {
		this.path += "/yago1";
		return this;
	}
	
	public EvaluationPaths dbpedia() {
		this.path += "/dbpedia";
		return this;
	}
	
	public EvaluationPaths dbpediaOntology() {
		this.path += "dbpedia-ontology";
		return this;
	}

	public EvaluationPaths depths() {
		this.path += "/depths";
		return this;
	}
	
	public EvaluationPaths properties() {
		this.path += "/properties";
		return this;
	}
	
	public EvaluationPaths types() {
		this.path += "/types";
		return this;
	}
	
	public EvaluationPaths labels() {
		this.path += "/labels";
		return this;
	}

	public EvaluationPaths goldStandard(String knowledgeBase) {
		this.path += "/gold-standards/" + knowledgeBase;
		return this;
	}
	
	public EvaluationPaths results(String knowledgeBase) {
		this.path += "/results/" + knowledgeBase;
		return this;
	}
	
	public EvaluationPaths ranges(String index) {
		this.path += "/" + index + "-ranges";
		return this;
	}
	
	public EvaluationPaths domains(String index) {
		this.path += "/" + index + "-domains";
		return this;
	}
	
	public EvaluationPaths file(String fileName) {
		this.path += "/" + fileName;
		return this;
	}
}
