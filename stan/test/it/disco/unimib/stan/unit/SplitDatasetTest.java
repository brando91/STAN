package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.TrainingDataPath;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.experiments.GoldStandardEntry;
import it.disco.unimib.stan.experiments.SchemaIni;
import it.disco.unimib.stan.experiments.SplitDataset;
import it.disco.unimib.stan.experiments.TrainingColumn;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class SplitDatasetTest {
	
	@Before
	public void setUp() {
		new AnnotationArea().clear();
	}
	
	@Test
	public void splitSingleColumn() throws Exception {
		ArrayList<GoldStandardEntry> goldStandard = new ArrayList<GoldStandardEntry>();
		goldStandard.add(new GoldStandardEntry("eshop-col1", "prezzo", 1));
		VirtualResource table = new VirtualResource()
												.withName("eshop")
												.addLine("value1")
												.addLine("value2");
		
		TrainingDataPath testPath = new TrainingDataPath().test();
		new SplitDataset(goldStandard, new SchemaIni(new VirtualResource())).split(testPath, table);
		FileResource resource = new FileResource(testPath.column("prezzo").file("eshop-col1").path());
		
		assertThat(new TrainingColumn(resource).parseColumn().members(), contains("value1","value2"));
	}	
	
	@Test
	public void splitTwoColumns() throws Exception {
		ArrayList<GoldStandardEntry> goldStandard = new ArrayList<GoldStandardEntry>();
		goldStandard.add(new GoldStandardEntry("eshop-col1", "prezzo", 1));
		goldStandard.add(new GoldStandardEntry("eshop-col2", "sku", 1));
		VirtualResource table = new VirtualResource()
												.withName("eshop")
												.addLine("a1|a2")
												.addLine("b1|b2");
		
		TrainingDataPath testPath = new TrainingDataPath().test();
		new SplitDataset(goldStandard, new SchemaIni(new VirtualResource())).split(testPath, table);
		FileResource pricePath = new FileResource(testPath.column("prezzo").file("eshop-col1").path());
		FileResource skuPath = new FileResource(testPath.column("sku").file("eshop-col2").path());
		
		assertThat(new TrainingColumn(pricePath).parseColumn().members(), contains("a1","b1"));
		assertThat(new TrainingColumn(skuPath).parseColumn().members(), contains("a2","b2"));
	}
	
	@Test
	public void splitTwoTables() throws Exception {
		ArrayList<GoldStandardEntry> goldStandard = new ArrayList<GoldStandardEntry>();
		goldStandard.add(new GoldStandardEntry("eshop-col1", "prezzo", 1));
		goldStandard.add(new GoldStandardEntry("pipposhop-col1", "prezzo", 1));
		VirtualResource table1 = new VirtualResource()
												.withName("eshop")
												.addLine("eshopValue");
		VirtualResource table2 = new VirtualResource()
												.withName("pipposhop")
												.addLine("pippoValue");
		
		TrainingDataPath testPath = new TrainingDataPath().test();
		new SplitDataset(goldStandard, new SchemaIni(new VirtualResource())).split(testPath, table1, table2);
		FileResource eshopPath = new FileResource(testPath.column("prezzo").file("eshop-col1").path());
		FileResource pipposhopPath = new FileResource(testPath.column("prezzo").file("pipposhop-col1").path());
		
		assertThat(new TrainingColumn(eshopPath).parseColumn().members(), contains("eshopValue"));
		assertThat(new TrainingColumn(pipposhopPath).parseColumn().members(), contains("pippoValue"));
	}
	
	@Test
	public void tableWithHeader() throws Exception {
		ArrayList<GoldStandardEntry> goldStandard = new ArrayList<GoldStandardEntry>();
		goldStandard.add(new GoldStandardEntry("eshop-col1", "prezzo", 1));
		SchemaIni schemaIni = new SchemaIni(new VirtualResource()
														.addLine("[eshop]")
														.addLine("ColNameHeader=True"));
		VirtualResource table = new VirtualResource()
												.withName("eshop")
												.addLine("header")
												.addLine("eshopValue");
		
		TrainingDataPath testPath = new TrainingDataPath().test();
		new SplitDataset(goldStandard, schemaIni).split(testPath, table);
		FileResource eshopPath = new FileResource(testPath.column("prezzo").file("eshop-col1").path());
		
		assertThat(new TrainingColumn(eshopPath).parseColumn().hasHeader(), equalTo(true));
		assertThat(new TrainingColumn(eshopPath).parseColumn().header(), equalTo("header"));
	}
	
	@Test
	public void tableWithoutHeader() throws Exception {
		ArrayList<GoldStandardEntry> goldStandard = new ArrayList<GoldStandardEntry>();
		goldStandard.add(new GoldStandardEntry("eshop-col1", "prezzo", 1));
		VirtualResource table1 = new VirtualResource()
												.withName("eshop")
												.addLine("eshopValue");
		
		TrainingDataPath testPath = new TrainingDataPath().test();
		new SplitDataset(goldStandard, new SchemaIni(new VirtualResource())).split(testPath, table1);
		FileResource eshopPath = new FileResource(testPath.column("prezzo").file("eshop-col1").path());
		
		assertThat(new TrainingColumn(eshopPath).parseColumn().hasHeader(), equalTo(false));
		assertThat(new TrainingColumn(eshopPath).parseColumn().header(), equalTo("NO HEADER"));
	}
	
	@Test
	public void numericTable() throws Exception {
		ArrayList<GoldStandardEntry> goldStandard = new ArrayList<GoldStandardEntry>();
		goldStandard.add(new GoldStandardEntry("eshop-col1", "prezzo", 1));
		VirtualResource table = new VirtualResource()
												.withName("eshop")
												.addLine("1")
												.addLine("5");
		
		TrainingDataPath testPath = new TrainingDataPath().test();
		new SplitDataset(goldStandard, new SchemaIni(new VirtualResource())).split(testPath, table);
		FileResource path = new FileResource(testPath.column("prezzo").file("eshop-col1").path());
		
		assertThat(new TrainingColumn(path).parseColumn().numericMembers(), contains(1.0, 5.0));
	}
	
	@Test
	public void saveInCorrectPlace() throws Exception {
		ArrayList<GoldStandardEntry> goldStandard = new ArrayList<GoldStandardEntry>();
		goldStandard.add(new GoldStandardEntry("eshop.csv-col1", "category", 1));
		VirtualResource table = new VirtualResource()
												.withName("eshop.csv")
												.addLine("any");
		
		TrainingDataPath testPath = new TrainingDataPath().test();
		new SplitDataset(goldStandard, new SchemaIni(new VirtualResource())).split(testPath, table);
		String path = testPath.column("category").file("eshop.csv-col1").path();
		
		assertThat(new File(path).exists(), equalTo(true));
	}

}
