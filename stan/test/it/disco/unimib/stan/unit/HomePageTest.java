package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.webapp.HomePage;

import org.junit.Test;

public class HomePageTest extends WebUnitTest{

	@Test
	public void route() throws Exception {
		assertThat(new HomePage().route(), equalTo(""));
	}
	
	@Test
	public void showsPreviousWork() throws Exception {
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", new VirtualResource().addLine("any")).toUpload();
		
		user.browseTo("");
		
		assertThat(user.pageContent(), not(containsString("Resume")));
		assertThat(user.pageContent(), not(containsString("table.csv")));
		
		user.browseTo("/start")
			.upload("upload-table", "table", uploadedTable)
			.type("upload-table", "separator", "|")
			.clickOn("upload-table", "upload")
			.browseTo("");
		
		assertThat(user.pageContent(), containsString("table.csv"));
	}
}
