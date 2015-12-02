package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.IndexesPath;
import it.disco.unimib.stan.core.KarmaNumericAnnotator;
import it.disco.unimib.stan.core.KarmaSearch;
import it.disco.unimib.stan.core.KarmaTextualAnnotator;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.experiments.PrepareKarmaIndex;
import it.disco.unimib.stan.experiments.TrainingColumn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class PrepareKarmaIndexTest {
	
	String numericIndexTestPath = new IndexesPath().numericTest().path();
	String indexesTestPath = new IndexesPath().test().path();
	KarmaTextualAnnotator textualAnnotator;
	KarmaNumericAnnotator numericAnnotator;
	
	@Before
	public void setUp() throws Exception{
		new AnnotationArea().clear();
		textualAnnotator = new KarmaTextualAnnotator().indexPath(indexesTestPath);
		numericAnnotator = new KarmaNumericAnnotator().indexPath(numericIndexTestPath);
	}
	
	@Test
	public void emptyIndex() throws Exception {
		startTraining();
		new PrepareKarmaIndex().indexColumn(textualAnnotator, numericAnnotator, trainingSet(new TrainingColumn(new VirtualResource())));
		textualAnnotator.train("");
		endTraining();
		
		assertThat(new KarmaSearch(new File(numericIndexTestPath)).getNumericDocuments().size(), equalTo(0));
	}

	@Test
	public void oneColumn() throws Exception {
		VirtualResource table = new VirtualResource()
										.addLine("NO HEADER")
										.addLine("column-value");

		startTraining();
		new PrepareKarmaIndex().indexColumn(textualAnnotator, numericAnnotator, trainingSet(new TrainingColumn(table)));
		textualAnnotator.train("true-label");
		endTraining();
		
		KarmaSearch index = new KarmaSearch(new File(indexesTestPath));
		
		assertThat(index.getContentsForLabel("true-label"), contains("column-value"));
	}
	
	@Test
	public void moreDocuments() throws Exception {
		VirtualResource categoryColumn = new VirtualResource().addLine("NO HEADER").addLine("cellulari");
		VirtualResource descriptionColumn = new VirtualResource().addLine("NO HEADER").addLine("descrizione");

		startTraining();
		new PrepareKarmaIndex().indexColumn(textualAnnotator, numericAnnotator, trainingSet(new TrainingColumn(categoryColumn)));
		textualAnnotator.train("categoria");
		new PrepareKarmaIndex().indexColumn(textualAnnotator, numericAnnotator, trainingSet(new TrainingColumn(descriptionColumn)));
		textualAnnotator.train("descrizione");
		endTraining();
		
		KarmaSearch index = new KarmaSearch(new File(indexesTestPath));
		
		assertThat(index.getContentsForLabel("categoria"), contains("cellulari"));
		assertThat(index.getContentsForLabel("descrizione"), contains("descrizione"));
	}
	
	@Test
	public void indexOnlyOneTextualDocument() throws Exception {
		VirtualResource categoryColumn1 = new VirtualResource().addLine("NO HEADER").addLine("cellulari");
		VirtualResource categoryColumn2 = new VirtualResource().addLine("NO HEADER").addLine("tablet");

		startTraining();
		new PrepareKarmaIndex().indexColumn(textualAnnotator, 
											numericAnnotator, 
											trainingSet(new TrainingColumn(categoryColumn1), new TrainingColumn(categoryColumn2)));
		textualAnnotator.train("categoria");
		endTraining();
		
		KarmaSearch index = new KarmaSearch(new File(indexesTestPath));
		
		assertThat(index.getNumDocs(), equalTo(1));
		assertThat(index.getContentsForLabel("categoria"), contains("cellulari", "tablet"));
	}
	
	@Test
	public void indexOnlyOneNumericDocument() throws Exception {
		VirtualResource priceColumn1 = new VirtualResource().addLine("NO HEADER").addLine("1");
		VirtualResource priceColumn2 = new VirtualResource().addLine("NO HEADER").addLine("2");

		startTraining();
		new PrepareKarmaIndex().indexColumn(textualAnnotator, 
											numericAnnotator, 
											trainingSet(new TrainingColumn(priceColumn1), new TrainingColumn(priceColumn2)));
		numericAnnotator.train("prezzo");
		endTraining();
		
		KarmaSearch index = new KarmaSearch(new File(numericIndexTestPath));
		assertThat(index.getNumDocs(), equalTo(1));
		assertThat(index.getNumericDocuments().get("prezzo"), contains(1.0, 2.0));
	}
	
	@Test
	public void dontIndexHeader() throws Exception {
		VirtualResource table = new VirtualResource()
											.addLine("header")
											.addLine("column-value");
		
		startTraining();
		new PrepareKarmaIndex().indexColumn(textualAnnotator, numericAnnotator, trainingSet(new TrainingColumn(table)));
		textualAnnotator.train("true-label");
		endTraining();
		
		KarmaSearch index = new KarmaSearch(new File(indexesTestPath));
		
		assertThat(index.getContentsForLabel("true-label"), not(contains("header")));
	}
	
	private ArrayList<TrainingColumn> trainingSet(TrainingColumn... columns) {
		ArrayList<TrainingColumn> training = new ArrayList<TrainingColumn>();
		for(TrainingColumn column : columns){
			training.add(column);
		}
		return training;
	}
	
	private void endTraining() throws IOException {
		textualAnnotator.endTraining();
		numericAnnotator.endTraining();
	}

	private void startTraining() throws IOException {
		textualAnnotator.startTraining();
		numericAnnotator.startTraining();
	}
}
