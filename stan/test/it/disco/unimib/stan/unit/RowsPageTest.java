package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.webapp.RowsPage;
import it.disco.unimib.stan.webapp.UploadTablePage;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

public class RowsPageTest {

	@Before
	public void setUp(){
		new AnnotationArea().clear();
	}
	
	@Test
	public void route() throws Exception {
		assertThat(new RowsPage().route(), equalTo("annotation/rows"));
	}
	
	@Test
	public void noTableUploaded() throws Exception {
		new UploadTablePage().process(new CommunicationTestDouble());
		
		assertThat(new RowsPage().process(new CommunicationTestDouble()), equalTo("{\"total\":\"0\",\"rows\":[]}"));
	}
	
	@Test
	public void limit() throws Exception {
		VirtualResource table = new VirtualResource().addLine("line1")
														   .addLine("line2");
		new UploadTablePage().process(new CommunicationTestDouble()
														.withUploadedResource("table", new UploadedFileTestDouble("limit.csv", table))
														.withParameter("separator", "|")
														.setCookie("user", "username"));
		
		CommunicationTestDouble request = new CommunicationTestDouble()
														.withParameter("limit", "1")
														.withParameter("offset", "0")
														.setCookie("user", "username")
														.setCookie("table", encrypt("limit.csv"));
		String rows = new RowsPage().process(request);
		
		assertThat(rows, containsString("\"total\":\"2\""));
		assertThat(rows, containsString("line1"));
		assertThat(rows, not(containsString("line2")));
	}
	
	@Test
	public void lowerBoundLimit() throws Exception {
		VirtualResource table = new VirtualResource().addLine("line1")
														   .addLine("line2");
		new UploadTablePage().process(new CommunicationTestDouble()
													.withUploadedResource("table", new UploadedFileTestDouble("limit.csv", table))
													.withParameter("separator", "|")
													.setCookie("user", "username"));
		
		CommunicationTestDouble request = new CommunicationTestDouble()
													.withParameter("limit", "1")
													.withParameter("offset", "0")
													.setCookie("user", "username")
													.setCookie("table", encrypt("limit.csv"));
		String rows = new RowsPage().process(request);
		
		assertThat(rows, containsString("line1"));
		assertThat(rows, not(containsString("line2")));
	}
	
	@Test
	public void overLimit() throws Exception {
		VirtualResource table = new VirtualResource().addLine("line1")
														   .addLine("line2")
														   .addLine("line3");
		new UploadTablePage().process(new CommunicationTestDouble()
													.withUploadedResource("table", new UploadedFileTestDouble("limit.csv", table))
													.withParameter("separator", "|")
													.setCookie("user", "username"));
		
		CommunicationTestDouble request = new CommunicationTestDouble()
													.withParameter("limit", "2")
													.withParameter("offset", "2")
													.setCookie("user", "username")
													.setCookie("table", encrypt("limit.csv"));
		String rows = new RowsPage().process(request);
		
		assertThat(rows, not(containsString("line1")));
		assertThat(rows, not(containsString("line2")));
		assertThat(rows, containsString("line3"));
	}
	
	@Test
	public void offset() throws Exception {
		VirtualResource table = new VirtualResource().addLine("line1")
														   .addLine("line2");
		new UploadTablePage().process(new CommunicationTestDouble()
											.withUploadedResource("table", new UploadedFileTestDouble("offset.csv", table))
											.withParameter("separator", "|")
											.setCookie("user", "username"));
		
		CommunicationTestDouble request = new CommunicationTestDouble()
											.withParameter("limit", "1")
											.withParameter("offset", "1")
											.setCookie("user", "username")
											.setCookie("table", encrypt("offset.csv"));
		String rows = new RowsPage().process(request);
		
		assertThat(rows, containsString("\"total\":\"2\""));
		assertThat(rows, containsString("line2"));
		assertThat(rows, not(containsString("line1")));
	}
	
