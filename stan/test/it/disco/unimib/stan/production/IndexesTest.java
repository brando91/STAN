package it.disco.unimib.stan.production;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.EvaluationPaths;
import it.disco.unimib.stan.core.EvaluationResources;

import java.io.File;

import org.junit.Test;

public class IndexesTest {
	
	@Test
	public void dbpediaIndex() throws Exception {
		assertThat(new File(new EvaluationResources().indexPath("dbpedia")).exists(), equalTo(true));
	}
	
	@Test
	public void dbpediaDomains() throws Exception {
		assertThat(new File(new EvaluationPaths().domains("dbpedia").path()).exists(), equalTo(true));
	}
	
	@Test
	public void dbpediaRanges() throws Exception {
		assertThat(new File(new EvaluationPaths().ranges("dbpedia").path()).exists(), equalTo(true));
	}
	
	@Test
	public void depths() throws Exception {
		assertThat(new File(new EvaluationPaths().indexes().dbpedia().depths().path()).exists(), equalTo(true));
	}
}