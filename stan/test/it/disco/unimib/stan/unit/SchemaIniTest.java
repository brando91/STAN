package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.experiments.SchemaAnnotation;
import it.disco.unimib.stan.experiments.SchemaIni;

import org.junit.Test;

public class SchemaIniTest {

	@Test
	public void shouldGetTheschemaIniType() throws Exception {
		VirtualResource schemaIni = new VirtualResource()
											.addLine("[pippo.txt")
											.addLine("Col1=Any text");
		SchemaAnnotation schemaAnnotation = new SchemaIni(schemaIni).parse().get(0).columns().get("Col1");
		
		assertThat(schemaAnnotation.annotation(), equalTo("Any"));
		assertThat(schemaAnnotation.type(), equalTo("text"));
	}
	
	@Test
	public void shouldGetThePredefinedType() throws Exception {
		VirtualResource schemaIni = new VirtualResource()
											.addLine("[pippo.txt")
											.addLine("Col1=Prodotto text");
		SchemaAnnotation schemaAnnotation = new SchemaIni(schemaIni).parse().get(0).columns().get("Col1");
		
		assertThat(schemaAnnotation.annotation(), equalTo("Prodotto"));
		assertThat(schemaAnnotation.type(), equalTo("Product"));
	}
}
