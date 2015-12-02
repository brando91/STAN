package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.stan.core.SemanticAnnotation;
import it.disco.unimib.stan.experiments.EvaluationPaths;
import it.disco.unimib.stan.experiments.TrecResult;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TrecResultTest {
	
	@Before
	public void setUp(){
		new AnnotationArea().clear();
	}
	
	@Test
	public void emptyResult() throws Exception {
		assertThat(new TrecResult("any").asTRECResults().size(), equalTo(0));
	}
	
	@Test
	public void oneResult() throws Exception {
		ArrayList<SemanticAnnotation> predictions = new ArrayList<SemanticAnnotation>();
		predictions.add(new SemanticAnnotation("label", 3.0));
		ArrayList<String> trecResults = new TrecResult("algo")
							.addPrediction("id", predictions)
							.asTRECResults();
		
		assertThat(trecResults.size(), equalTo(1));
		assertThat(trecResults.get(0), equalTo("id Q0 label 1 3.0 algo"));
	}
	
	@Test
	public void twoResults() throws Exception {
		ArrayList<SemanticAnnotation> predictions = new ArrayList<SemanticAnnotation>();
		predictions.add(new SemanticAnnotation("label1", 3.0));
		predictions.add(new SemanticAnnotation("label2", 2.5));
		ArrayList<String> trecResults = new TrecResult("algo")
							.addPrediction("id", predictions)
							.asTRECResults();
		
		assertThat(trecResults.get(0), equalTo("id Q0 label1 1 3.0 algo"));
		assertThat(trecResults.get(1), equalTo("id Q0 label2 2 2.5 algo"));
	}
	
	@Test
	public void moreIds() throws Exception {
		ArrayList<SemanticAnnotation> predictions = new ArrayList<SemanticAnnotation>();
		predictions.add(new SemanticAnnotation("label1", 3.0));
		predictions.add(new SemanticAnnotation("label2", 2.5));
		ArrayList<String> trecResults = new TrecResult("algo")
							.addPrediction("id1", predictions)
							.addPrediction("id2", predictions)
							.asTRECResults();
		
		assertThat(trecResults.get(0), equalTo("id1 Q0 label1 1 3.0 algo"));
		assertThat(trecResults.get(1), equalTo("id1 Q0 label2 2 2.5 algo"));
		assertThat(trecResults.get(2), equalTo("id2 Q0 label1 1 3.0 algo"));
		assertThat(trecResults.get(3), equalTo("id2 Q0 label2 2 2.5 algo"));
	}
	
	@Test
	public void saveResults() throws Exception {
		ArrayList<SemanticAnnotation> predictions = new ArrayList<SemanticAnnotation>();
		predictions.add(new SemanticAnnotation("label", 3.0));
		new TrecResult("algo")
					.addPrediction("id", predictions)
					.save("test", "dataset", "type", "size");
		
		String path = new EvaluationPaths().results().folder("test").file("dataset-type-size.qrels").path();
		assertThat(new File(path).exists(), equalTo(true));
	}
	
	@Test
	public void emptyCandidatesCountsAsEmpty() throws Exception {
		ArrayList<CandidateProperty> predictions = new ArrayList<CandidateProperty>();
		ArrayList<String> trecResults = new TrecResult("algo")
							.addPrediction("id", predictions)
							.asTRECResults();
		
		assertThat(trecResults.size(), equalTo(1));
		assertThat(trecResults.get(0), equalTo("id Q0 null 1 0.0 algo"));
	}
}
