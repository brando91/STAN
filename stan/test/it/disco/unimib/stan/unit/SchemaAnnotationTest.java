package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.experiments.SchemaAnnotation;

import org.junit.Test;

public class SchemaAnnotationTest {

	@Test
	public void simpleProperty() throws Exception {
		assertThat(new SchemaAnnotation("SKU", "Text").type(), equalTo("Text"));
	}
	
	@Test
	public void brandProperty() throws Exception {
		assertThat(new SchemaAnnotation("Marca", "Text").type(), equalTo("Brand"));
	}
	
	@Test
	public void productProperty() throws Exception {
		assertThat(new SchemaAnnotation("Prodotto", "Memo").type(), equalTo("Product"));
	}
	
	@Test
	public void imageProperty() throws Exception {
		assertThat(new SchemaAnnotation("Immagine", "Text").type(), equalTo("URL"));
	}
	
	@Test
	public void linkProperty() throws Exception {
		assertThat(new SchemaAnnotation("Link", "Text").type(), equalTo("URL"));
	}
	
	@Test
	public void categoryProperty() throws Exception {
		assertThat(new SchemaAnnotation("Categoria", "Text").type(), equalTo("Category"));
	}
}
