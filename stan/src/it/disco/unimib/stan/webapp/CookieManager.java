package it.disco.unimib.stan.webapp;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

public class CookieManager {

	private Communication communication;
	final String tableCookie = "table";
	final String userCookie = "user";

	public CookieManager(Communication requestAndResponse) {
		this.communication = requestAndResponse;
	}
	
	public void setTable(String tableName) {
		setCookie(tableCookie, encrypt(tableName));
	}

	public String setUserIfMissing() {
		String current = getCookie(userCookie);
		if(current == null){
			String randomUser = randomUser();
			setCookie(userCookie, randomUser);
			return randomUser;
		}
		return current;
	}

	public String getUser() {
		return getCookie(userCookie);
	}
	
	public String getTable() {
		String cookie = getCookie(tableCookie);
		return (cookie == null) ? null : decrypt(cookie);
	}

	private void setCookie(String name, String value) {
		this.communication.setCookie(name, value);
	}
	
	private String getCookie(String name) {
		return this.communication.getCookie(name);
	}
	
	private String randomUser() {
		return UUID.randomUUID().toString();
	}
	
	private String encrypt(String value) {
		return new String(Base64.encodeBase64String(value.getBytes()));
	}
	
	private String decrypt(String value) {
		return new String(Base64.decodeBase64(value));
	}
}
