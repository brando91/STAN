package it.disco.unimib.stan.webapp;


public interface Communication {
	
	public String getParameter(String parameter);
	
	public UploadedResource getUploadedFile(String parameter);

	public boolean isCheckBoxEnabled(String string);
	
	public void setResponseStatus(int status);

	void sendRedirect(String path, String parameter, String value) throws Exception;
	
	public void setContentType(String contentType);

	public Communication setCookie(String name, String value);

	public String getCookie(String name);
}
