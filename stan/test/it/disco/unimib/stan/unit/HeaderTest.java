package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.Column;
import it.disco.unimib.stan.core.Header;
import it.disco.unimib.stan.webapp.WorkingTable;

import java.util.ArrayList;

import org.junit.Test;

public class HeaderTest {

	@Test
	public void emptyHeader() throws Exception {
		assertThat(new Header(new ArrayList<Column>()).asOriginalList().size(), equalTo(0));
	}
	
	@Test
	public void oneColumnHeader() throws Exception {
		ArrayList<Column> columns = new ArrayList<Column>();
		columns.add(new Column("id").withHeader("Product"));
		
		assertThat(new Header(columns).asOriginalList().size(), equalTo(1));
		assertThat(new Header(columns).asOriginalList().get(0), equalTo("Product"));
	}
	
	@Test
	public void twoColumnsHeader() throws Exception {
		ArrayList<Column> columns = new ArrayList<Column>();
		columns.add(new Column("id-1").withHeader("Product"));
		columns.add(new Column("id-2").withHeader("Brand"));
		
		assertThat(new Header(columns).asOriginalList().size(), equalTo(2));
		assertThat(new Header(columns).asOriginalList().get(0), equalTo("Product"));
		assertThat(new Header(columns).asOriginalList().get(1), equalTo("Brand"));
	}
	
	@Test
	public void noHeaderColumnIsShownAsMinus() throws Exception {
		ArrayList<Column> columns = new ArrayList<Column>();
		columns.add(new Column("id"));
		
		assertThat(new Header(columns).asOriginalList().get(0), equalTo("-"));
	}
	
	@Test
	public void annotatedHeader() throws Exception {
		ArrayList<Column> columns = new ArrayList<Column>();
		columns.add(new Column("id").withHeader("Name"));
		Header header = new Header(columns);

		assertThat(header.asOriginalList().get(0), equalTo("Name"));
		
		WorkingTable currentTable = new WorkingTable("table.csv", "username");
		currentTable.save()
				  	.saveAnnotation("1", "http://foaf-name", "foaf-name", "any", "any");
		
		assertThat(header.asAnnotatedList(currentTable).get(0).label(), equalTo("foaf-name"));
	}
	
	@Test
	public void rightPosition() throws Exception {
		ArrayList<Column> columns = new ArrayList<Column>();
		columns.add(new Column("id-0").withHeader("Name"));
		columns.add(new Column("id-1").withHeader("Age"));
		
		assertThat(new Header(columns).inPosition(1), equalTo("Name"));
		assertThat(new Header(columns).inPosition(2), equalTo("Age"));
	}
}
