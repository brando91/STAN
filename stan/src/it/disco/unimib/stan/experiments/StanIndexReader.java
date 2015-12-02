package it.disco.unimib.stan.experiments;

import it.disco.unimib.labeller.index.IndexFields;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.NIOFSDirectory;

public class StanIndexReader {

	private DirectoryReader reader;
	private IndexSearcher searcher;
	private IndexFields indexFields;

	public StanIndexReader(NIOFSDirectory directory) throws IOException {
		this.reader = DirectoryReader.open(directory);
		this.searcher = new IndexSearcher(reader);
		this.indexFields = new IndexFields("ecommerce");
	}

	public int countDocuments() {
		return reader.numDocs();
	}

	public Document getDocumentByLabel(String literal) throws IOException {
		Query query = new TermQuery(new Term(indexFields.label(), literal));
		TopDocs results = searcher.search(query, 10, Sort.INDEXORDER);
		ScoreDoc[] hits = results.scoreDocs;
		return searcher.doc(hits[0].doc);
	}

}
