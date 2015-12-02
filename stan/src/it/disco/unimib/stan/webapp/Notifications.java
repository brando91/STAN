package it.disco.unimib.stan.webapp;

import java.util.ArrayList;


public class Notifications {

	public ArrayList<String> errors;
	public ArrayList<String> infos;

	public Notifications() {
		this.errors = new ArrayList<String>();
		this.infos = new ArrayList<String>();
	}
	
	public void addError(String error) {
		if(notEmpty(error)) this.errors.add(error);
	}

	public void addInfo(String info) {
		if(notEmpty(info)) this.infos.add(info);
	}

	private boolean notEmpty(String info) {
		return !info.equals("");
	}

}
