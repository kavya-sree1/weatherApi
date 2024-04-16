package com.weather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherApiApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @WithMockUser(username = "user", roles = {"USER_ROLE"})
    public void testApplicationStarts() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/weather/", String.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Hello! Welcome to our Weather Api", response.getBody());
    }
    @Test
    public void testContextInitialization(@Autowired ApplicationContext context) {
        assertNotNull(context);
    }
 
}
