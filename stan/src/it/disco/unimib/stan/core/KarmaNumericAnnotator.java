package it.disco.unimib.stan.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import jsc.independentsamples.SmirnovTest;
import jsc.tests.H1;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;

public class KarmaNumericAnnotator {
	
	private File indexDirectory;
	private KarmaIndex indexer;
	private Document document;
	private KarmaSearch index;

	public KarmaNumericAnnotator indexPath(String path) throws IOException {
		this.indexDirectory = new File(path);
		FileUtils.forceMkdir(new File(new IndexesPath().path()));
		FileUtils.forceMkdir(this.indexDirectory);
		return this;
	}
	
	public KarmaNumericAnnotator startTraining() throws IOException{
		this.indexer = new KarmaIndex(this.indexDirectory);
		this.indexer.open();
		return this;
	}
	
	public KarmaNumericAnnotator newDocument() {
		this.document = new Document();
		return this;
	}
	
	public KarmaNumericAnnotator updateDocument(ArrayList<Double> examples) {
		for(Double example : examples){
			this.document.add(new DoubleField(KarmaIndex.NUMERIC_EXAMPLE_FIELD_NAME, example, Field.Store.YES));
		}
		return this;
	}
	
	public KarmaNumericAnnotator train(String label) throws IOException{
		if(label != null){
			indexer.addDocument(this.document, label);
		}
		return this;
	}
	
	public KarmaNumericAnnotator endTraining() throws IOException {
		indexer.commit();
		indexer.close();
		return this;
	}
	
	public void startPredicting() throws IOException{
		index = new KarmaSearch(indexDirectory);
	}
	
	public ArrayList<SemanticAnnotation> predictAnnotation(ArrayList<Double> testExamples, int resultsLimit) throws Exception{
		double[] testSample = arrayFrom(testExamples);
		ArrayList<SemanticAnnotation> predictions = new ArrayList<SemanticAnnotation>();
		//KolmogorovSmirnovTest test = new KolmogorovSmirnovTest();
	    
	    HashMap<String, ArrayList<Double>> documents = index.getNumericDocuments();
		for (String label : documents.keySet()) {
	    	
	    	double[] trainSample = arrayFrom(documents.get(label));

	    	if(trainSample.length >= 2 && testSample.length >= 2){
		  		SmirnovTest test = new SmirnovTest(testSample, trainSample, H1.NOT_EQUAL, true);
		    	double pValue = test.getSP();
		    	//pValue = test.kolmogorovSmirnovTest(sample1, sample2);
		    	predictions.add(new SemanticAnnotation(label, pValue));
	    	}
	    }
	   
		Collections.sort(predictions, new AnnotationComparator());
		resultsLimit = (resultsLimit > predictions.size()) ? predictions.size() : resultsLimit;
		ArrayList<SemanticAnnotation> results = new ArrayList<SemanticAnnotation>();
		results.addAll(predictions.subList(0, resultsLimit));
		return results;
	}
	
	public void endPredicting() {
		index.close();
	}
	
	private double[] arrayFrom(ArrayList<Double> list) {
		double[] array = new double[list.size()];
	  	for(int i = 0; i < list.size(); i++){
	      array[i] = list.get(i);
	  	}
		return array;
	}
}
