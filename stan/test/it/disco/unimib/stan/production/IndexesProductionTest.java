package it.disco.unimib.stan.production;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.EvaluationResources;
import it.disco.unimib.stan.core.EvaluationPaths;

import java.io.File;

import org.junit.Test;

public class IndexesProductionTest {
	
	@Test
	public void dbpediaIndex() throws Exception {
		assertThat(new File(new EvaluationResources().indexPath("dbpedia")).listFiles().length, greaterThan(0));
	}
	
	@Test
	public void dbpediaDomains() throws Exception {
		assertThat(new File(new EvaluationPaths().domains("dbpedia").path()).listFiles().length, greaterThan(0));
	}
	
	@Test
	public void dbpediaRanges() throws Exception {
		assertThat(new File(new EvaluationPaths().ranges("dbpedia").path()).listFiles().length, greaterThan(0));
	}
	
	@Test
	public void depths() throws Exception {
		assertThat(new File(new EvaluationPaths().indexes().dbpedia().depths().path()).listFiles().length, greaterThan(0));
	}
}
