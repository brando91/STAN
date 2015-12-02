package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.TypeHierarchy;
import it.disco.unimib.labeller.unit.InputFileTestDouble;
import it.disco.unimib.labeller.unit.TripleBuilder;
import it.disco.unimib.stan.core.IndexesPath;
import it.disco.unimib.stan.core.Json;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.junit.Test;

public class AnnotationApiPageTest extends WebUnitTest{

	@Test
	public void emptyRequest() throws Exception {
		user.makePOST(new POSTBuilder("api/annotate"));
		
		Json jsonResponse = new Json().deserialize(user.pageJSON());
		
		assertThat(jsonResponse.getJsonArray("results").size(), equalTo(0));
	}
	
	@Test
	public void responseWithUriLabelAndScore() throws Exception {
		buildIndex();
		ArrayList<String> values = new ArrayList<String>();
		values.add("value");
		values.add("another_value");
		Json json = new Json()
						.parameters("values", values)
						.parameter("kb", "test")
						.parameter("context", "context");
		
		user.makePOST(new POSTBuilder("api/annotate").withData(json));
		
		String pageContent = user.pageJSON();
		Json jsonResponse = new Json().deserialize(pageContent);
		
		Json candidate = new Json().deserialize(jsonResponse.getJsonArray("results").get(0).serialize());
		assertThat(candidate.get("uri"), not(isEmptyOrNullString()));
		assertThat(candidate.get("label"), not(isEmptyOrNullString()));
		assertThat(candidate.get("score"), not(isEmptyOrNullString()));
	}
		
	@Test
	public void withoutContextShouldNotReturnResults() throws Exception {
		buildIndex();
		ArrayList<String> values = new ArrayList<String>();
		values.add("value");
		values.add("another_value");
		Json json = new Json()
						.parameters("values", values)
						.parameter("kb", "test");
		
		user.makePOST(new POSTBuilder("api/annotate").withData(json));
		
		Json jsonResponse = new Json().deserialize(user.pageJSON());
		
		assertThat(jsonResponse.getJsonArray("results").size(), equalTo(0));
	}
	
	private void buildIndex() throws Exception {
		File dirPath = new File(new IndexesPath().labellingTest().path());
		FileUtils.forceMkdir(dirPath);
		Directory directory = new NIOFSDirectory(dirPath.toPath());
		EntityValues types = new EntityValues(directory).add(new TripleBuilder().withSubject("http://a_subject")
																				.withObject("http://context")
																				.asTriple())
																	.closeWriter();
		
		EntityValues labels = new EntityValues(directory).add(new TripleBuilder().withSubject("http://context")
																				.withLiteral("context")
																				.asTriple())
																	.closeWriter();
		new Evidence(directory, 
					new TypeHierarchy(new InputFileTestDouble()),
					types,
					labels,
					new IndexFields("dbpedia"))
										.add(new TripleBuilder().withSubject("http://a_subject")
																.withProperty("http://predicate")
																.withLiteral("value").asTriple())
										.add(new TripleBuilder().withSubject("http://a_subject_without_context")
																.withProperty("http://predicate")
																.withLiteral("another_value").asTriple())
										.closeWriter();
	}
}
