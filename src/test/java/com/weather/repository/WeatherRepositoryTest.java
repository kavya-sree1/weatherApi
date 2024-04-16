package com.weather.repository;
import com.weather.exceptions.WeatherException;
import com.weather.model.ErrorData;
import com.weather.model.Weather;
import com.weather.repository.WeatherRepository;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations="/application-test.properties")
@SpringBootTest  
@Transactional
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class WeatherRepositoryTest {

    @Autowired
    private WeatherRepository weatherRepository; 

    @Autowired
    private DataSource dataSource;

    private IDatabaseConnection dbUnitConnection;

    @BeforeEach
    public void setUp() throws Exception {
    	weatherRepository.createWeatherTable();
        dbUnitConnection = new DatabaseDataSourceConnection(dataSource);
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(getClass().getClassLoader().getResourceAsStream("data.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (dbUnitConnection != null) {
            dbUnitConnection.close();
        }
    }
    @Test
    public void testCreateWeather() {
        Weather weather = new Weather(504492L, "Hyderabad", 25.5, 15.5, "Medium", "Low", "Weak", false);
        int savedWeather = weatherRepository.save(weather);
        assertEquals(1, savedWeather);
    }

    @Test
    public void testFindByPincode_Positive() throws WeatherException {
        long pincode = 505452L;
        Weather weather = weatherRepository.findByPincode(pincode);
        assertNotNull(weather);
        assertEquals(pincode, weather.getPincode());
    }

    @Test
    public void testFindByPincode_Negative() {
        long nonExistingPincode = 999999L;
        WeatherException exception=assertThrows(WeatherException.class, () -> {
            weatherRepository.findByPincode(nonExistingPincode);
        });
        assertEquals("ERR-404", exception.getErrorData().getErrorCode());
        assertEquals("404 Not Found", exception.getErrorData().getErrorDesc());
        assertEquals("Weather not Found for Pincode", exception.getMessage());
    }
    
    @Test
    public void testSaveSuccess() {
        Weather weather = new Weather(505482L, "Hyderabad", 25.5, 15.5, "Medium", "Low", "Weak", false);
        int result=weatherRepository.save(weather);
        assertEquals(1, result);
    }

    @Test
    public void testSaveWeather_DuplicateKeyException() {
        Weather weather = new Weather(505482L, "Hyderabad", 25.5, 15.5, "Medium", "Low", "Weak", false);
        weatherRepository.save(weather);
        Weather duplicateWeather = new Weather(505482L, "Hyderabad", 30.0, 20.0, "High", "Medium", "Strong", true);
        WeatherException exception= assertThrows(WeatherException.class, () -> {
            weatherRepository.save(duplicateWeather);
        });
        assertEquals("ERR-409", exception.getErrorData().getErrorCode());
        assertEquals("409 Conflict", exception.getErrorData().getErrorDesc());
        assertEquals("Weather is already present for pincode", exception.getMessage());
    }
    
    @Test
    public void testSaveWithInvalidPincode() {
	    Weather invalidWeather=new Weather(0L, "Karimnagar", 35.5, 20.5, "High", "Medium", "Strong", false);
        WeatherException exception=assertThrows(WeatherException.class, () -> {
            weatherRepository.save(invalidWeather);
        });
        assertEquals("ERR-400", exception.getErrorData().getErrorCode());
        assertEquals("400 Bad Request", exception.getErrorData().getErrorDesc());
        assertEquals("Pincode is invalid", exception.getMessage()); 
    }

    @Test
    public void testDeleteByPincode_Positive() throws WeatherException{
        Weather weather = new Weather(123456L, "Test Area", 25.0, 15.0, "Medium", "Low", "Weak", false);
        weatherRepository.save(weather);
        int deletedCount = weatherRepository.deleteByPincode(weather.getPincode());
        assertEquals(1, deletedCount);
    }
    
    @Test
    public void testDeleteByPincode_NotFound() {
        long pincode = 99999L;
        WeatherException exception = assertThrows(WeatherException.class, () -> weatherRepository.deleteByPincode(pincode));
        assertEquals("No weather data found for pincode", exception.getMessage());
        assertEquals("ERR-404", exception.getErrorData().getErrorCode());
        assertEquals("404 Not Found", exception.getErrorData().getErrorDesc());
    }

    

    @Test
    public void testUpdateSuccess() throws WeatherException{
        Weather weather = weatherRepository.findByPincode(505452L);
        weather.setPincode(505452L);
        weather.setAreaName("Updated Area");
        weather.setMaxTemp(35.0);
        weather.setMinTemp(25.0); 
        weather.setHumidity("Medium"); 
        weather.setRainfall("Low");
        weather.setWind("Moderate");
        weather.setDanger(false); 
        int updatedCount = weatherRepository.update(weather);
        assertEquals(1, updatedCount);
        Weather updatedWeather = weatherRepository.findByPincode(weather.getPincode());
        assertNotNull(updatedWeather);
        assertEquals(505452L, updatedWeather.getPincode());
        assertEquals("Updated Area", updatedWeather.getAreaName());  
        assertEquals(35.0, updatedWeather.getMaxTemp());
        assertEquals(25.0, updatedWeather.getMinTemp());
        assertEquals("Medium", updatedWeather.getHumidity());
        assertEquals("Low", updatedWeather.getRainfall());
        assertEquals("Moderate", updatedWeather.getWind());
        assertEquals(false, updatedWeather.isDanger());
    }
    @Test
    public void testUpdate_NotFound() {
        Weather weather = new Weather(99999L, "Invalid City", 30.0, 20.0, "High", "Medium", "Strong", true);
        WeatherException exception = assertThrows(WeatherException.class, () -> weatherRepository.update(weather));
        assertEquals("weather not found for pincode", exception.getMessage());
        assertEquals("ERR-404", exception.getErrorData().getErrorCode());
        assertEquals("404 Not Found", exception.getErrorData().getErrorDesc());
    }
    
    @Test
    public void testFindAll() {
        List<Weather> weatherList = weatherRepository.findAll();
        assertNotNull(weatherList);
        assertEquals(2, weatherList.size());
    }
}




