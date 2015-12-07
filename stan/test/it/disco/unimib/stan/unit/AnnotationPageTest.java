package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.core.WorkingAreaPaths;
import it.disco.unimib.stan.webapp.AnnotationPage;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

public class AnnotationPageTest extends WebUnitTest{

	@Before
	public void setUp() throws IOException{
		new AnnotationArea().clear();
	}
	
	@Test
	public void route() throws Exception {
		assertThat(new AnnotationPage().route(), equalTo("annotation"));
	}
	
	@Test
	public void noTableUploaded() throws Exception {
		assertThat(new AnnotationPage().process(new CommunicationTestDouble()), containsString("Missing table"));
	}
	
	@Test
	public void successfullyUploaded() throws Exception {
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", new VirtualResource().addLine("any")).toUpload();
		
		user.browseTo("/start")
			.upload("upload-table", "table", uploadedTable)
			.type("upload-table", "separator", "|")
			.clickOn("upload-table", "upload")
			.waitForElementToShow("any");
		
		assertThat(user.pageContent(), containsString("table.csv successfully uploaded"));
		assertThat(user.pageContent(), containsString("any"));
		assertThat(new File(new WorkingAreaPaths().tables(user.getCookie("user")).file("table.csv").path()).exists(), equalTo(true));
	}
	
	@Test
	public void tableWithHeader() throws Exception {
		VirtualResource table = new VirtualResource().addLine("header line");
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", table).toUpload();
		
		user.browseTo("/start")
				.upload("upload-table", "table", uploadedTable)
				.type("upload-table", "separator", "|")
				.check("upload-table", "header")
				.clickOn("upload-table", "upload");
		
		assertThat(user.getElementTextById("header-1"), equalTo("header line"));
	}
	
	@Test
	public void annotateColumnWithDatatypeProperty() throws Exception {
		VirtualResource table = new VirtualResource().addLine("Apple");
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", table).toUpload();
		
		user.browseTo("/start")
				.upload("upload-table", "table", uploadedTable)
				.type("upload-table", "separator", "|")
				.clickOn("upload-table", "upload");
		assertThat(user.getElementTextById("header-1"), equalTo("-"));
		
		user.setHiddenInputValue("knowledgeBase", "test")
			.clickOn("header-1")
			.type("annotation-form", "annotation", "Marca")
			.type("annotation-form", "object-type", "xsd:string")
			.setHiddenInputValue("typeSet", "datatype")
			.clickOn("annotation-form", "save-annotation")
			.waitForElementToShow("Marca");
		
		assertThat(user.getElementTextById("header-1"), equalTo("Marca"));
		assertThat(user.getElementAttributeById("header-1", "object"), equalTo("xsd:string"));
		assertThat(user.getElementAttributeById("header-1", "property-type"), equalTo("datatype"));
	}
	
	@Test
	public void annotateColumnWithObjectProperty() throws Exception {
		VirtualResource table = new VirtualResource().addLine("Apple");
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", table).toUpload();
		
		user.browseTo("/start")
				.upload("upload-table", "table", uploadedTable)
				.type("upload-table", "separator", "|")
				.clickOn("upload-table", "upload");
		assertThat(user.getElementTextById("header-1"), equalTo("-"));
		
		user.setHiddenInputValue("knowledgeBase", "test")
			.clickOn("header-1")
			.type("annotation-form", "annotation", "http://hasBanana")
			.type("annotation-form", "object-type", "http://Banana")
			.setHiddenInputValue("typeSet", "concept")
			.clickOn("annotation-form", "save-annotation")
			.waitForElementToShow("hasBanana");
		
		assertThat(user.getElementTextById("header-1"), equalTo("hasBanana"));
		assertThat(user.getElementAttributeById("header-1", "object"), equalTo("http://Banana"));
		assertThat(user.getElementAttributeById("header-1", "property-type"), equalTo("concept"));
	}
	
	@Test
	public void canReturnToAnnotation() throws Exception {
		VirtualResource table = new VirtualResource().addLine("header line");
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", table).toUpload();
		
		user.browseTo("/start")
			.upload("upload-table", "table", uploadedTable)
			.type("upload-table", "separator", "|")
			.check("upload-table", "header")
			.clickOn("upload-table", "upload")
			.browseTo("/ontologies")
			.browseTo("/annotation");
		
		assertThat(user.pageContent(), containsString("header line"));
	}
	
	@Test
	public void tableCookieEncripted() throws Exception {
		VirtualResource table = new VirtualResource().addLine("header line");
		FileResource uploadedTable = new UploadedFileTestDouble("cookie-table.csv", table).toUpload();
		
		user.browseTo("/start")
			.upload("upload-table", "table", uploadedTable)
			.type("upload-table", "separator", "|")
			.check("upload-table", "header")
			.clickOn("upload-table", "upload");
		
		assertThat(decrypt(user.getCookie("table")), equalTo("cookie-table.csv"));
	}
	
	@Test
	public void notOverrideUserCookie() throws Exception {
		VirtualResource table = new VirtualResource().addLine("header line");
		FileResource uploadedTable1 = new UploadedFileTestDouble("table1.csv", table).toUpload();
		FileResource uploadedTable2 = new UploadedFileTestDouble("table2.csv", table).toUpload();
		
		user.browseTo("/start")
			.upload("upload-table", "table", uploadedTable1)
			.type("upload-table", "separator", "|")
			.check("upload-table", "header")
			.clickOn("upload-table", "upload");
		
		String userCookie = user.getCookie("user");
		
		user.browseTo("/start")
			.upload("upload-table", "table", uploadedTable2)
			.type("upload-table", "separator", "|")
			.check("upload-table", "header")
			.clickOn("upload-table", "upload");
		
		assertThat(user.getCookie("user"), equalTo(userCookie));
	}
	
	@Test
	public void showSetSubject() throws Exception {
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
			.clickOn("header-1");
		
		assertThat(user.getElementTextById("subject-type"), equalTo(""));
		
		user.clickOn("subject")
			.check("set-subject-form", "isSubject")
			.type("set-subject-form", "type", "http://Type")
			.clickOn("set-subject-form", "set-subject")
			.clickOn("header-2");
		
		assertThat(user.getElementTextById("subject-type"), equalTo("http://Type"));
	}
	
	@Test
	public void autocompleteSubjectTypesSuggestion() throws Exception {
		ClientCommunication request = new ClientCommunication("http://abstat.disco.unimib.it");
		assertThat(request.getStatusCode("/api/v1/autocomplete/concepts?dataset=dbpedia-3.9-infobox&q=cit"), equalTo(200));
	}
	
	@Test
	public void autocompletePropertiesSuggestion() throws Exception {
		ClientCommunication request = new ClientCommunication("http://abstat.disco.unimib.it");
		assertThat(request.getStatusCode("/api/v1/autocomplete/properties?dataset=dbpedia-3.9-infobox&q=birt"), equalTo(200));
	}

	private String decrypt(String cookie) {
		return new String(Base64.decodeBase64(cookie));
	}
}
