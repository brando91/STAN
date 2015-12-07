package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.EncodingDetector;
import it.disco.unimib.stan.core.Resource;
import it.disco.unimib.stan.core.VirtualResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class UploadedFile implements UploadedResource{

	private String name;
	private List<String> lines;

	public UploadedFile(Part part) throws IOException {
		name = getFileName(part);
	    lines = IOUtils.readLines(part.getInputStream(), new EncodingDetector(part.getInputStream()).encoding());
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public void save(String path) throws IOException {
		FileUtils.writeLines(new File(path), "UTF-8", lines);
	}

	@Override
	public Resource uploadedResource() {
		return new VirtualResource()
						.withName(name)
						.addLines(lines);
	}
	
	private String getFileName(Part part) {
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	            return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1);
	        }
	    }
	    return "";
	}
}
