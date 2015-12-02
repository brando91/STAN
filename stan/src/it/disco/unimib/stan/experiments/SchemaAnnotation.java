package it.disco.unimib.stan.experiments;

import java.util.HashMap;

public class SchemaAnnotation {

	private String type;
	private String annotation;
	private HashMap<String, String> types;

	public SchemaAnnotation(String annotation, String type) {
		this.annotation = annotation;
		this.type = type;
		this.types = new HashMap<String, String>();
		definedTypes();
	}

	public String annotation() {
		return this.annotation;
	}

	public String type() {
		String definedType = this.types.get(annotation.toLowerCase());
		return (definedType != null) ? definedType : this.type; 
	}

	private HashMap<String, String> definedTypes() {
		put("marca", "Brand");
		put("brand", "Brand");
		put("prodotto", "Product");
		urls("images", "img", "imglarge", "imgmedium", "immagine", "immaginegrande", "immaginel", "immaginelarge", "immaginem", "immaginemedia",
			 "immaginemedium", "immagineno", "immaginepiccola", "immaginesmall", "smallimmagine");
		urls("link", "linkinglese", "locaionurl");
		categories("categoria", "categoriagoogle", "categorialink", "categoriamacro", "categoriaold", "macrocat", "macrocategoria", "megacategoria",
					"sottocat", "sottocategoria");
		return types;
	}

	private void urls(String... annotations) {
		addAll("URL", annotations);
	}

	private void categories(String... categories) {
		addAll("Category", categories);
	}

	private void addAll(String type, String... annotations) {
		for(String annotation : annotations){
			put(annotation, type);
		}
	}
	
	private void put(String annotation, String type) {
		types.put(annotation.toLowerCase(), type);
	}
}
