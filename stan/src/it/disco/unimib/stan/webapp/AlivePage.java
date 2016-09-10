package it.disco.unimib.stan.webapp;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;


public class AlivePage implements Page{

	File version = new File("./version");
	
	@Before
	public void setUp(){
		FileUtils.deleteQuietly(version);
	}

	@Override
	public String route() {
		return "alive";
	}

	@Override
	public String process(Communication requestAndResponse) throws IOException	{
		return new Template("alive")
						.content(FileUtils.readLines(version).get(0))
						.breadcrumb("Alive")
						.page();
	}

}
