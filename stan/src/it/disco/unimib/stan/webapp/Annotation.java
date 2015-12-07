package it.disco.unimib.stan.webapp;


public class Annotation {

	public String uri;
	public String label;
	public String object;
	public String propertyType;
	public String styleClass;

	public Annotation(String uri, String label, String object, String propertyType) {
		this.uri = uri;
		this.label = label;
		this.object = object;
		this.propertyType = propertyType;
		this.styleClass = currentClass();
	}

	public Annotation setAsSubject() {
		this.styleClass = "subject";
		return this;
	}
	
	public String uri() {
		return this.uri;
	}

	public String label() {
		return this.label;
	}

	public String object() {
		return this.object;
	}
	
	public String propertyType() {
		return this.propertyType;
	}

	public String classSet() {
		return this.styleClass;
	}
	
	public boolean isSet() {
		return (uri != null && !uri.isEmpty());
	}
	
	private String currentClass() {
		String currentClass = "";
		if(isSet()){
			currentClass = (propertyType.isEmpty()) ? "datatype" : propertyType;
		}
		return currentClass;
	}
}
