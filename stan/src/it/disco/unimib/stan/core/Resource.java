package it.disco.unimib.stan.core;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.LineIterator;

public interface Resource {

	String name();
	
	ArrayList<String> lines() throws IOException;

	ArrayList<String> linesEncoded(String encoding) throws IOException;

	LineIterator lineIterator(String encoding) throws IOException;

}
