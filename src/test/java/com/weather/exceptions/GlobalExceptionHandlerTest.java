package com.weather.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.weather.exceptions.GlobalExceptionHandler;
import com.weather.exceptions.WeatherException;
import com.weather.model.ErrorData;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    
    @BeforeEach
    public void setup() {
        exceptionHandler = new GlobalExceptionHandler();
    }
    
    @Test
    public void testHandleWeatherException() {
    	 WeatherException weatherException = mock(WeatherException.class);
         ErrorData errorData = new ErrorData("ERR-404", "Not Found"); 
         when(weatherException.getErrorData()).thenReturn(errorData);
         ResponseEntity<ErrorData> responseEntity = exceptionHandler.handleWeatherException(weatherException);
         assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
         assertEquals("ERR-404", responseEntity.getBody().getErrorCode());
    }
    
    @Test
    public void testDetermineHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, exceptionHandler.determineHttpStatus("ERR-400"));
        assertEquals(HttpStatus.NOT_FOUND, exceptionHandler.determineHttpStatus("ERR-404"));
        assertEquals(HttpStatus.CONFLICT, exceptionHandler.determineHttpStatus("ERR-409"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exceptionHandler.determineHttpStatus("ERR-500"));     }
}
