package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.LogEvents;
import it.disco.unimib.stan.core.TrainingDataPath;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class SplitEcommerceDataset {

	public static void main(String[] args) throws Exception {

		ArrayList<GoldStandardEntry> goldStandardEntries = new GoldStandard("ecommerce-gold-standard").writtenEntries();
		SchemaIni schemaIni = new SchemaIni(new FileResource(new DataPaths().ecommerce().file("schema.ini").path()));
		TrainingDataPath where = new TrainingDataPath().ecommerce();
		ArrayList<FileResource> resources = new FileResource(new DataPaths().ecommerce().listings().path()).listResources();
		FileResource[] tables = resources.toArray(new FileResource[resources.size()]);
		
		FileUtils.deleteQuietly(new File(where.path()));
		
		new LogEvents().info("STARTED DATASET SPLITTING");
		new SplitDataset(goldStandardEntries, schemaIni).split(where, tables);
		new LogEvents().info("DATASET SPLITTING COMPLETED");
	}

}
