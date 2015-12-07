package it.disco.unimib.stan.webapp;

import java.util.ArrayList;

public class Mapping {

	private ArrayList<String> lines;

	public Mapping(String mappingName) {
		this.lines = new ArrayList<String>();
		addName(mappingName);
	}
	
	public ArrayList<String> build() {
		return this.lines;
	}

	private void addName(String mappingName) {
		this.lines.add("<#" + mappingName + ">");
	}

	public Mapping withLogicalSource(String tableName) {
		this.lines.addAll(logicalSourceBlock(tableName));
		return this;
	}

	public Mapping withSubject(String subjectUri, String subjectType, String reference) {
		this.lines.addAll(subjectBlock(subjectUri, subjectType));
		withLabelProperty(reference);
		return this;
	}
	
	public Mapping withDatatypeProperty(String property, String reference, String datatype) {
		this.lines.addAll(datatypePropertyBlock(addQuotes(property), reference, datatype));
		return this;
	}
	
	public Mapping withLabelProperty(String reference) {
		this.lines.addAll(datatypePropertyBlock("rdfs:label", reference, ""));
		return this;
	}
	
	public Mapping withObjectProperty(String property, String reference) {
		this.lines.addAll(objectPropertyBlock(property, reference));
		return this;
	}

	private ArrayList<String> logicalSourceBlock(String tableName) {
		ArrayList<String> logicalSourceBlock = new ArrayList<String>();
		logicalSourceBlock.add(openBlock("rml:logicalSource"));
		logicalSourceBlock.add(innerStatement("rml:source", addQuotes(tableName)));
		logicalSourceBlock.add(lastStatement("rml:referenceFormulation", "ql:CSV"));
		logicalSourceBlock.add(closeBlock());
		return logicalSourceBlock;
	}
	
	private ArrayList<String> subjectBlock(String subjectUri, String subjectType) {
		ArrayList<String> subjectBlock = new ArrayList<String>();
		subjectBlock.add(openBlock("rr:subjectMap"));
		subjectBlock.add(innerStatement("rr:template", addQuotes(subjectUri)));
		subjectBlock.add(lastStatement("rr:class", "<" + subjectType + ">"));
		subjectBlock.add(closeBlock());
		return subjectBlock;
	}

	private ArrayList<String> datatypePropertyBlock(String property, String reference, String datatype) {
		ArrayList<String> propertyBlock = new ArrayList<String>();
		propertyBlock.add(openBlock("rr:predicateObjectMap"));
		propertyBlock.add(innerStatement("rr:predicate", property));
		propertyBlock.add(openBlock("rr:objectMap"));
		propertyBlock.add(innerStatement("rml:reference", addQuotes(reference)));
		if(!datatype.isEmpty()) propertyBlock.add(lastStatement("rr:datatype", "<" + datatype + ">"));
		propertyBlock.add(closeInnerBlock());
		propertyBlock.add(closeBlock());
		return propertyBlock;
	}
	
	private ArrayList<String> objectPropertyBlock(String property, String reference) {
		ArrayList<String> propertyBlock = new ArrayList<String>();
		propertyBlock.add(openBlock("rr:predicateObjectMap"));
		propertyBlock.add(innerStatement("rr:predicate", addQuotes(property)));
		propertyBlock.add(openBlock("rr:objectMap"));
		propertyBlock.add(lastStatement("rr:parentTriplesMap", "<#" + reference + ">"));
		propertyBlock.add(closeInnerBlock());
		propertyBlock.add(closeBlock());
		return propertyBlock;
	}

	private String openBlock(String type) {
		return type + " [";
	}

	private String innerStatement(String term, String value) {
		return lastStatement(term, value) + ";";
	}
	
	private String lastStatement(String term, String value) {
		return term + " " + value;
	}

	private String closeBlock() {
		return "];";
	}
	
	private String closeInnerBlock() {
		return "]";
	}
	
	private String addQuotes(String string) {
		return "\"" + string + "\"";
	}
}
