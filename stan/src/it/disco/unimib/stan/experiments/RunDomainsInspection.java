package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.CSVTable;
import it.disco.unimib.stan.core.Column;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.IndexesPath;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

public class RunDomainsInspection {

	public static void main(String[] args) throws Exception {
		SchemaIni schemaIni = new SchemaIni(new FileResource(new DataPaths().ecommerce().file("schema.ini").path()));
		ArrayList<MerchantConfiguration> configurations = schemaIni.parse();
		HashMap<String, HashSet<String>> domains = new HashMap<String, HashSet<String>>();
		
		for(MerchantConfiguration configuration : configurations){
			FileResource listing = new FileResource(new DataPaths().ecommerce().listings().file(configuration.merchantName()).path());
			if(configuration.hasHeader() && listing.asFile().exists()){
				CSVTable table = new CSVTable(listing)
												.withSeparator("|")
												.withHeader();
				for(Column column : table.columns()){
					SchemaAnnotation annotation = configuration.getAnnotationFor(column.id());
					if(annotation != null && !column.header().isEmpty()){
						if(domains.containsKey(annotation.annotation())){
							domains.get(annotation.annotation()).add(asRange(column.header()));
						}
						else{
							HashSet<String> types = new HashSet<String>();
							types.add(asRange(column.header()));
							domains.put(annotation.annotation(), types);
						}
					}
				}
			}
		}
		
		String folder = new IndexesPath().ecommerceDomains().path();
		FileUtils.deleteQuietly(new File(folder));
		FileUtils.forceMkdir(new File(folder));
		for(String property : domains.keySet()){
			FileUtils.writeLines(new File(folder + "/" + property.replace("/", "_")), "UTF-8", domains.get(property));
		}
	}

	private static String asRange(String type) {
		return type + "|0|0|0";
	}
}
