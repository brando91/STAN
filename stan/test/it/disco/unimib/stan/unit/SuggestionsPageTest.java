package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.TypeHierarchy;
import it.disco.unimib.labeller.unit.InputFileTestDouble;
import it.disco.unimib.labeller.unit.TripleBuilder;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.IndexesPath;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.webapp.SuggestionsPage;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.junit.Before;
import org.junit.Test;

public class SuggestionsPageTest extends WebUnitTest{

	@Before
	public void setUp(){
		new AnnotationArea().clear();
	}
	
	@Test
	public void route() throws Exception {
		assertThat(new SuggestionsPage().route(), equalTo("suggestions"));
	}
	
	@Test
	public void getSuggestions() throws Exception {
		buildIndex();
		VirtualResource table = new VirtualResource().addLine("cellulari")
													 .addLine("frigoriferi");
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", table).toUpload();
		
		POSTBuilder post = new POSTBuilder("suggestions")
											.withParameter("kb", "test")
											.withParameter("column", "1")
											.withParameter("context", "context");
		
		user.browseTo("/start")
				.upload("upload-table", "table", uploadedTable)
				.type("upload-table", "separator", "|")
				.clickOn("upload-table", "upload")
				.makePOST(post);

		assertThat(user.pageJSON(), containsString("categoria"));
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
																.withProperty("http://categoria")
																.withLiteral("cellulari")
																.asTriple())
										.add(new TripleBuilder().withSubject("http://a_subject_without_context")
																.withProperty("http://categoria")
																.withLiteral("frigoriferi")
																.asTriple())
										.closeWriter();
	}
}
