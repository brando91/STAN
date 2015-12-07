package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.core.WorkingAreaPaths;
import it.disco.unimib.stan.webapp.ExportTriplesPage;
import it.disco.unimib.stan.webapp.MappingsReader;

import java.io.File;

import org.junit.Test;

public class ExportTriplesPageTest extends WebUnitTest{

	@Test
	public void route() throws Exception {
		assertThat(new ExportTriplesPage().route(), equalTo("export/triples"));
	}
	
	@Test
	public void triplesExport() throws Exception {
		VirtualResource resource = new VirtualResource()
													.addLine("subjectHeader|objectHeader")
													.addLine("subject|object");
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", resource).toUpload();
		user.browseTo("/start")
			.upload("upload-table", "table", uploadedTable)
			.type("upload-table", "separator", "|")
			.check("upload-table", "header")
			.clickOn("upload-table", "upload")
			.setHiddenInputValue("knowledgeBase", "test")
			.clickOn("header-1")
			.clickOn("subject")
			.check("set-subject-form", "isSubject")
			.type("set-subject-form", "type", "http://anyType")
			.clickOn("set-subject-form", "set-subject")
			.clickOn("export-triples")
			.waitForElementToShow("Export completed");
		
		String userCookie = user.getCookie("user");
		FileResource mappingsFile = new FileResource(new WorkingAreaPaths().mappings(userCookie).file("table.csv_mappings.ttl").path());
		String tablePath = new WorkingAreaPaths().tables(userCookie).file("table.csv").path();
		MappingsReader mappings = new MappingsReader(mappingsFile);
		
		assertThat(mappings.hasSourceLocation(new File(tablePath).getAbsolutePath()), equalTo(true));
	}
}
