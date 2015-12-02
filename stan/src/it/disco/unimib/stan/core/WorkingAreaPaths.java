package it.disco.unimib.stan.core;

public class WorkingAreaPaths {
	
	private String path;

	public WorkingAreaPaths() {
		this.path = "./working-area";
	}

	public WorkingAreaPaths file(String name) {
		this.path += "/" + name;
		return this;
	}

	public String path() {
		return this.path;
	}
	
	public WorkingAreaPaths tables(String user) {
		this.path += "/tables/" + user;
		return this;
	}

	public WorkingAreaPaths schemas(String user) {
		this.path += "/schemas/" + user;
		return this;
	}

	public WorkingAreaPaths mappings(String user) {
		this.path += "/mappings/" + user;
		return this;
	}

	public WorkingAreaPaths toDownload() {
		this.path += "/toDownload";
		return this;
	}

}
