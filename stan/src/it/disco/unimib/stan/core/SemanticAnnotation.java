package it.disco.unimib.stan.core;

public class SemanticAnnotation {

	private String label;
	private double score;
	
	public SemanticAnnotation(String label, double score) {
		this.label = label;
		this.score = score;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public double getScore() {
		return this.score;
	}
}
