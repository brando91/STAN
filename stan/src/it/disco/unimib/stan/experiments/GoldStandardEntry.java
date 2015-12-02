package it.disco.unimib.stan.experiments;

public class GoldStandardEntry {

	private String id;
	private String label;
	private int score;

	public GoldStandardEntry(String id, String label, int score) {
		this.id = id;
		this.label = label;
		this.score = score;
	}

	public String display() {
		return clean(this.id) + " Q0 " + clean(this.label) + " " + this.score;
	}
	
	private String clean(String string) {
		return string.toLowerCase().replace(" ", "_");
	}

	public String id() {
		return this.id;
	}

	public String label() {
		return this.label;
	}

}
