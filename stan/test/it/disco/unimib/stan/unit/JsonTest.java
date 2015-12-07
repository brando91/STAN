package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.Json;

import java.util.ArrayList;

import org.junit.Test;

public class JsonTest {
	
	@Test
	public void emptyJSON() throws Exception {
		String json = new Json().serialize();
		
		assertThat(json, equalTo("{}"));
	}
	
	@Test
	public void oneParameter() throws Exception {
		String json = new Json()
							.parameter("name", "value")
							.serialize();
		
		assertThat(json, equalTo("{\"name\":\"value\"}"));
	}
	
	@Test
	public void twoParameters() throws Exception {
		String json = new Json()
							.parameter("name1", "value1")
							.parameter("name2", "value2")
							.serialize();
		
		assertThat(json, equalTo("{\"name1\":\"value1\",\"name2\":\"value2\"}"));
	}
	
	@Test
	public void emptyNested() throws Exception {
		String json = new Json()
							.parameters("nested", new ArrayList<String>())
							.serialize();
		
		assertThat(json, equalTo("{\"nested\":[]}"));
	}
	
	@Test
	public void nestedJson() throws Exception {
		ArrayList<String> members = new ArrayList<String>();
		members.add("value");
		String json = new Json()
							.parameters("nested", members)
							.serialize();
		
		assertThat(json, equalTo("{\"nested\":[\"value\"]}"));
	}
	
	@Test
	public void nestedNumericJson() throws Exception {
		ArrayList<Double> members = new ArrayList<Double>();
		members.add(2.5);
		String json = new Json()
							.numericParameters("nested", members)
							.serialize();
		
		assertThat(json, equalTo("{\"nested\":[2.5]}"));
	}
	
	@Test
	public void nestedJsonWithTwoValues() throws Exception {
		ArrayList<String> members = new ArrayList<String>();
		members.add("value1");
		members.add("value2");
		String json = new Json()
							.parameters("nested", members)
							.serialize();
		
		assertThat(json, equalTo("{\"nested\":[\"value1\",\"value2\"]}"));
	}

	@Test
	public void twoNestedJson() throws Exception {
		ArrayList<String> members = new ArrayList<String>();
		members.add("value");
		String json = new Json()
							.parameters("nested1", members)
							.parameters("nested2", members)
							.serialize();
		
		assertThat(json, equalTo("{\"nested1\":[\"value\"],\"nested2\":[\"value\"]}"));
	}
	
	@Test
	public void jsonNestedInJson() throws Exception {
		ArrayList<Json> members = new ArrayList<Json>();
		members.add(new Json().parameter("p1", "v1")
							  .parameter("p2", "v2"));
		String json = new Json()
							.jsonParameters("rows", members)
							.serialize();
		
		assertThat(json, equalTo("{\"rows\":[{\"p2\":\"v2\",\"p1\":\"v1\"}]}"));
	}
	
	@Test
	public void twoNestedJsonInJson() throws Exception {
		ArrayList<Json> members = new ArrayList<Json>();
		members.add(new Json().parameter("p1", "v1"));
		members.add(new Json().parameter("p1", "v1"));
		String json = new Json()
							.jsonParameters("rows", members)
							.serialize();
		
		assertThat(json, equalTo("{\"rows\":[{\"p1\":\"v1\"},{\"p1\":\"v1\"}]}"));
	}
	
	@Test
	public void deserializeEmptyJSON() throws Exception {
		String json = new Json()
							.deserialize("{}")
							.get("any");

		assertThat(json, equalTo(""));
	}
	
	@Test
	public void deserializeOneParameter() throws Exception {
		String json = new Json()
							.deserialize("{\"name\":\"value\"}")
							.get("name");
		
		assertThat(json, equalTo("value"));
	}
	
	@Test
	public void deserializeTwoParameters() throws Exception {
		Json deserialize = new Json().deserialize("{\"name1\":\"value1\",\"name2\":\"value2\"}");
		
		String firstParameter = deserialize.get("name1");
		String secondParameter = deserialize.get("name2");
		
		assertThat(firstParameter, equalTo("value1"));
		assertThat(secondParameter, equalTo("value2"));
	}
	
	@Test
	public void deserializeEmptyNested() throws Exception {
		ArrayList<String> values = new Json()
									.deserialize("{\"nested\":[\"\"]}")
									.getArray("nested");

		assertThat(values.size(), equalTo(0));
	}
	
	@Test
	public void deserializeNestedJson() throws Exception {
		ArrayList<String> values = new Json()
									.deserialize("{\"nested\":[\"value\"]}")
									.getArray("nested");
		
		assertThat(values, contains("value"));
	}
	
	@Test
	public void deserializeNestedNumericJson() throws Exception {
		ArrayList<String> values = new Json()
									.deserialize("{\"nested\":[5.0]}")
									.getArray("nested");
		
		assertThat(values, contains("5.0"));
	}
	
	@Test
	public void deserializeNestedJsonWithTwoValues() throws Exception {
		ArrayList<String> values = new Json()
									.deserialize("{\"nested\":[\"value1\",\"value2\"]}")
									.getArray("nested");
		
		assertThat(values, contains("value1", "value2"));
	}
	
	@Test
	public void deserializeTwoNestedJson() throws Exception {
		Json json = new Json().deserialize("{\"nested1\":[\"value1\"],\"nested2\":[\"value2\"]}");
		ArrayList<String> values1 = json.getArray("nested1");
		ArrayList<String> values2 = json.getArray("nested2");
		
		assertThat(values1, contains("value1"));
		assertThat(values2, contains("value2"));
	}
	
	@Test
	public void escapeQuotes() throws Exception {
		String json = new Json()
							.parameter("name", "\"value")
							.serialize();

		assertThat(json, equalTo("{\"name\":\"\\\"value\"}"));
	}
	
	@Test
	public void nestedJsonEscaped() throws Exception {
		ArrayList<String> members = new ArrayList<String>();
		members.add("\"value");
		String json = new Json()
							.parameters("nested", members)
							.serialize();
		
		assertThat(json, equalTo("{\"nested\":[\"\\\"value\"]}"));
	}
	
	@Test
	public void deserializeElementWithComma() throws Exception {
		String toDeserialize = new Json()
				.parameter("location", "\"(41.841529563, -87.658467635)\"")
				.serialize();

		Json json = new Json().deserialize(toDeserialize);

		assertThat(json.get("location"), equalTo("\"(41.841529563, -87.658467635)\""));
	}
	
	@Test
	public void deserializeJsonList() throws Exception {
		ArrayList<Json> results = new ArrayList<Json>();
		results.add(new Json().parameter("par1", "val1"));
		results.add(new Json().parameter("par2", "val2"));
		String toDeserialize = new Json()
				.jsonParameters("results", results)
				.serialize();

		ArrayList<Json> jsonArray = new Json().deserialize(toDeserialize).getJsonArray("results");
		assertThat(new Json().deserialize(jsonArray.get(0).serialize()).get("par1"), equalTo("val1"));
		assertThat(new Json().deserialize(jsonArray.get(1).serialize()).get("par2"), equalTo("val2"));
	}
	
	@Test
	public void getJsonObject() throws Exception {
		Json json = new Json().deserialize("{\"q0\": {\"query\": \"foo\"},\"q1\":{\"query\": \"bar\"}}");
		
		Json json2 = json.getJson("q0");
		assertThat(json2.get("query"), equalTo("foo"));
		assertThat(json.getJson("q1").get("query"), equalTo("bar"));
	}
}
