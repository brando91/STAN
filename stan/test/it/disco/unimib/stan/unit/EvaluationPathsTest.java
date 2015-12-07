package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import it.disco.unimib.stan.core.EvaluationPaths;

import org.junit.Test;

public class EvaluationPathsTest {

	@Test
	public void root() throws Exception {
		assertThat(new EvaluationPaths().path(), equalTo("../evaluation"));
	}
	
	@Test
	public void indexes() throws Exception {
		assertThat(new EvaluationPaths().indexes().path(), equalTo("../evaluation/labeller-indexes"));
	}
	
	@Test
	public void file() throws Exception {
		assertThat(new EvaluationPaths().indexes().file("any.csv").path(), equalTo("../evaluation/labeller-indexes/any.csv"));
	}
	
	@Test
	public void goldStandards() throws Exception {
		assertThat(new EvaluationPaths().goldStandard("dbpedia-enhanced").path(), equalTo("../evaluation/gold-standards/dbpedia-enhanced"));
	}
	
	@Test
	public void results() throws Exception {
		assertThat(new EvaluationPaths().results("dbpedia-enhanced").path(), equalTo("../evaluation/results/dbpedia-enhanced"));
	}
	
	@Test
	public void ranges() throws Exception {
		assertThat(new EvaluationPaths().ranges("dbpedia").path(), equalTo("../evaluation/dbpedia-ranges"));
	}
	
	@Test
	public void domains() throws Exception {
		assertThat(new EvaluationPaths().domains("dbpedia").path(), equalTo("../evaluation/dbpedia-domains"));
	}
}
