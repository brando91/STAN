package it.disco.unimib.stan.core;

import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.stan.webapp.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.store.Directory;

public class StanAnnotator {

	private AlgorithmConfiguration configuration;
	private Index index;
	private String kb;
	private Directory directory;

	public StanAnnotator(String knowledgeBase)  {
		this.kb = knowledgeBase;
		this.configuration = new AlgorithmConfiguration().setKnowledgeBase(knowledgeBase);
	}
	
	public StanAnnotator withContext() throws Exception{
		this.configuration.withContext();
		this.directory = configuration.directory();
		this.index = new ContextualizedEvidence(directory, configuration.similarity(), new IndexFields(kb));
		return this;
	}
	
	public StanAnnotator withoutContext() throws Exception{
		this.configuration.withoutContext();
		this.directory = configuration.directory();
		this.index = new ContextualizedEvidence(directory, configuration.similarity(), new IndexFields(kb));
		return this;
	}

	public List<CandidateProperty> annotate(ArrayList<String> members) throws Exception {
		return annotate(new Context(""), members);
	}
	
	public List<CandidateProperty> annotate(Context context, ArrayList<String> members) throws Exception {
		return configuration.configure(index).annotate(new ContextualizedValues(escape(context.cleaned()), asArray(members)));
	}
	
	public void closeIndex() throws IOException {
		this.directory.close();
	}

	private String[] asArray(ArrayList<String> values) {
		values = removeEmptyValues(values);
		return values.toArray(new String[values.size()]);
	}

	private ArrayList<String> removeEmptyValues(ArrayList<String> values) {
		ArrayList<String> cleaned = new ArrayList<String>();
		for(String value : values){
			String trim = value.trim();
			if(!trim.isEmpty() && !trim.equals("-")) cleaned.add(escape(value));
		}
		return cleaned;
	}

	private String escape(String toEscape) {
		return toEscape.replace("<", "").replace(">", "").replace("=", "");
	}
}
