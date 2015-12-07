package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.FileResource;

import java.io.IOException;
import java.util.ArrayList;

public class MappingsReader {

	private ArrayList<String> lines;

	public MappingsReader(FileResource mappingsFile) throws IOException {
		this.lines = mappingsFile.lines();
	}

	public boolean hasNamespaces() {
		return lines.contains("@prefix rml: <http://semweb.mmlab.be/ns/rml#>.") &&
			   lines.contains("@prefix ql: <http://semweb.mmlab.be/ns/ql#>.") &&
			   lines.contains("@prefix rr: <http://www.w3.org/ns/r2rml#>.");
	}

	public boolean hasSourceLocation(String location) {
		return lines.contains("rml:source \"" + location + "\";");
	}

	public boolean hasSubject(String template, String type) {
		return lines.contains("rr:template \"" + template + "\";") &&
			   lines.contains("rr:class " + "<" + type + ">");
	}

	public boolean hasDatatypeProperty(String property, String reference, String datatype) {
		return lines.contains("rr:predicate \"" + property + "\";") &&
			   lines.contains("rml:reference \""+ reference + "\";") &&
			   lines.contains("rr:datatype <" + datatype + ">");
	}

	public Boolean hasObjectProperty(String property, String label, String reference, String objectType) {
		return lines.contains("rr:predicate \"" + property + "\";") &&
			   lines.contains("rr:parentTriplesMap <#" + label + ">") &&
			   lines.contains("rr:template \"http://noNamespace/{" + reference + "}\";") &&
			   lines.contains("rr:class <" + objectType + ">") &&
			   lines.contains("rr:predicate rdfs:label;") &&
			   lines.contains("rml:reference \""+ reference + "\";");
	}

}
