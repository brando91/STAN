package it.disco.unimib.stan.unit;

import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.Resource;
import it.disco.unimib.stan.webapp.UploadedResource;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class UploadedFileTestDouble implements UploadedResource{

	private String name;
	private Resource resource;

	public UploadedFileTestDouble(String name, Resource resource) {
		this.name = name;
		this.resource = resource;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public void save(String path) throws IOException {
		FileUtils.writeLines(new File(path), "UTF-8", this.resource.lines());
	}

	@Override
	public Resource uploadedResource() {
		return this.resource;
	}
	
	public FileResource toUpload() throws IOException {
		String path = "./tmp/" + this.name;
		this.save(path);
		return new FileResource(path);
	}

}
