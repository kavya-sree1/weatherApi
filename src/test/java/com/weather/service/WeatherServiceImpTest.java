package com.weather.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.weather.exceptions.WeatherException;
import com.weather.model.Weather;
import com.weather.repository.WeatherRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImpTest {

	@Mock
	WeatherRepository weatherRepository;

	@InjectMocks
	WeatherServiceImp weatherService;

	private Weather validWeather;
	private Weather invalidWeather;

	@BeforeEach
	public void setUp() {
		validWeather = new Weather(505452L, "Karimnagar", 35.5, 20.5, "High", "Medium", "Strong", false);
	}

	@Test
	void testCreateWeatherTable() {
		weatherService.createWeatherTable();
		verify(weatherRepository, times(1)).createWeatherTable();
	}

	@Test
    void testFindByPincode_Success() throws WeatherException {
        when(weatherRepository.findByPincode(505452L)).thenReturn(validWeather);
        Weather result = weatherService.findByPincode(505452L);
        assertEquals(validWeather, result);
    }

	@Test
    void testFindByPincode_NotFound() {
        when(weatherRepository.findByPincode(99999L)).thenThrow(WeatherException.class);
        WeatherException exception=assertThrows(WeatherException.class, () -> weatherService.findByPincode(99999L));
        assertEquals("ERR-404", exception.getErrorData().getErrorCode());
        assertEquals("404 Not Found", exception.getErrorData().getErrorDesc());
        assertEquals("Weather not Found for Pincode", exception.getMessage());
    }

	@Test
    void testSave_Success() throws WeatherException {
        when(weatherRepository.save(validWeather)).thenReturn(1);
        int result = weatherService.save(validWeather);
        assertEquals(1, result);
    }

	@Test
    void testSave_Conflict() {
        when(weatherRepository.save(validWeather)).thenThrow(WeatherException.class);
        WeatherException exception=assertThrows(WeatherException.class, () -> weatherService.save(validWeather));
        assertEquals("ERR-409", exception.getErrorData().getErrorCode());
        assertEquals("409 Conflict", exception.getErrorData().getErrorDesc());
        assertEquals("Weather is already present for pincode", exception.getMessage());
    }

	@Test
    void testDeleteByPincode_Success() throws WeatherException {
        when(weatherRepository.deleteByPincode(12345L)).thenReturn(1);
        int result = weatherService.deleteByPincode(12345L);
        assertEquals(1, result);
    }

	@Test
    void testDeleteByPincode_NotFound() {
        when(weatherRepository.deleteByPincode(99999L)).thenThrow(WeatherException.class);
        WeatherException exception=assertThrows(WeatherException.class, () -> weatherService.deleteByPincode(99999L));
        assertEquals("ERR-404", exception.getErrorData().getErrorCode());
        assertEquals("404 Not Found", exception.getErrorData().getErrorDesc());
        assertEquals("No weather data found for pincode", exception.getMessage());
    }

	@Test
	void testUpdate_Success() throws WeatherException {
		int expectedResult = 1;
		when(weatherRepository.update(validWeather)).thenReturn(expectedResult);
		int result = weatherService.update(validWeather);
		assertEquals(expectedResult, result);
	}

	@Test
	void testFindAll() {
		List<Weather> weatherList = new ArrayList<>();
		weatherList.add(validWeather);
		when(weatherRepository.findAll()).thenReturn(weatherList);
		List<Weather> result = weatherService.findAll();
		assertEquals(weatherList.size(), result.size());
		assertEquals(validWeather, result.get(0));
		if (validWeather.getMaxTemp() > 35.0 || validWeather.getMinTemp() > 35.0) {
            assertTrue(result.get(0).isDanger());
        }
	}
	
	 @Test
	    void testValidateWeather_ValidData() {
			validWeather = new Weather(505432L, "Karimnagar", 34.5, 23.5, "High", "Medium","Strong", false);
	        assertDoesNotThrow(() -> weatherService.validateWeather(validWeather),
	                "No exception expected for valid weather data");
	        assertFalse(validWeather.isDanger(), "Danger flag should be false for valid data");
	    }

	    @Test
	    void testValidateWeather_InvalidAreaNameLength() {
			invalidWeather = new Weather(505432L, "Invalid Area Name Exceeding 30 Characters", 34.5, 20.5, "High", "Medium","Strong", true);
	        WeatherException exception = assertThrows(WeatherException.class,
	                () -> weatherService.validateWeather(invalidWeather),
	                "Expected exception when area name is too long");
	        assertEquals("Area name should be less than 30 characters", exception.getMessage());
	    }

	    @Test
	    void testValidateWeather_InvalidTemperatureFormat() {
			invalidWeather = new Weather(505432L, "Karimnagar", 345.5, 204.5, "High", "Medium","Strong", true);
	        WeatherException exception = assertThrows(WeatherException.class,
	                () -> weatherService.validateWeather(invalidWeather),
	                "Expected exception when temperature has invalid format");
	        assertEquals("Temperature should be in ##.## format", exception.getMessage());
	    }
	    
	    @Test
	    void testSetDangerFlag() {
	        validWeather.setMaxTemp(40.0); 
	        validWeather.setMinTemp(30.0);
	        assertFalse(validWeather.isDanger(), "Initial danger flag should be false");
	        weatherService.validateWeather(validWeather);
	        assertTrue(validWeather.isDanger(), "Danger flag should be true as maxTemp is greater than 35.0");
	    }
	    
	}

	


