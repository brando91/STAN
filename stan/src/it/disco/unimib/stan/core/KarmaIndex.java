package it.disco.unimib.stan.core;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;

/**
 * This class is responsible for creation of an index, adding documents to the
 * index, committing changes
 * 
 * @author ramnandan
 * 
 */

public class KarmaIndex {
	public static String NUMERIC_EXAMPLE_FIELD_NAME = "numeric_example";
	public static String CONTENT_FIELD_NAME = "content";
	public static String LABEL_FIELD_NAME = "label";

	private IndexWriter indexWriter = null;
	private File directory;

	public KarmaIndex(File directory) throws IOException {
		this.directory = directory;
	}

	public void open() throws IOException {

		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		// Creates a new index if one does not exist,
		// otherwise opens the index and documents will be appended
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);

		indexWriter = new IndexWriter(FSDirectory.open(this.directory.toPath()), config);
	}

	public void commit() throws IOException {
		indexWriter.commit();
	}
	
	public void close() throws IOException {
		indexWriter.close();
	}
	
	public void addDocument(Document document, String label) throws IOException {
		document.add(new StringField(LABEL_FIELD_NAME, label, Field.Store.YES));
		indexWriter.addDocument(document);
	}
}