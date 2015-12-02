package it.disco.unimib.stan.core;

import java.util.ArrayList;

public class Json {
	
	private JSONObject jsonObject;
	
	public Json() {
		this.jsonObject = new JSONObject();
	}

	public Json(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public String serialize() {
		return jsonObject.toString();
	}

	public Json parameter(String name, String value) {
		jsonObject.put(name, value);
		return this;
	}
	
	public Json numericParameter(String name, double value) {
		jsonObject.put(name, value);
		return this;
	}
	
	public Json parameters(String name, ArrayList<String> members) {
		jsonObject.put(name, members);
		return this;
	}
	
	public Json numericParameters(String name, ArrayList<Double> members) {
		jsonObject.put(name, members);
		return this;
	}
	
	public Json jsonParameters(String name, ArrayList<Json> members) {
		JSONArray jsonArray = new JSONArray();
		for(Json json : members){
			jsonArray.put(json.asObject());
		}
		jsonObject.put(name, jsonArray);
		return this;
	}
	
	public Json deserialize(String json){
		this.jsonObject = new JSONObject(json);
		return this;
	}
	
	public String get(String parameter) {
		String value = "";
		try{
			value = this.jsonObject.getString(parameter);
		}
		catch(Exception e){}
		
		return value;
	}
	
	public String getNumeric(String parameter) {
		String value = "";
		try{
			value = this.jsonObject.getInt(parameter) + "";
		}
		catch(Exception e){}
		
		return value;
	}
	
	public Json getJson(String parameter) {
		return new Json(this.jsonObject.getJSONObject(parameter));
	}
	
	public ArrayList<String> getArray(String parameter) {
		ArrayList<String> values = new ArrayList<String>();
		JSONArray jsonArray;
		try{
			jsonArray = this.jsonObject.getJSONArray(parameter);
		}
		catch(Exception e){
			return values;
		}
		
		for(int i = 0; i < jsonArray.length(); i++){
			String value = "";
			try{
				value = jsonArray.getString(i);
			}
			catch(Exception e){
				value = jsonArray.getDouble(i) + "";
			}
			if(!value.isEmpty()) values.add(value);
		}
		
		return values;
	}
	
	public ArrayList<Json> getJsonArray(String parameter) {
		ArrayList<Json> values = new ArrayList<Json>();
		JSONArray jsonArray;
		try{
			jsonArray = this.jsonObject.getJSONArray(parameter);
		}
		catch(Exception e){
			return values;
		}
		for(int i = 0; i < jsonArray.length(); i++){
			values.add(new Json(jsonArray.getJSONObject(i)));
		}
		return values;
	}
	
	private JSONObject asObject() {
		return this.jsonObject;
	}
}
