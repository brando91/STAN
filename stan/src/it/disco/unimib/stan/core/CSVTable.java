package it.disco.unimib.stan.core;

import java.io.IOException;
import java.util.ArrayList;

public class CSVTable{

	private Resource table;
	private boolean hasHeader;
	private String separator;
	private String textDelimiter;

	public CSVTable(Resource table) {
		this.table = table;
		this.hasHeader = false;
		this.separator = "\\|";
		this.textDelimiter = "";
	}
	
	public String separator() {
		return (separator.equals("\\|")) ? "|" : separator;
	}
	
	public CSVTable withSeparator(String separator) {
		this.separator = (separator.equals("|")) ? "\\|" : separator;
		return this;
	}
	
	public String textDelimiter() {
		return this.textDelimiter;
	}
	
	public CSVTable withTextDelimiter(String delimiter) {
		this.textDelimiter = delimiter;
		return this;
	}
	
	public boolean hasHeader() {
		return this.hasHeader;
	}
	
	public CSVTable withHeader() {
		this.hasHeader = true;
		return this;
	}
	
	public Header getHeader() throws Exception{
		return new Header(this.columns());
	}
	
	public ArrayList<Row> rows() throws IOException {
		ArrayList<String> lines = this.table.lines();
		ArrayList<Column> columns = initializeColumns(lines);
		ArrayList<Row> rows = new ArrayList<Row>();
		
		for(int row = 0; row < lines.size(); row++){
			if(firstLineWithHeaderOrEmptyLine(lines, row)) continue;
			
			String[] splittedLine = split(lines.get(row));
			Row currentRow = new Row();
			for(int col = 0; col < columns.size(); col++){
				try{
					currentRow.withCell(cleanToken(splittedLine[col]));
				}
				catch(Exception e)
				{
					logError(table.name(), row);
					continue;
				}
			}
			rows.add(currentRow);
		}
		return rows;
	}

	public ArrayList<Column> columns() throws Exception {
		ArrayList<String> lines = this.table.lines();
		ArrayList<Column> columns = initializeColumns(lines);
		
		for(int row = 0; row < lines.size(); row++){
			if(firstLineWithHeaderOrEmptyLine(lines, row)) continue;
			
			String[] splittedLine = split(lines.get(row));
			for(int col = 0; col < columns.size(); col++){
				try{
					columns.get(col).withMember(cleanToken(splittedLine[col]));
				}
				catch(Exception e)
				{
					logError(table.name(), row);
					continue;
				}
			}
		}
		return columns;
	}

	private ArrayList<Column> initializeColumns(ArrayList<String> lines) {
		ArrayList<Column> columns = new ArrayList<Column>();
		if(lines.size() > 0){
			String[] firstRow = split(lines.get(0));
			for(int i = 0; i < firstRow.length; i++){
				String header = (this.hasHeader) ? cleanToken(firstRow[i]) : "";
				columns.add(new Column(columnId(i)).withHeader(header));
			}
		}
		return columns;
	}

	private boolean firstLineWithHeaderOrEmptyLine(ArrayList<String> lines, int row) {
		return (row == 0 && this.hasHeader) || (lines.get(row).trim().isEmpty());
	}

	private String columnId(int columnNumber) {
		return (table.name() + "-col" + (columnNumber + 1)).toLowerCase();
	}
	
	private String[] split(String toSplit) {
		if(textDelimiter.isEmpty()){
			return toSplit.split(separator, -1);
		}
		return toSplit.split(separator + "(?=([^" + textDelimiter + "]*" + textDelimiter + "[^" + textDelimiter + "]*" + textDelimiter + ")*[^" + textDelimiter + "]*$)", -1);
	}

	private String cleanToken(String token) {
		return token.replace(textDelimiter, "");
	}
	
	private void logError(String table, int row) {
		new LogEvents().error("Error importing " + table + " at row " + row, null);
	}
}