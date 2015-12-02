package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.IndexesPath;
import it.disco.unimib.stan.core.KarmaNumericAnnotator;
import it.disco.unimib.stan.core.KarmaSearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class KarmaNumericAnnotatorTest {
	
	private String indexPath;

	public KarmaNumericAnnotatorTest() throws Exception {
		new AnnotationArea().clear();
		this.indexPath = new IndexesPath().numericTest().path();
	}

	@Test
	public void trainLabel() throws Exception {
		ArrayList<Double> examples = new ArrayList<Double>();
		examples.add(2.0);
		new KarmaNumericAnnotator()
							.indexPath(indexPath)
							.startTraining()
							.newDocument()
							.updateDocument(examples)
							.train("label")
							.endTraining();
		
		assertThat(getStoredExamplesFor("label"), contains(2.0));
	}
	
	private ArrayList<Double> getStoredExamplesFor(String label) throws IOException {
		return new KarmaSearch(new File(indexPath)).getNumericDocuments().get(label);
	}
}
