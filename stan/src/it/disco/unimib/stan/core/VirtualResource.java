package it.disco.unimib.stan.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class VirtualResource implements Resource{

	private ArrayList<String> lines;
	private String resourceName;
	
	public VirtualResource() {
		this.lines = new ArrayList<String>();
	}
	
	@Override
	public ArrayList<String> lines() throws IOException {
		return this.lines;
	}
	
	@Override
	public ArrayList<String> linesEncoded(String encoding) throws IOException {
		return this.lines;
	}
	
	@Override
	public LineIterator lineIterator(String encoding) throws IOException {
		File file = new File(new TempPath().file(UUID.randomUUID().toString()).path());
		FileUtils.writeLines(file, encoding, this.lines);
		return FileUtils.lineIterator(file, encoding);
	}
	
	@Override
	public String name() {
		return this.resourceName;
	}

	public VirtualResource addLine(String line) {
		this.lines.add(line);
		return this;
	}
	
	public VirtualResource addLines(List<String> lines) {
		this.lines.addAll(lines);
		return this;
	}

	public VirtualResource withName(String resourceName) {
		this.resourceName = resourceName;
		return this;
	}
}
