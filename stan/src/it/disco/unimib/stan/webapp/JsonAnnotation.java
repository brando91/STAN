package it.disco.unimib.stan.webapp;

public class JsonAnnotation {

	private String column;
	private String uri;
	private String label;
	private String objectType;
	private String propertyType;

	public JsonAnnotation(String column, String uri, String label, String objectType, String propertyType) {
		this.column = column;
		this.uri = uri;
		this.label = label;
		this.objectType = objectType;
		this.propertyType = propertyType;
	}

	public String getColumn() {
		return this.column;
	}

	public String getUri() {
		return this.uri;
	}

	public String getLabel() {
		return this.label;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public String getPropertyType() {
		return this.propertyType;
	}

	public boolean isObjectProperty() {
		return this.propertyType.equals("concept");
	}

}
