package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.Row;
import it.disco.unimib.stan.core.SemanticAnnotation;

import java.util.ArrayList;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

public class Template{
	
	private ST template;
	private Notifications notifications;

	public Template(String pageName) {
		STGroupDir stGroupDir = new STGroupDir("./templates", '$', '$');
		template = stGroupDir.getInstanceOf(pageName);
		notifications = new Notifications();
	}

	public String page() {
		template.add("notifications", notifications);
		return template.render();
	}
	
	public Template content(String content) {
		template.add("content", content);
		return this;
	}
	
	public Template status(int statusCode) {
		template.add("status", statusCode + "");
		return this;
	}
	
	public Template predictions(ArrayList<SemanticAnnotation> predictions) {
		template.add("predictions", predictions);
		return this;
	}

	public Template breadcrumb(String breadcrumb) {
		template.add("breadcrumb", breadcrumb);
		return this;
	}

	public Template error(String error) {
		notifications.addError(error);
		return this;
	}

	public Template info(String infoMessage) {
		notifications.addInfo(infoMessage);
		return this;
	}

	public Template rows(ArrayList<Row> rows) {
		template.add("rows", rows);
		return this;
	}

	public Template header(ArrayList<Annotation> headers) {
		template.add("header", headers);
		return this;
	}

	public Template currentTableName(String tableName) {
		template.add("tableName", tableName);
		return this;
	}

	public Template hasPreviousWork(boolean hasPrevious) {
		template.add("hasPrevious", hasPrevious);
		return this;
	}
}