package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.AverageTime;
import it.disco.unimib.stan.core.CSVTable;
import it.disco.unimib.stan.core.Column;
import it.disco.unimib.stan.core.ElapsedTime;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.LogEvents;
import it.disco.unimib.stan.core.StanAnnotator;
import it.disco.unimib.stan.webapp.Context;

import java.util.ArrayList;

public class RunChicagoStanAnnotation {

	public static void main(String[] args) throws Exception{
		int values = Integer.parseInt(args[0]);
		
		new LogEvents().info("EXPERIMENTS STARTED");
		String path = new DataPaths().chicago().chicagoDataset().path();
		ArrayList<FileResource> resources = new FileResource(path).listResources();

		StanAnnotator contextualizedAnnotator = new StanAnnotator("dbpedia").withContext();
		
		AverageTime averageTime = new AverageTime();
		ElapsedTime elapsedTime = new ElapsedTime();
		
		for(FileResource resource : resources){
			new LogEvents().info("Started evaluation on " + resource.name() + " table");
			ArrayList<Column> columns = new CSVTable(resource).withSeparator(",").columns();
			for(Column column : columns){
				new LogEvents().info("Started evaluation on " + column.id() + " column");
				elapsedTime.start();
				contextualizedAnnotator.annotate(new Context(fixed(column.id())), limit(column.members(), values));
				elapsedTime.stop();
				averageTime.add(elapsedTime.delta());
			}
		}
		new LogEvents().info("EXPERIMENTS COMPLETED");
		new LogEvents().info("TIME ANNOTATING COLUMNS: " + averageTime.average());
	}

	private static String fixed(String table) {
		if(table.contains("census_block")) return "Place";	
		if(table.contains("census_data")) return "Place";
		if(table.contains("crimes")) return "Crime";
		if(table.contains("schools")) return "School";
		
		return "";
	}
	
	private static ArrayList<String> limit(ArrayList<String> members, int limit) {
		return new ReservoirSampling<String>().getAtMostDistinctSamples(limit, members);
	}
}
