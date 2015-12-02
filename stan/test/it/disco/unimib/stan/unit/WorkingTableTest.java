package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.WorkingAreaPaths;
import it.disco.unimib.stan.webapp.WorkingTable;

import org.junit.Before;
import org.junit.Test;

public class WorkingTableTest {
	
	@Before
	public void setUp(){
		new AnnotationArea().clear();
	}

	@Test
	public void save() throws Exception {
		new WorkingTable("table.csv", "username").save();
		
		FileResource saved = new FileResource(new WorkingAreaPaths().schemas("username").file("table.csv.txt").path());
		
		assertThat(saved.asFile().exists(), equalTo(true));
		assertThat(saved.lines(), contains("tableName=table.csv"));
		assertThat(new WorkingTable("table.csv", "username").getTableName(), equalTo("table.csv"));
	}
	
	@Test
	public void emptyWorkingTable() throws Exception {
		assertThat(new WorkingTable("not existing", "any").getTableName(), equalTo(""));
	}
	
	@Test
	public void override() throws Exception {
		new WorkingTable("old.csv", "username").save();
		assertThat(new WorkingTable("old.csv", "username").getTableName(), equalTo("old.csv"));
		
		new WorkingTable("new.csv", "username").save();
		assertThat(new WorkingTable("new.csv", "username").getTableName(), equalTo("new.csv"));
	}
	
	@Test
	public void multiUser() throws Exception {
		new WorkingTable("table.csv", "user1").withSeparator("$").save();
		new WorkingTable("table.csv", "user2").withSeparator("€").save();
		
		assertThat(new WorkingTable("table.csv", "user1").getSeparator(), equalTo("$"));
		assertThat(new WorkingTable("table.csv", "user2").getSeparator(), equalTo("€"));
	}
	
	@Test
	public void getCurrentSeparator() throws Exception {
		new WorkingTable("table.csv", "username").withSeparator("|").save();
		
		assertThat(new WorkingTable("table.csv", "username").getSeparator(), equalTo("|"));
	}
	
	@Test
	public void getCurrentDelimiter() throws Exception {
		new WorkingTable("table.csv", "username").withDelimiter("\"").save();
		
		assertThat(new WorkingTable("table.csv", "username").getDelimiter(), equalTo("\""));
	}
	
	@Test
	public void getCurrentHeader() throws Exception {
		new WorkingTable("table.csv", "username").save();
		assertThat(new WorkingTable("table.csv", "username").hasHeader(), equalTo(false));
		
		new WorkingTable("table.csv", "username").withHeader(true).save();
		assertThat(new WorkingTable("table.csv", "username").hasHeader(), equalTo(true));
	}
	
	@Test
	public void getList() throws Exception {
		new WorkingTable("old.csv", "username").save();
		new WorkingTable("new.csv", "username").save();
		
		assertThat(new WorkingTable("new.csv", "username").getTableName(), equalTo("new.csv"));
	}
	
	@Test
	public void saveAnnotation() throws Exception {
		new WorkingTable("table.csv", "username")
						  .save()
						  .saveAnnotation("1", "http://property", "property", "anyType", "type1")
						  .saveAnnotation("2", "http://property2", "property2", "anyType", "type2");
		
		assertThat(new WorkingTable("table.csv", "username").annotationForColumn(1).uri(), equalTo("http://property"));
		assertThat(new WorkingTable("table.csv", "username").annotationForColumn(1).label(), equalTo("property"));
		assertThat(new WorkingTable("table.csv", "username").annotationForColumn(1).propertyType(), equalTo("type1"));
		assertThat(new WorkingTable("table.csv", "username").annotationForColumn(2).uri(), equalTo("http://property2"));
		assertThat(new WorkingTable("table.csv", "username").annotationForColumn(2).label(), equalTo("property2"));
		assertThat(new WorkingTable("table.csv", "username").annotationForColumn(2).propertyType(), equalTo("type2"));
	}
	
	@Test
	public void overrideAnnotation() throws Exception {
		new WorkingTable("table.csv", "username")
						  .save()
						  .saveAnnotation("1", "http://property", "property", "anyType", "any")
						  .saveAnnotation("1", "http://property2", "property2", "anyType", "any");
		
		assertThat(new WorkingTable("table.csv", "username").annotationForColumn(1).uri(), equalTo("http://property2"));
		assertThat(new WorkingTable("table.csv", "username").annotationForColumn(1).label(), equalTo("property2"));
	}
}
