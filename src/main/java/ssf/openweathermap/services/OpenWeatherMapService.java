package ssf.openweathermap.services;

import static ssf.openweathermap.constants.OWMConstants.OWM_CITY_QUERY;
import static ssf.openweathermap.constants.OWMConstants.OWM_KEY;
import static ssf.openweathermap.constants.OWMConstants.OWM_KEY_QUERY;
import static ssf.openweathermap.constants.OWMConstants.OWM_UNITS;
import static ssf.openweathermap.constants.OWMConstants.OWM_UNITS_QUERY;
import static ssf.openweathermap.constants.OWMConstants.OWM_URL;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class OpenWeatherMapService {
    private final String API_KEY = System.getenv(OWM_KEY).trim();

    public JsonObject useExchange(String city) throws HttpClientErrorException, IOException {
        // build uri
        String url = UriComponentsBuilder
                .fromUriString(OWM_URL)
                .queryParam(OWM_CITY_QUERY, city)
                .queryParam(OWM_KEY_QUERY, API_KEY)
                .queryParam(OWM_UNITS_QUERY, OWM_UNITS)
                .toUriString();

        // build RequestEntity and ResponseEntity
        // send request to OpenWeatherMap API
        RequestEntity<Void> req = RequestEntity.get(url).build();
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> resp = restTemplate.exchange(req, String.class);

        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Error: status code %s"
                    .formatted(resp.getStatusCode().toString()));
        }
        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();
            return data;
        }
    }
}
