package it.disco.unimib.stan.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.NIOFSDirectory;


/**
 * This class is responsible for predicting top-k suggestions for textual data
 * using TF-IDF based cosine similarity approach and checking if a document for
 * a semantic label already exists
 * 
 * @author ramnandan
 * 
 */
public class KarmaSearch {
	private IndexSearcher indexSearcher = null;
	private Analyzer analyzer = null;
	private DirectoryReader indexReader;
	private HashMap<Integer, String> cachedTextualDocuments;
	private HashMap<String, ArrayList<Double>> cachedNumericDocuments;

	public KarmaSearch(File indexDirectory) throws IOException {
		indexReader = DirectoryReader.open(NIOFSDirectory.open(indexDirectory.toPath()));
		indexSearcher = new IndexSearcher(indexReader);
		analyzer = new StandardAnalyzer();
		cachedTextualDocuments = new HashMap<Integer, String>();
		cachedNumericDocuments = new HashMap<String, ArrayList<Double>>();
	}

	public ArrayList<SemanticAnnotation> getTopK(int k, String content) throws ParseException, IOException {
		ArrayList<SemanticAnnotation> results = new ArrayList<SemanticAnnotation>();
		int spaces = content.length() - content.replace(" ", "").length();
		if (spaces > BooleanQuery.getMaxClauseCount()) {
			BooleanQuery.setMaxClauseCount(spaces);
		}

		QueryParser parser = new QueryParser(KarmaIndex.CONTENT_FIELD_NAME, this.analyzer);
		Query query = parser.parse(QueryParser.escape(content));

		TopDocs relevantDocuments = indexSearcher.search(query, k);
		Set<String> fieldsToLoad = new HashSet<String>();
		fieldsToLoad.add(KarmaIndex.LABEL_FIELD_NAME);
		
		for(ScoreDoc document : relevantDocuments.scoreDocs){
			results.add(new SemanticAnnotation(label(fieldsToLoad, document.doc), document.score));
		}
		return results;
	}

	private String label(Set<String> fieldsToLoad, int id) throws IOException {
		if(cachedTextualDocuments.containsKey(id)){
			return cachedTextualDocuments.get(id);
		}
		else{
			Document doc = indexSearcher.doc(id, fieldsToLoad);
			String label = doc.get(KarmaIndex.LABEL_FIELD_NAME);
			cachedTextualDocuments.put(id, label);
			return label;
		}
	}

	public HashMap<String, ArrayList<Double>> getNumericDocuments() throws IOException {
		if(cachedNumericDocuments.size() == 0){
			MatchAllDocsQuery matchAllDocsClause = new MatchAllDocsQuery();
			Builder query = new BooleanQuery.Builder();
			query.add(matchAllDocsClause, Occur.MUST);
			TopDocs results = indexSearcher.search(query.build(), Integer.MAX_VALUE, Sort.INDEXORDER);
			ScoreDoc[] hits = results.scoreDocs;
	
			for(int i=0; i<hits.length; i++) {
				Document document = indexSearcher.doc(hits[i].doc);
				cachedNumericDocuments.put(document.get(KarmaIndex.LABEL_FIELD_NAME), getNumbers(document));
			}
		}
		return cachedNumericDocuments;
	}

	private ArrayList<Double> getNumbers(Document document) {
		ArrayList<Double> numericKnowledge = new ArrayList<Double>();
		for(IndexableField field : document.getFields()){
			if(field.name().equals(KarmaIndex.NUMERIC_EXAMPLE_FIELD_NAME)){
				numericKnowledge.add((Double)field.numericValue());
			}
		}
		return numericKnowledge;
	}

	public ArrayList<String> getContentsForLabel(String label) throws IOException {
		ArrayList<String> contents = new ArrayList<String>();
		Document document = this.getDocumentForLabel(label);
		for(IndexableField field : document.getFields()){
			if(field.name().equals(KarmaIndex.CONTENT_FIELD_NAME)){
				contents.add(field.stringValue());
			}
		}
		return contents;
	}

	private Document getDocumentForLabel(String label) throws IOException {
		Query query = new TermQuery(new Term(KarmaIndex.LABEL_FIELD_NAME, label));
		TopDocs results = indexSearcher.search(query, 10, Sort.INDEXORDER);
		ScoreDoc[] hits = results.scoreDocs;

		for(int i=0; i<hits.length; i++) {
			Document doc = indexSearcher.doc(hits[i].doc);
			String labelString = doc.get(KarmaIndex.LABEL_FIELD_NAME);
			if (labelString.equalsIgnoreCase(label)) {
				return doc;
			}		
		}
		return null;
	}
	
	public int getNumDocs(){
		return this.indexReader.numDocs();
	}

	public void close() {
		try {
			indexSearcher.getIndexReader().close();
		} catch (IOException e) {
			// Ignore
		}
	}
}