package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.Json;

import java.util.ArrayList;

public class JsonAnnotationStatus {

	private ArrayList<Json> annotations;

	public JsonAnnotationStatus(String jsonAnnotations) {
		this.annotations = new Json().deserialize(jsonAnnotations).getJsonArray("columns");
	}

	public ArrayList<JsonAnnotation> annotations() {
		ArrayList<JsonAnnotation> jsonAnnotations = new ArrayList<JsonAnnotation>();
		for(Json annotation : annotations){
			annotation.deserialize(annotation.serialize());
			jsonAnnotations.add(new JsonAnnotation(
										annotation.getNumeric("column"), 
										annotation.get("uri").trim(), 
										annotation.get("label").trim(),
										annotation.get("object-type").trim(),
										annotation.get("property-type").trim()
								));
		}
		return jsonAnnotations;
	}

}
