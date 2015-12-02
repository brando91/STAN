package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.FileResource;

import java.io.File;
import java.util.ArrayList;

class GenerateGoldStandard {

	public static void main(String[] args) throws Exception {
		FileResource schemaIni = new FileResource(new DataPaths().ecommerce().file("schema.ini").path());
		File listingsDir = new File(new DataPaths().ecommerce().listings().path());
		ArrayList<String> listingsAvailable = new ArrayList<String>();
		for(File file : listingsDir.listFiles()){
			listingsAvailable.add(file.getName());
		}
		
		new GoldStandard("ecommerce-gold-standard").delete();
		new GenerateEcommerceGoldStandard(schemaIni, listingsAvailable).generate().save();
	}

}
