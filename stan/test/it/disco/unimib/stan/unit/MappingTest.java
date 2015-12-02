package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.webapp.Mapping;

import java.util.ArrayList;

import org.junit.Test;

public class MappingTest {

	@Test
	public void emptyMapping() throws Exception {
		assertThat(new Mapping("AirportMapping").build(), hasItem("<#AirportMapping>"));
	}
	
	@Test
	public void logicalSource() throws Exception {
		ArrayList<String> content = new Mapping("AirportMapping")
													.withLogicalSource("http://www.example.com/Airport.csv")
													.build();
		
		assertThat(content, contains("<#AirportMapping>",
									 "rml:logicalSource [",
										 "rml:source \"http://www.example.com/Airport.csv\";",
										 "rml:referenceFormulation ql:CSV",
									 "];"));
	}
	
	@Test
	public void subject() throws Exception {
		ArrayList<String> content = new Mapping("AirportMapping")
													.withSubject("http://airport.example.com/{id}", "http://vocab.org/transit/terms/Stop", "id")
													.build();
		
		assertThat(content, contains("<#AirportMapping>",
									 "rr:subjectMap [",
										 "rr:template \"http://airport.example.com/{id}\";",
										 "rr:class <http://vocab.org/transit/terms/Stop>",
									 "];",
									 "rr:predicateObjectMap [", 
									 	"rr:predicate rdfs:label;",
									 	"rr:objectMap [",
									 		"rml:reference \"id\";",
								 		"]",
							 		"];"));
	}
	
	@Test
	public void simpleProperty() throws Exception {
		ArrayList<String> content = new Mapping("AirportMapping")
													.withDatatypeProperty("transit:route", "stop", "http://int")
													.build();
		
		assertThat(content, contains("<#AirportMapping>",
									 "rr:predicateObjectMap [",
										 "rr:predicate \"transit:route\";",
										 "rr:objectMap [",
											 "rml:reference \"stop\";",
											 "rr:datatype <http://int>",
										 "]",
									 "];"));
	}
	
	@Test
	public void mappingWithOneProperty() throws Exception {
		ArrayList<String> content = new Mapping("MyMapping")
													.withLogicalSource("http://source")
													.withSubject("http://subject", "http://owl.it/type", "any")
													.withDatatypeProperty("gr:property", "property", "http://www.w3.org/2001/XMLSchema#double")
													.build();
		
		assertThat(content, hasItem("<#MyMapping>"));
		assertThat(content, hasItem("rml:source \"http://source\";"));
		assertThat(content, hasItem("rr:template \"http://subject\";"));
		assertThat(content, hasItem("rr:predicate \"gr:property\";"));
		assertThat(content, hasItem("rml:reference \"property\";"));
		assertThat(content, hasItem("rr:datatype <http://www.w3.org/2001/XMLSchema#double>"));
	}
	
	@Test
	public void mappingWithTwoProperties() throws Exception {
		ArrayList<String> content = new Mapping("MyMapping")
													.withDatatypeProperty("gr:property", "property", "xsd:text")
													.withDatatypeProperty("gr:property2", "property2", "xsd:text")
													.build();
		
		assertThat(content, hasItem("rr:predicate \"gr:property\";"));
		assertThat(content, hasItem("rr:predicate \"gr:property2\";"));
		assertThat(content, hasItem("rml:reference \"property\";"));
		assertThat(content, hasItem("rml:reference \"property2\";"));
	}
	
	@Test
	public void objectProperty() throws Exception {
		ArrayList<String> content = new Mapping("BananaMapping")
													.withObjectProperty("dbo:country", "country")
													.build();
		
		assertThat(content, contains("<#BananaMapping>",
									 "rr:predicateObjectMap [",
										 "rr:predicate \"dbo:country\";",
										 "rr:objectMap [",
											 "rr:parentTriplesMap <#country>",
										 "]",
									 "];"));
	}
}
