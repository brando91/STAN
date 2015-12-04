package org.apache.lucene.index;

import junit.framework.TestCase;
import org.apache.lucene.analysis.standard.UAX29URLEmailAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Created by dmitry on 4/3/15.
 */
public class IndexTester extends TestCase {

    private String indexPath = "src/test/indices/lukeindex";
    private IndexWriterConfig indexCfg;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDummy() {
        assertTrue(true == true);
    }

    public void testCreateIndexDir() throws Exception {
        populate();
    }

    private void populate() throws Exception {
      Path path = FileSystems.getDefault().getPath(indexPath);
      Directory directory = NIOFSDirectory.open(path);
      indexCfg = new IndexWriterConfig(new UAX29URLEmailAnalyzer());
      indexCfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

      IndexWriter writer = new IndexWriter(directory, indexCfg);

      Document doc = new Document();
      doc.add(new StringField("aaa", "1", Field.Store.NO));
      doc.add(new Field("bbb", "2", Field.Store.NO, Field.Index.NOT_ANALYZED));

      // sanity check
      doc.add(new StringField("ccc", "3", Field.Store.YES));

      writer.addDocument(doc);
      writer.close();
    }
}
