package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;
import it.disco.unimib.stan.core.WorkingAreaPaths;
import it.disco.unimib.stan.webapp.Mapping;
import it.disco.unimib.stan.webapp.Mappings;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class MappingsTest {

	@Before
	public void setUp(){
		new AnnotationArea().clear();
	}
	
	@Test
	public void containsNamespaces() throws Exception {
		ArrayList<String> content = new Mappings().build().lines();
		
		assertThat(content, hasItem("@prefix rml: <http://semweb.mmlab.be/ns/rml#>."));
		assertThat(content, hasItem("@prefix ql: <http://semweb.mmlab.be/ns/ql#>."));
		assertThat(content, hasItem("@prefix rr: <http://www.w3.org/ns/r2rml#>."));
		assertThat(content, hasItem("@prefix xsd: <http://www.w3.org/2001/XMLSchema#>."));
		assertThat(content, hasItem("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>."));
	}
	
	@Test
	public void containsMappings() throws Exception {
		ArrayList<String> content = new Mappings()
											.withMapping(new Mapping("AirportMapping"))
											.withMapping(new Mapping("CitiesMapping"))
											.build()
											.lines();
		
		assertThat(content, hasItem("<#AirportMapping>"));
		assertThat(content, hasItem("<#CitiesMapping>"));
	}
	
	@Test
	public void dotForEachMapping() throws Exception {
		ArrayList<String> content = new Mappings()
											.withMapping(new Mapping("AirportMapping"))
											.withMapping(new Mapping("CitiesMapping"))
											.build()
											.lines();
		
		int dots = 0;
		for(String line : content){
			if(line.equals(".")) dots++;
		}
		assertThat(dots, equalTo(2));
	}
	
	@Test
	public void saveMappings() throws Exception {
		Mappings mappings = new Mappings();
		mappings
			.withMapping(new Mapping("AirportMapping"))
			.withMapping(new Mapping("CitiesMapping"))
			.build();
		mappings.save("table.csv", "username");
		
		String path = new WorkingAreaPaths().mappings("username").file("table.csv_mappings.ttl").path();
		assertThat(new File(path).exists(), equalTo(true));
	}
}
