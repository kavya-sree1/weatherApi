package com.weather.model;

public class ErrorData {
	private String errorCode;
	private String errorDesc;
	public ErrorData(String errorCode, String errorDesc) {
		super();
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	

}
