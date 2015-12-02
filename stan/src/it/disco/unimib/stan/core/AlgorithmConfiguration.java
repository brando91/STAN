package it.disco.unimib.stan.core;

import it.disco.unimib.labeller.benchmark.EvaluationResources;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.ScaledDepths;
import it.disco.unimib.labeller.index.SimilarityMetric;
import it.disco.unimib.labeller.properties.AnnotationAlgorithm;
import it.disco.unimib.labeller.properties.DatasetSummary;
import it.disco.unimib.labeller.properties.DomainAndRangeConsistency;
import it.disco.unimib.labeller.unit.InputFileTestDouble;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;

public class AlgorithmConfiguration {

	private String kb;
	private String occurrences;
	private String context;
	private EvaluationResources resources;
	
	public AlgorithmConfiguration() {
		this.resources = new EvaluationResources().evaluationPath("../cluster-labelling/evaluation");
	}

	public AlgorithmConfiguration setKnowledgeBase(String kb) {
		this.kb = kb;
		return this;
	}

	public AlgorithmConfiguration withContext() {
		this.occurrences = "contextualized";
		this.context = "partial";
		return this;
	}
	
	public AlgorithmConfiguration withoutContext() {
		this.occurrences = "simple";
		this.context = "no";
		return this;
	}
	
	public Directory directory() throws IOException {
		String path = resources.indexPath(kb);
		if(testEnvironment()) path = new IndexesPath().labellingTest().path();
		if(ecommerceEnvironment()) path = new IndexesPath().labelling("ecommerce").path();
		if(ecommerceEvaluationEnvironment()) path = new IndexesPath().labelling("evaluation").path();

		return new NIOFSDirectory(new File(path).toPath());
	}
	
	public SimilarityMetric similarity() throws Exception {
		return resources.occurrences(occurrences);
	}
	
	public AnnotationAlgorithm configure(Index index) throws Exception {
		return new DomainAndRangeConsistency(index, resources.context(new IndexFields(kb), context), domainSummaries(), rangeSummaries(), types());
	}

	private DatasetSummary rangeSummaries() throws Exception {
		if(testEnvironment()) return new DatasetSummary();
		if(ecommerceEnvironment() || ecommerceEvaluationEnvironment()) return resources.summaryFrom(new IndexesPath().ecommerceRanges().path());
		return resources.rangeSummariesFrom(kb);
	}

	private DatasetSummary domainSummaries() throws Exception {
		if(testEnvironment()) return new DatasetSummary();
		if(ecommerceEnvironment() || ecommerceEvaluationEnvironment()) return resources.summaryFrom(new IndexesPath().ecommerceDomains().path());
		return resources.domainSummariesFrom(kb);
	}

	private ScaledDepths types() throws Exception {
		if(testEnvironment() || ecommerceEnvironment() || ecommerceEvaluationEnvironment()) return new ScaledDepths(new InputFileTestDouble());
		else return resources.hierarchyFrom(kb);
	}

	private boolean testEnvironment() {
		return kb.equals("test");
	}
	
	private boolean ecommerceEnvironment() {
		return kb.equals("ecommerce");
	}
	
	private boolean ecommerceEvaluationEnvironment() {
		return kb.equals("ecommerce-evaluation");
	}
}
