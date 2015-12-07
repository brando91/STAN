package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.webapp.SaveStatusPage;
import it.disco.unimib.stan.webapp.WorkingTable;

import org.junit.Before;
import org.junit.Test;

public class SaveStatusPageTest extends WebUnitTest{

	@Before
	public void setUp(){
		new AnnotationArea().clear();
	}
	
	@Test
	public void route() throws Exception {
		assertThat(new SaveStatusPage().route(), equalTo("save-status"));
	}
	
	@Test
	public void saveOneColumnn() throws Exception {
		VirtualResource table = new VirtualResource().addLine("any");
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", table).toUpload();
		
		user.browseTo("/start")
				.upload("upload-table", "table", uploadedTable)
				.type("upload-table", "separator", "|")
				.clickOn("upload-table", "upload")
				.setHiddenInputValue("knowledgeBase", "test")
				.clickOn("header-1")
				.type("annotation-form", "annotation", "Marca")
				.type("annotation-form", "object-type", "xsd:string")
				.setHiddenInputValue("typeSet", "datatype")
				.clickOn("annotation-form", "save-annotation")
				.clickOn("save");
		
		assertThat(new WorkingTable("table.csv", user.getCookie("user")).annotationForColumn(1).label(), equalTo("Marca"));
		assertThat(new WorkingTable("table.csv", user.getCookie("user")).annotationForColumn(1).object(), equalTo("xsd:string"));
		assertThat(new WorkingTable("table.csv", user.getCookie("user")).annotationForColumn(1).propertyType(), equalTo("datatype"));
	}
	
	@Test
	public void readFromPreviousDatatypePropertyAnnotation() throws Exception {
		VirtualResource table = new VirtualResource().addLine("any");
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", table).toUpload();
		
		user.browseTo("/start")
				.upload("upload-table", "table", uploadedTable)
				.type("upload-table", "separator", "|")
				.clickOn("upload-table", "upload");
		
		new WorkingTable("table.csv", user.getCookie("user")).saveAnnotation("1", "http://uri", "label", "xsd:integer", "datatypeProperty");
		
		user.browseTo("/annotation");
		
		assertThat(user.getElementTextById("header-1"), equalTo("label"));
		assertThat(user.getElementAttributeById("header-1", "title"), equalTo("http://uri"));
		assertThat(user.getElementAttributeById("header-1", "object"), equalTo("xsd:integer"));
		assertThat(user.getElementAttributeById("header-1", "property-type"), equalTo("datatypeProperty"));
	}
	
	@Test
	public void readFromPreviousObjectPropertyAnnotation() throws Exception {
		VirtualResource table = new VirtualResource().addLine("any");
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", table).toUpload();
		
		user.browseTo("/start")
				.upload("upload-table", "table", uploadedTable)
				.type("upload-table", "separator", "|")
				.clickOn("upload-table", "upload");
		
		new WorkingTable("table.csv", user.getCookie("user")).saveAnnotation("1", "http://objectUri", "label", "http://Concept", "objectProperty");
		
		user.browseTo("/annotation");
		
		assertThat(user.getElementTextById("header-1"), equalTo("label"));
		assertThat(user.getElementAttributeById("header-1", "title"), equalTo("http://objectUri"));
		assertThat(user.getElementAttributeById("header-1", "object"), equalTo("http://Concept"));
		assertThat(user.getElementAttributeById("header-1", "property-type"), equalTo("objectProperty"));
	}
	
	@Test
	public void setSubject() throws Exception {
		VirtualResource table = new VirtualResource().addLine("column1|column2");
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", table).toUpload();
		
		user.browseTo("/start")
				.upload("upload-table", "table", uploadedTable)
				.type("upload-table", "separator", "|")
				.clickOn("upload-table", "upload")
				.setHiddenInputValue("knowledgeBase", "test")
				.clickOn("header-1")
				.clickOn("subject")
				.check("set-subject-form", "isSubject")
				.type("set-subject-form", "type", "http://anyType")
				.clickOn("set-subject-form", "set-subject")
				.clickOn("save");
		
		WorkingTable currentTable = new WorkingTable("table.csv", user.getCookie("user"));
		assertThat(currentTable.annotationForColumn(1).classSet(), equalTo("subject"));
		assertThat(currentTable.annotationForColumn(1).uri(), equalTo("http://anyType"));
		assertThat(currentTable.annotationForColumn(2).classSet(), not(equalTo("subject")));
	}
	
	@Test
	public void readFromPreviousSubjectSet() throws Exception {
		VirtualResource table = new VirtualResource().addLine("column1|subjectColumn");
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", table).toUpload();
		
		user.browseTo("/start")
				.upload("upload-table", "table", uploadedTable)
				.type("upload-table", "separator", "|")
				.clickOn("upload-table", "upload")
				.clickOn("save");
		
		new WorkingTable("table.csv", user.getCookie("user"))
								.saveAnnotation("2", "http://Person", "Person", "anyType", "")
								.saveSubject("2");
		
		user.browseTo("/annotation");
		
		assertThat(user.getElementAttributeById("header-2", "class"), containsString("subject"));
		assertThat(user.getElementAttributeById("header-2", "title"), equalTo("http://Person"));
		assertThat(user.getElementTextById("header-2"), equalTo("Person"));
	}
	
	@Test
	public void annotationTypes() throws Exception {
		VirtualResource table = new VirtualResource().addLine("column1|column2|column3");
		FileResource uploadedTable = new UploadedFileTestDouble("table.csv", table).toUpload();
		
		user.browseTo("/start")
				.upload("upload-table", "table", uploadedTable)
				.type("upload-table", "separator", "|")
				.clickOn("upload-table", "upload")
				.clickOn("save");
		
		new WorkingTable("table.csv", user.getCookie("user"))
								.saveAnnotation("2", "http://label", "label", "xsd:string", "datatype")
								.saveAnnotation("3", "http://cityOfBirth", "cityOfBirth", "http://City", "concept");
		
		user.browseTo("/annotation");
		
		assertThat(user.getElementAttributeById("header-1", "class"), not(containsString("datatype")));
		assertThat(user.getElementAttributeById("header-1", "class"), not(containsString("concept0")));
		assertThat(user.getElementAttributeById("header-2", "class"), containsString("datatype"));
		assertThat(user.getElementAttributeById("header-3", "class"), containsString("concept"));
	}
}
