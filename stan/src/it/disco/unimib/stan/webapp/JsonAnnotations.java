package it.disco.unimib.stan.webapp;

import java.io.IOException;

public class JsonAnnotations{

	public void save(String table, String user, JsonAnnotationStatus jsonStatus, String subject) throws IOException {
		WorkingTable currentTable = new WorkingTable(table, user);
		
		currentTable.saveSubject(split(subject));
		for(JsonAnnotation annotation : jsonStatus.annotations()){
			currentTable.saveAnnotation(annotation.getColumn(), 
										annotation.getUri(), 
										annotation.getLabel(),
										annotation.getObjectType(),
										annotation.getPropertyType());
		}
	}

	private String split(String subject) {
		if(!subject.isEmpty()) return subject.split("-")[1];
		return "";
	}
	
}