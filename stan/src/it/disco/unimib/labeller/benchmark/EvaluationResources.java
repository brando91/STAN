package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.ConstantSimilarity;
import it.disco.unimib.labeller.index.FullyContextualizedValue;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.OnlyValue;
import it.disco.unimib.labeller.index.PartiallyContextualizedValue;
import it.disco.unimib.labeller.index.ScaledDepths;
import it.disco.unimib.labeller.index.SelectionCriterion;
import it.disco.unimib.labeller.index.SimilarityMetric;
import it.disco.unimib.labeller.index.SimilarityMetricWrapper;
import it.disco.unimib.labeller.properties.DatasetSummary;
import it.disco.unimib.labeller.unit.InputFileTestDouble;
import it.disco.unimib.stan.core.EvaluationPaths;
import it.disco.unimib.stan.core.IndexesPath;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

public class EvaluationResources {
	
	public ScaledDepths hierarchyFrom(String knowledgeBase) throws Exception {
		if(knowledgeBase.startsWith("yago1")){
			return new ScaledDepths(new InputFile(new File(new EvaluationPaths().indexes().yago().depths().file("types.csv").path())));
		}
		if(knowledgeBase.startsWith("dbpedia")){
			return new ScaledDepths(new InputFile(new File(new EvaluationPaths().indexes().dbpedia().depths().file("types.csv").path())));
		}
		if(knowledgeBase.startsWith("ecommerce") || knowledgeBase.startsWith("test")){
			return new ScaledDepths(new InputFileTestDouble());
		}
		return null;
	}

	public String indexPath(String knowledgeBase) {
		HashMap<String, String> paths = new HashMap<String, String>();
		paths.put("dbpedia", new EvaluationPaths().indexes().dbpedia().properties().path());
		paths.put("dbpedia-ontology", new EvaluationPaths().indexes().dbpediaOntology().properties().path());
		paths.put("dbpedia-with-labels", new EvaluationPaths().indexes().dbpedia().properties().path());
		paths.put("yago1", new EvaluationPaths().indexes().yago().properties().path());
		paths.put("yago1-simple", new EvaluationPaths().indexes().yago().properties().path());
		paths.put("test", new IndexesPath().labellingTest().path());
		paths.put("ecommerce", new IndexesPath().labelling("ecommerce").path());
		paths.put("ecommerce-evaluation", new IndexesPath().labelling("evaluation").path());
		return paths.get(knowledgeBase);
	}
	
	public String goldStandardPath(String knowledgeBase) throws Exception {
		HashMap<String, String> paths = new HashMap<String, String>();
		paths.put("dbpedia", new EvaluationPaths().goldStandard("dbpedia-enhanced").path());
		paths.put("dbpedia-ontology", new EvaluationPaths().goldStandard("dbpedia-enhanced-ontology").path());
		paths.put("dbpedia-with-labels", new EvaluationPaths().goldStandard("dbpedia-enhanced").path());
		paths.put("yago1", new EvaluationPaths().goldStandard("yago1-enhanced").path());
		paths.put("yago1-simple", new EvaluationPaths().goldStandard("yago1-simple").path());
		return paths.get(knowledgeBase);
	}
	
	public SimilarityMetric occurrences(String type) throws Exception{
		HashMap<String, SimilarityMetric> occurrences = new HashMap<String, SimilarityMetric>();
		occurrences.put("simple", new ConstantSimilarity());
		occurrences.put("contextualized", new SimilarityMetricWrapper(new JaccardSimilarity()));
		return occurrences.get(type);
	}
	
	public SelectionCriterion context(IndexFields fields, String type) throws Exception{
		HashMap<String, SelectionCriterion> contexts = new HashMap<String, SelectionCriterion>();
		
		contexts.put("complete", new FullyContextualizedValue(fields));
		contexts.put("no", new OnlyValue(fields));
		contexts.put("partial", new PartiallyContextualizedValue(fields));
		return contexts.get(type);
	}
	
	public DatasetSummary rangeSummariesFrom(String knowledgeBase) throws Exception {
		String directory = "";
		if(knowledgeBase.startsWith("yago1")){
			directory = new EvaluationPaths().ranges("yago1").path();
		}
		else if(knowledgeBase.startsWith("dbpedia")){
			directory = new EvaluationPaths().ranges("dbpedia").path();
		}
		else if(knowledgeBase.startsWith("ecommerce")){
			directory = new IndexesPath().ecommerceRanges().path();
		}
		else if(knowledgeBase.startsWith("test")){
			return new DatasetSummary();
		}
		return summaryFrom(directory);
	}
	
	public DatasetSummary domainSummariesFrom(String knowledgeBase) throws Exception {
		String directory = "";
		if(knowledgeBase.startsWith("yago1")){
			directory = new EvaluationPaths().domains("yago1").path();
		}
		else if(knowledgeBase.startsWith("dbpedia")){
			directory = new EvaluationPaths().domains("dbpedia").path();
		}
		else if(knowledgeBase.startsWith("ecommerce")){
			directory = new IndexesPath().ecommerceDomains().path();
		}
		else if(knowledgeBase.startsWith("test")){
			return new DatasetSummary();
		}
		return summaryFrom(directory);
	}
	
	public DatasetSummary summaryFrom(String directory) throws Exception {
		List<InputFile> summaries = new ArrayList<InputFile>();
		for(File file : new File(directory).listFiles()){
			summaries.add(new InputFile(file));
		}
		return new DatasetSummary(summaries.toArray(new InputFile[summaries.size()]));
	}

}
