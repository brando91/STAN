package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.CSVTable;
import it.disco.unimib.stan.core.Column;
import it.disco.unimib.stan.core.Row;
import it.disco.unimib.stan.core.VirtualResource;

import java.util.ArrayList;

import org.junit.Test;

public class CSVTableTest {

	@Test
	public void emptyTable() throws Exception {
		assertThat(new CSVTable(new VirtualResource()).columns().size(), equalTo(0));
	}
	
	@Test
	public void tableWithOneColumnAndOneMember() throws Exception {
		ArrayList<Column> columns = new CSVTable(new VirtualResource().addLine("lonely-member")).columns();
		
		assertThat(columns.size(), equalTo(1));
		assertThat(columns.get(0).members(), hasItem("lonely-member"));
	}
	
	@Test
	public void tableWithOneColumnAndMoreMembers() throws Exception {
		VirtualResource csv = new VirtualResource()
									.addLine("member1")
									.addLine("member2")
									.addLine("member3");
		ArrayList<Column> columns = new CSVTable(csv).columns();
		
		assertThat(columns.get(0).members().size(), equalTo(3));
		assertThat(columns.get(0).members(), hasItem("member1"));
		assertThat(columns.get(0).members(), hasItem("member2"));
		assertThat(columns.get(0).members(), hasItem("member3"));
	}
	
	@Test
	public void tableWithTwoColumns() throws Exception {
		VirtualResource csv = new VirtualResource()
									.addLine("column1|column2");
		ArrayList<Column> columns = new CSVTable(csv).columns();
		
		assertThat(columns.get(0).members(), hasItem("column1"));
		assertThat(columns.get(1).members(), hasItem("column2"));
	}
	
	@Test
	public void columnIdShouldBeComposedByFileNameAndColumnNumber() throws Exception {
		VirtualResource csv = new VirtualResource()
									.withName("table")
									.addLine("any1|any2");
		ArrayList<Column> columns = new CSVTable(csv).columns();
		
		assertThat(columns.get(0).id(), equalTo("table-col1"));
		assertThat(columns.get(1).id(), equalTo("table-col2"));
	}
	
	@Test
	public void tableWithHeader() throws Exception {
		VirtualResource csv = new VirtualResource()
									.addLine("header")
									.addLine("value");
		CSVTable table = new CSVTable(csv).withHeader();
		
		assertThat(table.columns().get(0).members(), not(hasItem("header")));
		assertThat(table.columns().get(0).header(), equalTo("header"));
	}
	
	@Test
	public void tableWithHeadeWithDifferentNumberOfFields() throws Exception {
		VirtualResource csv = new VirtualResource()
									.addLine("header1|header2")
									.addLine("value1|value2|");
		CSVTable table = new CSVTable(csv).withHeader();
		
		assertThat(table.columns().size(), equalTo(2));
	}
	
	@Test
	public void considerEvenEmptyColumns() throws Exception {
		VirtualResource csv = new VirtualResource()
									.addLine("a|b|c|");
		CSVTable table = new CSVTable(csv);
		
		assertThat(table.columns().size(), equalTo(4));
	}
	
	@Test
	public void listingWithEmptyLine() throws Exception {
		VirtualResource csv = new VirtualResource()
										.addLine("a")
										.addLine("")
										.addLine(" ")
										.addLine("b");
		CSVTable table = new CSVTable(csv);
		
		ArrayList<Column> columns = table.columns();
		assertThat(columns.get(0).members().size(), equalTo(2));
	}
	
	@Test
	public void columnIdShouldBeLowerCase() throws Exception {
		VirtualResource csv = new VirtualResource()
											.addLine("a")
											.withName("UPPER-Case");
		
		CSVTable table = new CSVTable(csv);
		assertThat(table.columns().get(0).id(), containsString("upper-case"));
	}
	
	@Test
	public void allNumericValues() throws Exception {
		VirtualResource csv = new VirtualResource().addLine("1");
		
		CSVTable table = new CSVTable(csv);
		assertThat(table.columns().get(0).isNumeric(), equalTo(true));
	}
	
