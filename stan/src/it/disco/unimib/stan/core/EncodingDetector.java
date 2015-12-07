package it.disco.unimib.stan.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.mozilla.intl.chardet.nsDetector;

public class EncodingDetector {

	private InputStream inputStream;

	public EncodingDetector(File file) throws IOException {
		this.inputStream = new FileInputStream(file);
	}

	public EncodingDetector(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String encoding() throws IOException {
		byte[] buf = new byte[4096];
		nsDetector detector = new nsDetector();
		int nread = inputStream.read(buf);
		int loops = 0;
	    while (!detector.DoIt(buf, nread, false) && loops < 1000) {
	    	nread = inputStream.read(buf);
	    	detector.HandleData(buf, nread);
	    	loops++;
	    } 
	    
	    detector.DataEnd();
	    inputStream.close();
	    String encoding = detector.getProbableCharsets()[0];
	    detector.Reset();
		return (encoding.equals("nomatch")) ? "UTF-8" : encoding;
	}

}
