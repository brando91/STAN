package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.Column;

import java.util.ArrayList;

import org.junit.Test;

public class ColumnTest {

	@Test
	public void allText() throws Exception {
		ArrayList<String> members = columnMembers("a", "b", "c");
		
		Column column = new Column("any").withAllMembers(members);
		
		assertThat(column.isNumeric(), equalTo(false));
		assertThat(column.isTextual(), equalTo(true));
	}
	
	@Test
	public void allNumbers() throws Exception {
		ArrayList<String> members = columnMembers("1", "2", "3");
		
		Column column = new Column("any").withAllMembers(members);
		
		assertThat(column.isNumeric(), equalTo(true));
		assertThat(column.isTextual(), equalTo(false));
	}
	
	@Test
	public void lessThanSixtyPercent() throws Exception {
		ArrayList<String> members = columnMembers("1", "2", "3", "4", "5", "a", "b", "c", "d", "e");
		
		Column column = new Column("any").withAllMembers(members);
		
		assertThat(column.isNumeric(), equalTo(false));
		assertThat(column.isTextual(), equalTo(true));
	}
	
	@Test
	public void sixtyPercent() throws Exception {
		ArrayList<String> members = columnMembers("1", "2", "3", "4", "5", "6", "b", "c", "d", "e");
		
		Column column = new Column("any").withAllMembers(members);
		
		assertThat(column.isNumeric(), equalTo(true));
		assertThat(column.isTextual(), equalTo(true));
	}
	
	@Test
	public void eightyPercent() throws Exception {
		ArrayList<String> members = columnMembers("1", "2", "3", "4", "5", "6", "7", "8", "d", "e");
		
		Column column = new Column("any").withAllMembers(members);
		
		assertThat(column.isNumeric(), equalTo(true));
		assertThat(column.isTextual(), equalTo(false));
	}
	
	@Test
	public void queryTime70Percent() throws Exception {
		ArrayList<String> members = columnMembers("1", "2", "3", "4", "5", "6", "7", "c", "d", "e");
		
		Column column = new Column("any").withAllMembers(members);
		
		assertThat(column.isNumericAtQueryTime(), equalTo(true));
	}
	
	@Test
	public void queryTime60Percent() throws Exception {
		ArrayList<String> members = columnMembers("1", "2", "3", "4", "5", "6", "b", "c", "d", "e");
		
		Column column = new Column("any").withAllMembers(members);
		
		assertThat(column.isNumericAtQueryTime(), equalTo(false));
	}

	private ArrayList<String> columnMembers(String... members) {
		ArrayList<String> m = new ArrayList<String>();
		for(String member : members){
			m.add(member);
		}
		return m;
	}
}
