package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.rmlProcessor.RMLEngine;
import it.disco.unimib.stan.rmlProcessor.RMLMapping;
import it.disco.unimib.stan.rmlProcessor.RMLMappingFactory;

import java.io.File;

public class RMLProcessor {
	
	public void generateTriples(String mappings, String triples) throws Exception{
		String absolutePath = new File(mappings).getAbsolutePath();
		RMLMapping mapping = RMLMappingFactory.extractRMLMapping(absolutePath);
        new RMLEngine().runRMLMapping(mapping, "", triples, true);
	}

}
