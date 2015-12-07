package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.core.WorkingAreaPaths;
import it.disco.unimib.stan.webapp.ExportMappingsPage;
import it.disco.unimib.stan.webapp.MappingsReader;

import org.junit.Before;
import org.junit.Test;

public class ExportMappingsPageTest extends WebUnitTest{
	
	@Before
	public void setUp(){
		new AnnotationArea().clear();
	}

	@Test
	public void route() throws Exception {
		assertThat(new ExportMappingsPage().route(), equalTo("export/mappings"));
	}
	
	@Test
	public void emptyExport() throws Exception {
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
			.clickOn("export-mappings")
			.waitForElementToShow("Export completed");
		
		FileResource mappingsFile = new FileResource(new WorkingAreaPaths().mappings(user.getCookie("user")).file("table.csv_mappings.ttl").path());
		
		MappingsReader mappings = new MappingsReader(mappingsFile);
		assertThat(mappings.hasNamespaces(), equalTo(true));
		assertThat(mappings.hasSourceLocation("table.csv"), equalTo(true));
		assertThat(mappings.hasSubject("http://noNamespace/{subjectHeader}", "http://anyType"), equalTo(true));
	}
	
	@Test
	public void exportWithOneDatatypeProperty() throws Exception {
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
			.clickOn("header-2")
			.type("annotation-form", "annotation", "http://property")
			.type("annotation-form", "object-type", "xsd:string")
			.clickOn("annotation-form", "save-annotation")
			.clickOn("export-mappings")
			.waitForElementToShow("Export completed");
		
		FileResource mappingsFile = new FileResource(new WorkingAreaPaths().mappings(user.getCookie("user")).file("table.csv_mappings.ttl").path());
		
		MappingsReader mappings = new MappingsReader(mappingsFile);
		assertThat(mappings.hasSubject("http://noNamespace/{subjectHeader}", "http://anyType"), equalTo(true));
		assertThat(mappings.hasDatatypeProperty("http://property", "objectHeader", "xsd:string"), equalTo(true));
	}
	
	@Test
	public void exportWithOneObjectProperty() throws Exception {
		VirtualResource resource = new VirtualResource()
													.addLine("School|City")
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
			.type("set-subject-form", "type", "http://School")
			.clickOn("set-subject-form", "set-subject")
			.clickOn("header-2")
			.type("annotation-form", "annotation", "http://locatedIn")
			.type("annotation-form", "object-type", "http://City")
			.setHiddenInputValue("typeSet", "concept")
			.clickOn("annotation-form", "save-annotation")
			.clickOn("export-mappings")
			.waitForElementToShow("Export completed");
		
		FileResource mappingsFile = new FileResource(new WorkingAreaPaths().mappings(user.getCookie("user")).file("table.csv_mappings.ttl").path());
		
		MappingsReader mappings = new MappingsReader(mappingsFile);
		assertThat(mappings.hasSubject("http://noNamespace/{School}", "http://School"), equalTo(true));
		assertThat(mappings.hasObjectProperty("http://locatedIn", "locatedIn", "City", "http://City"), equalTo(true));
	}
	
	@Test
	public void exportWithTwoProperties() throws Exception {
		VirtualResource resource = new VirtualResource()
													.addLine("subjectHeader|objectHeader1|objectHeader2")
													.addLine("subject|object|object2");
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
			.clickOn("header-2")
			.type("annotation-form", "annotation", "http://property1")
			.type("annotation-form", "object-type", "xsd:string")
			.setHiddenInputValue("typeSet", "datatype")
			.clickOn("annotation-form", "save-annotation")
			.clickOn("header-3")
			.type("annotation-form", "annotation", "http://property2")
			.type("annotation-form", "object-type", "xsd:integer")
			.setHiddenInputValue("typeSet", "datatype")
			.clickOn("annotation-form", "save-annotation")
			.clickOn("export-mappings")
			.waitForElementToShow("Export completed");
		
		FileResource mappingsFile = new FileResource(new WorkingAreaPaths().mappings(user.getCookie("user")).file("table.csv_mappings.ttl").path());
		
		MappingsReader mappings = new MappingsReader(mappingsFile);
		assertThat(mappings.hasDatatypeProperty("http://property1", "objectHeader1", "xsd:string"), equalTo(true));
		assertThat(mappings.hasDatatypeProperty("http://property2", "objectHeader2", "xsd:integer"), equalTo(true));
	}
	
	@Test
	public void exportWithNamespace() throws Exception {
		VirtualResource resource = new VirtualResource()
													.addLine("subjectHeader|objectHeader")
													.addLine("subject|object");
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", resource).toUpload();
		user.browseTo("/start")
			.upload("upload-table", "table", uploadedTable)
			.type("upload-table", "separator", "|")
			.check("upload-table", "header")
			.clickOn("upload-table", "upload")
			.browseTo("set-namespace")
			.type("set-namespace", "namespace", "http://namespacebello/")
			.clickOn("set-namespace", "set")
			.browseTo("annotation")
			.setHiddenInputValue("knowledgeBase", "test")
			.clickOn("header-1")
			.clickOn("subject")
			.check("set-subject-form", "isSubject")
			.type("set-subject-form", "type", "http://anyType")
			.clickOn("set-subject-form", "set-subject")
			.clickOn("header-2")
			.type("annotation-form", "annotation", "http://property")
			.type("annotation-form", "object-type", "xsd:string")
			.clickOn("annotation-form", "save-annotation")
			.clickOn("export-mappings")
			.waitForElementToShow("Export completed");
		
		FileResource mappingsFile = new FileResource(new WorkingAreaPaths().mappings(user.getCookie("user")).file("table.csv_mappings.ttl").path());
		
		MappingsReader mappings = new MappingsReader(mappingsFile);
		assertThat(mappings.hasSubject("http://namespacebello/{subjectHeader}", "http://anyType"), equalTo(true));
		assertThat(mappings.hasDatatypeProperty("http://property", "objectHeader", "xsd:string"), equalTo(true));
	}
}