	@Test
	public void allTextualValues() throws Exception {
		VirtualResource csv = new VirtualResource().addLine("value");
		
		CSVTable table = new CSVTable(csv);
		assertThat(table.columns().get(0).isNumeric(), equalTo(false));
	}
	
	@Test
	public void moreThanHalfAreNumericValues() throws Exception {
		VirtualResource csv = new VirtualResource()
											.addLine("1")
											.addLine("2")
											.addLine("v");
		
		CSVTable table = new CSVTable(csv);
		assertThat(table.columns().get(0).isNumeric(), equalTo(true));
	}
	
	@Test
	public void moreThanHalfAreTextualValues() throws Exception {
		VirtualResource csv = new VirtualResource()
											.addLine("1")
											.addLine("a")
											.addLine("v");
		
		CSVTable table = new CSVTable(csv);
		assertThat(table.columns().get(0).isNumeric(), equalTo(false));
	}
	
	@Test
	public void commaAndPointAreBothParsedAsNumbers() throws Exception {
		VirtualResource csv = new VirtualResource()
											.addLine("1.0")
											.addLine("1,0");
		
		CSVTable table = new CSVTable(csv);
		assertThat(table.columns().get(0).numericMembers().size(), equalTo(2));
	}
	
	@Test
	public void columnWithTextDelimiter() throws Exception {
		VirtualResource csv = new VirtualResource().addLine("\"a|b\"|c");
		CSVTable table = new CSVTable(csv).withTextDelimiter("\"");
		
		assertThat(table.columns().size(), equalTo(2));
		assertThat(table.columns().get(0).members().get(0), equalTo("a|b"));
		assertThat(table.columns().get(1).members().get(0), equalTo("c"));
	}
	
	@Test
	public void moreLines() throws Exception {
		VirtualResource csv = new VirtualResource()
									.addLine("member1")
									.addLine("member2")
									.addLine("member3");
		ArrayList<Row> rows = new CSVTable(csv).rows();
		
		assertThat(rows.size(), equalTo(3));
		assertThat(rows.get(0).cells().get(0), equalTo("member1"));
	}
	
	@Test
	public void moreColumns() throws Exception {
		VirtualResource csv = new VirtualResource()
									.addLine("member11|member12")
									.addLine("member21|member22");
		ArrayList<Row> rows = new CSVTable(csv).rows();
		
		assertThat(rows.size(), equalTo(2));
		assertThat(rows.get(0).cells().get(0), equalTo("member11"));
		assertThat(rows.get(0).cells().get(1), equalTo("member12"));
		assertThat(rows.get(1).cells().get(0), equalTo("member21"));
		assertThat(rows.get(1).cells().get(1), equalTo("member22"));
	}
	
	@Test
	public void emptyHeader() throws Exception {
		VirtualResource csv = new VirtualResource()
									.addLine("member11|member12")
									.addLine("member21|member22");
		ArrayList<String> header = new CSVTable(csv).getHeader().asOriginalList();
		
		assertThat(header.size(), equalTo(2));
		assertThat(header.get(0), equalTo("-"));
		assertThat(header.get(1), equalTo("-"));
	}
	
	@Test
	public void withHeader() throws Exception {
		VirtualResource csv = new VirtualResource()
									.addLine("header1|header2")
									.addLine("member21|member22");
		ArrayList<String> header = new CSVTable(csv)
											.withHeader()
											.getHeader()
												.asOriginalList();
		
		assertThat(header.size(), equalTo(2));
		assertThat(header.get(0), equalTo("header1"));
		assertThat(header.get(1), equalTo("header2"));
		
	}
	
	@Test
	public void rowsWithHeader() throws Exception {
		VirtualResource csv = new VirtualResource()
									.addLine("header1|header2")
									.addLine("member21|member22");
		ArrayList<Row> rows = new CSVTable(csv).withHeader().rows();
		
		assertThat(rows.size(), equalTo(1));
		assertThat(rows.get(0).cells().get(0), equalTo("member21"));
		
	}
}
