package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.IndexesPath;
import it.disco.unimib.stan.core.Json;
import it.disco.unimib.stan.core.KarmaNumericAnnotator;
import it.disco.unimib.stan.core.KarmaTextualAnnotator;
import it.disco.unimib.stan.webapp.KarmaAnnotationPage;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class KarmaAnnotationPageTest {
	
	KarmaTextualAnnotator textualAnnotator;
	private KarmaNumericAnnotator numericAnnotator;
	
	@Before
	public void setUp() throws Exception{
		new AnnotationArea().clear();
		textualAnnotator = new KarmaTextualAnnotator().indexPath(new IndexesPath().test().path());
		numericAnnotator = new KarmaNumericAnnotator().indexPath(new IndexesPath().numericTest().path());
	}
	
	@Test
	public void karmaRoute() throws Exception {
		assertThat(new KarmaAnnotationPage(textualAnnotator, numericAnnotator).route(), equalTo("karma-annotation"));
	}
	
	@Test
	public void emptyColumns() throws Exception {
		CommunicationTestDouble communication = new CommunicationTestDouble();
		
		assertThat(new KarmaAnnotationPage(textualAnnotator, numericAnnotator).process(communication), containsString("Missing columns"));
		assertThat(communication.responseStatus(), equalTo(400));
	}
	
	@Test
	public void emptyDataset() throws Exception {
		CommunicationTestDouble communication = new CommunicationTestDouble()
													.withParameter("columns", "column data");
		
		assertThat(new KarmaAnnotationPage(textualAnnotator, numericAnnotator).process(communication), containsString("Missing dataset"));
		assertThat(communication.responseStatus(), equalTo(400));
	}
	
	@Test
	public void karmaTextualPrediction() throws Exception {
		ArrayList<String> examples = new ArrayList<String>();
		examples.add("example");
		textualAnnotator.startTraining();
		textualAnnotator.newDocument();
		textualAnnotator.updateDocument(examples);
		textualAnnotator.train("prediction");
		textualAnnotator.endTraining();
		numericAnnotator.startTraining()
						.endTraining();
		String json = new Json().parameters("column", examples).serialize();
		
		CommunicationTestDouble communication = new CommunicationTestDouble()
													.withParameter("columns", json)
													.withParameter("dataset", "test");
		
		assertThat(new KarmaAnnotationPage(textualAnnotator, numericAnnotator).process(communication), containsString("prediction"));
	}
	
	@Test
	public void karmaNumericPrediction() throws Exception {
		ArrayList<Double> examples = new ArrayList<Double>();
		examples.add(6.0);
		examples.add(8.0);
		numericAnnotator.startTraining()
						.newDocument()
						.updateDocument(examples)
						.train("label")
						.endTraining();
		textualAnnotator.startTraining();
		textualAnnotator.endTraining();
		
		String json = new Json().numericParameters("column", examples).serialize();
		
		CommunicationTestDouble communication = new CommunicationTestDouble()
													.withParameter("columns", json)
													.withParameter("dataset", "test");
		
		assertThat(new KarmaAnnotationPage(textualAnnotator, numericAnnotator).process(communication), containsString("label"));
	}
}
