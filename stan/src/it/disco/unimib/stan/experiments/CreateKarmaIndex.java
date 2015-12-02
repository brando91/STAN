package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.IndexesPath;
import it.disco.unimib.stan.core.LogEvents;
import it.disco.unimib.stan.core.TrainingDataPath;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class CreateKarmaIndex {

	public static void main(String[] args) throws Exception {
		String domain = "ecommerce";
		
		String indexPath = new IndexesPath().karma(domain).path();
		String numericIndexPath = new IndexesPath().karmaNumeric(domain).path();
		FileUtils.deleteQuietly(new File(indexPath));
		FileUtils.deleteQuietly(new File(numericIndexPath));
		
		new LogEvents().info("STARTED INDEXING");
		new PrepareKarmaIndex().buildIndex(indexPath, numericIndexPath, new TrainingDataPath().ecommerce());
		new LogEvents().info("INDEXING SUCCESFULLY COMPLETED");
	}
}
