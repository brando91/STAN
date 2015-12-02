package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.stan.core.IndexesPath;
import it.disco.unimib.stan.core.Resource;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.experiments.PrepareStanIndex;
import it.disco.unimib.stan.experiments.SchemaIni;
import it.disco.unimib.stan.experiments.StanIndexReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.store.NIOFSDirectory;
import org.junit.Before;
import org.junit.Test;

public class PrepareStanIndexTest {

	@Before
	public void setUp(){
		new AnnotationArea().clear();
	}
	
	@Test
	public void emptyIndex() throws Exception {
		ArrayList<Resource> trainingSet = new ArrayList<Resource>();
		SchemaIni schemaIni = new SchemaIni(new VirtualResource());
		schemaIni.parse();
		
		new PrepareStanIndex().buildIndex(trainingSet, indexDirectory(), schemaIni);
		
		assertThat(new StanIndexReader(indexDirectory()).countDocuments(), equalTo(0));
	}
	
	@Test
	public void oneDocument() throws Exception {
		ArrayList<Resource> trainingSet = new ArrayList<Resource>();
		trainingSet.add(new VirtualResource().addLine("header")
											 .addLine("any")
											 .withName("tableName.txt"));	
		SchemaIni schemaIni = new SchemaIni(new VirtualResource()
													.addLine("[tableName.txt]")
													.addLine("ColNameHeader=True")
													.addLine("Col1=Annotation text"));
		schemaIni.parse();
		
		new PrepareStanIndex().buildIndex(trainingSet, indexDirectory(), schemaIni);
		
		assertThat(new StanIndexReader(indexDirectory()).countDocuments(), equalTo(1));
	}
	
	@Test
	public void twoDocuments() throws Exception {
		ArrayList<Resource> trainingSet = new ArrayList<Resource>();
		trainingSet.add(new VirtualResource().addLine("header")
											 .addLine("first")
											 .addLine("second")
											 .withName("tableName.txt"));	
		SchemaIni schemaIni = new SchemaIni(new VirtualResource()
													.addLine("[tableName.txt]")
													.addLine("ColNameHeader=True")
													.addLine("Col1=Annotation text"));
		schemaIni.parse();
		
		new PrepareStanIndex().buildIndex(trainingSet, indexDirectory(), schemaIni);
		
		assertThat(new StanIndexReader(indexDirectory()).countDocuments(), equalTo(2));
	}
	
	@Test
	public void correctFields() throws Exception {
		ArrayList<Resource> trainingSet = new ArrayList<Resource>();
		trainingSet.add(new VirtualResource().addLine("header")
											 .addLine("value")
											 .withName("tableName.txt"));	
		SchemaIni schemaIni = new SchemaIni(new VirtualResource()
													.addLine("[tableName.txt]")
													.addLine("ColNameHeader=True")
													.addLine("Col1=Annotation text"));
		schemaIni.parse();
		
		new PrepareStanIndex().buildIndex(trainingSet, indexDirectory(), schemaIni);
		
		IndexFields fields = new IndexFields("ecommerce");
		Document document = new StanIndexReader(indexDirectory()).getDocumentByLabel("annotation");
		assertThat(document.get(fields.label()), equalTo("annotation"));
		assertThat(document.get(fields.literal()), equalTo("value"));
		assertThat(document.get(fields.context()), equalTo("header"));
		assertThat(document.get(fields.objectType()), equalTo("text"));
		assertThat(document.get(fields.property()), equalTo("annotation"));
	}

	private NIOFSDirectory indexDirectory() throws IOException {
		return new NIOFSDirectory(new File(new IndexesPath().labellingTest().path()).toPath());
	}
}
