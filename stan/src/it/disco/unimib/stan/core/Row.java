package it.disco.unimib.stan.core;

import java.util.ArrayList;

public class Row {

	public ArrayList<String> cells;
	
	public Row() {
		this.cells = new ArrayList<String>();
	}

	public void withCell(String cell) {
		this.cells.add(cell);
	}

	public ArrayList<String> cells() {
		return this.cells;
	}
}
