package it.disco.unimib.stan.core;

import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.ScaledDepths;
import it.disco.unimib.labeller.index.SimilarityMetric;
import it.disco.unimib.labeller.properties.AnnotationAlgorithm;
import it.disco.unimib.labeller.properties.DatasetSummary;
import it.disco.unimib.labeller.properties.DomainAndRangeConsistency;

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
		this.resources = new EvaluationResources();
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
		return new NIOFSDirectory(new File(resources.indexPath(kb)).toPath());
	}
	
	public SimilarityMetric similarity() throws Exception {
		return resources.occurrences(occurrences);
	}
	
	public AnnotationAlgorithm configure(Index index) throws Exception {
		return new DomainAndRangeConsistency(index, resources.context(new IndexFields(kb), context), domainSummaries(), rangeSummaries(), types());
	}

	private DatasetSummary rangeSummaries() throws Exception {
		return resources.rangeSummariesFrom(kb);
	}

	private DatasetSummary domainSummaries() throws Exception {
		return resources.domainSummariesFrom(kb);
	}

	private ScaledDepths types() throws Exception {
		return resources.hierarchyFrom(kb);
	}
}
