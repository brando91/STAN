package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import it.disco.unimib.stan.experiments.EvaluationPaths;

import org.junit.Test;

public class EvaluationPathsTest {

	@Test
	public void evaluationDirectory() {
		String evaluationPath = new EvaluationPaths().path();
		
		assertThat(evaluationPath, equalTo("../evaluation"));
	}
	
	@Test
	public void goldStandardDirectory() {
		String goldStandardPath = new EvaluationPaths()
								.goldStandards()
								.path();
		
		assertThat(goldStandardPath, equalTo("../evaluation/gold-standards"));
	}
	
	@Test
	public void resultsDirectory() {
		String resultsPath = new EvaluationPaths()
								.results()
								.path();
		
		assertThat(resultsPath, equalTo("../evaluation/results"));
	}
	
	@Test
	public void shouldReturnAFileInDIrectory() {
		String resultsPath = new EvaluationPaths()
								.goldStandards()
								.file("gold.qrels")
								.path();
		
		assertThat(resultsPath, equalTo("../evaluation/gold-standards/gold.qrels"));
	}

}
