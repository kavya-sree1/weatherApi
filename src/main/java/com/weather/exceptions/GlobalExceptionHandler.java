package com.weather.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.weather.model.ErrorData;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(WeatherException.class)
    public ResponseEntity<ErrorData> handleWeatherException(WeatherException ex) {
        ErrorData errorData=new ErrorData(ex.getErrorData().getErrorCode(),ex.getMessage());
        HttpStatus status=determineHttpStatus(errorData.getErrorCode());
        return ResponseEntity.status(status).body(errorData);
    }  
	public  HttpStatus determineHttpStatus(String errorCode) {
        switch (errorCode) {
            case "ERR-400":
                return HttpStatus.BAD_REQUEST;
            case "ERR-404":
                return HttpStatus.NOT_FOUND;
            case "ERR-409":
                return HttpStatus.CONFLICT;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
	}
}





