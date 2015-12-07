package it.disco.unimib.stan.unit;

import it.disco.unimib.stan.webapp.Communication;
import it.disco.unimib.stan.webapp.UploadedResource;

import java.util.ArrayList;
import java.util.HashMap;

public class CommunicationTestDouble implements Communication {
	
	private HashMap<String, String> parameters;
	private HashMap<String, UploadedResource> uploads;
	private int responseStatus;
	private ArrayList<String> enabledCheckboxes;
	private HashMap<String, String> cookies;
	
	public CommunicationTestDouble(){
		this.parameters = new HashMap<String, String>();
		this.cookies = new HashMap<String, String>();
		this.uploads = new HashMap<String, UploadedResource>();
		this.enabledCheckboxes = new ArrayList<String>();
	}
	
	@Override
	public String getParameter(String parameter) {
		return (parameters.containsKey(parameter)) ? parameters.get(parameter) : "";
	}
	
	@Override
	public UploadedResource getUploadedFile(String parameter) {
		return this.uploads.get(parameter);
	}
	
	@Override
	public boolean isCheckBoxEnabled(String parameter) {
		return this.enabledCheckboxes.contains(parameter);
	}
	
	public CommunicationTestDouble withParameter(String name, String value) {
		this.parameters.put(name, value);
		return this;
	}

	public CommunicationTestDouble withUploadedResource(String name, UploadedFileTestDouble uploadedTable) {
		this.uploads.put(name, uploadedTable);
		return this;
	}
	
	public CommunicationTestDouble withCheckBoxEnabled(String parameter) {
		this.enabledCheckboxes.add(parameter);
		return this;
	}
	
	@Override
	public void setResponseStatus(int status) {
		this.responseStatus = status;
	}

	public int responseStatus() {
		return this.responseStatus;
	}

	@Override
	public void sendRedirect(String path, String parameter, String value) throws Exception { }

	@Override
	public void setContentType(String contentType) {
	}

	@Override
	public CommunicationTestDouble setCookie(String name, String value) {
		this.cookies.put(name, value);
		return this;
	}

	@Override
	public String getCookie(String name) {
		return this.cookies.get(name);
	}
}
