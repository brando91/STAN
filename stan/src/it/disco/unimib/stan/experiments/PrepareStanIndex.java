package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.CSVTable;
import it.disco.unimib.stan.core.Column;
import it.disco.unimib.stan.core.LogEvents;
import it.disco.unimib.stan.core.Resource;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.lucene.store.NIOFSDirectory;

public class PrepareStanIndex {

	public void buildIndex(ArrayList<Resource> trainingSet, NIOFSDirectory directory, SchemaIni schemaIni) throws Exception {
		StanIndexWriter index = new StanIndexWriter(directory);
		double completed = 0;
		
		for(Resource tableFile : trainingSet){
			MerchantConfiguration configuration = schemaIni.get(tableFile.name());
			for(Column column : table(tableFile, configuration).columns()){
				SchemaAnnotation annotation = configuration.getAnnotationFor(column.id());
				if(annotation != null){
					for(String cell : column.members()){
						index.add(context(column), annotation.annotation().toLowerCase(), cell, annotation.type());
					}
				}
			}
			completed++;
			new LogEvents().info(tableFile.name() + " import completed (" + new DecimalFormat("###.##").format((completed/trainingSet.size())*100) + "%)");
		}
		index.closeWriter();
	}
	
	private static CSVTable table(Resource tableFile, MerchantConfiguration configuration) {
		CSVTable table = new CSVTable(tableFile).withSeparator("|");
		if(configuration.hasHeader()) table.withHeader();
		return table;
	}
	
	private static String context(Column column) {
		String header = column.header();
		return (header.equals("NO HEADER")) ? "" : header;
	}
}
