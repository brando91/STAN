package it.disco.unimib.labeller.unit;

import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.tools.OutputFile;

import java.util.ArrayList;
import java.util.List;

public class OutputFileTestDouble implements OutputFile{

	private List<String> lines = new ArrayList<String>();
	
	@Override
	public OutputFile write(String line) throws Exception {
		lines.add(line);
		return this;
	}
	
	public List<String> getWrittenLines(){
		return lines;
	}

	public InputFile asToInputFile() {
		InputFileTestDouble input = new InputFileTestDouble();
		for(String line : lines){
			input.withLine(line);
		}
		return input;
	}
}