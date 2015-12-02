package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.Resource;

import java.util.ArrayList;
import java.util.List;

public class SchemaIni {

	private Resource resource;
	private ArrayList<MerchantConfiguration> configurations;

	public SchemaIni(Resource schemaIni) {
		this.resource = schemaIni;
		this.configurations = new ArrayList<MerchantConfiguration>();
	}

	public ArrayList<MerchantConfiguration> parse() throws Exception {
		ArrayList<MerchantConfiguration> configurations = new ArrayList<MerchantConfiguration>();
		ArrayList<String> lines = this.resource.lines();
		int start = 0;
		int end = 0;
		boolean lookingForStart = true;

		for(int i = 0; i < lines.size(); i++){
			String line = lines.get(i);
			if(line.startsWith("[") && lookingForStart){
				start = i;
				lookingForStart = false;
				continue;
			}
			if(line.equalsIgnoreCase("") && !lookingForStart){
				end = i;
				lookingForStart = true;
				MerchantConfiguration configuration = parseConfiguration(lines.subList(start, end));
				if(notAlreadyExists(configuration.merchantName(), configurations)){
					configurations.add(configuration);
				}
			}
			if(i == lines.size()-1){
				MerchantConfiguration configuration = parseConfiguration(lines.subList(start, i+1));
				if(notAlreadyExists(configuration.merchantName(), configurations)){
					configurations.add(configuration);
				}
			}
		}
		this.configurations = configurations;
		return configurations;
	}
	
	public MerchantConfiguration get(String merchant) {
		for(MerchantConfiguration configuration : configurations){
			if(configuration.merchantName().equals(merchant)){
				return configuration;
			}
		}
		return null;
	}

	private boolean notAlreadyExists(String merchantName, ArrayList<MerchantConfiguration> configurations) {
		for(MerchantConfiguration configuration : configurations){
			if(configuration.merchantName().equals(merchantName)){
				return false;
			}
		}
		return true;
	}

	private MerchantConfiguration parseConfiguration(List<String> configuration) {
		MerchantConfiguration parsedConfiguration = new MerchantConfiguration();
		for (String line : configuration) {
			if(line.startsWith("[")){
				parsedConfiguration.setMerchant(line);
			}
			if(line.startsWith("Col") && !(line.startsWith("ColNameHeader"))){
				parsedConfiguration.addColumn(line);
			}
			if(line.startsWith("ColNameHeader")){
				parsedConfiguration.setHeader(line.split("=")[1]);
			}
			if(line.startsWith("Format=")){
				parsedConfiguration.setDelimiter(line.split("=")[1]);
			}
		}
		return parsedConfiguration;
	}
}
