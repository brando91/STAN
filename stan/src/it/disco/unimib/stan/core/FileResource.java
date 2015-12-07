package it.disco.unimib.stan.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class FileResource implements Resource{

	private File file;

	public FileResource(String path) {
		this.file = new File(path);
	}

	@Override
	public ArrayList<String> lines() throws IOException {
		return (ArrayList<String>) FileUtils.readLines(this.file, new EncodingDetector(this.file).encoding());
	}
	
	@Override
	public ArrayList<String> linesEncoded(String encoding) throws IOException {
		return (ArrayList<String>) FileUtils.readLines(this.file, encoding);
	}
	
	@Override
	public LineIterator lineIterator(String encoding) throws IOException {
		return FileUtils.lineIterator(this.file, encoding);
	}
	
	@Override
	public String name() {
		return this.file.getName();
	}

	public void delete() {
		this.file.delete();
	}

	public String path() throws IOException {
		return this.file.getCanonicalPath();
	}

	public File asFile() {
		return this.file;
	}

	public ArrayList<FileResource> listResources() {
		ArrayList<FileResource> resources = new ArrayList<FileResource>();
		File[] files = this.file.listFiles();
		if(files != null){
			for(File file : files){
				resources.add(new FileResource(file.getPath()));
			}
		}
		return resources;
	}
}
