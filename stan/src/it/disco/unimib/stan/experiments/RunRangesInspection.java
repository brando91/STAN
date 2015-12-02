package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.IndexesPath;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

public class RunRangesInspection {

	public static void main(String[] args) throws Exception {
		SchemaIni schemaIni = new SchemaIni(new FileResource(new DataPaths().ecommerce().file("schema.ini").path()));
		ArrayList<MerchantConfiguration> configurations = schemaIni.parse();
		HashMap<String, HashSet<String>> ranges = new HashMap<String, HashSet<String>>();
		
		for(MerchantConfiguration configuration : configurations){
			HashMap<String, SchemaAnnotation> columns = configuration.columns();
			for(String column : columns.keySet()){
				SchemaAnnotation annotation = columns.get(column);
				if(!annotation.type().isEmpty()){
					if(ranges.containsKey(annotation.annotation())){
						ranges.get(annotation.annotation()).add(asRange(annotation.type()));
					}
					else{
						HashSet<String> types = new HashSet<String>();
						types.add(asRange(annotation.type()));
						ranges.put(annotation.annotation(), types);
					}
				}
			}
		}
		
		String folder = new IndexesPath().ecommerceRanges().path();
		FileUtils.deleteQuietly(new File(folder));
		FileUtils.forceMkdir(new File(folder));
		for(String property : ranges.keySet()){
			FileUtils.writeLines(new File(folder + "/" + property.replace("/", "_")), "UTF-8", ranges.get(property));
		}
	}

	private static String asRange(String type) {
		return type + "|0|0|0";
	}

}
