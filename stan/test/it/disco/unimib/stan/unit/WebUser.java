package it.disco.unimib.stan.unit;

import it.disco.unimib.stan.core.FileResource;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class WebUser {

	private WebClient browser;
	private String address;
	private HtmlPage page;
	private Page jsonPage;

	public WebUser(String address) {
		this.browser = new WebClient(BrowserVersion.FIREFOX_38);
		this.browser.getOptions().setTimeout(tenMinutes());
		this.browser.setAjaxController(new NicelyResynchronizingAjaxController());
		this.address = address;
	}

	public void close() {
		this.browser.close();
	}
	
	public WebUser browseTo(String page) throws Exception{
		this.page = browser.<HtmlPage>getPage(composeLocation(page));
		return this;
	}

	public String pageContent(){
		return page.asText();
	}
	
	public String pageHTML() {
		return page.asXml();
	}
	
	public String pageJSON(){
		return jsonPage.getWebResponse().getContentAsString();
	}
	
	public WebUser type(String formName, String fieldName, String content) throws Exception{
		inForm(formName).getInputByName(fieldName).type(content);
		return this;
	}
	
	public WebUser check(String formName, String fieldName) {
		inForm(formName).getInputByName(fieldName).setChecked(true);
		return this;
	}
	
	public WebUser clickOn(String formName, String buttonName) throws Exception{
		page = inForm(formName).getButtonByName(buttonName).click();
		return this;
	}
	
	public WebUser clickOn(String elementName) throws Exception{
		page = page.getAnchorByName(elementName).click();
		return this;
	}
	
	public WebUser selectOptionOn(String formName, String dropdownName, String option){
		HtmlSelect dropdown = inDropdown(formName, dropdownName);
		dropdown.setSelectedAttribute(dropdown.getOptionByText(option), true);
		return this;
	}
	
	public ArrayList<String> getOptions(String formName, String dropdownName){
		ArrayList<String> options = new ArrayList<String>();
		for(HtmlOption option : inDropdown(formName, dropdownName).getOptions()){
			options.add(option.getText());
		}
		return options;
	}
	
	private HtmlSelect inDropdown(String formName, String dropdownName) {
		HtmlSelect dropdown = inForm(formName).getSelectByName(dropdownName);
		return dropdown;
	}
	
	public WebUser upload(String formName, String fieldName, FileResource file) throws Exception{
		inForm(formName).getInputByName(fieldName).setValueAttribute(file.path());
		return this;
	}
	
	public String getElementTextById(String elementId) {
		return elementById(elementId).asText();
	}
	
	public String getElementAttributeById(String elementId, String attributeName) {
		return elementById(elementId).getAttribute(attributeName);
	}
	
	public WebUser setHiddenInputValue(String inputId, String value) {
		((HtmlHiddenInput) elementById(inputId)).setValueAttribute(value);
		return this;
	}

	public WebUser waitForElementToShow(String element) throws Exception {
		for(int i = 0; i < 10; i++){
			if(pageContent().contains(element)){
				return this;
			}
			Thread.sleep(1000);
		}
		throw new Exception("'" + element + "' not found.");
	}
	
	public String currentURL() {
		return page.getUrl().toString();
	}

	public int statusCode() {
		return page.getWebResponse().getStatusCode();
	}
	
	public String getCookie(String name) {
		return browser.getCookieManager().getCookie(name).getValue();
	}

	public void makePOST(POSTBuilder post) throws Exception {
		WebRequest requestSettings = new WebRequest(new URL(composeLocation(post.route())), HttpMethod.POST);

		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new NameValuePair("data", post.data()));
		HashMap<String, String> postParameters = post.parameters();
		for(String parameter : postParameters.keySet()){
			parameters.add(new NameValuePair(parameter, postParameters.get(parameter)));
		}
		requestSettings.setRequestParameters(parameters);
	
		jsonPage = browser.getPage(requestSettings);
	}
	
	private HtmlForm inForm(String formName) {
		return page.getFormByName(formName);
	}
	
	private DomElement elementById(String elementId) {
		return page.getElementById(elementId);
	}
	
	private int tenMinutes() {
		return 10*60*1000;
	}

	private String composeLocation(String page) {
		return address + "/" + page;
	}
}
