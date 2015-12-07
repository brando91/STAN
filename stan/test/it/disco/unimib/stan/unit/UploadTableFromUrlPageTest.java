package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.WorkingAreaPaths;
import it.disco.unimib.stan.webapp.UploadTableFromUrlPage;
import it.disco.unimib.stan.webapp.WorkingTable;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class UploadTableFromUrlPageTest {

	@Before
	public void setUp(){
		new AnnotationArea().clear();
	}
	
	@Test
	public void route() throws Exception {
		assertThat(new UploadTableFromUrlPage().route(), equalTo("upload-table-url"));
	}
	
	@Test
	public void saveTableFromUrl() throws Exception {
		CommunicationTestDouble request = new CommunicationTestDouble()
																.withParameter("url", "https://data.cityofchicago.org/api/views/kn9c-c2s2/rows.csv?accessType=DOWNLOAD")
																.withParameter("separator", "|")
																.setCookie("user", "username");
		new UploadTableFromUrlPage().process(request);
		
		String downloadedTable = "Census_Data_-_Selected_socioeconomic_indicators_in_Chicago__2008___2012.csv";
		assertThat(new File(new WorkingAreaPaths().tables("username").file(downloadedTable).path()).exists(), equalTo(true));
		assertThat(new WorkingTable(downloadedTable, "username").getSeparator(), equalTo("|"));
	}
}
