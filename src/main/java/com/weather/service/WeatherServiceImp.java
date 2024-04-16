package com.weather.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import com.weather.exceptions.WeatherException;
import com.weather.model.ErrorData;
import com.weather.model.Weather;
import com.weather.repository.WeatherRepository;
@Service
public class WeatherServiceImp implements WeatherService {
	@Autowired
	private WeatherRepository weatherRepository;
	
	@Override
	public void createWeatherTable() {
		weatherRepository.createWeatherTable();
	}
	@Override
    public Weather findByPincode(Long pincode) throws WeatherException {
        try {
            return weatherRepository.findByPincode(pincode);
        } catch (WeatherException e) {
		    ErrorData errorData=new ErrorData("ERR-404","404 Not Found");
			throw new WeatherException(errorData,"Weather not Found for Pincode");
        }
    }	
	@Override
    public int save(Weather weather) throws WeatherException {
        validateWeather(weather);
        try {
            return weatherRepository.save(weather);
        } catch (WeatherException e) {
			ErrorData errorData=new ErrorData("ERR-409","409 Conflict");
			throw new WeatherException(errorData,"Weather is already present for pincode");	
        }
    }

    @Override
    public int deleteByPincode(long pincode) throws WeatherException {
        try {
            return weatherRepository.deleteByPincode(pincode);
        } catch (WeatherException e) {
		    ErrorData errorData=new ErrorData("ERR-404","404 Not Found");
			throw new WeatherException(errorData,"No weather data found for pincode");	
        }
    }

    @Override
    public int update(Weather weather) throws WeatherException {
        validateWeather(weather);
        return weatherRepository.update(weather);
    }

    public void validateWeather(Weather weather) throws WeatherException {
        if (weather.getAreaName().length() > 30) {
        	ErrorData errorData=new ErrorData("ERR-400","400 Bad Request");
            throw new WeatherException(errorData,"Area name should be less than 30 characters");
        }
        if (!(validTemperatureCheck(weather.getMaxTemp())) || !(validTemperatureCheck(weather.getMinTemp()))) {
        	ErrorData errorData=new ErrorData("ERR-400","400 Bad Request");
            throw new WeatherException(errorData,"Temperature should be in ##.## format");
        }
        if (weather.getMaxTemp() > 35.0 || weather.getMinTemp() > 35.0) {
            weather.setDanger(true);
        } 
    }
	public boolean validTemperatureCheck(double temperature) {
		return String.valueOf(temperature).matches("\\d{1,2}\\.\\d{1,2}");
	}
	@Override
	public List<Weather> findAll() {
		List<Weather> weatherList=weatherRepository.findAll();
		for(Weather weather:weatherList) {
			if (weather.getMaxTemp() > 35.0 || weather.getMinTemp() > 35.0) {
            weather.setDanger(true);
		}
		}
		return weatherList;
	}
}




