package it.disco.unimib.stan.unit;

import org.eclipse.jetty.util.log.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class NoLogging implements Logger, ErrorHandler {

	//Suppress Jetty warnings
	@Override public String getName() { return "no"; }
    @Override public void warn(String msg, Object... args) { }
    @Override public void warn(Throwable thrown) { }
    @Override public void warn(String msg, Throwable thrown) { }
    @Override public void info(String msg, Object... args) { }
    @Override public void info(Throwable thrown) { }
    @Override public void info(String msg, Throwable thrown) { }
    @Override public boolean isDebugEnabled() { return false; }
    @Override public void setDebugEnabled(boolean enabled) { }
    @Override public void debug(String msg, Object... args) { }
    @Override public void debug(Throwable thrown) { }
    @Override public void debug(String msg, Throwable thrown) { }
    @Override public Logger getLogger(String name) { return this; }
    @Override public void ignore(Throwable ignored) { }
    
    //Suppress XML DOM warnings
	@Override public void error(SAXParseException arg0) throws SAXException { }
	@Override public void fatalError(SAXParseException arg0) throws SAXException { }
	@Override public void warning(SAXParseException arg0) throws SAXException { }

}
