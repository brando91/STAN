package it.disco.unimib.stan.webapp;

import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.stan.core.Json;
import it.disco.unimib.stan.core.StanAnnotator;

import java.util.ArrayList;
import java.util.List;

public class AnnotationApiPage implements Page {

	@Override
	public String route() {
		return "api/annotate";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception {
		if(new ParameterValidator("data").isCorrectRequest(requestAndResponse)){
			Json data = new Json().deserialize(requestAndResponse.getParameter("data"));
			ArrayList<String> values = data.getArray("values");
			String context = data.get("context");
			String knowledgeBase = data.get("kb");
			ArrayList<Json> results = new ArrayList<Json>();
			
			if(values.size() > 0 && !knowledgeBase.isEmpty() && !context.isEmpty()){
				StanAnnotator stanAnnotator = new StanAnnotator(knowledgeBase);
				stanAnnotator.withContext();
				
				List<CandidateProperty> candidates = stanAnnotator.annotate(new Context(context), values);
				for(CandidateProperty candidate : candidates){
					results.add(new Json()
										.parameter("uri", candidate.uri())
										.parameter("label", candidate.label())
										.parameter("score", candidate.score() + ""));
				}
			}
			requestAndResponse.setContentType("application/json");
			return new Json().jsonParameters("results", results).serialize();
		}
		else{
			requestAndResponse.setContentType("application/json");
			return new Json().serialize();
		}
	}
}
