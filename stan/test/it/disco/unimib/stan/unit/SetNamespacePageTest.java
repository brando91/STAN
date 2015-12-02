package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.webapp.SetNamespacePage;
import it.disco.unimib.stan.webapp.WorkingTable;

import org.junit.Before;
import org.junit.Test;

public class SetNamespacePageTest extends WebUnitTest{
	
	@Before
	public void setUp(){
		new AnnotationArea().clear();
	}
	
	@Test
	public void route() throws Exception {
		assertThat(new SetNamespacePage().route(), equalTo("set-namespace"));
	}
	
	@Test
	public void saveNamespace() throws Exception {
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", new VirtualResource().addLine("any")).toUpload();
		
		user.browseTo("/start")
			.upload("upload-table", "table", uploadedTable)
			.type("upload-table", "separator", "|")
			.clickOn("upload-table", "upload")
			.browseTo("set-namespace")
			.type("set-namespace", "namespace", "http://my/namespace/")
			.clickOn("set-namespace", "set");
		
		assertThat(new WorkingTable("table.csv", user.getCookie("user")).getNamespace(), equalTo("http://my/namespace/"));
	}
	
	@Test
	public void loadNamespace() throws Exception {
		FileResource uploadedTable = new UploadedFileTestDouble("mytable.csv", new VirtualResource().addLine("any")).toUpload();
		
		user.browseTo("/start")
				.upload("upload-table", "table", uploadedTable)
				.type("upload-table", "separator", "|")
				.clickOn("upload-table", "upload")
				.browseTo("set-namespace")
				.type("set-namespace", "namespace", "http://my/namespace/")
				.clickOn("set-namespace", "set")
				.browseTo("annotation")
				.browseTo("set-namespace");
		
		assertThat(user.pageContent(), containsString("http://my/namespace/"));
	}

}
