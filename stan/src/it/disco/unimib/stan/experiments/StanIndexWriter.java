package it.disco.unimib.stan.experiments;

import it.disco.unimib.labeller.index.IndexFields;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.NIOFSDirectory;

public class StanIndexWriter {

	private IndexWriter writer;
	private IndexFields indexFields;

	public StanIndexWriter(NIOFSDirectory directory) throws IOException {
		this.indexFields = new IndexFields("ecommerce");
		this.writer = new IndexWriter(directory, new IndexWriterConfig(indexFields.analyzer()).setRAMBufferSizeMB(95));
	}

	public void add(String subjectType, String property, String object, String objectType) throws Exception {
		Document document = new Document();
		
		document.add(new Field(indexFields.property(), property, TextField.TYPE_STORED));
		document.add(new Field(indexFields.label(), property, TextField.TYPE_STORED));
		document.add(new Field(indexFields.namespace(), "", TextField.TYPE_STORED));
		document.add(new Field(indexFields.literal(), object, TextField.TYPE_STORED));
		document.add(new Field(indexFields.objectType(), objectType, TextField.TYPE_STORED));
		document.add(new Field(indexFields.subjectType(), subjectType, TextField.TYPE_STORED));
		document.add(new Field(indexFields.context(), subjectType, TextField.TYPE_STORED));
		
		writer.addDocument(document);
	}
	
	public StanIndexWriter closeWriter() throws Exception {
		writer.close();
		return this;
	}
}
