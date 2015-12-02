package it.disco.unimib.stan.webapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Context {

	private String context;

	public Context(String context) {
		this.context = context;
	}

	public String cleaned() {
		return StringUtils.join(removeExtension(), " ").replace("_", " ").replace("-", " ");
	}

	private List<String> removeExtension() {
		ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(context.split("\\.")));
		return (tokens.size() > 1) ? tokens.subList(0, tokens.size()-1) : tokens;
	}
}
