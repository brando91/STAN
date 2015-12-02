package it.disco.unimib.stan.unit;

import it.disco.unimib.stan.core.LogEvents;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.BeforeSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.extensions.cpsuite.ClasspathSuite.IncludeJars;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@IncludeJars(true)
@ClassnameFilters({"it.disco.unimib.stan.unit.*"})
public class TestSuite {

	@BeforeSuite
	public static void setLogging() throws Exception{
		File logs = new File("logs");
		FileUtils.deleteDirectory(logs);
		FileUtils.forceMkdir(logs);
		new LogEvents();
	}
}
