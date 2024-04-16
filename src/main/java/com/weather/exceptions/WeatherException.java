package com.weather.exceptions;

import com.weather.model.ErrorData;

public class WeatherException extends RuntimeException{
	private ErrorData errorData;
	 public WeatherException(ErrorData errorData,String message) {
	        super(message);
	        this.errorData=errorData;
	    }
	 
	 public ErrorData getErrorData() {
		 return errorData;
	 }
}





