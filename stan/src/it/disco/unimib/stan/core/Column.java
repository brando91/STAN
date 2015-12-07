package it.disco.unimib.stan.core;

import java.util.ArrayList;

public class Column {

	private String id;
	private ArrayList<String> members;
	private ArrayList<Double> numericMembers;
	private final double NUMERIC_PERCENTAGE = 0.6;
	private final double NUMERIC_PERCENTAGE_AT_QUERY_TIME = 0.7;
	private final double TEXTUAL_PERCENTAGE = 0.8;
	private String header;
	
	public Column(String id) {
		this.members = new ArrayList<String>();
		this.numericMembers = new ArrayList<Double>();
		this.id = id;
		this.header = "";
	}

	public String id() {
		return this.id;
	}
	
	public ArrayList<String> members() {
		return this.members;
	}
	
	public ArrayList<Double> numericMembers() {
		return this.numericMembers;
	}

	public Column withMember(String member) {
		if(notNullOrEmpty(member)){
			this.members.add(member);
			Numeric numeric = new Numeric(member);
			if(numeric.isNumeric()){
				this.numericMembers.add(numeric.asDouble());
			}
		}
		return this;
	}

	public Column withAllMembers(ArrayList<String> members) {
		for(String member : members){
			this.withMember(member);
		}
		return this;
	}
	
	public Column withHeader(String header) {
		this.header = header;
		return this;
	}
	
	public String header() {
		return this.header;
	}

	public boolean isNumeric() {
		return (this.numericMembers.size() >= (double) this.members.size()*this.NUMERIC_PERCENTAGE);
	}

	public boolean isTextual() {
		return (this.numericMembers.size() < (double) this.members.size()*this.TEXTUAL_PERCENTAGE);
	}

	public boolean isNumericAtQueryTime() {
		return (this.numericMembers.size() >= (double) this.members.size()*this.NUMERIC_PERCENTAGE_AT_QUERY_TIME);
	}
	
	private boolean notNullOrEmpty(String member) {
		return !member.equalsIgnoreCase("null") && !member.trim().isEmpty();
	}
}
