package it.disco.unimib.stan.webapp;

import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.stan.core.CSVTable;
import it.disco.unimib.stan.core.Column;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.Json;
import it.disco.unimib.stan.core.ReservoirSampling;
import it.disco.unimib.stan.core.StanAnnotator;
import it.disco.unimib.stan.core.WorkingAreaPaths;

import java.util.ArrayList;
import java.util.List;

public class SuggestionsPage implements ApplicationHandler {

	@Override
	public String route() {
		return "suggestions";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception {
		ArrayList<Json> annotations = new ArrayList<Json>();
		
		if(new ParameterValidator("kb", "column", "context").isCorrectRequest(requestAndResponse)){
			Context context = new Context(requestAndResponse.getParameter("context"));
			String knowledgeBase = requestAndResponse.getParameter("kb");
			Column column = columns(new CookieManager(requestAndResponse)).get(Integer.parseInt(requestAndResponse.getParameter("column"))-1);
			
			ArrayList<String> sample = new ReservoirSampling<String>().getAtMostDistinctSamples(20, column.members());
			
			List<CandidateProperty> candidates = new StanAnnotator(knowledgeBase).withContext().annotate(context, sample);
			for(CandidateProperty candidate : candidates){
				annotations.add(new Json()
									.parameter("uri", candidate.uri())
									.parameter("label", candidate.label()));
			}
		}
		else{
			requestAndResponse.setResponseStatus(400);
		}
		
		requestAndResponse.setContentType("application/json");
		return new Json().jsonParameters("annotations", annotations).serialize();
	}

	private ArrayList<Column> columns(CookieManager cookieManager) throws Exception {
		String user = cookieManager.getUser();
		WorkingTable workingTable = new WorkingTable(cookieManager.getTable(), user);
		FileResource resource = new FileResource(new WorkingAreaPaths().tables(user).file(workingTable.getTableName()).path());
		CSVTable table = new CSVTable(resource)
								.withSeparator(workingTable.getSeparator())
								.withTextDelimiter(workingTable.getDelimiter());
		if(workingTable.hasHeader()) table.withHeader();

		return table.columns();
	}
}
