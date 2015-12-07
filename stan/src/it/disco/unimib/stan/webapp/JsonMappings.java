package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.CSVTable;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.Header;
import it.disco.unimib.stan.core.WorkingAreaPaths;

import java.io.IOException;

public class JsonMappings {

	public void save(String table, String user, JsonAnnotationStatus jsonStatus, String sourceLocation) throws Exception {
		WorkingTable currentTable = new WorkingTable(table, user);
		Header header = currentHeader(currentTable, user);
		int subjectColumn = currentTable.getSubject();
		Mappings mappings = new Mappings();
		
		Annotation subjectAnnotation = currentTable.annotationForColumn(subjectColumn);
		Mapping subjectSource = source(subjectAnnotation.label(), sourceLocation, currentTable, header.inPosition(subjectColumn), subjectAnnotation.uri());
		
		for(JsonAnnotation property : jsonStatus.annotations()){
			int columnId = Integer.parseInt(property.getColumn());
			if(columnId == subjectColumn || columnNotAnnotated(property)) continue;
			
			if(property.isObjectProperty()){
				Mapping objectSource = source(property.getLabel(), sourceLocation, currentTable, header.inPosition(columnId), property.getObjectType());
				mappings.withMapping(objectSource);
				subjectSource.withObjectProperty(property.getUri(), property.getLabel());
			}
			else{
				subjectSource.withDatatypeProperty(property.getUri(), header.inPosition(columnId), property.getObjectType());
			}
		}
		
		mappings.withMapping(subjectSource)
				.build()
				.save(currentTable.getTableName(), user);
	}

	private Mapping source(String sourceName, String location, WorkingTable table, String reference, String type) throws IOException {
		return new Mapping(sourceName)
					.withLogicalSource(location)
					.withSubject(namespace(table) + "{" + reference + "}", type, reference);
	}

	private String namespace(WorkingTable currentTable) throws IOException {
		String namespace = currentTable.getNamespace();
		return (namespace.isEmpty()) ? "http://noNamespace/" : namespace;
	}

	private boolean columnNotAnnotated(JsonAnnotation annotation) {
		return annotation.getUri().isEmpty();
	}

	private Header currentHeader(WorkingTable currentTable, String user) throws Exception {
		FileResource table = new FileResource(new WorkingAreaPaths().tables(user).file(currentTable.getTableName()).path());
		return new CSVTable(table)
						.withSeparator(currentTable.getSeparator())
						.withTextDelimiter(currentTable.getDelimiter())
						.withHeader()
						.getHeader();
	}
}