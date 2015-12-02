package it.disco.unimib.stan.experiments;


public class DataPaths {
	
	private String path;

	public DataPaths() {
		this.path = "../data";
	}

	public DataPaths ecommerce() {
		this.path += "/ecommerce";
		return this;
	}

	public DataPaths file(String fileName) {
		this.path += "/" + fileName;
		return this;
	}

	public String path() {
		return this.path;
	}

	public DataPaths listings() {
		this.path += "/listings";
		return this;
	}

	public DataPaths chicago() {
		this.path += "/chicago";
		return this;
	}

	public DataPaths chicagoDataset() {
		this.path += "/chicago-dataset";
		return this;
	}

	public DataPaths test() {
		this.path += "/test";
		return this;
	}

}
