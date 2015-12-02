package it.disco.unimib.stan.experiments;

import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.stan.core.SemanticAnnotation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class TrecResult {

	private String algorithmName;
	private ArrayList<String> predictions;

	public TrecResult(String algorithm) {
		this.algorithmName = algorithm;
		this.predictions = new ArrayList<String>();
	}

	public ArrayList<String> asTRECResults() {
		return this.predictions;
	}

	public TrecResult addPrediction(String id, ArrayList<SemanticAnnotation> predictions) {
		if(predictions.size() > 0){
			for(int i = 0; i < predictions.size(); i++){
				SemanticAnnotation prediction = predictions.get(i);
				savePrediction(id, i, prediction.getScore(), prediction.getLabel());
			}
		}
		else{
			savePrediction(id, 0, 0, "NULL");
		}
		return this;
	}

	public TrecResult addPrediction(String id, List<CandidateProperty> candidates) {
		if(candidates.size() > 0){
			for(int i = 0; i < candidates.size(); i++){
				CandidateProperty prediction = candidates.get(i);
				savePrediction(id, i, prediction.score(), prediction.label());
			}
		}
		else{
			savePrediction(id, 0, 0, "NULL");
		}
			
		return this;
	}

	public void save(String algorithm, String dataset, String type, String size) throws IOException {
		String fileName = dataset + "-" + type + "-" + size;
		String path = new EvaluationPaths().results().folder(algorithm).file(fileName  + ".qrels").path();
		FileUtils.writeLines(new File(path), this.predictions);
	}
	
	private void savePrediction(String id, int i, double score, String label) {
		this.predictions.add(id + 
							" Q0 " + 
							label.toLowerCase() + " " + 
							(i+1) + " " + 
							score + " " +
							this.algorithmName);
	}
}
