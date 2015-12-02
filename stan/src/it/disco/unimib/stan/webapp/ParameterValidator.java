package it.disco.unimib.stan.webapp;

public class ParameterValidator{
	
	private String[] mandatoryParameters;
	private String missingParameter;

	public ParameterValidator(String... mandatoryParameters) {
		this.mandatoryParameters = mandatoryParameters;
		this.missingParameter = "";
	}

	public boolean isCorrectRequest(Communication requestAndResponse) {
		for(String parameter : mandatoryParameters){
			if(nullOrEmpty(requestAndResponse.getParameter(parameter)) && isNull(requestAndResponse.getUploadedFile(parameter))){
				missingParameter = parameter; 
				return false;
			}
		}
		return true;
	}

	public String missingParameter() {
		return this.missingParameter;
	}

	private boolean isNull(Object object) {
		return object == null;
	}
	
	private boolean nullOrEmpty(String string) {
		return (isNull(string) || string.isEmpty());
	}
}