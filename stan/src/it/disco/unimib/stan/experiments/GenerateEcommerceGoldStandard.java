package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class GenerateEcommerceGoldStandard {
	
	private Resource schemaIni;
	private ArrayList<String> listings;
	
	public GenerateEcommerceGoldStandard(Resource schemaIni, ArrayList<String> listings) {
		this.schemaIni = schemaIni;
		this.listings = listings;
	}

	public GoldStandard generate() throws Exception {
		GoldStandard goldStandard = new GoldStandard("ecommerce-gold-standard");
		
		ArrayList<MerchantConfiguration> configurations = new SchemaIni(this.schemaIni).parse();
		for (MerchantConfiguration configuration : configurations) {
			if(configuration.delimiter().contains("|") && listingAvailable(configuration.merchantName())){
				HashMap<String, SchemaAnnotation> columns = configuration.columns();
				for (String column : columns.keySet()) {
					goldStandard.addEntry(configuration.merchantName() + "-" + column, columns.get(column).annotation(), 1);
				}
			}
		}
		
		return goldStandard;
	}

	private boolean listingAvailable(String listingName) {
		return this.listings.contains(listingName);
	}
}
