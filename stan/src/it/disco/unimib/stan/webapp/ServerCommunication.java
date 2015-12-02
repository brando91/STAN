package it.disco.unimib.stan.webapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
public class ServerCommunication implements Communication{

	private HttpServletRequest request;
	private HttpServletResponse response;

	public ServerCommunication(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	@Override
	public String getParameter(String parameter){
		String payload = request.getParameter(parameter);
		return (payload == null) ? "" : payload;
	}
	
	@Override
	public boolean isCheckBoxEnabled(String parameter) {
		String checkbox = request.getParameter(parameter);
		return checkbox != null && checkbox.equals("on");
	}
	
	@Override
	public UploadedResource getUploadedFile(String parameter) {
		try{
			return new UploadedFile(request.getPart(parameter));
		}
		catch(Exception e){
			return null;
		}
	}
	
	@Override
	public void setResponseStatus(int status) {
		this.response.setStatus(status);
	}

	@Override
	public void sendRedirect(String path, String parameter, String value) throws Exception {
		this.response.sendRedirect(path + "?" + parameter + "=" + urlEncode(value));
	}

	@Override
	public void setContentType(String contentType) {
		this.response.setContentType(contentType);
	}

	@Override
	public ServerCommunication setCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(tenYears());
		response.addCookie(cookie);
		return this;
	}
	
	@Override
	public String getCookie(String name) {
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies){
			if(cookie.getName().equals(name)){
				return cookie.getValue();
			}
		}
		return null;
	}

	private int tenYears() {
		return 10*365*24*60*60*1000;
	}
	
	private String urlEncode(String parameter) throws UnsupportedEncodingException {
		return URLEncoder.encode(parameter, "UTF-8");
	}
}
