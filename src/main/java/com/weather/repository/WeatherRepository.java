package com.weather.repository;

import java.util.List;

import com.weather.exceptions.WeatherException;
import com.weather.model.Weather;

public interface WeatherRepository {
	void createWeatherTable();
	Weather findByPincode(Long pincode) throws WeatherException;
	int save(Weather weather) throws WeatherException;
	int deleteByPincode(long pincode) throws WeatherException;
	int update(Weather weather) throws WeatherException;
	List<Weather> findAll();
}
