package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.AverageTime;
import it.disco.unimib.stan.core.CSVTable;
import it.disco.unimib.stan.core.Column;
import it.disco.unimib.stan.core.ElapsedTime;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.IndexesPath;
import it.disco.unimib.stan.core.KarmaNumericAnnotator;
import it.disco.unimib.stan.core.KarmaTextualAnnotator;
import it.disco.unimib.stan.core.LogEvents;
import it.disco.unimib.stan.core.SemanticAnnotation;
import it.disco.unimib.stan.core.TrainingDataPath;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

public class RunEcommerceKarmaAnnotation {

	public static void main(String[] args) throws Exception {
		double testFraction = Double.parseDouble(args[0]);
		
		String textualIndexPath = new IndexesPath().karma("evaluation").path();
		String numericIndexPath = new IndexesPath().karmaNumeric("evaluation").path();
		TrainingDataPath trainingPath = new TrainingDataPath().ecommerce();
		
		new LogEvents().info("EXPERIMENTS STARTED");
		TrecResult trecTextualResult = new TrecResult("karma-on-ecommerce");
		TrecResult trecNumericResult = new TrecResult("karma-on-ecommerce");
		TrecResult trecAverageResult = new TrecResult("karma-on-ecommerce");
		AverageTime averageTime = new AverageTime();
		
		HashMap<Integer, ArrayList<String>> samples = samples(testFraction);
		for(Integer testId : samples.keySet()){
			new LogEvents().info("Started round " + testId + " of " + samples.size());
			ArrayList<String> testSet = samples.get(testId);
			prepareIndex(textualIndexPath, numericIndexPath, trainingPath, testSet);
			KarmaTextualAnnotator textualAnnotator = new KarmaTextualAnnotator().indexPath(textualIndexPath);
			KarmaNumericAnnotator numericAnnotator = new KarmaNumericAnnotator().indexPath(numericIndexPath);
			textualAnnotator.startPredicting();
			numericAnnotator.startPredicting();
			
			double completed = 0;
			ElapsedTime elapsedTime = new ElapsedTime();
			for(String tableName : testSet){
				new LogEvents().info("Started evaluation on " + tableName + " table (" + new DecimalFormat("###.##").format((completed/testSet.size())*100) + "%)");
				FileResource table = new FileResource(new DataPaths().ecommerce().listings().file(tableName).path());
				for(Column column  : new CSVTable(table).columns()){
					if(column.isNumericAtQueryTime() && !column.numericMembers().isEmpty()){
						elapsedTime.start();
						ArrayList<SemanticAnnotation> predictions = predictNumeric(numericAnnotator, column);
						elapsedTime.stop();
						trecNumericResult.addPrediction(column.id(), predictions);
						trecAverageResult.addPrediction(column.id(), predictions);
					}
					else if(!column.members().isEmpty()){
						elapsedTime.start();
						ArrayList<SemanticAnnotation> predictions = predictTextual(textualAnnotator, column);
						elapsedTime.stop();
						trecTextualResult.addPrediction(column.id(), predictions);
						trecAverageResult.addPrediction(column.id(), predictions);
					}
					averageTime.add(elapsedTime.delta());
				}
				completed++;
			}
			textualAnnotator.endPredicting();
			numericAnnotator.endPredicting();
		}
		trecTextualResult.save("karma", "ecommerce", "textual", "k" + nice(testFraction));
		trecNumericResult.save("karma", "ecommerce", "numeric", "k" + nice(testFraction));
		trecAverageResult.save("karma", "ecommerce", "average", "k" + nice(testFraction));
		new LogEvents().info("EXPERIMENTS COMPLETED");
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

	private static String nice(double testFraction) {
		int values = fraction(testFraction);
		String nice = values + "";
		if(values < 10){
			nice = "0" + nice;
		}
		return nice;
	}
	
	private static int fraction(double testFraction) {
		return (int)(testFraction*100);
	}

	private static void prepareIndex(String textualIndexPath, String numericIndexPath, TrainingDataPath trainingPath, ArrayList<String> testSet) throws Exception {
		FileUtils.deleteQuietly(new File(textualIndexPath));
		FileUtils.deleteQuietly(new File(numericIndexPath));
		new PrepareKarmaIndex().buildIndex(textualIndexPath, numericIndexPath, trainingPath, testSet);
	}

	private static ArrayList<SemanticAnnotation> predictTextual(KarmaTextualAnnotator textualAnnotator, Column column) throws IOException {
		return textualAnnotator.predictAnnotation(new ReservoirSampling<String>().getAtMostSamples(200, column.members()), 20);
	}

	private static ArrayList<SemanticAnnotation> predictNumeric(KarmaNumericAnnotator numericAnnotator, Column column) throws Exception {
		return numericAnnotator.predictAnnotation(new ReservoirSampling<Double>().getAtMostSamples(200, column.numericMembers()), 20);
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
