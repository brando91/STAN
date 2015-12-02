package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.KarmaNumericAnnotator;
import it.disco.unimib.stan.core.KarmaTextualAnnotator;
import it.disco.unimib.stan.core.LogEvents;
import it.disco.unimib.stan.core.TrainingDataPath;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PrepareKarmaIndex {

	public void buildIndex(String indexPath, String numericIndexPath, TrainingDataPath trainingPath) throws Exception {
		buildIndex(indexPath, numericIndexPath, trainingPath, new ArrayList<String>());
	}
	
	public void buildIndex(String indexPath, String numericIndexPath, TrainingDataPath trainingPath, ArrayList<String> testSet) throws Exception {
		KarmaTextualAnnotator textualAnnotator = new KarmaTextualAnnotator()
														.indexPath(indexPath)
														.startTraining();
		KarmaNumericAnnotator numericAnnotator = new KarmaNumericAnnotator()
														.indexPath(numericIndexPath)
														.startTraining();
		
		ArrayList<FileResource> columnFolders = new FileResource(trainingPath.path()).listResources();
		
		double completed = 0;
		for(FileResource folder : columnFolders){
			new LogEvents().info(folder.name() + " import started");
			
			ArrayList<TrainingColumn> trainingColumns = new ArrayList<TrainingColumn>();
			for(FileResource trainingColumn : folder.listResources()){
				if(!testSet.contains(tableName(trainingColumn.name()))){
					trainingColumns.add(new TrainingColumn(trainingColumn));
				}
			}
			indexColumn(textualAnnotator, numericAnnotator, trainingColumns);
			
			numericAnnotator.train(folder.name());
			textualAnnotator.train(folder.name());
			
			completed++;
			new LogEvents().info(folder.name() + " import completed (" + new DecimalFormat("###.##").format((completed/columnFolders.size())*100) + "%)");
		}
		
		numericAnnotator.endTraining();
		textualAnnotator.endTraining();
	}
	
	private String tableName(String columnName) {
		return columnName.substring(0, columnName.lastIndexOf("-"));
	}

	public void indexColumn(KarmaTextualAnnotator textualAnnotator, KarmaNumericAnnotator numericAnnotator, ArrayList<TrainingColumn> columns) throws Exception {
		numericAnnotator.newDocument();
		textualAnnotator.newDocument();
		for(TrainingColumn column : columns){
			column.parseColumn();
			if(column.isNumeric()){
				numericAnnotator.updateDocument(column.numericMembers());
			}
			if(column.isTextual()){
				textualAnnotator.updateDocument(column.members());
			}
		}
	}
}
