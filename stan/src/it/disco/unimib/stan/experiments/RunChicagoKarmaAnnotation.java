package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.CSVTable;
import it.disco.unimib.stan.core.Column;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.IndexesPath;
import it.disco.unimib.stan.core.KarmaNumericAnnotator;
import it.disco.unimib.stan.core.KarmaTextualAnnotator;
import it.disco.unimib.stan.core.LogEvents;
import it.disco.unimib.stan.core.SemanticAnnotation;

import java.io.IOException;
import java.util.ArrayList;

public class RunChicagoKarmaAnnotation {

	public static void main(String[] args) throws Exception {

		TrecResult trecResult = new TrecResult("karma-on-chicago");
		String path = new DataPaths().chicago().chicagoDataset().path();
		ArrayList<FileResource> resources = new FileResource(path).listResources();
		
		new LogEvents().info("EXPERIMENTS STARTED");
		for(FileResource resource : resources){
			new LogEvents().info("Started evaluation on " + resource.name() + " table");
			ArrayList<Column> columns = new CSVTable(resource).withSeparator(",").columns();
			for(Column column : columns){
				new LogEvents().info("Started evaluation on " + column.id() + " column");
				ArrayList<SemanticAnnotation> predictions = new ArrayList<SemanticAnnotation>();
				if(column.isNumericAtQueryTime()){
					predictions = predictNumeric(column);
				}
				else{
					predictions = predictTextual(column);
				}
				trecResult.addPrediction(column.id(), predictions);
			}
		}
		trecResult.save("karma", "chicago", "average", "full");
		new LogEvents().info("EXPERIMENTS COMPLETED");
	}

	private static ArrayList<SemanticAnnotation> predictTextual(Column column) throws IOException {
		String textualIndexPath = new IndexesPath().karma("chicago").path();
		KarmaTextualAnnotator textualAnnotator = new KarmaTextualAnnotator().indexPath(textualIndexPath);
		textualAnnotator.startPredicting();
		ArrayList<SemanticAnnotation> predictions = textualAnnotator.predictAnnotation(new ReservoirSampling<String>().getAtMostSamples(500, column.members()), 30);
		textualAnnotator.endPredicting();
		return predictions;
	}

	private static ArrayList<SemanticAnnotation> predictNumeric(Column column) throws Exception {
		String numericIndexPath = new IndexesPath().karmaNumeric("chicago").path();
		KarmaNumericAnnotator numericAnnotator = new KarmaNumericAnnotator().indexPath(numericIndexPath);
		numericAnnotator.startPredicting();
		ArrayList<SemanticAnnotation> predictions = numericAnnotator.predictAnnotation(new ReservoirSampling<Double>().getAtMostSamples(250, column.numericMembers()), 30);
		numericAnnotator.endPredicting();
		return predictions;
	}

}
