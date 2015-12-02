package it.disco.unimib.stan.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 * This is the API class for the semantic typing module, implementing the
 * combined approach of TF-IDF based cosine similarity and Kolmogorov-Smirnov
 * test approaches for textual and numeric respectively by 
 * Ramnandan.S.K and Amol Mittal.
 * 
 * @author ramnandan
 * 
 */

public class KarmaTextualAnnotator {

	private File indexDirectory;
	private KarmaIndex indexer;
	private KarmaSearch index;
	private Document document;

	/**
	 * NOTE: Currently, TF-IDF based approach is used for both textual and
	 * numeric data due to bug in KS test on Apache Commons Math.
	 * 
	 * TODO: Integrate KS test when this bug is resolved :
	 * https://issues.apache.org/jira/browse/MATH-1131
	 * @param annotator TODO
	 * @throws IOException 
	 */

	public KarmaTextualAnnotator indexPath(String indexPath) throws IOException {
		indexDirectory = new File(indexPath);
		FileUtils.forceMkdir(new File(new IndexesPath().path()));
		FileUtils.forceMkdir(indexDirectory);
		return this;
	}

	public KarmaTextualAnnotator startTraining() throws IOException {
		this.indexer = new KarmaIndex(indexDirectory);
		indexer.open();
		return this;
	}
	
	public KarmaTextualAnnotator newDocument() {
		this.document = new Document();
		return this;
	}
	
	public KarmaTextualAnnotator updateDocument(ArrayList<String> members) {
		if(members.size() == 0){
			new LogEvents().debug("@examples list cannot be empty.");
			return this;
		}
		
		ArrayList<String> cleanedExamples = new ArrayList<String>();
		cleanedExamplesList(members, cleanedExamples);
		
		// making sure that the condition where the examples list is not empty
		// but contains junk only is not accepted
		if (cleanedExamples.size() == 0) {
			new LogEvents().debug("@examples list contains forbidden characters only.");
			return this;
		}
		
		for(String member : members){
			this.document.add(new TextField(KarmaIndex.CONTENT_FIELD_NAME, member, Field.Store.YES));
		}
		return this;
	}
	
	/**
	 * Adds the passed list of examples for training
	 * 
	 * @param label
	 *            True label for the list of example.
	 * @param examples
	 *            List of example strings.
	 * @return True if success, else False
	 */
	public synchronized boolean train(String label) {
		boolean savingSuccessful = false;
		// running basic sanity checks in the input arguments
		if (label == null || label.trim().length() == 0) {
			new LogEvents().debug("@label argument cannot be null or an empty string.");
			return false;
		}
		
		label = label.trim();

		// if the column is textual
		try {
			// treat content of column as single document
			indexer.addDocument(this.document, label);
			savingSuccessful = true;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		return savingSuccessful;
	}
	
	public void endTraining() throws IOException {
		indexer.commit();
		indexer.close();
	}

	/**
	 * @param examples
	 *            - list of examples of an unknown type
	 * @param numPredictions
	 *            - required number of predictions in descending order
	 * @param predictedLabels
	 *            - the argument in which the ordered list of labels is
	 *            returned. the size of this list could be smaller than
	 *            numPredictions if there aren't that many labels in the model
	 *            already
	 * @param confidenceScores
	 *            - the probability of the examples belonging to the labels
	 *            returned.
	 * @param exampleProbabilities
	 *            - the size() == examples.size(). It contains, for each
	 *            example, in the same order, a double array that contains the
	 *            probability of belonging to the labels returned in
	 *            predictedLabels.
	 * @param columnFeatures
	 *            - this Map supplies ColumnFeatures such as ColumnName, etc.
	 * @return True, if successful, else False
	 */
	
	public void startPredicting() throws IOException{
		index = new KarmaSearch(indexDirectory);
	}
	
	public ArrayList<SemanticAnnotation> predictAnnotation(List<String> examples,int numPredictions) {
		ArrayList<SemanticAnnotation> result = new ArrayList<SemanticAnnotation>();
		
		// Sanity checks for arguments
		if (examples == null || examples.size() == 0 || numPredictions <= 0) {
			new LogEvents().debug("Invalid arguments. Possible problems: examples list size is zero, numPredictions is non-positive");
			return result;
		}

		// construct single text for test column
		StringBuilder sb = new StringBuilder();
		for (String ex : examples) {
			sb.append(ex);
			sb.append(" ");
		}
		
		try {
			result = index.getTopK(numPredictions, sb.toString());
			new LogEvents().debug("Got " + result.size() + " predictions");
			return result;
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public void endPredicting() {
		index.close();
	}

	/**
	 * @param uncleanList
	 *            List of all examples
	 * @param cleanedList
	 *            List with examples that don't have unallowed chars and others
	 *            such as nulls or empty strings This method cleans the examples
	 *            list passed to it. Generally, it is used by other methods to
	 *            sanitize lists passed from outside.
	 */
	private void cleanedExamplesList(List<String> uncleanList, List<String> cleanedList) {
		cleanedList.clear();
		for (String example : uncleanList) {
			if (example != null) {
				example = example.trim();
				if (example.length() != 0) {
					cleanedList.add(example);
				}
			}
		}
	}
}
