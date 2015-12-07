package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.WorkingAreaPaths;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class Mappings {

	private ArrayList<String> lines;
	private ArrayList<Mapping> mappings;

	public Mappings() {
		this.lines = new ArrayList<String>();
		this.mappings = new ArrayList<Mapping>();
		addNamespaces();
	}
	
	public Mappings build() {
		for(Mapping mapping : this.mappings){
			this.lines.addAll(mapping.build());
			this.lines.add(".");
		}
		return this;
	}
	
	public ArrayList<String> lines(){
		return this.lines;
	}
	
	public void save(String tableName, String user) throws IOException {
		File mappingsFile = new File(new WorkingAreaPaths().mappings(user).file(tableName + "_mappings.ttl").path());
		File toDownloadFile = new File(new WorkingAreaPaths().mappings(user).toDownload().file(tableName + "_mappings.ttl").path());
		FileUtils.writeLines(mappingsFile, "UTF-8", this.lines);
		FileUtils.copyFile(mappingsFile, toDownloadFile);
	}

	public Mappings withMapping(Mapping mapping) {
		this.mappings.add(mapping);
		return this;
	}
	
	private void addNamespaces() {
		this.lines.add(namespace("rml", "http://semweb.mmlab.be/ns/rml#"));
		this.lines.add(namespace("ql", "http://semweb.mmlab.be/ns/ql#"));
		this.lines.add(namespace("rr", "http://www.w3.org/ns/r2rml#"));
		this.lines.add(namespace("xsd", "http://www.w3.org/2001/XMLSchema#"));
		this.lines.add(namespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#"));
		this.lines.add("");
	}

	private String namespace(String prefix, String uri) {
		return "@prefix " + prefix + ": <" + uri + ">.";
	}
}
