package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.Resource;

import java.io.IOException;

public interface UploadedResource {

	String name();

	void save(String path) throws IOException;

	Resource uploadedResource();

}
