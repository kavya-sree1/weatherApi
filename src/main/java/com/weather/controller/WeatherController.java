package com.weather.controller;

import com.weather.exceptions.WeatherException;
import com.weather.model.Weather;
import com.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather")

public class WeatherController {
	@Autowired
	private WeatherService weatherService;
    
	@GetMapping("/")
    public ResponseEntity<String> rootEndpoint() {
        return ResponseEntity.ok("Hello! Welcome to our Weather Api");
    }
	
	@PostMapping("/createTable")
	public ResponseEntity<String> createWeatherTable() {
		weatherService.createWeatherTable();
		return ResponseEntity.ok("Weather table created successfully");
	}
    
	@GetMapping("/findByPincode/{pincode}")
	public ResponseEntity<Object> findByPincode(@PathVariable Long pincode) throws WeatherException {
		Weather weather = weatherService.findByPincode(pincode);
		return ResponseEntity.ok(weather);
	}
    
	@PostMapping("/save")
	public ResponseEntity<Object> save(@RequestBody Weather weather) throws WeatherException {
		int result = weatherService.save(weather);
		return ResponseEntity.ok(result);
	}
	
	@DeleteMapping("/deleteByPincode/{pincode}")
	public ResponseEntity<Object> deleteByPincode(@PathVariable Long pincode) throws WeatherException {
		int result = weatherService.deleteByPincode(pincode);
		return ResponseEntity.ok(result);

	}
	@PutMapping("/update")
	public ResponseEntity<Object> update(@RequestBody Weather weather) throws WeatherException {
		int result = weatherService.update(weather);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/findAll")
	public ResponseEntity<List<Weather>> findAll() {
		List<Weather> weatherList = weatherService.findAll();
		return ResponseEntity.ok(weatherList);
	}

}
