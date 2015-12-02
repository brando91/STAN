package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.Column;
import it.disco.unimib.stan.core.IndexesPath;
import it.disco.unimib.stan.core.Json;
import it.disco.unimib.stan.core.KarmaNumericAnnotator;
import it.disco.unimib.stan.core.KarmaTextualAnnotator;
import it.disco.unimib.stan.core.SemanticAnnotation;

import java.io.IOException;
import java.util.ArrayList;

public class KarmaAnnotationPage implements ApplicationHandler{

	private KarmaTextualAnnotator textualAnnotator;
	private KarmaNumericAnnotator numericAnnotator;

	public KarmaAnnotationPage(KarmaTextualAnnotator karmaLabeller, KarmaNumericAnnotator numericAnnotator) throws Exception {
		this.textualAnnotator = karmaLabeller;
		this.numericAnnotator = numericAnnotator;
	}
	
	@Override
	public String route() {
		return "karma-annotation";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception {
		ParameterValidator validator = new ParameterValidator("columns", "dataset");
		
		if(!validator.isCorrectRequest(requestAndResponse)){
			requestAndResponse.setResponseStatus(400);
			return new ErrorTemplate("karma-annotation", "Karma-Predictions", "Missing " + validator.missingParameter()).page();
		}
		else{
			return new Template("karma-annotation")
									.predictions(calculatePredictions(requestAndResponse.getParameter("columns"), 
																   	  requestAndResponse.getParameter("dataset")))
								    .breadcrumb("Karma-Predictions")
								    .page();
		}
	}

	private ArrayList<SemanticAnnotation> calculatePredictions(String columns, String dataset) throws IOException, Exception {
		ArrayList<SemanticAnnotation> predictions;
		this.textualAnnotator.indexPath(new IndexesPath().karma(dataset).path());
		this.textualAnnotator.startPredicting();
		this.numericAnnotator.indexPath(new IndexesPath().karmaNumeric(dataset).path());
		this.numericAnnotator.startPredicting();
		
		Column column = new Column("any").withAllMembers(new Json().deserialize(columns).getArray("column"));
		if(column.isNumericAtQueryTime()){
			predictions = this.numericAnnotator.predictAnnotation(column.numericMembers(), 30);
		}
		else{
			predictions = this.textualAnnotator.predictAnnotation(column.members(), 30);
		}
		this.textualAnnotator.endPredicting();
		this.numericAnnotator.endPredicting();
		return predictions;
	}

}
