package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.MatchingProperty;
import it.disco.unimib.labeller.index.Triples;
import it.disco.unimib.stan.core.EvaluationPaths;

import java.io.File;

import org.apache.lucene.store.SimpleFSDirectory;

public class RunKeyValueIndexing {

	public static void main(String[] args) throws Exception{
		String source = args[0];
		String destination = args[1];
		String property = args[2];
		
		EntityValues index = new EntityValues(new SimpleFSDirectory(new File(new EvaluationPaths().indexes().path() + "/" + destination).toPath()));
		for(File file : new File(new EvaluationPaths().path() + "/" + source).listFiles()){
			System.out.println("processing " + file);
			new Triples(new InputFile(file)).fill(index, new MatchingProperty(property));
		}
		index.closeWriter();
	}
}
