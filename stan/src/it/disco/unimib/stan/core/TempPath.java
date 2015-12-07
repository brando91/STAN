package it.disco.unimib.stan.core;

public class TempPath {

	private String path;

	public TempPath() {
		this.path = "/tmp/stan";
	}

	public String path() {
		return this.path;
	}

	public TempPath file(String file) {
		this.path += file;
		return this;
	}
}
