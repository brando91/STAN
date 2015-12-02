package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.IndexesPath;
import it.disco.unimib.stan.core.LogEvents;
import it.disco.unimib.stan.core.Resource;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.store.NIOFSDirectory;

public class CreateStanIndex {

	public static void main(String[] args) throws Exception{
		File indexDirectory = new File(new IndexesPath().labelling("ecommerce").path());
		FileUtils.deleteQuietly(indexDirectory);
		SchemaIni schemaIni = new SchemaIni(new FileResource(new DataPaths().ecommerce().file("schema.ini").path()));
		schemaIni.parse();
		
		new LogEvents().info("STARTED INDEXING FOR STAN");
		new PrepareStanIndex().buildIndex(listings(), new NIOFSDirectory(indexDirectory.toPath()), schemaIni);
		new LogEvents().info("INDEXING SUCCESSFULLY COMPLETED");
	}
	
	private static ArrayList<Resource> listings() {
		String listingsPath = new DataPaths().ecommerce().listings().path();
		ArrayList<Resource> listings = new ArrayList<Resource>();
		for(FileResource resource : new FileResource(listingsPath).listResources()){
			listings.add(resource);
		}
		return listings;
	}
}
