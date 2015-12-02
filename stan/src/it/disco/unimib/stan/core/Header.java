package it.disco.unimib.stan.core;

import it.disco.unimib.stan.webapp.Annotation;
import it.disco.unimib.stan.webapp.WorkingTable;

import java.io.IOException;
import java.util.ArrayList;

public class Header {

	private ArrayList<Column> columns;

	public Header(ArrayList<Column> columns) {
		this.columns = columns;
	}

	public ArrayList<String> asOriginalList() {
		ArrayList<String> header = new ArrayList<String>();
		for(Column column : columns){
			header.add(header(column));
		}
		return header;
	}

	public ArrayList<Annotation> asAnnotatedList(WorkingTable workingTable) throws IOException {
		ArrayList<String> tableHeader = this.asOriginalList();
		ArrayList<Annotation> headers = new ArrayList<Annotation>();
		
		for(int i = 0; i < tableHeader.size(); i++){
			Annotation annotation = workingTable.annotationForColumn(i+1);
			if(annotation.isSet()){
				headers.add(annotation);
			}
			else{
				headers.add(new Annotation("", tableHeader.get(i), "", ""));
			}
		}
		return headers;
	}

	public String inPosition(int position) {
		return this.asOriginalList().get(position -1 );
	}
	
	private String header(Column column) {
		return (column.header().isEmpty()) ? "-" : column.header();
	}
}
