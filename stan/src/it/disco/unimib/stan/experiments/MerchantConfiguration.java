package it.disco.unimib.stan.experiments;

import java.util.HashMap;

public class MerchantConfiguration {

	private String merchantName;
	private HashMap<String, SchemaAnnotation> columns;
	private String delimiter;
	private String hasHeader;
	
	public MerchantConfiguration() {
		this.columns = new HashMap<String, SchemaAnnotation>();
		this.delimiter = "|";
	}

	public void setMerchant(String merchantName) {
		this.merchantName = merchantName.replace("[", "").replace("]", "");
	}
	
	public String merchantName() {
		return this.merchantName;
	}

	public void addColumn(String columnLine) {
		this.columns.put(columnLine.split("=")[0], annotation(columnLine.split("=")[1]));
	}

	private SchemaAnnotation annotation(String column) {
		String[] split = column.split(" ");
		return new SchemaAnnotation(removeNumbers(split[0]), split[1]);
	}

	private String removeNumbers(String column) {
		String lastChar = column.charAt(column.length()-1) + "";
		String lastTwoChars = column.charAt(column.length()-2) + lastChar;
		try{
			Integer.parseInt(lastTwoChars);
			return column.substring(0, column.length()-2);
		}
		catch(Exception e){
			try{
				Integer.parseInt(lastChar);
				return column.substring(0, column.length()-1);
			}
			catch(Exception f){
				return column;
			}
		}
	}

	public HashMap<String, SchemaAnnotation> columns() {
		return this.columns;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String delimiter() {
		return this.delimiter;
	}

	public void setHeader(String header) {
		this.hasHeader = header;
	}

	public boolean hasHeader() {
		return Boolean.parseBoolean(this.hasHeader);
	}

	public SchemaAnnotation getAnnotationFor(String columnId) {
		String replace = columnId.replace(merchantName.toLowerCase() + "-", "");
		String firstCapital = firstCapital(replace);
		return columns.get(firstCapital);
	}
	
	private String firstCapital(String string) {
		String firstLetter = string.charAt(0) + "";
		return firstLetter.toUpperCase() + string.substring(1);
	}
}