	@Test
	public void tableWithSeparator() throws Exception {
		VirtualResource table = new VirtualResource().addLine("first,second");
		new UploadTablePage().process(new CommunicationTestDouble()
													.withUploadedResource("table", new UploadedFileTestDouble("table.csv", table))
													.withParameter("separator", ",")
													.setCookie("user", "username"));
		
		CommunicationTestDouble request = new CommunicationTestDouble()
													.withParameter("limit", "10")
													.withParameter("offset", "0")
													.setCookie("user", "username")
													.setCookie("table", encrypt("table.csv"));
		String rows = new RowsPage().process(request);
		
		assertThat(rows, not(containsString("first,second")));
	}
	
	@Test
	public void tableWithPipeSeparator() throws Exception {
		VirtualResource table = new VirtualResource().addLine("first|second");
		new UploadTablePage().process(new CommunicationTestDouble()
													.withUploadedResource("table", new UploadedFileTestDouble("table.csv", table))
													.withParameter("separator", "|")
													.setCookie("user", "username"));
		
		CommunicationTestDouble request = new CommunicationTestDouble()
													.withParameter("limit", "10")
													.withParameter("offset", "0")
													.setCookie("user", "username")
													.setCookie("table", encrypt("table.csv"));
		String rows = new RowsPage().process(request);
		
		assertThat(rows, not(containsString("first|second")));
	}
	
	@Test
	public void tableWithTextDelimiter() throws Exception {
		VirtualResource table = new VirtualResource().addLine("first|\"second1|second2\"");
		new UploadTablePage().process(new CommunicationTestDouble()
													.withUploadedResource("table", new UploadedFileTestDouble("table.csv", table))
													.withParameter("separator", "|")
													.withParameter("delimiter", "\"")
													.setCookie("user", "username"));
		
		CommunicationTestDouble request = new CommunicationTestDouble()
													.withParameter("limit", "10")
													.withParameter("offset", "0")
													.setCookie("user", "username")
													.setCookie("table", encrypt("table.csv"));
		String rows = new RowsPage().process(request);
		
		assertThat(rows, containsString("second1|second2"));
	}
	
	@Test
	public void tableWithTextDelimiterBracket() throws Exception {
		VirtualResource table = new VirtualResource().addLine("first|%second1|second2%");
		new UploadTablePage().process(new CommunicationTestDouble()
													.withUploadedResource("table", new UploadedFileTestDouble("table.csv", table))
													.withParameter("separator", "|")
													.withParameter("delimiter", "%")
													.setCookie("user", "username"));
		
		CommunicationTestDouble request = new CommunicationTestDouble()
													.withParameter("limit", "10")
													.withParameter("offset", "0")
													.setCookie("table", encrypt("table.csv"))
													.setCookie("user", "username");
		String rows = new RowsPage().process(request);
		
		assertThat(rows, containsString("second1|second2"));
		assertThat(rows, not(containsString("first|second1|second2")));
	}
	
	@Test
	public void tableWithHeader() throws Exception {
		VirtualResource table = new VirtualResource()
												.addLine("header")
												.addLine("firstline");
		
		new UploadTablePage().process(new CommunicationTestDouble()
													.withUploadedResource("table", new UploadedFileTestDouble("table.csv", table))
													.withParameter("separator", "|")
													.withCheckBoxEnabled("header")
													.setCookie("user", "username"));
		
		CommunicationTestDouble request = new CommunicationTestDouble()
													.withParameter("limit", "10")
													.withParameter("offset", "0")
													.setCookie("user", "username")
													.setCookie("table", encrypt("table.csv"));
		String rows = new RowsPage().process(request);
		
		assertThat(rows, containsString("\"total\":\"1\""));
		assertThat(rows, containsString("firstline"));
		assertThat(rows, not(containsString("header")));
	}
	
	private String encrypt(String value) {
		return new String(Base64.encodeBase64String(value.getBytes()));
	}
}
