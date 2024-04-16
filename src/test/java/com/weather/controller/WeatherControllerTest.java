package com.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.model.Weather;
import com.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user", roles = {"USER_ROLE"})
    public void testCreateWeatherTable() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/weather/createTable"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Weather table created successfully"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER_ROLE"})
    public void testFindByPincode_Success() throws Exception {
        Weather weather = new Weather(505452L, "Karimnagar", 37.5, 20.5, "High", "Medium", "Strong", true);
        when(weatherService.findByPincode(505452L)).thenReturn(weather);

        mockMvc.perform(MockMvcRequestBuilders.get("/weather/findByPincode/505452"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pincode").value(505452))
                .andExpect(MockMvcResultMatchers.jsonPath("$.areaName").value("Karimnagar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.maxTemp").value(37.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minTemp").value(20.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.humidity").value("High"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rainfall").value("Medium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wind").value("Strong"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.danger").value(true));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER_ROLE"})
    public void testSaveWeather_Success() throws Exception {
        Weather weather = new Weather(505452L, "Karimnagar", 37.5, 20.5, "High", "Medium", "Strong", true);
        when(weatherService.save(weather)).thenReturn(1);
        mockMvc.perform(post("/weather/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weather)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser(username = "user", roles = {"USER_ROLE"})
    public void testDeleteByPincode_Success() throws Exception {
        when(weatherService.deleteByPincode(505452L)).thenReturn(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/weather/deleteByPincode/505452"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER_ROLE"})
    public void testUpdateWeather_Success() throws Exception {
        Weather weather = new Weather(505452L, "Karimnagar", 37.5, 20.5, "High", "Medium", "Strong", true);
        when(weatherService.update(weather)).thenReturn(1);
        mockMvc.perform(MockMvcRequestBuilders.put("/weather/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weather)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER_ROLE"})
    public void testFindAll_Success() throws Exception {
        Weather weather1 = new Weather(505452L, "Karimnagar", 37.5, 20.5, "High", "Medium", "Strong", true);
        Weather weather2 = new Weather(505422L, "Jagtial", 30.5, 23.5, "High", "Medium", "Strong", false);
        when(weatherService.findAll()).thenReturn(Arrays.asList(weather1, weather2));

        mockMvc.perform(MockMvcRequestBuilders.get("/weather/findAll"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pincode").value(505452))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].areaName").value("Karimnagar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].maxTemp").value(37.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].minTemp").value(20.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].humidity").value("High"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rainfall").value("Medium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].wind").value("Strong"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].danger").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].pincode").value(505422))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].areaName").value("Jagtial"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].maxTemp").value(30.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].minTemp").value(23.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].humidity").value("High"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].rainfall").value("Medium"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].wind").value("Strong"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].danger").value(false));
    }
}

