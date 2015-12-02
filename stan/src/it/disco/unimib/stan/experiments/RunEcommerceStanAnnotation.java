package it.disco.unimib.stan.experiments;

import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.stan.core.AverageTime;
import it.disco.unimib.stan.core.CSVTable;
import it.disco.unimib.stan.core.Column;
import it.disco.unimib.stan.core.ElapsedTime;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.IndexesPath;
import it.disco.unimib.stan.core.LogEvents;
import it.disco.unimib.stan.core.Resource;
import it.disco.unimib.stan.core.StanAnnotator;
import it.disco.unimib.stan.webapp.Context;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.store.NIOFSDirectory;

public class RunEcommerceStanAnnotation {
	
	public static void main(String[] args) throws Exception{
		double testFraction = Double.parseDouble(args[0]);
		int values = Integer.parseInt(args[1]);
		
		new LogEvents().info("EXPERIMENTS STARTED");
		SchemaIni schemaIni = new SchemaIni(new FileResource(new DataPaths().ecommerce().file("schema.ini").path()));
		schemaIni.parse();
		TrecResult trecResultWithContext = new TrecResult("stan-on-ecommerce");
		TrecResult trecResultWithoutContext = new TrecResult("stan-on-ecommerce");
		TrecResult trecResultAverage = new TrecResult("stan-on-ecommerce");
		AverageTime withContextTime = new AverageTime();
		AverageTime withoutContextTime = new AverageTime();
		AverageTime averageTime = new AverageTime();
		
		HashMap<Integer, ArrayList<String>> samples = samples(testFraction);
		for(Integer testId : samples.keySet()){
			new LogEvents().info("Started round " + testId + " of " + samples.size());
			ArrayList<String> testSet = samples.get(testId);
			
			File indexDirectory = new File(new IndexesPath().labelling("evaluation").path());
			FileUtils.deleteQuietly(indexDirectory);
			NIOFSDirectory directory = new NIOFSDirectory(indexDirectory.toPath());
			prepareIndex(testSet, directory, schemaIni);
			directory.close();
			StanAnnotator contextualizedAnnotator = new StanAnnotator("ecommerce-evaluation").withContext();
			StanAnnotator decontextualizedAnnotator = new StanAnnotator("ecommerce-evaluation").withoutContext();
			
			double completed = 0;
			ElapsedTime elapsedTime = new ElapsedTime();
			for(String tableName : testSet){
				new LogEvents().info("Started evaluation on " + tableName + " (" + new DecimalFormat("###.##").format((completed/testSet.size())*100) + "%)");
				CSVTable table = table(tableName, schemaIni);
				for(Column column : table.columns()){
					String context = context(column);
					ArrayList<String> members = limit(column.members(), values);
					if(members.size() > 0){
					
						try{
							if(!context.isEmpty()){
								elapsedTime.start();
								List<CandidateProperty> candidates = contextualizedAnnotator.annotate(new Context(context), members);
								elapsedTime.stop();
								trecResultWithContext.addPrediction(column.id(), candidates);
								trecResultAverage.addPrediction(column.id(), candidates);
								withContextTime.add(elapsedTime.delta());
								averageTime.add(elapsedTime.delta());
							}
							else{
								elapsedTime.start();
								List<CandidateProperty> candidates = decontextualizedAnnotator.annotate(members);
								elapsedTime.stop();
								trecResultWithoutContext.addPrediction(column.id(), candidates);
								trecResultAverage.addPrediction(column.id(), candidates);
								withoutContextTime.add(elapsedTime.delta());
								averageTime.add(elapsedTime.delta());
							}
						}
						catch(Exception e){
							continue;
						}
					}
				}
				completed++;
			}
			contextualizedAnnotator.closeIndex();
			decontextualizedAnnotator.closeIndex();
		}
		trecResultWithContext.save("stan", nice(fraction(testFraction)) + "p_ecommerce", "context", "k" + nice(values));
		trecResultWithoutContext.save("stan", nice(fraction(testFraction)) + "p_ecommerce", "noContext", "k" + nice(values));
		trecResultAverage.save("stan", nice(fraction(testFraction)) + "p_ecommerce", "average", "k" + nice(values));
		new LogEvents().info("EXPERIMENTS COMPLETED");
		new LogEvents().info("TIME ANNOTATING COLUMNS WITH CONTEXT: " + withContextTime.average());
		new LogEvents().info("TIME ANNOTATING COLUMNS WITHOUT CONTEXT: " + withoutContextTime.average());
		new LogEvents().info("TIME ANNOTATING COLUMNS AVERAGE: " + averageTime.average());
	}
	
	private static HashMap<Integer, ArrayList<String>> samples(double testFraction) {
		HashMap<Integer, ArrayList<String>> samples = new ReservoirSampling<String>().samples(testFraction, listings());
		if(testFraction > 0.5){
			HashMap<Integer, ArrayList<String>> oneSample = new HashMap<Integer, ArrayList<String>>();
			oneSample.put(1, samples.get(1));
			return oneSample;
		}
		return samples;
	}

	private static String nice(int values) {
		String nice = values + "";
		if(values < 10){
			nice = "0" + nice;
		}
		return nice;
	}
	
	private static int fraction(double testFraction) {
		return (int)(testFraction*100);
	}

	private static void prepareIndex(ArrayList<String> testSet, NIOFSDirectory directory, SchemaIni schemaIni) throws Exception {
		ArrayList<String> trainingSet = listings();
		trainingSet.removeAll(testSet);
		ArrayList<Resource> trainingTables = new ArrayList<Resource>();
		for(String table : trainingSet){
			trainingTables.add(new FileResource(new DataPaths().ecommerce().listings().file(table).path()));
		}
		new PrepareStanIndex().buildIndex(trainingTables, directory, schemaIni);
	}
	
	private static ArrayList<String> limit(ArrayList<String> members, int limit) {
		return new ReservoirSampling<String>().getAtMostDistinctSamples(limit, members);
	}

	private static String context(Column column) {
		String header = column.header();
		return (header.equals("NO HEADER")) ? "" : header.trim();
	}
	
	private static CSVTable table(String tableName, SchemaIni schemaIni) {
		String listingPath = new DataPaths().ecommerce().listings().file(tableName).path();
		CSVTable table = new CSVTable(new FileResource(listingPath))
									.withSeparator("|");
		if(schemaIni.get(tableName).hasHeader()) table.withHeader();
		return table;
	}

	private static ArrayList<String> listings() {
		String listingsPath = new DataPaths().ecommerce().listings().path();
		ArrayList<FileResource> resources = new FileResource(listingsPath).listResources();
		ArrayList<String> listings = new ArrayList<String>();
		for(FileResource resource : resources){
			listings.add(resource.name());
		}
		return listings;
	}
}