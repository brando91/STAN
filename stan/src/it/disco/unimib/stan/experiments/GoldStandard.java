package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.FileResource;

import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class GoldStandard {
	
	private FileResource resource;
	private ArrayList<GoldStandardEntry> entries;

	public GoldStandard(String name) {
		this.entries = new ArrayList<GoldStandardEntry>();
		this.resource = new FileResource(new EvaluationPaths()
											.goldStandards()
											.file(name + ".qrels")
											.path());
	}
	
	public GoldStandard addEntry(String id, String label, int relevance) {
		this.entries.add(new GoldStandardEntry(id, label, relevance));
		return this;
	}
	
	public ArrayList<GoldStandardEntry> entries() {
		return this.entries;
	}
	
	public ArrayList<GoldStandardEntry> writtenEntries() throws Exception {
		ArrayList<GoldStandardEntry> entries = new ArrayList<GoldStandardEntry>();
		for(String line : this.resource.lines()){
			String[] fields = line.split(" ");
			entries.add(new GoldStandardEntry(fields[0], fields[2], Integer.parseInt(fields[3])));
		}
		return entries;
	}

	public void save() throws Exception {
		ArrayList<String> lines = new ArrayList<String>();
		for(GoldStandardEntry entry : this.entries){
			lines.add(entry.display());
		}
		FileUtils.writeLines(this.resource.asFile(), lines);
	}
	
	public void delete() {
		this.resource.delete();
	}
}
