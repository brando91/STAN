package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.WorkingAreaPaths;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class WorkingTable {

	private File file;
	private ArrayList<String> lines;
	private String tableName;
	private static String subjectField = "subject";
	private static String namespaceField = "namespace";
	private static String tableField = "tableName";
	private static String separatorField = "separator";
	private static String delimiterField = "delimiter";
	private static String headerField = "header";

	public WorkingTable(String tableName, String user) {
		this.file = new File(new WorkingAreaPaths().schemas(user).file(tableName + ".txt").path());
		this.lines = new ArrayList<String>();
		this.tableName = tableName;
	}
	
	public WorkingTable save() throws IOException {
		addLine(tableField, this.tableName);
		FileUtils.writeLines(this.file, "UTF-8", this.lines);
		return this;
	}
	
	public WorkingTable withSeparator(String separator) {
		addLine(separatorField, separator);
		return this;
	}
	
	public WorkingTable withDelimiter(String delimiter) {
		addLine(delimiterField, delimiter);
		return this;
	}
	
	public WorkingTable withHeader(boolean hasHeader) {
		String bool = hasHeader ? "true" : "false";
		addLine(headerField, bool);
		return this;
	}
	
	public String getTableName() throws IOException {
		return read(tableField);
	}

	public String getSeparator() throws IOException {
		return read(separatorField);
	}
	
	public String getDelimiter() throws IOException {
		return read(delimiterField);
	}
	
	public String getNamespace() throws IOException {
		return read(namespaceField);
	}
	
	public boolean hasHeader() throws IOException {
		return read(headerField).equals("true");
	}
	
	public int getSubject() throws Exception {
		return Integer.parseInt(read(subjectField));
	}
	
	public Annotation annotationForColumn(int column) throws IOException {
		String savedMapping = read("column_" + column);
		if(!savedMapping.isEmpty()){
			String[] split = savedMapping.split("\\|", -1);
			Annotation annotation = new Annotation(split[0], split[1], split[2], split[3]);
			if(isSubject(column)) annotation.setAsSubject();
			return annotation; 
		}
		return new Annotation("", "", "", "");
	}
	
	public WorkingTable saveAnnotation(String column, String uri, String label, String objectType, String propertyType) throws IOException {
		saveField("column_" + column, uri + "|" + label + "|" + objectType + "|" + propertyType);
		return this;
	}
	
	public WorkingTable saveNamespace(String namespace) throws IOException {
		saveField(namespaceField, namespace);
		return this;
	}
	
	public WorkingTable saveSubject(String subject) throws IOException {
		saveField(subjectField, subject);
		return this;
	}

	private void saveField(String field, String value) throws IOException {
		this.lines = (ArrayList<String>) FileUtils.readLines(this.file);
		removeIfAlreadyExists(tableField);
		removeIfAlreadyExists(field);
		addLine(field, value);
		save();
	}
	
	private void removeIfAlreadyExists(String field) {
		String toRemove = "";
		for(String line : lines){
			if(line.split("=")[0].contains(field)){
				toRemove = line;
				break;
			}
		}
		this.lines.remove(toRemove);
	}

	private void addLine(String field, String value) {
		if(!value.equals("")) this.lines.add(field + "=" + value);
	}
	
	private String read(String field) throws IOException {
		if(exists()){
			for(String line : FileUtils.readLines(this.file, "UTF-8")){
				String[] split = line.split("=");
				if(split[0].contains(field)) {
					return split[1];
				}
			}
		}
		return "";
	}
	
	private boolean isSubject(int column) throws IOException {
		String subject = read(subjectField);
		return (!subject.isEmpty() && Integer.parseInt(subject) == column); 
	}
	
	private boolean exists() {
		return this.file.exists();
	}
}