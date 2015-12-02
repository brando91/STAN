package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.core.WorkingAreaPaths;
import it.disco.unimib.stan.webapp.UploadTablePage;
import it.disco.unimib.stan.webapp.WorkingTable;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class UploadTablePageTest {

	@Before
	public void setUp(){
		new AnnotationArea().clear();
	}
	
	@Test
	public void route() throws Exception {
		assertThat(new UploadTablePage().route(), equalTo("upload-table"));
	}
	
	@Test
	public void saveUploadedFile() throws Exception {
		CommunicationTestDouble request = new CommunicationTestDouble()
																.withUploadedResource("table", new UploadedFileTestDouble("table.csv", new VirtualResource()))
																.withParameter("separator", "|")
																.setCookie("user", "username");
		new UploadTablePage().process(request);
		
		assertThat(new File(new WorkingAreaPaths().tables("username").file("table.csv").path()).exists(), equalTo(true));
		assertThat(new WorkingTable("table.csv", "username").getTableName(), equalTo("table.csv"));
	}
}
