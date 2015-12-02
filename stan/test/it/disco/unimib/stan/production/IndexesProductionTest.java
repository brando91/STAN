package it.disco.unimib.stan.production;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.EvaluationResources;

import java.io.File;

import org.junit.Test;

public class IndexesProductionTest {
	
	@Test
	public void dbpediaIndex() throws Exception {
		assertThat(new File(new EvaluationResources().evaluationPath("../cluster-labelling/evaluation").indexPath("dbpedia")).listFiles().length, greaterThan(0));
	}
	
	@Test
	public void dbpediaDomains() throws Exception {
		assertThat(new File("../cluster-labelling/evaluation/dbpedia-domains").listFiles().length, greaterThan(0));
	}
	
	@Test
	public void dbpediaRanges() throws Exception {
		assertThat(new File("../cluster-labelling/evaluation/dbpedia-ranges").listFiles().length, greaterThan(0));
	}
	
	@Test
	public void depths() throws Exception {
		assertThat(new File("../cluster-labelling/evaluation/labeller-indexes/dbpedia/depths").listFiles().length, greaterThan(0));
	}
}
