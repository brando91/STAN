package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.Column;
import it.disco.unimib.stan.core.Resource;

import java.util.ArrayList;
import java.util.List;

public class TrainingColumn {

	private Column column;
	private Resource resource;
	
	public TrainingColumn(Resource trainingColumn) throws Exception {
		this.resource = trainingColumn;
		this.column = new Column(trainingColumn.name());
	}

	public boolean hasHeader() {
		return (!this.header().equals("NO HEADER"));
	}

	public String header() {
		return (this.column.header().isEmpty()) ? "NO HEADER" : this.column.header();
	}
	
	public TrainingColumn parseColumn() throws Exception {
		List<String> lines = this.resource.linesEncoded("UTF-8");
		for(int i = 0; i < lines.size(); i++){
			if(i == 0){
				this.column.withHeader(lines.get(i));
			}
			else{
				this.column.withMember(lines.get(i));
			}
		}
		return this;
	}

	public ArrayList<String> members() {
		return this.column.members();
	}

	public ArrayList<Double> numericMembers() {
		return this.column.numericMembers();
	}

	public boolean isNumeric() {
		return this.column.isNumeric();
	}
	
	public boolean isTextual() {
		return this.column.isTextual();
	}

	public String id() {
		return this.column.id();
	}
}
