package it.disco.unimib.stan.unit;

import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class ClientCommunication {

	private String host;
	private CloseableHttpClient client;

	public ClientCommunication(String host) {
		this.host = host;
		this.client = HttpClients.createDefault();
	}
	
	public int getStatusCode(String path) throws Exception {
		return httpGet(path).getStatusLine().getStatusCode();
	}

	private CloseableHttpResponse httpGet(String path) throws Exception{
		HttpGet request = new HttpGet();
		request.setURI(new URI(host + "/" + path));
		return this.client.execute(request);
	}
}
