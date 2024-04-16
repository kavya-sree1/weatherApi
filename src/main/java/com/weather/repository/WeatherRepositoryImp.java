package com.weather.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.weather.exceptions.WeatherException;
import com.weather.model.ErrorData;
import com.weather.model.Weather;

@Repository
public class WeatherRepositoryImp implements WeatherRepository{
	private final JdbcTemplate jdbcTemplate;

	public WeatherRepositoryImp(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	 public void createWeatherTable() {
	        String sql = "CREATE TABLE IF NOT EXISTS Weather (" + "pincode BIGINT PRIMARY KEY,"+ "area_name VARCHAR(30),"+ "max_temp DOUBLE,"+ "min_temp DOUBLE,"+ "humidity VARCHAR(255)," + "rainfall VARCHAR(255)," + "wind VARCHAR(255)," + "danger BOOLEAN" + ")";
	        jdbcTemplate.execute(sql);
     }
     
	 public Weather findByPincode(Long pincode) throws WeatherException {
		 try {
	        String sql = "SELECT * FROM weather WHERE pincode = ?";
	            Weather weather= jdbcTemplate.queryForObject(sql, new WeatherRowMapper(),new Object[]{pincode} );
	            return weather;  
		 }catch(DataAccessException e) {
			    ErrorData errorData=new ErrorData("ERR-404","404 Not Found");
				throw new WeatherException(errorData,"Weather not Found for Pincode");
		 }
	    }

	public int save(Weather weather) throws WeatherException  {
		 if (weather.getPincode() <= 0) {
		        ErrorData errorData = new ErrorData("ERR-400", "400 Bad Request");
		        throw new WeatherException(errorData, "Pincode is invalid");
		    }
		String sql = "INSERT INTO weather (pincode, area_name, max_temp, min_temp, humidity, rainfall, wind,danger) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?,?)";
					int result;
		try {
			result = jdbcTemplate.update(sql, weather.getPincode(), weather.getAreaName(), weather.getMaxTemp(),
					weather.getMinTemp(), weather.getHumidity(), weather.getRainfall(), weather.getWind(),
					weather.isDanger());
		} catch (DataAccessException e) {
			ErrorData errorData=new ErrorData("ERR-409","409 Conflict");
			throw new WeatherException(errorData,"Weather is already present for pincode");	
		}
		return result;

	}

	public int deleteByPincode(long pincode) throws WeatherException {
		int result;
	    String sql = "DELETE FROM weather WHERE pincode = ?";
		result= jdbcTemplate.update(sql, pincode);
		if(result!=1) {
		  ErrorData errorData=new ErrorData("ERR-404","404 Not Found");
			throw new WeatherException(errorData,"No weather data found for pincode");	
		}
		return result;
	}

	public int update(Weather weather) throws WeatherException {
		int result;
				String sql = "UPDATE WEATHER SET AREA_NAME = ?, MAX_TEMP = ?, MIN_TEMP = ?, HUMIDITY = ?, RAINFALL = ?, WIND = ?, DANGER = ? WHERE PINCODE = ?";
				result = jdbcTemplate.update(sql, weather.getAreaName(), weather.getMaxTemp(), weather.getMinTemp(),
						weather.getHumidity(), weather.getRainfall(), weather.getWind(), weather.isDanger(),
						weather.getPincode());
				if(result!=1) {
					ErrorData errorData=new ErrorData("ERR-404","404 Not Found");
					throw new WeatherException(errorData,"weather not found for pincode");		
			}
			
		return result;	
	}

	public List<Weather> findAll() {
		String sql = "SELECT * FROM weather";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Weather weather = new Weather(
				    rs.getLong("pincode"),
				    rs.getString("area_name"),
				    rs.getDouble("max_temp"),
				    rs.getDouble("min_temp"),
				    rs.getString("humidity"),
				    rs.getString("rainfall"),
				    rs.getString("wind"),
				    rs.getBoolean("danger")
				);

			return weather;
		});
	}

}




