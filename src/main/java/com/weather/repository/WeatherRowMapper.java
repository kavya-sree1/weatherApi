package com.weather.repository;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.weather.model.Weather;

public class WeatherRowMapper implements RowMapper<Weather> {

    @Override
    public Weather mapRow(ResultSet rs, int rowNum) throws SQLException {
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
    }
}



